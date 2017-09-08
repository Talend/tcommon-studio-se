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
package org.talend.core.sqlbuilder.util;

import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.runtime.i18n.Messages;

/**
 * Text handling utility.
 * 
 * @author qzhang
 */
public class TextUtil {

    private static int num = 0;

    public static final int DEFAULT_WRAPLENGTH = 150;

    private static final String NEWLINE_SEPARATOR = "\n"; //$NON-NLS-1$

    private static final String NEWLINE_EXPR = "\\n"; //$NON-NLS-1$

    private static final String RETURN_EXPR = "\\r"; //$NON-NLS-1$

    private static final String TAB_EXPR = "\\t"; //$NON-NLS-1$

    private static String dialogTitle;

    public static final String KEY = "KEY"; //$NON-NLS-1$

    public static final String SQL_BUILDER_TITLE_COMP_PREFIX = "SQL Builder [Component Mode] - Job:"; //$NON-NLS-1$

    public static final String SQL_BUILDER_TITLE_COMP_NAME = " - Component:"; //$NON-NLS-1$

    public static final String SQL_BUILDER_TITLE_REP = "SQL Builder [Repository Mode]"; //$NON-NLS-1$

    public static final String SQL_BUILDER_TITLE_COMP_MODPREFIX = "SQL Builder - Component Mode - Job:"; //$NON-NLS-1$

    /**
     * qzhang TextUtil constructor comment.
     */
    public TextUtil() {
    }

    public static String getNewQueryLabel() {
        return Messages.getString("GenerateSelectSQLAction.NewQuery", num++); //$NON-NLS-1$
    }

    /**
     * Clear all linebreaks and carriage returns from input text.
     * 
     * @return cleaned string
     */
    public static String removeLineBreaks(String input) {
        if (input == null) {
            return null;
        }
        String tmp = input.replaceAll(NEWLINE_EXPR, " "); //$NON-NLS-1$
        tmp = tmp.replaceAll(TAB_EXPR, " "); //$NON-NLS-1$
        return tmp.replaceAll(RETURN_EXPR, ""); //$NON-NLS-1$
    }

    /**
     * Return the text reformatted to have a max charwidth of maxWidth.
     * 
     * @param maxWidth number of chars that the text can be wide.
     */
    public static String getWrappedText(String input) {
        return getWrappedText(input, DEFAULT_WRAPLENGTH);
    }

    /**
     * Return the text reformatted to have a max charwidth of maxWidth.
     * 
     * @param maxWidth number of chars that the text can be wide.
     */
    public static String getWrappedText(String input, int maxWidth) {

        if (input == null) {
            return ""; //$NON-NLS-1$
        }

        String[] text = input.split(NEWLINE_EXPR);
        String wrappedText = ""; //$NON-NLS-1$

        for (int i = 0; i < text.length; i++) {

            text[i] = text[i].replaceAll(RETURN_EXPR, ""); //$NON-NLS-1$

            if (text[i].length() == 0) {
                continue;
            }

            if (text[i].length() <= maxWidth) {
                wrappedText += text[i];

                if (i < text.length - 1) {
                    wrappedText += NEWLINE_SEPARATOR;
                }
            } else {

                String tmp = text[i];

                while (tmp.length() > maxWidth) {

                    for (int j = tmp.length() - 1; j >= 0; j--) {

                        if (j < maxWidth) {

                            char c = text[i].charAt(j);
                            if (c == ',') {
                                wrappedText += tmp.substring(0, j + 1);
                                wrappedText += NEWLINE_SEPARATOR;
                                tmp = tmp.substring(j + 1);
                                break;
                            }
                            if (c == ' ') {
                                wrappedText += tmp.substring(0, j + 1);
                                wrappedText += NEWLINE_SEPARATOR;
                                tmp = tmp.substring(j + 1);
                                break;
                            }
                        }

                        if (j == 0) {
                            wrappedText += tmp.substring(0, maxWidth + 1);
                            tmp = ""; //$NON-NLS-1$
                            break;
                        }
                    }

                }

                wrappedText += tmp;
                wrappedText += NEWLINE_SEPARATOR;
            }

        }

        return wrappedText;
    }

    /**
     * Replace all occurrences of replaceFrom in inputString with replaceTo.
     * 
     * @param inputString string to update
     * @param replaceFrom occurrences to replace
     * @param replaceTo string that replaces occurrences
     * @return
     */
    public static String replaceChar(String inputString, char replaceFrom, String replaceTo) {

        if (inputString == null || inputString.length() == 0) {
            return inputString;
        }

        StringBuffer buffer = new StringBuffer();
        char[] input = inputString.toCharArray();

        for (char element : input) {

            if (element == replaceFrom) {
                buffer.append(replaceTo);
            } else {
                buffer.append(element);
            }
        }

        return buffer.toString();
    }

    public static String addSqlQuots(String dbType, String sql, String schema) {
        if (isDoubleQuotesNeededDbType(dbType)) {
            if (schema != null && !"".equals(schema)) { //$NON-NLS-1$
                sql = "\"" + schema + "\".\"" + sql + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            } else {
                sql = "\"" + sql + "\""; //$NON-NLS-1$ //$NON-NLS-2$
            }

        } else {
            if (schema != null && !"".equals(schema)) { //$NON-NLS-1$
                sql = schema + "." + sql; //$NON-NLS-1$
            }
        }
        return sql;
    }

    public static String removeQuots(String query) {
        if (query == null) {
            return ""; //$NON-NLS-1$
        }
        if (ConnectionParameters.isJavaProject()) {
            if (query.startsWith("\"") && query.endsWith("\"") && query.length() > 1) { //$NON-NLS-1$ //$NON-NLS-2$
                return query.substring(1, query.length() - 1);
            } else {
                return query;
            }
        } else {
            if (query.startsWith("\'") && query.endsWith("\'") && query.length() > 1) { //$NON-NLS-1$ //$NON-NLS-2$
                return query.substring(1, query.length() - 1);
            } else {
                return query;
            }

        }
    }

    public static String getDialogTitle() {
        return dialogTitle;
    }

    public static void setDialogTitle(String dialogTitle) {
        TextUtil.dialogTitle = dialogTitle;
    }

    public static void setDialogTitle(String jobName, String nodeLabel, String uniqueName) {
        String title = ""; //$NON-NLS-1$
        if (nodeLabel == null || uniqueName == null) {
            title = SQL_BUILDER_TITLE_COMP_PREFIX + jobName;
        } else if (nodeLabel.indexOf("__UNIQUE_NAME__") != -1) { //$NON-NLS-1$
            title = SQL_BUILDER_TITLE_COMP_PREFIX + jobName;
            title += SQL_BUILDER_TITLE_COMP_NAME + uniqueName;
        } else {
            title = SQL_BUILDER_TITLE_COMP_MODPREFIX + jobName;
            title += SQL_BUILDER_TITLE_COMP_NAME + nodeLabel + "(" + uniqueName + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        dialogTitle = title;
    }

    /**
     * ftang Comment method "isDoubleQuotesNeededDbType".
     * 
     * @param dbType
     * @return
     */
    public static boolean isDoubleQuotesNeededDbType(String dbType) {
        boolean isPostgresql = dbType.equalsIgnoreCase(EDatabaseTypeName.PSQL.getXmlName());
        boolean isGreenplum = dbType.equalsIgnoreCase(EDatabaseTypeName.GREENPLUM.getXmlName());
        boolean isParaccel = dbType.equalsIgnoreCase(EDatabaseTypeName.PARACCEL.getXmlName());
        boolean isH2 = dbType.equalsIgnoreCase(EDatabaseTypeName.H2.getXmlName());
        return isPostgresql || isGreenplum || isParaccel || isH2;
    }

    /**
     * DOC zli Comment method "isOracleDbType".
     * 
     * @param dbType
     * @return
     */
    public static boolean isOracleDbType(String dbType) {
        boolean isOracle = dbType.equalsIgnoreCase(EDatabaseTypeName.ORACLEFORSID.getXmlName())
                || dbType.equalsIgnoreCase(EDatabaseTypeName.ORACLEFORSID.getDisplayName())
                || dbType.equalsIgnoreCase(EDatabaseTypeName.ORACLESN.getXmlName())
                || dbType.equalsIgnoreCase(EDatabaseTypeName.ORACLESN.getDisplayName())
                || dbType.equalsIgnoreCase(EDatabaseTypeName.ORACLE_OCI.getXmlName())
                || dbType.equalsIgnoreCase(EDatabaseTypeName.ORACLE_OCI.getDisplayName());

        return isOracle;
    }

    public static String calEscapeValue(String value) {
        if (value == null) {
            return null;
        }
        if (value.contains(NEWLINE_EXPR)) {
            value = value.replace(NEWLINE_EXPR, NEWLINE_SEPARATOR);
        }
        if (value.contains(RETURN_EXPR)) {
            value = value.replace(RETURN_EXPR, "\r");
        }
        if (value.contains(TAB_EXPR)) {
            value = value.replace(TAB_EXPR, "\t");
        }
        return value;
    }
}
