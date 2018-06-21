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
package org.talend.repository.items.importexport.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class DefaultImportExportItemsActionHelper implements IImportExportItemsActionHelper {

    /**
     * DOC ggu DefaultImportExportItemsActionHelper constructor comment.
     */
    public DefaultImportExportItemsActionHelper() {
        //
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper#isImportEnabled(org.eclipse
     * .jface.viewers.TreeViewer, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public boolean isImportEnabled(TreeViewer viewer, ISelection selection) {
        return isImportEnabledForSelection(selection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper#isImportEnabled(org.eclipse
     * .jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public boolean isImportEnabled(IAction action, ISelection selection) {
        return isImportEnabledForSelection(selection);
    }

    protected boolean isImportEnabledForSelection(ISelection selection) {
        boolean enabled = false;
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structSelection = (IStructuredSelection) selection;
            IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

            // don't support multi-selections
            if (structSelection != null && structSelection.size() == 1) {
                // must be RepositoryNode
                Object firstElement = structSelection.getFirstElement();
                if (firstElement instanceof RepositoryNode) {
                    RepositoryNode repositoryNode = (RepositoryNode) firstElement;
                    // if not readonly and in main project(non-ref-Project).
                    if (!factory.isUserReadOnlyOnCurrentProject()
                            && ProjectManager.getInstance().isInCurrentMainProject(repositoryNode)) {
                        // enable on folder
                        if (ENodeType.SYSTEM_FOLDER.equals(repositoryNode.getType())
                                || ENodeType.SIMPLE_FOLDER.equals(repositoryNode.getType())
                                || ERepositoryObjectType.METADATA.equals(repositoryNode.getContentType())
                                || ERepositoryObjectType.CODE.equals(repositoryNode.getContentType())) {
                            enabled = true;
                            // if folder is logic deleted, disable it.
                            if (repositoryNode.getObject() != null && repositoryNode.getObject().isDeleted()) {
                                enabled = false;
                            }
                            if (ERepositoryObjectType.REFERENCED_PROJECTS.equals(repositoryNode.getProperties(EProperties.CONTENT_TYPE))) {
                                enabled = false;
                            }
                        }
                    }
                }
            }
        }
        return enabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper#isExportEnabled(org.eclipse
     * .jface.viewers.TreeViewer, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public boolean isExportEnabled(TreeViewer viewer, ISelection selection) {
        return isExportEnabledForSelection(selection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper#isExportEnabled(org.eclipse
     * .jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public boolean isExportEnabled(IAction action, ISelection selection) {
        return isExportEnabledForSelection(selection);
    }

    protected boolean isExportEnabledForSelection(ISelection selection) {
        return false;
    }

}
