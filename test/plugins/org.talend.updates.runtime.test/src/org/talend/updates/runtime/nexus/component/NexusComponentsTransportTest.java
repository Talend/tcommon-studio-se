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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.core.nexus.HttpClientTransport;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class NexusComponentsTransportTest {

    static class NexusComponentsTransportTestClass extends NexusComponentsTransport {

        public NexusComponentsTransportTestClass(String nexusURL, String nexusUser, char[] nexusPass) {
            super(nexusURL, nexusUser, nexusPass);
        }

    }

    private static NexusComponentsTransport nexusTransport;

    private static MavenArtifact indexArtifact;

    @BeforeClass
    public static void prepare() {
        // fake nexus
        String nexusURL = "http://localhost:8081/nexus/content/repositories/components/";
        String nexusUser = "admin";
        String nexusPass = "admin";

        nexusTransport = new NexusComponentsTransportTestClass(nexusURL, nexusUser, nexusPass.toCharArray());

        // create without server, just want to get index artifact
        NexusShareComponentsManager manager = new NexusShareComponentsManager(new NexusServerBean());
        indexArtifact = manager.getIndexArtifact();
    }

    @AfterClass
    public static void cleanup() {
        nexusTransport = null;
    }

    File tmpFolder = null;

    @Before
    public void before() throws Exception {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "index"); //$NON-NLS-1$  //$NON-NLS-2$

        Assert.assertNotNull(nexusTransport);
        Assert.assertNotNull(indexArtifact);
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_uploadXml_myServerInReleaseRepo_IntegrationTest() throws Exception {
        final NexusServerBean nexusServerBean = IntegrationTestHelper.getNexusServerReleaseBean();
        NexusComponentsTransportTestClass myNexusTransport = new NexusComponentsTransportTestClass(
                nexusServerBean.getRepositoryURI(), nexusServerBean.getUserName(), nexusServerBean.getPassword().toCharArray());
        if (!CommonsPlugin.isDebugMode() && !myNexusTransport.isAvailable()) {
            return; // if not debug, won't do test
        }
        Assert.assertTrue("Make sure the Nexus server settings are correct in IntegrationTestHelper",
                myNexusTransport.isAvailable());

        IProgressMonitor monitor = new NullProgressMonitor();

        // 1.1 prepare the index files
        final File index640File = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_640_INDEX_FILE);
        Assert.assertNotNull(index640File);
        Assert.assertTrue(index640File.exists());
        final long index640Sum32 = FilesUtils.getChecksumAlder32(index640File);

        final File index641File = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_641_INDEX_FILE);
        Assert.assertNotNull(index641File);
        Assert.assertTrue(index641File.exists());
        final long index641Sum32 = FilesUtils.getChecksumAlder32(index641File);

        // 1.2 prepare the local index test file
        final File indexTestFile = new File(tmpFolder, indexArtifact.getFileName());
        Assert.assertFalse(indexTestFile.exists());

        // 1.3 prepare the nexus index remote uri
        HttpClientTransport fakeHttpTransport = new HttpClientTransport(myNexusTransport.getNexusURL(),
                myNexusTransport.getNexusUser(), myNexusTransport.getNexusPassStr()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                return null;
            }

        };
        final URI indeNexusURI = fakeHttpTransport.createURI(indexArtifact);

        // 2. test
        long oldIndexSum32 = -1;
        boolean available = myNexusTransport.isAvailable(monitor, indexArtifact);
        if (available) { // existed, download to check
            myNexusTransport.doHttpDownload(monitor, indexTestFile, indexArtifact);
            Assert.assertTrue("Download failure, when existed the index in Nexus server", indexTestFile.exists());
            long remoteIndexSum32 = FilesUtils.getChecksumAlder32(indexTestFile);
            if (remoteIndexSum32 == index640Sum32 // same as 640
                    || remoteIndexSum32 == index641Sum32) { // same as 641

                myNexusTransport.doHttpDelete(monitor, indexArtifact);

                // not existed yet
                Assert.assertFalse("Remove failure :" + indeNexusURI, myNexusTransport.isAvailable(monitor, indexArtifact));
            } else { // unknown, will overwrite later
                oldIndexSum32 = remoteIndexSum32;
            }
        }
        // try to upload 640 index to init first.
        myNexusTransport.doHttpUpload(monitor, index640File, indexArtifact);

        Assert.assertTrue("Failed to upload the index to :" + indeNexusURI, myNexusTransport.isAvailable(monitor, indexArtifact));

        // try to download the the 640 content
        myNexusTransport.doHttpDownload(monitor, indexTestFile, indexArtifact);
        Assert.assertTrue("Download failure" + indeNexusURI, indexTestFile.exists());
        long nexusIndex640Sum32 = FilesUtils.getChecksumAlder32(indexTestFile);
        Assert.assertEquals("The index contents are not same", index640Sum32, nexusIndex640Sum32);
        if (oldIndexSum32 > -1) { // have existed old one
            Assert.assertNotEquals("Overwrite failure: " + indeNexusURI, oldIndexSum32, nexusIndex640Sum32);
        }

        // try to upload 641 index
        myNexusTransport.doHttpDelete(monitor, indexArtifact); // remove existed one, else will have error
        myNexusTransport.doHttpUpload(monitor, index641File, indexArtifact);

        Assert.assertTrue("Failed to upload the index to :" + indeNexusURI, myNexusTransport.isAvailable(monitor, indexArtifact));

        myNexusTransport.doHttpDownload(monitor, indexTestFile, indexArtifact);
        Assert.assertTrue("Download failure: " + indeNexusURI, indexTestFile.exists());
        long nexusIndex641Sum32 = FilesUtils.getChecksumAlder32(indexTestFile);
        Assert.assertEquals("The index contents are not same", index641Sum32, nexusIndex641Sum32);
    }

    @Test
    public void test_uploadZip_myServerInReleaseRepo_IntegrationTest() throws Exception {
        final NexusServerBean nexusServerBean = IntegrationTestHelper.getNexusServerReleaseBean();
        NexusComponentsTransportTestClass myNexusTransport = new NexusComponentsTransportTestClass(
                nexusServerBean.getRepositoryURI(), nexusServerBean.getUserName(), nexusServerBean.getPassword().toCharArray());
        if (!CommonsPlugin.isDebugMode() && !myNexusTransport.isAvailable()) {
            return; // if not debug, won't do test
        }
        Assert.assertTrue("Make sure the Nexus server settings are correct in IntegrationTestHelper",
                myNexusTransport.isAvailable());

        IProgressMonitor monitor = new NullProgressMonitor();

        // 1.1 prepare the zip files
        final File myJiraZipFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(myJiraZipFile);
        Assert.assertTrue(myJiraZipFile.exists());
        final long myJiraZipSum32 = FilesUtils.getChecksumAlder32(myJiraZipFile);
        ComponentIndexBean myJiraIndexBean = new ComponentIndexManager().create(myJiraZipFile);
        myJiraIndexBean = myJiraIndexBean.cloneWithoutSnapshot();
        final MavenArtifact myJiraArtifact = myJiraIndexBean.getMavenArtifact();

        final File myFakeJiraZipFile = BundleFileUtil.getBundleFile(P2InstallerTest.class, P2InstallerTest.TEST_COMP_FAKE_MYJIRA);
        Assert.assertNotNull(myFakeJiraZipFile);
        Assert.assertTrue(myFakeJiraZipFile.exists());
        final long myFakeJiraSum32 = FilesUtils.getChecksumAlder32(myFakeJiraZipFile);

        // 1.2 prepare the local zip test file
        final File jiraLocalZipFile = new File(tmpFolder, "myJira.zip"); //$NON-NLS-1$
        Assert.assertFalse(jiraLocalZipFile.exists());

        // 1.3 prepare the nexus index remote uri
        HttpClientTransport fakeHttpTransport = new HttpClientTransport(myNexusTransport.getNexusURL(),
                myNexusTransport.getNexusUser(), myNexusTransport.getNexusPassStr()) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                return null;
            }

        };
        final URI myJiraNexusURI = fakeHttpTransport.createURI(myJiraArtifact);

        // 2. test
        long oldMyJiraSum32 = -1;
        boolean available = myNexusTransport.isAvailable(monitor, myJiraArtifact);
        if (available) { // existed, download to check
            myNexusTransport.doHttpDownload(monitor, jiraLocalZipFile, myJiraArtifact);
            Assert.assertTrue("Download failure, when existed the component in Nexus server", jiraLocalZipFile.exists());
            long remoteMyJiraSum32 = FilesUtils.getChecksumAlder32(jiraLocalZipFile);
            if (remoteMyJiraSum32 == myJiraZipSum32 // same as 640
                    || remoteMyJiraSum32 == myFakeJiraSum32) { // same as 641

                myNexusTransport.doHttpDelete(monitor, myJiraArtifact);

                // not existed yet
                Assert.assertFalse("Remove failure :" + myJiraNexusURI, myNexusTransport.isAvailable(monitor, myJiraArtifact));
            } else { // unknown, will overwrite later
                oldMyJiraSum32 = remoteMyJiraSum32;
            }
        }
        // try to upload one comp zip init first.
        myNexusTransport.doHttpUpload(monitor, myJiraZipFile, myJiraArtifact);

        Assert.assertTrue("Failed to upload the component to :" + myJiraNexusURI,
                myNexusTransport.isAvailable(monitor, myJiraArtifact));

        // try to download the the 640 content
        myNexusTransport.doHttpDownload(monitor, jiraLocalZipFile, myJiraArtifact);
        Assert.assertTrue("Download failure" + myJiraNexusURI, jiraLocalZipFile.exists());
        long nexusMyJiraSum32 = FilesUtils.getChecksumAlder32(jiraLocalZipFile);
        Assert.assertEquals("The component zip contents are not same", myJiraZipSum32, nexusMyJiraSum32);
        if (oldMyJiraSum32 > -1) { // have existed old one
            Assert.assertNotEquals("Overwrite failure: " + myJiraNexusURI, oldMyJiraSum32, nexusMyJiraSum32);
        }

        // try to upload fake comp zip one
        myNexusTransport.doHttpDelete(monitor, myJiraArtifact); // remove existed one, else will have error
        myNexusTransport.doHttpUpload(monitor, myFakeJiraZipFile, myJiraArtifact);

        Assert.assertTrue("Failed to upload the component to :" + myJiraNexusURI,
                myNexusTransport.isAvailable(monitor, myJiraArtifact));

        myNexusTransport.doHttpDownload(monitor, jiraLocalZipFile, myJiraArtifact);
        Assert.assertTrue("Download failure: " + myJiraNexusURI, jiraLocalZipFile.exists());
        Assert.assertEquals("The component zip contents are not same", myFakeJiraSum32,
                FilesUtils.getChecksumAlder32(jiraLocalZipFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_doHttpUpload_nullFile() throws Exception {
        nexusTransport.doHttpUpload(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_doHttpUpload_nonExistFile() throws Exception {
        nexusTransport.doHttpUpload(null, new File("test.txt"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_doHttpUpload_invalidFile() throws Exception {
        nexusTransport.doHttpUpload(null, tmpFolder, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_doHttpUpload_nullArtifact() throws Exception {
        File file = new File(tmpFolder, "abc.txt");
        file.createNewFile();
        nexusTransport.doHttpUpload(null, file, null);
    }

    @Test
    public void test_doHttpUpload_networkIssue() throws Exception {
        // get one test file
        final File indexFile = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_641_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

            @Override
            protected HttpResponse httpPut(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI, File uploadFile)
                    throws IOException {
                HttpResponse httpResponse = mock(HttpResponse.class);

                StatusLine statusLine = mock(StatusLine.class);
                when(statusLine.getStatusCode()).thenReturn(404);
                when(httpResponse.getStatusLine()).thenReturn(statusLine);

                return httpResponse;
            }
        };
        try {

            transport.doHttpUpload(null, indexFile, indexArtifact);

            Assert.assertFalse("won't be here", true);
        } catch (Exception e) {
            HttpClientTransport httpTransport = new HttpClientTransport(nexusTransport.getNexusURL(),
                    nexusTransport.getNexusUser(), nexusTransport.getNexusPassStr()) {

                @Override
                protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws Exception {
                    return null;
                }

            };
            Assert.assertEquals(httpTransport.createURI(indexArtifact).toString(), e.getMessage()); // uri
            Assert.assertNotNull(e.getCause());
            Assert.assertTrue(e.getCause() instanceof BusinessException);
        }
    }

    @Test
    public void test_doHttpUpload_200() throws Exception {

        // get one test file
        final File indexFile = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_641_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

            @Override
            protected HttpResponse httpPut(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI, File uploadFile)
                    throws IOException {
                HttpResponse httpResponse = mock(HttpResponse.class);

                StatusLine statusLine = mock(StatusLine.class);
                when(statusLine.getStatusCode()).thenReturn(200); // ?
                when(httpResponse.getStatusLine()).thenReturn(statusLine);

                return httpResponse;
            }
        };

        transport.doHttpUpload(null, indexFile, indexArtifact);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_doHttpDownload_nullFile() throws Exception {
        nexusTransport.doHttpDownload(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_doHttpDownload_nullArtifact() throws Exception {
        File file = new File(tmpFolder, "abc.txt");
        nexusTransport.doHttpDownload(null, file, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_doHttpDownload_nonExistedArtifact() throws Throwable {
        File targetFile = new File(tmpFolder, indexArtifact.getFileName());
        Assert.assertFalse(targetFile.exists());

        try {
            NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                    nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

                @Override
                protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws IOException {
                    throw new FileNotFoundException("XXXX");
                }

            };

            transport.doHttpDownload(null, targetFile, indexArtifact);

            Assert.assertTrue("Shouldn't be here", false);
        } catch (Exception e) {
            Assert.assertNotNull(e.getCause());
            Assert.assertTrue("Have wrong exception", e.getCause() instanceof FileNotFoundException);
            Assert.assertEquals("XXXX", e.getCause().getMessage());
            throw e.getCause();
        }
    }

    @Test
    public void test_doHttpDownload_overwriteExistedFile() throws Exception {
        File targetFile = new File(tmpFolder, indexArtifact.getFileName());
        Assert.assertFalse(targetFile.exists());

        final ComponentIndexManager indexManager = new ComponentIndexManager();

        ComponentIndexBean indexBean = new ComponentIndexBean();
        indexBean.setValue(ComponentIndexNames.name, "abc");
        indexBean.setValue(ComponentIndexNames.bundle_id, "org.talend.components.abc");
        indexBean.setValue(ComponentIndexNames.version, "0.1.0");
        indexBean.setValue(ComponentIndexNames.mvn_uri, "mvn:org.talend.components/components-abc/0.1.0/zip");
        indexManager.createIndexFile(targetFile, indexBean);
        Assert.assertTrue(targetFile.exists());

        List<ComponentIndexBean> indexBeans = indexManager.parse(targetFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(1, indexBeans.size());

        // get one test file
        final File indexFile = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_641_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

            @Override
            protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws IOException {
                HttpResponse httpResponse = mock(HttpResponse.class);

                StatusLine statusLine = mock(StatusLine.class);
                when(statusLine.getStatusCode()).thenReturn(200);
                when(httpResponse.getStatusLine()).thenReturn(statusLine);

                FileInputStream inputStream = new FileInputStream(indexFile);

                HttpEntity httpEntity = mock(HttpEntity.class);
                when(httpEntity.getContent()).thenReturn(inputStream);
                when(httpResponse.getEntity()).thenReturn(httpEntity);

                return httpResponse;
            }

        };

        transport.doHttpDownload(null, targetFile, indexArtifact);

        Assert.assertTrue("Download failure", targetFile.exists());

        indexBeans = indexManager.parse(targetFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals("Overrite falure", 3, indexBeans.size());

        ComponentIndexBean bean0 = indexBeans.get(0);
        Assert.assertEquals("JIRA", bean0.getName());
        Assert.assertEquals("0.18.0", bean0.getVersion());
        Assert.assertEquals("org.talend.components.jira", bean0.getBundleId());
        Assert.assertEquals("tos_di,tos_bd", bean0.getProduct());
        Assert.assertEquals("mvn:org.talend.components/components-jira/0.18.0/zip", bean0.getMvnURI());
    }

    @Test
    public void test_doHttpDownload_createNewFile() throws Exception {
        File targetFile = new File(tmpFolder, indexArtifact.getFileName());
        Assert.assertFalse(targetFile.exists());

        // get one test file
        final File indexFile = BundleFileUtil.getBundleFile(ComponentIndexManagerTest.class,
                ComponentIndexManagerTest.PATH_641_INDEX_FILE);
        Assert.assertNotNull(indexFile);
        Assert.assertTrue(indexFile.exists());

        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

            @Override
            protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws IOException {
                HttpResponse httpResponse = mock(HttpResponse.class);

                StatusLine statusLine = mock(StatusLine.class);
                when(statusLine.getStatusCode()).thenReturn(200);
                when(httpResponse.getStatusLine()).thenReturn(statusLine);

                FileInputStream inputStream = new FileInputStream(indexFile);

                HttpEntity httpEntity = mock(HttpEntity.class);
                when(httpEntity.getContent()).thenReturn(inputStream);
                when(httpResponse.getEntity()).thenReturn(httpEntity);

                return httpResponse;
            }

        };

        transport.doHttpDownload(null, targetFile, indexArtifact);

        Assert.assertTrue("Download failure", targetFile.exists());

        final List<ComponentIndexBean> indexBeans = new ComponentIndexManager().parse(targetFile);
        Assert.assertNotNull(indexBeans);
        Assert.assertEquals(3, indexBeans.size());

        ComponentIndexBean bean0 = indexBeans.get(0);
        Assert.assertEquals("JIRA", bean0.getName());
        Assert.assertEquals("0.18.0", bean0.getVersion());
        Assert.assertEquals("org.talend.components.jira", bean0.getBundleId());
        Assert.assertEquals("tos_di,tos_bd", bean0.getProduct());
        Assert.assertEquals("mvn:org.talend.components/components-jira/0.18.0/zip", bean0.getMvnURI());
    }

    @Test
    public void test_doHttpDownload_networkIssue() throws Exception {
        File targetFile = new File(tmpFolder, indexArtifact.getFileName());
        targetFile.createNewFile();
        Assert.assertTrue(targetFile.exists());

        try {
            NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                    nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

                @Override
                protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws IOException {
                    HttpResponse httpResponse = mock(HttpResponse.class);

                    StatusLine statusLine = mock(StatusLine.class);
                    when(statusLine.getStatusCode()).thenReturn(200);
                    when(httpResponse.getStatusLine()).thenReturn(statusLine);

                    HttpEntity httpEntity = mock(HttpEntity.class);
                    InputStream inputStream = mock(InputStream.class);
                    when(httpEntity.getContent()).thenReturn(inputStream);
                    when(httpResponse.getEntity()).thenReturn(httpEntity);

                    return httpResponse;
                }

                @Override
                protected boolean writeStream(IProgressMonitor monitor, InputStream httpStream, OutputStream fileStream)
                        throws IOException {
                    throw new InterruptedIOException("XXXX");
                }

            };

            transport.doHttpDownload(null, targetFile, indexArtifact);

            Assert.assertTrue("never be here, because will have exception", false);
        } catch (Exception e) {
            Assert.assertTrue(e.getCause() instanceof InterruptedIOException);
            Assert.assertEquals("XXXX", e.getCause().getMessage()); // must be same exception
        }
        Assert.assertFalse("Should remove the unfinished file", targetFile.exists());
    }

    @Test
    public void test_isAvailable_null() {
        final boolean available = nexusTransport.isAvailable(null, null);
        Assert.assertFalse("The artifact is null, should be false", available);
    }

    @Test(expected = OperationCanceledException.class)
    public void test_isAvailable_cancel() {
        IProgressMonitor monitor = new NullProgressMonitor();
        monitor.setCanceled(true);
        nexusTransport.isAvailable(monitor, null);
    }

    @Test
    public void test_isAvailable_unavailableServer() {
        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(
                "http://localhost:10000/nexus/content/repositories/components/", null, null);
        final boolean available = transport.isAvailable(null, indexArtifact);
        Assert.assertFalse("Server should be not existed", available);
    }

    @Test
    public void test_isAvailable_existed200() {
        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

            @Override
            protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws IOException {
                final HttpResponse httpResponse = mock(HttpResponse.class);
                StatusLine statusLine = mock(StatusLine.class);
                when(statusLine.getStatusCode()).thenReturn(200);
                when(httpResponse.getStatusLine()).thenReturn(statusLine);
                return httpResponse;
            }

        };
        final boolean available = transport.isAvailable(null, indexArtifact);
        Assert.assertTrue("Will be ok when response 200", available);
    }

    @Test
    public void test_isAvailable_nonExisted400() {
        NexusComponentsTransportTestClass transport = new NexusComponentsTransportTestClass(nexusTransport.getNexusURL(),
                nexusTransport.getNexusUser(), nexusTransport.getNexusPass()) {

            @Override
            protected HttpResponse httpGet(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws IOException {
                final HttpResponse httpResponse = mock(HttpResponse.class);
                StatusLine statusLine = mock(StatusLine.class);
                when(statusLine.getStatusCode()).thenReturn(400);
                when(httpResponse.getStatusLine()).thenReturn(statusLine);
                return httpResponse;
            }

        };
        final boolean available = transport.isAvailable(null, indexArtifact);
        Assert.assertFalse("Not found, then response 400", available);
    }

}
