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
package org.talend.core.repository.link;

import org.talend.core.repository.seeker.RepositorySeekerManager;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public abstract class AbstractRepoViewLinker implements IRepoViewLinker {

    protected RepositoryNode searchRepoViewNode(String itemId) {
        if (itemId != null) {
            IRepositoryNode searchedNode = RepositorySeekerManager.getInstance().searchRepoViewNode(itemId);
            if (searchedNode instanceof RepositoryNode) {
                return (RepositoryNode) searchedNode;
            }
        }
        return null;
    }

    protected void expandToRepoViewNode(RepositoryNode repoNode) {
        if (repoNode != null) {
            RepositorySeekerManager seekerManager = RepositorySeekerManager.getInstance();
            seekerManager.expandNode(seekerManager.getRepoTreeViewer(), repoNode, 1);
        }
    }

}
