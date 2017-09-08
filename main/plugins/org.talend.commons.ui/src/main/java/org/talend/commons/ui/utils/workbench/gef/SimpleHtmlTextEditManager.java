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

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Text;

/**
 * Manage the direct edit of a text in GEF. <br/>
 * 
 * $Id: SimpleHtmlTextEditManager.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class SimpleHtmlTextEditManager extends DirectEditManager {

    Font scaledFont;

    String labelText = null;

    public SimpleHtmlTextEditManager(GraphicalEditPart source, Class editorType, SimpleHtmlCellEditorLocator locator) {
        super(source, editorType, locator);
    }

    @Override
    protected void bringDown() {
        Font disposeFont = scaledFont;
        scaledFont = null;
        super.bringDown();
        if (disposeFont != null) {
            disposeFont.dispose();
        }
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    @Override
    protected void initCellEditor() {
        SimpleHtmlFigure label = (SimpleHtmlFigure) (getEditPart()).getFigure();
        if (labelText == null) {
            String initialLabelText = label.getText();
            getCellEditor().setValue(initialLabelText);
        } else {
            getCellEditor().setValue(labelText);
        }
        Text text = (Text) getCellEditor().getControl();
        IFigure figure = (getEditPart()).getFigure();
        scaledFont = figure.getFont();
        FontData data = scaledFont.getFontData()[0];
        Dimension fontSize = new Dimension(0, data.getHeight());
        label.translateToAbsolute(fontSize);
        data.setHeight(fontSize.height);
        scaledFont = new Font(null, data);
        text.setFont(scaledFont);
        text.selectAll();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.tools.DirectEditManager#getCellEditor()
     */
    @Override
    public CellEditor getCellEditor() {
        // TODO Auto-generated method stub
        return super.getCellEditor();
    }

}
