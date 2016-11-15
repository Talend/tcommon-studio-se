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
package org.talend.commons.ui.swt.colorstyledtext.jedit;

public interface ISyntaxListener {

    void newRules(String name, boolean highlightDigits, boolean ignoreCase, String digitRE, char escape,
            String defaultTokenType);

    void newEOLSpan(String type, String text);

    void newSpan(String type, String begin, String end, boolean atLineStart, boolean excludeMatch, boolean noLineBreak,
            boolean noWordBreak, String delegate);

    void newKeywords(KeywordMap keywords);

    void newTextSequence(String type, String text, boolean atLineStart, boolean atWhitespaceEnd, boolean atWordStart,
            String delegate);

    void newMark(String type, String text, boolean atLineStart, boolean atWhitespaceEnd, boolean atWordStart,
            String delegate, boolean isPrevious, boolean excludeMatch);
}
