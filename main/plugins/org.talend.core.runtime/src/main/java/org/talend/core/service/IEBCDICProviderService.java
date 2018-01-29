// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
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

import java.util.Map;

import org.talend.core.IProviderService;
import org.talend.core.model.process.INode;
import org.talend.core.model.properties.EbcdicConnectionItem;

/**
 * ggu class global comment. Detailled comment
 */
public interface IEBCDICProviderService extends IProviderService {

    public boolean isEbcdicNode(final INode node);

    public EbcdicConnectionItem getRepositoryItem(final INode node);

    public boolean isRepositorySchemaLine(INode node, Map<String, Object> lineValue);

}
