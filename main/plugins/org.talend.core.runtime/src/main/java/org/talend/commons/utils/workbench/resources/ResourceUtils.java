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
package org.talend.commons.utils.workbench.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.ResourceNotFoundException;
import org.talend.commons.i18n.internal.Messages;
import org.talend.core.model.general.Project;

/**
 * Provides utilities methods on IResource.<br/>
 * 
 * $Id: ResourceUtils.java 54939 2011-02-11 01:34:57Z mhirt $
 * 
 */
public final class ResourceUtils {

    private static Logger log = Logger.getLogger(ResourceUtils.class);

    /**
     * Default Constructor. Must not be used.
     */
    private ResourceUtils() {
    }

    /**
     * Load a project (IResource speaking) in the current workspace from a project (model speaking).
     * 
     * @param project - the project to retrieve
     * @return the IProject matching the project
     * @throws PersistenceException if the IProject cannot be retrieve
     */
    public static IProject getProject(Project project) throws PersistenceException {
        return getProject(project.getTechnicalLabel());
    }

    /**
     * Load a project in the current workspace.
     * 
     * @param projectName - the name of the project to retrieve
     * @return the IProject with the right name
     * @throws PersistenceException if no project with that name exists in the workspace
     */
    public static IProject getProject(String projectName) throws PersistenceException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProject fsProject = workspace.getRoot().getProject(projectName);

        if (!fsProject.exists()) {
            String msg = Messages.getString("resources.project.notGet", fsProject.getName()); //$NON-NLS-1$
            throw new ResourceNotFoundException(msg);
        }
        return fsProject;
    }

    /**
     * Find all projects in the workspace having the given nature.
     * 
     * @param natureId Nature id.
     * @return all projects in the workspace having the given nature.
     * @throws PersistenceException
     */
    public static IProject[] getProjetWithNature(String natureId) throws PersistenceException {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject[] allProjects = root.getProjects();

        List<IProject> projects = new ArrayList<IProject>();
        for (IProject prj : allProjects) {
            try {
                prj.open(IResource.BACKGROUND_REFRESH, null);
                if (prj.hasNature(natureId)) {
                    projects.add(prj);
                }
            } catch (CoreException e) {
                // Do nothing
            }
        }

        IProject[] prjs = new IProject[projects.size()];
        prjs = projects.toArray(prjs);
        return prjs;
    }

    /**
     * Load a folder from the specified project.
     * 
     * @param source - the source project
     * @param folderName - the name of the folder to retrieve
     * @param forceExists - true to throw a <code>ResourceNotFoundException</code> when does'nt exist
     * @return the folder matching the name
     * @throws PersistenceException if a folder with this name doesn't exist in the specified project
     */
    public static IFolder getFolder(IProject source, String folderName, boolean forceExists) throws PersistenceException {
        IFolder processFolder = source.getFolder(folderName);

        if (forceExists && !processFolder.exists()) {
            String msg = Messages.getString("resources.folder.notGet", folderName, source.getName()); //$NON-NLS-1$
            throw new ResourceNotFoundException(msg);
        }
        return processFolder;
    }

    public static void createFolder(IFolder folder) throws PersistenceException {
        try {
            if (!folder.getParent().exists()) {
                if (folder.getParent() instanceof IFolder) {
                    createFolder((IFolder) folder.getParent());
                }
            }
            folder.create(true, true, null);
        } catch (CoreException e) {
            String msg = Messages.getString("resources.folder.notCreated", folder.getName()); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        }
    }

    public static void deleteResource(IResource resource) throws PersistenceException {
        try {
            resource.delete(true, null);
        } catch (CoreException e) {
            String msg = Messages.getString("resources.folder.notDeleted", resource.getName()); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        }
    }

    public static int emptyFolder(IFolder folder) throws PersistenceException {
        int nbResourcesDeleted = 0;
        try {
            for (IResource res : folder.members()) {
                try {
                    deleteResource(res);
                    nbResourcesDeleted++;
                } catch (PersistenceException e) {
                    CommonExceptionHandler.process(e);
                }
            }
            return nbResourcesDeleted;
        } catch (CoreException e) {
            String msg = Messages.getString("resources.folder.notEmptied", folder.getName()); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        }
    }

    public static void moveResource(IResource res, IPath path) throws PersistenceException {
        try {
            res.move(path, true, null);
        } catch (CoreException e) {
            String msg = Messages.getString("resources.resource.notMoved", res.getName(), path); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        }
    }

    /**
     * Load a file from the specified folder.
     * 
     * @param source - the folder to looked for in
     * @param fileName - the file to retrieve
     * @param forceExists - if the file must exists
     * @return the file matching the fileName
     * @throws PersistenceException if a file with the specified name doesn't exist and forceExists is TRUE
     */
    public static IFile getFile(IFolder source, String fileName, boolean forceExists) throws PersistenceException {
        IFile file = source.getFile(fileName);
        if (forceExists && !file.exists()) {
            String msg = Messages.getString("resources.file.notGet", fileName, source.getName()); //$NON-NLS-1$
            throw new ResourceNotFoundException(msg);
        }
        return file;
    }

    public static IResource[] getMembers(IContainer container) throws PersistenceException {
        try {
            IResource[] members = container.members();
            return members;
        } catch (CoreException e) {
            String msg = Messages.getString("resources.members.notGet", container.getName()); //$NON-NLS-1$
            PersistenceException ex = new PersistenceException(msg, e);
            throw ex;
        }
    }

    public static IFile getFileMember(IContainer container, IPath path) throws PersistenceException {
        return container.getFile(path);
    }

    public static IResource getResourceMember(IContainer container, IPath path) throws PersistenceException {
        return container.findMember(path);
    }

    public static IFolder[] getSubMembers(IContainer container) throws PersistenceException {
        IResource[] members = getMembers(container);
        List<IFolder> toReturn = new ArrayList<IFolder>();
        for (IResource res : members) {
            if (res instanceof IFolder) {
                toReturn.add((IFolder) res);
            }
        }
        return toReturn.toArray(new IFolder[] {});

    }

    /**
     * Create the file and close the stream.
     * 
     * @param stream
     * @param file
     * @throws PersistenceException
     */
    public static void createFile(InputStream stream, IFile file) throws PersistenceException {
        try {
            if (stream == null) {
                String msg = Messages.getString(
                        "resources.file.notCreated", file.getName(), Messages.getString("ResourceUtils.streamNull")); //$NON-NLS-1$ //$NON-NLS-2$ 
                throw new PersistenceException(msg);
            }
            file.create(stream, true, null);
        } catch (CoreException e) {
            String msg = Messages.getString("resources.file.notCreated", file.getName(), e.getMessage()); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                CommonExceptionHandler.process(e);
            }
        }
    }

    /**
     * Convenience method to delete a file.<br/>
     * 
     * @param file - the file to delete
     * @throws PersistenceException - if the file canoot be deleted
     */
    public static void deleteFile(IFile file) throws PersistenceException {
        try {
            file.delete(true, false, null);
        } catch (CoreException e) {
            String msg = Messages.getString("resources.file.notDeleted", file.getName()); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        }
    }

    public static void deleteRevisionFile(IFile file) throws PersistenceException {
        try {
            file.delete(false, true, null);
        } catch (CoreException e) {
            String msg = Messages.getString("resources.file.notDeleted", file.getName()); //$NON-NLS-1$
            throw new PersistenceException(msg, e);
        }
    }

    public static InputStream getContent(IFile file) throws PersistenceException {
        try {
            return file.getContents();
        } catch (CoreException e) {
            String msg = Messages.getString("resources.fileContent.notGet", file.getName()); //$NON-NLS-1$
            PersistenceException ex = new PersistenceException(msg, e);
            throw ex;
        }
    }

    /**
     * Use this method to test if two pathes are compatible for a move.<br/>
     * 
     * Test mainly if pathes aren't equals and if source isn't a descendant of source (second test only if forFolder is
     * <code>false</code>.<br/>
     * 
     * <i>Example : /aaa/ cannot be moved into /aaa/bbb/ but /aaa/file.xml can be moved in /aaa/bbb/file.xml</i>
     * 
     * @param source - the source path
     * @param target - the target path
     * @param forFolder - <code>true</code> if the test is for a folder move or <code>false</code> if it's for a file
     * move.
     * @return
     */
    public static boolean isCorrectDestination(IPath source, IPath target, boolean forFolder) {
        // Rule 1 : Test if source & target are differents
        if (source.equals(target)) {
            log.trace(Messages.getString("ResourceUtils.cnanotMoveRule1", source, target)); //$NON-NLS-1$
            return false;
        }

        // Rule 2 : target is the root
        if (target.segmentCount() == 0) {
            log.trace(Messages.getString("ResourceUtils.canMove1") + source + Messages.getString("ResourceUtils.go") + target); //$NON-NLS-1$ //$NON-NLS-2$
            return true;
        }

        // Test if target isn't a descendant of the source :
        if (forFolder) {
            int common = source.matchingFirstSegments(target);
            // Rule 3 : Descendant
            if (common == source.segmentCount()) {
                log.trace(Messages.getString("ResourceUtils.cannotMoveRule3", source, target)); //$NON-NLS-1$
                return false;
            }

            // Rule 4 : Identical last segment
            // if (source.lastSegment().equals(target.lastSegment())) {
            // log.trace("Cannot move " + source + " -> " + target + " (Rule 4-Identical last segment)");
            // return false;
            // }
        }

        log.trace(Messages.getString("ResourceUtils.canMove2", source, target)); //$NON-NLS-1$
        return true;
    }

}
