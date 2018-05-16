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
package org.talend.repository.view.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepoViewRefreshHelper {

    protected List<String> resourceFolderList = new ArrayList<String>();

    public RepoViewRefreshHelper() {
        for (ERepositoryObjectType type : (ERepositoryObjectType[]) ERepositoryObjectType.values(ERepositoryObjectType.class)) {
            // must be resource folder
            if (type.isResouce() && type.hasFolder()) {
                resourceFolderList.add(type.getFolder());
            }
        }
    }

    public IFile getValidResourceFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(filePath.trim()));
        if (file != null && file.isAccessible()) {
            final IPath projectLocation = file.getProject().getLocation();
            final IPath fileLocation = file.getLocation();
            final IPath projectRelativePath = fileLocation.makeRelativeTo(projectLocation);
            if (!projectRelativePath.isEmpty() && resourceFolderList.contains(projectRelativePath.segment(0))) {
                return file;
            }
        }
        return null;
    }
}