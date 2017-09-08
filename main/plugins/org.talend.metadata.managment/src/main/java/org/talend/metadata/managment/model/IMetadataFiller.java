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

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;

import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * zshen class global comment. The class help for fill all kinds of metadata elements
 */
public interface IMetadataFiller<T extends Connection> {

    /**
     * 
     * zshen Comment method "fillUIParams". convert a Map of UI parameter to IMetadataConnection
     * 
     * @param paramMap
     * @return null only if paramMap is null
     */
    public IMetadataConnection fillUIParams(Map<String, String> paramMap);

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
    public IMetadataConnection fillUIParams(DatabaseConnection conn);

    /**
     * 
     * zshen Comment method "fillUIConnParams".
     * 
     * @param metadataBean sotre information of the connection which you will get.
     * @param connection which you want to be fill Connection.
     * @return connection which have be fill by the information store on the metadataBean.null when the information is
     * not right or the parameter of connection is null;
     */
    public T fillUIConnParams(IMetadataConnection metadataBean, T connection);

    /**
     * 
     * DOC zshen Comment method "fillCatalogs".
     * 
     * @param dbConn the connection which you want schema to be filled.Can't be null if need to fill the catalogs into
     * the object of connection. And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param catalogFilter The list for filter catalogs which you want get.If it is null all of catalogs which belong
     * to the connection will be return.
     * @return The list of catalogs after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<Catalog> fillCatalogs(T dbConn, DatabaseMetaData dbJDBCMetadata, List<String> catalogFilter);

    public List<Catalog> fillCatalogs(T dbConn, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> catalogFilter);

    /**
     * 
     * zshen Comment method "fillSchemas".
     * 
     * @param dbConn the connection which you want schema to be filled.Can't be null if need to fill the schemas into
     * the object of connection.And if Linked is false everything is ok.
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * @param Filter The list for filter schemas which you want to get.If it is null all of schenas which belong to the
     * connection will be return.
     * @return The list of schemas after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<Package> fillSchemas(T dbConn, DatabaseMetaData dbJDBCMetadata, List<String> Filter);

    public List<Package> fillSchemas(T dbConn, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> Filter);

    /**
     * wzhang Comment method "fillAll".
     * 
     * @param pack the object(catalog or schema) which you want tables to be filled.Can't be null if need to fill the
     * tables into the object of package(catalog or schema).And if Linked is false everything is ok.
     * 
     * @param dbJDBCMetadata If it is null the method will do nothing and return null too.
     * 
     * @param tableFilter The list for filter tables which you want to get.If it is null all of tables which belong to
     * the package will be return.
     * 
     * @param tablePattern another method to filter the tables.the table will be keep if it's name match to the
     * tablePattern.And if you don't want to use it null is ok.
     * 
     * @param tableType the type of Table which you want to fill.It should be the one of TableType enum.
     * 
     * @return The list of tables/views/sysnonyms after filter.Will return null only if dbJDBCMetadata isn't normal.
     */
    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> tableFilter,
            String tablePattern, String[] tableType);

    public List<MetadataTable> fillAll(Package pack, DatabaseMetaData dbJDBCMetadata, IMetadataConnection metaConnection,
            List<String> tableFilter, String tablePattern, String[] tableType);

    /**
     * 
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
            String[] tableType);

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
            String[] tableType);

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
            String columnPattern);

    public List<TdColumn> fillColumns(ColumnSet colSet, IMetadataConnection iMetadataConnection, DatabaseMetaData dbJDBCMetadata,
            List<String> columnFilter, String columnPattern);

    /**
     * 
     * zshen Comment method "isLinked".
     * 
     * @return get whether the subElements need to be linked to the parent element.
     */
    public boolean isLinked();

    /**
     * 
     * zshen Comment method "isLinked". set whether the subElements need to be linked to the parent element.
     * 
     * @return
     */
    public void setLinked(boolean isLinked);

    /**
     * this is used to check a Connection and at last the connection will be closed at once.
     * 
     * @param metadataBean
     * @return
     */
    public ReturnCode checkConnection(IMetadataConnection metadataBean);

    /**
     * this is used to create a Connection and the connection will be not closed.
     * 
     * @param metadataBean
     * @return
     */
    public TypedReturnCode<?> createConnection(IMetadataConnection metadataBean);

    /**
     * 
     * DOC mzhao Fill catalog with schema children.
     * 
     * @param dbConn
     * @param dbJDBCMetadata
     * @param catalog
     * @param schemaFilter
     * @return
     * @throws Throwable
     * 
     */
    public List<Schema> fillSchemaToCatalog(T dbConn, DatabaseMetaData dbJDBCMetadata, Catalog catalog, List<String> schemaFilter)
            throws Throwable;
}
