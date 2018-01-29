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
package org.talend.core.services;

import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.model.general.Project;

/**
 * nma class global comment. Detailled comment
 */
public interface ISVNProviderService extends IService {

    public boolean isProjectInSvnMode();

    public String getLastSVNRevision(Object process);

    public String getCurrentSVNRevision(Object process);

    public String[] getBranchList(Project project);

    public boolean isSVNProject(Project p) throws PersistenceException;

    public void svnEclipseHandlerDelete(IProject eclipseProject, Project currentProject, String filePath);

    public String getProjectUrl(Project project);

    public void clean();
}
