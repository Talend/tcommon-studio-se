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
package org.talend.commons.ui.swt.colorstyledtext.scanner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.NumberRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.talend.commons.ui.swt.colorstyledtext.ColorManager;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mode;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Modes;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Rule;
import org.talend.commons.ui.swt.colorstyledtext.jedit.TextSequence;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Type;
import org.talend.commons.ui.swt.colorstyledtext.rules.CToken;
import org.talend.commons.ui.swt.colorstyledtext.rules.CasedWordRule;
import org.talend.commons.ui.swt.colorstyledtext.rules.ColoringWordDetector;
import org.talend.commons.ui.swt.colorstyledtext.rules.ITokenFactory;
import org.talend.commons.ui.utils.data.text.rules.StringRuleBasedScanner;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ColoringScanner.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ColoringScanner extends StringRuleBasedScanner {

    public static final String NULL = "NULL"; //$NON-NLS-1$

    public static final String COMMENT1 = "COMMENT1"; //$NON-NLS-1$

    public static final String COMMENT2 = "COMMENT2"; //$NON-NLS-1$

    public static final String LITERAL1 = "LITERAL1"; //$NON-NLS-1$

    public static final String LITERAL2 = "LITERAL2"; //$NON-NLS-1$

    public static final String LABEL = "LABEL"; //$NON-NLS-1$

    public static final String KEYWORD1 = "KEYWORD1"; //$NON-NLS-1$

    public static final String KEYWORD2 = "KEYWORD2"; //$NON-NLS-1$

    public static final String KEYWORD3 = "KEYWORD3"; //$NON-NLS-1$

    public static final String FUNCTION = "FUNCTION"; //$NON-NLS-1$

    public static final String MARKUP = "MARKUP"; //$NON-NLS-1$

    public static final String OPERATOR = "OPERATOR"; //$NON-NLS-1$

    public static final String DIGIT = "DIGIT"; //$NON-NLS-1$

    public static final String INVALID = "INVALID"; //$NON-NLS-1$

    protected Mode mode;

    protected ColorManager colorManager;

    protected int markLength = -1;

    protected char escape;

    /**
     * DOC nrousseau ColoringPartitionScanner class global comment. Detailled comment <br/>
     * 
     * $Id: ColoringScanner.java 7038 2007-11-15 14:05:48Z plegall $
     * 
     */
    class LitePartitionScanner extends RuleBasedPartitionScanner {

        public int getOffset() {
            return fOffset;
        }

        public void setTokenOffset(int off) {
            fTokenOffset = off;
        }

        public void setOffset(int off) {
            fOffset = off;
        }
    }

    /**
     * This will setup the rules for elements *not* defined as children of the keywords elements. JEdit allows the
     * "type" of an element to appear inside the keywords element or outside. For example you could have the LITERAL2
     * tag inside the keywords element and as a top level element.
     */
    public ColoringScanner(String filename, ColorManager colorManager) {
        this(Modes.getModeFor(filename), colorManager);
    }

    public ColoringScanner(Mode mode, ColorManager colorManager) {
        super();
        this.colorManager = colorManager;
        this.mode = mode;
        escape = mode.getDefaultRuleSet().getEscape();
        setRules(createRuleSet(mode.getDefaultRuleSet()));
    }

    private IRule[] createRuleSet(Rule rule) {
        List<IRule> rules = new ArrayList<IRule>();
        IToken defaultToken = newToken(ColorManager.NULL_COLOR);

        ColoringEditorTools.add(rule, rules, new ITokenFactory() {

            public IToken makeToken(Type type) {
                return new CToken(type);
            }
        });
        addTextSequenceRules(rule, rules, defaultToken);
        return (IRule[]) rules.toArray(new IRule[rules.size()]);
    }

    private void addTextSequenceRules(Rule ruleSet, List<IRule> rules, IToken defaultToken) {
        ColoringWordDetector wordDetector = new ColoringWordDetector();
        if (ruleSet.getHighlightDigits()) {
            rules.add(new NumberRule(newToken(ColorManager.DIGIT_COLOR)));
        }
        CasedWordRule wordRule = new CasedWordRule(wordDetector, defaultToken, ruleSet.getKeywords().ignoreCase());

        addKeywordRule(ruleSet, "COMMENT1", ColorManager.COMMENT1_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "COMMENT2", ColorManager.COMMENT2_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "LITERAL1", ColorManager.LITERAL1_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "LITERAL2", ColorManager.LITERAL2_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "LABEL", ColorManager.LABEL_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "KEYWORD1", ColorManager.KEYWORD1_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "KEYWORD2", ColorManager.KEYWORD2_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "KEYWORD3", ColorManager.KEYWORD3_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "FUNCTION", ColorManager.FUNCTION_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "MARKUP", ColorManager.MARKUP_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "OPERATOR", ColorManager.OPERATOR_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "DIGIT", ColorManager.DIGIT_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        addKeywordRule(ruleSet, "INVALID", ColorManager.INVALID_COLOR, wordRule, wordDetector); //$NON-NLS-1$
        rules.add(wordRule);
    }

    private void addKeywordRule(Rule ruleSet, String type, String tokenName, CasedWordRule keywordRule,
            ColoringWordDetector wordDetector) {
        String[] keywords = ruleSet.getKeywords().get(type);
        IToken keywordToken = newToken(tokenName);
        if (keywords != null && keywords.length != 0) {
            for (int i = 0; i < keywords.length; i++) {
                wordDetector.addWord(keywords[i]);
                keywordRule.addWord(keywords[i], keywordToken);
            }
        }
        List allOfType = ruleSet.get(type);
        for (Iterator allI = allOfType.iterator(); allI.hasNext();) {
            Type aType = (Type) allI.next();
            if (aType.getType() == Type.SEQ && wordDetector.isWordStart(((TextSequence) aType).getText().charAt(0))) {

                keywordRule.addWord(aType.getText(), keywordToken);
            }
        }
    }

    private IToken newToken(String colorName) {
        CToken token = new CToken(colorName);
        return token;
    }

    public int getTokenLength() {
        if (markLength != -1) {
            int length = markLength;
            markLength = -1;
            return length;
        }
        return super.getTokenLength();
    }

    public int getOffset() {
        return fOffset;
    }

    public void setOffset(int loc) {
        fOffset = loc;
    }

    public void setMarkLength(int length) {
        markLength = length;
    }

    public int backup() {
        --fOffset;
        if (fOffset > -1) {
            return fText.charAt(fOffset);
        } else {
            fOffset = 0;
        }
        return ICharacterScanner.EOF;
    }

    public void parse(String text) {
        setRange(text, 0, text.length());
    }

    /**
     * Move the token offset by a delta.
     * 
     * @param delta - a positive or negative amount to move the token offset.
     * @nonapi
     */
    public void moveTokenOffset(int delta) {
        fTokenOffset = fTokenOffset + delta;
    }

}
