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
package org.talend.designer.maven.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.template.MavenTemplateManager;
import org.talend.designer.maven.tools.repo.LocalRepositoryManager;
import org.talend.designer.runprocess.IProcessor;
import org.talend.repository.ProjectManager;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

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
    public static void checkParent(Model curModel, IFile curPomFile, IProcessor processor, String specialVersion) {
        Parent parent = curModel.getParent();
        if (parent == null) {
            parent = new Parent();
            curModel.setParent(parent);
        } else {
            // TODO, if existed, maybe just replace, not overwrite
        }
        final Map<String, Object> templateParameters = PomUtil.getTemplateParameters(processor);
        Model codeProjectTemplateModel = MavenTemplateManager.getCodeProjectTemplateModel(templateParameters);

        if (specialVersion != null) {
            codeProjectTemplateModel.setVersion(specialVersion);
        }

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

    /**
     * 
     * Get artifact relative path
     * 
     * @param artifact
     * @return
     */
    public static String getArtifactPath(MavenArtifact artifact) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(artifact.getGroupId().replaceAll("\\.", "/"));

        buffer.append("/");
        buffer.append(artifact.getArtifactId());

        if (artifact.getVersion() != null) {
            buffer.append("/");
            buffer.append(artifact.getVersion());
        }

        buffer.append("/");
        buffer.append(artifact.getArtifactId());
        if (artifact.getVersion() != null) {
            buffer.append("-");
            buffer.append(artifact.getVersion());
        }
        if (artifact.getClassifier() != null) {
            buffer.append("-");
            buffer.append(artifact.getClassifier());
        }
        if (artifact.getType() != null) {
            buffer.append(".");
            buffer.append(artifact.getType());
        } else {
            // add default extension
            buffer.append(".jar");
        }
        return buffer.toString();
    }

    /**
     * Get absolute path for installed artifact
     * 
     * @param artifact
     * @return installed artifact absolute path , it will return null if artifact is not installed.
     */
    public static String getAbsArtifactPath(MavenArtifact artifact) {
        if (artifact == null) {
            return null;
        }
        String mvnUri = MavenUrlHelper.generateMvnUrl(artifact);
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            ILibraryManagerService librariesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            return librariesService.getJarPathFromMaven(mvnUri);
        } else {
            String localMavenUri = mvnUri.replace("mvn:", "mvn:" + MavenConstants.LOCAL_RESOLUTION_URL + "!"); //$NON-NLS-1$ //$NON-NLS-2$
            File resolve = null;
            try {
                resolve = TalendMavenResolver.getMavenResolver().resolve(localMavenUri);
            } catch (IOException | RuntimeException e) {
                resolve = null;
            }
            if (resolve != null) {
                return resolve.getAbsolutePath();
            }
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
            File createTempFile = File.createTempFile(TalendMavenConstants.PACKAGING_POM, "");
            createTempFile.delete();
            String tmpFolderName = createTempFile.getName();
            IFolder folder = tmpFolder.getFolder(tmpFolderName);
            folder.create(true, true, null);
            IFile pomFile = folder.getFile(TalendMavenConstants.POM_FILE_NAME);

            MODEL_MANAGER.createMavenModel(pomFile, createModel(artifact));
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

    private static Model createModel(MavenArtifact artifact) {
        Model pomModel = new Model();
        pomModel.setModelVersion(TalendMavenConstants.POM_VERSION);
        pomModel.setModelEncoding(TalendMavenConstants.DEFAULT_ENCODING);
        pomModel.setGroupId(artifact.getGroupId());
        pomModel.setArtifactId(artifact.getArtifactId());
        pomModel.setVersion(artifact.getVersion());
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            IBrandingService brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(
                    IBrandingService.class);
            pomModel.setDescription("Generated by " + brandingService.getCorporationName());//$NON-NLS-1$
        } else {
            pomModel.setDescription("It's generated pom"); //$NON-NLS-1$
        }
        String artifactType = artifact.getType();
        if (StringUtils.isEmpty(artifactType)) {
            artifactType = TalendMavenConstants.PACKAGING_JAR;
        }
        pomModel.setPackaging(artifactType);
        return pomModel;
    }

    public static String generatePomInFolder(File baseFolder, MavenArtifact artifact) throws Exception {
        if (baseFolder == null || artifact == null) {
            return null;
        }
        if (!baseFolder.exists()) {
            baseFolder.mkdirs();
        }
        File pomFile = new File(baseFolder, TalendMavenConstants.POM_FILE_NAME);

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        MavenPlugin.getMaven().writeModel(createModel(artifact), buf);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(false);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        TransformerFactory tfactory = TransformerFactory.newInstance();

        Document document = documentBuilder.parse(new ByteArrayInputStream(buf.toByteArray()));
        Element documentElement = document.getDocumentElement();

        NamedNodeMap attributes = documentElement.getAttributes();

        if (attributes == null || attributes.getNamedItem("xmlns") == null) { //$NON-NLS-1$
            Attr attr = document.createAttribute("xmlns"); //$NON-NLS-1$
            attr.setTextContent("http://maven.apache.org/POM/4.0.0"); //$NON-NLS-1$
            documentElement.setAttributeNode(attr);
        }

        if (attributes == null || attributes.getNamedItem("xmlns:xsi") == null) { //$NON-NLS-1$
            Attr attr = document.createAttribute("xmlns:xsi"); //$NON-NLS-1$
            attr.setTextContent("http://www.w3.org/2001/XMLSchema-instance"); //$NON-NLS-1$
            documentElement.setAttributeNode(attr);
        }

        if (attributes == null || attributes.getNamedItem("xsi:schemaLocation") == null) { //$NON-NLS-1$
            Attr attr = document.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation"); //$NON-NLS-1$ //$NON-NLS-2$
            attr.setTextContent("http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"); //$NON-NLS-1$
            documentElement.setAttributeNode(attr);
        }
        Transformer transformer = tfactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(pomFile);
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(source, result);

        return pomFile.getAbsolutePath();
    }

    /**
     * 
     * Create pom without refresh eclipse resources
     * 
     * @param artifact
     * @return
     */
    public static String generatePom2(MavenArtifact artifact) {
        try {
            Project project = ProjectManager.getInstance().getCurrentProject();
            IProject fsProject = ResourceUtils.getProject(project);
            SecureRandom random = new SecureRandom();
            IPath tempPath = fsProject.getLocation().append("temp").append("pom" + Math.abs(random.nextLong()));
            File tmpFolder = new File(tempPath.toPortableString());
            tmpFolder.mkdirs();
            return generatePomInFolder(tmpFolder, artifact);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    /**
     * 
     * in order to make sure no compile error for editor, so add all needed dependencies always.
     */
    public static Collection<Dependency> getCodesDependencies(IFile projectPomFile, String projectTechName) throws CoreException {
        Map<String, Dependency> codesDependencies = new LinkedHashMap<String, Dependency>();

        // routines
        addCodeDependencies(codesDependencies, projectPomFile, TalendMavenConstants.DEFAULT_ROUTINES_ARTIFACT_ID,
                MavenTemplateManager.getRoutinesTempalteModel(projectTechName));

        // beans
        addCodeDependencies(codesDependencies, projectPomFile, TalendMavenConstants.DEFAULT_BEANS_ARTIFACT_ID,
                MavenTemplateManager.getBeansTempalteModel(projectTechName));
        // pigudfs
        addCodeDependencies(codesDependencies, projectPomFile, TalendMavenConstants.DEFAULT_PIGUDFS_ARTIFACT_ID,
                MavenTemplateManager.getPigUDFsTempalteModel(projectTechName));

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

    public static Map<String, Object> getTemplateParameters(IProcessor processor) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (processor != null) {
            final Property property = processor.getProperty();
            return getTemplateParameters(property);
        }
        return parameters;
    }

    public static Map<String, Object> getTemplateParameters(Property property) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (property != null && property.eResource() != null) {
            final org.talend.core.model.properties.Project project = ProjectManager.getInstance().getProject(property);
            if (project != null // from reference projects
                    && !ProjectManager.getInstance().getCurrentProject().getTechnicalLabel().equals(project.getTechnicalLabel())) {
                parameters.put(MavenTemplateManager.KEY_PROJECT_NAME, project.getTechnicalLabel());
            }
        }
        return parameters;
    }

    public static String getProjectNameFromTemplateParameter(Map<String, Object> parameters) {
        // get the default based one project still
        String projectName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        if (parameters != null && !parameters.isEmpty()) {
            final Object pName = parameters.get(MavenTemplateManager.KEY_PROJECT_NAME);
            if (pName != null && !pName.toString().isEmpty()) {
                projectName = pName.toString();
            }
        }
        return projectName;
    }

    /**
     * If no deploy version or custom version, will use pom variable. Else, will use the setting version directly.
     */
    public static String getJobVersionForPomProperty(Map<String, Object> argumentsMap, Property curProperty) {
        return getJobVersionForPomProperty(argumentsMap, curProperty, curProperty);
    }

    public static String getJobVersionForPomProperty(Map<String, Object> argumentsMap, Property jobProperty, Property curProperty) {
        String deployVersion = null;
        if (argumentsMap != null) {
            final Object value = argumentsMap.get(TalendProcessArgumentConstant.ARG_DEPLOY_VERSION);
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                deployVersion = value.toString();
            }
        }
        String customVersion = null;
        if (jobProperty != null && jobProperty.getAdditionalProperties() != null) {
            final Object value = jobProperty.getAdditionalProperties().get(MavenConstants.NAME_USER_VERSION);
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                customVersion = value.toString();
            }
        }

        String jobVersion;
        if (curProperty != null && (deployVersion != null || customVersion != null)) {// have set special version
            // if set special version, set the literal value of job.
            jobVersion = curProperty.getVersion();
        } else {
            // if no special version, will reuse the project version of pom for variable.
            jobVersion = "${project.version}";
        }
        return jobVersion;
    }

    public static Document loadAssemblyFile(IProgressMonitor monitor, IFile assemblyFile) throws ParserConfigurationException,
            SAXException, IOException {
        final File file = assemblyFile.getLocation().toFile();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(file);
        return document;
    }

    public static void saveAssemblyFile(IProgressMonitor monitor, IFile assemblyFile, Document document)
            throws TransformerException, IOException {
        final File file = assemblyFile.getLocation().toFile();
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer transFormer = transFactory.newTransformer();
        transFormer.setOutputProperty(OutputKeys.INDENT, "yes");
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
            transFormer.transform(new DOMSource(document), new StreamResult(output));
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }
}
