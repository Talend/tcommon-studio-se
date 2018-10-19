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
package org.talend.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.talend.core.database.EDatabaseTypeName;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class TalendQuoteUtils {

    public static final String SINGLE_QUOTE = "'"; //$NON-NLS-1$

    public static final String ANTI_QUOTE = "`"; //$NON-NLS-1$

    public static final String QUOTATION_MARK = "\""; //$NON-NLS-1$

    public static final String QUOTATION_ESC_MARK = "\""; //$NON-NLS-1$

    public static final String LBRACKET = "["; //$NON-NLS-1$

    public static final String RBRACKET = "]"; //$NON-NLS-1$

    private static final String JAVA_DECLARE_STRING = "\""; //$NON-NLS-1$

    private static final String PERL_DECLARE_STRING = "'"; //$NON-NLS-1$

    private static final String JAVA_CONNECT_STRING = "+"; //$NON-NLS-1$

    private static final String PERL_CONNECT_STRING = "."; //$NON-NLS-1$

    /*
     * ((?<!\\)".*?(?<!\\)") or ((?<!\\)'.?(?<!\\)')
     */
    private static final String QUOTE_PATTERN = "((?<!\\\\)" + getQuoteChar() + ".*?(?<!\\\\)" + getQuoteChar() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    public static String getQuoteChar() {
        return isPerlProject() ? SINGLE_QUOTE : QUOTATION_MARK;
    }

    private static boolean isPerlProject() {
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        switch (language) {
        case JAVA:
            return false;
        default: // PERL
            return true;
        }
    }

    public static String getStringDeclare() {
        return isPerlProject() ? PERL_DECLARE_STRING : JAVA_DECLARE_STRING;
    }

    public static String getStringConnect() {
        return isPerlProject() ? PERL_CONNECT_STRING : JAVA_CONNECT_STRING;
    }

    public static String addQuotes(String text) {
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        switch (language) {
        case JAVA:
            return addQuotes(text, QUOTATION_MARK);
        default: // PERL
            return addQuotes(text, SINGLE_QUOTE);
        }
    }
    
    public static String addQuotesForComplexusString(String input){
        if (input == null) {
            return null;
        }
        
        int count = 0;
        for(int i = 0; i < input.length(); i++){
            if(QUOTATION_MARK.equals(String.valueOf(input.charAt(i)))){
                count++;
            }
        }
        if(count == 0){
            return addQuotesIfNotExist(input);
        }
        int a = count%2;
        if(a == 0){
            return input;
        }else{
            input = addPairQuotesIfNotExist(input);
        }
        return input;
    }

    public static String declareString(String input) {
        if (input == null) {
            return null;
        }
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        switch (language) {
        case JAVA:
            return JAVA_DECLARE_STRING + input + JAVA_DECLARE_STRING;
        default: // PERL
            return PERL_DECLARE_STRING + input + PERL_DECLARE_STRING;
        }

    }

    public static String addSingleQuotesIfNotExist(String text) {
        return addQuotesIfNotExist(text, SINGLE_QUOTE);
    }

    public static String addQuotesIfNotExist(String text) {
        return addQuotesIfNotExist(text, QUOTATION_MARK);
    }

    /**
     * DOC ycbai Comment method "addQuotesIfNotExist".
     * 
     * @param text
     * @return
     */
    public static String addQuotesIfNotExist(String text, String quote) {
        if (text == null) {
            return null;
        }
        if (!text.startsWith(quote)) {
            text = quote + text;
        }
        if (!text.endsWith(quote) || (quote != null && quote.equals(text))) {
            text = text + quote;
        }
        return text;
    }

    public static String addPairQuotesIfNotExist(String text) {
        return addPairQuotesIfNotExist(text, QUOTATION_MARK);
    }

    /**
     * DOC ycbai Comment method "addPairQuotesIfNotExist".
     * <p>
     * Add pair of quotes(begin and end of the text) if the text is not surrounded with them.
     * </p>
     * 
     * @param text
     * @param quote
     * @return
     */
    public static String addPairQuotesIfNotExist(String text, String quote) {
        String newText = text;
        if (newText == null || quote == null) {
            return null;
        }
        if (!(text.startsWith(quote) && text.endsWith(quote) && text.length() > 1)) {
            newText = addQuotes(newText);
        }
        return newText;
    }

    public static String addQuotes(String text, String quoteStyle) {
        String newString;

        if (quoteStyle.equals(SINGLE_QUOTE)) {
            newString = SINGLE_QUOTE + checkStringQuotes(text) + SINGLE_QUOTE;
        } else if (quoteStyle.equals(ANTI_QUOTE)) {
            newString = ANTI_QUOTE + checkStringQuotationMarks(text) + ANTI_QUOTE;
        } else if (quoteStyle.equals(LBRACKET) || quoteStyle.equals(RBRACKET)) {
            newString = LBRACKET + checkStringQuotationMarks(text) + RBRACKET;
        } else {
            newString = QUOTATION_MARK + checkStringQuotationMarks(text) + QUOTATION_MARK;
        }
        return newString;
    }

    public static String checkStringQuotes(String str) {
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        return str.replace("'", "\\'"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * For job script. it needs to transfer <code>\"</code> to <code>\\"</code>. Added by Marvin Wang on Dec 13, 2012.
     */
    public static String checkSlashQuotationMarks(String str) {
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        return str.replace("\\\"", "\\\\\""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static String checkSlashAndRemoveQuotation(String str) {
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        return str.replace("\\\"", "\""); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static String checkStringQuotationMarks(String str) {
        String newStr = str;
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        if (newStr.contains("\"")) { //$NON-NLS-1$
            newStr = newStr.replace("\"", "\\\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return newStr;
    }

    public static String removeQuotes(String text) {
        if (text == null) {
            return null;
        }
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        switch (language) {
        case JAVA:
            return removeQuotes(text, QUOTATION_MARK);
        default: // PERL
            return removeQuotes(text, SINGLE_QUOTE);
        }
    }

    public static String removeQuotesIfExist(String text) {
        if (text == null) {
            return null;
        }
        ECodeLanguage language = LanguageManager.getCurrentLanguage();

        switch (language) {
        case JAVA:
            if (text.startsWith(QUOTATION_MARK)) {
                return removeQuotes(text, QUOTATION_MARK);
            } else {
                return text;
            }
        default: // PERL
            if (text.startsWith(SINGLE_QUOTE)) {
                return removeQuotes(text, SINGLE_QUOTE);
            } else {
                return text;
            }
        }
    }

    public static String removeQuotes(String text, String quotation) {
        if (text == null) {
            return null;
        }
        if (text.length() > 1) {
            String substring = text.substring(0, 1);
            if (quotation.equals(substring)) {
                text = text.substring(1, text.length());
            }
            substring = text.substring(text.length() - 1, text.length());
            if (quotation.equals(substring)) {
                text = text.substring(0, text.length() - 1);
            }
        }
        return text;
    }

    /**
     * 
     * ggu Comment method "addQuotesForSQLString".
     * 
     * if simple is true, the text should not be the context or variables.
     */
    public static String addQuotesForSQLString(String text, String quoteStyle, boolean simple) {

        String con = getStringConnect();
        String newString;
        if (simple) {
            String declare = getStringDeclare();
            text = removeQuotes(text, declare);
        }

        if (quoteStyle.equals(SINGLE_QUOTE)) {
            if (simple) {
                newString = declareString(SINGLE_QUOTE + text + SINGLE_QUOTE);
            } else {
                newString = declareString(SINGLE_QUOTE) + con + text + con + declareString(SINGLE_QUOTE);
            }
        } else if (quoteStyle.equals(ANTI_QUOTE)) {
            if (simple) {
                newString = declareString(ANTI_QUOTE + text + ANTI_QUOTE);
            } else {
                newString = declareString(ANTI_QUOTE) + con + text + con + declareString(ANTI_QUOTE);
            }
        } else if (quoteStyle.equals(LBRACKET) || quoteStyle.equals(RBRACKET)) {
            if (simple) {
                newString = declareString(LBRACKET + text + RBRACKET);
            } else {
                newString = declareString(LBRACKET) + con + text + con + declareString(RBRACKET);
            }
        } else {
            /*
             * quote is specific
             */
            if (simple) {
                newString = declareString("\\" + QUOTATION_MARK + text + "\\" + QUOTATION_MARK); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                newString = declareString("\\" + QUOTATION_MARK) + con + text + con + declareString("\\" + QUOTATION_MARK); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return newString;
    }

    /**
     * 
     * ggu Comment method "filterQuote".
     * 
     * used for the string parsing, will ignore the char \" or \'.
     */
    public static String filterQuote(final String str) {
        String newStr = replaceNewLine(str);

        Matcher regexMatcher = quotePatternRegex.matcher(newStr);
        String nonQuoteStr = newStr;
        if (regexMatcher.find()) {
            String quoteStr = regexMatcher.group(1);
            int index = newStr.indexOf(quoteStr);
            nonQuoteStr = newStr.substring(0, index);
            nonQuoteStr += newStr.substring(index + quoteStr.length());
            return filterQuote(nonQuoteStr);

        }
        return nonQuoteStr;
    }

    private static Pattern quotePatternRegex = Pattern.compile(QUOTE_PATTERN, Pattern.CANON_EQ | Pattern.MULTILINE);

    /**
     * 
     * ggu Comment method "isCommonString".
     * 
     * if there are no any quotes , variables and expression(connected string) in string, will return true.
     * 
     */
    public static boolean isCommonString(final String str) {
        String newStr = replaceNewLine(str);

        Matcher regexMatcher = quotePatternRegex.matcher(newStr);
        if (regexMatcher.find()) { // has quote
            String non = filterQuote(newStr);
            if (!"".equals(non.trim())) { // has variables or is expression //$NON-NLS-1$
                return false;
            }
        }
        return true;
    }

    private static String replaceNewLine(final String str) {
        if (str == null) {
            return ""; //$NON-NLS-1$
        }
        String newStr = str;

        newStr = newStr.replace("\r", " "); //$NON-NLS-1$ //$NON-NLS-2$
        newStr = newStr.replace("\n", " "); //$NON-NLS-1$ //$NON-NLS-2$
        newStr = newStr.trim();

        return newStr;
    }

    private static boolean isLeft = true;

    public static String getQuoteByDBType(EDatabaseTypeName name) {

        switch (name) {
        case GODBC:
            return QUOTATION_MARK;
        case IBMDB2:
            return QUOTATION_MARK;
        case INGRES:
            return QUOTATION_MARK;
        case MSODBC:
            return QUOTATION_MARK;
        case MSSQL:
            return QUOTATION_MARK;
        case MYSQL:
        case AMAZON_AURORA:
            return ANTI_QUOTE;
        case ORACLEFORSID:
            return QUOTATION_MARK;
        case ORACLESN:
            return QUOTATION_MARK;
        case PSQL:
        case GREENPLUM:
        case PARACCEL:
        case PLUSPSQL:
            return QUOTATION_MARK;
        case SYBASEASE:
            return QUOTATION_MARK;
        case SYBASEIQ:
            return QUOTATION_MARK;
        case INTERBASE:
            return QUOTATION_MARK;
        case SQLITE:
            return QUOTATION_MARK;
        case FIREBIRD:
            return QUOTATION_MARK;
        case INFORMIX:
            return QUOTATION_MARK;
        case ACCESS:
            return getBracket();
        case TERADATA:
            return QUOTATION_MARK;
        case H2:
            return QUOTATION_ESC_MARK;
            // case JAVADB_DERBYCLIENT:
            // return QUOTATION_MARK;
            // case JAVADB_JCCJDBC:
            // return QUOTATION_MARK;
            // case JAVADB_EMBEDED:
            // return QUOTATION_MARK;
        default:
            return QUOTATION_MARK;
        }
    }

    /**
     * qzhang Comment method "getBracket".
     * 
     * @return
     */
    private static String getBracket() {
        if (isLeft) {
            isLeft = false;
            return LBRACKET;
        } else {
            isLeft = true;
            return RBRACKET;
        }
    }

    public static void setLeft(boolean left) {
        isLeft = left;
    }

    /**
     * 
     * ggu Comment method "checkAndRemoveQuotes".
     * 
     * if the query is common string and start with quote, will remove the quotes.
     */
    public static String checkAndRemoveQuotes(String query) {
        if (query == null) {
            query = ""; //$NON-NLS-1$
        }
        query = query.trim();
        if (query.startsWith(getQuoteChar()) && isCommonString(query)) {
            return removeQuotes(query);
        }
        return query; // keep it
    }

    /**
     * 
     * plv Comment method "checkAndAddBackslashes".
     * 
     * if str contains "\" ,will change "\" to "\\"
     */
    public static String checkAndAddBackslashes(String str) {
        if (StringUtils.isEmpty(str)) {
            return ""; //$NON-NLS-1$
        }
        str = str.replace("\\", "\\\\");//$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }

    /**
     * 
     * plv Comment method "checkAndRemoveBackslashes".
     * 
     * if str contains "\\" ,will change "\\" to "\"
     */
    public static String checkAndRemoveBackslashes(String str) {
        if (StringUtils.isEmpty(str)) {
            return ""; //$NON-NLS-1$
        }
        str = str.replace("\\\\", "\\");//$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }
}
