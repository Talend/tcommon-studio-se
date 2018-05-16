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
package org.talend.repository.items.importexport.ui.wizard.imports.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.repository.items.importexport.wizard.models.FolderImportNode;
import org.talend.repository.items.importexport.wizard.models.ItemImportNode;
import org.talend.repository.items.importexport.wizard.models.ProjectImportNode;
import org.talend.repository.items.importexport.wizard.models.TypeImportNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportItemsViewerSorter extends ViewerSorter {

    private static final int BASE_CATEGORY = 1;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
     */
    @Override
    public int category(Object element) {
        if (element instanceof ProjectImportNode) {
            return BASE_CATEGORY;
        } else if (element instanceof TypeImportNode) {
            return BASE_CATEGORY + 1;
        } else if (element instanceof FolderImportNode) {
            return BASE_CATEGORY + 2;
        } else if (element instanceof ItemImportNode) {
            return BASE_CATEGORY + 3;
        }
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public int compare(Viewer viewer, Object o1, Object o2) {
        // if (o1 instanceof TypeImportNode && o2 instanceof TypeImportNode) {
        // // maybe it's not good to use the ordinal.
        // return ((TypeImportNode) o2).getType().ordinal() - ((TypeImportNode) o1).getType().ordinal();
        // } else if (o1 instanceof TypeImportNode) {
        // return 1;
        // } else if (o2 instanceof TypeImportNode) {
        // return -1;
        //
        // }
        return super.compare(viewer, o1, o2);
    }

}
