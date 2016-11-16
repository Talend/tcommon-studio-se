// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.metadata.builder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.IMetadataTable;

/**
 * DOC cantoine Meta Data Connection. Contains info of conncection. <br/>
 * 
 * $Id: MetadataConnection.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class MetadataConnection implements IMetadataConnection {

    private String dbVersionString;

    private String dbType;

    private String driverClass;

    private String driverJarPath;

    private String url;

    private String port;

    private String username;

    private String password;

    private String database;

    private String schema;

    private String serverName;

    private String dataSourceName;

    private String fileFieldName;

    private String sqlSyntax;

    private String stringQuote;

    private String nullChar;

    private List<IMetadataTable> listTables;

    private String mapping;

    private String product;

    private InputStream xmlStream;

    private String dbRootPath;

    private String additionalParams;

    private boolean sqlMode;

    // MOD by zshen for mdmConnection of top
    private String datafilter;

    private String universe;

    private String Datamodel;

    private String Datacluster;

    // ~

    // MOD by zshen for DatabaseConnection of top
    private boolean retrieveAllMetadata;

    private String purpose;

    private String description;

    private String status;

    private String version;

    private String author;

    private String otherParameter;

    private String dbName;

    private String uiSchema;

    private boolean contentModel = false;

    private String contextId;

    private String contextName;

    // ~

    private String comment;

    private String id;

    private String label;

    private Object connection;

    private Map<String, Object> otherParameters;

    public MetadataConnection() {
        otherParameters = new HashMap<String, Object>();
    }

    public boolean isSqlMode() {
        return this.sqlMode;
    }

    public void setSqlMode(boolean sqlMode) {
        this.sqlMode = sqlMode;
    }

    public String getDbType() {
        return this.dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerName() {
        return this.serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getDataSourceName() {
        return this.dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getFileFieldName() {
        return this.fileFieldName;
    }

    public void setFileFieldName(String fileFieldName) {
        this.fileFieldName = fileFieldName;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;

    }

    public String getSchema() {
        return this.schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSqlSyntax() {
        return this.sqlSyntax;
    }

    public void setSqlSyntax(String sqlSyntax) {
        this.sqlSyntax = sqlSyntax;
    }

    public String getStringQuote() {
        return this.stringQuote;
    }

    public void setStringQuote(String stringQuote) {
        this.stringQuote = stringQuote;
    }

    public String getNullChar() {
        return this.nullChar;
    }

    public void setNullChar(String nullChar) {
        this.nullChar = nullChar;
    }

    public InputStream getXmlStream() {
        return xmlStream;
    }

    public void setXmlStream(InputStream xmlStream) {
        this.xmlStream = xmlStream;
    }

    public List<IMetadataTable> getListTables() {
        return this.listTables;
    }

    public void setListTables(List<IMetadataTable> listTables) {
        this.listTables = listTables;
    }

    public String getMapping() {
        return this.mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getProduct() {
        return this.product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDbRootPath() {
        return this.dbRootPath;
    }

    public void setDbRootPath(String dbRootPath) {
        this.dbRootPath = dbRootPath;
    }

    public String getAdditionalParams() {
        return this.additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }

    public String getDriverJarPath() {
        return this.driverJarPath;
    }

    public void setDriverJarPath(String driverJarPath) {
        this.driverJarPath = driverJarPath;
    }

    public String getDbVersionString() {
        return this.dbVersionString;
    }

    public void setDbVersionString(String dbVersionString) {
        this.dbVersionString = dbVersionString;
    }

    @Override
    /*
     * qli comment the method equals. rewrite the method equals.
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MetadataConnection)) {
            return false;
        }
        MetadataConnection otherConnection = (MetadataConnection) obj;
        if (!checkString(otherConnection.getDbRootPath(), this.getDbRootPath())) {
            return false;
        }
        if (!checkString(otherConnection.getDbType(), this.getDbType())) {
            return false;
        }
        if (!checkString(otherConnection.getDbVersionString(), this.getDbVersionString())) {
            return false;
        }
        if (!checkString(otherConnection.getDriverClass(), this.getDriverClass())) {
            return false;
        }
        if (!checkString(otherConnection.getDriverJarPath(), this.getDriverJarPath())) {
            return false;
        }
        if (!checkString(otherConnection.getPassword(), this.getPassword())) {
            return false;
        }
        if (!checkString(otherConnection.getUrl(), this.getUrl())) {
            return false;
        }
        if (!checkString(otherConnection.getUsername(), this.getUsername())) {
            return false;
        }
        if (!checkString(otherConnection.getSchema(), this.getSchema())) {
            return false;
        }
        return true;
    }

    private boolean checkString(String first, String second) {
        if (first == null && second == null) {
            return true;
        }
        if (first != null && first.equals(second)) {
            return true;
        }
        return false;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDatafilter() {
        return datafilter;
    }

    public void setDatafilter(String datafilter) {
        this.datafilter = datafilter;
    }

    public String getUniverse() {
        return universe;
    }

    public void setUniverse(String universe) {
        this.universe = universe;
    }

    public boolean isRetrieveAllMetadata() {
        return retrieveAllMetadata;
    }

    public void setRetrieveAllMetadata(boolean retrieveAllMetadata) {
        this.retrieveAllMetadata = retrieveAllMetadata;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getCurrentConnection() {
        return connection;
    }

    public void setCurrentConnection(Object dbconn) {
        this.connection = dbconn;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOtherParameter() {
        return otherParameter;
    }

    public void setOtherParameter(String otherParameter) {
        this.otherParameter = otherParameter;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUiSchema() {
        return this.uiSchema;
    }

    public void setUiSchema(String uiSchema) {
        this.uiSchema = uiSchema;
    }

    public String getDatamodel() {
        return Datamodel;
    }

    public void setDatamodel(String datamodel) {
        Datamodel = datamodel;
    }

    public String getDatacluster() {
        return Datacluster;
    }

    public void setDatacluster(String datacluster) {
        Datacluster = datacluster;
    }

    public boolean isContentModel() {
        return contentModel;
    }

    public void setContentModel(boolean contentModel) {
        this.contentModel = contentModel;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public Object getParameter(String key) {
        if (otherParameters != null)
            return otherParameters.get(key);
        return null;
    }

    public void setParameter(String key, Object value) {
        if (otherParameters == null)
            otherParameters = new HashMap<String, Object>();
        otherParameters.put(key, value);
    }

    /**
     * In this class, all parameters that will be added later are stored in map, so return the map.
     */
    public Map<String, Object> getOtherParameters() {
        return otherParameters;
    }

}
