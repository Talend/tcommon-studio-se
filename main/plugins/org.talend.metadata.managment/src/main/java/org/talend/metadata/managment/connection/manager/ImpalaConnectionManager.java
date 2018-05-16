// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.metadata.managment.connection.manager;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.classloader.ClassLoaderFactory;
import org.talend.core.classloader.DynamicClassLoader;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.IHadoopDistributionService;
import org.talend.core.hadoop.conf.EHadoopConfProperties;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.utils.ReflectionUtils;

import metadata.managment.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImpalaConnectionManager extends DataBaseConnectionManager {

    private final static ImpalaConnectionManager manager = new ImpalaConnectionManager();

    private ImpalaConnectionManager() {
    }

    public static ImpalaConnectionManager getInstance() {
        return manager;
    }

    public void checkConnection(IMetadataConnection metadataConn) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        createConnection(metadataConn);
    }

    public Connection createConnection(final IMetadataConnection metadataConn) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, SQLException {
        FutureTask<Connection> futureTask = new FutureTask<Connection>(new Callable<Connection>() {

            @Override
            public Connection call() throws Exception {
                Connection conn = null;
                String connURL = metadataConn.getUrl();
                String username = metadataConn.getUsername();
                String password = metadataConn.getPassword();

                // 1. Get class loader.
                ClassLoader currClassLoader = Thread.currentThread().getContextClassLoader();
                ClassLoader impalaClassLoader = getClassLoader(metadataConn);
                Thread.currentThread().setContextClassLoader(impalaClassLoader);
                try {
                    // 2. Fetch the HiveDriver from the new classloader
                    Class<?> driver = Class.forName(EDatabase4DriverClassName.IMPALA.getDriverClass(), true, impalaClassLoader);
                    Driver hiveDriver = (Driver) driver.newInstance();

                    Map<String, Object> otherParametersMap = metadataConn.getOtherParameters();
                    if (otherParametersMap != null) {
                        boolean useKerb = Boolean
                                .valueOf((String) otherParametersMap.get(ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
                        if (useKerb) {
                            Object conf = Class.forName("org.apache.hadoop.conf.Configuration", true, impalaClassLoader) //$NON-NLS-1$
                                    .newInstance();
                            EHadoopConfProperties.AUTHENTICATION.set(conf, "KERBEROS"); //$NON-NLS-1$
                            ReflectionUtils.invokeStaticMethod("org.apache.hadoop.security.UserGroupInformation", //$NON-NLS-1$
                                    impalaClassLoader, "setConfiguration", new Object[] { conf }); //$NON-NLS-1$
                        }
                    }

                    // 3. Try to connect by driver
                    Properties info = new Properties();
                    username = username != null ? username : ""; //$NON-NLS-1$
                    password = password != null ? password : "";//$NON-NLS-1$
                    info.setProperty("user", username);//$NON-NLS-1$
                    info.setProperty("password", password);//$NON-NLS-1$
                    conn = hiveDriver.connect(connURL, info);
                } finally {
                    Thread.currentThread().setContextClassLoader(currClassLoader);
                }
                return conn;
            }
        });

        ThreadGroup threadGroup = new ThreadGroup(this.getClass().getName() + ".createConnection"); //$NON-NLS-1$
        Thread newThread = new Thread(threadGroup, futureTask);
        newThread.start();

        Connection conn = null;
        try {
            conn = futureTask.get(getDBConnectionTimeout(), TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            threadGroup.interrupt();
            addBackgroundJob(futureTask, newThread);
            throw new SQLException(Messages.getString("ImpalaConnectionManager.getConnection.timeout"), e); //$NON-NLS-1$
        } catch (Throwable e1) {
            throw new SQLException(e1);
        }
        return conn;
    }

    private void addBackgroundJob(final FutureTask task, Thread thread) {
        StackTraceElement stElement = null;
        StackTraceElement stackTraceElements[] = thread.getStackTrace();
        if (stackTraceElements != null && 0 < stackTraceElements.length) {
            stElement = stackTraceElements[0];
        }
        String currentMethod;
        String title = ""; //$NON-NLS-1$
        if (stElement != null) {
            currentMethod = stElement.getClassName() + "." + stElement.getMethodName(); //$NON-NLS-1$
            title = Messages.getString("ImpalaConnectionManager.getConnection.waitFinish", currentMethod); //$NON-NLS-1$
        } else {
            title = Messages.getString("ImpalaConnectionManager.getConnection.waitFinish.empty"); //$NON-NLS-1$
        }
        Job backgroundJob = new Job(title) {

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    task.get();
                } catch (Throwable e) {
                    // nothing need to do
                }
                return Status.OK_STATUS;
            }
        };
        backgroundJob.setUser(false);
        backgroundJob.setPriority(Job.DECORATE);
        backgroundJob.schedule();
    }

    private int getDBConnectionTimeout() {
        int timeout = 15;
        try {
            timeout = CoreRuntimePlugin.getInstance().getDesignerCoreService().getDBConnectionTimeout();
        } catch (Exception e) {
            // can't get timeout in some cases, for example: can't get designerCoreService when running jobs
        }
        return timeout;
    }

    private IHadoopDistributionService getHadoopDistributionService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopDistributionService.class)) {
            return (IHadoopDistributionService) GlobalServiceRegister.getDefault().getService(IHadoopDistributionService.class);
        }
        return null;
    }

    private ClassLoader getClassLoader(IMetadataConnection metadataConn) {
        IHadoopDistributionService hadoopService = getHadoopDistributionService();
        if (hadoopService != null) {
            String distribution = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
            String version = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);

            IHDistribution impalaDistribution = hadoopService.getImpalaDistributionManager().getDistribution(distribution, false);
            if (impalaDistribution != null) {
                String impalaIndex = EDatabaseTypeName.IMPALA.getProduct() + ClassLoaderFactory.KEY_SEPARATOR
                        + impalaDistribution.getName();
                if (impalaDistribution.useCustom()) {
                    String jarsStr = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
                    String index = "CustomImpala" + ClassLoaderFactory.KEY_SEPARATOR + impalaIndex + ClassLoaderFactory.KEY_SEPARATOR + metadataConn.getId(); //$NON-NLS-1$
                    DynamicClassLoader classLoader = ClassLoaderFactory.getCustomClassLoader(index, jarsStr);
                    if (classLoader != null) {
                        return classLoader;
                    }
                } else {
                    IHDistributionVersion impalaVersion = impalaDistribution.getHDVersion(version, false);
                    if (impalaVersion != null) {
                        boolean isKeb = Boolean.valueOf((String) metadataConn
                                .getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
                        DynamicClassLoader classLoader = ClassLoaderFactory.getClassLoader(impalaIndex
                                + ClassLoaderFactory.KEY_SEPARATOR + impalaVersion.getVersion() + (isKeb ? "?USE_KRB" : ""));//$NON-NLS-1$//$NON-NLS-2$

                        // if not work for extension point, try modules from hadoop distribution
                        if (classLoader == null) {
                            classLoader = ClassLoaderFactory.getClassLoader(impalaVersion);
                        }
                        if (classLoader != null) {
                            return classLoader;
                        }
                    }
                }

            }

        }

        return this.getClass().getClassLoader();

    }
}
