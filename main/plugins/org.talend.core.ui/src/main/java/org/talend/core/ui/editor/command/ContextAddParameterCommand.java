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
package org.talend.core.ui.editor.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.i18n.Messages;

/**
 * Command that will add a new parameter in all contexts. <br/>
 * 
 * $Id$
 * 
 */
public class ContextAddParameterCommand extends Command {

    IContextManager contextManager;

    IContextParameter contextParam;

    Map<IContext, IContextParameter> addedParameters;

    ContextManagerHelper helper;

    public ContextAddParameterCommand(IContextManager contextManager, IContextParameter contextParam) {
        this.contextManager = contextManager;
        this.contextParam = contextParam;
        this.setLabel(Messages.getString("ContextAddParameterCommand.label")); //$NON-NLS-1$
        this.helper = new ContextManagerHelper(contextManager);
        addedParameters = new HashMap<IContext, IContextParameter>();
    }

    private void refreshPropertyView() {
        // IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        // IViewPart view = page.findView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
        // PropertySheet sheet = (PropertySheet) view;
        // final IPage currentPage = sheet.getCurrentPage();
        // if (currentPage instanceof TabbedPropertySheetPage) {
        // TabbedPropertySheetPage tabbedPropertySheetPage = (TabbedPropertySheetPage) currentPage;
        // tabbedPropertySheetPage.refresh();
        // }
    }

    // /**
    // * qzhang Comment method "refreshContextView".
    // */
    // private void refreshContextView() {
    // IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    // // refresh context view of DI
    // IViewPart view = page.findView(AbstractContextView.CTX_ID_DESIGNER);
    // if (view instanceof AbstractContextView) {
    // ((AbstractContextView) view).updateContextView(true);
    // }
    // // refresh context view of DQ
    // if (GlobalServiceRegister.getDefault().isServiceRegistered(ITdqUiService.class)) {
    // ITdqUiService tdqUiService = (ITdqUiService) GlobalServiceRegister.getDefault().getService(ITdqUiService.class);
    // if (tdqUiService != null) {
    // tdqUiService.updateContextView(true);
    // }
    // }
    // }

    @Override
    public void execute() {
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            IContext context = contextManager.getListContext().get(i);
            IContextParameter toAdd = contextParam.clone();
            addedParameters.put(context, toAdd);
            toAdd.setContext(context);
            context.getContextParameterList().add(toAdd);
        }
        contextManager.fireContextsChangedEvent();
        this.helper.refreshContextView();
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            IContext context = contextManager.getListContext().get(i);
            context.getContextParameterList().remove(addedParameters.get(context));
        }
        contextManager.fireContextsChangedEvent();
        this.helper.refreshContextView();
    }
}
