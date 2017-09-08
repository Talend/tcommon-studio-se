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
package org.talend.metadata.managment.ui.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.cwm.helper.CatalogHelper;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SchemaHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

import orgomg.cwm.resource.relational.Catalog;
import orgomg.cwm.resource.relational.Schema;

/**
 * this class is used when switching context group name.
 */
public class SwitchContextGroupNameImpl implements ISwitchContext {

    private static Logger log = Logger.getLogger(SwitchContextGroupNameImpl.class);

    private static SwitchContextGroupNameImpl instance;

    private SwitchContextGroupNameImpl() {
    }

    /**
     * get a instance of this class.
     *
     * @return
     */
    public static SwitchContextGroupNameImpl getInstance() {
        if (instance == null) {
            instance = new SwitchContextGroupNameImpl();
        }
        return instance;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.model.metadata.builder.database.ISwitchContext#updateContextGroup(org.talend.core.model.
     * properties .ContextItem, org.talend.core.model.metadata.builder.connection.Connection)
     */
    @Override
    public boolean updateContextGroup(ConnectionItem connItem, String selectedContext) {
        if (connItem == null) {
            return false;
        }
        Connection con = connItem.getConnection();
        // MOD msjian 2012-2-13 TDQ-4559: make it support file/mdm connection
        if (con != null) {
            // TDQ-4559~
            String oldContextName = con.getContextName();

            if (!isContextIsValid(selectedContext, con)) {
                return false;
            }
            con.setContextName(selectedContext);
            if (con instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) connItem.getConnection();
                String newURL = getChangedURL(dbConn, selectedContext);
                dbConn.setURL(newURL);

                updateConnectionForSidOrUiSchema(dbConn, oldContextName);
            }

            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            try {
                factory.save(connItem);
            } catch (PersistenceException e) {
                log.error(e, e);
            }
            return true;
        }
        return false;
    }

    /**
     * DOC talend Comment method "checkContextIsValid".
     *
     * @param selectedContext
     * @paramconn
     */
    private boolean isContextIsValid(String selectedContext, Connection conn) {
        String oldContextName = conn.getContextName();
        boolean retCode = false;
        if (conn instanceof DatabaseConnection) {
            EDatabaseTypeName dbType = EDatabaseTypeName.getTypeFromDbType(((DatabaseConnection) conn).getDatabaseType());

            if (dbType == EDatabaseTypeName.GODBC) {// for ODBC
                retCode = true;
            } else if (dbType == EDatabaseTypeName.GENERAL_JDBC) {
                retCode = true;
            } else {
                DatabaseConnection dbConn = (DatabaseConnection) conn;
                boolean hasCatalog = ConnectionHelper.hasCatalog(dbConn);
                boolean hasSchema = ConnectionHelper.hasSchema(dbConn);
                ContextType newContextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, selectedContext, false);
                ContextType oldContextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, oldContextName, false);
                String newSidOrDatabase = ConnectionContextHelper.getOriginalValue(newContextType, dbConn.getSID());
                String newUiShema = ConnectionContextHelper.getOriginalValue(newContextType, dbConn.getUiSchema());
                String oldSidOrDatabase = ConnectionContextHelper.getOriginalValue(oldContextType, dbConn.getSID());
                String oldUiShema = ConnectionContextHelper.getOriginalValue(oldContextType, dbConn.getUiSchema());
                if (hasCatalog) {// for example mysql
                    retCode = !isEmptyString(oldSidOrDatabase) && !isEmptyString(newSidOrDatabase);
                    if (hasSchema) {// for example mssql
                        retCode &= !isEmptyString(oldUiShema) && !isEmptyString(newUiShema);
                    }
                } else if (hasSchema) {// for example oracle
                    retCode = !isEmptyString(oldUiShema) && !isEmptyString(newUiShema);
                }
            }
        } else if (conn instanceof FileConnection) {
            retCode = true;
        }

        return retCode;

    }

    /**
     *
     * change context Group need to synchronization name of catalog or schema
     *
     * @param dbConn
     * @param oldContextName
     */
    private void updateConnectionForSidOrUiSchema(DatabaseConnection dbConn, String oldContextName) {
        String selectedContext = dbConn.getContextName();
        ContextType newContextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, selectedContext, false);
        ContextType oldContextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, oldContextName, false);
        String newSidOrDatabase = ConnectionContextHelper.getOriginalValue(newContextType, dbConn.getSID());
        String newUiShema = ConnectionContextHelper.getOriginalValue(newContextType, dbConn.getUiSchema());
        String oldSidOrDatabase = ConnectionContextHelper.getOriginalValue(oldContextType, dbConn.getSID());
        String oldUiShema = ConnectionContextHelper.getOriginalValue(oldContextType, dbConn.getUiSchema());
        if (!isEmptyString(newSidOrDatabase) && !isEmptyString(oldSidOrDatabase)) {// for example mysql or mssql
            Catalog catalog = CatalogHelper.getCatalog(dbConn, oldSidOrDatabase);
            if (catalog != null) {
                catalog.setName(newSidOrDatabase);

                Schema schema = SchemaHelper.getSchemaByName(CatalogHelper.getSchemas(catalog), oldUiShema);// for
                                                                                                            // example
                                                                                                            // mssql
                if (schema != null) {
                    schema.setName(newUiShema);
                }
            }
        }
        if (!isEmptyString(newUiShema) && !isEmptyString(oldUiShema)) {// for example oracle
            Schema schema = SchemaHelper.getSchema(dbConn, oldUiShema);
            if (schema != null) {
                schema.setName(newUiShema);
            }
        }
    }

    /**
     *
     * check whether str is null or length is zero
     *
     * @param str
     * @return
     */
    private boolean isEmptyString(final String str) {
        return str == null || str.length() == 0;
    }

    /**
     * change the URL according to selected context Added yyin 20120918 TDQ-5668
     *
     * @param connItem
     * @param con
     * @param selectedContext
     */
    private String getChangedURL(DatabaseConnection dbConn, String selectedContext) {
        ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(dbConn, selectedContext, false);
        String url = dbConn.getURL();
        if(url != null){
            return url;
        }
        String server = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getServerName());
        String username = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getRawPassword());
        String port = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getPort());
        String sidOrDatabase = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getSID());
        String datasource = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDatasourceName());
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getFileFieldName());
        String dbRootPath = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getDBRootPath());
        String additionParam = ConnectionContextHelper.getOriginalValue(contextType, dbConn.getAdditionalParams());

        return DatabaseConnStrUtil.getURLString(dbConn.getDatabaseType(), dbConn.getDbVersionString(), server, username,
                password, port, sidOrDatabase, filePath.toLowerCase(), datasource, dbRootPath, additionParam);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.repository.ui.utils.ISwitchContext#updateContextForConnectionItems(java.util.Map,
     * org.talend.core.model.properties.ContextItem)
     */
    @Override
    public boolean updateContextForConnectionItems(Map<String, String> contextGroupRanamedMap, ContextItem contextItem) {
        if (contextItem == null) {
            return false;
        }
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            List<IRepositoryViewObject> allConnectionItem = factory.getAll(ProjectManager.getInstance().getCurrentProject(),
                    ERepositoryObjectType.METADATA_CONNECTIONS);

            for (IRepositoryViewObject connectionItem : allConnectionItem) {
                Item item = connectionItem.getProperty().getItem();
                if (item instanceof ConnectionItem && ConnectionContextHelper.checkContextMode((ConnectionItem) item) != null) {
                    Connection con = ((ConnectionItem) item).getConnection();
                    String contextId = con.getContextId();
                    if (contextId != null && contextId.equals(contextItem.getProperty().getId())) {
                        String oldContextGroup = con.getContextName();
                        boolean modified = false;
                        if (oldContextGroup != null && !"".equals(oldContextGroup)) { //$NON-NLS-1$
                            String newContextGroup = contextGroupRanamedMap.get(oldContextGroup);
                            if (newContextGroup != null) { // rename
                                con.setContextName(newContextGroup);
                                modified = true;
                            }
                        } else { // if not set, set default group
                            ContextItem originalItem = ContextUtils.getContextItemById2(contextId);
                            con.setContextName(originalItem.getDefaultContext());
                            modified = true;
                        }
                        if (modified) {
                            factory.save(item);
                        }
                    }
                }
            }

            IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
            if (hadoopClusterService != null) {
                hadoopClusterService.updateConfJarsByContextGroup(contextItem, contextGroupRanamedMap);
            }

            return true;
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return false;
    }
}
