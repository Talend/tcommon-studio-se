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
package org.talend.core.runtime.repository.selection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryTypeSelectionViewerFilter extends ViewerFilter {

    private ERepositoryObjectType repositoryType;

    public RepositoryTypeSelectionViewerFilter(ERepositoryObjectType repositoryType) {
        super();
        this.repositoryType = repositoryType;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            if (ERepositoryObjectType.REFERENCED_PROJECTS.equals(node.getContentType())) {
                return true;
            }
            IRepositoryNode typeNode = node.getRoot().getRootRepositoryNode(repositoryType);
            if (typeNode == node) {
                return true;
            }
            if (repositoryType.equals(node.getContentType())) {
                return true;
            }
            return false;
        }
        return true; // if others, no filter
    }

}
