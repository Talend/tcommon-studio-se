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
package org.talend.commons.ui.utils.workbench.gef;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * Create a text cell editor to edit a text in GEF. <br/>
 * 
 * $Id: SimpleHtmlCellEditorLocator.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class SimpleHtmlCellEditorLocator implements CellEditorLocator {

    /**
     * 
     */
    private static final int A_4 = 4;

    private SimpleHtmlFigure label;

    public SimpleHtmlCellEditorLocator(SimpleHtmlFigure label) {
        setLabel(label);
    }

    public void relocate(CellEditor celleditor) {
        Text text = (Text) celleditor.getControl();
        Point sel = text.getSelection();
        Point pref = text.computeSize(-1, -1);
        Rectangle rect = label.getBounds().getCopy();
        label.translateToAbsolute(rect);
        text.setBounds(rect.x - A_4, rect.y - 1, pref.x + 1, pref.y + 1);
        text.setSelection(0);
        text.setSelection(sel);
    }

    protected SimpleHtmlFigure getLabel() {
        return label;
    }

    protected void setLabel(SimpleHtmlFigure label) {
        this.label = label;
    }
}
