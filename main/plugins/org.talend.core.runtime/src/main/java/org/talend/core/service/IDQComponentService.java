// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;

/**
 * created by talend on Dec 17, 2014 Detailled comment
 *
 */
public interface IDQComponentService extends IService {

    /**
     *
     * Handle component chaged
     *
     * @param oldConnection
     * @param newMetadataTable
     */
    void externalComponentChange(IConnection oldConnection, IMetadataTable newMetadataTable);

    /**
     * Change the attribute of MatchingData and use parameterValue instead of original one.
     *
     * @param node The node which need to be modified
     */
    void correctlyCustomMatcherParameter(NodeType node);

    /**
     * Change the attribute of MatchingData and use Integer value instead of double value.
     * 
     * @param node The node which need to be modified
     */
    void covertConfindWeight2Int(NodeType node);

}
