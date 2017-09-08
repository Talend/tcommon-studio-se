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
package org.talend.core.model.metadata.designerproperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.database.EDatabase4DriverClassName;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.DatabaseConnStrUtil;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.BRMSConnection;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.Escape;
import org.talend.core.model.metadata.builder.connection.FieldSeparator;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.RowSeparator;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.metadata.designerproperties.PropertyConstants.CDCTypeMode;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.DatabaseConnectionItem;
import org.talend.core.model.repository.DragAndDropManager;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.model.utils.IDragAndDropServiceHandler;
import org.talend.core.runtime.i18n.Messages;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.PackageHelper;

/**
 * DOC wzhang class global comment. Detailled comment
 */
public class ComponentToRepositoryProperty {

    /**
     * DOC wzhang Comment method "setValue".
     * 
     * @param connection
     * @param node
     */
    public static boolean setValue(ConnectionItem connectionItem, INode node) {

        if (connectionItem == null || node == null) {
            return false;
        }
        // impossible to use OCI in oracle
        IElementParameter elementParameter = node.getElementParameter("CONNECTION_TYPE"); //$NON-NLS-1$
        if (elementParameter != null) {
            if ("ORACLE_OCI".equals(elementParameter.getValue())) { //$NON-NLS-1$
                Shell shell = Display.getCurrent().getActiveShell();
                String title = Messages.getString("ComponentToRepositoryProperty.error"); //$NON-NLS-1$
                String message = Messages.getString("ComponentToRepositoryProperty.ImpossibleUseOCI"); //$NON-NLS-1$
                MessageDialog.openError(shell, title, message);
                return false;
            }
        }
        IElementParameter propertyParam = null;
        for (IElementParameter param : node.getElementParameters()) {
            if (param.getFieldType() == EParameterFieldType.PROPERTY_TYPE) {
                if (propertyParam == null) {
                    // set as first one by default, since usually there is only one.
                    propertyParam = param;
                }
                IElementParameter childParam = param.getChildParameters().get("PROPERTY_TYPE");
                if (childParam != null && childParam.getValue().equals("REPOSITORY")) {
                    IElementParameter elemParam = param.getChildParameters().get("REPOSITORY_PROPERTY_TYPE");
                    if (connectionItem.getProperty().getId().equals(elemParam.getValue())) {
                        propertyParam = param;
                        break;
                    }
                }
            }
        }
        if (propertyParam == null) {
            return false;
        }

        if (connectionItem instanceof DatabaseConnectionItem) {
            // add url instance ------DataStringConnection
            DatabaseConnection conn = (DatabaseConnection) ((DatabaseConnectionItem) connectionItem).getConnection();
            // see bug in 18011, set url and driver_jar.
            setDatabaseType(conn, node);
            conn.setURL(DatabaseConnStrUtil.getURLString(conn));

            // see bug in feature 5998, set dbmsId.
            String repositoryType = node.getElementParameter("PROPERTY_TYPE").getRepositoryValue(); //$NON-NLS-1$
            if (repositoryType.startsWith("DATABASE") && repositoryType.contains(":")) { //$NON-NLS-1$ //$NON-NLS-2$
                String product = repositoryType.substring(repositoryType.indexOf(":") + 1); //$NON-NLS-1$
                // see bug in feature 17761.
                if (product.equals(EDatabaseTypeName.GENERAL_JDBC.getProduct())) {
                    String driverClass = getParameterValue(conn, node, node.getElementParameter("DRIVER_CLASS")); //$NON-NLS-1$
                    List<EDatabase4DriverClassName> driverClasses = EDatabase4DriverClassName.indexOfByDriverClass(driverClass);
                    if (driverClasses.size() > 0) { // use the first one
                        product = driverClasses.get(0).getDbType().getProduct();
                    } else {
                        product = EDatabaseTypeName.MYSQL.getProduct();
                    }
                }
                if (MetadataTalendType.getDefaultDbmsFromProduct(product) != null) {
                    String mapping = MetadataTalendType.getDefaultDbmsFromProduct(product).getId();
                    conn.setDbmsId(mapping);
                }
            }
        }
        for (IElementParameter param : node.getElementParameters()) {
            String repositoryValue = param.getRepositoryValue();
            if (repositoryValue != null) {
                if (param.getRepositoryProperty() != null && !param.getRepositoryProperty().equals(propertyParam.getName())) {
                    continue;
                }
                setValue(connectionItem.getConnection(), node, param);
            }
        }
        return true;
    }

    /**
     * 
     * DOC wzhang Comment method "setValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    public static void setValue(Connection connection, INode node, IElementParameter param) {
        if (connection == null || node == null || param == null) {
            return;
        } else if (connection instanceof XmlFileConnection) {
            setXmlFileValue((XmlFileConnection) connection, node, param);
        } else if (connection instanceof DatabaseConnection) {
            setDatabaseValue((DatabaseConnection) connection, node, param);
        } else if (connection instanceof EbcdicConnection) {
            setEbcdicValue((EbcdicConnection) connection, node, param);
        } else if (connection instanceof DelimitedFileConnection) {
            setDelimitedFileValue((DelimitedFileConnection) connection, node, param);
        } else if (connection instanceof LDAPSchemaConnection) {
            setLDAPSchemaValue((LDAPSchemaConnection) connection, node, param);
        } else if (connection instanceof WSDLSchemaConnection) {
            setWSDLSchemaValue((WSDLSchemaConnection) connection, node, param);
        } else if (connection instanceof LdifFileConnection) {
            setLdifFileValue((LdifFileConnection) connection, node, param);
        } else if (connection instanceof RegexpFileConnection) {
            setRegexpFileValue((RegexpFileConnection) connection, node, param);
        } else if (connection instanceof PositionalFileConnection) {
            setPositionalFileValue((PositionalFileConnection) connection, node, param);
        } else if (connection instanceof FileExcelConnection) {
            setFileExcelValue((FileExcelConnection) connection, node, param);
        } else if (connection instanceof SAPConnection) {
            setSAPValue((SAPConnection) connection, node, param);
        } else if (connection instanceof SalesforceSchemaConnection) {
            setSalesforceSchema((SalesforceSchemaConnection) connection, node, param);
        } else if (connection instanceof MDMConnection) {
            setMDMValue((MDMConnection) connection, node, param);
        } else if (connection instanceof BRMSConnection) {
            setBRMSValue((BRMSConnection) connection, node, param);
        } else if (connection instanceof HL7Connection) {
            setHL7Value((HL7Connection) connection, node, param);
        }
        for (IDragAndDropServiceHandler handler : DragAndDropManager.getHandlers()) {
            if (handler.canHandle(connection)) {
                handler.setComponentValue(connection, node, param);
            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "getParameterValue".
     * 
     * @param node
     * @param paramName
     * @return
     */
    public static String getParameterValue(Connection connection, INode node, IElementParameter param) {
        String originalValue = getParameterOriginalValue(connection, node, param);
        if (originalValue != null) {
            return TalendQuoteUtils.removeQuotes(originalValue);
        }
        return null;
    }

    private static String getParameterOriginalValue(Connection connection, INode node, IElementParameter param) {
        if (node != null || param != null) {
            if (param != null) {
                Object o = param.getValue();
                if (o instanceof String || o instanceof Boolean || o instanceof Integer || o instanceof Long
                        || o instanceof Character) {
                    String value = String.valueOf(o);
                    if (isConetxtParaMode(connection, value)) {
                        value = getContextOriginalValue(connection, node, value);
                    }
                    return value;
                } else if (o instanceof List && param.getName().equals("DRIVER_JAR")) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) o;
                    String userDir = System.getProperty("user.dir"); //$NON-NLS-1$
                    String pathSeparator = System.getProperty("file.separator"); //$NON-NLS-1$
                    String defaultPath = userDir + pathSeparator + "lib" + pathSeparator + "java"; //$NON-NLS-1$ //$NON-NLS-2$
                    Character comma = ';';
                    String symbol = "\\";
                    String jarspath = "";
                    for (int i = 0; i < list.size(); i++) {
                        jarspath = jarspath + defaultPath + symbol + list.get(i).get("JAR_NAME");
                        if (i < list.size() - 1) {
                            jarspath = jarspath + comma.toString();
                        }
                    }
                    return jarspath;
                }
            }
        }
        return null;
    }

    protected static String getValueFromRepositoryName(Connection connection, INode node, String repositoryName) {
        for (IElementParameter param : (List<IElementParameter>) node.getElementParameters()) {
            if (param.getRepositoryValue() != null) {
                if (param.getRepositoryValue().equals(repositoryName)) {
                    if (param.getFieldType().equals(EParameterFieldType.CLOSED_LIST)) {
                        String repositoryItem = getRepositoryItemFromRepositoryName(param, repositoryName);
                        if (isConetxtParaMode(connection, repositoryItem)) {
                            return getContextOriginalValue(connection, node, repositoryItem);
                        }
                        return repositoryItem;
                    } else {
                        String value = (String) param.getValue();
                        if (isConetxtParaMode(connection, value)) {
                            return getContextOriginalValue(connection, node, value);
                        }
                        return value;
                    }
                }
            }
        }
        return ""; //$NON-NLS-1$
    }

    protected static String getRepositoryItemFromRepositoryName(IElementParameter param, String repositoryName) {
        String value = (String) param.getValue();
        Object[] valuesList = param.getListItemsValue();
        String[] originalList = param.getListItemsDisplayName();
        for (int i = 0; i < valuesList.length; i++) {
            if (valuesList[i].equals(value)) {
                return originalList[i];
            }
        }

        return ""; //$NON-NLS-1$
    }

    private static Object getParameterObjectValue(INode node, String paramName) {
        if (node != null || paramName != null) {
            IElementParameter param = node.getElementParameter(paramName);
            if (param != null) {
                Object o = param.getValue();
                return o;
            }
        }
        return null;
    }

    /**
     * 
     * DOC wzhang Comment method "setDatabaseType".
     * 
     * @param connection
     * @param node
     */
    private static void setDatabaseType(DatabaseConnection connection, INode node) {
        IElementParameter parameter = node.getElementParameter("TYPE"); //$NON-NLS-1$
        if (parameter == null) {
            // GreePlum
            IElementParameter para = node.getElementParameter("PROPERTY"); //$NON-NLS-1$
            if (para != null) {
                if (para.getRepositoryValue().endsWith(EDatabaseTypeName.GREENPLUM.getProduct())) {
                    connection.setDatabaseType(EDatabaseTypeName.GREENPLUM.getDisplayName());
                    connection.setProductId(EDatabaseTypeName.GREENPLUM.getProduct());
                }
                // PostgresPlus
                if (para.getRepositoryValue().endsWith(EDatabaseTypeName.PLUSPSQL.getProduct())) {
                    connection.setDatabaseType(EDatabaseTypeName.PLUSPSQL.getDisplayName());
                    connection.setProductId(EDatabaseTypeName.PLUSPSQL.getProduct());
                }
                // jdbc
                if (para.getRepositoryValue().endsWith(EDatabaseTypeName.GENERAL_JDBC.getProduct())) {
                    connection.setDatabaseType(EDatabaseTypeName.GENERAL_JDBC.getDisplayName());
                    connection.setProductId(EDatabaseTypeName.GENERAL_JDBC.getProduct());
                }
                // vertica output component have no TYPE ElementParameter .
                if (para.getRepositoryValue().endsWith(EDatabaseTypeName.VERTICA.getProduct())) {
                    connection.setDatabaseType(EDatabaseTypeName.VERTICA.getDisplayName());
                    connection.setProductId(EDatabaseTypeName.VERTICA.getProduct());
                }
            }
            return;
        }
        // mysql
        else if (EDatabaseTypeName.MYSQL.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.MYSQL.getDisplayName());
            connection.setProductId(EDatabaseTypeName.MYSQL.getProduct());
        }
        // mssql
        else if (EDatabaseTypeName.MSSQL.getXmlName().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.MSSQL.getDisplayName());
            connection.setProductId(EDatabaseTypeName.MSSQL.getProduct());
        }
        // Exasolution
        else if (EDatabaseTypeName.EXASOL.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.EXASOL.getDisplayName());
            connection.setProductId(EDatabaseTypeName.EXASOL.getProduct());
        }
        // Psql
        else if (EDatabaseTypeName.PSQL.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.PSQL.getDisplayName());
            connection.setProductId(EDatabaseTypeName.PSQL.getProduct());
        }
        // PlusSql
        else if (EDatabaseTypeName.PLUSPSQL.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.PLUSPSQL.getDisplayName());
            connection.setProductId(EDatabaseTypeName.PLUSPSQL.getProduct());
        }
        // DB2
        else if (EDatabaseTypeName.IBMDB2.getProduct().equalsIgnoreCase(((String) parameter.getValue()).replace(' ', '_'))) {
            connection.setDatabaseType(EDatabaseTypeName.IBMDB2.getDisplayName());
            connection.setProductId(EDatabaseTypeName.IBMDB2.getProduct());
        }
        // Ingres
        else if (EDatabaseTypeName.INGRES.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.INGRES.getDisplayName());
            connection.setProductId(EDatabaseTypeName.INGRES.getProduct());
        }
        // Interbase
        else if (EDatabaseTypeName.INTERBASE.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.INTERBASE.getDisplayName());
            connection.setProductId(EDatabaseTypeName.INTERBASE.getProduct());
        }
        // Sqlite
        else if (EDatabaseTypeName.SQLITE.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.SQLITE.getDisplayName());
            connection.setProductId(EDatabaseTypeName.SQLITE.getProduct());
        }
        // Firebird
        else if (EDatabaseTypeName.FIREBIRD.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.FIREBIRD.getDisplayName());
            connection.setProductId(EDatabaseTypeName.FIREBIRD.getProduct());
        }
        // Informix
        else if (EDatabaseTypeName.INFORMIX.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.INFORMIX.getDisplayName());
            connection.setProductId(EDatabaseTypeName.INFORMIX.getProduct());
        }
        // Access
        else if (EDatabaseTypeName.ACCESS.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.ACCESS.getDisplayName());
            connection.setProductId(EDatabaseTypeName.ACCESS.getProduct());
        }
        // Teradata
        else if (EDatabaseTypeName.TERADATA.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.TERADATA.getDisplayName());
            connection.setProductId(EDatabaseTypeName.TERADATA.getProduct());
        }
        // AS400
        else if (EDatabaseTypeName.AS400.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.AS400.getDisplayName());
            connection.setProductId(EDatabaseTypeName.AS400.getProduct());
        }
        // Vertica
        else if (EDatabaseTypeName.VERTICA.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.VERTICA.getDisplayName());
            connection.setProductId(EDatabaseTypeName.VERTICA.getProduct());
        }
        // MaxDB
        else if (EDatabaseTypeName.MAXDB.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.MAXDB.getDisplayName());
            connection.setProductId(EDatabaseTypeName.MAXDB.getProduct());
        }
        // Paraccel
        else if (EDatabaseTypeName.PARACCEL.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.PARACCEL.getDisplayName());
            connection.setProductId(EDatabaseTypeName.PARACCEL.getProduct());
        }
        // Redshift
        else if (EDatabaseTypeName.REDSHIFT.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.REDSHIFT.getDisplayName());
            connection.setProductId(EDatabaseTypeName.REDSHIFT.getProduct());
        }
        // NeTezza
        else if (EDatabaseTypeName.NETEZZA.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            connection.setDatabaseType(EDatabaseTypeName.NETEZZA.getDisplayName());
            connection.setProductId(EDatabaseTypeName.NETEZZA.getProduct());
        }
        // Sybase
        else if (((String) parameter.getValue()).toLowerCase().startsWith(EDatabaseTypeName.SYBASEASE.getProduct().toLowerCase())) {
            parameter = node.getElementParameter("TYPE"); //$NON-NLS-1$
            if ("SybaseASE".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.SYBASEASE.getDisplayName());
                connection.setProductId(EDatabaseTypeName.SYBASEASE.getProduct());
            }
            // not exist in "DB Type" in Database Connection page.
            // else if ("SybaseIQ".equals(parameter.getValue())) {
            // connection.setDatabaseType(EDatabaseTypeName.SYBASEIQ.getDisplayName());
            // }
            return;
        }

        // oracle
        else if (EDatabaseTypeName.ORACLEFORSID.getProduct().equalsIgnoreCase((String) parameter.getValue())
                || EDatabaseTypeName.ORACLEFORSID.getXmlName().equalsIgnoreCase((String) parameter.getValue())
                || EDatabaseTypeName.ORACLESN.getXmlName().equalsIgnoreCase((String) parameter.getValue())
                || EDatabaseTypeName.ORACLE_OCI.getXmlName().equalsIgnoreCase((String) parameter.getValue())
                || EDatabaseTypeName.ORACLE_CUSTOM.getXmlName().equalsIgnoreCase((String) parameter.getValue())) {
            parameter = node.getElementParameter("CONNECTION_TYPE"); //$NON-NLS-1$
            // if ("ORACLE_OCI".equals(parameter.getValue())) {
            // }

            if ("ORACLE_SERVICE_NAME".equals(parameter.getValue()) || "service_name".equals(parameter.getValue())) { //$NON-NLS-1$ //$NON-NLS-2$
                connection.setDatabaseType(EDatabaseTypeName.ORACLESN.getDisplayName());
                connection.setProductId(EDatabaseTypeName.ORACLESN.getProduct());
            } else if ("ORACLE_SID".equals(parameter.getValue()) || "sid".equals(parameter.getValue())) { //$NON-NLS-1$  //$NON-NLS-2$
                connection.setDatabaseType(EDatabaseTypeName.ORACLEFORSID.getDisplayName());
                connection.setProductId(EDatabaseTypeName.ORACLESN.getProduct());
            } else if ("ORACLE_CUSTOM".equals(parameter.getValue()) || "rac".equals(parameter.getValue())) { //$NON-NLS-1$  //$NON-NLS-2$
                connection.setDatabaseType(EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName());
                connection.setProductId(EDatabaseTypeName.ORACLESN.getProduct());
            }
            return;
        }
        // HSql
        else if (EDatabaseTypeName.HSQLDB_SERVER.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            parameter = node.getElementParameter("RUNNING_MODE"); //$NON-NLS-1$
            if ("HSQLDB_SERVER".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.HSQLDB_SERVER.getDisplayName());
                connection.setProductId(EDatabaseTypeName.HSQLDB_SERVER.getProduct());
            } else if ("HSQLDB_WEBSERVER".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.HSQLDB_WEBSERVER.getDisplayName());
                connection.setProductId(EDatabaseTypeName.HSQLDB_WEBSERVER.getProduct());
            } else if ("HSQLDB_INPROGRESS_PERSISTENT".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getDisplayName());
                connection.setProductId(EDatabaseTypeName.HSQLDB_IN_PROGRESS.getProduct());
            }
            return;
        }

        // JavaDB
        else if (EDatabaseTypeName.JAVADB_EMBEDED.getProduct().equalsIgnoreCase((String) parameter.getValue())) {
            parameter = node.getElementParameter("FRAMEWORK_TYPE"); //$NON-NLS-1$
            if ("EMBEDED".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.JAVADB_EMBEDED.getDisplayName());
                connection.setProductId(EDatabaseTypeName.JAVADB_EMBEDED.getProduct());
            } else if ("JCCJDBC".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.JAVADB_JCCJDBC.getDisplayName());
                connection.setProductId(EDatabaseTypeName.JAVADB_JCCJDBC.getProduct());
            } else if ("DERBYCLIENT".equals(parameter.getValue())) { //$NON-NLS-1$
                connection.setDatabaseType(EDatabaseTypeName.JAVADB_DERBYCLIENT.getDisplayName());
                connection.setProductId(EDatabaseTypeName.JAVADB_DERBYCLIENT.getProduct());
            }
            return;
        }

        // DB
        else if (node.getComponent().getName().startsWith("tDBInput") || node.getComponent().getName().startsWith("tDBOutput")) { //$NON-NLS-1$ //$NON-NLS-2$
            parameter = node.getElementParameter("PROPERTY"); //$NON-NLS-1$
            if (parameter.getRepositoryValue().endsWith(EDatabaseTypeName.GODBC.getProduct())) {
                connection.setDatabaseType(EDatabaseTypeName.GODBC.getDisplayName());
                connection.setProductId(EDatabaseTypeName.GODBC.getProduct());
            }
        }

        // SAX
        // can not find corresponding component. also not exist in EDatabaseType.java.
    }

    /**
     * 
     * DOC wzhang Comment method "setDatabaseValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setDatabaseValue(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("USERNAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUsername(value);
            }
        }
        if ("PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                String pwd = TalendQuoteUtils.checkAndRemoveBackslashes(value);
                connection.setRawPassword(TalendQuoteUtils.removeQuotes(pwd));
            }
        }
        if ("SERVER_NAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setServerName(value);
            }
        }
        if ("PORT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPort(value);
            }
        }
        if ("SID".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setSID(value);
            }
        }
        if ("SCHEMA".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                if (connection.getDatabaseType().equals(EDatabaseTypeName.ORACLEFORSID.getDisplayName())) {
                    connection.setUiSchema(TalendQuoteUtils.removeQuotes(value.toUpperCase()));
                } else {
                    connection.setUiSchema(TalendQuoteUtils.removeQuotes(value));
                }
            }
        }
        if ("CDC_TYPE_MODE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null && Boolean.valueOf(value).booleanValue()) {
                connection.setCdcTypeMode(CDCTypeMode.LOG_MODE.getName());
            }
        }
        // for feature 11674
        if ("DBPATH".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDBRootPath(value);
            }
        }
        if ("PROPERTIES_STRING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setAdditionalParams(value);
            }
        }
        if ("FILE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFileFieldName(value);
            }
        }
        if ("DATASOURCE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDatasourceName(value);
            }
        }
        if ("SERVER_NAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setServerName(value);
            }
        }

        if (connection.getDatabaseType().equals(EDatabaseTypeName.ORACLEFORSID.getDisplayName())) {
            setDatabaseValueForOracleSid(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.ORACLESN.getDisplayName())) {
            setDatabaseValueForOracleSeverName(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.ACCESS.getDisplayName())) {
            setDatabaseValueForAccess(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.AS400.getDisplayName())) {
            setDatabaseValueForAs400(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.VERTICA.getDisplayName())) {
            setDatabaseValueForVertica(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.JAVADB.getDisplayName())
                || connection.getDatabaseType().equals(EDatabaseTypeName.JAVADB_EMBEDED.getDisplayName())
                || connection.getDatabaseType().equals(EDatabaseTypeName.JAVADB_JCCJDBC.getDisplayName())
                || connection.getDatabaseType().equals(EDatabaseTypeName.JAVADB_DERBYCLIENT.getDisplayName())) {
            setDatabaseValueForjavadb(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.NETEZZA.getDisplayName())) {
            setDatabaseValueForNetezza(connection, node, param);
        }
        if (connection.getDatabaseType().equals(EDatabaseTypeName.GENERAL_JDBC.getDisplayName())) {
            setDatabaseValueForJdbc(connection, node, param);
        }
    }

    /**
     * 
     * DOC wzhang Comment method "setDatabaseValueForOracleSid".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setDatabaseValueForOracleSid(DatabaseConnection connection, INode node, IElementParameter param) {

        if ("DB_VERSION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            String dbVersionName = EDatabaseVersion4Drivers.getDbVersionName(EDatabaseTypeName.ORACLEFORSID, value);
            if (value != null) {
                connection.setDbVersionString(dbVersionName);
            }
        }
        if ("SID".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            if (param != null && "ORACLE_OCI".equals(param.getValue())) { //$NON-NLS-1$
                String value = getParameterValue(connection, node, node.getElementParameter("LOCAL_SERVICE_NAME")); //$NON-NLS-1$
                if (value != null) {
                    connection.setSID(value);
                }
            } else {
                String value = getParameterValue(connection, node, node.getElementParameter("DBNAME")); //$NON-NLS-1$
                if (value != null) {
                    connection.setSID(value);
                }
            }
        }
    }

    private static void setDatabaseValueForOracleSeverName(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("DB_VERSION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            String dbVersionName = EDatabaseVersion4Drivers.getDbVersionName(EDatabaseTypeName.ORACLESN, value);
            if (value != null) {
                connection.setDbVersionString(dbVersionName);
            }
        }
        if ("SID".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            if (param != null && "ORACLE_OCI".equals(param.getValue())) { //$NON-NLS-1$
                String value = getParameterValue(connection, node, node.getElementParameter("LOCAL_SERVICE_NAME")); //$NON-NLS-1$
                if (value != null) {
                    connection.setSID(value);
                }
            } else {
                String value = getParameterValue(connection, node, node.getElementParameter("DBNAME")); //$NON-NLS-1$
                if (value != null) {
                    connection.setSID(value);
                }
            }
        }
    }

    private static void setDatabaseValueForAs400(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("DB_VERSION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            String dbVersionName = EDatabaseVersion4Drivers.getDbVersionName(EDatabaseTypeName.AS400, value);
            if (value != null) {
                connection.setDbVersionString(dbVersionName);
            }
        }

    }

    private static void setDatabaseValueForVertica(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("DB_VERSION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            String dbVersionName = EDatabaseVersion4Drivers.getDbVersionName(EDatabaseTypeName.VERTICA, value);
            if (value != null) {
                connection.setDbVersionString(dbVersionName);
            }
        }
    }

    private static void setDatabaseValueForAccess(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("DB_VERSION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                EDatabaseVersion4Drivers version = EDatabaseVersion4Drivers.indexOfByVersion(value);
                if (version != null) {
                    connection.setDbVersionString(version.getVersionValue());
                }
            }
        }
    }

    private static void setDatabaseValueForjavadb(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("SID".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setSID(value);
            }
        }
        if ("DIRECTORY".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDBRootPath(value);
            }
        }
    }

    private static void setDatabaseValueForNetezza(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("DBNAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDatasourceName(value);
            }
        }
    }

    private static void setDatabaseValueForJdbc(DatabaseConnection connection, INode node, IElementParameter param) {
        if ("URL".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setURL(value);
            }
        }
        if ("DRIVER_JAR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDriverJarPath(value);
            }
        }
        if ("DRIVER_CLASS".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDriverClass(value);
            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "setXmlFileValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setXmlFileValue(XmlFileConnection connection, INode node, IElementParameter param) {

        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setXmlFilePath(value);
            }
        }
        if ("ENCODING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEncoding(value);
            }
        }
        EList emfSchemaList = connection.getSchema();
        if (emfSchemaList.size() < 1) {
            emfSchemaList.add(ConnectionFactory.eINSTANCE.createXmlXPathLoopDescriptor());
        }

        XmlXPathLoopDescriptor xmlDesc = (XmlXPathLoopDescriptor) emfSchemaList.get(0);
        if ("XPATH_QUERY".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                xmlDesc.setAbsoluteXPathQuery(value);
            }
        }
        if ("LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, node.getElementParameter("LIMIT")); //$NON-NLS-1$
            if (value != null && value.trim().length() > 0) {
                xmlDesc.setLimitBoucle(Integer.valueOf(value));
            }
        }
        if ("XML_MAPPING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            if (param != null) {
                EList schemaTargets = xmlDesc.getSchemaTargets();
                List<Map<String, Object>> tableInfo = (List<Map<String, Object>>) param.getValue();
                for (Map<String, Object> mapObject : tableInfo) {
                    String schema = (String) mapObject.get("SCHEMA_COLUMN"); //$NON-NLS-1$
                    if (schema != null) {
                        String query = (String) mapObject.get("QUERY"); //$NON-NLS-1$
                        SchemaTarget schemaTarget = ConnectionFactory.eINSTANCE.createSchemaTarget();
                        schemaTargets.add(schemaTarget);
                        schemaTarget.setTagName(schema);
                        schemaTarget.setRelativeXPathQuery(TalendQuoteUtils.removeQuotes(query));
                    }
                }
            }
        }

    }

    /**
     * 
     * DOC wzhang Comment method "setLDAPSchemaValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setLDAPSchemaValue(LDAPSchemaConnection connection, INode node, IElementParameter param) {
        if ("HOST".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = (getParameterValue(connection, node, param)).replaceAll("\\\\\\\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$ 
            if (value != null) {
                connection.setHost(value);
            }
        }
        if ("PORT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPort(value);
            }
        }
        if ("BASEDN".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = (getParameterValue(connection, node, param)).replaceAll("\\\\\\\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$ 
            if (value != null) {
                connection.setSelectedDN(value);
            }
        }
        if ("PROTOCOL".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEncryptionMethodName(value);
            }
        }

        if ("AUTHENTIFICATION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            Object o = param.getValue();
            if (o != null && o instanceof Boolean) {
                connection.setUseAuthen((Boolean) o);
            }
        }

        if ("USER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = (getParameterValue(connection, node, param)).replaceAll("\\\\\\\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$ 
            if (value != null) {
                connection.setBindPrincipal(value);
            }
        }
        if ("PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = (getParameterValue(connection, node, param)).replaceAll("\\\\\\\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$ 
            if (value != null) {
                connection.setBindPassword(connection.getValue(value, true));
            }
        }
        if ("FILTER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilter(value);
            }
        }
        if ("ALIASES".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setAliases(value);
            }
        }
        if ("REFERRALS".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setReferrals(value);
            }
        }
        if ("COLUMN_COUNT_LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setCountLimit(value);
            }
        }
        if ("TIME_OUT_LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setTimeOutLimit(value);
            }
        }
    }

    private static void setWSDLSchemaValue(WSDLSchemaConnection connection, INode node, IElementParameter param) {
        if ("ENDPOINT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setWSDL(value);
            }
        }
        if ("WSDLURL".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEndpointURI(value);
            }
        }
        if ("NEED_AUTH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setNeedAuth(Boolean.valueOf(value));
            }
        }
        if ("AUTH_USERNAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUserName(value);
            }
        }
        if ("AUTH_PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPassword(connection.getValue(value, true));
            }
        }
        if ("USE_PROXY".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUseProxy(Boolean.valueOf(value));
            }
        }
        if ("PROXY_HOST".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyHost(value);
            }
        }
        if ("PROXY_PORT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyPort(value);
            }
        }
        if ("PROXY_USERNAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyUser(value);
            }
        }
        if ("PROXY_PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyPassword(connection.getValue(value, true));
            }
        }
        if ("METHOD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setMethodName(value);
            }
        }
        if ("TIMEOUT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setTimeOut(Integer.valueOf(value));
            }
        }
        if ("PORT_NAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPortName(value);
            }
        }
        if ("WSDL_PARAMS".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            Object value = param.getValue();
            if (value != null && value instanceof ArrayList) {
                ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) value;
                ArrayList<String> result = new ArrayList<String>();
                for (HashMap<String, String> m : list) {
                    Iterator<Map.Entry<String, String>> it = m.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String> entry = it.next();
                        result.add(entry.getValue());
                    }
                }
                connection.setParameters(result);
            }
        }
        if ("SERVICE_CONFIGURATION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            IExternalNode enode = node.getExternalNode();
            if (enode != null) {
                List<IMetadataTable> imetalist = enode.getMetadataList();
                if (imetalist != null && !imetalist.isEmpty()) {
                    EList<orgomg.cwm.objectmodel.core.Package> packageList = connection.getDataPackage();
                    if (packageList != null && !packageList.isEmpty()) {
                        List<MetadataTable> metalist = new ArrayList<MetadataTable>(imetalist.size());
                        for (IMetadataTable imetatable : imetalist) {
                            MetadataTable metatable = ConvertionHelper.convert(imetatable);
                            if ("FLOW".equals(imetatable.getAttachedConnector())) { //$NON-NLS-1$
                                metatable.setLabel("Input"); //$NON-NLS-1$
                            } else if ("OUTPUT".equals(imetatable.getAttachedConnector())) { //$NON-NLS-1$
                                metatable.setLabel("Output"); //$NON-NLS-1$
                            }
                            metalist.add(metatable);
                        }
                        orgomg.cwm.objectmodel.core.Package pkg = packageList.get(0);
                        pkg.getOwnedElement().clear();
                        PackageHelper.addMetadataTable(metalist, packageList.get(0));
                    }
                }
            }
        }
    }

    private static void setEbcdicValue(EbcdicConnection connection, INode node, IElementParameter param) {
        if ("DATA_FILE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDataFile(value);
            }
        }
        if ("XC2J_FILE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setMidFile(value);
            }
        }
        if ("ENCODING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEncoding(value);
            }
        }
    }

    private static void setMDMValue(MDMConnection connection, INode node, IElementParameter param) {
        if ("MDM_VERSION".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setVersion(value);
            }
        }

        if ("USERNAME".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUsername(value);
            }
        }

        if ("PASSWORD".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                ConnectionHelper.setPassword(connection, value);
            }
        }

        if ("MDMURL".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setServerUrl(value);
            }
        }

        if ("UNIVERSE".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUniverse(value);
            }
        }

        if ("DATAMODEL".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDatamodel(value);
            }
        }

        if ("DATACLUSTER".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDatacluster(value);
            }
        }
    }

    private static void setBRMSValue(BRMSConnection connection, INode node, IElementParameter param) {
        if ("XML_FIELD".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setXmlField(value);
            }
        }

        if ("GUVNOR_URL".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUrlName(value);
            }
        }

        if ("TAC_WEBAPP".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setTacWebappName(value);
            }
        }

        if ("CLASS_NAME".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setClassName(value);
            }
        }

        if ("GUVNOR_PACKAGE".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPackage(value);
            }
        }

        if ("MODULE_USED".equals(param.getRepositoryValue())) {//$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setModuleUsed(value);
            }
        }
    }

    private static void setHL7Value(HL7Connection connection, INode node, IElementParameter param) {
        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilePath(value);
            }
        }
        if ("START_MSG".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setStartChar(value);
            }
        }
        if ("END_MSG".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEndChar(value);
            }
        }

    }

    private static void setLdifFileValue(LdifFileConnection connection, INode node, IElementParameter param) {
        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilePath(value);
            }
        }
    }

    private static void setFileExcelValue(FileExcelConnection connection, INode node, IElementParameter param) {
        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilePath(value);
            }
        }
        if ("SELECT_ALL_SHEETS".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setSelectAllSheets(Boolean.valueOf(value).booleanValue());
            }
        }

        if ("ADVANCED_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setAdvancedSpearator(Boolean.valueOf(value).booleanValue());
            }
        }
        if ("HEADER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setHeaderValue(value);
            }
        }
        if ("FOOTER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFooterValue(value);
            }
        }
        if ("LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setLimitValue(value);
            }
        }
        if ("FIRST_COLUMN".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFirstColumn(value);
            }
        }
        if ("LAST_COLUMN".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setLastColumn(value);
            }
        }

        if ("THOUSANDS_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setThousandSeparator(value);
            }
        }
        if ("DECIMAL_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setDecimalSeparator(value);
            }
        }
        if ("ENCODING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEncoding(value);
            }
        }

    }

    /**
     * 
     * DOC wzhang Comment method "setDelimitedFileValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setDelimitedFileValue(DelimitedFileConnection connection, INode node, IElementParameter param) {
        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilePath(value);
            }
        }
        if ("ROW_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            if (param.isShow(node.getElementParameters())) {
                String value = getParameterValue(connection, node, param);
                if (value != null) {
                    // set the type
                    RowSeparator rowSeparator = null;
                    if ("\\n".equals(value)) { //$NON-NLS-1$
                        rowSeparator = RowSeparator.STANDART_EOL_LITERAL;
                    } else {
                        rowSeparator = RowSeparator.CUSTOM_STRING_LITERAL;
                        value = getParameterOriginalValue(connection, node, param);
                    }
                    connection.setRowSeparatorType(rowSeparator);

                    // set the value
                    connection.setRowSeparatorValue(value);
                }
            }
        }
        if ("FIELD_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                // set the type
                FieldSeparator separatorType = null;
                if (";".equals(value)) { //$NON-NLS-1$
                    separatorType = FieldSeparator.SEMICOLON_LITERAL;
                } else if (",".equals(value)) { //$NON-NLS-1$
                    separatorType = FieldSeparator.COMMA_LITERAL;
                } else if ("\\t".equals(value)) { //$NON-NLS-1$
                    separatorType = FieldSeparator.TABULATION_LITERAL;
                } else if (" ".equals(value)) { //$NON-NLS-1$
                    separatorType = FieldSeparator.SPACE_LITERAL;
                } else if ("''".equals(value)) { //$NON-NLS-1$
                    separatorType = FieldSeparator.ALT_65_LITERAL;
                } else {
                    separatorType = FieldSeparator.CUSTOM_UTF8_LITERAL;
                    // custom string, need to reserve the quota
                    value = getParameterOriginalValue(connection, node, param);
                }
                connection.setFieldSeparatorType(separatorType);

                // set the value
                connection.setFieldSeparatorValue(value);
            }
        }
        if ("CSV_OPTION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setCsvOption(Boolean.valueOf(value).booleanValue());
            }
            if (connection.isCsvOption()) {
                connection.setEscapeType(Escape.CSV);
            } else {
                connection.setEscapeType(Escape.DELIMITED);
            }
        }
        if ("ESCAPE_CHAR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterOriginalValue(connection, node, param);
            if (value != null) {
                // the tFileInputDelimited, the default value of Escape char is ["""] rather than ["\""]
                // so... need some format..
                if ("\"\"\"".equals(value)) { //$NON-NLS-1$
                    value = "\"\\\"\""; //$NON-NLS-1$
                }
                connection.setEscapeChar(value);
            }
        }
        if ("TEXT_ENCLOSURE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterOriginalValue(connection, node, param);
            if (value != null) {
                // the tFileInputDelimited, the default value of text enclosure is ["""] rather than ["\""]
                // so... need some format..
                if ("\"\"\"".equals(value)) { //$NON-NLS-1$
                    value = "\"\\\"\""; //$NON-NLS-1$
                }
                connection.setTextEnclosure(value);
            }
        }
        if ("HEADER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setHeaderValue(value);
            }
        }

        if ("FOOTER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFooterValue(value);
            }
        }
        if ("LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setLimitValue(value);
            }
        }
        if ("REMOVE_EMPTY_ROW".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setRemoveEmptyRow(Boolean.valueOf(value).booleanValue());
            }
        }
        if ("ENCODING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEncoding(value);
            }
        }
        if ("SPLITRECORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setSplitRecord(Boolean.valueOf(value).booleanValue());
            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "setPositionalFileValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */

    private static void setPositionalFileValue(PositionalFileConnection connection, INode node, IElementParameter param) {
        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilePath(value);
            }
        }
        if ("ROW_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setRowSeparatorValue(value);
            }
        }
        if ("PATTERN".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFieldSeparatorValue(value);
            }
        }
        if ("REMOVE_EMPTY_ROW".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setRemoveEmptyRow(Boolean.valueOf(value).booleanValue());
            }
        }
        if ("HEADER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setHeaderValue(value);
            }
        }
        if ("FOOTER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFooterValue(value);
            }
        }
        if ("LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setLimitValue(value);
            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "setRegexpFileValue".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setRegexpFileValue(RegexpFileConnection connection, INode node, IElementParameter param) {
        if ("FILE_PATH".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFilePath(value);
            }
        }
        if ("ROW_SEPARATOR".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setRowSeparatorValue(value);
            }
        }
        if ("REGEXP".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFieldSeparatorValue(value);
            }
        }
        if ("HEADER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setHeaderValue(value);
            }
        }
        if ("FOOTER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setFooterValue(value);
            }
        }
        if ("LIMIT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setLimitValue(value);
            }
        }
        if ("REMOVE_EMPTY_ROW".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setRemoveEmptyRow(Boolean.valueOf(value).booleanValue());
            }
        }
        if ("ENCODING".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setEncoding(value);
            }
        }
    }

    /*
     * SAP
     */
    private static void setSAPValue(SAPConnection connection, INode node, IElementParameter param) {
        if ("CLIENT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setClient(value);
            }
        }
        if ("PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPassword(connection.getValue(value, true));
            }
        }
        if ("LANGUAGE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setLanguage(value);
            }
        }
        if ("HOSTNAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setHost(value);
            }
        }
        if ("USERID".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUsername(value);
            }
        }
        if ("SYSTEMNUMBER".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setSystemNumber(value);
            }
        }
    }

    /**
     * 
     * DOC wzhang Comment method "setSalesforceSchema".
     * 
     * @param connection
     * @param node
     * @param repositoryValue
     */
    private static void setSalesforceSchema(SalesforceSchemaConnection connection, INode node, IElementParameter param) {

        if ("ENDPOINT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setWebServiceUrl(value);
            }
        }
        if ("USER_NAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setUserName(value);
            }
        }
        if ("PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setPassword(connection.getValue(value, true));
            }
        }
        if ("MODULENAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setModuleName(value);
            }
        }
        if ("QUERY_CONDITION".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setQueryCondition(value);
            }

        }
        if ("BATCH_SIZE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            connection.setBatchSize(value);
            // add for feature 7507
        }
        if ("UES_PROXY".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            connection.setUseProxy(Boolean.valueOf(value));
        }
        if ("PROXY_HOST".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyHost(value);
            }

        }
        if ("PROXY_PORT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyPort(value);
            }

        }
        if ("PROXY_USERNAME".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyUsername(value);
            }

        }
        if ("PROXY_PASSWORD".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setProxyPassword(connection.getValue(value, true));
            }

        }
        if ("TIMEOUT".equals(param.getRepositoryValue())) { //$NON-NLS-1$
            String value = getParameterValue(connection, node, param);
            if (value != null) {
                connection.setTimeOut(value);
            }

        }
    }

    /**
     * wzhang Comment method "isConetxtParaMode".
     */
    private static boolean isConetxtParaMode(Connection connection, String value) {
        if (value == null) {
            return false;
        }
        if (connection.isContextMode() && ContextParameterUtils.isContainContextParam(value)) {
            return true;
        }
        return false;
    }

    /**
     * wzhang Comment method "getContextOriginalValue".
     */
    private static String getContextOriginalValue(Connection connection, INode node, String value) {
        if (!isConetxtParaMode(connection, value)) {
            String variable = ContextParameterUtils.getVariableFromCode(value);
            IContextManager contextManager = node.getProcess().getContextManager();
            IContext context = contextManager.getContext(value);
            List<IContextParameter> contextParameterList = context.getContextParameterList();
            for (IContextParameter contextPara : contextParameterList) {
                String contextName = contextPara.getName();
                if (contextName != null && contextName.equals(variable)) {
                    value = contextPara.getValue();
                }
            }
        }
        return value;
    }
}
