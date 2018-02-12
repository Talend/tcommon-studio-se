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
package org.talend.repository.localprovider.model;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;

/**
 * Provides utilities methods relative to model on IResource.<br/>
 * 
 * $Id$
 * 
 */
public final class LocalResourceModelUtils {

    private LocalResourceModelUtils() {
    }

    public static IFolder getFolder(Project project, ERepositoryObjectType type) throws PersistenceException {
        IProject fsProject = ResourceUtils.getProject(project);
        return ResourceUtils.getFolder(fsProject, ERepositoryObjectType.getFolderName(type), true);
    }
}
