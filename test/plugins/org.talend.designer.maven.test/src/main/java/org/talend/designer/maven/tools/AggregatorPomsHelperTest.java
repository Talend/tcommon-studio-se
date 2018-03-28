// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.m2e.core.MavenPlugin;
import org.junit.Test;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.repository.ProjectManager;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class AggregatorPomsHelperTest {

    @Test
    public void testAddToAndRemoveFromParentModules() throws Exception {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        final IProject project = root.getProject(projectTechName);
        IFolder pomsFolder = project.getFolder(TalendJavaProjectConstants.DIR_POMS);
        IFolder jobFolder = pomsFolder.getFolder("jobs").getFolder("process").getFolder("job1");
        if (!jobFolder.exists()) {
            jobFolder.create(true, true, null);
        }
        IFile jobPom = jobFolder.getFile("pom.xml");
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel("Job1");
        property.setVersion("1.0");
        AggregatorPomsHelper.addToParentModules(jobPom,null);

        IFile projectPom = pomsFolder.getFile("pom.xml");
        Model model = MavenPlugin.getMavenModelManager().readMavenModel(projectPom);
        assertNotNull(model.getModules());
        assertTrue(model.getModules().contains("jobs/process/job1"));

        AggregatorPomsHelper.removeFromParentModules(jobPom);

        model = MavenPlugin.getMavenModelManager().readMavenModel(projectPom);
        assertNotNull(model.getModules());
        assertFalse(model.getModules().contains("jobs/process/job1"));
    }

    @Test
    public void testGetJobProjectName() {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel("Job1");
        property.setVersion("1.0");
        String jobProjectName = new AggregatorPomsHelper(projectTechName).getJobProjectName(property);
        assertEquals(projectTechName + "_JOB1_1.0", jobProjectName);
    }

    @Test
    public void getJobProjectFolderName() {
        String label = "Job1";
        String version = "1.0";
        String jobFolderName = AggregatorPomsHelper.getJobProjectFolderName(label, version);
        assertEquals("job1_1.0", jobFolderName);
    }

    @Test
    public void testgetJobProjectId() {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        String id = "abcde-_e";
        String version = "1.0";
        String jobProjectId = AggregatorPomsHelper.getJobProjectId(projectTechName, id, version);
        assertEquals(projectTechName + "|abcde-_e|1.0", jobProjectId);
    }

}
