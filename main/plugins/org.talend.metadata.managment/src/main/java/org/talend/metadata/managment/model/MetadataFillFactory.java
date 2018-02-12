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
package org.talend.metadata.managment.model;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.utils.sugars.ReturnCode;

import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * zshen class global comment. Detailled comment
 */
public class MetadataFillFactory {

    static Logger log = Logger.getLogger(MetadataFillFactory.class);

    private static IMetadataFiller<MDMConnection> MDMmetadataFiller = null;

    private static IMetadataFiller<DatabaseConnection> DBmetadataFiller = null;

    private static IMetadataFiller metadataFiller = null;

    private static MetadataFillFactory instance = null;

    private MetadataFillFactory() {
        if (MDMmetadataFiller == null) {
            MDMmetadataFiller = new MDMConnectionFillerImpl();
        }
        if (DBmetadataFiller == null) {
            DBmetadataFiller = new DBConnectionFillerImpl();
        }
    }

    public static MetadataFillFactory getMDMInstance() {
        if (instance == null) {
            instance = new MetadataFillFactory();
        }
        metadataFiller = MDMmetadataFiller;
        return instance;
    }

    /**
     * 
     * Get DatabaseConnection Instance
     * 
     * @deprecated use {@link #getDBInstance(EDatabaseTypeName)} instead it
     * @return
     */
    @Deprecated
    public static MetadataFillFactory getDBInstance() {
        if (instance == null) {
            instance = new MetadataFillFactory();
        }
        metadataFiller = DBmetadataFiller;
        return instance;
    }

    /**
     * Get DatabaseConnection Instance
     * 
     * @param eDatabaseType the type of you want to fill database
     * @return
     */
    public static MetadataFillFactory getDBInstance(EDatabaseTypeName eDatabaseType) {
        if (instance == null) {
            instance = new MetadataFillFactory();
        }
        switch (eDatabaseType) {
        case SYBASEASE:
        case SYBASEIQ:
            metadataFiller = new SybaseConnectionFillerImpl();
            break;
        case NETEZZA:
            metadataFiller = new NetezzaConnectionFiller();
            break;
        default:
            metadataFiller = DBmetadataFiller;
        }

        return instance;
    }

    public static MetadataFillFactory getDBInstance(Connection connection) {
        IMetadataConnection metadataConnection = ConvertionHelper.convert(connection, true);
        if (metadataConnection == null) {
            return getDBInstance();
        }
        return getDBInstance(metadataConnection);
    }

    public static MetadataFillFactory getDBInstance(IMetadataConnection metadataConnection) {
        EDatabaseTypeName eDatabaseType = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
        if (instance == null) {
            instance = new MetadataFillFactory();
        }
        switch (eDatabaseType) {
        case SYBASEASE:
        case SYBASEIQ:
            metadataFiller = new SybaseConnectionFillerImpl();
            break;
        case NETEZZA:
            metadataFiller = new NetezzaConnectionFiller();
            break;
        case GENERAL_JDBC:
            if (isJdbcNetezza(metadataConnection)) {
                metadataFiller = new NetezzaConnectionFiller();
                break;
            }
        default:
            metadataFiller = DBmetadataFiller;
        }

        return instance;
    }

    /**
     * 
     * zshen Comment method "fillUIParams". convert a Map of UI parameter to IMetadataConnection
     * 
     * @param paramMap
     * @return null only if paramMap is null
     */
    public IMetadataConnection fillUIParams(Map<String, String> paramMap) {
        return metadataFiller.fillUIParams(paramMap);
    }

    /**
     * 
     * zshen Comment method "fillUIParams". convert a DatabaseConnection object to IMetadataConnection
     * 
     * @deprecated
     * @see {@link org.talend.core.model.metadata.builder.ConvertionHelper#fillUIParams(IMetadataConnection, DatabaseConnection)}
     * @param conn
     * @return null only if conn is null
     */
    @Deprecated
    public IMetadataConnection fillUIParams(DatabaseConnection conn) {
        return metadataFiller.fillUIParams(conn);
    }

    /**
     * 
     * zshen Comment method "fillUIConnParams".
     * 
     * @param metadataBean sotre information of the connection which you will get.
     * @param connection which you want to be fill Connection.
     * @return connection which have be fill by the information store on the metadataBean.null when the information is
     * right or the parameter of connection is null;
     */
    public Connection fillUIConnParams(IMetadataConnection metadataBean, Connection connection) {
        return metadataFiller.fillUIConnParams(metadataBean, connection);
    }

    /**
     * 
     * Fill the catalog with the restrain of catalogFilter, note that if the database name (e.g SID for SQL server ) on
     * connection wizard UI is set, then this method will only return one catalog with this name.
     * 
     * @param dbConn the connection which you want schema to be filled.Can't be null if need to fill the catalogs into
     * the object of connection. And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param catalogFilter The list for filter catalogs which you want get.If it is null all of catalogs which belong
     * to the connection will be return.
     * @return The list of catalogs after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<Catalog> fillCatalogs(Connection dbConn, DatabaseMetaData dbJDBCMetadata, List<String> catalogFilter) {
        return metadataFiller.fillCatalogs(dbConn, dbJDBCMetadata, catalogFilter);
    }

    public List<Catalog> fillCatalogs(Connection dbConn, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> catalogFilter) {
        return metadataFiller.fillCatalogs(dbConn, dbJDBCMetadata, metaConnection, catalogFilter);
    }

    /**
     * Fill the schema with the restrain of schemaFilter, note that if the schema name (e.g UISchema for Oracle ) on
     * connection wizard UI is set, then this method will only return one schema with this name.
     * 
     * @param dbConn the connection which you want schema to be filled.Can't be null if need to fill the schemas into
     * the object of connection.And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param Filter The list for filter schemas which you want to get.If it is null all of schenas which belong to the
     * connection will be return.
     * @return The list of schemas after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<Package> fillSchemas(Connection dbConn, DatabaseMetaData dbJDBCMetadata, List<String> schemaFilter) {
        return metadataFiller.fillSchemas(dbConn, dbJDBCMetadata, schemaFilter);
    }

    public List<Package> fillSchemas(Connection dbConn, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> schemaFilter) {
        return metadataFiller.fillSchemas(dbConn, dbJDBCMetadata, metaConnection, schemaFilter);
    }

    /**
     * 
     * wzhang Comment method "fillAll".
     * 
     * @param pack the object(catalog or schema) which you want tables to be filled.Can't be null if need to fill the
     * tables into the object of package(catalog or schema).And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param tableFilter The list for filter tables which you want to get.If it is null all of tables which belong to
     * the package will be return.
     * @param tablePattern another method to filter the tables.the table will be keep if it's name match to the
     * tablePattern.And if you don't want to use it null is ok.
     * @param tableType the type of Table which you want to fill.It should be the one of TableType enum.
     * @return The list of tables/views/sysnonyms after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter,
            String tablePattern, String[] tableType) {
        return metadataFiller.fillAll(pack, dbJDBCMetadata, tableFilter, tablePattern, tableType);
    }

    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> tableFilter, String tablePattern, String[] tableType) {
        return metadataFiller.fillAll(pack, dbJDBCMetadata, metaConnection, tableFilter, tablePattern, tableType);
    }

    /**
     * 
     * zshen Comment method "fillTables".
     * 
     * @param pack the object(catalog or schema) which you want tables to be filled.Can't be null if need to fill the
     * tables into the object of package(catalog or schema).And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param tableFilter The list for filter tables which you want to get.If it is null all of tables which belong to
     * the package will be return.
     * @param tablePattern another method to filter the tables.the table will be keep if it's name match to the
     * tablePattern.And if you don't want to use it null is ok.
     * @param tableType the type of Table which you want to fill.It should be the one of TableType enum.
     * @return The list of tables after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<TdTable> fillTables(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter, String tablePattern,
            String[] tableType) {
        return metadataFiller.fillTables(pack, dbJDBCMetadata, tableFilter, tablePattern, tableType);
    }

    /**
     * 
     * zshen Comment method "fillTables".
     * 
     * @param pack the object(catalog or schema) which you want tables to be filled.Can't be null if need to fill the
     * tables into the object of package(catalog or schema).And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param tableFilter tableFilter The list for filter tables which you want to get.If it is null all of tables which
     * belong to the package will be return.
     * @param tableType the type of Table which you want to fill.It should be the one of TableType enum.
     * @return The list of tables after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<TdTable> fillTables(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter, String[] tableType) {
        return metadataFiller.fillTables(pack, dbJDBCMetadata, tableFilter, null, tableType);
    }

    /**
     * 
     * zshen Comment method "fillViews".
     * 
     * @param pack the object(catalog or schema) which you want views to be filled.Can't be null if need to fill the
     * views into the object of package(catalog or schema).And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param viewFilter The list for filter views which you want to get.If it is null all of views which belong to the
     * package will be return.
     * @return The list of views after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<TdView> fillViews(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> viewFilter, String[] tableType) {
        return metadataFiller.fillViews(pack, dbJDBCMetadata, viewFilter, null, tableType);
    }

    /**
     * 
     * zshen Comment method "fillViews".
     * 
     * @param pack the object(catalog or schema) which you want views to be filled.Can't be null if need to fill the
     * views into the object of package(catalog or schema).And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param viewFilter The list for filter views which you want to get.If it is null all of views which belong to the
     * package will be return.
     * @param another method to filter the views.the table will be keep if it's name match to the viewFilter. And if you
     * don't want to use it null is ok.
     * @return The list of views after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<TdView> fillViews(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> viewFilter, String viewPattern,
            String[] tableType) {
        return metadataFiller.fillViews(pack, dbJDBCMetadata, viewFilter, viewPattern, tableType);
    }

    /**
     * 
     * zshen Comment method "fillColumns".
     * 
     * @param colSet the object(tdView or tdTable) which you want columns to be filled.Can't be null if need to fill the
     * views into the object of package(tdView or tdTable).
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param columnFilter list for filter columns which you want to get.If it is null all of columns which belong to
     * the MetadataTable will be return.
     * @param columnPattern another method to filter the columns.the column will be keep if it's name match to the
     * columnPattern. And if you don't want to use it null is ok.
     * @return The list of column after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<TdColumn> fillColumns(ColumnSet colSet, DatabaseMetaData dbJDBCMetadata, List<String> columnFilter,
            String columnPattern) {
        return metadataFiller.fillColumns(colSet, dbJDBCMetadata, columnFilter, columnPattern);
    }

    /**
     * 
     * zshen Comment method "fillColumns".
     * 
     * @param colSet the object(tdView or tdTable) which you want columns to be filled.Can't be null if need to fill the
     * views into the object of package(tdView or tdTable).
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param columnFilter list for filter columns which you want to get.If it is null all of columns which belong to
     * the MetadataTable will be return.
     * @return The list of column after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<TdColumn> fillColumns(ColumnSet colSet, DatabaseMetaData dbJDBCMetadata, List<String> columnFilter) {
        return metadataFiller.fillColumns(colSet, dbJDBCMetadata, columnFilter, null);
    }

    public List<TdColumn> fillColumns(ColumnSet colSet, IMetadataConnection iMetadataConnection, DatabaseMetaData dbJDBCMetadata,
            List<String> columnFilter) {
        return metadataFiller.fillColumns(colSet, iMetadataConnection, dbJDBCMetadata, columnFilter, null);
    }

    /**
     * 
     * zshen Comment method "isLinked".
     * 
     * @return get whether the subElements need to be linked to the parent element.
     */
    public boolean isLinked() {
        return metadataFiller.isLinked();
    }

    /**
     * 
     * zshen Comment method "isLinked". set whether the subElements need to be linked to the parent element.
     * 
     * @return
     */
    public void setLinked(boolean isLinked) {
        metadataFiller.setLinked(isLinked);
    }

    /**
     * check a Connection and at last will close connection.
     * 
     * @param metadataBean
     * @return
     */
    public ReturnCode checkConnection(IMetadataConnection metadataBean) {
        return metadataFiller.checkConnection(metadataBean);
    }

    /**
     * create a Connection and at last will not close connection.
     * 
     * @param metadataBean
     * @return
     */
    public ReturnCode createConnection(IMetadataConnection metadataBean) {
        return metadataFiller.createConnection(metadataBean);
    }

    /**
     * 
     Fill the schema to catalog with the restrain of schemaFilter, note that if the schema name (e.g UISchema for SQL
     * server ) on connection wizard UI is set, then this method will only return one schema with this name.
     * 
     * @param dbConn
     * @param dbJDBCMetadata
     * @param catalog
     * @param schemaFilter
     * @return
     */
    public List<Schema> fillSchemaToCatalog(Connection dbConn, DatabaseMetaData dbJDBCMetadata, Catalog catalog,
            List<String> schemaFilter) {

        try {
            return metadataFiller.fillSchemaToCatalog(dbConn, dbJDBCMetadata, catalog, schemaFilter);
        } catch (Throwable e) {
            log.error(e, e);
        }

        return new ArrayList<Schema>();
    }

    /**
     * Getter for metadataFiller.
     * 
     * @return the metadataFiller
     */
    public IMetadataFiller getMetadataFiller() {
        return metadataFiller;
    }

    public static boolean isJdbcNetezza(IMetadataConnection metadataConnection) {
        if (metadataConnection == null) {
            return false;
        }
        String dbType = metadataConnection.getDbType();
        String driverClass = metadataConnection.getDriverClass();
        return isJdbcNetezza(dbType, driverClass);
    }

    public static boolean isJdbcNetezza(String dbType, String driverClass) {
        if (!StringUtils.isBlank(dbType) && !StringUtils.isBlank(driverClass)) {
            return StringUtils.equals(EDatabaseTypeName.GENERAL_JDBC.getDisplayName(), dbType)
                    && StringUtils.indexOf(StringUtils.lowerCase(driverClass), "netezza") > -1; //$NON-NLS-1$    
        }
        return false;
    }
}
