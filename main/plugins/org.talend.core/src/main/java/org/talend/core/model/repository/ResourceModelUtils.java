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
package org.talend.core.model.repository;

import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.model.general.Project;

/**
 * Provides utilities methods relative to model on IResource. <br/>
 * 
 * $Id: ResourceModelUtils.java 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public class ResourceModelUtils {

    /**
     * Load a project (IResource speaking) in the current workspace from a project (model speaking).
     * 
     * @param project - the project to retrieve
     * @return the IProject matching the project
     * @throws PersistenceException if the IProject cannot be retrieve
     */
    public static IProject getProject(Project project) throws PersistenceException {
        return ResourceUtils.getProject(project.getTechnicalLabel());
    }
}
