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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;

/**
 * cli class global comment. Detailled comment
 */
@SuppressWarnings("unchecked")
public abstract class TreeBranchLayout extends AbstractLayout {

    private Transposer transposer;

    private final TreeBranch branch;

    private int[] cachedContourLeft;

    private int[] cachedContourRight;

    private int depth = -1;

    private boolean horizontal = true;

    private int[] preferredRowHeights;

    private int rowHeight;

    private int branchLineStyle = SWT.LINE_SOLID;

    private RGB branchLineColor;

    private static Map<RGB, Color> colorsMap = new HashMap<RGB, Color>();

    public TreeBranchLayout(TreeBranch branch) {
        this.branch = branch;
    }

    public int[] getCachedContourLeft() {
        return this.cachedContourLeft;
    }

    public void setCachedContourLeft(int[] cachedContourLeft) {
        this.cachedContourLeft = cachedContourLeft;
    }

    public int[] getCachedContourRight() {
        return this.cachedContourRight;
    }

    public void setCachedContourRight(int[] cachedContourRight) {
        this.cachedContourRight = cachedContourRight;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public TreeBranch getBranch() {
        return this.branch;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setPreferredRowHeights(int[] preferredRowHeights) {
        this.preferredRowHeights = preferredRowHeights;
    }

    public abstract void calculateDepth();

    public int[] getContourLeft() {
        if (cachedContourLeft == null)
            updateContours();
        return cachedContourLeft;
    }

    public int[] getContourRight() {
        if (cachedContourRight == null)
            updateContours();
        return cachedContourRight;
    }

    public int getDepth() {
        if (!branch.isExpanded())
            return 1;
        if (depth == -1)
            calculateDepth();
        return depth;
    }

    public int[] getPreferredRowHeights() {
        if (preferredRowHeights == null)
            updateRowHeights();
        return preferredRowHeights;
    }

    public List getSubtrees() {
        return branch.getContentsPane().getChildren();
    }

    public Transposer getTransposer() {
        if (transposer == null)
            transposer = branch.getRoot().getTransposer();
        return transposer;
    }

    public int getMajorSpacing() {
        return branch.getRoot().getMajorSpacing();
    }

    public void invalidate() {
        preferredRowHeights = null;
        cachedContourLeft = null;
        cachedContourRight = null;
        depth = -1;
        super.invalidate();
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public abstract void paintLines(Graphics g);

    public void paintRows(Graphics g) {

    }

    public void setHorizontal(boolean value) {
        horizontal = value;
    }

    public void setRowHeights(int heights[], int offset) {
        if (rowHeight != heights[offset]) {
            rowHeight = heights[offset];
            branch.revalidate();
        }
    }

    public int getBranchLineStyle() {
        return this.branchLineStyle;
    }

    public void setBranchLineStyle(int branchLineStyle) {
        this.branchLineStyle = branchLineStyle;
    }

    public RGB getBranchLineColor() {
        if (this.branchLineColor == null) {
            this.branchLineColor = ColorConstants.gray.getRGB();
        }
        return this.branchLineColor;
    }

    public void setBranchLineColor(RGB branchLineColor) {
        this.branchLineColor = branchLineColor;
    }

    public Color generateColor() {

        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        final Color result[] = new Color[1];
        final Display tmpDis = display;
        if (display != null) {
            display.syncExec(new Runnable() {

                public void run() {
                    synchronized (result) {
                        RGB rgb = getBranchLineColor();
                        Color color = colorsMap.get(rgb);
                        if (color == null || color.isDisposed()) {
                            color = new Color(tmpDis, rgb);
                            colorsMap.put(rgb, color);
                        }
                        result[0] = color;
                    }
                }
            });
        }
        synchronized (result) {
            return result[0];
        }

    }

    public abstract void updateContours();

    public abstract void updateRowHeights();

}
