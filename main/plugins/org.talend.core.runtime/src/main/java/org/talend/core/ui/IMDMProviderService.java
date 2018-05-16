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
package org.talend.core.ui;

import java.util.Map;

import org.talend.core.IProviderService;
import org.talend.core.model.metadata.builder.connection.Concept;
import org.talend.core.model.metadata.builder.connection.MDMConnection;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.MDMConnectionItem;

/**
 * DOC hwang class global comment. Detailled comment
 */
public interface IMDMProviderService extends IProviderService {

    public boolean isMDMNode(final INode node);

    public MDMConnectionItem getRepositoryItem(final INode node);

    public boolean isRepositorySchemaLine(INode node, Map<String, Object> lineValue);

    public boolean initConcepts(MDMConnection mdmConn);

    public String getXPathPrefixValue(Concept concept);
}
