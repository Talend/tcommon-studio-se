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
package org.talend.core.model.repository.documentation.generation;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.Project;
import org.talend.repository.ProjectManager;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: RepositoryPathProvider.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class DocumentationPathProvider {

    public static IPath getPathProjectFolder(Item item, String folderName) {
        return getPathProjectFolder(ProjectManager.getInstance().getProject(item), folderName);
    }

    public static IPath getPathProjectFolder(Project project, String folderName) {
        try {
            IProject iProject = ProjectManager.getInstance().getResourceProject(project);
            if (iProject != null) {
                IFolder folder = ResourceUtils.getFolder(iProject, folderName, true);
                return folder.getLocation();
            }
        } catch (PersistenceException e) {
            //
        }
        return null;
    }

    public static IPath getPathFileName(Item item, String folderName, String fileName) {
        IPath pathProjectFolder = getPathProjectFolder(item, folderName);
        if (pathProjectFolder != null) {
            return pathProjectFolder.append(fileName);
        }
        return null;
    }

    public static IPath getPathFileName(Project project, String folderName, String fileName) {
        IPath pathProjectFolder = getPathProjectFolder(project, folderName);
        if (pathProjectFolder != null) {
            return pathProjectFolder.append(fileName);
        }
        return null;
    }

}
