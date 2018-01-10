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
package org.talend.updates.runtime.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.resource.BundleFileUtil;
import org.talend.commons.utils.resource.UpdatesHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class P2InstallerTest {

    public static final String TEST_COMP_MYJIRA = "resources/components/components-myjira-0.16.0-SNAPSHOT-updatesite.zip";

    public static final String TEST_COMP_FAKE_MYJIRA = "resources/components/components-myjira-0.16.0-SNAPSHOT-fake.zip";

    class P2InstallerTestClass extends P2Installer {

        protected Set<InstalledUnit> installPatchRepository(File metadataRepository, File artifactRepository)
                throws ProvisionException {
            return Collections.emptySet();
        }
    }

    @Test
    public void test_copyConfigFile() throws IOException {
        P2InstallerTestClass installer = new P2InstallerTestClass();

        File configrationFile = new File(Platform.getConfigurationLocation().getURL().getPath(), UpdatesHelper.FILE_CONFIG_INI);
        Assert.assertTrue(configrationFile.exists());
        File tempConfigrationFile = new File(installer.getTmpInstallFolder(), UpdatesHelper.FILE_CONFIG_INI);
        Assert.assertFalse(tempConfigrationFile.exists());

        installer.copyConfigFile(false);
        Assert.assertTrue(tempConfigrationFile.exists());

        testConfigIni(configrationFile, false);
        // change contents
        PrintWriter pw = new PrintWriter(new FileWriter(configrationFile, true));
        try {
            pw.println();
            pw.println("test123=abc");
        } finally {
            pw.close();
        }
        testConfigIni(configrationFile, true);
        installer.copyConfigFile(true);
        testConfigIni(configrationFile, false);
        installer.clean();
    }

    private void testConfigIni(File configrationFile, boolean testExisted) throws IOException {
        Properties configProp = new Properties();
        FileInputStream fis = new FileInputStream(configrationFile);
        try {
            configProp.load(fis);
            if (testExisted) {
                Assert.assertTrue(configProp.containsKey("test123"));
                Assert.assertEquals("abc", configProp.getProperty("test123"));
            } else {
                Assert.assertFalse(configProp.containsKey("test123"));
            }
        } finally {
            fis.close();
        }
    }

    @Test
    public void test_installPatchFile_empty() throws Exception {
        P2InstallerTestClass installer = new P2InstallerTestClass();

        // null
        Set<InstalledUnit> installedUnits = installer.installPatchFile(null, true);
        Assert.assertTrue(installedUnits.isEmpty());

        // not existed
        installedUnits = installer.installPatchFile(new File("abc"), true);
        Assert.assertTrue(installedUnits.isEmpty());

        // folder
        installedUnits = installer.installPatchFile(installer.getTmpInstallFolder(), true);
        Assert.assertTrue(installedUnits.isEmpty());
        installer.clean();

        // not zip file
        File file = new File(installer.getTmpInstallFolder(), "abc.test");
        file.createNewFile();
        installedUnits = installer.installPatchFile(file, true);
        Assert.assertTrue(installedUnits.isEmpty());
        installer.clean();
    }

    @Test(expected = IOException.class)
    public void test_installPatchFile_validZip() throws Exception {
        P2InstallerTestClass installer = new P2InstallerTestClass();

        try {
            // not valid zip
            File file = new File(installer.getTmpInstallFolder(), "abc.zip");
            file.createNewFile();
            installer.installPatchFile(file, true);
        } finally {
            installer.clean();
        }
    }

    @Test
    public void test_installPatchFile() throws Exception {
        final File testDataFile = BundleFileUtil.getBundleFile(this.getClass(), TEST_COMP_MYJIRA);
        Assert.assertNotNull(testDataFile);
        Assert.assertTrue(testDataFile.exists());
        P2InstallerTestClass installer = new P2InstallerTestClass() {

            @Override
            public Set<InstalledUnit> installPatchFile(File updatesiteZip, boolean keepChangeConfigIni) throws Exception {
                Set<InstalledUnit> unit = new HashSet<InstalledUnit>();
                unit.add(new InstalledUnit("myJira", "0.16.0"));
                return unit;
            }

        };

        Set<InstalledUnit> installedUnits = installer.installPatchFile(testDataFile, true);
        Assert.assertFalse(installedUnits.isEmpty());
        Assert.assertEquals(1, installedUnits.size());
        final InstalledUnit one = installedUnits.iterator().next();
        Assert.assertEquals("myJira", one.getBundle());
        Assert.assertEquals("0.16.0", one.getVersion());
        installer.clean();
    }

    @Test
    public void test_installPatchFolder_empty() throws Exception {
        P2InstallerTestClass installer = new P2InstallerTestClass();

        // null
        Set<InstalledUnit> installedUnits = installer.installPatchFolder(null, true);
        Assert.assertTrue(installedUnits.isEmpty());

        // not existed
        installedUnits = installer.installPatchFolder(new File("abc"), true);
        Assert.assertTrue(installedUnits.isEmpty());

        // zip file
        File file = new File(installer.getTmpInstallFolder(), "abc.test");
        file.createNewFile();
        installedUnits = installer.installPatchFolder(file, true);
        Assert.assertTrue(installedUnits.isEmpty());
        installer.clean();

    }

    @Test
    public void test_installPatchFolder() throws Exception {
        P2InstallerTestClass installer = new P2InstallerTestClass() {

            protected Set<InstalledUnit> installPatchRepository(File metadataRepository, File artifactRepository)
                    throws ProvisionException {
                Set<InstalledUnit> unit = new HashSet<InstalledUnit>();
                unit.add(new InstalledUnit("test", "0.1.0"));
                return unit;
            }
        };
        Set<InstalledUnit> installedUnits = installer.installPatchFolder(installer.getTmpInstallFolder(), true);
        Assert.assertFalse(installedUnits.isEmpty());
        Assert.assertEquals(1, installedUnits.size());
        final InstalledUnit one = installedUnits.iterator().next();
        Assert.assertEquals("test", one.getBundle());
        Assert.assertEquals("0.1.0", one.getVersion());
        installer.clean();
    }
}
