package org.talend.repository;

// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.impl.PropertiesFactoryImpl;

public class ReferenceProjectProviderTest {

    private String refProjectName1 = "PROJECT_R";

    private String refProjectBranch1 = "tags/tag1";

    private String refProjectName2 = "PROJECT_R1";

    private String refProjectBranch2 = "master";

    @Test
    public void testLoadProjectReferenceSetting() throws Exception {
        Project project = getTestProject();
        String branchName = "master";
        ReferenceProjectProvider provider = new TestBaseReferenceProjectProvider(project, branchName);
        provider.loadSettings();
        if (provider.getProjectReference().size() == 0) {
            testSaveProjectReferenceSetting();
        }
        assertNotNull(provider.getProjectReference());
    }

    @Test
    public void testSaveProjectReferenceSetting() throws Exception {
        Project project = getTestProject();
        String branchName = "master";
        ReferenceProjectProvider provider = new TestBaseReferenceProjectProvider(project, branchName);
        List<ProjectReference> projectReferenceList = getDefaultProjectReferenceList();
        provider.setProjectReference(projectReferenceList);
        provider.saveSettings();
    }

    @Test
    public void testGetProjectReference() throws Exception {
        Project project = getTestProject();
        String branchName = "master";
        ReferenceProjectProvider provider = new TestBaseReferenceProjectProvider(project, branchName);
        provider.loadSettings();
        if (provider.getProjectReference().size() == 0) {
            testSaveProjectReferenceSetting();
        }
        List<ProjectReference> list = provider.getProjectReference();
        ProjectReference pr1 = list.get(0);
        assertEquals(refProjectName1, pr1.getReferencedProject().getTechnicalLabel());
        assertEquals(refProjectBranch1, pr1.getReferencedBranch());
        ProjectReference pr2 = list.get(1);
        assertEquals(refProjectName2, pr2.getReferencedProject().getTechnicalLabel());
        assertEquals(refProjectBranch2, pr2.getReferencedBranch());
        assertNotNull(provider.getProjectReference());
    }

    @Test
    public void testSetProjectReference() throws Exception {
        Project project = getTestProject();
        String branchName = "master";
        ReferenceProjectProvider provider = new TestBaseReferenceProjectProvider(project, branchName);
        provider.loadSettings();
        List<ProjectReference> projectReferenceList = getDefaultProjectReferenceList();
        provider.setProjectReference(projectReferenceList);
        provider.saveSettings();
    }

    private Project getTestProject() {
        return ProjectManager.getInstance().getCurrentProject().getEmfProject();
    }

    private List<ProjectReference> getDefaultProjectReferenceList() {
        List<ProjectReference> list = new ArrayList<ProjectReference>();
        PropertiesFactory propertiesFactory = PropertiesFactoryImpl.init();
        Project project = getTestProject();
        ProjectReference pr = propertiesFactory.createProjectReference();
        pr.setProject(project);
        pr.setBranch("master");
        pr.setReferencedBranch(refProjectBranch1);
        Project rp = propertiesFactory.createProject();
        rp.setTechnicalLabel(refProjectName1);
        pr.setReferencedProject(rp);
        list.add(pr);

        ProjectReference pr1 = propertiesFactory.createProjectReference();
        pr1.setProject(project);
        pr1.setBranch("master");
        pr1.setReferencedBranch(refProjectBranch2);
        Project rp1 = propertiesFactory.createProject();
        rp1.setTechnicalLabel(refProjectName2);
        pr1.setReferencedProject(rp1);
        list.add(pr1);
        return list;
    }
}

class TestBaseReferenceProjectProvider extends ReferenceProjectProvider {

    public TestBaseReferenceProjectProvider(Project project, String branchName) {
        super(project);
    }

    protected File getConfigurationFile() throws PersistenceException {
        Bundle bundle = Platform.getBundle("org.talend.core.runtime.test"); //$NON-NLS-1$
        URL confEntry = bundle.getEntry("resources/reference_projects.settings"); //$NON-NLS-1$
        try {
            return new File(FileLocator.toFileURL(confEntry).getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
