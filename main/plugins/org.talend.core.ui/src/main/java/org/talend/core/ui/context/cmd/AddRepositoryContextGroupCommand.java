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
package org.talend.core.ui.context.cmd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.context.JobContext;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.editor.command.ContextRemoveParameterCommand;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AddRepositoryContextGroupCommand extends Command {

    private IProgressMonitor monitor;

    private IContextManager manager;

    private IContextModelManager modelManager;

    private final Set<String> nameSet;

    private final List<ContextParameterType> parameterList;

    private List<ContextItem> selectedItems;

    private ContextManagerHelper helper;

    private List<ContextParameterType> newAddParameter = new ArrayList<ContextParameterType>();

    private List<ContextRemoveParameterCommand> cmds = new ArrayList<ContextRemoveParameterCommand>();

    public AddRepositoryContextGroupCommand(IProgressMonitor monitor, IContextModelManager modelManager,
            final List<ContextItem> selectedItems, final Set<String> nameSet, ContextManagerHelper helper,
            final List<ContextParameterType> parameterList) {
        super();
        this.monitor = monitor;
        this.modelManager = modelManager;
        if (modelManager != null) {
            this.manager = modelManager.getContextManager();
        }
        this.selectedItems = selectedItems;
        this.nameSet = nameSet;
        this.parameterList = parameterList;
        this.helper = helper;
    }

    @Override
    public void execute() {
        // for Group
        if (modelManager == null || manager == null || selectedItems == null || selectedItems.isEmpty() || nameSet == null) {
            return;
        }
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        IContext defaultContext = manager.getDefaultContext();

        // specially process the context group(bug 4025)
        boolean special = false;
        ContextItem item = null;
        if (defaultContext.getContextParameterList().isEmpty() && selectedItems.size() == 1) {
            special = true;
            item = selectedItems.get(0);
            List<String> contextNames = new ArrayList<String>();
            for (ContextType context : (List<ContextType>) item.getContext()) {
                String repContextName = context.getName();
                if (repContextName != null)
                    contextNames.add(repContextName.toLowerCase());
            }
            Iterator<IContext> iterator = manager.getListContext().iterator();
            // TODO Remove the contexts except "default" and matched to repository without case sensitive.

            while (iterator.hasNext()) {
                String name = iterator.next().getName();
                // if not select new default context, will not delete the default context for job.
                if (name != null) {
                    if (contextNames.contains(name.toLowerCase())
                            || (!isJobContextExistInRepository(nameSet, item.getDefaultContext()) && name.toLowerCase().equals(
                                    defaultContext.getName().toLowerCase()))) {
                        continue;
                    }

                }
                iterator.remove();
            }
            monitor.worked(1);
        }
        //
        IContext newDefaultContext = null;
        for (String name : nameSet) {
            JobContext newContext = new JobContext(name);

            List<IContextParameter> newParamList = new ArrayList<IContextParameter>();
            newContext.setContextParameterList(newParamList);
            JobContextParameter param;
            for (IContextParameter parameter : defaultContext.getContextParameterList()) {
                param = (JobContextParameter) parameter.clone();
                param.setContext(newContext);
                newParamList.add(param);
            }
            manager.getListContext().add(newContext);
            if (special && item != null) {
                if (name.equals(item.getDefaultContext())) {
                    newDefaultContext = newContext;
                }
            }
            monitor.worked(1);
        }
        if (special) {
            if (newDefaultContext != null) { // have selected new default context
                modelManager.onContextChangeDefault(manager, newDefaultContext);
            } else { // nameSet is empty
                IContext context = manager.getContext(item.getDefaultContext());
                if (context != null) {
                    modelManager.onContextChangeDefault(manager, context);
                }
            }
            monitor.worked(1);
        }

        // for Parameter
        if (modelManager == null || helper == null || parameterList == null || manager == null) {
            return;
        }
        newAddParameter.clear();
        List<IContextParameter> existParas = new ArrayList<>(defaultContext.getContextParameterList());
        for (ContextParameterType defaultContextParamType : parameterList) {
            ContextItem contextItem = (ContextItem) helper.getParentContextItem(defaultContextParamType);
            if (contextItem == null) {
                continue;
            }

            IContextParameter paramExisted = helper.getExistedContextParameter(defaultContextParamType.getName());
            if (paramExisted != null) {
                existParas.remove(paramExisted);
                // existed.
                if (!paramExisted.isBuiltIn() && contextItem.getProperty().getId().equals(paramExisted.getSource())) {
                    // update the parameter.
                    // modelManager.onContextRemoveParameter(manager, defaultContextParamType.getName(),
                    // paramExisted.getSource());
                    new ContextRemoveParameterCommand(manager, defaultContextParamType.getName(), paramExisted.getSource())
                            .execute();
                    helper.addContextParameterType(defaultContextParamType);

                }
            } else {
                // add the context
                helper.addContextParameterType(defaultContextParamType);
                newAddParameter.add(defaultContextParamType);
            }
            monitor.worked(1);
        }
        
        //remove the params which is unchecked
        for(IContextParameter param : existParas){
            if (param.isBuiltIn()){
                continue;
            }
            new ContextRemoveParameterCommand(manager, param.getName(), param.getSource())
            .execute();
        }
        
    }

    /**
     * Added by Marvin Wang on Jun.6, 2012 for judging if the default context group exist in the set.
     */
    private boolean isJobContextExistInRepository(Set<String> nameSet, String defaultContextName) {
        Iterator<String> it = nameSet.iterator();
        while (it.hasNext()) {
            String selectedName = it.next();
            if (selectedName != null && defaultContextName != null) {
                if (selectedName.toLowerCase().equals(defaultContextName.toLowerCase()))
                    return true;
            }
        }
        return false;
    }

    @Override
    public void redo() {
        // execute();
        for (ContextRemoveParameterCommand cmd : cmds) {
            if (cmd.canUndo()) {
                cmd.undo();
            }
        }
        List<IContext> removeList = new ArrayList<IContext>();
        for (IContext con : manager.getListContext()) {
            if (nameSet.contains(con.getName())) {
                removeList.add(con);
            }
        }
        manager.getListContext().addAll(removeList);
        helper.refreshContextView();
    }

    @Override
    public void undo() {
        List<IContext> removeList = new ArrayList<IContext>();
        for (IContext con : manager.getListContext()) {
            if (nameSet.contains(con.getName())) {
                removeList.add(con);
            }
        }
        manager.getListContext().removeAll(removeList);
        cmds.clear();
        for (ContextParameterType defaultContextParamType : newAddParameter) {
            ContextItem contextItem = (ContextItem) helper.getParentContextItem(defaultContextParamType);
            if (contextItem == null) {
                continue;
            }

            IContextParameter paramExisted = helper.getExistedContextParameter(defaultContextParamType.getName());
            if (paramExisted != null) {
                // modelManager.onContextRemoveParameter(manager, defaultContextParamType.getName(),
                // paramExisted.getSource());
                ContextRemoveParameterCommand contextRemoveParameterCommand = new ContextRemoveParameterCommand(manager,
                        defaultContextParamType.getName(), paramExisted.getSource());
                contextRemoveParameterCommand.execute();
                cmds.add(contextRemoveParameterCommand);
            }
        }

        helper.refreshContextView();
    }

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

}
