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
package org.talend.core.model.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.UpdateContextVariablesHelper;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.types.ContextParameterJavaTypeManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.model.RepositoryConstants;

/**
 * Utilities to work with IContextParamet objects. <br/>
 * 
 * $Id: ContextParameterUtils.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public final class ContextParameterUtils {

    public static final String JAVA_NEW_CONTEXT_PREFIX = "context."; //$NON-NLS-1$

    private static final String PERL_STARTWITH = "$_context{"; //$NON-NLS-1$

    private static final String PERL_ENDWITH = "}"; //$NON-NLS-1$

    private static final String JAVA_STARTWITH = "((String)context.getProperty(\""; //$NON-NLS-1$

    private static final String JAVA_ENDWITH = "\"))"; //$NON-NLS-1$

    private static final String LINE = "_"; //$NON-NLS-1$

    private static final String EMPTY = ""; //$NON-NLS-1$

    private static final String NON_CONTEXT_PATTERN = "[^a-zA-Z0-9_]"; //$NON-NLS-1$

    /**
     * Constructs a new ContextParameterUtils.
     */
    private ContextParameterUtils() {
    }

    /**
     * Script code used to invoque a context parameter in a given language.
     * 
     * @param parameter Context parameter used in script.
     * @param language Language of the script.
     * @return Script code invoquing the context parameter.
     */
    public static String getScriptCode(IContextParameter parameter) {
        if (parameter == null) {
            return null;
        }
        String code = getScriptCode(parameter.getName(), parameter.getType());
        if (code == null) {
            return parameter.getName();
        }
        return code;
    }

    public static String getScriptCode(ContextParameterType parameter) {
        if (parameter == null) {
            return null;
        }
        String code = getScriptCode(parameter.getName(), parameter.getType());
        if (code == null) {
            return parameter.getName();
        }
        return code;
    }

    private static String getScriptCode(final String name, final String type) {
        if (name == null || type == null) {
            return null;
        }
        String code;

        final String string = JAVA_STARTWITH + name + JAVA_ENDWITH;
        JavaType javaType = ContextParameterJavaTypeManager.getJavaTypeFromId(type);
        String typeToGenerate = ContextParameterJavaTypeManager.getTypeToGenerate(type, true);
        if (javaType.isPrimitive()) {
            if (typeToGenerate.compareTo("String") == 0) { //$NON-NLS-1$
                code = string;
            } else if (typeToGenerate.compareTo("Integer") == 0) { //$NON-NLS-1$
                code = "Integer.parseInt(" + string + ")"; //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                code = typeToGenerate + ".parse" + typeToGenerate + "(" + string + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        } else if (typeToGenerate.compareTo("java.util.Date") == 0) { //$NON-NLS-1$

            code = "(" + typeToGenerate + ")" + "(new SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\")" + ".parse" + "(" + string //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    + "))"; //$NON-NLS-1$
        } else if (typeToGenerate.compareTo("java.lang.Object") == 0) { //$NON-NLS-1$
            code = "(" + typeToGenerate + ")" + string; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            code = "(" + typeToGenerate + ")" + string; //$NON-NLS-1$ //$NON-NLS-2$
        }
        return code;
    }

    public static String parseScriptContextCode(String code, IContextManager contextManager) {
        // final ECodeLanguage language = ((RepositoryContext)
        // CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
        // .getProject().getLanguage();
        // if (!isContainContextParam(code)) {
        // return code;
        // } else {
        // String paraName = getContextString(code, language);
        for (IContext context : contextManager.getListContext()) {
            code = parseScriptContextCode(code, context);
        }
        // for (IContextParameter param : context.getContextParameterList()) {
        // if (param.getName().equals(paraName)) {
        // // return code.replace(getScriptCode(param, language),
        // param.getValue());
        // return parseScriptContextCode(code.replace(getScriptCode(param,
        // language), param.getValue()),
        // contextManager);
        // }
        // }
        // }
        // }
        return code;
    }

    public static Date getDate(String s) {
        try {
            final Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s); //$NON-NLS-1$
            return parse;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String parseScriptContextCode(String code, IContext context) {
        if (code == null) {
            return null;
        }
        if (!isContainContextParam(code)) {
            return code;
        } else {
            String paraName = getContextString(code);
            IContextParameter param = context.getContextParameter(paraName);
            if (param != null) {
                String value = param.getValue();
                if (value == null || code.equals(value.trim())) {
                    return value;
                }
                return parseScriptContextCode(param.getValue(), context);// Multi-layer
                // context
                // refrence
            } else {
                return code;
            }

        }
    }

    private static String getContextString(String code) {
        if (code != null) {
            if (containOldContext(code)) {
                return code.substring(code.indexOf(JAVA_STARTWITH) + JAVA_STARTWITH.length(), code.indexOf(JAVA_ENDWITH));
            } else if (containNewContext(code)) {
                if (code.startsWith(JAVA_NEW_CONTEXT_PREFIX)) {
                    return code.substring(JAVA_NEW_CONTEXT_PREFIX.length());
                }
            }
        }
        return code;
    }

    public static boolean isContainContextParam(String code) {
        return containOldContext(code) || containNewContext(code);
    }

    /**
     * DOC qzhang Comment method "containoldContext".
     * 
     * @param code
     * @return
     */
    private static boolean containOldContext(String code) {
        return code != null
                && (code.contains(JAVA_STARTWITH.substring(0, JAVA_STARTWITH.length() - 1)) && code.contains(JAVA_ENDWITH));
    }

    /**
     * DOC qzhang Comment method "containoldContext".
     * 
     * @param code
     * @return
     */
    private static boolean containNewContext(String code) {
        return containContextVariables(code);
    }

    public static String getNewScriptCode(final String name) {
        return getNewScriptCode(name, ECodeLanguage.JAVA);
    }

    /**
     * 
     * ggu Comment method "getNewScriptCode".
     * 
     * example: context.var1 for java, $_context{var1} for perl.
     */
    public static String getNewScriptCode(final String name, ECodeLanguage language) {
        if (name == null) {
            return null;
        }
        return JAVA_NEW_CONTEXT_PREFIX + name;
    }

    /**
     * 
     * ggu Comment method "getVariableFromCode".
     * 
     * only for new script code and the first variables. and if there is no variable in code, return null.
     */
    public static String getVariableFromCode(String code) {
        return getVariableFromCode(code, JAVA_NEW_CONTEXT_PREFIX);
    }

    public static String getVariableFromCode(String code, String prefix) {
        if (code == null) {
            return null;
        }
        // if (isContainContextParam(code)) {
        String pattern = null;
        String varPattern = "(.+?)"; //$NON-NLS-1$
        String wordPattern = "\\b"; //$NON-NLS-1$
        pattern = wordPattern + replaceCharForRegex(prefix) + varPattern + wordPattern;
        if (pattern != null) {
            Pattern regex = Pattern.compile(pattern, Pattern.CANON_EQ);
            Matcher regexMatcher = regex.matcher(code);
            if (regexMatcher.find()) {
                try {
                    String var = regexMatcher.group(1);
                    if (var != null && ContextParameterUtils.isValidParameterName(var)) {
                        return var;
                    }
                } catch (RuntimeException re) {
                    // not match
                }
            }
        }
        // }
        return null;
    }

    private static String replaceCharForRegex(String pattern) {
        if (pattern == null) {
            return null;
        }
        pattern = pattern.replaceAll("\\(", "\\\\("); //$NON-NLS-1$ //$NON-NLS-2$
        pattern = pattern.replaceAll("\\)", "\\\\)"); //$NON-NLS-1$ //$NON-NLS-2$
        // for java
        pattern = pattern.replaceAll("\\.", "\\\\."); //$NON-NLS-1$ //$NON-NLS-2$
        // for perl
        pattern = pattern.replaceAll("\\{", "\\\\{"); //$NON-NLS-1$ //$NON-NLS-2$
        pattern = pattern.replaceAll("\\}", "\\\\}"); //$NON-NLS-1$ //$NON-NLS-2$
        // error??
        // pattern = pattern.replaceAll("\\$", "\\\\$"); //$NON-NLS-1$ //$NON-NLS-2$
        int index = pattern.indexOf("$"); //$NON-NLS-1$
        if (index > -1) { // found
            String str1 = pattern.substring(0, index);
            String str2 = pattern.substring(index + 1);
            pattern = str1 + "\\$" + str2; //$NON-NLS-1$

        }
        return pattern;
    }

    /**
     * 
     * ggu Comment method "containContextVariables".
     * 
     * check the string contain context, or not.
     */
    public static boolean containContextVariables(String str) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        String nonQuoteStr = TalendQuoteUtils.filterQuote(str);
        return getVariableFromCode(nonQuoteStr) != null;
    }


    public static boolean containCodeVariable(String str, String varPrefix) {
        if (str == null) {
            return false;
        }
        str = str.trim();
        String nonQuoteStr = TalendQuoteUtils.filterQuote(str);
        return getVariableFromCode(nonQuoteStr, varPrefix) != null;
    }

    /**
     * 
     * ggu Comment method "checkAndHideParameter".
     * 
     * hide the value. if the type is password
     */
    public static String checkAndHideValue(IContextParameter parameter) {
        if (parameter == null) {
            return null;
        }

        if (PasswordEncryptUtil.isPasswordType(parameter.getType())) {
            return PasswordEncryptUtil.getPasswordDisplay(parameter.getValue());
        } else {
            return parameter.getDisplayValue();
        }
    }

    /**
     * 
     * ggu Comment method "isPasswordType".
     * 
     * 
     */
    public static boolean isPasswordType(IContextParameter parameter) {
        if (parameter == null) {
            return false;
        }
        return PasswordEncryptUtil.isPasswordType(parameter.getType());
    }

    public static String updateValue(final String value, final String oldName, final String newName) {
        if (value == null || oldName == null || newName == null) {
            return value; // keep original value
        }

        PatternCompiler compiler = new Perl5Compiler();
        Perl5Matcher matcher = new Perl5Matcher();
        matcher.setMultiline(true);
        Perl5Substitution substitution = new Perl5Substitution(newName + "$2", //$NON-NLS-1$
                Perl5Substitution.INTERPOLATE_ALL);

        org.apache.oro.text.regex.Pattern pattern;
        try {
            pattern = compiler.compile("\\b(" //$NON-NLS-1$
                    + UpdateContextVariablesHelper.replaceSpecialChar(oldName) + ")(\\b|\\_)"); //$NON-NLS-1$
        } catch (MalformedPatternException e) {
            return value; // keep original value
        }

        if (matcher.contains(value, pattern)) {
            // replace
            String returnValue = Util.substitute(matcher, pattern, substitution, value, Util.SUBSTITUTE_ALL);
            return returnValue;

        }
        return value; // keep original value

    }

    public static String getOriginalValue(ContextType contextType, final String value) {
        if (value == null) {
            return EMPTY;
        }
        if (contextType != null && ContextParameterUtils.isContainContextParam(value)) {
            String var = ContextParameterUtils.getVariableFromCode(value);
            if (var != null) {
                ContextParameterType param = null;
                for (ContextParameterType paramType : (List<ContextParameterType>) contextType.getContextParameter()) {
                    if (paramType.getName().equals(var)) {
                        param = paramType;
                        break;
                    }
                }
                if (param != null) {
                    String value2 = param.getRawValue();
                    if (value2 != null) {
                        // return TalendTextUtils.removeQuotes(value2); //some value can't be removed for quote
                        return value2;
                    }
                }
                return EMPTY;
            }
        }
        return value;
    }

    /**
     * DOC ycbai Comment method "isContextParamOfContextType".
     * 
     * <p>
     * Estimate if the context parameter exists in the context type.
     * 
     * @param contextType
     * @param value
     * @return
     */
    public static boolean isContextParamOfContextType(ContextType contextType, String value) {
        if (contextType == null || value == null) {
            return false;
        }
        ContextParameterType param = null;
        if (ContextParameterUtils.isContainContextParam(value)) {
            String var = ContextParameterUtils.getVariableFromCode(value);
            if (var != null) {
                for (ContextParameterType paramType : (List<ContextParameterType>) contextType.getContextParameter()) {
                    if (var.equals(paramType.getName())) {
                        param = paramType;
                        break;
                    }
                }
            }
        }
        return param != null;
    }

    public static boolean isValidParameterName(String name) {
        if (name != null) {
            // for java, the var name not be named with java keywords.
            if (ContextUtils.isJavaKeyWords(name)) {
                return false;
            }
            return Pattern.matches(RepositoryConstants.CONTEXT_AND_VARIABLE_PATTERN, name);
        }
        return false;
    }

    public static boolean isEmptyParameter(String source) {
        return source.equals(StringUtils.EMPTY);
    }

    public static boolean isContextMode(Connection connection, String value) {
        if (connection == null || value == null) {
            return false;
        }
        if (connection.isContextMode() && ContextParameterUtils.isContainContextParam(value)) {
            return true;
        }
        return false;
    }

    /**
     * DOC ycbai Comment method "getValidParameterName".
     * 
     * <p>
     * Get a valid context parameter name which revise the invalid characters.
     * </p>
     * 
     * @param name
     * @return
     */
    public static String getValidParameterName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        if (isValidParameterName(name)) {
            return name;
        }
        String newName = name.replaceAll(NON_CONTEXT_PATTERN, LINE);
        if (ContextUtils.isJavaKeyWords(newName)) {
            newName = LINE + name;
        }
        return newName;
    }

}
