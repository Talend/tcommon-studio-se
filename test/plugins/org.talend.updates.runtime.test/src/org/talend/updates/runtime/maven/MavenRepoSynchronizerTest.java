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
package org.talend.updates.runtime.maven;

import java.io.File;

import org.apache.maven.model.Model;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenRepoSynchronizerTest {

    File tmpFolder = null;

    File updatesiteLibFolder;

    File localRepoFolder;

    @Before
    public void prepare() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "maven"); //$NON-NLS-1$  //$NON-NLS-2$

        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        FilesUtils.unzip(testDataFile.getAbsolutePath(), tmpFolder.getAbsolutePath());

        updatesiteLibFolder = new File(tmpFolder, ComponentsInstallComponent.FOLDER_M2_REPOSITORY); // m2/repositroy
        Assert.assertTrue(updatesiteLibFolder.exists());
        Assert.assertTrue(updatesiteLibFolder.isDirectory());

        // check from local
        final String localRepositoryPath = MavenPlugin.getMaven().getLocalRepositoryPath();
        Assert.assertNotNull(localRepositoryPath);
        localRepoFolder = new File(localRepositoryPath);
        Assert.assertTrue(localRepoFolder.exists());

    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_sync_alone() throws Exception {
        final MavenArtifact jarArtifact = new MavenArtifact();
        jarArtifact.setGroupId(MavenConstants.DEFAULT_LIB_GROUP_ID);
        jarArtifact.setArtifactId("mytest");
        jarArtifact.setVersion("6.0.0");
        doTestLib(jarArtifact, "It's a test jar");
    }

    @Test
    public void test_sync_withParentGroupId() throws Exception {
        final MavenArtifact jarArtifact = new MavenArtifact();
        jarArtifact.setGroupId(MavenConstants.DEFAULT_LIB_GROUP_ID);
        jarArtifact.setArtifactId("withparent");
        jarArtifact.setVersion("6.4.0");
        doTestLib(jarArtifact, "test jar with parent groupId");
    }

    @Test
    public void test_sync_withFullParent() throws Exception {
        final MavenArtifact jarArtifact = new MavenArtifact();
        jarArtifact.setGroupId(MavenConstants.DEFAULT_LIB_GROUP_ID);
        jarArtifact.setArtifactId("withparentfull");
        jarArtifact.setVersion("6.4.0");
        doTestLib(jarArtifact, "test jar with parent groupId and version");
    }

    @Test
    public void test_sync_withBundlePackaging() throws Exception {
        final MavenArtifact jarArtifact = new MavenArtifact();
        jarArtifact.setGroupId(MavenConstants.DEFAULT_LIB_GROUP_ID);
        jarArtifact.setArtifactId("bundle-test");
        jarArtifact.setVersion("6.4.0");
        doTestLib(jarArtifact, "test jar for bundle packaging");
    }

    private void doTestLib(MavenArtifact jarArtifact, String pomDesc) throws Exception {
        IMaven maven = MavenPlugin.getMaven();
        String localRepositoryPath = maven.getLocalRepositoryPath();
        Assert.assertNotNull(localRepositoryPath);

        // final String artifactPath = PomUtil.getArtifactPath(jarArtifact);
        // IPath libPath = new Path(localRepositoryPath).append(artifactPath);
        IPath libPath = new Path(localRepositoryPath).append(jarArtifact.getGroupId().replace(".", "/"))
                .append(jarArtifact.getArtifactId()).append(jarArtifact.getVersion()).append(jarArtifact.getFileName(false));
        File libFile = libPath.toFile();
        if (libFile.exists()) {
            libFile.delete();
        }
        File pomFile = libPath.removeFileExtension().addFileExtension(TalendMavenConstants.PACKAGING_POM).toFile();
        if (pomFile.exists()) {
            pomFile.delete();
        }
        // only test in local
        MavenRepoSynchronizer sync = new MavenRepoSynchronizer(updatesiteLibFolder, false);
        sync.installM2RepositoryLibs(updatesiteLibFolder);

        Assert.assertTrue("sync lib for M2 repository failure", libFile.exists());
        Assert.assertTrue(pomFile.exists());

        Model model = MavenPlugin.getMaven().readModel(pomFile);
        Assert.assertNotNull(model);
        if (pomDesc != null) {
            Assert.assertNotEquals(pomDesc, model.getDescription());// won't be original one.
        }
        // should have generated one always.
        Assert.assertNotNull(model.getDescription());
        Assert.assertTrue("Should be generated pom", model.getDescription().startsWith("Generated by"));
    }
}
