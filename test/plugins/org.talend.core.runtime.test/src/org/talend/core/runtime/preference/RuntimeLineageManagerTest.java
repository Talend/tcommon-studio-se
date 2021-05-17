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
package org.talend.core.runtime.preference;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.core.runtime.projectsetting.RuntimeLineageManager;
import org.talend.repository.ProjectManager;
import org.talend.utils.json.JSONObject;

import us.monoid.json.JSONArray;

/**
 * created by hcyi on Jul 29, 2020
 * Detailled comment
 *
 */
public class RuntimeLineageManagerTest {

    static final String TEST_FILE_PREFIX = "org.talend.runtimeLineage"; //$NON-NLS-1$

    private RuntimeLineageManager runtimeLineageManager = new RuntimeLineageManager();

    @BeforeClass
    @AfterClass
    public static void clean() throws PersistenceException {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        IProject project = ResourceUtils.getProject(currentProject);
        IFolder settingsFolder = project.getFolder(ProjectPreferenceManager.DEFAULT_PREFERENCES_DIRNAME);
        if (settingsFolder.exists()) {
            File folder = settingsFolder.getLocation().toFile();
            File[] testPrefFiles = folder.listFiles(new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith(TEST_FILE_PREFIX);
                }
            });
            if (testPrefFiles != null) {
                for (File f : testPrefFiles) {
                    f.delete();
                }
            }
        }
    }

    public void init() {
        try {
            ProjectPreferenceManager prefManager = runtimeLineageManager.getPrefManager();
            JSONArray jobsJson = new JSONArray();
            JSONObject jobJson = new JSONObject();
            jobJson.put(runtimeLineageManager.JOB_ID, "_HT5BMNFmEeqhpr5Qh0-X9g");
            jobsJson.put(jobJson);
            prefManager.setValue(runtimeLineageManager.RUNTIMELINEAGE_ALL, false);
            prefManager.setValue(runtimeLineageManager.RUNTIMELINEAGE_SELECTED, jobsJson.toString());
            prefManager.save();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @Test
    public void testSaveValue4NULL() {
        runtimeLineageManager.save(null, true);
        runtimeLineageManager.load();

        Assert.assertEquals(0, runtimeLineageManager.getSelectedJobIds().size());
    }

    @Test
    public void testIsRuntimeLineageSetting4NULL() {
        Assert.assertFalse(runtimeLineageManager.isRuntimeLineageSetting(null));
    }

    @Test
    public void testIsRuntimeLineageSetting() {
        init();
        runtimeLineageManager.load();

        Assert.assertTrue(runtimeLineageManager.isRuntimeLineageSetting("_HT5BMNFmEeqhpr5Qh0-X9g")); //$NON-NLS-1$
    }

    @Test
    public void testIsRuntimeLineagePrefsExist() {
        runtimeLineageManager.save(null, true);

        Assert.assertTrue(runtimeLineageManager.isRuntimeLineagePrefsExist());
    }

    @Test
    public void testIsUseRuntimeLineageAll() {
        runtimeLineageManager.save(null, true);
        ProjectPreferenceManager prefManager = runtimeLineageManager.getPrefManager();

        Assert.assertTrue(prefManager.getBoolean(RuntimeLineageManager.RUNTIMELINEAGE_ALL));
    }
}
