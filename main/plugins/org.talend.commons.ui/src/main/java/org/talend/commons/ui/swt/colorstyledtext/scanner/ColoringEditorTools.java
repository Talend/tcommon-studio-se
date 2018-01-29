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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.talend.commons.ui.swt.colorstyledtext.ColorManager;
import org.talend.commons.ui.swt.colorstyledtext.jedit.EOLSpan;
import org.talend.commons.ui.swt.colorstyledtext.jedit.IVisitor;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mark;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mode;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Rule;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Span;
import org.talend.commons.ui.swt.colorstyledtext.jedit.TextSequence;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Type;
import org.talend.commons.ui.swt.colorstyledtext.rules.CasedPatternRule;
import org.talend.commons.ui.swt.colorstyledtext.rules.ColoringWhitespaceDetector;
import org.talend.commons.ui.swt.colorstyledtext.rules.ColoringWordDetector;
import org.talend.commons.ui.swt.colorstyledtext.rules.DelegateToken;
import org.talend.commons.ui.swt.colorstyledtext.rules.EndOfLineRule;
import org.talend.commons.ui.swt.colorstyledtext.rules.ITokenFactory;
import org.talend.commons.ui.swt.colorstyledtext.rules.StarRule;
import org.talend.commons.ui.swt.colorstyledtext.rules.TextSequenceRule;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: ColoringEditorTools.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ColoringEditorTools {

    public static void add(Rule rule, List<IRule> rules, ITokenFactory factory) {
        List allTypes = rule.getTypes();
        for (Iterator typeI = allTypes.iterator(); typeI.hasNext();) {
            Type type = (Type) typeI.next();
            add(rule, type, factory, rules);
        }
    }

    public static void add(final Rule rule, final Type type, ITokenFactory factory, final List<IRule> rules) {
        final IToken token = factory.makeToken(type);
        final Mode mode = rule.getMode();
        final boolean ignoreCase = rule.getIgnoreCase();
        type.accept(new IVisitor() {

            public void acceptSpan(Span span) {
                IToken defaultToken = token;
                if (span.hasDelegate()) {
                    Rule delegateRule = mode.getRule(span.getDelegate());
                    defaultToken = new DelegateToken(type, delegateRule, span.getEnd());
                }
                /*
                 * Using a PatternRule instead of a MultiLineRule because PatternRule exposes the break on newline
                 * behavior.
                 */
                PatternRule pat = new CasedPatternRule(span.getStart(), span.getEnd(), defaultToken, mode.getDefaultRuleSet()
                        .getEscape(), span.noLineBreak(), ignoreCase);
                rules.add(pat);
            }

            public void acceptTextSequence(TextSequence text) {
                /*
                 * If the text sequence can be recognized as a word, don't add it. This reduces the number of partitions
                 * created. If the text sequence can not be recognized as a word add it as a text sequence.
                 */
                if (isWordStart(text.getText().charAt(0))) {
                    return;
                }
                rules.add(new TextSequenceRule(text.getText(), token, ignoreCase));
            }

            public void acceptEolSpan(EOLSpan eolSpan) {
                rules.add(new EndOfLineRule(eolSpan.getText(), token, ignoreCase));
            }

            public void acceptMark(Mark mark) {
                rules.add(new StarRule(mark, new ColoringWhitespaceDetector(), wordDetector, token));
            }
        });
    }

    protected static ColoringWordDetector wordDetector = new ColoringWordDetector();

    public ColoringEditorTools() {
        // do nothing
    }

    protected static boolean isWordStart(char c) {
        return wordDetector.isWordStart(c);
    }

    public boolean affectsTextPresentation(PropertyChangeEvent event) {
        String property = event.getProperty();
        if (property == null) {
            return false;
        }
        if (property.endsWith("Bold")) { //$NON-NLS-1$
            property = property.substring(0, property.length() - ColorManager.BOLD_SUFFIX.length());
        }
        boolean affects = property.equals(ColorManager.COMMENT1_COLOR) || property.equals(ColorManager.COMMENT2_COLOR)
                || property.equals(ColorManager.LITERAL1_COLOR) || property.equals(ColorManager.LITERAL2_COLOR)
                || property.equals(ColorManager.LABEL_COLOR) || property.equals(ColorManager.KEYWORD1_COLOR)
                || property.equals(ColorManager.KEYWORD2_COLOR) || property.equals(ColorManager.KEYWORD3_COLOR)
                || property.equals(ColorManager.FUNCTION_COLOR) || property.equals(ColorManager.MARKUP_COLOR)
                || property.equals(ColorManager.OPERATOR_COLOR) || property.equals(ColorManager.DIGIT_COLOR)
                || property.equals(ColorManager.INVALID_COLOR) || property.equals(ColorManager.NULL_COLOR);
        // System.out.println(affects);
        return affects;
    }

}
