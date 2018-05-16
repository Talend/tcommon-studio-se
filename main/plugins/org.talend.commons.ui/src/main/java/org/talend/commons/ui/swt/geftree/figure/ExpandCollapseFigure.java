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
package org.talend.commons.ui.swt.geftree.figure;

import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.swt.geftree.TreeSelectManager;

/**
 * cli class global comment. Detailled comment
 */
public class ExpandCollapseFigure extends ImageFigure {

    private TreeBranch parent;

    public ExpandCollapseFigure(TreeBranch parent) {
        this(parent, null);
    }

    public ExpandCollapseFigure(TreeBranch parent, Image image) {
        this(parent, image, PositionConstants.CENTER);
    }

    public ExpandCollapseFigure(TreeBranch parent, Image image, int alignment) {
        super(image, alignment);
        this.parent = parent;
        init();
    }

    public TreeBranch getParent() {
        return this.parent;
    }

    private void init() {
        // need check later, not works.
        this.addMouseListener(new MouseListener.Stub() {

            public void mousePressed(MouseEvent me) {
                if (getParent() != null) {
                    TreeSelectManager manager = TreeSelectManager.getManager();
                    manager.setSelection(getParent()); // select first

                    getParent().doExpandCollapse();
                }
            }
        });
    }
}
