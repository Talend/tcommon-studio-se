// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.utils.ProductVersion;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class VersionUtilsTest {

    private File mojo_properties;

    private File eclipseproductFile;

    @Before
    public void setUp() throws Exception {
        mojo_properties = new Path(Platform.getConfigurationLocation().getURL().getPath()).append("mojo_version.properties") //$NON-NLS-1$
                .toFile();
        backupEcilpseproductFile();
    }

    /**
     * Test method for {@link org.talend.commons.utils.VersionUtils#getTalendVersion()}.
     */
    @Test
    public void testGetTalendVersion() {
        ProductVersion talendVersion = ProductVersion.fromString(VersionUtils.getTalendVersion());
        ProductVersion studioVersion = ProductVersion.fromString(VersionUtils.getDisplayVersion());
        assertEquals(studioVersion, talendVersion);
    }

    @Test
    public void testGetPluginVersion__Eclipseproduct() throws Exception {
        String talendVersion = VersionUtils.getTalendVersion();
        setPropertiesValue(eclipseproductFile, "version", talendVersion + ".20190500_1200-SNAPSHOT");
        assertEquals(talendVersion + "-SNAPSHOT", VersionUtils.getMojoVersion("ci.builder.version"));

        setPropertiesValue(eclipseproductFile, "version", talendVersion + ".20190500_1200-M5");
        assertEquals(talendVersion + "-M5", VersionUtils.getMojoVersion("ci.builder.version"));

        setPropertiesValue(eclipseproductFile, "version", talendVersion + ".20190500_1200");
        assertEquals(talendVersion, VersionUtils.getMojoVersion("ci.builder.version"));

        // for other revision, use release version as default.
        setPropertiesValue(eclipseproductFile, "version", talendVersion + ".20190500_1200-RC1");
        assertEquals(talendVersion, VersionUtils.getMojoVersion("ci.builder.version"));
    }

    private void setPropertiesValue(File propertiesFile, String key, String value) throws Exception {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        try (OutputStream out = new FileOutputStream(propertiesFile)) {
            properties.store(out, "From junit");
        }
    }

    private void backupEcilpseproductFile() throws Exception {
        File installFolder = URIUtil.toFile(URIUtil.toURI(Platform.getInstallLocation().getURL()));
        eclipseproductFile = new File(installFolder, ".eclipseproduct");//$NON-NLS-1$
        File targetFile = new File(installFolder, "bak.eclipseproduct");//$NON-NLS-1$
        if (targetFile.exists()) {
            targetFile.delete();
        }
        FilesUtils.copyFile(eclipseproductFile, targetFile);
    }

    private void restoreEclipseproductFile() throws Exception {
        File installFolder = URIUtil.toFile(URIUtil.toURI(Platform.getInstallLocation().getURL()));
        File backupFile = new File(installFolder, "bak.eclipseproduct");//$NON-NLS-1$
        File targetFile = eclipseproductFile;
        try {
            if (targetFile.exists()) {
                targetFile.delete();
            }
            FilesUtils.copyFile(backupFile, targetFile);
        } finally {
            backupFile.delete();
        }
    }

    @Test
    public void testGetTalendPureVersion() {
        String expect = "7.2.1";
        String test = "Talend Cloud Big Data-7.2.1.20190620_1446";
        String result = VersionUtils.getTalendPureVersion(test);
        assertEquals(expect,result);
        
        test = "Talend Cloud Real-Time Big Data Platform-7.2.1.20190620_1446";
        result = VersionUtils.getTalendPureVersion(test);
        assertEquals(expect, result);

        expect = "7.3.1";
        test = "Talend Cloud Big Data-7.3.1.20190917_1941-SNAPSHOT";
        result = VersionUtils.getTalendPureVersion(test);
        assertEquals(expect, result);

        test = "Talend Cloud Real-Time Big Data Platform-7.3.1.20190917_1941-SNAPSHOT";
        result = VersionUtils.getTalendPureVersion(test);
        assertEquals(expect,result);
    }

    @Test
    public void testGetProductVersionWithoutBranding() {
        String expect = "7.3.1.20190620_1446";
        String test = "Talend Cloud Big Data-7.3.1.20190620_1446";
        String result = VersionUtils.getProductVersionWithoutBranding(test);
        assertEquals(expect,result);
        
        test = "Talend Cloud Real-Time Big Data Platform-7.3.1.20190620_1446";
        result = VersionUtils.getProductVersionWithoutBranding(test);
        assertEquals(expect, result);

        expect = "7.3.1.20190917_1941-SNAPSHOT";
        test = "Talend Cloud Big Data-7.3.1.20190917_1941-SNAPSHOT";
        result = VersionUtils.getProductVersionWithoutBranding(test);
        assertEquals(expect, result);

        test = "Talend Cloud Real-Time Big Data Platform-7.3.1.20190917_1941-SNAPSHOT";
        result = VersionUtils.getProductVersionWithoutBranding(test);
        assertEquals(expect,result);

        expect = "7.3.1.20190919_1941-patch";
        test = "Talend Cloud Big Data-7.3.1.20190919_1941-patch";
        result = VersionUtils.getProductVersionWithoutBranding(test);
        assertEquals(expect, result);

        test = "Talend Cloud Real-Time Big Data Platform-7.3.1.20190919_1941-patch";
        result = VersionUtils.getProductVersionWithoutBranding(test);
        assertEquals(expect, result);
    }

    @Test
    public void testIsInvalidProductVersion() {
        // test nightly/milestone build
        // do not check
        assertFalse(VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-SNAPSHOT",
                "Talend Cloud Big Data-7.3.1.20200209_1446-SNAPSHOT"));
        assertFalse(VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-M1",
                "Talend Cloud Big Data-7.3.1.20200209_1446-SNAPSHOT"));
        assertFalse(
                VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-M1", "Talend Cloud Big Data-7.3.1.20200209_1446-M2"));
        assertFalse(VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-SNAPSHOT",
                "Talend Cloud Big Data-7.3.1.20200209_1446-M2"));
        // do check
        assertTrue(VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-SNAPSHOT",
                "Talend Cloud Big Data-7.3.1.20200209_1446-patch"));
        assertTrue(VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-M1",
                "Talend Cloud Big Data-7.3.1.20200209_1446-patch"));

        // test release build
        assertTrue(
                VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941", "Talend Cloud Big Data-7.3.1.20200209_1446-patch"));
        assertTrue(VersionUtils.isInvalidProductVersion("7.3.1.20200201_1941-patch",
                "Talend Cloud Big Data-7.3.1.20200209_1446-patch"));
        assertFalse(
                VersionUtils.isInvalidProductVersion("7.3.1.20200209_1941-patch", "Talend Cloud Big Data-7.3.1.20200201_1446"));

    }

    @After
    public void tearDown() throws Exception {
        if (mojo_properties != null && mojo_properties.exists()) {
            mojo_properties.delete();
        }
        restoreEclipseproductFile();
    }

}
