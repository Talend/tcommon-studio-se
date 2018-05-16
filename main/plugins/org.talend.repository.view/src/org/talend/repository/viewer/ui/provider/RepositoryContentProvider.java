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
package org.talend.repository.viewer.ui.provider;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.ui.branding.IBrandingConfiguration;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * Content provider for the repository view.<br/>
 * 
 * $Id$
 * 
 */
public class RepositoryContentProvider implements IStructuredContentProvider, ITreeContentProvider {

    private final IRepositoryView view;

    private final IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    public RepositoryContentProvider(IRepositoryView view) {
        super();
        this.view = view;
    }

    @Override
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
    }

    @Override
    public void dispose() {
    }

    protected IRepositoryView getView() {
        return view;
    }

    @Override
    public Object[] getElements(Object parent) {
        IRepositoryView view2 = getView();
        if (view2 == null || parent.equals(view2.getViewSite())) {
            IProjectRepositoryNode root = getRoot();
            if (root != null) {
                if (root.getChildren().isEmpty()) {
                    initialize();
                }
                return root.getChildren().toArray();
            }
        }
        return getChildren(parent);
    }

    @Override
    public Object getParent(Object child) {

        return ((RepositoryNode) child).getParent();
    }

    @Override
    public Object[] getChildren(Object parent) {
        if (parent == null) {
            return new RepositoryNode[0];
        }
        RepositoryNode repositoryNode = ((RepositoryNode) parent);
        if (repositoryNode.getRoot() instanceof ProjectRepositoryNode) {
            ((ProjectRepositoryNode) repositoryNode.getRoot()).initNode(repositoryNode);
        }

        return repositoryNode.getChildren().toArray();
    }

    @Override
    public boolean hasChildren(Object parent) {
        Boolean boolean1 = factory.hasChildren(parent);
        if (boolean1 != null) {
            return boolean1;
        } else {
            if (parent instanceof RepositoryNode) {
                RepositoryNode repositoryNode = (RepositoryNode) parent;
                if (repositoryNode.isInitialized()) {
                    return repositoryNode.getChildren().size() > 0;
                } else {
                    return getChildren(parent).length > 0;
                }
            }
            return true;
        }
    }

    private void initialize() {
        ProjectRepositoryNode root = getRoot();

        String currentPerspective = IBrandingConfiguration.PERSPECTIVE_DI_ID;

        try {
            IRepositoryView view2 = getView();
            if (view2 != null) {
                currentPerspective = view2.getSite().getPage().getPerspective().getId();
            } else {
                IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (activeWorkbenchWindow != null) {
                    IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
                    if (activePage != null) {
                        currentPerspective = activePage.getPerspective().getId();
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
            // this exception is just in case, since for some specific cases, page can be null (shouldn't happen but..)
        }
        root.initialize(currentPerspective);
    }

    public RepositoryNode getRootRepositoryNode(ERepositoryObjectType type) {
        RepositoryNode rootRepositoryNode = getRoot().getRootRepositoryNode(type);
        if (rootRepositoryNode == null) {
            initialize();
            // re-retrieve
            rootRepositoryNode = getRoot().getRootRepositoryNode(type);
        }
        if (rootRepositoryNode != null && rootRepositoryNode.getChildren().isEmpty()) {
            // retrieve child
            getChildren(rootRepositoryNode);

        }
        return rootRepositoryNode;
    }

    public ProjectRepositoryNode getRoot() {
        return ProjectRepositoryNode.getInstance();
    }
}
