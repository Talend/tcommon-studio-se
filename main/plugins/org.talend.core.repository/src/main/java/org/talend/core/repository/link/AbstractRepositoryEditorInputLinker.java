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

import org.eclipse.ui.IEditorInput;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.IRepositoryEditorInput;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public abstract class AbstractRepositoryEditorInputLinker extends AbstractRepoViewLinker {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.link.IRepoViewLinker#getRelationNode(org.eclipse.ui.IEditorInput)
     */
    @Override
    public RepositoryNode getRelationNode(IEditorInput editorInput) {
        RepositoryNode repositoryNode = null;

        if (isValidEditorInput(editorInput)) {
            IRepositoryEditorInput repoEditorInput = (IRepositoryEditorInput) editorInput;
            // search always
            repositoryNode = searchRepoViewNode(repoEditorInput.getId());

            // repositoryNode = repoEditorInput.getRepositoryNode();
            // if (repositoryNode == null) {
            // // have done expand when search
            // repositoryNode = searchRepoViewNode(repoEditorInput.getId());
            // // } else {// old way to retrive.
            // // repoEditorInput.setRepositoryNode(null);
            // // repositoryNode = repoEditorInput.getRepositoryNode();
            // } else {
            // expandToRepoViewNode(repositoryNode);
            // }
        }
        return repositoryNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.link.IRepoViewLinker#isRelation(org.eclipse.ui.IEditorInput, java.lang.String)
     */
    @Override
    public boolean isRelation(IEditorInput editorInput, String repoNodeId) {
        return isRelation(editorInput, repoNodeId, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.link.IRepoViewLinker#isRelation(org.eclipse.ui.IEditorInput, java.lang.String,
     * java.lang.String)
     */
    @Override
    public boolean isRelation(IEditorInput editorInput, String repoNodeId, String version) {
        // only work for IRepositoryEditorInput
        boolean isRelation = false;
        if (repoNodeId != null && isValidEditorInput(editorInput)) {
            Item item = ((IRepositoryEditorInput) editorInput).getItem();
            if (item != null) {
                // same as repository item id
                isRelation = repoNodeId.equals(item.getProperty().getId());
                if (version != null) {
                    isRelation = isRelation && version.equals(item.getProperty().getVersion());
                }
            }
        }

        return isRelation;
    }

    protected boolean isValidEditorInput(IEditorInput editorInput) {
        Class<? extends IRepositoryEditorInput> repoEditorInputClass = getRepoEditorInputClass();
        if (editorInput != null && repoEditorInputClass != null && editorInput.getClass().equals(repoEditorInputClass)) {
            return true;
        }
        return false;
    }

    protected abstract Class<? extends IRepositoryEditorInput> getRepoEditorInputClass();

}
