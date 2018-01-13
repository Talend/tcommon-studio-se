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

import java.util.ArrayList;
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
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.properties.Property;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.process.TalendProcessArgumentConstant;
import org.talend.designer.maven.launch.MavenPomCommandLauncher;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;

/**
 * DOC zwxue class global comment. Detailled comment
 */
public class BuildCacheManager {

    private final String BUILD_AGGREGATOR_POM_NAME = "build-aggregator.pom"; //$NON-NLS-1$

    private final String SEPARATOR = "|"; //$NON-NLS-1$

    private static BuildCacheManager instance;

    private Map<String, String> cache = new HashMap<>();

    private Map<String, String> currentCache = new HashMap<>();

    private Set<String> currentmodules = new HashSet<>();

    private Set<ITalendProcessJavaProject> subjobProjects = new HashSet<>();

    private IFile pomFile;

    private AggregatorPomsHelper aggregatorPomsHelper;

    private BuildCacheManager() {
        aggregatorPomsHelper = new AggregatorPomsHelper(ProjectManager.getInstance().getCurrentProject());
    }

    public synchronized static BuildCacheManager getInstance() {
        if (instance == null) {
            instance = new BuildCacheManager();
        }
        return instance;
    }

    public boolean isJobBuild(Property property) {
        // FIXME use Date compare via ItemDateParser.parseAdditionalDate()
        String cachedTimestamp = cache.get(getKey(property));
        String currentTimeStamp = getTimestamp(property);

        return currentTimeStamp.equals(cachedTimestamp);
    }

    public void putCache(Property property) {
        currentCache.put(getKey(property), getTimestamp(property));
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
        if (!currentmodules.isEmpty()) {
            createBuildAggregatorPom();

            String goal = (String) argumentsMap.get(TalendProcessArgumentConstant.ARG_GOAL);
            MavenPomCommandLauncher mavenLauncher = new MavenPomCommandLauncher(pomFile, goal);
            mavenLauncher.setArgumentsMap(argumentsMap);
            try {
                mavenLauncher.execute(monitor);
            } finally {
                deleteBuildAggregatorPom();
                // better do restore right after aggregator build.
                restoreSubjobPoms();
            }
        }
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

    private String getTimestamp(Property property) {
        String value = (String) property.getAdditionalProperties().get("modified_date"); //$NON-NLS-1$
        return value;
    }

    private String getModulePath(Property property) {
        String modulePath = ""; //$NON-NLS-1$
        IPath basePath = null;
        IPath jobProjectPath = AggregatorPomsHelper.getJobProjectPath(property, null);
        if (!ProjectManager.getInstance().isInCurrentMainProject(property)) {
            modulePath = "../../"; //$NON-NLS-1$
            basePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
        } else {
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
