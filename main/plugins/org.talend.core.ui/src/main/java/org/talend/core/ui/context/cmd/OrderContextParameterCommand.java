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

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;

/**
 * ggu class global comment. Detailled comment
 */
public class OrderContextParameterCommand extends Command {

    private boolean up = false;

    private IContextParameter movedParam;

    private IContextManager manager;

    private boolean execution = false;

    public OrderContextParameterCommand(IContextManager manager, IContextParameter movedParam, boolean up) {
        super();
        this.up = up;
        this.movedParam = movedParam;
        this.manager = manager;
    }

    public boolean isExecution() {
        return this.execution;
    }

    @Override
    public void execute() {
        if (this.manager == null || this.movedParam == null) {
            return;
        }
        int index = -1;
        final IContext defaultContext = this.manager.getDefaultContext();
        for (int i = 0; i < defaultContext.getContextParameterList().size(); i++) {
            IContextParameter param = defaultContext.getContextParameterList().get(i);
            if (param.getName().equals(movedParam.getName())) {
                index = i;
                break;
            }
        }
        if (index == -1) { // not found
            execution = false;
            return;
        }

        List<IContext> listContext = this.manager.getListContext();
        for (IContext context : listContext) {
            if (!orderContextParameters(context, index, up)) {
                execution = false;
                return;
            }
        }
        execution = true;
    }

    private boolean orderContextParameters(IContext context, int index, boolean up) {
        if (context == null || index < 0) {
            return false;
        }
        List<IContextParameter> contextParameterList = context.getContextParameterList();
        final int size = contextParameterList.size();
        if (index > size - 1) {
            return false;
        }

        final IContextParameter movedParameter = contextParameterList.get(index);
        contextParameterList.remove(index);
        if (up && index == 0) { // up and first of element

            contextParameterList.add(movedParameter);
        } else if (!up && index == size - 1) { // down and end of element

            contextParameterList.add(0, movedParameter);
        } else { // normal
            for (int i = 0; i < size; i++) {
                if (i == index) {
                    if (up) {
                        contextParameterList.add(index - 1, movedParameter);
                    } else {
                        contextParameterList.add(index + 1, movedParameter);
                    }
                    break;
                }
            }
        }
        return true;
    }

}
