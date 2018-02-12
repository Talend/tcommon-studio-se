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
package org.talend.core.repository.ui.utils;

import java.util.HashMap;
import java.util.Map;

import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.IRepositoryNode.ENodeType;

/**
 * Used to cache repository node and it's child folders
 */
public class RepositoryNodeCache {
    
    private IRepositoryNode repNode;
    
    private Map<String, IRepositoryNode> folderCacheMap = new HashMap<String, IRepositoryNode>();

    public RepositoryNodeCache(IRepositoryNode repNode) {
        this.repNode = repNode;
    }

    public void clearChildFoldersCache() {
        folderCacheMap.clear();
    }

    private String getFolderKey(IRepositoryNode repNode) {
        String key = null;
        ENodeType nodeType = repNode.getType();
        if (nodeType == ENodeType.STABLE_SYSTEM_FOLDER || nodeType == ENodeType.SYSTEM_FOLDER) {
            key = repNode.getLabel();
        } else {
            throw new RuntimeException("Not support type: " + nodeType); //$NON-NLS-1$
        }
        return key;
    }

    private IRepositoryNode getChildFolderCache(String key) {
        return folderCacheMap.get(key);
    }

    public IRepositoryNode getChildFolderCache(IRepositoryNode childFolderNode) {
        return getChildFolderCache(getFolderKey(childFolderNode));
    }

    private void addChildFolderCache(String key, IRepositoryNode childFolderNode) {
        folderCacheMap.put(key, childFolderNode);
    }

    public void addChildFolderCache(IRepositoryNode childFolderNode) {
        addChildFolderCache(getFolderKey(childFolderNode), childFolderNode);
    }

    public IRepositoryNode removeChildFolderCache(IRepositoryNode childFolderNode) {
        return folderCacheMap.remove(getFolderKey(childFolderNode));
    }

    public IRepositoryNode getRepositoryNode() {
        return repNode;
    }
}
