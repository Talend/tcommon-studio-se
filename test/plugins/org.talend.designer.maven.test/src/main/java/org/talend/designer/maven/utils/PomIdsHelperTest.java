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

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PomIdsHelperTest {


    @Test
    public void test_getProjectGroupId_empty() {
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
    public void test_getCodesGroupId_default() {
        final String technicalLabel = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();

        String codesGroupId = PomIdsHelper.getCodesGroupId("abc");
        Assert.assertNotNull(codesGroupId);
        Assert.assertEquals("org.example." + technicalLabel.toLowerCase() + ".abc", codesGroupId);
    }

    @Test
    @Ignore
    public void test_getCodesGroupId() {
        String codesGroupId = PomIdsHelper.getCodesGroupId("pro", "abc");
        Assert.assertNotNull(codesGroupId);
        Assert.assertEquals("org.example.auto_login_project.abc", codesGroupId);

        codesGroupId = PomIdsHelper.getCodesGroupId("PRO", "abc");
        Assert.assertNotNull(codesGroupId);
        Assert.assertEquals("org.talend.abc.pro", codesGroupId);
    }

    @Test
    public void test_getJobGroupId_empty() {
        String jobGroupId = PomIdsHelper.getJobGroupId((String) null);
        Assert.assertNotNull(jobGroupId);
        Assert.assertEquals("org.talend.job", jobGroupId);

        jobGroupId = PomIdsHelper.getJobGroupId("");
        Assert.assertNotNull(jobGroupId);
        Assert.assertEquals("org.talend.job", jobGroupId);

        jobGroupId = PomIdsHelper.getJobGroupId("    ");
        Assert.assertNotNull(jobGroupId);
        Assert.assertEquals("org.talend.job", jobGroupId);
    }

    @Test
    public void test_getJobGroupId() {
        String jobGroupId = PomIdsHelper.getJobGroupId("abc");
        Assert.assertNotNull(jobGroupId);
        Assert.assertEquals("org.talend.job.abc", jobGroupId);

        jobGroupId = PomIdsHelper.getJobGroupId(" abc ");
        Assert.assertNotNull(jobGroupId);
        Assert.assertEquals("org.talend.job.abc", jobGroupId);

        jobGroupId = PomIdsHelper.getJobGroupId(" AbC   ");
        Assert.assertNotNull(jobGroupId);
        Assert.assertEquals("org.talend.job.abc", jobGroupId);
    }

    @Test
    public void test_getTestGroupId_empty() {
        String testGroupId = PomIdsHelper.getTestGroupId((String) null);
        Assert.assertNotNull(testGroupId);
        Assert.assertEquals("org.talend.test", testGroupId);

        testGroupId = PomIdsHelper.getTestGroupId("");
        Assert.assertNotNull(testGroupId);
        Assert.assertEquals("org.talend.test", testGroupId);

        testGroupId = PomIdsHelper.getTestGroupId("    ");
        Assert.assertNotNull(testGroupId);
        Assert.assertEquals("org.talend.test", testGroupId);
    }

    @Test
    public void test_getTestGroupId() {
        String testGroupId = PomIdsHelper.getTestGroupId("abc");
        Assert.assertNotNull(testGroupId);
        Assert.assertEquals("org.talend.test.abc", testGroupId);

        testGroupId = PomIdsHelper.getTestGroupId(" abc ");
        Assert.assertNotNull(testGroupId);
        Assert.assertEquals("org.talend.test.abc", testGroupId);

        testGroupId = PomIdsHelper.getTestGroupId(" AbC   ");
        Assert.assertNotNull(testGroupId);
        Assert.assertEquals("org.talend.test.abc", testGroupId);
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
    public void testGetProjectVersion() {
        assertEquals(PomUtil.getDefaultMavenVersion(), PomIdsHelper.getProjectVersion());
        ProjectPreferenceManager projectPreferenceManager = DesignerMavenPlugin.getPlugin().getProjectPreferenceManager();
        projectPreferenceManager.setValue(MavenConstants.PROJECT_VERSION, "1.1.0");
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, true);
        assertEquals("1.1.0-SNAPSHOT", PomIdsHelper.getProjectVersion());
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, false);
        assertEquals("1.1.0", PomIdsHelper.getProjectVersion());
    }
    
    @Test
    public void testGetJobVersion() {
        Property property = PropertiesFactory.eINSTANCE.createProperty();
        // test default
        property.setVersion("2.1");
        assertEquals(VersionUtils.getPublishVersion("2.1"), PomIdsHelper.getJobVersion(property));
        // test custom version
        property.getAdditionalProperties().put(MavenConstants.NAME_USER_VERSION, "1.1.0");
        assertEquals("1.1.0", PomIdsHelper.getJobVersion(property));
        // test custom version with snapshot
        property.getAdditionalProperties().put(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, "true");
        assertEquals("1.1.0-SNAPSHOT", PomIdsHelper.getJobVersion(property));
    }
}
