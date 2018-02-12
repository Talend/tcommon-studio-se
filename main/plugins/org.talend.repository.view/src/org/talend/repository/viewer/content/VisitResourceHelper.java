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
package org.talend.repository.viewer.content;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.model.general.TalendNature;
import org.talend.core.repository.constants.FileConstants;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class VisitResourceHelper {

    private final IResourceDelta srcDelta;

    public VisitResourceHelper(IResourceDelta srcDelta) {
        super();
        this.srcDelta = srcDelta;
    }

    protected IResourceDelta getSrcDelta() {
        return this.srcDelta;
    }

    public boolean valid(IPath topLevelNodeWorkspaceRelativePath, boolean refMerged) {
        // if it's talend.project, will do for all nodes to refresh.
        if (isTalendProjectFile(getSrcDelta())) {
            return true;
        }

        // ignore
        if (isIgnoredResource(getSrcDelta())) {
            return false;
        }

        //
        if (validResourcePath(getSrcDelta(), topLevelNodeWorkspaceRelativePath, refMerged)) {
            return true;
        }
        return false;
    }

    protected boolean isTalendProjectFile(IResourceDelta delta) {
        IResource resource = delta.getResource();
        if (resource != null && resource instanceof IFile) {
            IPath path = resource.getFullPath();
            if (path != null && path.lastSegment() != null) {
                return FileConstants.LOCAL_PROJECT_FILENAME.equals(path.lastSegment());
            }
        }
        return false;
    }

    public boolean isIgnoredResource(IResourceDelta delta) {
        IResource resource = delta.getResource();
        try {
            if (resource != null) {
                if (resource.getProject() == null) {
                    // must be workspace root, then must check the children resources affected.
                    for (IResourceDelta childResourceDelta : delta.getAffectedChildren()) {
                        IResource childResource = childResourceDelta.getResource();
                        // if not under talend project. ignore
                        if (childResource.getProject() != null && childResource.getProject().hasNature(TalendNature.ID)) {
                            return false; // only validate if there is a talend project here.
                        }
                    }
                    return true;
                } else {
                    // if not under talend project. ignore
                    if (!resource.getProject().hasNature(TalendNature.ID)) {
                        return true; // if not talend project, ignore.
                    }
                    // if under svn folder, ignore
                    if (FilesUtils.isSVNFolder(resource)) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        } catch (CoreException e) {
            return false;
        }
        return false;
    }

    private boolean validResourcePath(IResourceDelta delta, IPath topLevelNodeWorkspaceRelativePath, boolean refMerged) {
        if (isIgnoredResource(delta)) {
            return false;
        }
        IResourceDelta[] affectedChildren = delta.getAffectedChildren();

        IResource res = delta.getResource();
        if (res instanceof IProject && delta.getKind() == IResourceDelta.REMOVED) {
            return false;
        }
        IPath path = delta.getFullPath();

        boolean noChild = (affectedChildren == null || (affectedChildren.length == 0));

        // check for the empty folder, except the ".svn"
        if (!noChild
                && affectedChildren != null
                && affectedChildren.length == 1
                && (FilesUtils.isSVNFolder(affectedChildren[0].getResource()) || affectedChildren[0].getResource().getName()
                        .equals(org.talend.utils.io.FilesUtils.GITKEEP))) {
            noChild = true;
        }

        // be sure we are the last path of the resources and then check for the right folder and then check for file of
        // type .properties or folder
        if (path != null && noChild && (isValidFileResource(res) || (res instanceof IContainer))) {
            if (isMatchedPath(topLevelNodeWorkspaceRelativePath, path)) {
                return true;
            } else if (refMerged) { // if merged
                // remove the project segment
                if (res != null) {
                    path = res.getProjectRelativePath();
                } else {
                    path = path.removeFirstSegments(1);
                }
                IPath relativePath = topLevelNodeWorkspaceRelativePath.removeFirstSegments(1);
                if (isMatchedPath(relativePath, path)) {
                    return true;
                }
            }
        }

        if (affectedChildren != null) {
            for (IResourceDelta child : affectedChildren) {
                if (validResourcePath(child, topLevelNodeWorkspaceRelativePath, refMerged)) {
                    return true;
                }
            }
        }

        return false;
    }

    protected boolean isValidFileResource(IResource res) {
        return FileConstants.PROPERTIES_EXTENSION.equals(res.getFileExtension());
    }

    private boolean isMatchedPath(IPath topLevelNodeWorkspaceRelativePath, IPath path) {
        int matchingFirstSegments = path.matchingFirstSegments(topLevelNodeWorkspaceRelativePath);
        if (matchingFirstSegments > 0
                && (path.segmentCount() == matchingFirstSegments || matchingFirstSegments == topLevelNodeWorkspaceRelativePath
                        .segmentCount())) {
            return true;
        }
        return false;
    }

}
