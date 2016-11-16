// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.ui.utils.data.text.rules;

import org.eclipse.jface.text.Assert;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class StringRuleBasedScanner implements ICharacterScanner, IStringTokenScanner {

    /** The list of rules of this scanner. */
    protected IRule[] fRules;

    /** The token to be returned by default if no rule fires. */
    protected IToken fDefaultReturnToken;

    /** The document to be scanned. */
    protected String fText;

    /** The cached legal line delimiters of the document. */
    protected char[][] fDelimiters;

    /** The offset of the next character to be read. */
    protected int fOffset;

    /** The end offset of the range to be scanned. */
    protected int fRangeEnd;

    /** The offset of the last read token. */
    protected int fTokenOffset;

    /** The cached column of the current scanner position. */
    protected int fColumn;

    /** Internal setting for the un-initialized column cache. */
    protected static final int UNDEFINED = -1;

    public StringRuleBasedScanner() {
    }

    public void setPredicateRules(IPredicateRule[] rules) {
        setRules(rules);
    }

    /**
     * Configures the scanner with the given sequence of rules.
     * 
     * @param rules the sequence of rules controlling this scanner
     */
    public void setRules(IRule[] rules) {
        if (rules != null) {
            fRules = new IRule[rules.length];
            System.arraycopy(rules, 0, fRules, 0, rules.length);
        } else {
            fRules = null;
        }
    }

    /**
     * Configures the scanner's default return token. This is the token which is returned when non of the rules fired
     * and EOF has not been reached.
     * 
     * @param defaultReturnToken the default return token
     * @since 2.0
     */
    public void setDefaultReturnToken(IToken defaultReturnToken) {
        Assert.isNotNull(defaultReturnToken.getData());
        fDefaultReturnToken = defaultReturnToken;
    }

    public int getColumn() {
        return UNDEFINED;
    }

    public char[][] getLegalLineDelimiters() {
        return fDelimiters;
    }

    public int read() {
        if (fOffset < 0) {
            return EOF;
        }
        try {

            if (fOffset < fRangeEnd) {
                return fText.charAt(fOffset);
            }

            return EOF;

        } finally {
            ++fOffset;
            fColumn = UNDEFINED;
        }
    }

    public void unread() {
        --fOffset;
    }

    public int getTokenLength() {
        if (fOffset < fRangeEnd) {
            return fOffset - getTokenOffset();
        }
        return fRangeEnd - getTokenOffset();
    }

    public int getTokenOffset() {
        return fTokenOffset;
    }

    public IToken nextToken() {
        fTokenOffset = fOffset;
        fColumn = UNDEFINED;

        if (fRules != null) {
            for (int i = 0; i < fRules.length; i++) {
                IToken token = (fRules[i].evaluate(this));
                if (!token.isUndefined()) {
                    return token;
                }
            }
        }

        if (read() == EOF) {
            return Token.EOF;
        }
        return fDefaultReturnToken;
    }

    public void setRange(String text, int offset, int length) {
        fText = text;
        fOffset = offset;
        fColumn = UNDEFINED;
        fRangeEnd = Math.min(text.length(), offset + length);
        String[] delimiters = new String[] { "\n" }; //$NON-NLS-1$
        fDelimiters = new char[delimiters.length][];
        for (int i = 0; i < delimiters.length; i++) {
            fDelimiters[i] = delimiters[i].toCharArray();
        }

        if (fDefaultReturnToken == null) {
            fDefaultReturnToken = new Token(null);
        }
    }
}
