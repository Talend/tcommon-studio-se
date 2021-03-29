// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

public class RepositoryContextUpdateService extends AbstractRepositoryContextUpdateService {

    @Override
    public boolean updateContextParameter(Connection conn, String oldValue, String newValue) {
        boolean isModified = false;
        if (conn.isContextMode()) {
            if (conn instanceof DatabaseConnection) {
                return updateDatabaseConnectinParam((DatabaseConnection) conn, oldValue, newValue);
            }
            if (conn instanceof FileExcelConnection) {
                return updateFileExcelConnectionParam((FileExcelConnection) conn, oldValue, newValue);
            }
            if (conn instanceof DelimitedFileConnection) {
                return updatDelimitedFileConnectionParam((DelimitedFileConnection) conn, oldValue, newValue);
            }
            if (conn instanceof RegexpFileConnection) {
                return updateRegexpFileConnectionParam((RegexpFileConnection) conn, oldValue, newValue);
            }
            if (conn instanceof LdifFileConnection) {
                return updateLdifFileConnectionParam((LdifFileConnection) conn, oldValue, newValue);
            }
            if (conn instanceof PositionalFileConnection) {
                return updatePositionalFileConnectionParam((PositionalFileConnection) conn, oldValue, newValue);
            }
            if (conn instanceof XmlFileConnection) {
                return updateXmlFileConnectionParam((XmlFileConnection) conn, oldValue, newValue);
            }
            if (conn instanceof SalesforceSchemaConnection) {
                return updateSalesforceSchemaConnectionParam((SalesforceSchemaConnection) conn, oldValue, newValue);
            }
            if (conn instanceof WSDLSchemaConnection) {
                return updateWSDLSchemaConnectionParam((WSDLSchemaConnection) conn, oldValue, newValue);
            }
            if (conn instanceof SAPConnection) {
                return updateSAPConnectionParam((SAPConnection) conn, oldValue, newValue);
            }
        }
        return isModified;
    }

    private boolean updateParameters(DatabaseConnection dbConn, String oldValue, String newValue) {
        boolean isModified = false;
        EMap<String, String> parameters = dbConn.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            for (Entry<String, String> entry : parameters.entrySet()) {
                if (entry != null) {
                    String value = entry.getValue();
                    if (StringUtils.equals(value, oldValue)) {
                        entry.setValue(newValue);
                        isModified = true;
                    }
                }
            }
        }

        boolean hadoopUpdateResult = updateHadoopPropertiesForDbConnection(dbConn, oldValue, newValue);
        return isModified || hadoopUpdateResult;
    }

    private boolean updateHadoopPropertiesForDbConnection(DatabaseConnection dbConn, String oldValue, String newValue) {
        boolean isModified = false;
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
                        isModified = true;
                    }
                }
                String hadoopPropertiesJson = HadoopRepositoryUtil.getHadoopPropertiesJsonStr(hadoopPropertiesList);
                if (EDatabaseConnTemplate.HIVE.getDBDisplayName().equals(databaseType)) {
                    dbConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES, hadoopPropertiesJson);
                    isModified = true;
                } else if (EDatabaseConnTemplate.HBASE.getDBDisplayName().equals(databaseType)) {
                    dbConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES, hadoopPropertiesJson);
                    isModified = true;
                } else if (EDatabaseConnTemplate.MAPRDB.getDBDisplayName().equals(databaseType)) {
                    dbConn.getParameters().put(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES, hadoopPropertiesJson);
                    isModified = true;
                }
            }
        }
        return isModified;
    }

	private boolean updateDatabaseConnectinParam(DatabaseConnection dbConn, String oldValue, String newValue) {
		boolean compPropertiesResult = updateCompPropertiesContextParameter(dbConn, oldValue, newValue);
		boolean isModified = false;
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
			// Added by Marvin Wang on Nov.7, 2012 for bug TDI-12596, because schema can not
			// be
			// propagated to metadata db.
			dbConn.setUiSchema(newValue);
			isModified = true;
		} else if (dbConn.getDatasourceName() != null && dbConn.getDatasourceName().equals(oldValue)) {
            dbConn.setDatasourceName(newValue);
            isModified = true;
        } else {
			isModified = updateParameters(dbConn, oldValue, newValue);
		}
		return isModified || compPropertiesResult;
	}

    private boolean updateFileExcelConnectionParam(FileExcelConnection conn, String oldValue, String newValue) {
        boolean isModified = false;
        if (conn.getFirstColumn() != null && conn.getFirstColumn().equals(oldValue)) {
            conn.setFirstColumn(newValue);
            isModified = true;
        } else if (conn.getLastColumn() != null && conn.getLastColumn().equals(oldValue)) {
            conn.setLastColumn(newValue);
            isModified = true;
        } else if (conn.getThousandSeparator() != null && conn.getThousandSeparator().equals(oldValue)) {
            conn.setThousandSeparator(newValue);
            isModified = true;
        } else if (conn.getDecimalSeparator() != null && conn.getDecimalSeparator().equals(oldValue)) {
            conn.setDecimalSeparator(newValue);
            isModified = true;
        } else if (conn.getFilePath() != null && conn.getFilePath().equals(oldValue)) {
            conn.setFilePath(newValue);
            isModified = true;
        } else if (conn.getEncoding() != null && conn.getEncoding().equals(oldValue)) {
            conn.setEncoding(newValue);
            isModified = true;
        } else if (conn.getLimitValue() != null && conn.getLimitValue().equals(oldValue)) {
            conn.setLimitValue(newValue);
            isModified = true;
        } else if (conn.getHeaderValue() != null && conn.getHeaderValue().equals(oldValue)) {
            conn.setHeaderValue(newValue);
            isModified = true;
        } else if (conn.getFooterValue() != null && conn.getFooterValue().equals(oldValue)) {
            conn.setFooterValue(newValue);
            isModified = true;
        }
        return isModified;
    }

    private boolean updatDelimitedFileConnectionParam(DelimitedFileConnection conn, String oldValue, String newValue) {
        boolean isModified = false;
        if (conn.getFilePath() != null && conn.getFilePath().equals(oldValue)) {
            conn.setFilePath(newValue);
            isModified = true;
        } else if (conn.getEncoding() != null && conn.getEncoding().equals(oldValue)) {
            conn.setEncoding(newValue);
            isModified = true;
        } else if (conn.getLimitValue() != null && conn.getLimitValue().equals(oldValue)) {
            conn.setLimitValue(newValue);
            isModified = true;
        } else if (conn.getHeaderValue() != null && conn.getHeaderValue().equals(oldValue)) {
            conn.setHeaderValue(newValue);
            isModified = true;
        } else if (conn.getFooterValue() != null && conn.getFooterValue().equals(oldValue)) {
            conn.setFooterValue(newValue);
            isModified = true;
        } else if (conn.getRowSeparatorValue() != null && conn.getRowSeparatorValue().equals(oldValue)) {
            conn.setRowSeparatorValue(newValue);
            isModified = true;
        } else if (conn.getFieldSeparatorValue() != null && conn.getFieldSeparatorValue().equals(oldValue)) {
            conn.setFieldSeparatorValue(newValue);
            isModified = true;
        }
        return isModified;
    }

    private boolean updateRegexpFileConnectionParam(RegexpFileConnection conn, String oldValue, String newValue) {
        boolean isModified = false;
        if (conn.getFilePath() != null && conn.getFilePath().equals(oldValue)) {
            conn.setFilePath(newValue);
            isModified = true;
        } else if (conn.getEncoding() != null && conn.getEncoding().equals(oldValue)) {
            conn.setEncoding(newValue);
            isModified = true;
        } else if (conn.getLimitValue() != null && conn.getLimitValue().equals(oldValue)) {
            conn.setLimitValue(newValue);
            isModified = true;
        } else if (conn.getHeaderValue() != null && conn.getHeaderValue().equals(oldValue)) {
            conn.setHeaderValue(newValue);
            isModified = true;
        } else if (conn.getFooterValue() != null && conn.getFooterValue().equals(oldValue)) {
            conn.setFooterValue(newValue);
            isModified = true;
        } else if (conn.getRowSeparatorValue() != null && conn.getRowSeparatorValue().equals(oldValue)) {
            conn.setRowSeparatorValue(newValue);
            isModified = true;
        } else if (conn.getFieldSeparatorValue() != null && conn.getFieldSeparatorValue().equals(oldValue)) {
            conn.setFieldSeparatorValue(newValue);
            isModified = true;
        }

        return isModified;
    }

    private boolean updateLdifFileConnectionParam(LdifFileConnection conn, String oldValue, String newValue) {
        boolean isModified = false;
        if (conn.getFilePath() != null && conn.getFilePath().equals(oldValue)) {
            conn.setFilePath(newValue);
            isModified = true;
        }
        return isModified;
    }

    private boolean updatePositionalFileConnectionParam(PositionalFileConnection dbConn, String oldValue, String newValue) {
        boolean isModified = false;
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
        return isModified;
    }

    private boolean updateXmlFileConnectionParam(XmlFileConnection dbConn, String oldValue, String newValue) {
        boolean isModified = false;
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
        return isModified;
    }

    private boolean updateSalesforceSchemaConnectionParam(SalesforceSchemaConnection ssConn, String oldValue, String newValue) {
        boolean isModified = false;
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
        return isModified;
    }

    private boolean updateWSDLSchemaConnectionParam(WSDLSchemaConnection dbConn, String oldValue, String newValue) {
        boolean isModified = false;
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
        return isModified;
    }

    private boolean updateSAPConnectionParam(SAPConnection sapConn, String oldValue, String newValue) {
        boolean isModified = false;
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
        return isModified;
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
