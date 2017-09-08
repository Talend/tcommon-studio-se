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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.CorePlugin;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.process.JobInfo;

/**
 * Management of deletion and protection on resource of jobs.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ResourceManager.java 下午03:44:34 2007-7-6 +0000 (2007-7-6) yzhang $
 * 
 */
public class JobResourceManager {

    private static JobResourceManager manager;

    private Map<String, JobResource> protectedJobs;

    /**
     * Set the default constructor as private avoid accessed by other method.
     * 
     * yzhang ResourceManager constructor comment.
     */
    private JobResourceManager() {
        protectedJobs = new HashMap<String, JobResource>();
    }

    /**
     * Return the single instance of this class.
     * 
     * yzhang Comment method "getInstance".
     * 
     * @return
     */
    public static JobResourceManager getInstance() {
        if (manager == null) {
            manager = new JobResourceManager();
        }
        return manager;
    }

    /**
     * Add protection on the resource of job avoid deleted by some operation.
     * 
     * yzhang Comment method "addProtection".
     */
    public void addProtection(IJobResourceProtection protection) {
        String[] ids = protection.calculateProtectedIds();
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            JobResource resource = protection.getJobResource(id);
            protectedJobs.put(id, resource);
        }

    }

    /**
     * To see whether this resource is protected by other operation.
     * 
     * yzhang Comment method "canBeDeleted".
     * 
     * @param resource
     */
    private boolean canBeDeleted(JobResource resource) {
        String uniqueName = resource.getUniqueResourceName();
        for (JobResource r : protectedJobs.values()) {
            if (r.getUniqueResourceName().equalsIgnoreCase(uniqueName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * To see whether this resource is protected by other operation.
     * 
     * yzhang Comment method "isProtected".
     * 
     * @param resource
     * @return
     */
    public boolean isProtected(JobResource resource) {
        return !canBeDeleted(resource);
    }

    /**
     * Delete the resource.
     * 
     * yzhang Comment method "deleteResource".
     * 
     * @param resource
     */
    public void deleteResource(JobResource resource) {
        if (!canBeDeleted(resource)) {
            return;
        }
        try {
            List<IResource> resources = resource.getResource();
            for (IResource re : resources) {
                if (re != null) {
                    re.delete(true, null);
                }
            }
        } catch (CoreException e) {
            // can't delete don't cause problem in the application so ignore it
            // RuntimeExceptionHandler.process(e);
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

    }

    /**
     * Release the protection of the resource under specific job.
     * 
     * yzhang Comment method "removeProtection".
     * 
     * @param protection
     */
    public void removeProtection(IJobResourceProtection protection) {
        String[] ids = protection.getProtectedIds();
        if (ids == null) {
            return;
        }
        for (String id : ids) {
            protectedJobs.remove(id);
        }
    }

    /**
     * 
     * DOC achen Comment method "getJobResource".
     * 
     * @param jobInfo
     * @return
     */
    public JobResource getJobResource(JobInfo jobInfo) {
        String projectName = ((RepositoryContext) CorePlugin.getContext().getProperty(Context.REPOSITORY_CONTEXT_KEY))
                .getProject().getLabel();
        String currentJobId = "talend_editor_" + projectName + "_" + jobInfo.getJobName() //$NON-NLS-1$ //$NON-NLS-2$
                + "_" + jobInfo.getJobVersion(); //$NON-NLS-1$
        return protectedJobs.get(currentJobId);
    }
}
