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
package org.talend.updates.runtime.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.eclipse.core.runtime.Platform;
import org.junit.Test;

/**
 * created by ycbai on 2017年5月23日 Detailled comment
 *
 */
public class PathUtilsTest {

    @Test
    public void testGetComponentsFolder() throws IOException {
        File componentsFolder = PathUtils.getComponentsFolder();
        assertNotNull(componentsFolder);
        assertTrue(componentsFolder.exists());
        File expectedFolder = new File(Platform.getConfigurationLocation().getDataArea(PathUtils.FOLDER_COMPS).getPath());
        assertEquals(expectedFolder, componentsFolder);
    }

    @Test
    public void testGetComponentsInstalledFolder() throws IOException {
        File intalledFolder = PathUtils.getComponentsInstalledFolder();
        assertNotNull(intalledFolder);
        assertTrue(intalledFolder.exists());
        File expectedFolder = new File(PathUtils.getComponentsFolder(), PathUtils.FOLDER_INSTALLED);
        assertEquals(expectedFolder, intalledFolder);
    }

    @Test
    public void testGetComponentsSharedFolder() throws IOException {
        File sharedFolder = PathUtils.getComponentsSharedFolder();
        assertNotNull(sharedFolder);
        assertTrue(sharedFolder.exists());
        File expectedFolder = new File(PathUtils.getComponentsFolder(), PathUtils.FOLDER_SHARED);
        assertEquals(expectedFolder, sharedFolder);
    }

    @Test
    public void testGetP2RepURIFromCompFile_null() throws IOException {
        URI p2RepURI = PathUtils.getP2RepURIFromCompFile(null);
        assertNull(p2RepURI);
    }

    @Test
    public void testGetP2RepURIFromCompFile_zip() throws IOException {
        File compFile = new File("/tmp/testcomp.zip");
        URI p2RepURI = PathUtils.getP2RepURIFromCompFile(compFile);
        URI expectURI = URI.create("jar:" + compFile.toURI().toString() + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expectURI, p2RepURI);
    }

    @Test
    public void testGetP2RepURIFromCompFile_jar() throws IOException {
        File compFile = new File("/tmp/testcomp.jar");
        URI p2RepURI = PathUtils.getP2RepURIFromCompFile(compFile);
        URI expectURI = URI.create("jar:" + compFile.toURI().toString() + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
        assertEquals(expectURI, p2RepURI);
    }

    @Test
    public void testGetP2RepURIFromCompFile_other() throws IOException {
        File compFile = File.createTempFile("testcomp.text", null); //$NON-NLS-1$
        URI p2RepURI = PathUtils.getP2RepURIFromCompFile(compFile);
        assertNull(p2RepURI);
        compFile.delete();
    }

    @Test
    public void testGetCompFileFromP2RepURI() throws IOException {
        File compFile = PathUtils.getCompFileFromP2RepURI(null);
        assertNull(compFile);

        File testCompFile = File.createTempFile("testcomp", null); //$NON-NLS-1$
        URI testP2RepURI = URI.create("jar:" + testCompFile.toURI().toString() + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
        compFile = PathUtils.getCompFileFromP2RepURI(testP2RepURI);
        assertEquals(testCompFile, compFile);
        assertTrue(compFile.exists());

        compFile = PathUtils.getCompFileFromP2RepURI(testCompFile.toURI());
        assertEquals(testCompFile, compFile);
        assertTrue(compFile.exists());

        testCompFile.delete();
    }

}
