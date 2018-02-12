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
package org.talend.metadata.managment.hive;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import metadata.managment.i18n.Messages;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.database.AbstractFakeDatabaseMetaData;
import org.talend.commons.utils.database.EmbeddedHiveResultSet;
import org.talend.commons.utils.system.EnvironmentUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.exception.WarningSQLException;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.runtime.hd.hive.HiveMetadataHelper;
import org.talend.core.utils.ReflectionUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.utils.sql.metadata.constants.GetTable;
import org.talend.utils.sql.metadata.constants.MetaDataConstants;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EmbeddedHiveDataBaseMetadata extends AbstractFakeDatabaseMetaData {

    private static final String TABLE_TYPE = "TABLE"; //$NON-NLS-1$

    private static final String HIVE_SCHEMA_DEFAULT = "default"; //$NON-NLS-1$

    private Object hiveObject;

    private IMetadataConnection metadataConn;

    private ClassLoader classLoader;

    private boolean isSupportJRE;

    public EmbeddedHiveDataBaseMetadata(IMetadataConnection metadataConn) {
        super();
        this.metadataConn = metadataConn;
        this.classLoader = HiveClassLoaderFactory.getInstance().getClassLoader(metadataConn);
        this.isSupportJRE = true;
        init();
    }

    private void init() {
        if (hiveObject == null) {
            ClassLoader currCL = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);
            try {
                Class calss = Class.forName("org.apache.hadoop.hive.ql.metadata.Hive", true, classLoader); //$NON-NLS-1$
                Method closeCurrentMethod = calss.getDeclaredMethod("closeCurrent"); //$NON-NLS-1$
                closeCurrentMethod.invoke(null);

                Method hiveGetMethod = calss.getDeclaredMethod("get"); //$NON-NLS-1$

                hiveObject = hiveGetMethod.invoke(null);

                boolean useKerberos = Boolean
                        .valueOf((String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_USE_KRB));
                boolean useKeytab = Boolean
                        .valueOf((String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB));
                if (useKerberos) {
                    Object conf = Class.forName("org.apache.hadoop.conf.Configuration", true, classLoader).newInstance(); //$NON-NLS-1$
                    ReflectionUtils.invokeMethod(conf, "set", new Object[] { "hadoop.security.authentication", "kerberos" }); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                    ReflectionUtils.invokeStaticMethod("org.apache.hadoop.security.UserGroupInformation", classLoader, //$NON-NLS-1$
                            "setConfiguration", new Object[] { conf }); //$NON-NLS-1$
                    if (useKeytab) {
                        String principal = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
                        String keytabPath = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
                        ReflectionUtils.invokeStaticMethod("org.apache.hadoop.security.UserGroupInformation", classLoader, //$NON-NLS-1$
                                "loginUserFromKeytab", new String[] { principal, keytabPath }); //$NON-NLS-1$
                    }
                }
            } catch (UnsupportedClassVersionError e) {
                // catch the UnsupportedClassVersionError to show user the current jre version is lower
                isSupportJRE = false;
            } catch (Exception e) {
                ExceptionHandler.process(e);
            } finally {
                Thread.currentThread().setContextClassLoader(currCL);
            }
        }
    }

    /**
     * For hive embedded mode if tables can be fetched from metastore db without any exceptions, then return
     * <code>true</code>. If there is any exceptions thrown, then that indicates it is failed to connection to hive.
     * Added by Marvin Wang on Mar 12, 2013.
     * 
     * @return
     * @throws SQLException
     */
    public boolean checkConnection() throws SQLException {
        if (!isSupportJRE) {
            throw new SQLException("This function is not available with a JDK < 1.7"); //$NON-NLS-1$ 
        }
        boolean isWindows = EnvironmentUtils.isWindowsSystem();
        String hiveDistribution = (String) this.metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        String hiveVersion = (String) this.metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);

        boolean isNotSupportEmbedded = HiveMetadataHelper.doSupportEmbeddedMode(hiveDistribution, hiveVersion, false);
        if (isWindows && isNotSupportEmbedded) {
            throw new WarningSQLException(Messages.getString("EmbeddedHiveDataBaseMetadata.functionNotSupportMessage")); //$NON-NLS-1$ 
        }
        getTables(this.metadataConn.getDatabase(), null, null, new String[] { "TABLE", "VIEW", "SYSTEM_TABLE" }); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getSchemas()
     */
    @Override
    public ResultSet getSchemas() throws SQLException {
        return new EmbeddedHiveResultSet();
    }

    @Override
    public String getDatabaseProductName() throws SQLException {
        return EDatabaseTypeName.HIVE.getDisplayName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.fakejdbc.FakeDatabaseMetaData#getCatalogs()
     */
    @Override
    public ResultSet getCatalogs() throws SQLException {
        EmbeddedHiveResultSet resultSet = new EmbeddedHiveResultSet();
        resultSet.setMetadata(new String[] { MetaDataConstants.TABLE_CAT.name() });
        List<String[]> list = new ArrayList<String[]>();
        String currentDB = this.metadataConn.getDatabase();
        if (StringUtils.isBlank(currentDB)) {
            try {
                List<String> dbs = (List<String>) ReflectionUtils.invokeMethod(hiveObject, "getAllDatabases", new Object[0]); //$NON-NLS-1$
                if (dbs != null) {
                    for (String db : dbs) {
                        list.add(new String[] { db });
                    }
                }
            } catch (Exception e) {
                throw new SQLException(e);
            }
        } else {
            list.add(new String[] { currentDB });
        }
        resultSet.setData(list);
        return resultSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getPrimaryKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        return new EmbeddedHiveResultSet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getTableTypes()
     */
    @Override
    public ResultSet getTableTypes() throws SQLException {

        List<String[]> list = new ArrayList<String[]>();
        list.add(new String[] { TABLE_TYPE });

        EmbeddedHiveResultSet tableResultSet = new EmbeddedHiveResultSet();
        tableResultSet.setMetadata(new String[] { GetTable.TABLE_TYPE.name() });
        tableResultSet.setData(list);

        return tableResultSet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getExportedKeys(java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        return new EmbeddedHiveResultSet();
    }

    @Override
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return false; // FIXME, need check
    }

    @Override
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return false; // FIXME, need check
    }

    @Override
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return false; // FIXME, need check
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getTables(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public ResultSet getTables(String catalog, String schema, String tableNamePattern, String[] types) throws SQLException {
        if (hiveObject == null) {
            throw new SQLException("Unable to instantiate org.apache.hadoop.hive.ql.metadata.Hive."); //$NON-NLS-1$
        }
        String hiveCat = catalog;
        if (StringUtils.isBlank(hiveCat)) {
            hiveCat = HIVE_SCHEMA_DEFAULT;
        }
        String[] hiveTypes = types;
        if (hiveTypes == null) {
            hiveTypes = new String[0];
        }

        // Added this for TDI-25456 by Marvin Wang on Apr. 11, 2013.
        ClassLoader currCL = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        EmbeddedHiveResultSet tableResultSet = new EmbeddedHiveResultSet();
        tableResultSet.setMetadata(TABLE_META);
        List<String[]> list = new ArrayList<String[]>();
        tableResultSet.setData(list);

        try {
            Class hiveClass = hiveObject.getClass();
            Method method = hiveClass.getDeclaredMethod("getConf");//$NON-NLS-1$ 
            Object hiveConf = method.invoke(hiveObject);
            Class hiveConfClass = hiveConf.getClass();
            // find ConfVar enum in the HiveConf class.
            Class confVarClass = null;
            for (Class curClass : hiveConfClass.getClasses()) {
                if (curClass.getSimpleName().equals("ConfVars")) { //$NON-NLS-1$
                    confVarClass = curClass;
                    break;
                }
            }
            if (confVarClass != null) {
                Object confVar = null;
                // try to find enumeration: ConfVars.METASTORE_CLIENT_SOCKET_TIMEOUT
                for (Object curConfVar : confVarClass.getEnumConstants()) {
                    if (curConfVar.toString().equals("hive.metastore.client.socket.timeout")) { //$NON-NLS-1$
                        confVar = curConfVar;
                        break;
                    }
                }
                if (confVar != null) {
                    Method setIntVarMethod = hiveConfClass.getDeclaredMethod("setIntVar", confVarClass, int.class); //$NON-NLS-1$
                    int timeout = 15;
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                        IDesignerCoreService designerService = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                                .getService(IDesignerCoreService.class);
                        timeout = designerService.getDBConnectionTimeout();
                    }
                    setIntVarMethod.invoke(hiveConf, confVar, timeout);
                }
            }
            String tempTableNamepattern = tableNamePattern;
            if (StringUtils.isEmpty(tempTableNamepattern) || TableInfoParameters.DEFAULT_FILTER.equals(tempTableNamepattern)) {
                tempTableNamepattern = "*"; //$NON-NLS-1$
            }
            Object tables = ReflectionUtils.invokeMethod(hiveObject,
                    "getTablesByPattern", new Object[] { hiveCat, tempTableNamepattern }); //$NON-NLS-1$
            if (tables instanceof List) {
                List<String> tableList = (List<String>) tables;
                for (String tableName : tableList) {
                    String tableType = getTableType(hiveCat, tableName);
                    if (tableType != null && ArrayUtils.contains(hiveTypes, tableType)) {
                        String[] array = new String[] { "", hiveCat, tableName, tableType, "" }; //$NON-NLS-1$//$NON-NLS-2$
                        list.add(array);
                    }
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(currCL);
        }

        return tableResultSet;
    }

    /**
     * DOC ycbai Comment method "getTableType".
     * 
     * @param dbName
     * @param tableName
     * @return the type of table.
     * @throws Exception
     */
    private String getTableType(String dbName, String tableName) throws Exception {
        if (dbName == null || tableName == null) {
            return null;
        }
        Object table = ReflectionUtils.invokeMethod(hiveObject, "getTable", new Object[] { dbName, tableName }); //$NON-NLS-1$
        if (table != null) {
            return String.valueOf(ReflectionUtils.invokeMethod(table, "getTableType", new Object[0])); //$NON-NLS-1$
        }

        return ETableTypes.TABLETYPE_TABLE.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.utils.database.FakeDatabaseMetaData#getColumns(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     */

    @Override
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
            throws SQLException {
        String hiveCat = catalog;
        if (StringUtils.isBlank(hiveCat)) {
            hiveCat = HIVE_SCHEMA_DEFAULT;
        }

        EmbeddedHiveResultSet tableResultSet = new EmbeddedHiveResultSet();
        tableResultSet.setMetadata(COLUMN_META);
        List<String[]> list = new ArrayList<String[]>();
        tableResultSet.setData(list);

        if (hiveObject != null) { // got the hive object
            ClassLoader currCL = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(classLoader);
            try {
                Object table = ReflectionUtils.invokeMethod(hiveObject, "getTable", new Object[] { hiveCat, tableNamePattern }); //$NON-NLS-1$
                if (table != null) {
                    Class tableClass = table.getClass();
                    Method getAllColsMethod = tableClass.getDeclaredMethod("getAllCols");//$NON-NLS-1$ 
                    Object cols = getAllColsMethod.invoke(table);
                    if (cols instanceof List) {
                        List colsList = (List) cols;
                        for (Object colObj : colsList) {
                            Class fieldSchemaClass = colObj.getClass();

                            // col name
                            Method getNameMethod = fieldSchemaClass.getDeclaredMethod("getName"); //$NON-NLS-1$
                            Object nameObj = getNameMethod.invoke(colObj);
                            final String colName = nameObj != null ? nameObj.toString() : ""; //$NON-NLS-1$

                            // col type
                            Method getTypeMethod = fieldSchemaClass.getDeclaredMethod("getType"); //$NON-NLS-1$
                            Object typeObj = getTypeMethod.invoke(colObj);
                            final String coltype = typeObj != null ? typeObj.toString() : ""; //$NON-NLS-1$

                            // col comment
                            Method getCommentMethod = fieldSchemaClass.getDeclaredMethod("getComment"); //$NON-NLS-1$
                            Object commentObj = getCommentMethod.invoke(colObj);
                            final String colComment = commentObj != null ? commentObj.toString() : ""; //$NON-NLS-1$

                            final int colSize = -1; // FIXME, need check.
                            final int colPrecision = 0; // FIXME, need check.
                            final int colRedix = 0; // FIXME, need check.
                            String[] array = new String[] { tableNamePattern, colName, coltype, String.valueOf(colSize),
                                    String.valueOf(colPrecision), String.valueOf(colRedix), "NO", colComment, "" }; //$NON-NLS-1$ //$NON-NLS-2$
                            list.add(array);
                        }
                    }
                }
            } catch (Exception e) {
                throw new SQLException(e);
            } finally {
                Thread.currentThread().setContextClassLoader(currCL);
            }
        }
        return tableResultSet;
    }
}
