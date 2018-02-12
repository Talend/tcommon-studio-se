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
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * created by Talend on Apr 15, 2015 Detailled comment
 *
 */
public interface INOSQLService extends IService {

    public ERepositoryObjectType getNOSQLRepositoryType();

    public boolean isNoSQLConnection(Connection connection);
    
    public boolean isUseReplicaSet(Connection connection);

    public String getMongoDBReplicaSets(Connection connection);
    

}
