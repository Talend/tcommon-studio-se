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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.repo.LocalRepositoryManager;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;

/**
 * created by ggu on 6 Feb 2015 Detailled comment
 *
 */
public class PomUtil {

    private static final MavenModelManager MODEL_MANAGER = MavenPlugin.getMavenModelManager();

    public static void savePom(IProgressMonitor monitor, Model model, IFile pomFile) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (pomFile == null) {
            throw new NullPointerException("the output file is null.");
        }

        /*
         * copied the codes from createMavenModel of MavenModelManager
         */
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        MavenPlugin.getMaven().writeModel(model, buf);

        ByteArrayInputStream source = new ByteArrayInputStream(buf.toByteArray());
        if (pomFile.exists()) {
            pomFile.setContents(source, true, false, monitor);
        } else {
            pomFile.create(source, true, monitor);
        }
    }

    /**
     * main for the codes pom without version.
     * 
     * get the pom name, if name is null, return default one "pom.xml", else will be "pom_<name>.xml"
     */
    public static String getPomFileName(String name) {
        return getPomFileName(name, null);
    }

    /**
     * 
     * get the pom name, if name is null, return default one "pom.xml", else will be "pom_<name>_<version>.xml"
     */
    public static String getPomFileName(String name, String version) {
        String pomFileName = TalendMavenConstants.POM_FILE_NAME;
        if (StringUtils.isNotBlank(name)) {
            pomFileName = TalendMavenConstants.POM_NAME + '_' + name.trim();
            if (StringUtils.isNotBlank(version)) {
                pomFileName += '_' + version.trim();
            }
            pomFileName += TalendMavenConstants.XML_EXT;
        }
        return pomFileName;
    }

    /**
     * get the assembly name, if name is null, return default one "assembly.xml", else will be
     * "assembly_<name>_<version>.xml"
     */
    public static String getAssemblyFileName(String name, String version) {
        String assemblyFileName = TalendMavenConstants.ASSEMBLY_FILE_NAME;
        if (StringUtils.isNotBlank(name)) {
            assemblyFileName = TalendMavenConstants.ASSEMBLY_NAME + '_' + name.trim();
            if (StringUtils.isNotBlank(version)) {
                assemblyFileName += '_' + version.trim();
            }
            assemblyFileName += TalendMavenConstants.XML_EXT;
        }
        return assemblyFileName;
    }

    /**
     * 
     * DOC ggu Comment method "getDefaultMavenVersion".
     * 
     * @return 6.0.0, without classifier.
     */
    public static String getDefaultMavenVersion() {
        String version = VersionUtils.getVersion();
        try {
            org.osgi.framework.Version v = new org.osgi.framework.Version(version);
            // only get major.minor.micro
            org.osgi.framework.Version simpleVersion = new org.osgi.framework.Version(v.getMajor(), v.getMinor(), v.getMicro());
            version = simpleVersion.toString();
        } catch (IllegalArgumentException e) {
            version = TalendMavenConstants.DEFAULT_VERSION;
        }
        return version;
    }

    /**
     * 
     * DOC ggu Comment method "checkParent". make sure the parent are unified.
     * 
     * @param curModel
     * @param curPomFile
     */
    public static void checkParent(Model curModel, IFile curPomFile) {
        Parent parent = curModel.getParent();
        if (parent == null) {
            parent = new Parent();
            curModel.setParent(parent);
        } else {
            // TODO, if existed, maybe just replace, not overwrite
        }
        // always depend on current poject
        Model codeProjectTemplateModel = MavenTemplateManager.getCodeProjectTemplateModel();
        parent.setGroupId(codeProjectTemplateModel.getGroupId());
        parent.setArtifactId(codeProjectTemplateModel.getArtifactId());
        parent.setVersion(codeProjectTemplateModel.getVersion());

        parent.setRelativePath("./" + TalendMavenConstants.POM_FILE_NAME);

    }

    /**
     * DOC ggu Comment method "createModuleSystemScopeDependency".
     * 
     * @return
     */
    public static Dependency createDependency(String groupId, String artifactId, String version, String type, String classifier) {
        if (artifactId == null) {
            return null;
        }
        Dependency dependency = new SortableDependency();
        dependency.setGroupId(groupId == null ? MavenConstants.DEFAULT_LIB_GROUP_ID : groupId);
        dependency.setArtifactId(artifactId);
        dependency.setVersion(version == null ? MavenConstants.DEFAULT_LIB_VERSION : version);
        dependency.setType(type == null ? TalendMavenConstants.PACKAGING_JAR : type);
        dependency.setClassifier(classifier);

        return dependency;
    }

    public static Dependency createDependency(String groupId, String artifactId, String version, String type) {
        return createDependency(groupId, artifactId, version, type, null);
    }

    public static Dependency createModuleDependency(String str) {
        if (str == null) {
            return null;
        }
        String mvnUrl = str;
        if (!MavenUrlHelper.isMvnUrl(str)) {
            mvnUrl = MavenUrlHelper.generateMvnUrlForJarName(str);
        }

        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
        if (artifact != null) {
            return createDependency(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), artifact.getType(),
                    artifact.getClassifier());
        }
        return null;
    }

    /**
     * 
     * According to the process, generate the groud id, like org.talend.process.di.demo.
     */
    public static String generateGroupId(final IProcessor jProcessor) {
        final Property property = jProcessor.getProperty();
        final IProcess process = jProcessor.getProcess();

        final String projectFolderName = JavaResourcesHelper.getProjectFolderName(property.getItem());
        return generateGroupId(projectFolderName, process.getComponentsType());
    }

    public static String generateGroupId(String projectFolderName, String type) {
        String groupId = JavaResourcesHelper.getGroupName(projectFolderName);

        if (type != null) {
            groupId += '.' + type.toLowerCase();
        }
        return groupId;
    }

    public static String generateGroupId(final JobInfo jobInfo) {
        ProcessItem processItem = jobInfo.getProcessItem();
        if (processItem != null) {
            String componentsType = null;
            IProcess process = jobInfo.getProcess();
            if (process != null) {
                componentsType = process.getComponentsType();
            }

            final String projectFolderName = JavaResourcesHelper.getProjectFolderName(processItem);
            return generateGroupId(projectFolderName, componentsType);
        } else { // return one default one.
            return generateGroupId(null, null);
        }

    }

    public static boolean isAvailable(Dependency dependency) {
        MavenArtifact artifact = convertToArtifact(dependency);
        return artifact != null && isAvailable(artifact);
    }

    public static boolean isAvailable(MavenArtifact artifact) {
        // just unify the API to check with "getAbsArtifactPath".
        String absArtifactPath = getAbsArtifactPath(artifact);
        if (absArtifactPath != null) {
            return true;
        }
        return false;
    }

    public static boolean isAvailable(String mvnUri) {
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUri);
        if (artifact != null) {
            return isAvailable(artifact);
        }
        return false;
    }

    /**
     * return the list of existed maven artifacts in local repository.
     */
    public static Set<String> availableArtifacts(IProgressMonitor monitor, String[] mvnUrls) throws Exception {
        Set<String> existedMvnUrls = new LinkedHashSet<String>();
        if (mvnUrls != null) {
            for (String mvnUrl : mvnUrls) {
                MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnUrl);
                if (isAvailable(artifact)) {
                    existedMvnUrls.add(mvnUrl);
                }
            }
        }
        return existedMvnUrls;
    }

    public static String generateMvnUrl(Dependency dependency) {
        if (dependency != null) {
            return MavenUrlHelper.generateMvnUrl(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion(),
                    dependency.getType(), dependency.getClassifier());
        }
        return null;
    }

    public static MavenArtifact convertToArtifact(Dependency dependency) {
        if (dependency != null) {
            MavenArtifact artifact = new MavenArtifact();

            artifact.setGroupId(dependency.getGroupId());
            artifact.setArtifactId(dependency.getArtifactId());
            artifact.setVersion(dependency.getVersion());
            artifact.setClassifier(dependency.getClassifier());
            artifact.setType(dependency.getType());

            return artifact;
        }
        return null;
    }

    public static void installJar(LocalRepositoryManager repoManager, File libFile, MavenArtifact artifact) throws Exception {
        // in lib/java, and not existed in m2/repo
        if (libFile.exists() && !PomUtil.isAvailable(artifact)) {
            repoManager.install(libFile, artifact);
        }
    }

    /**
     * 
     * Try to find the template files form the path which based on root container first. if not found, will try to find
     * in parent folder until root container.
     */
    public static File getTemplateFile(IContainer templateRootContainer, IPath templateRelativePath, String fileName) {
        if (templateRootContainer == null || !templateRootContainer.exists() || fileName == null || fileName.length() == 0) {
            return null;
        }
        IContainer baseContainer = templateRootContainer; // support found the file in current base container.
        boolean hasPath = templateRelativePath != null && !templateRelativePath.isEmpty();
        if (hasPath) {
            baseContainer = templateRootContainer.getFolder(templateRelativePath);
        }
        if (baseContainer.exists()) { // if the relative path is not existed, won't find again.
            IFile file = null;
            if (baseContainer instanceof IFolder) {
                file = ((IFolder) baseContainer).getFile(fileName);
            } else if (baseContainer instanceof IProject) {
                file = ((IProject) baseContainer).getFile(fileName);
            }
            if (file != null && file.exists()) {
                return file.getLocation().toFile();
            } else if (hasPath) {
                // find from parent folder
                return getTemplateFile(templateRootContainer, templateRelativePath.removeLastSegments(1), fileName);
            }
        }
        return null;
    }

    public static String getArtifactPath(MavenArtifact artifact) {
        IMaven maven = MavenPlugin.getMaven();
        String artifactPath = null;
        try {
            artifactPath = maven.getArtifactPath(maven.getLocalRepository(), artifact.getGroupId(), artifact.getArtifactId(),
                    artifact.getVersion(), artifact.getType(), artifact.getClassifier());
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        return artifactPath;
    }

    public static String getAbsArtifactPath(MavenArtifact artifact) {
        if (artifact == null) {
            return null;
        }
        IMaven maven = MavenPlugin.getMaven();
        String artifactPath = getArtifactPath(artifact);
        if (artifactPath == null) {
            return null;
        }
        String localRepositoryPath = maven.getLocalRepositoryPath();
        if (!localRepositoryPath.endsWith("/") && !localRepositoryPath.endsWith("\\")) {
            localRepositoryPath = localRepositoryPath + "/";
        }
        File file = new File(localRepositoryPath + artifactPath);
        if (file.exists()) {
            return file.getAbsolutePath();
        }

        return null;
    }

    public static String generatePom(MavenArtifact artifact) {
        try {
            Project project = ProjectManager.getInstance().getCurrentProject();
            IProject fsProject = ResourceUtils.getProject(project);
            IFolder tmpFolder = fsProject.getFolder("temp");
            if (!tmpFolder.exists()) {
                tmpFolder.create(true, true, null);
            }
            String tmpFolderName = File.createTempFile(TalendMavenConstants.PACKAGING_POM, "").getName();
            IFolder folder = tmpFolder.getFolder(tmpFolderName);
            folder.create(true, true, null);
            IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);

            Model pomModel = new Model();
            pomModel.setModelVersion(TalendMavenConstants.POM_VERSION);
            pomModel.setModelEncoding(TalendMavenConstants.DEFAULT_ENCODING);
            pomModel.setGroupId(artifact.getGroupId());
            pomModel.setArtifactId(artifact.getArtifactId());
            pomModel.setVersion(artifact.getVersion());
            String artifactType = artifact.getType();
            if (artifactType == null || "".equals(artifactType)) {
                artifactType = TalendMavenConstants.PACKAGING_JAR;
            }
            pomModel.setPackaging(artifactType);
            MODEL_MANAGER.createMavenModel(pomFile, pomModel);
            return pomFile.getLocation().toPortableString();
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        } catch (IOException e) {
            ExceptionHandler.process(e);
        } catch (CoreException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     * 
     * in order to make sure no compile error for editor, so add all needed dependencies always.
     */
    public static Collection<Dependency> getCodesDependencies(IFile projectPomFile) throws CoreException {
        Map<String, Dependency> codesDependencies = new LinkedHashMap<String, Dependency>();

        // routines
        addCodeDependencies(codesDependencies, projectPomFile, TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID,
                MavenTemplateManager.getRoutinesTempalteModel());

        // beans
        addCodeDependencies(codesDependencies, projectPomFile, TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID,
                MavenTemplateManager.getBeansTempalteModel());
        // pigudfs
        addCodeDependencies(codesDependencies, projectPomFile, TalendMavenConstants.DEFAULT_PIGUDFS_ARTIFACT_ID,
                MavenTemplateManager.getPigUDFsTempalteModel());

        return codesDependencies.values();
    }

    private static void addCodeDependencies(Map<String, Dependency> codesDependencies, IFile projectPomFile, String pomName,
            Model defaultModel) throws CoreException {
        IFile routinesPomFile = projectPomFile.getProject().getFile(PomUtil.getPomFileName(pomName));
        Model model = defaultModel;
        if (routinesPomFile.exists()) {
            model = MODEL_MANAGER.readMavenModel(routinesPomFile);
        }
        List<Dependency> dependencies = model.getDependencies();
        for (Dependency d : dependencies) {
            String mvnUrl = generateMvnUrl(d);
            if (!codesDependencies.containsKey(mvnUrl)) {
                codesDependencies.put(mvnUrl, d);
            }
        }
    }

    public static List<String> getMavenCodesModules(IProcess process) {
        List<String> codesModules = new ArrayList<String>();

        // add routines always.
        String routinesModule = PomUtil.getPomFileName(TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID);
        codesModules.add(routinesModule);

        // PigUDFs
        if (ProcessUtils.isRequiredPigUDFs(process)) {
            String pigudfsModule = PomUtil.getPomFileName(TalendMavenConstants.DEFAULT_PIGUDFS_ARTIFACT_ID);
            codesModules.add(pigudfsModule);
        }

        // Beans
        if (ProcessUtils.isRequiredBeans(process)) {
            String beansModule = PomUtil.getPomFileName(TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID);
            codesModules.add(beansModule);
        }

        return codesModules;
    }

    public static List<String> getCodesExportJars(IProcess process) {
        List<String> codesJars = new ArrayList<String>();
        // add routines always.
        codesJars.add(JavaUtils.ROUTINES_JAR);

        // PigUDFs
        if (ProcessUtils.isRequiredPigUDFs(process)) {
            codesJars.add(JavaUtils.PIGUDFS_JAR);
        }

        // Beans
        if (ProcessUtils.isRequiredBeans(process)) {
            codesJars.add(JavaUtils.BEANS_JAR);
        }
        return codesJars;
    }
}
