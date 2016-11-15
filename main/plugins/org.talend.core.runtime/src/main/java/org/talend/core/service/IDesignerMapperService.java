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

public interface IDesignerMapperService extends IService {

    public boolean isVirtualComponent(final INode node);

    public void renameJoinTable(IProcess process, IExternalData data, List<String> createdNames);

    public List<String> getJoinTableNames(IExternalData data);

    public void createAutoMappedNode(INode node, IConnection inputConnection, IConnection outputConnection);

    public void updateLink(INode node, IConnection oldConnection, IConnection newConnection);

    public List<String> getRepositorySchemaIds(AbstractExternalData nodeData);

    public List<String> getRepositorySchemaIds(IExternalData nodeData);

    public String getRepositorySchemaId(IExternalMapTable table);

    public void updateMapperTableEntries(IExternalNode externalNode, String schemaId, IMetadataTable metadataTable);

    public void renameMapperTable(IExternalNode externalNode, String schemaId, String newSchemaId, IMetadataTable metadataTable);

    public boolean isSameMetadata(IExternalNode externalNode, String schemaId, IMetadataTable metadataTable);

    public List<String> getExpressionFilter(IExternalData nodeData);

}
