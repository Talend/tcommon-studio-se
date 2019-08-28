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

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.designer.maven.model.TalendJavaProjectConstants;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PomUtilTest {

    @Test
    public void testGetPomFileName() {
        String pomFileName = PomUtil.getPomFileName(null);
        Assert.assertEquals("pom.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName("");
        Assert.assertEquals("pom.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName("ABC");
        Assert.assertEquals("pom_ABC.xml", pomFileName);
    }

    @Test
    public void testGetPomFileNameVersion() {
        String pomFileName = PomUtil.getPomFileName(null, null);
        Assert.assertEquals("pom.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName("", null);
        Assert.assertEquals("pom.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName(" ", "  ");
        Assert.assertEquals("pom.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName("ABC", null);
        Assert.assertEquals("pom_ABC.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName("ABC", " ");
        Assert.assertEquals("pom_ABC.xml", pomFileName);

        pomFileName = PomUtil.getPomFileName("ABC", "1.0");
        Assert.assertEquals("pom_ABC_1.0.xml", pomFileName);
    }

    @Test
    public void testGetAssemblyFileName() {
        String assemblyFileName = PomUtil.getAssemblyFileName(null, null);
        Assert.assertEquals("assembly.xml", assemblyFileName);

        assemblyFileName = PomUtil.getAssemblyFileName("", null);
        Assert.assertEquals("assembly.xml", assemblyFileName);

        assemblyFileName = PomUtil.getAssemblyFileName(" ", null);
        Assert.assertEquals("assembly.xml", assemblyFileName);

        assemblyFileName = PomUtil.getAssemblyFileName("ABC", null);
        Assert.assertEquals("assembly_ABC.xml", assemblyFileName);

        assemblyFileName = PomUtil.getAssemblyFileName("ABC", "    ");
        Assert.assertEquals("assembly_ABC.xml", assemblyFileName);

        assemblyFileName = PomUtil.getAssemblyFileName("ABC", "1.0");
        Assert.assertEquals("assembly_ABC_1.0.xml", assemblyFileName);
    }

    @Test
    public void testCreateModuleDependency() {
        Dependency moduleDependency = PomUtil.createDependency(null, null, null, null);
        Assert.assertNull(moduleDependency);

        moduleDependency = PomUtil.createDependency(null, "test", null, null);
        Assert.assertNotNull(moduleDependency);
        Assert.assertEquals("org.talend.libraries", moduleDependency.getGroupId());
        Assert.assertEquals("test", moduleDependency.getArtifactId());
        Assert.assertEquals(MavenConstants.DEFAULT_LIB_VERSION, moduleDependency.getVersion());
        Assert.assertEquals("jar", moduleDependency.getType());

        moduleDependency = PomUtil.createDependency("org.talend.job", "test", "0.1", "zip");
        Assert.assertNotNull(moduleDependency);
        Assert.assertEquals("org.talend.job", moduleDependency.getGroupId());
        Assert.assertEquals("test", moduleDependency.getArtifactId());
        Assert.assertEquals("0.1", moduleDependency.getVersion());
        Assert.assertEquals("zip", moduleDependency.getType());
    }

    @Test
    public void testCreateModuleDependencyWithClassifier() {
        Dependency moduleDependency = PomUtil.createDependency("org.talend.job", "test", "0.1", "zip");
        Assert.assertNotNull(moduleDependency);
        Assert.assertEquals("org.talend.job", moduleDependency.getGroupId());
        Assert.assertEquals("test", moduleDependency.getArtifactId());
        Assert.assertEquals("0.1", moduleDependency.getVersion());
        Assert.assertEquals("zip", moduleDependency.getType());
        Assert.assertNull(moduleDependency.getClassifier());

        moduleDependency = PomUtil.createDependency("org.talend.job", "test", "0.1", "zip", "src");
        Assert.assertNotNull(moduleDependency);
        Assert.assertEquals("org.talend.job", moduleDependency.getGroupId());
        Assert.assertEquals("test", moduleDependency.getArtifactId());
        Assert.assertEquals("0.1", moduleDependency.getVersion());
        Assert.assertEquals("zip", moduleDependency.getType());
        Assert.assertEquals("src", moduleDependency.getClassifier());
    }

    @Test
    public void testGetTemplateFileNull() throws Exception {
        File templateFile = PomUtil.getTemplateFile(null, null, null);
        Assert.assertNull(templateFile);

        templateFile = PomUtil.getTemplateFile(null, new Path(""), null);
        Assert.assertNull(templateFile);

        templateFile = PomUtil.getTemplateFile(null, new Path(""), "");
        Assert.assertNull(templateFile);

    }

    @Test
    public void test_getTemplateFile_NonExisted() throws Exception {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        Assert.assertNotNull(currentProject);
        IProject project = ResourceUtils.getProject(currentProject.getTechnicalLabel());
        IFolder tempFolder = project.getFolder("temp");
        final File tempFile = tempFolder.getLocation().toFile();
        if (tempFolder.exists()) {
            FilesUtils.deleteFolder(tempFile, false);
        } else {
            tempFolder.create(true, true, null);
        }
        tempFolder.refreshLocal(IResource.DEPTH_INFINITE, null);

        IFolder baseFolder = tempFolder.getFolder("TemplateBaseFolder");
        if (!baseFolder.exists()) {
            baseFolder.create(true, true, null);
        }

        File baseFile = baseFolder.getLocation().toFile();
        File template1File = new File(baseFile, "template1.txt");
        File folder1 = new File(baseFile, "folder1");
        File subfolder1 = new File(baseFile, "folder1/subfolder1");
        File template2File = new File(folder1, "template2.txt");
        File subTemplate2File = new File(subfolder1, "template2.txt");

        File foundFile = PomUtil.getTemplateFile(baseFolder, null, "template1.txt"); // current
        Assert.assertNull(foundFile); // not existed the template1

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template1.txt");
        Assert.assertNull(foundFile); // not existed the template1

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1"), "template2.txt");
        Assert.assertNull(foundFile); // not existed the template2

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template2.txt");
        Assert.assertNull(foundFile); // not existed the template2

        subfolder1.mkdirs();
        template1File.createNewFile();
        template2File.createNewFile();
        subTemplate2File.createNewFile();

        Assert.assertTrue(template1File.exists());
        Assert.assertTrue(template2File.exists());
        Assert.assertTrue(subfolder1.exists());

        tempFolder.refreshLocal(IResource.DEPTH_INFINITE, null);

        foundFile = PomUtil.getTemplateFile(baseFolder, null, "template1.txt"); // from current base folder directly
        Assert.assertNotNull(foundFile);
        Assert.assertEquals(template1File, foundFile);

        foundFile = PomUtil.getTemplateFile(baseFolder, null, "template2.txt"); // from current base folder directly
        Assert.assertNull(foundFile);

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1"), "template2.txt");
        Assert.assertNotNull(foundFile); // first template2
        Assert.assertEquals(template2File, foundFile);

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template2.txt");
        Assert.assertNotNull(foundFile); // sub template2
        Assert.assertEquals(subTemplate2File, foundFile);

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template1.txt");
        Assert.assertNotNull(foundFile); // find from parent foler until base folder
        Assert.assertEquals(template1File, foundFile);

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template3.txt");
        Assert.assertNull(foundFile); // base folder don't contain template3

        File template3File = new File(baseFile.getParentFile(), "template3.txt");
        template3File.createNewFile();
        Assert.assertTrue(template3File.exists());
        tempFolder.refreshLocal(IResource.DEPTH_INFINITE, null);

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template3.txt");
        Assert.assertNull(foundFile); // base folder don't contain template3

        foundFile = PomUtil.getTemplateFile(tempFolder, new Path("folder1/subfolder1"), "template3.txt");
        Assert.assertNull(foundFile); // path not contained the template3

        foundFile = PomUtil.getTemplateFile(tempFolder, new Path("TemplateBaseFolder/folder1/subfolder1"), "template3.txt");
        Assert.assertNotNull(foundFile);
        Assert.assertEquals(template3File, foundFile);

        // clean test files
        FilesUtils.deleteFolder(tempFile, false);
        tempFolder.refreshLocal(IResource.DEPTH_INFINITE, null);

    }

    @Test
    public void testIsAvailableString() throws Exception {
        File baseFile = null;
        try {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            Assert.assertNotNull(currentProject);
            IProject project = ResourceUtils.getProject(currentProject.getTechnicalLabel());
            IFolder tempFolder = project.getFolder("temp");
            if (!tempFolder.exists()) {
                tempFolder.create(true, true, null);
            }
            IFolder baseFolder = tempFolder.getFolder("testIsAvailableString");
            if (!baseFolder.exists()) {
                baseFolder.create(true, true, null);
            }
            baseFile = baseFolder.getLocation().toFile();
            File test1 = new File(baseFile, "test1.txt");
            test1.createNewFile();

            File test2 = new File(baseFile, "test2.jar");
            test2.createNewFile();

            TalendMavenResolver.upload("org.talend.libraries", "test1", null, "txt", "6.0.0", test1);
            TalendMavenResolver.upload("org.talend.studio", "test2", null, "jar", "6.0.0", test1);

            Assert.assertTrue(PomUtil.isAvailable("mvn:org.talend.libraries/test1/6.0.0/txt"));
            Assert.assertTrue(PomUtil.isAvailable("mvn:org.talend.studio/test2/6.0.0/jar"));
        } finally {
            if (baseFile != null) {
                FilesUtils.deleteFolder(baseFile, true);
            }
            MavenArtifact artifact = new MavenArtifact();
            artifact.setGroupId("org.talend.libraries");
            artifact.setArtifactId("test1");
            artifact.setType("txt");
            artifact.setVersion("6.0.0");
            String absArtifactPath = PomUtil.getAbsArtifactPath(artifact);
            File file = new File(absArtifactPath);
            FilesUtils.deleteFolder(file.getParentFile().getParentFile(), true);
            file.getParentFile().getParentFile().delete();
            artifact.setGroupId("org.talend.studio");
            artifact.setArtifactId("test2");
            artifact.setType("jar");
            artifact.setVersion("6.0.0");
            absArtifactPath = PomUtil.getAbsArtifactPath(artifact);
            file = new File(absArtifactPath);
            FilesUtils.deleteFolder(file.getParentFile().getParentFile(), true);

        }
    }

    @Test
    public void test_generatePom_nullType() throws CoreException {
        doTestGeneratePom(null);
    }

    @Test
    public void test_generatePom_emptyType() throws CoreException {
        doTestGeneratePom("");
    }

    @Test
    public void test_generatePom_jarType() throws CoreException {
        doTestGeneratePom("jar");
    }

    @Test
    public void test_generatePom_zipType() throws CoreException {
        doTestGeneratePom("zip");
    }

    private void doTestGeneratePom(String type) throws CoreException {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setArtifactId("testJar");
        artifact.setGroupId("org.talend.libraries");
        artifact.setType(type);
        artifact.setVersion("6.4.1");
        String generatedPom = PomUtil.generatePom(artifact);
        File pomFile = new File(generatedPom);

        Assert.assertTrue(pomFile.exists());

        Model model = MavenPlugin.getMaven().readModel(pomFile);

        Assert.assertEquals("org.talend.libraries", model.getGroupId());
        Assert.assertEquals("testJar", model.getArtifactId());
        Assert.assertEquals("6.4.1", model.getVersion());
        Assert.assertTrue("Should be generated pom", model.getDescription().startsWith("Generated by"));
        if (StringUtils.isEmpty(type)) { // default jar
            Assert.assertEquals(TalendMavenConstants.PACKAGING_JAR, model.getPackaging());
        } else {
            Assert.assertEquals(type, model.getPackaging());
        }

        Assert.assertEquals("4.0.0", model.getModelVersion());

        Assert.assertNull(model.getModelEncoding());

        FilesUtils.deleteFolder(pomFile.getParentFile(), true);
    }

    @Test
    public void test_generatePom2_nullType() throws CoreException {
        doTestGeneratePom2(null);
    }

    @Test
    public void test_generatePom2_emptyType() throws CoreException {
        doTestGeneratePom2("");
    }

    @Test
    public void test_generatePom2_jarType() throws CoreException {
        doTestGeneratePom2("jar");
    }

    @Test
    public void test_generatePom2_zipType() throws CoreException {
        doTestGeneratePom2("zip");
    }

    private void doTestGeneratePom2(String type) throws CoreException {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setArtifactId("testJar");
        artifact.setGroupId("org.talend.libraries");
        artifact.setType(type);
        artifact.setVersion("6.4.1");
        String generatedPom = PomUtil.generatePom2(artifact);
        File pomFile = new File(generatedPom);

        Assert.assertTrue(pomFile.exists());

        Model model = MavenPlugin.getMaven().readModel(pomFile);

        Assert.assertEquals("org.talend.libraries", model.getGroupId());
        Assert.assertEquals("testJar", model.getArtifactId());
        Assert.assertEquals("6.4.1", model.getVersion());
        Assert.assertTrue("Should be generated pom", model.getDescription().startsWith("Generated by"));
        if (StringUtils.isEmpty(type)) { // default jar
            Assert.assertEquals(TalendMavenConstants.PACKAGING_JAR, model.getPackaging());
        } else {
            Assert.assertEquals(type, model.getPackaging());
        }

        Assert.assertEquals("4.0.0", model.getModelVersion());

        Assert.assertNull(model.getModelEncoding());

        FilesUtils.deleteFolder(pomFile.getParentFile(), true);
    }

    @Test
    public void testGetTemplateParameters_IProcessor_null() {
        Map<String, Object> templateParameters = PomUtil.getTemplateParameters((IProcessor) null);
        Assert.assertNotNull(templateParameters);
        Assert.assertEquals(0, templateParameters.size());

    }

    @Test
    public void testGetTemplateParameters_Property_null() {
        Map<String, Object> templateParameters = PomUtil.getTemplateParameters((Property) null);
        Assert.assertNotNull(templateParameters);
        Assert.assertEquals(0, templateParameters.size());
    }

    @Test
    public void testGetTemplateParameters_Property_noeResourse() {
        Property p = PropertiesFactory.eINSTANCE.createProperty();
        Map<String, Object> templateParameters = PomUtil.getTemplateParameters(p);
        Assert.assertNotNull(templateParameters);
        Assert.assertEquals(0, templateParameters.size());
    }

    // @Test
    public void testGetTemplateParameters_Property_reference() {
        // can't test, except use mock way, because need mock one reference project.
    }

    @Test
    public void testGetProjectNameFromTemplateParameter_empty() {
        final String currentProjectName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();

        String projectName = PomUtil.getProjectNameFromTemplateParameter(null);
        Assert.assertNotNull(projectName);
        Assert.assertEquals(currentProjectName, projectName);

        projectName = PomUtil.getProjectNameFromTemplateParameter(Collections.emptyMap());
        Assert.assertNotNull(projectName);
        Assert.assertEquals(currentProjectName, projectName);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(MavenTemplateManager.KEY_PROJECT_NAME, "");
        projectName = PomUtil.getProjectNameFromTemplateParameter(parameters);
        Assert.assertNotNull(projectName);
        Assert.assertEquals(currentProjectName, projectName);
    }

    @Test
    public void testGetProjectNameFromTemplateParameter_diff() {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(MavenTemplateManager.KEY_PROJECT_NAME, "ABC");
        String projectName = PomUtil.getProjectNameFromTemplateParameter(parameters);
        Assert.assertNotNull(projectName);
        Assert.assertEquals("ABC", projectName);
    }

    @Test
    public void testGetPomRelativePath() throws Exception {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IFolder pomsFolder = root.getFolder(new Path(projectTechName + "/" + TalendJavaProjectConstants.DIR_POMS));
        IFile routinePom = pomsFolder.getFolder("code").getFolder("routines").getFile("pom.xml");
        String relativePath = PomUtil.getPomRelativePath(routinePom.getLocation().toFile(), "poms");
        assertEquals("../../", relativePath);

        IFolder jobFolder = pomsFolder.getFolder("jobs").getFolder("process").getFolder("relativeTest");
        if (!jobFolder.exists()) {
            jobFolder.create(true, true, null);
        }
        IFile jobPom = jobFolder.getFile("pom.xml");
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setGroupId("org.talend.test");
        model.setArtifactId("testRelative");
        model.setVersion("1.0.0");
        PomUtil.savePom(null, model, jobPom);

        assertTrue(jobPom.exists());

        relativePath = PomUtil.getPomRelativePath(jobPom.getLocation().toFile(), "poms");
        assertEquals("../../../", relativePath);
    }

    @Test
    public void testGetArtifactPath() {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId("org.talend.libraries");
        artifact.setArtifactId("test");
        artifact.setVersion("1.0.0");
        String artifactPath = PomUtil.getArtifactPath(artifact);
        String expectedPath = "org/talend/libraries/test/1.0.0/test-1.0.0.jar";
        Assert.assertEquals(artifactPath, expectedPath);

        artifact.setGroupId("org.talend.libraries");
        artifact.setArtifactId("test");
        artifact.setVersion("1.0.0");
        artifact.setType("exe");
        artifactPath = PomUtil.getArtifactPath(artifact);
        expectedPath = "org/talend/libraries/test/1.0.0/test-1.0.0.exe";
        Assert.assertEquals(artifactPath, expectedPath);

        artifact.setGroupId("org.talend.libraries");
        artifact.setArtifactId("test");
        artifact.setVersion("1.0.0");
        artifact.setClassifier("jdk");
        artifact.setType("exe");
        artifactPath = PomUtil.getArtifactPath(artifact);
        expectedPath = "org/talend/libraries/test/1.0.0/test-1.0.0-jdk.exe";
        Assert.assertEquals(artifactPath, expectedPath);

    }

    @Test
    public void getAbsArtifactPath() {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId("org.talend.libraries");
        artifact.setArtifactId("getAbsArtifactPath");
        artifact.setVersion("1.0.0");
        String absArtifactPath = PomUtil.getAbsArtifactPath(artifact);
        Assert.assertNull(absArtifactPath);

        artifact.setGroupId("org.apache.commons");
        artifact.setArtifactId("commons-lang3");
        artifact.setVersion("3.4");
        artifact.setType("jar");
        absArtifactPath = PomUtil.getAbsArtifactPath(artifact);
        Assert.assertNotNull(absArtifactPath);
        Assert.assertTrue(new File(absArtifactPath).exists());
    }

    @Test
    public void testUpdateMainJobDependencies() throws Exception {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IFolder pomsFolder = root.getFolder(new Path(projectTechName + "/" + TalendJavaProjectConstants.DIR_POMS));
        IFolder testFolder = pomsFolder.getFolder("jobs").getFolder("process").getFolder("UpdateMainJobDependencies");
        if (testFolder.exists()) {
            testFolder.delete(true, null);
        }
        testFolder.create(true, true, null);
        // create test depdenency: main_job==>subJob1==>subJob2==>main_job
        IFolder mainFolder = testFolder.getFolder("main_job");
        mainFolder.create(true, true, null);
        IFile mainPom = mainFolder.getFile("pom.xml");
        Model modelMain = creatModel("main_job");
        String mainDepStr = "mvn:org.talend.libraries/mainDep/6.0.0/jar";
        Dependency mainDep = PomUtil.createModuleDependency(mainDepStr);
        modelMain.getDependencies().add(mainDep);
        Dependency mainDep_subjob1 = PomUtil.createModuleDependency("mvn:org.talend.test/subJob1/1.0.0/jar");
        modelMain.getDependencies().add(mainDep_subjob1);
        PomUtil.savePom(null, modelMain, mainPom);

        IFolder subjob1Folder = testFolder.getFolder("subjob1");
        subjob1Folder.create(true, true, null);
        IFile subjob1Pom = subjob1Folder.getFile("pom.xml");
        Model modelSubjob1 = creatModel("subjob1");
        String subjob1DepStr = "mvn:org.talend.libraries/subjob1Dep/6.0.0/jar";
        Dependency subjob1Dep = PomUtil.createModuleDependency(subjob1DepStr);
        modelSubjob1.getDependencies().add(subjob1Dep);
        Dependency subjob1Dep_subjob2 = PomUtil.createModuleDependency("mvn:org.talend.test/subJob2/1.0.0/jar");
        modelSubjob1.getDependencies().add(subjob1Dep_subjob2);
        PomUtil.savePom(null, modelSubjob1, subjob1Pom);

        IFolder subjob2Folder = testFolder.getFolder("subjob2");
        subjob2Folder.create(true, true, null);
        IFile subjob2Pom = subjob2Folder.getFile("pom.xml");
        Model modelSubjob2 = creatModel("subjob2");
        String subjob2DepStr = "mvn:org.talend.libraries/subjob2Dep/6.0.0/jar";
        Dependency subjob2Dep = PomUtil.createModuleDependency(subjob2DepStr);
        modelSubjob2.getDependencies().add(subjob2Dep);
        Dependency subjob2Dep_mainjob = PomUtil.createModuleDependency("mvn:org.talend.test/main_job/1.0.0/jar");
        modelSubjob2.getDependencies().add(subjob2Dep_mainjob);
        PomUtil.savePom(null, modelSubjob2, subjob2Pom);

        List<IFile> subjobPoms = new ArrayList<IFile>();
        subjobPoms.add(subjob1Pom);
        subjobPoms.add(subjob2Pom);
        Set<String> subjobURLs = new HashSet<String>();
        subjobURLs.add("mvn:org.talend.test/subJob1/1.0.0/jar");
        subjobURLs.add("mvn:org.talend.test/subJob2/1.0.0/jar");
        subjobURLs.add("mvn:org.talend.test/main_job/1.0.0/jar");
        PomUtil.updateMainJobDependencies(mainPom, subjobPoms, subjobURLs, null);

        modelMain = MavenPlugin.getMavenModelManager().readMavenModel(mainPom);
        Assert.assertEquals(modelMain.getDependencies().size(), 3);
        Assert.assertEquals(PomUtil.generateMvnUrl(modelMain.getDependencies().get(0)), mainDepStr);
        Assert.assertEquals(PomUtil.generateMvnUrl(modelMain.getDependencies().get(1)), subjob1DepStr);
        Assert.assertEquals(PomUtil.generateMvnUrl(modelMain.getDependencies().get(2)), subjob2DepStr);

        testFolder.delete(true, null);
    }

    private Model creatModel(String artifactId) {
        Model model = new Model();
        model.setModelVersion("4.0.0");
        model.setGroupId("org.talend.test");
        model.setArtifactId(artifactId);
        model.setVersion("1.0.0");
        return model;
    }
}
