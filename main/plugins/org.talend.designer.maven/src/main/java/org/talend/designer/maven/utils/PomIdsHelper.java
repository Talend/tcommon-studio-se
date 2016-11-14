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

import org.talend.core.model.general.Project;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.properties.Property;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.designer.maven.model.TalendMavenConstants;
import org.talend.repository.ProjectManager;

/**
 * DOC ggu class global comment. Detailed comment
 */
public class PomIdsHelper {

    /**
     * @return "org.talend.master.<ProjectName>", like "org.talend.master.test".
     * 
     * If projectName is null, will return default one "org.talend.master" only.
     * 
     * always depend on current project.
     */
    public static String getProjectGroupId() {
        final Project currentProject = ProjectManager.getInstance().getCurrentProject();
        if (currentProject != null) {
            String technicalLabel = currentProject.getTechnicalLabel();
            return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_MASTER + '.' + technicalLabel);
        }
        return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_MASTER);
    }

    /**
     * @return "code".
     * 
     */
    public static String getProjectArtifactId() {
        return TalendMavenConstants.DEFAULT_CODE_PROJECT_ARTIFACT_ID;
    }

    /**
     * @return "<version>"
     * 
     */
    public static String getProjectVersion() {
        return PomUtil.getDefaultMavenVersion();
    }

    /**
     * @return "org.talend.code.<projectName>", like "org.talend.code.test".
     * 
     * if project is null, will return "org.talend.code".
     * 
     * always depend on current project.
     */
    public static String getCodesGroupId(String baseName) {
        final Project currentProject = ProjectManager.getInstance().getCurrentProject();
        if (currentProject != null) {
            String technicalLabel = currentProject.getTechnicalLabel();
            return JavaResourcesHelper.getGroupName(baseName + '.' + technicalLabel);
        }
        return JavaResourcesHelper.getGroupName(baseName);
    }

    /**
     * @return "<version>"
     * 
     */
    public static String getCodesVersion() {
        return PomUtil.getDefaultMavenVersion();
    }

    /**
     * @return "org.talend.job.<name>", like "org.talend.job.test"
     */
    public static String getJobGroupId(String name) {
        if (name != null) {
            return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_JOB + '.' + name);
        }
        return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_JOB);
    }

    public static String getTestGroupId(String name) {
        if (name != null) {
            return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_TEST + '.' + name);
        }
        return JavaResourcesHelper.getGroupName(TalendMavenConstants.DEFAULT_TEST);
    }

    public static String getTestGroupId(Property property) {
        if (property != null) {
            ProjectManager pManager = ProjectManager.getInstance();
            Project currentProject = pManager.getCurrentProject();
            if (currentProject != null) {
                return getTestGroupId(currentProject.getTechnicalLabel());
            }
        }
        return getTestGroupId((String) null);
    }

    /**
     * @return "org.talend.job.<projectName>".
     */
    public static String getJobGroupId(Property property) {
        if (property != null) {
            ProjectManager pManager = ProjectManager.getInstance();
            Project currentProject = pManager.getCurrentProject();
            if (currentProject != null) {
                return getJobGroupId(currentProject.getTechnicalLabel());
            }
        }
        return getJobGroupId((String) null);
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
        if (property != null) {
            String version = property.getVersion();
            return version;
        }
        return null;
    }

    public static String getJobVersion(JobInfo jobInfo) {
        if (jobInfo != null) {
            return jobInfo.getJobVersion();
        }
        return null;
    }

}
