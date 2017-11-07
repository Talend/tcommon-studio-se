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
package org.talend.designer.maven.utils;

import org.junit.Assert;
import org.junit.Test;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenUrlHelperTest {

    @Test
    public void testParseMvnUrl4InvalidPrefix() {
        Assert.assertNull(MavenUrlHelper.parseMvnUrl(null));
        Assert.assertNull(MavenUrlHelper.parseMvnUrl(""));
        Assert.assertNull(MavenUrlHelper.parseMvnUrl("abc"));
    }

    @Test
    public void testParseMvnUrl4NoArtifactId() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc");
        Assert.assertNull(artifact);
    }

    @Test
    public void testParseMvnUrl4GroupArtifact() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("LATEST", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertNull(artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4Version() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz/6.0.0");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("6.0.0", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertNull(artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4VersionLatest() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz/LATEST");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("LATEST", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertNull(artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4VersionRange() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz/(5.6,6.0]");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("(5.6,6.0]", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertNull(artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4Type() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz/6.0.0/zip");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("6.0.0", artifact.getVersion());
        Assert.assertEquals("zip", artifact.getType());
        Assert.assertNull(artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4Classifier() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz/6.0.0/zip/source");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("6.0.0", artifact.getVersion());
        Assert.assertEquals("zip", artifact.getType());
        Assert.assertEquals("source", artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4ALl() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:org.talend/org.talend.routines/6.0.0/zip/source");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("org.talend", artifact.getGroupId());
        Assert.assertEquals("org.talend.routines", artifact.getArtifactId());
        Assert.assertEquals("6.0.0", artifact.getVersion());
        Assert.assertEquals("zip", artifact.getType());
        Assert.assertEquals("source", artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4ClassifierWithoutType() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz/6.0.0//source");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("6.0.0", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertEquals("source", artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4ClassifierWithoutVersion() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz//zip/source");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("LATEST", artifact.getVersion());
        Assert.assertEquals("zip", artifact.getType());
        Assert.assertEquals("source", artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4ClassifierWithoutVersionAndType() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:abc/xyz///source");
        Assert.assertNotNull(artifact);
        Assert.assertNull(artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("LATEST", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertEquals("source", artifact.getClassifier());
    }

    @Test
    public void testParseMvnUrl4RepoURl() {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl("mvn:XXXX!abc/xyz");
        Assert.assertNotNull(artifact);
        Assert.assertEquals("XXXX", artifact.getRepositoryUrl());
        Assert.assertEquals("abc", artifact.getGroupId());
        Assert.assertEquals("xyz", artifact.getArtifactId());
        Assert.assertEquals("LATEST", artifact.getVersion());
        Assert.assertEquals("jar", artifact.getType());
        Assert.assertNull(artifact.getClassifier());
    }

    @Test
    public void testBuildMvnUrlByJarName() {
        Assert.assertNull(MavenUrlHelper.generateMvnUrlForJarName(null));
        Assert.assertNull(MavenUrlHelper.generateMvnUrlForJarName(""));
        // without snapshot
        String mvnUrl1 = MavenUrlHelper.generateMvnUrlForJarName("test.jar");
        Assert.assertEquals("mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar", mvnUrl1);
        // with type
        String mvnUrl11 = MavenUrlHelper.generateMvnUrlForJarName("test.jar", true, false);
        Assert.assertEquals("mvn:org.talend.libraries/test/6.0.0/jar", mvnUrl11);
        // without type and snapshot
        String mvnUrl12 = MavenUrlHelper.generateMvnUrlForJarName("test.jar", false, false);
        Assert.assertEquals("mvn:org.talend.libraries/test/6.0.0", mvnUrl12);
        // with type and snapshot
        String mvnUrl13 = MavenUrlHelper.generateMvnUrlForJarName("test.jar", true, true);
        Assert.assertEquals("mvn:org.talend.libraries/test/6.0.0-SNAPSHOT/jar", mvnUrl13);

        String mvnUrl2 = MavenUrlHelper.generateMvnUrlForJarName("abc");
        Assert.assertEquals("mvn:org.talend.libraries/abc/6.0.0-SNAPSHOT", mvnUrl2);

        String mvnUrl3 = MavenUrlHelper.generateMvnUrlForJarName("abc.zip");
        Assert.assertEquals("mvn:org.talend.libraries/abc/6.0.0-SNAPSHOT/zip", mvnUrl3);

        String mvnUrl4 = MavenUrlHelper.generateMvnUrlForJarName(".zip");
        Assert.assertNull(mvnUrl4);

        String mvnUrl5 = MavenUrlHelper.generateMvnUrlForJarName("test-6.1.0-SNAPSHOT.jar");
        Assert.assertEquals("mvn:org.talend.libraries/test-6.1.0-SNAPSHOT/6.0.0-SNAPSHOT/jar", mvnUrl5);

    }

    @Test
    public void testGenerateMvnUrl() {
        String mvnUrl = MavenUrlHelper.generateMvnUrl("org.talend", "test", null, null, null);
        Assert.assertEquals("mvn:org.talend/test", mvnUrl);
    }

    @Test
    public void testGenerateMvnUrl4Version() {
        String mvnUrl = MavenUrlHelper.generateMvnUrl("org.talend", "test", "6.0", null, null);
        Assert.assertEquals("mvn:org.talend/test/6.0", mvnUrl);
    }

    @Test
    public void testGenerateMvnUrl4VersionAndType() {
        String mvnUrl = MavenUrlHelper.generateMvnUrl("org.talend", "test", "6.0", "zip", null);
        Assert.assertEquals("mvn:org.talend/test/6.0/zip", mvnUrl);
    }

    @Test
    public void testGenerateMvnUrl4All() {
        String mvnUrl = MavenUrlHelper.generateMvnUrl("org.talend", "test", "6.0", "zip", "source");
        Assert.assertEquals("mvn:org.talend/test/6.0/zip/source", mvnUrl);
    }

    @Test
    public void testGenerateMvnUrl4WithoutVersion() {
        String mvnUrl = MavenUrlHelper.generateMvnUrl("org.talend", "test", null, "zip", "source");
        Assert.assertEquals("mvn:org.talend/test//zip/source", mvnUrl);
    }

    @Test
    public void testGenerateMvnUrl4WithoutVersionAndType() {
        String mvnUrl = MavenUrlHelper.generateMvnUrl("org.talend", "test", null, null, "source");
        Assert.assertEquals("mvn:org.talend/test///source", mvnUrl);
    }

    @Test
    public void testAddTypeForMavenUri() {
        String moduleName = "test.jar";
        String mvnURI = "mvn:org.talend/test/6.0/jar";
        String mvnURIWithType = MavenUrlHelper.addTypeForMavenUri(mvnURI, moduleName);
        Assert.assertEquals(mvnURIWithType, "mvn:org.talend/test/6.0/jar");

        moduleName = "test.exe";
        mvnURI = "mvn:org.talend/test/6.0";
        mvnURIWithType = MavenUrlHelper.addTypeForMavenUri(mvnURI, moduleName);
        Assert.assertEquals(mvnURIWithType, "mvn:org.talend/test/6.0/exe");

        moduleName = "test";
        mvnURI = "mvn:org.talend/test/6.0";
        mvnURIWithType = MavenUrlHelper.addTypeForMavenUri(mvnURI, moduleName);
        Assert.assertEquals(mvnURIWithType, "mvn:org.talend/test/6.0/jar");

        moduleName = "test.jar";
        mvnURI = "mvn:org.talend.libraries/abc/6.0.0-SNAPSHOT/zip";
        mvnURIWithType = MavenUrlHelper.addTypeForMavenUri(mvnURI, moduleName);
        Assert.assertEquals(mvnURIWithType, "mvn:org.talend.libraries/abc/6.0.0-SNAPSHOT/zip");
    }
}
