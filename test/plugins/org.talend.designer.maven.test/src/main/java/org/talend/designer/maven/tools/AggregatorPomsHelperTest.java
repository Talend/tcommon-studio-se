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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.m2e.core.MavenPlugin;
import org.junit.Test;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
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
    public void testAddToParentModulesWithFilter() throws Exception {
    	 String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
         IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
         final IProject project = root.getProject(projectTechName);
         IFolder pomsFolder = project.getFolder(TalendJavaProjectConstants.DIR_POMS);
         IFile projectPom = pomsFolder.getFile("pom.xml");
         Model projectModel = MavenPlugin.getMavenModelManager().readMavenModel(projectPom);
         
         IFolder jobFolder = pomsFolder.getFolder("jobs").getFolder("process").getFolder("job1");
         if (!jobFolder.exists()) {
             jobFolder.create(true, true, null);
         }
         IFile jobPom = jobFolder.getFile("pom.xml");
         if(!jobPom.exists()) {
        	 Model jobModel = new Model();
        	 jobModel.setModelVersion("4.0.0"); 
        	 jobModel.setGroupId("org.example.aa"); 
        	 jobModel.setArtifactId(projectModel.getArtifactId());
        	 jobModel.setVersion(projectModel.getVersion());
        	 jobModel.setPackaging(TalendMavenConstants.PACKAGING_JAR);
             MavenPlugin.getMavenModelManager().createMavenModel(jobPom, jobModel);
         }
         Property property = createJobProperty("job1","0.1");
         
         setFilterContent("(label=other)",projectTechName);
         AggregatorPomsHelper.addToParentModules(jobPom,property);
         projectModel = MavenPlugin.getMavenModelManager().readMavenModel(projectPom);
         assertNotNull(projectModel.getModules());
         assertFalse(projectModel.getModules().contains("jobs/process/job1"));
         
         setFilterContent("(label=job1)",projectTechName);
         AggregatorPomsHelper.addToParentModules(jobPom,property);
         projectModel = MavenPlugin.getMavenModelManager().readMavenModel(projectPom);
         assertNotNull(projectModel.getModules());
         assertTrue(projectModel.getModules().contains("jobs/process/job1"));
         
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
    
    private Property createJobProperty(String joblabel, String version) throws Exception {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        String id = ProxyRepositoryFactory.getInstance().getNextId();
        property.setId(id);
        property.setLabel(joblabel);
        property.setVersion(version);
        item.setProperty(property);
        ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();
        ParametersType parameterType = TalendFileFactory.eINSTANCE.createParametersType();
        process.setParameters(parameterType);
        item.setProcess(process);
        return property;

    }
    
    private void setFilterContent(String filter,String projectTechName) {
    	Project project = ProjectManager.getInstance().getProjectFromProjectTechLabel(projectTechName);
        ProjectPreferenceManager preferenceManager = new ProjectPreferenceManager(project, DesignerMavenPlugin.PLUGIN_ID,false);
        IPreferenceStore preferenceStore = preferenceManager.getPreferenceStore();
        preferenceStore.setValue(MavenConstants.POM_FILTER, filter);
    }

}
