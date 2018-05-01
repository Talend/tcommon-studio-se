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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.m2e.core.MavenPlugin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.core.model.utils.emf.talendfile.ParametersType;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFileFactory;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class AggregatorPomsHelperTest {

    private AggregatorPomsHelper helper;

    private IRunProcessService runProcessService;

    private List<Property> testJobs;

    private ProjectPreferenceManager projectPreferenceManager;

    private String projectTechName;

    private String defaultProjectGroupId;

    private String defaultProjectVersion;

    private boolean defaultUseSnapshot;

    private boolean needResetPom;

    @Before
    public void setUp() throws Exception {
        helper = new AggregatorPomsHelper();

        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        assertNotNull(runProcessService);

        projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();

        testJobs = new ArrayList<>();

        projectPreferenceManager = DesignerMavenPlugin.getPlugin().getProjectPreferenceManager();
        defaultProjectGroupId = PomIdsHelper.getProjectGroupId();
        defaultProjectVersion = PomIdsHelper.getProjectVersion();
        defaultUseSnapshot = false;
    }

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
        AggregatorPomsHelper.addToParentModules(jobPom, null);

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
        if (!jobPom.exists()) {
            Model jobModel = new Model();
            jobModel.setModelVersion("4.0.0");
            jobModel.setGroupId("org.example.aa");
            jobModel.setArtifactId(projectModel.getArtifactId());
            jobModel.setVersion(projectModel.getVersion());
            jobModel.setPackaging(TalendMavenConstants.PACKAGING_JAR);
            MavenPlugin.getMavenModelManager().createMavenModel(jobPom, jobModel);
        }
        Property property = createJobProperty("job1", "0.1", false);

        projectPreferenceManager.setValue(MavenConstants.POM_FILTER, "(label=other)");
        AggregatorPomsHelper.addToParentModules(jobPom, property);
        projectModel = MavenPlugin.getMavenModelManager().readMavenModel(projectPom);
        assertNotNull(projectModel.getModules());
        assertFalse(projectModel.getModules().contains("jobs/process/job1"));

        projectPreferenceManager.setValue(MavenConstants.POM_FILTER, "(label=job1)");
        AggregatorPomsHelper.addToParentModules(jobPom, property);
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

    @Test
    public void testGetCodeFolder() {
        IFolder routinesFolder = helper.getCodeFolder(ERepositoryObjectType.ROUTINES);
        assertEquals("/" + projectTechName + "/poms/code/routines", routinesFolder.getFullPath().toPortableString());
    }

    @Test
    public void testGetProcessFolder() {
        IFolder processFolder = helper.getProcessFolder(ERepositoryObjectType.PROCESS);
        assertEquals("/" + projectTechName + "/poms/jobs/process", processFolder.getFullPath().toPortableString());

        IFolder processMRFolder = helper.getProcessFolder(ERepositoryObjectType.PROCESS_MR);
        assertEquals("/" + projectTechName + "/poms/jobs/process_mr", processMRFolder.getFullPath().toPortableString());

        IFolder processStormFolder = helper.getProcessFolder(ERepositoryObjectType.PROCESS_STORM);
        assertEquals("/" + projectTechName + "/poms/jobs/process_storm", processStormFolder.getFullPath().toPortableString());

        IFolder jobletFolder = helper.getProcessFolder(ERepositoryObjectType.JOBLET);
        assertEquals("/" + projectTechName + "/poms/jobs/joblets", jobletFolder.getFullPath().toPortableString());

        IFolder sparkJobletFolder = helper.getProcessFolder(ERepositoryObjectType.SPARK_JOBLET);
        assertEquals("/" + projectTechName + "/poms/jobs/joblets_spark", sparkJobletFolder.getFullPath().toPortableString());

        IFolder sparkStrJobletFolder = helper.getProcessFolder(ERepositoryObjectType.SPARK_STREAMING_JOBLET);
        assertEquals("/" + projectTechName + "/poms/jobs/joblets_spark_streaming",
                sparkStrJobletFolder.getFullPath().toPortableString());

        IFolder routeFolder = helper.getProcessFolder(ERepositoryObjectType.PROCESS_ROUTE);
        assertEquals("/" + projectTechName + "/poms/jobs/routes", routeFolder.getFullPath().toPortableString());

        IFolder routeletFolder = helper.getProcessFolder(ERepositoryObjectType.PROCESS_ROUTELET);
        assertEquals("/" + projectTechName + "/poms/jobs/routelets", routeletFolder.getFullPath().toPortableString());

        IFolder serviceFolder = helper.getProcessFolder(ERepositoryObjectType.valueOf("SERVICES"));
        assertEquals("/" + projectTechName + "/poms/jobs/services", serviceFolder.getFullPath().toPortableString());

    }

    @Test
    public void testGetItemPomFolder() throws Exception {
        Property property = createJobProperty("testItemFolderJob", "0.1", false);
        IFolder folder = AggregatorPomsHelper.getItemPomFolder(property);
        assertEquals("/" + projectTechName + "/poms/jobs/process/testitemfolderjob_0.1", folder.getFullPath().toPortableString());
        folder = AggregatorPomsHelper.getItemPomFolder(property, "0.2");
        assertEquals("/" + projectTechName + "/poms/jobs/process/testitemfolderjob_0.2", folder.getFullPath().toPortableString());
    }

    @Test
    public void testGetCodeProjectId() {
        String routinesId = AggregatorPomsHelper.getCodeProjectId(ERepositoryObjectType.ROUTINES, projectTechName);
        assertEquals(projectTechName + "|ROUTINES", routinesId);
        String pidUdfsId = AggregatorPomsHelper.getCodeProjectId(ERepositoryObjectType.PIG_UDF, projectTechName);
        assertEquals(projectTechName + "|PIG_UDF", pidUdfsId);
        String beansId = AggregatorPomsHelper.getCodeProjectId(ERepositoryObjectType.valueOf("BEANS"), projectTechName);
        assertEquals(projectTechName + "|BEANS", beansId);
    }

    @Test
    public void testUpdateRefProjectModules() throws Exception {
        needResetPom = true;
        List<ProjectReference> references = new ArrayList<>();
        {
            ProjectReference reference = PropertiesFactory.eINSTANCE.createProjectReference();
            Project project = PropertiesFactory.eINSTANCE.createProject();
            project.setTechnicalLabel("TESTPROJECT1");
            reference.setReferencedProject(project);
            references.add(reference);
        }
        {
            ProjectReference reference = PropertiesFactory.eINSTANCE.createProjectReference();
            Project project = PropertiesFactory.eINSTANCE.createProject();
            project.setTechnicalLabel("TESTPROJECT2");
            reference.setReferencedProject(project);
            references.add(reference);
        }
        Model model = MavenPlugin.getMavenModelManager().readMavenModel(helper.getProjectRootPom());
        List<String> modules = model.getModules();
        modules.add("../../TESTPROJECT1/poms");
        modules.add("../../TESTPROJECT2/poms");
        AggregatorPomsHelper _helper = new AggregatorPomsHelper() {

            @Override
            public boolean needUpdateRefProjectModules() {
                return true;
            }

        };
        _helper.updateRefProjectModules(references);
        validatePomContent(helper.getProjectRootPom().getLocation().toFile(), defaultProjectGroupId, defaultProjectVersion,
                modules);
    }

    /**
     * test change of project groupId, project version, with-snapshot.
     */
    @Test
    public void testSyncAllPomsByProjectLevelChange() throws Exception {
        String projectGroupId = "org.example.test";
        String projectVersion = "8.7.0-SNAPSHOT";

        projectPreferenceManager.setValue(MavenConstants.PROJECT_GROUPID, "org.example.test");
        projectPreferenceManager.setValue(MavenConstants.PROJECT_VERSION, "8.7.0");
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, true);
        needResetPom = true;

        List<String> modules = new ArrayList<>();
        modules.add("code/routines");
        modules.add("jobs/process/testsyncalljob1_0.1");

        Property jobProperty = createJobProperty("testSyncAllJob1", "0.1", true);
        String jobGroupId = PomIdsHelper.getJobGroupId(jobProperty);
        String jobVersion = PomIdsHelper.getJobVersion(jobProperty);

        helper.syncAllPomsWithoutProgress(new NullProgressMonitor());

        // check project pom.
        IFile projectPomFile = new AggregatorPomsHelper().getProjectRootPom();
        validatePomContent(projectPomFile.getLocation().toFile(), projectGroupId, projectVersion, modules);
        // check project pom install result.
        File installedProjectPom = getInstalledFileFromLocalRepo(projectGroupId,
                TalendMavenConstants.DEFAULT_CODE_PROJECT_ARTIFACT_ID, projectVersion, MavenConstants.PACKAGING_POM);
        validatePomContent(installedProjectPom, projectGroupId, projectVersion, modules);

        // check routine pom.
        IFile routinePomFile = runProcessService.getTalendCodeJavaProject(ERepositoryObjectType.ROUTINES).getProjectPom();
        String routineGroupId = PomIdsHelper.getCodesGroupId("code");
        String routineVersion = PomIdsHelper.getCodesVersion();
        validatePomContent(routinePomFile.getLocation().toFile(), routineGroupId, routineVersion);

        // check routine install result.
        while (true) {
            Job[] jobs = Job.getJobManager().find(AggregatorPomsHelper.FAMILY_UPDATE_CODES);
            if (jobs.length == 0) {
                break;
            }
            Thread.sleep(100);
        }
        File installedRoutinePom = getInstalledFileFromLocalRepo(routineGroupId,
                TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID, routineVersion, MavenConstants.PACKAGING_POM);
        assertNotNull(installedRoutinePom);

        // check job pom.
        IFile jobPomFile = runProcessService.getTalendJobJavaProject(jobProperty).getProjectPom();
        validatePomContent(jobPomFile.getLocation().toFile(), jobGroupId, projectGroupId, jobVersion, projectVersion, null);
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void testSyncAllPomsByJobLevelChange() throws Exception {
        Property jobProperty = createJobProperty("testSyncAllJob2", "0.1", false);
        EMap additionalProperties = jobProperty.getAdditionalProperties();
        String customJobGroupId = "org.example.testJob";
        String customJobVersion = "8.8.0";
        additionalProperties.put(MavenConstants.NAME_GROUP_ID, customJobGroupId);
        additionalProperties.put(MavenConstants.NAME_USER_VERSION, customJobVersion);
        ProxyRepositoryFactory.getInstance().create(jobProperty.getItem(), new Path(""));

        helper.syncAllPomsWithoutProgress(new NullProgressMonitor());

        IFile jobPomFile = runProcessService.getTalendJobJavaProject(jobProperty).getProjectPom();
        validatePomContent(jobPomFile.getLocation().toFile(), customJobGroupId, defaultProjectGroupId, customJobVersion,
                defaultProjectVersion, null);
    }

    private void validatePomContent(File pomFile, String groupId, String version) throws CoreException {
        validatePomContent(pomFile, groupId, null, version, null, null);
    }

    private void validatePomContent(File pomFile, String groupId, String version, List<String> modules) throws CoreException {
        validatePomContent(pomFile, groupId, null, version, null, modules);
    }

    private void validatePomContent(File pomFile, String groupId, String parentGroupId, String version, String parentVersion,
            List<String> modules)
            throws CoreException {
        Model model = MavenPlugin.getMaven().readModel(pomFile);
        assertEquals(groupId, model.getGroupId());
        assertEquals(version, model.getVersion());
        if (parentGroupId != null) {
            assertEquals(parentGroupId, model.getParent().getGroupId());
        }
        if (parentVersion != null) {
            assertEquals(parentVersion, model.getParent().getVersion());
        }
        if (modules != null) {
            List<String> currentModules = model.getModules();
            assertNotNull(currentModules);
            for (String module : modules) {
                assertTrue(currentModules.contains(module));
            }
        }
    }

    private Property createJobProperty(String label, String version, boolean create) throws Exception {
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
            ProxyRepositoryFactory.getInstance().create(item, new Path(""));
            testJobs.add(property);
        }

        return property;
    }

    private File getInstalledFileFromLocalRepo(String groupId, String artifactId, String version, String packaging)
            throws IOException {
        String projectMvnUrl = MavenUrlHelper.generateMvnUrl(groupId, artifactId, version, packaging, null);
        String projectLocalMavenUri = projectMvnUrl.replace("mvn:", "mvn:" + MavenConstants.LOCAL_RESOLUTION_URL + "!");
        File installedFile = TalendMavenResolver.getMavenResolver().resolve(projectLocalMavenUri);
        return installedFile;
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
        // reset to default preferences.
        projectPreferenceManager.setValue(MavenConstants.PROJECT_GROUPID, defaultProjectGroupId);
        projectPreferenceManager.setValue(MavenConstants.PROJECT_VERSION, defaultProjectVersion);
        projectPreferenceManager.setValue(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT, defaultUseSnapshot);
        projectPreferenceManager.setValue(MavenConstants.POM_FILTER, "");
        // reset all poms.
        if (needResetPom) {
            helper.syncAllPomsWithoutProgress(new NullProgressMonitor());
            needResetPom = false;
        }
    }

}
