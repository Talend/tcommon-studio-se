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
package org.talend.commons.ui.runtime.swt.tableviewer.tableeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.ws.WindowSystem;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ButtonPushImageTableEditorContent extends TableEditorContentNotModifiable {

    private Image image;

    private Image imageHover;

    /**
     * DOC amaumont ButtonImageTableEditorContent constructor comment.
     */
    public ButtonPushImageTableEditorContent() {
        super();
    }

    public Control initialize(Table table, TableEditor tableEditor, final TableViewerCreatorColumnNotModifiable currentColumn,
            final Object currentRowObject, Object currentCellValue) {

        final Label label = new Label(table, SWT.NONE);
        label.setBackground(label.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        label.setImage(image);
        Point size = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Rectangle bounds = label.getBounds();
        bounds.y -= 5;
        label.setBounds(bounds);
        tableEditor.grabHorizontal = WindowSystem.isGTK();
        tableEditor.minimumHeight = size.y;
        tableEditor.minimumWidth = size.x;

        label.addMouseListener(new MouseListener() {

            private boolean hooked;

            public void mouseDoubleClick(MouseEvent e) {
                // TODO Auto-generated method stub

            }

            public void mouseDown(MouseEvent e) {
                hooked = true;
            }

            public void mouseUp(MouseEvent e) {
                Rectangle boundsLabel = label.getBounds();
                Point mousePosition = new Point(e.x, e.y);

                if (hooked && mousePosition.x > 0 && mousePosition.y > 0 && mousePosition.x < boundsLabel.width
                        && mousePosition.y < boundsLabel.height) {
                    hooked = false;
                    selectionEvent(currentColumn, currentRowObject);
                }
            }

        });
        return label;
    }

    /**
     * DOC amaumont Comment method "selectionEvent".
     * 
     * @param bean
     * @param currentRowObject
     */
    protected abstract void selectionEvent(TableViewerCreatorColumnNotModifiable column, Object bean);

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImageHover(Image imageHover) {
        throw new UnsupportedOperationException();
        // this.imageHover = imageHover;
    }

    /**
     * Getter for image.
     * 
     * @return the image
     */
    public Image getImage() {
        return this.image;
    }

    /**
     * Getter for imageHover.
     * 
     * @return the imageHover
     */
    public Image getImageHover() {
        return this.imageHover;
    }

}
