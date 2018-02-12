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
package org.talend.core.repository.ui.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryNode;

/**
 * 
 * Helper class for restoring folders.
 */
public class RestoreFolderUtil {

    Map<ERepositoryObjectType, Set<String>> foldersMap = new HashMap<ERepositoryObjectType, Set<String>>();

    IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();

    IPath restoreFolderIfNotExists(ERepositoryObjectType type, RepositoryNode node) throws PersistenceException {
        Set<String> folders = getFolders(type);
        String oldPath = node.getObject().getProperty().getItem().getState().getPath();
        return createFolders(folders, new Path(oldPath), type);
    }

    private IPath createFolders(Set<String> folders, IPath path, ERepositoryObjectType type) throws PersistenceException {
        if (folders.contains(path.toString())) {
            return path;
        }

        String lastSegment = path.lastSegment();
        if (lastSegment != null) {
            // create parent folder
            IPath parent = createFolders(folders, path.removeLastSegments(1), type);
            factory.createFolder(type, parent, lastSegment);
            folders.add(path.toString());
            return path;
        } else {
            return new Path(""); //$NON-NLS-1$
        }
    }

    public Set<String> getFolders(ERepositoryObjectType type) throws PersistenceException {
        Set<String> folders = foldersMap.get(type);
        if (folders == null) {
            folders = new HashSet<String>(factory.getFolders(type));
            foldersMap.put(type, folders);
        }
        return folders;
    }

    public void clear() {
        foldersMap.clear();
    }

}
