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
package org.talend.repository.items.importexport.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.repository.items.importexport.handlers.model.ImportItem;

/**
 */
public abstract class ResourcesManager {

    protected Map<IPath, Object> path2Object = new HashMap<IPath, Object>();

    protected List<IPath> emptyFolders = new ArrayList<IPath>();

    public void add(String path, Object object) {
        path2Object.put(new Path(path), object);
    }

    public void add(IPath path, Object object) {
        path2Object.put(path, object);
    }

    public Set<IPath> getPaths() {
        return path2Object.keySet();
    }

    public boolean isContainsPath(IPath path) {
        return getPaths().contains(path);
    }

    final public InputStream getStream(IPath path) throws IOException {
        return getStream(path, null);
    }

    public void addFolder(String path) {
        boolean isEmptyFolder = true;
        for (IPath objectPath : path2Object.keySet()) {
            if (objectPath.toPortableString().contains(path)) {
                isEmptyFolder = false;
                break;
            }
        }
        if (isEmptyFolder) {
            Path newPath = new Path(path);
            emptyFolders.add(newPath);
        }
    }

    public List<IPath> getEmptyFolders() {
        Collections.sort(emptyFolders, new Comparator<IPath>() {

            @Override
            public int compare(IPath o1, IPath o2) {
                return o1.segmentCount() - o2.segmentCount();
            }
        });
        return emptyFolders;

    }

    public abstract InputStream getStream(IPath path, ImportItem importItem) throws IOException;

    public abstract boolean collectPath2Object(Object root);

    public abstract void closeResource();

    public abstract Object getRoot();
}
