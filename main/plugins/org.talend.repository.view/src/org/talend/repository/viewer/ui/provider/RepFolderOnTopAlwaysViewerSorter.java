// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.Viewer;
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

    public int compare(Viewer viewer, Object e1, Object e2) {
        int cat1 = category(e1);
        int cat2 = category(e2);

        if (cat1 != cat2) {
            return cat1 - cat2;
        }

        String name1 = getLabel(viewer, e1);
        String name2 = getLabel(viewer, e2);

        // use the comparator to compare the strings
        return getComparator().compare(name1, name2);
    }

    private String getLabel(Viewer viewer, Object e1) {
        String name1;
        if (viewer == null || !(viewer instanceof ContentViewer)) {
            name1 = e1.toString();
        } else {
            IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
            if (prov instanceof ILabelProvider) {
                ILabelProvider lprov = (ILabelProvider) prov;
                if (lprov instanceof DecoratingLabelProvider) {
                    // Bug 364735: use the real label provider to avoid unstable
                    // sort behavior if the decoration is running while sorting.
                    // decorations are usually visual aids to the user and
                    // shouldn't be used in ordering.
                    DecoratingLabelProvider dprov = (DecoratingLabelProvider) lprov;
                    lprov = dprov.getLabelProvider();
                }
                name1 = lprov.getText(e1);
            } else {
                name1 = e1.toString();
            }
        }
        if (name1 == null) {
            name1 = "";//$NON-NLS-1$
        }
        return StringUtils.strip(name1, "> "); //$NON-NLS-1$
    }

}
