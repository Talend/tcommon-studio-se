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
package org.talend.designer.rowgenerator.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.talend.commons.exception.ExceptionHandler;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend-code-templates.xml 1 2007-3-28 下午02:42:59 (星期五, 29 九月 2006) qzhang $
 * 
 */
public abstract class AbstractFunctionParser {

    protected List<TalendType> list = new ArrayList<TalendType>();

    public static final String FUNCTION_PARAMETERS_REGEX = "\\{param\\}(\\s)*(.*)"; //$NON-NLS-1$

    public static final String FUNCTION_NAME_REGEX = "sub(\\s)*(.*)"; //$NON-NLS-1$

    public static final String FUNCTION_TYPE_REGEX = "\\{talendTypes\\}(\\s)*(.*)"; //$NON-NLS-1$

    public static final String CATEGORYREGEX = "\\{Category\\}(\\s)*(.*)"; //$NON-NLS-1$

    public static final String EMPTY_STRING = ""; //$NON-NLS-1$

    public static final String FUNCTION_DESCRIPTION_REGEX = "##(\\s)*((\\r)*\\n)#(\\s)*(.*)"; //$NON-NLS-1$

    public static final String FUNCTION_REGEX = "##(\\s)*(.*(\\r)*\\n)+?(sub)(\\s)+([^\\s]+)"; //$NON-NLS-1$

    public abstract void parse();

    /**
     * Getter for list.
     * 
     * @return the list
     */
    public List<TalendType> getList() {
        return this.list;
    }

    /**
     * qzhang Comment method "parse description of the function".
     * 
     * @param string
     */
    protected String parseDescription(String string) {
        try {
            Pattern regex = Pattern.compile(FUNCTION_DESCRIPTION_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(5);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return EMPTY_STRING;
    }

    /**
     * qzhang Comment method "parseFunction".
     * 
     * @param string
     * @return
     */
    protected String parseFunctionType(String string) {
        try {
            Pattern regex = Pattern.compile(FUNCTION_TYPE_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(2);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return EMPTY_STRING;
    }

    /**
     * parse the input file for the category.
     * 
     * @param string
     * @return
     */
    protected String parseCategoryType(String string) {
        try {
            Pattern regex = Pattern.compile(CATEGORYREGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(2);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return EMPTY_STRING;
    }

    /**
     * qzhang Comment method "parseFunctionParameters".
     * 
     * @param string
     * @return
     */
    protected String[] parseFunctionParameters(String string) {
        List<String> list1 = new ArrayList<String>();
        try {
            Pattern regex = Pattern.compile(FUNCTION_PARAMETERS_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);

            while (matcher.find()) {
                String s = matcher.group(2);
                list1.add(s);
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return list1.toArray(new String[list1.size()]);
    }

    /**
     * qzhang Comment method "convertToParameter".
     * 
     * @param parameter
     * @return
     */
    protected Parameter[] convertToParameter(String[] p) {
        ParameterFactory pf = new ParameterFactory();
        Parameter[] parameters = new Parameter[p.length];
        for (int i = 0; i < p.length; i++) {
            parameters[i] = pf.getParameter(p[i]);
        }
        return parameters;
    }

    /**
     * Gets the TalendType that already created, if the wanted one is one existent ,create one.
     * 
     * @param name
     * @return
     */
    protected TalendType getTalendType(String name) {
        for (TalendType type : list) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        TalendType type = new TalendType();
        type.setName(name);
        list.add(type);

        return type;
    }

}
