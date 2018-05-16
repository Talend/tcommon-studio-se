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
package org.talend.commons.ui.swt.proposal;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.runtime.swt.proposal.IControlContentAdapterExtended;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: StyledTextContentAdapterExtended.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class StyledTextContentAdapterExtended extends StyledTextContentAdapter implements
        IControlContentAdapterExtended {

    private String filterValue;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.designer.mapper.ui.proposal.expression.IControlContentAdapterExtended#getFilterValue()
     */
    public String getFilterValue(Control control) {
        String controlContents = getControlContents(control);
        int cursorPosition = getCursorPosition(control);
        String text = controlContents.substring(0, cursorPosition);
        int lastCRIndex = text.lastIndexOf("\n"); //$NON-NLS-1$
        int lastSpaceIndex = text.lastIndexOf(" "); //$NON-NLS-1$
        if (lastSpaceIndex != -1 && (lastCRIndex != -1 && lastSpaceIndex > lastCRIndex || lastCRIndex == -1)) {
            return text.substring(lastSpaceIndex + 1, text.length());
        }
        if (lastCRIndex != -1) {
            return text.substring(lastCRIndex + 1, text.length());
        }
        return text;
    }

    public void insertControlContents(Control control, String text, int cursorPosition) {
        int filterValueLength = filterValue.length();
        String controlContents = getControlContents(control);
        Point selection = ((StyledText) control).getSelection();
        StyledText styledText = (StyledText) control;
        if (selection.x != selection.y) {
            super.insertControlContents(control, text, cursorPosition);
            return;
        } else {
            int remaingCharsOffset = 0;
            // int nextSpaceIndex = -1;
            // char[] separators = new char[] { ' ', '.', '\'', '"', '(', ')', '+' };
            // for (int i = selection.x; i < controlContents.length(); i++) {
            // if (ArrayUtils.contains(separators, controlContents.charAt(i))) {
            // nextSpaceIndex = i;
            // break;
            // }
            // }
            // int nextCrIndex = controlContents.indexOf('\n', selection.x);
            // if (filterValueLength == 0) {
            // remaingCharsOffset = 0;
            // } else if (nextSpaceIndex != -1 && (nextCrIndex != -1 && nextSpaceIndex < nextCrIndex || nextCrIndex ==
            // -1)) {
            // remaingCharsOffset = nextSpaceIndex - selection.x;
            // } else if (nextCrIndex != -1) {
            // remaingCharsOffset = nextCrIndex - 1 - selection.x;
            // } else {
            // remaingCharsOffset = controlContents.length() - selection.x;
            // }
            styledText.replaceTextRange(selection.x - filterValueLength, filterValueLength + remaingCharsOffset, text);
        }
        int offsetCursor = selection.x - filterValueLength + text.length();
        int textLength = styledText.getText().length();
        if (offsetCursor <= textLength) {
            styledText.setSelection(offsetCursor, offsetCursor);
        }
        styledText.redraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.proposal.IControlContentAdapterExtended#setUsedFilterValue(java.lang.String)
     */
    public void setUsedFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

}
