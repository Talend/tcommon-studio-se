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
package org.talend.commons.ui.swt.geftree;

import org.eclipse.draw2d.IFigure;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;

/**
 * cli class global comment. Detailled comment
 */
public class TreeSelectManager {

    private static TreeSelectManager manager;

    private TreeSelectManager() {
        //
    }

    public static TreeSelectManager getManager() {
        if (manager == null) {
            manager = new TreeSelectManager();
        }
        return manager;
    }

    private TreeBranch selection;

    public TreeBranch getSelection() {
        return this.selection;
    }

    public void setSelection(TreeBranch selection) {
        select(this.selection, false); // de-select the old selection

        this.selection = selection;

        select(this.selection, true); // select the new one.

    }

    private void select(TreeBranch selection, boolean selected) {
        if (selection != null) {
            IFigure element = selection.getElement();
            if (element != null && element instanceof ITreeAction) {
                ((ITreeAction) element).setSelected(selected);
            }
        }
    }

    public IFigure getSelectElement() {
        if (getSelection() != null) {
            return getSelection().getElement();
        }
        return null;
    }
}
