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
package org.talend.commons.ui.swt.colorstyledtext;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MapperColorStyledText.java 898 2006-12-07 11:06:17Z amaumont $
 * 
 */
public class UnnotifiableColorStyledText extends ColorStyledText {

    public UnnotifiableColorStyledText(Composite parent, int style, IPreferenceStore store, String languageMode) {
        super(parent, style, store, languageMode);
    }

    /**
     * This implementation is needed because synchronisation with TableEntries does not work using standard
     * implementation : <BR> - focus of TableEntry is lost when a character is typed <BR> - coloration does'nt work when
     * a character is typed in a TableEntry
     * 
     * <BR>
     * <BR>
     * <b>WARNING: listeners will not be notified in this implementation.</b>
     * 
     * @param text new widget content. Replaces existing content. Line styles that were set using StyledText API are
     * discarded. The current selection is also discarded.
     * @exception SWTException
     * <ul>
     * <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
     * <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
     * </ul>
     * @exception IllegalArgumentException
     * <ul>
     * <li>ERROR_NULL_ARGUMENT when string is null</li>
     * </ul>
     */
    public void setTextWithoutNotifyListeners(String text) {
        checkWidget();
        if (text == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        getContent().setText(text);
        colorize(getScanner());
    }

    public void replaceTextRangeWithoutNotifyListeners(int start, int length, String text) {
        checkWidget();
        if (text == null) {
            SWT.error(SWT.ERROR_NULL_ARGUMENT);
        }
        int contentLength = getCharCount();
        // if(start < 0) {
        // start = 0;
        // }
        int end = start + length;
        // if(end > contentLength) {
        // end = contentLength-1;
        // }
        // System.out.println(start + " " + end + " " + contentLength + " '" + getText()+"'");
        if (start > end || start < 0 || end > contentLength) {
            SWT.error(SWT.ERROR_INVALID_RANGE);
        }
        getContent().replaceTextRange(start, length, text);
        colorize(getScanner());
    }

    // public void insertText(String text, int start) {
    // checkWidget();
    // if (text == null) {
    // SWT.error(SWT.ERROR_NULL_ARGUMENT);
    // }
    // getContent().replaceTextRange(start, 0, text);
    // colorize(getScanner());
    // }

}
