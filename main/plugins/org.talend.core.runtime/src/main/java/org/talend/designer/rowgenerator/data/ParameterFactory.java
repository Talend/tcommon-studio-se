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
package org.talend.designer.rowgenerator.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.talend.commons.exception.ExceptionHandler;

/**
 * class global comment. Detailled comment <br/>
 * 
 * $Id: ParameterFactory.java,v 1.6 2007/01/31 05:20:51 pub Exp $
 * 
 */
public class ParameterFactory {

    /**
     * 
     */
    private static final String PARAMETER_TYPE_REGEX = "(^[^\\s^\\(]+)(\\s|\\()"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_STRING = "string"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_INT = "int"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_LIST = "list"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_DOUBLE = "double"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_OBJECT = "object"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_BOOLEAN = "boolean"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_LONG = "long"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_DATE = "date"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_CHAR = "char"; //$NON-NLS-1$

    public static final String PARAMETER_TYPE_SHORT = "short"; //$NON-NLS-1$

    /**
     * s qzhang Comment method "getParameter". <br/>
     * .
     * 
     * format is: {param} <type/>[(<default value or closed list values/>)] <name/>[ : <comment/>]
     * 
     * @param string
     * @return
     */
    public Parameter getParameter(String string) {
        String type = getType(string);
        Parameter p = createParameter(type);
        if (p != null) {
            if (string.indexOf(type) != -1) {
                String startString = string.substring(0, string.indexOf(type));
                String endString = string.substring(string.indexOf(type) + type.length(), string.length());
                string = (startString + endString).trim();
            }
            if (string.endsWith(":")) { //$NON-NLS-1$
                string = string.substring(0, string.length() - 1);
            }
            String[] s = string.split(":"); //$NON-NLS-1$
            if (s != null && s.length > 1) {
                p.setComment(s[s.length - 1].trim());
                string = string.substring(0, string.length() - s[s.length - 1].length() - 1);
            }

            if (string.startsWith("(")) { //$NON-NLS-1$
                String defaultValue = string.replaceAll("(\\((.*)\\)).*", "$2"); //$NON-NLS-1$ //$NON-NLS-2$
                String temp = string.replaceAll("(\\((.*)\\)).*", "$1"); //$NON-NLS-1$ //$NON-NLS-2$

                setDefaultValue(p, defaultValue);
                string = string.substring(temp.length());
            }

            p.setName(string.trim());
        }
        return p;
    }

    /**
     * qzhang Comment method "setDefaultValue".
     * 
     * @param p
     * @param value
     */
    private void setDefaultValue(Parameter p, String value) {
        if (p instanceof ListParameter) {
            ListParameter lp = (ListParameter) p;
            String[] values = value.split(","); //$NON-NLS-1$
            lp.setValues(values);
            if (values.length > 0) {
                lp.setValue(values[0]);
            }
        } else {
            p.setValue(value);
        }

    }

    /**
     * qzhang Comment method "getType".
     * 
     * @param string
     * @return
     */
    private String getType(String string) {
        try {
            Pattern regex = Pattern.compile(PARAMETER_TYPE_REGEX, Pattern.CANON_EQ);
            Matcher matcher = regex.matcher(string);
            if (matcher.find()) {
                String s = matcher.group(1);
                return s;
            }
        } catch (PatternSyntaxException ex) {
            ExceptionHandler.process(ex);
        }
        return ""; //$NON-NLS-1$

    }

    private Parameter createParameter(String type) {
        Parameter parameter = null;
        if (type.toLowerCase().startsWith(PARAMETER_TYPE_STRING)) {
            parameter = new StringParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_INT)) {
            parameter = new IntParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_LIST)) {
            parameter = new ListParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_DOUBLE)) {
            parameter = new DoubleParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_OBJECT)) {
            parameter = new ObjectParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_BOOLEAN)) {
            parameter = new BooleanParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_LONG)) {
            parameter = new LongParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_DATE)) {
            parameter = new DateParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_CHAR)) {
            parameter = new CharParameter();
        } else if (type.equalsIgnoreCase(PARAMETER_TYPE_SHORT)) {
            parameter = new ShortParameter();
        } else {
            parameter = new ObjectParameter();
        }
        if (parameter != null) {
            parameter.setType(type);
        }
        return parameter;
    }
}
