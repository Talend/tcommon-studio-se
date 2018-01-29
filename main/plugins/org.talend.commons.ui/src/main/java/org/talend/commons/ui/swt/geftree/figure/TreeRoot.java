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
package org.talend.commons.ui.swt.geftree.figure;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Transposer;
import org.talend.commons.ui.swt.geftree.ITreeAction;

/**
 * cli class global comment. Detailled comment
 */
public class TreeRoot extends TreeBranch {

    private int major = 10;

    private int minor = 10;

    private Transposer transposer = new Transposer();

    private boolean compression;

    public TreeRoot(IFigure title) {
        super(title);
    }

    public TreeRoot(IFigure title, int style) {
        super(title, style);
    }

    public TreeRoot(IFigure title, boolean enableExpand) {
        super(title, enableExpand);
    }

    public TreeRoot(IFigure title, int style, boolean enableExpand) {
        super(title, style, enableExpand);
    }

    public int getMajorSpacing() {
        return major;
    }

    /**
     * @return
     */
    public int getMinorSpacing() {
        return minor;
    }

    public TreeRoot getRoot() {
        return this;
    }

    public Transposer getTransposer() {
        return transposer;
    }

    public boolean isHorizontal() {
        return !transposer.isEnabled();
    }

    /**
     * sets the space (in pixels) between this branch's node and its subtrees.
     */
    public void setMajorSpacing(int value) {
        this.major = value;
        invalidateTree();
        revalidate();
    }

    public void setHorizontal(boolean value) {
        transposer.setEnabled(!value);
        invalidateTree();
        revalidate();
    }

    /**
     * @param i
     */
    public void setMinorSpacing(int i) {
        minor = i;
        invalidateTree();
        revalidate();
    }

    /**
     * @see org.eclipse.draw2d.Figure#validate()
     */
    public void validate() {
        if (isValid())
            return;
        setRowHeights(getPreferredRowHeights(), 0);
        super.validate();
    }

    /**
     * @return
     */
    public boolean isCompressed() {
        return compression;
    }

    /**
     * @param b
     */
    public void setCompression(boolean b) {
        compression = b;
    }

    public void dispose() {
        getSelectManager().setSelection(null);
        if (getElement() instanceof ITreeAction) {
            ((ITreeAction) getElement()).dispose();
        }
        removeAll();
    }
}
