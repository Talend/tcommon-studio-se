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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class NexusShareComponentsManagerTest {

    class NexusShareComponentsManagerTestClass extends NexusShareComponentsManager {

        public NexusShareComponentsManagerTestClass(ArtifactRepositoryBean compNexusServer) {
            super(compNexusServer);
        }

    }

    static ArtifactRepositoryBean serverBean;

    @BeforeClass
    public static void parepare() {
        serverBean = new ArtifactRepositoryBean();
        serverBean.setServer("http://localhost:8081/nexus");
        serverBean.setUserName("");
        serverBean.setPassword("");
        serverBean.setRepositoryId("components");
    }

    @AfterClass
    public static void cleanup() {
        serverBean = null;
    }

    File tmpFolder = null;

    @Before
    public void before() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "index"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_deployComponent_myServerInReleaseRepo_IntegrationTest() throws Exception {
        final ArtifactRepositoryBean artifactRepositoryBean = IntegrationTestHelper.getNexusServerReleaseBean();
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(artifactRepositoryBean);
        final NexusComponentsTransport nexusTransport = shareManager.getNexusTransport();
        if (!CommonsPlugin.isDebugMode() && !nexusTransport.isAvailable()) {
            return; // if not debug, won't do test
        }
        Assert.assertTrue("Make sure the Nexus server settings are correct in IntegrationTestHelper",
                nexusTransport.isAvailable());

        IProgressMonitor monitor = new NullProgressMonitor();
        final ComponentIndexManager indexManager = shareManager.getIndexManager();
        final MavenArtifact indexArtifact = shareManager.getIndexArtifact();

        // 1 prepare the test component
        final File compFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());
        ComponentIndexBean compIndexBean = indexManager.create(compFile);
        compIndexBean = compIndexBean.cloneWithoutSnapshot();

        final MavenArtifact compArtifact = compIndexBean.getMavenArtifact();

        // 1.1 delete the index
        if (nexusTransport.isAvailable(monitor, indexArtifact)) {
            nexusTransport.doHttpDelete(monitor, indexArtifact);
        }

        // 1.2 delete the component
        if (nexusTransport.isAvailable(monitor, compArtifact)) {
            nexusTransport.doHttpDelete(monitor, compArtifact);
        }

        // 2 deploy component
        boolean deployed = shareManager.deployComponent(monitor, compFile, compIndexBean);
        Assert.assertTrue("Deploy component failure", deployed);

        // 2.1 test index
        Assert.assertTrue("Create the index failure", nexusTransport.isAvailable(monitor, indexArtifact));
        File testIndexFile = new File(this.tmpFolder, indexArtifact.getFileName());
        nexusTransport.doHttpDownload(monitor, testIndexFile, indexArtifact);
        Assert.assertTrue("Download created index failure", testIndexFile.exists());
        List<ComponentIndexBean> indexBeans = indexManager.parse(testIndexFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(1, indexBeans.size());
        Assert.assertEquals(compIndexBean, indexBeans.get(0));

        // 2.2 test component
        Assert.assertTrue("Didn't deploy the component successfully", nexusTransport.isAvailable(monitor, compArtifact));
        File testComZipFile = new File(this.tmpFolder, compArtifact.getFileName());
        nexusTransport.doHttpDownload(monitor, testComZipFile, compArtifact);
        Assert.assertTrue("Download deployed component failure", testComZipFile.exists());
        Assert.assertEquals("The deployed component must be same as original one", FilesUtils.getChecksumAlder32(compFile),
                FilesUtils.getChecksumAlder32(testComZipFile));

        // 3 test another version
        final File fakeCompFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_FAKE_MYJIRA);
        Assert.assertNotNull(fakeCompFile);
        Assert.assertTrue(fakeCompFile.exists());

        final ComponentIndexBean fakeCompIndexBean = new ComponentIndexBean();
        fakeCompIndexBean.setRequiredFieldsValue(compIndexBean.getName(), compIndexBean.getBundleId(), "10.0.0",
                "mvn:org.talend.components/components-myjira/10.0.0/zip");
        final MavenArtifact fakeCompArtifact = MavenUrlHelper.parseMvnUrl(fakeCompIndexBean.getMvnURI());

        deployed = shareManager.deployComponent(monitor, fakeCompFile, fakeCompIndexBean);
        Assert.assertTrue("Deploy component failure", deployed);

        // 3.1 test index
        Assert.assertTrue("Update the index failure", nexusTransport.isAvailable(monitor, indexArtifact));
        nexusTransport.doHttpDownload(monitor, testIndexFile, indexArtifact);
        Assert.assertTrue("Download updated index failure", testIndexFile.exists());
        indexBeans = indexManager.parse(testIndexFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(2, indexBeans.size());
        Assert.assertEquals(compIndexBean, indexBeans.get(0));
        Assert.assertEquals(fakeCompIndexBean, indexBeans.get(1));

        // 3.2 test component
        Assert.assertTrue("Didn't deploy the component successfully", nexusTransport.isAvailable(monitor, fakeCompArtifact));
        File testFakeComZipFile = new File(this.tmpFolder, fakeCompArtifact.getFileName());
        nexusTransport.doHttpDownload(monitor, testFakeComZipFile, fakeCompArtifact);
        Assert.assertTrue("Download deployed component failure", testFakeComZipFile.exists());
        Assert.assertEquals("The deployed component must be same as original one", FilesUtils.getChecksumAlder32(fakeCompFile),
                FilesUtils.getChecksumAlder32(testFakeComZipFile));

        // 4 test another component
        final ComponentIndexBean anotherCompIndexBean = new ComponentIndexBean();
        anotherCompIndexBean.setRequiredFieldsValue("ABC", "org.talend.components.abc", "1.2.3",
                "mvn:org.talend.components/components-abc/1.2.3/zip");
        final MavenArtifact anotherCompArtifact = MavenUrlHelper.parseMvnUrl(anotherCompIndexBean.getMvnURI());

        // use same fake file
        deployed = shareManager.deployComponent(monitor, fakeCompFile, anotherCompIndexBean);
        Assert.assertTrue("Deploy component failure", deployed);

        // 4.1 test index
        Assert.assertTrue("Update the index failure", nexusTransport.isAvailable(monitor, indexArtifact));
        nexusTransport.doHttpDownload(monitor, testIndexFile, indexArtifact);
        Assert.assertTrue("Download updated index failure", testIndexFile.exists());
        indexBeans = indexManager.parse(testIndexFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(3, indexBeans.size());
        Assert.assertEquals(anotherCompIndexBean, indexBeans.get(0));
        Assert.assertEquals(compIndexBean, indexBeans.get(1));
        Assert.assertEquals(fakeCompIndexBean, indexBeans.get(2));

        // 4.2 test component(fake one)
        Assert.assertTrue("Didn't deploy the component successfully", nexusTransport.isAvailable(monitor, anotherCompArtifact));
        File testAnotherComZipFile = new File(this.tmpFolder, anotherCompArtifact.getFileName());
        nexusTransport.doHttpDownload(monitor, testAnotherComZipFile, anotherCompArtifact);
        Assert.assertTrue("Download deployed component failure", testAnotherComZipFile.exists());
        Assert.assertEquals("The deployed component must be same as original one", FilesUtils.getChecksumAlder32(fakeCompFile),
                FilesUtils.getChecksumAlder32(testAnotherComZipFile));

        // 5 deploy same version, but different zip(use fake one instead)
        compIndexBean.setValue(ComponentIndexNames.license_uri, "http://abc.com"); // update the license uri also
        deployed = shareManager.deployComponent(monitor, fakeCompFile, compIndexBean);
        Assert.assertTrue("Deploy component failure", deployed);

        // 5.1 test index
        Assert.assertTrue("Update the index failure", nexusTransport.isAvailable(monitor, indexArtifact));
        nexusTransport.doHttpDownload(monitor, testIndexFile, indexArtifact);
        Assert.assertTrue("Download updated index failure", testIndexFile.exists());
        indexBeans = indexManager.parse(testIndexFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(3, indexBeans.size());
        Assert.assertEquals(anotherCompIndexBean, indexBeans.get(0));
        Assert.assertEquals(compIndexBean, indexBeans.get(1));
        Assert.assertEquals("http://abc.com", indexBeans.get(1).getLicenseURI());
        Assert.assertEquals(fakeCompIndexBean, indexBeans.get(2));

        // 5.2 test component(same as fake one)
        Assert.assertTrue("Didn't deploy the component successfully", nexusTransport.isAvailable(monitor, compArtifact));
        File testComZipFile2 = new File(this.tmpFolder, anotherCompArtifact.getFileName());
        nexusTransport.doHttpDownload(monitor, testComZipFile2, compArtifact);
        Assert.assertTrue("Download deployed component failure", testComZipFile2.exists());
        Assert.assertEquals("The deployed component must be same as original one", FilesUtils.getChecksumAlder32(fakeCompFile),
                FilesUtils.getChecksumAlder32(testComZipFile2));
    }

    @Test
    public void test_deployComponent_withIndexBean_nullFile() {
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);
        boolean deployed = shareManager.deployComponent(null, null, null);
        Assert.assertFalse(deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_notExistedFile() {
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);
        boolean deployed = shareManager.deployComponent(null, new File("abc.txt"), null);
        Assert.assertFalse(deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_nonFile() {
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);
        boolean deployed = shareManager.deployComponent(null, tmpFolder, null);
        Assert.assertFalse(deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_invalidFile() throws IOException {
        File txtFile = new File(tmpFolder, "abc.txt");
        txtFile.createNewFile();
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);
        boolean deployed = shareManager.deployComponent(null, txtFile, null);
        Assert.assertFalse(deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_nullBean() throws Exception {
        final File compFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean) {

            @Override
            NexusComponentsTransport createNexusComponentsTransport() {
                return new NexusComponentsTransport(serverBean.getRepositoryURL(), serverBean.getUserName(),
                        serverBean.getPassword() != null ? serverBean.getPassword().toCharArray() : null) {

                    @Override
                    public void doHttpUpload(IProgressMonitor monitor, File targetFile, MavenArtifact artifact) throws Exception {
                        // nothing for test, just no exception and success done.
                    }

                    @Override
                    public void doHttpDelete(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
                        // nothing to do.
                    }

                    @Override
                    public boolean isAvailable(IProgressMonitor monitor, MavenArtifact artifact) {
                        return false;
                    }

                };
            }

        };
        boolean deployed = shareManager.deployComponent(null, compFile, null);
        Assert.assertTrue("Deploy failure", deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_hasBean() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean) {

            @Override
            NexusComponentsTransport createNexusComponentsTransport() {
                return new NexusComponentsTransport(serverBean.getRepositoryURL(), serverBean.getUserName(),
                        serverBean.getPassword() != null ? serverBean.getPassword().toCharArray() : null) {

                    @Override
                    public void doHttpUpload(IProgressMonitor monitor, File targetFile, MavenArtifact artifact) throws Exception {
                        // nothing for test, just no exception and success done.
                    }

                    @Override
                    public void doHttpDelete(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
                        // nothing to do.
                    }

                    @Override
                    public boolean isAvailable(IProgressMonitor monitor, MavenArtifact artifact) {
                        return false;
                    }

                };
            }

        };
        final ComponentIndexBean indexBean = new ComponentIndexManager().create(compFile);
        boolean deployed = shareManager.deployComponent(null, compFile, indexBean);
        Assert.assertTrue("Deploy failure", deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_nonExistedCompInServer() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean) {

            @Override
            NexusComponentsTransport createNexusComponentsTransport() {
                return new NexusComponentsTransport(serverBean.getRepositoryURL(), serverBean.getUserName(),
                        serverBean.getPassword() != null ? serverBean.getPassword().toCharArray() : null) {

                    @Override
                    public void doHttpUpload(IProgressMonitor monitor, File targetFile, MavenArtifact artifact) throws Exception {
                        // nothing for test, just no exception and success done.
                    }

                    @Override
                    public void doHttpDelete(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
                        // nothing to do.
                    }

                    @Override
                    public boolean isAvailable(IProgressMonitor monitor, MavenArtifact artifact) {
                        return false;
                    }

                };
            }

        };

        final ComponentIndexBean indexBean = shareManager.getIndexManager().create(compFile);
        boolean deployed = shareManager.deployComponent(null, compFile, indexBean);
        Assert.assertTrue("Deploy failure", deployed);
    }

    @Test
    public void test_deployComponent_withIndexBean_existedCompInServer() throws IOException {
        final File compFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(compFile);
        Assert.assertTrue(compFile.exists());

        final File indexFile = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_641_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        final File tempIndexFile = new File(this.tmpFolder, indexFile.getName());
        FilesUtils.copyFile(indexFile, tempIndexFile);

        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean) {

            @Override
            NexusComponentsTransport createNexusComponentsTransport() {
                return new NexusComponentsTransport(serverBean.getRepositoryURL(), serverBean.getUserName(),
                        serverBean.getPassword() != null ? serverBean.getPassword().toCharArray() : null) {

                    @Override
                    public void doHttpUpload(IProgressMonitor monitor, File targetFile, MavenArtifact artifact) throws Exception {
                        // nothing for test, just no exception and success done.
                    }

                    @Override
                    public void doHttpDelete(IProgressMonitor monitor, MavenArtifact artifact) throws Exception {
                        // nothing to do.
                    }

                    @Override
                    public boolean isAvailable(IProgressMonitor monitor, MavenArtifact artifact) {
                        return true;
                    }

                };
            }

            @Override
            File downloadIndexFile(IProgressMonitor monitor) {
                return tempIndexFile;
            }
        };
        final ComponentIndexBean indexBean = shareManager.getIndexManager().create(compFile);
        boolean deployed = shareManager.deployComponent(null, compFile, indexBean);
        Assert.assertTrue("Deploy failure", deployed);
    }

    @Test
    public void test_checkComponent_null() {
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);
        ComponentStatus status = shareManager.checkComponent(null, null, null);
        Assert.assertNotNull(status);
        Assert.assertEquals(ComponentStatus.NONE, status);
    }

    @Test
    public void test_checkComponent_emptyExisted() { // NEW_LATEST
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.3");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentStatus status = shareManager.checkComponent(null, Collections.emptyList(), testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index shouldn't be existed at all", ComponentStatus.NEW_LATEST == status.getStatusCode());
    }

    @Test
    public void test_checkComponent_nonExisted() { // NEW_LATEST
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        List<ComponentIndexBean> remoteComponents = new ArrayList<ComponentIndexBean>();
        remoteComponents.add(indexBean1);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.3");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentStatus status = shareManager.checkComponent(null, remoteComponents, testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index shouldn't be existed at all", ComponentStatus.NEW_LATEST == status.getStatusCode());
    }

    @Test
    public void test_checkComponent_newLatestVersion() {// NEW_LATEST
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        List<ComponentIndexBean> remoteComponents = new ArrayList<ComponentIndexBean>();
        remoteComponents.add(indexBean1);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.3");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentStatus status = shareManager.checkComponent(null, remoteComponents, testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index should be lastest version and not existed",
                ComponentStatus.NEW_LATEST == status.getStatusCode());
        Assert.assertEquals(testIndexBean, status.getIndexBean());
        Assert.assertEquals("Shouldn't be existed old version of component", 0, status.getExitedComponents().size());
    }

    @Test
    public void test_checkComponent_existedLatestVersion() {// EXIST_LATEST
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBean indexBean2 = new ComponentIndexBean();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        List<ComponentIndexBean> remoteComponents = new ArrayList<ComponentIndexBean>();
        remoteComponents.add(indexBean1);
        remoteComponents.add(indexBean2);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.3");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentStatus status = shareManager.checkComponent(null, remoteComponents, testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index should be lastest version and be existed already",
                ComponentStatus.EXIST_LATEST == status.getStatusCode());
        Assert.assertEquals(testIndexBean, status.getIndexBean());
        Assert.assertEquals("Should have been existed components", 1, status.getExitedComponents().size());
        Assert.assertEquals(indexBean2, status.getExitedComponents().get(0));
    }

    @Test
    public void test_checkComponent_newLowerVersion() { // NEW_LOWER
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBean indexBean2 = new ComponentIndexBean();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentIndexBean indexBean3 = new ComponentIndexBean();
        indexBean3.setValue(ComponentIndexNames.name, "Abc");
        indexBean3.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean3.setValue(ComponentIndexNames.version, "1.3.0");
        indexBean3.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.3.0/zip");

        List<ComponentIndexBean> remoteComponents = new ArrayList<ComponentIndexBean>();
        remoteComponents.add(indexBean1);
        remoteComponents.add(indexBean2);
        remoteComponents.add(indexBean3);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.4");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.4/zip");

        ComponentStatus status = shareManager.checkComponent(null, remoteComponents, testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index should have one lastest version", ComponentStatus.NEW_LOWER == status.getStatusCode());
        Assert.assertEquals(testIndexBean, status.getIndexBean());
        Assert.assertEquals("Existed some components", 2, status.getExitedComponents().size());
        Assert.assertEquals(indexBean2, status.getExitedComponents().get(0));
        Assert.assertEquals(indexBean3, status.getExitedComponents().get(1));
    }

    @Test
    public void test_checkComponent_existedLowerVersion() {// EXIST_LOWER
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBean indexBean2 = new ComponentIndexBean();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentIndexBean indexBean3 = new ComponentIndexBean();
        indexBean3.setValue(ComponentIndexNames.name, "Abc");
        indexBean3.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean3.setValue(ComponentIndexNames.version, "1.3.0");
        indexBean3.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.3.0/zip");

        List<ComponentIndexBean> remoteComponents = new ArrayList<ComponentIndexBean>();
        remoteComponents.add(indexBean1);
        remoteComponents.add(indexBean2);
        remoteComponents.add(indexBean3);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.3");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        ComponentStatus status = shareManager.checkComponent(null, remoteComponents, testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index should have one lastest version", ComponentStatus.EXIST_LOWER == status.getStatusCode());
        Assert.assertEquals(testIndexBean, status.getIndexBean());
        Assert.assertEquals("Existed some components", 2, status.getExitedComponents().size());
        Assert.assertEquals(indexBean2, status.getExitedComponents().get(0));
        Assert.assertEquals(testIndexBean, status.getExitedComponents().get(0)); // same one
        Assert.assertEquals(indexBean3, status.getExitedComponents().get(1));
    }

    @Test
    public void test_checkComponent_existedSameVersion_diffOtherSetting_product() {// EXIST_LOWER
        NexusShareComponentsManagerTestClass shareManager = new NexusShareComponentsManagerTestClass(serverBean);

        ComponentIndexBean indexBean1 = new ComponentIndexBean();
        indexBean1.setValue(ComponentIndexNames.name, "Test");
        indexBean1.setValue(ComponentIndexNames.bundle_id, "org.talend.test");
        indexBean1.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean1.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-test/1.2.3/zip");

        ComponentIndexBean indexBean2 = new ComponentIndexBean();
        indexBean2.setValue(ComponentIndexNames.name, "Abc");
        indexBean2.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean2.setValue(ComponentIndexNames.version, "1.2.3");
        indexBean2.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        indexBean2.setValue(ComponentIndexNames.product, "tos"); //

        ComponentIndexBean indexBean3 = new ComponentIndexBean();
        indexBean3.setValue(ComponentIndexNames.name, "Abc");
        indexBean3.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        indexBean3.setValue(ComponentIndexNames.version, "1.3.0");
        indexBean3.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.3.0/zip");

        List<ComponentIndexBean> remoteComponents = new ArrayList<ComponentIndexBean>();
        remoteComponents.add(indexBean1);
        remoteComponents.add(indexBean2);
        remoteComponents.add(indexBean3);

        ComponentIndexBean testIndexBean = new ComponentIndexBean();
        testIndexBean.setValue(ComponentIndexNames.name, "Abc");
        testIndexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.abc");
        testIndexBean.setValue(ComponentIndexNames.version, "1.2.3");
        testIndexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/1.2.3/zip");

        testIndexBean.setValue(ComponentIndexNames.product, "tis"); //

        ComponentStatus status = shareManager.checkComponent(null, remoteComponents, testIndexBean);
        Assert.assertNotNull(status);
        Assert.assertTrue("The index should have one lastest version", ComponentStatus.EXIST_LOWER == status.getStatusCode());
        final ComponentIndexBean indexBean = status.getIndexBean();
        Assert.assertEquals(testIndexBean, indexBean);
        Assert.assertEquals("tis", indexBean.getProduct());

        Assert.assertEquals("Existed some components", 2, status.getExitedComponents().size());
        final ComponentIndexBean bean0 = status.getExitedComponents().get(0);
        Assert.assertEquals(indexBean2, bean0);
        Assert.assertEquals(testIndexBean, bean0); // same one
        Assert.assertEquals("tos", bean0.getProduct());

        Assert.assertEquals(indexBean3, status.getExitedComponents().get(1));
    }
}
