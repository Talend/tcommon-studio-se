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
package org.talend.repository.viewer.ui.provider;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ViewerSorter;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;

/**
 * created by ycbai on 2013-7-24 Detailled comment
 * 
 */
public class MetadataColumnViewerSorter extends ViewerSorter {

    @Override
    public int category(Object element) {
        if (element instanceof RepositoryNode) {
            RepositoryNode node = (RepositoryNode) element;
            if (ERepositoryObjectType.METADATA_CON_COLUMN.equals(node.getProperties(EProperties.CONTENT_TYPE))) {
                RepositoryNode parent = node.getParent().getParent();
                if (parent != null
                        && ERepositoryObjectType.METADATA_CON_TABLE.equals(parent.getProperties(EProperties.CONTENT_TYPE))
                        && parent.getObject() instanceof MetadataTableRepositoryObject) {
                    MetadataTableRepositoryObject tableObject = (MetadataTableRepositoryObject) parent.getObject();
                    MetadataColumnRepositoryObject columnObject = (MetadataColumnRepositoryObject) ((RepositoryNode) element)
                            .getObject();
                    MetadataColumn tColumn = columnObject.getTdColumn();
                    EList<MetadataColumn> columns = tableObject.getTable().getColumns();
                    for (int i = 0; i < columns.size(); i++) {
                        MetadataColumn column = columns.get(i);
                        if (column.getName() != null && column.getName().equals(tColumn.getName())) {
                            return i;
                        }
                    }
                }
            }
        }
        return super.category(element);
    }

}
