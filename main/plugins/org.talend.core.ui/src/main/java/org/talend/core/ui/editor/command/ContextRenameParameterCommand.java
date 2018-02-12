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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.i18n.Messages;

/**
 * Command that will rename the parameter in all contexts. <br/>
 * 
 * $Id$
 * 
 */
public class ContextRenameParameterCommand extends Command {

    String oldName, newName;

    IContextManager contextManager;

    private String sourceId;

    private ContextManagerHelper helper;

    public ContextRenameParameterCommand(IContextManager contextManager, String sourceId, String oldName, String newName) {
        this(contextManager, oldName, newName);
        this.sourceId = sourceId;

    }

    public ContextRenameParameterCommand(IContextManager contextManager, String oldName, String newName) {
        this.contextManager = contextManager;
        this.oldName = oldName;
        this.newName = newName;
        this.helper = new ContextManagerHelper(contextManager);
        setLabel(Messages.getString("ContextRenameParameterCommand.renameParameter")); //$NON-NLS-1$
    }

    /**
     * qzhang Comment method "refreshContextView".
     */
    // private void refreshContextView() {
    // IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    // // refresh context view of DI
    // IViewPart view = page.findView(AbstractContextView.CTX_ID_DESIGNER);
    // if (view instanceof AbstractContextView) {
    // ((AbstractContextView) view).updateContextView(true, false, false);
    // }
    // // refresh context view of DQ
    // if (GlobalServiceRegister.getDefault().isServiceRegistered(ITdqUiService.class)) {
    // ITdqUiService tdqUiService = (ITdqUiService) GlobalServiceRegister.getDefault().getService(ITdqUiService.class);
    // if (tdqUiService != null) {
    // tdqUiService.updateContextView(true, false, false);
    // }
    // }
    // }

    @Override
    public void execute() {
        boolean found;
        List<IContextParameter> listParams;

        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                IContextParameter contextParameter = listParams.get(j);
                String tempName = contextParameter.getName();
                String tempSourceId = contextParameter.getSource();
                if (tempName.equals(oldName) && tempSourceId.equals(sourceId)) {
                    contextParameter.setName(newName);
                    // see 0003889: Context script code not refreshed.
                    String scriptCode = contextParameter.getScriptCode().replaceAll(oldName, newName);
                    contextParameter.setScriptCode(scriptCode);
                    // if the user haven't modified prompt, change it
                    if (contextParameter.getPrompt().equals(oldName + "?")) { //$NON-NLS-1$
                        contextParameter.setPrompt(newName + "?"); //$NON-NLS-1$
                    }

                    found = true;
                }
            }
        }
        contextManager.fireContextsChangedEvent();
        this.helper.refreshContextViewForRename();
    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public void undo() {
        boolean found;
        List<IContextParameter> listParams;

        for (int i = 0; i < contextManager.getListContext().size(); i++) {
            listParams = contextManager.getListContext().get(i).getContextParameterList();
            found = false;
            for (int j = 0; j < listParams.size() && !found; j++) {
                IContextParameter contextParameter = listParams.get(j);
                String name = contextParameter.getName();
                String tempSourceId = contextParameter.getSource();
                if (name.equals(newName) && tempSourceId.equals(sourceId)) {
                    contextParameter.setName(oldName);
                    // see 0003889: Context script code not refreshed.
                    String scriptCode = contextParameter.getScriptCode().replaceAll(newName, oldName);
                    contextParameter.setScriptCode(scriptCode);
                    // if the user haven't modified prompt, change it
                    if (contextParameter.getPrompt().equals(newName + "?")) { //$NON-NLS-1$
                        contextParameter.setPrompt(oldName + "?"); //$NON-NLS-1$
                    }
                    found = true;
                }
            }
        }
        this.helper.refreshContextViewForRename();
    }
}
