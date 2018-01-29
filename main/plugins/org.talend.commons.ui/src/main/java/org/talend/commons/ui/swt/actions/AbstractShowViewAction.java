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
package org.talend.commons.ui.swt.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id: AbstractShowViewAction.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public abstract class AbstractShowViewAction extends Action {

    public AbstractShowViewAction() {
        super();
        this.setActionDefinitionId(getDefinitionId());
    }

    public abstract String getDefinitionId();

    public abstract String getViewId();

    @Override
    public void run() {
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
        try {
            page.showView(getViewId());
        } catch (PartInitException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }
    }
}
