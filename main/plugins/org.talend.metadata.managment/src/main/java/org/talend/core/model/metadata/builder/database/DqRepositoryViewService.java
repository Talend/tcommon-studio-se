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
package org.talend.core.model.metadata.builder.database;

import java.io.UnsupportedEncodingException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import metadata.managment.i18n.Messages;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.database.ExtractMetaDataFromDataBase.ETableTypes;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.impl.ProjectReferenceImpl;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ColumnSetHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdTable;
import org.talend.cwm.relational.TdView;
import org.talend.cwm.xml.TdXmlElementType;
import org.talend.metadata.managment.model.MetadataFillFactory;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.utils.sql.ConnectionUtils;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.objectmodel.core.TaggedValue;
import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.ColumnSet;
import orgomg.cwm.resource.relational.Schema;

/**
 * @author scorreia
 * 
 * Services for the DQ Repository view.
 */
public final class DqRepositoryViewService {

    private static Logger log = Logger.getLogger(DqRepositoryViewService.class);

    private static final SimpleDateFormat SMPL_DATE_FMT = new SimpleDateFormat("yyyyMMddhhmmss"); //$NON-NLS-1$

    /**
     * if true, the catalogs (and schemas) are stored in the same file as the data provider. Used for tests only.
     * 
     * TODO scorreia (saving catalog outside data provider's file) set it to false for big databases.
     * 
     * In case when optimization is needed: set this boolean to false and correct code so that everything works as
     * before (DQ Repository view must not show catalog's files and Catalogs must still be children of the Data
     * provider). Check also that old files (.prv) are still readable by the application.
     */

    private DqRepositoryViewService() {
    }

    private static final char CHAR_TO_REMOVE = '/';

    private static final char REPLACEMENT_CHAR = '_';

    public static final String[] TABLE_TYPES = new String[] { ETableTypes.TABLETYPE_TABLE.getName(),
            ETableTypes.EXTERNAL_TABLE.getName(), ETableTypes.MANAGED_TABLE.getName(), ETableTypes.INDEX_TABLE.getName(),
            ETableTypes.SYSTEM_TABLE.getName() };

    public static final String[] VIEW_TYPES = new String[] { ETableTypes.TABLETYPE_VIEW.getName(),
            ETableTypes.VIRTUAL_VIEW.getName() };

    /**
     * Method "createTechnicalName" creates a technical name used for file system storage. MOD mzhao make this method as
     * public access.
     * 
     * @param functionalName the user friendly name
     * @return the technical name created from the user given name.
     */
    public static String createTechnicalName(final String functionalName) {
        String techname = "no_name"; //$NON-NLS-1$
        if (functionalName == null) {
            log.warn(Messages.getString("DqRepositoryViewService.NONE_NULL_FUNCTION_NAME")); //$NON-NLS-1$
            return techname;
        }
        // encode in base 64 so that all characters such white spaces, accents,
        // everything that is dangerous when used
        // for file names are removed
        try {
            // encode
            String b64 = new String(Base64.encodeBase64(functionalName.getBytes()), "UTF-8"); //$NON-NLS-1$
            // replace special characters
            String date = SMPL_DATE_FMT.format(new Date(System.currentTimeMillis()));
            techname = b64.replace(CHAR_TO_REMOVE, REPLACEMENT_CHAR) + date;
        } catch (UnsupportedEncodingException e) {
            log.error(e, e);
        } // .replaceAll(B64ID, PREFIX);
        if (log.isDebugEnabled()) {
            log.debug("Functional name: " + functionalName + " -> techname: " + techname); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return techname;
    }

    /**
     * Method "refreshDataProvider" reload database structure. Existing elements (catalogs, tables...) must not be
     * replaced by new elements. Only their content must be updated because these elements can be refered to by
     * analysis.
     * 
     * @param dataProvider
     * @param catalogPattern the catalogs to load (can be null, meaning all are loaded)
     * @param schemaPattern the schema to load (can be null, meaning all are loaded)
     * @return true if the catalog have been reload
     */
    public static boolean refreshDataProvider(Connection dataProvider, String catalogPattern, String schemaPattern) {
        // TODO scorreia implement me
        return false;
    }

    /**
     * DOC scorreia Comment method "refreshTables".
     * 
     * @param schema
     * @param tablePattern
     * @return
     */
    public static boolean refreshTables(Package schema, String tablePattern) {
        // TODO scorreia implement me
        return false;
    }

    /**
     * DOC scorreia Comment method "refreshViews".
     * 
     * @param schema
     * @param viewPattern
     * @return
     */
    public static boolean refreshViews(Package schema, String viewPattern) {
        // TODO scorreia implement me
        return false;
    }

    /**
     * DOC scorreia Comment method "refreshColumns".
     * 
     * @param table
     * @param columnPattern
     * @return
     */
    public static boolean refreshColumns(ColumnSet table, String columnPattern) {
        // TODO scorreia implement me
        return false;
    }

    /**
     * Method "getTables" loads the tables from the database or get the tables from the catalog .
     * 
     * @param dataProvider the data provider
     * @param catalog the catalog (must not be null)
     * @param tablePattern the pattern of the tables to be loaded (from the DB)
     * @param loadFromDB true if we want to load tables from the database. false when the tables are already in the
     * catalog.
     * @return the list of tables. Theses tables are not added to the given catalog. It must be done by the caller.
     * @throws Exception
     */
    public static List<TdTable> getTables(Connection dataProvider, Catalog catalog, String tablePattern, boolean loadFromDB,
            boolean isPersist2Con) throws Exception {
        TypedReturnCode<java.sql.Connection> trc = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = trc.getObject();
        try {
            return getTables(connection, dataProvider, catalog, tablePattern, loadFromDB, isPersist2Con);
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    public static List<TdTable> getTables(java.sql.Connection sqlConnection, Connection dataProvider, Catalog catalog,
            String tablePattern, boolean loadFromDB, boolean isPersist2Con) throws Exception {
        if (loadFromDB && !(isComeFromRefrenceProject(dataProvider) && isPersist2Con)) {
            return loadTables(sqlConnection, dataProvider, catalog, tablePattern, isPersist2Con);
        } else {
            return CatalogHelper.getTables(catalog);
        }
    }

    public static List<TdTable> getTables(Connection dataProvider, Schema schema, String tablePattern, boolean loadFromDB,
            boolean isPersist2Con) throws Exception {
        TypedReturnCode<java.sql.Connection> trc = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = trc.getObject();
        try {
            return getTables(connection, dataProvider, schema, tablePattern, loadFromDB, isPersist2Con);
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    /**
     * get the tables belong to the schema.
     * 
     * @param sqlConnection the java.sql.Connection
     * @param dataProvider the talend Connection
     * @param schema the schema
     * @param tablePattern table pattern
     * @param loadFromDB load form db or not
     * @return
     * @throws Exception
     */
    public static List<TdTable> getTables(java.sql.Connection sqlConnection, Connection dataProvider, Schema schema,
            String tablePattern, boolean loadFromDB, boolean isPersist2Con) throws Exception {
        if (loadFromDB && !(isComeFromRefrenceProject(dataProvider) && isPersist2Con)) {
            final Catalog parentCatalog = CatalogHelper.getParentCatalog(schema);
            return loadTables(sqlConnection, dataProvider, parentCatalog, schema, tablePattern, isPersist2Con);
        } else {
            return SchemaHelper.getTables(schema);
        }
    }

    public static List<TdTable> getTables(Connection dataProvider, Package pack, String tablePattern, boolean loadFromDB,
            boolean isPersist2Con) throws Exception {
        TypedReturnCode<java.sql.Connection> trc = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = trc.getObject();
        try {
            return getTables(connection, dataProvider, pack, tablePattern, loadFromDB, isPersist2Con);
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    public static List<TdTable> getTables(java.sql.Connection sqlConnection, Connection dataProvider, Package pack,
            String tablePattern, boolean loadFromDB, boolean isPersist2Con) throws Exception {
        if (pack instanceof Schema) {
            return getTables(sqlConnection, dataProvider, (Schema) pack, tablePattern, loadFromDB, isPersist2Con);
        } else if (pack instanceof Catalog) {
            return getTables(sqlConnection, dataProvider, (Catalog) pack, tablePattern, loadFromDB, isPersist2Con);
        }
        return new ArrayList<TdTable>();
    }

    public static List<TdView> getViews(Connection dataProvider, Catalog catalog, String viewPattern, boolean loadFromDB,
            boolean isPersist2Con) throws Exception {
        TypedReturnCode<java.sql.Connection> trc = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = trc.getObject();
        try {
            return getViews(connection, dataProvider, catalog, viewPattern, loadFromDB, isPersist2Con);
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    public static List<TdView> getViews(java.sql.Connection sqlConnection, Connection dataProvider, Catalog catalog,
            String viewPattern, boolean loadFromDB, boolean isPersist2Con) throws Exception {
        if (loadFromDB && !(isComeFromRefrenceProject(dataProvider) && isPersist2Con)) {
            return loadViews(sqlConnection, dataProvider, catalog, null, viewPattern, isPersist2Con);
        } else {
            return CatalogHelper.getViews(catalog);
        }
    }

    public static List<TdView> getViews(Connection dataProvider, Schema schema, String viewPattern, boolean loadFromDB,
            boolean isPersist2Con) throws Exception {
        TypedReturnCode<java.sql.Connection> trc = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = trc.getObject();
        try {
            return getViews(connection, dataProvider, schema, viewPattern, loadFromDB, isPersist2Con);
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    public static List<TdView> getViews(java.sql.Connection sqlConnection, Connection dataProvider, Schema schema,
            String viewPattern, boolean loadFromDB, boolean isPersist2Con) throws Exception {
        if (loadFromDB && !(isComeFromRefrenceProject(dataProvider) && isPersist2Con)) {
            // get catalog is exists
            final Catalog parentCatalog = CatalogHelper.getParentCatalog(schema);
            return loadViews(sqlConnection, dataProvider, parentCatalog, schema, viewPattern, isPersist2Con);
        } else {
            return SchemaHelper.getViews(schema);
        }
    }

    public static List<TdView> getViews(Connection dataProvider, Package pack, String viewPattern, boolean loadFromDB,
            boolean isPersist2Con) throws Exception {
        TypedReturnCode<java.sql.Connection> trc = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = trc.getObject();
        try {
            return getViews(connection, dataProvider, pack, viewPattern, loadFromDB, isPersist2Con);
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    public static List<TdView> getViews(java.sql.Connection sqlConnection, Connection dataProvider, Package pack,
            String viewPattern, boolean loadFromDB, boolean isPersist2Con) throws Exception {
        if (pack instanceof Schema) {
            return getViews(sqlConnection, dataProvider, (Schema) pack, viewPattern, loadFromDB, isPersist2Con);
        } else if (pack instanceof Catalog) {
            return getViews(sqlConnection, dataProvider, (Catalog) pack, viewPattern, loadFromDB, isPersist2Con);
        }
        return new ArrayList<TdView>();
    }

    /**
     * @param dataProvider the data provider for connecting to database (can be null when the columns are not loaded
     * from the database)
     * @param columnSet a column set (Table or View)
     * @param loadFromDB true if columns must be loaded from the database
     * @return
     * @throws Exception the exception the connection checking is not ok.
     */
    public static List<TdColumn> getColumns(Connection dataProvider, ColumnSet columnSet, boolean loadFromDB) throws Exception {
        return getColumns(dataProvider, columnSet, loadFromDB, true);
    }

    /**
     * @param dataProvider the data provider for connecting to database (can be null when the columns are not loaded
     * from the database)
     * @param columnSet a column set (Table or View)
     * @param loadFromDB true if columns must be loaded from the database
     * @param link2Conn true if need to link columns on the columnSet
     * @return
     * @throws Exception the exception the connection checking is not ok.
     */
    public static List<TdColumn> getColumns(Connection dataProvider, ColumnSet columnSet, boolean loadFromDB, boolean link2Conn)
            throws Exception {
        if (loadFromDB && !isComeFromRefrenceProject(dataProvider)) {
            // MOD by zshen use new API to fill Columns
            TypedReturnCode<java.sql.Connection> rcConn = MetadataConnectionUtils
                    .createConnection((DatabaseConnection) dataProvider);
            java.sql.Connection connection = rcConn.getObject();
            try {
                DatabaseMetaData dm = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(connection,
                        (DatabaseConnection) dataProvider);
                // never change old connection model
                if (!link2Conn) {
                    MetadataFillFactory.getDBInstance(dataProvider).setLinked(false);
                }
                List<TdColumn> fillColumns = MetadataFillFactory.getDBInstance(dataProvider).fillColumns(columnSet, dm, null,
                        null);
                MetadataFillFactory.getDBInstance(dataProvider).setLinked(true);
                return fillColumns;
            } finally {
                if (connection != null) {
                    ConnectionUtils.closeConnection(connection);
                }
            }
            // ~
            // return loadColumns(dataProvider, columnSet, columnPattern);
        } else {
            return ColumnSetHelper.getColumns(columnSet);
        }
    }

    /**
     * DOC zshen Comment method "isComeFromRefrenceProject".
     * 
     * @return
     */
    public static boolean isComeFromRefrenceProject(ModelElement modelElement) {
        String currentResourceProjectName = null;
        try {
            currentResourceProjectName = modelElement.eResource().getURI().segment(1);
        } catch (NullPointerException e) {
            return false;
        }
        return isComeFromRefrenceProject(currentResourceProjectName);
    }

    /**
     * DOC zshen Comment method "isComeFromRefrenceProject".
     * 
     * @return
     */
    public static boolean isComeFromRefrenceProject(Property property) {
        String currentResourceProjectName = null;
        try {
            currentResourceProjectName = property.getItem().eResource().getURI().segment(1);
        } catch (NullPointerException e) {
            return false;
        }
        return isComeFromRefrenceProject(currentResourceProjectName);
    }

    /**
     * DOC zshen Comment method "isComeFromRefrenceProject".
     * 
     * @return
     */
    public static boolean isComeFromRefrenceProject(String currentResourceProjectName) {
        RepositoryContext repositoryContext = (RepositoryContext) org.talend.core.runtime.CoreRuntimePlugin.getInstance()
                .getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY);
        if (repositoryContext == null) {
            return false;
        }
        Project project = repositoryContext.getProject();
        if (project == null) {
            return false;
        }
        boolean isLocal = project.getEmfProject().isLocal();
        boolean isReference = project.getEmfProject().isReference();
        if (isLocal || isReference) {
            return false;
        }
        EList<ProjectReferenceImpl> referencedProjects = project.getEmfProject().getReferencedProjects();
        if (referencedProjects.size() > 0) {
            boolean isFromReferenceProject = iteration2FindReferenceProject(referencedProjects, currentResourceProjectName);
            if (isFromReferenceProject) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOC zshen Comment method "iteration2FindReferenceProject".
     * 
     * @param projectRef
     * @param currentResourceProjectName
     */
    private static boolean iteration2FindReferenceProject(EList<ProjectReferenceImpl> referencedProjects,
            String currentResourceProjectName) {
        for (ProjectReferenceImpl currprojectRef : referencedProjects) {
            String label = currprojectRef.getReferencedProject().getLabel();
            if (StringUtils.equalsIgnoreCase(currentResourceProjectName, label)) {
                return true;
            }
            boolean isFromReferenceProject = iteration2FindReferenceProject(currprojectRef.getReferencedProject()
                    .getReferencedProjects(), currentResourceProjectName);
            if (isFromReferenceProject) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method "getColumns". The link between the column set and its columns is set in this method when required.
     * 
     * @param dataProvider the data provider for connecting to database (can be null when the columns are not loaded
     * from the database)
     * @param columnSet a column set (Table or View)
     * @param columnPattern the pattern of the columns to get (can be null)
     * @param loadFromDB true if columns must be loaded from the database
     * @return
     * @throws Exception
     * @Deprecated use {@link DqRepositoryViewService#getColumns(Connection, ColumnSet, boolean)}
     */
    @Deprecated
    public static List<TdColumn> getColumns(Connection dataProvider, ColumnSet columnSet, String columnPattern, boolean loadFromDB)
            throws Exception {
        return getColumns(dataProvider, columnSet, loadFromDB);
    }

    /**
     * Method "createFilename".
     * 
     * @param folder the folder
     * @param basename the filename without extension
     * @param extension the extension of the file
     * @return the path "folder/basename.extension"
     */
    public static String createFilename(String basename, String extension) {
        return createTechnicalName(basename) + "." + extension;//$NON-NLS-1$
    }

    /**
     * Method "loadTables".
     * 
     * @param sqlConnection
     * @param dataProvider
     * @param catalog (must not be null)
     * @param tablePattern
     * @return the list of tables matching the given pattern
     * @throws Exception
     */
    private static List<TdTable> loadTables(java.sql.Connection sqlConnection, Connection dataProvider, Catalog catalog,
            String tablePattern, boolean isPersist2Con) throws Exception {
        List<TdTable> tables = new ArrayList<TdTable>();
        assert dataProvider != null;
        assert catalog != null;

        String catalogName = catalog.getName();
        if (catalogName == null) {
            log.error(Messages.getString("DqRepositoryViewService.NO_CATALOGS")); //$NON-NLS-1$
            return tables;
        }
        return loadTables(sqlConnection, dataProvider, catalog, null, tablePattern, isPersist2Con);
    }

    private static List<TdTable> loadTables(java.sql.Connection sqlConnection, Connection dataProvider, Catalog catalog,
            Schema schema, String tablePattern, boolean isPersist2Con) throws Exception {
        List<TdTable> tables = new ArrayList<TdTable>();
        DatabaseMetaData dm = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConnection,
                (DatabaseConnection) dataProvider, false);
        Package pack = schema == null ? catalog : schema;
        MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(dataProvider);

        if (isPersist2Con) {
            // will persistence to connection item
            tables = dbInstance.fillTables(pack, dm, null, tablePattern, TABLE_TYPES);
        } else {
            // will not persistence to connection item
            dbInstance.setLinked(false);
            tables = dbInstance.fillTables(pack, dm, null, tablePattern, TABLE_TYPES);
            dbInstance.setLinked(true);
        }
        return tables;
    }

    private static List<TdView> loadViews(java.sql.Connection sqlConnection, Connection dataProvider, Catalog catalog,
            Schema schema, String viewPattern, boolean isPersist2Con) throws Exception {
        List<TdView> views = new ArrayList<TdView>();
        DatabaseMetaData dm = ExtractMetaDataUtils.getInstance().getDatabaseMetaData(sqlConnection,
                (DatabaseConnection) dataProvider, false);
        Package pack = schema == null ? catalog : schema;
        MetadataFillFactory dbInstance = MetadataFillFactory.getDBInstance(dataProvider);

        if (isPersist2Con) {
            // will persistence to connection item
            views = dbInstance.fillViews(pack, dm, null, viewPattern, VIEW_TYPES);
        } else {
            // will not persistence to connection item
            dbInstance.setLinked(false);
            views = dbInstance.fillViews(pack, dm, null, viewPattern, VIEW_TYPES);
            dbInstance.setLinked(true);
        }
        return views;
    }

    /**
     * Method "readFromFile".
     * 
     * @param file the file to read
     * @return the Data provider if found.
     * @deprecated use repository API or use resourceFileMap instead it
     */
    @Deprecated
    public static TypedReturnCode<Connection> readFromFile(IFile file) throws NoSuchElementException {
        TypedReturnCode<Connection> rc = new TypedReturnCode<Connection>();

        URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), false);
        Resource r = ProxyRepositoryFactory.getInstance().getRepositoryFactoryFromProvider().getResourceManager().resourceSet
                .getResource(uri, true);
        Collection<Connection> tdDataProviders = ConnectionHelper.getTdDataProviders(r.getContents());
        if (tdDataProviders.isEmpty()) {
            rc.setReturnCode(
                    Messages.getString("DqRepositoryViewService.NoDataProviderFound", file.getFullPath().toString()), false); //$NON-NLS-1$
        } else if (tdDataProviders.size() > 1) {
            rc.setReturnCode(StringUtils.EMPTY, false);
        } else {
            Connection prov = tdDataProviders.iterator().next();
            rc.setObject(prov);
        }
        return rc;
    }

    /**
     * DOC bZhou Comment method "getAllRepositoryResourceObjects".
     * 
     * Use this method to get all repository view objects used in TDQ Repository.
     * 
     * @param withDeleted
     * @return
     * @throws PersistenceException
     */
    public static List<IRepositoryViewObject> getAllRepositoryResourceObjects(boolean withDeleted) throws PersistenceException {
        List<IRepositoryViewObject> allObjectList = new ArrayList<IRepositoryViewObject>();

        for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            if (type.isDQItemType() && type.isResourceItem()) {
                allObjectList.addAll(ProxyRepositoryFactory.getInstance().getAll(type, withDeleted));
            }
        }

        return allObjectList;
    }

    /**
     * DOC bZhou Comment method "buildElementName".
     * 
     * @param element
     * @return
     */
    public static String buildElementName(Property property) {

        String elementName = "Unknown Label"; //$NON-NLS-1$
        if (property != null) {
            elementName = property.getDisplayName() + " " + property.getVersion(); //$NON-NLS-1$
        }

        return elementName;
    }

    public static Boolean hasChildren(TdXmlElementType element) {
        XMLSchemaBuilder xmlScheBuilder = XMLSchemaBuilder.getSchemaBuilder(element.getOwnedDocument());
        return xmlScheBuilder.isLeafNode(element).isOk();
    }

    /**
     * ADD gdbu 2011-7-25 bug : 23220
     * 
     * This method is used to connect to database to check if this catalog has Children.
     * 
     * @param dataProvider
     * @param catalog
     * @param childrenPattern
     * @param String[] childrenTypes
     * @return boolean
     * @throws Exception
     */
    public static boolean isCatalogHasChildren(Connection dataProvider, Catalog catalog, String childrenPattern,
            String[] childrenTypes) throws Exception {
        TypedReturnCode<java.sql.Connection> rcConn = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = rcConn.getObject();
        DatabaseMetaData dbJDBCMetadata = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        if (dataProvider instanceof DatabaseConnection) {
            dbJDBCMetadata = extractMeta.getDatabaseMetaData(connection, (DatabaseConnection) dataProvider, false);
        } else {
            TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DBTYPE, dataProvider.getTaggedValue());
            dbJDBCMetadata = extractMeta
                    .getDatabaseMetaData(connection, taggedValue == null ? "default" : taggedValue.getValue());//$NON-NLS-1$
        }
        Package catalogOrSchema = PackageHelper.getCatalogOrSchema(catalog);
        ResultSet tables = dbJDBCMetadata.getTables(catalogOrSchema.getName(), null, childrenPattern, childrenTypes);
        // MOD msjian TDQ-1806: fixed "Too many connections"
        try {
            if (tables.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }

    /**
     * This method is used to connect to database to check if this schema has children.
     * 
     * @param dataProvider
     * @param schema
     * @param childrenPattern
     * @param String[] childrenTypes
     * @return boolean
     * @throws Exception
     */
    public static boolean isSchemaHasChildren(Connection dataProvider, Schema schema, String childrenPattern,
            String[] childrenTypes) throws Exception {
        TypedReturnCode<java.sql.Connection> rcConn = MetadataConnectionUtils.createConnection((DatabaseConnection) dataProvider);
        java.sql.Connection connection = rcConn.getObject();
        DatabaseMetaData dbJDBCMetadata = null;
        ExtractMetaDataUtils extractMeta = ExtractMetaDataUtils.getInstance();
        if (dataProvider instanceof DatabaseConnection) {
            dbJDBCMetadata = extractMeta.getDatabaseMetaData(connection, (DatabaseConnection) dataProvider, false);
        } else {
            TaggedValue taggedValue = TaggedValueHelper.getTaggedValue(TaggedValueHelper.DBTYPE, dataProvider.getTaggedValue());
            dbJDBCMetadata = extractMeta
                    .getDatabaseMetaData(connection, taggedValue == null ? "default" : taggedValue.getValue());//$NON-NLS-1$
        }
        Package catalogOrSchema = PackageHelper.getCatalogOrSchema(schema);

        Package parentCatalog = PackageHelper.getParentPackage(catalogOrSchema);
        String schemaPattern = catalogOrSchema.getName();
        String catalogName = parentCatalog == null ? null : parentCatalog.getName();

        ResultSet tables = dbJDBCMetadata.getTables(catalogName, schemaPattern, childrenPattern, childrenTypes);
        // MOD msjian TDQ-1806: fixed "Too many connections"
        try {
            if (tables.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            if (connection != null) {
                ConnectionUtils.closeConnection(connection);
            }
        }
    }
}
