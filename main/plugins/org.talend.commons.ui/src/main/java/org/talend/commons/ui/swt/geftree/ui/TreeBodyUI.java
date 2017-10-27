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
package org.talend.commons.ui.swt.geftree.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.geftree.figure.ExpandCollapseFigure;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;

/**
 * DOC hwang class global comment. Detailled comment
 */
public abstract class TreeBodyUI {

    protected Composite body;

    protected FigureCanvas figureCanvas;

    public TreeBodyUI(ViewUI viewPart) {
        this.body = viewPart.getForm().getBody();
        this.figureCanvas = new FigureCanvas(body, SWT.DOUBLE_BUFFERED | SWT.BORDER);
        addExpendCollapseListener();
    }

    protected void addExpendCollapseListener() {
        if (this.figureCanvas == null) {
            return;
        }

        this.figureCanvas.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {

            }

            public void mouseDown(MouseEvent e) {

            }

            public void mouseUp(MouseEvent e) {
                IFigure figure = figureCanvas.getContents();
                if (figure != null && figure instanceof TreeBranch) {
                    TreeBranch root = (TreeBranch) figure;
                    List<ExpandCollapseFigure> list = getExpandCollapseFigures(root);
                    for (ExpandCollapseFigure exFigure : list) {
                        Point point = new Point(e.x, e.y);
                        exFigure.translateToRelative(point);
                        if (exFigure.containsPoint(point.x, point.y)) {
                            TreeBranch parent = exFigure.getParent();
                            parent.doExpandCollapse(parent);
                            break;
                        }
                    }
                }
            }
        });
    }

    protected List<ExpandCollapseFigure> getExpandCollapseFigures(Figure root) {
        List<ExpandCollapseFigure> list = new ArrayList<ExpandCollapseFigure>();
        if (root == null) {
            return list;
        }
        if (root instanceof ExpandCollapseFigure) {
            list.add((ExpandCollapseFigure) root);
        }
        List child = root.getChildren();
        for (int i = 0; i < child.size(); i++) {
            Object obj = child.get(i);
            if (obj instanceof Figure) {
                list.addAll(getExpandCollapseFigures((Figure) obj));
            }
        }
        return list;
    }
}
