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
package org.talend.core.model.metadata.builder.database.manager;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.DriverShim;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.TableNode;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.utils.ManagementTextUtils;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.metadata.managment.utils.TDColumnAttributeHelper;
import org.talend.repository.model.IRepositoryService;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sql.metadata.constants.GetColumn;
import org.talend.utils.sql.metadata.constants.GetTable;

import metadata.managment.i18n.Messages;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.NamedColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ExtractManager {

    public static final String TABLE_TYPE = "TABLE_TYPE"; //$NON-NLS-1$

    public static final String TABLE_SCHEMA = "TABLE_SCHEM"; //$NON-NLS-1$

    public static final String REMARKS = "REMARKS"; //$NON-NLS-1$

    public static final String SYNONYM_NAME = "SYNONYM_NAME"; //$NON-NLS-1$

    public static final String TABLE_NAME = "TABLE_NAME"; //$NON-NLS-1$

    public static final String TABLETYPE_VIEW = "V"; //$NON-NLS-1$

    public static final String TABLETYPE_TABLE = "T"; //$NON-NLS-1$

    protected static final String EMPTY = ""; //$NON-NLS-1$

    protected static final String DERBY_SHUTDOWN = "jdbc:derby:;shutdown=true"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(ExtractManager.class);

    private EDatabaseTypeName dbType;

    private Map<String, String> tableTypeMap = new Hashtable<String, String>();

    private Map<String, String> tableCommentsMap = new HashMap<String, String>();

    private int columnIndex;

    public ExtractManager(EDatabaseTypeName dbType) {
        super();
        this.dbType = dbType;
    }

    public EDatabaseTypeName getDbType() {
        return dbType;
    }

    public Map<String, String> getTableTypeMap() {
        return tableTypeMap;
    }

    public Map<String, String> getTableCommentsMap() {
        return tableCommentsMap;
    }

    protected ICoreService getCoreService() {
        ICoreService coreService = null;
        try {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
                coreService = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
            }
        } catch (Throwable e) {
            ExceptionHandler.process(e);
        }
        return coreService;

    }

    /**
     * 
     * DOC ggu Comment method "extractTablesFromDB".
     * 
     * work for @see ExtractMetaDataFromDataBase.extractTablesFromDB(...)
     * 
     * @param dbMetaData
     * @param metadataConnection
     * @param limit
     * @return
     */
    public List<IMetadataTable> extractTablesFromDB(DatabaseMetaData dbMetaData, IMetadataConnection metadataConnection,
            int... limit) {
        List<IMetadataTable> medataTables = new ArrayList<IMetadataTable>();
        if (dbMetaData == null || metadataConnection == null) {
            return medataTables;
        }
        String schema = getSchema(metadataConnection);
        List<String> tablesToFilter = getTablesToFilter(metadataConnection);
        if (tablesToFilter == null) {
            tablesToFilter = new ArrayList<String>();
        }
        try {
            Set<String> availableTableTypes = getAvailableTableTypes(dbMetaData);
            retrieveTables(dbMetaData, schema, medataTables, availableTableTypes, tablesToFilter, limit);
        } catch (SQLException e) {
            ExceptionHandler.process(e);
            log.error(e.toString());
            throw new RuntimeException(e);
        } catch (Exception e) {
            ExceptionHandler.process(e);
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        return medataTables;
    }

    /**
     * DOC ggu Comment method "getAvailableTableTypes".
     * 
     * 
     * make it's public,just for junit
     * 
     * @param dbMetaData
     * @return
     * @throws SQLException
     */
    public Set<String> getAvailableTableTypes(DatabaseMetaData dbMetaData) throws SQLException {
        ResultSet rsTableTypes = dbMetaData.getTableTypes();
        Set<String> availableTableTypes = new HashSet<String>();
        String[] neededTableTypes = { ETableTypes.TABLETYPE_TABLE.getName(), ETableTypes.TABLETYPE_VIEW.getName(),
                ETableTypes.TABLETYPE_SYNONYM.getName() };

        try {
            while (rsTableTypes.next()) {
                // StringUtils.trimToEmpty(name) is because bug 4547
                String currentTableType = StringUtils.trimToEmpty(rsTableTypes.getString(ExtractManager.TABLE_TYPE));
                // Because BASE TABLE which UniJDBCClientResultSet gets is not the support type of
                // UniJDBCDatabaseMetaData, do this so as to dispose bug 17281.
                if ("BASE TABLE".equalsIgnoreCase(currentTableType)) { //$NON-NLS-1$
                    currentTableType = ETableTypes.TABLETYPE_TABLE.getName();
                }
                if (ArrayUtils.contains(neededTableTypes, currentTableType)) {
                    availableTableTypes.add(currentTableType);
                }
            }
        } finally {
            rsTableTypes.close();// See bug 5029 Avoid "Invalid cursor exception"
        }
        return availableTableTypes;
    }

    public String getSchema(IMetadataConnection metadataConnection) {
        return metadataConnection != null ? metadataConnection.getSchema() : null;
    }

    protected List<String> getTablesToFilter(IMetadataConnection metadataConnection) {
        // for bug 11052
        ExtractMetaDataUtils.getInstance().setUseAllSynonyms(false);

        return new ArrayList<String>();
    }

    protected void retrieveTables(DatabaseMetaData dbMetaData, String schema, List<IMetadataTable> medataTables,
            Set<String> availableTableTypes, List<String> tablesToFilter, int... limit) throws SQLException {
        ResultSet rsTables = null;
        if (EMPTY.equals(schema)) {
            schema = null; // if empty, same as null, no schema.
        }
        rsTables = dbMetaData.getTables(null, schema, null, availableTableTypes.toArray(new String[] {}));
        if (rsTables != null) {
            try {
                getMetadataTables(medataTables, rsTables, dbMetaData.supportsSchemasInTableDefinitions(), tablesToFilter, limit);
            } finally {
                rsTables.close();
            }
        }
    }

    /**
     * DOC qzhang Comment method "getMetadataTables".
     * 
     * @param medataTables
     * @param rsTables
     * @throws SQLException
     */
    protected void getMetadataTables(List<IMetadataTable> medataTables, ResultSet rsTables, boolean supportSchema,
            List<String> tablesToFilter, int... limit) throws SQLException {
        if (rsTables == null) {
            return;
        }
        // hyWang add varribles limitNum and index for bug 7147
        int limitNum = -1;
        long index = 0;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        if (limit != null && limit.length > 0) {
            limitNum = limit[0];
        }

        while (rsTables.next()) {
            index++;
            // must be here, else will return limitNum+1 tables
            if (limitNum > 0 && index > limitNum) {
                break;
            }
            boolean isSynonym = false;
            MetadataTable medataTable = new MetadataTable();
            medataTable.setId(index + ""); //$NON-NLS-1$
            // See bug 5029 In some Linux odbc driver for MS SQL, their columns in ResultSet have not alias names.
            // Must use column index to fetch values.

            String tableName = extractMeta.getStringMetaDataInfo(rsTables, ExtractManager.TABLE_NAME, null);
            if (tableName == null) {
                tableName = extractMeta.getStringMetaDataInfo(rsTables, 3);
            }
            if (tableName == null) {
                // in case it's in fact a synonym
                tableName = extractMeta.getStringMetaDataInfo(rsTables, ExtractManager.SYNONYM_NAME, null);
                isSynonym = true;
            }
            if (tableName == null || tablesToFilter.contains(tableName)) {
                continue;
            }
            // if (!isOracle && tableName.startsWith("/")) {
            // continue;
            // }
            medataTable.setLabel(tableName);
            medataTable.setTableName(medataTable.getLabel());

            medataTable.setComment(extractMeta.getStringMetaDataInfo(rsTables, ExtractManager.REMARKS, null));

            String schema = extractMeta.getStringMetaDataInfo(rsTables, ExtractManager.TABLE_SCHEMA, null);
            if (schema == null) {
                schema = extractMeta.getStringMetaDataInfo(rsTables, 2);
            }

            String tableType = extractMeta.getStringMetaDataInfo(rsTables, ExtractManager.TABLE_TYPE, null);
            if (tableType == null) {
                tableType = extractMeta.getStringMetaDataInfo(rsTables, 4);
            }
            // if (tableType.startsWith("A")) {
            // System.out.println("AA");
            // }
            if (ExtractManager.TABLETYPE_TABLE.equals(tableType)) {
                tableType = ETableTypes.TABLETYPE_TABLE.getName();
            }
            if (ExtractManager.TABLETYPE_VIEW.equals(tableType)) {
                tableType = ETableTypes.TABLETYPE_VIEW.getName();
            }

            if (isSynonym) {
                tableType = "SYNONYM"; //$NON-NLS-1$
            }

            try {
                tableTypeMap.put(medataTable.getLabel(), tableType);
            } catch (Exception e) {
                tableTypeMap.put(medataTable.getLabel(), ETableTypes.TABLETYPE_TABLE.getName());
            }
            medataTables.add(medataTable);

        }
    }

    /**
     * 
     * DOC ggu Comment method "returnColumns".
     * 
     * work for @see ExtractMetaDataFromDataBase.returnColumns(...)
     * 
     * @param metadataConnection
     * @param tableNode
     * @param dontCreateClose
     * @return
     */
    public List<TdColumn> returnColumns(IMetadataConnection metadataConnection, TableNode tableNode, boolean... dontCreateClose) {
        if (metadataConnection == null || tableNode == null || tableNode.getType() != TableNode.TABLE) {
            return Collections.emptyList();
        }
        NamedColumnSet table = tableNode.getTable();
        if (table == null) {
            table = tableNode.getView();
        }
        if (table == null) {
            return Collections.emptyList();
        }
        //
        List<TdColumn> metadataColumns = new ArrayList<TdColumn>();
        boolean needCreateAndClose = dontCreateClose.length == 0 || !dontCreateClose[0];

        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        try {
            // WARNING Schema equals sid or database
            DatabaseMetaData dbMetaData = createDatabaseMetaData(metadataConnection, needCreateAndClose);

            TableNode newNode = tableNode;
            String name = newNode.getItemType();

            // StringUtils.trimToEmpty(name) is because bug 4547
            if (name != null && StringUtils.trimToEmpty(name).equals(ETableTypes.TABLETYPE_SYNONYM.getName())) {
                String tableName = getTableNameBySynonyms(extractMeta.getConn(), newNode.getValue());
                if (tableName != null && tableName.contains("/")) {
                    tableName = tableName.replace("/", "");
                }
                fillSynonyms(metadataConnection, metadataColumns, table, tableName, dbMetaData);
            } else {
                EDatabaseTypeName currentEDatabaseType = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
                metadataColumns = MetadataFillFactory.getDBInstance(currentEDatabaseType).fillColumns(table, metadataConnection,
                        dbMetaData, null);
            }

            // metadataColumns = ExtractMetaDataFromDataBase.extractColumns(dbMetaData, newNode, metadataConnection,
            // dbType);
            ColumnSetHelper.addColumns(table, metadataColumns);
        } catch (Exception e) {
            ExceptionHandler.process(e);
            log.error(e);
        } finally {
            // bug 22619
            if (needCreateAndClose && extractMeta.getConn() != null) {
                extractMeta.closeConnection();
            }
            MetadataConnectionUtils.closeDerbyDriver();
        }

        return metadataColumns;
    }

    protected DatabaseMetaData createDatabaseMetaData(IMetadataConnection metadataConnection, boolean needCreateAndClose)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        DatabaseMetaData dbMetaData = null;
        String dbType = metadataConnection.getDbType();
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();

        // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
            dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(metadataConnection);
        } else {
            if (needCreateAndClose || extractMeta.getConn() == null || extractMeta.getConn().isClosed()) {
                List list = extractMeta.getConnection(metadataConnection.getDbType(), metadataConnection.getUrl(),
                        metadataConnection.getUsername(), metadataConnection.getPassword(), metadataConnection.getDatabase(),
                        metadataConnection.getSchema(), metadataConnection.getDriverClass(),
                        metadataConnection.getDriverJarPath(), metadataConnection.getDbVersionString(),
                        metadataConnection.getAdditionalParams());
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof Driver) {
                            String driverClass = metadataConnection.getDriverClass();
                            if (MetadataConnectionUtils.isDerbyRelatedDb(driverClass, dbType)) {
                                MetadataConnectionUtils.setDerbyDriver((Driver) list.get(i));
                                break;
                            }
                        }
                    }
                }
            }
            dbMetaData = extractMeta.getDatabaseMetaData(extractMeta.getConn(), dbType, metadataConnection.isSqlMode(),
                    metadataConnection.getDatabase());
        }
        return dbMetaData;
    }

    /**
     * 
     * DOC ggu Comment method "getTableNameBySynonym".
     * 
     * @see ExtractMetaDataFromDataBase.getTableNameBySynonym(...), refactor for this.
     * @param conn
     * @param name
     * @return
     */
    public String getTableNameBySynonyms(Connection conn, String tableName) {
        return tableName; // by default
    }

    /**
     * 
     * DOC ggu Comment method "fillSynonms".
     * 
     * fill the columns for synonm,can get column name,column data type,length,precision,nullable
     * 
     * @param metadataConnection
     * @param metadataColumns
     * @param table
     * @param tableName
     * @param dbMetaData
     * @throws SQLException
     */
    protected void fillSynonyms(IMetadataConnection metadataConnection, List<TdColumn> metadataColumns, NamedColumnSet table,
            String tableName, DatabaseMetaData dbMetaData) throws SQLException {
        // nothing to do by default
    }

    /**
     * 
     * DOC ggu Comment method "closeConnect".
     * 
     * @param wapperDriver
     */
    public boolean closeConnection(IMetadataConnection metadataConnection, DriverShim wapperDriver) {
        return false;
    }

    public boolean closeConnectionForDerby(DriverShim wapperDriver) {
        if (wapperDriver != null) {
            try {
                wapperDriver.connect(DERBY_SHUTDOWN, null);
                return true;
            } catch (SQLException e) {
                // exception of shutdown success. no need to catch.
            }
        }
        return false;
    }

    /**
     * 
     * DOC ggu Comment method "returnMetadataColumnsFormTable".
     * 
     * @param metadataConnection
     * @param tableLabel
     * @param dontCreateClose
     * @return
     * @deprecated because still use it
     */
    @Deprecated
    public synchronized List<TdColumn> returnMetadataColumnsFormTable(IMetadataConnection metadataConnection, String tableLabel,
            boolean... dontCreateClose) {

        List<TdColumn> metadataColumns = new ArrayList<TdColumn>();

        boolean needCreateAndClose = dontCreateClose.length == 0 || !dontCreateClose[0];

        // bug 17980
        DriverShim wapperDriver = null;
        String dbType = "";
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();

        try {
            dbType = metadataConnection.getDbType();
            // WARNING Schema equals sid or database
            boolean isHive = EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType);
            if (!isHive && (needCreateAndClose || extractMeta.getConn() == null || extractMeta.getConn().isClosed())) {
                List list = extractMeta.getConnection(metadataConnection.getDbType(), metadataConnection.getUrl(),
                        metadataConnection.getUsername(), metadataConnection.getPassword(), metadataConnection.getDatabase(),
                        metadataConnection.getSchema(), metadataConnection.getDriverClass(),
                        metadataConnection.getDriverJarPath(), metadataConnection.getDbVersionString(),
                        metadataConnection.getAdditionalParams());
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i) instanceof DriverShim) {
                            wapperDriver = (DriverShim) list.get(i);
                        }
                    }
                }
            }

            DatabaseMetaData dbMetaData = null;
            // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
            if (isHive) {
                dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(metadataConnection);
            } else {
                dbMetaData = extractMeta.getDatabaseMetaData(extractMeta.getConn(), dbType, metadataConnection.isSqlMode(),
                        metadataConnection.getDatabase());
            }

            tableLabel = checkTableLabel(tableLabel);

            List<String> cataAndShema = getTableCatalogAndSchema((DatabaseConnection) metadataConnection.getCurrentConnection(),
                    tableLabel);
            metadataColumns = extractColumns(dbMetaData, metadataConnection, dbType, cataAndShema.get(0), cataAndShema.get(1),
                    tableLabel);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } finally {
            if (needCreateAndClose && extractMeta.getConn() != null) {
                ConnectionUtils.closeConnection(extractMeta.getConn());
            }
        }

        return metadataColumns;
    }

    protected String checkTableLabel(String tableLabel) {
        String name = ExtractMetaDataFromDataBase.getTableTypeByTableName(tableLabel);
        if (name != null && StringUtils.trimToEmpty(name).equals(ETableTypes.TABLETYPE_SYNONYM.getName())) {
            String tableName = getTableNameBySynonyms(ExtractMetaDataUtils.getInstance().getConn(), tableLabel);
            if (tableName.contains("/")) {
                tableName = tableName.replace("/", "");
            }
            tableLabel = tableName;
        }
        return tableLabel;
    }

    protected List<String> getTableCatalogAndSchema(DatabaseConnection dbConnection, String tableName) {
        String catalogName = null;
        String schemaName = null;
        List<String> catalogAndSchema = new ArrayList<String>();
        List<Catalog> catalogs = ConnectionHelper.getCatalogs(dbConnection);
        if (catalogs.isEmpty()) {
            List<Schema> schemas = ConnectionHelper.getSchema(dbConnection);
            if (schemas.isEmpty()) {
                schemaName = dbConnection.getUiSchema();
            } else {
                for (Schema s : schemas) {
                    EList<ModelElement> ownedElement = null;
                    if (s != null) {
                        ownedElement = s.getOwnedElement();
                        if (ownedElement != null) {
                            for (ModelElement m : ownedElement) {
                                if (m instanceof org.talend.core.model.metadata.builder.connection.MetadataTable) {
                                    org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable = (org.talend.core.model.metadata.builder.connection.MetadataTable) m;
                                    String label = metadataTable.getName();
                                    if (label.equals(tableName)) {
                                        schemaName = getSpecialSchema(dbConnection.getDatabaseType(), metadataTable);
                                        if (StringUtils.isEmpty(schemaName)) {
                                            schemaName = s.getName();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (Catalog c : catalogs) {
                EList<ModelElement> ownedElement = null;
                List<Schema> schemas = CatalogHelper.getSchemas(c);
                if (schemas.isEmpty()) {
                    if (c != null) {
                        ownedElement = c.getOwnedElement();
                        if (ownedElement != null) {
                            for (ModelElement m : ownedElement) {
                                if (m instanceof org.talend.core.model.metadata.builder.connection.MetadataTable) {
                                    String label = ((org.talend.core.model.metadata.builder.connection.MetadataTable) m)
                                            .getLabel();
                                    if (label.equals(tableName)) {
                                        catalogName = c.getName();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (Schema s : schemas) {
                        if (s != null) {
                            ownedElement = s.getOwnedElement();
                            if (ownedElement != null) {
                                for (ModelElement m : ownedElement) {
                                    if (m instanceof org.talend.core.model.metadata.builder.connection.MetadataTable) {
                                        String label = ((org.talend.core.model.metadata.builder.connection.MetadataTable) m)
                                                .getLabel();
                                        if (label.equals(tableName)) {
                                            catalogName = c.getName();
                                            schemaName = s.getName();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catalogAndSchema.add("".equals(catalogName) ? null : catalogName);
        catalogAndSchema.add((" ".equals(schemaName) || "".equals(schemaName)) ? null : schemaName); //$NON-NLS-1$
        return catalogAndSchema;
    }

    private String getSpecialSchema(String dbType,
            org.talend.core.model.metadata.builder.connection.MetadataTable metadataTable) {
        if (dbType != null && EDatabaseTypeName.SAPHana.getDisplayName().equals(dbType)) {
            if (metadataTable != null) {
                return TaggedValueHelper.getValueString(GetTable.TABLE_SCHEM.name(), metadataTable);
            }
        }
        return null;
    }

    protected List<TdColumn> extractColumns(DatabaseMetaData dbMetaData, IMetadataConnection metadataConnection,
            String databaseType, String catalogName, String schemaName, String tableName) {
        MappingTypeRetriever mappingTypeRetriever = null;
        columnIndex = 0;
        List<TdColumn> metadataColumns = new ArrayList<TdColumn>();
        List<String> columnLabels = new ArrayList<String>();
        Map<String, String> primaryKeys = new HashMap<String, String>();
        ResultSet columns = null;
        Statement stmt = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();

        try {
            boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(metadataConnection.getDbType());
            if (isAccess) {
                primaryKeys = retrievePrimaryKeys(dbMetaData, null, null, tableName);
            } else {
                primaryKeys = retrievePrimaryKeys(dbMetaData, catalogName, schemaName, tableName);
            }
            // Synchro view structure after alter commands.
            synchroViewStructure(catalogName, schemaName, tableName);

            if (EDatabaseTypeName.SAPHana.getDisplayName().equals(metadataConnection.getDbType())) {
                columns = dbMetaData.getColumns(catalogName, schemaName, tableName, null);
            } else {
                columns = getColumnsResultSet(dbMetaData, catalogName, schemaName, tableName);
            }
            if (MetadataConnectionUtils.isMysql(dbMetaData)) {
                boolean check = !Pattern.matches("^\\w+$", tableName);//$NON-NLS-1$
                if (check && !columns.next()) {
                    columns = getColumnsResultSet(dbMetaData, catalogName, schemaName,
                            TalendQuoteUtils.addQuotes(tableName, TalendQuoteUtils.ANTI_QUOTE));
                }
                columns.beforeFirst();
            }

            EDatabaseTypeName eDatabaseType = EDatabaseTypeName.getTypeFromDisplayName(databaseType);
            boolean isOracleProduct = false;
            if (eDatabaseType != null) {
                isOracleProduct = "ORACLE".equalsIgnoreCase(eDatabaseType.getProduct()); //$NON-NLS-1$
            }
            boolean isUseAllSynonyms = extractMeta.isUseAllSynonyms();

            IRepositoryService repositoryService = CoreRuntimePlugin.getInstance().getRepositoryService();
            while (columns.next()) {
                Boolean b = false;
                String fetchTableName = extractMeta.getStringMetaDataInfo(columns, ExtractManager.TABLE_NAME, null);
                fetchTableName = ManagementTextUtils.filterSpecialChar(fetchTableName); // for 8115
                if (fetchTableName.equals(tableName) || databaseType.equals(EDatabaseTypeName.SQLITE.getDisplayName())) {
                    TdColumn metadataColumn = RelationalFactory.eINSTANCE.createTdColumn();
                    String label = extractMeta.getStringMetaDataInfo(columns, "COLUMN_NAME", null); //$NON-NLS-1$
                    label = ManagementTextUtils.filterSpecialChar(label);
                    String sub = ""; //$NON-NLS-1$
                    String sub2 = ""; //$NON-NLS-1$
                    String label2 = label;
                    // label = label.replaceAll("[^_a-zA-Z0-9]", "_");
                    if (label != null && label.length() > 0 && label.startsWith("_")) { //$NON-NLS-1$
                        sub = label.substring(1);
                        if (sub != null && sub.length() > 0) {
                            sub2 = sub.substring(1);
                        }
                    }
                    final ICoreService coreService = getCoreService();
                    if (coreService != null
                            && (coreService.isKeyword(label) || coreService.isKeyword(sub) || coreService.isKeyword(sub2))) {
                        label = "_" + label; //$NON-NLS-1$
                        b = true;
                    }
                    // Validate the column if it contains space or illegal
                    // characters.
                    // if (repositoryService != null) {
                    // metadataColumn.setDisplayField(repositoryService.validateColumnName(metadataColumn.getLabel(),
                    // columnIndex));
                    label = MetadataToolHelper.validateColumnName(label, columnIndex, columnLabels);
                    metadataColumn.setLabel(label);
                    metadataColumn.setOriginalField(label2);
                    // }
                    columnIndex++;

                    if (primaryKeys != null && !primaryKeys.isEmpty()
                            && primaryKeys.get(metadataColumn.getOriginalField()) != null) {
                        metadataColumn.setKey(true);
                    } else {
                        metadataColumn.setKey(false);
                    }

                    String typeName = "TYPE_NAME"; //$NON-NLS-1$
                    if (isUseAllSynonyms) {
                        typeName = "DATA_TYPE"; //$NON-NLS-1$
                    }
                    String dbType = extractMeta.getStringMetaDataInfo(columns, typeName, null).toUpperCase();

                    if (EDatabaseTypeName.INFORMIX.getDisplayName().equals(metadataConnection.getDbType())) {
                        String tn = MetadataConnectionUtils.getColumnTypeName(dbMetaData.getConnection(), fetchTableName,
                                columnIndex);
                        if (tn != null) {
                            dbType = tn;
                        }
                    }

                    // For sometime the dbType will return one more space character at the end.So need to trim,comment
                    // for bug 17509
                    dbType = dbType.toUpperCase().trim();
                    dbType = ManagementTextUtils.filterSpecialChar(dbType);
                    dbType = handleDBtype(dbType);
                    metadataColumn.setSourceType(dbType);
                    Integer columnSize = new Integer(0);
                    Integer intMetaDataInfo = new Integer(0);

                    if (isOracleProduct && isUseAllSynonyms) {
                        setLengthAndPrecision(metadataColumn, columns, dbType);
                        columnSize = (int) metadataColumn.getLength();
                        intMetaDataInfo = (int) metadataColumn.getPrecision();
                    } else {
                        // if (isMYSQL) {
                        // columnSize = ExtractMetaDataUtils.getMysqlIntMetaDataInfo(resultMetadata, columnIndex);
                        // } else {
                        columnSize = extractMeta.getIntMetaDataInfo(columns, "COLUMN_SIZE");
                        // }
                        metadataColumn.setLength(columnSize);
                        intMetaDataInfo = extractMeta.getIntMetaDataInfo(columns, "DECIMAL_DIGITS");
                    }
                    // Convert dbmsType to TalendType

                    String talendType = null;

                    // qli modified to fix the bug 6654.
                    if (metadataConnection.getMapping() != null) {
                        mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(metadataConnection.getMapping());
                    }
                    talendType = mappingTypeRetriever.getDefaultSelectedTalendType(dbType, columnSize, intMetaDataInfo);
                    talendType = ManagementTextUtils.filterSpecialChar(talendType);
                    if (talendType == null) {
                        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                            talendType = JavaTypesManager.getDefaultJavaType().getId();
                            log.warn(Messages.getString("ExtractMetaDataFromDataBase.dbTypeNotFound", dbType)); //$NON-NLS-1$
                        }
                    } else {
                        // to remove when the new perl type will be added in talend.
                        // talendType = TypesManager.getTalendTypeFromXmlType(talendType);
                    }

                    metadataColumn.setTalendType(talendType);
                    // move for bug TDI-24016
                    String stringMetaDataInfo = ""; //$NON-NLS-1$
                    // for bug 13078

                    String commentInfo = ""; //$NON-NLS-1$
                    if (isOracleProduct && isUseAllSynonyms) {
                        metadataColumn.setNullable("Y".equals(columns.getString(GetColumn.NULLABLE.name()))); //$NON-NLS-1$
                        // keep same with OracleExtractManager
                        String defaultSelectedDbType = mappingTypeRetriever.getDefaultSelectedDbType(talendType);
                        metadataColumn.setSourceType(defaultSelectedDbType);
                    } else {
                        metadataColumn.setNullable(extractMeta.getBooleanMetaDataInfo(columns, "IS_NULLABLE")); //$NON-NLS-1$
                        stringMetaDataInfo = extractMeta.getStringMetaDataInfo(columns, "COLUMN_DEF", dbMetaData); //$NON-NLS-1$
                        // gcui:see bug 6450, if in the commentInfo have some invalid character then will remove it.
                        commentInfo = extractMeta.getStringMetaDataInfo(columns, ExtractManager.REMARKS, null);
                        if (commentInfo != null && commentInfo.length() > 0) {
                            commentInfo = ManagementTextUtils.filterSpecialChar(commentInfo);
                        }
                    }

                    // gcui:if not oracle database use "REMARKS" select comments
                    metadataColumn.setComment(commentInfo);
                    if (isOracleProduct && isUseAllSynonyms) {
                        metadataColumn.setName(label2);
                    } else {
                        // jdbc-odbc driver won't apply methods for access
                        addColumnAttributes(metadataConnection, columns, metadataColumn, label, label2, dbType, columnSize,
                                intMetaDataInfo, commentInfo);
                        checkPrecision(metadataColumn, tableName, dbType, intMetaDataInfo);
                    }

                    // cantoine : patch to fix 0x0 pb cause by Bad Schema
                    // description
                    if (stringMetaDataInfo != null && stringMetaDataInfo.length() > 0 && stringMetaDataInfo.charAt(0) == 0x0) {
                        stringMetaDataInfo = "\\0"; //$NON-NLS-1$
                    }
                    stringMetaDataInfo = ManagementTextUtils.filterSpecialChar(stringMetaDataInfo);
                    metadataColumn.setDefaultValue(stringMetaDataInfo);
                    extractMeta.handleDefaultValue(metadataColumn, dbMetaData);

                    // for bug 6919, oracle driver doesn't give correctly the length for timestamp
                    checkTypeForTimestamp(metadataConnection, metadataColumn, dbType);
                    metadataColumns.add(metadataColumn);
                    columnLabels.add(metadataColumn.getLabel());
                }
            }

            // gcui:if it is oracle database, use this SQL select comments.
            // there will do one query to retrieve all comments on the table.
            checkComments(metadataConnection, tableName, metadataColumns);

        } catch (Exception e) {
            ExceptionHandler.process(e);
            Status status = new Status(IStatus.ERROR, "org.talend.metadata.managment", 0,
                    "Error encountered when retrieving schema.", e);
            ErrorDialog errorDialog = new ErrorDialog(null, "Error", null, status, IStatus.ERROR);
            errorDialog.open();
            throw new RuntimeException(e);
        } finally {
            try {
                if (columns != null) {
                    columns.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                log.error(e.toString());
            }
        }

        return metadataColumns;
    }

    public void synchroViewStructure(String catalogName, String schemaName, String tableName) throws SQLException {
        // nothing to do by default
    }

    protected void setLengthAndPrecision(TdColumn column, ResultSet columns, String typeName) {
        /**
         * NOTE: The concepts of precision and scale in oracle are different with them in Talend Studio<br>
         * Please see: http://docs.oracle.com/cd/B28359_01/server.111/b28318/datatype.htm#i16209
         */
        int column_size = 0;
        long numPrecRadix = 0;
        try {
            if ("NUMBER".equalsIgnoreCase(typeName)) { //$NON-NLS-1$
                boolean isGetFailed = false;
                Object precision = columns.getObject("DATA_PRECISION");
                Object scale = columns.getObject("DATA_SCALE");
                if ((precision == null || precision.toString().isEmpty()) && (scale == null || scale.toString().isEmpty())) {
                    isGetFailed = true;
                }
                if (isGetFailed) {
                    // such as user dosen't set precision and scale for number
                    column_size = columns.getInt("DATA_LENGTH");
                    numPrecRadix = 0;
                } else {
                    column_size = columns.getInt("DATA_PRECISION");
                    numPrecRadix = columns.getLong("DATA_SCALE");
                }
            } else {
                // keep like before
                column_size = columns.getInt("DATA_LENGTH");
                numPrecRadix = columns.getLong("DATA_PRECISION");
            }

            column.setLength(column_size);
            column.setPrecision(numPrecRadix);
        } catch (Exception e1) {
            column.setLength(0);
            column.setPrecision(0);
            log.warn(e1, e1);
        }
    }

    private String handleDBtype(String type) {
        if (type.startsWith("TIMESTAMP(") && type.endsWith(")")) { //$NON-NLS-1$ //$NON-NLS-2$
            type = "TIMESTAMP"; //$NON-NLS-1$
        }
        final ICoreService coreService = getCoreService();
        if (coreService != null) {
            return coreService.validateValueForDBType(type);
        }
        return type;
    }

    protected Map<String, String> retrievePrimaryKeys(DatabaseMetaData dbMetaData, String catalogName, String schemaName,
            String tableName) throws SQLException {
        Map<String, String> primaryKeys = new HashMap<String, String>();

        try {
            ResultSet keys = null;
            try {
                keys = dbMetaData.getPrimaryKeys(catalogName, schemaName, tableName);
                primaryKeys.clear();
                while (keys.next()) {
                    primaryKeys.put(keys.getString("COLUMN_NAME"), "PRIMARY KEY"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } finally {
                if (keys != null) {
                    keys.close();
                }
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }

        checkUniqueKeyConstraint(tableName, primaryKeys, dbMetaData.getConnection());

        return primaryKeys;
    }

    protected void checkUniqueKeyConstraint(String lable, Map<String, String> primaryKeys, Connection connection) {
        // noting to do
        // PTODO ftang: should to support all kinds of databases not only for Mysql.
    }

    protected ResultSet getColumnsResultSet(DatabaseMetaData dbMetaData, String catalogName, String schemaName, String tableName)
            throws SQLException {
        ResultSet columns = null;
        if (dbMetaData != null) {
            if (tableName.contains("/")) {//$NON-NLS-1$
                tableName = tableName.replaceAll("/", "//");//$NON-NLS-1$ //$NON-NLS-2$
            }
            columns = dbMetaData.getColumns(catalogName, schemaName, tableName, null);
        }
        return columns;
    }

    protected void addColumnAttributes(IMetadataConnection metadataConnection, ResultSet columns, TdColumn metadataColumn,
            String label, String label2, String dbType, Integer columnSize, Integer intMetaDataInfo, String commentInfo)
            throws SQLException {
        TDColumnAttributeHelper.addColumnAttribute(label, label2, dbType, columnSize, intMetaDataInfo, commentInfo, columns,
                metadataColumn, (DatabaseConnection) metadataConnection.getCurrentConnection());// columnName,
    }

    protected void checkPrecision(TdColumn metadataColumn, String tableName, String dbType, Integer intMetaDataInfo) {
        metadataColumn.setPrecision(intMetaDataInfo);
    }

    protected void checkTypeForTimestamp(IMetadataConnection metadataConnection, TdColumn metadataColumn, String dbType) {
        // normal nothing to do
    }

    protected void checkComments(IMetadataConnection metadataConnection, String tableName, List<TdColumn> metadataColumns) {
        // normal nothing to do
    }

    /**
     * 
     * DOC ggu Comment method "returnTablesFormConnection".
     * 
     * work for @see ExtractMetaDataFromDataBase.returnTablesFormConnection(...)
     * 
     * @param metadataConnection
     * @param tableInfoParameters
     * @return
     */
    public List<String> returnTablesFormConnection(IMetadataConnection metadataConnection,
            TableInfoParameters tableInfoParameters) {
        getTableTypeMap().clear();
        List<String> itemTablesName = new ArrayList<String>();
        // add by wzhang
        // ExtractMetaDataUtils.metadataCon = iMetadataConnection;
        // end
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        List connList = extractMeta.getConnection(metadataConnection.getDbType(), metadataConnection.getUrl(),
                metadataConnection.getUsername(), metadataConnection.getPassword(), metadataConnection.getDatabase(),
                metadataConnection.getSchema(), metadataConnection.getDriverClass(), metadataConnection.getDriverJarPath(),
                metadataConnection.getDbVersionString(), metadataConnection.getAdditionalParams());
        try {
            if (!tableInfoParameters.isUsedName()) {
                if (tableInfoParameters.getSqlFiter() != null && !"".equals(tableInfoParameters.getSqlFiter())) { //$NON-NLS-1$
                    Statement stmt = extractMeta.getConn().createStatement();
                    extractMeta.setQueryStatementTimeout(stmt);
                    ResultSet rsTables = stmt.executeQuery(tableInfoParameters.getSqlFiter());
                    itemTablesName = ExtractMetaDataFromDataBase.getTableNamesFromQuery(rsTables, extractMeta.getConn());
                    rsTables.close();
                    stmt.close();
                }
            } else {
                itemTablesName = retrieveItemTables(metadataConnection, tableInfoParameters, itemTablesName);
            }
        } catch (SQLException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
        // filter tables or viewer from the recyclebin in the Oracle 10g.
        filterTablesFromRecycleBin(metadataConnection, itemTablesName);

        extractMeta.closeConnection();
        // for buy 15042
        DriverShim wapperDriver = null;
        if (connList != null && connList.size() > 0) {
            for (int i = 0; i < connList.size(); i++) {
                if (connList.get(i) instanceof DriverShim) {
                    wapperDriver = (DriverShim) connList.get(i);
                }
            }
        }

        // added for retrieve schema derby close
        closeConnection(metadataConnection, wapperDriver);

        return itemTablesName;
    }

    protected List<String> retrieveItemTables(IMetadataConnection metadataConnection, TableInfoParameters tableInfoParameters,
            List<String> itemTablesName)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Set<String> nameFiters = tableInfoParameters.getNameFilters();

        if (nameFiters.isEmpty()) {
            itemTablesName = getTableNamesFromTablesForMultiSchema(tableInfoParameters, "", metadataConnection); //$NON-NLS-1$
        } else {
            for (String s : nameFiters) {
                List<String> tableNamesFromTables = getTableNamesFromTablesForMultiSchema(tableInfoParameters, s,
                        metadataConnection);
                for (String string : tableNamesFromTables) {
                    if (!itemTablesName.contains(string)) {
                        itemTablesName.add(string);
                    }
                }
            }
        }
        return itemTablesName;
    }

    protected void filterTablesFromRecycleBin(IMetadataConnection metadataConnection, List<String> itemTablesName) {
        // nothing to do
    }

    /**
     * 
     * cli Comment method "getTableNamesFromTablesForMultiSchema".
     * 
     * bug 12179
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    protected List<String> getTableNamesFromTablesForMultiSchema(TableInfoParameters tableInfo, String namePattern,
            IMetadataConnection iMetadataConnection)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        String[] multiSchemas = extractMeta.getMultiSchems(extractMeta.getSchema());
        List<String> tableNames = new ArrayList<String>();
        if (multiSchemas != null) {
            for (String s : multiSchemas) {
                List<String> tableNamesFromTables = getTableNamesFromTables(
                        getResultSetFromTableInfo(tableInfo, namePattern, iMetadataConnection, s.trim()), iMetadataConnection);
                tableNames.addAll(tableNamesFromTables);
            }
        } else {
            List<String> tableNamesFromTables = getTableNamesFromTables(
                    getResultSetFromTableInfo(tableInfo, namePattern, iMetadataConnection, null), iMetadataConnection);
            tableNames.addAll(tableNamesFromTables);
        }
        return tableNames;
    }

    /**
     * DOC qzhang Comment method "getResultSetFromTableInfo".
     * 
     * @param dbMetaData
     * @return
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    protected ResultSet getResultSetFromTableInfo(TableInfoParameters tableInfo, String namePattern,
            IMetadataConnection iMetadataConnection, String schema)
            throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        ResultSet rsTables = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        String tableNamePattern = "".equals(namePattern) ? null : namePattern; //$NON-NLS-1$
        String[] types = new String[tableInfo.getTypes().size()];
        for (int i = 0; i < types.length; i++) {
            final String selectedTypeName = tableInfo.getTypes().get(i).getName();
            // bug 0017782 ,db2's SYNONYM need to convert to ALIAS;
            if ("SYNONYM".equals(selectedTypeName)
                    && iMetadataConnection.getDbType().equals(EDatabaseTypeName.IBMDB2.getDisplayName())) {
                types[i] = "ALIAS";
            } else {
                types[i] = selectedTypeName;
            }
        }

        DatabaseMetaData dbMetaData = null;
        // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
        if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(iMetadataConnection.getDbType())) {
            dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(iMetadataConnection);
        } else {
            dbMetaData = extractMeta.getDatabaseMetaData(extractMeta.getConn(), iMetadataConnection.getDbType(),
                    iMetadataConnection.isSqlMode(), iMetadataConnection.getDatabase());
        }
        // rsTables = dbMetaData.getTables(null, ExtractMetaDataUtils.schema, tableNamePattern, types);
        ResultSet rsTableTypes = null;
        rsTableTypes = dbMetaData.getTableTypes();
        Set<String> availableTableTypes = new HashSet<String>();
        String[] neededTableTypes = { ETableTypes.TABLETYPE_TABLE.getName(), ETableTypes.TABLETYPE_VIEW.getName(),
                ETableTypes.TABLETYPE_SYNONYM.getName() };
        while (rsTableTypes.next()) {
            String currentTableType = StringUtils.trimToEmpty(rsTableTypes.getString(ExtractManager.TABLE_TYPE));
            if (ArrayUtils.contains(neededTableTypes, currentTableType)) {
                availableTableTypes.add(currentTableType);
            }
        }
        rsTableTypes.close();
        rsTables = dbMetaData.getTables(null, schema, tableNamePattern, types);
        return rsTables;
    }

    /**
     * DOC qzhang Comment method "getTableNamesFromQuery".
     * 
     * @param rsTables
     * 
     * @return
     * @throws SQLException
     */
    protected List<String> getTableNamesFromTables(ResultSet resultSet, IMetadataConnection metadataConnection)
            throws SQLException {
        List<String> itemTablesName = new ArrayList<String>();
        tableCommentsMap.clear();
        if (resultSet != null) {

            while (resultSet.next()) {
                String nameKey = resultSet.getString(GetTable.TABLE_NAME.name());
                String colComment = getTableComment(metadataConnection, resultSet, nameKey);

                itemTablesName.add(nameKey);
                if (tableCommentsMap.containsKey(nameKey)) {
                    if (colComment == null) {
                        colComment = "";
                    }
                    tableCommentsMap.remove(nameKey);
                    tableCommentsMap.put(nameKey, colComment);
                }
                tableCommentsMap.put(nameKey, colComment);
                // bug 0017782 ,db2's SYNONYM need to convert to ALIAS;
                if (tableTypeMap.containsKey(nameKey)) {
                    tableTypeMap.remove(nameKey);
                    tableTypeMap.put(nameKey, resultSet.getString(ExtractManager.TABLE_TYPE));
                } else {
                    tableTypeMap.put(nameKey, resultSet.getString(ExtractManager.TABLE_TYPE));
                }
            }
            resultSet.close();
        }
        return itemTablesName;
    }

    public String getTableComment(IMetadataConnection metadataConnection, ResultSet resultSet, String nameKey)
            throws SQLException {
        return ExtractMetaDataFromDataBase.getTableComment(nameKey, resultSet, true,
                ExtractMetaDataUtils.getInstance().getConn());
    }

}
