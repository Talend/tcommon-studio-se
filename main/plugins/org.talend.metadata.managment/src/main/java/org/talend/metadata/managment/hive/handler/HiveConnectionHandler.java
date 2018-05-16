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
package org.talend.metadata.managment.hive.handler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.metadata.managment.connection.manager.HiveConnectionManager;

/**
 * created by xqliu on 2013-11-1 Detailled comment
 * 
 */
public class HiveConnectionHandler {

    private static Logger log = Logger.getLogger(HiveConnectionHandler.class);

    protected final String PROPERTY = "PROPERTY"; //$NON-NLS-1$

    protected final String VALUE = "VALUE"; //$NON-NLS-1$

    private IMetadataConnection metadataConnection;

    private Map<String, String> hadoopPropertiesMap = new HashMap<String, String>();

    /**
     * Getter for metadataConnection.
     * 
     * @return the metadataConnection
     */
    public IMetadataConnection getMetadataConnection() {
        return this.metadataConnection;
    }

    /**
     * Sets the metadataConnection.
     * 
     * @param metadataConnection the metadataConnection to set
     */
    public void setMetadataConnection(IMetadataConnection metadataConnection) {
        this.metadataConnection = metadataConnection;
    }

    /**
     * Getter for hadoopPropertiesMap.
     * 
     * @return the hadoopPropertiesMap
     */
    public Map<String, String> getHadoopPropertiesMap() {
        return this.hadoopPropertiesMap;
    }

    /**
     * Sets the hadoopPropertiesMap.
     * 
     * @param hadoopPropertiesMap the hadoopPropertiesMap to set
     */
    public void setHadoopPropertiesMap(Map<String, String> hadoopPropertiesMap) {
        this.hadoopPropertiesMap = hadoopPropertiesMap;
    }

    public HiveConnectionHandler(IMetadataConnection metadataConnection) {
        this.metadataConnection = metadataConnection;
        initHadoopProperties();
    }

    /**
     * init the hadoop properties according to the IMetadataConnection.
     */
    protected void initHadoopProperties() {
        getHadoopPropertiesMap().clear();
        String hadoopProperties = (String) getMetadataConnection().getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
        List<Map<String, Object>> hadoopPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties);
        for (Map<String, Object> map : hadoopPropertiesList) {
            Object objProp = map.get(PROPERTY);
            Object objValue = map.get(VALUE);
            if (objProp != null && objValue != null) {
                String key = TalendQuoteUtils.removeQuotes(objProp.toString());
                String value = TalendQuoteUtils.removeQuotes(objValue.toString());
                getHadoopPropertiesMap().put(key, value);
            }
        }
    }

    /**
     * create the hive connection, set/execute hadoop properties.
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    public java.sql.Connection createHiveConnection() throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        java.sql.Connection createConnection = null;
        if (getMetadataConnection() != null) {
            // create the hive connection
            createConnection = HiveConnectionManager.getInstance().createConnection(getMetadataConnection());
            // execute hadoop parameters
            executeHadoopParameters(createConnection);
        }
        return createConnection;
    }

    protected void executeHadoopParameters(java.sql.Connection sqlConnection) throws SQLException {
        Map<String, String> map = new HashMap<String, String>();
        // put the hadoop preperties which user input
        map.putAll(getHadoopPropertiesMap());
        // put the default hadoop parameters which user don't input
        map.putAll(getDefaultHadoopParameters());
        Statement createStatement = sqlConnection.createStatement();
        for (String key : map.keySet()) {
            createStatement.execute("SET " + key + "=" + map.get(key)); //$NON-NLS-1$ //$NON-NLS-2$
        }
        createStatement.close();
    }

    /**
     * if user don't input the default hadoop properties, add them and the default value here.
     * 
     * @return
     */
    protected Map<String, String> getDefaultHadoopParameters() {
        return new HashMap<String, String>();
    }
}
