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
package org.talend.core.service;

import java.util.List;

import org.talend.core.IService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IExternalData;
import org.talend.core.model.process.IExternalNode;
import org.talend.core.model.process.INode;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.node.IExternalMapTable;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public interface IDesignerMapperService extends IService {

    /**
     * 
     * DOC YeXiaowei Comment method "isVirtualComponent".
     * 
     * @param node
     * @return
     */
    public boolean isVirtualComponent(final INode node);

    public void renameJoinTable(IProcess process, IExternalData data, List<String> createdNames);

    public List<String> getJoinTableNames(IExternalData data);

    public void createAutoMappedNode(INode node, IConnection inputConnection, IConnection outputConnection);

    public void updateLink(INode node, IConnection oldConnection, IConnection newConnection);

    /**
     * DOC ycbai Comment method "getRepositorySchemaIds".
     * 
     * @param nodeData
     * @return
     */
    public List<String> getRepositorySchemaIds(AbstractExternalData nodeData);

    /**
     * DOC ycbai Comment method "getRepositorySchemaIds".
     * 
     * @param nodeType
     * @return
     */
    public List<String> getRepositorySchemaIds(IExternalData nodeData);

    public String getRepositorySchemaId(IExternalMapTable table);

    /**
     * DOC ycbai Comment method "updateMapperTableEntries".
     * 
     * @param nodeData
     * @param schemaId
     * @param metadataTable
     */
    public void updateMapperTableEntries(IExternalNode externalNode, String schemaId, IMetadataTable metadataTable);

    public void renameMapperTable(IExternalNode externalNode, String schemaId, String newSchemaId, IMetadataTable metadataTable);

    /**
     * DOC ycbai Comment method "isSameMetadata".
     * 
     * @param nodeData
     * @param schemaId
     * @param metadataTable
     * @return
     */
    public boolean isSameMetadata(IExternalNode externalNode, String schemaId, IMetadataTable metadataTable);

    public List<String> getExpressionFilter(IExternalData nodeData);

}
