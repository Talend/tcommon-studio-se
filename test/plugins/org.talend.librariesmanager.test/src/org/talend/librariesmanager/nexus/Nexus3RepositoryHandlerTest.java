// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.nexus;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.NexusServerBean.NexusType;

/**
 * created by wchen on Aug 18, 2017 Detailled comment
 *
 */
public class Nexus3RepositoryHandlerTest {

    @Test
    public void testGetRepositoryURL() {
        NexusServerBean serverBean = new NexusServerBean();
        serverBean.setServer("http://localhost:8081");
        serverBean.setRepositoryId("release-repository");
        serverBean.setSnapshotRepId("snapshot-repository");
        serverBean.setType(NexusType.NEXUS_3.name());
        Nexus3RepositoryHandler handler = new Nexus3RepositoryHandler();
        handler.setArtifactServerBean(serverBean);
        String releaseUrl = handler.getRepositoryURL(true);
        Assert.assertEquals("http://localhost:8081/repository/release-repository/", releaseUrl);
        String snapshotUrl = handler.getRepositoryURL(false);
        Assert.assertEquals("http://localhost:8081/repository/snapshot-repository/", snapshotUrl);
    }

}
