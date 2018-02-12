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

import org.talend.core.IService;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.process.IConnection;

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
}
