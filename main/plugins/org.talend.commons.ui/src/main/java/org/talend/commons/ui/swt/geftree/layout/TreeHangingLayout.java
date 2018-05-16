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
package org.talend.commons.ui.swt.geftree.layout;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.talend.commons.ui.swt.geftree.TreeAnimation;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;

/**
 * cli class global comment. Detailled comment
 */
@SuppressWarnings("unchecked")
public class TreeHangingLayout extends TreeBranchLayout {

    public TreeHangingLayout(TreeBranch branch) {
        super(branch);
    }

    public void calculateDepth() {
        int depth = 0;
        List subtrees = getBranch().getContents().getChildren();
        for (int i = 0; i < subtrees.size(); i++) {
            depth += ((TreeBranch) subtrees.get(i)).getDepth();
        }
        depth++;
        setDepth(depth);

    }

    public void setRowHeights(int[] heights, int offset) {
        super.setRowHeights(heights, offset);
        offset++;
        if (getBranch().isExpanded()) {
            List subtrees = getBranch().getContents().getChildren();
            TreeBranch subtree;

            for (int i = 0; i < subtrees.size(); i++) {
                subtree = (TreeBranch) subtrees.get(i);
                subtree.setRowHeights(heights, offset);
                offset += subtree.getDepth();
            }
        }
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#updateRowHeights()
     */
    public void updateRowHeights() {
        Transposer transposer = getBranch().getRoot().getTransposer();
        int[] preferredRowHeights = new int[getDepth()];
        Dimension pSize = getBranch().getElement().getPreferredSize();
        if (getBranch().getTitleImage() != null) {
            Dimension titleSize = getBranch().getTitleImage().getPreferredSize();
            pSize.height += Math.abs(pSize.height - titleSize.height) / 2;
        }
        preferredRowHeights[0] = transposer.t(pSize).height + getMajorSpacing();

        if (!getBranch().isExpanded()) {
            setPreferredRowHeights(preferredRowHeights);
            return;
        }
        List subtrees = getSubtrees();
        TreeBranch subtree;

        int offset = 1;
        for (int i = 0; i < subtrees.size(); i++) {
            subtree = (TreeBranch) subtrees.get(i);
            int rowHeights[] = subtree.getPreferredRowHeights();
            for (int row = 0; row < rowHeights.length; row++) {
                preferredRowHeights[row + offset] = rowHeights[row];
            }
            offset += subtree.getDepth();
        }
        setPreferredRowHeights(preferredRowHeights);
    }

    public int getGap() {
        return getBranch().getRoot().getMinorSpacing();
    }

    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
        Transposer transposer = getBranch().getRoot().getTransposer();
        Dimension result = transposer.t(getBranch().getElement().getPreferredSize().getCopy());
        result.height = getRowHeight();
        if (getBranch().getTitleImage() != null) {
            result.width += getBranch().getTitleImage().getPreferredSize().width;
        }
        IFigure pane = getBranch().getContentsPane();
        if (!pane.isVisible() || pane.getChildren().isEmpty()) {
            result.width += 14;
            return transposer.t(result);
        }
        Dimension d = transposer.t(getBranch().getContentsPane().getPreferredSize());
        result.width = Math.max(result.width, d.width + getGap() * 2);
        result.height += d.height;
        return transposer.t(result);
    }

    public void layout(IFigure f) {
        TreeAnimation.recordInitialState(f);
        if (TreeAnimation.playbackState(f))
            return;
        getBranch().getContentsPane().validate();

        Transposer transposer = getTransposer();
        Rectangle clientArea = new Rectangle();
        getBranch().getClientArea(clientArea);
        clientArea = transposer.t(clientArea);

        //
        Rectangle elemBounds = new Rectangle();
        elemBounds.setSize(transposer.t(getBranch().getElement().getPreferredSize()));
        elemBounds.height = getRowHeight() - getMajorSpacing();
        //
        int ecX = clientArea.x + 1;
        int ecY = clientArea.y + 4;

        int titleX = clientArea.x + 15;
        int titleY = clientArea.y;
        ImageFigure titleImage = getBranch().getTitleImage();
        if (titleImage != null) {
            Rectangle imgBounds = new Rectangle();
            imgBounds.setSize(transposer.t(titleImage.getPreferredSize()));
            // imgBounds.height = getRowHeight() - getMajorSpacing();
            imgBounds.setLocation(titleX, titleY);
            titleImage.setBounds(transposer.t(imgBounds));

            titleX += imgBounds.width;
            if (elemBounds.height > imgBounds.height / 2) {
                titleY += imgBounds.height / 3;
            }
            if (elemBounds.height > imgBounds.height) {
                ecY = titleY - imgBounds.height / 2;
            }
        }

        //
        if (getBranch().isEnableExpand()) {
            Rectangle ecBounds = new Rectangle();
            ecBounds.setSize(transposer.t(getBranch().getExpandCollapseFigure().getPreferredSize()));
            ecBounds.height = getRowHeight() - getMajorSpacing() - 7;

            ecBounds.setLocation(ecX, ecY);
            getBranch().getExpandCollapseFigure().setBounds(transposer.t(ecBounds));
        }

        elemBounds.setLocation(titleX, titleY);
        getBranch().getElement().setBounds(transposer.t(elemBounds));

        IFigure contents = getBranch().getContentsPane();
        Rectangle contentsBounds = new Rectangle(clientArea.getLocation().translate(getGap() * 2, getRowHeight()), transposer
                .t(contents.getPreferredSize()));
        contents.setBounds(transposer.t(contentsBounds));
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#paintLines(org.eclipse.draw2d.Graphics)
     */
    public void paintLines(Graphics g) {
        int gap = getGap();
        g.setLineStyle(getBranchLineStyle());
        g.setForegroundColor(generateColor());

        IFigure startFig = getBranch().getStartFigure();
        IFigure contents = getBranch().getContentsPane();

        if (getTransposer().isEnabled()) {
            int x = startFig.getBounds().right();
            int y = startFig.getBounds().y + gap;
            List children = contents.getChildren();
            if (children.size() == 0)
                return;
            int right = x;
            for (int i = 0; i < children.size(); i++) {
                TreeBranch treeBranch = (TreeBranch) children.get(i);
                Rectangle childStartBounds = treeBranch.getStartFigure().getBounds();

                Point pt = childStartBounds.getTop();
                g.drawLine(pt.x, y, pt.x, pt.y);
                right = Math.max(right, pt.x);
            }
            g.drawLine(x, y, right, y);

        } else {
            int x = startFig.getBounds().x + gap;
            int y = startFig.getBounds().bottom();
            List children = contents.getChildren();
            if (children.size() == 0)
                return;
            int bottom = y;
            for (int i = 0; i < children.size(); i++) {
                TreeBranch treeBranch = (TreeBranch) children.get(i);
                Rectangle childStartBounds = treeBranch.getStartFigure().getBounds();

                Point pt = childStartBounds.getLeft();
                g.drawLine(x, pt.y, pt.x, pt.y);
                bottom = Math.max(bottom, pt.y);
            }
            g.drawLine(x, y, x, bottom);
        }
    }

    public void updateContours() {
        // Make sure we layout first
        Transposer transposer = getTransposer();
        getBranch().validate();

        int[] cachedContourLeft = new int[getDepth()];
        int[] cachedContourRight = new int[getDepth()];

        Rectangle clientArea = transposer.t(getBranch().getClientArea(Rectangle.SINGLETON));
        Rectangle nodeBounds = transposer.t(getBranch().getElementBounds());
        int rightEdge = clientArea.right();

        cachedContourLeft[0] = nodeBounds.x - clientArea.x;
        cachedContourRight[0] = rightEdge - nodeBounds.right();

        if (!getBranch().isExpanded()) {
            setCachedContourLeft(cachedContourLeft);
            setCachedContourRight(cachedContourRight);
            return;
        }
        List subtrees = getBranch().getContents().getChildren();
        TreeBranch subtree;

        int leftSide = getGap();
        for (int i = 1; i < getDepth(); i++)
            cachedContourLeft[i] = leftSide;

        int rightMargin;
        int offset = 1;
        for (int i = 0; i < subtrees.size(); i++) {
            subtree = (TreeBranch) subtrees.get(i);
            rightMargin = rightEdge - transposer.t(subtree.getBounds()).right();
            int rightContour[] = subtree.getContourRight();
            for (int j = 0; j < rightContour.length; j++) {
                cachedContourRight[j + offset] = rightContour[j] + rightMargin;
            }
            offset += subtree.getDepth();
        }
        setCachedContourLeft(cachedContourLeft);
        setCachedContourRight(cachedContourRight);
    }
}
