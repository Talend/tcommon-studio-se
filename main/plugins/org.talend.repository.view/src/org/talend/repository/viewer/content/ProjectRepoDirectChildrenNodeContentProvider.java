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
package org.talend.repository.viewer.content;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.Platform;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * this handle content that root node is of type ProjectRepositoryNode
 * */
public abstract class ProjectRepoDirectChildrenNodeContentProvider extends ProjectRepoAbstractContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.ProjectRepoAbstractContentProvider#getProjectRepository(org.talend.repository
     * .model.RepositoryNode)
     */
    @Override
    protected ProjectRepositoryNode getProjectRepositoryNode(RepositoryNode element) {
        Assert.isTrue(element instanceof ProjectRepositoryNode);
        return (ProjectRepositoryNode) element;
    }

    /**
     * DOC sgandon Comment method "getTopLevelNodeFromProjectRepositoryNode".
     * 
     * @param element
     * @return
     */
    @Override
    abstract protected RepositoryNode getTopLevelNodeFromProjectRepositoryNode(ProjectRepositoryNode projectNode);

    @Override
    protected boolean isRootNodeType(Object element) {
        return (element instanceof ProjectRepositoryNode)
                || Platform.getAdapterManager().getAdapter(element, ProjectRepositoryNode.class) != null;
    }

    @Override
    protected RepositoryNode extractPotentialRootNode(Object element) {
        RepositoryNode potentialRootNode = null;
        if (element instanceof ProjectRepositoryNode) {
            potentialRootNode = (ProjectRepositoryNode) element;
        } else {
            potentialRootNode = (ProjectRepositoryNode) Platform.getAdapterManager().getAdapter(element,
                    ProjectRepositoryNode.class);
        }
        return potentialRootNode;
    }

}
