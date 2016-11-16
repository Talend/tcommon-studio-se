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
package org.talend.commons.ui.swt.proposal;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.talend.commons.ui.runtime.swt.proposal.IControlContentAdapterExtended;

public class StyledTextContentAdapterExtended extends StyledTextContentAdapter implements
        IControlContentAdapterExtended {

    private String filterValue;

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
            styledText.replaceTextRange(selection.x - filterValueLength, filterValueLength + remaingCharsOffset, text);
        }
        int offsetCursor = selection.x - filterValueLength + text.length();
        int textLength = styledText.getText().length();
        if (offsetCursor <= textLength) {
            styledText.setSelection(offsetCursor, offsetCursor);
        }
        styledText.redraw();
    }

    public void setUsedFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

}
