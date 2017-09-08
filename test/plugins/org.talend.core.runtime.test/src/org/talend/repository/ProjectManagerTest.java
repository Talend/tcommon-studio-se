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
package org.talend.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;

/**
 * 
 * @author Kevin Wang
 *
 */
public class ProjectManagerTest {

    @Before
    public void setup() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String mainBranch = "branches/a2";
        List<ProjectReference> referenceList = new ArrayList<ProjectReference>();

        ProjectReference refProject1 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject1.setBranch("projectBranch");
        refProject1.setReferencedBranch("refBranch");
        Project rProject1 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject1.setTechnicalLabel("refProject1");
        refProject1.setProject(project.getEmfProject());
        refProject1.setReferencedProject(rProject1.getEmfProject());

        ProjectReference refProject2 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject2.setBranch("projectBranch");
        refProject2.setReferencedBranch("refBranch");
        Project rProject2 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject2.setTechnicalLabel("refProject2");
        refProject2.setProject(project.getEmfProject());
        refProject2.setReferencedProject(rProject2.getEmfProject());

        referenceList.add(refProject1);
        referenceList.add(refProject2);

        ProjectManager.getInstance().setLocalRefBranch(project.getEmfProject(), mainBranch, referenceList);
    }

    @Test
    public void testSetAndGetLocalRefBranch() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String mainBranch = "branches/a2";

        List<ProjectReference> referenceList = new ArrayList<ProjectReference>();
        ProjectReference refProject1 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject1.setBranch("projectBranch");
        refProject1.setReferencedBranch("refBranch");
        Project rProject1 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject1.setTechnicalLabel("refProject1");
        refProject1.setProject(project.getEmfProject());
        refProject1.setReferencedProject(rProject1.getEmfProject());

        ProjectReference refProject2 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject2.setBranch("projectBranch");
        refProject2.setReferencedBranch("refBranch");
        Project rProject2 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject2.setTechnicalLabel("refProject2");
        refProject2.setProject(project.getEmfProject());
        refProject2.setReferencedProject(rProject2.getEmfProject());

        referenceList.add(refProject1);
        referenceList.add(refProject2);

        ProjectManager.getInstance().setLocalRefBranch(project.getEmfProject(), mainBranch, referenceList);

        Assert.assertEquals("projectBranch",
                ProjectManager.getInstance().getLocalProjectReferenceBranch(project.getEmfProject(), mainBranch, refProject1));
        Assert.assertEquals("refBranch", ProjectManager.getInstance()
                .getLocalProjectReferenceReferenceBranch(project.getEmfProject(), mainBranch, refProject1));

        ProjectManager.getInstance().deleteLocalRefBranchSetting(project.getEmfProject(), mainBranch);
    }

    @Test
    public void testDeleteLocalRefBranchSetting() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String mainBranch = "branches/a2";
        List<ProjectReference> referenceList = new ArrayList<ProjectReference>();

        ProjectReference refProject1 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject1.setBranch("projectBranch");
        refProject1.setReferencedBranch("refBranch");
        Project rProject1 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject1.setTechnicalLabel("refProject1");
        refProject1.setProject(project.getEmfProject());
        refProject1.setReferencedProject(rProject1.getEmfProject());

        ProjectReference refProject2 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject2.setBranch("projectBranch");
        refProject2.setReferencedBranch("refBranch");
        Project rProject2 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject2.setTechnicalLabel("refProject2");
        refProject2.setProject(project.getEmfProject());
        refProject2.setReferencedProject(rProject2.getEmfProject());

        referenceList.add(refProject1);
        referenceList.add(refProject2);

        ProjectManager.getInstance().setLocalRefBranch(project.getEmfProject(), mainBranch, referenceList);
        ProjectManager.getInstance().deleteLocalRefBranchSetting(project.getEmfProject(), mainBranch);

        Assert.assertNull(
                ProjectManager.getInstance().getLocalProjectReferenceBranch(project.getEmfProject(), "refBranch", refProject1));
        Assert.assertNull(
                ProjectManager.getInstance().getLocalProjectReferenceBranch(project.getEmfProject(), "refBranch", refProject2));
    }

    @Test
    public void testValidReferenceProject() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        String mainBranch = "branches/a2";
        ProjectManager.getInstance().setMainProjectBranch(project, mainBranch);

        List<ProjectReference> referenceList = new ArrayList<ProjectReference>();
        ProjectReference refProject1 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject1.setBranch("projectBranch");
        refProject1.setReferencedBranch("refBranch");
        Project rProject1 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject1.setTechnicalLabel("refProject1");
        refProject1.setProject(project.getEmfProject());
        refProject1.setReferencedProject(rProject1.getEmfProject());

        ProjectReference refProject2 = PropertiesFactoryImpl.eINSTANCE.createProjectReference();
        refProject2.setBranch("projectBranch");
        refProject2.setReferencedBranch("refBranch");
        Project rProject2 = new Project(PropertiesFactoryImpl.eINSTANCE.createProject());
        rProject2.setTechnicalLabel("refProject2");
        refProject2.setProject(project.getEmfProject());
        refProject2.setReferencedProject(rProject2.getEmfProject());

        referenceList.add(refProject1);
        referenceList.add(refProject2);

        ProjectManager.getInstance().setLocalRefBranch(project.getEmfProject(), mainBranch, referenceList);

        Assert.assertTrue(ProjectManager.validReferenceProject(project.getEmfProject(), refProject1));
        Assert.assertTrue(ProjectManager.validReferenceProject(project.getEmfProject(), refProject2));

        ProjectManager.getInstance().deleteLocalRefBranchSetting(project.getEmfProject(), mainBranch);

    }
    
    public void testgetCleanBranchName() {
        String branch = "branches/a2";
        Assert.assertEquals("a2", ProjectManager.getCleanBranchName(branch));
        
        String branch1 = "origin/a2";
        Assert.assertEquals("a2", ProjectManager.getCleanBranchName(branch1));
        
        String branch2 = "origin/master";
        Assert.assertEquals("master", ProjectManager.getCleanBranchName(branch2));
        
        String branchMaster = "master";
        Assert.assertEquals("master", ProjectManager.getCleanBranchName(branchMaster));
    }
}
