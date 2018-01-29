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
package org.talend.commons.ui.runtime.swt.tableviewer;

import org.eclipse.jface.dialogs.PopupDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TooltipPopup.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class TooltipPopup {

    private Popup popup;

    private Point cursorPosition;

    private Shell parent;

    private TooltipPopup(Shell parent) {
        this.parent = parent;
        init(parent);
    }

    /**
     * 
     * DOC amaumont DraggingInfosPopup class global comment. Detailled comment <br/>
     * 
     * $Id: TooltipPopup.java 7038 2007-11-15 14:05:48Z plegall $
     * 
     */
    private class Popup extends PopupDialog {

        private Composite mainComposite;

        private boolean visible;

        public Popup(Shell parent) {
            super(parent, SWT.ON_TOP, false, false, false, false, null, null);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            mainComposite = new Composite(parent, SWT.NONE);
            RowLayout rowLayout = new RowLayout(SWT.VERTICAL);
            rowLayout.fill = true;
            rowLayout.pack = true;
            rowLayout.marginWidth = 2;
            rowLayout.marginHeight = 0;
            rowLayout.marginBottom = 5;
            rowLayout.spacing = 0;
            mainComposite.setLayout(rowLayout);

            return mainComposite;
        }

        @Override
        protected void adjustBounds() {

            if (cursorPosition == null) {
                cursorPosition = new Point(0, 0);
            }
            Shell popupShell = popup.getShell();
            Rectangle popupBounds = popupShell.getBounds();
            final Shell mapperShell = popup.getParentShell();

            Point point = mapperShell.getDisplay().map(popupShell, mapperShell, new Point(0, 0));

            Rectangle boundsToRedraw = new Rectangle(point.x, point.y, popupBounds.width, popupBounds.height);

            Point preferredSize = getPreferredSize();
            int width = preferredSize.x;
            int height = preferredSize.y;
            boolean isVisible = this.visible;
            if (width != popupBounds.width || height != popupBounds.height) {
                setVisible(false);
            }
            int newX = cursorPosition.x - width - 20;
            if (newX < 0) {
                newX = 0;
            }
            Rectangle newBounds = new Rectangle(newX, cursorPosition.y - height - 20, width, height);
            getShell().setBounds(newBounds);

            // to get round refresh problem
            if (isVisible != this.visible) {
                setVisible(isVisible);
                // System.out.println("Adjust setVisible");
            } else {
                mapperShell.redraw(boundsToRedraw.x, boundsToRedraw.y, boundsToRedraw.width, boundsToRedraw.height,
                        false);
                // System.out.println("Adjust redraw");
            }
        }

        /**
         * DOC amaumont Comment method "setVisible".
         * 
         * @param visible
         */
        public void setVisible(boolean visible) {
            if (visible != this.visible) {
                this.visible = visible;
                getShell().setVisible(visible);
            }
        }

        private Point getPreferredSize() {
            return mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        }

    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param parent
     */
    private void init(Shell parent) {
        popup = new Popup(parent);
        popup.open();
        popup.setVisible(false);
    }

    public static TooltipPopup getNewShell(Shell parent) {
        return new TooltipPopup(parent);
    }

    /**
     * DOC amaumont Comment method "getBounds".
     * 
     * @return
     */
    public Rectangle getBounds() {
        return popup.getShell().getBounds();
    }

    /**
     * DOC amaumont Comment method "setVisible".
     * 
     * @param b
     */
    public void setVisible(boolean visible) {
        popup.setVisible(visible);
    }

    /**
     * DOC amaumont Comment method "setCursorPosition".
     * 
     * @param x
     * @param y
     */
    public void setCursorPosition(int x, int y) {
        this.cursorPosition = new Point(x, y);
        // System.out.println("setCursorPosition");
        popup.adjustBounds();
    }

    public Point getPositionFromMapperShellOrigin() {
        Display display = popup.getShell().getDisplay();
        return display.map(popup.getShell(), this.parent, new Point(0, 0));
    }

}
