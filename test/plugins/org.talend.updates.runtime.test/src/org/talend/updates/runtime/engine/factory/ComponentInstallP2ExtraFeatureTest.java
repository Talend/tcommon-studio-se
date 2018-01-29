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
package org.talend.updates.runtime.engine.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.updates.runtime.engine.P2InstallerTest;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.FeatureRepositories;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentInstallP2ExtraFeatureTest {

    class ComponentInstallP2ExtraFeatureTestClass extends NewComponentsInstallFactory.ComponentInstallP2ExtraFeature {

    }

    class ComponentsInstallComponentTestClass implements ComponentsInstallComponent {

        @Override
        public boolean install() {
            return false;
        }

        @Override
        public boolean needRelaunch() {
            return false;
        }

        @Override
        public String getInstalledMessages() {
            return null;
        }

        @Override
        public String getFailureMessage() {
            return null;
        }

        @Override
        public void setLogin(boolean login) {

        }

        @Override
        public void setComponentFolder(File componentFolder) {

        }

        @Override
        public List<File> getFailedComponents() {
            return Collections.emptyList();
        }

    }

    @Test
    public void test_prepareForLocalURI_null() throws IOException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();
        installP2.prepareForLocalURI(null);

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_prepareForLocalURI_site() throws IOException, URISyntaxException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();
        installP2.prepareForLocalURI(URI.create("https://update.talend.com/Studio/dev/"));

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_prepareForLocalURI_nonJarSchema() throws IOException, URISyntaxException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();
        installP2.prepareForLocalURI(URI.create("zip:" + "/abc/xyz.zip" + FeatureRepositories.URI_JAR_SUFFIX));

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_prepareForLocalURI_fileNotExisted() throws IOException, URISyntaxException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();
        File f = new File("abc.zip");
        installP2.prepareForLocalURI(URI.create(FeatureRepositories.URI_JAR_SCHEMA + ':' + f.toURI().toString()
                + FeatureRepositories.URI_JAR_SUFFIX));

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_prepareForLocalURI_normalZipFile() throws IOException, URISyntaxException {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), "resources/content.zip");
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();
        installP2.prepareForLocalURI(URI.create(FeatureRepositories.URI_JAR_SCHEMA + ':' + testDataFile.toURI().toString()
                + FeatureRepositories.URI_JAR_SUFFIX));

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_prepareForLocalURI_componentZipFile() throws IOException, URISyntaxException {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());

        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        installP2.prepareForLocalURI(URI.create(FeatureRepositories.URI_JAR_SCHEMA + ':' + testDataFile.toURI().toString()
                + FeatureRepositories.URI_JAR_SUFFIX));

        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(1, workFolder.list().length);

        File compZipFile = new File(workFolder, testDataFile.getName());
        assertTrue(compZipFile.exists());
        assertEquals(FilesUtils.getChecksumAlder32(testDataFile), FilesUtils.getChecksumAlder32(compZipFile));
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_doInstallFromFolder_noFiles() {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        final Status status = installP2.doInstallFromFolder();
        assertNull(status);
        assertFalse(installP2.needRestart());
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_doInstallFromFolder_allInstalled() throws IOException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass() {

            @Override
            ComponentsInstallComponent getInstallComponent() {
                return new ComponentsInstallComponentTestClass() {

                    @Override
                    public boolean install() {
                        return true;
                    }

                    @Override
                    public boolean needRelaunch() {
                        return true;
                    }

                    @Override
                    public String getInstalledMessages() {
                        return "All components insalled";
                    }
                };
            }

        };

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        new File(workFolder, "abc.txt").createNewFile(); // create fake file

        final Status status = installP2.doInstallFromFolder();
        assertNotNull(status);
        assertEquals(IStatus.OK, status.getSeverity());
        assertTrue(status.getMessage().contains("All components insalled"));

        assertTrue(installP2.needRestart());
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_doInstallFromFolder_noInstalled() throws IOException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass();

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        new File(workFolder, "abc.txt").createNewFile(); // create fake file

        final Status status = installP2.doInstallFromFolder();
        assertNotNull(status);
        assertEquals(IStatus.INFO, status.getSeverity());
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_doInstallFromFolder_someInstalled() throws IOException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass() {

            @Override
            ComponentsInstallComponent getInstallComponent() {
                return new ComponentsInstallComponentTestClass() {

                    @Override
                    public boolean install() {
                        return true;
                    }

                    @Override
                    public boolean needRelaunch() {
                        return true; // is some installed, should try restart
                    }

                    @Override
                    public String getInstalledMessages() {
                        return "Some components insalled";
                    }

                    @Override
                    public String getFailureMessage() {
                        return "Some components Failure";
                    }

                    @Override
                    public List<File> getFailedComponents() {
                        List<File> list = new ArrayList<File>();
                        list.add(new File("abc.zip"));
                        return list;
                    }
                };
            }

        };

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        new File(workFolder, "abc.txt").createNewFile(); // create fake file

        final Status status = installP2.doInstallFromFolder();
        assertNotNull(status);
        assertEquals(IStatus.ERROR, status.getSeverity());
        assertTrue(status.getMessage().contains("Some components insalled")
                && status.getMessage().contains("Some components Failure"));

        assertTrue(installP2.needRestart());
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test
    public void test_doInstallFromFolder_allFailure() throws IOException {
        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass() {

            @Override
            ComponentsInstallComponent getInstallComponent() {
                return new ComponentsInstallComponentTestClass() {

                    @Override
                    public String getFailureMessage() {
                        return "NO Components";
                    }

                    @Override
                    public List<File> getFailedComponents() {
                        List<File> list = new ArrayList<File>();
                        list.add(new File("abc.zip"));
                        return list;
                    }
                };
            }

        };

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        new File(workFolder, "abc.txt").createNewFile(); // create fake file

        final Status status = installP2.doInstallFromFolder();
        assertNotNull(status);
        assertEquals(IStatus.ERROR, status.getSeverity());
        assertTrue(status.getMessage().contains("NO Components"));

        assertFalse(installP2.needRestart());
        // clean
        FilesUtils.deleteFolder(workFolder, true);
    }

    @Test(expected = P2ExtraFeatureException.class)
    public void test_install_exception() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());
        List<URI> allRepoUris = new ArrayList<URI>();
        allRepoUris.add(new URI(FeatureRepositories.URI_JAR_SCHEMA + ':' + testDataFile.toURI().toString()
                + FeatureRepositories.URI_JAR_SUFFIX));

        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass() {

            void prepareForLocalURI(URI uri) throws IOException {
                throw new IOException();
            }
        };
        installP2.install(null, allRepoUris);
    }

    // @Test
    public void test_install_remoteSite() throws Exception {
        List<URI> allRepoUris = new ArrayList<URI>();
        allRepoUris.add(URI.create("https://update.talend.com/Studio/dev/"));

        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass() {

            void prepareForLocalURI(URI uri) throws IOException {
                assertTrue("Won't be here", false);
            }

            Status doInstallFromFolder() {
                return new Status(IStatus.INFO, Messages.getPlugiId(), "Nothing"); //$NON-NLS-1$
            }
        };
        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        IStatus status = installP2.install(null, allRepoUris);
        assertNotNull(status);
        assertEquals(IStatus.INFO, status.getSeverity());
        assertEquals("Nothing", status.getMessage());

        assertNotNull(workFolder);
        assertFalse(workFolder.exists()); // clean done
    }

    @Test
    public void test_install_severalZips() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), P2InstallerTest.TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());
        List<URI> allRepoUris = new ArrayList<URI>();
        allRepoUris.add(new URI(FeatureRepositories.URI_JAR_SCHEMA + ':' + testDataFile.toURI().toString()
                + FeatureRepositories.URI_JAR_SUFFIX));

        ComponentInstallP2ExtraFeatureTestClass installP2 = new ComponentInstallP2ExtraFeatureTestClass() {

            Status doInstallFromFolder() {
                final String[] list = getWorkFolder().list();
                assertNotNull(list);
                assertEquals(1, list.length);
                assertEquals(testDataFile.getName(), list[0]);

                return new Status(IStatus.OK, Messages.getPlugiId(), "Installed"); //$NON-NLS-1$
            }
        };

        final File workFolder = installP2.getWorkFolder();
        assertNotNull(workFolder);
        assertTrue(workFolder.exists());
        assertEquals(0, workFolder.list().length); // no any files

        IStatus status = installP2.install(null, allRepoUris);
        assertNotNull(status);
        assertEquals(IStatus.OK, status.getSeverity());
        assertEquals("Installed", status.getMessage());

        assertNotNull(workFolder);
        assertFalse(workFolder.exists()); // clean done
    }
}
