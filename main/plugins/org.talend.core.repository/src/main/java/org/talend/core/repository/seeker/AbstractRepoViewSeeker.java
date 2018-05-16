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
package org.talend.core.repository.seeker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.nodes.IProjectRepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 55206 2011-02-15 17:32:14Z mhirt $
 * 
 */
public abstract class AbstractRepoViewSeeker implements IRepositorySeeker<IRepositoryNode> {

    @Override
    public IRepositoryNode searchNode(TreeViewer viewer, String itemId) {
        if (itemId != null) {
            try {
                IRepositoryViewObject lastVersion = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory()
                        .getLastVersion(itemId);
                if (lastVersion != null) {
                    final ERepositoryObjectType itemType = lastVersion.getRepositoryObjectType();
                    if (validType(itemType)) {
                        ProjectRepositoryNode projectRepositoryNode = ProjectRepositoryNode.getInstance();
                        List<IRepositoryNode> rootTypeRepoNodes = getRootTypeRepositoryNodes(projectRepositoryNode, itemType);
                        for (IRepositoryNode rootNode : rootTypeRepoNodes) {
                            projectRepositoryNode.initNode(rootNode); // before search, init it
                            IRepositoryNode searchedRepoNode = searchRepositoryNode(rootNode, itemId, itemType);
                            // in fact, will search the main project first.
                            if (searchedRepoNode != null) {
                                return searchedRepoNode;
                            }

                        }
                    }
                }
            } catch (PersistenceException e) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    protected IRepositoryNode searchRepositoryNode(IRepositoryNode rootNode, String itemId, final ERepositoryObjectType itemType) {
        if (rootNode != null && itemId != null && itemType != null) {
            // in the first, search the current folder
            List<IRepositoryNode> folderChildren = new ArrayList<IRepositoryNode>();

            List<IRepositoryNode> children = rootNode.getChildren();
            for (IRepositoryNode childNode : children) {
                if (isRepositoryFolder(childNode)) {
                    folderChildren.add(childNode);
                } else if (validNode(childNode, itemId, itemType)) {
                    return childNode;
                }
            }
            // search in the folders
            for (IRepositoryNode folderNode : folderChildren) {
                IRepositoryNode searchedRepoNode = searchRepositoryNode(folderNode, itemId, itemType);
                if (searchedRepoNode != null) {
                    return searchedRepoNode;
                }
            }
        }
        return null;
    }

    protected List<IRepositoryNode> getRootTypeRepositoryNodes(IProjectRepositoryNode root, ERepositoryObjectType itemType) {
        List<IRepositoryNode> rootTypeNodes = new ArrayList<IRepositoryNode>();
        if (root != null && itemType != null) {
            IRepositoryNode rootTypeRepoNode = root.getRootRepositoryNode(itemType);
            if (rootTypeRepoNode != null) {
                rootTypeNodes.add(rootTypeRepoNode);
            }
            IRepositoryNode referenceProjectNode = root.getRootRepositoryNode(ERepositoryObjectType.REFERENCED_PROJECTS, true);
            if (referenceProjectNode != null) {
                List<IRepositoryNode> refProjects = referenceProjectNode.getChildren();
                if (refProjects != null && !refProjects.isEmpty()) {
                    for (IRepositoryNode repositoryNode : refProjects) {
                        if (repositoryNode instanceof IProjectRepositoryNode) {
                            IProjectRepositoryNode refProjectNode = (IProjectRepositoryNode) repositoryNode;
                            List<IRepositoryNode> refRootRepoNodes = getRootTypeRepositoryNodes(refProjectNode, itemType);
                            rootTypeNodes.addAll(refRootRepoNodes);
                        }
                    }
                }
            }
        }
        return rootTypeNodes;
    }

    protected boolean isRepositoryFolder(IRepositoryNode node) {
        if (node == null) {
            return false;
        }
        ENodeType type = node.getType();
        if (type == ENodeType.SIMPLE_FOLDER || type == ENodeType.STABLE_SYSTEM_FOLDER || type == ENodeType.SYSTEM_FOLDER
                || type == ENodeType.REFERENCED_PROJECT) {
            return true;
        }
        return false;
    }

    /**
     * 
     * Eeach implement classes should do the current type
     */
    protected boolean validType(ERepositoryObjectType itemType) {
        if (itemType != null) {
            List<ERepositoryObjectType> validationTypes = getValidationTypes();
            if (validationTypes != null) {
                return validationTypes.contains(itemType);
            }
        }
        return false;
    }

    protected List<ERepositoryObjectType> getValidationTypes() {
        List<ERepositoryObjectType> preExpandTypes = new ArrayList<ERepositoryObjectType>();
        return preExpandTypes;
    }

    protected boolean validNode(IRepositoryNode node, String itemId, ERepositoryObjectType itemType) {
        if (node != null && itemType != null && node.getId().equals(itemId) && itemType.equals(node.getObjectType())) {
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.seeker.IRepositorySeeker#neededExpand()
     */
    @Override
    public boolean neededExpand() {
        return true; // by default
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.seeker.IRepositorySeeker#expandNodes(org.eclipse.jface.viewers.TreeViewer,
     * java.lang.Object, int)
     */
    @Override
    public void expandNode(TreeViewer viewer, IRepositoryNode repoNode, int expandLevel) {
        if (repoNode != null && validType(repoNode.getObjectType())) {
            IProjectRepositoryNode root = repoNode.getRoot();
            if (root != null) {
                /*
                 * expand some node before expand other nodes.
                 * 
                 * Because the parent of some root type node is not the real parent. like all metadatas
                 */
                List<ERepositoryObjectType> preExpandTypes = getPreExpandTypes();
                if (preExpandTypes != null) {
                    for (ERepositoryObjectType preExpandType : preExpandTypes) {
                        IRepositoryNode preExpandNode = root.getRootRepositoryNode(preExpandType);
                        if (preExpandNode != null && viewer != null) {
                            viewer.expandToLevel(preExpandNode, expandLevel);
                        }
                    }
                }
            }
        }
        RepositorySeekerManager seekerManager = RepositorySeekerManager.getInstance();
        seekerManager.expandNode(seekerManager.getRepoTreeViewer(), repoNode, 1);
    }

    /**
     * 
     * Make sure the order is needed for the expanding
     */
    protected List<ERepositoryObjectType> getPreExpandTypes() {
        List<ERepositoryObjectType> preExpandTypes = new ArrayList<ERepositoryObjectType>();
        return preExpandTypes;
    }
}
