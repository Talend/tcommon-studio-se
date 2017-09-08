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
package org.talend.core.model.repository.job;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.process.JobInfo;
import org.talend.core.model.utils.JavaResourcesHelper;
import org.talend.core.model.utils.PerlResourcesHelper;

/**
 * Reference to both jobs and its resources.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JobResource.java 下午03:41:44 2007-7-6 +0000 (2007-7-6) yzhang $
 * 
 */
public class JobResource {

    private String projectName;

    private JobInfo jobInfo;

    private IWorkspace workspace;

    /**
     * Constructor.
     * 
     * yzhang JobResource constructor comment.
     */
    public JobResource() {
        workspace = ResourcesPlugin.getWorkspace();
    }

    public JobResource(String projectName, JobInfo jobInfo) {
        this();
        this.jobInfo = jobInfo;
        this.projectName = projectName.toLowerCase();
    }

    /**
     * Getter for projectName.
     * 
     * @return the projectName
     */
    public String getProjectName() {
        return this.projectName;
    }

    /**
     * Sets the projectName.
     * 
     * @param projectName the projectName to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName.toLowerCase();
    }

    /**
     * Get the unique resource name.
     * 
     * yzhang Comment method "getResourceName".
     */
    public String getUniqueResourceName() {
        return projectName + "." + jobInfo.getJobName() + "." + jobInfo.getJobVersion(); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Return the resource of current job.
     * 
     * yzhang Comment method "getResorcePath".
     * 
     * @return
     */
    public List<IResource> getResource() {
        List<IResource> resources = new ArrayList<IResource>();
        ECodeLanguage language = LanguageManager.getCurrentLanguage();
        IResource res = null;

        if (language == ECodeLanguage.JAVA) {
            res = workspace.getRoot().findMember(".Java/src" + "/" + projectName + "/" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + JavaResourcesHelper.getJobFolderName(jobInfo.getJobName(), jobInfo.getJobVersion()));
            if (res != null) {
                resources.add(res);
            }
        } else if (language == ECodeLanguage.PERL) {
            String rootProjectName = PerlResourcesHelper.getRootProjectName(projectName);
            res = workspace.getRoot().findMember(".Perl" + "/" //$NON-NLS-1$ //$NON-NLS-2$
                    + PerlResourcesHelper.getJobFileName(rootProjectName, jobInfo.getJobName(), jobInfo.getJobVersion()));
            if (res != null) {
                resources.add(res);
            }

            res = workspace.getRoot().findMember(
                    ".Perl" //$NON-NLS-1$
                            + "/" //$NON-NLS-1$
                            + PerlResourcesHelper.getContextFileName(rootProjectName, jobInfo.getJobName(),
                                    jobInfo.getJobVersion(), jobInfo.getContextName()));
            if (res != null) {
                resources.add(res);
            }
        }
        return resources;
    }

    public JobInfo getJobInfo() {
        return this.jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
}
