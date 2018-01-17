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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.ui.utils.PathUtils;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.database.conn.template.EDatabaseConnTemplate;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.hadoop.version.custom.ECustomVersionGroup;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.EMetadataEncoding;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.MappingType;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.MetadataToolHelper;
import org.talend.core.model.metadata.MultiSchemasUtil;
import org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty;
import org.talend.core.model.metadata.builder.connection.BRMSConnection;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.ConceptTarget;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.DelimitedFileConnection;
import org.talend.core.model.metadata.builder.connection.EDIFACTColumn;
import org.talend.core.model.metadata.builder.connection.EDIFACTConnection;
import org.talend.core.model.metadata.builder.connection.EbcdicConnection;
import org.talend.core.model.metadata.builder.connection.FTPConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.FileExcelConnection;
import org.talend.core.model.metadata.builder.connection.HL7Connection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.MdmConceptType;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.PositionalFileConnection;
import org.talend.core.model.metadata.builder.connection.RegexpFileConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParamData;
import org.talend.core.model.metadata.builder.connection.SAPFunctionParameter;
import org.talend.core.model.metadata.builder.connection.SAPFunctionUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceModuleUnit;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.WSDLParameter;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.metadata.designerproperties.PropertyConstants.CDCTypeMode;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.repository.DragAndDropManager;
import org.talend.core.model.update.UpdatesConstants;
import org.talend.core.model.utils.IDragAndDropServiceHandler;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.service.IMetadataManagmentService;
import org.talend.core.service.IMetadataManagmentUiService;
import org.talend.core.utils.KeywordsValidator;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.TaggedValueHelper;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 *
 * $Id: RepositoryToComponentProperty.java 51621 2010-11-23 09:18:34Z hwang $
 *
 */
public class RepositoryToComponentProperty {

    public static Object getValue(Connection connection, String value, IMetadataTable table, String targetComponent) {

        if (connection instanceof HL7Connection) {
            return getHL7Value((HL7Connection) connection, value);
        }

        if (connection instanceof FileConnection) {
            return getFileValue((FileConnection) connection, value);
        }
        if (connection instanceof XmlFileConnection) {
            return getXmlFileValue((XmlFileConnection) connection, value);
        }
        if (connection != null && connection.getCompProperties() == null && (connection instanceof DatabaseConnection)) {
            return getDatabaseValue((DatabaseConnection) connection, value, table, targetComponent);
        }

        if (connection instanceof FTPConnection) {
            return getFTPValue((FTPConnection) connection, value);
        }

        if (connection instanceof BRMSConnection) {
            return getBRMSValue((BRMSConnection) connection, value);
        }

        if (connection instanceof LDAPSchemaConnection) {
            return getLDAPValue((LDAPSchemaConnection) connection, value);
        }
        if (connection instanceof WSDLSchemaConnection) {
            return getWSDLValue((WSDLSchemaConnection) connection, value);
        }
        if (connection instanceof LdifFileConnection) {
            return getLdifFileValue((LdifFileConnection) connection, value);
        }
        if (connection instanceof FileExcelConnection) {
            return getExcelFileValue((FileExcelConnection) connection, value);
        }

        if (connection instanceof MDMConnection) {
            return getMDMValue((MDMConnection) connection, value, table);
        }

        if (connection instanceof SAPConnection) {
            return getSAPValue((SAPConnection) connection, value);
        }

        if (connection instanceof SalesforceSchemaConnection) {
            return getSalesforceSchemaValue((SalesforceSchemaConnection) connection, value, table);
        }

        if (connection instanceof EDIFACTConnection) {
            return getEDIFACTSchemaValue((EDIFACTConnection) connection, value);
        }

        for (IDragAndDropServiceHandler handler : DragAndDropManager.getHandlers()) {
            if (handler.canHandle(connection)) {
                return handler.getComponentValue(connection, value, table, targetComponent);
            }
        }
        return null;

    }

    public static Object getValue(Connection connection, String value, IMetadataTable table) {
        return getValue(connection, value, table, null);
    }

    /**
     * DOC guanglong.du Comment method "getEDIFACTSchemaValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getEDIFACTSchemaValue(EDIFACTConnection connection, String value) {
        if (connection == null) {
            return null;
        }
        if ("EDI_TYPE".equals(value)) {
            if (isContextMode(connection, connection.getXmlName())) {
                return connection.getXmlName();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getXmlName());
            }
        } else if ("EDI_VERSION".equals(value)) {
            if (isContextMode(connection, connection.getFileName())) {
                return connection.getFileName();
            } else {
                return TalendQuoteUtils.removeQuotes(connection.getFileName());
            }
        } else if ("XPATH_QUERY".equals(value)) {
            return TalendQuoteUtils.addQuotes("/unEdifact/interchangeMessage/" + connection.getXmlName());
        }
        return null;
    }

    /**
     *
     * DOC YeXiaowei Comment method "getSAPInputAndOutputValue".
     *
     * @param conn
     * @param value2
     * @param functionLabel
     * @param isInput
     * @return
     */
    public static void getSAPInputAndOutputValue(SAPConnection conn, List<Map<String, Object>> value2, String functionLabel,
            boolean isInput) {
        if (conn == null) {
            return;
        }
        SAPFunctionUnit unit = null;
        for (int i = 0; i < conn.getFuntions().size(); i++) {
            SAPFunctionUnit tmp = conn.getFuntions().get(i);
            if (tmp.getLabel().equals(functionLabel)) {
                unit = tmp;
                break;
            }
        }
        if (unit == null || unit.getParamData() == null) {
            return;
        }

        SAPFunctionParameter table = isInput ? unit.getParamData().getInputRoot() : unit.getParamData().getOutputRoot();

        value2.clear(); // Make sure for this
        if (isInput) {
            mergeColumn(table, table.getChildren(), value2);
        } else {
            for (int i = 0; i < table.getChildren().size(); i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                SAPFunctionParameter column = table.getChildren().get(i);
                // this part maybe no use , didn't find OUTPUT_PARAMS in sap component
                map.put("SAP_PARAMETER_TYPE", column.getType().replace('.', '_')); //$NON-NLS-1$
                map.put("SAP_TABLE_NAME", TalendQuoteUtils.addQuotes("")); //$NON-NLS-1$
                map.put("SAP_PARAMETER_NAME", TalendQuoteUtils.addQuotes(column.getName())); //$NON-NLS-1$
                if (!map.isEmpty()) {
                    value2.add(map);
                }
            }

        }
    }

    private static void mergeColumn(SAPFunctionParameter parent, EList<SAPFunctionParameter> columns,
            List<Map<String, Object>> values) {

        for (SAPFunctionParameter current : columns) {
            HashMap map = new HashMap<String, Object>();
            if (!current.getChildren().isEmpty()) {
                mergeColumn(current, current.getChildren(), values);
                continue;
            }
            String talendType = getTalendTypeFromJCOType(current.getType());
            String value = "";
            if (talendType.contains("String")) { //$NON-NLS-1$
                value = TalendQuoteUtils.addQuotes(current.getTestValue());
            } else {
                value = current.getTestValue();
            }
            String type = null;
            String structrueOrTableName = "";
            // if it is not root node
            if (!(parent.eContainer() instanceof SAPFunctionParamData)) {
                if (ISAPConstant.PARAM_STRUCTURE.equals(parent.getType())) {
                    type = "input_structure";//$NON-NLS-1$
                } else if (ISAPConstant.PARAM_TABLE.equals(parent.getType())) {
                    type = "table_input";//$NON-NLS-1$
                }
                structrueOrTableName = parent.getName();
            } else {
                type = "input_single";//$NON-NLS-1$
            }
            map.put("SAP_PARAMETER_VALUE", value); //$NON-NLS-1$
            map.put("SAP_PARAMETER_TYPE", type); //$NON-NLS-1$
            map.put("SAP_TABLE_NAME", TalendQuoteUtils.addQuotes(structrueOrTableName)); //$NON-NLS-1$
            map.put("SAP_PARAMETER_NAME", TalendQuoteUtils.addQuotes(current.getName())); //$NON-NLS-1$
            values.add(map);

        }

    }

    /**
     *
     * DOC xye Comment method "getSAPValuesForFunction".
     *
     * @param conn
     * @param functionLabel
     * @param paramterName
     * @return
     */
    public static String getSAPValuesForFunction(SAPConnection conn, String functionLabel, String paramterName) {
        SAPFunctionUnit unit = null;
        if (conn == null) {
            return null;
        }
        for (int i = 0; i < conn.getFuntions().size(); i++) {
            unit = conn.getFuntions().get(i);
            if (unit.getLabel().equals(functionLabel)) {
                break;
            }
        }
        if (unit == null) {
            return null;
        }
        if (paramterName.equals("SAP_ITERATE_OUT_TYPE")) { //$NON-NLS-1$
            if (unit.getOutputType() != null) {
                return unit.getOutputType().replace(".", "_"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else if (paramterName.equals("SAP_ITERATE_OUT_TABLENAME")) { //$NON-NLS-1$
            return TalendQuoteUtils.addQuotes(unit.getOutputTableName());
        }
        return null;
    }

    /**
     *
     * DOC YeXiaowei Comment method "getTalendTypeFromJCOType".
     *
     * @param jcoType
     * @return
     */
    private static String getTalendTypeFromJCOType(final String jcoType) {
        if (jcoType == null) {
            return ""; //$NON-NLS-1$
        }
        List<MappingType> advicedDbToTalendTypes = MetadataTalendType.getMappingTypeRetriever("sap_id")//$NON-NLS-1$
                .getAdvicedDbToTalendTypes(jcoType);
        if (advicedDbToTalendTypes.isEmpty()) {
            return "";//$NON-NLS-1$
        }
        return advicedDbToTalendTypes.get(0).getTalendType();
    }

    /**
     *
     * DOC YeXiaowei Comment method "getSAPValue".
     *
     * @param connection
     * @param value
     * @return
     */
    public static Object getSAPValue(SAPConnection connection, String value) {

        if ("CLIENT".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getClient())) {
                return connection.getClient();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getClient());
            }
        } else if ("USERID".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getUsername())) {
                return connection.getUsername();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getUsername());
            }
        } else if ("PASSWORD".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getPassword())) {
                return connection.getPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassword(), false));
            }
        } else if ("LANGUAGE".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getLanguage())) {
                return connection.getLanguage();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getLanguage());
            }
        } else if ("HOSTNAME".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getHost())) {
                return connection.getHost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getHost());
            }
        } else if ("SYSTEMNUMBER".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getSystemNumber())) {
                return connection.getSystemNumber();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getSystemNumber());
            }
        } else if ("VERSION".equals(value)) {
            // before 421 sap version value was defined as SAP2 SAP3 in component side(see 17789) and saved in items ,
            // after component changed the value to the jar name
            String version = connection.getJcoVersion();
            if (SapJcoVersion.SAP2.name().equals(version)) {
                version = SapJcoVersion.SAP2.getModulName();
            } else if (SapJcoVersion.SAP3.name().equals(version)) {
                version = SapJcoVersion.SAP3.getModulName();
            }

            return version;
        } else if ("SAP_PROPERTIES".equals(value)) {//$NON-NLS-1$
            EList<AdditionalConnectionProperty> additionalProperties = connection.getAdditionalProperties();
            List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
            for (AdditionalConnectionProperty property : additionalProperties) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (property.getPropertyName() != null && !"".equals(property.getPropertyName())) {
                    map.put("PROPERTY", TalendQuoteUtils.addQuotes(property.getPropertyName()));//$NON-NLS-1$
                    if (isContextMode(connection, property.getValue())) {
                        map.put("VALUE", property.getValue());//$NON-NLS-1$
                    } else {
                        map.put("VALUE", TalendQuoteUtils.addQuotes(property.getValue()));//$NON-NLS-1$
                    }
                    values.add(map);
                }
            }
            return values;
        } else if ("SAPHANA_HOST".equals(value)) { //$NON-NLS-1$
            String dbHost = TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_HOST, connection);
            if (isContextMode(connection, dbHost)) {
                return dbHost;
            } else {
                return TalendQuoteUtils.addQuotes(dbHost);
            }
        } else if ("SAPHANA_PORT".equals(value)) { //$NON-NLS-1$
            String dbPort = TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PORT, connection);
            if (isContextMode(connection, dbPort)) {
                return dbPort;
            } else {
                return TalendQuoteUtils.addQuotes(dbPort);
            }
        } else if ("SAPHANA_TABLESCHEMA".equals(value)) { //$NON-NLS-1$
            String dbSchema = TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_SCHEMA, connection);
            if (isContextMode(connection, dbSchema)) {
                return dbSchema;
            } else {
                return TalendQuoteUtils.addQuotes(dbSchema);
            }
        } else if ("SAPHANA_USER".equals(value)) { //$NON-NLS-1$
            String dbUsername = TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_USERNAME, connection);
            if (isContextMode(connection, dbUsername)) {
                return dbUsername;
            } else {
                return TalendQuoteUtils.addQuotes(dbUsername);
            }
        } else if ("SAPHANA_PASS".equals(value)) { //$NON-NLS-1$
            String dbPassword = TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PASSWORD, connection);
            if (isContextMode(connection, dbPassword)) {
                return dbPassword;
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(dbPassword, false));
            }
        }
        return null;
    }

    /**
     * DOC gcui Comment method "getHL7Value".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getHL7Value(HL7Connection connection, String value) {
        if ("FILE_PATH".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getFilePath())) {
                return connection.getFilePath();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getFilePath());
            }
        } else if ("START_MSG".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getStartChar())) {
                return connection.getStartChar();
            } else {
                return connection.getStartChar();
            }
        } else if ("END_MSG".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEndChar())) {
                return connection.getEndChar();
            } else {
                return connection.getEndChar();
            }
        }
        return null;
    }

    /**
     * DOC YeXiaowei Comment method "getSalesforceSchemaValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getSalesforceSchemaValue(SalesforceSchemaConnection connection, String value, IMetadataTable table) {
        if ("ENDPOINT".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getWebServiceUrl())
                    || isContextMode(connection, connection.getWebServiceUrlTextForOAuth())) {
                if (connection.getLoginType().equalsIgnoreCase("basic")) {
                    return connection.getWebServiceUrl();
                } else {
                    return connection.getWebServiceUrlTextForOAuth();
                }
            } else {
                if (connection.getLoginType().equalsIgnoreCase("basic")) {
                    return TalendQuoteUtils.addQuotes(connection.getWebServiceUrl());
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getWebServiceUrlTextForOAuth());
                }
            }
        } else if ("USER_NAME".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getUserName())) {
                return connection.getUserName();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getUserName());
            }
        } else if ("PASSWORD".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getPassword())) {
                return connection.getPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassword(), false));
            }
        }
        // for bug TDI-8662 . should be careful that connection.getModuleName() will always get the latest name of the
        // module which was the last one be retrived
        // else if ("CUSTOM_MODULE_NAME".equals(value)) { //$NON-NLS-1$
        // return TalendQuoteUtils.addQuotes(connection.getModuleName());
        // }
        else if ("MODULENAME".equals(value)) { //$NON-NLS-1$
            if (connection.isUseCustomModuleName()) {
                return "CustomModule"; //$NON-NLS-1$
            } else {
                if (table != null) {
                    SalesforceModuleUnit currentUnit = getSaleforceModuleUnitByTable(table, connection.getModules());
                    if (currentUnit != null) {
                        return currentUnit.getModuleName();
                    }
                }
                return connection.getModuleName();
            }
        } else if ("CUSTOM_MODULE_NAME".equals(value)) { //$NON-NLS-1$
            if (table != null) {
                SalesforceModuleUnit currentUnit = getSaleforceModuleUnitByTable(table, connection.getModules());
                if (currentUnit != null) {
                    return TalendQuoteUtils.addQuotes(currentUnit.getModuleName());
                }
            }
            return TalendQuoteUtils.addQuotes(connection.getModuleName());
        } else if ("QUERY_CONDITION".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getQueryCondition())) {
                return connection.getQueryCondition();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getQueryCondition());
            }
            // add for feature 7507
        } else if ("BATCH_SIZE".equals(value)) { //$NON-NLS-1$
            return connection.getBatchSize();
        } else if ("UES_PROXY".equals(value)) { //$NON-NLS-1$
            return connection.isUseProxy();
        } else if ("PROXY_HOST".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyHost())) {
                return connection.getProxyHost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyHost());
            }
        } else if ("PROXY_PORT".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyPort())) {
                return connection.getProxyPort();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyPort());
            }

        } else if ("PROXY_USERNAME".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyUsername())) {
                return connection.getProxyUsername();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyUsername());
            }

        } else if ("PROXY_PASSWORD".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyPassword())) {
                return connection.getProxyPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getProxyPassword(), false));
            }
        } else if ("TIMEOUT".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getTimeOut())) {
                return connection.getTimeOut();
            } else {
                return connection.getTimeOut();// TalendQuoteUtils.addQuotes(connection.getTimeOut());
            }
        } else if ("API_VERSION".equals(value)) {
            if (isContextMode(connection, connection.getSalesforceVersion())) {
                return connection.getSalesforceVersion();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getSalesforceVersion());
            }
        } else if ("OAUTH_CLIENT_ID".equals(value)) {
            if (isContextMode(connection, connection.getConsumeKey())) {
                return connection.getConsumeKey();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getConsumeKey());
            }
        } else if ("OAUTH_CLIENT_SECRET".equals(value)) {
            if (isContextMode(connection, connection.getConsumeSecret())) {
                return connection.getConsumeSecret();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getConsumeSecret(), false));
            }
        } else if ("OAUTH_CALLBACK_HOST".equals(value)) {
            if (isContextMode(connection, connection.getCallbackHost())) {
                return connection.getCallbackHost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getCallbackHost());
            }
        } else if ("OAUTH_CALLBACK_PORT".equals(value)) {
            if (isContextMode(connection, connection.getCallbackPort())) {
                return connection.getCallbackPort();
            } else {
                return connection.getCallbackPort();
            }
        } else if ("LOGIN_TYPE".equals(value)) {
            if (isContextMode(connection, connection.getLoginType())) {
                return connection.getLoginType();
            } else {
                if (connection.getLoginType().equalsIgnoreCase("basic")) {
                    return "BASIC";
                } else {
                    return "OAUTH";
                }
            }
        }
        return null;
    }

    private static SalesforceModuleUnit getSaleforceModuleUnitByTable(IMetadataTable table,
            EList<SalesforceModuleUnit> moduleList) {
        for (SalesforceModuleUnit unit : moduleList) {
            if (table.getLabel().equals(unit.getModuleName())) {
                return unit;
            }
        }
        return null;
    }

    /**
     * DOC qzhang Comment method "getWSDLValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getWSDLValue(WSDLSchemaConnection connection, String value) {
        if ("ENDPOINT".equals(value)) { //$NON-NLS-1$
            // if (!connection.isIsInputModel()) {
            // return TalendQuoteUtils.addQuotesIfNotExist(connection.getWSDL());
            // }
            if (isContextMode(connection, connection.getWSDL())) {
                return connection.getWSDL();
            } else {
                return TalendQuoteUtils.addQuotesIfNotExist(connection.getWSDL());
            }
        } else if ("NEED_AUTH".equals(value)) { //$NON-NLS-1$
            return new Boolean(connection.isNeedAuth());
        } else if ("AUTH_USERNAME".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getUserName())) {
                return connection.getUserName();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getUserName());
            }
        } else if ("AUTH_PASSWORD".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getPassword())) {
                return connection.getPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassword(), false));
            }
        } else if ("UES_PROXY".equals(value)) { //$NON-NLS-1$
            return new Boolean(connection.isUseProxy());
        } else if ("PROXY_HOST".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyHost())) {
                return connection.getProxyHost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyHost());
            }
        } else if ("PROXY_PORT".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyPort())) {
                return connection.getProxyPort();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyPort());
            }
        } else if ("PROXY_USERNAME".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyUser())) {
                return connection.getProxyUser();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyUser());
            }
        } else if ("PROXY_PASSWORD".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getProxyPassword())) {
                return connection.getProxyPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getProxyPassword(), false));
            }
        } else if ("METHOD".equals(value)) { //$NON-NLS-1$
            String methodName = connection.getMethodName();
            if (methodName != null && methodName.contains("(")) {//$NON-NLS-1$
                methodName = methodName.substring(0, methodName.lastIndexOf("("));//$NON-NLS-1$
            }
            if (!connection.isIsInputModel()) {
                return connection.getMethodName();
            }
            if (isContextMode(connection, connection.getMethodName())) {
                return connection.getMethodName();
            } else {
                return TalendQuoteUtils.addQuotesIfNotExist(methodName);
            }
        } else if ("TIMEOUT".equals(value)) { //$NON-NLS-1$
            Integer timeOut = new Integer(connection.getTimeOut());
            return timeOut.toString();
        } else if ("WSDLURL".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEndpointURI())) {
                return connection.getEndpointURI();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getEndpointURI());
            }
        } else if (value.equals("ENCODING")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEncoding())) {
                return connection.getEncoding();
            } else {
                if (connection.getEncoding() == null) {
                    // get the default encoding
                    return TalendQuoteUtils.addQuotes(EMetadataEncoding.getMetadataEncoding("").getName()); //$NON-NLS-1$
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getEncoding());
                }
            }
        } else if ("PARAMS".equals(value)) {
            return connection.getParameters();
        } else if ("SERVICE_NS".equals(value)) {
            return connection.getServerNameSpace();
        } else if ("SERVICE_NAME".equals(value)) {
            return connection.getServerName();
        } else if ("PORT_NS".equals(value)) {
            return connection.getPortNameSpace();
        } else if ("PORT_NAME".equals(value)) {
            return connection.getPortName();
        } else if ("INPUT_PARAMS".equals(value)) {
            return getOutputWSDLValue(connection.getParameterValue());
        } else if ("OUTPUT_PARAMS".equals(value)) {
            return getOutputWSDLValue(connection.getOutputParameter());
        }
        return null;
    }

    public static List<Map<String, String>> getOutputWSDLValue(EList list) {
        List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            WSDLParameter node = (WSDLParameter) list.get(i);
            map.put("EXPRESSION", node.getExpression());
            map.put("COLUMN", node.getColumn());
            map.put("SOURCE", node.getSource());
            map.put("ELEMENT", node.getElement());
            map.put("PARAMETERINFO", node.getParameterInfo());
            map.put("PARAPARENT", node.getParameterInfoParent());
            newList.add(map);
        }
        return newList;

    }

    /**
     * DOC hwang Comment method "getMDMValue".
     *
     * @param connection
     * @param value
     * @param node
     * @return
     */
    private static Object getMDMValue(MDMConnection connection, String value, IMetadataTable table) {
        if ("MDM_VERSION".equals(value)) {//$NON-NLS-1$
            if (connection.getVersion() == null || "".equals(connection.getVersion())) {
                return MDMVersions.MDM_S56.getKey();
            } else {
                return connection.getVersion();
            }
        } else if ("MDMURL".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getServerUrl())) {
                return connection.getServerUrl();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getServerUrl());
            }
        } else if ("USERNAME".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getUsername())) {
                return connection.getUsername();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getUsername());
            }
        } else if ("PASSWORD".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getPassword())) {
                return connection.getPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassword(), false));
            }
        } else if ("UNIVERSE".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getUniverse())) {
                return connection.getUniverse();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getUniverse());
            }
        } else if ("CONCEPT".equals(value)) { //$NON-NLS-1$
            Concept concept = getConcept(connection, table);
            String conceptName = null;
            if (concept != null) {
                conceptName = concept.getLoopExpression();
                if (conceptName != null && conceptName.startsWith("/")) {
                    conceptName = conceptName.split("/")[1];
                    // conceptName.substring(1, conceptName.length());
                }
                return TalendQuoteUtils.addQuotes(conceptName);
            }
        } else if ("DATACLUSTER".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getDatacluster())) {
                return connection.getDatacluster();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getDatacluster());
            }
        } else if (value.equals("XPATH_QUERY")) { //$NON-NLS-1$
            Concept concept = getConcept(connection, table);
            if (concept != null) {
                if (isContextMode(connection, concept.getLoopExpression())) {
                    return concept.getLoopExpression();
                } else {
                    String loop = concept.getLoopExpression();
                    if (MdmConceptType.RECEIVE.equals(concept.getConceptType())) {
                        final String[] split = loop.split("/");
                        if (split.length > 1) {
                            loop = "/" + split[split.length - 1];
                        }
                    }
                    return TalendQuoteUtils.addQuotes(loop);
                }
            }

        } else if ("DATAMODEL".equals(value)) {
            if (isContextMode(connection, connection.getDatamodel())) {
                return connection.getDatamodel();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getDatamodel());
            }
        } else if ("ROOT".equals(value)) {
            Concept concept = getConcept(connection, table);
            if (concept != null) {
                return getOutputXmlValue(concept.getRoot());
            }
        } else if ("LOOP".equals(value)) {
            Concept concept = getConcept(connection, table);
            if (concept != null) {
                return getOutputXmlValue(concept.getLoop());
            }
        } else if ("GROUP".equals(value)) {
            Concept concept = getConcept(connection, table);
            if (concept != null) {
                return getOutputXmlValue(concept.getGroup());
            }
        } else if ("XPATH_PREFIX".equals(value)) {
            Concept concept = getConcept(connection, table);
            if (concept != null) {
                return concept.getXPathPrefix();
            }
        }
        return null;
    }

    private static Concept getConcept(MDMConnection connection, IMetadataTable table) {
        if (table != null) {
            for (Concept concept : connection.getSchemas()) {
                // test if sourcename is null, this is only for compatibility with first mdm repository
                // released.
                if (concept != null && (concept.getLabel() != null && concept.getLabel().equals(table.getLabel()))) {
                    return concept;
                }
            }
        }
        return null;
    }

    private static String getStandardDbTypeFromConnection(String dbType) {

        // if (dbType.equals(EDatabaseTypeName.GODBC.getDisplayName())) {
        // return ODBC; MSODBC
        // }
        // if (dbType.equals(EDatabaseTypeName.MSODBC.getDisplayName())) {
        // return ODBC; MSODBC
        // }
        // if (dbType.equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
        // return SQL_SERVER; MSSQL
        // }

        // if (dbType.equals(EDatabaseTypeName.INTERBASE.getDisplayName())) {
        // return INTERBASE; Interbase
        // }

        return EDatabaseTypeName.getTypeFromDbType(dbType).getProduct();

    }

    private static Object getDatabaseValue(DatabaseConnection connection, String value, IMetadataTable table,
            String targetComponent) {

        String databaseType = connection.getDatabaseType();
        if (value.equals("TYPE")) { //$NON-NLS-1$
            String typeByProduct = getStandardDbTypeFromConnection(databaseType);
            // See bug 4565
            if (databaseType.equals(EDatabaseTypeName.ORACLEFORSID.getDisplayName())) {
                // see StatsAndLogConstants
                // This connection is Oracle_SID
                return EDatabaseTypeName.ORACLEFORSID.getXmlName();
            } else if (databaseType.equals(EDatabaseTypeName.ORACLESN.getDisplayName())) {
                // This connection is Oracle_service_name
                return EDatabaseTypeName.ORACLESN.getXmlName();
            } else if (databaseType.equals(EDatabaseTypeName.ORACLE_OCI.getDisplayName())) {
                return EDatabaseTypeName.ORACLE_OCI.getXmlName();
            } else if (databaseType.equals(EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName())) {
                return EDatabaseTypeName.ORACLE_CUSTOM.getXmlName();
            } else if (databaseType.equals(EDatabaseTypeName.MSSQL.getDisplayName())) {
                return EDatabaseTypeName.MSSQL.getXMLType(); // for component
            }

            else {
                return typeByProduct;
            }
        }
        if (value.equals("FRAMEWORK_TYPE")) { //$NON-NLS-1$
            if (isContextMode(connection, databaseType)) {
                if (databaseType.equals("JavaDB Embeded")) { //$NON-NLS-1$
                    return "EMBEDED"; //$NON-NLS-1$
                }
                if (databaseType.equals("JavaDB JCCJDBC")) { //$NON-NLS-1$
                    return "JCCJDBC"; //$NON-NLS-1$
                }
                if (databaseType.equals("JavaDB DerbyClient")) { //$NON-NLS-1$
                    return "DERBYCLIENT"; //$NON-NLS-1$
                }
            } else {
                if (databaseType.equals("JavaDB Embeded")) { //$NON-NLS-1$
                    return "EMBEDED"; //$NON-NLS-1$
                }
                if (databaseType.equals("JavaDB JCCJDBC")) { //$NON-NLS-1$
                    return "JCCJDBC"; //$NON-NLS-1$
                }
                if (databaseType.equals("JavaDB DerbyClient")) { //$NON-NLS-1$
                    return "DERBYCLIENT"; //$NON-NLS-1$
                }
            }
        }
        if (value.equals("SERVER_NAME")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getServerName());
        }
        if (value.equals("PORT")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getPort());
        }
        if (value.equals("SID") || value.equals("DATABASE_ALIAS")) { //$NON-NLS-1$ //$NON-NLS-2$
            if (("").equals(connection.getSID()) || connection.getSID() == null) { //$NON-NLS-1$
                if (isContextMode(connection, connection.getDatasourceName())) {
                    return connection.getDatasourceName();
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getDatasourceName());
                }
            } else {
                if (isContextMode(connection, connection.getSID())) {
                    return connection.getSID();
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getSID());
                }
            }
        }
        if (value.equals("DATASOURCE")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getDatasourceName());
        }
        // if (value.equals("USERNAME")) { //$NON-NLS-1$
        // return getAppropriateValue(connection, connection.getUsername());
        // }
        if (value.equals("PASSWORD")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getValue(connection.getPassword(), false));
        }
        if (value.equals("NULL_CHAR")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getNullChar());
        }
        if (value.equals("SCHEMA")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getUiSchema());
        }
        if (value.equals("FILE")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getFileFieldName());
        }
        if (value.equals("PROPERTIES_STRING")) { //$NON-NLS-1$
            return getAppropriateValue(connection, connection.getAdditionalParams());
        }
        if (value.equals("DRIVER")) { //$NON-NLS-1$
            String dbVersionString = connection.getDbVersionString();
            if (dbVersionString != null && EDatabaseConnTemplate.MSSQL.getDBDisplayName().equals(databaseType)) {
                return dbVersionString.toUpperCase();
            }
        }

        if (value.equals("CDC_MODE")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getCdcTypeMode())) {
                return connection.getCdcTypeMode();
            } else {
                return connection.getCdcTypeMode();
            }
        }

        if (value.equals("DB_VERSION")) { //$NON-NLS-1$
            String dbVersionString = connection.getDbVersionString();
            if (EDatabaseConnTemplate.ACCESS.getDBDisplayName().equals(databaseType)) {
                // @Deprecated: see bug 7262 this bug is Deprecated
                return dbVersionString;
            } else if (EDatabaseConnTemplate.MYSQL.getDBDisplayName().equals(databaseType)) {
                if (dbVersionString != null) {
                    return dbVersionString.toUpperCase();
                }
            } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
            } else if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_VERSION);
            } else if (EDatabaseTypeName.GENERAL_JDBC.getDisplayName().equals(databaseType)) {
                return dbVersionString;
            } else if (EDatabaseTypeName.SYBASEASE.getDisplayName().equals(databaseType)) {
                return dbVersionString;
            } else {
                String driverValue = EDatabaseVersion4Drivers.getDriversStr(databaseType, dbVersionString);
                if (EDatabaseConnTemplate.ORACLE_OCI.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.ORACLE_CUSTOM.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.ORACLEFORSID.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.ORACLESN.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.PLUSPSQL.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.PSQL.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.SAPHana.getDBDisplayName().equals(databaseType)
                        || EDatabaseConnTemplate.MSSQL.getDBDisplayName().equals(databaseType)) {
                    if (dbVersionString != null) {
                        driverValue = dbVersionString.toUpperCase();
                    }
                }
                if (isContextMode(connection, dbVersionString)) {
                    return dbVersionString;
                } else if (EDatabaseTypeName.VERTICA.getXmlName().equals(databaseType)) {
                    EDatabaseVersion4Drivers indexOfByVersionDisplay = EDatabaseVersion4Drivers.indexOfByVersion(dbVersionString);
                    if (indexOfByVersionDisplay != null) {
                        return indexOfByVersionDisplay.getVersionValue();
                    }
                } else {
                    return driverValue;
                }
            }
        }

        if (value.equals("CONNECTION_TYPE")) { //$NON-NLS-1$
            if (isContextMode(connection, databaseType)) {
                if (databaseType.equals(EDatabaseTypeName.ORACLEFORSID.getDisplayName())) {
                    return "ORACLE_SID";
                } else if (databaseType.equals(EDatabaseTypeName.ORACLESN.getDisplayName())) {
                    return "ORACLE_SERVICE_NAME";
                } else if (databaseType.equals(EDatabaseTypeName.ORACLE_OCI.getDisplayName())) {
                    return "ORACLE_OCI";
                } else if (databaseType.equals(EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName())) {
                    return "ORACLE_RAC";
                }
            } else {
                if (databaseType.equals(EDatabaseTypeName.ORACLEFORSID.getDisplayName())) {
                    return "ORACLE_SID";
                } else if (databaseType.equals(EDatabaseTypeName.ORACLESN.getDisplayName())) {
                    return "ORACLE_SERVICE_NAME";
                } else if (databaseType.equals(EDatabaseTypeName.ORACLE_OCI.getDisplayName())) {
                    return "ORACLE_OCI";
                } else if (databaseType.equals(EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName())) {
                    return "ORACLE_RAC";
                }
            }
        }

        // add new class name property
        if (value.equals("DRIVER_CLASS")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getDriverClass())) {
                return connection.getDriverClass();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getDriverClass());
            }
        }

        if (value.equals("URL")) { //$NON-NLS-1$
            String url = connection.getURL();
            if (isContextMode(connection, url)) {
                return url;
            } else {
                // TDI-18737:in case the url maybe null
                if (url != null) {
                    String h2Prefix = "jdbc:h2:"; //$NON-NLS-1$
                    if (url.startsWith(h2Prefix)) {
                        String path = url.substring(h2Prefix.length(), url.length());
                        // TDI-23861: handle the server mode of H2 which url has more than one colon.
                        if (path.split(":").length > 2) { //$NON-NLS-1$
                            int startIndex = path.lastIndexOf(":") - 1; //$NON-NLS-1$
                            String filePath = path.substring(startIndex);
                            h2Prefix += path.substring(0, startIndex);
                            path = filePath;
                        }
                        path = PathUtils.getPortablePath(path);
                        url = h2Prefix + path;
                    }
                    return TalendQuoteUtils.addQuotes(url);
                }
            }
        }

        // if (value.equals("DRIVER_PATH")) {
        // if (isContextMode(connection, connection.getDriverJarPath())) {
        // return connection.getDriverJarPath();
        // } else {
        // return TalendQuoteUtils.addQuotes(connection.getDriverJarPath());
        // }
        // }

        if (value.equals("DRIVER_JAR")) { //$NON-NLS-1$
            List<Map<String, Object>> value2 = new ArrayList<Map<String, Object>>();
            if (isContextMode(connection, connection.getDriverJarPath())) {
                Map<String, Object> line = new HashMap<String, Object>();
                line.put("JAR_NAME", connection.getDriverJarPath());
                value2.add(line);
            } else {
                String userDir = System.getProperty("user.dir"); //$NON-NLS-1$
                String pathSeparator = System.getProperty("file.separator"); //$NON-NLS-1$
                String defaultPath = userDir + pathSeparator + "lib" + pathSeparator + "java"; //$NON-NLS-1$ //$NON-NLS-2$
                String jarPath = connection.getDriverJarPath();

                if (jarPath == null) {
                    return null;
                }

                try {
                    Character comma = ';';
                    String[] jars = jarPath.split(comma.toString());
                    boolean deployed = false;
                    if (jars != null) {
                        for (String jar : jars) {
                            File file = Path.fromOSString(jar).toFile();
                            if (file.exists() && file.isFile()) {
                                String fileName = file.getName();
                                Map<String, Object> line = new HashMap<String, Object>();
                                line.put("JAR_NAME", fileName);
                                value2.add(line);
                                if (!new File(defaultPath + pathSeparator + fileName).exists()) {
                                    // deploy this library
                                    try {
                                        CoreRuntimePlugin.getInstance().getLibrariesService().deployLibrary(file.toURL());
                                        deployed = true;
                                    } catch (IOException e) {
                                        ExceptionHandler.process(e);
                                        return null;
                                    }
                                }
                            } else {
                                Map<String, Object> line = new HashMap<String, Object>();
                                line.put("JAR_NAME", jar);
                                value2.add(line);
                            }
                        }
                    }

                } catch (Exception e) {
                    return null;
                }
            }
            return value2;

        }
        if (value.equals("CDC_TYPE_MODE")) { //$NON-NLS-1$
            return new Boolean(CDCTypeMode.LOG_MODE.getName().equals(connection.getCdcTypeMode()));
        }
        // add this for tJavaDB embeded "DB Root Path"
        if (value.equals("DIRECTORY")) {//$NON-NLS-1$
            return getAppropriateValue(connection, connection.getDBRootPath());
        }
        // add for feature 11674
        if (value.equals("RUNNING_MODE")) {//$NON-NLS-1$
            String runningMode = "HSQLDB_IN_MEMORY";//$NON-NLS-1$
            if (EDatabaseTypeName.HSQLDB_IN_PROGRESS.getXmlName().equals(databaseType)) {
                runningMode = "HSQLDB_INPROGRESS_PERSISTENT";//$NON-NLS-1$
            } else if (EDatabaseTypeName.HSQLDB_SERVER.getXmlName().equals(databaseType)) {
                runningMode = "HSQLDB_SERVER";//$NON-NLS-1$
            } else if (EDatabaseTypeName.HSQLDB_WEBSERVER.getXmlName().equals(databaseType)) {
                runningMode = "HSQLDB_WEBSERVER";//$NON-NLS-1$
            }
            return runningMode;
        }
        if (value.equals("DBPATH")) {//$NON-NLS-1$
            return getAppropriateValue(connection, connection.getDBRootPath());
        }
        if (value.equals("DBNAME")) {//$NON-NLS-1$
            return getAppropriateValue(connection, connection.getDatasourceName());
        }
        if (value.equals("RAC_URL")) {
            return getAppropriateValue(connection, connection.getServerName());
        }

        if (value.equals("DISTRIBUTION")) {
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_DISTRIBUTION);
            } else if (EDatabaseTypeName.IMPALA.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_DISTRIBUTION);
            } else {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
            }
        }

        if (value.equals("HIVE_VERSION")) {
            return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        }

        if (value.equals("CONNECTION_MODE")) {
            return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);
        }

        if (value.equals("HBASE_DISTRIBUTION")) {
            return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_DISTRIBUTION);
        }

        if (value.equals("HBASE_VERSION")) {
            return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
        }

        if (value.equals("SET_TABLE_NS_MAPPING")) {
            return Boolean.valueOf(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_TABLE_NS_MAPPING));
        }

        if (value.equals("TABLE_NS_MAPPING")) {
            String tableNSMapping = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                tableNSMapping = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_TABLE_NS_MAPPING);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                tableNSMapping = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_TABLE_NS_MAPPING);
            }
            return getAppropriateValue(connection, tableNSMapping);
        }

        if (value.equals("HBASE_MASTER_PRINCIPAL")) {
            String hbaseMasterPrinc = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                hbaseMasterPrinc = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MASTERPRINCIPAL);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                hbaseMasterPrinc = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MASTERPRINCIPAL);
            }
            return getAppropriateValue(connection, hbaseMasterPrinc);
        }

        if (value.equals("HBASE_REGIONSERVER_PRINCIPAL")) {
            String hbaseRegPrinc = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                hbaseRegPrinc = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                hbaseRegPrinc = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_REGIONSERVERPRINCIPAL);
            }
            return getAppropriateValue(connection, hbaseRegPrinc);
        }

        if (value.equals("USE_MAPRTICKET")) {
            String useMaprTValue = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                useMaprTValue = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USE_MAPRTICKET);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                useMaprTValue = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USE_MAPRTICKET);
            } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
                useMaprTValue = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USE_MAPRTICKET);
            }
            return Boolean.parseBoolean(useMaprTValue);
        }
        if (value.equals("USERNAME")) {
            String connUserName = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                connUserName = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_USERNAME);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                connUserName = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_USERNAME);
            } else {
                connUserName = connection.getUsername();
            }
            return getAppropriateValue(connection, connUserName);
        }
        if (value.equals("MAPRTICKET_USERNAME")) {
            String maprticket_Username = connection.getParameters()
                    .get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_USERNAME);
            return getAppropriateValue(connection, maprticket_Username);
        }

        if (value.equals("MAPRTICKET_PASSWORD")) {
            String maprticket_Password = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                maprticket_Password = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                maprticket_Password = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_PASSWORD);
            } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
                maprticket_Password = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_PASSWORD);
            }
            return getAppropriateValue(connection, maprticket_Password);
        }
        if (value.equals("MAPRTICKET_CLUSTER")) {
            String maprticket_Cluster = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                maprticket_Cluster = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                maprticket_Cluster = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_CLUSTER);
            } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
                maprticket_Cluster = connection.getParameters()
                        .get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_CLUSTER);
            }
            return getAppropriateValue(connection, maprticket_Cluster);
        }
        if (value.equals("MAPRTICKET_DURATION")) {
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_AUTHENTICATION_MAPRTICKET_DURATION);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_AUTHENTICATION_MAPRTICKET_DURATION);
            } else if (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
                return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_AUTHENTICATION_MAPRTICKET_DURATION);
            }
        }

        if (value.equals("SET_MAPR_HOME_DIR")) {
            String setMapRHomeDir = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_SETMAPRHOMEDIR);
            return Boolean.parseBoolean(setMapRHomeDir);
        }
        if (value.equals("MAPR_HOME_DIR")) {
            String mapRHomeDir = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_MAPRHOMEDIR);
            return getAppropriateValue(connection, mapRHomeDir);
        }

        if (value.equals("SET_HADOOP_LOGIN")) {
            String setMapRHadoopLogin = connection.getParameters()
                    .get(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_SETMAPRHADOOPLOGIN);
            return Boolean.parseBoolean(setMapRHadoopLogin);
        }
        if (value.equals("HADOOP_LOGIN")) {
            String mapRHadoopLogin = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRTICKET_MAPRHADOOPLOGIN);
            return getAppropriateValue(connection, mapRHadoopLogin);
        }

        if (value.equals("HIVE_SERVER")) {
            return connection.getParameters().get(ConnParameterKeys.HIVE_SERVER_VERSION);
        }

        if (value.equals("EXECUTION_ENGINE")) {
            return connection.getParameters().get(ConnParameterKeys.HIVE_EXECUTION_ENGINE);
        }

        if (value.equals("HBASE_PARAMETERS")) {
            String message = null;
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES);
            } else if (EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_MAPRDB_PROPERTIES);
            }
            return HadoopRepositoryUtil.getHadoopPropertiesFullList(connection, message, true);
        }

        if (value.equals("USE_CLOUDERA_NAVIGATOR")) {
            return HadoopRepositoryUtil.useClouderaNavi(connection);
        }

        if (value.equals("CLOUDERA_NAVIGATOR_USERNAME")) {
            return getAppropriateValue(connection, HadoopRepositoryUtil.getClouderaNaviUserName(connection));
        }

        if (value.equals("CLOUDERA_NAVIGATOR_PASSWORD")) {
            return getAppropriateValue(connection, HadoopRepositoryUtil.getClouderaNaviPassword(connection));
        }

        if (value.equals("CLOUDERA_NAVIGATOR_URL")) {
            return getAppropriateValue(connection, HadoopRepositoryUtil.getClouderaNaviUrl(connection));
        }

        if (value.equals("CLOUDERA_NAVIGATOR_METADATA_URL")) {
            return getAppropriateValue(connection, HadoopRepositoryUtil.getClouderaNaviMetadataUrl(connection));
        }

        if (value.equals("CLOUDERA_NAVIGATOR_CLIENT_URL")) {
            return getAppropriateValue(connection, HadoopRepositoryUtil.getClouderaNaviClientUrl(connection));
        }

        if (value.equals("CLOUDERA_NAVIGATOR_AUTOCOMMIT")) {
            return HadoopRepositoryUtil.clouderaNaviAutoCommit(connection);
        }

        if (value.equals("CLOUDERA_NAVIGATOR_DISABLE_SSL_VALIDATION")) {
            return HadoopRepositoryUtil.clouderaNaviDisableSSL(connection);
        }

        if (value.equals("CLOUDERA_NAVIGATOR_DIE_ON_ERROR")) {
            return HadoopRepositoryUtil.clouderaNaviDieOnError(connection);
        }

        if (value.equals("HADOOP_ADVANCED_PROPERTIES")) {
            String message = null;
            if (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
                message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_PROPERTIES);
            } else if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)) {
                message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_PROPERTIES);
            }
            return HadoopRepositoryUtil.getHadoopPropertiesFullList(connection, message, true);
        }

        if (value.equals("ADVANCED_PROPERTIES") && EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
            String message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_JDBC_PROPERTIES);
            return HadoopRepositoryUtil.getHadoopPropertiesList(message, connection.isContextMode(), true);
        }

        if (value.equals("HIVE_ADDITIONAL_JDBC") && EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)) {
            return getAppropriateValue(connection,
                    connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HIVE_ADDITIONAL_JDBC_SETTINGS));
        }

        if (value.equals("USE_SSL") && (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)
                || EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType))) {
            String message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_SSL);
            return Boolean.parseBoolean(message);
        }

        if ((value.equals("SSL_TRUST_STORE") || value.equals("SSL_TRUSTSERVER_TRUSTSTORE"))
                && (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)
                || EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType))) {
            return getAppropriateValue(connection,
                    connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PATH));
        }

        if ((value.equals("SSL_TRUST_STORE_PASSWORD") || value.equals("SSL_TRUSTSERVER_PASSWORD"))
                && (EDatabaseTypeName.HIVE.getDisplayName().equals(databaseType)
                || EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType))) {
            return getAppropriateValue(connection, connection
                    .getValue(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_TRUST_STORE_PASSWORD), false));
        }
        
        if (value.equals("NEED_CLIENT_AUTH") && EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType)) {
            String message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NEED_CLIENT_AUTH);
            return Boolean.parseBoolean(message);
        }

        if (value.equals("SSL_KEYSTORE") && EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType)) {
            return getAppropriateValue(connection,
                    connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PATH));
        }

        if (value.equals("DISABLE_CBC_PROTECTION") && EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType)) {
            String message = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_DISABLE_CBC_PROTECTION);
            return Boolean.parseBoolean(message);
        }

        if (value.equals("SSL_KEYSTORE_PASSWORD") && EDatabaseTypeName.ORACLE_CUSTOM.getDisplayName().equals(databaseType)) {
            return getAppropriateValue(connection, connection
                    .getValue(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_SSL_KEY_STORE_PASSWORD), false));
        }

        if (value.equals("HADOOP_CUSTOM_JARS")) {
            if (targetComponent != null && targetComponent.startsWith("tPig")) {
                // for pig component
                String clusterID = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CLUSTER_ID);
                if (clusterID != null) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IHadoopClusterService.class)) {
                        IHadoopClusterService hadoopClusterService = (IHadoopClusterService) GlobalServiceRegister.getDefault()
                                .getService(IHadoopClusterService.class);
                        Map<String, String> hadoopCustomLibraries = hadoopClusterService.getHadoopCustomLibraries(clusterID);

                        if (EDatabaseTypeName.HBASE.getDisplayName().equals(connection.getDatabaseType())) {
                            return hadoopCustomLibraries.get(ECustomVersionGroup.PIG_HBASE.getName()) == null ? ""
                                    : hadoopCustomLibraries.get(ECustomVersionGroup.PIG_HBASE.getName());
                        }
                    }
                }
            }
            return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
        }

        if (value.equals(EParameterNameForComponent.PARA_NAME_FS_DEFAULT_NAME.getName())) {
            String nameNodeURL = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_URL);
            if (nameNodeURL == null && (databaseType).equals(EDatabaseTypeName.HBASE.getDisplayName())) {
                return nameNodeURL;
            } else if (isContextMode(connection, nameNodeURL)) {
                return nameNodeURL;
            } else {
                return TalendQuoteUtils.addQuotes(nameNodeURL);
            }
        }

        if (value.equals(EParameterNameForComponent.PARA_NAME_MAPRED_JT.getName())
                || value.equals(EParameterNameForComponent.PARA_NAME_RESOURCE_MANAGER.getName())) {
            String jobTrackerURL = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            return getAppropriateValue(connection, jobTrackerURL);
        }

        if (value.equals(EParameterNameForComponent.PARA_NAME_USE_YARN.getName())) {
            String useYarn = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_YARN);
            if (isContextMode(connection, useYarn)) {
                return useYarn;
            } else {
                return Boolean.valueOf(useYarn);
            }
        }

        if (value.equals("LOCAL")) {
            return false;
        }

        if (value.equals("MAPREDUCE")) {
            return true;
        }

        if (value.equals("LOAD") || value.equals("STORE")) {
            return "HBaseStorage";
        }

        if (value.equals("PIG_VERSION")) {
            return connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_VERSION);
        }

        if (value.equals("USE_KRB")) {
            String useKrbValue = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_KRB);
            if (useKrbValue == null) {
                return useKrbValue;
            } else {
                return Boolean.parseBoolean(useKrbValue);
            }
        }

        if (value.equals("MAPRED_JOB_TRACKER") || value.equals("MAPRED_RESOURCE_MANAGER")) {
            String mapredJobTracker = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_URL);
            return getAppropriateValue(connection, mapredJobTracker);
        }

        if (value.equals("NAMENODE_PRINCIPAL")) {
            String nameNodePrincipal = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_NAME_NODE_PRINCIPAL);
            return getAppropriateValue(connection, nameNodePrincipal);
        }

        /**
         * JOBTRACKER_PRINCIPAL and RESOURCEMANAGER_PRINCIPAL use the same field
         *
         * @see org.talend.repository.model.hadoopcluster.HadoopClusterConnection#getJtOrRmPrincipal()
         */
        if (value.equals("JOBTRACKER_PRINCIPAL") || value.equals("RESOURCEMANAGER_PRINCIPAL")) {
            String jtOrRmPrincipal = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_TRACKER_PRINCIPAL);
            return getAppropriateValue(connection, jtOrRmPrincipal);
        }

        if (value.equals("JOBHISTORY_PRINCIPAL")) {
            String jobHistoryPrincipal = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOB_HISTORY_PRINCIPAL);
            return getAppropriateValue(connection, jobHistoryPrincipal);
        }

        if (value.equals("ZOOKEEPER_QUORUM")) {
            return getAppropriateValue(connection, connection.getServerName());
        }

        if (value.equals("ZOOKEEPER_CLIENT_PORT")) {
            return getAppropriateValue(connection, connection.getPort());
        }

        if (value.equals("SET_ZNODE_PARENT")) {
            return Boolean.valueOf(connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_SET_ZNODE_PARENT));
        }

        if (value.equals("ZNODE_PARENT")) {
            String znodeParent = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_HBASE_ZNODE_PARENT);
            return getAppropriateValue(connection, znodeParent);
        }

        if (value.equals("COLUMN_MAPPING")) { //$NON-NLS-1$
            return getColumnMappingValue(connection, table);
        }

        if (value.equals("HIVE_PRINCIPAL")) {
            String hivePrincipal = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_HIVEPRINCIPLA);
            return getAppropriateValue(connection, hivePrincipal);
        }

        if (value.equals("METASTORE_JDBC_URL")) {
            String metadataUrl = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_METASTOREURL);
            return getAppropriateValue(connection, metadataUrl);
        }

        if (value.equals("METASTORE_CLASSNAME")) {
            String metastoreDC = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_DRIVERCLASS);
            return getAppropriateValue(connection, metastoreDC);
        }

        if (value.equals("METASTORE_USERNAME")) {
            String metastoreUser = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_USERNAME);
            return getAppropriateValue(connection, metastoreUser);
        }

        if (value.equals("METASTORE_PASSWORD")) {
            String metastorePwd = connection.getParameters().get(ConnParameterKeys.HIVE_AUTHENTICATION_PASSWORD);
            return getAppropriateValue(connection, metastorePwd);
        }

        if (value.equals("USE_KEYTAB")) {
            String USE_KEYTAB = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USEKEYTAB);
            if (USE_KEYTAB != null && USE_KEYTAB.equals("true")) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }

        if (value.equals("PRINCIPAL") || value.equals("KEYTAB_PRINCIPAL")) {
            String principal = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB_PRINCIPAL);
            return getAppropriateValue(connection, principal);
        }

        if (value.equals("KEYTAB_PATH")) {
            String keytab = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_KEYTAB);
            return getAppropriateValue(connection, keytab);
        }

        if (value.equals("IMPALA_PRINCIPAL")) {
            String principal = connection.getParameters().get(ConnParameterKeys.IMPALA_AUTHENTICATION_PRINCIPLA);
            return getAppropriateValue(connection, principal);
        }

        if (value.equals("IMPALA_VERSION")) {
            String impalaVersion = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_IMPALA_VERSION);
            if (impalaVersion != null) {
                // The value for IMPALA_CDH5 is wrong for a long time, so need to change it manaully, so can avoid to
                // add migration task
                if ("CLOUDERA_5".equals(impalaVersion)) {
                    impalaVersion = "Cloudera_CDH5_1";
                }
            }
            return impalaVersion;
        }

        if (value.equals("SET_SCHEDULER_ADDRESS")) {
            return true;
        }

        if (value.equals("RESOURCEMANAGER_SCHEDULER_ADDRESS")) {
            String rmSchAdr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_RESOURCEMANAGER_SCHEDULER_ADDRESS);
            return getAppropriateValue(connection, rmSchAdr);
        }

        if (value.equals("SET_JOBHISTORY_ADDRESS")) {
            return true;
        }

        if (value.equals("JOBHISTORY_ADDRESS")) {
            String jhAddr = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_JOBHISTORY_ADDRESS);
            return getAppropriateValue(connection, jhAddr);
        }

        if (value.equals("SET_STAGING_DIRECTORY")) {
            return true;
        }

        if (value.equals("STAGING_DIRECTORY")) {
            String stageDir = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_STAGING_DIRECTORY);
            return getAppropriateValue(connection, stageDir);
        }

        if (value.equals("USE_DATANODE_HOSTNAME")) {
            String USE_DATANODE_HOSTNAME = connection.getParameters().get(ConnParameterKeys.CONN_PARA_KEY_USE_DATANODE_HOSTNAME);
            return USE_DATANODE_HOSTNAME == null ? null : "true".equals(USE_DATANODE_HOSTNAME);
        }

        return null;

    }

    private static String getAppropriateValue(Connection connection, String rawValue) {
        if (isContextMode(connection, rawValue)) {
            return rawValue;
        } else {
            return TalendQuoteUtils.addQuotesIfNotExist(rawValue);
        }
    }

    /**
     * DOC nrousseau Comment method "getDatabaseValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getDatabaseValue(DatabaseConnection connection, String value) {
        return getDatabaseValue(connection, value, null, null);
    }

    private static boolean isContextMode(Connection connection, String value) {
        IMetadataManagmentUiService mmService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentUiService.class)) {
            mmService = (IMetadataManagmentUiService) GlobalServiceRegister.getDefault()
                    .getService(IMetadataManagmentUiService.class);
        }
        if (mmService != null) {
            return mmService.isContextMode(connection, value);
        }
        return false;
    }

    /**
     * DOC nrousseau Comment method "getFileValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getFileValue(FileConnection connection, String value) {
        if (value.equals("FILE_PATH")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getFilePath())) {
                return connection.getFilePath();
            } else {
                if (connection.getFilePath() != null) {
                    Path p = new Path(connection.getFilePath());
                    return TalendQuoteUtils.addQuotes(p.toPortableString());
                } else {
                    return ""; //$NON-NLS-1$
                }
            }
        }
        if (value.equals("ROW_SEPARATOR")) { //$NON-NLS-1$
            return connection.getRowSeparatorValue();
        }
        if (value.equals("FIELD_SEPARATOR")) { //$NON-NLS-1$
            return connection.getFieldSeparatorValue();
        }
        if (value.equals("HEADER")) { //$NON-NLS-1$
            if (connection.isUseHeader()) {
                return connection.getHeaderValue();
            } else {
                return "0"; //$NON-NLS-1$
            }
        }
        if (value.equals("FOOTER")) { //$NON-NLS-1$
            if (connection.isUseFooter()) {
                return connection.getFooterValue();
            } else {
                return "0"; //$NON-NLS-1$
            }
        }
        if (value.equals("LIMIT")) { //$NON-NLS-1$
            if (connection.isUseLimit()) {
                return connection.getLimitValue();
            } else {
                return ""; //$NON-NLS-1$
            }
        }
        if (value.equals("ENCODING")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEncoding())) {
                return connection.getEncoding();
            } else {
                if (connection.getEncoding() == null) {
                    // get the default encoding
                    return TalendQuoteUtils.addQuotes(EMetadataEncoding.getMetadataEncoding("").getName()); //$NON-NLS-1$
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getEncoding());
                }
            }
        }
        if (value.equals("REMOVE_EMPTY_ROW")) { //$NON-NLS-1$
            return new Boolean(connection.isRemoveEmptyRow());
        }
        if (value.equals("CSV_OPTION")) { //$NON-NLS-1$
            return new Boolean(connection.isCsvOption());
        }
        if (connection instanceof DelimitedFileConnection) {
            return getDelimitedFileValue((DelimitedFileConnection) connection, value);
        }
        if (connection instanceof PositionalFileConnection) {
            return getPositionalFileValue((PositionalFileConnection) connection, value);
        }
        if (connection instanceof RegexpFileConnection) {
            return getRegexpFileValue((RegexpFileConnection) connection, value);
        }
        if (connection instanceof LdifFileConnection) {
            return getLdifFileValue((LdifFileConnection) connection, value);
        }
        if (connection instanceof FileExcelConnection) {
            return getExcelFileValue((FileExcelConnection) connection, value);
        }
        if (connection instanceof EbcdicConnection) {
            return getEBCDICFieldValue((EbcdicConnection) connection, value);
        }
        return null;
    }

    /**
     *
     * ggu Comment method "getEBCDICFieldValue".
     *
     */
    private static Object getEBCDICFieldValue(EbcdicConnection connection, String value) {
        // if ("XC2J_FILE".equals(value)) { //$NON-NLS-1$
        // if (isContextMode(connection, connection.getFilePath())) {
        // return connection.getMidFile();
        // } else {
        // // Path p = new Path(connection.getMidFile());
        // return TalendQuoteUtils.addQuotes("");
        // }
        // }
        if ("DATA_FILE".equals(value)) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getDataFile())) {
                return connection.getDataFile();
            } else {
                Path p = new Path(""); //$NON-NLS-1$
                if (connection.getDataFile() != null) {
                    p = new Path(connection.getDataFile());
                }
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if ("CODE_PAGE".equals(value)) { //$NON-NLS-1$
            if (connection.getCodePage() == null) {
                return null;
            }
            if (isContextMode(connection, connection.getCodePage())) {
                return connection.getCodePage();
            } else {
                Path p = new Path(""); //$NON-NLS-1$
                if (connection.getCodePage() != null) {
                    p = new Path(connection.getCodePage());
                }
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        return null;
    }

    /**
     * DOC yexiaowei Comment method "getExcelFileValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getExcelFileValue(FileExcelConnection connection, String value) {
        if (value.equals("FILE_PATH")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getFilePath())) {
                return connection.getFilePath();
            } else {
                Path p = new Path(connection.getFilePath());
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if (value.equals("SHEET_NAME")) { //$NON-NLS-1$
            return TalendQuoteUtils.addQuotes(connection.getSheetName());
        }

        if (value.equals("SELECT_ALL_SHEETS")) { //$NON-NLS-1$
            return connection.isSelectAllSheets();
        }

        if (value.equals("FIRST_COLUMN")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getFirstColumn())) {
                return connection.getFirstColumn();
            } else {
                if (isPerlProject()) {
                    return TalendQuoteUtils.addQuotes(connection.getFirstColumn());
                } else {
                    return connection.getFirstColumn();
                }
            }
        }
        if (value.equals("LAST_COLUMN")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getLastColumn())) {
                return connection.getLastColumn();
            } else {
                if (isPerlProject()) {
                    if (connection.getLastColumn() != null && !connection.getLastColumn().equals("")) { //$NON-NLS-1$
                        return TalendQuoteUtils.addQuotes(connection.getLastColumn());
                    }
                } else {
                    return connection.getLastColumn();
                }
            }

        }
        if (value.equals("ADVANCED_SEPARATOR")) { //$NON-NLS-1$
            return connection.isAdvancedSpearator();
        }
        if (value.equals("THOUSANDS_SEPARATOR")) { //$NON-NLS-1$
            return connection.getThousandSeparator();
        }
        if (value.equals("DECIMAL_SEPARATOR")) { //$NON-NLS-1$
            return connection.getDecimalSeparator();
        }

        if (value.equals("SHEET_LIST")) { //$NON-NLS-1$
            return getExcelSheetTableValue(connection);
        }

        if (value.equals("GENERATION_MODE")) { //$NON-NLS-1$
            return connection.getGenerationMode();
        }

        return null;
    }

    private static boolean isPerlProject() {
        ECodeLanguage codeLanguage = LanguageManager.getCurrentLanguage();
        return (codeLanguage == ECodeLanguage.PERL);
    }

    /**
     * DOC YeXiaowei Comment method "getExcelSheetTableValue".
     *
     * @param connection
     */
    private static List<Map<String, Object>> getExcelSheetTableValue(FileExcelConnection connection) {
        ArrayList<String> list = connection.getSheetList();
        if (list == null || list.size() <= 0) {
            return null;
        }
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        for (String s : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("SHEETNAME", TalendQuoteUtils.addQuotes(s)); //$NON-NLS-1$
            maps.add(map);
        }
        return maps;

    }

    /**
     * DOC nrousseau Comment method "getPositionalFileValue".
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getPositionalFileValue(PositionalFileConnection connection, String value) {
        if (value.equals("PATTERN")) { //$NON-NLS-1$
            return connection.getFieldSeparatorValue();
        }
        if (value.equals("INCLUDEHEADER")) {
            return connection.isFirstLineCaption();
        }
        return null;
    }

    private static Object getDelimitedFileValue(DelimitedFileConnection connection, String value) {
        if (value.equals("ESCAPE_CHAR")) { //$NON-NLS-1$
            return connection.getEscapeChar();
        }
        if (value.equals("TEXT_ENCLOSURE")) { //$NON-NLS-1$
            return connection.getTextEnclosure();
        }

        if (value.equals("SPLITRECORD")) { //$NON-NLS-1$
            return connection.isSplitRecord();
        }
        return null;
    }

    private static Object getRegexpFileValue(RegexpFileConnection connection, String value) {
        if (value.equals("ESCAPE_CHAR")) { //$NON-NLS-1$
            return connection.getEscapeChar();
        }
        if (value.equals("TEXT_ENCLOSURE")) { //$NON-NLS-1$
            return connection.getTextEnclosure();
        }
        if (value.equals("REGEXP")) { //$NON-NLS-1$
            return connection.getFieldSeparatorValue();
        }
        return null;
    }

    // added by nma to deal with .xsd file
    public static Object getXmlAndXSDFileValue(XmlFileConnection connection, String value) {
        EList list = connection.getSchema();

        if (value.equals("FILE_PATH")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getXmlFilePath())) {
                return connection.getXmlFilePath();
            } else {
                Path p = new Path(connection.getXmlFilePath());
                if ((p.toPortableString()).endsWith("xsd")) { //$NON-NLS-1$
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentUiService.class)) {
                        IMetadataManagmentUiService mmUIService = (IMetadataManagmentUiService) GlobalServiceRegister.getDefault()
                                .getService(IMetadataManagmentUiService.class);
                        String newPath = mmUIService.getAndOpenXSDFileDialog(p);
                        if (newPath != null) {
                            return TalendQuoteUtils.addQuotes(newPath);
                        }
                    }
                }
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if (!list.isEmpty()) {
            XmlXPathLoopDescriptor xmlDesc = (XmlXPathLoopDescriptor) list.get(0);

            if (value.equals("LIMIT")) { //$NON-NLS-1$
                if ((xmlDesc == null) || (xmlDesc.getLimitBoucle() == null)) {
                    return ""; //$NON-NLS-1$
                } else {
                    return xmlDesc.getLimitBoucle().toString();
                }
            }
            if (value.equals("XPATH_QUERY")) { //$NON-NLS-1$
                if (xmlDesc == null) {
                    return ""; //$NON-NLS-1$
                } else {
                    if (isContextMode(connection, xmlDesc.getAbsoluteXPathQuery())) {
                        return xmlDesc.getAbsoluteXPathQuery();
                    } else {
                        return TalendQuoteUtils.addQuotes(xmlDesc.getAbsoluteXPathQuery());
                    }
                }
            }
        }
        if (value.equals("ENCODING")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEncoding())) {
                return connection.getEncoding();
            } else {
                if (connection.getEncoding() == null) {
                    // get the default encoding
                    return TalendQuoteUtils.addQuotes(EMetadataEncoding.getMetadataEncoding("").getName()); //$NON-NLS-1$
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getEncoding());
                }
            }
        }
        return null;
    }

    private static Object getXmlFileValue(XmlFileConnection connection, String value) {
        boolean isInputModel = connection.isInputModel();
        EList list;
        XmlXPathLoopDescriptor xmlDesc = null;
        if (isInputModel) {
            list = connection.getSchema();
            xmlDesc = (XmlXPathLoopDescriptor) list.get(0);
        }
        if (value.equals("FILE_PATH")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getXmlFilePath())) {
                return connection.getXmlFilePath();
            } else {
                Path p = new Path(connection.getXmlFilePath());
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if (value.equals("OUT_FILE_PATH")) {
            if (connection.getOutputFilePath() == null) {
                return "";
            }
            if (isContextMode(connection, connection.getOutputFilePath())) {
                return connection.getOutputFilePath();
            } else {
                Path p = new Path(connection.getOutputFilePath());
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        if (value.equals("LIMIT")) { //$NON-NLS-1$
            if ((xmlDesc == null) || (xmlDesc.getLimitBoucle() == null)) {
                return ""; //$NON-NLS-1$
            } else {
                return xmlDesc.getLimitBoucle().toString();
            }
        }
        if (value.equals("XPATH_QUERY")) { //$NON-NLS-1$
            if (xmlDesc == null) {
                return ""; //$NON-NLS-1$
            } else {
                if (isContextMode(connection, xmlDesc.getAbsoluteXPathQuery())) {
                    return xmlDesc.getAbsoluteXPathQuery();
                } else {
                    return TalendQuoteUtils.addQuotes(xmlDesc.getAbsoluteXPathQuery());
                }
            }
        }
        if (value.equals("ENCODING")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getEncoding())) {
                return connection.getEncoding();
            } else {
                if (connection.getEncoding() == null) {
                    // get the default encoding
                    return TalendQuoteUtils.addQuotes(EMetadataEncoding.getMetadataEncoding("").getName()); //$NON-NLS-1$
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getEncoding());
                }
            }
        }
        if (value.equals("ROOT")) {
            return getOutputXmlValue(connection.getRoot());
        }
        if (value.equals("GROUP")) {
            return getOutputXmlValue(connection.getGroup());
        }
        if (value.equals("LOOP")) {
            return getOutputXmlValue(connection.getLoop());
        }

        return null;
    }

    public static List<Map<String, String>> getOutputXmlValue(EList list) {
        List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<String, String>();
            XMLFileNode node = (XMLFileNode) list.get(i);
            String defaultValue = node.getDefaultValue();
            if (defaultValue == null) {
                defaultValue = ""; //$NON-NLS-1$
            }
            map.put("VALUE", defaultValue);
            map.put("ORDER", String.valueOf(node.getOrder()));
            map.put("PATH", node.getXMLPath());
            map.put("ATTRIBUTE", node.getAttribute());
            map.put("COLUMN", node.getRelatedColumn());
            newList.add(map);
        }
        return newList;

    }

    /**
     * qiang.zhang Comment method "getTableXMLMappingValue".
     *
     * @param connection
     * @param tableInfo
     * @param metaTable
     */
    public static void getTableXMLMappingValue(Connection connection, List<Map<String, Object>> tableInfo,
            IMetadataTable metaTable) {
        if (connection instanceof XmlFileConnection) {
            XmlFileConnection xmlConnection = (XmlFileConnection) connection;
            if (xmlConnection.isInputModel()) {
                EList objectList = xmlConnection.getSchema();
                XmlXPathLoopDescriptor xmlDesc = (XmlXPathLoopDescriptor) objectList.get(0);
                List<SchemaTarget> schemaTargets = xmlDesc.getSchemaTargets();
                tableInfo.clear();
                List<IMetadataColumn> listColumns = metaTable.getListColumns();
                // for (IMetadataColumn metadataColumn : listColumns) {
                // for (SchemaTarget schema : schemaTargets) {
                // // add for bug 12034
                // String label = metadataColumn.getLabel();
                // String tagName = schema.getTagName();
                // if (label.equals(tagName)
                // || (label.length() > 1 && label.startsWith("_") && label.substring(1).equals(tagName) &&
                // KeywordsValidator //$NON-NLS-1$
                // .isKeyword(tagName))) {
                // Map<String, Object> map = new HashMap<String, Object>();
                // map.put("SCHEMA_COLUMN", tagName); //$NON-NLS-1$
                // map.put("QUERY", TalendQuoteUtils.addQuotes(schema.getRelativeXPathQuery())); //$NON-NLS-1$
                // tableInfo.add(map);
                // }
                // }
                // }
                // for (SchemaTarget schema : schemaTargets) {
                // Map<String, Object> map = new HashMap<String, Object>();
                // map.put("SCHEMA_COLUMN", schema.getTagName()); //$NON-NLS-1$
                // map.put("QUERY", TalendQuoteUtils.addQuotes(schema.getRelativeXPathQuery())); //$NON-NLS-1$
                // tableInfo.add(map);
                // }

                String tagName;
                for (int j = 0; j < schemaTargets.size(); j++) {
                    SchemaTarget schemaTarget = schemaTargets.get(j);
                    if (schemaTarget.getTagName() != null && !schemaTarget.getTagName().equals("")) { //$NON-NLS-1$
                        tagName = "" + schemaTarget.getTagName().trim().replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
                        tagName = MetadataToolHelper.validateColumnName(tagName, j);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("SCHEMA_COLUMN", tagName); //$NON-NLS-1$
                        map.put("QUERY", TalendQuoteUtils.addQuotes(schemaTarget.getRelativeXPathQuery())); //$NON-NLS-1$
                        tableInfo.add(map);
                    }
                }
            }
        }
        if (connection instanceof MDMConnection) {
            MDMConnection xmlConnection = (MDMConnection) connection;
            EList objectList = xmlConnection.getSchemas();
            for (Concept concept : (List<Concept>) objectList) {
                if (concept.getLabel() == null || concept.getLabel().equals(metaTable.getLabel())) {
                    List<ConceptTarget> conceptTargets = concept.getConceptTargets();
                    tableInfo.clear();
                    List<IMetadataColumn> listColumns = metaTable.getListColumns();
                    for (IMetadataColumn metadataColumn : listColumns) {
                        for (ConceptTarget schema : conceptTargets) {
                            if (metadataColumn.getLabel().equals(schema.getTargetName())) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("SCHEMA_COLUMN", schema.getTargetName()); //$NON-NLS-1$
                                map.put("QUERY", TalendQuoteUtils.addQuotes(schema.getRelativeLoopExpression())); //$NON-NLS-1$
                                tableInfo.add(map);
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (connection instanceof EDIFACTConnection) {
            EDIFACTConnection edifactConnection = (EDIFACTConnection) connection;
            List<IMetadataColumn> objectList = metaTable.getListColumns();
            Map<String, Object> map = new HashMap<String, Object>();
            for (IMetadataColumn column : objectList) {
                if (column instanceof EDIFACTColumn) {
                    EDIFACTColumn edicolumn = (EDIFACTColumn) column;
                    String ediColumnName = edicolumn.getEDIColumnName();
                    String ediXpath = edicolumn.getEDIXpath();
                    map.put("COLUMN_NAME", ediColumnName); //$NON-NLS-1$
                    map.put("XPATH", ediXpath); //$NON-NLS-1$
                    tableInfo.add(map);
                }
            }
        }
    }

    /**
     * qiang.zhang Comment method "getTableXMLMappingValue".
     *
     * @param connection
     * @param tableInfo
     * @param metaTable
     */
    public static void getTableXMLMappingValue(Connection connection, List<Map<String, Object>> tableInfo,
            IMetadataTable metaTable, Map<String, String> colRenameMap) {
        if (connection instanceof XmlFileConnection) {
            XmlFileConnection xmlConnection = (XmlFileConnection) connection;
            if (xmlConnection.isInputModel()) {
                EList objectList = xmlConnection.getSchema();
                XmlXPathLoopDescriptor xmlDesc = (XmlXPathLoopDescriptor) objectList.get(0);
                List<SchemaTarget> schemaTargets = xmlDesc.getSchemaTargets();
                tableInfo.clear();
                List<IMetadataColumn> listColumns = metaTable.getListColumns();

                String tagName;
                for (int j = 0; j < schemaTargets.size(); j++) {
                    SchemaTarget schemaTarget = schemaTargets.get(j);
                    boolean foundColumn = false;
                    for (IMetadataColumn metadataColumn : listColumns) {
                        if (metadataColumn.getLabel().equals(schemaTarget.getTagName())) {
                            foundColumn = true;
                            tagName = "" + schemaTarget.getTagName().trim().replaceAll(" ", "_"); //$NON-NLS-1$ //$NON-NLS-2$
                            tagName = MetadataToolHelper.validateColumnName(tagName, j);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("SCHEMA_COLUMN", tagName); //$NON-NLS-1$
                            map.put("QUERY", TalendQuoteUtils.addQuotes(schemaTarget.getRelativeXPathQuery())); //$NON-NLS-1$
                            tableInfo.add(map);
                        }
                    }
                    // if can not found noramlly,so maybe the column name changed,use rename map we always found the new
                    // column name here
                    if (!foundColumn && colRenameMap != null && !colRenameMap.isEmpty()) {
                        Set<String> newNameSet = colRenameMap.keySet();
                        for (IMetadataColumn metadataColumn : listColumns) {
                            if (newNameSet.contains(metadataColumn.getLabel())) {
                                String oldColLabel = colRenameMap.get((metadataColumn.getLabel()));
                                if (schemaTarget.getTagName().equals(oldColLabel)) {
                                    foundColumn = true;
                                    schemaTarget.setTagName(metadataColumn.getLabel());
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("SCHEMA_COLUMN", metadataColumn.getLabel()); //$NON-NLS-1$
                                    map.put("QUERY", TalendQuoteUtils.addQuotes(schemaTarget.getRelativeXPathQuery())); //$NON-NLS-1$
                                    tableInfo.add(map);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (connection instanceof MDMConnection) {
            MDMConnection xmlConnection = (MDMConnection) connection;
            EList objectList = xmlConnection.getSchemas();
            for (Concept concept : (List<Concept>) objectList) {
                if (concept.getLabel() == null || concept.getLabel().equals(metaTable.getLabel())) {
                    List<ConceptTarget> conceptTargets = concept.getConceptTargets();
                    tableInfo.clear();
                    List<IMetadataColumn> listColumns = metaTable.getListColumns();
                    for (IMetadataColumn metadataColumn : listColumns) {
                        for (ConceptTarget schema : conceptTargets) {
                            if (metadataColumn.getLabel().equals(schema.getTargetName())) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("SCHEMA_COLUMN", schema.getTargetName()); //$NON-NLS-1$
                                map.put("QUERY", TalendQuoteUtils.addQuotes(schema.getRelativeLoopExpression())); //$NON-NLS-1$
                                tableInfo.add(map);
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (connection instanceof EDIFACTConnection) {
            EDIFACTConnection edifactConnection = (EDIFACTConnection) connection;
            List<IMetadataColumn> objectList = metaTable.getListColumns();
            Map<String, Object> map = new HashMap<String, Object>();
            for (IMetadataColumn column : objectList) {
                if (column instanceof EDIFACTColumn) {
                    EDIFACTColumn edicolumn = (EDIFACTColumn) column;
                    String ediColumnName = edicolumn.getEDIColumnName();
                    String ediXpath = edicolumn.getEDIXpath();
                    map.put("COLUMN_NAME", ediColumnName); //$NON-NLS-1$
                    map.put("XPATH", ediXpath); //$NON-NLS-1$
                    tableInfo.add(map);
                }
            }
        }
    }

    public static void getTableXMLMappingValue(Connection connection, List<Map<String, Object>> tableInfo, INode node) {
        List<IMetadataTable> metaTables = node.getMetadataList();

        if (connection instanceof XmlFileConnection || connection instanceof MDMConnection) {
            getTableXMLMappingValue(connection, tableInfo, metaTables.get(0));
        } else if (connection instanceof HL7Connection) {
            List<IMetadataTable> newMetaTables = new ArrayList<IMetadataTable>(metaTables);
            HL7Connection hl7Connection = (HL7Connection) connection;
            tableInfo.clear();
            for (MetadataTable repTable : ConnectionHelper.getTables(connection)) {
                IMetadataTable metaTable = null;
                Iterator<IMetadataTable> iterator = newMetaTables.iterator();
                while (iterator.hasNext()) {
                    IMetadataTable nodeTable = iterator.next();
                    if (repTable.getLabel() != null && repTable.getLabel().equals(nodeTable.getLabel())) {
                        metaTable = nodeTable;
                        iterator.remove();
                        break;
                    }
                }
                String xpathValue = "";
                for (MetadataColumn col : repTable.getColumns()) {
                    String original = col.getOriginalField();
                    if (original != null && !"".equals(original)) {
                        if (original.indexOf(TalendQuoteUtils.LBRACKET) != -1) {
                            original = original.substring(0, original.indexOf(TalendQuoteUtils.LBRACKET));
                        }
                        original = TalendQuoteUtils.addQuotes(original);
                        xpathValue += original;
                    }
                    if (repTable.getColumns().indexOf(col) < repTable.getColumns().size() - 1) {
                        xpathValue += ",";
                    }
                }
                Map<String, Object> map = new HashMap<String, Object>();
                if (metaTable != null) {
                    map.put("SCHEMA", metaTable.getTableName());

                } else {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentService.class)) {
                        IMetadataManagmentService mmService = (IMetadataManagmentService) GlobalServiceRegister.getDefault()
                                .getService(IMetadataManagmentService.class);
                        IMetadataTable convert = mmService.convertMetadataTable(repTable);
                        String uinqueTableName = node.getProcess()
                                .generateUniqueConnectionName(MultiSchemasUtil.getConnectionBaseName(repTable.getLabel()));
                        convert.setTableName(uinqueTableName);
                        // IProxyRepositoryFactory factory =
                        // CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory();
                        // IMetadataTable newMetadata = new org.talend.core.model.metadata.MetadataTable();
                        // newMetadata.setAttachedConnector(EConnectionType.FLOW_MAIN.getName());
                        // newMetadata.setTableName(uinqueTableName);
                        node.getProcess().addUniqueConnectionName(uinqueTableName);
                        node.getMetadataList().add(convert);
                        map.put("SCHEMA", uinqueTableName);
                    }
                }
                map.put("MAPPING", xpathValue);
                tableInfo.add(map);

            }
            if (!newMetaTables.isEmpty()) {
                metaTables.removeAll(newMetaTables);
                for (IMetadataTable table : newMetaTables) {
                    node.getProcess().removeUniqueConnectionName(table.getTableName());
                }
            }
        }

    }

    public static void getTableXmlFileValue(Connection connection, String value, IElementParameter param,
            List<Map<String, Object>> tableInfo, IMetadataTable metaTable) {
        if (connection instanceof XmlFileConnection) {
            XmlFileConnection xmlConnection = (XmlFileConnection) connection;
            if (xmlConnection.isInputModel()) {
                EList objectList = xmlConnection.getSchema();
                XmlXPathLoopDescriptor xmlDesc = (XmlXPathLoopDescriptor) objectList.get(0);
                if (value.equals("XML_MAPPING")) { //$NON-NLS-1$
                    if (xmlDesc == null) {
                        return;
                    } else {
                        String[] list = param.getListRepositoryItems();

                        int column = 0;
                        boolean found = false;
                        for (int k = 0; (k < list.length) && (!found); k++) {
                            if (list[k].equals("XML_QUERY")) { //$NON-NLS-1$
                                column = k;
                                found = true;
                            }
                        }
                        EList schemaList = xmlDesc.getSchemaTargets();
                        String[] names = param.getListItemsDisplayCodeName();
                        for (int k = 0; k < schemaList.size(); k++) {
                            if (tableInfo.size() > k) {
                                Map<String, Object> line = tableInfo.get(k);
                                if (metaTable != null) {
                                    if (metaTable.getListColumns().size() > k) {
                                        SchemaTarget schemaTarget = (SchemaTarget) schemaList.get(k);
                                        String strValue = TalendQuoteUtils.addQuotes(schemaTarget.getRelativeXPathQuery());
                                        line.put(names[column], strValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (connection instanceof MDMConnection) {
            MDMConnection mdmConnection = (MDMConnection) connection;
            EList objectList = mdmConnection.getSchemas();
            for (Concept concept : (List<Concept>) objectList) {
                if (concept.getLabel() != null && concept.getLabel().equals(metaTable.getLabel())) {
                    if (value.equals("XML_MAPPING")) { //$NON-NLS-1$
                        String[] list = param.getListRepositoryItems();

                        int column = 0;
                        boolean found = false;
                        for (int k = 0; (k < list.length) && (!found); k++) {
                            if (list[k].equals("XML_QUERY")) { //$NON-NLS-1$
                                column = k;
                                found = true;
                            }
                        }
                        EList conceptTargetsList = concept.getConceptTargets();
                        String[] names = param.getListItemsDisplayCodeName();
                        for (int k = 0; k < conceptTargetsList.size(); k++) {
                            if (tableInfo.size() > k) {
                                Map<String, Object> line = tableInfo.get(k);
                                if (metaTable != null) {
                                    if (metaTable.getListColumns().size() > k) {
                                        ConceptTarget conceptTarget = (ConceptTarget) conceptTargetsList.get(k);
                                        String strValue = TalendQuoteUtils.addQuotes(conceptTarget.getRelativeLoopExpression());
                                        line.put(names[column], strValue);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public static List<Map<String, Object>> getColumnMappingValue(Connection connection, IMetadataTable metaTable) {
        if (connection == null || metaTable == null) {
            return null;
        }

        List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();
        List<IMetadataColumn> columns = metaTable.getListColumns();
        if (connection instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) connection;
            String databaseType = dbConn.getDatabaseType();
            if (EDatabaseTypeName.HBASE.getDisplayName().equals(databaseType)
                    || EDatabaseTypeName.MAPRDB.getDisplayName().equals(databaseType)) {
                for (IMetadataColumn column : columns) {
                    Map<String, Object> row = new HashMap<String, Object>();
                    row.put("SCHEMA_COLUMN", column.getLabel()); //$NON-NLS-1$
                    row.put("FAMILY_COLUMN", TalendQuoteUtils.addQuotes(column.getAdditionalField().get("COLUMN FAMILY"))); //$NON-NLS-1$ //$NON-NLS-2$
                    values.add(row);
                }
            }
        }

        return values;
    }

    private static Object getLdifFileValue(LdifFileConnection connection, String value) {
        if (value.equals("FILE_PATH")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getFilePath())) {
                return connection.getFilePath();
            } else {
                Path p = new Path(connection.getFilePath());
                return TalendQuoteUtils.addQuotes(p.toPortableString());
            }
        }
        return null;
    }

    /**
     * Gets repository value for LDAP schema.
     *
     * @param connection
     * @param value
     * @return
     */
    private static Object getLDAPValue(LDAPSchemaConnection connection, String value) {

        if (value.equals("HOST")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getHost())) {
                return connection.getHost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getHost()).replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }

        if (value.equals("PORT")) { //$NON-NLS-1$
            return connection.getPort();
        }

        if (value.equals("BASEDN")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getSelectedDN())) {
                return connection.getSelectedDN();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getSelectedDN()).replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        String protocol = connection.getProtocol();// Simple or Anonymous
        if (value.equals("PROTOCOL")) { //$NON-NLS-1$
            String encryptionMethodName = connection.getEncryptionMethodName();
            if (encryptionMethodName.equals("LDAPS(SSL)")) { //$NON-NLS-1$
                return "LDAPS"; //$NON-NLS-1$
            }
        }

        boolean useAuthen = connection.isUseAuthen();
        if (value.equals("AUTHENTIFICATION")) { //$NON-NLS-1$
            return new Boolean(useAuthen);
        }

        if (useAuthen && value.equals("USER")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getBindPrincipal())) {
                return connection.getBindPrincipal();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getBindPrincipal()).replaceAll("\\\\", "\\\\\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        if (useAuthen && value.equals("PASSWORD")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getBindPassword())) {
                return connection.getBindPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getBindPassword(), false)).replaceAll("\\\\", //$NON-NLS-1$
                        "\\\\\\\\"); //$NON-NLS-1$
            }
        }
        if (value.equals("FILTER")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getFilter())) {
                return connection.getFilter();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getFilter());
            }
        }

        if (value.equals("MULTI_VALUE_SEPARATOR")) { //$NON-NLS-1$
            String separator = connection.getSeparator();
            return separator == null ? TalendQuoteUtils.addQuotes(",") : TalendQuoteUtils.addQuotes(separator); //$NON-NLS-1$
        }

        if (value.equals("COLUMN_COUNT_LIMIT")) { //$NON-NLS-1$
            return connection.getCountLimit();
        }

        if (value.equals("TIME_OUT_LIMIT")) { //$NON-NLS-1$
            return connection.getTimeOutLimit();
        }

        if (value.equals("ALIASES")) { //$NON-NLS-1$
            return connection.getAliases();
        }

        if (value.equals("REFERRALS")) { //$NON-NLS-1$
            return connection.getReferrals();
        }
        return null;
    }

    /**
     * DOC qiang.zhang Comment method "getXMLMappingValue".
     *
     * @param repositoryConnection
     * @param metadataTable
     * @return
     */
    public static List<Map<String, Object>> getXMLMappingValue(Connection connection, List<IMetadataTable> metadataTables,
            Map<String, String> colRenameMap) {
        if (metadataTables == null || metadataTables.isEmpty()) {
            return new ArrayList<Map<String, Object>>();
        }

        if (connection instanceof XmlFileConnection) {
            IMetadataTable metadataTable = metadataTables.get(0);
            XmlFileConnection xmlConnection = (XmlFileConnection) connection;
            EList objectList = xmlConnection.getSchema();
            XmlXPathLoopDescriptor xmlDesc = (XmlXPathLoopDescriptor) objectList.get(0);
            if (metadataTable != null) {
                if (xmlDesc != null) {
                    List<SchemaTarget> schemaTargets = xmlDesc.getSchemaTargets();
                    List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
                    for (IMetadataColumn col : metadataTable.getListColumns()) {
                        boolean foundColumn = false;
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("QUERY", null); //$NON-NLS-1$
                        for (int i = 0; i < schemaTargets.size(); i++) {
                            SchemaTarget sch = schemaTargets.get(i);
                            if (col.getLabel().equals(sch.getTagName())) {
                                // map.put("SCHEMA_COLUMN", sch.getTagName());
                                foundColumn = true;
                                map.put("QUERY", TalendQuoteUtils.addQuotes(sch.getRelativeXPathQuery())); //$NON-NLS-1$
                            }
                        }
                        if (!foundColumn && colRenameMap != null && !colRenameMap.isEmpty()) {
                            Set<String> newNameSet = colRenameMap.keySet();
                            for (int i = 0; i < schemaTargets.size(); i++) {
                                SchemaTarget sch = schemaTargets.get(i);
                                if (newNameSet.contains(sch.getTagName())) {
                                    String oldColLabel = colRenameMap.get(sch.getTagName());
                                    if (col.getLabel().equals(oldColLabel)) {
                                        foundColumn = true;
                                        map.put("QUERY", TalendQuoteUtils.addQuotes(sch.getRelativeXPathQuery())); //$NON-NLS-1$
                                    }
                                }
                            }
                        }
                        // if the Xml File Connection have the Keyword Column, can not get QUERY value .
                        if (map.get(UpdatesConstants.QUERY) == null) {
                            if (col.getLabel() != null && col.getLabel().startsWith("Column")) {
                                String temp = col.getLabel().substring(6, col.getLabel().length());
                                if (temp != null) {
                                    char c[] = temp.toCharArray();
                                    boolean flag = true;
                                    for (char element : c) {
                                        if (element < '0' || element > '9') {
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        int j = Integer.parseInt(temp);
                                        SchemaTarget schema = schemaTargets.get(j);
                                        if (schema != null && KeywordsValidator.isKeyword(schema.getTagName())) {
                                            map.put("QUERY", TalendQuoteUtils.addQuotes(schema.getRelativeXPathQuery())); //$NON-NLS-1$
                                        }
                                    }
                                }
                            }
                        }
                        maps.add(map);
                    }

                    // for (int i = 0; i < schemaTargets.size(); i++) {
                    // Map<String, Object> map = new HashMap<String, Object>();
                    // SchemaTarget sch = schemaTargets.get(i);
                    // map.put("QUERY", TalendQuoteUtils.addQuotes(sch.getRelativeXPathQuery()));
                    // maps.add(map);
                    // }
                    return maps;
                }
            }
        }
        if (connection instanceof MDMConnection) {
            IMetadataTable metadataTable = metadataTables.get(0);
            MDMConnection xmlConnection = (MDMConnection) connection;
            EList objectList = xmlConnection.getSchemas();
            if (metadataTable != null) {
                for (Concept concept : (List<Concept>) objectList) {
                    // test if sourcename is null, this is only for compatibility with first mdm repository released.
                    if (concept != null && concept.getLabel() != null && concept.getLabel().equals(metadataTable.getLabel())) {
                        List<ConceptTarget> conceptTargets = concept.getConceptTargets();
                        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
                        for (IMetadataColumn col : metadataTable.getListColumns()) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("QUERY", null); //$NON-NLS-1$
                            for (int i = 0; i < conceptTargets.size(); i++) {
                                ConceptTarget cpt = conceptTargets.get(i);
                                if (col.getLabel().equals(cpt.getTargetName())) {
                                    // map.put("SCHEMA_COLUMN", sch.getTagName());
                                    map.put("QUERY", TalendQuoteUtils.addQuotes(cpt.getRelativeLoopExpression())); //$NON-NLS-1$
                                }
                            }
                            maps.add(map);
                        }
                        return maps;
                    }
                }
            }
        }
        if (connection instanceof HL7Connection) {
            HL7Connection hl7Connection = (HL7Connection) connection;
            Set objectList = ConnectionHelper.getTables(hl7Connection);
            List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
            for (IMetadataTable tableOfNode : metadataTables) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (MetadataTable table : (Set<MetadataTable>) objectList) {
                    if (table != null && (table.getLabel() == null || table.getLabel().equals(tableOfNode.getLabel()))) {
                        String xpathValue = "";
                        for (MetadataColumn col : table.getColumns()) {
                            String original = col.getOriginalField();
                            if (original != null && !"".equals(original)) {
                                if (original.indexOf(TalendQuoteUtils.LBRACKET) != -1) {
                                    original = original.substring(0, original.indexOf(TalendQuoteUtils.LBRACKET));
                                }
                                original = TalendQuoteUtils.addQuotes(original);
                                xpathValue += original;
                            }
                            if (table.getColumns().indexOf(col) < table.getColumns().size() - 1) {
                                xpathValue += ",";
                            }
                        }

                        map.put("MAPPING", xpathValue);
                        map.put("SCHEMA", tableOfNode.getTableName());
                        maps.add(map);
                    }
                }

            }
            return maps;
        }
        return null;
    }

    private static Object getFTPValue(FTPConnection connection, String value) {

        if (value.equals("SERVER_NAME")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getHost())) {
                return connection.getHost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getHost());
            }
        }
        if (value.equals("PORT")) { //$NON-NLS-1$
            return connection.getPort(); // no quote in component
            // if (isContextMode(connection, connection.getPort())) {
            // return connection.getPort();
            // } else {
            // return TalendQuoteUtils.addQuotes(connection.getPort());
            // }
        }
        if (value.equals("USERNAME")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getUsername())) {
                return connection.getUsername();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getUsername());
            }
        }
        if (value.equals("PASSWORD")) { //$NON-NLS-1$
            if (isContextMode(connection, connection.getPassword())) {
                return connection.getPassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassword(), false));
            }
        }
        if (value.equals("KEYSTORE_FILE")) {
            if (isContextMode(connection, connection.getKeystoreFile())) {
                return connection.getKeystoreFile();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getKeystoreFile());
            }
        }

        if (value.equals("KEYSTROE_PASS")) {
            if (isContextMode(connection, connection.getKeystorePassword())) {
                return connection.getKeystorePassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getKeystorePassword(), false));
            }
        }
        if (value.equals("PRIVATEKEY")) {
            if (isContextMode(connection, connection.getPrivatekey())) {
                return connection.getPrivatekey();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getPrivatekey());
            }
        }

        if (value.equals("PASSPHRASE")) {
            if (isContextMode(connection, connection.getPassphrase())) {
                return connection.getPassphrase();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getPassphrase(), false));
            }
        }
        if (value.equals("AUTH_METHOD")) {
            if (isContextMode(connection, connection.getMethod())) {
                return connection.getMethod();
            } else {
                if (connection.getMethod().equals("Public key")) {
                    return "PUBLICKEY";
                } else if (connection.getMethod().equals("Password")) {
                    return "PASSWORD";
                }
                return TalendQuoteUtils.addQuotes(connection.getMethod());
            }
        }
        if (value.equals("SFTP")) {
            return connection.isSFTP();
        }
        if (value.equals("FTPS")) {
            return connection.isFTPS();
        }
        if (value.equals("CONNECT_MODE")) {
            if (connection.getMode() == null) {
                return "";
            }
            return connection.getMode().toUpperCase();
        }

        if (value.equals("USE_ENCODING")) {
            return connection.isUseFileNameEncoding();
        }

        if (value.equals("ENCODING")) {
            if (isContextMode(connection, connection.getCustomEncode())) {
                return connection.getCustomEncode();
            } else {
                if (connection.getCustomEncode() == null) {
                    // get the default encoding
                    return TalendQuoteUtils.addQuotes(EMetadataEncoding.getMetadataEncoding("").getName()); //$NON-NLS-1$
                } else {
                    return TalendQuoteUtils.addQuotes(connection.getCustomEncode());
                }
            }
        }
        if (value.equals("USE_PROXY")) {
            return connection.isUsesocks();
        }
        if (value.equals("PROXY_HOST")) {
            if (isContextMode(connection, connection.getProxyhost())) {
                return connection.getProxyhost();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyhost());
            }
        }
        if (value.equals("PROXY_PORT")) {
            if (isContextMode(connection, connection.getProxyport())) {
                return connection.getProxyport();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyport());
            }
        }
        if (value.equals("PROXY_USERNAME")) {
            if (isContextMode(connection, connection.getProxyuser())) {
                return connection.getProxyuser();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getProxyuser());
            }
        }
        if (value.equals("PROXY_PASSWORD")) {
            if (isContextMode(connection, connection.getProxypassword())) {
                return connection.getProxypassword();
            } else {
                return TalendQuoteUtils.addQuotes(connection.getValue(connection.getProxypassword(), false));
            }
        }
        return null;
    }

    private static Object getBRMSValue(BRMSConnection connection, String value) {
        if (value.equals("XML_FIELD")) {
            return connection.getXmlField();
        }
        if (value.equals("GUVNOR_URL")) {
            return addQuotesIfNecessary(connection, connection.getUrlName());
        }
        if (value.equals("TAC_WEBAPP")) {
            return addQuotesIfNecessary(connection, connection.getTacWebappName());
        }
        if (value.equals("CLASS_NAME")) {
            return addQuotesIfNecessary(connection, connection.getClassName());
        }
        if (value.equals("GUVNOR_PACKAGE")) {
            return addQuotesIfNecessary(connection, connection.getPackage());
        }
        if (value.equals("MODULE_USED")) {
            return connection.getModuleUsed();
        }
        return null;
    }

    private static Object addQuotesIfNecessary(Connection connection, String value) {
        if (!isContextMode(connection, value)) {
            if (!value.startsWith(TalendQuoteUtils.QUOTATION_MARK) && !value.endsWith(TalendQuoteUtils.QUOTATION_MARK)) {
                return TalendQuoteUtils.addQuotes(value);
            }
        }
        return value;
    }

    public static boolean isGenericRepositoryValue(Connection connection, List<ComponentProperties> componentProperties,
            String paramName) {
        for (IDragAndDropServiceHandler handler : DragAndDropManager.getHandlers()) {
            if (handler.canHandle(connection)) {
                return handler.isGenericRepositoryValue(componentProperties, paramName);
            }
        }
        return false;
    }

    public static Object getGenericRepositoryValue(Connection connection, List<ComponentProperties> componentProperties,
            String paramName) {
        for (IDragAndDropServiceHandler handler : DragAndDropManager.getHandlers()) {
            if (handler.canHandle(connection)) {
                return handler.getGenericRepositoryValue(componentProperties, paramName);
            }
        }
        return null;
    }
}
