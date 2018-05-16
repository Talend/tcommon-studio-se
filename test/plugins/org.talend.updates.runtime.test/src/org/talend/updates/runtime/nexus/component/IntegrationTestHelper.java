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
package org.talend.updates.runtime.nexus.component;

import org.talend.core.nexus.NexusServerBean;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class IntegrationTestHelper {

    /**
     * this test is release nexus URI.
     */
    public static NexusServerBean getNexusServerReleaseBean() {
        NexusServerBean propertyNexusServer = NexusServerManager.getInstance().getPropertyNexusServer();
        if (propertyNexusServer != null) { // reuse the property one
            return propertyNexusServer;
        }
        String nexusURL = "http://localhost:8081/nexus";
        String nexusUser = "admin";
        String nexusPass = "xxxx";
        String repositoryId = "components";

        final NexusServerBean nexusServerBean = new NexusServerBean();
        nexusServerBean.setServer(nexusURL);
        nexusServerBean.setUserName(nexusUser);
        nexusServerBean.setPassword(nexusPass);
        nexusServerBean.setRepositoryId(repositoryId);

        return nexusServerBean;
    }
}
