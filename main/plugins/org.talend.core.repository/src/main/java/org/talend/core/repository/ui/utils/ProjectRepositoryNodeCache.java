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
package org.talend.core.repository.ui.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.nodes.IProjectRepositoryNode;
import org.talend.utils.string.StringUtilities;

/**
 * Used to cache repository node of project, not contains child folder of repository node, since their key are different
 */
public class ProjectRepositoryNodeCache {

    private Map<String, RepositoryNodeCache> repNodeCacheMap = new HashMap<String, RepositoryNodeCache>();

    public IRepositoryNode getCache(IRepositoryNode repNode) {
        IRepositoryNode returnValue = null;
        ENodeType nodeType = repNode.getType();
        if (nodeType == ENodeType.REPOSITORY_ELEMENT) {
            returnValue = getRepositoryNode(getKey(repNode));
            return returnValue;
        } else if (nodeType == ENodeType.SYSTEM_FOLDER || nodeType == ENodeType.STABLE_SYSTEM_FOLDER) {
            IRepositoryNode repElemNode = getRepositoryElementNode(repNode);
            RepositoryNodeCache cache = getRepositoryNodeCache(getKey(repElemNode));
            if (cache != null) {
                returnValue = cache.getChildFolderCache(repNode);
            }
            return returnValue;
        } else {
            throw new RuntimeException("Not supported type: " + nodeType); //$NON-NLS-1$
        }
    }

    private IRepositoryNode getRepositoryNode(String key) {
        IRepositoryNode repNode = null;
        RepositoryNodeCache cache = getRepositoryNodeCache(key);
        if (cache != null) {
            repNode = cache.getRepositoryNode();
        }
        return repNode;
    }

    private RepositoryNodeCache getRepositoryNodeCache(String key) {
        return repNodeCacheMap.get(key);
    }

    public void addCache(IRepositoryNode repNode, boolean forceUpdate) {
        ENodeType nodeType = repNode.getType();
        if (nodeType == ENodeType.REPOSITORY_ELEMENT) {
            String key = getKey(repNode);
            RepositoryNodeCache nodeCache = repNodeCacheMap.get(key);
            if (nodeCache == null || forceUpdate) {
                nodeCache = new RepositoryNodeCache(repNode);
            }
            repNodeCacheMap.put(key, nodeCache);
        } else if (nodeType == ENodeType.STABLE_SYSTEM_FOLDER || nodeType == ENodeType.SYSTEM_FOLDER) {
            IRepositoryNode repElemNode = getRepositoryElementNode(repNode);
            String key = getKey(repElemNode);
            RepositoryNodeCache nodeCache = repNodeCacheMap.get(key);
            if (nodeCache == null) {
                nodeCache = new RepositoryNodeCache(repElemNode);
                repNodeCacheMap.put(key, nodeCache);
            }
            IRepositoryNode childFolderCache = nodeCache.getChildFolderCache(repNode);
            if (childFolderCache == null || forceUpdate) {
                nodeCache.addChildFolderCache(repNode);
            }
        } else {
            throw new RuntimeException("Not supported node type: " + nodeType); //$NON-NLS-1$
        }
    }

    public IRepositoryNode removeCache(IRepositoryNode repNode) {
        ENodeType nodeType = repNode.getType();
        if (nodeType == ENodeType.REPOSITORY_ELEMENT) {
            String key = getKey(repNode);
            RepositoryNodeCache nodeCache = repNodeCacheMap.remove(key);
            if (nodeCache != null) {
                return nodeCache.getRepositoryNode();
            } else {
                return null;
            }
        } else if (nodeType == ENodeType.STABLE_SYSTEM_FOLDER || nodeType == ENodeType.SYSTEM_FOLDER) {
            IRepositoryNode repElemNode = getRepositoryElementNode(repNode);
            String key = getKey(repElemNode);
            RepositoryNodeCache nodeCache = repNodeCacheMap.get(key);
            if (nodeCache != null) {
                return nodeCache.removeChildFolderCache(repNode);
            } else {
                return null;
            }
        } else {
            throw new RuntimeException("Not supported node type: " + nodeType); //$NON-NLS-1$
        }
    }

    public IRepositoryNode removeCache(String key) {
        RepositoryNodeCache nodeCache = repNodeCacheMap.remove(key);
        if (nodeCache != null) {
            return nodeCache.getRepositoryNode();
        } else {
            return null;
        }
    }

    private IRepositoryNode getRepositoryElementNode(IRepositoryNode childNode) {
        IRepositoryNode repElemNode = childNode;
        while (repElemNode != null && !(repElemNode instanceof IProjectRepositoryNode)) {
            repElemNode = repElemNode.getParent();
            if (repElemNode == null) {
                break;
            }
            ENodeType type = repElemNode.getType();
            if (type == ENodeType.REPOSITORY_ELEMENT) {
                break;
            }
        }
        if (repElemNode == null || repElemNode == childNode) {
            throw new RuntimeException("Can't find repository element"); //$NON-NLS-1$
        }
        return repElemNode;
    }

    private String getKey(IRepositoryNode repNode) {
        ENodeType nodeType = repNode.getType();
        if (nodeType == ENodeType.SIMPLE_FOLDER) {
            throw new RuntimeException("Better to create a FolderNodeCache for " + nodeType); //$NON-NLS-1$
        }
        if (nodeType != ENodeType.REPOSITORY_ELEMENT) {
            throw new RuntimeException("Don't support for " + nodeType); //$NON-NLS-1$
        }
        IRepositoryViewObject repViewObj = repNode.getObject();
        return getKey(repViewObj);
    }

    private String getKey(IRepositoryViewObject repViewObj) {
        /**
         * Also see ResourceCollectorVisitor, the key is used dirrectly.
         */
        // return repViewObj.getProjectLabel() + "/" + repViewObj.getId(); //$NON-NLS-1$
        Property property = repViewObj.getProperty();
        String strPath = property.eResource().getURI().path();
        IPath path = new Path(strPath);
        path = path.removeFirstSegments(1);
        String key = path.toString();
        key = StringUtilities.removeStartingString(key, "/"); //$NON-NLS-1$
        return key;
    }

    public void clearCache() {
        repNodeCacheMap.clear();
    }
}
