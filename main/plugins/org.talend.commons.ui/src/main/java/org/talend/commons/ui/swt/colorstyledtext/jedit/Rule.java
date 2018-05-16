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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.colorstyledtext.scanner.ColoringScanner;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: Rule.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class Rule {

    protected String name;

    protected Map types;

    protected KeywordMap keywords;

    protected List orderdList;

    protected Mode mode;

    protected char escape;

    protected boolean highlightDigits = false;

    protected boolean ignoreCase = true;

    protected String digitRE;

    protected String defaultTokenType = ColoringScanner.NULL;

    public static final String DEFAULT_NAME = "MAIN"; //$NON-NLS-1$

    private static final KeywordMap EMPTY_MAP = new KeywordMap(false);

    public static Rule newRule(Mode mode, String name, boolean highlightDigits, boolean ignoreCase, String digitRE, char escape,
            String defaultTokenType) {

        Rule newRule = new Rule(name);
        newRule.mode = mode;
        newRule.highlightDigits = highlightDigits;
        newRule.ignoreCase = ignoreCase;
        newRule.digitRE = digitRE;
        newRule.escape = escape;
        newRule.defaultTokenType = defaultTokenType;
        return newRule;
    }

    public Rule(String name) {
        super();
        types = new HashMap();
        orderdList = new ArrayList();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public void add(Type type) {
        orderdList.add(type);
        List all = (List) types.get(type.getColor());
        if (all == null) {
            all = new ArrayList();
            types.put(type.getColor(), all);
        }
        all.add(type);
    }

    public List get(String type) {
        List all = (List) types.get(type);
        if (all == null) {
            return Collections.EMPTY_LIST;
        }
        return all;
    }

    public String toString() {
        return Messages.getString("Rule.rule", name); //$NON-NLS-1$
    }

    public KeywordMap getKeywords() {
        if (keywords == null) {
            return EMPTY_MAP;
        }
        return keywords;
    }

    public List getTypes() {
        return orderdList;
    }

    public void setKeywords(KeywordMap keywordMap) {
        this.keywords = keywordMap;
    }

    public String getDefaultTokenType() {
        return defaultTokenType;
    }

    public char getEscape() {
        return escape;
    }

    public boolean getIgnoreCase() {
        return ignoreCase;
    }

    public boolean getHighlightDigits() {
        return highlightDigits;
    }

    public Mode getMode() {
        return mode;
    }

}
