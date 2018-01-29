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
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.Token;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: CasedPatternRule.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class CasedPatternRule extends PatternRule {

    protected boolean ignoreCase;

    public CasedPatternRule(String startSequence, String endSequence, IToken token, char escapeCharacter,
            boolean breaksOnEOL) {
        this(startSequence, endSequence, token, escapeCharacter, breaksOnEOL, false);
    }

    public CasedPatternRule(String startSequence, String endSequence, IToken token, char escapeCharacter,
            boolean breaksOnEOL, boolean ignoreCase) {

        super((ignoreCase ? startSequence.toLowerCase() : startSequence), (endSequence == null ? null
                : (ignoreCase ? endSequence.toLowerCase() : endSequence)), token, escapeCharacter, breaksOnEOL);
        this.ignoreCase = ignoreCase;
    }

    /* Copied from my superclass and modified to support case sensitivity. */
    protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {
        if (!ignoreCase) {
            return super.doEvaluate(scanner, resume);
        }
        if (resume) {
            if (endSequenceDetected(scanner)) {
                return fToken;
            }
        } else {
            int c = scanner.read();
            if (c == fStartSequence[0] || Character.toLowerCase((char) c) == fStartSequence[0]) {
                if (sequenceDetected(scanner, fStartSequence, false)) {
                    if (endSequenceDetected(scanner)) {
                        return fToken;
                    }
                }
            }
        }
        scanner.unread();
        return Token.UNDEFINED;
    }

    /* Copied from my superclass and modified to support case sensitivity. */
    protected boolean endSequenceDetected(ICharacterScanner scanner) {
        if (!ignoreCase) {
            return super.endSequenceDetected(scanner);
        }
        int c;
        char[][] delimiters = scanner.getLegalLineDelimiters();
        while ((c = scanner.read()) != ICharacterScanner.EOF) {
            if (c == fEscapeCharacter) {
                // Skip the escaped character.
                scanner.read();
            } else if (fEndSequence.length > 0
                    && (c == fEndSequence[0] || Character.toLowerCase((char) c) == fEndSequence[0])) {
                // Check if the specified end sequence has been found.
                if (sequenceDetected(scanner, fEndSequence, true)) {
                    return true;
                }
            } else if (fBreaksOnEOL) {
                // Check for end of line since it can be used to terminate the pattern.
                for (int i = 0; i < delimiters.length; i++) {
                    if (c == delimiters[i][0] && sequenceDetected(scanner, delimiters[i], false)) {
                        return true;
                    }
                }
            }
        }
        scanner.unread();
        return true;
    }

    /* Copied from my superclass and modified to support case sensitivity. */
    protected boolean sequenceDetected(ICharacterScanner scanner, char[] sequence, boolean eofAllowed) {
        if (!ignoreCase) {
            return super.sequenceDetected(scanner, sequence, eofAllowed);
        }
        for (int i = 1; i < sequence.length; i++) {
            int c = scanner.read();
            if (c == ICharacterScanner.EOF && eofAllowed) {
                return true;
            } else if (c != sequence[i] || c != Character.toLowerCase((char) c)) {
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
