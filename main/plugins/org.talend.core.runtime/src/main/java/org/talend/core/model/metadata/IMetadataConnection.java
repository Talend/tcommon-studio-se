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
package org.talend.core.model.metadata;

import java.util.List;
import java.util.Map;

import org.talend.core.database.conn.ConnParameterKeys;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IMetadataConnection.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IMetadataConnection extends IMetadata {

    public String getId();

    public void setId(String id);

    public void setComment(String comment);

    public String getComment();

    public void setLabel(String label);

    public String getLabel();

    public String getDbType();

    public void setDbType(String dbType);

    public String getDriverJarPath();

    public void setDriverJarPath(String driverJarPath);

    public String getDriverClass();

    public void setDriverClass(String driverClass);

    public String getUrl();

    public void setUrl(String url);

    public String getPort();

    public void setPort(String port);

    public String getUsername();

    public void setUsername(String username);

    public String getPassword();

    public void setPassword(String password);

    public String getServerName();

    public void setServerName(String serverName);

    public String getDataSourceName();

    public void setDataSourceName(String dataSourceName);

    public String getFileFieldName();

    public void setFileFieldName(String fileFieldName);

    public String getDatabase();

    public void setDatabase(String database);

    public String getSchema();

    public void setSchema(String schema);

    public String getSqlSyntax();

    public void setSqlSyntax(String sqlSyntax);

    public String getStringQuote();

    public void setStringQuote(String stringQuote);

    public String getNullChar();

    public void setNullChar(String nullChar);

    public List<IMetadataTable> getListTables();

    public void setListTables(List<IMetadataTable> listTables);

    public void setProduct(String product);

    public String getProduct();

    public void setMapping(String mapping);

    public String getMapping();

    public String getDbRootPath();

    public void setDbRootPath(String dbRootPath);

    public String getAdditionalParams();

    public void setAdditionalParams(String additionalParams);

    public String getDbVersionString();

    public void setDbVersionString(String dbVersionString);

    public boolean isSqlMode();

    public void setSqlMode(boolean sqlMode);

    // keep the link to the emf model for now, But this part should be removed if possible later

    public Object getCurrentConnection();

    public void setCurrentConnection(Object dbconn);

    public String getDatafilter();

    public void setDatafilter(String datafilter);

    public String getUniverse();

    public void setUniverse(String universe);

    public boolean isRetrieveAllMetadata();

    public void setRetrieveAllMetadata(boolean retrieveAllMetadata);

    public String getPurpose();

    public void setPurpose(String purpose);

    public String getDescription();

    public void setDescription(String description);

    public String getStatus();

    public void setStatus(String status);

    public String getVersion();

    public void setVersion(String version);

    public String getAuthor();

    public void setAuthor(String author);

    public String getOtherParameter();

    public void setOtherParameter(String otherParameter);

    public String getDbName();

    public void setDbName(String dbName);

    public String getUiSchema();

    public void setUiSchema(String schema);

    public String getDatamodel();

    public void setDatamodel(String datamodel);

    public String getDatacluster();

    public void setDatacluster(String datacluster);

    public boolean isContentModel();

    public void setContentModel(boolean contentModel);

    public String getContextId();

    public void setContextId(String contextId);

    public String getContextName();

    public void setContextName(String contextName);

    /**
     * Returns the value that you stored in the data collection by the key. Normally, it is like this key-value. For
     * business it is extensible for later whenever you want to add a new parameter for a connection. The constants for
     * key are stored in {@link ConnParameterKeys}. Added by Marvin Wang on Aug. 8, 2012.
     * 
     * @param key
     * @return
     */
    Object getParameter(String key);

    /**
     * Puts the key-value you want to add into a collection, which is extensible for later whenever you want to add a
     * new parameter for connection. The constants for key are stored in {@link ConnParameterKeys}. Added by Marvin Wang
     * on Aug. 8, 2012.
     * 
     * @param key
     * @param value
     */
    void setParameter(String key, Object value);

    /**
     * Returns all parameters using the format "key-value" which are stored by map.
     */
    Map<String, Object> getOtherParameters();

}
