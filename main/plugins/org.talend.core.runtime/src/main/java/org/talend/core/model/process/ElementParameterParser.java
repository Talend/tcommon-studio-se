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
package org.talend.core.model.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.commons.utils.generation.CodeGenerationUtils;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.RulesItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.SQLPatternUtils;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.ItemCacheManager;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 *
 * $Id: ElementParameterParser.java 52559 2010-12-13 04:14:06Z nrousseau $
 *
 */
public final class ElementParameterParser {

    public static final String UNIQUE_NAME = "UNIQUE_NAME"; //$NON-NLS-1$

    private ElementParameterParser() {
    }

    public static String getValue(final IElement node, final String text) {
        String value = new String(""); //$NON-NLS-1$
        if (text == null) {
            return value;
        }
        if (node == null) {
            return null;
        }

        List<IElementParameter> params = (List<IElementParameter>) node.getElementParametersWithChildrens();
        if (params != null && !params.isEmpty()) {
            for (IElementParameter param : params) {
                if (text.indexOf(param.getVariableName()) != -1) {
                    value = getDisplayValue(param);
                    break;
                }
            }
        }
        return value;
    }

    public static String getValue(final IProcess process, final String nodeUniqueName, final String key) {
        for (INode processNode : process.getGeneratingNodes()) {
            if (nodeUniqueName.equals(processNode.getUniqueName())) {
                return ElementParameterParser.getValue(processNode, key);
            }
        }
        return ""; //$NON-NLS-1$
    }

    public static String getStringElementParameterValue(IElementParameter parameter) {
        return getDisplayValue(parameter);
    }

    public static boolean canEncrypt(final IElement node, final String parameterName) {
        String value = getValue(node, parameterName);
        if (canEncryptValue(value)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOC nrousseau Comment method "canEncryptValue".
     * 
     * @param value
     * @return
     */
    public static boolean canEncryptValue(String value) {
        return value != null && value.startsWith("\"") && value.endsWith("\"") //$NON-NLS-1$//$NON-NLS-2$
                && TalendQuoteUtils.filterQuote(value).length() == 0;
    }

    public static String getEncryptedValue(final IElement node, final String parameterName) {
        String value = getValue(node, parameterName);
        value = getEncryptedValue(value);
        return value;
    }

    /**
     * DOC nrousseau Comment method "getEncryptedValue".
     * 
     * @param value
     * @return
     * @throws Exception
     */
    public static String getEncryptedValue(String value) {
        if (canEncryptValue(value)) {
            try {
                String removeQuotes = TalendQuoteUtils.removeQuotes(value);
                removeQuotes = TalendQuoteUtils.checkSlashAndRemoveQuotation(removeQuotes);
                return TalendQuoteUtils.addQuotes(PasswordEncryptUtil.encryptPasswordHex(removeQuotes),
                        TalendQuoteUtils.QUOTATION_MARK);
            } catch (Exception e) {
                ExceptionHandler.process(e);
                return value;
            }
        }
        return value;
    }

    public static String getPasswordValue(final IElement node, final String parameterName) {
        if (getObjectValue(node, parameterName) == null) {
            // this exception only be allowed in dev environment
            ExceptionHandler.process(new Exception("Can't get value from current node " + node + " or the paramter name" //$NON-NLS-1$ //$NON-NLS-2$
                    + parameterName));
            return null;
        } else {
            if (ElementParameterParser.canEncrypt(node, parameterName)) {
                return "routines.system.PasswordEncryptUtil.decryptPassword(" //$NON-NLS-1$
                        + ElementParameterParser.getEncryptedValue(node, parameterName) + ")"; //$NON-NLS-1$
            } else {
                return ElementParameterParser.getValue(node, parameterName);
            }
        }
    }

    public static Boolean getBooleanValue(final IElement node, final String parameterName) {
        if (getObjectValue(node, parameterName) == null) {
            // this exception only be allowed in dev environment
            ExceptionHandler.process(new Exception("Can't get value from current node " + node + " or the paramter name" //$NON-NLS-1$ //$NON-NLS-2$
                    + parameterName));
            return null;
        } else {
            return Boolean.valueOf(getValue(node, parameterName));
        }
    }

    public static INode getLinkedNodeValue(final IElement node, final String parameterName) {
        if (getObjectValue(node, parameterName) == null) {
            // this exception only be allowed in dev environment
            ExceptionHandler.process(new Exception("Can't get value from current node " + node + " or the paramter name" //$NON-NLS-1$ //$NON-NLS-2$
                    + parameterName));
            return null;
        } else {
            String linkedNodeName = ElementParameterParser.getValue(node, parameterName);
            // when linked node in joblet, can be found in generating node list only
            java.util.List<? extends INode> allGeneratingNodes = ((INode) node).getProcess().getGeneratingNodes();
            if (allGeneratingNodes != null && allGeneratingNodes.size() > 0) {
                for (INode linkedNode : allGeneratingNodes) {
                    if (linkedNode.getUniqueName().equals(linkedNodeName)) {
                        return linkedNode;
                    }
                }
            }
        }
        return null;
    }

    public static String getWrapDoubleQuoteValue(final IElement node, final String parameterName) {
        if (getObjectValue(node, parameterName) == null) {
            // this exception only be allowed in dev environment
            ExceptionHandler.process(new Exception("Can't get value from current node " + node + " or the paramter name" //$NON-NLS-1$ //$NON-NLS-2$
                    + parameterName));
            return null;
        } else {
            return "\"" + getValue(node, parameterName) + "\""; //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public static List<Map<String, String>> getTableValue(final IElement node, final String parameterName) {
        Object objectValue = getObjectValue(node, parameterName);
        if (objectValue == null) {
            // this exception only be allowed in dev environment
            ExceptionHandler.process(new Exception("Can't get value from current node " + node + " or the paramter name" //$NON-NLS-1$ //$NON-NLS-2$
                    + parameterName));
            return null;
        } else {
            return ((List<Map<String, String>>) objectValue);
        }
    }

    public static List<Map<String, String>> getTableElementParameterValue(IElementParameter parameter) {
        if (parameter.getFieldType() == EParameterFieldType.TABLE) {
            return createTableValues((List<Map<String, Object>>) parameter.getValue(), parameter);
        }
        return null;
    }

    /**
     * Prefixes and suffixes a Java comment to match Java code problems with UI designer fields.
     *
     * @param node
     * @param text
     * @param fieldName name of field declared in xml component file (or other value for a manual processing)
     * @return
     */
    public static String getValueWithUIFieldKey(final INode node, final String text, String fieldName) {
        String value = getValue(node, text);
        String key = CodeGenerationUtils.buildProblemKey(node.getUniqueName(), fieldName);
        return CodeGenerationUtils.buildJavaStartFieldKey(key) + value + CodeGenerationUtils.buildJavaEndFieldKey(key);
    }

    /**
     * DOC xtan Comment method "getValueWithJavaType".
     * <p>
     * if there want a char ',', but user input in GUI like this ",", this method just deal with this case.
     * </p>
     *
     * @param node
     * @param text
     * @param javaType
     * @return
     */
    public static String getValueWithJavaType(final INode node, final String text, JavaType javaType) {
        String value = getValue(node, text);

        // the default return old value
        String result = value;

        String trimValue = null;

        if (value == null) {
            return null;
        } else {
            trimValue = value.trim();
        }

        if (javaType == JavaTypesManager.CHARACTER) {
            // 5 cases: null(no this parameter in shadow process),
            // String.length()=0(nothing in GUI), "", '', null(this
            // means null directly in GUI)
            if (trimValue.equals("") || trimValue.equals("\"\"") || trimValue.equals("''") || trimValue.equals("null")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                return null;
            } else if (trimValue.length() >= 3 && trimValue.startsWith("\"") && trimValue.endsWith("\"")) { //$NON-NLS-1$ //$NON-NLS-2$
                // "a", "ab", "a, 'a', 'ab', 'a, 93, '\042', '\u0022', '\'',
                // "\u0022"

                String substring = trimValue.substring(1, trimValue.length() - 1);

                if (substring.length() == 1 && (substring.charAt(0) == '\\' || substring.charAt(0) == '\'')) {
                    // support to input like that: "\", "'"
                    result = "'" + "\\" + substring + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    result = "'" + substring + "'"; //$NON-NLS-1$ //$NON-NLS-2$
                }
            }
        }

        return result;
    }

    /**
     * Only work with one element.
     *
     * @param element
     * @param text
     * @return
     */
    public static Object getObjectValue(final IElement element, final String text) {
        if (text == null || element == null) {
            return null;
        }

        List<IElementParameter> params = (List<IElementParameter>) element.getElementParametersWithChildrens();
        if (params != null && !params.isEmpty()) {
            for (int i = 0; i < params.size(); i++) {
                IElementParameter param = params.get(i);
                if (text.indexOf(param.getVariableName()) != -1
                        || (param.getVariableName() != null && param.getVariableName().contains(text))) {
                    if (param.getFieldType() == EParameterFieldType.TABLE) {
                        return createTableValues((List<Map<String, Object>>) param.getValue(), param);
                    }
                    return param.getValue();
                }
            }
        }
        return null;
    }

    /**
     * this method working with tMatchgroup component which has several tables of mathcing rule.
     *
     * @param element
     * @param text
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object getMultiObjectValue(final IElement element, final String text) {
        if (text == null) {
            return null;
        }
        IElementParameter param;

        List<IElementParameter> params = (List<IElementParameter>) element.getElementParametersWithChildrens();
        if (params != null && !params.isEmpty()) {
            List<List<Map<String, String>>> multiObjectValues = new ArrayList<List<Map<String, String>>>();
            for (int i = 0; i < params.size(); i++) {
                param = params.get(i);
                if (text.indexOf(param.getVariableName()) != -1
                        || (param.getVariableName() != null && param.getVariableName().contains(text))) {
                    List<?> paramValues = (List<?>) param.getValue();
                    if (param.getFieldType() == EParameterFieldType.TABLE) {
                        if (paramValues != null && paramValues.size() > 0) {
                            multiObjectValues.add(createTableValues((List<Map<String, Object>>) paramValues, param));
                        }
                        return multiObjectValues;
                    }
                    return paramValues;
                }
            }
        }

        return null;
    }

    public static List<Map<String, String>> createTableValues(final List<Map<String, Object>> paramValues,
            final IElementParameter param) {
        List<Map<String, String>> tableValues = new ArrayList<Map<String, String>>();
        if (paramValues != null) {
            for (Map<String, Object> currentLine : paramValues) {
                tableValues.add(copyLine(currentLine, param));
            }
        }
        return tableValues;
    }

    private static Map<String, String> copyLine(Map<String, Object> currentLine, IElementParameter param) {
        Map<String, String> newLine = new HashMap<String, String>();
        String[] items = param.getListItemsDisplayCodeName();
        for (int i = 0; i < items.length; i++) {
            Object o = currentLine.get(items[i]);
            if (o instanceof Integer) {
                IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[i];
                if ((((Integer) o) == -1) || (tmpParam.getListItemsValue().length == 0)) {
                    newLine.put(items[i], ""); //$NON-NLS-1$
                } else {
                    newLine.put(items[i], (String) tmpParam.getListItemsValue()[(Integer) o]);
                }
            } else {
                if (o instanceof String) {
                    if (param.getName().equals("SQLPATTERN_VALUE")) { //$NON-NLS-1$
                        SQLPatternItem item = SQLPatternUtils.getItemFromCompoundId(param.getElement(), ((String) o));
                        if (item != null) {
                            newLine.put(items[i], new String(item.getContent().getInnerContent()));
                        } else {
                            newLine.put(items[i], ""); //$NON-NLS-1$
                        }
                    } else if (param.getElement() != null && param.getElement() instanceof INode
                            && ((INode) param.getElement()).getComponent().getName().equals("tWebService")) {
                        String replacedValue = (String) o;
                        if (items[i].equals("EXPRESSION")) {
                            String inputRow = "row1";
                            List connList = ((INode) param.getElement()).getIncomingConnections();
                            if (connList.size() > 0) {
                                inputRow = ((IConnection) connList.get(0)).getName();
                                replacedValue = replacedValue.replace("input.", inputRow + ".");
                            }
                        }
                        newLine.put(items[i], replacedValue);
                    } else {
                        newLine.put(items[i], (String) o);
                        if ("".equals(o) && param.getListItemsValue() != null //$NON-NLS-1$
                                && param.getListItemsValue()[i] instanceof IElementParameter) {
                            IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[i];
                            if (tmpParam.getListItemsValue() != null && tmpParam.getListItemsValue().length > 0) {
                                newLine.put(items[i], (String) tmpParam.getListItemsValue()[0]);
                            }
                        }
                    }
                } else {
                    if (o instanceof Boolean) {
                        newLine.put(items[i], ((Boolean) o).toString());
                    } else {
                        newLine.put(items[i], ""); //$NON-NLS-1$
                    }
                }
            }
        }
        return newLine;
    }

    /**
     * Only work with one element.
     *
     * @param element
     * @param text
     * @return
     */
    public static Object getObjectValueXML(final IElement element, final String text) {
        if (text == null) {
            return null;
        }
        IElementParameter param;

        for (int i = 0; i < element.getElementParameters().size(); i++) {
            param = element.getElementParameters().get(i);
            if (text.indexOf(param.getVariableName()) != -1) {
                if (param.getFieldType() == EParameterFieldType.TABLE) {
                    return createTableValuesXML((List<Map<String, Object>>) param.getValue(), param);
                }
                return param.getValue();
            }
        }
        return null;
    }

    private static List<Map<String, String>> createTableValuesXML(final List<Map<String, Object>> paramValues,
            final IElementParameter param) {
        List<Map<String, String>> tableValues = new ArrayList<Map<String, String>>();
        for (Map<String, Object> currentLine : paramValues) {
            tableValues.add(copyLineXML(currentLine, param));
        }
        return tableValues;
    }

    private static Map<String, String> copyLineXML(Map<String, Object> currentLine, IElementParameter param) {
        Map<String, String> newLine = new HashMap<String, String>();
        // PTODO cantoine : check with Nico if cause trouble with others
        // Components.
        String[] items = currentLine.keySet().toArray(new String[] {});
        // //{"QUERY"};
        for (int i = 0; i < items.length; i++) {
            Object o = currentLine.get(items[i]);
            if (o instanceof Integer) {
                IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[i];
                if ((((Integer) o) == -1) || (tmpParam.getListItemsValue().length == 0)) {
                    newLine.put(items[i], ""); //$NON-NLS-1$
                } else {
                    newLine.put(items[i], (String) tmpParam.getListItemsValue()[(Integer) o]);
                }
            } else {
                if (o instanceof String) {
                    newLine.put(items[i], (String) o);
                } else {
                    if (o instanceof Boolean) {
                        newLine.put(items[i], ((Boolean) o).toString());
                    } else {
                        newLine.put(items[i], "*** ERROR in Table ***"); //$NON-NLS-1$
                    }
                }
            }
        }
        return newLine;
    }

    /**
     * Only work with xmlmap.
     *
     * @param element
     * @return
     */
    public static Object getObjectValueXMLTree(final IElement element) {
        if (element instanceof INode) {
            INode node = (INode) element;
            if (node.getExternalNode() != null) {
                return EcoreUtil.copy(node.getExternalNode().getExternalEmfData());
            }
        }
        return null;
    }

    public static String parse(final IElement element, final String text) {
        String newText = ""; //$NON-NLS-1$
        if ((element == null) || (text == null)) {
            return newText;
        }
        IElementParameter param;
        newText = text;
        for (int i = 0; i < element.getElementParameters().size(); i++) {
            param = element.getElementParameters().get(i);
            if (newText.contains(param.getVariableName())) {
                String value = getDisplayValue(param);
                newText = newText.replace(param.getVariableName(), value);
            }
        }
        // [TESB-8518]a new complex parameter pattern.
        // __PREF:plugin-bundle-name:preference-key__
        for (int indexBegin = 0; (indexBegin = newText.indexOf("__PREF:", indexBegin)) >= 0;) {
            int indexColon = newText.indexOf(':', indexBegin + 7);
            int indexEnd = newText.indexOf("__", indexColon + 1);
            String pluginName = newText.substring(indexBegin + 7, indexColon);
            String prefKey = newText.substring(indexColon + 1, indexEnd);
            String prefValue;
            try {
                ScopedPreferenceStore pref = new ScopedPreferenceStore(new InstanceScope(), pluginName);
                prefValue = pref.getString(prefKey);
            } catch (Exception e) {
                // get pref value failed.
                prefValue = "";
            }
            newText = newText.substring(0, indexBegin) + prefValue + newText.substring(indexEnd + 2);
            indexBegin += prefValue.length();
        }
        return newText;
    }

    /**
     *
     * If a database componenet use an existing connection, its label may replace with variable from the existing
     * connection not the component variable. see bug 0005456: Label Format __DBNAME__ not valid when using existing
     * connection
     *
     * @param text String that contains variables which need to replace
     * @param variableMap A map contains variable name and IElementParameter in a pair
     * @return
     */
    public static String replaceWithExistingConnection(String text, Map<String, IElementParameter> variableMap) {
        if (text == null) {
            return ""; //$NON-NLS-1$
        }
        String newText = text;

        for (String var : variableMap.keySet()) {
            if (newText.contains(var)) {
                IElementParameter param = variableMap.get(var);
                String value = ElementParameterParser.getDisplayValue(param);
                newText = newText.replace(param.getVariableName(), value);
            }
        }
        return newText;

    }

    public static boolean checkIfContextExisted(String processId, String contextName) {
        ProcessItem selectedJob = ItemCacheManager.getProcessItem(processId);
        EList jobContexts = selectedJob.getProcess().getContext();
        for (Object c : jobContexts) {
            if (!(c instanceof ContextType)) {
                continue;
            }
            ContextType ct = (ContextType) c;
            if (contextName.equals(ct.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String getProcessSelectedContext(String processId) {
        ProcessItem currentRoute = ItemCacheManager.getProcessItem(processId);
        return currentRoute.getProcess().getDefaultContext();
    }

    @SuppressWarnings("unchecked")
    private static String getDisplayValue(final IElementParameter param) {
        Object value = param.getValue();

        if (value instanceof String) {

            if ("PROCESS_TYPE_VERSION".equals(param.getName()) && value.equals(RelationshipItemBuilder.LATEST_VERSION)) { //$NON-NLS-1$
                String jobId = (String) param.getParentParameter().getChildParameters().get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
                if (processItem == null) {
                    return ""; //$NON-NLS-1$
                }
                return processItem.getProperty().getVersion();
            } else if ("PROCESS_TYPE_CONTEXT".equals(param.getName())) { //$NON-NLS-1$
                String jobId = (String) param.getParentParameter().getChildParameters().get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
                if (processItem == null) {
                    return ""; //$NON-NLS-1$
                }
                // check if the selected context exists, if not, use the default
                // context of the job.
                boolean contextExists = false;
                for (Object object : processItem.getProcess().getContext()) {
                    if (object instanceof ContextType) {
                        if (((ContextType) object).getName() != null && ((ContextType) object).getName().equals(value)) {
                            contextExists = true;
                            continue;
                        }
                    }
                }
                if (!contextExists) {
                    return processItem.getProcess().getDefaultContext();
                }
                return (String) value;
            } else if ("SELECTED_JOB_NAME".equals(param.getName())) {
                String jobId = (String) param.getChildParameters().get("PROCESS_TYPE_PROCESS").getValue(); //$NON-NLS-1$
                ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
                if (processItem == null) {
                    return ""; //$NON-NLS-1$
                }
                return processItem.getProperty().getLabel();
            }
            // hywang add for 6484
            else if ("SELECTED_FILE".equals(param.getRepositoryValue())) { //$NON-NLS-1$
                IElementParameter propertyParam = param.getElement().getElementParameter("PROPERTY:REPOSITORY_PROPERTY_TYPE"); //$NON-NLS-1$
                if (propertyParam != null && propertyParam.getValue() != null && !propertyParam.getValue().equals("")) { //$NON-NLS-1$
                    try {
                        IRepositoryViewObject object = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory()
                                .getLastVersion((String) propertyParam.getValue());
                        if (object != null) {
                            Item item = object.getProperty().getItem();
                            String extension = null;

                            String rule = ""; //$NON-NLS-1$
                            String processLabelAndVersion = null;
                            if (item instanceof RulesItem) {
                                RulesItem rulesItem = (RulesItem) item;
                                extension = rulesItem.getExtension();
                                if (param.getElement() instanceof INode) {
                                    INode node = (INode) param.getElement();
                                    IProcess process = node.getProcess();
                                    String jobLabel = process.getName();
                                    String jobVersion = process.getVersion();
                                    processLabelAndVersion = JavaResourcesHelper.getJobFolderName(jobLabel, jobVersion);
                                }

                                rule = "rules/final/" + processLabelAndVersion + "/" + rulesItem.getProperty().getLabel() //$NON-NLS-1$ //$NON-NLS-2$
                                        + rulesItem.getProperty().getVersion() + extension;
                            }
                            return TalendQuoteUtils.addQuotes(rule);
                        } else {
                            return param.getValue().toString();
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }

                }
            } else if (param.getFieldType() == EParameterFieldType.TEXT_AREA) {
                return param.getValue().toString().replace("\n", " ").replace("\r", " ");
            }

            return (String) value;
        }
        if (param.getFieldType() == EParameterFieldType.RADIO || param.getFieldType() == EParameterFieldType.CHECK
                || param.getFieldType() == EParameterFieldType.AS400_CHECK) {
            if (value instanceof Boolean) {
                return ((Boolean) param.getValue()).toString();
            } else {
                return Boolean.FALSE.toString();
            }
        }

        if (param.getFieldType() == EParameterFieldType.TABLE) {
            List<Map<String, Object>> tableValues = (List<Map<String, Object>>) param.getValue();
            String[] items = param.getListItemsDisplayCodeName();
            String stringValues = "{"; //$NON-NLS-1$
            for (int i = 0; i < tableValues.size(); i++) {
                Map<String, Object> lineValues = tableValues.get(i);
                stringValues += "["; //$NON-NLS-1$
                for (int j = 0; j < items.length; j++) {

                    Object currentValue = lineValues.get(items[j]);
                    if (currentValue instanceof Integer) {
                        IElementParameter tmpParam = (IElementParameter) param.getListItemsValue()[j];
                        if (tmpParam.getListItemsDisplayName().length != 0) {
                            stringValues += tmpParam.getListItemsDisplayName()[(Integer) currentValue];
                        }
                    } else {
                        stringValues += currentValue;
                    }

                    if (j != (items.length - 1)) {
                        stringValues += ","; //$NON-NLS-1$
                    }
                }
                stringValues += "]"; //$NON-NLS-1$
                if (i != (tableValues.size() - 1)) {
                    stringValues += ","; //$NON-NLS-1$
                }
            }
            stringValues += "}"; //$NON-NLS-1$
            return stringValues;
        }

        return new String(""); //$NON-NLS-1$
    }

    /**
     * DOC qiang.zhang Comment method "getUNIQUENAME".
     *
     * @param node
     * @return
     */
    public static String getUNIQUENAME(NodeType node) {
        List<ElementParameterType> parameters = node.getElementParameter();
        for (ElementParameterType elementParam : parameters) {
            if (UNIQUE_NAME.equals(elementParam.getName())) {
                return elementParam.getValue();
            }
        }
        return ""; //$NON-NLS-1$
    }

    /**
     *
     * DOC YeXiaowei Comment method "isPerlProject".
     *
     * @return
     */
    public static boolean isPerlProject() {

        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        switch (language) {
        case PERL:
            return true;
        default: // PERL
            return false;
        }
    }

}
