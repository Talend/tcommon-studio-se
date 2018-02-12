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
package org.talend.repository.viewer.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.repository.model.ProjectRepositoryNode;
import org.talend.repository.navigator.RepoViewCommonNavigator;

/**
 * 
 * DOC ggu RepoRefreshAction class global comment. Detailled comment
 */
public class RepoRefreshAction implements IViewActionDelegate {

    RepoViewCommonNavigator view;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    @Override
    public void run(IAction action) {
        // FIXME maybe later,will remove this flag
        ProjectRepositoryNode.refProjectBool = true;

        view.refreshView();
        ProjectRepositoryNode.refProjectBool = false;
        // qli modified to fix the bug 6659.
        RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.ROUTINES);
        RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.PIG_UDF);
        RepositoryManager.syncRoutineAndJoblet(ERepositoryObjectType.JOBLET);
        RepositoryManager.syncUserComponents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     * org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        // nothing to do when selection changes.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
     */
    @Override
    public void init(IViewPart theView) {
        view = (RepoViewCommonNavigator) theView;
    }

}
