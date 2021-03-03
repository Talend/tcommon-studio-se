// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.utils.io.FilesUtils;

public class UpdateToolsTest {

    private List<File> testFiles = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        testFiles.clear();
    }

    @Test
    public void testCollectAndCleanUpDropBundles() throws Exception {
        String talendBundleNameA = "org.talend.existing.a";
        String talendBundleNameB = "org.talend.existing.b";
        String oldTalendVersion = "7.3.1.20191101_0200";
        String newTalendVersion = "7.3.1.20191226_1111";

        String _3rdBundleNameA = "third.party.a";
        String _3rdBundleNameB = "third.party.b";
        String old3rdVersion = "7.3.1.20180101_0000";
        String new3rdVersion = "7.3.1.20190701_2222";

        Set<IInstallableUnit> validInstall = new HashSet<>();
        // new patch bundle for existing ones
        validInstall.add(createIU(talendBundleNameA, Version.create(newTalendVersion), false));
        validInstall.add(createIU(talendBundleNameB, Version.create(newTalendVersion), true));
        // new extra feature' bundle
        validInstall.add(createIU("org.talend.new.c", Version.create(newTalendVersion), false));
        // new third-party patch bundle
        validInstall.add(createIU(_3rdBundleNameA, Version.create(new3rdVersion), false));
        validInstall.add(createIU(_3rdBundleNameB, Version.create(new3rdVersion), false));

        // bundles defined in new_feature.index
        Map<String, String> extraBundles = new HashMap<>();
        extraBundles.put("org.talend.new.c", "");
        extraBundles.put("third.party.a", new3rdVersion);
        extraBundles.put("third.party.b", new3rdVersion);

        // drop bundles defined in new_feature.index
        Map<String, String> dropBundleInfo = new HashMap<>();
        dropBundleInfo.put(_3rdBundleNameA, old3rdVersion);
        dropBundleInfo.put(_3rdBundleNameB, old3rdVersion);

        // create test plugins
        createFile(talendBundleNameA, oldTalendVersion, false);
        createFile(talendBundleNameB, oldTalendVersion, true);
        createFile(_3rdBundleNameA, old3rdVersion, false);
        createFile(_3rdBundleNameA, new3rdVersion, false);
        createFile(_3rdBundleNameB, old3rdVersion, false);
        createFile(_3rdBundleNameB, new3rdVersion, false);

        File pluginFolder = UpdateTools.getProductRootFolder().toPath().resolve("plugins").toFile();
        File dropFile = new File(pluginFolder, "droplist");
        testFiles.add(dropFile);
        List<File> pluginsToDrop = new ArrayList<>();
        pluginsToDrop.add(new File(pluginFolder, talendBundleNameA + "_" + oldTalendVersion + ".jar"));
        pluginsToDrop.add(new File(pluginFolder, talendBundleNameB + "_" + oldTalendVersion));
        pluginsToDrop.add(new File(pluginFolder, _3rdBundleNameA + "_" + old3rdVersion + ".jar"));
        pluginsToDrop.add(new File(pluginFolder, _3rdBundleNameB + "_" + old3rdVersion + ".jar"));

        UpdateTools.collectDropBundles(validInstall, extraBundles, dropBundleInfo);

        assertTrue(dropFile.exists());
        List<String> dropList = Files.readAllLines(dropFile.toPath());
        assertEquals(pluginsToDrop.size(), dropList.size());
        pluginsToDrop.forEach(f -> {
            assertTrue(dropList.contains(f.getAbsolutePath()));
        });

        int sizeBefore = pluginFolder.listFiles().length;
        UpdateTools.cleanUpDropBundles();
        int sizeAfter = pluginFolder.listFiles().length;

        // also droplist file will be deleted.
        assertEquals(pluginsToDrop.size() + 1, sizeBefore - sizeAfter);
        pluginsToDrop.forEach(f -> assertFalse(f.exists()));
        assertFalse(dropFile.exists());
    }

    @Test
    @Ignore
    /**
     * can not test in Linux, all files can always be deleted.
     */
    public void testCleanUpDropBundles_DeleteFail() throws Exception {
        File pluginFolder = UpdateTools.getProductRootFolder().toPath().resolve("plugins").toFile();
        File dropFile = new File(pluginFolder, "droplist");
        testFiles.add(dropFile);
        File testFile1 = new File(pluginFolder, "file.can.not.delete_7.3.1.20190101_0300.jar");
        testFile1.createNewFile();
        testFiles.add(testFile1);
        File testFile2 = new File(pluginFolder, "file.can.delete_7.3.1.20190101_0300.jar");
        testFile2.createNewFile();
        testFiles.add(testFile2);
        Files.write(dropFile.toPath(), Arrays.asList(testFile1.getAbsolutePath(), testFile2.getAbsolutePath()));
        // make it occupied
        try (InputStream in = new FileInputStream(testFile1)) {
            UpdateTools.cleanUpDropBundles();
        }
        List<String> list = Files.readAllLines(dropFile.toPath());
        assertEquals(1, list.size());
        assertEquals(testFile1.getAbsolutePath(), list.get(0));
        assertFalse(testFile2.exists());
    }

    @SuppressWarnings("restriction")
    private IInstallableUnit createIU(String id, Version version, boolean isFolder) throws IOException {
        InstallableUnit iu = new InstallableUnit();
        iu.setId(id);
        iu.setVersion(version);
        createFile(id, version.toString(), isFolder);
        return iu;
    }

    private void createFile(String id, String version, boolean isFolder) throws IOException {
        // create test plugin file.
        File pluginFolder = UpdateTools.getProductRootFolder().toPath().resolve("plugins").toFile();
        String fileName = id + "_" + version;
        File bundle;
        if (!isFolder) {
            fileName += ".jar";
            bundle = new File(pluginFolder, fileName);
            bundle.createNewFile();
        } else {
            bundle = new File(pluginFolder, fileName);
            bundle.mkdir();
            File testFile = new File(bundle, "test.txt");
            testFile.createNewFile();
        }
        testFiles.add(bundle);
    }

    @After
    public void tearDown() {
        testFiles.stream().filter(f -> f.exists()).forEach(f -> {
            if (f.isDirectory()) {
                FilesUtils.deleteFolder(f, true);
            } else {
                f.delete();
            }
        });
    }

}
