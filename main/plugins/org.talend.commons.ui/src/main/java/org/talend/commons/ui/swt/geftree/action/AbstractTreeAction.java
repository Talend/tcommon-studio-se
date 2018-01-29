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
package org.talend.commons.ui.swt.geftree.action;

import org.eclipse.jface.action.Action;
import org.talend.commons.ui.swt.geftree.figure.TreeBranch;

/**
 * cli class global comment. Detailled comment
 */
public abstract class AbstractTreeAction extends Action {

    private String groupId;

    private TreeBranch selection;

    public AbstractTreeAction() {
        super();
        setEnabled(false);
    }

    public AbstractTreeAction(String id, String text) {
        this();
        setId(id);
        setText(text);
    }

    public String getGroupId() {
        return this.groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public TreeBranch getSelection() {
        return this.selection;
    }

    public void init(TreeBranch branch) {
        this.selection = branch;
        this.setEnabled(calcEnable());
    }

    @Override
    public abstract void run();

    public abstract boolean calcEnable();
}
