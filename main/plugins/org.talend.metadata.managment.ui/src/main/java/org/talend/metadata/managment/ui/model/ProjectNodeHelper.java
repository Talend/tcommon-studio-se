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
package org.talend.metadata.managment.ui.model;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Priority;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.database.EDatabaseSchemaOrCatalogMapping;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataUtils;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.utils.sql.ConnectionUtils;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;
import orgomg.cwm.resource.relational.impl.SchemaImpl;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class ProjectNodeHelper {

    /*
     * To make the database connection only show and refresh the tables in the specified datapackage when change the
     * value of database connection's SID and UISchema
     */
    /* return all tables from current datapackage with set,so that the result is disorted */
    public static Set<org.talend.core.model.metadata.builder.connection.MetadataTable> getTablesFromSpecifiedDataPackage(
            DatabaseConnection dbconn) {
        IMetadataConnection iMetadataConnection = ConvertionHelper.convert(dbconn);
        String schema = dbconn.getUiSchema();
        String catalog = dbconn.getSID();
        String databaseType = dbconn.getDatabaseType();
        EDatabaseTypeName currentType = EDatabaseTypeName.getTypeFromDbType(databaseType);
        EDatabaseSchemaOrCatalogMapping curCatalog = currentType.getCatalogMappingField();
        EDatabaseSchemaOrCatalogMapping curSchema = currentType.getSchemaMappingField();
        if (curCatalog != null && curSchema != null) {
            switch (curCatalog) {
            case Login:
                catalog = dbconn.getUsername();
                break;
            case None:
                catalog = "";
                break;
            }
            switch (curSchema) {
            case Login:
                schema = dbconn.getUsername();
                break;
            case Schema:
                schema = dbconn.getUiSchema();
                break;
            case None:
                schema = "";
                break;
            case Default_Name:
                schema = dbconn.getName(); // label for default name for
                // access or such kind of
                // non-catalogs databases
                break;
            }
        }
        boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(iMetadataConnection.getDbType());
        if (!isAccess) {
            schema = ExtractMetaDataUtils.getInstance().getDBConnectionSchema(dbconn);
        }
        return getTablesFromCurrentCatalogOrSchema(catalog, schema, dbconn);
    }

    public static Set<org.talend.core.model.metadata.builder.connection.MetadataTable> getTablesFromCurrentCatalogOrSchema(
            String dbsid, String schema, DatabaseConnection dbconn) {

        Set<org.talend.core.model.metadata.builder.connection.MetadataTable> allTables = new HashSet<org.talend.core.model.metadata.builder.connection.MetadataTable>();
        /* context model show all tables */
        if (dbconn.isContextMode()) {
            allTables = ConnectionHelper.getTables(dbconn);
        } else {
            boolean hasSchemaInCatalog = false;
            Catalog c = (Catalog) ConnectionHelper.getPackage(dbsid, dbconn, Catalog.class);
            Schema s = (Schema) ConnectionHelper.getPackage(schema, dbconn, Schema.class);
            List<Schema> subschemas = new ArrayList<Schema>();
            if (c != null) {
                subschemas = CatalogHelper.getSchemas(c);
                hasSchemaInCatalog = subschemas.size() > 0;
            }
            if (c != null && s == null && !hasSchemaInCatalog) { // only catalog
                PackageHelper.getAllTables(c, allTables);
                // PackageHelper.addMetadataTable(dbtable, c);

            } else if (s != null && !hasSchemaInCatalog && c == null) { // only schema
                PackageHelper.getAllTables(s, allTables);
                // PackageHelper.addMetadataTable(dbtable, s);
            } else if (c != null && hasSchemaInCatalog) { // both schema and catalog
                subschemas = CatalogHelper.getSchemas(c);
                hasSchemaInCatalog = subschemas.size() > 0;
                if (subschemas.size() > 0) {
                    for (Schema current : subschemas) {
                        if (current.getName() == null) {
                            /* if the current schema no name should set an empty string for name, bug 17244 */
                            current.setName("");
                        }
                        if (current.getName().equals(schema)) {
                            s = current;
                            break;
                        }
                    }
                    /**
                     * if dont specifc a schema because of getUiSchema() is null,show all cataogs table by default,or it
                     * will cause bug 0016578
                     */
                    if (s == null || "".equals(s)) {
                        // allTables = ConnectionHelper.getTables(dbconn);
                        PackageHelper.getAllTables(c, allTables);
                    } else {
                        PackageHelper.getAllTables(s, allTables);
                    }
                    // PackageHelper.addMetadataTable(dbtable, s);
                }
            } else {
                // return nothing
            }
        }
        return allTables;
    }

    /*
     * To make the database connection only show and refresh the tables in the specified datapackage when change the
     * value of database connection's SID and UISchema
     */
    /* return all tables from current datapackage with List,so that the result is order-sorted */
    public static List<org.talend.core.model.metadata.builder.connection.MetadataTable> getTablesFromSpecifiedDataPackageWithOders(
            DatabaseConnection dbconn) {
        // if the database connection is contextmodel, need to get the original value of every parameter
        IMetadataConnection iMetadataConnection = ConvertionHelper.convert(dbconn);
        String schema = dbconn.getUiSchema();
        String catalog = dbconn.getSID();
        String databaseType = dbconn.getDatabaseType();
        EDatabaseTypeName currentType = EDatabaseTypeName.getTypeFromDbType(databaseType);
        EDatabaseSchemaOrCatalogMapping curCatalog = currentType.getCatalogMappingField();
        EDatabaseSchemaOrCatalogMapping curSchema = currentType.getSchemaMappingField();
        if (curCatalog != null && curSchema != null) {
            switch (curCatalog) {
            case Login:
                catalog = dbconn.getUsername();
                break;
            case None:
                catalog = "";
                break;
            }
            switch (curSchema) {
            case Login:
                schema = dbconn.getUsername();
                break;
            case Schema:
                schema = dbconn.getUiSchema();
                break;
            case None:
                schema = "";
                break;
            case Default_Name:
                schema = dbconn.getName(); // label for default name for
                // access or such kind of
                // non-catalogs databases
                break;
            }
        }
        boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(iMetadataConnection.getDbType());
        if (!isAccess) {
            schema = ExtractMetaDataUtils.getInstance().getDBConnectionSchema(dbconn);
        }
        return getTablesFromCurrentCatalogOrSchemaWithOrders(catalog, schema, dbconn);
    }

    public static List<org.talend.core.model.metadata.builder.connection.MetadataTable> getTablesFromCurrentCatalogOrSchemaWithOrders(
            String dbsid, String schema, DatabaseConnection dbconn) {

        List<org.talend.core.model.metadata.builder.connection.MetadataTable> allTables = new ArrayList<org.talend.core.model.metadata.builder.connection.MetadataTable>();
        /* context model show all tables */
        if (dbconn.isContextMode()) {
            allTables = ConnectionHelper.getTablesWithOrders(dbconn);
        } else {
            boolean hasSchemaInCatalog = false;
            Catalog c = (Catalog) ConnectionHelper.getPackage(dbsid, dbconn, Catalog.class);
            Schema s = (Schema) ConnectionHelper.getPackage(schema, dbconn, Schema.class);
            List<Schema> subschemas = new ArrayList<Schema>();
            if (c != null) {
                subschemas = CatalogHelper.getSchemas(c);
                hasSchemaInCatalog = subschemas.size() > 0;
            }
            if (c != null && s == null && !hasSchemaInCatalog) { // only catalog
                PackageHelper.getAllTablesWithOrders(c, allTables);
                // PackageHelper.addMetadataTable(dbtable, c);

            } else if (s != null && !hasSchemaInCatalog && c == null) { // only schema
                PackageHelper.getAllTablesWithOrders(s, allTables);
                // PackageHelper.addMetadataTable(dbtable, s);
            } else if (c != null && hasSchemaInCatalog) { // both schema and catalog
                subschemas = CatalogHelper.getSchemas(c);
                hasSchemaInCatalog = subschemas.size() > 0;
                if (subschemas.size() > 0) {
                    for (Schema current : subschemas) {
                        if (current.getName().equals(schema)) {
                            s = current;
                            break;
                        }
                    }
                    /**
                     * if dont specifc a schema because of getUiSchema() is null,show all cataogs table by default,or it
                     * will cause bug 0016578
                     */
                    if (s == null || "".equals(s)) {
                        // allTables = ConnectionHelper.getTables(dbconn);
                        PackageHelper.getAllTablesWithOrders(c, allTables);
                    } else {
                        PackageHelper.getAllTablesWithOrders(s, allTables);
                    }
                    // PackageHelper.addMetadataTable(dbtable, s);
                }
            } else {
                // return nothing

            }
        }
        return allTables;
    }

    /**
     * 
     * wzhang Comment method "addDefaultTableForSpecifiedDataPackage". this function only for add metadataTable.
     * 
     * @param dbconn
     * @param dbtable
     * @throws SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ClassNotFoundException
     */
    public static void addDefaultTableForSpecifiedDataPackage(DatabaseConnection dbconn, MetadataTable dbtable)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        // if the database connection is contextmodel, need to get the original value of every parameter
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        IMetadataConnection imetadataConnection = ConvertionHelper.convert(dbconn);
        String schema = imetadataConnection.getSchema();
        String catalog = imetadataConnection.getDatabase();
        String databaseType = imetadataConnection.getDbType();
        EDatabaseTypeName currentType = EDatabaseTypeName.getTypeFromDbType(databaseType);

        // IDBMetadataProvider extractor = ExtractMetaDataFromDataBase.getProviderByDbType(databaseType);
        // if (extractor != null && currentType.isUseProvider()) {
        // catalog = extractor.getDefaultCatalogName();
        // }

        EDatabaseSchemaOrCatalogMapping curCatalog = currentType.getCatalogMappingField();
        EDatabaseSchemaOrCatalogMapping curSchema = currentType.getSchemaMappingField();
        if (curCatalog != null && curSchema != null) {
            switch (curCatalog) {
            case Login:
                catalog = imetadataConnection.getUsername();
                break;
            case None:
                catalog = "";
                break;
            }
            switch (curSchema) {
            case Login:
                schema = imetadataConnection.getUsername();
                break;
            case Schema:
                schema = imetadataConnection.getSchema();
                break;
            case None:
                schema = "";
                break;
            case Default_Name:
                schema = dbconn.getName(); // label for default name for
                // access or such kind of
                // non-catalogs databases
                break;
            }
        }
        boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(imetadataConnection.getDbType());
        if (!isAccess) {
            schema = extractMeta.getMeataConnectionSchema(imetadataConnection);
        }
        // for olap connection
        boolean isOlap = extractMeta.isOLAPConnection(dbconn);
        if (isOlap) {
            List<Catalog> catalogs = ConnectionHelper.getCatalogs(dbconn);
            if (!catalogs.isEmpty()) {
                Catalog c = catalogs.get(0);
                catalog = c.getName();
                if (!CatalogHelper.getSchemas(c).isEmpty()) {
                    Schema s = CatalogHelper.getSchemas(c).get(0);
                    schema = s.getName();
                }
            }
        }

        addTableForTemCatalogOrSchema(catalog, schema, dbconn, dbtable, imetadataConnection);
    }

    public static void addTableForSpecifiedDataPackage(DatabaseConnection dbconn, MetadataTable dbtable)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        // if the database connection is contextmodel, need to get the original value of every parameter
        IMetadataConnection imetadataConnection = ConvertionHelper.convert(dbconn);
        DatabaseConnection conn = (DatabaseConnection) imetadataConnection.getCurrentConnection();
        Collection<orgomg.cwm.objectmodel.core.Package> newDataPackage = EcoreUtil.copyAll(dbconn.getDataPackage());
        ConnectionHelper.addPackages(newDataPackage, conn);

        // String catalog = "";
        // fixed bug TDI-19395
        String catalog = imetadataConnection.getDatabase();
        String schema = "";
        EObject container = dbtable.eContainer();
        if (container != null) {
            if (container instanceof Schema) {
                schema = ((Schema) container).getName();
                EObject c = container.eContainer();
                if (c != null && c instanceof Catalog) {
                    catalog = ((Catalog) c).getName();
                }
            } else if (container instanceof Catalog) {
                catalog = ((Catalog) container).getName();
            }
        }
        boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(imetadataConnection.getDbType());
        if (!isAccess) {
            schema = ExtractMetaDataUtils.getInstance().getMeataConnectionSchema(imetadataConnection);
        }
        addTableForTemCatalogOrSchema(catalog, schema, dbconn, dbtable, imetadataConnection);
    }

    public static void addTableForTemCatalogOrSchema(String dbsid, String schema, DatabaseConnection connection,
            MetadataTable dbtable, IMetadataConnection iMetadataConnection) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, SQLException {
        addTableForTemCatalogOrSchema(dbsid, schema, connection, dbtable, iMetadataConnection, 1);
    }

    private static void addTableForTemCatalogOrSchema(String dbsid, String schema, DatabaseConnection connection,
            MetadataTable dbtable, IMetadataConnection iMetadataConnection, int stackCount) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, SQLException {
        if (3 < stackCount) {
            // if loop count is more than 3 times, seems it will never end this loop, and will never get the dbsid and
            // schema
            return;
        }
        boolean hasSchemaInCatalog = false;
        boolean isAccess = EDatabaseTypeName.ACCESS.getDisplayName().equals(iMetadataConnection.getDbType());
        Catalog c = (Catalog) ConnectionHelper.getPackage(dbsid, connection, Catalog.class);
        Schema s = (Schema) ConnectionHelper.getPackage(schema, connection, Schema.class);
        List<Schema> subschemas = new ArrayList<Schema>();
        if (c != null) {
            subschemas = CatalogHelper.getSchemas(c);
            hasSchemaInCatalog = subschemas.size() > 0;
        }
        if (c != null && s == null && !hasSchemaInCatalog) { // only catalog
            PackageHelper.addMetadataTable(dbtable, c);

        } else if (s != null && !hasSchemaInCatalog && c == null) { // only schema
            PackageHelper.addMetadataTable(dbtable, s);
        } else if (c != null && hasSchemaInCatalog) { // both schema and catalog
            subschemas = CatalogHelper.getSchemas(c);
            hasSchemaInCatalog = subschemas.size() > 0;
            if (subschemas.size() > 0) {
                for (Schema current : subschemas) {
                    if (current.getName().equals(schema)) {
                        s = current;
                        break;
                    }
                }

                if (s != null) {
                    // for bug 16794
                    if (s instanceof SchemaImpl) {
                        SchemaImpl schemaElement = (SchemaImpl) s;
                        EList<ModelElement> ownedElement = schemaElement.getOwnedElement();
                        ownedElement.add(dbtable);

                    }
                } else if (subschemas.size() > 0) {
                    // added for bug 17467
                    // set db connection's schema as null, and retrieve schema again, to add some tables
                    for (int i = 0; i < subschemas.size(); i++) {
                        SchemaImpl schemaElement = (SchemaImpl) subschemas.get(i);
                        EList<ModelElement> ownedElement = schemaElement.getOwnedElement();
                        ownedElement.add(dbtable);
                    }
                }
                // PackageHelper.addMetadataTable(dbtable, s);
            }
        } else if (s == null && c == null && !isAccess && stackCount == 1) { // TDI-20584:after migration from 4.0 to
                                                                             // 4.2,lost all catalogs
            // and schemas for database
            // in case after migration connetion from the version before 4.0,there is no any db structure on
            // temConnection,it will casue pbs,so sychronize with imetadataConnection
            fillCatalogAndSchemas(iMetadataConnection, connection);
            addTableForTemCatalogOrSchema(dbsid, schema, connection, dbtable, iMetadataConnection, stackCount + 1);
        } else {
            /*
             * if there is no catalog or schema,create the structure correctly rather than always create a catalog,found
             * this issue when fixing bug 16636
             */
            ProjectNodeHelper.addCatalogOrSchema(iMetadataConnection, connection);
            if (isAccess) {
                addTableForTemCatalogOrSchema(dbsid, connection.getName(), connection, dbtable, iMetadataConnection,
                        stackCount + 1);
            } else {
                addTableForTemCatalogOrSchema(dbsid, schema, connection, dbtable, iMetadataConnection, stackCount + 1);
            }
        }
    }

    /* create catalog or schema for a database connection,the structure is the same as TOP */
    public static void addCatalogOrSchema(IMetadataConnection metadataConnection, DatabaseConnection dbconn) {
        EDatabaseSchemaOrCatalogMapping catalog = null;
        EDatabaseSchemaOrCatalogMapping schema = null;
        EDatabaseTypeName type = EDatabaseTypeName.getTypeFromDbType(metadataConnection.getDbType());
        if (type.equals(EDatabaseTypeName.GENERAL_JDBC)) {
            String realtype = ExtractMetaDataUtils.getInstance().getDbTypeByClassName(metadataConnection.getDriverClass());
            type = EDatabaseTypeName.getTypeFromDbType(realtype);
            catalog = type.getCatalogMappingField();
            schema = type.getSchemaMappingField();
        } else {
            catalog = type.getCatalogMappingField();
            schema = type.getSchemaMappingField();
        }
        fillValuesForSchemaOrCatalog(catalog, schema, metadataConnection, dbconn);
    }

    private static void fillValuesForSchemaOrCatalog(EDatabaseSchemaOrCatalogMapping catalog,
            EDatabaseSchemaOrCatalogMapping schema, IMetadataConnection metadataConnection, DatabaseConnection dbconn) {
        Schema s = null;
        Catalog c = null;
        List<Schema> schemas = new ArrayList<Schema>();
        String user = metadataConnection.getUsername();
        String defaultname = dbconn.getName();
        String dbsid = metadataConnection.getDatabase();
        String dbuischema = metadataConnection.getSchema();
        if (schema != null && catalog != null) {
            if (schema.equals(EDatabaseSchemaOrCatalogMapping.None) && !catalog.equals(EDatabaseSchemaOrCatalogMapping.None)) {// only
                // catalog
                if (catalog.equals(EDatabaseSchemaOrCatalogMapping.Sid)) {
                    c = CatalogHelper.createCatalog(dbsid);
                    c.getDataManager().add(dbconn);
                    ConnectionHelper.addCatalog(c, dbconn);
                }

            } else if (!schema.equals(EDatabaseSchemaOrCatalogMapping.None) // only schema
                    && catalog.equals(EDatabaseSchemaOrCatalogMapping.None)) {
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Schema)) {
                    s = SchemaHelper.createSchema(dbuischema);
                    s.getDataManager().add(dbconn);
                    ConnectionHelper.addSchema(s, dbconn);
                }
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Login)) {
                    s = SchemaHelper.createSchema(user);
                    s.getDataManager().add(dbconn);
                    ConnectionHelper.addSchema(s, dbconn);
                }
                if (schema.equals(EDatabaseSchemaOrCatalogMapping.Default_Name)) { // for databases like access
                    s = SchemaHelper.createSchema(defaultname);
                    s.getDataManager().add(dbconn);
                    ConnectionHelper.addSchema(s, dbconn);
                }
            } else { // both schema and catalog
                String cvalue = dbsid;
                String svalue = null;
                cvalue = dbsid;
                switch (schema) {
                case Sid:
                    svalue = dbsid;
                    break;
                case Schema:
                    svalue = dbuischema;
                    break;
                case Login:
                    svalue = user;
                    break;
                }
                c = CatalogHelper.createCatalog(cvalue);
                s = SchemaHelper.createSchema(svalue);
                schemas.add(s);
                CatalogHelper.addSchemas(schemas, c);
                c.getDataManager().add(dbconn);
                ConnectionHelper.addCatalog(c, dbconn);
            }
        }
    }

    public static void fillCatalogAndSchemas(IMetadataConnection iMetadataConnection, DatabaseConnection temConnection)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        java.sql.Connection sqlConn = null;
        try {
            MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(iMetadataConnection);
            temConnection = (DatabaseConnection) dbInstance.fillUIConnParams(iMetadataConnection, temConnection);
            sqlConn = MetadataConnectionUtils.createConnection(iMetadataConnection).getObject();
            // because there is no any structure after import into 423 from 402,just sychronized the two connection's
            // UISchema for fill catalogs and scheams
            if (!temConnection.isContextMode()
                    && ((DatabaseConnection) iMetadataConnection.getCurrentConnection()).getUiSchema() != null) {
                temConnection.setUiSchema(((DatabaseConnection) iMetadataConnection.getCurrentConnection()).getUiSchema());
            }

            String dbType = iMetadataConnection.getDbType();
            if (sqlConn != null) {
                DatabaseMetaData dbMetaData = null;
                // Added by Marvin Wang on Mar. 13, 2013 for loading hive jars dynamically, refer to TDI-25072.
                if (EDatabaseTypeName.HIVE.getXmlName().equalsIgnoreCase(dbType)) {
                    dbMetaData = HiveConnectionManager.getInstance().extractDatabaseMetaData(iMetadataConnection);
                } else {
                    dbMetaData = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConn, dbType, false,
                            iMetadataConnection.getDatabase());
                }
                dbInstance.fillCatalogs(temConnection, dbMetaData, iMetadataConnection,
                        MetadataConnectionUtils.getPackageFilter(temConnection, dbMetaData, true));

                dbInstance.fillSchemas(temConnection, dbMetaData, iMetadataConnection,
                        MetadataConnectionUtils.getPackageFilter(temConnection, dbMetaData, false));
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (sqlConn != null) {
                ConnectionUtils.closeConnection(sqlConn);
            }
            MetadataConnectionUtils.closeDerbyDriver();
        }
    }

    /* method is used to remove table from database */
    public static void removeTables(String tableLabel, orgomg.cwm.objectmodel.core.Package subpack, DatabaseConnection connection) {
        if (subpack == null) {
            for (orgomg.cwm.objectmodel.core.Package pk : connection.getDataPackage()) {
                Iterator<ModelElement> iterator = pk.getOwnedElement().iterator();
                while (iterator.hasNext()) {
                    Object o = iterator.next();
                    if (o instanceof MetadataTable) {
                        MetadataTable table = (MetadataTable) o;
                        if (table.getLabel() != null && table.getLabel().equals(tableLabel)) {
                            iterator.remove();
                            break;
                        }
                    }
                    if (o instanceof orgomg.cwm.objectmodel.core.Package) {
                        subpack = (orgomg.cwm.objectmodel.core.Package) o;
                        removeTables(tableLabel, subpack, connection);
                    }

                }
            }
        } else {
            Iterator<ModelElement> iterator = subpack.getOwnedElement().iterator();
            while (iterator.hasNext()) {
                Object o = iterator.next();
                if (o instanceof MetadataTable) {
                    MetadataTable table = (MetadataTable) o;
                    if (table.getLabel() != null && table.getLabel().equals(tableLabel)) {
                        iterator.remove();
                        break;
                    }
                }

            }
        }
    }

    public static void removeTablesFromCurrentCatalogOrSchema(String dbsid, String schema, DatabaseConnection dbconn,
            Collection<? extends MetadataTable> tablesToDelete) {
        boolean hasSchemaInCatalog = false;
        Catalog c = (Catalog) ConnectionHelper.getPackage(dbsid, dbconn, Catalog.class);
        String dbType = dbconn.getDatabaseType();
        Schema s = null;
        if (dbType != null && !dbType.isEmpty()) {
            EDatabaseTypeName currentType = EDatabaseTypeName.getTypeFromDbType(dbconn.getDatabaseType());
            if (currentType != null && "ORACLE".equals(currentType.getProduct())) { //$NON-NLS-1$
                s = getCorrectSchemaForOracle(schema, dbconn, tablesToDelete);
            }
        }
        if (s == null) {
            s = (Schema) ConnectionHelper.getPackage(schema, dbconn, Schema.class);
        }
        List<Schema> subschemas = new ArrayList<Schema>();
        if (c != null) {
            subschemas = CatalogHelper.getSchemas(c);
            hasSchemaInCatalog = subschemas.size() > 0;
        }

        if (c != null && s == null && !hasSchemaInCatalog) { // only catalog
            c.getOwnedElement().removeAll(tablesToDelete);

        } else if (s != null && !hasSchemaInCatalog && c == null) { // only schema
            s.getOwnedElement().removeAll(tablesToDelete);
            // PackageHelper.addMetadataTable(dbtable, s);
        } else if (c != null && hasSchemaInCatalog) { // both schema and catalog
            subschemas = CatalogHelper.getSchemas(c);
            hasSchemaInCatalog = subschemas.size() > 0;
            if (subschemas.size() > 0) {
                for (Schema current : subschemas) {
                    if (current.getName().equals(schema)) {
                        s = current;
                        break;
                    }
                }
                /**
                 * if dont specifc a schema because of getUiSchema() is null,show all cataogs table by default,or it
                 * will cause bug 0016578
                 */
                if (s == null || "".equals(s)) {
                    // allTables = ConnectionHelper.getTables(dbconn);

                    // added for bug 17467
                    // set db connection's schema as null, and retrieve schema again, to remove some tables
                    EList<ModelElement> ownedElement = c.getOwnedElement();
                    if (ownedElement instanceof EObjectContainmentWithInverseEList) {
                        EObjectContainmentWithInverseEList elist = (EObjectContainmentWithInverseEList) ownedElement;
                        if (!elist.isEmpty() && elist.size() > 0) {
                            for (int i = 0; i < elist.size(); i++) {
                                Object object = elist.get(i);
                                if (object instanceof SchemaImpl) {
                                    SchemaImpl schemaImpl = (SchemaImpl) object;
                                    EList<ModelElement> ownedElement2 = schemaImpl.getOwnedElement();
                                    ownedElement2.removeAll(tablesToDelete);
                                }
                            }
                        }
                    }

                    ownedElement.removeAll(tablesToDelete);

                } else {
                    s.getOwnedElement().removeAll(tablesToDelete);
                }
                // PackageHelper.addMetadataTable(dbtable, s);
            }
        } else {
            // return nothing
        }
    }

    private static Schema getCorrectSchemaForOracle(String schema, DatabaseConnection dbconn,
            Collection<? extends MetadataTable> tablesToDelete) {
        try {
            List<Schema> schemasExist = ConnectionHelper.getSchema(dbconn);
            if (schemasExist != null && !schemasExist.isEmpty()) {
                List<Schema> findedSchema = new ArrayList<Schema>();
                Iterator<Schema> iter = schemasExist.iterator();
                while (iter.hasNext()) {
                    Schema s = iter.next();
                    if (s == null) {
                        continue;
                    }
                    if (s.getOwnedElement().containsAll(tablesToDelete)) {
                        if (s.getName().equals(schema)) {
                            return s;
                        }
                        findedSchema.add(s);
                    }
                }
                if (!findedSchema.isEmpty()) {
                    return findedSchema.get(0);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e, Priority.WARN);
        }
        return null;
    }
}
