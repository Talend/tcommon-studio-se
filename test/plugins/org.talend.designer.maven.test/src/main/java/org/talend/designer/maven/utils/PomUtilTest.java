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
package org.talend.designer.maven.utils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;
import org.junit.Assert;
import org.junit.Test;
import org.ops4j.pax.url.mvn.MavenResolver;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.Property;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
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
    public void testGetTemplateFileNonExisted() throws Exception {
        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        Assert.assertNotNull(currentProject);
        IProject project = ResourceUtils.getProject(currentProject.getTechnicalLabel());
        IFolder tempFolder = project.getFolder("temp");
        if (!tempFolder.exists()) {
            tempFolder.create(true, true, null);
        }
        IFolder baseFolder = tempFolder.getFolder("TemplateBaseFolder");
        if (!baseFolder.exists()) {
            baseFolder.create(true, true, null);
        }
        File baseFile = baseFolder.getLocation().toFile();
        if (baseFile.exists()) { // clean folder
            FilesUtils.deleteFolder(baseFile, false);
        }
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

        foundFile = PomUtil.getTemplateFile(baseFolder, new Path("folder1/subfolder1"), "template3.txt");
        Assert.assertNull(foundFile); // base folder don't contain template3

        foundFile = PomUtil.getTemplateFile(tempFolder, new Path("folder1/subfolder1"), "template3.txt");
        Assert.assertNull(foundFile); // path not contained the template3

        foundFile = PomUtil.getTemplateFile(tempFolder, new Path("TemplateBaseFolder/folder1/subfolder1"), "template3.txt");
        Assert.assertNotNull(foundFile);
        Assert.assertEquals(template3File, foundFile);

        // clean test files
        template3File.delete();
        FilesUtils.deleteFolder(baseFile, true);

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

            MavenResolver mvnResolver = TalendLibsServerManager.getInstance().getMavenResolver();
            mvnResolver.upload("org.talend.libraries", "test1", null, "txt", "6.0.0", test1);
            mvnResolver.upload("org.talend.studio", "test2", null, "jar", "6.0.0", test1);

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
    public void testGeneratePom2() {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setArtifactId("testJar");
        artifact.setGroupId("org.talend.libraries");
        artifact.setType("jar");
        artifact.setVersion("6.3.1");
        String generatedPom = PomUtil.generatePom2(artifact);
        File pomFile = new File(generatedPom);
        Assert.assertTrue(pomFile.exists());
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
}
