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
package org.talend.designer.maven.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.maven.model.Model;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.core.runtime.util.ItemDateParser;
import org.talend.designer.maven.launch.MavenPomCommandLauncher;
import org.talend.designer.maven.model.BuildCacheInfo;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryService;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class BuildCacheManager {

    public final static String BUILD_AGGREGATOR_POM_NAME = "build-aggregator.pom"; //$NON-NLS-1$

    private final String SEPARATOR = "|"; //$NON-NLS-1$

    private static BuildCacheManager instance;

    private Map<String, BuildCacheInfo> cache = new HashMap<>();

    private Map<String, BuildCacheInfo> currentCache = new HashMap<>();

    private Set<String> currentmodules = new HashSet<>();

    private Set<ITalendProcessJavaProject> subjobProjects = new HashSet<>();

    private Map<ERepositoryObjectType, Date> codesLastChangeCache = new HashMap<>();

    private Map<ERepositoryObjectType, Date> codesLastBuildCache = new HashMap<>();

    private IFile pomFile;

    private AggregatorPomsHelper aggregatorPomsHelper;

    private BuildCacheManager() {
        aggregatorPomsHelper = new AggregatorPomsHelper();
    }

    public synchronized static BuildCacheManager getInstance() {
        if (instance == null) {
            instance = new BuildCacheManager();
        }
        return instance;
    }

    public boolean isJobBuild(Property property) {
        BuildCacheInfo cacheInfo = cache.get(getKey(property));
        if (cacheInfo == null) {
            return false;
        }
        Date cachedTimestamp = cacheInfo.getTimestamp();
        Date currentTimeStamp = getTimestamp(property);
        if (currentTimeStamp.compareTo(cachedTimestamp) != 0) {
            return false;
        }
        String cachedBuildType = cacheInfo.getBuildType();
        String currentBuildType = getBuildType(property);
        // default build type of standard/bd job could be null
        if (cachedBuildType == null && currentBuildType == null) {
            return true;
        }
        if (cachedBuildType == null || currentBuildType == null) {
            return false;
        }

        return currentBuildType.equals(cachedBuildType);
    }

    public void putCache(Property property) {
        currentCache.put(getKey(property), generateCacheInfo(property));
        currentmodules.add(getModulePath(property));
        subjobProjects.add(getTalendJobJavaProject(property));
    }

    public void removeCache(Property property) {
        currentCache.remove(getKey(property));
        currentmodules.remove(getModulePath(property));
        subjobProjects.remove(getTalendJobJavaProject(property));
    }

    public void clearCurrentCache() {
        currentCache.clear();
        currentmodules.clear();
        subjobProjects.clear();
        aggregatorPomsHelper = new AggregatorPomsHelper();
    }

    public void performBuildSuccess() {
        cache.putAll(currentCache);
        clearCurrentCache();
    }

    public void performBuildFailure() {
        restoreSubjobPoms();
        clearCurrentCache();
    }

    public void build(IProgressMonitor monitor, Map<String, Object> argumentsMap) throws Exception {
        if (needTempAggregator()) {
            createBuildAggregatorPom();
            try {
                for (ITalendProcessJavaProject project : subjobProjects) {
                    MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
                    Model projModel = mavenModelManager.readMavenModel(project.getProjectPom());
                    MavenArtifact artifact = new MavenArtifact();
                    artifact.setGroupId(projModel.getGroupId());
                    artifact.setArtifactId(projModel.getArtifactId());
                    artifact.setVersion(projModel.getVersion());
                    String artifactPath = PomUtil.getArtifactPath(artifact);

                    final IMaven maven = MavenPlugin.getMaven();
                    String localRepositoryPath = maven.getLocalRepositoryPath();
                    if (localRepositoryPath != null) {
                        File moduleFolder = new File(localRepositoryPath, artifactPath);

                        PomUtil.cleanLastUpdatedFile(moduleFolder.getParentFile());
                    }
                }

                String goal = (String) argumentsMap.get(TalendProcessArgumentConstant.ARG_GOAL);
                MavenPomCommandLauncher mavenLauncher = new MavenPomCommandLauncher(pomFile, goal);
                mavenLauncher.setArgumentsMap(argumentsMap);
                mavenLauncher.execute(monitor);
            } finally {
                deleteBuildAggregatorPom();
                // better do restore right after aggregator build.
                restoreSubjobPoms();
            }
        }
    }

    /**
     * DOC nrousseau Comment method "needTempAggregator".
     * 
     * @return
     */
    public boolean needTempAggregator() {
        return !currentmodules.isEmpty();
    }

    public void updateCodesLastChangeDate(ERepositoryObjectType codeType, Property property) {
        Date currentLastChangeDate = getTimestamp(property);
        Date cacheLastChangeDate = codesLastChangeCache.get(codeType);
        if (cacheLastChangeDate == null || currentLastChangeDate.compareTo(cacheLastChangeDate) != 0) {
            codesLastChangeCache.put(codeType, currentLastChangeDate);
        }
    }

    public void updateCodeLastBuildDate(ERepositoryObjectType codeType) {
        Date cacheLastChangeDate = codesLastChangeCache.get(codeType);
        if (cacheLastChangeDate == null) {
            cacheLastChangeDate = new Date();
            codesLastChangeCache.put(codeType, cacheLastChangeDate);
        }
        codesLastBuildCache.put(codeType, cacheLastChangeDate);
    }

    public boolean isCodesBuild(ERepositoryObjectType codeType) {
        Date lastBuildDate = codesLastBuildCache.get(codeType);
        if (lastBuildDate == null) {
            return false;
        }
        Date cacheLastChangeDate = codesLastChangeCache.get(codeType);
        if (cacheLastChangeDate == null) {
            return false;
        }
        return lastBuildDate.compareTo(cacheLastChangeDate) == 0;
    }

    private void createBuildAggregatorPom() throws Exception {
        pomFile = aggregatorPomsHelper.getProjectPomsFolder().getFile(new Path(BUILD_AGGREGATOR_POM_NAME));
        Model model = new Model();
        model.setModelVersion("4.0.0"); //$NON-NLS-1$
        model.setGroupId(TalendMavenConstants.DEFAULT_GROUP_ID);
        model.setArtifactId("build.aggregator"); //$NON-NLS-1$
        model.setVersion("7.0.0"); //$NON-NLS-1$
        model.setPackaging(TalendMavenConstants.PACKAGING_POM);
        model.setModules(new ArrayList<String>());
        model.getModules().addAll(currentmodules);

        PomUtil.savePom(null, model, pomFile);
    }

    private void deleteBuildAggregatorPom() throws CoreException {
        pomFile = aggregatorPomsHelper.getProjectPomsFolder().getFile(new Path(BUILD_AGGREGATOR_POM_NAME));
        if (pomFile.exists()) {
            pomFile.delete(true, false, null);
        }
    }

    private String getKey(Property property) {
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        String jobId = property.getId();
        String jobVersion = property.getVersion();
        String key = projectTechName + SEPARATOR + jobId + SEPARATOR + jobVersion;
        return key;
    }

    private BuildCacheInfo generateCacheInfo(Property property) {
        return new BuildCacheInfo(getKey(property), getBuildType(property), getTimestamp(property));
    }

    private Date getTimestamp(Property property) {
        return ItemDateParser.parseAdditionalDate(property, ItemProductKeys.DATE.getModifiedKey());
    }

    private String getBuildType(Property property) {
        return (String) property.getAdditionalProperties().get(TalendProcessArgumentConstant.ARG_BUILD_TYPE);
    }

    private String getModulePath(Property property) {
        String modulePath = null;
        IPath basePath = null;
        IPath jobProjectPath = AggregatorPomsHelper.getJobProjectPath(property, null);
        if (!ProjectManager.getInstance().isInCurrentMainProject(property)) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
                IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault()
                        .getService(IRepositoryService.class);
                if (service.isGIT()) {
                    modulePath = "../../../../"; //$NON-NLS-1$
                    basePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().append("/.repositories"); //$NON-NLS-1$
                } else if (service.isSVN()) {
                    modulePath = "../../"; //$NON-NLS-1$
                    basePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
                }
            }
            if (modulePath == null || basePath == null) {
                throw new RuntimeException("modulePath or basePath can not be null!"); //$NON-NLS-1$
            }
        } else {
            modulePath = ""; //$NON-NLS-1$
            basePath = aggregatorPomsHelper.getProjectPomsFolder().getLocation();
        }
        jobProjectPath = jobProjectPath.makeRelativeTo(basePath);
        modulePath += jobProjectPath.toPortableString();

        return modulePath;
    }

    private void restoreSubjobPoms() {
        // restore all modules pom file.
        for (ITalendProcessJavaProject subjobProject : subjobProjects) {
            PomUtil.restorePomFile(subjobProject);
        }
    }

    private ITalendProcessJavaProject getTalendJobJavaProject(Property property) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            return service.getTalendJobJavaProject(property);
        }
        return null;
    }

}
