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
package org.talend.updates.runtime.engine.component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.updates.runtime.engine.component.RemoteComponentsTransport.RemoteComponent;
import org.talend.updates.runtime.engine.factory.NewComponentsInstallFactory;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RemoteComponentsTransportTest {

    class RemoteComponentsTransportTestClass extends RemoteComponentsTransport {

        public RemoteComponentsTransportTestClass(File workFolder) {
            super(workFolder);
        }

        @Override
        Transport getTransport() {
            Transport transport = null;
            try {
                transport = super.getTransport();
            } catch (Exception e) {
                //
            }
            if (transport == null) { // seems when dev, will null
                transport = new Transport() {

                    @Override
                    public IStatus download(URI toDownload, OutputStream target, long startPos, IProgressMonitor monitor) {
                        return null;
                    }

                    @Override
                    public IStatus download(URI toDownload, OutputStream target, IProgressMonitor monitor) {
                        final Bundle bundle = FrameworkUtil.getBundle(this.getClass());
                        try {
                            final InputStream inputStream = toDownload.toURL().openStream();
                            FileCopyUtils.copyStreams(inputStream, target);
                        } catch (Exception e) {
                            return new Status(IStatus.ERROR, bundle.getSymbolicName(), "ERROR", e);
                        }
                        return new Status(IStatus.OK, bundle.getSymbolicName(), "OK");
                    }

                    @Override
                    public InputStream stream(URI toDownload, IProgressMonitor monitor) throws FileNotFoundException,
                            CoreException, AuthenticationFailedException {
                        return null;
                    }

                    @Override
                    public long getLastModified(URI toDownload, IProgressMonitor monitor) throws CoreException,
                            FileNotFoundException, AuthenticationFailedException {
                        return 0;
                    }

                };
            }
            return transport;
        }

    }

    File tmpFolder = null;

    @Before
    public void prepare() {
        tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("transport", "test"); //$NON-NLS-1$  //$NON-NLS-2$
    }

    @After
    public void clean() {
        if (tmpFolder != null) {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
    }

    @Test
    public void test_downloadFile_empty() throws Exception {
        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder);
        File downloadFile = transport.downloadFile(null, null, null);
        Assert.assertNull(downloadFile);

        final URI uri = URI.create("file:///");
        downloadFile = transport.downloadFile(null, uri, "");
        Assert.assertNull(downloadFile);

        downloadFile = transport.downloadFile(null, uri, "  ");
        Assert.assertNull(downloadFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_downloadFile_localFile_notExisted() throws Exception {
        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder);
        final URI uri = URI.create("file:///tmp");
        transport.downloadFile(null, uri, "abc.txt");
    }

    @Test
    public void test_downloadFile_localFile_existed() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/components/"
                + NewComponentsInstallFactory.FILE_COMPONENTS_INDEX);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder);
        final File downloadFile = transport.downloadFile(null, testDataFile.getParentFile().toURI(),
                NewComponentsInstallFactory.FILE_COMPONENTS_INDEX);
        Assert.assertNotNull(downloadFile);
        Assert.assertTrue(downloadFile.exists());
        Assert.assertTrue(downloadFile.length() > 0); // not empty file

        Assert.assertEquals("Not same contents after download", FilesUtils.getChecksumAlder32(testDataFile),
                FilesUtils.getChecksumAlder32(downloadFile));

    }

    @Test
    public void test_downloadFile_siteFile_notExisted() throws Exception {
        try {
            RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder);
            final URI uri = URI.create("https://raw.githubusercontent.com/Talend/tcommon-studio-se/master");
            transport.downloadFile(null, uri, "abc.txt");

            Assert.assertTrue("File shouldn't be found", false); // never be here
        } catch (UnknownHostException e) {
            // nothing to do, when can't access web
        } catch (FileNotFoundException e) {
            // if enable to access, will be this exception
            Assert.assertTrue("File shouldn't be found", true);
        }
    }

    @Test
    public void test_downloadFile_siteFile_existed() throws Exception {
        try {
            RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder);
            URI uri = URI.create("https://raw.githubusercontent.com/Talend/tcommon-studio-se/master");
            final String fileName = "README.md";
            File downloadFile = transport.downloadFile(null, uri, fileName);
            Assert.assertNotNull(downloadFile);
            Assert.assertTrue("Download failure from " + uri + " for file: " + fileName, downloadFile.exists());
            Assert.assertTrue(downloadFile.length() > 0); // not empty file

            // with last /
            uri = URI.create("https://raw.githubusercontent.com/Talend/tcommon-studio-se/master/");
            downloadFile = transport.downloadFile(null, uri, fileName);
            Assert.assertNotNull(downloadFile);
            Assert.assertTrue("Download failure from " + uri + " for file: " + fileName, downloadFile.exists());
            Assert.assertTrue(downloadFile.length() > 0); // not empty file
        } catch (UnknownHostException e) {
            // nothing to do, when can't access web
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void test_retrieveComponents_null() throws Exception {
        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder) {

            @Override
            File downloadFile(IProgressMonitor monitor, URI base, String name) throws IOException {
                return null;
            }

        };
        transport.retrieveComponents(null, null);
    }

    @Test
    public void test_retrieveComponents() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/components/"
                + NewComponentsInstallFactory.FILE_COMPONENTS_INDEX);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder) {

            @Override
            File downloadFile(IProgressMonitor monitor, URI base, String name) throws IOException {
                return testDataFile; // use fake file
            }

        };
        // null uri can work for fake file
        List<RemoteComponent> components = transport.retrieveComponents(null, null);
        Assert.assertTrue(transport.getFailedDownloadComponents().isEmpty());
        Assert.assertEquals(4, components.size());

        RemoteComponent comp1 = components.get(0);
        Assert.assertEquals("JIRA", comp1.name);
        Assert.assertEquals("0.18.0", comp1.version);
        Assert.assertEquals("org.talend.components.jira_0.18.0.zip", comp1.file);

        RemoteComponent comp2 = components.get(1);
        Assert.assertEquals("JIRA", comp2.name);
        Assert.assertEquals("0.18.1", comp2.version);
        Assert.assertEquals("org.talend.components.jira_0.18.1.zip", comp2.file);

        RemoteComponent comp3 = components.get(2);
        Assert.assertEquals("JIRA", comp3.name);
        Assert.assertEquals("0.18.2", comp3.version);
        Assert.assertEquals("org.talend.components.jira_0.18.2.zip", comp3.file);

        RemoteComponent comp4 = components.get(3);
        Assert.assertEquals("MyJira", comp4.name);
        Assert.assertEquals("0.16.0", comp4.version);
        Assert.assertEquals("components-myjira-0.16.0-SNAPSHOT-updatesite.zip", comp4.file);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_download_retrieveFailure() throws Exception {
        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder) {

            @Override
            List<RemoteComponent> retrieveComponents(IProgressMonitor monitor, URI base) throws Exception {
                return null;
            }

        };

        transport.download(null, null); // null uri can work for fake file
    }

    @Test(expected = FileNotFoundException.class)
    public void test_download_retrieveFailureEmpty() throws Exception {
        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder) {

            @Override
            List<RemoteComponent> retrieveComponents(IProgressMonitor monitor, URI base) throws Exception {
                return Collections.emptyList();
            }

        };

        transport.download(null, null); // null uri can work for fake file
    }

    @Test
    public void test_download_componentNotExisted() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/components/"
                + NewComponentsInstallFactory.FILE_COMPONENTS_INDEX);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder) {

            @Override
            List<RemoteComponent> retrieveComponents(IProgressMonitor monitor, URI base) throws Exception {
                List<RemoteComponent> compsList = new ArrayList<RemoteComponent>();
                RemoteComponent comp1 = new RemoteComponent();
                comp1.name = "MyJira";
                comp1.version = "0.18.0";
                comp1.file = "components-myjira-0.18.0.zip";
                compsList.add(comp1);

                RemoteComponent comp2 = new RemoteComponent();
                comp2.name = "MyJira";
                comp2.version = "0.18.2";
                comp2.file = "components-myjira-0.18.2.zip";
                compsList.add(comp2);

                return compsList;
            }

        };

        transport.download(null, testDataFile.getParentFile().toURI());

        final List<String> failedDownloadComponents = transport.getFailedDownloadComponents();
        Assert.assertEquals(2, failedDownloadComponents.size());
        Assert.assertEquals("components-myjira-0.18.0.zip", failedDownloadComponents.get(0));
        Assert.assertEquals("components-myjira-0.18.2.zip", failedDownloadComponents.get(1));

        Assert.assertTrue(tmpFolder.exists());
        Assert.assertEquals(0, tmpFolder.list().length); // no any file downloaded
    }

    @Test
    public void test_download() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        RemoteComponentsTransportTestClass transport = new RemoteComponentsTransportTestClass(tmpFolder) {

            @Override
            List<RemoteComponent> retrieveComponents(IProgressMonitor monitor, URI base) throws Exception {
                List<RemoteComponent> compsList = new ArrayList<RemoteComponent>();
                RemoteComponent comp = new RemoteComponent();
                comp.name = "MyJira";
                comp.version = "0.16.0";
                comp.file = "components-myjira-0.16.0-SNAPSHOT-updatesite.zip";
                compsList.add(comp);

                return compsList;
            }

        };

        transport.download(null, testDataFile.getParentFile().toURI());
        Assert.assertTrue(tmpFolder.exists());
        Assert.assertEquals(1, tmpFolder.list().length); // no index, but have the comp zip

        File compUpdatesiteZip = new File(tmpFolder, "components-myjira-0.16.0-SNAPSHOT-updatesite.zip");
        Assert.assertTrue(compUpdatesiteZip.exists());
        Assert.assertEquals(FilesUtils.getChecksumAlder32(testDataFile), FilesUtils.getChecksumAlder32(compUpdatesiteZip));
    }
}
