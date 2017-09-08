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
package org.talend.commons.ui.swt.colorstyledtext.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.talend.commons.ui.swt.colorstyledtext.jedit.Mark;
import org.talend.commons.ui.swt.colorstyledtext.scanner.ColoringScanner;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: StarRule.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class StarRule implements IPredicateRule {

    protected boolean isPrevious, excludeMatch, atLineStart;

    protected char[] text;

    protected static final int UNDEFINED = -1;

    /** The token to be returned on success. */
    protected IToken fToken;

    /** The pattern's column constrain. */
    protected int fColumn = UNDEFINED;

    /** The pattern's escape character. */
    protected char fEscapeCharacter;

    /** Indicates whether end of line termines the pattern. */
    protected boolean fBreaksOnEOL;

    protected IWhitespaceDetector whiteDetector;

    protected IWordDetector wordDetector;

    private boolean atWhitepsaceEnd;

    public StarRule(Mark mark, IWhitespaceDetector whitespace, IWordDetector word, IToken success) {
        isPrevious = mark.isMarkPrevious();
        excludeMatch = mark.getExcludeMatch();
        atLineStart = mark.isAtLineStart();
        atWhitepsaceEnd = mark.atWhitespaceEnd();
        text = mark.getText().toCharArray();
        this.whiteDetector = whitespace;
        this.wordDetector = word;
        fToken = success;
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

    /**
     * Evaluates this rules without considering any column constraints.
     * 
     * @param scanner the character scanner to be used
     * @return the token resulting from this evaluation
     */
    protected IToken doEvaluate(ICharacterScanner scanner) {
        if (isPrevious) {
            return doEvaluatePrevious(scanner, false);
        }
        return doEvaluateFollowing(scanner, false);
    }

    /**
     * Evaluates this rules without considering any column constraints. Resumes detection, i.e. looks only for the end
     * sequence required by this rule if the <code>resume</code> flag is set.
     * 
     * @param scanner the character scanner to be used
     * @param resume <code>true</code> if detection should be resumed, <code>false</code> otherwise
     * @return the token resulting from this evaluation
     * @since 2.0
     */
    protected IToken doEvaluatePrevious(ICharacterScanner scanner, boolean resume) {
        if (resume) {
            if (sequenceDetectedPrevious(scanner, false)) {
                return fToken;
            }
        } else {
            char c = (char) scanner.read();
            /*
             * Mark Previous :: c is either the end of the pattern, some other char, EOL, whitespace or EOF
             */
            if (c == ICharacterScanner.EOF) {
                return Token.UNDEFINED;
            }
            if (c == text[0]) {
                if (sequenceDetectedPrevious(scanner, false)) {
                    return fToken;
                }
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

    /**
     * Evaluates this rules without considering any column constraints. Resumes detection, i.e. looks only for the end
     * sequence required by this rule if the <code>resume</code> flag is set.
     * 
     * @param scanner the character scanner to be used
     * @param resume <code>true</code> if detection should be resumed, <code>false</code> otherwise
     * @return the token resulting from this evaluation
     * @since 2.0
     */
    protected IToken doEvaluateFollowing(ICharacterScanner scanner, boolean resume) {
        if (resume) {
            if (sequenceDetectedFollowing(scanner, false)) {
                return fToken;
            }
        } else {
            char c = (char) scanner.read();
            /*
             * Mark Previous :: c is either the end of the pattern, some other char, EOL, whitespace or EOF
             */
            if (c == ICharacterScanner.EOF) {
                return Token.UNDEFINED;
            }
            if (c == text[0]) {
                if (sequenceDetectedFollowing(scanner, false)) {
                    return fToken;
                }
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

    /*
     * @see IRule#evaluate
     */
    public IToken evaluate(ICharacterScanner aScanner) {
        IToken answer = evaluate(aScanner, false);
        if (answer == Token.UNDEFINED) {
            return Token.UNDEFINED;
        }
        if (!(aScanner instanceof ColoringScanner)) {
            return answer;
        }
        ColoringScanner scanner = (ColoringScanner) aScanner;
        if (isPrevious) {
            int tokenLength = startOfToken(scanner);
            scanner.moveTokenOffset(-(tokenLength - text.length));
            // scanner.markLength = excludeMatch ? tokenLength - text.length: tokenLength;
            scanner.setMarkLength(tokenLength);
        }
        return answer;
    }

    private int startOfToken(ColoringScanner scanner) {
        int original = scanner.getOffset();
        scanner.backup(); // this backs up to the MARK tag, for example (
        int c = scanner.backup();
        while (c != ICharacterScanner.EOF && !whiteDetector.isWhitespace((char) c) && wordDetector.isWordPart((char) c)) {
            c = scanner.backup();
        }
        int start = scanner.getOffset();
        // Restore the offset
        scanner.setOffset(original);
        return start == 0 ? original - start : original - start - 1;
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
    protected boolean sequenceDetectedFollowing(ICharacterScanner scanner, boolean eofAllowed) {
        int c;
        int read = 1;
        for (; read < text.length; read++) {
            c = scanner.read();
            if (c == ICharacterScanner.EOF && eofAllowed) {
                return true;
            } else if (c != text[read]) {
                // Non-matching character detected, rewind the scanner back to the start.
                // Do not unread the first character.
                scanner.unread();
                for (int j = read - 1; j > 0; j--) {
                    scanner.unread();
                }
                return false;
            }
        }
        // scan until we hit whitespace or EOF
        read = 1;
        c = scanner.read();
        while (c != ICharacterScanner.EOF) {
            if (whiteDetector.isWhitespace((char) c) || c == ICharacterScanner.EOF) {
                scanner.unread();
                return true;
            }
            c = scanner.read();
            read++;
        }
        // Non-matching character detected, rewind the scanner back to the start.
        // Do not unread the first character.
        scanner.unread();
        for (int j = read - 1; j > 0; j--) {
            scanner.unread();
        }
        return false;
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
    protected boolean sequenceDetectedPrevious(ICharacterScanner scanner, boolean eofAllowed) {
        int c;
        int read = 1;
        for (; read < text.length; read++) {
            c = scanner.read();
            if (c == ICharacterScanner.EOF && eofAllowed) {
                return true;
            } else if (c != text[read]) {
                // Non-matching character detected, rewind the scanner back to the start.
                // Do not unread the first character.
                scanner.unread();
                for (int j = read - 1; j > 0; j--) {
                    scanner.unread();
                }
                return false;
            }
        }
        if (atWhitepsaceEnd) {
            c = scanner.read();
            scanner.unread();
            if (whiteDetector.isWhitespace((char) c) || c == ICharacterScanner.EOF) {
                return true;
            }
            for (int j = read - 1; j > 0; j--) {
                scanner.unread();
            }
            return false;
        }
        return true;
    }

    /*
     * @see IPredicateRule#evaluate(ICharacterScanner, boolean)
     * @since 2.0
     */
    public IToken evaluate(ICharacterScanner scanner, boolean resume) {
        if (fColumn == UNDEFINED) {
            return isPrevious ? doEvaluatePrevious(scanner, resume) : doEvaluateFollowing(scanner, resume);
        }
        int c = scanner.read();
        scanner.unread();
        if (c == text[0]) {
            return (fColumn == scanner.getColumn() ? (isPrevious ? doEvaluatePrevious(scanner, resume)
                    : doEvaluateFollowing(scanner, resume)) : Token.UNDEFINED);
        }
        return Token.UNDEFINED;
    }

    /*
     * @see IPredicateRule#getSuccessToken()
     * @since 2.0
     */
    public IToken getSuccessToken() {
        return fToken;
    }
}
