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
package org.talend.designer.maven.template;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AbstractMavenTemplateManagerTest {

    class MavenTemplateManagerTestClass extends AbstractMavenTemplateManager {

        MavenTemplateManagerTestClass() {
            this.bundleName = DesignerMavenPlugin.PLUGIN_ID;
        }
    }

    @Test
    public void test_getProjectPreferenceManager_empty() throws Exception {
        MavenTemplateManagerTestClass templateManager = new MavenTemplateManagerTestClass();
        final ProjectPreferenceManager defaultPreferenceManager = templateManager.getProjectPreferenceManager();
        Assert.assertNotNull(defaultPreferenceManager);

        // null parameter
        ProjectPreferenceManager projectPreferenceManager = templateManager.getProjectPreferenceManager(null);
        Assert.assertNotNull(projectPreferenceManager);
        // still default one
        Assert.assertEquals(projectPreferenceManager.getProject().getName(), defaultPreferenceManager.getProject().getName());

        // empty parameter
        projectPreferenceManager = templateManager.getProjectPreferenceManager(Collections.emptyMap());
        Assert.assertNotNull(projectPreferenceManager);
        // still default one
        Assert.assertEquals(projectPreferenceManager.getProject().getName(), defaultPreferenceManager.getProject().getName());

        Map<String, Object> parameter = new HashMap<String, Object>();

        parameter.put(MavenTemplateManager.KEY_PROJECT_NAME, null);
        projectPreferenceManager = templateManager.getProjectPreferenceManager(parameter);
        Assert.assertNotNull(projectPreferenceManager);
        // still default one
        Assert.assertEquals(projectPreferenceManager.getProject().getName(), defaultPreferenceManager.getProject().getName());

        parameter.put(MavenTemplateManager.KEY_PROJECT_NAME, "");
        projectPreferenceManager = templateManager.getProjectPreferenceManager(parameter);
        Assert.assertNotNull(projectPreferenceManager);
        // still default one
        Assert.assertEquals(projectPreferenceManager.getProject().getName(), defaultPreferenceManager.getProject().getName());

    }

    @Test
    public void test_getProjectPreferenceManager_currentProject() throws Exception {
        MavenTemplateManagerTestClass templateManager = new MavenTemplateManagerTestClass();
        final ProjectPreferenceManager defaultPreferenceManager = templateManager.getProjectPreferenceManager();
        Assert.assertNotNull(defaultPreferenceManager);

        Map<String, Object> parameter = new HashMap<String, Object>();
        final String curTechnicalLabel = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        parameter.put(MavenTemplateManager.KEY_PROJECT_NAME, curTechnicalLabel);

        ProjectPreferenceManager projectPreferenceManager = templateManager.getProjectPreferenceManager(parameter);
        Assert.assertNotNull(projectPreferenceManager);

        Assert.assertEquals(defaultPreferenceManager.getProject().getName(), projectPreferenceManager.getProject().getName());
        Assert.assertEquals(curTechnicalLabel, projectPreferenceManager.getProject().getName());

    }

    @Test
    public void test_getProjectPreferenceManager_referenceProject() throws Exception {
        MavenTemplateManagerTestClass templateManager = new MavenTemplateManagerTestClass() {

            // get one fake one, and make sure no exception
            protected IProject getProject(String techName) throws PersistenceException {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IProject project = workspace.getRoot().getProject(techName);
                return project;
            }
        };
        final ProjectPreferenceManager defaultPreferenceManager = templateManager.getProjectPreferenceManager();
        Assert.assertNotNull(defaultPreferenceManager);

        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put(MavenTemplateManager.KEY_PROJECT_NAME, "ABC_TEST");

        ProjectPreferenceManager projectPreferenceManager = templateManager.getProjectPreferenceManager(parameter);
        Assert.assertNotNull(projectPreferenceManager);

        Assert.assertEquals("ABC_TEST", projectPreferenceManager.getProject().getName());

    }
}
