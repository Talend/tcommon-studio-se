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
package org.talend.core.services;

import java.util.Map;

import org.talend.core.IProviderService;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.FTPConnectionItem;

/**
 * DOC hwang class global comment. Detailled comment
 */
public interface IFTPProviderService extends IProviderService {

    public boolean isFTPNode(final INode node);

    public FTPConnectionItem getRepositoryItem(final INode node);

    public boolean isRepositorySchemaLine(INode node, Map<String, Object> lineValue);
}
