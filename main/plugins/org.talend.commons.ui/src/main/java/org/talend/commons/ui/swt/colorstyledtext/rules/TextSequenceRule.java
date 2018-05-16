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
package org.talend.commons.ui.swt.colorstyledtext.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: TextSequenceRule.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class TextSequenceRule extends Object implements IPredicateRule {

    /**
     * An implementation of <code>IRule</code> capable of detecting words Word rules also allow for the association of
     * tokens with specific words. That is, not only can the rule be used to provide tokens for exact matches, but also
     * for the generalized notion of a word in the context in which it is used. A word rules uses a word detector to
     * determine what a word is.
     * 
     * @see IWordDetector
     */
    protected static final int UNDEFINED = -1;

    /** The word detector used by this rule. */
    protected IWordDetector fDetector;

    /** The token to be returned on success. */
    protected IToken token;

    /** The column constraint. */
    protected int fColumn = UNDEFINED;

    protected char[] word;

    protected boolean ignoreCase;

    private StringBuffer fBuffer = new StringBuffer();

    public TextSequenceRule(String wordToMatch, IToken token, boolean ignoreCase) {
        this.token = token;
        word = (ignoreCase ? wordToMatch.toLowerCase().toCharArray() : wordToMatch.toCharArray());
        this.ignoreCase = ignoreCase;
    }

    /**
     * Sets a column constraint for this rule. If set, the rule's token will only be returned if the pattern is detected
     * starting at the specified column. If the column is smaller then 0, the column constraint is considered removed.
     * 
     * @param column the column in which the pattern starts
     */
    public void setColumnConstraint(int column) {
        if (column < 0) {
            column = UNDEFINED;
        }
        fColumn = column;
    }

    /*
     * @see IRule#evaluate
     */
    public IToken evaluate(ICharacterScanner scanner) {
        return evaluate(scanner, false);
    }

    /**
     * Returns the characters in the buffer to the scanner.
     * 
     * @param scanner the scanner to be used
     */
    protected void unreadBuffer(ICharacterScanner scanner) {
        for (int i = fBuffer.length() - 1; i >= 0; i--) {
            scanner.unread();
        }
    }

    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
        if (fColumn == UNDEFINED) {
            return doEvaluate(scanner, resume);
        }

        int c = scanner.read();
        scanner.unread();
        if (c == word[0]) {
            return (fColumn == scanner.getColumn() ? doEvaluate(scanner, resume) : Token.UNDEFINED);
        }
        return Token.UNDEFINED;
    }

    /**
     * Evaluates this rules without considering any column constraints.
     * 
     * @param scanner the character scanner to be used
     * @return the token resulting from this evaluation
     */
    protected IToken doEvaluate(ICharacterScanner scanner) {
        return doEvaluate(scanner, false);
    }

    /**
     * Evaluates this rules without considering any column constraints. Resumes detection, i.e. look sonly for the end
     * sequence required by this rule if the <code>resume</code> flag is set.
     * 
     * @param scanner the character scanner to be used
     * @param resume <code>true</code> if detection should be resumed, <code>false</code> otherwise
     * @return the token resulting from this evaluation
     * @since 2.0
     */
    protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
        if (resume) {
            if (sequenceDetected(scanner)) {
                return token;
            }
        } else {
            int c = scanner.read();
            if (c == word[0] || Character.toLowerCase((char) c) == word[0]) {
                if (sequenceDetected(scanner)) {
                    return token;
                }
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

    public IToken getSuccessToken() {
        return token;
    }

    /**
     * Returns whether the next characters to be read by the character scanner are an exact match with the given
     * sequence. No escape characters are allowed within the sequence. If specified the sequence is considered to be
     * found when reading the EOF character.
     * 
     * @param scanner the character scanner to be used
     * @param sequence the sequence to be detected
     * @param eofAllowed indicated whether EOF terminates the pattern
     * @return <code>true</code> if the given sequence has been detected
     */
    protected boolean sequenceDetected(ICharacterScanner scanner) {
        for (int i = 1; i < word.length; i++) {
            int c = scanner.read();
            if (c != word[i] || c != Character.toLowerCase((char) c)) {
                // Non-matching character detected, rewind the scanner back to the start.
                // Do not unread the first character.
                scanner.unread();
                for (int j = i - 1; j > 0; j--) {
                    scanner.unread();
                }
                return false;
            }
        }
        return true;
    }
}
