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
package org.talend.core.model.utils;

import java.util.List;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.process.IElement;
import org.talend.core.model.process.IElementParameter;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.Item;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.RepositoryNode;

public interface IDragAndDropServiceHandler {

    /**
     * to judge can handle or not(base on connection)
     * 
     * @param connection - connection
     */
    public boolean canHandle(Connection connection);

    /**
     * get parameter value of the connection
     * 
     * @param connection - connection
     * @param value - parameter name
     */
    public Object getComponentValue(Connection connection, String value, IMetadataTable table);

    public Object getComponentValue(Connection connection, String value, IMetadataTable table, String targetComponent);

    /**
     * get components list when you drag&drop a repositoryNode to processEditor
     * 
     * @param item - the Item of the selectedNode
     * @param seletetedNode - the repositoryNode you selected
     * @param type - the type of the selectedNode
     */
    public List<IComponent> filterNeededComponents(Item item, RepositoryNode seletetedNode, ERepositoryObjectType type);

    /**
     * get the default component and input/output names for the quick drag&drop
     * 
     * @param item - the Item of the selectedNode
     * @param type - the type of the selectedNode
     */
    public IComponentName getCorrespondingComponentName(Item item, ERepositoryObjectType type);

    /**
     * set parameter value of the connection
     * 
     * @param connection - connection
     * @param node - node
     * @param param - param
     */
    public void setComponentValue(Connection connection, INode node, IElementParameter param);

    public ERepositoryObjectType getType(String repositoryType);

    /**
     * Set metadata table relevant parameters value of the element.
     * 
     * @param connection
     * @param ele
     * @param metadataTable
     */
    public void handleTableRelevantParameters(Connection connection, IElement ele, IMetadataTable metadataTable);

    /**
     * Check if the table can do data viewer.
     * 
     * @param connection
     * @param metadataTable
     * @return
     */
    public boolean isValidForDataViewer(Connection connection, IMetadataTable metadataTable);

    /**
     * 
     * @param connection
     * @param paramName
     * @return
     */
    public boolean isGenericRepositoryValue(List<ComponentProperties> componentProperties, String paramName);
}
