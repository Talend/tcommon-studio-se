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
package org.talend.metadata.managment.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.data.list.ListUtils;
import org.talend.commons.utils.database.AS400DatabaseMetaData;
import org.talend.commons.utils.database.DB2ForZosDataBaseMetadata;
import org.talend.commons.utils.database.SybaseDatabaseMetaData;
import org.talend.commons.utils.database.TeradataDataBaseMetadata;
import org.talend.core.ICoreService;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MappingTypeRetriever;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.core.model.metadata.builder.database.JavaSqlFactory;
import org.talend.core.model.metadata.builder.database.PluginConstant;
import org.talend.core.model.metadata.builder.database.TableInfoParameters;
import org.talend.core.model.metadata.builder.database.manager.ExtractManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.TalendCWMService;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.RelationalFactory;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdExpression;
import org.talend.cwm.relational.TdSqlDataType;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.hive.EmbeddedHiveDataBaseMetadata;
import org.talend.metadata.managment.repository.ManagerConnection;
import org.talend.metadata.managment.utils.DatabaseConstant;
import org.talend.metadata.managment.utils.ManagementTextUtils;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sql.Java2SqlType;
import org.talend.utils.sql.metadata.constants.GetColumn;
import org.talend.utils.sql.metadata.constants.GetForeignKey;
import org.talend.utils.sql.metadata.constants.GetPrimaryKey;
import org.talend.utils.sql.metadata.constants.GetTable;
import org.talend.utils.sql.metadata.constants.MetaDataConstants;
import org.talend.utils.sql.metadata.constants.TableType;

import metadata.managment.i18n.Messages;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.ForeignKey;
import orgomg.cwm.resource.relational.PrimaryKey;
import orgomg.cwm.resource.relational.Schema;
import orgomg.cwm.resource.relational.enumerations.NullableType;

/**
 * @author zshen
 */
public class DBConnectionFillerImpl extends MetadataFillerImpl<DatabaseConnection> {

    private static Logger log = Logger.getLogger(DBConnectionFillerImpl.class);

    private static Driver driver = null;

    @Override
    public DatabaseConnection fillUIConnParams(IMetadataConnection metadataBean, DatabaseConnection connection) {
        DatabaseConnection newConnection = null;
        if (connection == null) {
            newConnection = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        }
        if (super.fillUIConnParams(metadataBean, newConnection == null ? connection : newConnection) == null) {
            return null;
        }
        DatabaseConnection dbconn = null;
        if (newConnection != null) {
            dbconn = newConnection;
        } else {
            dbconn = connection;
        }
        if (newConnection != null && dbconn != null) {
            dbconn.setDriverJarPath(metadataBean.getDriverJarPath());
            dbconn.setProductId(metadataBean.getProduct());
            dbconn.setDbmsId(metadataBean.getMapping());
            dbconn.setAdditionalParams(metadataBean.getAdditionalParams());
            dbconn.setDriverClass(metadataBean.getDriverClass());
            dbconn.setDatabaseType(metadataBean.getDbType());
            dbconn.setName(metadataBean.getLabel());
            dbconn.setLabel(metadataBean.getLabel());
            dbconn.setVersion(metadataBean.getVersion());
            // MOD klliu bug 21074 2011-05-19
            dbconn.setUiSchema(metadataBean.getUiSchema());
            dbconn.setSQLMode(metadataBean.isSqlMode());
            dbconn.setSID(metadataBean.getDatabase());
            // MOD copy parameter from metadata to dbconn
            for (Map.Entry<String, Object> parameter : metadataBean.getOtherParameters().entrySet()) {
                dbconn.getParameters().put(parameter.getKey(),
                        parameter.getValue() == null ? StringUtils.EMPTY : parameter.getValue().toString());
            }
        }
        java.sql.Connection sqlConnection = null;
        try {
            // MetadataConnectionUtils.setMetadataCon(metadataBean);
            // fill some base parameter
            if (newConnection != null) {
                fillMetadataParams(metadataBean, newConnection);
            }

            DatabaseMetaData dbMetadata = null;
            // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
            if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(metadataBean.getDbType())) {
                dbMetadata = HiveConnectionManager.getInstance().extractDatabaseMetaData(metadataBean);
            } else {
                sqlConnection = MetadataConnectionUtils.createConnection(metadataBean).getObject();
                // software
                dbMetadata = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConnection, dbconn, false);
            }

            if (dbMetadata != null) {
                // MOD sizhaoliu TDQ-6316 The 2 tagged values should be added for all database including Hive
                String productName = dbMetadata.getDatabaseProductName() == null ? PluginConstant.EMPTY_STRING
                        : dbMetadata.getDatabaseProductName();
                String productVersion = dbMetadata.getDatabaseProductVersion() == null ? PluginConstant.EMPTY_STRING
                        : dbMetadata.getDatabaseProductVersion();
                // TDI-35419 TDQ-11853: make sure the redshift connection save the productName is redshift, not use the
                // postgresql.becauses we use this value to create dbmslauguage
                boolean isRedshift = dbconn.getDatabaseType().equals(EDatabaseTypeName.REDSHIFT.getDisplayName());
                if (isRedshift) {
                    productName = EDatabaseTypeName.REDSHIFT.getDisplayName();
                }
                // ~
                TaggedValueHelper.setTaggedValue(dbconn, TaggedValueHelper.DB_PRODUCT_NAME, productName);
                TaggedValueHelper.setTaggedValue(dbconn, TaggedValueHelper.DB_PRODUCT_VERSION, productVersion);

                boolean isHive = dbconn.getDatabaseType().equals(EDatabaseTypeName.HIVE.getDisplayName());
                boolean isHiveJdbc = dbconn.getDatabaseType().equals(EDatabaseTypeName.GENERAL_JDBC.getDisplayName())
                        && dbconn.getDriverClass() != null
                        && dbconn.getDriverClass().equals(EDatabase4DriverClassName.HIVE.getDriverClass());
                boolean isImpala = dbconn.getDatabaseType().equals(EDatabaseTypeName.IMPALA.getDisplayName());
                boolean isImpalaJdbc = dbconn.getDatabaseType().equals(EDatabaseTypeName.IMPALA.getDisplayName())
                        && dbconn.getDriverClass() != null
                        && dbconn.getDriverClass().equals(EDatabase4DriverClassName.IMPALA.getDriverClass());
                if (!isHive && !isHiveJdbc && !isImpala && !isImpalaJdbc) {
                    String identifierQuote = dbMetadata.getIdentifierQuoteString();
                    ConnectionHelper.setIdentifierQuoteString(identifierQuote == null ? "" : identifierQuote, dbconn); //$NON-NLS-1$
                }
            }
        } catch (SQLException e) {
            log.error(e, e);
        } catch (ClassNotFoundException e) {
            CommonExceptionHandler.process(e);
        } catch (InstantiationException e) {
            CommonExceptionHandler.process(e);
        } catch (IllegalAccessException e) {
            CommonExceptionHandler.process(e);
        } finally {
            if (sqlConnection != null) {
                ConnectionUtils.closeConnection(sqlConnection);
            }
            if (driver != null
                    && MetadataConnectionUtils.isDerbyRelatedDb(metadataBean.getDriverClass(), metadataBean.getDbType())) {
                try {
                    driver.connect("jdbc:derby:;shutdown=true", null); //$NON-NLS-1$
                } catch (SQLException e) {
                    // exception of shutdown success. no need to catch.
                }
            }
        }
        if (newConnection != null) {
            return newConnection;
        } else {
            return connection;
        }
    }

    /**
     * From r94372 , we should never give a null parameter "IMetadataConnection metaConnection" , because we used this
     * parameter for some kind of databases...
     */
    @Override
    @Deprecated
    public List<Package> fillSchemas(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata, List<String> Filter) {
        return fillSchemas(dbConn, dbJDBCMetadata, null, Filter);
    }

    @Override
    public List<Package> fillSchemas(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata,
            IMetadataConnection metaConnection, List<String> schemaFilter) {
        List<Schema> returnSchemas = new ArrayList<Schema>();
        if (dbJDBCMetadata == null || (dbConn != null && ConnectionHelper.getCatalogs(dbConn).size() > 0)
                || ConnectionUtils.isSybase(dbJDBCMetadata)) {
            return null;
        }
        ResultSet schemas = null;
        // teradata use db name to filter schema
        // MOD jlolling TDI-34429 EXASol database behaves pretty much in the same way as Oracle
        if (dbConn != null && EDatabaseTypeName.TERADATA.getProduct().equals(dbConn.getProductId())) {
            if (!dbConn.isContextMode()) {
                String sid = getDatabaseName(dbConn);
                if (sid != null && sid.length() > 0) {
                    schemaFilter.add(sid);
                }
            } else {
                IMetadataConnection iMetadataCon = metaConnection;
                if (iMetadataCon == null) {
                    iMetadataCon = ConvertionHelper.convert(dbConn);
                }
                String sid = iMetadataCon.getDatabase();
                if (sid != null && sid.length() > 0) {
                    schemaFilter.add(sid);
                }
            }
        }
        // TDI-17172 : if the schema is not fill, as the db context model, should clear "schemaFilter" .
        if (dbConn != null && dbConn.isContextMode()) {
            if (EDatabaseTypeName.ORACLEFORSID.getProduct().equals(dbConn.getProductId())
                    || EDatabaseTypeName.IBMDB2.getProduct().equals(dbConn.getProductId())) {
                IMetadataConnection iMetadataCon = metaConnection;
                if (iMetadataCon == null) {
                    iMetadataCon = ConvertionHelper.convert(dbConn);
                }
                if (iMetadataCon != null) {
                    String schemaTemp = iMetadataCon.getSchema();
                    if ("".equals(schemaTemp)) { //$NON-NLS-1$
                        schemaFilter.clear();
                    }
                }
            }
        }

        try {
            if (dbConn != null && EDatabaseTypeName.ACCESS.getProduct().equals(dbConn.getProductId())) {
                return null;
            }
            schemas = dbJDBCMetadata.getSchemas();
        } catch (SQLException e) {
            // TDI-34429, jlolling we do not get an exception if the database simply do not have schemas!
            // in this case the resultset is empty
            log.error("Retrieve schema from database failed: " + e.getMessage(), e); //$NON-NLS-1$
        }
        boolean hasSchema = false;
        try {
            boolean isHive2 = HiveConnectionManager.getInstance().isHive2(metaConnection);
            if (schemas != null && !ConnectionUtils.isOdbcHyperFileSQL(dbJDBCMetadata)) {
                String schemaName = null;
                while (schemas.next()) {
                    if (!ConnectionUtils.isOdbcTeradata(dbJDBCMetadata) && !isHive2) {
                        schemaName = schemas.getString(MetaDataConstants.TABLE_SCHEM.name());
                        if (schemaName == null) {
                            schemaName = schemas.getString(DatabaseConstant.ODBC_ORACLE_SCHEMA_NAME);
                        }
                        if (schemaName == null) {
                            // try to get first column
                            schemaName = schemas.getString(1);
                        }
                    } else {
                        schemaName = schemas.getString(1);
                    }
                    hasSchema = true;
                    String uiSchemaOnConnWizard = null;
                    if (dbConn != null) {
                        uiSchemaOnConnWizard = dbConn.getUiSchema();
                        // for hive2 db name is treat as schema
                        if (isHive2) {
                            uiSchemaOnConnWizard = getDatabaseName(dbConn);
                        }
                    }

                    EDatabaseTypeName dbTypeName = EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType());
                    if ((!StringUtils.isEmpty(uiSchemaOnConnWizard) && !isNullUiSchema(dbConn)) && dbConn != null) {
                        // If the UiSchema on ui is not empty, the schema name should be same to this UiSchema name.
                        Schema schema = SchemaHelper.createSchema(TalendCWMService.getReadableName(dbConn, uiSchemaOnConnWizard));
                        returnSchemas.add(schema);
                        break;
                    } else if (isCreateElement(schemaFilter, schemaName, ManagerConnection.isSchemaCaseSensitive(dbTypeName))) {
                        Schema schema = SchemaHelper.createSchema(schemaName);
                        returnSchemas.add(schema);
                    }
                }
                schemas.close();
            }
        } catch (SQLException e) {
            log.error("Retrieving schemas from resultset failed: " + e.getMessage(), e);
        }
        // handle case of SQLite (no schema no catalog)
        // MOD gdbu 2011-4-12 bug : 18975
        // ResultSet catalogs = dbJDBCMetadata.getCatalogs();
        // ~18975
        if (!hasSchema) {
            // TDI-30715: Only here handle the lightweight db which no catalogs and no schemas(such as Sqlite)
            fillSqliteFakeSchemas(returnSchemas);
        }
        // MOD gdbu 2011-4-12 bug : 18975
        // catalogs.close();
        // ~18975
        Set<MetadataTable> tableSet = new HashSet<MetadataTable>();
        if (dbConn != null) {
            tableSet.addAll(ConnectionHelper.getTables(dbConn));
        }
        // oldSchemas is use for record tables when click finish,then tables will be replace and null.
        List<Schema> oldSchemas = new ArrayList<Schema>();
        for (MetadataTable table : tableSet) {
            EObject eContainer = table.eContainer();
            if (eContainer != null && eContainer instanceof Schema && !oldSchemas.contains(eContainer)) {
                oldSchemas.add((Schema) eContainer);
            }
        }

        if (isLinked() && !returnSchemas.isEmpty()) {
            ConnectionHelper.addSchemas(returnSchemas, dbConn);
        }
        // if have same schema in current connection,need to fill tables.
        for (Schema schema : oldSchemas) {
            List<Schema> list = new ArrayList<Schema>();
            String name = schema.getName();
            Schema s = (Schema) ConnectionHelper.getPackage(name, dbConn, Schema.class);
            if (s != null) {
                list.add(s);
                ConnectionHelper.removeSchemas(list, dbConn);
                ConnectionHelper.addSchema(schema, dbConn);
            } else {
                ConnectionHelper.addSchema(schema, dbConn);
            }
        }
        return ListUtils.castList(Package.class, returnSchemas);
    }

    /**
     * fill the fake schemas into sqlite database connection since Sqlite no catalogs and no schemas.
     * 
     * @param fakeSchemas
     * @return
     */
    private void fillSqliteFakeSchemas(List<Schema> fakeSchemas) {
        fakeSchemas.add(SchemaHelper.createSchema(" "));
    }

    @Override
    public List<Catalog> fillCatalogs(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata, List<String> catalogFilter) {
        return fillCatalogs(dbConn, dbJDBCMetadata, null, catalogFilter);
    }

    @Override
    public List<Catalog> fillCatalogs(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata,
            IMetadataConnection metaConnection, List<String> catalogFilter) {
        List<Catalog> catalogList = new ArrayList<Catalog>();
        if (dbJDBCMetadata == null) {
            return null;
        }

        if (ConnectionUtils.isPostgresql(dbJDBCMetadata)) {
            return fillPostgresqlCatalogs(metaConnection, dbConn, dbJDBCMetadata, catalogList);
        }

        // TDI-17172 : if the catalog is not fill, as the db context model, should clear "catalogFilter" .
        if (dbConn != null && dbConn.isContextMode()) {
            if (EDatabaseTypeName.MYSQL.getProduct().equals(dbConn.getProductId())
                    || EDatabaseTypeName.MSSQL.getProduct().equals(dbConn.getProductId())
                    || EDatabaseTypeName.MSSQL05_08.getProduct().equals(dbConn.getProductId())) {
                IMetadataConnection iMetadataCon = metaConnection;
                if (iMetadataCon == null) {
                    iMetadataCon = ConvertionHelper.convert(dbConn);
                }
                if (iMetadataCon != null) {
                    String catalogTemp = iMetadataCon.getDatabase();
                    if ("".equals(catalogTemp)) { //$NON-NLS-1$
                        catalogFilter.clear();
                    }
                }
            }
        }

        try {
            if (!isDbSupportCatalogNames(dbJDBCMetadata)) {
                return catalogList;
            }
            if (!isDbHasCatalogs(dbJDBCMetadata)) {
                ConnectionHelper.removeAllPackages(dbConn);
                return catalogList;
            }
            ResultSet catalogNames = null;
            if (dbJDBCMetadata instanceof SybaseDatabaseMetaData) {
                catalogNames = ((SybaseDatabaseMetaData) dbJDBCMetadata).getCatalogs(dbConn.getUsername());
            } else {
                catalogNames = dbJDBCMetadata.getCatalogs();
            }
            // this filter is for filter schema later
            List<String> schemaFilterList = new ArrayList<String>();
            if (catalogNames != null) {
                boolean isHive = MetadataConnectionUtils.isHive(dbJDBCMetadata);
                boolean isSybase = MetadataConnectionUtils.isSybase(dbJDBCMetadata);
                // else DB support getCatalogs() method
                while (catalogNames.next()) {
                    // MOD xqliu 2009-11-03 bug 9841
                    String catalogName = null;
                    try {
                        String temp = null;
                        // since hive don't support some methods
                        if (isHive) {
                            temp = MetaDataConstants.TABLE_CAT.name();
                        } else {
                            temp = MetadataConnectionUtils.isOdbcPostgresql(dbJDBCMetadata)
                                    ? DatabaseConstant.ODBC_POSTGRESQL_CATALOG_NAME
                                    : MetaDataConstants.TABLE_CAT.name();
                        }
                        catalogName = catalogNames.getString(temp);
                        // MOD zshen filter ODBC catalog
                        // FIXME isODBCCatalog is not a good name
                        if (!isHive && !MetadataConnectionUtils.isODBCCatalog(catalogName, dbJDBCMetadata)) {
                            continue;
                        }
                    } catch (Exception e) {
                        log.warn(e, e);
                        if (dbJDBCMetadata.getDatabaseProductName() != null && dbJDBCMetadata.getDatabaseProductName()
                                .toLowerCase().indexOf(DatabaseConstant.POSTGRESQL_PRODUCT_NAME) > -1) {
                            catalogName = ""; //$NON-NLS-1$
                        }
                    }
                    // Changed by Marvin Wang on Jan. 6, 2012 for bug TDI-24222. I haved discussed with Shen Ze about
                    // the fix,it is okay for them. What we did is to avoid creating a new catalog when catalogName is
                    // "null" for DB2.
                    if (catalogName != null) {
                        // MOD xqliu 2010-03-03 feature 11412

                        if (!isNullSID(dbConn) && dbConn != null
                                && !dbConn.getDatabaseType().equals(EDatabaseTypeName.AS400.getDisplayName())
                                && !dbConn.getDatabaseType().equals(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName())
                                && !dbConn.getDatabaseType().equals(EDatabaseTypeName.HSQLDB_SERVER.getDisplayName())
                                && !dbConn.getDatabaseType().equals(EDatabaseTypeName.HSQLDB_WEBSERVER.getDisplayName())) {
                            String databaseOnConnWizard = getDatabaseName(dbConn);

                            // If the SID on ui is not empty, the catalog name should be same to this SID name.
                            postFillCatalog(catalogList, catalogFilter, schemaFilterList,
                                    TalendCWMService.getReadableName(dbConn, databaseOnConnWizard), dbConn);
                            break;
                        } else if (isCreateElement(catalogFilter, catalogName, false)) {
                            postFillCatalog(catalogList, catalogFilter, schemaFilterList, catalogName, dbConn);
                        }
                    }
                    // ~11412
                }
                // --- release the result set.
                catalogNames.close();
                if (!isHive) {
                    List<Catalog> removeCatalogList = new ArrayList<Catalog>();
                    for (Catalog catalog : catalogList) {
                        List<Schema> schemaList = new ArrayList<Schema>();
                        try {
                            schemaList = fillSchemaToCatalog(dbConn, dbJDBCMetadata, catalog, schemaFilterList);
                            if (!schemaList.isEmpty() && schemaList.size() > 0) {
                                CatalogHelper.addSchemas(schemaList, catalog);
                            }
                        } catch (Throwable e) {
                            removeCatalogList.add(catalog);
                        }
                    }
                    // TDQ-1625
                    if (isSybase && catalogFilter != null && !catalogFilter.isEmpty() && catalogFilter.size() > 0
                            && catalogList.isEmpty() && catalogList.size() == 0) {
                        catalogFilter.clear();
                        return fillCatalogs(dbConn, dbJDBCMetadata, catalogFilter);
                    }
                    catalogList.removeAll(removeCatalogList);
                }

                Set<MetadataTable> tableSet = ConnectionHelper.getTables(dbConn);
                // replaceCatalogs is use for record tables when click finish, then set to current connection.
                List<Catalog> replaceCatalogs = new ArrayList<Catalog>();
                List<String> catalogName = new ArrayList<String>();
                for (MetadataTable table : tableSet) {
                    EObject eContainer = table.eContainer();
                    if (eContainer != null) {
                        if (eContainer instanceof Catalog) {
                            Catalog c = (Catalog) eContainer;
                            String name = c.getName();
                            if (!catalogName.contains(name)) {
                                replaceCatalogs.add(c);
                                catalogName.add(name);
                            }
                        } else if (eContainer instanceof Schema) {
                            EObject parent = eContainer.eContainer();
                            if (parent != null && parent instanceof Catalog) {
                                Catalog c = (Catalog) parent;
                                String name = c.getName();
                                if (!catalogName.contains(name)) {
                                    List<Schema> filterSchemas = new ArrayList<Schema>();
                                    List<String> schemaName = new ArrayList<String>();
                                    List<Schema> schemas = CatalogHelper.getSchemas(c);
                                    for (Schema schema : schemas) {
                                        if (schemaFilterList != null) {
                                            if (schemaFilterList.contains(schema.getName())) {
                                                filterSchemas.add(schema);
                                                schemaName.add(schema.getName());
                                            } else if (schema.getOwnedElement() != null && !schema.getOwnedElement().isEmpty()) {
                                                filterSchemas.add(schema);
                                                schemaName.add(schema.getName());
                                            }
                                        }
                                    }
                                    // get schema in current connection
                                    for (Catalog catalog : catalogList) {
                                        if (catalog.getName().equals(name)) {
                                            boolean added = false;
                                            for (Schema schema : CatalogHelper.getSchemas(catalog)) {
                                                if (!schemaName.contains(schema.getName())) {
                                                    filterSchemas.add(schema);
                                                    added = true;
                                                }
                                            }
                                            if (added) {
                                                break;
                                            }
                                        }
                                    }

                                    c.getOwnedElement().clear();
                                    CatalogHelper.addSchemas(filterSchemas, c);
                                    replaceCatalogs.add(c);
                                    catalogName.add(name);
                                }
                            }
                        }
                    }
                }
                if (this.isLinked() && !catalogList.isEmpty()) {
                    ConnectionHelper.addCatalogs(catalogList, dbConn);
                }
                // if have same schema in current connection,need to fill tables.
                for (Catalog catalog : replaceCatalogs) {
                    List<Catalog> list = new ArrayList<Catalog>();
                    String name = catalog.getName();
                    Catalog c = (Catalog) ConnectionHelper.getPackage(name, dbConn, Catalog.class);
                    if (c != null) {
                        list.add(c);
                        ConnectionHelper.removeCatalogs(list, dbConn);
                        ConnectionHelper.addCatalog(catalog, dbConn);
                    } else {
                        ConnectionHelper.addCatalog(catalog, dbConn);
                    }
                }
            }
        } catch (SQLException e) {
            log.warn("JDBC getCatalogs() method is not available with this driver.", e); //$NON-NLS-1$
        }

        return catalogList;
    }

    /**
     * return the database name of the DatabaseConnection, if the dbtype is jdbc should get the database name form the
     * url.
     * 
     * @param dbConn
     * @return
     */
    protected String getDatabaseName(DatabaseConnection dbConn) {
        return dbConn.getSID();
    }

    /**
     * judge db support get catalogNames or not
     * 
     * @param dbJDBCMetadata
     * @return
     */
    private boolean isDbSupportCatalogNames(DatabaseMetaData dbJDBCMetadata) throws SQLException {
        // Now here that OracleForSid,db2,OdbcTeradata,Exasol dosen't support the catalog name.
        if (ConnectionUtils.isOracleForSid(dbJDBCMetadata, EDatabaseTypeName.ORACLEFORSID.getProduct())
                || ConnectionUtils.isDB2(dbJDBCMetadata) || ConnectionUtils.isOdbcTeradata(dbJDBCMetadata)
                || ConnectionUtils.isExasol(dbJDBCMetadata)) {
            return false;
        }
        return true;
    }

    /**
     * judge db have catalogs or not
     * 
     * @param dbJDBCMetadata
     * @return
     */
    private boolean isDbHasCatalogs(DatabaseMetaData dbJDBCMetadata) throws SQLException {
        // Although Sqlite support 'dbJDBCMetadata.getCatalogs()',but in fact it has no catalogs,just return.
        if (ConnectionUtils.isSqlite(dbJDBCMetadata, EDatabaseTypeName.SQLITE.getProduct())) {
            return false;
        }
        return true;
    }

    /**
     * fill the catalog and schemas into Postgresql database connection.
     * 
     * @param dbConn
     * @param dbJDBCMetadata
     * @param catalogList
     * @return
     */
    private List<Catalog> fillPostgresqlCatalogs(IMetadataConnection metaConnection, DatabaseConnection dbConn,
            DatabaseMetaData dbJDBCMetadata, List<Catalog> catalogList) {
        String catalogName = getDatabaseName(dbConn);

        if (StringUtils.isEmpty(catalogName)) {
            catalogName = dbConn.getUsername();
        }

        if (StringUtils.isNotEmpty(catalogName)) {
            List<String> filterList = new ArrayList<String>();
            filterList.addAll(postFillCatalog(metaConnection, catalogList, filterList,
                    TalendCWMService.getReadableName(dbConn, catalogName), dbConn));
            List<Schema> schemaList = new ArrayList<Schema>();
            for (Catalog catalog : catalogList) {
                try {
                    schemaList = fillSchemaToCatalog(dbConn, dbJDBCMetadata, catalog, filterList);
                    if (!schemaList.isEmpty() && schemaList.size() > 0) {
                        CatalogHelper.addSchemas(schemaList, catalog);
                    }
                } catch (Throwable e) {
                    log.info(e);
                }
            }

            replaceTablesForDbConn(dbConn, catalogList, filterList);
        }
        return catalogList;
    }

    /**
     * judge whether SID is null or empty string whatever context mode or nor
     * 
     * @param dbConn
     * @return
     */
    private boolean isNullSID(Connection dbConn) {
        if (dbConn instanceof DatabaseConnection) {
            String databaseOnConnWizard = getDatabaseName((DatabaseConnection) dbConn);
            String readableName = TalendCWMService.getReadableName(dbConn, databaseOnConnWizard);
            if (StringUtils.isEmpty(databaseOnConnWizard) || StringUtils.isEmpty(readableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * judge whether UiSchema is null or empty string whatever context mode or nor
     * 
     * @param dbConn
     * @return
     */
    protected boolean isNullUiSchema(Connection dbConn) {
        if (dbConn instanceof DatabaseConnection) {
            String databaseOnConnWizard = ((DatabaseConnection) dbConn).getUiSchema();
            String readableName = TalendCWMService.getReadableName(dbConn, databaseOnConnWizard);
            if (StringUtils.isEmpty(databaseOnConnWizard) || StringUtils.isEmpty(readableName)) {
                return true;
            }
        }
        return false;
    }

    private List<String> postFillCatalog(List<Catalog> catalogList, List<String> catalogFilterList, List<String> scheamFilterList,
            String catalogName, Connection dbConn) {
        Catalog catalog = CatalogHelper.createCatalog(catalogName);
        catalogList.add(catalog);
        DatabaseConnection dbConnection = (DatabaseConnection) dbConn;

        if (dbConnection.getDatabaseType() != null
                && dbConnection.getDatabaseType().equals(EDatabaseTypeName.AS400.getDisplayName())) {// AS400
            // TDI-17986
            IMetadataConnection iMetadataCon = ConvertionHelper.convert(dbConnection);
            if (iMetadataCon != null) {
                if (!StringUtils.isEmpty(iMetadataCon.getDatabase()) && !catalogFilterList.contains(iMetadataCon.getDatabase())) {
                    // TDI-23485:filter database for AS400,should not use schema filter
                    catalogFilterList.add(iMetadataCon.getDatabase());
                }
                String pattern = ExtractMetaDataUtils.getInstance()
                        .retrieveSchemaPatternForAS400(iMetadataCon.getAdditionalParams());
                String sid = getDatabaseName(dbConnection);
                if (pattern != null && !"".equals(pattern)) { //$NON-NLS-1$
                    String[] multiSchems = ExtractMetaDataUtils.getInstance().getMultiSchems(pattern);
                    if (multiSchems != null) {
                        for (String s : multiSchems) {
                            if (!StringUtils.isEmpty(s) && !scheamFilterList.contains(s)) {
                                scheamFilterList.add(s);
                            }
                        }
                    }
                } else if (sid != null && !"".equals(sid)) { //$NON-NLS-1$
                    scheamFilterList.add(sid);
                }
            }
        } else {
            String uiSchema = dbConnection.getUiSchema();
            if (uiSchema != null) {
                uiSchema = TalendCWMService.getReadableName(dbConn, uiSchema);
            }
            if (!StringUtils.isBlank(uiSchema) && !scheamFilterList.contains(uiSchema)) {
                scheamFilterList.add(uiSchema);
            }
        }
        return scheamFilterList;
    }

    @Deprecated
    private List<String> postFillCatalog(IMetadataConnection metaConnection, List<Catalog> catalogList, List<String> filterList,
            String catalogName, Connection dbConn) {
        Catalog catalog = CatalogHelper.createCatalog(catalogName);
        catalogList.add(catalog);
        DatabaseConnection dbConnection = (DatabaseConnection) dbConn;

        if (dbConnection.getDatabaseType() != null
                && dbConnection.getDatabaseType().equals(EDatabaseTypeName.AS400.getDisplayName())) {// AS400
            // TDI-17986
            IMetadataConnection iMetadataCon = ConvertionHelper.convert(dbConnection);
            if (iMetadataCon != null) {
                if (!StringUtils.isEmpty(iMetadataCon.getDatabase()) && !filterList.contains(iMetadataCon.getDatabase())) {
                    filterList.add(iMetadataCon.getDatabase());
                }
                String pattern = ExtractMetaDataUtils.getInstance()
                        .retrieveSchemaPatternForAS400(iMetadataCon.getAdditionalParams());
                if (pattern != null && !"".equals(pattern)) { //$NON-NLS-1$
                    String[] multiSchems = ExtractMetaDataUtils.getInstance().getMultiSchems(pattern);
                    if (multiSchems != null) {
                        for (String s : multiSchems) {
                            if (!StringUtils.isEmpty(s) && !filterList.contains(s)) {
                                filterList.add(s);
                            }
                        }
                    }
                }
            }
        } else {
            String uiSchema = dbConnection.getUiSchema();
            if (uiSchema != null) {
                uiSchema = TalendCWMService.getReadableName(dbConn, uiSchema);
            }
            if (!StringUtils.isBlank(uiSchema) && !filterList.contains(uiSchema)) {
                filterList.add(uiSchema);
            }
        }
        return filterList;
    }

    @Override
    public List<Schema> fillSchemaToCatalog(DatabaseConnection dbConn, DatabaseMetaData dbJDBCMetadata, Catalog catalog,
            List<String> schemaFilter) throws Throwable {
        ResultSet schemaRs = null;
        try {
            // Case of JDK 1.6
            if (dbJDBCMetadata.getDriverName().equals(DatabaseConstant.MSSQL_DRIVER_NAME_JDBC2_0)) {
                Method getSchemaMethod = dbJDBCMetadata.getClass().getMethod("getSchemas", String.class, String.class); //$NON-NLS-1$
                schemaRs = (ResultSet) getSchemaMethod.invoke(dbJDBCMetadata, catalog.getName(), null);
            }
        } catch (SecurityException e) {
            // Case of JDK1.5
        } catch (NoSuchMethodException e) {
            // do nothing here !!!
        } catch (IllegalArgumentException e) {
            // do nothing here !!!
        } catch (IllegalAccessException e) {
            // do nothing here !!!
        } catch (InvocationTargetException e) {
            // Case of JDK1.5
            if (e.getTargetException().getClass().toString().equals("SQLServerException")) { //$NON-NLS-1$
                throw e.getTargetException();
            }
        } catch (SQLException e) {
            log.error(e, e);
        }

        if (schemaRs == null) {
            try {
                if (dbJDBCMetadata instanceof SybaseDatabaseMetaData) {
                    schemaRs = ((SybaseDatabaseMetaData) dbJDBCMetadata).getSchemas(catalog.getName(), null);
                } else if (dbJDBCMetadata instanceof AS400DatabaseMetaData) {
                    String schemaPattern = null;
                    if (!schemaFilter.isEmpty()) {
                        schemaPattern = schemaFilter.get(0);
                    }
                    schemaRs = dbJDBCMetadata.getSchemas(catalog.getName(), schemaPattern);
                } else {
                    try {
                        String schemaPattern = null;
                        if (!schemaFilter.isEmpty()) {
                            schemaPattern = schemaFilter.get(0);
                        }
                        schemaRs = dbJDBCMetadata.getSchemas(catalog.getName(), schemaPattern);
                        if (schemaRs == null) {
                            schemaRs = dbJDBCMetadata.getSchemas();
                        }
                    } catch (Throwable t) {
                        // some drivers don't support getSchemas(catalog, pattern), if exception occurs, just try again
                        // with getSchemas()
                        schemaRs = dbJDBCMetadata.getSchemas();
                    }
                }
            } catch (SQLException e) {
                if (log.isDebugEnabled()) {
                    log.debug(e, e);
                }
            }
        }

        List<String> schemaNameCacheTmp = new ArrayList<String>();
        List<Schema> schemaList = new ArrayList<Schema>();

        if (schemaRs == null) {
            log.error("Schema result set is null"); //$NON-NLS-1$
        } else {
            try {
                while (schemaRs.next()) {
                    String schemaName = getSchemaName(schemaRs, dbJDBCMetadata, catalog);
                    if (schemaName == null) {
                        continue;
                    }

                    EDatabaseTypeName dbTypeName = EDatabaseTypeName.getTypeFromDbType(dbConn.getDatabaseType());
                    // MOD mzhao bug 9606 filter duplicated schemas.
                    if (!schemaNameCacheTmp.contains(schemaName) && !MetadataConnectionUtils.isMysql(dbJDBCMetadata)) {
                        if (dbConn != null && !isNullUiSchema(dbConn)) {
                            // this case we only create one schema which name is same as UiSchema
                            Schema createByUiSchema = createSchemaByUiSchema(dbConn);
                            schemaList.add(createByUiSchema);
                            break;
                        } else if (isCreateElement(schemaFilter, schemaName,
                                ManagerConnection.isSchemaCaseSensitive(dbTypeName))) {
                            Schema schema = SchemaHelper.createSchema(schemaName);
                            schemaList.add(schema);
                            schemaNameCacheTmp.add(schemaName);
                        }
                    }
                }
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug(e, e);
                }
            } finally {
                schemaRs.close();
            }
        }

        return schemaList;
    }

    protected Schema createSchemaByUiSchema(DatabaseConnection dbConn) {
        String uiSchemaOnConnWizard = dbConn.getUiSchema();
        // If the UiSchema on ui is not empty, the shema name should be same to this UiSchema name.
        return SchemaHelper.createSchema(TalendCWMService.getReadableName(dbConn, uiSchemaOnConnWizard));
    }

    protected String getSchemaName(ResultSet schemaRs, DatabaseMetaData dbJDBCMetadata, Catalog catalog) {
        String schemaName = null;
        String catalogName = null;
        try {
            schemaName = schemaRs.getString(MetaDataConstants.TABLE_SCHEM.name());
            // MOD klliu bug 19004 2011-03-31
            if (!(MetadataConnectionUtils.isPostgresql(dbJDBCMetadata) || MetadataConnectionUtils.isSybase(dbJDBCMetadata))) {
                catalogName = schemaRs.getString(MetaDataConstants.TABLE_CATALOG.name());
            }

            // the case for mssql
            if (MetadataConnectionUtils.isMssql(dbJDBCMetadata)) {
                // seems schema is supported since SqlServer 2005
                if (dbJDBCMetadata.getDatabaseMajorVersion() < 9 && dbJDBCMetadata.getDriverMajorVersion() > 1) {
                    if (catalogName != null && !catalogName.equals(schemaName)) {
                        schemaName = catalogName;
                    }
                } else {
                    if (catalogName != null && !catalogName.equals(catalog.getName())) {
                        return null;
                    }
                }
            }
            if (!MetadataConnectionUtils.isMssql(dbJDBCMetadata.getConnection()) && catalogName != null
                    && !catalogName.equals(catalog.getName())) {
                return null;
            }
        } catch (Exception e) {
            // not some things need to do
        }
        return schemaName;
    }

    @Override
    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> tableFilter, String tablePattern, String[] tableType) {

        List<MetadataTable> list = new ArrayList<MetadataTable>();
        if (dbJDBCMetadata == null) {
            return null;
        }
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        Package catalogOrSchema = PackageHelper.getCatalogOrSchema(pack);
        String catalogName = null;
        String schemaPattern = null;

        if (catalogOrSchema != null) {
            // catalog
            if (catalogOrSchema instanceof Catalog) {
                catalogName = catalogOrSchema.getName();
            } else {// schema
                Package parentCatalog = PackageHelper.getParentPackage(catalogOrSchema);
                // in the fillSchema, we set one default schema with " ", but this one doesn't exist, so we should
                // replace to get the tables from all schemas instead
                schemaPattern = " ".equals(catalogOrSchema.getName()) ? null : catalogOrSchema.getName(); //$NON-NLS-1$
                catalogName = parentCatalog == null ? null : parentCatalog.getName();
            }
        }
        try {
            // common
            boolean isOracle8i = true;
            boolean isOracle = false;
            boolean isOracleJdbc = false;
            String tableComment = null;
            List<String> tablesToFilter = new ArrayList<String>();
            if (pack != null) {
                Connection c = ConnectionHelper.getConnection(pack);
                isOracle8i = MetadataConnectionUtils.isOracle8i(c);
                isOracle = MetadataConnectionUtils.isOracle(c);
                isOracleJdbc = MetadataConnectionUtils.isOracleJDBC(c);
                if ((isOracleJdbc || isOracle) && !isOracle8i) {// oracle and not oracle8
                    Statement stmt;
                    try {
                        // MOD qiongli TDQ-4732 use the common method to create statement both DI and DQ,avoid Exception
                        // for top.
                        stmt = dbJDBCMetadata.getConnection().createStatement();
                        ResultSet rsTables = stmt.executeQuery(TableInfoParameters.ORACLE_10G_RECBIN_SQL);
                        tablesToFilter = ExtractMetaDataFromDataBase.getTableNamesFromQuery(rsTables,
                                dbJDBCMetadata.getConnection());
                        rsTables.close();
                        stmt.close();
                    } catch (SQLException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }

            boolean isHive2 = HiveConnectionManager.getInstance().isHive2(metaConnection);
            if (isHive2) {
                // for CDH4 HIVE2 , the table type are MANAGED_TABLE and EXTERNAL_TABLE ......
                // tableType = null;
            }
            ResultSet tables = dbJDBCMetadata.getTables(catalogName, schemaPattern, tablePattern, tableType);

            while (tables.next()) {
                String coloumnName = GetTable.TABLE_SCHEM.name();
                if (schemaPattern != null) {
                    try {
                        tables.getString(coloumnName);
                    } catch (Exception e) {
                        coloumnName = GetTable.TABLE_SCHEMA.name();
                    }
                }
                String tableName = getStringFromResultSet(tables, GetTable.TABLE_NAME.name());
                String temptableType = getStringFromResultSet(tables, GetTable.TABLE_TYPE.name());

                // for special db. teradata_sql_model/db2_zos/as400
                temptableType = convertSpecialTableType(temptableType);
                // for
                if (!isCreateElement(tableFilter, tableName, false)) {
                    continue;
                }
                if (tableName == null || tablesToFilter.contains(tableName)) {
                    continue;
                }
                // if (!isOracle && !isOracle8i && !isOracleJdbc && tableName.startsWith("/")) { //$NON-NLS-1$
                // continue;
                // }
                if (!isOracle8i) {
                    tableComment = getTableComment(dbJDBCMetadata, tables, tableName, catalogName, schemaPattern);
                }
                MetadataTable metadatatable = null;
                if (TableType.VIEW.toString().equals(temptableType) || ETableTypes.VIRTUAL_VIEW.getName().equals(temptableType)) {
                    metadatatable = RelationalFactory.eINSTANCE.createTdView();
                } else {
                    metadatatable = RelationalFactory.eINSTANCE.createTdTable();
                }

                metadatatable.setName(tableName);
                // Added by Marvin Wang on Feb. 6, 2012 for bug TDI-24413, it is just for hive external table.
                if (ETableTypes.TABLETYPE_EXTERNAL_TABLE.getName().equals(temptableType)
                        || ETableTypes.EXTERNAL_TABLE.getName().equals(temptableType)
                        || ETableTypes.MANAGED_TABLE.getName().equals(temptableType)
                        || ETableTypes.INDEX_TABLE.getName().equals(temptableType)
                        || ETableTypes.TABLETYPE_BASE_TABLE.getName().equals(temptableType)) {
                    metadatatable.setTableType(ETableTypes.TABLETYPE_TABLE.getName());
                } else if (ETableTypes.VIRTUAL_VIEW.getName().equals(temptableType)) {
                    metadatatable.setTableType(ETableTypes.TABLETYPE_VIEW.getName());
                } else if (ETableTypes.TABLETYPE_CALCULATION_VIEW.getName().equals(temptableType)) {
                    String catalog = getStringFromResultSet(tables, GetTable.TABLE_CAT.name());
                    TaggedValueHelper.setTaggedValue(metadatatable, GetTable.TABLE_CAT.name(), catalog);
                    String schema = getStringFromResultSet(tables, GetTable.TABLE_SCHEM.name());
                    TaggedValueHelper.setTaggedValue(metadatatable, GetTable.TABLE_SCHEM.name(), schema);
                    TaggedValueHelper.setTaggedValue(metadatatable, GetTable.TABLE_TYPE.name(), temptableType);
                    metadatatable.setTableType(ETableTypes.TABLETYPE_CALCULATION_VIEW.getName());
                } else {
                    metadatatable.setTableType(temptableType);
                }
                metadatatable.setLabel(metadatatable.getName());
                if (tableComment != null) {
                    metadatatable.setComment(tableComment);
                    ColumnSetHelper.setComment(tableComment, metadatatable);
                }
                try {
                    if (tables.getString("SYSTEM_TABLE_NAME") != null && tables.getString("SYSTEM_TABLE_SCHEMA") != null //$NON-NLS-1$//$NON-NLS-2$
                            && tables.getString("TABLE_SCHEMA") != null) { //$NON-NLS-1$
                        TaggedValueHelper.setTaggedValue(metadatatable, TaggedValueHelper.SYSTEMTABLENAME,
                                tables.getString("SYSTEM_TABLE_NAME").trim()); //$NON-NLS-1$
                        TaggedValueHelper.setTaggedValue(metadatatable, TaggedValueHelper.SYSTEMTABLESCHEMA,
                                tables.getString("SYSTEM_TABLE_SCHEMA").trim()); //$NON-NLS-1$
                        TaggedValueHelper.setTaggedValue(metadatatable, TaggedValueHelper.TABLESCHEMA,
                                tables.getString("TABLE_SCHEMA").trim()); //$NON-NLS-1$
                    }
                } catch (SQLException e) {
                    // don't catch anything if the system table name or schema doesn't exist
                    // this part is needed only for as400
                }
                list.add(metadatatable);
            }
            if (dbJDBCMetadata.getDatabaseProductName() != null
                    && dbJDBCMetadata.getDatabaseProductName().equals("Microsoft SQL Server")) { //$NON-NLS-1$
                for (String element : tableType) {
                    if (element.equals("SYNONYM")) { //$NON-NLS-1$
                        Statement stmt = extractMeta.getConn().createStatement();
                        extractMeta.setQueryStatementTimeout(stmt);
                        String schemaname = schemaPattern + ".sysobjects"; //$NON-NLS-1$
                        String sql = "select name from " + schemaname + " where xtype='SN'"; //$NON-NLS-1$//$NON-NLS-2$
                        if ("dbo".equalsIgnoreCase(schemaPattern)) { //$NON-NLS-1$
                            // SELECT name AS object_name ,SCHEMA_NAME(schema_id) AS schema_name FROM sys.objects where
                            // type='SN'
                            ResultSet rsTables = stmt.executeQuery(sql);
                            while (rsTables.next()) {
                                String nameKey = rsTables.getString("name").trim(); //$NON-NLS-1$

                                MetadataTable metadatatable = null;
                                metadatatable = RelationalFactory.eINSTANCE.createTdTable();

                                metadatatable.setName(nameKey);
                                metadatatable.setTableType(ETableTypes.TABLETYPE_SYNONYM.getName());
                                metadatatable.setLabel(metadatatable.getName());
                                list.add(metadatatable);
                            }
                        }
                    }
                }
            } else if (dbJDBCMetadata.getDatabaseProductName() != null
                    && dbJDBCMetadata.getDatabaseProductName().startsWith("DB2/")) { //$NON-NLS-1$
                for (String element : tableType) {
                    if (element.equals("SYNONYM")) { //$NON-NLS-1$
                        Statement stmt = extractMeta.getConn().createStatement();
                        extractMeta.setQueryStatementTimeout(stmt);
                        String sql = "SELECT NAME FROM SYSIBM.SYSTABLES where TYPE='A' and BASE_SCHEMA = '" + schemaPattern + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                        ResultSet rsTables = stmt.executeQuery(sql);
                        while (rsTables.next()) {
                            String nameKey = rsTables.getString("NAME").trim(); //$NON-NLS-1$

                            MetadataTable metadatatable = null;
                            metadatatable = RelationalFactory.eINSTANCE.createTdTable();

                            metadatatable.setName(nameKey);
                            metadatatable.setTableType(ETableTypes.TABLETYPE_SYNONYM.getName());
                            metadatatable.setLabel(metadatatable.getName());
                            list.add(metadatatable);
                        }
                    }
                }
            }
            if (isLinked()) {
                PackageHelper.addMetadataTable(ListUtils.castList(MetadataTable.class, list), pack);
            }
        } catch (SQLException e) {
            log.error(e, e);
        }

        return list;

    }

    /**
     * get Table Comment.
     * 
     * @param dbJDBCMetadata
     * @param tables
     * @param tableName
     * @param catalogName
     * @param schemaPattern
     * @return
     */
    private String getTableComment(DatabaseMetaData dbJDBCMetadata, ResultSet tables, String tableName, String catalogName,
            String schemaPattern) {
        String tableComment = getRemarksFromResultSet(tables);
        try {
            String productName = dbJDBCMetadata.getDatabaseProductName();
            if (StringUtils.isBlank(tableComment)) {
                String selectRemarkOnTable = MetadataConnectionUtils.getCommentQueryStr(productName, tableName, catalogName,
                        schemaPattern);
                if (selectRemarkOnTable != null) {
                    tableComment = executeGetCommentStatement(selectRemarkOnTable, dbJDBCMetadata.getConnection());
                }
            }
        } catch (SQLException e) {
            log.error(e, e);
        }
        return tableComment;
    }

    /**
     * get the Column Comment especially for oracle type.
     * 
     * @param dbJDBCMetadata
     * @param columns
     * @param tableName
     * @param columnName
     * @param schemaPattern
     * @return
     */
    private String getColumnComment(DatabaseMetaData dbJDBCMetadata, ResultSet columns, String tableName, String columnName,
            String schemaPattern) {
        String columnComment = getRemarksFromResultSet(columns);
        try {
            if (StringUtils.isBlank(columnComment) && MetadataConnectionUtils.isOracle(dbJDBCMetadata)) {
                String selectRemarkOnTable = "SELECT COMMENTS FROM ALL_COL_COMMENTS WHERE TABLE_NAME='" + tableName //$NON-NLS-1$
                        + "' AND OWNER='" + schemaPattern.toUpperCase() + "' AND COLUMN_NAME='" + columnName + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                columnComment = executeGetCommentStatement(selectRemarkOnTable, dbJDBCMetadata.getConnection());
            }
        } catch (SQLException e) {
            log.error(e, e);
        }
        if (columnComment == null) {
            columnComment = "";//$NON-NLS-1$
        }
        return columnComment;
    }

    @Override
    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter,
            String tablePattern, String[] tableType) {
        return fillAll(pack, dbJDBCMetadata, null, tableFilter, tablePattern, tableType);
    }

    @Override
    public List<TdTable> fillTables(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter, String tablePattern,
            String[] tableType) {
        List<TdTable> tableList = new ArrayList<TdTable>();
        if (dbJDBCMetadata == null) {
            return null;
        }
        Package catalogOrSchema = PackageHelper.getCatalogOrSchema(pack);
        String catalogName = null;
        String schemaPattern = null;

        if (catalogOrSchema != null) {
            // catalog
            if (catalogOrSchema instanceof Catalog) {
                catalogName = catalogOrSchema.getName();
                if (MetadataConnectionUtils.isAS400(catalogOrSchema)) {
                    return tableList;
                }
            } else {// schema
                Package parentCatalog = PackageHelper.getParentPackage(catalogOrSchema);
                schemaPattern = catalogOrSchema.getName();
                catalogName = parentCatalog == null ? null : parentCatalog.getName();
            }
        }
        try {
            // common
            boolean flag = true;
            String tableComment = null;
            List<String> tablesToFilter = new ArrayList<String>();
            if (pack != null) {
                Connection c = ConnectionHelper.getConnection(pack);
                flag = MetadataConnectionUtils.isOracle8i(c);
                boolean isOracle = MetadataConnectionUtils.isOracle(c);
                boolean isOracleJdbc = MetadataConnectionUtils.isOracleJDBC(c);
                // MetadataConnectionUtils.isOracle8i(connection)
                if ((isOracle || isOracleJdbc) && !flag) {// oracle and not oracle8
                    Statement stmt;
                    try {
                        // MOD qiongli TDQ-4732 use the common method to create statement both DI and DQ,avoid Exception
                        // for top.
                        stmt = dbJDBCMetadata.getConnection().createStatement();
                        ResultSet rsTables = stmt.executeQuery(TableInfoParameters.ORACLE_10G_RECBIN_SQL);
                        tablesToFilter = ExtractMetaDataFromDataBase.getTableNamesFromQuery(rsTables,
                                dbJDBCMetadata.getConnection());
                        rsTables.close();
                        stmt.close();
                    } catch (SQLException e) {
                        ExceptionHandler.process(e);
                    }
                }
            }

            ResultSet tables = dbJDBCMetadata.getTables(catalogName, schemaPattern, tablePattern, tableType);

            while (tables.next()) {
                String tableName = getStringFromResultSet(tables, GetTable.TABLE_NAME.name());
                String temptableType = getStringFromResultSet(tables, GetTable.TABLE_TYPE.name());
                // for special db. teradata_sql_model/db2_zos/as400
                temptableType = convertSpecialTableType(temptableType);
                // if TableType is view type don't create it at here.
                if (TableType.VIEW.toString().equals(temptableType)) {
                    continue;
                }

                // for
                if (!isCreateElement(tableFilter, tableName, false)) {
                    continue;
                }

                if (tableName == null || tablesToFilter.contains(tableName) || tableName.startsWith("/")) { //$NON-NLS-1$
                    continue;
                }
                if (!flag) {
                    tableComment = getTableComment(dbJDBCMetadata, tables, tableName, catalogName, schemaPattern);
                }
                // create table
                TdTable table = RelationalFactory.eINSTANCE.createTdTable();
                table.setName(tableName);
                table.setTableType(temptableType);
                table.setLabel(table.getName());
                // MOD qiongli 2011-11-30 TDQ-3930.set id for this retrive table.
                table.setId(EcoreUtil.generateUUID());
                if (tableComment != null) {
                    ColumnSetHelper.setComment(tableComment, table);
                }
                tableList.add(table);
            }
            if (isLinked()) {
                PackageHelper.addMetadataTable(ListUtils.castList(MetadataTable.class, tableList), pack);
            }

        } catch (SQLException e) {
            log.error(e, e);
        }
        return tableList;
    }

    @Override
    public List<TdView> fillViews(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> viewFilter, String viewPattern,
            String[] tableType) {
        List<TdView> viewList = new ArrayList<TdView>();
        if (dbJDBCMetadata == null) {
            return null;
        }
        Package catalogOrSchema = PackageHelper.getCatalogOrSchema(pack);
        String catalogName = null;
        String schemaPattern = null;

        if (catalogOrSchema != null) {
            // catalog
            if (catalogOrSchema instanceof Catalog) {
                catalogName = catalogOrSchema.getName();

                if (MetadataConnectionUtils.isAS400(catalogOrSchema)) {
                    return viewList;
                }
            } else {// schema
                Package parentCatalog = PackageHelper.getParentPackage(catalogOrSchema);
                schemaPattern = catalogOrSchema.getName();
                catalogName = parentCatalog == null ? null : parentCatalog.getName();
            }
        }
        try {

            ResultSet tables = dbJDBCMetadata.getTables(catalogName, schemaPattern, viewPattern, tableType);
            while (tables.next()) {

                String tableName = getStringFromResultSet(tables, GetTable.TABLE_NAME.name());
                String type = getStringFromResultSet(tables, GetTable.TABLE_TYPE.name());
                // for special db. teradata_sql_model/db2_zos/as400
                type = convertSpecialTableType(type);
                if (!isCreateElement(viewFilter, tableName, false)) {
                    continue;
                }
                // common
                boolean flag = true;
                String tableComment = null;
                if (pack != null) {
                    Connection c = ConnectionHelper.getConnection(pack);
                    flag = MetadataConnectionUtils.isOracle8i(c);
                }
                if (!flag) {
                    tableComment = getTableComment(dbJDBCMetadata, tables, tableName, catalogName, schemaPattern);
                }
                // create table
                TdView table = RelationalFactory.eINSTANCE.createTdView();
                table.setName(tableName);
                table.setTableType(type);
                table.setLabel(table.getName());
                if (tableComment != null) {
                    ColumnSetHelper.setComment(tableComment, table);
                }
                viewList.add(table);
            }
            if (isLinked()) {
                PackageHelper.addMetadataTable(ListUtils.castList(MetadataTable.class, viewList), pack);
            }
        } catch (SQLException e) {
            log.error(e, e);
        }
        return viewList;
    }

    /**
     * Add try/catch, some DB donot support some strings, like :REMARKS. (TDQ-9344) and it should not break any
     * operations, if some string can not be get from the resultset, just continue to get others.
     * 
     * @param tables
     * @param tableComment
     * @return
     */
    private String getStringFromResultSet(ResultSet resultSet, String nameOfString) {
        String valueOfString = null;
        try {
            valueOfString = resultSet.getString(nameOfString);
        } catch (SQLException e) {
            log.warn(e, e);
        }
        return valueOfString;
    }

    private String getRemarksFromResultSet(ResultSet resultSet) {
        String valueOfString = null;
        try {
            valueOfString = resultSet.getString(GetColumn.REMARKS.name());
        } catch (SQLException e) {
            // do nothing
        }
        return valueOfString;
    }

    private int getIntFromResultSet(ResultSet resultSet, String nameOfInt) {
        int valueOfInt = -1;
        try {
            valueOfInt = resultSet.getInt(nameOfInt);
        } catch (SQLException e) {
            log.error(e, e);
        }
        return valueOfInt;
    }

    @Override
    public List<TdColumn> fillColumns(ColumnSet colSet, IMetadataConnection iMetadataConnection, DatabaseMetaData dbJDBCMetadata,
            List<String> columnFilter, String columnPattern) {
        if (colSet == null || dbJDBCMetadata == null) {
            return null;
        }
        List<TdColumn> returnColumns = new ArrayList<TdColumn>();
        List<String> columnLabels = new ArrayList<String>();
        Map<String, TdColumn> columnMap = new HashMap<String, TdColumn>();
        String typeName = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        try {
            String catalogName = getName(CatalogHelper.getParentCatalog(colSet));
            Schema schema = SchemaHelper.getParentSchema(colSet);
            if (catalogName == null && schema != null) {
                catalogName = getName(CatalogHelper.getParentCatalog(schema));
            }
            String schemaPattern = getName(schema);
            schemaPattern = " ".equals(schemaPattern) ? null : schemaPattern; //$NON-NLS-1$
            String tablePattern = getName(colSet);
            // --- add columns to table
            // TDI-28578 Metadata wizard doesn't display tables starting with '/'
            boolean isOracle = MetadataConnectionUtils.isOracle(dbJDBCMetadata);
            if (isOracle && tablePattern.contains("/")) {//$NON-NLS-1$
                tablePattern = tablePattern.replaceAll("/", "//");//$NON-NLS-1$ //$NON-NLS-2$
            }

            boolean isHive = MetadataConnectionUtils.isHive(dbJDBCMetadata);
            ResultSet columns = null;
            String tableType = TaggedValueHelper.getValueString(GetTable.TABLE_TYPE.name(), colSet);
            if (tableType != null && ETableTypes.TABLETYPE_CALCULATION_VIEW.getName().equals(tableType)) {
                catalogName = TaggedValueHelper.getValueString(GetTable.TABLE_CAT.name(), colSet);
                schemaPattern = TaggedValueHelper.getValueString(GetTable.TABLE_SCHEM.name(), colSet);
                columns = dbJDBCMetadata.getColumns(catalogName, schemaPattern, tablePattern, columnPattern);
            } else {
                columns = dbJDBCMetadata.getColumns(catalogName, schemaPattern, tablePattern, columnPattern);
            }
            if (MetadataConnectionUtils.isMysql(dbJDBCMetadata)) {
                boolean check = !Pattern.matches("^\\w+$", tablePattern);//$NON-NLS-1$
                if (check && !columns.next()) {
                    columns = dbJDBCMetadata.getColumns(catalogName, schemaPattern,
                            TalendQuoteUtils.addQuotes(tablePattern, TalendQuoteUtils.ANTI_QUOTE), columnPattern);
                }
                columns.beforeFirst();
            }
            int index = 0;
            while (columns.next()) {
                int decimalDigits = 0;
                int numPrecRadix = 0;

                String fetchTableName = extractMeta.getStringMetaDataInfo(columns, ExtractManager.TABLE_NAME, null);
                fetchTableName = ManagementTextUtils.filterSpecialChar(fetchTableName);
                String databaseType = iMetadataConnection.getDbType();
                if (fetchTableName.equals(tablePattern) || databaseType.equals(EDatabaseTypeName.SQLITE.getDisplayName())) {
                    String columnName = getStringFromResultSet(columns, GetColumn.COLUMN_NAME.name());
                    TdColumn column = ColumnHelper.createTdColumn(columnName);

                    String label = column.getLabel();
                    label = ManagementTextUtils.filterSpecialChar(label);
                    String label2 = label;
                    ICoreService coreService = CoreRuntimePlugin.getInstance().getCoreService();
                    if (coreService != null && coreService.isKeyword(label)) {
                        label = "_" + label; //$NON-NLS-1$
                    }

                    label = MetadataToolHelper.validateColumnName(label, index, columnLabels);
                    column.setLabel(label);
                    column.setOriginalField(label2);

                    int dataType = 0;

                    if (!extractMeta.needFakeDatabaseMetaData(iMetadataConnection)) {
                        dataType = getIntFromResultSet(columns, GetColumn.DATA_TYPE.name());
                    }
                    // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because obviously the
                    // sql
                    // data type it is null and results in a NPE
                    typeName = getStringFromResultSet(columns, GetColumn.TYPE_NAME.name());
                    if (EDatabaseTypeName.INFORMIX.getDisplayName().equals(iMetadataConnection.getDbType())) {
                        String tn = MetadataConnectionUtils.getColumnTypeName(dbJDBCMetadata.getConnection(), fetchTableName,
                                index + 1);
                        if (tn != null) {
                            typeName = tn;
                        }
                    }
                    typeName = typeName.toUpperCase().trim();
                    typeName = ManagementTextUtils.filterSpecialChar(typeName);
                    if (typeName.startsWith("TIMESTAMP(")) { //$NON-NLS-1$
                        typeName = "TIMESTAMP"; //$NON-NLS-1$
                    }
                    typeName = MetadataToolHelper.validateValueForDBType(typeName);
                    String pattern = null;
                    if (MetadataConnectionUtils.isMssql(dbJDBCMetadata)) {
                        if (typeName.toLowerCase().equals("date")) { //$NON-NLS-1$
                            dataType = 91;
                            pattern = TalendQuoteUtils.addQuotes("dd-MM-yyyy");
                            // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because
                            // obviously
                            // the sql
                            // data type it is null and results in a NPE
                        } else if (typeName.toLowerCase().equals("time")) { //$NON-NLS-1$
                            dataType = 92;
                            pattern = TalendQuoteUtils.addQuotes("HH:mm:ss");
                            // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because
                            // obviously
                            // the sql
                            // data type it is null and results in a NPE
                        }
                    }
                    try {
                        int column_size = getIntFromResultSet(columns, GetColumn.COLUMN_SIZE.name());
                        column.setLength(column_size);
                        decimalDigits = getIntFromResultSet(columns, GetColumn.DECIMAL_DIGITS.name());

                        if (isHive && typeName.equalsIgnoreCase("DECIMAL") && columnPattern == null
                                && getStringFromResultSet(columns, GetColumn.DECIMAL_DIGITS.name()) == null) {
                            ResultSet result = dbJDBCMetadata.getColumns(catalogName, schemaPattern, tablePattern, columnName);
                            while (result.next()) {
                                decimalDigits = getIntFromResultSet(result, GetColumn.DECIMAL_DIGITS.name());
                            }
                        }
                        column.setPrecision(decimalDigits);
                        // Teradata SQL Mode no need this column
                        if (!MetadataConnectionUtils.isTeradataSQLMode(iMetadataConnection)
                                && !(dbJDBCMetadata instanceof DB2ForZosDataBaseMetadata)) {
                            numPrecRadix = getIntFromResultSet(columns, GetColumn.NUM_PREC_RADIX.name());
                        }
                    } catch (Exception e1) {
                        log.warn(e1, e1);
                    }

                    // SqlDataType
                    TdSqlDataType sqlDataType = MetadataConnectionUtils.createDataType(dataType, typeName, decimalDigits,
                            numPrecRadix);
                    column.setSqlDataType(sqlDataType);
                    if (pattern != null) {
                        column.setPattern(pattern);
                    }

                    // Null able
                    if (!extractMeta.needFakeDatabaseMetaData(iMetadataConnection)) {
                        int nullable = getIntFromResultSet(columns, GetColumn.NULLABLE.name());
                        column.getSqlDataType().setNullable(NullableType.get(nullable));
                    }
                    // Default value
                    String defaultValue = getStringFromResultSet(columns, GetColumn.COLUMN_DEF.name());

                    // TdExpression
                    Object defaultValueObject = null;
                    try {
                        defaultValueObject = columns.getObject(GetColumn.COLUMN_DEF.name());
                    } catch (Exception e1) {
                        log.warn(e1, e1);
                    }
                    String defaultStr = (defaultValueObject != null) ? String.valueOf(defaultValueObject) : defaultValue;
                    defaultStr = ManagementTextUtils.filterSpecialChar(defaultStr);
                    TdExpression defExpression = createTdExpression(GetColumn.COLUMN_DEF.name(), defaultStr);
                    column.setInitialValue(defExpression);

                    DatabaseConnection dbConnection = (DatabaseConnection) ConnectionHelper.getConnection(colSet);
                    String dbmsId = dbConnection == null ? null : dbConnection.getDbmsId();
                    if(EDatabaseTypeName.GENERAL_JDBC.getXMLType().equals(dbConnection.getDatabaseType())&&(dbmsId ==null||"".equals(dbmsId) )){
                        dbmsId ="mysql_id";
                    }
                    if (dbmsId != null) {
                        MappingTypeRetriever mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(dbmsId);
                        if (mappingTypeRetriever == null) {
                            @SuppressWarnings("null")
                            EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(dbConnection.getDatabaseType(), false);
                            if (dbType != null) {
                                mappingTypeRetriever = MetadataTalendType.getMappingTypeRetrieverByProduct(dbType.getProduct());
                            }
                        }
                        if (mappingTypeRetriever != null) {
                            String talendType = mappingTypeRetriever.getDefaultSelectedTalendType(typeName,
                                    extractMeta.getIntMetaDataInfo(columns, "COLUMN_SIZE"), //$NON-NLS-1$
                                    ExtractMetaDataUtils.getInstance().getIntMetaDataInfo(columns, "DECIMAL_DIGITS"));
                            column.setTalendType(talendType);
                            column.setSourceType(typeName);
                        }
                    }
                    try {
                        column.setNullable("YES".equals(getStringFromResultSet(columns, GetColumn.IS_NULLABLE.name()))); //$NON-NLS-1$
                    } catch (Exception e) {
                        // do nothing
                    }

                    // Comment, getColumnComment() method should be called at the end of this loop, because if the
                    // database
                    // type is oracle 12c, when call this method will close the stream of the columns ResultSet which
                    // create
                    // by dbJDBCMetadata.getColumns()
                    String colComment = getColumnComment(dbJDBCMetadata, columns, tablePattern, column.getName(), schemaPattern);
                    colComment = ManagementTextUtils.filterSpecialChar(colComment);
                    column.setComment(colComment);
                    ColumnHelper.setComment(colComment, column);

                    extractMeta.handleDefaultValue(column, dbJDBCMetadata);
                    returnColumns.add(column);
                    columnLabels.add(column.getLabel());
                    columnMap.put(columnName, column);
                    index++;
                }
            }
            columns.close();
            if (isLinked()) {
                ColumnSetHelper.addColumns(colSet, returnColumns);
            }
            fillPkandFk(colSet, columnMap, dbJDBCMetadata, catalogName, schemaPattern, tablePattern);
        } catch (Exception e) {
            log.error(e, e);
        }
        // ~
        return returnColumns;
    }

    @Override
    public List<TdColumn> fillColumns(ColumnSet colSet, DatabaseMetaData dbJDBCMetadata, List<String> columnFilter,
            String columnPattern) {
        if (colSet == null || dbJDBCMetadata == null) {
            return null;
        }
        List<TdColumn> returnColumns = new ArrayList<TdColumn>();
        Map<String, TdColumn> columnMap = new HashMap<String, TdColumn>();
        String typeName = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        try {
            String catalogName = getName(CatalogHelper.getParentCatalog(colSet));
            Schema schema = SchemaHelper.getParentSchema(colSet);
            if (catalogName == null && schema != null) {
                catalogName = getName(CatalogHelper.getParentCatalog(schema));
            }
            String schemaPattern = getName(schema);
            schemaPattern = " ".equals(schemaPattern) ? null : schemaPattern; //$NON-NLS-1$
            String tablePattern = getName(colSet);
            // --- add columns to table
            boolean isOracle = MetadataConnectionUtils.isOracle(dbJDBCMetadata);
            if (isOracle && tablePattern.contains("/")) {//$NON-NLS-1$
                tablePattern = tablePattern.replaceAll("/", "//");//$NON-NLS-1$ //$NON-NLS-2$
            }
            ResultSet columns = dbJDBCMetadata.getColumns(catalogName, schemaPattern, tablePattern, columnPattern);
            // MOD qiongli 2012-8-15 TDQ-5898,Odbc Terdata don't support some API.
            boolean isOdbcTeradata = ConnectionUtils.isOdbcTeradata(dbJDBCMetadata);

            // get the MappingTypeRetriever according to the DbmsId of the DatabaseConnection
            MappingTypeRetriever mappingTypeRetriever = null;
            DatabaseConnection dbConnection = (DatabaseConnection) ConnectionHelper.getConnection(colSet);
            String dbmsId = JavaSqlFactory.getDbmsId(dbConnection);
            if (StringUtils.isBlank(dbmsId)) {
                log.error(Messages.getString("DBConnectionFillerImpl.dbmsIdIsBlank")); //$NON-NLS-1$
            } else {
                mappingTypeRetriever = MetadataTalendType.getMappingTypeRetriever(dbmsId);
            }
            if (mappingTypeRetriever == null) {
                EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(dbConnection.getDatabaseType(), false);
                if (dbType != null) {
                    mappingTypeRetriever = MetadataTalendType.getMappingTypeRetrieverByProduct(dbType.getProduct());
                }
            }

            while (columns.next()) {
                int decimalDigits = 0;
                int numPrecRadix = 0;

                String fetchTableName = extractMeta.getStringMetaDataInfo(columns, ExtractManager.TABLE_NAME, null);
                fetchTableName = ManagementTextUtils.filterSpecialChar(fetchTableName);
                String databaseType = dbConnection.getDatabaseType();
                if (fetchTableName.equals(tablePattern) || databaseType.equals(EDatabaseTypeName.SQLITE.getDisplayName())) {
                    String columnName = getStringFromResultSet(columns, GetColumn.COLUMN_NAME.name());
                    TdColumn column = ColumnHelper.createTdColumn(columnName);

                    int dataType = 0;
                    try {
                        // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because obviously
                        // the
                        // sql
                        // data type it is null and results in a NPE
                        typeName = getStringFromResultSet(columns, GetColumn.TYPE_NAME.name());
                        typeName = typeName.toUpperCase().trim();
                        typeName = ManagementTextUtils.filterSpecialChar(typeName);
                        if (typeName.startsWith("TIMESTAMP(")) { //$NON-NLS-1$
                            typeName = "TIMESTAMP"; //$NON-NLS-1$
                        }
                        typeName = MetadataToolHelper.validateValueForDBType(typeName);
                        if (dbJDBCMetadata instanceof DB2ForZosDataBaseMetadata) {
                            // MOD klliu bug TDQ-1164 2011-09-26
                            dataType = Java2SqlType.getJavaTypeBySqlType(typeName);
                            decimalDigits = getIntFromResultSet(columns, GetColumn.DECIMAL_DIGITS.name());
                            // ~
                        } else if (dbJDBCMetadata instanceof TeradataDataBaseMetadata) {
                            // dataType = columns.getInt(GetColumn.TYPE_NAME.name());
                            dataType = Java2SqlType.getTeradataJavaTypeBySqlTypeAsInt(typeName);
                            typeName = Java2SqlType.getTeradataJavaTypeBySqlTypeAsString(typeName);
                        } else {
                            dataType = getIntFromResultSet(columns, GetColumn.DATA_TYPE.name());
                            if (!isOdbcTeradata) {
                                numPrecRadix = getIntFromResultSet(columns, GetColumn.NUM_PREC_RADIX.name());
                                decimalDigits = getIntFromResultSet(columns, GetColumn.DECIMAL_DIGITS.name());
                            }
                        }
                        if (MetadataConnectionUtils.isMssql(dbJDBCMetadata)) {
                            if (typeName.toLowerCase().equals("date")) { //$NON-NLS-1$
                                dataType = 91;
                                // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because
                                // obviously
                                // the sql
                                // data type it is null and results in a NPE
                            } else if (typeName.toLowerCase().equals("time")) { //$NON-NLS-1$
                                dataType = 92;
                                // MOD scorreia 2010-07-24 removed the call to column.getSQLDataType() here because
                                // obviously
                                // the sql
                                // data type it is null and results in a NPE
                            }
                        }

                        if (!isOdbcTeradata) {
                            int column_size = getIntFromResultSet(columns, GetColumn.COLUMN_SIZE.name());
                            column.setLength(column_size);
                        }

                    } catch (Exception e1) {
                        log.warn(e1, e1);
                    }

                    // SqlDataType
                    TdSqlDataType sqlDataType = MetadataConnectionUtils.createDataType(dataType, typeName, decimalDigits,
                            numPrecRadix);
                    column.setSqlDataType(sqlDataType);

                    // Null able
                    int nullable = 0;
                    if (dbJDBCMetadata instanceof DB2ForZosDataBaseMetadata || dbJDBCMetadata instanceof TeradataDataBaseMetadata
                            || dbJDBCMetadata instanceof EmbeddedHiveDataBaseMetadata) {
                        String isNullable = getStringFromResultSet(columns, "IS_NULLABLE");//$NON-NLS-1$
                        if (!isNullable.equals("Y")) { //$NON-NLS-1$
                            nullable = 1;
                        }
                    } else {
                        nullable = getIntFromResultSet(columns, GetColumn.NULLABLE.name());
                    }
                    column.getSqlDataType().setNullable(NullableType.get(nullable));

                    // TdExpression
                    Object defaultvalue = null;
                    try {
                        if (!isOdbcTeradata) {
                            defaultvalue = columns.getObject(GetColumn.COLUMN_DEF.name());
                        }
                    } catch (Exception e1) {
                        log.warn(e1, e1);
                    }
                    String defaultStr = (defaultvalue != null) ? String.valueOf(defaultvalue) : null;
                    TdExpression defExpression = createTdExpression(GetColumn.COLUMN_DEF.name(), defaultStr);
                    column.setInitialValue(defExpression);
                    extractMeta.handleDefaultValue(column, dbJDBCMetadata);

                    if (mappingTypeRetriever != null) {
                        String talendType = mappingTypeRetriever.getDefaultSelectedTalendType(typeName,
                                extractMeta.getIntMetaDataInfo(columns, "COLUMN_SIZE"), //$NON-NLS-1$
                                (dbJDBCMetadata instanceof TeradataDataBaseMetadata) ? 0
                                        : extractMeta.getIntMetaDataInfo(columns, "DECIMAL_DIGITS"));
                        column.setTalendType(talendType);
                        String defaultSelectedDbType = mappingTypeRetriever.getDefaultSelectedDbType(talendType);
                        column.setSourceType(defaultSelectedDbType);
                    }

                    // Comment
                    // MOD msjian TDQ-8546: fix the oracle type database column's comment is wrong
                    // getColumnComment() method should be called at the end of this loop, because if the database type
                    // is
                    // oracle 12c, when call this method will close the stream of the columns ResultSet which create by
                    // dbJDBCMetadata.getColumns()
                    String colComment = getColumnComment(dbJDBCMetadata, columns, tablePattern, column.getName(), schemaPattern);
                    ColumnHelper.setComment(colComment, column);

                    try {
                        column.setNullable(nullable == 1);
                    } catch (Exception e) {
                        // do nothing
                    }

                    returnColumns.add(column);
                    columnMap.put(columnName, column);
                }
            }
            columns.close();
            if (isLinked()) {
                ColumnSetHelper.addColumns(colSet, returnColumns);
            }
            fillPkandFk(colSet, columnMap, dbJDBCMetadata, catalogName, schemaPattern, tablePattern);
        } catch (Exception e) {
            log.error(e, e);
        }
        // ~
        return returnColumns;
    }

    /**
     * DOC scorreia Comment method "executeGetCommentStatement".
     * 
     * @param queryStmt
     * @return
     */
    private String executeGetCommentStatement(String queryStmt, java.sql.Connection connection) {
        String comment = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            statement.execute(queryStmt);

            // get the results
            resultSet = statement.getResultSet();
            if (resultSet != null) {
                while (resultSet.next()) {
                    comment = (String) resultSet.getObject(1);
                }
            }
        } catch (SQLException e) {
            // do nothing here
        } finally {
            // -- release resources
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    log.error(e, e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error(e, e);
                }
            }
        }
        return comment;
    }

    private void fillPkandFk(ColumnSet colSet, Map<String, TdColumn> columnMap, DatabaseMetaData dbJDBCMetadata,
            String catalogName, String schemaName, String tableName) throws Exception {
        if (columnMap.size() > 0) {
            Map<String, ForeignKey> foreignKeysMap = new HashMap<String, ForeignKey>();
            if (orgomg.cwm.resource.relational.RelationalPackage.eINSTANCE.getTable().isSuperTypeOf(colSet.eClass())) {
                try {
                    // primary key
                    // MOD qiongli 2011-2-21,bug 18828 ,Access database dosen't support 'getPrimaryKeys(...)'.
                    if (MetadataConnectionUtils.isOdbcExcel(dbJDBCMetadata) || MetadataConnectionUtils.isAccess(dbJDBCMetadata)
                            || MetadataConnectionUtils.isHive(dbJDBCMetadata)) {
                        log.info("This database don't support primary key and foreign key"); //$NON-NLS-1$
                        return;
                    }
                    ResultSet pkResult = dbJDBCMetadata.getPrimaryKeys(catalogName, schemaName, tableName);
                    PrimaryKey primaryKey = null;
                    while (pkResult.next()) {
                        String colName = pkResult.getString(GetPrimaryKey.COLUMN_NAME.name());
                        String pkName = pkResult.getString(GetPrimaryKey.PK_NAME.name());
                        if (pkName == null) {
                            continue;
                        }
                        if (primaryKey == null) {
                            primaryKey = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createPrimaryKey();
                            primaryKey.setName(pkName);
                        } else if (!pkName.equals(primaryKey.getName())) {
                            throw new Exception("the table" + colSet + " have two or more primaryKeys"); //$NON-NLS-1$ //$NON-NLS-2$
                        }
                        columnMap.get(colName).getUniqueKey().add(primaryKey);
                        columnMap.get(colName).setKey(true);
                        TableHelper.addPrimaryKey((TdTable) colSet, primaryKey);
                    }
                    pkResult.close();
                    // foreign key
                    ForeignKey foreignKey = null;
                    ResultSet fkResult = null;
                    try {
                        // some databases (eg. sqlite) jave not yet implemented this method
                        fkResult = dbJDBCMetadata.getImportedKeys(catalogName, schemaName, tableName);
                    } catch (Exception e) {
                        log.warn(e, e);
                    }
                    if (fkResult != null) {
                        while (fkResult.next()) {
                            String fkname = fkResult.getString(GetForeignKey.FK_NAME.name());
                            String colName = fkResult.getString(GetForeignKey.FKCOLUMN_NAME.name());
                            if (foreignKey == null || foreignKeysMap.get(fkname) == null) {
                                foreignKey = orgomg.cwm.resource.relational.RelationalFactory.eINSTANCE.createForeignKey();
                                foreignKey.setName(fkname);
                                foreignKeysMap.put(fkname, foreignKey);
                            }
                            columnMap.get(colName).getKeyRelationship().add(foreignKey);
                        }
                        fkResult.close();
                        TableHelper.addForeignKeys((TdTable) colSet,
                                Arrays.asList(foreignKeysMap.values().toArray(new ForeignKey[foreignKeysMap.values().size()])));
                    }
                } catch (SQLException e) {
                    log.error(e, e);
                }
            }
        }
    }

    private String convertSpecialTableType(String oldTableType) {
        // for special db. teradata_sql_model/db2_zos/as400
        if (oldTableType != null) {
            // In the as400, "P" is one case for table
            if ("T".equals(oldTableType) || "P".equals(oldTableType)) { //$NON-NLS-1$ //$NON-NLS-2$
                return ETableTypes.TABLETYPE_TABLE.getName();
            }
            if ("V".equals(oldTableType)) { //$NON-NLS-1$
                return ETableTypes.TABLETYPE_VIEW.getName();
            }
            if ("A".equals(oldTableType)) { //$NON-NLS-1$
                return ETableTypes.TABLETYPE_ALIAS.getName();
            }
        }
        return oldTableType;
    }

    /**
     * zshen Comment method "createTdExpression". from BooleanExpressionHelper.
     * 
     * @param language
     * @param body
     * @return
     */
    private TdExpression createTdExpression(String language, String body) {
        TdExpression expression = RelationalFactory.eINSTANCE.createTdExpression();
        expression.setBody(body);
        expression.setLanguage(language);
        return expression;
    }

    public static void setDriver(Driver d) {
        driver = d;
    }

    private void replaceTablesForDbConn(Connection dbConn, List<Catalog> catalogList, List<String> schemaFilterList) {
        Set<MetadataTable> tableSet = ConnectionHelper.getTables(dbConn);
        // replaceCatalogs is use for record tables when click finish, then set to current connection.
        List<Catalog> replaceCatalogs = new ArrayList<Catalog>();
        List<String> catalogName = new ArrayList<String>();
        for (MetadataTable table : tableSet) {
            EObject eContainer = table.eContainer();
            if (eContainer != null) {
                if (eContainer instanceof Catalog) {
                    Catalog c = (Catalog) eContainer;
                    String name = c.getName();
                    if (!catalogName.contains(name)) {
                        replaceCatalogs.add(c);
                        catalogName.add(name);
                    }
                } else if (eContainer instanceof Schema) {
                    EObject parent = eContainer.eContainer();
                    if (parent != null && parent instanceof Catalog) {
                        Catalog c = (Catalog) parent;
                        String name = c.getName();
                        if (!catalogName.contains(name)) {
                            List<Schema> filterSchemas = new ArrayList<Schema>();
                            List<String> schemaName = new ArrayList<String>();
                            List<Schema> schemas = CatalogHelper.getSchemas(c);
                            for (Schema schema : schemas) {
                                if (schemaFilterList != null) {
                                    if (schemaFilterList.contains(schema.getName())) {
                                        filterSchemas.add(schema);
                                        schemaName.add(schema.getName());
                                    } else if (schema.getOwnedElement() != null && !schema.getOwnedElement().isEmpty()) {
                                        filterSchemas.add(schema);
                                        schemaName.add(schema.getName());
                                    }
                                }
                            }
                            // get schema in current connection
                            for (Catalog catalog : catalogList) {
                                if (catalog.getName().equals(name)) {
                                    boolean added = false;
                                    for (Schema schema : CatalogHelper.getSchemas(catalog)) {
                                        if (!schemaName.contains(schema.getName())) {
                                            filterSchemas.add(schema);
                                            added = true;
                                        }
                                    }
                                    if (added) {
                                        break;
                                    }
                                }
                            }

                            c.getOwnedElement().clear();
                            CatalogHelper.addSchemas(filterSchemas, c);
                            replaceCatalogs.add(c);
                            catalogName.add(name);
                        }
                    }
                }
            }
        }
        if (this.isLinked() && !catalogList.isEmpty()) {
            ConnectionHelper.addCatalogs(catalogList, dbConn);
        }
        // if have same schema in current connection,need to fill tables.
        for (Catalog catalog : replaceCatalogs) {
            List<Catalog> list = new ArrayList<Catalog>();
            String name = catalog.getName();
            Catalog c = (Catalog) ConnectionHelper.getPackage(name, dbConn, Catalog.class);
            if (c != null) {
                list.add(c);
                ConnectionHelper.removeCatalogs(list, dbConn);
                ConnectionHelper.addCatalog(catalog, dbConn);
            } else {
                ConnectionHelper.addCatalog(catalog, dbConn);
            }
        }
    }
}
