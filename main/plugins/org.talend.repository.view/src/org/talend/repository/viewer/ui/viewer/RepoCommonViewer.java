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
package org.talend.repository.viewer.ui.viewer;

import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.navigator.CommonViewer;
import org.eclipse.ui.navigator.INavigatorContentService;
import org.talend.repository.viewer.ui.provider.AgentTreeViewerListener;
import org.talend.repository.viewer.ui.provider.INavigatorContentServiceProvider;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepoCommonViewer extends CommonViewer implements ITreeViewerListener, INavigatorContentServiceProvider {

    private AgentTreeViewerListener agent = new AgentTreeViewerListener();

    public RepoCommonViewer(String viewerId, Composite aParent, int aStyle) {
        super(viewerId, aParent, aStyle);
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
    public INavigatorContentService getNavigatorContentService() {
        return super.getNavigatorContentService();
    }

}
