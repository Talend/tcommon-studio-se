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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.context.ContextUtils;
import org.talend.core.model.context.JobContextParameter;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.editor.command.ContextRemoveParameterCommand;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class CheckAndAddContextVariablesCommand extends Command {

    private IContextManager ctxManager = null;

    private List<ContextType> contexts = new ArrayList<ContextType>();

    private Set<String> neededVars = new HashSet<String>();

    private String defaultContextName = null;

    private boolean added;

    private String contextItemId = null;

    private Set<String> addedVars = new HashSet<String>();

    private List<ContextParameterType> newAddParameter = new ArrayList<ContextParameterType>();

    private ContextManagerHelper helper;

    public CheckAndAddContextVariablesCommand(final List<ContextType> contexts, final String defaultContextName,
            final String contextItemId, final Set<String> neededVars, final IContextManager ctxManager, boolean added) {
        this.contexts = contexts;
        this.defaultContextName = defaultContextName;
        this.contextItemId = contextItemId;
        this.neededVars = neededVars;
        this.ctxManager = ctxManager;
        this.added = added;
        this.helper = new ContextManagerHelper(ctxManager);
    }

    public void execute() {
        Set<String> addedVars = new HashSet<String>();
        for (IContext context : ctxManager.getListContext()) {

            ContextType type = ContextUtils.getContextTypeByName(contexts, context.getName(), defaultContextName);
            if (type != null) {
                for (String var : neededVars) {
                    if (context.getContextParameter(var) != null) {
                        continue;
                    }
                    ContextParameterType param = ContextUtils.getContextParameterTypeByName(type, var);
                    newAddParameter.add(param);
                    if (param != null) {
                        //
                        if (added) {
                            JobContextParameter contextParam = new JobContextParameter();

                            ContextUtils.updateParameter(param, contextParam);
                            if (contextItemId != null) {
                                contextParam.setSource(contextItemId);
                            }
                            contextParam.setContext(context);

                            context.getContextParameterList().add(contextParam);
                        }
                        addedVars.add(var);
                    }
                }
            }
        }
    }

    public void redo() {
        execute();
        this.helper.refreshContextView();
    }

    public void undo() {

        ContextManagerHelper helper = new ContextManagerHelper(ctxManager);
        List<IContext> removeList = new ArrayList<IContext>();
        for (IContext con : ctxManager.getListContext()) {
            if (addedVars.contains(con.getName())) {
                removeList.add(con);
            }
        }
        ctxManager.getListContext().removeAll(removeList);

        for (ContextParameterType defaultContextParamType : newAddParameter) {
            ContextItem contextItem = (ContextItem) helper.getParentContextItem(defaultContextParamType);
            if (contextItem == null) {
                continue;
            }

            IContextParameter paramExisted = helper.getExistedContextParameter(defaultContextParamType.getName());
            if (paramExisted != null) {
                // modelManager.onContextRemoveParameter(manager, defaultContextParamType.getName(),
                // paramExisted.getSource());
                new ContextRemoveParameterCommand(ctxManager, defaultContextParamType.getName(), paramExisted.getSource())
                        .execute();
            }
        }
        helper.refreshContextView();

    }

}
