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
package org.talend.core.repository.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.ui.internal.navigator.NavigatorContentServiceContentProvider;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.utils.RepositoryManagerHelper;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.repositoryObject.MetadataColumnRepositoryObject;
import org.talend.core.repository.model.repositoryObject.MetadataTableRepositoryObject;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.model.StableRepositoryNode;
import org.talend.repository.ui.views.IRepositoryView;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class RepositoryNodeManager {

    public static void createTables(RepositoryNode node, IRepositoryViewObject repObj, Connection metadataConnection) {
        createTables(node, repObj, metadataConnection, ERepositoryObjectType.METADATA_CON_TABLE);
    }

    public static void createTables(RepositoryNode node, final IRepositoryViewObject repObj, Connection metadataConnection,
            ERepositoryObjectType repositoryObjectType) {
        Set<MetadataTable> tableset = ConnectionHelper.getTables(metadataConnection);
        for (MetadataTable currentTable : tableset) {
            if (currentTable == null) {
                continue;
            }
            RepositoryNode tableNode = createMetatableNode(node, repObj, currentTable, repositoryObjectType);
            if (!SubItemHelper.isDeleted(currentTable)) {
                node.getChildren().add(tableNode);
            }
            if (currentTable.getColumns().size() > 0) {
                createColumns(tableNode, repObj, currentTable);
            }
        }
    }

    public static RepositoryNode createMetatableNode(RepositoryNode node, IRepositoryViewObject repObj, MetadataTable table) {
        return createMetatableNode(node, repObj, table, ERepositoryObjectType.METADATA_CON_TABLE);
    }

    public static RepositoryNode createMetatableNode(RepositoryNode node, IRepositoryViewObject repObj, MetadataTable table,
            ERepositoryObjectType repositoryObjectType) {
        MetadataTableRepositoryObject modelObj = new MetadataTableRepositoryObject(repObj, table);
        RepositoryNode tableNode = new RepositoryNode(modelObj, node, ENodeType.REPOSITORY_ELEMENT);
        tableNode.setProperties(EProperties.LABEL, modelObj.getLabel());
        tableNode.setProperties(EProperties.CONTENT_TYPE, repositoryObjectType);
        return tableNode;
    }

    public static void createColumns(RepositoryNode tableNode, IRepositoryViewObject repObj, MetadataTable metadataTable) {
        createColumns(tableNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_COLUMN);
    }

    public static void createColumns(RepositoryNode tableNode, IRepositoryViewObject repObj, MetadataTable metadataTable,
            String columnsFolderName) {
        createColumns(tableNode, repObj, metadataTable, ERepositoryObjectType.METADATA_CON_COLUMN, columnsFolderName);
    }

    public static void createColumns(RepositoryNode tableNode, IRepositoryViewObject repObj, MetadataTable metadataTable,
            ERepositoryObjectType repositoryObjectType) {
        createColumns(tableNode, repObj, metadataTable, repositoryObjectType, Messages.getString("ProjectRepositoryNode.columns"));
    }

    public static void createColumns(RepositoryNode tableNode, IRepositoryViewObject repObj, MetadataTable metadataTable,
            ERepositoryObjectType repositoryObjectType, String columnsFolderName) {
        List<MetadataColumn> columnList = new ArrayList<MetadataColumn>();
        columnList.addAll(metadataTable.getColumns());
        int num = columnList.size();
        StringBuffer floderName = new StringBuffer();
        floderName.append(columnsFolderName);
        floderName.append("(");//$NON-NLS-1$
        floderName.append(num);
        floderName.append(")");//$NON-NLS-1$
        RepositoryNode columnsNode = new StableRepositoryNode(tableNode, floderName.toString(), ECoreImage.FOLDER_CLOSE_ICON);
        tableNode.getChildren().add(columnsNode);

        for (MetadataColumn column : columnList) {
            if (column == null) {
                continue;
            }
            RepositoryNode columnNode = createMataColumnNode(columnsNode, repObj, column);
            columnsNode.getChildren().add(columnNode);
        }
    }

    public static RepositoryNode createMataColumnNode(RepositoryNode node, IRepositoryViewObject repObj, MetadataColumn column) {
        return createMataColumnNode(node, repObj, column, ERepositoryObjectType.METADATA_CON_COLUMN);
    }

    public static RepositoryNode createMataColumnNode(RepositoryNode node, IRepositoryViewObject repObj, MetadataColumn column,
            ERepositoryObjectType repositoryObjectType) {
        MetadataColumnRepositoryObject columnObj = new MetadataColumnRepositoryObject(repObj, column);
        RepositoryNode columnNode = new RepositoryNode(columnObj, node, ENodeType.REPOSITORY_ELEMENT);
        columnNode.setProperties(EProperties.LABEL, columnObj.getLabel());
        columnNode.setProperties(EProperties.CONTENT_TYPE, repositoryObjectType);
        return columnNode;
    }

    public static Object[] findChildrenFromRepository(RepositoryNode repNode) {
        List<RepositoryNode> children = new ArrayList<RepositoryNode>();
        IRepositoryView repView = RepositoryManagerHelper.findRepositoryView();
        if (repView != null && repView.getViewer() != null) {
            IContentProvider contentProvider = repView.getViewer().getContentProvider();
            if (contentProvider instanceof NavigatorContentServiceContentProvider) {
                NavigatorContentServiceContentProvider navigatorProvider = (NavigatorContentServiceContentProvider) contentProvider;
                Object[] nc = navigatorProvider.getChildren(repNode);
                return nc;

            }
        }
        return new Object[0];
    }

}
