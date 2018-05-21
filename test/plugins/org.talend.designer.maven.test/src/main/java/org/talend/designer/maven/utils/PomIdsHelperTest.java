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
package org.talend.designer.maven.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.joblet.model.JobletFactory;
import org.talend.designer.joblet.model.JobletProcess;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PomIdsHelperTest {

    private ProjectPreferenceManager projectPreferenceManager;

    private String defaultProjectGroupId;

    private String defaultProjectVersion;

    private boolean defaultWithSnapshot;

    private boolean defaultAppendFolder;

    private boolean defaultSkipBaseGroupId;

    private String defaultPomFilter;

    private List<Property> testJobs;

    private String projectName;

    @Before
    public void setUp() {
        testJobs = new ArrayList<>();
        projectPreferenceManager = DesignerMavenPlugin.getPlugin().getProjectPreferenceManager();

        defaultProjectGroupId = projectPreferenceManager.getValue(MavenConstants.PROJECT_GROUPID);
        defaultProjectVersion = projectPreferenceManager.getValue(MavenConstants.PROJECT_VERSION);
        defaultWithSnapshot = projectPreferenceManager.getBoolean(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT);
        defaultAppendFolder = projectPreferenceManager.getBoolean(MavenConstants.APPEND_FOLDER_TO_GROUPID);
        defaultSkipBaseGroupId = projectPreferenceManager.getBoolean(MavenConstants.SKIP_BASE_GROUPID);
        defaultPomFilter = projectPreferenceManager.getValue(MavenConstants.POM_FILTER);

        projectName = ProjectManager.getInstance().getCurrentProject().getLabel().toLowerCase();
    }

    @Test
    public void test_getProjectGroupIdDefault() {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        String expectValue = "org.example";
        if (currentProject != null) {
            expectValue = expectValue + '.' + currentProject.getTechnicalLabel().toLowerCase();
        }
        String projectGroupId = PomIdsHelper.getProjectGroupId();
        Assert.assertNotNull(projectGroupId);
        Assert.assertEquals(expectValue, projectGroupId);
    }

    @Test
    public void testGetProjectGroupIdCustom() {
        String expectValue = "org.example.test";
        projectPreferenceManager.setValue(MavenConstants.PROJECT_GROUPID, expectValue);
        String projectGroupId = PomIdsHelper.getProjectGroupId();
        Assert.assertNotNull(projectGroupId);
        Assert.assertEquals(expectValue, projectGroupId);
    }

    @Test
    public void testGetProjectVersion() {
        assertEquals(PomUtil.getDefaultMavenVersion(), PomIdsHelper.getProjectVersion());

        projectPreferenceManager.setValue(MavenConstants.PROJECT_VERSION, "1.1.0");
        assertEquals("1.1.0", PomIdsHelper.getProjectVersion());
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, true);
        assertEquals("1.1.0-SNAPSHOT", PomIdsHelper.getProjectVersion());
    }

    @Test
    public void test_getCodesGroupId() {
        assertEquals("org.example." + projectName + ".abc", PomIdsHelper.getCodesGroupId("abc"));

        projectPreferenceManager.setValue(MavenConstants.PROJECT_GROUPID, "org.example.test");
        assertEquals("org.example.test.abc", PomIdsHelper.getCodesGroupId("abc"));
        projectPreferenceManager.setValue(MavenConstants.APPEND_FOLDER_TO_GROUPID, true);
        assertEquals("org.example.test.abc", PomIdsHelper.getCodesGroupId("abc"));
        projectPreferenceManager.setValue(MavenConstants.SKIP_BASE_GROUPID, true);
        assertEquals("org.example.test.abc", PomIdsHelper.getCodesGroupId("abc"));
    }

    @Test
    public void test_getCodesVersionDefault() {
        String expectValue = PomUtil.getDefaultMavenVersion();
        String actualValue = PomIdsHelper.getCodesVersion();
        assertEquals(expectValue, actualValue);
    }

    @Test
    public void test_getCodesVersionCustom() {
        projectPreferenceManager.setValue(MavenConstants.PROJECT_VERSION, "1.1.0");
        assertEquals("1.1.0", PomIdsHelper.getCodesVersion());
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, true);
        assertEquals("1.1.0-SNAPSHOT", PomIdsHelper.getCodesVersion());
    }

    @Test
    public void testGetJobGroupIdDefault() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel("testGroupIdChangeDefaultJob");
        property.setVersion("0.1");
        assertEquals("org.example." + projectName + ".job", PomIdsHelper.getJobGroupId(property));
    }

    @Test
    public void testGetJobGroupIdCustom() throws Exception {
        Property property1 = PropertiesFactory.eINSTANCE.createProperty();
        property1.setLabel("testGroupIdChangeCustomJob1");
        property1.setVersion("0.1");
        property1.getAdditionalProperties().put(MavenConstants.NAME_GROUP_ID, "org.example.test");
        assertEquals("org.example.test", PomIdsHelper.getJobGroupId(property1));

        projectPreferenceManager.setValue(MavenConstants.APPEND_FOLDER_TO_GROUPID, true);
        Property property2 = createJobProperty("testGroupIdChangeCustomJob2", "0.1", "", true);
        assertEquals("org.example." + projectName + "", PomIdsHelper.getJobGroupId(property2));
        Property property3 = createJobProperty("testGroupIdChangeCustomJob3", "0.1", "f1/f2", true);
        assertEquals("org.example." + projectName + ".f1.f2", PomIdsHelper.getJobGroupId(property3));
        projectPreferenceManager.setValue(MavenConstants.SKIP_BASE_GROUPID, true);
        assertEquals("f1.f2", PomIdsHelper.getJobGroupId(property3));
    }

    @Test
    public void test_getJobArtifactId_Property_null() {
        String jobArtifactId = PomIdsHelper.getJobArtifactId((Property) null);
        Assert.assertNull(jobArtifactId);
    }

    @Test
    public void test_getJobArtifactId_Property() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();

        property.setLabel("Hello");
        String jobArtifactId = PomIdsHelper.getJobArtifactId(property);
        Assert.assertNotNull(jobArtifactId);
        Assert.assertEquals("Hello", jobArtifactId);

        property.setLabel("Hello ");
        jobArtifactId = PomIdsHelper.getJobArtifactId(property);
        Assert.assertNotNull(jobArtifactId);
        Assert.assertEquals("Hello_", jobArtifactId);

        property.setLabel(" H ello ");
        jobArtifactId = PomIdsHelper.getJobArtifactId(property);
        Assert.assertNotNull(jobArtifactId);
        Assert.assertEquals("_H_ello_", jobArtifactId);
    }

    @Test
    public void test_getJobArtifactId_JobInfo_null() {
        String jobArtifactId = PomIdsHelper.getJobArtifactId((JobInfo) null);
        Assert.assertNull(jobArtifactId);
    }

    @Test
    public void test_getJobArtifactId_JobInfo() {
        class JobInfoTestClss extends JobInfo {

            String jobName;

            public JobInfoTestClss(String jobName) {
                super("123", "Default", "1.1");
                this.jobName = jobName;
            }

            @Override
            public String getJobName() {
                return jobName;
            }
        }

        JobInfoTestClss jobInfo = new JobInfoTestClss("World");
        String jobArtifactId = PomIdsHelper.getJobArtifactId(jobInfo);
        Assert.assertNotNull(jobArtifactId);
        Assert.assertEquals("World", jobArtifactId);

        jobInfo = new JobInfoTestClss("World ");
        jobArtifactId = PomIdsHelper.getJobArtifactId(jobInfo);
        Assert.assertNotNull(jobArtifactId);
        Assert.assertEquals("World_", jobArtifactId);

        jobInfo = new JobInfoTestClss("Wo rld ");
        jobArtifactId = PomIdsHelper.getJobArtifactId(jobInfo);
        Assert.assertNotNull(jobArtifactId);
        Assert.assertEquals("Wo_rld_", jobArtifactId);
    }

    @Test
    public void testGetJobVersionDefault() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        // test default
        property.setVersion("2.1");
        assertEquals(VersionUtils.getPublishVersion("2.1"), PomIdsHelper.getJobVersion(property));
    }

    @Test
    public void testGetJobVersionCustom() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        // test custom version
        property.getAdditionalProperties().put(MavenConstants.NAME_USER_VERSION, "1.1.0");
        assertEquals("1.1.0", PomIdsHelper.getJobVersion(property));
        // test custom version with snapshot
        property.getAdditionalProperties().put(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, "true");
        assertEquals("1.1.0-SNAPSHOT", PomIdsHelper.getJobVersion(property));
    }

    @Test
    public void testGetJobletGroupId() throws Exception {
        Property property1 = createJobletProperty("testGroupIdChangeJoblet1", "0.1", "", false);
        assertEquals("org.example." + projectName + ".joblet", PomIdsHelper.getJobletGroupId(property1));

        projectPreferenceManager.setValue(MavenConstants.PROJECT_GROUPID, "org.example.test");
        assertEquals("org.example.test.joblet", PomIdsHelper.getJobletGroupId(property1));

        Property property2 = createJobletProperty("testGroupIdChangeJoblet2", "0.1", "", true);
        projectPreferenceManager.setValue(MavenConstants.APPEND_FOLDER_TO_GROUPID, true);
        assertEquals("org.example.test", PomIdsHelper.getJobletGroupId(property2));

        Property property3 = createJobletProperty("testGroupIdChangeJoblet3", "0.1", "f1/f2", true);
        projectPreferenceManager.setValue(MavenConstants.APPEND_FOLDER_TO_GROUPID, true);
        assertEquals("org.example.test.f1.f2", PomIdsHelper.getJobletGroupId(property3));

        projectPreferenceManager.setValue(MavenConstants.SKIP_BASE_GROUPID, true);
        assertEquals("f1.f2", PomIdsHelper.getJobletGroupId(property3));
    }

    @Test
    public void testGetJobletVersion() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel("jobletTest1");
        property.setVersion("0.1");
        assertEquals("0.1.0", PomIdsHelper.getJobletVersion(property));
        property.setVersion("2.0");
        assertEquals("2.0.0", PomIdsHelper.getJobletVersion(property));
    }

    @Test
    public void testGetJobletArtifactId() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        property.setLabel("jobletTest1");
        assertEquals("jobletTest1", PomIdsHelper.getJobletArtifactId(property));
    }

    @Test
    public void testGetDefaultProjetGroupId() {
        assertEquals("org.example.test1", PomIdsHelper.getDefaultProjetGroupId("test1"));
        assertEquals("org.example.test1", PomIdsHelper.getDefaultProjetGroupId("TEST1"));
    }

    @Test
    public void testIsValidGroupId() {
        assertTrue(PomIdsHelper.isValidGroupId("org.example.test"));
        assertTrue(PomIdsHelper.isValidGroupId("org.example"));
        assertTrue(PomIdsHelper.isValidGroupId("org"));
        assertFalse(PomIdsHelper.isValidGroupId("@rg"));
        assertFalse(PomIdsHelper.isValidGroupId("org "));
        assertFalse(PomIdsHelper.isValidGroupId("or g"));
        assertFalse(PomIdsHelper.isValidGroupId(" org.example. test"));
        assertFalse(PomIdsHelper.isValidGroupId("org. example.test"));
        assertFalse(PomIdsHelper.isValidGroupId("org.example.test "));
    }

    @Test
    public void testGetPomFilter() {
        projectPreferenceManager.setValue(MavenConstants.POM_FILTER, "");
        assertEquals("", PomIdsHelper.getPomFilter());
        projectPreferenceManager.setValue(MavenConstants.POM_FILTER, "label=job1");
        assertEquals("label=job1", PomIdsHelper.getPomFilter());
    }

    private Property createJobProperty(String label, String version, String path, boolean create)
            throws Exception {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        String id = ProxyRepositoryFactory.getInstance().getNextId();
        property.setId(id);
        property.setLabel(label);
        property.setVersion(version);

        ProcessItem item = PropertiesFactory.eINSTANCE.createProcessItem();
        item.setProperty(property);

        ProcessType process = TalendFileFactory.eINSTANCE.createProcessType();
        ParametersType parameterType = TalendFileFactory.eINSTANCE.createParametersType();
        process.setParameters(parameterType);
        item.setProcess(process);

        if (create) {
            if (path == null) {
                path = "";
            }
            ProxyRepositoryFactory.getInstance().create(item, new Path(path));
            testJobs.add(property);
        }

        return property;
    }

    private Property createJobletProperty(String label, String version, String path, boolean create) throws Exception {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        String id = ProxyRepositoryFactory.getInstance().getNextId();
        property.setId(id);
        property.setLabel(label);
        property.setVersion(version);

        JobletProcessItem item = PropertiesFactory.eINSTANCE.createJobletProcessItem();
        item.setProperty(property);

        JobletProcess jobletProcess = JobletFactory.eINSTANCE.createJobletProcess();
        ParametersType parameterType = TalendFileFactory.eINSTANCE.createParametersType();
        jobletProcess.setParameters(parameterType);
        item.setJobletProcess(jobletProcess);

        if (create) {
            if (path == null) {
                path = "";
            }
            ProxyRepositoryFactory.getInstance().create(item, new Path(path));
            testJobs.add(property);
        }

        return property;
    }

    @After
    public void tearDown() throws Exception {
        // clear all test jobs.
        if (!testJobs.isEmpty()) {
            for (Property property : testJobs) {
                IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
                IRepositoryViewObject repObj = factory.getLastVersion(property.getId());
                if (repObj != null) {
                    factory.deleteObjectPhysical(repObj);
                }
            }
            testJobs.clear();
        }
        projectPreferenceManager.setValue(MavenConstants.PROJECT_GROUPID, defaultProjectGroupId);
        projectPreferenceManager.setValue(MavenConstants.PROJECT_VERSION, defaultProjectVersion);
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, defaultWithSnapshot);
        projectPreferenceManager.setValue(MavenConstants.APPEND_FOLDER_TO_GROUPID, defaultAppendFolder);
        projectPreferenceManager.setValue(MavenConstants.SKIP_BASE_GROUPID, defaultSkipBaseGroupId);
        projectPreferenceManager.setValue(MavenConstants.POM_FILTER, defaultPomFilter);
    }

}
