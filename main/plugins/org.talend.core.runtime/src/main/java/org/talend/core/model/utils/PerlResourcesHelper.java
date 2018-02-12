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
package org.talend.core.model.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.designer.runprocess.ItemCacheManager;
import org.talend.repository.ProjectManager;

/**
 * bqian An util tools for perl version resources. <br/>
 * 
 */
public class PerlResourcesHelper {

    private static final String FILE_SUFFIX = ".pl"; //$NON-NLS-1$

    public static final boolean USE_VERSIONING = true;

    /**
     * Gets the specific resource in the perl project.
     * 
     * @param name
     * @param projectName
     * 
     * @return
     */
    public static IResource getSpecificResourceInPerlProject(IPath path) throws CoreException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            IProject project = service.getProject(ECodeLanguage.PERL);
            IResource resource = project.findMember(path);
            return resource;
        }
        return null;
    }

    /**
     * Gets current project name.
     * 
     * @return
     */
    public static String getCurrentProjectName() {
        ProjectManager pManager = ProjectManager.getInstance();
        Project project = pManager.getCurrentProject();
        return project.getTechnicalLabel();
    }

    /**
     * ftang Comment method "escapeSpace".
     * 
     * @param name
     * @return
     */
    public static String escapeSpace(String name) {
        return name != null ? name.replace(" ", "") : ""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public static String getRootProjectName(Property property) {
        if (property == null) {
            return ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        }
        return getRootProjectName(property.getItem());
    }

    public static String getRootProjectName(Item item) {
        ProjectManager pManager = ProjectManager.getInstance();
        org.talend.core.model.properties.Project p = pManager.getProject(item);
        String projectFolderName = p.getTechnicalLabel();
        return projectFolderName;
    }

    public static String getRootProjectName(String name) {
        return name.toUpperCase();
    }

    public static String getJobFileName(String projectName, String jobName, String version) {
        String jobFileName = projectName + ".job_" + escapeSpace(jobName); //$NON-NLS-1$
        if (USE_VERSIONING) {
            jobFileName += "_" + version; //$NON-NLS-1$
        }
        jobFileName += FILE_SUFFIX;

        return jobFileName;
    }

    public static String getJobFileName(String jobId, String version) {
        ProcessItem processItem = ItemCacheManager.getProcessItem(jobId);
        if (processItem != null) {
            return getJobFileName(getRootProjectName(processItem), processItem.getProperty().getLabel(), version);
        }
        return null;
    }

    public static String getContextFileName(String projectName, String jobName, String version, String context) {
        String contextFileName = projectName + ".job_" + escapeSpace(jobName); //$NON-NLS-1$
        if (USE_VERSIONING) {
            contextFileName += "_" + version; //$NON-NLS-1$
        }
        contextFileName += "_" + escapeSpace(context); //$NON-NLS-1$
        contextFileName += FILE_SUFFIX;

        return contextFileName;
    }

}
