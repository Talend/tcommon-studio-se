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
package org.talend.core.ui.editor.command;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.i18n.Messages;

/**
 * Command that will change the default context.
 * 
 * $Id$
 * 
 */
public class ContextChangeDefaultCommand extends Command {

    IContext newDefault;

    IContext oldDefault;

    IContextManager contextManager;

    ContextManagerHelper helper;

    public ContextChangeDefaultCommand(IContextManager contextManager, IContext newDefault) {
        this.newDefault = newDefault;
        this.contextManager = contextManager;
        this.oldDefault = contextManager.getDefaultContext();
        this.helper = new ContextManagerHelper(contextManager);
        this.setLabel(Messages.getString("ContextChangeDefaultCommand.label")); //$NON-NLS-1$
    }

    /**
     * qzhang Comment method "refreshContextView".
     */
    // private void refreshContextView() {
    // IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    // // refresh context view of DI
    // IViewPart view = page.findView(AbstractContextView.CTX_ID_DESIGNER);
    // if (view instanceof AbstractContextView) {
    // ((AbstractContextView) view).updateContextView(true, false);
    // }
    // // refresh context view of DQ
    // if (GlobalServiceRegister.getDefault().isServiceRegistered(ITdqUiService.class)) {
    // ITdqUiService tdqUiService = (ITdqUiService) GlobalServiceRegister.getDefault().getService(ITdqUiService.class);
    // if (tdqUiService != null) {
    // tdqUiService.updateContextView(true, false);
    // }
    // }
    // }

    @Override
    public void execute() {
        contextManager.setDefaultContext(newDefault);
        contextManager.fireContextsChangedEvent();
        this.helper.refreshContextView();
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        contextManager.setDefaultContext(oldDefault);
        contextManager.fireContextsChangedEvent();
        // refreshContextView();
    }
}
