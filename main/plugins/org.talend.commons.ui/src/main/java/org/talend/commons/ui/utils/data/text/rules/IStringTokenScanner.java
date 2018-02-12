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
package org.talend.commons.ui.utils.data.text.rules;

import org.eclipse.jface.text.rules.IToken;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: IStringTokenScanner.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public interface IStringTokenScanner {

    /**
     * Configures the scanner by providing access to the document range that should be scanned.
     * 
     * @param text to scan
     * @param offset the offset of the document range to scan
     * @param length the length of the document range to scan
     */
    void setRange(String text, int offset, int length);

    /**
     * Returns the next token in the document.
     * 
     * @return the next token in the document
     */
    IToken nextToken();

    /**
     * Returns the offset of the last token read by this scanner.
     * 
     * @return the offset of the last token read by this scanner
     */
    int getTokenOffset();

    /**
     * Returns the length of the last token read by this scanner.
     * 
     * @return the length of the last token read by this scanner
     */
    int getTokenLength();
}
