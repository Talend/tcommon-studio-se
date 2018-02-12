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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EMap;
import org.talend.core.IRepositoryContextService;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.model.metadata.Dbms;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.MetadataConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.relational.TdColumn;
import org.talend.cwm.relational.TdView;
import org.talend.metadata.managment.utils.EDataBaseType;
import org.talend.metadata.managment.utils.MetadataConnectionUtils;
import org.talend.model.bridge.ReponsitoryContextBridge;
import org.talend.utils.sugars.ReturnCode;
import org.talend.utils.sugars.TypedReturnCode;
import orgomg.cwm.objectmodel.core.ModelElement;
import orgomg.cwm.objectmodel.core.Package;
import orgomg.cwm.resource.relational.ColumnSet;

/**
 * DOC zshen class global comment. Detailled comment
 */
public abstract class MetadataFillerImpl<T extends Connection> implements IMetadataFiller<T> {

    private static Logger log = Logger.getLogger(MetadataFillFactory.class);

    private boolean isLinked = true;

    /*
     * (non-Jsdoc)
     * 
     * @see
     * org.talend.core.model.metadata.IMetadataFiller#fillMetadataParams(org.talend.core.model.metadata.IMetadataConnection
     * , org.talend.core.model.metadata.builder.connection.Connection)
     */
    protected void fillMetadataParams(IMetadataConnection metadataBean, T connection) {
        if (metadataBean == null || connection == null) {
            return;
        }
        if (!connection.isContextMode()) {
            // set Url
            ConnectionHelper.setURL(connection, metadataBean.getUrl());
            // set Password
            ConnectionHelper.setPassword(connection, metadataBean.getPassword());
            // set userName
            ConnectionHelper.setUsername(connection, metadataBean.getUsername());
            // set serverName
            ConnectionHelper.setServerName(connection, metadataBean.getServerName());
            // set port
            ConnectionHelper.setPort(connection, metadataBean.getPort());
            // set sid
            ConnectionHelper.setSID(connection, metadataBean.getDatabase());
        }
        // status
        String status = metadataBean.getStatus();
        ConnectionHelper.setDevStatus(status, connection);
        // version
        String version = metadataBean.getVersion();
        ConnectionHelper.setVersion(version, connection);
        // purpose
        String purpose = metadataBean.getPurpose();
        ConnectionHelper.setPurpose(purpose, connection);
        // Description
        String description = metadataBean.getPurpose();
        ConnectionHelper.setDescription(description, connection);
        // author
        String author = metadataBean.getAuthor();
        ConnectionHelper.setAuthor(author, connection);

        // otherParameter
        String otherParameter = metadataBean.getOtherParameter();
        ConnectionHelper.setOtherParameter(otherParameter, connection);
        // retrieveAllMetadata
        // boolean retrieveAllMetadata = metadataBean.isRetrieveAllMetadata();
        // ConnectionHelper.setRetrieveAllMetadata(retrieveAllMetadata, connection);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.IMetadataFiller#fillUIConnParams(org.talend.core.model.metadata.IMetadataConnection
     * , org.talend.core.model.metadata.builder.connection.Connection)
     */
    @Override
    public T fillUIConnParams(IMetadataConnection metadataBean, T connection) {
        if (connection == null || metadataBean == null) {
            return null;
        }

        /************************************************************************
         * currently just comment the "checkConnection" function, in case causes any unpredictable problem
         */
        // ReturnCode rc = checkConnection(metadataBean);
        //
        // if (!rc.isOk()) {
        // return null;
        // // need a dialog to show the error.
        // }

        this.fillMetadataParams(metadataBean, connection);
        return connection;
    }

    /*
     * @see org.talend.core.model.metadata.IMetadataFill#fillUIParams(java.util.Map)
     */
    @Override
    public IMetadataConnection fillUIParams(Map<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        IMetadataConnection metadataConnection = new MetadataConnection();
        Iterator<String> iter = paramMap.keySet().iterator();
        while (iter.hasNext()) {
            String paramKey = iter.next();
            String ParamValue = paramMap.get(paramKey);
            if ("driverPath".equalsIgnoreCase(paramKey)) {
                metadataConnection.setDriverJarPath(ParamValue);
            } else if ("sqlTypeName".equalsIgnoreCase(paramKey)) {
                // set dbType
                metadataConnection.setDbType(ParamValue);
                // set product(ProductId) and Schema(UISchema)
                EDatabaseTypeName edatabasetypeInstance = EDatabaseTypeName.getTypeFromDisplayName(ParamValue);
                String product = edatabasetypeInstance.getProduct();
                metadataConnection.setProduct(product);
                // set mapping(DbmsId)
                if (!ReponsitoryContextBridge.isDefautProject()) {
                    Dbms defaultDbmsFromProduct = MetadataTalendType.getDefaultDbmsFromProduct(product);
                    if (defaultDbmsFromProduct != null) {
                        String mapping = defaultDbmsFromProduct.getId();
                        metadataConnection.setMapping(mapping);
                    }
                }
                // set dbVersionString
                List<EDatabaseVersion4Drivers> dbTypeList = EDatabaseVersion4Drivers.indexOfByDbType(ParamValue);
                if (dbTypeList.size() == 1) {
                    metadataConnection.setDbVersionString(dbTypeList.get(0).getVersionValue());
                }
            } else if ("filePath".equalsIgnoreCase(paramKey)) {
                metadataConnection.setFileFieldName(ParamValue);
            } else if ("jdbcUrl".equalsIgnoreCase(paramKey)) {
                metadataConnection.setUrl(ParamValue);
            } else if ("aDDParameter".equalsIgnoreCase(paramKey)) {
                metadataConnection.setAdditionalParams(ParamValue);
            } else if ("driverClassName".equalsIgnoreCase(paramKey)) {
                metadataConnection.setDriverClass(ParamValue);
            } else if ("host".equalsIgnoreCase(paramKey)) {
                metadataConnection.setServerName(ParamValue);
            } else if ("port".equalsIgnoreCase(paramKey)) {
                metadataConnection.setPort(ParamValue);
            } else if ("dbName".equalsIgnoreCase(paramKey)) {
                metadataConnection.setDatabase(ParamValue);
            } else if ("otherParameter".equalsIgnoreCase(paramKey)) {
                metadataConnection.setOtherParameter(ParamValue);
            } else if ("retrieveAllMetadata".equalsIgnoreCase(paramKey)) {
                metadataConnection.setRetrieveAllMetadata(Boolean.valueOf(ParamValue));
            } else if ("name".equalsIgnoreCase(paramKey)) {
                metadataConnection.setLabel(ParamValue);
            } else if ("purpose".equalsIgnoreCase(paramKey)) {
                metadataConnection.setPurpose(ParamValue);
            } else if ("description".equalsIgnoreCase(paramKey)) {
                metadataConnection.setDescription(ParamValue);
            } else if ("author".equalsIgnoreCase(paramKey)) {
                metadataConnection.setAuthor(ParamValue);
            } else if ("status".equalsIgnoreCase(paramKey)) {
                metadataConnection.setStatus(ParamValue);
            } else if ("version".equalsIgnoreCase(paramKey)) {
                metadataConnection.setVersion(ParamValue);
            } else if ("password".equalsIgnoreCase(paramKey)) {
                metadataConnection.setPassword(ParamValue);
            } else if ("user".equalsIgnoreCase(paramKey)) {
                metadataConnection.setUsername(ParamValue);
            } else if ("universe".equalsIgnoreCase(paramKey)) {
                metadataConnection.setUniverse(ParamValue);
            } else if ("datafilter".equalsIgnoreCase(paramKey)) {
                metadataConnection.setDatafilter(ParamValue);
            } else if ("dbName".equalsIgnoreCase(paramKey)) {
                metadataConnection.setDataSourceName(ParamValue);
            } else if ("dbmsId".equalsIgnoreCase(paramKey) && metadataConnection.getMapping() == null) {
                metadataConnection.setMapping(ParamValue);
            } else if ("uiSchema".equalsIgnoreCase(paramKey) && metadataConnection.getMapping() == null) {
                metadataConnection.setUiSchema(ParamValue);
            }
        }
        return metadataConnection;
    }

    /*
     * @see org.talend.core.model.metadata.IMetadataFiller#fillUIParams(DatabaseConnection)
     */
    @Override
    public IMetadataConnection fillUIParams(DatabaseConnection conn) {
        if (conn == null) {
            return null;
        }
        IMetadataConnection metadataConnection = new MetadataConnection();
        IRepositoryContextService repositoryContextService = CoreRuntimePlugin.getInstance().getRepositoryContextService();
        if (repositoryContextService != null) {
            repositoryContextService.setMetadataConnectionParameter(conn, metadataConnection);
        } else {
            // driverPath
            metadataConnection.setDriverJarPath(conn.getDriverJarPath());

            // set dbType
            metadataConnection.setDbType(conn.getDatabaseType());
            // set product(ProductId) and Schema(UISchema)
            EDatabaseTypeName edatabasetypeInstance = EDatabaseTypeName.getTypeFromDisplayName(conn.getDatabaseType());
            String product = edatabasetypeInstance.getProduct();
            metadataConnection.setProduct(product);
            // set mapping(DbmsId)
            if (!ReponsitoryContextBridge.isDefautProject()) {
                Dbms defaultDbmsFromProduct = MetadataTalendType.getDefaultDbmsFromProduct(product);
                if (defaultDbmsFromProduct != null) {
                    String mapping = defaultDbmsFromProduct.getId();
                    metadataConnection.setMapping(mapping);
                }
            }
            // set dbVersionString
            metadataConnection.setDbVersionString(conn.getDbVersionString());

            // filePath
            metadataConnection.setFileFieldName(conn.getFileFieldName());
            // jdbcUrl
            metadataConnection.setUrl(conn.getURL());
            // aDDParameter
            metadataConnection.setAdditionalParams(conn.getAdditionalParams());
            // driverClassName
            metadataConnection.setDriverClass(conn.getDriverClass());
            // host
            metadataConnection.setServerName(conn.getServerName());
            // port
            metadataConnection.setPort(conn.getPort());
            // dbName
            metadataConnection.setDatabase(conn.getSID());
            // otherParameter
            metadataConnection.setOtherParameter(ConnectionHelper.getOtherParameter(conn));
            // password
            metadataConnection.setPassword(ConnectionHelper.getPassword(conn));
            // user
            metadataConnection.setUsername(conn.getUsername());
            // dbName
            metadataConnection.setDataSourceName(conn.getDatasourceName());
            // schema
            metadataConnection.setSchema(conn.getUiSchema());
            // dbmsId
            if (metadataConnection.getMapping() == null) {
                metadataConnection.setMapping(conn.getDbmsId());
            }
        }
        // retrieveAllMetadata
        // metadataConnection.setRetrieveAllMetadata(ConnectionHelper.getRetrieveAllMetadata(conn));
        // name
        metadataConnection.setLabel(conn.getLabel());
        // purpose
        metadataConnection.setPurpose(ConnectionHelper.getPurpose(conn));
        // description
        metadataConnection.setDescription(ConnectionHelper.getDescription(conn));
        // author
        metadataConnection.setAuthor(ConnectionHelper.getAuthor(conn));
        // status
        metadataConnection.setStatus(ConnectionHelper.getDevStatus(conn));
        // version
        metadataConnection.setVersion(ConnectionHelper.getVersion(conn));
        // universe
        metadataConnection.setUniverse(ConnectionHelper.getUniverse(conn));
        metadataConnection.setSqlMode(conn.isSQLMode());
        fillOtherParameters(metadataConnection, conn);
        return metadataConnection;

    }

    /**
     * Fills other parameters from db connection into metadata connection. Added by Marvin Wang on Aug 13, 2012.
     * 
     * @param metaConn
     * @param dbConn
     */
    protected void fillOtherParameters(IMetadataConnection metaConn, DatabaseConnection dbConn) {
        EMap<String, String> map = dbConn.getParameters();
        if (map != null && map.size() > 0) {
            Map<String, Object> metadataMap = metaConn.getOtherParameters();
            if (metadataMap == null) {
                metadataMap = new HashMap<String, Object>();
            }
            for (Entry<String, String> entry : map.entrySet()) {
                metadataMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.IMetadataFiller#fillViews(orgomg.cwm.objectmodel.core.Package,
     * java.sql.DatabaseMetaData, java.util.List, java.lang.String)
     */
    @Override
    public List<TdView> fillViews(Package pack, DatabaseMetaData dbJDBCMetadata, List<String> viewFilter, String viewPattern,
            String[] tableTypes) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.metadata.IMetadataFiller#fillColumns(orgomg.cwm.resource.relational.ColumnSet,
     * java.sql.DatabaseMetaData, java.util.List, java.lang.String)
     */
    @Override
    public List<TdColumn> fillColumns(ColumnSet colSet, DatabaseMetaData dbJDBCMetadata, List<String> columnFilter,
            String columnPattern) {
        return null;
    }

    @Override
    public List<TdColumn> fillColumns(ColumnSet colSet, IMetadataConnection iMetadataConnection, DatabaseMetaData dbJDBCMetadata,
            List<String> columnFilter, String columnPattern) {
        return null;
    }

    /**
     * 
     * zshen Comment method "isLinked".
     * 
     * @return get whether the subElements need to be linked to the parent element.
     */
    @Override
    public boolean isLinked() {
        return isLinked;
    }

    /**
     * 
     * zshen Comment method "isLinked". set whether the subElements need to be linked to the parent element.
     * 
     * @return
     */
    @Override
    public void setLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.IMetadataFiller#checkConnection(org.talend.core.model.metadata.IMetadataConnection
     * )
     */
    @Override
    public ReturnCode checkConnection(IMetadataConnection metadataBean) {
        return createConnection(metadataBean, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.metadata.IMetadataFiller#createConnection(org.talend.core.model.metadata.IMetadataConnection
     * )
     */
    @Override
    public TypedReturnCode<?> createConnection(IMetadataConnection metadataBean) {
        return createConnection(metadataBean, false);
    }

    /**
     * create a Connection and whether close it depend on closeConnection.
     * 
     * @param metadataBean
     * @param closeConnection
     * @return
     */
    public TypedReturnCode<?> createConnection(IMetadataConnection metadataBean, boolean closeConnection) {
        TypedReturnCode<java.sql.Connection> rc = new TypedReturnCode<java.sql.Connection>();
        if (EDataBaseType.MDM.getProductName().equalsIgnoreCase(metadataBean.getDbType())) {
            try {
                Object stub = MetadataConnectionUtils.getXtentisBindingStub(metadataBean);
                if (stub != null) {
                    rc.setOk(true);
                    rc.setMessage("OK"); //$NON-NLS-1$
                } else {
                    rc.setOk(false);
                    rc.setMessage("Can not connect to mdm server !");
                }
            } catch (Exception e) {
                log.warn(e, e);
                rc.setOk(false);
                rc.setMessage(e.getMessage());
            }
        } else {
            rc = MetadataConnectionUtils.createConnection(metadataBean, closeConnection);
        }
        return rc;
    }

    /**
     * 
     * This method is to judge whether the db element need to be created or not according to the filter list.
     * 
     * @param filterList
     * @param elementName
     * @param isCaseSensitive some Schemas are CaseSensitive
     * @return true if there is no filter set on UI, or the currently elementName is included in this filter list.
     */
    protected boolean isCreateElement(List<String> filterList, String elementName, boolean isCaseSensitive) {
        if (filterList == null || filterList.isEmpty()) {
            return true;
        }
        for (String name : filterList) {
            if ((isCaseSensitive && elementName.equals(name)) || (!isCaseSensitive && elementName.equalsIgnoreCase(name))) {
                return true;
            }
        }
        return false;
    }

    protected String getName(ModelElement element) {
        return element != null ? element.getName() : null;
    }

}
