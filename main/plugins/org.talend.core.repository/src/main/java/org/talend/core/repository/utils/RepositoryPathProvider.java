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
package org.talend.core.repository.utils;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.Project;
import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: RepositoryPathProvider.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class RepositoryPathProvider {

    public static IProject getProject() throws PersistenceException {
        RepositoryContext repositoryContext = (RepositoryContext) CoreRuntimePlugin.getInstance().getContext()
                .getProperty(Context.REPOSITORY_CONTEXT_KEY);
        Project project = repositoryContext.getProject();
        return ResourceUtils.getProject(project);
    }

    public static IPath getPathRootProject() {

        try {
            IProject iProject = getProject();
            return iProject.getFullPath();
        } catch (PersistenceException e) {
            return null;
        }
    }

    public static IPath getPathProjectFolder(String folderName) {
        try {
            IProject iProject = getProject();
            IFolder folder = ResourceUtils.getFolder(iProject, folderName, true);
            return folder.getLocation();
        } catch (PersistenceException e) {
            return null;
        }

    }

    public static IFolder getFolder(String folderName) {
        try {
            IProject iProject = getProject();
            IFolder folder = ResourceUtils.getFolder(iProject, folderName, true);
            return folder;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public static IPath getPathFileName(String folderName, String fileName) {
        IPath pathProjectFolder = getPathProjectFolder(folderName);
        return pathProjectFolder.append(fileName);
    }

}
