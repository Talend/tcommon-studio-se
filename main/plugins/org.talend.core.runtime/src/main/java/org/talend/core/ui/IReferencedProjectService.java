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
package org.talend.core.ui;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IWorkbenchWindow;
import org.talend.core.IService;

/**
 * DOC wchen class global comment. Detailled comment
 */
public interface IReferencedProjectService extends IService {

    public boolean isMergeRefProject();

    public void addMergeAction(IWorkbenchWindow window, IToolBarManager toolBar);
}
