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
package org.talend.repository.viewer.ui.provider;

import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepFolderOnTopAlwaysViewerSorter extends ViewerSorter {

    @Override
    public int category(Object element) {
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            // the folder will be on top always.
            if (node.getType() == ENodeType.SIMPLE_FOLDER) {
                return Integer.MIN_VALUE + 1;
            } else if (node.getType() == ENodeType.STABLE_SYSTEM_FOLDER || node.getType() == ENodeType.SYSTEM_FOLDER) {
                return Integer.MIN_VALUE; // system folder will be front of the created folder(SIMPLE_FOLDER)
            }
        }
        return super.category(element);
    }
}
