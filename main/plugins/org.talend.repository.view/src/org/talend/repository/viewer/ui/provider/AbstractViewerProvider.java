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
package org.talend.repository.viewer.ui.provider;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractViewerProvider {

    public AbstractViewerProvider() {
        super();
    }

    protected int getStyle() {
        return SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL;
    }

    public TreeViewer createViewer(final Composite parent) {
        TreeViewer treeViewer = createTreeViewer(parent, getStyle());
        treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

        addProviders(treeViewer);

        checkSorter(treeViewer);

        treeViewer.setInput(getInputRoot(getProjectRepositoryNode()));

        return treeViewer;
    }

    protected void addProviders(TreeViewer treeViewer) {
        //
    }

    protected void checkSorter(TreeViewer treeViewer) {
        //
    }

    protected abstract TreeViewer createTreeViewer(final Composite parent, final int style);

    public IProjectRepositoryNode getProjectRepositoryNode() {
        return ProjectRepositoryNode.getInstance();
    }

    protected IRepositoryNode getInputRoot(final IProjectRepositoryNode projectRepoNode) {
        if (projectRepoNode instanceof IRepositoryNode) {
            return (IRepositoryNode) projectRepoNode;
        }
        return null;
    }
}
