// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.update;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.talend.core.AbstractRepositoryContextUpdateService;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;

public class GeneralConnectionContextService extends AbstractRepositoryContextUpdateService {

    @Override
    public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
        boolean isModified = false;
        if (conn.isContextMode()) {
            if (conn instanceof DatabaseConnection) {
                DatabaseConnection dbConn = (DatabaseConnection) conn;
                if (dbConn.getAdditionalParams() != null && dbConn.getAdditionalParams().equals(oldValue)) {
                    dbConn.setAdditionalParams(newValue);
                    isModified = true;
                } else if (dbConn.getUsername() != null && dbConn.getUsername().equals(oldValue)) {
                    dbConn.setUsername(newValue);
                    isModified = true;
                } else if (dbConn.getPassword() != null && dbConn.getPassword().equals(oldValue)) {
                    dbConn.setPassword(newValue);
                    isModified = true;
                } else if (dbConn.getServerName() != null && dbConn.getServerName().equals(oldValue)) {
                    dbConn.setServerName(newValue);
                    isModified = true;
                } else if (dbConn.getPort() != null && dbConn.getPort().equals(oldValue)) {
                    dbConn.setPort(newValue);
                    isModified = true;
                } else if (dbConn.getSID() != null && dbConn.getSID().equals(oldValue)) {
                    dbConn.setSID(newValue);
                    isModified = true;
                } else if (dbConn.getDbmsId() != null && dbConn.getDbmsId().equals(oldValue)) {
                    dbConn.setDbmsId(newValue);
                    isModified = true;
                } else if (dbConn.getDriverClass() != null && dbConn.getDriverClass().equals(oldValue)) {
                    dbConn.setDriverClass(newValue);
                    isModified = true;
                } else if (dbConn.getDriverJarPath() != null && dbConn.getDriverJarPath().equals(oldValue)) {
                    dbConn.setDriverJarPath(newValue);
                    isModified = true;
                } else if (dbConn.getURL() != null && dbConn.getURL().equals(oldValue)) {
                    dbConn.setURL(newValue);
                    isModified = true;
                } else if (dbConn.getUiSchema() != null && dbConn.getUiSchema().equals(oldValue)) {
                    // Added by Marvin Wang on Nov.7, 2012 for bug TDI-12596, because schema can not be
                    // propagated to metadata db.
                    dbConn.setUiSchema(newValue);
                    isModified = true;
                } else {
                    updateParameters(dbConn, oldValue, newValue);
                }
            }

            if (conn instanceof FileExcelConnection) {
                if (((FileExcelConnection) conn).getFirstColumn() != null
                        && ((FileExcelConnection) conn).getFirstColumn().equals(oldValue)) {
                    ((FileExcelConnection) conn).setFirstColumn(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getLastColumn() != null
                        && ((FileExcelConnection) conn).getLastColumn().equals(oldValue)) {
                    ((FileExcelConnection) conn).setLastColumn(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getThousandSeparator() != null
                        && ((FileExcelConnection) conn).getThousandSeparator().equals(oldValue)) {
                    ((FileExcelConnection) conn).setThousandSeparator(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getDecimalSeparator() != null
                        && ((FileExcelConnection) conn).getDecimalSeparator().equals(oldValue)) {
                    ((FileExcelConnection) conn).setDecimalSeparator(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getFilePath() != null
                        && ((FileExcelConnection) conn).getFilePath().equals(oldValue)) {
                    ((FileExcelConnection) conn).setFilePath(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getEncoding() != null
                        && ((FileExcelConnection) conn).getEncoding().equals(oldValue)) {
                    ((FileExcelConnection) conn).setEncoding(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getLimitValue() != null
                        && ((FileExcelConnection) conn).getLimitValue().equals(oldValue)) {
                    ((FileExcelConnection) conn).setLimitValue(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getHeaderValue() != null
                        && ((FileExcelConnection) conn).getHeaderValue().equals(oldValue)) {
                    ((FileExcelConnection) conn).setHeaderValue(newValue);
                    isModified = true;
                } else if (((FileExcelConnection) conn).getFooterValue() != null
                        && ((FileExcelConnection) conn).getFooterValue().equals(oldValue)) {
                    ((FileExcelConnection) conn).setFooterValue(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof DelimitedFileConnection) {
                if (((DelimitedFileConnection) conn).getFilePath() != null
                        && ((DelimitedFileConnection) conn).getFilePath().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setFilePath(newValue);
                    isModified = true;
                } else if (((DelimitedFileConnection) conn).getEncoding() != null
                        && ((DelimitedFileConnection) conn).getEncoding().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setEncoding(newValue);
                    isModified = true;
                } else if (((DelimitedFileConnection) conn).getLimitValue() != null
                        && ((DelimitedFileConnection) conn).getLimitValue().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setLimitValue(newValue);
                    isModified = true;
                } else if (((DelimitedFileConnection) conn).getHeaderValue() != null
                        && ((DelimitedFileConnection) conn).getHeaderValue().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setHeaderValue(newValue);
                    isModified = true;
                } else if (((DelimitedFileConnection) conn).getFooterValue() != null
                        && ((DelimitedFileConnection) conn).getFooterValue().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setFooterValue(newValue);
                    isModified = true;
                } else if (((DelimitedFileConnection) conn).getRowSeparatorValue() != null
                        && ((DelimitedFileConnection) conn).getRowSeparatorValue().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setRowSeparatorValue(newValue);
                    isModified = true;
                } else if (((DelimitedFileConnection) conn).getFieldSeparatorValue() != null
                        && ((DelimitedFileConnection) conn).getFieldSeparatorValue().equals(oldValue)) {
                    ((DelimitedFileConnection) conn).setFieldSeparatorValue(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof RegexpFileConnection) {
                if (((RegexpFileConnection) conn).getFilePath() != null
                        && ((RegexpFileConnection) conn).getFilePath().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setFilePath(newValue);
                    isModified = true;
                } else if (((RegexpFileConnection) conn).getEncoding() != null
                        && ((RegexpFileConnection) conn).getEncoding().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setEncoding(newValue);
                    isModified = true;
                } else if (((RegexpFileConnection) conn).getLimitValue() != null
                        && ((RegexpFileConnection) conn).getLimitValue().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setLimitValue(newValue);
                    isModified = true;
                } else if (((RegexpFileConnection) conn).getHeaderValue() != null
                        && ((RegexpFileConnection) conn).getHeaderValue().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setHeaderValue(newValue);
                    isModified = true;
                } else if (((RegexpFileConnection) conn).getFooterValue() != null
                        && ((RegexpFileConnection) conn).getFooterValue().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setFooterValue(newValue);
                    isModified = true;
                } else if (((RegexpFileConnection) conn).getRowSeparatorValue() != null
                        && ((RegexpFileConnection) conn).getRowSeparatorValue().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setRowSeparatorValue(newValue);
                    isModified = true;
                } else if (((RegexpFileConnection) conn).getFieldSeparatorValue() != null
                        && ((RegexpFileConnection) conn).getFieldSeparatorValue().equals(oldValue)) {
                    ((RegexpFileConnection) conn).setFieldSeparatorValue(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof LdifFileConnection) {
                LdifFileConnection dbConn = (LdifFileConnection) conn;
                if (dbConn.getFilePath() != null && dbConn.getFilePath().equals(oldValue)) {
                    dbConn.setFilePath(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof PositionalFileConnection) {
                PositionalFileConnection dbConn = (PositionalFileConnection) conn;
                if (dbConn.getFilePath() != null && dbConn.getFilePath().equals(oldValue)) {
                    dbConn.setFilePath(newValue);
                    isModified = true;
                } else if (dbConn.getEncoding() != null && dbConn.getEncoding().equals(oldValue)) {
                    dbConn.setEncoding(newValue);
                    isModified = true;
                } else if (dbConn.getLimitValue() != null && dbConn.getLimitValue().equals(oldValue)) {
                    dbConn.setLimitValue(newValue);
                    isModified = true;
                } else if (dbConn.getHeaderValue() != null && dbConn.getHeaderValue().equals(oldValue)) {
                    dbConn.setHeaderValue(newValue);
                    isModified = true;
                } else if (dbConn.getFooterValue() != null && dbConn.getFooterValue().equals(oldValue)) {
                    dbConn.setFooterValue(newValue);
                    isModified = true;
                } else if (dbConn.getRowSeparatorValue() != null && dbConn.getRowSeparatorValue().equals(oldValue)) {
                    dbConn.setRowSeparatorValue(newValue);
                    isModified = true;
                } else if (dbConn.getFieldSeparatorValue() != null && dbConn.getFieldSeparatorValue().equals(oldValue)) {
                    dbConn.setFieldSeparatorValue(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof XmlFileConnection) {
                XmlFileConnection dbConn = (XmlFileConnection) conn;
                if (dbConn.getXmlFilePath() != null && dbConn.getXmlFilePath().equals(oldValue)) {
                    dbConn.setXmlFilePath(newValue);
                    isModified = true;
                } else if (dbConn.getEncoding() != null && dbConn.getEncoding().equals(oldValue)) {
                    dbConn.setEncoding(newValue);
                    isModified = true;
                } else if (dbConn.getOutputFilePath() != null && dbConn.getOutputFilePath().equals(oldValue)) {
                    dbConn.setOutputFilePath(newValue);
                    isModified = true;
                }
                EList schema = dbConn.getSchema();
                if (schema != null && schema.size() > 0) {
                    if (schema.get(0) instanceof XmlXPathLoopDescriptor) {
                        XmlXPathLoopDescriptor descriptor = (XmlXPathLoopDescriptor) schema.get(0);
                        if (descriptor.getAbsoluteXPathQuery() != null && descriptor.getAbsoluteXPathQuery().equals(oldValue)) {
                            descriptor.setAbsoluteXPathQuery(newValue);
                            isModified = true;
                        }
                    }
                }
            }

            if (conn instanceof SalesforceSchemaConnection) {
                SalesforceSchemaConnection ssConn = (SalesforceSchemaConnection) conn;
                if (ssConn.getWebServiceUrl() != null && ssConn.getWebServiceUrl().equals(oldValue)) {
                    ssConn.setWebServiceUrl(newValue);
                    isModified = true;
                } else if (ssConn.getPassword() != null && ssConn.getPassword().equals(oldValue)) {
                    // in fact, because in context mode. can setPassword directly.
                    // ssConn.setPassword(ssConn.getValue(newValue,true));
                    ssConn.setPassword(newValue);
                    isModified = true;
                } else if (ssConn.getUserName() != null && ssConn.getUserName().equals(oldValue)) {
                    ssConn.setUserName(newValue);
                    isModified = true;
                } else if (ssConn.getTimeOut() != null && ssConn.getTimeOut().equals(oldValue)) {
                    ssConn.setTimeOut(newValue);
                    isModified = true;
                } else if (ssConn.getBatchSize() != null && ssConn.getBatchSize().equals(oldValue)) {
                    ssConn.setBatchSize(newValue);
                    isModified = true;
                } else if (ssConn.getQueryCondition() != null && ssConn.getQueryCondition().equals(oldValue)) {
                    ssConn.setQueryCondition(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof WSDLSchemaConnection) {
                WSDLSchemaConnection dbConn = (WSDLSchemaConnection) conn;
                if (dbConn.getUserName() != null && dbConn.getUserName().equals(oldValue)) {
                    dbConn.setUserName(newValue);
                    isModified = true;
                } else if (dbConn.getPassword() != null && dbConn.getPassword().equals(oldValue)) {
                    dbConn.setPassword(newValue);
                    isModified = true;
                } else if (dbConn.getProxyHost() != null && dbConn.getProxyHost().equals(oldValue)) {
                    dbConn.setProxyHost(newValue);
                    isModified = true;
                } else if (dbConn.getProxyPassword() != null && dbConn.getProxyPassword().equals(oldValue)) {
                    dbConn.setProxyPassword(newValue);
                    isModified = true;
                } else if (dbConn.getProxyUser() != null && dbConn.getProxyUser().equals(oldValue)) {
                    dbConn.setProxyUser(newValue);
                    isModified = true;
                } else if (dbConn.getProxyPort() != null && dbConn.getProxyPort().equals(oldValue)) {
                    dbConn.setProxyPort(newValue);
                    isModified = true;
                }
            }

            if (conn instanceof SAPConnection) {
                SAPConnection sapConn = (SAPConnection) conn;
                if (sapConn.getClient() != null && sapConn.getClient().equals(oldValue)) {
                    sapConn.setClient(newValue);
                    isModified = true;
                } else if (sapConn.getUsername() != null && sapConn.getUsername().equals(oldValue)) {
                    sapConn.setUsername(newValue);
                    isModified = true;
                } else if (sapConn.getPassword() != null && sapConn.getPassword().equals(oldValue)) {
                    sapConn.setPassword(newValue);
                    isModified = true;
                } else if (sapConn.getHost() != null && sapConn.getHost().equals(oldValue)) {
                    sapConn.setHost(newValue);
                    isModified = true;
                } else if (sapConn.getSystemNumber() != null && sapConn.getSystemNumber().equals(oldValue)) {
                    sapConn.setSystemNumber(newValue);
                    isModified = true;
                } else if (sapConn.getLanguage() != null && sapConn.getLanguage().equals(oldValue)) {
                    sapConn.setLanguage(newValue);
                    isModified = true;
                } else {
                    for (AdditionalConnectionProperty sapProperty : sapConn.getAdditionalProperties()) {
                        if (sapProperty.getValue() != null && sapProperty.getValue().equals(oldValue)) {
                            sapProperty.setValue(newValue);
                            isModified = true;
                        }
                    }
                }
            }
        }
        return isModified;
    }

    private void updateParameters(DatabaseConnection dbConn, String oldValue, String newValue) {
        EMap<String, String> parameters = dbConn.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            for (Entry<String, String> entry : parameters.entrySet()) {
                if (entry != null) {
                    String value = entry.getValue();
                    if (StringUtils.equals(value, oldValue)) {
                        entry.setValue(newValue);
                    }
                }
            }
        }

        updateHadoopPropertiesForDbConnection(dbConn, oldValue, newValue);
    }

    private void updateHadoopPropertiesForDbConnection(DatabaseConnection dbConn, String oldValue, String newValue) {
        EMap<String, String> parameters = dbConn.getParameters();
        String databaseType = parameters.get(ConnParameterKeys.CONN_PARA_KEY_DB_TYPE);
        String hadoopProperties = "";
        if (databaseType != null) {
            if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
                hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
            } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)) {
                hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES);
            } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType)) {
                hadoopProperties = parameters.get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES);
            }
            List<Map<String, Object>> hadoopPropertiesList = HadoopRepositoryUtil.getHadoopPropertiesList(hadoopProperties);
            if (!hadoopPropertiesList.isEmpty()) {
                for (Map<String, Object> propertyMap : hadoopPropertiesList) {
                    String propertyValue = (String) propertyMap.get("VALUE");
                    if (propertyValue.equals(oldValue)) {
                        propertyMap.put("VALUE", newValue);
                    }
                }
                String hadoopPropertiesJson = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(hadoopPropertiesList);
                if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
                    dbConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES, hadoopPropertiesJson);
                } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)) {
                    dbConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES, hadoopPropertiesJson);
                } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType)) {
                    dbConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES, hadoopPropertiesJson);
                }
            }
        }
    }

    @Override
    public boolean accept(Connection connection) {
        if (connection instanceof DatabaseConnection || connection instanceof FileExcelConnection
                || connection instanceof DelimitedFileConnection || connection instanceof RegexpFileConnection
                || connection instanceof LdifFileConnection || connection instanceof PositionalFileConnection
                || connection instanceof XmlFileConnection || connection instanceof SalesforceSchemaConnection
                || connection instanceof WSDLSchemaConnection || connection instanceof SAPConnection) {
            return true;
        }
        return false;
    }

}
