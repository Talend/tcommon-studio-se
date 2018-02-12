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
package org.talend.repository.viewer.content;

import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;

abstract public class SubEmptyTopNodeContentProvider extends ProjectRepoAbstractContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.ProjectRepoAbstractContentProvider#getProjectRepository(org.talend.repository
     * .model.RepositoryNode)
     */
    @Override
    protected ProjectRepositoryNode getProjectRepositoryNode(RepositoryNode element) {
        return ((ProjectRepositoryNode) element.getParent());
    }

    @Override
    protected RepositoryNode extractPotentialRootNode(Object element) {
        return (RepositoryNode) element;
    }

}
