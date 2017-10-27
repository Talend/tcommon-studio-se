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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Util;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC xye class global comment. Detailled comment
 */
public class SQLFormatUtil {

    private static final String LINE_SEP = System.getProperty("line.separator"); //$NON-NLS-1$

    private static final String SPACE = " "; //$NON-NLS-1$

    private static final Collection<String> KEYWORDS = new ArrayList<String>();

    public static void main(String[] args) {
        // p(new SQLFormatUtil().formatSQL(sql));
    }

    public String formatSQL(final String statement) throws Exception { // Maybe beed contextIsUsed
        // parameter
        String temp = beforeFormat(statement);
        // Scan Text and modify the text

        // init keys
        Collection<String> keys = findSQLKeyWords();
        KEYWORDS.clear();
        KEYWORDS.addAll(keys);

        Collection<String> words = new ArrayList<String>();
        try {
            Collection<String> organs = separate(temp, generateRegexByKeyWords(KEYWORDS));
            Collection<String> molecule = spillByRegex(organs, "\\s{1,}"); // spill by space //$NON-NLS-1$
            for (String str : molecule) {
                words.addAll(separate(str, ",")); // spill by space; //$NON-NLS-1$
            }
        } catch (MalformedPatternException e) {
            ExceptionHandler.process(e);
            return statement;
        }

        StringBuilder builder = new StringBuilder();

        String p = null;

        // find longest keyword to deduce the offset for every new line
        int longestKey = 0;
        for (String str : words) {
            if (isKeyWords(str)) {
                int length = str.length();
                if (length > longestKey) {
                    longestKey = length;
                }
            }
        }

        // assemble sql line
        for (String current : words) {
            if (needAppendToNewLine(current, p)) {
                builder.append(LINE_SEP);
                // Fill pachler
                if (!isKeyWords(current)) {
                    builder.append(lengthSpaces(longestKey));
                }
                builder.append(current);

            } else {
                if (isKeyWords(p)) {
                    builder.append(lengthSpaces(longestKey - p.length()));
                }
                builder.append(current);
            }

            builder.append(SPACE);

            p = current;

        }
        // bug 6103
        return afterFormat(builder.toString()).trim();
    }

    private String beforeFormat(final String sql) {
        if (sql == null) {
            return null;
        }
        // String trimSql = TalendTextUtils.trimParameter(sql);
        // Remove all multi-space
        String temp = sql.replaceAll("\\s{2,}", SPACE); //$NON-NLS-1$
        return temp;
    }

    private String afterFormat(final String sql) {
        if (sql == null) {
            return null;
        }
        String temp = sql.replaceAll("\\s{1,},", ","); // Remove blank before the , //$NON-NLS-1$ //$NON-NLS-2$
        return temp;
    }

    private boolean needAppendToNewLine(String cur, String pre) {
        if (pre == null) {
            return false;
        }

        if (cur != null && isKeyWords(cur)) {
            return true;
        }

        if (pre.endsWith(",")) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    private boolean isKeyWords(final String str) {
        if (str == null) {
            return false;
        }
        String temp = str;
        if (str.startsWith("\"")) { //$NON-NLS-1$
            temp = TalendTextUtils.removeQuotes(str);
        }

        return KEYWORDS.contains(temp.toUpperCase());
    }

    private String lengthSpaces(int length) {
        if (length <= 0) {
            return ""; //$NON-NLS-1$
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(SPACE);
        }
        return builder.toString();
    }

    private String generateRegexByKeyWords(Collection<String> keys) {
        StringBuilder builder = new StringBuilder();

        for (String key : keys) {
            builder.append("\b[\"]*"); //$NON-NLS-1$
            builder.append(key);
            builder.append("\b"); //$NON-NLS-1$
            builder.append("|"); //$NON-NLS-1$
        }

        String res = builder.toString();
        return res.substring(0, res.length() - 1);
    }

    private Collection<String> findSQLKeyWords() {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            document = saxReader.read(CommonsPlugin.class.getClassLoader().getResourceAsStream("/modes/tsql.xml")); //$NON-NLS-1$
            List list = document.selectNodes("//MODE/RULES/KEYWORDS/KEYWORD1"); //$NON-NLS-1$
            Iterator iter = list.iterator();
            Collection<String> res = new HashSet<String>();
            while (iter.hasNext()) {
                Element element = (Element) iter.next();
                String key = element.getTextTrim();
                if (key != null && !key.equals("")) { //$NON-NLS-1$
                    res.add(key.toUpperCase());
                }
            }

            return res;

        } catch (DocumentException e) {
            ExceptionHandler.process(e);
            return null;
        }

    }

    /**
     * 
     * @param regex
     * @return
     * @throws MalformedPatternException
     */
    public Collection<String> separate(final String source, final String regex) throws MalformedPatternException {
        if (source == null) {
            return null;
        }

        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regex);

        PatternMatcher matcher = new Perl5Matcher();
        PatternMatcherInput input = new PatternMatcherInput(source);

        Collection<String> results = new ArrayList<String>();
        int beginOffset = 0;
        while (matcher.contains(input, pattern)) {
            MatchResult currentResult = matcher.getMatch();
            if (currentResult.beginOffset(0) > beginOffset) {
                results.add(source.substring(beginOffset, currentResult.beginOffset(0)));
            }

            results.add(currentResult.group(0));
            beginOffset = currentResult.endOffset(0);
        }

        results.add(source.substring(beginOffset, source.length()));

        // Use split will remove this matched part
        // Util.split(results, matcher, pattern, source);

        return results;
    }

    public Collection<String> spillByRegex(Collection<String> source, final String regex) throws MalformedPatternException {
        if (source == null || source.isEmpty()) {
            return source;
        }

        Collection<String> results = new ArrayList<String>();

        PatternCompiler compiler = new Perl5Compiler();
        Pattern pattern = compiler.compile(regex, Perl5Compiler.MULTILINE_MASK);
        PatternMatcher matcher = new Perl5Matcher();
        for (String str : source) {
            Collection<String> temp = new ArrayList<String>();
            Util.split(temp, matcher, pattern, str);
            results.addAll(temp);
        }

        return results;
    }
}
