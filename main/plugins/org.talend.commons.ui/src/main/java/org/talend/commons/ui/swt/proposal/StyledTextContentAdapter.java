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

import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * DOC nrousseau class global comment. Detailled comment <br/>
 * 
 * $Id: StyledTextContentAdapter.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class StyledTextContentAdapter implements IControlContentAdapter {

    public String getControlContents(Control control) {
        return ((StyledText) control).getText();
    }

    public void setControlContents(Control control, String text, int cursorPosition) {
        ((StyledText) control).setText(text);
        ((StyledText) control).setSelection(cursorPosition, cursorPosition);
    }

    public void insertControlContents(Control control, String text, int cursorPosition) {
        Point selection = ((StyledText) control).getSelection();
        StyledText styledText = (StyledText) control;
        if (selection.x != selection.y) {
            styledText.replaceTextRange(selection.x, Math.abs(selection.y - selection.x), text);
        } else {
            styledText.insert(text);
        }
        int offsetCursor = selection.x + text.length();
        int textLength = styledText.getText().length();
        if (offsetCursor <= textLength) {
            styledText.setSelection(offsetCursor, offsetCursor);
        }
    }

    public int getCursorPosition(Control control) {
        return ((StyledText) control).getCaretOffset();
    }

    public Rectangle getInsertionBounds(Control control) {
        StyledText text = (StyledText) control;
        Point selection = text.getSelection();
        Point pointLocation = text.getLocationAtOffset(selection.x);
        Rectangle insertionBounds = new Rectangle(pointLocation.x, pointLocation.y, 1, text.getLineHeight() + 2);
        return insertionBounds;
    }

    public void setCursorPosition(Control control, int position) {
        // ((StyledText) control).setSelection(new Point(position, position));
    }
}
