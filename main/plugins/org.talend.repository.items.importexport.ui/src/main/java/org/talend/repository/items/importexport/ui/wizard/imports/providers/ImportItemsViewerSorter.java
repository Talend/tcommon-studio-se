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
package org.talend.repository.items.importexport.ui.wizard.imports.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.commons.utils.Version;
import org.talend.repository.items.importexport.wizard.models.FolderImportNode;
import org.talend.repository.items.importexport.wizard.models.ImportNode;
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
    	if(o1 instanceof ImportNode && o2 instanceof ImportNode) {

    		if(((ImportNode)o1).getItemRecord() != null && ((ImportNode)o2).getItemRecord() != null 
        			&& ((ImportNode)o1).getItemRecord().getProperty() != null 
        			&& ((ImportNode)o2).getItemRecord().getProperty() != null) {
        		String label1 = ((ImportNode)o1).getItemRecord().getProperty().getLabel();
                String label2 = ((ImportNode)o2).getItemRecord().getProperty().getLabel();
                if (label1 == null) {
                    return -1;
                }
                if (label2 == null) {
                    return 1;
                }
                if(label1.equals(label2)) {
                	String version1 = ((ImportNode)o1).getItemRecord().getProperty().getVersion();
                	String version2 = ((ImportNode)o2).getItemRecord().getProperty().getVersion();
                	
                	if (version1 == null) {
                        return -1;
                    }
                    if (version2 == null) {
                        return 1;
                    }
                    return new Version(version1).compareTo(new Version(version2));
                }
            }
    	}
        return super.compare(viewer, o1, o2);
    }

}
