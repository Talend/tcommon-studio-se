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

import org.eclipse.jface.viewers.Viewer;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.model.RepositoryNode;

public abstract class EmptyTopNodeContentProvider extends ProjectRepoDirectChildrenNodeContentProvider {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.LegacyRepositoryContentProvider#getRepositoryNodeChildren(java.lang.Object,
     * org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected Object[] getRepositoryNodeChildren(RepositoryNode repositoryNode) {
        return NO_CHILDREN;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.viewer.content.FolderListenerSingleTopContentProvider#inputChanged(org.eclipse.jface.viewers
     * .Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        // do nothing caus we do not need any notification
    }

    @Override
    public void resetTopLevelNode(RepositoryNode aTopLevelNode) {
        // not need re-init it.
        // super.initAndClear();
    }
}
