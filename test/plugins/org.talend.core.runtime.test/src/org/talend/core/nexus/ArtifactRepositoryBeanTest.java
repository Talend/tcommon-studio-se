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
package org.talend.core.nexus;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.nexus.ArtifactRepositoryBean.NexusType;

/**
 * DOC jding  class global comment. Detailled comment
 */
public class ArtifactRepositoryBeanTest {

    @Test
    public void testSplitRepositoryUrl() {
        String repositoryType = ArtifactRepositoryBean.NexusType.ARTIFACTORY.name();
        // default contextpath artifactory
        String urlCase = "http://localhost:8081/artifactory/Releases/";
        // non-default contextpath
        String urlCase1 = "http://localhost:8081/artifacts/Releases/";
        // root contextpath
        String urlCase2 = "http://localhost:8081/Releases/";

        String[] splitedUrl = NexusType.splitRepositoryUrl(urlCase, repositoryType);
        Assert.assertEquals("http://localhost:8081/artifactory/", splitedUrl[0]);
        Assert.assertEquals("Releases", splitedUrl[1]);

        String[] splitedUrl_1 = NexusType.splitRepositoryUrl(urlCase1, repositoryType);
        Assert.assertEquals("http://localhost:8081/artifacts/", splitedUrl_1[0]);
        Assert.assertEquals("Releases", splitedUrl_1[1]);

        String[] splitedUrl_2 = NexusType.splitRepositoryUrl(urlCase2, repositoryType);
        Assert.assertEquals("http://localhost:8081/", splitedUrl_2[0]);
        Assert.assertEquals("Releases", splitedUrl_2[1]);
    }

}
