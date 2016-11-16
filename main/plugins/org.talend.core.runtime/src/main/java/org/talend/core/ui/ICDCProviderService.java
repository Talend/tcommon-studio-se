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
package org.talend.core.ui;

import org.talend.core.IService;
import org.talend.core.model.metadata.builder.connection.CDCConnection;
import org.talend.core.model.metadata.builder.connection.DatabaseConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.repository.model.RepositoryNode;

public interface ICDCProviderService extends IService {

    void createCDCTypes(RepositoryNode recBinNode, RepositoryNode cdcNode, CDCConnection connection);

    public boolean isSubscriberTableNode(RepositoryNode node);

    public boolean canCreateCDCConnection(DatabaseConnection conn);

    public String getCDCConnectionLinkId(ConnectionItem item);

    public boolean isSystemSubscriberTable(RepositoryNode node);
}
