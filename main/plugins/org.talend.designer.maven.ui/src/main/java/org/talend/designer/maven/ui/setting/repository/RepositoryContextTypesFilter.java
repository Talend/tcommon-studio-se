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
package org.talend.designer.maven.ui.setting.repository;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryContextTypesFilter extends ViewerFilter {

    private List<ERepositoryObjectType> contentTypes;

    public RepositoryContextTypesFilter(List<ERepositoryObjectType> contentTypes) {
        super();
        this.contentTypes = contentTypes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof TreeItem) {
            Object data = ((TreeItem) element).getData();
            if (parentElement instanceof TreeItem) {
                Object parentData = ((TreeItem) parentElement).getData();
                return select(viewer, parentData, data);
            }
            return select(viewer, parentElement, data);
        } else if (element instanceof RepositoryNode) {
            RepositoryNode node = ((RepositoryNode) element);
            // only for folders , not for element nodes.
            if (!ENodeType.REPOSITORY_ELEMENT.equals(node.getType())) {
                // add all or only the content types
                if (contentTypes == null || contentTypes.contains(node.getContentType())) {
                    return true;
                }
            }
        }
        return false;
    }

}
