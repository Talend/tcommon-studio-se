// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.resource.UpdatesHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class P2InstallerTest {

    class P2InstallerTestClass extends P2Installer {

        protected void copyConfigFile(boolean restore) throws IOException {
            super.copyConfigFile(restore);
        }

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
            pw.println("test123=abc");
        } finally {
            pw.close();
        }
        testConfigIni(configrationFile, true);
        installer.copyConfigFile(true);
        testConfigIni(configrationFile, false);
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

}
