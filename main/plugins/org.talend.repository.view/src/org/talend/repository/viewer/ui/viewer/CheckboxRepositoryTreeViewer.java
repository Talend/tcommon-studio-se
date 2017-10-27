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
package org.talend.repository.viewer.ui.viewer;

import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.talend.repository.viewer.ui.provider.AgentTreeViewerListener;

/**
 * DOC hcw class global comment. Detailled comment
 */
public class CheckboxRepositoryTreeViewer extends ContainerCheckedTreeViewer implements ITreeViewerListener {

    private AgentTreeViewerListener agent = new AgentTreeViewerListener();

    private TreeItem lastClickedItem = null;

    public CheckboxRepositoryTreeViewer(Composite parent, int style) {
        super(parent, style);
    }

    protected boolean getExpanded(Item item) {
        agent.getExpanded(item);
        return super.getExpanded(item);
    }

    public void setExpandedState(Object elementOrTreePath, boolean expanded) {
        if (expanded) {
            agent.internalExpand(elementOrTreePath);
        } else {
            agent.internalCollapse(elementOrTreePath);
        }
        super.setExpandedState(elementOrTreePath, expanded);
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        agent.treeCollapsed(event);

    }

    public void treeExpanded(TreeExpansionEvent event) {
        agent.treeExpanded(event);
    }

    @Override
    protected void handleSelect(SelectionEvent event) {
        lastClickedItem = null;
        if (event.detail == SWT.CHECK) {
            TreeItem item = (TreeItem) event.item;
            lastClickedItem = item;
        }
        super.handleSelect(event);
    }

    public TreeItem getLastClickedItem() {
        return this.lastClickedItem;
    }

}
