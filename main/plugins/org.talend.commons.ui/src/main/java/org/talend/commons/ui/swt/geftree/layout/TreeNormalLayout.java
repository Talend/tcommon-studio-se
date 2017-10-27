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
package org.talend.commons.ui.swt.geftree.layout;

import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
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
public class TreeNormalLayout extends TreeBranchLayout {

    public TreeNormalLayout(TreeBranch branch) {
        super(branch);
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#calculateDepth()
     */
    public void calculateDepth() {
        int depth = 0;
        List subtrees = getSubtrees();
        for (int i = 0; i < subtrees.size(); i++)
            depth = Math.max(depth, ((TreeBranch) subtrees.get(i)).getDepth());
        depth++;
        setDepth(depth);
    }

    protected Dimension calculatePreferredSize(IFigure container, int wHint, int hHint) {
        Rectangle union = getBranch().getElementBounds().getCopy();
        // if (branch.isExpanded())
        union.union(getBranch().getContentsPane().getBounds());

        return union.getSize();
    }

    public void layout(IFigure f) {
        TreeAnimation.recordInitialState(f);
        if (TreeAnimation.playbackState(f))
            return;

        Transposer transposer = getTransposer();
        IFigure contents = getBranch().getContentsPane();
        IFigure element = getBranch().getElement();
        contents.validate();

        Rectangle branchBounds = transposer.t(getBranch().getBounds());
        Point topLeft = branchBounds.getTopLeft();
        Rectangle nodeLocation = new Rectangle(topLeft, transposer.t(element.getPreferredSize()));
        nodeLocation.height = getRowHeight() - getMajorSpacing();

        if (!contents.isVisible() || contents.getChildren().isEmpty()) {
            nodeLocation.x += (branchBounds.width - nodeLocation.width) / 2;
            element.setBounds(transposer.t(nodeLocation));
            contents.setBounds(transposer.t(nodeLocation.getTranslated(0, getRowHeight()).setSize(0, 0)));
            return;
        }

        Rectangle contentsLocation = new Rectangle(topLeft, transposer.t(contents.getPreferredSize()));
        contents.setSize(contents.getPreferredSize());
        contentsLocation.y += getRowHeight();

        TreeBranch firstChild = (TreeBranch) contents.getChildren().get(0);
        TreeBranch lastChild = (TreeBranch) contents.getChildren().get(contents.getChildren().size() - 1);
        int leftInset = firstChild.getContourLeft()[0] + transposer.t(firstChild.getBounds()).x
                - transposer.t(contents.getBounds()).x;
        int rightInset = lastChild.getContourRight()[0] - transposer.t(lastChild.getBounds()).right()
                + transposer.t(contents.getBounds()).right();
        rightInset = Math.max(rightInset, 0);
        leftInset = Math.max(leftInset, 0);
        int childrenSpan = contentsLocation.width - leftInset - rightInset;

        switch (getBranch().getAlignment()) {
        case PositionConstants.CENTER:
            leftInset += (childrenSpan - nodeLocation.width) / 2;
        }

        if (leftInset > 0)
            nodeLocation.x += leftInset;
        else
            contentsLocation.x -= leftInset;

        int adjust = branchBounds.width - Rectangle.SINGLETON.setBounds(contentsLocation).union(nodeLocation).width;
        adjust /= 2;
        nodeLocation.x += adjust;
        contentsLocation.x += adjust;
        element.setBounds(transposer.t(nodeLocation));
        // Animation.setBounds(node, transposer.t(nodeLocation));
        // Animation.setBounds(contents, transposer.t(contentsLocation));
        contents.setBounds(transposer.t(contentsLocation));
    }

    public void mergeContour(int[] destination, int[] source, int startdepth, int offset) {
        for (int i = startdepth; i < source.length; i++)
            destination[i + 1] = source[i] + offset;
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#paintLines(org.eclipse.draw2d.Graphics)
     */
    public void paintLines(Graphics g) {
        g.setLineStyle(getBranchLineStyle());
        g.setForegroundColor(generateColor());

        if (getTransposer().isEnabled()) {
            IFigure element = getBranch().getElement();
            int left = element.getBounds().right();
            int right = getBranch().getContentsPane().getBounds().x - 1;
            int yMid = element.getBounds().getCenter().y;
            int xMid = (left + right) / 2;
            List children = getSubtrees();
            if (children.size() == 0)
                return;
            g.drawLine(left, yMid, xMid, yMid);
            int yMin = yMid;
            int yMax = yMid;
            for (int i = 0; i < children.size(); i++) {
                int y = ((TreeBranch) children.get(i)).getElementBounds().getCenter().y;
                g.drawLine(xMid, y, right, y);
                yMin = Math.min(yMin, y);
                yMax = Math.max(yMax, y);
            }
            g.drawLine(xMid, yMin, xMid, yMax);

        } else {
            IFigure element = getBranch().getElement();
            int xMid = element.getBounds().getCenter().x;
            int top = element.getBounds().bottom();
            int bottom = getBranch().getContentsPane().getBounds().y - 1;
            int yMid = (top + bottom) / 2;
            List children = getSubtrees();
            if (children.size() == 0)
                return;
            g.drawLine(xMid, top, xMid, yMid);
            int xMin = xMid;
            int xMax = xMid;
            for (int i = 0; i < children.size(); i++) {
                int x = ((TreeBranch) children.get(i)).getElementBounds().getCenter().x;
                g.drawLine(x, yMid, x, bottom);
                xMin = Math.min(xMin, x);
                xMax = Math.max(xMax, x);
            }
            g.drawLine(xMin, yMid, xMax, yMid);
        }
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#updateContours()
     */

    public void updateContours() {
        Transposer transposer = getTransposer();
        // Make sure we layout first
        getBranch().validate();

        int[] cachedContourLeft = new int[getDepth()];
        int[] cachedContourRight = new int[getDepth()];

        Rectangle clientArea = transposer.t(getBranch().getElementBounds().getUnion(getBranch().getContents().getBounds()));
        Rectangle nodeBounds = transposer.t(getBranch().getElementBounds());
        // Rectangle contentsBounds = transposer.t(getBranch().getContentsPane().getBounds());

        cachedContourLeft[0] = nodeBounds.x - clientArea.x;
        cachedContourRight[0] = clientArea.right() - nodeBounds.right();
        if (!getBranch().isExpanded()) {
            setCachedContourLeft(cachedContourLeft);
            setCachedContourRight(cachedContourRight);
            return;
        }

        List subtrees = getSubtrees();
        TreeBranch subtree;

        int currentDepth = 0;
        for (int i = 0; i < subtrees.size() && currentDepth < getDepth(); i++) {
            subtree = (TreeBranch) subtrees.get(i);
            if (subtree.getDepth() > currentDepth) {
                int leftContour[] = subtree.getContourLeft();
                int leftOffset = transposer.t(subtree.getBounds()).x - clientArea.x;
                mergeContour(cachedContourLeft, leftContour, currentDepth, leftOffset);
                currentDepth = subtree.getDepth();
            }
        }

        currentDepth = 0;
        for (int i = subtrees.size() - 1; i >= 0 && currentDepth < getDepth(); i--) {
            subtree = (TreeBranch) subtrees.get(i);
            if (subtree.getDepth() > currentDepth) {
                int rightContour[] = subtree.getContourRight();
                int rightOffset = clientArea.right() - transposer.t(subtree.getBounds()).right();
                mergeContour(cachedContourRight, rightContour, currentDepth, rightOffset);
                currentDepth = subtree.getDepth();
            }
        }
        setCachedContourLeft(cachedContourLeft);
        setCachedContourRight(cachedContourRight);
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#updateRowHeights()
     */
    public void updateRowHeights() {
        Transposer transposer = getTransposer();
        int[] preferredRowHeights = new int[getDepth()];

        preferredRowHeights[0] = transposer.t(getBranch().getElement().getPreferredSize()).height + getMajorSpacing();

        if (!getBranch().isExpanded()) {
            setPreferredRowHeights(preferredRowHeights);
            return;
        }
        List subtrees = getSubtrees();
        TreeBranch subtree;

        for (int i = 0; i < subtrees.size(); i++) {
            subtree = (TreeBranch) subtrees.get(i);
            int rowHeights[] = subtree.getPreferredRowHeights();
            for (int row = 0; row < rowHeights.length; row++)
                preferredRowHeights[row + 1] = Math.max(preferredRowHeights[row + 1], rowHeights[row]);
        }
        setPreferredRowHeights(preferredRowHeights);
    }

    /**
     * @see org.talend.commons.ui.swt.geftree.layout.TreeBranchLayout.tree.BranchLayout#setRowHeights(int[], int)
     */
    public void setRowHeights(int[] heights, int offset) {
        super.setRowHeights(heights, offset);
        if (getBranch().isExpanded()) {
            List subtrees = getSubtrees();
            offset++;
            for (int i = 0; i < subtrees.size(); i++)
                ((TreeBranch) subtrees.get(i)).setRowHeights(heights, offset);
        }
    }

}
