// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
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
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import metadata.managment.i18n.Messages;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.HiveConfKeysForTalend;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.hadoop.version.EHadoopVersion4Drivers;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.connection.hive.HiveModeInfo;
import org.talend.core.model.metadata.connection.hive.HiveServerVersionInfo;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.ReflectionUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.metadata.managment.hive.EmbeddedHiveDataBaseMetadata;
import org.talend.metadata.managment.hive.HiveClassLoaderFactory;
import org.talend.metadata.managment.hive.handler.CDH4YarnHandler;
import org.talend.metadata.managment.hive.handler.CDH5YarnHandler;
import org.talend.metadata.managment.hive.handler.HDP130Handler;
import org.talend.metadata.managment.hive.handler.HDP200YarnHandler;
import org.talend.metadata.managment.hive.handler.HiveConnectionHandler;
import org.talend.metadata.managment.hive.handler.Mapr212Handler;

/**
 * Created by Marvin Wang on Mar 13, 2013.
 */
public class HiveConnectionManager extends DataBaseConnectionManager {

    private static HiveConnectionManager manager;

    /**
     * DOC marvin HiveDataBaseConnectionManager constructor comment.
     */
    private HiveConnectionManager() {
    }

    public static synchronized HiveConnectionManager getInstance() {
        if (manager == null) {
            manager = new HiveConnectionManager();
        }
        return manager;
    }

    /**
     * Creates a connection by the given argument, maybe it is an embedded hive connection or a standalone connection,
     * that depends on the parameter in {@link IMetadataConnection#getParameter(String)}. The key is
     * {@link ConnParameterKeys#CONN_PARA_KEY_HIVE_MODE}. Added by Marvin Wang on Mar 14, 2013.
     * 
     * @param metadataConn
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public Connection createConnection(IMetadataConnection metadataConn) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        Connection conn = null;
        String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
        if (HiveModeInfo.get(hiveModel) == HiveModeInfo.STANDALONE) {
            conn = createHiveStandloneConnection(metadataConn);
        } else {
            conn = createHiveEmbeddedConnection(metadataConn);
        }
        return conn;
    }

    /**
     * Returns the driver by the given argument. Added by Marvin Wang on Mar 14, 2013.
     * 
     * @param metadataConn
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Driver getDriver(IMetadataConnection metadataConn) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException {
        ClassLoader hiveClassLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConn);
        String connURL = metadataConn.getUrl();
        Driver driver = null;
        if (connURL != null) {
            if (connURL.startsWith(DatabaseConnConstants.HIVE_2_URL_FORMAT)) {
                Class<?> driverClass = Class.forName(EDatabase4DriverClassName.HIVE2.getDriverClass(), true, hiveClassLoader);
                driver = (Driver) driverClass.newInstance();
            } else if (connURL.startsWith(DatabaseConnConstants.HIVE_1_URL_FORMAT)) {
                Class<?> driverClass = Class.forName(EDatabase4DriverClassName.HIVE.getDriverClass(), true, hiveClassLoader);
                driver = (Driver) driverClass.newInstance();
            } else {
                // Create a default hive connection.
            }
        }
        return driver;
    }

    private Connection createHiveStandloneConnection(IMetadataConnection metadataConn) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, SQLException {
        Connection hiveStandaloneConn = null;
        String connURL = metadataConn.getUrl();
        if (connURL != null) {
            boolean useKerberos = Boolean.valueOf((String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
            boolean useMaprTicket = Boolean.valueOf((String) metadataConn
                    .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USE_MAPRTICKET));
            String hivePrincipal = (String) metadataConn.getParameter(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
            ClassLoader hiveClassLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConn);
            if (useMaprTicket) {
                setMaprTicketPropertiesConfig(metadataConn);
            }
            if (useKerberos) {
                System.setProperty(HiveConfKeysForTalend.HIVE_CONF_KEY_HIVE_METASTORE_KERBEROS_PRINCIPAL.getKey(), hivePrincipal);
                String principal = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
                String keytabPath = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
                boolean useKeytab = Boolean
                        .valueOf((String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB));
                if (useKeytab) {
                    try {
                        ReflectionUtils.invokeStaticMethod("org.apache.hadoop.security.UserGroupInformation", hiveClassLoader, //$NON-NLS-1$
                                "loginUserFromKeytab", new String[] { principal, keytabPath }); //$NON-NLS-1$
                    } catch (Exception e) {
                        throw new SQLException(e);
                    }
                }
            }
            if (useMaprTicket) {
                setMaprTicketConfig(metadataConn, hiveClassLoader, useKerberos);
            }
            if (connURL.startsWith(DatabaseConnConstants.HIVE_2_URL_FORMAT)) {
                hiveStandaloneConn = createHive2StandaloneConnection(metadataConn);
            } else if (connURL.startsWith(DatabaseConnConstants.HIVE_1_URL_FORMAT)) {
                hiveStandaloneConn = createHive1StandaloneConnection(metadataConn);
            } else {
                // Create a default hive connection.
            }
        }
        setHiveJDBCProperties(metadataConn, hiveStandaloneConn);
        return hiveStandaloneConn;
    }

    protected void setMaprTicketPropertiesConfig(IMetadataConnection metadataConn) {
        boolean setMapRHomeDir = Boolean.valueOf((String) metadataConn
                .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_SETMAPRHOMEDIR));
        String mapRHomeDir = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_MAPRHOMEDIR);
        System.setProperty("pname", "MapRLogin");//$NON-NLS-1$ //$NON-NLS-2$
        System.setProperty("https.protocols", "TLSv1.2");//$NON-NLS-1$ //$NON-NLS-2$
        System.setProperty("mapr.home.dir", setMapRHomeDir ? mapRHomeDir : "/opt/mapr");//$NON-NLS-1$ //$NON-NLS-2$
    }

    private void setMaprTicketConfig(IMetadataConnection metadataConn, ClassLoader classLoader, boolean useKerberos)
            throws SQLException {
        String mapRTicketUsername = (String) metadataConn
                .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
        String mapRTicketPassword = (String) metadataConn
                .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
        String mapRTicketCluster = (String) metadataConn
                .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
        String mapRTicketDuration = (String) metadataConn
                .getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
        boolean setMapRHadoopLogin = Boolean.valueOf((String) metadataConn
                .getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_SETMAPRHADOOPLOGIN));
        String mapRHadoopLogin = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_MAPRHADOOPLOGIN);
        Long desiredTicketDurInSecs = 86400L;
        if (mapRTicketDuration != null && StringUtils.isNotBlank(mapRTicketDuration)) {
            if (mapRTicketDuration.endsWith("L")) {//$NON-NLS-1$ 
                mapRTicketDuration = mapRTicketDuration.substring(0, mapRTicketDuration.length() - 1);
                desiredTicketDurInSecs = Long.valueOf(mapRTicketDuration) + 'L';
            } else if (StringUtils.isNumeric(mapRTicketDuration)) {
                desiredTicketDurInSecs = Long.valueOf(mapRTicketDuration) + 'L';
            }
        }
        try {
            Object mapRClientConfig = ReflectionUtils.newInstance(
                    "com.mapr.login.client.MapRLoginHttpsClient", classLoader, new Object[] {}); //$NON-NLS-1$
            if (useKerberos) {
                System.setProperty("hadoop.login", setMapRHadoopLogin ? mapRHadoopLogin : "kerberos");//$NON-NLS-1$ //$NON-NLS-2$
                ReflectionUtils.invokeMethod(mapRClientConfig,
                        "getMapRCredentialsViaKerberos", new Object[] { mapRTicketCluster, desiredTicketDurInSecs }); //$NON-NLS-1$
            } else {
                if (setMapRHadoopLogin) {
                    System.setProperty("hadoop.login", mapRHadoopLogin);//$NON-NLS-1$
                } else {
                    ReflectionUtils.invokeMethod(mapRClientConfig, "setCheckUGI", new Object[] { false }, boolean.class);//$NON-NLS-1$
                }
                String version = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
                Object[] argsObj = new Object[] { mapRTicketCluster, mapRTicketUsername, mapRTicketPassword,
                        desiredTicketDurInSecs };
                if (version != null && "MAPR520".compareTo(version) <= 0) {//$NON-NLS-1$
                    argsObj = new Object[] { mapRTicketCluster, mapRTicketUsername, mapRTicketPassword, desiredTicketDurInSecs,
                            "" };//$NON-NLS-1$
                }
                ReflectionUtils.invokeMethod(mapRClientConfig, "getMapRCredentialsViaPassword", argsObj); //$NON-NLS-1$
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Connection createHive2StandaloneConnection(final IMetadataConnection metadataConn) throws ClassNotFoundException,
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
                ClassLoader hiveClassLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConn);
                Thread.currentThread().setContextClassLoader(hiveClassLoader);
                try {
                    // 2. Fetch the HiveDriver from the new classloader
                    Class<?> driver = Class.forName(EDatabase4DriverClassName.HIVE2.getDriverClass(), true, hiveClassLoader);
                    Driver hiveDriver = (Driver) driver.newInstance();

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
            throw new SQLException(Messages.getString("HiveConnectionManager.getConnection.timeout"), e); //$NON-NLS-1$
        } catch (Throwable e1) {
            throw new SQLException(e1);
        }

        return conn;
    }

    private Connection createHive1StandaloneConnection(final IMetadataConnection metadataConn) throws ClassNotFoundException,
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
                ClassLoader hiveClassLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConn);
                Thread.currentThread().setContextClassLoader(hiveClassLoader);
                try {
                    // 2. Fetch the HiveDriver from the new classloader
                    Class<?> driver = Class.forName(EDatabase4DriverClassName.HIVE.getDriverClass(), true, hiveClassLoader);
                    Driver hiveDriver = (Driver) driver.newInstance();

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
            throw new SQLException(Messages.getString("HiveConnectionManager.getConnection.timeout"), e); //$NON-NLS-1$
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
            title = Messages.getString("HiveConnectionManager.getConnection.waitFinish", currentMethod); //$NON-NLS-1$
        } else {
            title = Messages.getString("HiveConnectionManager.getConnection.waitFinish.empty"); //$NON-NLS-1$
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

    private Connection createHiveEmbeddedConnection(IMetadataConnection metadataConn) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        String connURL = metadataConn.getUrl();
        String username = metadataConn.getUsername();
        String password = metadataConn.getPassword();

        // 1. Try to connect by driver
        Properties info = new Properties();

        username = username != null ? username : ""; //$NON-NLS-1$
        password = password != null ? password : "";//$NON-NLS-1$
        info.setProperty("user", username);//$NON-NLS-1$
        info.setProperty("password", password);//$NON-NLS-1$
        JavaSqlFactory.doHivePreSetup((DatabaseConnection) metadataConn.getCurrentConnection());

        // 2. Get class loader.
        ClassLoader currClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader hiveClassLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConn);
        Thread.currentThread().setContextClassLoader(hiveClassLoader);

        // 3. Fetch the HiveDriver from the new classloader
        Driver hiveDriver = null;
        Connection conn = null;
        try {
            if (connURL != null) {
                if (connURL.startsWith(DatabaseConnConstants.HIVE_2_URL_FORMAT)) {
                    Class<?> driver = Class.forName(EDatabase4DriverClassName.HIVE2.getDriverClass(), true, hiveClassLoader);
                    hiveDriver = (Driver) driver.newInstance();
                    conn = hiveDriver.connect(connURL, info);
                } else if (connURL.startsWith(DatabaseConnConstants.HIVE_1_URL_FORMAT)) {
                    Class<?> driver = Class.forName(EDatabase4DriverClassName.HIVE.getDriverClass(), true, hiveClassLoader);
                    hiveDriver = (Driver) driver.newInstance();
                    conn = hiveDriver.connect(connURL, info);
                } else {
                    // Create a default hive connection.
                }
            }
        } catch (SQLException e) {
            CommonExceptionHandler.process(e);
        } finally {
            Thread.currentThread().setContextClassLoader(currClassLoader);
        }
        setHiveJDBCProperties(metadataConn, conn);
        return conn;
    }

    /**
     * Checks if Hive can be connected.
     * 
     * <pre>
     * <li>For hive standalone model, it trys to {
     * {@link #createHiveStandloneConnection(IMetadataConnection)}, if there is no any exceptions thrown, then it
     * indicates Standalone Hive can be connected. 
     * <li>For hive embedded model, it just checks if tables can be fetched from a "fake" database metadata of hive, 
     * The fake database metadata is {@link EmbeddedHiveDataBaseMetadata#checkConnection()}.
     * </pre>
     * 
     * Added by Marvin Wang on Mar 18, 2013.
     * 
     * @param metadataConn
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public void checkConnection(IMetadataConnection metadataConn) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        setHadoopProperties(metadataConn);
        String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
        if (HiveModeInfo.get(hiveModel) == HiveModeInfo.STANDALONE) {
            createHiveStandloneConnection(metadataConn);
        } else {
            EmbeddedHiveDataBaseMetadata embeddedHiveDatabaseMetadata = new EmbeddedHiveDataBaseMetadata(metadataConn);
            embeddedHiveDatabaseMetadata.checkConnection();
        }
    }

    /**
     * <p>
     * Set hadoop properties to the hive connection.
     * </p>
     * DOC ycbai Comment method "setHadoopProperties".
     * 
     * @param metadataConn
     */
    private void setHadoopProperties(IMetadataConnection metadataConn) {
        if (metadataConn == null) {
            return;
        }
        Object connectionObj = metadataConn.getCurrentConnection();
        if (connectionObj instanceof DatabaseConnection) {
            DatabaseConnection currentConnection = (DatabaseConnection) connectionObj;
            String currentHadoopProperties = currentConnection.getParameters().get(
                    ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
            List<Map<String, Object>> hadoopProperties = HadoopRepositoryUtil.getHadoopPropertiesFullList(currentConnection,
                    currentHadoopProperties, false);
            for (Map<String, Object> propMap : hadoopProperties) {
                String key = TalendQuoteUtils.removeQuotesIfExist(String.valueOf(propMap.get("PROPERTY"))); //$NON-NLS-1$
                String value = TalendQuoteUtils.removeQuotesIfExist(String.valueOf(propMap.get("VALUE"))); //$NON-NLS-1$
                if (StringUtils.isNotEmpty(key) && value != null) {
                    System.setProperty(key, value);
                }
            }
        }
    }

    /**
     * <p>
     * Set JDBC properties to the hive db connection.
     * </p>
     * DOC ycbai Comment method "setHiveJDBCProperties".
     * 
     * @param metadataConn
     * @param dbConn
     */
    private void setHiveJDBCProperties(IMetadataConnection metadataConn, Connection dbConn) {
        if (metadataConn == null || dbConn == null) {
            return;
        }
        Object jdbcPropertiesObj = metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
        if (jdbcPropertiesObj == null) {
            return;
        }
        String jdbcPropertiesStr = String.valueOf(jdbcPropertiesObj);
        List<Map<String, Object>> jdbcProperties = HadoopRepositoryUtil.getHadoopPropertiesList(jdbcPropertiesStr);
        Statement statement = null;
        try {
            statement = dbConn.createStatement();
            for (Map<String, Object> propMap : jdbcProperties) {
                String key = TalendQuoteUtils.removeQuotesIfExist(String.valueOf(propMap.get("PROPERTY"))); //$NON-NLS-1$
                String value = TalendQuoteUtils.removeQuotesIfExist(String.valueOf(propMap.get("VALUE"))); //$NON-NLS-1$
                if (StringUtils.isNotEmpty(key) && value != null) {
                    statement.execute("SET " + key + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.process(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Extracts the database metadata from hive connection by the given argument {@link IMetadataConnection}, it can be
     * hive embedded or standalone .Added by Marvin Wang on Mar 14, 2013.
     * 
     * @param metadataConn
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public DatabaseMetaData extractDatabaseMetaData(IMetadataConnection metadataConn) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, SQLException {
        String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
        if (HiveModeInfo.get(hiveModel) == HiveModeInfo.STANDALONE) {
            return extractHiveStandaloneDatabaseMetaData(metadataConn);
        } else {
            return extractHiveEmbeddedDatabaseMetaData(metadataConn);
        }
    }

    private DatabaseMetaData extractHiveEmbeddedDatabaseMetaData(IMetadataConnection metadataConn) {
        return new EmbeddedHiveDataBaseMetadata(metadataConn);
    }

    private DatabaseMetaData extractHiveStandaloneDatabaseMetaData(IMetadataConnection metadataConn)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Connection conn = createHiveStandloneConnection(metadataConn);
        return conn.getMetaData();
    }

    public boolean isCDHHive2(IMetadataConnection metadataConn) {
        if (metadataConn != null) {
            String connURL = metadataConn.getUrl();
            if (connURL != null) {
                if (connURL.startsWith(DatabaseConnConstants.HIVE_2_URL_FORMAT)) {
                    String distroVersion = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
                    if ("Cloudera_CDH4".equals(distroVersion)) { //$NON-NLS-1$
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isHive2(IMetadataConnection metadataConn) {
        if (metadataConn != null) {
            String hiveServerKey = (String) metadataConn.getParameter(ConnParameterKeys.HIVE_SERVER_VERSION);
            return HiveServerVersionInfo.HIVE_SERVER_2.getKey().equals(hiveServerKey);
        }

        return false;
    }

    /**
     * 
     * create a related hive hanlder when run an analysis.embeded model need to execute some hadoop parametes for
     * embeded model. if it is standalone model,return HiveConnectionHandler.
     * 
     * @param metadataConnection
     * @return
     */
    public HiveConnectionHandler createHandler(IMetadataConnection metadataConnection) {
        HiveConnectionHandler handler = null;
        String version = (String) metadataConnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        String hiveModel = (String) metadataConnection.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
        if (HiveModeInfo.get(hiveModel) == HiveModeInfo.STANDALONE) {
            handler = new HiveConnectionHandler(metadataConnection);
        } else {
            if (EHadoopVersion4Drivers.HDP_1_3.getVersionValue().equals(version)) {
                handler = new HDP130Handler(metadataConnection);
            } else if (EHadoopVersion4Drivers.CLOUDERA_CDH4_YARN.getVersionValue().equals(version)) {
                handler = new CDH4YarnHandler(metadataConnection);
            } else if (EHadoopVersion4Drivers.HDP_2_0.getVersionValue().equals(version)) {
                handler = new HDP200YarnHandler(metadataConnection);
            } else if (EHadoopVersion4Drivers.MAPR212.getVersionValue().equals(version)
                    || EHadoopVersion4Drivers.MAPR301.getVersionValue().equals(version)) {
                handler = new Mapr212Handler(metadataConnection);
            } else if (EHadoopVersion4Drivers.CLOUDERA_CDH5.getVersionValue().equals(version)) {
                handler = new CDH5YarnHandler(metadataConnection);
            } else {
                handler = new HiveConnectionHandler(metadataConnection);
            }
        }
        return handler;
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
}
