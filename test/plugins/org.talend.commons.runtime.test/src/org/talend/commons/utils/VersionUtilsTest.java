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

    @Test
    public void testGetPluginVersion__MojoProperties() throws Exception {
        String talendVersion = VersionUtils.getTalendVersion();
        assertEquals(talendVersion + "-SNAPSHOT", VersionUtils.getMojoVersion("ci.builder.version"));
        if (mojo_properties.exists()) {
            mojo_properties.delete();
        }
        mojo_properties.createNewFile();
        setPropertiesValue(mojo_properties, "ci.builder.version", talendVersion + "-patch1");
        assertEquals(talendVersion + "-patch1", VersionUtils.getMojoVersion("ci.builder.version"));

        setPropertiesValue(mojo_properties, "ci.builder.version", "6.0.0");
        assertEquals(talendVersion + "-SNAPSHOT", VersionUtils.getMojoVersion("ci.builder.version"));
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

    @After
    public void tearDown() throws Exception {
        if (mojo_properties != null && mojo_properties.exists()) {
            mojo_properties.delete();
        }
        restoreEclipseproductFile();
    }

}
