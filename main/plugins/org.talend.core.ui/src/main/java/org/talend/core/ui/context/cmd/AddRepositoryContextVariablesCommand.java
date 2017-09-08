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
package org.talend.core.ui.context.cmd;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class AddRepositoryContextVariablesCommand extends Command {

    private IProgressMonitor monitor;

    private IContextModelManager modelManager;

    private ContextManagerHelper helper;

    private final List<ContextParameterType> parameterList;

    private IContextManager manager;

    public AddRepositoryContextVariablesCommand(IProgressMonitor monitor, IContextModelManager modelManager,
            ContextManagerHelper helper, final List<ContextParameterType> parameterList) {
        super();
        this.monitor = monitor;
        this.modelManager = modelManager;
        this.helper = helper;
        this.parameterList = parameterList;
        if (modelManager != null) {
            this.manager = modelManager.getContextManager();
        }
    }

    @Override
    public void execute() {
        if (modelManager == null || helper == null || parameterList == null || parameterList.isEmpty() || manager == null) {
            return;
        }
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }

        for (ContextParameterType defaultContextParamType : parameterList) {
            ContextItem contextItem = (ContextItem) helper.getParentContextItem(defaultContextParamType);
            if (contextItem == null) {
                continue;
            }

            IContextParameter paramExisted = helper.getExistedContextParameter(defaultContextParamType.getName());
            if (paramExisted != null) {
                // existed.
                if (!paramExisted.isBuiltIn() && contextItem.getProperty().getId().equals(paramExisted.getSource())) {
                    // update the parameter.
                    modelManager.onContextRemoveParameter(manager, defaultContextParamType.getName(), paramExisted.getSource());
                    helper.addContextParameterType(defaultContextParamType);

                }
            } else {
                // add the context
                helper.addContextParameterType(defaultContextParamType);

            }
            monitor.worked(1);
        }
    }

}
