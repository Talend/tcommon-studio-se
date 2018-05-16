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
package org.talend.cwm.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.softwaredeployment.TdSoftwareSystem;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.cwm.xml.TdXmlSchema;
import org.talend.utils.security.CryptoHelper;
import org.talend.utils.sql.ConnectionUtils;

import orgomg.cwm.foundation.softwaredeployment.Component;
import orgomg.cwm.foundation.softwaredeployment.DataManager;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Namespace;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.record.RecordFile;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * Utility class for data provider handling.
 */
/**
 * DOC root class global comment. Detailled comment
 */
public class ConnectionHelper {

    public static final String DOT_STRING = "."; //$NON-NLS-1$

    // MOD xqliu 2011-07-04 feature 22201
    // public static final String PASSPHRASE = "99ZwBDt1L9yMX2ApJx fnv94o99OeHbCGuIHTy22
    // V9O6cZ2i374fVjdV76VX9g49DG1r3n90hT5c1"; //$NON-NLS-1$
    public static final String PASSPHRASE = ConnectionUtils.PASSPHRASE;

    // ~

    private static Logger log = Logger.getLogger(ConnectionHelper.class);

    /**
     * Method "createTdDataProvider" creates a data provider with the given name.
     * 
     * @param name the name of the data provider (could be null)
     * @return the created data provider.
     */
    public static DatabaseConnection createDatabaseConnection(String name) {
        DatabaseConnection provider = ConnectionFactory.eINSTANCE.createDatabaseConnection();
        provider.setName(name);
        return provider;
    }

    /**
     * Method create MDM connection
     * 
     * @param name
     * @return
     */
    public static MDMConnection createMDMConnection(String name) {
        MDMConnection provider = ConnectionFactory.eINSTANCE.createMDMConnection();
        provider.setName(name);
        provider.setLabel(name);
        return provider;
    }

    public static Connection getTdDataProvider(Package catalog) {
        assert catalog != null;
        Collection<Connection> tdDataProviders = getTdDataProviders(catalog.getDataManager());
        if ((tdDataProviders.isEmpty()) || (tdDataProviders.size() > 1)) {
            // check whether given object is a schema contained in a catalog
            Namespace cat = catalog.getNamespace();
            if (cat != null) {
                Catalog realCatalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(cat);
                if (realCatalog != null) {
                    return getTdDataProvider(realCatalog);
                }
            }
            // MOD qiongli 2011-3-17,bug 19475.avoid to return null for Ingress/db2.
            Connection con = null;
            Iterator<Connection> it = tdDataProviders.iterator();
            while (it.hasNext()) {
                con = it.next();
                if (con.getDataPackage().contains(catalog)) {
                    break;
                }
            }
            return con;
        }
        // else
        return tdDataProviders.iterator().next();
    }

    /**
     * Method "getTdDataProvider".
     * 
     * @param column
     * @return the data provider or null
     */
    public static DatabaseConnection getTdDataProvider(TdColumn column) {
        ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(column);
        if (columnSetOwner == null) {
            return null;
        }
        return getDataProvider(columnSetOwner);
    }

    /**
     * Method "getDataProvider".
     * 
     * @param columnSetOwner
     * @return the data provider or null
     */
    public static DatabaseConnection getDataProvider(ColumnSet columnSetOwner) {
        Namespace schemaOrCatalog = columnSetOwner.getNamespace();
        if (schemaOrCatalog == null) {
            return null;
        }
        Schema schema = SwitchHelpers.SCHEMA_SWITCH.doSwitch(schemaOrCatalog);
        if (schema != null) {
            return (DatabaseConnection) getTdDataProvider(schema);
        }
        // else
        Catalog catalog = SwitchHelpers.CATALOG_SWITCH.doSwitch(schemaOrCatalog);
        if (catalog != null) {
            return (DatabaseConnection) getTdDataProvider(catalog);
        }
        // else
        return null;
    }

    /**
     * Method "getTdDataProviders".
     * 
     * @param objects a collection of objects
     * @return the subset of objects containing only the TdDataProviders.
     */
    public static Collection<Connection> getTdDataProviders(Collection<? extends EObject> objects) {
        Collection<Connection> list = new ArrayList<Connection>();
        getTdDataProvider(objects, list);
        return list;
    }

    /**
     * Method "getTdDataProvider" adds the TdDataProviders found in the objects collection into the resultingCollection.
     * 
     * @param objects collection in which to search for TdDataProviders (must not be null)
     * @param resultingCollection the collection in which the TdDataProviders are added (must not be null).
     * @return true if resulting collection is not empty.
     */
    public static boolean getTdDataProvider(Collection<? extends EObject> objects, Collection<Connection> resultingCollection) {

        assert objects != null;
        assert resultingCollection != null;
        for (EObject object : objects) {
            Connection dataProv = SwitchHelpers.CONNECTION_SWITCH.doSwitch(object);
            if (dataProv != null) {
                resultingCollection.add(dataProv);
            }
        }
        return !resultingCollection.isEmpty();
    }

    /**
     * Method "setTechnicalName".
     * 
     * @param dataProvider the data provider
     * @param technicalName the technical name of the given data provider
     * @return true if the technical name was not set before.
     */
    public static boolean setTechnicalName(DatabaseConnection dataProvider, String technicalName) {
        return TaggedValueHelper.setTaggedValue(dataProvider, TaggedValueHelper.TECH_NAME_TAGGED_VAL, technicalName);
    }

    /**
     * Method "getTechnicalName".
     * 
     * @param element a cwm element
     * @return the technical name of the element (or null if none)
     */
    public static String getTechnicalName(ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.TECH_NAME_TAGGED_VAL,
                element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue();
    }

    /**
     * Method "setIdentifierQuoteString" sets a comment on the given element.
     * 
     * @param identifierQuoteString the quote to set
     * @param dataProvider the data provider
     * @return true if the value was not set before.
     */
    public static boolean setIdentifierQuoteString(String identifierQuoteString, Connection dataProvider) {
        return TaggedValueHelper.setTaggedValue(dataProvider, TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING,
                identifierQuoteString);
    }

    /**
     * Method "getIdentifierQuoteString".
     * 
     * @param dataProvider
     * @return the identifier quote string
     */
    public static String getIdentifierQuoteString(Connection dataProvider) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DB_IDENTIFIER_QUOTE_STRING,
                dataProvider.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue();
    }

    /**
     * Method "getDatabaseConnection" returns the data provider when the catalog (or schema) is associated to only one
     * data provider. It returns null if there is no data provider or more than one data provider.
     * 
     * @param catalog the catalog or schema
     * @return the associated data provider or null
     */
    public static Connection getConnection(Package thePackage) {
        Collection<Connection> tdDatabaseConnections = ConnectionHelper.getConnections(thePackage.getDataManager());
        if ((tdDatabaseConnections.isEmpty()) || (tdDatabaseConnections.size() > 1)) {
            // check whether given object is a schema contained in a catalog
            Namespace cat = thePackage.getNamespace();
            if (cat != null) {
                Package realCatalog = SwitchHelpers.PACKAGE_SWITCH.doSwitch(cat);
                if (realCatalog != null) {
                    return getConnection(realCatalog);
                }
            } // else no package is owner so return null
            return null;
        } // else we have go the connection so return it
        return tdDatabaseConnections.iterator().next();
    }

    /**
     * DOC xqliu Comment method "getDatabaseConnection".
     * 
     * @param xmlElement
     * @return
     */
    public static Connection getConnection(TdXmlElementType xmlElement) {
        return getConnection(xmlElement.getOwnedDocument());
    }

    /**
     * Method "getDatabaseConnection".
     * 
     * @param column
     * @return the data provider or null
     */
    public static Connection getConnection(TdColumn column) {
        ColumnSet columnSetOwner = ColumnHelper.getColumnOwnerAsColumnSet(column);
        if (columnSetOwner == null) {
            return null;
        }
        return getConnection(columnSetOwner);
    }

    /**
     * Method "getDatabaseConnection".
     * 
     * @param columnSetOwner
     * @return the data provider or null
     */
    public static Connection getConnection(ColumnSet columnSetOwner) {
        Namespace schemaOrCatalog = columnSetOwner.getNamespace();
        if (schemaOrCatalog == null) {
            return null;
        }
        Package thePackage = SwitchHelpers.PACKAGE_SWITCH.doSwitch(schemaOrCatalog);
        if (thePackage != null) {
            return getConnection(thePackage);
        } // else
        return null;
    }

    /**
     * Method "getDatabaseConnections".
     * 
     * @param objects a collection of objects
     * @return the subset of objects containing only the DatabaseConnections.
     */
    public static Collection<Connection> getConnections(Collection<? extends EObject> objects) {
        Collection<Connection> list = new ArrayList<Connection>();
        getConnection(objects, list);
        return list;
    }

    /**
     * Method "getDatabaseConnection" adds the DatabaseConnections found in the objects collection into the
     * resultingCollection.
     * 
     * @param objects collection in which to search for DatabaseConnections (must not be null)
     * @param resultingCollection the collection in which the DatabaseConnections are added (must not be null).
     * @return true if resulting collection is not empty.
     */
    public static boolean getConnection(Collection<? extends EObject> objects, Collection<Connection> resultingCollection) {
        assert objects != null;
        assert resultingCollection != null;
        for (EObject object : objects) {
            Connection dataProv = SwitchHelpers.CONNECTION_SWITCH.doSwitch(object);
            if (dataProv != null) {
                resultingCollection.add(dataProv);
            }
        }
        return !resultingCollection.isEmpty();
    }

    public static boolean addCatalogs(Collection<Catalog> catalogs, Connection dataProvider) {
        return addPackages(catalogs, dataProvider);
    }

    public static boolean addCatalog(Catalog catalog, Connection dataProvider) {
        return addPackage(catalog, dataProvider);
    }

    public static boolean addSchemas(Collection<Schema> schemas, Connection dataProvider) {
        return addPackages(schemas, dataProvider);
    }

    public static boolean addSchema(Schema schema, Connection dataProvider) {
        return addPackage(schema, dataProvider);
    }

    public static void removeCatalogs(Collection<Catalog> catalogs, Connection connection) {
        connection.getDataPackage().removeAll(catalogs);
    }

    public static void removeSchemas(Collection<Schema> schemas, Connection connection) {
        connection.getDataPackage().removeAll(schemas);
    }

    public static void removeAllPackages(Connection connection) {
        Set<Catalog> oldConnectionCatalogs = getAllCatalogs(connection);
        if (!oldConnectionCatalogs.isEmpty()) {
            ConnectionHelper.removeCatalogs(oldConnectionCatalogs, connection);
        }
        Set<Schema> oldConnectionSchemas = getAllSchemas(connection);
        if (!oldConnectionSchemas.isEmpty()) {
            ConnectionHelper.removeSchemas(oldConnectionSchemas, connection);
        }
    }

    // MOD mzhao feature 10238
    public static boolean addXMLDocuments(Collection<TdXmlSchema> xmlDocuments, Connection dataProvider) {
        return addPackages(xmlDocuments, dataProvider);
    }

    // MOD bzhou bug 16715
    public static boolean addXMLDocuments(Collection<TdXmlSchema> xmlDocuments) {
        if (xmlDocuments != null && xmlDocuments.iterator().hasNext()) {
            TdXmlSchema xmlSchema = xmlDocuments.iterator().next();
            DataManager dataManager = xmlSchema.getDataManager().get(0);
            if (dataManager != null) {
                return addPackages(xmlDocuments, (Connection) dataManager);
            }
        }
        return false;
    }

    /**
     * 
     * @deprecated use {@link SoftwareSystemManager#getSoftwareSystem(Connection dataProvider)} instead.
     * @param dataProvider
     * @return
     * @deprecated software system is not used any more
     */
    @Deprecated
    public static TdSoftwareSystem getSoftwareSystem(Connection dataProvider) {

        final Component component = dataProvider.getComponent();
        if (component != null) {
            final Namespace namespace = component.getNamespace();
            if (namespace != null) {
                final TdSoftwareSystem softwareSystem = SwitchHelpers.TDSOFTWARE_SYSTEM_SWITCH.doSwitch(namespace);
                dataProvider.getDeployedSoftwareSystem().addAll(softwareSystem.getDeployment());
                return softwareSystem;
            }
        }
        return null;
    }

    /**
     * Method "setSoftwareSystem" sets the relation between the dataprovider and the software system.
     * 
     * @param dataProvider (must not be null)
     * @param softwareSystem (must not be null)
     * @return true if the link between the data provider and the software system is set
     */
    public static boolean setSoftwareSystem(Connection dataProvider, TdSoftwareSystem softwareSystem) {
        assert softwareSystem != null;
        final EList<ModelElement> ownedElements = softwareSystem.getOwnedElement();
        for (ModelElement modelElement : ownedElements) {
            if (modelElement != null) {
                Component component = SwitchHelpers.COMPONENT_SWITCH.doSwitch(modelElement);
                if (component != null) {
                    dataProvider.setComponent(component);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean addPackages(Collection<? extends Package> packages, Connection dataProvider) {
        boolean added = false;
        if ((dataProvider != null) && (packages != null)) {
            List<Package> packageList = dataProvider.getDataPackage();
            if (packageList != null && packageList.size() > 0) {
                packageList.clear();
            }

            Resource eResource = dataProvider.eResource();
            if (eResource != null) {
                eResource.getContents().addAll(packages);
            }

            // FIXME potential NPE here!
            added = packageList.addAll(packages);
        }
        return added;
    }

    /**
     * add the give package to the Conneciton. You may directly use Connection.getDataPackage.add()
     * 
     * @param pack the package to be added (never null)
     * @param connection the connection to own the Package (never null)
     * @return true if add was suscesful
     */
    public static boolean addPackage(Package pack, Connection connection) {
        boolean added = false;
        if ((connection != null) && (pack != null)) {
            for (Package oldPack : connection.getDataPackage()) {
                if (pack.getName() != null && oldPack.getName() != null) {
                    if (pack.getName().equals(oldPack.getName())) {
                        return added;
                    }
                }
            }
            added = connection.getDataPackage().add(pack);
        }
        return added;
    }

    /**
     * Method "getCatalogs".
     * 
     * @param dataProvider the data provider
     * @return the catalogs contained in the data provider
     */
    public static List<Catalog> getCatalogs(Connection dataProvider) {
        return CatalogHelper.getCatalogs(dataProvider.getDataPackage());
    }

    /**
     * 
     * Judge whether contain Catalog for the dataProvider
     * 
     * @param dataProvider
     * @return
     */
    public static boolean hasCatalog(Connection dataProvider) {
        List<Catalog> catalogs = CatalogHelper.getCatalogs(dataProvider.getDataPackage());
        return catalogs.size() > 0;
    }

    /**
     * Method "getSchema".
     * 
     * @param dataProvider the data provider
     * @return the schemas contained in the data provider
     */
    public static List<Schema> getSchema(Connection dataProvider) {
        return SchemaHelper.getSchemas(dataProvider.getDataPackage());
    }

    /**
     * 
     * Two case has been contain one is only schema another one is schema contain in some catalog
     * 
     * @param dataProvider
     * @return
     */
    public static boolean hasSchema(Connection dataProvider) {
        List<Schema> schemas = SchemaHelper.getSchemas(dataProvider.getDataPackage());
        if (schemas.size() == 0) {
            List<Catalog> catalogs = CatalogHelper.getCatalogs(dataProvider.getDataPackage());
            for (Catalog catalog : catalogs) {
                return CatalogHelper.getSchemas(catalog).size() > 0;
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * DOC ycbai Comment method "getSchemaByName".
     * 
     * @param dataProvider
     * @param schemaName
     * @return
     */
    public static Schema getSchemaByName(Connection dataProvider, String schemaName) {
        return SchemaHelper.getSchemaByName(dataProvider.getDataPackage(), schemaName);
    }

    /**
     * 
     * DOC mzhao Comment method "getTdXmlDocument".
     * 
     * @param dataProvider
     * @return
     */
    public static List<TdXmlSchema> getTdXmlDocument(Connection dataProvider) {
        return XmlSchemaHelper.getDocuments(dataProvider.getDataPackage());
    }

    /**
     * DOC xqliu Comment method "getUniverse".
     * 
     * @param element
     * @return
     */
    public static String getUniverse(MDMConnection element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.UNIVERSE, element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue() == null ? "" : taggedValue.getValue();
    }

    /**
     * zshen Comment method "getDataFilter".
     * 
     * @param element the connection which contain dataFilter tag.It should be a MdmConnection.
     * @return the value of datafilter tag.empty string will be return when the element havn't contain datafilter tag.
     */
    public static String getDataFilter(MDMConnection element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DATA_FILTER, element.getTaggedValue());
        if (taggedValue == null) {
            return "";
        }
        return taggedValue.getValue() == null ? "" : taggedValue.getValue();
    }

    /**
     * zshen Comment method "getDataFilter".
     * 
     * @param element the connection which contain dataFilter tag.It should be a MdmConnection.
     * @return the value of datafilter tag.empty string will be return when the element havn't contain datafilter tag.
     */
    public static void setDataFilter(String datafilter, MDMConnection element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.DATA_FILTER, datafilter);
    }

    /**
     * DOC xqliu Comment method "setUniverse".
     * 
     * @param universe
     * @param element
     */
    public static void setUniverse(String universe, MDMConnection element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.UNIVERSE, universe);
    }

    /**
     * DOC xqliu Comment method "getUniverse".
     * 
     * @param element
     * @return
     */
    public static String getUniverse(Connection element) {
        MDMConnection mdmConnection = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(element);
        if (mdmConnection != null) {
            return getUniverse(mdmConnection);
        }
        return "";
    }

    /**
     * DOC xqliu Comment method "setUniverse".
     * 
     * @param universe
     * @param element
     */
    public static void setUniverse(String universe, Connection element) {
        MDMConnection mdmConnection = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(element);
        if (mdmConnection != null) {
            setUniverse(universe, mdmConnection);
        }
    }

    // MOD klliu 2010-10-09 feature 15821
    /**
     * 
     * DOC klliu Comment method "getOtherParameter".
     * 
     * @return
     */
    public static String getOtherParameter(ModelElement element) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.OTHER_PARAMETER, element.getTaggedValue());
        if (tv == null) {
            return "";
        }
        return tv.getValue();
    }

    /**
     * 
     * DOC klliu Comment method "setOtherParameter".
     * 
     * @param otherParameter
     */
    public static void setOtherParameter(String otherParameter, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.OTHER_PARAMETER, otherParameter);
    }

    /**
     * DOC xqliu Comment method "setRetrieveAllMetadata". ADD xqliu 2010-03-03 feature 11412
     * 
     * @param retrieveAllMetadata
     * @param element
     * @deprecated don't use TaggedValue any more
     */
    @Deprecated
    public static void setRetrieveAllMetadata(boolean retrieveAllMetadata, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.RETRIEVE_ALL, String.valueOf(retrieveAllMetadata));
    }

    /**
     * if the connection has sid return false, else return true (don't need the TaggedValue any more)
     * 
     * @param element
     * @return
     */
    public static boolean getRetrieveAllMetadata(ModelElement element) {
        if (element != null && element instanceof Connection) {
            if (element instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) element;
                String sid = dbConn.getSID();
                if (sid != null && !"".equals(sid.trim())) {
                    // MOD klliu bug 22900
                    TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.RETRIEVE_ALL,
                            element.getTaggedValue());
                    // if connection is created by 4.2 or 5.0 ,the tagedValue(RETRIEVE_ALL) has been removed.
                    if (taggedValue != null) {
                        String value = taggedValue.getValue();
                        if (value.equals("true")) {
                            return true;
                        }
                    }
                    // ~
                    if (isOracle(dbConn) || isPostgresql(dbConn)) {
                        String uiSchema = dbConn.getUiSchema();
                        if (uiSchema != null && !"".equals(uiSchema.trim())) {
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else if (element instanceof MDMConnection) {
                MDMConnection mdmConn = (MDMConnection) element;
                String context = mdmConn.getContext();
                if (context != null && !"".equals(context.trim())) {
                    return false;
                } else {
                    return true;
                }
            } else if (element instanceof FileConnection) {
                // do file connection can filter catalog/schema?
                return true;
            }
        }
        return true;
        // TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.RETRIEVE_ALL,
        // element.getTaggedValue());
        // if (taggedValue == null) {
        // return true;
        // }
        // return Boolean.valueOf(taggedValue.getValue());
    }

    /**
     * DOC bZhou Comment method "isJDBC".
     * 
     * @param connection
     * @return
     */
    public static boolean isJDBC(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType();
            return databaseType.equals("General JDBC");
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isOracle".
     * 
     * @param connection
     * @return
     */
    public static boolean isOracle(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("oracle"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isMysql".
     * 
     * @param connection
     * @return
     */
    public static boolean isMysql(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("mysql"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isMssql".
     * 
     * @param connection
     * @return
     */
    public static boolean isMssql(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("sql server"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isPostgresql".
     * 
     * @param connection
     * @return
     */
    public static boolean isPostgresql(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("postgresql"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isInformix".
     * 
     * @param connection
     * @return
     */
    public static boolean isInformix(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("informix"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isIngress".
     * 
     * @param connection
     * @return
     */
    public static boolean isIngress(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("ingres"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isDb2".
     * 
     * @param connection
     * @return
     */
    public static boolean isDb2(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("db2"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isSybase".
     * 
     * @param connection
     * @return
     */
    public static boolean isSybase(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("sybase"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isTeradata".
     * 
     * @param connection
     * @return
     */
    public static boolean isTeradata(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("teradata"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * DOC xqliu Comment method "isNetezza".
     * 
     * @param connection
     * @return
     */
    public static boolean isNetezza(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return databaseType.toLowerCase().contains("netezza"); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * 
     * judge if it is hive connection by DatabaseType.
     * 
     * @param connection
     * @return
     */
    public static boolean isHive(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return "Hive".equalsIgnoreCase(databaseType) || "Impala".equalsIgnoreCase(databaseType); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return false;
    }

    public static boolean isVertica(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return "Vertica".equalsIgnoreCase(databaseType); //$NON-NLS-1$
        }
        return false;
    }

    public static boolean isRedshift(Connection connection) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(connection);
        if (dbConn != null) {
            String databaseType = dbConn.getDatabaseType() == null ? "" : dbConn.getDatabaseType(); //$NON-NLS-1$
            return "Redshift".equalsIgnoreCase(databaseType); //$NON-NLS-1$
        }
        return false;
    }

    /**
     * Gets the metadata table by id from connection. Created by Marvin Wang on May 8, 2012.
     * 
     * @param connection
     * @param id
     * @return the metadata table if found, otherwise, return <code>null</code>.
     */
    public static MetadataTable getTableById(Connection connection, String id) {
        Set<MetadataTable> tables = getTables(connection);
        if (tables != null && tables.size() > 0) {
            for (MetadataTable metadataTable : tables) {
                if (metadataTable != null && id.equals(metadataTable.getId())) {
                    return metadataTable;
                }
            }
        }
        return null;
    }

    /**
     * return a set of all MetadataTable linked to this connection by inspecting through all the connection Package and
     * sub-packages
     * 
     * @param connection the connection to find the related table
     * @return a set of tables.
     */
    public static Set<MetadataTable> getTables(Connection connection) {
        HashSet<MetadataTable> result = new HashSet<MetadataTable>();
        if (connection instanceof SalesforceSchemaConnection) {
            SalesforceSchemaConnection salesforceConnection = (SalesforceSchemaConnection) connection;
            List<SalesforceModuleUnit> units = salesforceConnection.getModules();
            for (SalesforceModuleUnit unit : units) {
                for (MetadataTable table : new ArrayList<MetadataTable>(unit.getTables())) {
                    result.add(table);
                }
            }
            return result;
        }

        if (connection instanceof SAPConnection) {
            EList<Package> packages = connection.getDataPackage();
            for (Package pack : new ArrayList<Package>(packages)) {
                PackageHelper.getAllTables(pack, result);
            }
            result.addAll(((SAPConnection) connection).getBWAdvancedDataStoreObjects());
            result.addAll(((SAPConnection) connection).getBWDataSources());
            result.addAll(((SAPConnection) connection).getBWDataStoreObjects());
            result.addAll(((SAPConnection) connection).getBWInfoCubes());
            result.addAll(((SAPConnection) connection).getBWInfoObjects());
            return result;
        }
        // else if (connection instanceof SAPConnection) {
        // SAPConnection sapConnection = (SAPConnection) connection;
        // final EList<SAPFunctionUnit> funtions = sapConnection.getFuntions();
        // for (SAPFunctionUnit unit : new ArrayList<SAPFunctionUnit>(funtions)) {
        // // add tables from function
        // result.addAll(unit.getTables());
        // }
        // // add sap table
        // EList<Package> packages = connection.getDataPackage();
        // for (Package pack : new ArrayList<Package>(packages)) {
        // PackageHelper.getAllTables(pack, result);
        // }
        // return result;
        // }

        if (connection != null) {
            EList<Package> packages = connection.getDataPackage();
            for (Package pack : new ArrayList<Package>(packages)) {
                PackageHelper.getAllTables(pack, result);
            }
        }
        return result;
    }

    public static Set<MetadataTable> getTables(Connection connection, SAPFunctionUnit functionUnit) {
        HashSet<MetadataTable> result = new HashSet<MetadataTable>();
        if (functionUnit == null) {
            return result;
        }
        if (connection instanceof SAPConnection) {
            SAPConnection sapConnection = (SAPConnection) connection;
            final EList<SAPFunctionUnit> funtions = sapConnection.getFuntions();
            for (SAPFunctionUnit unit : funtions) {
                if (functionUnit.getLabel() != null && functionUnit.getLabel().equals(unit.getLabel())) {
                    result.addAll(unit.getInputTables());
                    result.addAll(unit.getTables());
                }
            }
            return result;
        }

        return result;
    }

    public static List<MetadataTable> getTablesWithOrders(Connection connection) {
        ArrayList<MetadataTable> result = new ArrayList<MetadataTable>();
        EList<Package> packages = connection.getDataPackage();
        for (Package pack : packages) {
            PackageHelper.getAllTablesWithOrders(pack, result);
        }
        return result;
    }

    /**
     * return the list of schemas related to a Connectio, it is look for direct Schema and all the potential Schema
     * owned by a Schema.
     * 
     * @param connection, the connection to look for schemas
     * @return Set of unique Schemas related to the connection
     */
    public static Set<Schema> getAllSchemas(Connection connection) {
        return getAllDataPackages(connection, Schema.class);
    }

    /**
     * return the list of Catalogs related to a Connectio, it is look for direct Catalog and all the potential Catalog
     * owned by a Catalog.
     * 
     * @param connection, the connection to look for Catalogs
     * @return Set of unique Catalogs related to the connection
     */
    public static Set<Catalog> getAllCatalogs(Connection connection) {
        return getAllDataPackages(connection, Catalog.class);
    }

    // hywang
    public static Package getPackage(String name, Connection connection, Class theClass) {
        Set<Package> allCatalogs = getAllDataPackages(connection, theClass);
        Iterator<Package> it = allCatalogs.iterator();
        while (it.hasNext()) {
            Package current = it.next();
            if (current.getName() != null && current.getName().equals(name)) {
                return current;
            }
        }
        return null;
    }

    /**
     * get all the packages and their sub(owned) packages of the connection.
     * 
     * @param connection the connection that refers to the packages
     * @return the list of TdTables found in the given list (never null, but can be empty).
     */
    @SuppressWarnings("unchecked")
    public static <T extends Package> Set<T> getAllDataPackages(Connection connection, Class<T> theClass) {
        assert connection != null;
        assert theClass != null;
        Set<T> result = new HashSet<T>(connection.getDataPackage().size());
        for (Package pack : connection.getDataPackage()) {
            if (pack != null && theClass.isAssignableFrom(pack.getClass())) {
                result.add((T) pack);
                // get all packages owned by this package
                result.addAll(PackageHelper.getOwnedElements(pack, theClass));
            }
        }
        return result;
    }

    /**
     * DOC xqliu Comment method "getDecryptPassword".
     * 
     * @param password
     * @return
     */
    public static String getDecryptPassword(String password) {
        return CryptoHelper.getDefault().decrypt(password);
    }

    /**
     * DOC xqliu Comment method "getEncryptPassword".
     * 
     * @param password
     * @return
     */
    public static String getEncryptPassword(String password) {
        return CryptoHelper.getDefault().encrypt(password);
    }

    /**
     * DOC bZhou Comment method "setAuthor".
     * 
     * @param element
     * @param author
     * @return
     */
    public static boolean setAuthor(String author, ModelElement element) {
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.AUTHOR, author);
    }

    /**
     * DOC bZhou Comment method "getAuthor".
     * 
     * @param element
     * @return
     */
    public static String getAuthor(ModelElement element) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.AUTHOR, element.getTaggedValue());
        if (tv == null) {
            return "";//$NON-NLS-1$
        }
        return tv.getValue();
    }

    /**
     * Method "setDevStatus" sets the development status of the given element.
     * 
     * @param element
     * @param status the state to set.
     * @return
     */
    public static boolean setDevStatus(String statusLabel, ModelElement element) {
        // MOD mzhao feature 7479 2009-10-16
        if (statusLabel == null) {
            return false;
        }
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.DEV_STATUS, statusLabel);
    }

    /**
     * Method "getDevStatus". MOD mzhao feature 7479
     * 
     * @param element such as Analysis, DataProvider...
     * @return the development status of the element
     */
    public static String getDevStatus(ModelElement element) {
        TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DEV_STATUS, element.getTaggedValue());
        if (taggedValue == null) {
            return "";//$NON-NLS-1$
        }
        String statusValueString = taggedValue.getValue();
        return statusValueString;
    }

    /**
     * Method "setVersion" sets the version of the given element.
     * 
     * @param version the version to set
     * @param element the element
     * @return true if the value was not set before.
     */
    public static boolean setVersion(String version, ModelElement element) {
        String statusStr = String.valueOf(version);
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.VERSION, statusStr);
    }

    /**
     * Method "getVersion".
     * 
     * @param element
     * @return the version of the element
     */
    public static String getVersion(ModelElement element) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.VERSION, element.getTaggedValue());
        if (tv == null) {
            return VersionUtils.DEFAULT_VERSION;
        }
        return tv.getValue();
    }

    /**
     * Method "setPurpose".
     * 
     * @param purpose the purpose to set or create
     * @param element a CWM element
     */
    public static void setPurpose(String purpose, ModelElement element) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.PURPOSE, purpose);
    }

    /**
     * Method "getPurpose".
     * 
     * @param element a CWM element
     * @return the purpose or null
     */
    public static String getPurpose(ModelElement element) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.PURPOSE, element.getTaggedValue());
        if (tv == null) {
            return "";//$NON-NLS-1$
        }
        return tv.getValue();
    }

    /**
     * DOC bZhou Comment method "getDescription".
     * 
     * @param element
     * @return
     */
    public static String getDescription(ModelElement element) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DESCRIPTION, element.getTaggedValue());
        if (tv == null) {
            return "";//$NON-NLS-1$
        }
        return tv.getValue();
    }

    /**
     * DOC bZhou Comment method "setDescription".
     * 
     * @param description
     * @param element
     * @return
     */
    public static boolean setDescription(String description, ModelElement element) {
        return TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.DESCRIPTION, description);
    }

    /**
     * DOC xqliu Comment method "setServerName".
     * 
     * @param conn
     * @param serverName
     */
    public static void setServerName(Connection conn, String serverName) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setServerName(serverName);
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            mdmConn.setServer(serverName);
        }
    }

    /**
     * DOC xqliu Comment method "setPort".
     * 
     * @param conn
     * @param port
     */
    public static void setPort(Connection conn, String port) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setPort(port);
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            mdmConn.setPort(port);
        }
    }

    /**
     * DOC xqliu Comment method "setSID".
     * 
     * @param conn
     * @param sid
     */
    public static void setSID(Connection conn, String sid) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setSID(sid);
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            mdmConn.setContext(sid);
        }
    }

    /**
     * DOC xqliu Comment method "setURL".
     * 
     * @param conn
     * @param url
     */
    public static void setURL(Connection conn, String url) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setURL(url);
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            mdmConn.setPathname(url);
        }
    }

    /**
     * DOC xqliu Comment method "setUsername".
     * 
     * @param conn
     * @param username
     */
    public static void setUsername(Connection conn, String username) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setUsername(username);
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            mdmConn.setUsername(username);
        }
    }

    /**
     * DOC xqliu Comment method "setPassword".
     * 
     * @param conn
     * @param password
     */
    public static void setPassword(Connection conn, String password) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            dbConn.setRawPassword(password);
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            mdmConn.setPassword(mdmConn.getValue(password, true));
        }
    }

    /**
     * zshen Comment method "getPassword".
     * 
     * @param conn
     * @param password
     */
    public static String getPassword(Connection conn) {
        DatabaseConnection dbConn = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (dbConn != null) {
            return dbConn.getRawPassword();
        }
        MDMConnection mdmConn = SwitchHelpers.MDMCONNECTION_SWITCH.doSwitch(conn);
        if (mdmConn != null) {
            return mdmConn.getValue(mdmConn.getPassword(), false);
        }
        return null;
    }

    /**
     * 
     * DOC qiongli Comment method "getTdDataProvider".
     * 
     * @param column
     * @return
     */
    public static Connection getTdDataProvider(MetadataColumn column) {
        MetadataTable mTable = ColumnHelper.getColumnOwnerAsMetadataTable(column);
        if (mTable == null) {
            return null;
        }
        return getTdDataProvider(mTable);
    }

    /**
     * 
     * DOC qiongli Comment method "getTdDataProvider".
     * 
     * @param mTable
     * @return
     */
    public static Connection getTdDataProvider(MetadataTable mTable) {
        Package parentCatalogOrSchema = ColumnSetHelper.getParentCatalogOrSchema(mTable);
        // DelimitedFile conn doesn't have Catalog and Schema.
        if (parentCatalogOrSchema == null) {
            Package thePackage = null;
            if (mTable != null && mTable.getNamespace() != null) {
                if (mTable.getNamespace() instanceof RecordFile) {
                    thePackage = (RecordFile) mTable.getNamespace();
                }
            }
            if (thePackage == null) {
                return null;
            }
            return getTdDataProvider(thePackage);
        } else {
            return getTdDataProvider(parentCatalogOrSchema);
        }
    }

    // Added yyin 20121203 TDQ-6497 use "IS_DB_NEED_RELOAD" to replace "USING_URL"
    public static boolean setIsConnNeedReload(Connection conn, boolean isNeedReload) {
        return TaggedValueHelper.setTaggedValue(conn, TaggedValueHelper.IS_CONN_NEED_RELOAD, String.valueOf(isNeedReload));
    }

    public static boolean getIsConnNeedReload(Connection conn) {
        return TaggedValueHelper.getValueBoolean(TaggedValueHelper.IS_CONN_NEED_RELOAD, conn);

    }

    // ~

    /**
     * Compares this Using URL tagged value to the specified connection. The result is true if and only if the url is
     * not equal with the tagged value. yyi 2011-04-14
     * 
     * @param conn
     * @return <code>true</code> if the <code>Url </code>are not equal; <code>false</code> otherwise.
     */
    public static boolean isUrlChanged(Connection conn) {

        DatabaseConnection connection1 = SwitchHelpers.DATABASECONNECTION_SWITCH.doSwitch(conn);
        if (connection1 != null) {
            return getIsConnNeedReload(connection1);
        }
        return false;
    }

    /**
     * 
     * Get "filter" attribute in data base connection file.
     * 
     * @return
     */
    public static String getPackageFilter(ModelElement element) {
        TaggedValue tv = TaggedValueHelper.getTaggedValue(TaggedValueHelper.PACKAGE_FILTER, element.getTaggedValue());
        if (tv == null) {
            return "";//$NON-NLS-1$
        }
        return tv.getValue();
    }

    /**
     * 
     * Add "filter" attribute to data base connection file.
     * 
     * @param otherParameter
     */
    public static void setPackageFilter(ModelElement element, String packageFilter) {
        TaggedValueHelper.setTaggedValue(element, TaggedValueHelper.PACKAGE_FILTER, packageFilter);
    }
}
