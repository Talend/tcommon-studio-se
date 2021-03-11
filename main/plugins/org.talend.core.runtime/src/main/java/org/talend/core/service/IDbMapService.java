// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.talend.core.IService;
import org.talend.core.model.process.IExternalNode;
import org.talend.designer.core.model.utils.emf.talendfile.AbstractExternalData;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * DOC talend class global comment. Detailled comment
 */
public interface IDbMapService extends IService {

    public boolean isDbMapComponent(IExternalNode node);

    public void updateEMFDBMapData(NodeType nodeType, String oldValue, String newValue);

    public void undoConnectionDelete(IExternalNode node, AbstractExternalData oldEmfData, String connectionLabel);
}
