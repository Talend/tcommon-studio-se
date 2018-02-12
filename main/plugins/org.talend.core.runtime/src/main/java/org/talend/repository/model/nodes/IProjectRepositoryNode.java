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
package org.talend.repository.model.nodes;

import java.util.List;
import java.util.Map;

import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.repository.model.IRepositoryNode;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public interface IProjectRepositoryNode {

    public org.talend.core.model.general.Project getProject();

    public List<IRepositoryNode> getChildren();

    public IRepositoryNode getRecBinNode();

    public IRepositoryNode getRootRepositoryNode(ERepositoryObjectType type);

    public RepositoryNode getRootRepositoryNode(ERepositoryObjectType type, boolean tryInit);

    public Map<String, RepositoryNode> getGenericTopNodesMap();

    public void dispose();

    public void initNode(final IRepositoryNode rootTypeNode);
}
