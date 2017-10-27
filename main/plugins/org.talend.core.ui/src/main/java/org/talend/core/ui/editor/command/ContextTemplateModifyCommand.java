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
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.process.IProcess;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.i18n.Messages;

/**
 * Command that will modify a context.
 * 
 * $Id: ContextModifyCommand.java 3351 2007-05-04 12:14:00Z plegall $
 * 
 */
public class ContextTemplateModifyCommand extends Command {

    IContext tmpContext;

    IContextManager contextManager;

    IContextParameter template;

    private IProcess process;

    private ContextManagerHelper helper;

    public ContextTemplateModifyCommand(IProcess process, IContextManager contextManager, IContextParameter parameter) {
        this.process = process;
        this.contextManager = contextManager;
        this.helper = new ContextManagerHelper(contextManager);
        template = parameter;
        setLabel(Messages.getString("ContextModifyCommand.label")); //$NON-NLS-1$
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
        // check modified type
        propagateType(contextManager, template);
        contextManager.fireContextsChangedEvent();
        this.helper.refreshContextView();
        // TODO Removes the attached context files
        // try {
        // CorePlugin.getDefault().getRepositoryService().getProxyRepositoryFactory().removeContextFiles(process,
        // oldContext);
        // } catch (Exception e) {
        // ExceptionHandler.process(e);
        // }
    }

    private void propagateType(IContextManager contextManager, IContextParameter param) {
        for (IContext context : contextManager.getListContext()) {
            IContextParameter paramToModify = context.getContextParameter(param.getName());
            paramToModify.setType(param.getType());

            paramToModify.setComment(param.getComment());

        }
    }

    @Override
    public void redo() {
        // tmpContext = currentContext.clone();
        // currentContext.setName(oldContext.getName());
        // currentContext.setConfirmationNeeded(oldContext.isConfirmationNeeded());
        //
        // List<IContextParameter> oldListContextParam = oldContext.getContextParameterList();
        // List<IContextParameter> curListContextParam = currentContext.getContextParameterList();
        //
        // boolean found;
        // IContextParameter oldParam = null;
        // IContextParameter curParam;
        // String name = null;
        // for (int i = 0; i < oldListContextParam.size(); i++) {
        // found = false;
        // oldParam = oldListContextParam.get(i);
        // for (int j = 0; j < curListContextParam.size() && !found; j++) {
        // curParam = curListContextParam.get(j);
        // name = oldParam.getName();
        // if (curParam.getName().equals(name)) {
        // found = true;
        // curParam.setPromptNeeded(oldParam.isPromptNeeded());
        // curParam.setComment(oldParam.getComment());
        // curParam.setPrompt(oldParam.getPrompt());
        // curParam.setType(oldParam.getType());
        // curParam.setValue(oldParam.getValue());
        // }
        // }
        // }
        //
        // oldContext = tmpContext;
        // for (IContextParameter param : oldContext.getContextParameterList()) {
        // String paramName = param.getName();
        // IContextParameter newParam = currentContext.getContextParameter(paramName);
        // if (!newParam.getType().equals(param.getType())) {
        // propagateType(contextManager, newParam);
        // }
        // }
        //
        // contextManager.fireContextsChangedEvent();
        // refreshContextView();
    }

    @Override
    public void undo() {
        // tmpContext = currentContext.clone();
        // currentContext.setName(oldContext.getName());
        // currentContext.setConfirmationNeeded(oldContext.isConfirmationNeeded());
        //
        // List<IContextParameter> oldListContextParam = oldContext.getContextParameterList();
        // List<IContextParameter> curListContextParam = currentContext.getContextParameterList();
        //
        // boolean found;
        // IContextParameter oldParam = null;
        // IContextParameter curParam;
        // String name = null;
        // for (int i = 0; i < oldListContextParam.size(); i++) {
        // found = false;
        // oldParam = oldListContextParam.get(i);
        // for (int j = 0; j < curListContextParam.size() && !found; j++) {
        // curParam = curListContextParam.get(j);
        // name = oldParam.getName();
        // if (curParam.getName().equals(name)) {
        // found = true;
        // curParam.setPromptNeeded(oldParam.isPromptNeeded());
        // curParam.setComment(oldParam.getComment());
        // curParam.setPrompt(oldParam.getPrompt());
        // curParam.setType(oldParam.getType());
        // curParam.setValue(oldParam.getValue());
        // }
        // }
        // }
        // oldContext = tmpContext;
        // for (IContextParameter param : oldContext.getContextParameterList()) {
        // String paramName = param.getName();
        // IContextParameter newParam = currentContext.getContextParameter(paramName);
        // if (!newParam.getType().equals(param.getType())) {
        // propagateType(contextManager, newParam);
        // }
        // }
        //
        // contextManager.fireContextsChangedEvent();
        // refreshContextView();
    }
}
