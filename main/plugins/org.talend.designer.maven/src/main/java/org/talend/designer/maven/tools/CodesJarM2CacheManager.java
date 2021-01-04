// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.cwm.helper.ResourceHelper;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.maven.launch.MavenPomCommandLauncher;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.tools.creator.CreateMavenBeansJarPom;
import org.talend.designer.maven.tools.creator.CreateMavenRoutinesJarPom;
import org.talend.designer.maven.utils.PomIdsHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;

public class CodesJarM2CacheManager {

    private static final String KEY_MODIFIED_DATE = "MODIFIED_DATE"; //$NON-NLS-1$

    private static final String KEY_DEPENDENCY_LIST = "DEPENDENCY_LIST"; //$NON-NLS-1$

    private static final String KEY_INNERCODE_PREFIX = "INNERCODE"; //$NON-NLS-1$

    private static final String KEY_SEPERATOR = "|"; //$NON-NLS-1$

    private static final String DEP_SEPERATOR = ","; //$NON-NLS-1$

    private static final String EMPTY_DATE;

    public final static String BUILD_AGGREGATOR_POM_NAME = "build-codesjar-aggregator.pom"; //$NON-NLS-1$


    private static File cacheFolder;

    static {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        EMPTY_DATE = ResourceHelper.dateFormat().format(c.getTime());
        cacheFolder = new File(MavenPlugin.getMaven().getLocalRepositoryPath()).toPath().resolve(".codecache").resolve("codesjar")
                .toFile();
        if (!cacheFolder.exists()) {
            cacheFolder.mkdirs();
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean needUpdateCodesJarProject(Property property) {
        try {
            ERepositoryObjectType codeType = ERepositoryObjectType.getItemType(property.getItem());
            String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
            Project project = ProjectManager.getInstance().getProjectFromProjectTechLabel(projectTechName);
            File cacheFile = getCacheFile(projectTechName, property);
            if (!cacheFile.exists()) {
                return true;
            }
            DateFormat format = ResourceHelper.dateFormat();
            Properties cache = new Properties();
            cache.load(new FileInputStream(cacheFile));
            String currentTime = getModifiedDate(property);
            String cachedTime = cache.getProperty(KEY_MODIFIED_DATE);
            // check codesjar modified date
            if (cachedTime == null) {
                return true;
            }
            if (format.parse(currentTime).compareTo(format.parse(cachedTime)) != 0) {
                return true;
            }

            // check dependency list
            String dependencies = cache.getProperty(KEY_DEPENDENCY_LIST);
            List<String> cachedDepList;
            if (dependencies == null) {
                cachedDepList = Collections.emptyList();
            } else {
                cachedDepList = Arrays.asList(dependencies.split(DEP_SEPERATOR));
            }
            EList<IMPORTType> imports = ((RoutinesJarItem) property.getItem()).getRoutinesJarType().getImports();
            List<String> currentDepList = imports.stream().map(IMPORTType::getMVN).collect(Collectors.toList());
            if (cachedDepList.size() != currentDepList.size()) {
                return true;
            }
            if (!cachedDepList.isEmpty() && !cachedDepList.stream().allMatch(s -> currentDepList.contains(s))) {
                return true;
            }

            // check inner codes
            List<IRepositoryViewObject> currentInnerCodes = ProxyRepositoryFactory.getInstance().getAllInnerCodes(project,
                    codeType, property);
            Map<Object, Object> cachedInnerCodes = cache.entrySet().stream()
                    .filter(e -> e.getKey().toString().startsWith(KEY_INNERCODE_PREFIX))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            // check A/D
            if (currentInnerCodes.size() != cachedInnerCodes.size()) {
                return true;
            }
            // check M
            for (IRepositoryViewObject codeItem : currentInnerCodes) {
                Property innerCodeProperty = codeItem.getProperty();
                String key = getInnerCodeKey(projectTechName, innerCodeProperty);
                String cacheValue = (String) cachedInnerCodes.get(key);
                if (cacheValue != null) {
                    Date currentDate = ResourceHelper.dateFormat().parse(getModifiedDate(innerCodeProperty));
                    Date cachedDate = ResourceHelper.dateFormat().parse(cacheValue);
                    if (currentDate.compareTo(cachedDate) != 0) {
                        return true;
                    }
                }
            }
        } catch (PersistenceException | IOException | ParseException e) {
            ExceptionHandler.process(e);
            // if any exception, still update in case breaking build job
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void updateCodesJarProjectCache(Property property) {
        ERepositoryObjectType codeType = ERepositoryObjectType.getItemType(property.getItem());
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        Project project = ProjectManager.getInstance().getProjectFromProjectTechLabel(projectTechName);
        Properties cache = new Properties();
        File cacheFile = getCacheFile(projectTechName, property);
        // update codesjar modified date
        cache.setProperty(KEY_MODIFIED_DATE, getModifiedDate(property));
        // update dependencies
        EList<IMPORTType> imports = ((RoutinesJarItem) property.getItem()).getRoutinesJarType().getImports();
        StringBuilder builder = new StringBuilder();
        if (!imports.isEmpty()) {
            imports.forEach(i -> builder.append(i.getMVN()).append(DEP_SEPERATOR));
            cache.setProperty(KEY_DEPENDENCY_LIST, StringUtils.stripEnd(builder.toString(), DEP_SEPERATOR));
        }
        // update inner codes
        try (OutputStream out = new FileOutputStream(cacheFile)) {
            List<IRepositoryViewObject> allInnerCodes = ProxyRepositoryFactory.getInstance().getAllInnerCodes(project, codeType,
                    property);
            for (IRepositoryViewObject codeItem : allInnerCodes) {
                Property innerCodeProperty = codeItem.getProperty();
                String key = getInnerCodeKey(projectTechName, innerCodeProperty);
                String value = getModifiedDate(innerCodeProperty);
                cache.put(key, value);
            }
            cache.store(out, StringUtils.EMPTY);
        } catch (PersistenceException | IOException e) {
            ExceptionHandler.process(e);
        }
    }

    // TODO check callers of updateCodeProjectPom()
    public static void updateCodesJarProjectPom(IProgressMonitor monitor, Property property, IFile pomFile) throws Exception {
        ERepositoryObjectType type = ERepositoryObjectType.getItemType(property.getItem());
        if (type != null) {
            if (ERepositoryObjectType.ROUTINESJAR == type) {
                createRoutinesJarPom(property, pomFile, monitor);
            } else if (ERepositoryObjectType.BEANSJAR != null && ERepositoryObjectType.BEANSJAR == type) {
                createBeansJarPom(property, pomFile, monitor);
            }
        }
    }

    private static void createRoutinesJarPom(Property property, IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenRoutinesJarPom createTemplatePom = new CreateMavenRoutinesJarPom(property, pomFile);
        createTemplatePom.setProjectName(ProjectManager.getInstance().getProject(property).getTechnicalLabel());
        createTemplatePom.create(monitor);
    }

    private static void createBeansJarPom(Property property, IFile pomFile, IProgressMonitor monitor) throws Exception {
        CreateMavenBeansJarPom createTemplatePom = new CreateMavenBeansJarPom(property, pomFile);
        createTemplatePom.setProjectName(ProjectManager.getInstance().getProject(property).getTechnicalLabel());
        createTemplatePom.create(monitor);
    }

    /**
     * for logon project
     */
    public static void updateCodesJarProject(IProgressMonitor monitor) {
        updateCodesJarProject(monitor, true, true, false);
    }

    public static void updateCodesJarProject(IProgressMonitor monitor, boolean regeneratePom) {
        updateCodesJarProject(monitor, regeneratePom, false, false);
    }

    public static void updateCodesJarProject(IProgressMonitor monitor, boolean regeneratePom, boolean buildInMain,
            boolean forceBuild) {
        RepositoryWorkUnit workUnit = new RepositoryWorkUnit<Object>("update codesjar project") { //$NON-NLS-1$

            @Override
            protected void run() {
                Set<Property> toUpdate = new HashSet<>();
                CodesJarResourceCache.getAllCodesJars().forEach(p -> {
                    ITalendProcessJavaProject codesJarProject = getRunProcessService().getTalendCodesJarJavaProject(p);
                    if (forceBuild || needUpdateCodesJarProject(p)) {
                        if (regeneratePom) {
                            try {
                                updateCodesJarProjectPom(monitor, p, codesJarProject.getProjectPom());
                            } catch (Exception e) {
                                ExceptionHandler.process(e);
                            }
                        }
                        codesJarProject.buildWholeCodeProject();
                        toUpdate.add(p);
                    }
                });
                try {
                    install(toUpdate, monitor);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
                toUpdate.forEach(p -> updateCodesJarProjectCache(p));
                // build other codesJar projects which are in main project
                // FIXME might be quite slow if too many
                // solutions:
                // 1. use build cache, won't build others here but build when needed, problem is that need to maintain
                // other build cache
                // 2. build all others here, don't need build cache
                // both solutions need to build those in threads, question is, can eclipse build projects in thread?
                if (buildInMain) {
                    CodesJarResourceCache.getAllCodesJars().stream()
                            .filter(p -> !toUpdate.contains(p) && ProjectManager.getInstance().isInCurrentMainProject(p))
                            .forEach(p -> getRunProcessService().getTalendCodesJarJavaProject(p).buildWholeCodeProject());
                }
            }
        };
        workUnit.setAvoidUnloadResources(true);
        ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(workUnit);
    }

    // TODO to build in parallel
    private static void parallelBuild(IProgressMonitor monitor, List<IProject> projects) throws CoreException {
        Set<IBuildConfiguration> configs = new HashSet<>(3);
        for (IProject project : projects) {
            try {
                configs.add(project.getActiveBuildConfig());
            } catch (Exception e) {
                // Ignore project
            }
        }
        // TODO find a way to trigger parallel build, check Workspace.interalBuild
        ResourcesPlugin.getWorkspace().build(configs.toArray(new IBuildConfiguration[configs.size()]),
                IncrementalProjectBuilder.FULL_BUILD, false, monitor);
        // or just call buildParallel directlly
        org.eclipse.core.internal.resources.Workspace workspace = (Workspace) ResourcesPlugin.getWorkspace();
        // workspace.getBuildManager().buildParallel(configs, requestedConfigs, trigger, buildJobGroup, monitor);
    }

    public static void updateCodesJarProject(Property property) throws Exception {
        IProgressMonitor monitor = new NullProgressMonitor();
        ITalendProcessJavaProject codesJarProject = getRunProcessService().getTalendCodesJarJavaProject(property);
        updateCodesJarProjectPom(monitor, property, codesJarProject.getProjectPom());
        codesJarProject.buildWholeCodeProject();
        Set<Property> set = new HashSet<>();
        set.add(property);
        install(set, monitor);
    }

    private static void install(Set<Property> toUpdate, IProgressMonitor monitor) throws Exception {
        IFile pomFile = createBuildAggregatorPom(toUpdate);
        Set<ITalendProcessJavaProject> projects = toUpdate.stream()
                .map(p -> getRunProcessService().getTalendCodesJarJavaProject(p)).collect(Collectors.toSet());
        try {
            for (ITalendProcessJavaProject project : projects) {
                Model model = MavenPlugin.getMavenModelManager().readMavenModel(project.getProjectPom());
                MavenArtifact artifact = new MavenArtifact();
                artifact.setGroupId(model.getGroupId());
                artifact.setArtifactId(model.getArtifactId());
                artifact.setVersion(model.getVersion());
                String artifactPath = PomUtil.getArtifactPath(artifact);

                String localRepositoryPath = MavenPlugin.getMaven().getLocalRepositoryPath();
                if (localRepositoryPath != null) {
                    File moduleFolder = new File(localRepositoryPath, artifactPath);
                    PomUtil.cleanLastUpdatedFile(moduleFolder.getParentFile());
                }
            }
            Map<String, Object> argumentsMap = new HashMap<>();
            argumentsMap.put(TalendProcessArgumentConstant.ARG_PROGRAM_ARGUMENTS,
                    "-T 1C -f " + BUILD_AGGREGATOR_POM_NAME + " " + TalendMavenConstants.ARG_MAIN_SKIP); //$NON-NLS-1$ //$NON-NLS-2$
            MavenPomCommandLauncher mavenLauncher = new MavenPomCommandLauncher(pomFile, TalendMavenConstants.GOAL_INSTALL);
            mavenLauncher.setArgumentsMap(argumentsMap);
            mavenLauncher.setSkipTests(true);
            mavenLauncher.execute(monitor);
        } finally {
            if (pomFile.exists()) {
                pomFile.delete(true, false, monitor);
            }
        }
    }

    private static IFile createBuildAggregatorPom(Set<Property> toUpdate) throws Exception {
        IFile pomFile = new AggregatorPomsHelper().getProjectPomsFolder().getFile(new Path(BUILD_AGGREGATOR_POM_NAME));
        Model model = new Model();
        model.setModelVersion("4.0.0"); //$NON-NLS-1$
        model.setGroupId(TalendMavenConstants.DEFAULT_GROUP_ID);
        model.setArtifactId("build.codesjar.aggregator"); //$NON-NLS-1$
        model.setVersion("7.0.0"); //$NON-NLS-1$
        model.setPackaging(TalendMavenConstants.PACKAGING_POM);
        model.setModules(new ArrayList<String>());
        toUpdate.stream().forEach(p -> model.getModules().add(getModulePath(p)));
        Parent parent = new Parent();
        parent.setGroupId(PomIdsHelper.getProjectGroupId());
        parent.setArtifactId(PomIdsHelper.getProjectArtifactId());
        parent.setVersion(PomIdsHelper.getProjectVersion());
        model.setParent(parent);
        PomUtil.savePom(null, model, pomFile);
        return pomFile;
    }

    private static String getModulePath(Property property) {
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        IPath basePath = new AggregatorPomsHelper().getProjectPomsFolder().getLocation();
        IPath codeJarProjectPath = new AggregatorPomsHelper(projectTechName).getCodesJarFolder(property).getLocation();
        String modulePath = codeJarProjectPath.makeRelativeTo(basePath).toPortableString();
        return modulePath;
    }

    public static File getCacheFile(String projectTechName, Property property) {
        String cacheFileName = PomIdsHelper.getCodesJarGroupId(projectTechName, property.getItem()) + "." //$NON-NLS-1$
                + property.getLabel().toLowerCase() + "-" //$NON-NLS-1$
                + PomIdsHelper.getCodesVersion(projectTechName) + ".cache"; // $NON-NLS-1$
        return new File(cacheFolder, cacheFileName);
    }

    private static String getInnerCodeKey(String projectTechName, Property property) {
        return KEY_INNERCODE_PREFIX + KEY_SEPERATOR + property.getId() + KEY_SEPERATOR + property.getVersion();
    }

    private static String getModifiedDate(Property property) {
        String modifiedDate = (String) property.getAdditionalProperties().get(ItemProductKeys.DATE.getModifiedKey());
        return StringUtils.isNotBlank(modifiedDate) ? modifiedDate : EMPTY_DATE;
    }

    private static IRunProcessService getRunProcessService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            return (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        return null;
    }

}
