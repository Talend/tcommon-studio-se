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
package org.talend.commons.ui.swt.geftree.action;

import org.talend.commons.ui.swt.geftree.figure.TreeBranch;
import org.talend.commons.ui.swt.geftree.layout.TreeAnimatingLayer;

/**
 * cli class global comment. Detailled comment
 */
public class ExpandCollapseAction extends AbstractTreeAction {

    public static final String ID = "org.talend.geftree.action.ExpandCollapseAction";

    public ExpandCollapseAction() {
        super(ID, "Expand/Collapse");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.datalineage.ui.tree.action.AbstractTreeAction#calcEnable()
     */
    @Override
    public boolean calcEnable() {
        TreeBranch selection = getSelection();
        if (selection != null) {
            TreeAnimatingLayer contents = selection.getContents();
            if (contents != null) {
                return !contents.getChildren().isEmpty();
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.datalineage.ui.tree.action.AbstractTreeAction#run()
     */
    @Override
    public void run() {
        TreeBranch selection = getSelection();
        if (selection != null) {
            selection.doExpandCollapse();
        }
    }

}
