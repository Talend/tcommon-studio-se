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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.utils.VersionUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.IESBService;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Property;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.repository.utils.ItemResourceUtil;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class PomIdsHelper {

    public static final String PREFIX_DEFAULT_GROUPID = "org.example."; //$NON-NLS-1$

    private static Map<String, ProjectPreferenceManager> preferenceManagers = new HashMap<>();

    /**
     * get current project groupId.
     */
    public static String getProjectGroupId() {
        return getProjectGroupId(null);
    }

    public static String getProjectGroupId(String projectTechName) {
        if (projectTechName == null) {
            projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        }
        String groupId = getPreferenceManager(projectTechName).getValue(MavenConstants.PROJECT_GROUPID);
        return groupId;
    }

    /**
     * @return "code".
     * 
     */
    public static String getProjectArtifactId() {
        return TalendMavenConstants.DEFAULT_CODE_PROJECT_ARTIFACT_ID;
    }

    /**
     * get current project version.
     * 
     */
    public static String getProjectVersion() {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        return getProjectVersion(projectTechName);
    }

    public static String getProjectVersion(String projectTechName) {
        ProjectPreferenceManager projectPreferenceManager = getPreferenceManager(projectTechName);
        String projectVersion = projectPreferenceManager.getValue(MavenConstants.PROJECT_VERSION);
        if (StringUtils.isBlank(projectVersion)) {
            projectVersion = PomUtil.getDefaultMavenVersion();
        }
        boolean useSnapshot = projectPreferenceManager.getBoolean(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT);
        if (useSnapshot) {
            projectVersion += "-SNAPSHOT"; //$NON-NLS-1$
        }
        return projectVersion;
    }

    /**
     * 
     * get current project codes groupId.
     */
    public static String getCodesGroupId(String baseName) {
        return getCodesGroupId(null, baseName);
    }

    public static String getCodesGroupId(String projectTechName, String baseName) {
        if (projectTechName == null) {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            if (currentProject != null) {
                projectTechName = currentProject.getTechnicalLabel();
            }
        }
        ProjectPreferenceManager manager = getPreferenceManager(projectTechName);
        String groupId = manager.getValue(MavenConstants.PROJECT_GROUPID);
        groupId += "." + baseName; //$NON-NLS-1$
        return groupId;
    }

    /**
     * get current project codes version.
     * 
     */
    public static String getCodesVersion() {
        return getCodesVersion(null);
    }

    public static String getCodesVersion(String projectTechName) {
        if (projectTechName == null) {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            if (currentProject != null) {
                projectTechName = currentProject.getTechnicalLabel();
            }
        }
        return getProjectVersion(projectTechName);
    }

    @Deprecated
    public static String getJobGroupId(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_JOB + '.' + name.trim().toLowerCase());
        }
        return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_JOB);
    }

    private static String getDefaultgroupIdSuffix(Property property) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
            IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
            return service.getDefaultGroupIdSuffix(property);
        }
        return TalendMavenConstants.DEFAULT_JOB;
    }

    /**
     * @return "org.talend.job.<projectName>".
     */
    public static String getJobGroupId(Property property) {
        if (property != null) {
            if (property.getAdditionalProperties() != null) {
                String groupId = (String) property.getAdditionalProperties().get(MavenConstants.NAME_GROUP_ID);
                if (groupId != null) {
                    return groupId;
                }
            }
            String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
            return getGroupId(projectTechName, getDefaultgroupIdSuffix(property), property);
        }

        return null;
    }

    public static String getJobletGroupId(Property property) {
        String projectTechName = ProjectManager.getInstance().getProject(property).getTechnicalLabel();
        ERepositoryObjectType jobletType = ERepositoryObjectType.getType(property);
        return getGroupId(projectTechName, getJobletBaseName(jobletType), property);
    }

    public static String getJobletBaseName(ERepositoryObjectType jobletType) {
        if (jobletType == ERepositoryObjectType.JOBLET) {
            return TalendMavenConstants.DEFAULT_JOBLET;
        }
        if (jobletType == ERepositoryObjectType.SPARK_JOBLET) {
            return TalendMavenConstants.DEFAULT_SPARK_JOBLET;
        }
        if (jobletType == ERepositoryObjectType.SPARK_STREAMING_JOBLET) {
            return TalendMavenConstants.DEFAULT_SPARK_STREAMING_JOBLET;
        }
        return null;
    }

    public static String getJobletArtifactId(Property property) {
        return getJobArtifactId(property);
    }

    public static String getJobletVersion(Property property) {
        return VersionUtils.getPublishVersion(property.getVersion());
    }

    /**
     * @deprecated use getJobGroupId() instead
     */
    public static String getTestGroupId(String name) {
        if (name != null && !name.trim().isEmpty()) {
            return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_TEST + '.' + name.trim().toLowerCase());
        }
        return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_TEST);
    }

    /**
     * @deprecated use getJobGroupId() instead
     */
    public static String getTestGroupId(Property property) {
        if (property != null) {
            Project currentProject = ProjectManager.getInstance().getCurrentProject();
            if (currentProject != null) {
                return getTestGroupId(currentProject.getTechnicalLabel());
            }
        }
        return getTestGroupId((String) null);
    }

    /**
     * @return "<projectName>.<jobName>".
     */
    public static String getJobArtifactId(Property property) {
        if (property != null) {
            return JavaResourcesHelper.escapeFileName(property.getLabel());
        }
        return null;
    }

    public static String getJobArtifactId(JobInfo jobInfo) {
        if (jobInfo != null) {
            return JavaResourcesHelper.escapeFileName(jobInfo.getJobName());
        }
        return null;
    }

    /**
     * @return "<jobVersion>-<projectName>".
     */
    public static String getJobVersion(Property property) {
        String version = null;
        if (property != null) {
            boolean useSnapshot = false;
            if (property.getAdditionalProperties() != null) {
                version = (String) property.getAdditionalProperties().get(MavenConstants.NAME_USER_VERSION);
                useSnapshot = property.getAdditionalProperties().containsKey(MavenConstants.NAME_PUBLISH_AS_SNAPSHOT);
            }
            if (version == null) {
                version = VersionUtils.getPublishVersion(property.getVersion());
            }
            if (useSnapshot) {
                version += MavenConstants.SNAPSHOT;
            }
        }
        return version;
    }

    public static String getJobVersion(JobInfo jobInfo) {
        if (jobInfo != null) {
            return jobInfo.getJobVersion();
        }
        return null;
    }

    public static String getPomFilter() {
        String projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        ProjectPreferenceManager manager = getPreferenceManager(projectTechName);
        return manager.getValue(MavenConstants.POM_FILTER);
    }

    private static String getGroupId(String projectTechName, String baseName, Property property) {
        if (projectTechName == null) {
            projectTechName = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        }
        ProjectPreferenceManager manager = getPreferenceManager(projectTechName);
        String groupId = manager.getValue(MavenConstants.PROJECT_GROUPID);
        // job and joblet
        boolean appendFolderName = manager.getBoolean(MavenConstants.APPEND_FOLDER_TO_GROUPID);
        if (!appendFolderName) {
            if (baseName != null) {
                groupId += "." + baseName; //$NON-NLS-1$
            }
            return groupId;
        }
        if (property != null) {
            String suffix = getJobFolderSuffix(property); // $NON-NLS-1$
            if (!StringUtils.isEmpty(suffix)) {
                boolean skipBaseGroupId = manager.getBoolean(MavenConstants.SKIP_BASE_GROUPID);
                if (skipBaseGroupId) {
                    return suffix;
                }
                groupId += "." + suffix; //$NON-NLS-1$
            }
        }
        return groupId;
    }

    private static String getJobFolderSuffix(Property property) {
        String suffix = ItemResourceUtil.getItemRelativePath(property).toPortableString();
        suffix = StringUtils.strip(suffix, "/"); //$NON-NLS-1$
        suffix = StringUtils.replace(suffix, "/", "."); //$NON-NLS-1$ //$NON-NLS-2$
        return suffix;
    }

    public static String getDefaultProjetGroupId(String projectName) {
        return PREFIX_DEFAULT_GROUPID + projectName.toLowerCase();
    }

    public static boolean isValidGroupId(String text) {
        if (text != null && text.matches("[\\w\\.]+")) { //$NON-NLS-1$
            return true;
        }
        return false;
    }

    public static void resetPreferencesManagers() {
        preferenceManagers.clear();
    }

    private static ProjectPreferenceManager getPreferenceManager(String projectTechName) {
        if (!preferenceManagers.containsKey(projectTechName)) {
            Project project = ProjectManager.getInstance().getProjectFromProjectTechLabel(projectTechName);
            ProjectPreferenceManager preferenceManager = new ProjectPreferenceManager(project, DesignerMavenPlugin.PLUGIN_ID,
                    false);
            IPreferenceStore preferenceStore = preferenceManager.getPreferenceStore();
            if (StringUtils.isEmpty(preferenceStore.getString(MavenConstants.PROJECT_GROUPID))) {
                preferenceStore.setValue(MavenConstants.PROJECT_GROUPID, getDefaultProjetGroupId(projectTechName));
            }
            if (StringUtils.isEmpty(preferenceStore.getString(MavenConstants.PROJECT_VERSION))) {
                preferenceStore.setValue(MavenConstants.PROJECT_VERSION, PomUtil.getDefaultMavenVersion());
            }
            if (preferenceStore.getString(MavenConstants.POM_FILTER) == null) {
                preferenceStore.setValue(MavenConstants.POM_FILTER, "");
            }
            preferenceManager.save();
            preferenceManagers.put(projectTechName, preferenceManager);
            return preferenceManager;
        }
        return preferenceManagers.get(projectTechName);
    }

}
