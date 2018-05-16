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
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.process.IProcess2;
import org.talend.core.model.process.ProcessUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Property;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryNode;

/**
 * An util tools for java version resources. Detailled comment <br/>
 * 
 */
/**
 * bqian An util tools for java version resources. <br/>
 * 
 */
public class JavaResourcesHelper {

    /**
     * Gets the specific resource in the java project.
     * 
     * @param name
     * @param projectName
     * 
     * @return
     */
    public static IResource getSpecificResourceInJavaProject(IPath path) throws CoreException {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService service = (IRunProcessService) GlobalServiceRegister.getDefault().getService(
                    IRunProcessService.class);
            ITalendProcessJavaProject talendProcessJavaProject = service.getTempJavaProject();
            if (talendProcessJavaProject != null) {
                IProject project = talendProcessJavaProject.getProject();
                IResource resource = project.findMember(path);
                return resource;
            }
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
        String name = project.getTechnicalLabel().toLowerCase();
        return name;
    }

    public static String getJobFolderName(String jobName, String version) {
        String newJobName = escapeFileName(jobName).toLowerCase();
        if (version != null) {
            newJobName += '_' + version.replace('.', '_'); 
        }
        return newJobName;
    }

    /**
     * 
     * get the jar name like maven "artifactId.version"
     */
    public static String getJobJarName(String jobName, String version) {
        String newJobName = getJobFolderName(jobName, version);
        return newJobName;
    }

    public static String escapeFileName(final String fileName) {
        return fileName != null ? fileName.replace(' ', '_') : ""; //$NON-NLS-1$
    }

    /**
     * even project is "Test", will return "test".
     */
    public static String getProjectFolderName(Property property) {
        if (property == null) {
            return ProjectManager.getInstance().getCurrentProject().getTechnicalLabel().toLowerCase();
        }
        return getProjectFolderName(property.getItem());
    }

    /**
     * even project is "Test", will return "test".
     */
    public static String getProjectFolderName(Item item) {
        ProjectManager pManager = ProjectManager.getInstance();
        org.talend.core.model.properties.Project p = pManager.getProject(item);
        return getProjectFolderName(p.getTechnicalLabel());
    }

    public static String getProjectFolderName(String projectName) {
        String projectFolderName = projectName.toLowerCase();
        return projectFolderName;
    }

    /**
     * project name is "Test". will return "org.talend.test".
     * 
     * If other branding, like "Camel", will return "org.camel.test".
     * @deprecated
     */
    public static String getGroupName(String projectName) {

        String itemGroupPrefixName = "org.talend"; //$NON-NLS-1$
        if (projectName != null) {
            itemGroupPrefixName += '.' + projectName;
        }
        return itemGroupPrefixName.trim().toLowerCase();
    }

    /**
     * return the getGroupName with project "Test", and item name "TestJob".
     * 
     * something like: "org.talend.test.testjob".
     * @deprecated
     */
    public static String getGroupItemName(String projectName, String itemName) {
        if (itemName == null) {
            return null;
        }
        return getGroupName(projectName) + '.' + getJobFolderName(itemName, null);
    }

    public static String getSimpleClassName(IProcess process) {
        if(ProcessUtils.isTestContainer(process)){
            return process.getName() + "Test"; //$NON-NLS-1$
        }
        return process.getName();
    }

    public static String getJobClassName(IProcess2 process) {
        Item processItem = process.getProperty().getItem();
        return getJobClassPackageName(processItem) + "." + getSimpleClassName(process); //$NON-NLS-1$
    }

    /**
     * if project "Test" and item "TestJob 0.1" , will return "test.testjob_0_1"
     * 
     */
    public static String getJobClassPackageName(Item processItem) {
        return getJobClassPackageName(processItem, false);
    }

    public static String getJobClassPackageName(Item processItem, boolean isTest) {
        Property itemProperty = processItem.getProperty();
        String packageName = getProjectFolderName(processItem);
        if (isTest) {
            Item baseItem = ProcessUtils.getTestContainerBaseItem(processItem);
            if (baseItem != null && baseItem.getProperty() != null) {
                Property baseItemProperty = baseItem.getProperty();
                packageName = packageName + '.' + getJobFolderName(baseItemProperty.getLabel(), baseItemProperty.getVersion());
            }
        }
        packageName = packageName + '.' + getJobFolderName(itemProperty.getLabel(), itemProperty.getVersion());
        return packageName;
    }

    public static String getJobClassPackageName(String projectName, String jobName, String jobVersion) {
        String packageName = getProjectFolderName(projectName) + '.' + getJobFolderName(jobName, jobVersion);
        return packageName;
    }

    /**
     * if project "Test" and item "TestJob 0.1" , will return "test.testjob_0_1.TestJob"
     * 
     */
    public static String getJobPackagedClass(Item processItem, boolean filenameFromLabel) {
        return getJobPackagedClass(processItem, filenameFromLabel, false);
    }

    public static String getJobPackagedClass(Item processItem, boolean filenameFromLabel, boolean isTest) {
        String jobName = filenameFromLabel ? escapeFileName(processItem.getProperty().getLabel()) : processItem.getProperty()
                .getId();
        if (isTest) {
            jobName = jobName + "Test"; //$NON-NLS-1$
        }
        return getJobClassPackageName(processItem, isTest) + '.' + jobName;
    }

    public static String getJobClassName(Item processItem) {
        return getJobPackagedClass(processItem, true);
    }

    public static String getJobClassName(RepositoryNode jobNode) {
        return getJobClassName(jobNode.getObject().getProperty().getItem());
    }

    /**
     * if project "Test" and item "TestJob 0.1" , will return "test/testjob_0_1".
     * 
     */
    public static String getJobClassPackageFolder(Item processItem) {
        return getJobClassPackageFolder(processItem, false);
    }

    public static String getJobClassPackageFolder(Item processItem, boolean isTest) {
        String packageName = getJobClassPackageName(processItem, isTest);
        return changePackage2Path(packageName);
    }

    /**
     * seems only used for generator in javajet
     */
    public static String getJobClassPackageFolder(IProcess process) {
        if (process instanceof IProcess2) {
            Property property = ((IProcess2) process).getProperty();
            if (property != null) {
                return getJobClassPackageFolder(property.getItem(), ProcessUtils.isTestContainer(process));
            }
        }
        return null;
    }

    public static String getJobClassPackageFolder(String projectName, String jobName, String jobVersion) {
        String packageName = getJobClassPackageName(projectName, jobName, jobVersion);
        return changePackage2Path(packageName);
    }

    private static String changePackage2Path(String packageName) {
        if (packageName != null) {
            return packageName.replace('.', '/');
        }
        return null;
    }

    /**
     * project is test. job is "TestJob 0.1".
     * 
     * will return test/testjob_0_1/TestJob.java
     */
    public static String getJobClassFilePath(Item processItem, boolean filenameFromLabel) {
        return getJobClassFilePath(processItem, filenameFromLabel, false);
    }

    public static String getJobClassFilePath(Item processItem, boolean filenameFromLabel, boolean isTest) {
        String jobPackagedClass = getJobPackagedClass(processItem, filenameFromLabel, isTest);
        String path = changePackage2Path(jobPackagedClass);
        if (path != null) {
            return path + JavaUtils.JAVA_EXTENSION;
        }
        return null;
    }

    /**
     * like Default
     */
    public static String getJobContextName(IContext c) {
        return escapeFileName(c.getName());
    }

    /**
     * like Default.properties
     */
    public static String getJobContextFileName(IContext c) {
        return getJobContextName(c) + JavaUtils.JAVA_CONTEXT_EXTENSION;
    }

    public static String getResouceClasspath(String resourceId, String resourceVersion) {
        IRepositoryViewObject rvo = null;
        try {
            if (RelationshipItemBuilder.LATEST_VERSION.equals(resourceVersion)) {
                rvo = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getLastVersion(resourceId);
            } else {
                rvo = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory().getSpecificVersion(resourceId, resourceVersion, true);
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        if (rvo != null) {
            return getResouceClasspath(rvo.getProperty().getItem(), resourceVersion);
        }
        return ""; //$NON-NLS-1$
    }

    public static String getResouceClasspath(Item item, String resourceVersion) {
        String resouceClasspath = item.getState().getPath();
        if (resouceClasspath != null && !resouceClasspath.isEmpty()) {
            resouceClasspath += '/';
        } else {
            resouceClasspath = ""; //$NON-NLS-1$
        }
        String fileName = item.getProperty().getLabel();
        if (!RelationshipItemBuilder.LATEST_VERSION.equals(resourceVersion)) {
            final IPath path = new Path(fileName);
            final String fileExtension = path.getFileExtension();
            fileName = path.removeFileExtension().toPortableString() + '_' + resourceVersion;
            if (fileExtension != null && !fileExtension.isEmpty()) {
                fileName += '.' + fileExtension;
            }
        }
        return resouceClasspath + fileName;
    }

}
