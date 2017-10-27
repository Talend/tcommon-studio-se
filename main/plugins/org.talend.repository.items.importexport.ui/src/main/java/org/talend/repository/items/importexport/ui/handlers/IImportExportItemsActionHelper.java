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
package org.talend.repository.items.importexport.ui.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IImportExportItemsActionHelper {

    /**
     * 
     * DOC ggu Comment method "isImportEnabled".
     * 
     * Work for the method init of ImportItemsAction.
     * 
     * @param viewer, current repository view.
     * @param selection, the selection of repository view.
     * @return if all actions return false, will disable the action. If there is one action to return true, will enable
     * it.
     */
    boolean isImportEnabled(TreeViewer viewer, ISelection selection);

    boolean isImportEnabled(IAction action, ISelection selection);

    /**
     * 
     * DOC ggu Comment method "isExportEnabled".
     * 
     * @param viewer, current repository view.
     * @param selection, the selection of repository view.
     * @return if all actions return false, will disable the action. If there is one action to return true, will enable
     * it.
     */
    boolean isExportEnabled(TreeViewer viewer, ISelection selection);

    boolean isExportEnabled(IAction action, ISelection selection);
}
