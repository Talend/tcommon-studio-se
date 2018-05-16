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
package org.talend.metadata.managment.ui.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.metadata.ISAPConstant;
import org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty;
import org.talend.core.model.metadata.builder.connection.ConnectionFactory;
import org.talend.core.model.metadata.builder.connection.FTPConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.SchemaTarget;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.builder.connection.XmlXPathLoopDescriptor;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.cwm.helper.TaggedValueHelper;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.model.IConnParamName;

/**
 * ggu class global comment. Detailled comment
 */
public final class OtherConnectionContextUtils {

    private static final ECodeLanguage LANGUAGE = LanguageManager.getCurrentLanguage();

    private static final String BASIC = "basic";

    /**
     * 
     */
    public enum EParamName implements IConnParamName {
        FilePath,

        // xml
        // Encoding,
        XmlFilePath,
        XPathQuery,
        OutputFilePath,

        // Salesforce
        WebServiceUrl,
        UserName,
        Password,
        BatchSize,
        TimeOut,
        QueryCondition,
        SFProxyHost,
        SFProxyPort,
        SFProxyUsername,
        SFProxyPassword,
        // Salesforce oauth
        WebServiceUrlForOauth,
        ConsumerKey,
        ConsumerSecret,
        CallbackHost,
        CallbackPort,
        SalesforceVersion,
        token,

        // LDAP
        Host,
        Port,
        BindPassword,
        BindPrincipal,
        Filter,
        CountLimit,
        TimeOutLimit,
        BaseDN,

        // WSDL
        WSDL,
        ProxyHost,
        ProxyPort,
        ProxyUser,
        ProxyPassword,
        MethodName,
        EndpointURI,
        Encoding,
        // These constants seems to be used by deprecated code
        // Added temporary to avoid studio build failure
        MDMURL,
        UNIVERSE,
        DATACLUSTER,
        DATAMODEL,
        // MDM
        MDM_URL,
        MDM_Username,
        MDM_Password,

        // FTP
        FTPHOST,
        FTPPORT,
        FTPUAERNAME,
        FTPPASSWORD,

        // DATACERT CONNECTION
        URL,
        Directory,
        // for sap
        Client,
        SystemNumber,
        Language,
        // Hana database properties
        DbHost,
        DbPort,
        DbSchema,
        DbUsername,
        DbPassword,
    }

    /*
     * LdifFile
     */
    static List<IContextParameter> getLdifFileVariables(String prefixName, LdifFileConnection conn) {
        if (conn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        // TODO there is some problem for the DND
        paramName = prefixName + EParamName.FilePath;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getFilePath(), JavaTypesManager.FILE);

        return varList;
    }

    static void setLdifFilePropertiesForContextMode(String prefixName, LdifFileConnection conn) {
        if (conn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.FilePath;
        conn.setFilePath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
    }

    static void setLdifFileForExistContextMode(LdifFileConnection conn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        String ldifVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                ldifVariableName = "";
                EParamName ldifParam = (EParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(ldifParam.name())) {
                                ldifVariableName = model.getName();
                                break;
                            }
                        }
                    }
                }
                if (ldifParam.equals(EParamName.FilePath)) {
                    conn.setFilePath(ContextParameterUtils.getNewScriptCode(ldifVariableName, LANGUAGE));
                }
            }

        }
    }

    static void revertLdifFilePropertiesForContextMode(LdifFileConnection conn, ContextType contextType) {
        if (conn == null || contextType == null) {
            return;
        }
        String filePath = ConnectionContextHelper.getOriginalValue(contextType, conn.getFilePath());
        filePath = TalendQuoteUtils.removeQuotes(filePath);
        conn.setFilePath(filePath);
    }

    @SuppressWarnings("unchecked")
    public static LdifFileConnection cloneOriginalValueLdifFileConnection(LdifFileConnection fileConn, ContextType contextType) {
        if (fileConn == null) {
            return null;
        }

        LdifFileConnection cloneConn = ConnectionFactory.eINSTANCE.createLdifFileConnection();

        String filePath = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getFilePath());
        filePath = TalendQuoteUtils.removeQuotes(filePath);
        cloneConn.setFilePath(filePath);

        cloneConn.setLimitEntry(fileConn.getLimitEntry());
        cloneConn.setServer(fileConn.getServer());
        cloneConn.setUseLimit(fileConn.isUseLimit());

        ConnectionContextHelper.cloneConnectionProperties(fileConn, cloneConn);

        EList values = fileConn.getValue();
        if (values != null && values instanceof BasicEList) {
            cloneConn.getValue().addAll((EList) ((BasicEList) values).clone());
        }
        return cloneConn;
    }

    /*
     * get variables for context .So need to add Quotes.
     */
    static List<IContextParameter> getXmlFileVariables(String prefixName, XmlFileConnection conn) {

        if (conn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        if (!conn.isInputModel()) {
            String outputFilePath = conn.getOutputFilePath();
            paramName = prefixName + EParamName.OutputFilePath;
            ConnectionContextHelper.createParameters(varList, paramName, outputFilePath, JavaTypesManager.FILE);
        } else {

            String xmlFilePath = conn.getXmlFilePath();
            String encoding = conn.getEncoding();

            if (LANGUAGE.equals(ECodeLanguage.PERL)) {
                xmlFilePath = TalendQuoteUtils.addQuotes(xmlFilePath);
                encoding = TalendQuoteUtils.addQuotes(encoding);
            }
            paramName = prefixName + EParamName.XmlFilePath;
            ConnectionContextHelper.createParameters(varList, paramName, xmlFilePath, JavaTypesManager.FILE);

            paramName = prefixName + EParamName.Encoding;
            ConnectionContextHelper.createParameters(varList, paramName, encoding);

            EList schema = conn.getSchema();
            if (schema != null) {
                Object object = schema.get(0);
                if (object instanceof XmlXPathLoopDescriptor) {
                    XmlXPathLoopDescriptor loopDesc = (XmlXPathLoopDescriptor) object;
                    paramName = prefixName + EParamName.XPathQuery;
                    String absoluteXPathQuery = loopDesc.getAbsoluteXPathQuery();
                    if (LANGUAGE.equals(ECodeLanguage.PERL)) {
                        absoluteXPathQuery = TalendQuoteUtils.addQuotes(absoluteXPathQuery);
                    }
                    ConnectionContextHelper.createParameters(varList, paramName, absoluteXPathQuery);
                }
            }
        }

        return varList;
    }

    static void setXmlFilePropertiesForContextMode(String prefixName, XmlFileConnection conn) {
        if (conn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        if (conn.isInputModel()) {
            paramName = prefixName + EParamName.XmlFilePath;
            conn.setXmlFilePath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
            paramName = prefixName + EParamName.Encoding;
            conn.setEncoding(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
            EList schema = conn.getSchema();
            if (schema != null) {
                if (schema.get(0) instanceof XmlXPathLoopDescriptor) {
                    XmlXPathLoopDescriptor descriptor = (XmlXPathLoopDescriptor) schema.get(0);
                    paramName = prefixName + EParamName.XPathQuery;
                    descriptor.setAbsoluteXPathQuery(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
                }
            }
        } else {
            paramName = prefixName + EParamName.OutputFilePath;
            conn.setOutputFilePath(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
        }

    }

    static void setXmlFileForExistContextMode(XmlFileConnection conn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        if (conn == null) {
            return;
        }
        String xmlVaribleName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                xmlVaribleName = "";
                EParamName xmlParam = (EParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(xmlParam.name())) {
                                xmlVaribleName = model.getName();
                                break;
                            }
                        }
                    }
                }
                if (conn.isInputModel()) {
                    switch (xmlParam) {
                    case Encoding:
                        conn.setXmlFilePath(ContextParameterUtils.getNewScriptCode(xmlVaribleName, LANGUAGE));
                        break;
                    case XmlFilePath:
                        conn.setXmlFilePath(ContextParameterUtils.getNewScriptCode(xmlVaribleName, LANGUAGE));
                        break;
                    case XPathQuery:
                        EList schema = conn.getSchema();
                        if (schema != null) {
                            if (schema.get(0) instanceof XmlXPathLoopDescriptor) {
                                XmlXPathLoopDescriptor descriptor = (XmlXPathLoopDescriptor) schema.get(0);
                                descriptor
                                        .setAbsoluteXPathQuery(ContextParameterUtils.getNewScriptCode(xmlVaribleName, LANGUAGE));
                            }
                        }
                    default:
                    }

                } else {
                    if (xmlParam.equals(EParamName.OutputFilePath)) {
                        conn.setOutputFilePath(ContextParameterUtils.getNewScriptCode(xmlVaribleName, LANGUAGE));
                    }
                }
            }
        }
    }

    static void revertXmlFilePropertiesForContextMode(XmlFileConnection conn, ContextType contextType) {
        if (conn == null || contextType == null) {
            return;
        }
        if (!conn.isInputModel()) {
            String outputFilePath = ConnectionContextHelper.getOriginalValue(contextType, conn.getOutputFilePath());
            outputFilePath = TalendQuoteUtils.removeQuotes(outputFilePath);
            conn.setOutputFilePath(outputFilePath);
        } else {
            String filePath = ConnectionContextHelper.getOriginalValue(contextType, conn.getXmlFilePath());
            String encoding = ConnectionContextHelper.getOriginalValue(contextType, conn.getEncoding());

            filePath = TalendQuoteUtils.removeQuotes(filePath);
            conn.setXmlFilePath(filePath);
            encoding = TalendQuoteUtils.removeQuotes(encoding);
            conn.setEncoding(encoding);
            EList schema = conn.getSchema();
            if (schema != null) {
                if (schema.get(0) instanceof XmlXPathLoopDescriptor) {
                    XmlXPathLoopDescriptor descriptor = (XmlXPathLoopDescriptor) schema.get(0);
                    String xPahtQuery = ConnectionContextHelper.getOriginalValue(contextType, descriptor.getAbsoluteXPathQuery());
                    xPahtQuery = TalendQuoteUtils.removeQuotes(xPahtQuery);
                    descriptor.setAbsoluteXPathQuery(xPahtQuery);
                }
            }

        }
    }

    @SuppressWarnings("unchecked")
    public static XmlFileConnection cloneOriginalValueXmlFileConnection(XmlFileConnection fileConn, ContextType contextType) {
        if (fileConn == null) {
            return null;
        }

        XmlFileConnection cloneConn = ConnectionFactory.eINSTANCE.createXmlFileConnection();

        String filePath = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getXmlFilePath());
        String encoding = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getEncoding());

        filePath = TalendQuoteUtils.removeQuotes(filePath);
        cloneConn.setXmlFilePath(filePath);
        encoding = TalendQuoteUtils.removeQuotes(encoding);
        cloneConn.setEncoding(encoding);
        //
        cloneConn.setMaskXPattern(fileConn.getMaskXPattern());
        cloneConn.setXsdFilePath(fileConn.getXsdFilePath());
        cloneConn.setGuess(fileConn.isGuess());

        ConnectionContextHelper.cloneConnectionProperties(fileConn, cloneConn);

        cloneConn.getSchema().clear();

        List<XmlXPathLoopDescriptor> descs = fileConn.getSchema();
        for (XmlXPathLoopDescriptor desc : descs) {
            XmlXPathLoopDescriptor cloneDesc = ConnectionFactory.eINSTANCE.createXmlXPathLoopDescriptor();
            cloneDesc.setLimitBoucle(desc.getLimitBoucle().intValue());
            String xPathQuery = ConnectionContextHelper.getOriginalValue(contextType, desc.getAbsoluteXPathQuery());
            xPathQuery = TalendQuoteUtils.removeQuotes(xPathQuery);
            cloneDesc.setAbsoluteXPathQuery(xPathQuery);

            cloneDesc.getSchemaTargets().clear();
            List<SchemaTarget> schemaTargets = desc.getSchemaTargets();
            for (SchemaTarget schemaTarget : schemaTargets) {
                SchemaTarget cloneSchemaTarget = ConnectionFactory.eINSTANCE.createSchemaTarget();
                cloneSchemaTarget.setRelativeXPathQuery(schemaTarget.getRelativeXPathQuery());
                cloneSchemaTarget.setTagName(schemaTarget.getTagName());

                cloneSchemaTarget.setSchema(cloneDesc);
                cloneDesc.getSchemaTargets().add(cloneSchemaTarget);
            }

            cloneDesc.setConnection(cloneConn);
            cloneConn.getSchema().add(cloneDesc);
        }

        return cloneConn;
    }

    /*
     * Salesforce schema
     */
    static List<IContextParameter> getSalesforceVariables(String prefixName, SalesforceSchemaConnection ssConn) {
        if (ssConn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        if (ssConn.getLoginType().equalsIgnoreCase(BASIC)) {
            paramName = prefixName + EParamName.WebServiceUrl;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getWebServiceUrl());

            paramName = prefixName + EParamName.UserName;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getUserName());

            paramName = prefixName + EParamName.Password;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getValue(ssConn.getPassword(), false),
                    JavaTypesManager.PASSWORD);

            paramName = prefixName + EParamName.BatchSize;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getBatchSize());

            paramName = prefixName + EParamName.TimeOut;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getTimeOut(), JavaTypesManager.INTEGER);

            // paramName = prefixName + EParamName.QueryCondition;
            // ConnectionContextHelper.createParameters(varList, paramName, ssConn.getQueryCondition());

            paramName = prefixName + EParamName.SFProxyHost;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getProxyHost());

            paramName = prefixName + EParamName.SFProxyPort;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getProxyPort());

            paramName = prefixName + EParamName.SFProxyUsername;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getProxyUsername());

            paramName = prefixName + EParamName.SFProxyPassword;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getValue(ssConn.getProxyPassword(), false),
                    JavaTypesManager.PASSWORD);
        } else {
            paramName = prefixName + EParamName.WebServiceUrlForOauth;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getWebServiceUrlTextForOAuth());

            paramName = prefixName + EParamName.ConsumerKey;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getConsumeKey());

            paramName = prefixName + EParamName.ConsumerSecret;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getValue(ssConn.getConsumeSecret(), false),
                    JavaTypesManager.PASSWORD);

            paramName = prefixName + EParamName.CallbackHost;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getCallbackHost());

            paramName = prefixName + EParamName.CallbackPort;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getCallbackPort(), JavaTypesManager.INTEGER);

            paramName = prefixName + EParamName.SalesforceVersion;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getSalesforceVersion());

            paramName = prefixName + EParamName.token;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getToken());

            paramName = prefixName + EParamName.BatchSize;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getBatchSize());

            paramName = prefixName + EParamName.TimeOut;
            ConnectionContextHelper.createParameters(varList, paramName, ssConn.getTimeOut(), JavaTypesManager.INTEGER);

            // paramName = prefixName + EParamName.QueryCondition;
            // ConnectionContextHelper.createParameters(varList, paramName, ssConn.getQueryCondition());
        }
        return varList;
    }

    static List<IContextParameter> getMDMConnectionVariables(String prefixName, MDMConnection conn) {
        if (conn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.MDM_Username;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());

        paramName = prefixName + EParamName.MDM_Password;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getValue(conn.getPassword(), false),
                JavaTypesManager.PASSWORD);

        paramName = prefixName + EParamName.MDM_URL;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getServerUrl());

        return varList;
    }

    static List<IContextParameter> getFTPSChemaVariables(String prefixName, FTPConnection conn) {
        if (conn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.FTPHOST;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getHost());

        paramName = prefixName + EParamName.FTPPORT;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getPort());

        paramName = prefixName + EParamName.FTPUAERNAME;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());

        paramName = prefixName + EParamName.FTPPASSWORD;
        ConnectionContextHelper.createParameters(varList, paramName, conn.getValue(conn.getPassword(), false));

        return varList;
    }

    static List<IContextParameter> getSAPContextVariables(final String prefixName, SAPConnection conn,
            Set<IConnParamName> paramSet) {

        if (conn == null || prefixName == null || paramSet == null || paramSet.isEmpty()) {
            return Collections.emptyList();
        }

        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        String paramPrefix = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                EParamName sapParam = (EParamName) param;
                paramName = paramPrefix + sapParam;
                switch (sapParam) {
                case Client:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getClient());
                    break;
                case Host:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getHost());
                    break;
                case UserName:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getUsername());
                    break;
                case Password:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getValue(conn.getPassword(), false),
                            JavaTypesManager.PASSWORD);
                    break;
                case SystemNumber:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getSystemNumber());
                    break;
                case Language:
                    ConnectionContextHelper.createParameters(varList, paramName, conn.getLanguage());
                    break;
                // Hana database properties
                case DbHost:
                    ConnectionContextHelper.createParameters(varList, paramName,
                            TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_HOST, conn));
                    break;
                case DbPort:
                    ConnectionContextHelper.createParameters(varList, paramName,
                            TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PORT, conn));
                    break;
                case DbSchema:
                    ConnectionContextHelper.createParameters(varList, paramName,
                            TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_SCHEMA, conn));
                    break;
                case DbUsername:
                    ConnectionContextHelper.createParameters(varList, paramName,
                            TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_USERNAME, conn));
                    break;
                case DbPassword:
                    ConnectionContextHelper.createParameters(varList, paramName,
                            conn.getValue(TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PASSWORD, conn), false),
                            JavaTypesManager.PASSWORD);
                    break;
                default:
                }
            }
        }
        // Create sap context parameters for additional properties
        for (AdditionalConnectionProperty sapProperty : conn.getAdditionalProperties()) {
            String sapPropertyContextName = getValidSapContextName(sapProperty.getPropertyName());
            ConnectionContextHelper.createParameters(varList, sapPropertyContextName, sapProperty.getValue());
        }

        return varList;
    }

    static String getValidSapContextName(String originalName) {
        if (originalName.contains(ConnectionContextHelper.DOT)) {
            return originalName.replace(ConnectionContextHelper.DOT, ConnectionContextHelper.LINE);
        }
        return originalName;
    }

    static void setSAPConnectionPropertiesForContextMode(String prefixName, SAPConnection sapCon, Set<IConnParamName> paramSet) {

        if (sapCon == null || prefixName == null) {
            return;
        }

        String originalVariableName = prefixName + ConnectionContextHelper.LINE;
        String sapVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                EParamName sapParam = (EParamName) param;
                originalVariableName = prefixName + ConnectionContextHelper.LINE;
                sapVariableName = originalVariableName + sapParam;
                setSAPConnnectionBasicPropertiesForContextMode(sapCon, sapParam, sapVariableName);
            }
        }
        setSAPConnectionAdditionPropertiesForContextMode(sapCon);
    }

    static void setSAPConnectionPropertiesForExistContextMode(SAPConnection sapConn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        if (sapConn == null) {
            return;
        }

        String sapVariableName = null;
        ContextItem currentContext = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                EParamName sapParam = (EParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        currentContext = entry.getKey();
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(sapParam.name())) {
                                sapVariableName = model.getName();
                                break;
                            }
                        }
                    }
                }
                sapVariableName = getCorrectVariableName(currentContext, sapVariableName, sapParam);
                setSAPConnnectionBasicPropertiesForContextMode(sapConn, sapParam, sapVariableName);
            }
        }
        setSAPConnectionAdditionPropertiesForContextMode(sapConn);
    }

    static void setSAPConnnectionBasicPropertiesForContextMode(SAPConnection sapConn, EParamName sapParam,
            String sapBasicVarName) {
        switch (sapParam) {
        case Client:
            sapConn.setClient(ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case Host:
            sapConn.setHost(ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case UserName:
            sapConn.setUsername(ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case Password:
            sapConn.setPassword(ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case SystemNumber:
            sapConn.setSystemNumber(ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case Language:
            sapConn.setLanguage(ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        // Hana database properties
        case DbHost:
            TaggedValueHelper.setTaggedValue(sapConn, ISAPConstant.PROP_DB_HOST,
                    ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case DbPort:
            TaggedValueHelper.setTaggedValue(sapConn, ISAPConstant.PROP_DB_PORT,
                    ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case DbSchema:
            TaggedValueHelper.setTaggedValue(sapConn, ISAPConstant.PROP_DB_SCHEMA,
                    ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case DbUsername:
            TaggedValueHelper.setTaggedValue(sapConn, ISAPConstant.PROP_DB_USERNAME,
                    ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        case DbPassword:
            TaggedValueHelper.setTaggedValue(sapConn, ISAPConstant.PROP_DB_PASSWORD,
                    ContextParameterUtils.getNewScriptCode(sapBasicVarName, LANGUAGE));
            break;
        default:
        }
    }

    static void setSAPConnectionAdditionPropertiesForContextMode(SAPConnection sapConn) {
        for (AdditionalConnectionProperty sapProperty : sapConn.getAdditionalProperties()) {
            String sapPropertyContextName = getValidSapContextName(sapProperty.getPropertyName());
            sapProperty.setValue(ContextParameterUtils.getNewScriptCode(sapPropertyContextName, LANGUAGE));
        }
    }

    static void revertSAPPropertiesForContextMode(SAPConnection conn, ContextType contextType) {
        String client = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getClient()));
        String host = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getHost()));
        String userName = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getUsername()));
        String passWord = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getValue(conn.getPassword(), false)));
        String systemNumber = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getSystemNumber()));
        String language = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getLanguage()));
        conn.setContextMode(false);
        conn.setClient(client);
        conn.setHost(host);
        conn.setUsername(userName);
        conn.setPassword(conn.getValue(passWord, true));
        conn.setSystemNumber(systemNumber);
        conn.setLanguage(language);

        for (AdditionalConnectionProperty sapProperty : conn.getAdditionalProperties()) {
            String contextPropertyValue = TalendQuoteUtils
                    .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, sapProperty.getValue()));
            sapProperty.setValue(contextPropertyValue);
        }
        // Hana database properties
        String dbHost = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_HOST, conn)));
        String dbPort = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PORT, conn)));
        String dbSchema = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_SCHEMA, conn)));
        String dbUsername = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_USERNAME, conn)));
        String dbPassword = TalendQuoteUtils.removeQuotes(ConnectionContextHelper.getOriginalValue(contextType,
                conn.getValue(TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PASSWORD, conn), false)));
        TaggedValueHelper.setTaggedValue(conn, ISAPConstant.PROP_DB_HOST, dbHost);
        TaggedValueHelper.setTaggedValue(conn, ISAPConstant.PROP_DB_PORT, dbPort);
        TaggedValueHelper.setTaggedValue(conn, ISAPConstant.PROP_DB_SCHEMA, dbSchema);
        TaggedValueHelper.setTaggedValue(conn, ISAPConstant.PROP_DB_USERNAME, dbUsername);
        TaggedValueHelper.setTaggedValue(conn, ISAPConstant.PROP_DB_PASSWORD, conn.getValue(dbPassword, true));
    }

    public static SAPConnection cloneOriginalValueSAPConnection(SAPConnection fileConn, ContextType contextType) {
        if (fileConn == null) {
            return null;
        }

        SAPConnection cloneConn = ConnectionFactory.eINSTANCE.createSAPConnection();

        String client = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getClient());
        String host = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getHost());
        String user = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getUsername());
        String pass = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getValue(fileConn.getPassword(), false));
        String sysNumber = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getSystemNumber());
        String language = ConnectionContextHelper.getOriginalValue(contextType, fileConn.getLanguage());
        cloneConn.setClient(client);
        cloneConn.setHost(host);
        cloneConn.setUsername(user);
        cloneConn.setPassword(cloneConn.getValue(pass, true));
        cloneConn.setSystemNumber(sysNumber);
        cloneConn.setLanguage(language);
        if (fileConn.getAdditionalProperties() != null && !fileConn.getAdditionalProperties().isEmpty()) {
            Collection<AdditionalConnectionProperty> addtionalProperties = EcoreUtil.copyAll(fileConn.getAdditionalProperties());
            cloneConn.getAdditionalProperties().addAll(addtionalProperties);
            for (AdditionalConnectionProperty prop : cloneConn.getAdditionalProperties()) {
                String propValue = ConnectionContextHelper.getOriginalValue(contextType, prop.getValue());
                prop.setValue(propValue);
            }
        }
        // Hana database properties
        String dbHost = ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_HOST, fileConn));
        String dbPort = ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PORT, fileConn));
        String dbSchema = ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_SCHEMA, fileConn));
        String dbUsername = ConnectionContextHelper.getOriginalValue(contextType,
                TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_USERNAME, fileConn));
        String dbPassword = ConnectionContextHelper.getOriginalValue(contextType,
                fileConn.getValue(TaggedValueHelper.getValueString(ISAPConstant.PROP_DB_PASSWORD, fileConn), false));
        TaggedValueHelper.setTaggedValue(cloneConn, ISAPConstant.PROP_DB_HOST, dbHost);
        TaggedValueHelper.setTaggedValue(cloneConn, ISAPConstant.PROP_DB_PORT, dbPort);
        TaggedValueHelper.setTaggedValue(cloneConn, ISAPConstant.PROP_DB_SCHEMA, dbSchema);
        TaggedValueHelper.setTaggedValue(cloneConn, ISAPConstant.PROP_DB_USERNAME, dbUsername);
        TaggedValueHelper.setTaggedValue(cloneConn, ISAPConstant.PROP_DB_PASSWORD, dbPassword);

        ConnectionContextHelper.cloneConnectionProperties(fileConn, cloneConn);

        return cloneConn;
    }

    static void setMDMConnectionPropertiesForContextMode(String prefixName, MDMConnection mdmCon, Set<IConnParamName> paramSet) {
        if (mdmCon == null || prefixName == null) {
            return;
        }

        String originalVariableName = prefixName + ConnectionContextHelper.LINE;
        String mdmVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                EParamName mdmParam = (EParamName) param;
                originalVariableName = prefixName + ConnectionContextHelper.LINE;
                mdmVariableName = originalVariableName + mdmParam;
                setMDMConnnectionBasicPropertiesForContextMode(mdmCon, mdmParam, mdmVariableName);
            }
        }
    }

    static void setMDMConnectionPropertiesForExistContextMode(MDMConnection mdmConn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        if (mdmConn == null) {
            return;
        }

        String mdmVariableName = null;
        ContextItem currentContext = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                EParamName mdmParam = (EParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        currentContext = entry.getKey();
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(mdmParam.name())) {
                                mdmVariableName = model.getName();
                                break;
                            }
                        }
                    }
                }
                mdmVariableName = getCorrectVariableName(currentContext, mdmVariableName, mdmParam);
                setMDMConnnectionBasicPropertiesForContextMode(mdmConn, mdmParam, mdmVariableName);
            }
        }
    }

    static void setMDMConnnectionBasicPropertiesForContextMode(MDMConnection mdmConn, EParamName mdmParam,
            String mdmBasicVarName) {
        switch (mdmParam) {
        case MDM_URL:
            mdmConn.setServerUrl(ContextParameterUtils.getNewScriptCode(mdmBasicVarName, LANGUAGE));
            break;
        case MDM_Username:
            mdmConn.setUsername(ContextParameterUtils.getNewScriptCode(mdmBasicVarName, LANGUAGE));
            break;
        case MDM_Password:
            mdmConn.setPassword(ContextParameterUtils.getNewScriptCode(mdmBasicVarName, LANGUAGE));
            break;
        default:
        }
    }

    static void revertMDMConnectionPropertiesForContextMode(MDMConnection conn, ContextType contextType) {
        if (conn == null || contextType == null) {
            return;
        }
        String username = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getUsername()));
        String password = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getPassword()));
        String serverUrl = TalendQuoteUtils
                .removeQuotes(ConnectionContextHelper.getOriginalValue(contextType, conn.getServerUrl()));

        conn.setUsername(username);
        conn.setPassword(password);
        conn.setServerUrl(serverUrl);
    }

    static void setSalesforcePropertiesForContextMode(String prefixName, SalesforceSchemaConnection ssConn,
            ContextItem contextItem, Set<IConnParamName> paramSet, Map<String, String> map) {
        if (ssConn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;

        String originalVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                EParamName saleforceParam = (EParamName) param;
                originalVariableName = prefixName + saleforceParam;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        if (originalVariableName.equals(entry.getValue())) {
                            originalVariableName = entry.getKey();
                            break;
                        }
                    }
                }
                originalVariableName = getCorrectVariableName(contextItem, originalVariableName, saleforceParam);
                switch (saleforceParam) {
                case WebServiceUrl:
                    ssConn.setWebServiceUrl(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case UserName:
                    ssConn.setUserName(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case Password:
                    ssConn.setPassword(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case WebServiceUrlForOauth:
                    ssConn.setWebServiceUrlTextForOAuth(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case ConsumerKey:
                    ssConn.setConsumeKey(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case ConsumerSecret:
                    ssConn.setConsumeSecret(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case CallbackHost:
                    ssConn.setCallbackHost(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case CallbackPort:
                    ssConn.setCallbackPort(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case SalesforceVersion:
                    ssConn.setSalesforceVersion(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case token:
                    ssConn.setToken(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case BatchSize:
                    ssConn.setBatchSize(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case TimeOut:
                    ssConn.setTimeOut(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case QueryCondition:
                    ssConn.setQueryCondition(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case SFProxyHost:
                    ssConn.setProxyHost(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case SFProxyPort:
                    ssConn.setProxyPort(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case SFProxyUsername:
                    ssConn.setProxyUsername(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                case SFProxyPassword:
                    ssConn.setProxyPassword(ContextParameterUtils.getNewScriptCode(originalVariableName, LANGUAGE));
                    break;
                default:
                }
            }
        }
    }

    static void setSalesforcePropertiesForExistContextMode(SalesforceSchemaConnection ssConn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        if (ssConn == null) {
            return;
        }

        String sslVariableName = null;
        ContextItem currentContext = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                if (param instanceof EParamName) {
                    EParamName ssParam = (EParamName) param;
                    if (map != null && map.size() > 0) {
                        for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                            currentContext = entry.getKey();
                            List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                            for (ConectionAdaptContextVariableModel model : modelList) {
                                if (model.getValue().equals(ssParam.name())) {
                                    sslVariableName = model.getName();
                                    break;
                                }
                            }
                        }
                    }
                    sslVariableName = getCorrectVariableName(currentContext, sslVariableName, ssParam);
                    switch (ssParam) {
                    case WebServiceUrl:
                        ssConn.setWebServiceUrl(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case UserName:
                        ssConn.setUserName(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case Password:
                        ssConn.setPassword(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case WebServiceUrlForOauth:
                        ssConn.setWebServiceUrlTextForOAuth(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case ConsumerKey:
                        ssConn.setConsumeKey(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case ConsumerSecret:
                        ssConn.setConsumeSecret(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case CallbackHost:
                        ssConn.setCallbackHost(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case CallbackPort:
                        ssConn.setCallbackPort(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case SalesforceVersion:
                        ssConn.setSalesforceVersion(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case token:
                        ssConn.setToken(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case BatchSize:
                        ssConn.setBatchSize(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case TimeOut:
                        ssConn.setTimeOut(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case QueryCondition:
                        ssConn.setQueryCondition(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case SFProxyHost:
                        ssConn.setProxyHost(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case SFProxyPort:
                        ssConn.setProxyPort(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case SFProxyUsername:
                        ssConn.setProxyUsername(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    case SFProxyPassword:
                        ssConn.setProxyPassword(ContextParameterUtils.getNewScriptCode(sslVariableName, LANGUAGE));
                        break;
                    default:
                    }
                }

            }
        }
    }

    static void revertSalesforcePropertiesForContextMode(SalesforceSchemaConnection ssConn, ContextType contextType) {
        if (ssConn == null || contextType == null) {
            return;
        }
        String url = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getWebServiceUrl());
        String userName = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getUserName());
        String password = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getValue(ssConn.getPassword(), false));
        String batchSize = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getBatchSize());
        String queryCondition = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getQueryCondition());
        String timeOut = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getTimeOut());
        String webServiceUrlForOauth = ConnectionContextHelper.getOriginalValue(contextType,
                ssConn.getWebServiceUrlTextForOAuth());
        String consumerKey = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getConsumeKey());
        String consumerSecret = ConnectionContextHelper.getOriginalValue(contextType,
                ssConn.getValue(ssConn.getConsumeSecret(), false));
        String callbackHost = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getCallbackHost());
        String callbackPort = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getCallbackPort());
        String salesforceVersion = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getSalesforceVersion());
        String token = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getToken());
        String proxyHost = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getProxyHost());
        String proxyPort = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getProxyPort());
        String proxyUsername = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getProxyUsername());
        String proxyPassword = ConnectionContextHelper.getOriginalValue(contextType,
                ssConn.getValue(ssConn.getProxyPassword(), false));

        ssConn.setWebServiceUrl(url);
        ssConn.setUserName(userName);
        ssConn.setPassword(ssConn.getValue(password, true));
        ssConn.setBatchSize(batchSize);
        ssConn.setQueryCondition(queryCondition);
        ssConn.setTimeOut(timeOut);
        ssConn.setWebServiceUrlTextForOAuth(webServiceUrlForOauth);
        ssConn.setConsumeKey(consumerKey);
        ssConn.setConsumeSecret(ssConn.getValue(consumerSecret, true));
        ssConn.setCallbackHost(callbackHost);
        ssConn.setCallbackPort(callbackPort);
        ssConn.setSalesforceVersion(salesforceVersion);
        ssConn.setToken(token);
        ssConn.setProxyHost(proxyHost);
        ssConn.setProxyPort(proxyPort);
        ssConn.setProxyUsername(proxyUsername);
        ssConn.setProxyPassword(ssConn.getValue(proxyPassword, true));

    }

    @SuppressWarnings("unchecked")
    public static SalesforceSchemaConnection cloneOriginalValueSalesforceConnection(SalesforceSchemaConnection ssConn,
            ContextType contextType) {
        if (ssConn == null) {
            return null;
        }

        SalesforceSchemaConnection cloneConn = ConnectionFactory.eINSTANCE.createSalesforceSchemaConnection();

        String url = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getWebServiceUrl());
        String userName = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getUserName());
        String password = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getValue(ssConn.getPassword(), false));
        String batchSize = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getBatchSize());
        String timeOut = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getTimeOut());
        String queryCondition = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getQueryCondition());
        String webServiceUrlForOauth = ConnectionContextHelper.getOriginalValue(contextType,
                ssConn.getWebServiceUrlTextForOAuth());
        String consumerKey = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getConsumeKey());
        String consumerSecret = ConnectionContextHelper.getOriginalValue(contextType,
                ssConn.getValue(ssConn.getConsumeSecret(), false));
        String callbackHost = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getCallbackHost());
        String callbackPort = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getCallbackPort());
        String salesforceVersion = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getSalesforceVersion());
        String token = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getToken());
        String proxyHost = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getProxyHost());
        String proxyPort = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getProxyPort());
        String proxyUsername = ConnectionContextHelper.getOriginalValue(contextType, ssConn.getProxyUsername());
        String proxyPassword = ConnectionContextHelper.getOriginalValue(contextType,
                ssConn.getValue(ssConn.getProxyPassword(), false));

        cloneConn.setWebServiceUrl(url);
        cloneConn.setUserName(userName);
        cloneConn.setPassword(cloneConn.getValue(password, true));
        cloneConn.setBatchSize(batchSize);
        cloneConn.setTimeOut(timeOut);
        cloneConn.setQueryCondition(queryCondition);
        cloneConn.setWebServiceUrlTextForOAuth(webServiceUrlForOauth);
        cloneConn.setConsumeKey(consumerKey);
        cloneConn.setConsumeSecret(cloneConn.getValue(consumerSecret, true));
        cloneConn.setCallbackHost(callbackHost);
        cloneConn.setCallbackPort(callbackPort);
        cloneConn.setSalesforceVersion(salesforceVersion);
        cloneConn.setToken(token);
        cloneConn.setProxyHost(proxyHost);
        cloneConn.setProxyPort(proxyPort);
        cloneConn.setProxyUsername(proxyUsername);
        cloneConn.setProxyPassword(cloneConn.getValue(proxyPassword, true));

        ConnectionContextHelper.cloneConnectionProperties(ssConn, cloneConn);

        cloneConn.setModuleName(ssConn.getModuleName());
        cloneConn.setLoginType(ssConn.getLoginType());

        return cloneConn;
    }

    /*
     * LDAP schema
     */
    static List<IContextParameter> getLDAPSchemaVariables(String prefixName, LDAPSchemaConnection ldapConn) {
        if (ldapConn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.Host;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getHost());

        paramName = prefixName + EParamName.Port;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getPort(), JavaTypesManager.INTEGER);

        paramName = prefixName + EParamName.BindPassword;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getValue(ldapConn.getBindPassword(), false),
                JavaTypesManager.PASSWORD);

        paramName = prefixName + EParamName.BindPrincipal;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getBindPrincipal());

        paramName = prefixName + EParamName.Filter;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getFilter());

        paramName = prefixName + EParamName.TimeOutLimit;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getTimeOutLimit(), JavaTypesManager.INTEGER);

        paramName = prefixName + EParamName.CountLimit;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getCountLimit(), JavaTypesManager.INTEGER);

        paramName = prefixName + EParamName.BaseDN;
        ConnectionContextHelper.createParameters(varList, paramName, ldapConn.getSelectedDN());
        return varList;
    }

    static void setLDAPSchemaPropertiesForContextMode(String prefixName, LDAPSchemaConnection ldapConn) {
        if (ldapConn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.Host;
        ldapConn.setHost(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.Port;
        ldapConn.setPort(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.BindPrincipal;
        ldapConn.setBindPrincipal(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.BindPassword;
        ldapConn.setBindPassword(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.CountLimit;
        ldapConn.setCountLimit(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.TimeOutLimit;
        ldapConn.setTimeOutLimit(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.Filter;
        ldapConn.setFilter(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

        paramName = prefixName + EParamName.BaseDN;
        ldapConn.setSelectedDN(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
    }

    static void setLDAPSchemaPropertiesForExistContextMode(LDAPSchemaConnection ldapConn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        if (ldapConn == null) {
            return;
        }

        String ldapVariableName = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                ldapVariableName = "";
                EParamName ldapParam = (EParamName) param;
                if (map != null && map.size() > 0) {
                    for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                        List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                        for (ConectionAdaptContextVariableModel model : modelList) {
                            if (model.getValue().equals(ldapParam.name())) {
                                ldapVariableName = model.getName();
                                break;
                            }
                        }
                    }
                }
                switch (ldapParam) {
                case Host:
                    ldapConn.setHost(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case Port:
                    ldapConn.setPort(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case BindPrincipal:
                    ldapConn.setBindPrincipal(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case BindPassword:
                    ldapConn.setBindPassword(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case CountLimit:
                    ldapConn.setCountLimit(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case TimeOutLimit:
                    ldapConn.setTimeOutLimit(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case Filter:
                    ldapConn.setFilter(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                case BaseDN:
                    ldapConn.setSelectedDN(ContextParameterUtils.getNewScriptCode(ldapVariableName, LANGUAGE));
                    break;
                default:
                }
            }
        }
    }

    static void revertLDAPSchemaPropertiesForContextMode(LDAPSchemaConnection ldapConn, ContextType contextType) {
        if (ldapConn == null || contextType == null) {
            return;
        }
        String host = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getHost());
        String port = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getPort());
        String bindPrincipal = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getBindPrincipal());
        String bindPassword = ConnectionContextHelper.getOriginalValue(contextType,
                ldapConn.getValue(ldapConn.getBindPassword(), false));
        String filter = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getFilter());
        String countLimit = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getCountLimit());
        String timeOutLimit = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getTimeOutLimit());
        String baseDN = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getSelectedDN());

        ldapConn.setHost(host);
        ldapConn.setPort(port);
        ldapConn.setBindPrincipal(bindPrincipal);
        ldapConn.setBindPassword(ldapConn.getValue(bindPassword, true));
        ldapConn.setFilter(filter);
        ldapConn.setCountLimit(countLimit);
        ldapConn.setTimeOutLimit(timeOutLimit);
        ldapConn.setSelectedDN(baseDN);
    }

    @SuppressWarnings("unchecked")
    public static LDAPSchemaConnection cloneOriginalValueLDAPSchemaConnection(LDAPSchemaConnection ldapConn,
            ContextType contextType) {
        if (ldapConn == null) {
            return null;
        }

        LDAPSchemaConnection cloneConn = ConnectionFactory.eINSTANCE.createLDAPSchemaConnection();

        String host = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getHost());
        String port = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getPort());
        String bindPrincipal = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getBindPrincipal());
        String bindPassword = ConnectionContextHelper.getOriginalValue(contextType,
                cloneConn.getValue(ldapConn.getBindPassword(), false));
        String filter = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getFilter());
        String countLimit = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getCountLimit());
        String timeOutLimit = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getTimeOutLimit());
        String baseDN = ConnectionContextHelper.getOriginalValue(contextType, ldapConn.getSelectedDN());

        cloneConn.setHost(host);
        cloneConn.setPort(port);
        cloneConn.setBindPrincipal(bindPrincipal);
        cloneConn.setBindPassword(cloneConn.getValue(bindPassword, true));
        cloneConn.setFilter(filter);
        cloneConn.setCountLimit(countLimit);
        cloneConn.setTimeOutLimit(timeOutLimit);

        cloneConn.setSeparator(ldapConn.getSeparator());
        cloneConn.setProtocol(ldapConn.getProtocol());
        cloneConn.setSelectedDN(baseDN);
        cloneConn.setStorePath(ldapConn.getStorePath());

        cloneConn.setAliases(ldapConn.getAliases());
        cloneConn.setReferrals(ldapConn.getReferrals());
        cloneConn.setEncryptionMethodName(ldapConn.getEncryptionMethodName());
        cloneConn.setGetBaseDNsFromRoot(ldapConn.isGetBaseDNsFromRoot());
        cloneConn.setLimitValue(ldapConn.getLimitValue());
        cloneConn.setSavePassword(ldapConn.isSavePassword());
        cloneConn.setUseAdvanced(ldapConn.isUseAdvanced());
        cloneConn.setUseAuthen(ldapConn.isUseAuthen());
        cloneConn.setUseLimit(ldapConn.isUseLimit());

        ConnectionContextHelper.cloneConnectionProperties(ldapConn, cloneConn);

        EList baseDNs = ldapConn.getBaseDNs();
        if (baseDNs != null && baseDNs instanceof BasicEList) {
            cloneConn.getBaseDNs().addAll((EList) ((BasicEList) baseDNs).clone());
        }

        EList values = ldapConn.getValue();
        if (values != null && values instanceof BasicEList) {
            cloneConn.getValue().addAll((EList) ((BasicEList) values).clone());
        }
        EList returnAttributes = ldapConn.getReturnAttributes();
        if (returnAttributes != null && returnAttributes instanceof BasicEList) {
            cloneConn.getReturnAttributes().addAll((EList) ((BasicEList) returnAttributes).clone());
        }
        return cloneConn;
    }

    public static FTPConnection cloneOriginalValueFTPConnection(FTPConnection ftpConn, ContextType contextType) {
        if (ftpConn == null) {
            return null;
        }
        FTPConnection cloneConn = ConnectionFactory.eINSTANCE.createFTPConnection();
        String host = ConnectionContextHelper.getOriginalValue(contextType, ftpConn.getHost());
        String port = ConnectionContextHelper.getOriginalValue(contextType, ftpConn.getPort());
        String userName = ConnectionContextHelper.getOriginalValue(contextType, ftpConn.getUsername());
        String password = ConnectionContextHelper.getOriginalValue(contextType, ftpConn.getValue(ftpConn.getPassword(), false));

        cloneConn.setHost(host);
        cloneConn.setPort(port);
        cloneConn.setUsername(userName);
        cloneConn.setPassword(cloneConn.getValue(password, true));

        return cloneConn;
    }

    /*
     * WSDL Schema
     */
    static List<IContextParameter> getWSDLSchemaVariables(String prefixName, WSDLSchemaConnection wsdlConn) {
        if (wsdlConn == null || prefixName == null) {
            return Collections.emptyList();
        }
        List<IContextParameter> varList = new ArrayList<IContextParameter>();
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.WSDL;
        ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getWSDL());
        if (wsdlConn.isIsInputModel()) {
            paramName = prefixName + EParamName.MethodName;
            ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getMethodName());
        }
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            if (wsdlConn.isIsInputModel()) {
                paramName = prefixName + EParamName.UserName;
                ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getUserName());

                paramName = prefixName + EParamName.Password;
                ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getValue(wsdlConn.getPassword(), false),
                        JavaTypesManager.PASSWORD);

                paramName = prefixName + EParamName.ProxyHost;
                ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getProxyHost());

                paramName = prefixName + EParamName.ProxyPort;
                ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getProxyPort(), JavaTypesManager.INTEGER);

                paramName = prefixName + EParamName.ProxyUser;
                ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getProxyUser());

                paramName = prefixName + EParamName.ProxyPassword;
                ConnectionContextHelper.createParameters(varList, paramName,
                        wsdlConn.getValue(wsdlConn.getProxyPassword(), false), JavaTypesManager.PASSWORD);
            }
            break;
        case PERL:
        default:
            paramName = prefixName + EParamName.EndpointURI;
            ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getEndpointURI());

            paramName = prefixName + EParamName.Encoding;
            ConnectionContextHelper.createParameters(varList, paramName, wsdlConn.getEncoding());
            break;
        }

        return varList;
    }

    static void setWSDLSchemaPropertiesForContextMode(String prefixName, WSDLSchemaConnection wsdlConn) {
        if (wsdlConn == null || prefixName == null) {
            return;
        }
        prefixName = prefixName + ConnectionContextHelper.LINE;
        String paramName = null;

        paramName = prefixName + EParamName.WSDL;
        wsdlConn.setWSDL(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
        if (wsdlConn.isIsInputModel()) {
            paramName = prefixName + EParamName.MethodName;
            wsdlConn.setMethodName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
        }
        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            if (wsdlConn.isIsInputModel()) {
                paramName = prefixName + EParamName.UserName;
                wsdlConn.setUserName(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

                paramName = prefixName + EParamName.Password;
                wsdlConn.setPassword(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

                paramName = prefixName + EParamName.ProxyHost;
                wsdlConn.setProxyHost(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

                paramName = prefixName + EParamName.ProxyPort;
                wsdlConn.setProxyPort(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

                paramName = prefixName + EParamName.ProxyUser;
                wsdlConn.setProxyUser(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

                paramName = prefixName + EParamName.ProxyPassword;
                wsdlConn.setProxyPassword(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
            }
            break;
        case PERL:
        default:
            paramName = prefixName + EParamName.EndpointURI;
            wsdlConn.setEndpointURI(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));

            paramName = prefixName + EParamName.Encoding;
            wsdlConn.setEncoding(ContextParameterUtils.getNewScriptCode(paramName, LANGUAGE));
            break;
        }

    }

    static void setWSDLSchemaPropertiesForExistContextMode(WSDLSchemaConnection wsdlConn, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> map) {
        if (wsdlConn == null) {
            return;
        }
        String paramName = null;
        String wsdlVariableName = null;
        ContextItem currentContext = null;
        for (IConnParamName param : paramSet) {
            if (param instanceof EParamName) {
                if (param instanceof EParamName) {
                    EParamName wsdlParam = (EParamName) param;
                    if (map != null && map.size() > 0) {
                        for (Map.Entry<ContextItem, List<ConectionAdaptContextVariableModel>> entry : map.entrySet()) {
                            currentContext = entry.getKey();
                            List<ConectionAdaptContextVariableModel> modelList = entry.getValue();
                            for (ConectionAdaptContextVariableModel model : modelList) {
                                if (model.getValue().equals(wsdlParam.name())) {
                                    wsdlVariableName = model.getName();
                                    break;
                                }
                            }
                        }
                    }
                    if (wsdlVariableName != null && wsdlParam.equals(EParamName.WSDL)) {
                        wsdlConn.setWSDL(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                    }
                    if (wsdlVariableName != null && wsdlParam.equals(EParamName.EndpointURI)) {
                        wsdlConn.setEndpointURI(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                    }
                    if (wsdlVariableName != null && wsdlParam.equals(EParamName.Encoding)) {
                        wsdlConn.setEncoding(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                    }
                    if (wsdlConn.isIsInputModel()) {
                        switch (wsdlParam) {
                        case MethodName:
                            wsdlConn.setMethodName(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        case UserName:
                            wsdlConn.setUserName(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        case Password:
                            wsdlConn.setPassword(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        case ProxyHost:
                            wsdlConn.setProxyHost(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        case ProxyPort:
                            wsdlConn.setProxyPort(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        case ProxyUser:
                            wsdlConn.setProxyUser(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        case ProxyPassword:
                            wsdlConn.setProxyPassword(ContextParameterUtils.getNewScriptCode(wsdlVariableName, LANGUAGE));
                        default:
                        }
                    }
                }
            }
        }
    }

    static void revertWSDLSchemaPropertiesForContextMode(WSDLSchemaConnection wsdlConn, ContextType contextType) {
        if (wsdlConn == null || contextType == null) {
            return;
        }
        String wsdl = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getWSDL());
        if (wsdlConn.isIsInputModel()) {
            String username = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getUserName());
            String password = ConnectionContextHelper.getOriginalValue(contextType,
                    wsdlConn.getValue(wsdlConn.getPassword(), false));
            String proxyHost = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getProxyHost());
            String proxyPort = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getProxyPort());
            String proxyUser = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getProxyUser());
            String proxyPassword = ConnectionContextHelper.getOriginalValue(contextType,
                    wsdlConn.getValue(wsdlConn.getProxyPassword(), false));
            String methodName = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getMethodName());
            String encoding = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getEncoding());
            String endpointURL = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getEndpointURI());

            wsdlConn.setUserName(username);
            wsdlConn.setPassword(wsdlConn.getValue(password, true));
            wsdlConn.setProxyHost(proxyHost);
            wsdlConn.setProxyPort(proxyPort);
            wsdlConn.setProxyUser(proxyUser);
            wsdlConn.setProxyPassword(wsdlConn.getValue(proxyPassword, true));
            wsdlConn.setMethodName(methodName);
            wsdlConn.setEncoding(encoding);
            wsdlConn.setEndpointURI(endpointURL);
        }
        wsdlConn.setWSDL(wsdl);
    }

    @SuppressWarnings("unchecked")
    public static WSDLSchemaConnection cloneOriginalValueWSDLSchemaConnection(WSDLSchemaConnection wsdlConn,
            ContextType contextType) {
        if (wsdlConn == null) {
            return null;
        }

        WSDLSchemaConnection cloneConn = ConnectionFactory.eINSTANCE.createWSDLSchemaConnection();

        String wsdl = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getWSDL());
        if (wsdlConn.isIsInputModel()) {
            String username = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getUserName());
            String password = ConnectionContextHelper.getOriginalValue(contextType,
                    wsdlConn.getValue(wsdlConn.getPassword(), false));
            String proxyHost = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getProxyHost());
            String proxyPort = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getProxyPort());
            String proxyUser = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getProxyUser());
            String proxyPassword = ConnectionContextHelper.getOriginalValue(contextType,
                    wsdlConn.getValue(wsdlConn.getProxyPassword(), false));
            String methodName = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getMethodName());
            String encoding = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getEncoding());
            String endpointURL = ConnectionContextHelper.getOriginalValue(contextType, wsdlConn.getEndpointURI());

            cloneConn.setUserName(username);
            cloneConn.setPassword(cloneConn.getValue(password, true));
            cloneConn.setProxyHost(proxyHost);
            cloneConn.setProxyPort(proxyPort);
            cloneConn.setProxyUser(proxyUser);
            cloneConn.setProxyPassword(cloneConn.getValue(proxyPassword, true));
            cloneConn.setMethodName(methodName);
            cloneConn.setEncoding(encoding);
            cloneConn.setEndpointURI(endpointURL);

            cloneConn.setNeedAuth(wsdlConn.isNeedAuth());
            cloneConn.setUseProxy(wsdlConn.isUseProxy());
        }
        cloneConn.setWSDL(wsdl);
        ConnectionContextHelper.cloneConnectionProperties(wsdlConn, cloneConn);

        cloneConn.setParameters((ArrayList) wsdlConn.getParameters().clone());
        EList values = wsdlConn.getValue();
        if (values != null && values instanceof BasicEList) {
            cloneConn.getValue().addAll((EList) ((BasicEList) values).clone());
        }
        return cloneConn;
    }

    public static XmlFileConnection getOriginalValueConnection(XmlFileConnection connection, ConnectionItem connectionItem,
            boolean isContextMode, boolean defaultContext) {
        if (isContextMode) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connectionItem.getConnection(),
                    connectionItem.getConnection().getContextName(), defaultContext);
            return OtherConnectionContextUtils.cloneOriginalValueXmlFileConnection(connection, contextType);
        }
        return connection;

    }

    public static SAPConnection getOriginalValueConnection(SAPConnection connection, String contextString,
            boolean defaultContext) {
        if (connection.isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connection, contextString,
                    defaultContext);
            return OtherConnectionContextUtils.cloneOriginalValueSAPConnection(connection, contextType);
        }
        return connection;

    }

    private static String getCorrectVariableName(ContextItem contextItem, String originalVariableName, EParamName dbParam) {
        Set<String> contextVarNames = ContextUtils.getContextVarNames(contextItem);
        // if not contains it ,will get originalVariableName from the context
        if (contextVarNames != null && !contextVarNames.contains(originalVariableName)) {
            for (String varName : contextVarNames) {
                if (varName.endsWith(dbParam.name())) {
                    return varName;
                }
            }
        }
        return originalVariableName;
    }
}
