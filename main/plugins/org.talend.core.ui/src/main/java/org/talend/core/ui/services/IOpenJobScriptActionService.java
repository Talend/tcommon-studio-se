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
package org.talend.core.ui.services;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.talend.core.IService;
import org.talend.core.model.properties.Item;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public interface IOpenJobScriptActionService extends IService {

    public Action getOpenJobScriptAction(IWorkbenchWindow window);

    public IFile createWorkspaceLink(IProject fsProject, Item item) throws CoreException;
}
