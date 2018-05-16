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
package org.talend.helpers.ui.actions;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.contexts.IContextService;
import org.talend.helpers.HelpersPlugin;
import org.talend.helpers.i18n.Messages;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LogActiveContextsAction extends Action implements IWorkbenchWindowActionDelegate {

    private static Logger log = Logger.getLogger(LogActiveContextsAction.class);

    public LogActiveContextsAction() {
        super();
        this.setActionDefinitionId("logActiveContexts"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        IContextService contextService = (IContextService) HelpersPlugin.getDefault().getWorkbench().getAdapter(
                IContextService.class);
        Collection col = contextService.getActiveContextIds();
        for (Object o : col) {
            log.info(Messages.getString("LogActiveContextsAction.activeContextLog", o)); //$NON-NLS-1$
        }
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
    }

    public void run(IAction action) {
        run();
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
