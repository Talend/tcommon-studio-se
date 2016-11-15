// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.view.sorter;

import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.talend.repository.model.RepositoryNode;

public abstract class RepositoryNodeCompareSorter extends RepositoryCompareSorter {

    @Override
    public void sort(Viewer viewer, Object parentPath, Object[] children) {
        if (parentPath instanceof TreePath) {
            Object lastSegm = ((TreePath) parentPath).getLastSegment();
            if (lastSegm instanceof RepositoryNode) {
                sortChildren((RepositoryNode) lastSegm, children);
            }
        } else if (parentPath instanceof RepositoryNode) {
            sortChildren((RepositoryNode) parentPath, children);
        }
    }

    protected void sortChildren(RepositoryNode parent, Object[] children) {
        sortChildren(children);
    }
}
