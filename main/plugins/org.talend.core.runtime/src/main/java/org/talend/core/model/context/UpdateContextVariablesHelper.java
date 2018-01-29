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
package org.talend.core.model.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ConnectionType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;

/**
 * ggu class global comment. Detailled comment.
 */
public final class UpdateContextVariablesHelper {

    /**
     * 
     * ggu Comment method "updateProcess".
     * 
     * update parameters for the current opened job .
     */
    public static boolean updateProcessForRenamed(IProcess process, final String oldVarName, final String newVarName) {
        if (process == null || oldVarName == null || newVarName == null) {
            return false;
        }
        Map<String, String> renamedMap = new HashMap<String, String>();
        renamedMap.put(newVarName, oldVarName);

        return updateProcessForRenamed(process, renamedMap);
    }

    /**
     * renamedMap must be mapped from new to old.
     */
    @SuppressWarnings("unchecked")
    public static boolean updateProcessForRenamed(IProcess process, final Map<String, String> renamedMap) {
        if (process == null || renamedMap == null || renamedMap.isEmpty()) {
            return false;
        }
        // old to new map
        Map<String, String> varScriptCodeMap = retrieveRenamedScriptCodeMap(renamedMap);
        if (varScriptCodeMap.isEmpty()) {
            return false;
        }
        boolean changed = false;

        // update main parameters
        changed = updateElementParameters((List<IElementParameter>) process.getElementParametersWithChildrens(), varScriptCodeMap);

        // update nodes parameters.
        for (INode node : (List<INode>) process.getGraphicalNodes()) {
            for (String oldScriptCode : varScriptCodeMap.keySet()) {
                String newScriptCode = varScriptCodeMap.get(oldScriptCode);
                if (newScriptCode != null) {
                    node.renameData(oldScriptCode, newScriptCode);
                    changed = true;
                }
            }
            // update links parameters(bug 3993)
            for (IConnection conn : node.getOutgoingConnections()) {
                changed |= updateElementParameters((List<IElementParameter>) conn.getElementParametersWithChildrens(),
                        varScriptCodeMap);
            }
        }

        return changed;
    }

    /**
     * 
     * ggu Comment method "updateProcess".
     * 
     * update paramters for the job item file.
     * 
     * renamedMap must be mapped from new to old.
     */
    public static boolean updateProcessForRenamed(ProcessType processType, final Map<String, String> renamedMap) {
        if (processType == null || renamedMap == null || renamedMap.isEmpty()) {
            return false;
        }
        Map<String, String> renamedScriptCodeMap = retrieveRenamedScriptCodeMap(renamedMap);
        if (renamedScriptCodeMap.isEmpty()) {
            return false;
        }
        return updateProcess(processType, renamedScriptCodeMap, false);
    }

    @SuppressWarnings("unchecked")
    public static boolean updateProcessForReplacedOldScriptCode(ProcessType processType) {
        if (processType == null) {
            return false;
        }
        // get old syntax map of the context variables.
        Map<String, String> replacedScriptCodeMap = retrieveReplacedScriptCodeMap(processType.getContext(),
                processType.getDefaultContext());
        if (replacedScriptCodeMap.isEmpty()) {
            return false;
        }
        return updateProcess(processType, replacedScriptCodeMap, true);

    }

    @SuppressWarnings("unchecked")
    private static boolean updateProcess(ProcessType processType, final Map<String, String> varScriptCodeMap, boolean oldSyntax) {
        if (processType == null || varScriptCodeMap == null || varScriptCodeMap.isEmpty()) {
            return false;
        }

        boolean changed = false;
        // update process parameter
        changed = updateElementParameter(processType.getParameters().getElementParameter(), varScriptCodeMap, oldSyntax);
        // update nodes parameter
        for (NodeType node : (List<NodeType>) processType.getNode()) {
            // update parameter
            changed |= updateElementParameter(node.getElementParameter(), varScriptCodeMap, oldSyntax);

            // update extend node data
            String strdata = node.getStringData();
            if (strdata != null) {
                String newData = replaceQuotStringData(strdata, varScriptCodeMap, oldSyntax);
                if (newData != null && !newData.equals(strdata)) {
                    node.setStringData(newData);
                    changed = true;
                }
            }
        }
        // update links parameters(bug 3993)
        for (ConnectionType conn : (List<ConnectionType>) processType.getConnection()) {
            // update parameter
            changed |= updateElementParameter(conn.getElementParameter(), varScriptCodeMap, oldSyntax);
        }
        return changed;
    }

    private static boolean updateElementParameter(List<ElementParameterType> eleParameterList,
            final Map<String, String> varScriptCodeMap, boolean oldSyntax) {
        if (eleParameterList == null || eleParameterList.isEmpty() || varScriptCodeMap == null || varScriptCodeMap.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (ElementParameterType eleParameterType : eleParameterList) {
            String oldValue = eleParameterType.getValue();
            if (oldValue != null) {
                String newValue = hasAndReplaceValue(oldValue, varScriptCodeMap, oldSyntax);

                if (newValue != null && !oldValue.equals(newValue)) {
                    eleParameterType.setValue(newValue);
                    changed = true;
                }
            }
        }
        return changed;
    }

    private static String hasAndReplaceValue(final String value, final Map<String, String> varScriptCodeMap, boolean oldSyntax) {
        if (value == null || varScriptCodeMap == null || varScriptCodeMap.isEmpty()) {
            return value; // keep original value
        }

        String returnValue = value;
        for (String oldScriptCode : varScriptCodeMap.keySet()) {
            String contextNameFullName = varScriptCodeMap.get(oldScriptCode);

            returnValue = hasAndReplaceValue(returnValue, replaceSpecialChar(oldScriptCode), varScriptCodeMap.get(oldScriptCode),
                    oldSyntax);
            if (LanguageManager.getCurrentLanguage() == ECodeLanguage.JAVA) {
                // add this for bug 3455
                returnValue = migrateContextPropertySetter(returnValue, contextNameFullName.replaceAll("context\\.", ""), false); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return returnValue;
    }

    private static String migrateContextPropertySetter(String fullContent, String varName, boolean isExtension) {
        String beginString = "context.setProperty("; //$NON-NLS-1$

        if (!fullContent.contains(beginString)) {
            return fullContent;
        }

        String regex = beginString + "\"" + varName + "\","; //$NON-NLS-1$ //$NON-NLS-2$
        regex = replaceSpecialChar(regex);
        if (isExtension) {
            regex = regex.replaceAll("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        regex = "(.*?)(" + regex + ")(.*?)\\);"; //$NON-NLS-1$ //$NON-NLS-2$

        String out = ContextParameterUtils.JAVA_NEW_CONTEXT_PREFIX + varName + "="; //$NON-NLS-1$
        out = "$1" + out + "$3;"; //$NON-NLS-1$ //$NON-NLS-2$

        return fullContent.replaceAll(regex, out);
    }

    private static String hasAndReplaceValue(final String value, final String oldScriptCode, final String newScriptCode,
            boolean oldSyntax) {
        if (value == null || oldScriptCode == null || newScriptCode == null) {
            return value; // keep original value
        }
        PatternCompiler compiler = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        matcher.setMultiline(true);

        // old syntax script code.
        String prefix = "\\b("; //$NON-NLS-1$
        String suffix = ")\\b"; //$NON-NLS-1$
        if (oldSyntax) {
            prefix = "("; //$NON-NLS-1$
            suffix = ")"; //$NON-NLS-1$
        }
        String returnValue = value;

        Pattern pattern;
        try {
            pattern = compiler.compile(prefix + oldScriptCode + suffix);
        } catch (MalformedPatternException e) {
            return value; // keep original value
        }

        if (matcher.contains(value, pattern)) {
            // replace
            Perl5Substitution substitution = new Perl5Substitution(newScriptCode, Perl5Substitution.INTERPOLATE_ALL);
            returnValue = Util.substitute(matcher, pattern, substitution, value, Util.SUBSTITUTE_ALL);
        }

        return returnValue;
    }

    /**
     * 
     * ggu Comment method "retrieveRenamedScriptCodeMap".
     * 
     * retrieve the renamed variables script code map.
     */
    private static Map<String, String> retrieveRenamedScriptCodeMap(final Map<String, String> renamedMap) {
        if (renamedMap == null || renamedMap.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> scriptCodeMap = new HashMap<String, String>();
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        for (String newName : renamedMap.keySet()) {
            String oldName = renamedMap.get(newName);

            String oldScriptCode = ContextParameterUtils.getNewScriptCode(oldName, language);
            String newScriptCode = ContextParameterUtils.getNewScriptCode(newName, language);
            if (oldScriptCode == null || newScriptCode == null) {
                continue;
            }
            scriptCodeMap.put(oldScriptCode, newScriptCode);

        }

        for (String newName : renamedMap.keySet()) {
            String oldName = renamedMap.get(newName);

            String oldScriptCode = ContextParameterUtils.getNewScriptCode(oldName, language);
            String newScriptCode = ContextParameterUtils.getNewScriptCode(newName, language);
            if (oldScriptCode == null || newScriptCode == null) {
                continue;
            }
            scriptCodeMap.put(oldScriptCode, newScriptCode);

        }
        return scriptCodeMap;
    }

    /**
     * 
     * ggu Comment method "retrieveReplacedScriptCodeMap".
     * 
     * retrieve the new and old variables script code map.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, String> retrieveReplacedScriptCodeMap(List<ContextType> contextsList, final String contextName) {

        if (contextsList == null || contextsList.isEmpty() || contextName == null) {
            return Collections.emptyMap();
        }

        ContextType contextType = ContextUtils.getContextTypeByName(contextsList, contextName);
        if (contextType == null) {
            return Collections.emptyMap();
        }

        Map<String, String> varsScriptCodeMap = new HashMap<String, String>();
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        for (ContextParameterType parameter : (List<ContextParameterType>) contextType.getContextParameter()) {
            String oldCode = ContextParameterUtils.getScriptCode(parameter);
            String newCode = ContextParameterUtils.getNewScriptCode(parameter.getName(), language);
            varsScriptCodeMap.put(oldCode, newCode);
        }
        return varsScriptCodeMap;
    }

    private static String replaceQuotStringData(String data, final Map<String, String> varScriptCodeMap, boolean oldSyntax) {
        Map<String, String> varScriptCodeMapExt = new HashMap<String, String>();

        for (String oldScriptCode : varScriptCodeMap.keySet()) {
            String newScriptCode = varScriptCodeMap.get(oldScriptCode);
            if (newScriptCode == null) {
                continue;
            }
            oldScriptCode = oldScriptCode.replaceAll("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$
            varScriptCodeMapExt.put(oldScriptCode, newScriptCode);

            data = migrateContextPropertySetter(data, newScriptCode.replaceAll("context\\.", ""), true); //$NON-NLS-1$ //$NON-NLS-2$

        }
        return hasAndReplaceValue(data, varScriptCodeMapExt, oldSyntax);

    }

    public static String replaceSpecialChar(String expression) {
        if (expression == null) {
            return null;
        }
        expression = expression.replaceAll("\\(", "\\\\("); //$NON-NLS-1$ //$NON-NLS-2$
        expression = expression.replaceAll("\\)", "\\\\)"); //$NON-NLS-1$ //$NON-NLS-2$
        expression = expression.replaceAll("\\.", "\\\\."); //$NON-NLS-1$ //$NON-NLS-2$
        return expression;

    }

    private static boolean updateElementParameters(List<IElementParameter> parameters, Map<String, String> varScriptCodeMap) {
        if (parameters == null || parameters.isEmpty() || varScriptCodeMap == null || varScriptCodeMap.isEmpty()) {
            return false;
        }
        boolean changed = false;
        for (IElementParameter parameter : parameters) {
            Object obj = parameter.getValue();
            if (obj != null && obj instanceof String) {
                String value = (String) obj;
                String newValue = hasAndReplaceValue(value, varScriptCodeMap, false);
                if (newValue != null && !value.equals(newValue)) {
                    parameter.setValue(newValue);
                    changed = true;
                }

            }
        }
        return changed;
    }
}
