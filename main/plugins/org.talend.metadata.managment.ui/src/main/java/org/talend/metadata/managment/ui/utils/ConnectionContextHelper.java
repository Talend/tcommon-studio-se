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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.hadoop.IHadoopClusterService;
import org.talend.core.hadoop.repository.HadoopRepositoryUtil;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.AdditionalConnectionProperty;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.metadata.builder.connection.FTPConnection;
import org.talend.core.model.metadata.builder.connection.FileConnection;
import org.talend.core.model.metadata.builder.connection.GenericSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LDAPSchemaConnection;
import org.talend.core.model.metadata.builder.connection.LdifFileConnection;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.metadata.builder.connection.SAPConnection;
import org.talend.core.model.metadata.builder.connection.SalesforceSchemaConnection;
import org.talend.core.model.metadata.builder.connection.WSDLSchemaConnection;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.metadata.designerproperties.RepositoryToComponentProperty;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.process.EComponentCategory;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.CloneConnectionUtils;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.service.INOSQLService;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.SelectRepositoryContextGroupDialog;
import org.talend.core.ui.context.cmd.CheckAndAddContextDNDCommand;
import org.talend.core.ui.context.cmd.CheckAndAddContextVariablesCommand;
import org.talend.core.ui.context.cmd.MergeContextVariablesCommand;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.core.ui.process.IGEFProcess;
import org.talend.core.ui.services.IDesignerCoreUIService;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.impl.ContextTypeImpl;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.metadata.managment.ui.model.IConnParamName;
import org.talend.metadata.managment.ui.utils.DBConnectionContextUtils.EDBParamName;
import org.talend.metadata.managment.ui.utils.ExtendedNodeConnectionContextUtils.EHadoopParamName;
import org.talend.metadata.managment.ui.utils.FileConnectionContextUtils.EFileParamName;
import org.talend.metadata.managment.ui.utils.OtherConnectionContextUtils.EParamName;
import org.talend.metadata.managment.ui.wizard.context.ContextModeWizard;
import org.talend.metadata.managment.ui.wizard.context.ContextWizard;
import org.talend.metadata.managment.ui.wizard.metadata.ContextSetsSelectionDialog;
import org.talend.metadata.managment.ui.wizard.metadata.ShowAddedContextdialog;
import org.talend.repository.UpdateRepositoryUtils;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.RepositoryNodeUtilities;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;

/**
 * ggu class global comment. Detailled comment
 */
public final class ConnectionContextHelper {

    public static final String LINE = "_"; //$NON-NLS-1$

    public static final String EMPTY = ""; //$NON-NLS-1$

    public static final String DOT = "."; //$NON-NLS-1$

    public static IContextManager contextManager;

    /**
     * 
     * ggu Comment method "checkContextMode".
     * 
     * initialize and check context mode for the ConnectionItem.
     */
    public static ContextItem checkContextMode(ConnectionItem connItem) {
        if (connItem == null) {
            return null;
        }

        return checkContextMode(connItem.getConnection());
    }

    public static ContextItem checkContextMode(Connection connection) {
        if (connection == null) {
            return null;
        }
        String contextId = connection.getContextId();
        if (contextId == null || EMPTY.equals(contextId.trim()) || RepositoryNode.NO_ID.equals(contextId.trim())) {
            connection.setContextMode(false);
            connection.setContextId(null);
            return null;
        }
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            IRepositoryViewObject context = factory.getLastVersion(contextId);
            if (context != null && factory.getStatus(context) != ERepositoryStatus.DELETED) {
                if (context.getProperty().getItem() instanceof ContextItem) {
                    connection.setContextMode(true);
                    return (ContextItem) context.getProperty().getItem();
                }
            }
        } catch (PersistenceException e) {
            //
        }
        connection.setContextMode(false);
        connection.setContextId(null);
        return null;
    }

    public static void openInConetxtModeDialog() {
        MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                Messages.getString("ConnectionContextHelper.ContextTitle"), //$NON-NLS-1$
                Messages.getString("ConnectionContextHelper.InConextMessages")); //$NON-NLS-1$
    }

    public static void openOutConetxtModeDialog() {
        MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                Messages.getString("ConnectionContextHelper.ContextTitle"), //$NON-NLS-1$
                Messages.getString("ConnectionContextHelper.OutConextMessages")); //$NON-NLS-1$
    }

    /**
     * 
     * ggu Comment method "exportAsContext".
     * 
     */
    public static Map<ContextItem, List<ConectionAdaptContextVariableModel>> exportAsContext(String defaultContextName,
            ConnectionItem connItem, Set<IConnParamName> paramSet) {
        if (connItem == null) {
            return null;
        }
        List<IContextParameter> varList = createContextParameters(connItem, paramSet);
        if (varList == null || varList.isEmpty()) {
            return null;
        }

        ISelection selection = getRepositoryContext(defaultContextName, false);

        if (selection == null) {
            return null;
        }
        Map<ContextItem, List<ConectionAdaptContextVariableModel>> variableContextMap = new HashMap();
        List<ConectionAdaptContextVariableModel> models = new ArrayList<ConectionAdaptContextVariableModel>();

        Set<String> connectionVaribles = getConnVariables(connItem, paramSet);
        ContextModeWizard contextWizard = new ContextModeWizard(defaultContextName, selection.isEmpty(), selection, varList,
                connectionVaribles);
        WizardDialog dlg = new WizardDialog(Display.getCurrent().getActiveShell(), contextWizard);
        if (dlg.open() == Window.OK) {
            ContextItem contextItem = contextWizard.getContextItem();
            models = contextWizard.getAdaptModels();
            if (contextItem != null) {
                variableContextMap.put(contextItem, models);
            }
            contextManager = contextWizard.getContextManager();
            // 1) in the ContextWizard when user click ok, there will set the ContextItem property's label with it's
            // displayName; 2) in the ContextWizard, user may change the context name; so needn't to set the
            // ContextItem property's label with contextName here
            // if (contextItem != null) {
            // contextItem.getProperty().setLabel(contextName);
            // }
            return variableContextMap;
        }
        return null;
    }

    /**
     * 
     * change the mode of lebel to Context mode
     * 
     * @param label which you want to convert(it is the name of connection normal)
     * @return Context mode label
     */
    public static String convertContextLabel(String label) {
        if (label != null) {
            String invalidCharactor = "[\\~\\!\\`\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\+\\=\\{\\[\\]\\}\\:\\;\\'\\|\\<\\,\\>\\.\\?\\/\"￥；：‘”、《，》。？]";//$NON-NLS-1$ 
            String newLabel = label.replaceAll(invalidCharactor, "_"); //$NON-NLS-1$ 
            Pattern pattern = Pattern.compile("^[0-9]+.*$"); //$NON-NLS-1$
            Matcher m = pattern.matcher(newLabel);
            if (m.matches()) {
                newLabel = "_" + newLabel; //$NON-NLS-1$
            }
            //
            try {
                ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                List<IRepositoryViewObject> contextObjectList = factory.getAll(ERepositoryObjectType.CONTEXT, true);

                int i = 1;
                String tmpLabel = newLabel;
                while (!isValidContextName(contextObjectList, tmpLabel)) {
                    tmpLabel = newLabel + i;
                    i++;
                }
                return tmpLabel;
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
            return newLabel;
        }
        return null;
    }

    private static boolean isValidContextName(List<IRepositoryViewObject> contextObjectList, String name) {
        if (contextObjectList != null) {
            for (IRepositoryViewObject object : contextObjectList) {
                Item item = object.getProperty().getItem();
                if (item.getProperty().getLabel().equalsIgnoreCase(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<IContextParameter> createContextParameters(ConnectionItem connectionItem, Set<IConnParamName> paramSet) {
        if (connectionItem == null) {
            return null;
        }
        final String label = convertContextLabel(connectionItem.getProperty().getLabel());
        Connection conn = connectionItem.getConnection();

        List<IContextParameter> varList = null;
        if (conn instanceof DatabaseConnection) {
            varList = DBConnectionContextUtils.getDBVariables(label, (DatabaseConnection) conn, paramSet);
        } else if (conn instanceof FileConnection) {
            varList = FileConnectionContextUtils.getFileVariables(label, (FileConnection) conn, paramSet);
        } else if (conn instanceof LdifFileConnection) {
            varList = OtherConnectionContextUtils.getLdifFileVariables(label, (LdifFileConnection) conn);
        } else if (conn instanceof XmlFileConnection) {
            varList = OtherConnectionContextUtils.getXmlFileVariables(label, (XmlFileConnection) conn);
        } else if (conn instanceof LDAPSchemaConnection) {
            varList = OtherConnectionContextUtils.getLDAPSchemaVariables(label, (LDAPSchemaConnection) conn);
        } else if (conn instanceof WSDLSchemaConnection) {
            varList = OtherConnectionContextUtils.getWSDLSchemaVariables(label, (WSDLSchemaConnection) conn);
        } else if (conn instanceof SalesforceSchemaConnection) {
            varList = OtherConnectionContextUtils.getSalesforceVariables(label, (SalesforceSchemaConnection) conn);
        } else if (conn instanceof GenericSchemaConnection) {
            //
        } else if (conn instanceof MDMConnection) {
            varList = OtherConnectionContextUtils.getMDMConnectionVariables(label, (MDMConnection) conn);
        } else if (conn instanceof FTPConnection) {
            varList = OtherConnectionContextUtils.getFTPSChemaVariables(label, (FTPConnection) conn);
        } else if (conn instanceof SAPConnection) {
            varList = OtherConnectionContextUtils.getSAPContextVariables(label, (SAPConnection) conn, paramSet);
        } else {
            varList = ExtendedNodeConnectionContextUtils.getContextVariables(label, conn, paramSet);
        }

        return varList;
    }

    private static Set<String> getConnVariables(ConnectionItem connectionItem, Set<IConnParamName> paramSet) {
        if (connectionItem == null) {
            return null;
        }

        Set<String> varList = new HashSet<String>();

        collectConnVariablesOfBasicInfo(varList, paramSet);

        collectConnVariablesOfAdditionalTable(varList, connectionItem);

        return varList;
    }

    private static void collectConnVariablesOfBasicInfo(Set<String> varList, final Set<IConnParamName> paramSet) {
        Iterator<IConnParamName> paramIt = paramSet.iterator();
        while (paramIt.hasNext()) {
            Object param = paramIt.next();
            if (param instanceof EDBParamName) {
                varList.add(((EDBParamName) param).name());
            }
            if (param instanceof EFileParamName) {
                varList.add(((EFileParamName) param).name());
            }
            if (param instanceof EParamName) {
                varList.add(((EParamName) param).name());
            }
            if (param instanceof EHadoopParamName) {
                varList.add(((EHadoopParamName) param).name());
            }
            if (param instanceof GenericConnParamName) {
                varList.add(((GenericConnParamName) param).getName());
            }
        }
    }

    private static void collectConnVariablesOfAdditionalTable(Set<String> varList, ConnectionItem connectionItem) {
        Connection currentConnection = connectionItem.getConnection();
        // TODO:Maybe later has other type support context for the additional table.Now only sap
        INOSQLService service = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(INOSQLService.class)) {
            service = (INOSQLService) GlobalServiceRegister.getDefault().getService(INOSQLService.class);
        }
        if (service != null && service.isUseReplicaSet(currentConnection)) {
            String replicaSets = service.getMongoDBReplicaSets(currentConnection);
            try {
                JSONArray jsa = new JSONArray(replicaSets);
                for (int i = 0; i < jsa.length(); i++) {
                    int paramNum = i + 1;
                    String hostParamName = ExtendedNodeConnectionContextUtils.getReplicaParamName(EHadoopParamName.ReplicaHost,
                            paramNum);
                    String portParamName = ExtendedNodeConnectionContextUtils.getReplicaParamName(EHadoopParamName.ReplicaPort,
                            paramNum);
                    varList.add(hostParamName);
                    varList.add(portParamName);
                }
                if (varList.contains(EHadoopParamName.ReplicaSets.name())) {
                    varList.remove(EHadoopParamName.ReplicaSets.name());
                }
            } catch (JSONException e) {
                ExceptionHandler.process(e);
            }
        } else if (currentConnection instanceof SAPConnection) {
            SAPConnection sapConn = (SAPConnection) currentConnection;
            for (AdditionalConnectionProperty sapProperty : sapConn.getAdditionalProperties()) {
                varList.add(sapProperty.getPropertyName());
            }
        } else if (currentConnection instanceof DatabaseConnection) {
            DatabaseConnection dbConn = (DatabaseConnection) currentConnection;
            List<Map<String, Object>> hadoopPropertiesList = DBConnectionContextUtils.getHiveOrHbaseHadoopProperties(dbConn);
            if (!hadoopPropertiesList.isEmpty()) {
                for (Map<String, Object> propertyMap : hadoopPropertiesList) {
                    varList.add((String) propertyMap.get("PROPERTY"));
                }
            }
            List<Map<String, Object>> hiveJdbcPropertiesList = DBConnectionContextUtils.getHiveJdbcProperties(dbConn);
            if (!hiveJdbcPropertiesList.isEmpty()) {
                for (Map<String, Object> propertyMap : hiveJdbcPropertiesList) {
                    varList.add((String) propertyMap.get("PROPERTY"));
                }
            }
        } else {
            Set<String> additionPropertyList = ExtendedNodeConnectionContextUtils
                    .getAdditionalPropertiesVariablesForExistContext(currentConnection);
            Iterator<String> it = additionPropertyList.iterator();
            while (it.hasNext()) {
                String additionalConProperty = it.next();
                varList.add(additionalConProperty);
            }
        }
    }

    public static void setPropertiesForContextMode(String defaultContextName, ConnectionItem connectionItem,
            ContextItem contextItem, Set<IConnParamName> paramSet, Map<String, String> map) {
        if (connectionItem == null || contextItem == null) {
            return;
        }

        Connection conn = connectionItem.getConnection();

        if (conn instanceof DatabaseConnection) {
            DBConnectionContextUtils.setPropertiesForContextMode(defaultContextName, (DatabaseConnection) conn, contextItem,
                    paramSet, map);
            // DBConnectionContextUtils.updateConnectionParam((DatabaseConnection) conn, map);
        } else if (conn instanceof FileConnection) {
            FileConnectionContextUtils.setPropertiesForContextMode(defaultContextName, (FileConnection) conn, paramSet);
        } else if (conn instanceof LdifFileConnection) {
            OtherConnectionContextUtils.setLdifFilePropertiesForContextMode(defaultContextName, (LdifFileConnection) conn);
        } else if (conn instanceof XmlFileConnection) {
            OtherConnectionContextUtils.setXmlFilePropertiesForContextMode(defaultContextName, (XmlFileConnection) conn);
        } else if (conn instanceof LDAPSchemaConnection) {
            OtherConnectionContextUtils.setLDAPSchemaPropertiesForContextMode(defaultContextName, (LDAPSchemaConnection) conn);
        } else if (conn instanceof WSDLSchemaConnection) {
            OtherConnectionContextUtils.setWSDLSchemaPropertiesForContextMode(defaultContextName, (WSDLSchemaConnection) conn);
        } else if (conn instanceof SalesforceSchemaConnection) {
            OtherConnectionContextUtils.setSalesforcePropertiesForContextMode(defaultContextName,
                    (SalesforceSchemaConnection) conn, contextItem, paramSet, map);
        } else if (conn instanceof GenericSchemaConnection) {
            //
        } else if (conn instanceof SAPConnection) {
            OtherConnectionContextUtils.setSAPConnectionPropertiesForContextMode(defaultContextName, (SAPConnection) conn,
                    paramSet);
        } else if (conn instanceof MDMConnection) {
            OtherConnectionContextUtils.setMDMConnectionPropertiesForContextMode(defaultContextName, (MDMConnection) conn,
                    paramSet);
        } else {
            ExtendedNodeConnectionContextUtils.setConnectionPropertiesForContextMode(defaultContextName, conn, paramSet);
        }
        // set connection for context mode
        connectionItem.getConnection().setContextMode(true);
        connectionItem.getConnection().setContextId(contextItem.getProperty().getId());
        connectionItem.getConnection().setContextName(contextItem.getDefaultContext());

    }

    public static void setPropertiesForExistContextMode(ConnectionItem connectionItem, Set<IConnParamName> paramSet,
            Map<ContextItem, List<ConectionAdaptContextVariableModel>> modelMap) {
        if (connectionItem == null) {
            return;
        }
        ContextItem selItem = null;
        if (modelMap.keySet().size() == 1) {
            selItem = modelMap.keySet().iterator().next();
        }
        Connection conn = connectionItem.getConnection();

        if (conn instanceof DatabaseConnection) {
            DBConnectionContextUtils.setPropertiesForExistContextMode((DatabaseConnection) conn, paramSet, modelMap);
        } else if (conn instanceof FileConnection) {
            FileConnectionContextUtils.setPropertiesForExistContextMode((FileConnection) conn, paramSet, modelMap);
        } else if (conn instanceof LdifFileConnection) {
            OtherConnectionContextUtils.setLdifFileForExistContextMode((LdifFileConnection) conn, paramSet, modelMap);
        } else if (conn instanceof XmlFileConnection) {
            OtherConnectionContextUtils.setXmlFileForExistContextMode((XmlFileConnection) conn, paramSet, modelMap);
        } else if (conn instanceof LDAPSchemaConnection) {
            OtherConnectionContextUtils.setLDAPSchemaPropertiesForExistContextMode((LDAPSchemaConnection) conn, paramSet,
                    modelMap);
        } else if (conn instanceof SalesforceSchemaConnection) {
            OtherConnectionContextUtils.setSalesforcePropertiesForExistContextMode((SalesforceSchemaConnection) conn, paramSet,
                    modelMap);
        } else if (conn instanceof WSDLSchemaConnection) {
            OtherConnectionContextUtils.setWSDLSchemaPropertiesForExistContextMode((WSDLSchemaConnection) conn, paramSet,
                    modelMap);
        } else if (conn instanceof SAPConnection) {
            OtherConnectionContextUtils.setSAPConnectionPropertiesForExistContextMode((SAPConnection) conn, paramSet, modelMap);
        } else if (conn instanceof MDMConnection) {
            OtherConnectionContextUtils.setMDMConnectionPropertiesForExistContextMode((MDMConnection) conn, paramSet, modelMap);
        } else {
            ExtendedNodeConnectionContextUtils.setConnectionPropertiesForExistContextMode(conn, paramSet, modelMap);
        }

        // set connection for context mode
        connectionItem.getConnection().setContextMode(true);
        connectionItem.getConnection().setContextId(selItem.getProperty().getId());
        connectionItem.getConnection().setContextName(selItem.getDefaultContext());
    }

    public static void createParameters(List<IContextParameter> varList, String paramName, String value) {
        createParameters(varList, paramName, value, null);
    }

    public static void createParameters(List<IContextParameter> varList, String paramName, String value, JavaType type) {
        if (varList == null || paramName == null) {
            return;
        }

        if (value == null) {
            value = EMPTY;
        }

        JobContextParameter contextParam = new JobContextParameter();
        contextParam.setName(paramName);

        switch (LanguageManager.getCurrentLanguage()) {
        case JAVA:
            if (type == null) {
                contextParam.setType(MetadataTalendType.getDefaultTalendType());
            } else {
                contextParam.setType(type.getId());
            }
            break;
        case PERL:
        default:
            if (type != null) {
                if (type == JavaTypesManager.DIRECTORY) {
                    contextParam.setType(ContextParameterJavaTypeManager.PERL_DIRECTORY);
                    break;
                } else if (type == JavaTypesManager.FILE) {
                    contextParam.setType(ContextParameterJavaTypeManager.PERL_FILE);
                    break;
                } else if (type == JavaTypesManager.INTEGER) {
                    contextParam.setType(MetadataTalendType.getPerlTypes()[3]); // int
                    break;
                } else if (type == JavaTypesManager.PASSWORD) {
                    contextParam.setType(ContextParameterJavaTypeManager.PERL_PASSWORD);
                    break;
                }
            }
            contextParam.setType(MetadataTalendType.getPerlTypes()[5]); // string
            break;
        }

        contextParam.setPrompt(paramName + "?"); //$NON-NLS-1$
        if (value != null) {
            contextParam.setValue(value);
        }
        contextParam.setComment(EMPTY);
        varList.add(contextParam);
    }

    public static ISelection getRepositoryContext(final String contextNameOrId, boolean isId) {
        if (contextNameOrId == null || "".equals(contextNameOrId.trim())) { //$NON-NLS-1$
            return null;
        }
        if (isId && RepositoryNode.NO_ID.equals(contextNameOrId.trim())) {
            return null;
        }
        IRepositoryViewObject contextObject = null;
        try {
            ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
            List<IRepositoryViewObject> contextObjectList = factory.getAll(ERepositoryObjectType.CONTEXT, true);
            if (contextObjectList != null) {
                for (IRepositoryViewObject object : contextObjectList) {
                    Item item = object.getProperty().getItem();
                    if (item != null && item instanceof ContextItem) {
                        String itemNameOrId = null;
                        if (isId) {
                            itemNameOrId = item.getProperty().getId();
                        } else {
                            itemNameOrId = item.getProperty().getLabel();
                        }
                        if (contextNameOrId.equals(itemNameOrId)) {
                            contextObject = object;
                            break;
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            //
        }

        StructuredSelection selection = new StructuredSelection();
        if (contextObject != null) {
            RepositoryNode repositoryNode = RepositoryNodeUtilities.getRepositoryNode(contextObject);
            if (repositoryNode != null) {
                selection = new StructuredSelection(repositoryNode);
            }
        }
        return selection;
    }

    /**
     * 
     * ggu Comment method "upateContext".
     * 
     * open the context wizard to update context parameters.
     */
    public static boolean upateContext(ConnectionItem connItem) {
        if (connItem == null) {
            return false;
        }

        Shell activeShell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        boolean checked = MessageDialog.openConfirm(activeShell, Messages.getString("ConnectionContextHelper.UpdateTitle"), //$NON-NLS-1$
                Messages.getString("ConnectionContextHelper.UpdateMessages")); //$NON-NLS-1$
        if (!checked) {
            return false;
        }
        ISelection selection = getRepositoryContext(connItem.getConnection().getContextId(), true);
        if (selection == null || selection.isEmpty()) {
            return false;
        }
        Object firstElement = ((IStructuredSelection) selection).getFirstElement();
        if (firstElement instanceof RepositoryNode) {
            ContextWizard contextWizard = new ContextWizard(PlatformUI.getWorkbench(), false, (RepositoryNode) firstElement,
                    false);
            WizardDialog dlg = new WizardDialog(activeShell, contextWizard);
            if (dlg.open() == Window.OK) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * ggu Comment method "processContextForJob".
     * 
     * @ignoreContextMode if true, only work for jobtemplate plugin(so far).
     */
    public static void addContextForNodeParameter(final INode node, final ConnectionItem connItem, final boolean ignoreContextMode) {
        if (node == null || connItem == null) {
            return;
        }
        IProcess process = node.getProcess();
        if (process instanceof IProcess2) {
            addContextForElementParameters((IProcess2) process, connItem, node.getElementParameters(), null, ignoreContextMode);
        }
    }

    /**
     * 
     * ggu Comment method "addContextForProcessParameter".
     * 
     * @param process
     * @param connItem
     * @param section for EXTRA and STATSANDLOGS
     * @ignoreContextMode if true, only work for jobtemplate plugin(so far).
     */
    public static void addContextForProcessParameter(final IProcess2 process, final ConnectionItem connItem,
            final EComponentCategory category, final boolean ignoreContextMode) {
        if (process == null || connItem == null) {
            return;
        }
        addContextForElementParameters(process, connItem, process.getElementParameters(), category, ignoreContextMode);
    }

    /**
     * 
     * ggu Comment method "addContextForElementParameters".
     * 
     * @param process
     * @param connItem
     * @param elementParameters
     * @param category
     * @param checked
     */
    private static void addContextForElementParameters(final IProcess2 process, final ConnectionItem connItem,
            List<? extends IElementParameter> elementParameters, final EComponentCategory category,
            final boolean ignoreContextMode) {
        if (connItem == null || elementParameters == null || process == null) {
            return;
        }

        if (isHadoopSubItem(connItem)) {
            addContextForHadoopElementParameters(process, connItem, elementParameters, category, ignoreContextMode);
            return;
        }

        Connection connection = connItem.getConnection();
        if (connection != null && connection.isContextMode()) {
            // get the context variables from the node parameters.
            Set<String> neededVars = retrieveContextVar(elementParameters, connection, category);
            boolean isGeneric = isGenericConnection(connection);
            if (neededVars != null && !neededVars.isEmpty() || isGeneric) {
                ContextItem contextItem = ContextUtils.getContextItemById2(connection.getContextId());
                if (contextItem != null) {
                    // find added variables
                    Set<String> tempVars = null;
                    if(isGeneric){
                        tempVars = checkAndAddContextVariables(contextItem, process.getContextManager(),
                                false);
                    }else{
                        tempVars = checkAndAddContextVariables(contextItem, neededVars, process.getContextManager(),
                                false);
                    }
                    Set<String> addedVars = tempVars; 
                    
                    if (addedVars != null && !addedVars.isEmpty()
                            && (isGeneric || !isAddContextVar(contextItem, process.getContextManager(), neededVars))) {
                        AtomicBoolean added = new AtomicBoolean();
                        if (ignoreContextMode) {
                            addContextVarForJob(process, contextItem, addedVars);
                            added.set(true);
                        } else {
                            // show
                            Map<String, Set<String>> addedVarsMap = new HashMap<String, Set<String>>();
                            addedVarsMap.put(connItem.getProperty().getId(), addedVars);
                            PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

                                @Override
                                public void run() {
                                    if (showContextdialog(process, contextItem, process.getContextManager(), addedVarsMap,
                                            addedVars)) {
                                        added.set(true);
                                    }
                                }
                            });
                        }
                        // refresh context view
                        if (added.get()) {
                            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                                        .getService(IDesignerCoreService.class);
                                PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

                                    @Override
                                    public void run() {
                                        service.switchToCurContextsView();
                                    }
                                });
                            }
                        }
                    }
                }
            }

        }
    }

    private static boolean isHadoopSubItem(ConnectionItem connItem) {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService.isHadoopSubItem2(connItem);
        }
        return false;
    }

    private static void addContextForHadoopElementParameters(final IProcess2 process, final ConnectionItem connItem,
            List<? extends IElementParameter> elementParameters, final EComponentCategory category,
            final boolean ignoreContextMode) {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            Connection connection = connItem.getConnection();
            if (hadoopClusterService.isInContextMode(connection)) {
                Set<String> neededVars = retrieveContextVar(elementParameters, connection, category);
                if (neededVars != null && !neededVars.isEmpty()) {
                    ContextItem contextItem = ContextUtils.getContextItemById2(connection.getContextId());
                    ConnectionItem hadoopClusterItem = (ConnectionItem) hadoopClusterService.getHadoopClusterBySubitemId(connItem
                            .getProperty().getId());
                    Connection hadoopClusterConnection = hadoopClusterItem.getConnection();
                    ContextItem hadoopClusterContextItem = ContextUtils.getContextItemById2(hadoopClusterConnection
                            .getContextId());
                    Set<String> hcNeededVars = retrieveContextVar(elementParameters, hadoopClusterConnection, category, true);
                    List<ContextItem> contextItems = new ArrayList<>();
                    if (contextItem != null || hadoopClusterContextItem != null) {
                        // find added variables
                        Set<String> connAddedVars = null;
                        Set<String> hcAddedVars = null;
                        if (contextItem != null) {
                            connAddedVars = checkAndAddContextVariables(contextItem, neededVars, process.getContextManager(),
                                    false);
                            contextItems.add(contextItem);
                        }
                        if (hadoopClusterContextItem != null) {
                            hcAddedVars = checkAndAddContextVariables(hadoopClusterContextItem, hcNeededVars,
                                    process.getContextManager(), false);
                            contextItems.add(hadoopClusterContextItem);
                        }

                        boolean added = false;
                        Map<String, Set<String>> addedVarsMap = new HashMap<>();
                        Map<ContextItem, Set<String>> contextToVars = new HashMap<>();
                        if (connAddedVars != null && !connAddedVars.isEmpty()
                                && !isAddContextVar(contextItem, process.getContextManager(), neededVars)) {
                            if (ignoreContextMode) {
                                addContextVarForJob(process, contextItem, connAddedVars);
                                added = true;
                            } else {
                                addedVarsMap.put(connItem.getProperty().getId(), connAddedVars);
                                contextToVars.put(contextItem, connAddedVars);
                            }
                        }
                        if (hcAddedVars != null && !hcAddedVars.isEmpty()
                                && !isAddContextVar(hadoopClusterContextItem, process.getContextManager(), hcNeededVars)) {
                            if (ignoreContextMode) {
                                addContextVarForJob(process, hadoopClusterContextItem, hcAddedVars);
                                added = true;
                            } else {
                                addedVarsMap.put(hadoopClusterItem.getProperty().getId(), hcAddedVars);
                                contextToVars.put(hadoopClusterContextItem, hcAddedVars);
                            }
                        }

                        if (addedVarsMap.size() > 0) {
                            if (showContextDialogForHadoop(process, contextItems, process.getContextManager(), addedVarsMap,
                                    contextToVars)) {
                                added = true;
                            }
                        }

                        // refresh context view
                        if (added) {
                            if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                                IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault()
                                        .getService(IDesignerCoreService.class);
                                service.switchToCurContextsView();
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addContextVarsToExistVariable(IProcess2 process, ContextItem contextItem, Set<String> addedVars,
            IContextManager contextMgr) {
        Set<String> addedContext = ConnectionContextHelper.checkAndAddContextVariables(contextItem, addedVars, contextMgr, false);
        if (addedContext != null && addedContext.size() > 0) {
            ConnectionContextHelper.addContextVarForJob(process, contextItem, addedVars);
        }
    }

    private static boolean showContextDialogForHadoop(IProcess2 process, List<ContextItem> contextItems,
            IContextManager contextMgr, Map<String, Set<String>> addedVarsMap, Map<ContextItem, Set<String>> contextToVars) {
        boolean isAddContext = false;
        ShowAddedContextdialog showDialog = new ShowAddedContextdialog(addedVarsMap, true);
        if (showDialog.open() == Window.OK) {
            if (ConnectionContextHelper.containsVariable(contextMgr)) {
                for (ContextItem contextItem : contextItems) {
                    Set<String> addedVars = contextToVars.get(contextItem);
                    if (addedVars != null) {
                        addContextVarsToExistVariable(process, contextItem, addedVars, contextMgr);
                    }
                }
            } else {
                // construct selectedContextItems
                List<ContextItem> selectedContextItems = new ArrayList<>();
                Set<String> groupSet = new HashSet<>();
                for (ContextItem contextItem : contextItems) {
                    selectedContextItems.add(contextItem);
                    // check Show ContextGroup
                    for (ContextType type : (List<ContextType>) contextItem.getContext()) {
                        groupSet.add(type.getName());
                    }
                }
                Set<String> curGroupSet = new HashSet<>();
                for (IContext context : contextMgr.getListContext()) {
                    curGroupSet.add(context.getName());
                }
                Set<String> contextGoupNameSet = new HashSet<>();
                if (!curGroupSet.containsAll(groupSet)) {
                    // ask to copy all context group
                    SelectRepositoryContextGroupDialog groupDialog = new SelectRepositoryContextGroupDialog(PlatformUI
                            .getWorkbench().getDisplay().getActiveShell(), contextMgr, new ContextManagerHelper(contextMgr),
                            selectedContextItems);
                    if (Dialog.OK == groupDialog.open()) {
                        contextGoupNameSet = groupDialog.getSelectedContextGroupName();
                    }
                }
                for (ContextItem contextItem : contextItems) {
                    addContextVarForJob(process, contextItem, contextMgr, contextToVars.get(contextItem), contextGoupNameSet);
                }
            }
            isAddContext = true;
        }
        return isAddContext;
    }

    public static boolean showContextdialog(IProcess2 process, ContextItem contextItem, IContextManager contextManager,
            Map<String, Set<String>> addedVarsMap, Set<String> addedVars) {
        boolean isAddContext = false;
        ShowAddedContextdialog showDialog = new ShowAddedContextdialog(addedVarsMap, true);
        if (showDialog.open() == Window.OK) {
            if (ConnectionContextHelper.containsVariable(contextManager)) {
                Set<String> addedContext = ConnectionContextHelper.checkAndAddContextVariables(contextItem, addedVars,
                        contextManager, false);
                if (addedContext != null && addedContext.size() > 0) {
                    ConnectionContextHelper.addContextVarForJob(process, contextItem, addedVars);
                }
            } else {
                showContextGroupDialog(process, contextItem, contextManager, addedVars);
            }
            isAddContext = true;
        }
        return isAddContext;
    }

    public static void showContextGroupDialog(IProcess2 process, ContextItem contextItem, IContextManager contextManager,
            Set<String> addedVars) {
        // construct selectedContextItems
        List<ContextItem> selectedContextItems = new ArrayList<ContextItem>();
        selectedContextItems.add(contextItem);
        // check Show ContextGroup
        Set<String> groupSet = new HashSet<String>();
        for (ContextType type : (List<ContextType>) contextItem.getContext()) {
            groupSet.add(type.getName());
        }
        Set<String> curGroupSet = new HashSet<String>();
        for (IContext context : contextManager.getListContext()) {
            curGroupSet.add(context.getName());
        }
        Set<String> contextGoupNameSet = new HashSet<String>();
        if (!curGroupSet.containsAll(groupSet)) {
            // ask to copy all context group
            SelectRepositoryContextGroupDialog groupDialog = new SelectRepositoryContextGroupDialog(PlatformUI.getWorkbench()
                    .getDisplay().getActiveShell(), contextManager, new ContextManagerHelper(contextManager),
                    selectedContextItems);
            if (Dialog.OK == groupDialog.open()) {
                contextGoupNameSet = groupDialog.getSelectedContextGroupName();
            }
        }
        addContextVarForJob(process, contextItem, contextManager, addedVars, contextGoupNameSet);

    }

    /**
     * 
     * ggu Comment method "checkNodesPropertiesForAddedContextMode".
     * 
     * @param process
     */
    public static void checkNodesPropertiesForAddedContextMode(final IProcess2 process) {
        if (process == null) {
            return;
        }

        Map<String, Set<String>> varsMap = new HashMap<String, Set<String>>();
        // main
        checkProcessMainProperties(varsMap, process, EComponentCategory.EXTRA);
        checkProcessMainProperties(varsMap, process, EComponentCategory.STATSANDLOGS);
        // nodes
        for (INode node : process.getGraphicalNodes()) {
            checkProcessNodesProperties(varsMap, node);
        }
        //
        if (!varsMap.isEmpty()) {
            Map<String, Set<String>> addedVarsMap = new HashMap<String, Set<String>>();
            for (String id : varsMap.keySet()) {
                ConnectionItem connItem = UpdateRepositoryUtils.getConnectionItemByItemId(id);
                if (connItem != null) {
                    ContextItem contextItem = ContextUtils.getContextItemById2(connItem.getConnection().getContextId());
                    if (contextItem != null) {
                        // add needed vars into job
                        Set<String> addedVars = checkAndAddContextVariables(contextItem, varsMap.get(id),
                                process.getContextManager(), false);
                        if (addedVars != null && !addedVars.isEmpty()) {
                            String source = UpdateRepositoryUtils.getRepositorySourceName(connItem);
                            addedVarsMap.put(source, addedVars);
                        }
                    }
                }
            }
            if (!addedVarsMap.isEmpty()) {
                // show
                ShowAddedContextdialog showDialog = new ShowAddedContextdialog(addedVarsMap);
                if (showDialog.open() == Window.OK) {
                    boolean added = false;
                    for (String id : varsMap.keySet()) {
                        ConnectionItem connItem = UpdateRepositoryUtils.getConnectionItemByItemId(id);
                        if (connItem != null) {
                            String source = UpdateRepositoryUtils.getRepositorySourceName(connItem);
                            if (addedVarsMap.containsKey(source)) {
                                ContextItem contextItem = ContextUtils.getContextItemById2(connItem.getConnection()
                                        .getContextId());
                                if (contextItem != null) {
                                    addContextVarForJob(process, contextItem, addedVarsMap.get(source));
                                    added = true;
                                }
                            }
                        }
                    }

                    // refresh context view
                    if (added) {
                        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
                            IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                                    IDesignerCoreService.class);
                            service.switchToCurContextsView();
                        }
                    }
                }
            }
        }
    }

    private static void checkProcessMainProperties(Map<String, Set<String>> varsMap, final IProcess2 process,
            final EComponentCategory category) {
        if (process != null && (category == EComponentCategory.EXTRA || EComponentCategory.STATSANDLOGS == category)) {
            calcContextVariablesFromParameters(varsMap, process.getElementParameters(), category);
        }
    }

    private static void checkProcessNodesProperties(Map<String, Set<String>> varsMap, final INode node) {
        if (node == null) {
            return;
        }
        calcContextVariablesFromParameters(varsMap, node.getElementParameters(), null);
    }

    private static void calcContextVariablesFromParameters(Map<String, Set<String>> varsMap,
            List<? extends IElementParameter> parameters, final EComponentCategory category) {
        if (parameters != null) {
            IElementParameter propertyParam = null;
            for (IElementParameter param : parameters) {
                if ((category == null || category == param.getCategory())
                        && param.getFieldType() == EParameterFieldType.PROPERTY_TYPE && param.isShow(parameters)) {
                    propertyParam = param;
                    break;
                }
            }
            if (propertyParam != null) {
                IElementParameter param = propertyParam.getChildParameters().get("REPOSITORY_PROPERTY_TYPE"); //$NON-NLS-1$
                if (param != null) {
                    String id = (String) param.getValue();
                    ConnectionItem connItem = UpdateRepositoryUtils.getConnectionItemByItemId(id);
                    if (connItem != null) {
                        checkContextMode(connItem);
                        Connection connection = connItem.getConnection();
                        if (connection.isContextMode()) {
                            Set<String> neededVars = retrieveContextVar(parameters, connection, category);
                            if (neededVars != null && !neededVars.isEmpty()) {
                                Set<String> varsSet = varsMap.get(id);
                                if (varsSet == null) {
                                    varsMap.put(id, neededVars);
                                } else {
                                    varsSet.addAll(neededVars);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Set<String> retrieveContextVar(List<? extends IElementParameter> elementParameters, Connection connection,
            EComponentCategory category) {
        return retrieveContextVar(elementParameters, connection, category, false);
    }
    
    public static boolean isGenericConnection(Connection connection){
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            IGenericWizardService wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(
                    IGenericWizardService.class);
            if (wizardService != null) {
                return wizardService.isGenericConnection(connection);
            }
        }
        return false;
    }

    public static Set<String> retrieveContextVar(List<? extends IElementParameter> elementParameters, Connection connection,
            EComponentCategory category, boolean onlyConsiderShowedParam) {
        if (elementParameters == null || connection == null) {
            return null;
        }
        Set<String> addedVars = new HashSet<String>();
        String var = null;
        for (IElementParameter param : elementParameters) {
            if (onlyConsiderShowedParam && !param.isShow(elementParameters)) {
                continue;
            }
            if (category == null || category == param.getCategory()) {
                String repositoryValue = param.getRepositoryValue();
                if (repositoryValue != null) {
                    Object objectValue = RepositoryToComponentProperty.getValue(connection, repositoryValue, null);

                    if (objectValue != null) {
                        if (objectValue instanceof List) {
                            List list = (List) objectValue;
                            for (int i = 0; i < list.size(); i++) {
                                Object object = list.get(i);
                                if (object instanceof HashMap) {
                                    Map map = (HashMap) object;
                                    if (!map.isEmpty()) {
                                        Set keySet = map.keySet();
                                        Iterator iterator = keySet.iterator();
                                        while (iterator.hasNext()) {
                                            String key = (String) iterator.next();
                                            Object object2 = map.get(key);
                                            if (object2 instanceof String) {
                                                var = ContextParameterUtils.getVariableFromCode((String) object2);
                                                if (var != null) {
                                                    addedVars.add(var);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (objectValue instanceof String) {
                            var = ContextParameterUtils.getVariableFromCode((String) objectValue);
                            if (var != null) {
                                addedVars.add(var);
                            }
                        }
                    }
                } else if ("ELT_SCHEMA_NAME".equals(param.getName()) && connection.isContextMode()
                        && param.getValue() instanceof String) {
                    // TDI-20184
                    var = ContextParameterUtils.getVariableFromCode((String) param.getValue());
                    if (var != null) {
                        addedVars.add(var);
                    }
                }
            }
        }

        return addedVars;
    }

    /**
     * DOC zli Comment method "addContextVarForJob".
     * 
     * @param process
     * @param contextItem
     * @param ctxManager added for bug 15608
     */
    public static void addContextVarForJob(IProcess2 process, final ContextItem contextItem, final IContextManager ctxManager) {
        addContextVarForJob(process, contextItem.getContext(), contextItem.getDefaultContext(),
                contextItem.getProperty().getId(), ctxManager);
    }

    /**
     * add the context list into the job.
     * 
     * @param process
     * @param contexts
     * @param defaultContextName
     * @param contextItemId the ContextItem's property Id or null
     * @param ctxManager
     */
    public static void addContextVarForJob(IProcess2 process, final List<ContextType> contexts, final String defaultContextName,
            final String contextItemId, final IContextManager ctxManager) {
        if (process == null || contexts == null || defaultContextName == null || ctxManager == null) {
            return;
        }

        // Command cmd = new Command() {
        //
        // @Override
        // public void execute() {
        // checkAndAddContextsVarDND(contexts, defaultContextName, contextItemId, ctxManager);
        // }
        // };

        CheckAndAddContextDNDCommand cmd = new CheckAndAddContextDNDCommand(contexts, defaultContextName, contextItemId,
                ctxManager);

        boolean executed = false;
        if (process instanceof IGEFProcess) {
            IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
            if (designerCoreUIService != null) {
                executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
            }
        }
        if (!executed) {
            cmd.execute();
        }

    }

    @SuppressWarnings("unchecked")
    public static void addContextVarForJob(IProcess2 process, final ContextItem contextItem, final Set<String> addedVars) {
        addContextVarForJob(process, contextItem.getContext(), contextItem.getDefaultContext(),
                contextItem.getProperty().getId(), addedVars);
    }

    /**
     * add the context variables into the job.
     * 
     * @param process
     * @param contexts
     * @param defaultContextName the default context name of contexts
     * @param contextItemId the ContextItem's property Id or null
     * @param addedVars the variables need to adding
     */
    public static void addContextVarForJob(IProcess2 process, final List<ContextType> contexts, final String defaultContextName,
            final String contextItemId, final Set<String> addedVars) {
        if (process == null || contexts == null || defaultContextName == null || addedVars == null || addedVars.isEmpty()) {
            return;
        }
        final IContextManager ctxManger = process.getContextManager();
        if (ctxManger != null) {
            CheckAndAddContextVariablesCommand cmd = new CheckAndAddContextVariablesCommand(contexts, defaultContextName,
                    contextItemId, addedVars, ctxManger, true);

            boolean executed = false;
            if (process instanceof IGEFProcess) {
                IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
                if (designerCoreUIService != null) {
                    executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
                }
            }
            if (!executed) {
                cmd.execute();
            }
        }
    }

    /**
     * merge the context variables and groups: add variables into the job; add context group into the job.
     * 
     * @param process
     * @param contexts
     * @param defaultContextName the default context name of contexts
     * @param contextItemId the ContextItem's property Id or null
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     */
    public static void mergeContextVarForJob(IProcess2 process, final List<ContextType> contexts,
            final String defaultContextName, final String contextItemId, final IContextManager ctxManager,
            final Set<String> addedVars, final Set<String> contextGoupNameSet) {
        if (process == null || contexts == null || defaultContextName == null || ctxManager == null || addedVars == null
                || contextGoupNameSet == null || (addedVars.isEmpty() && contextGoupNameSet.isEmpty())) {
            return;
        }

        Command cmd = new Command() {

            @Override
            public void execute() {
                mergeContextVariables(contexts, defaultContextName, contextItemId, ctxManager, addedVars, contextGoupNameSet);
            }
        };
        boolean executed = false;
        if (process instanceof IGEFProcess) {
            IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
            if (designerCoreUIService != null) {
                executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
            }
        }
        if (!executed) {
            cmd.execute();
        }

    }

    /**
     * merge the context variables and groups: add variables into the job; add context group into the job.
     * 
     * @param process
     * @param contextItem
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     */
    public static void mergeContextVarForJob(IProcess2 process, final ContextItem contextItem, final IContextManager ctxManager,
            final Set<String> addedVars, final Set<String> contextGoupNameSet) {
        if (process == null || contextItem == null || ctxManager == null || addedVars == null || contextGoupNameSet == null
                || (addedVars.isEmpty() && contextGoupNameSet.isEmpty())) {
            return;
        }

        Command cmd = new Command() {

            @Override
            public void execute() {
                mergeContextVariables(contextItem, ctxManager, addedVars, contextGoupNameSet);
            }
        };
        boolean executed = false;
        if (process instanceof IGEFProcess) {
            IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
            if (designerCoreUIService != null) {
                executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
            }
        }
        if (!executed) {
            cmd.execute();
        }

    }

    /**
     * merge the context variables and groups: add variables from contexts into ctxManager; add context group from
     * contexts into ctxManager.
     * 
     * @param contexts
     * @param defaultContextName the default context name of contexts
     * @param contextItemId the ContextItem's property Id or null
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     */
    protected static void mergeContextVariables(List<ContextType> contexts, String defaultContextName, String contextItemId,
            IContextManager ctxManager, Set<String> addedVars, Set<String> contextGoupNameSet) {
        mergeContextVariables(contexts, defaultContextName, contextItemId, ctxManager, addedVars, contextGoupNameSet, true);
    }

    /**
     * merge the context variables and groups: add variables from contexts into ctxManager; add context group from
     * contexts into ctxManager.
     * 
     * @param contexts
     * @param defaultContextName the default context name of contexts
     * @param contextItemId the ContextItem's property Id or null
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     * @param fillFlag fill the added context group with the default value of the ctxManager
     */
    protected static void mergeContextVariables(List<ContextType> contexts, String defaultContextName, String contextItemId,
            IContextManager ctxManager, Set<String> addedVars, Set<String> contextGoupNameSet, boolean fillFlag) {
        Map<String, List<ContextParameterType>> map = getContextNameParamsMap(contexts);
        if (map.isEmpty()) {
            return;
        }
        Set<String> existGroupNameSet = new HashSet<String>();
        Set<String> addedContextGroupNames = new HashSet<String>();
        Set<String> alreadyUpdateNameSet = new HashSet<String>();
        for (IContext con : ctxManager.getListContext()) {
            existGroupNameSet.add(con.getName());
        }
        if (contextGoupNameSet.isEmpty()) {
            contextGoupNameSet.add(defaultContextName);
        }
        for (String key : map.keySet()) {
            for (String groupName : contextGoupNameSet) {
                boolean isExtraGroup = false;
                for (String existGroup : existGroupNameSet) {
                    if (key.equals(existGroup)) {
                        isExtraGroup = true;
                        alreadyUpdateNameSet.add(existGroup);
                        break;
                    }
                }
                if (key.equals(groupName) || isExtraGroup) {
                    List<ContextParameterType> list = map.get(key);
                    JobContext jobContext = new JobContext(key);
                    boolean isExistContext = false;
                    if (isExtraGroup) {
                        for (IContext con : ctxManager.getListContext()) {
                            if (key.equals(con.getName())) {
                                if (con instanceof JobContext) {
                                    jobContext = (JobContext) con;
                                    isExistContext = true;
                                    break;
                                }
                            }
                        }
                    }
                    // add the new context's parameter into the new context group
                    setContextParameter(contextItemId, addedVars, list, jobContext);
                    if (!isExistContext) {
                        ctxManager.getListContext().add(jobContext);
                        addedContextGroupNames.add(jobContext.getName());
                    }
                    break;
                }
            }
        }
        // if job context group is not in current add's context,then update context group value to default group value
        existGroupNameSet.removeAll(alreadyUpdateNameSet);
        List<ContextParameterType> list = map.get(defaultContextName);
        if (list == null) {
            return;
        }
        for (String existGroup : existGroupNameSet) {
            for (IContext con : ctxManager.getListContext()) {
                if ((existGroup).equals(con.getName())) {
                    if (con instanceof JobContext) {
                        JobContext jobContext = (JobContext) con;
                        setContextParameter(contextItemId, addedVars, list, jobContext);
                    }
                }
            }
        }
        if (fillFlag) {
            // if the new conext group not in the job's context, then update the new context group value to the default
            // group value of the job
            List<IContextParameter> contextParameterList = ctxManager.getDefaultContext().getContextParameterList();
            if (contextParameterList == null) {
                return;
            }
            Set<String> existVars = new HashSet<String>();
            for (IContextParameter ctxPara : contextParameterList) {
                String ctxParaName = ctxPara.getName();
                if (!addedVars.contains(ctxParaName)) {
                    existVars.add(ctxParaName);
                }
            }
            for (String addedGroup : addedContextGroupNames) {
                for (IContext con : ctxManager.getListContext()) {
                    if (addedGroup.equals(con.getName())) {
                        if (con instanceof JobContext) {
                            JobContext jobContext = (JobContext) con;
                            setContextParameter(existVars, contextParameterList, jobContext);
                        }
                    }
                }
            }
        }
    }

    /**
     * DOC xqliu Comment method "getContextNameParamsMap".
     * 
     * @param contexts
     * @return
     */
    private static Map<String, List<ContextParameterType>> getContextNameParamsMap(List<ContextType> contexts) {
        Map<String, List<ContextParameterType>> map = new HashMap<String, List<ContextParameterType>>();
        Iterator<ContextType> iterator = contexts.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj instanceof ContextTypeImpl) {
                ContextTypeImpl contextTypeImpl = (ContextTypeImpl) obj;
                String name = contextTypeImpl.getName();
                EList<ContextParameterType> contextParameters = contextTypeImpl.getContextParameter();
                Iterator<ContextParameterType> contextParas = contextParameters.iterator();
                List<ContextParameterType> list = new ArrayList<ContextParameterType>();
                while (contextParas.hasNext()) {
                    ContextParameterType contextParameterType = contextParas.next();
                    list.add(contextParameterType);
                }
                map.put(name, list);
            }
        }
        return map;
    }

    /**
     * merge the context variables and groups: add variables from contextItem into ctxManager; add context group from
     * contextItem into ctxManager.
     * 
     * @param contextItem
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     */
    protected static void mergeContextVariables(ContextItem contextItem, IContextManager ctxManager, Set<String> addedVars,
            Set<String> contextGoupNameSet) {
        mergeContextVariables(contextItem.getContext(), contextItem.getDefaultContext(), contextItem.getProperty().getId(),
                ctxManager, addedVars, contextGoupNameSet);
    }

    /**
     * add the variables from ctxParams into JobContext.
     * 
     * @param addedVars the variables need to adding
     * @param ctxParams the list of IContextParameter
     * @param jobContext the JobContext
     */
    private static void setContextParameter(Set<String> addedVars, List<IContextParameter> ctxParams, JobContext jobContext) {
        for (IContextParameter contextImpl : ctxParams) {
            for (String var : addedVars) {
                if (var.equals(contextImpl.getName())) {
                    JobContextParameter contextParam = new JobContextParameter();
                    ContextUtils.updateParameter(contextImpl, contextParam);
                    if (contextImpl.getSource() != null) {
                        contextParam.setSource(contextImpl.getSource());
                    }
                    contextParam.setContext(jobContext);
                    jobContext.getContextParameterList().add(contextParam);
                }
            }
        }
    }

    /**
     * add the variables from ctxParams into JobContext.
     * 
     * @param contextItemId the ContextItem's property Id
     * @param addedVars the variables need to adding
     * @param ctxParams the list of IContextParameter
     * @param jobContext the JobContext
     */
    public static void setContextParameter(String contextItemId, Set<String> addedVars, List<ContextParameterType> ctxParams,
            JobContext jobContext) {
        for (ContextParameterType contextImpl : ctxParams) {
            for (String var : addedVars) {
                if (var.equals(contextImpl.getName())) {
                    JobContextParameter contextParam = new JobContextParameter();
                    ContextUtils.updateParameter(contextImpl, contextParam);
                    if (contextItemId != null) {
                        contextParam.setSource(contextItemId);
                    }
                    contextParam.setContext(jobContext);
                    jobContext.getContextParameterList().add(contextParam);
                }
            }
        }
    }

    /**
     * add the context from the ContextItem into the job.
     * 
     * @param process the job
     * @param contextItem the ContextItem
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     */
    public static void addContextVarForJob(IProcess2 process, final ContextItem contextItem, final IContextManager ctxManager,
            final Set<String> addedVars, final Set<String> contextGoupNameSet) {
        if (process == null || contextItem == null || ctxManager == null || addedVars == null || addedVars.isEmpty()) {
            return;
        }

        // Command cmd = new Command() {
        //
        // @Override
        // public void execute() {
        // checkAndAddContextVariables(contextItem, ctxManager, addedVars, contextGoupNameSet);
        // }
        // };

        MergeContextVariablesCommand cmd = new MergeContextVariablesCommand(contextItem.getContext(),
                contextItem.getDefaultContext(), contextItem.getProperty().getId(), ctxManager, addedVars, contextGoupNameSet,
                false);

        boolean executed = false;
        if (process instanceof IGEFProcess) {
            IDesignerCoreUIService designerCoreUIService = CoreUIPlugin.getDefault().getDesignerCoreUIService();
            if (designerCoreUIService != null) {
                executed = designerCoreUIService.executeCommand((IGEFProcess) process, cmd);
            }
        }
        if (!executed) {
            cmd.execute();
        }

    }

    /**
     * add the context from contextItem into the ctxManager: add the variables and the context groups.
     * 
     * @param contextItem
     * @param ctxManager
     * @param addedVars the variables need to adding
     * @param contextGoupNameSet the context group need to adding
     */
    public static void checkAndAddContextVariables(ContextItem contextItem, IContextManager ctxManager, Set<String> addedVars,
            Set<String> contextGoupNameSet) {
        mergeContextVariables(contextItem.getContext(), contextItem.getDefaultContext(), contextItem.getProperty().getId(),
                ctxManager, addedVars, contextGoupNameSet, false);
    }

    /**
     * add the variables from ctxParams into JobContext.
     * 
     * @param contextItem
     * @param addedVars the variables need to adding
     * @param ctxParams
     * @param jobContext
     */
    public static void setContextParameter(ContextItem contextItem, Set<String> addedVars, List<ContextParameterType> ctxParams,
            JobContext jobContext) {
        setContextParameter(contextItem.getProperty().getId(), addedVars, ctxParams, jobContext);
    }

    public static boolean isAddContextVar(ContextItem contextItem, IContextManager contextManager, Set<String> neededVars) {
        boolean isAdd = true;
        Set<String> addedVars = new HashSet<String>();
        for (IContext context : contextManager.getListContext()) {
            ContextType contextType = null;
            List<ContextType> contextTypeList = contextItem.getContext();
            for (ContextType contye : contextTypeList) {
                if (context.getName() != null && contye.getName().toLowerCase().equals(context.getName().toLowerCase())) {
                    contextType = contye;
                    break;
                }
            }
            if (contextType != null) {
                for (String var : neededVars) {
                    if (context.getContextParameter(var) != null) {
                        continue;
                    }
                    ContextParameterType param = ContextUtils.getContextParameterTypeByName(contextType, var);
                    if (param != null) {
                        addedVars.add(var);
                    }
                }
                break;
            }
        }
        if (addedVars != null && !addedVars.isEmpty()) {
            isAdd = false;
        }

        return isAdd;
    }

    /**
     * ggu Comment method "checkAndAddContextVariables".
     */
    public static Set<String> checkAndAddContextVariables(final ContextItem contextItem, final Set<String> neededVars,
            final IContextManager ctxManager, boolean added) {
        return checkAndAddContextVariables(contextItem.getContext(), contextItem.getDefaultContext(), contextItem.getProperty()
                .getId(), neededVars, ctxManager, added);
    }
    
    public static Set<String> checkAndAddContextVariables(final ContextItem contextItem,
            final IContextManager ctxManager, boolean added) {
        List<ContextType> contexts = contextItem.getContext();
        String defaultContextName = contextItem.getDefaultContext();
        String contextItemId = contextItem.getProperty().getId();
        Set<String> addedVars = new HashSet<String>();
        for (IContext context : ctxManager.getListContext()) {
            ContextType type = ContextUtils.getContextTypeByName(contexts, context.getName(), defaultContextName);
            if (type != null) {
                for (ContextParameterType param :(List<ContextParameterType>)type.getContextParameter()){
                    if (context.getContextParameter(param.getName()) != null) {
                        continue;
                    }
                    if(added){
                        JobContextParameter contextParam = new JobContextParameter();

                        ContextUtils.updateParameter(param, contextParam);
                        if (contextItemId != null) {
                            contextParam.setSource(contextItemId);
                        }
                        contextParam.setContext(context);

                        context.getContextParameterList().add(contextParam);
                    }
                    addedVars.add(param.getName());
                }
            }
        }
        return addedVars;
    }

    /**
     * check if there exist variables which need to add into the ctxManager.
     * 
     * @param contexts
     * @param defaultContextName
     * @param contextItemId
     * @param neededVars
     * @param ctxManager
     * @param added add the variable or not
     * @return
     */
    public static Set<String> checkAndAddContextVariables(final List<ContextType> contexts, final String defaultContextName,
            final String contextItemId, final Set<String> neededVars, final IContextManager ctxManager, boolean added) {
        Set<String> addedVars = new HashSet<String>();
        for (IContext context : ctxManager.getListContext()) {
            ContextType type = ContextUtils.getContextTypeByName(contexts, context.getName(), defaultContextName);
            if (type != null) {
                for (IContextParameter jobParam : context.getContextParameterList()) {
                    if (contextItemId.equals(jobParam.getSource())
                            && ContextUtils.getContextParameterTypeByName(type, jobParam.getName()) == null) {
                        jobParam.setSource(IContextParameter.BUILT_IN);
                    }
                }
                for (String var : neededVars) {
                    if (context.getContextParameter(var) != null) {
                        continue;
                    }
                    ContextParameterType param = ContextUtils.getContextParameterTypeByName(type, var);
                    if (param != null) {
                        //
                        if (added) {
                            JobContextParameter contextParam = new JobContextParameter();

                            ContextUtils.updateParameter(param, contextParam);
                            if (contextItemId != null) {
                                contextParam.setSource(contextItemId);
                            }
                            contextParam.setContext(context);

                            context.getContextParameterList().add(contextParam);
                        }
                        addedVars.add(var);
                    }
                }
            }
        }
        return addedVars;
    }

    /*
     * maybe will open dialog to confirm the context set.
     */
    public static ContextType getContextTypeForContextMode(Connection connection) {
        return getContextTypeForContextMode(connection, false);
    }

    public static ContextType getContextTypeForContextMode(Connection connection, String contextName) {
        return getContextTypeForContextMode(connection, contextName, false);
    }

    /*
     * Add for TBD use
     */
    public static ContextType getContextTypeForContextMode(IMetadataConnection connection) {
        return getContextTypeForContextMode(connection, false);
    }

    public static ContextType getContextTypeForContextMode(IMetadataConnection connection, String contextName) {
        return getContextTypeForContextMode(connection, contextName, false);
    }

    /**
     * 
     * wzhang Comment method "containsVariable".
     * 
     * @param ctxManager
     * @return
     */
    public static boolean containsVariable(IContextManager ctxManager) {
        List<IContext> listContext = ctxManager.getListContext();
        for (IContext context : listContext) {
            List<IContextParameter> paraList = context.getContextParameterList();
            if (!paraList.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * wzhang Comment method "checkAndAddContextsVarDND".
     * 
     * @param item
     * @param ctxManager
     */
    public static void checkAndAddContextsVarDND(ContextItem item, IContextManager ctxManager) {
        checkAndAddContextsVarDND(item.getContext(), item.getDefaultContext(), item.getProperty().getId(), ctxManager);
    }

    /**
     * add the context into the ctxManager.
     * 
     * @param contexts
     * @param defaultContextName the default context name of the contexts
     * @param contextItemId the ContextItem's property Id or null
     * @param ctxManager
     */
    public static void checkAndAddContextsVarDND(List<ContextType> contexts, String defaultContextName, String contextItemId,
            IContextManager ctxManager) {
        Map<String, List<ContextParameterType>> map = getContextNameParamsMap(contexts);
        if (map.isEmpty()) {
            return;
        }
        ctxManager.getListContext().clear();

        for (String key : map.keySet()) {
            List<ContextParameterType> list = map.get(key);
            JobContext jobContext = new JobContext(key);
            for (ContextParameterType contextImpl : list) {
                JobContextParameter contextParam = new JobContextParameter();
                ContextUtils.updateParameter(contextImpl, contextParam);
                if (contextItemId != null) {
                    contextParam.setSource(contextItemId);
                }
                contextParam.setContext(jobContext);
                jobContext.getContextParameterList().add(contextParam);
            }
            ctxManager.getListContext().add(jobContext);
            if (key.equals(defaultContextName)) {
                ctxManager.setDefaultContext(jobContext);
            }
        }
    }

    /*
     * if defaultContext is false, maybe will open dialog to confirm the context set. same as
     * getContextTypeForContextMode(connection).
     * 
     * if defaultContext is true, will use the default context.
     */
    public static ContextType getContextTypeForContextMode(Connection connection, boolean defaultContext) {
        return getContextTypeForContextMode(null, connection, null, defaultContext);
    }

    public static ContextType getContextTypeForContextMode(Connection connection, String selectedContext, boolean defaultContext) {
        return getContextTypeForContextMode(null, connection, selectedContext, defaultContext);
    }

    public static ContextType getContextTypeForContextMode(Shell shell, Connection connection) {
        return getContextTypeForContextMode(shell, connection, null, false);
    }

    public static ContextType getContextTypeForContextMode(Shell shell, Connection connection, String selectedContext,
            boolean defaultContext) {
        return getContextTypeForContextMode(shell, connection, selectedContext, defaultContext, false);
    }

    public static ContextType getContextTypeForContextMode(Shell shell, Connection connection, boolean canCancel) {
        return getContextTypeForContextMode(shell, connection, null, false, canCancel);
    }

    /*
     * Add for TBD using
     */
    public static ContextType getContextTypeForContextMode(IMetadataConnection connection, boolean defaultContext) {
        return getContextTypeForContextMode(null, connection, null, defaultContext);
    }

    public static ContextType getContextTypeForContextMode(IMetadataConnection connection, String selectedContext,
            boolean defaultContext) {
        return getContextTypeForContextMode(null, connection, selectedContext, defaultContext);
    }

    public static ContextType getContextTypeForContextMode(Shell shell, IMetadataConnection connection) {
        return getContextTypeForContextMode(shell, connection, null, false);
    }

    public static ContextType getContextTypeForContextMode(Shell shell, IMetadataConnection connection, String selectedContext,
            boolean defaultContext) {
        return getContextTypeForContextMode(shell, connection, selectedContext, defaultContext, false);
    }

    public static ContextType getContextTypeForContextMode(Shell shell, IMetadataConnection connection, boolean canCancel) {
        return getContextTypeForContextMode(shell, connection, null, false, canCancel);
    }

    /**
     * 
     * ggu Comment method "getContextTypeForContextMode".
     * 
     * if connection is in context mode,choose the context. if return null, the connection is not in context mode.
     * 
     * if canCancel is true, the selecting cotnext sets dialog will can be cancel.
     */
    private static ContextType getContextTypeForContextMode(Shell shell, Connection connection, String selectedContext,
            boolean defaultContext, boolean canCancel) {
        if (connection == null) {
            return null;
        }
        // TDI-17320
        Shell sqlBuilderDialogShell = getSqlBuilderDialogShell();
        if (sqlBuilderDialogShell != null && !sqlBuilderDialogShell.isDisposed() && shell == null) {
            shell = sqlBuilderDialogShell;
        }
        ContextItem contextItem = ContextUtils.getContextItemById2(connection.getContextId());
        if (contextItem != null && connection.isContextMode()) {
            if (defaultContext) {
                selectedContext = contextItem.getDefaultContext();
            } else if (selectedContext == null) {
                if (contextItem.getContext().size() > 1) {
                    final ContextSetsSelectionDialog setsDialog = new ContextSetsSelectionDialog(shell, contextItem, canCancel);
                    Display.getDefault().syncExec(new Runnable() {// launch the dialog box in the UI thread beacause the

                                // method may be called from other threads.

                                @Override
                                public void run() {
                                    setsDialog.open();
                                }
                            });
                    selectedContext = setsDialog.getSelectedContext();
                    connection.setContextName(selectedContext);
                } else {
                    selectedContext = contextItem.getDefaultContext();
                }
            }
            // if can cancel, can't return the default contex by auto.
            return ContextUtils.getContextTypeByName(contextItem, selectedContext, !canCancel);
        }
        return null;
    }

    /*
     * Add for TBD use
     */
    private static ContextType getContextTypeForContextMode(Shell shell, IMetadataConnection connection, String selectedContext,
            boolean defaultContext, boolean canCancel) {
        if (connection == null) {
            return null;
        }
        // TDI-17320
        Shell sqlBuilderDialogShell = getSqlBuilderDialogShell();
        if (sqlBuilderDialogShell != null && !sqlBuilderDialogShell.isDisposed() && shell == null) {
            shell = sqlBuilderDialogShell;
        }
        ContextItem contextItem = ContextUtils.getContextItemById2(connection.getContextId());
        if (contextItem != null && connection.getContextId() != null) {
            if (defaultContext) {
                selectedContext = contextItem.getDefaultContext();
            } else if (selectedContext == null) {
                if (contextItem.getContext().size() > 1) {
                    final ContextSetsSelectionDialog setsDialog = new ContextSetsSelectionDialog(shell, contextItem, canCancel);
                    Display.getDefault().syncExec(new Runnable() {// launch the dialog box in the UI thread beacause the

                                // method may be called from other threads.

                                @Override
                                public void run() {
                                    setsDialog.open();
                                }
                            });
                    selectedContext = setsDialog.getSelectedContext();
                    connection.setContextName(selectedContext);
                } else {
                    selectedContext = contextItem.getDefaultContext();
                }
            }
            // if can cancel, can't return the default contex by auto.
            return ContextUtils.getContextTypeByName(contextItem, selectedContext, !canCancel);
        }
        return null;
    }

    /**
     * DOC gcui Comment method "getContextTypeForJob".
     * 
     * @param shell
     * @param contextManager
     * @param canCancel
     * @return
     */
    public static String getContextTypeForJob(Shell shell, IContextManager contextManager, boolean canCancel) {
        ContextSetsSelectionDialog setsDialog = new ContextSetsSelectionDialog(shell, contextManager, canCancel);
        setsDialog.open();
        String currentDefaultName = setsDialog.getSelectedContext();
        return currentDefaultName;
    }

    /**
     * 
     * ggu Comment method "getOriginalValue".
     * 
     * if value is context mode, return original value.
     * 
     * @deprecated use instead ContextParameterUtils.getOriginalValue
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static String getOriginalValue(ContextType contextType, final String value) {
        return ContextParameterUtils.getOriginalValue(contextType, value);
    }

    /**
     * @deprecated use instead CloneConnectionUtils.cloneConnectionProperties
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    public static void cloneConnectionProperties(Connection sourceConn, Connection targetConn) {
        CloneConnectionUtils.cloneConnectionProperties(sourceConn, targetConn);
    }

    /*
     * 
     */
    public static int convertValue(String value) {
        if (value == null || value.equals(EMPTY)) {
            return -1;
        }
        int i = -1;
        try {
            i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            //
        }
        return i;
    }

    public static void revertPropertiesForContextMode(ConnectionItem connItem, ContextType contextType) {
        if (connItem == null || contextType == null) {
            return;
        }
        Connection conn = connItem.getConnection();
        if (conn instanceof DatabaseConnection) {
            DBConnectionContextUtils.revertPropertiesForContextMode((DatabaseConnection) conn, contextType);
        } else if (conn instanceof FileConnection) {
            FileConnectionContextUtils.revertPropertiesForContextMode((FileConnection) conn, contextType);
        } else if (conn instanceof LdifFileConnection) {
            OtherConnectionContextUtils.revertLdifFilePropertiesForContextMode((LdifFileConnection) conn, contextType);
        } else if (conn instanceof XmlFileConnection) {
            OtherConnectionContextUtils.revertXmlFilePropertiesForContextMode((XmlFileConnection) conn, contextType);
        } else if (conn instanceof LDAPSchemaConnection) {
            OtherConnectionContextUtils.revertLDAPSchemaPropertiesForContextMode((LDAPSchemaConnection) conn, contextType);
        } else if (conn instanceof WSDLSchemaConnection) {
            OtherConnectionContextUtils.revertWSDLSchemaPropertiesForContextMode((WSDLSchemaConnection) conn, contextType);
        } else if (conn instanceof SalesforceSchemaConnection) {
            OtherConnectionContextUtils.revertSalesforcePropertiesForContextMode((SalesforceSchemaConnection) conn, contextType);
        } else if (conn instanceof SAPConnection) {
            OtherConnectionContextUtils.revertSAPPropertiesForContextMode((SAPConnection) conn, contextType);
        } else if (conn instanceof MDMConnection) {
            OtherConnectionContextUtils.revertMDMConnectionPropertiesForContextMode((MDMConnection) conn, contextType);
        } else if (conn instanceof GenericSchemaConnection) {
            //
        } else {
            ExtendedNodeConnectionContextUtils.revertPropertiesForContextMode(conn, contextType);
        }
        // set connection for context mode
        conn.setContextMode(false);
        conn.setContextId(EMPTY);
    }

    private static Shell sqlBuilderDialogShell;

    public static Shell getSqlBuilderDialogShell() {
        return sqlBuilderDialogShell;
    }

    public static void setSqlBuilderDialogShell(Shell sqlBuilderDialogShellTem) {
        sqlBuilderDialogShell = sqlBuilderDialogShellTem;
    }

    public static Set<ContextItem> getContextItems() {
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        Set<ContextItem> itemList = null;

        try {
            itemList = new HashSet<ContextItem>(factory.getContextItem());
        } catch (PersistenceException e) {
            throw new RuntimeException(e);
        }
        if (itemList != null) {
            List<ContextItem> toRemove = new ArrayList<ContextItem>();

            for (ContextItem contextItem : itemList) {
                if (factory.getStatus(contextItem) == ERepositoryStatus.DELETED) {
                    toRemove.add(contextItem);
                }
            }
            itemList.removeAll(toRemove);
        }
        return itemList;
    }

    public static String getParamValueOffContext(Connection connection, String value) {
        String realValue = value;
        if (connection != null && connection.isContextMode()) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connection,
                    connection.getContextName());
            realValue = TalendQuoteUtils.removeQuotes(ContextParameterUtils.getOriginalValue(contextType, value));
        }
        return realValue;
    }

    /*
     * Add for TBD use
     */
    public static String getParamValueOffContext(IMetadataConnection connection, String value) {
        String realValue = value;
        if (connection != null && connection.getContextId() != null) {
            ContextType contextType = ConnectionContextHelper.getContextTypeForContextMode(connection,
                    connection.getContextName());
            realValue = TalendQuoteUtils.removeQuotes(ContextParameterUtils.getOriginalValue(contextType, value));
        }
        return realValue;
    }

    public static boolean isContextMode(Connection connection, String value) {
        if (connection == null || value == null) {
            return false;
        }

        if (isContextedConnection(connection) && ContextParameterUtils.isContainContextParam(value)) {
            return true;
        }
        return false;
    }

    private static boolean isContextedConnection(Connection connection) {
        IHadoopClusterService hadoopClusterService = getHadoopClusterService(connection);
        return isHadoopSubConnection(hadoopClusterService, connection) ? hadoopClusterService.isInContextMode(connection)
                : connection.isContextMode();
    }

    private static boolean isHadoopSubConnection(IHadoopClusterService hadoopClusterService, Connection connection) {
        return hadoopClusterService == null ? false
                : hadoopClusterService.getHadoopClusterConnectionBySubConnection(connection) != null;
    }

    private static IHadoopClusterService getHadoopClusterService(Connection connection) {
        IHadoopClusterService hadoopClusterService = HadoopRepositoryUtil.getHadoopClusterService();
        if (hadoopClusterService != null) {
            return hadoopClusterService;
        }
        return null;
    }

}
