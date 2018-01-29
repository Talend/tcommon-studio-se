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
package org.talend.core.runtime.preference;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProjectPreferenceManagerTest {

    static final String TEST_FILE_PREFIX = "org.talend.project.prefs.test";

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

    private String getTestQualifier(String baseName) {
        return TEST_FILE_PREFIX + '.' + baseName + System.currentTimeMillis();
    }

    @Test
    public void testCurrentProject() {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        testExist(new ProjectPreferenceManager('A' + getTestQualifier(currentProject.getTechnicalLabel())));
    }

    @Test
    public void testCurrentProject2() {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        testExist(new ProjectPreferenceManager(currentProject, 'B' + getTestQualifier(currentProject.getTechnicalLabel())));
    }

    @Test
    public void testCurrentProject3() throws PersistenceException {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        IProject project = ResourceUtils.getProject(currentProject);
        testExist(new ProjectPreferenceManager(project, 'C' + getTestQualifier(currentProject.getTechnicalLabel())));
    }

    private void testExist(ProjectPreferenceManager manager) {
        Assert.assertFalse(manager.exist());
        manager.setValue("key", "value"); // must set one value to make sure it's dirty
        manager.save();
        Assert.assertTrue(manager.exist());
    }

    @Test
    @Ignore
    public void testStringValue() {
        String testQualifier = getTestQualifier("TEST");
        ProjectPreferenceManager projectPrefManager = new ProjectPreferenceManager(testQualifier);
        projectPrefManager.setValue("abc", "xyz");
        projectPrefManager.setValue("key", "123");
        Assert.assertEquals("xyz", projectPrefManager.getValue("abc"));
        Assert.assertEquals("123", projectPrefManager.getValue("key"));
        Assert.assertNull(projectPrefManager.getValue("xxx"));
        testExist(projectPrefManager);

        ProjectPreferenceManager readProjectPrefManager = new ProjectPreferenceManager(testQualifier);
        Assert.assertTrue(readProjectPrefManager.exist());
        Assert.assertEquals("xyz", readProjectPrefManager.getValue("abc"));
        Assert.assertEquals("123", readProjectPrefManager.getValue("key"));
        Assert.assertNull(readProjectPrefManager.getValue("xxx"));

    }

    @Test
    @Ignore
    public void testIntValue() {
        String testQualifier = getTestQualifier("TEST");
        ProjectPreferenceManager projectPrefManager = new ProjectPreferenceManager(testQualifier);
        projectPrefManager.setValue("abc", 1);
        projectPrefManager.setValue("key", 2);
        Assert.assertEquals(1, projectPrefManager.getInt("abc"));
        Assert.assertEquals(2, projectPrefManager.getInt("key"));
        Assert.assertEquals(-1, projectPrefManager.getInt("xxx"));
        testExist(projectPrefManager);

        ProjectPreferenceManager readProjectPrefManager = new ProjectPreferenceManager(testQualifier);
        Assert.assertTrue(readProjectPrefManager.exist());
        Assert.assertEquals(1, readProjectPrefManager.getInt("abc"));
        Assert.assertEquals(2, readProjectPrefManager.getInt("key"));
        Assert.assertEquals(-1, readProjectPrefManager.getInt("xxx"));

    }

    @Test
    public void testBooleanValue() {
        String testQualifier = getTestQualifier("TEST");
        ProjectPreferenceManager projectPrefManager = new ProjectPreferenceManager(testQualifier);
        projectPrefManager.setValue("abc", true);
        projectPrefManager.setValue("key", false);
        Assert.assertEquals(true, projectPrefManager.getBoolean("abc"));
        Assert.assertEquals(false, projectPrefManager.getBoolean("key"));
        Assert.assertEquals(false, projectPrefManager.getBoolean("xxx"));
        testExist(projectPrefManager);

        ProjectPreferenceManager readProjectPrefManager = new ProjectPreferenceManager(testQualifier);
        Assert.assertTrue(readProjectPrefManager.exist());
        Assert.assertEquals(true, readProjectPrefManager.getBoolean("abc"));
        Assert.assertEquals(false, readProjectPrefManager.getBoolean("key"));
        Assert.assertEquals(false, readProjectPrefManager.getBoolean("xxx"));

    }
    
    @Test
    public void testReload() {
        String testQualifier = getTestQualifier("TEST");
        ProjectPreferenceManager projectPrefManager = new ProjectPreferenceManager(testQualifier);
        IPreferenceStore originalStore = projectPrefManager.getPreferenceStore();
        ProjectScope originalScope = projectPrefManager.getProjectScope();
        projectPrefManager.reload();
        assertNotEquals(projectPrefManager.getProjectScope(), originalScope);
        assertNotEquals(projectPrefManager.getPreferenceStore(), originalStore);
    }

}
