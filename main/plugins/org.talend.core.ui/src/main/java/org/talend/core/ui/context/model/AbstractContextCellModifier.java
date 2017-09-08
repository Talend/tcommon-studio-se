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
package org.talend.core.ui.context.model;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ICellModifier;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.AbstractContextTabEditComposite;
import org.talend.core.ui.context.IContextModelManager;

/**
 * cli class global comment. Detailled comment.
 */
public abstract class AbstractContextCellModifier implements ICellModifier {

    protected boolean repositoryFlag = false;

    protected AbstractContextTabEditComposite parentMode;

    public AbstractContextCellModifier(AbstractContextTabEditComposite parentMode, boolean repositoryFlag) {
        super();
        this.parentMode = parentMode;
        this.repositoryFlag = repositoryFlag;
    }

    protected AbstractContextTabEditComposite getParentMode() {
        return this.parentMode;
    }

    public boolean isRepositoryMode() {
        return this.repositoryFlag;
    }

    protected IContextModelManager getModelManager() {
        return parentMode.getContextModelManager();
    }

    protected IContextManager getContextManager() {
        return parentMode.getContextModelManager().getContextManager();
    }

    protected void updateRelatedNode(final Object[] objs, final IContextParameter param) {
        if (objs != null && objs.length > 0) {
            Command command = updateRelatedNodeCommand(objs, null);
            getParentMode().runCommand(command);
        }
        // set updated flag.
        if (param != null) {
            IContextManager manager = getContextManager();
            if (manager != null && manager instanceof JobContextManager) {
                JobContextManager jobContextManager = (JobContextManager) manager;
                // not added new
                if (!getModelManager().isRepositoryContext() || getModelManager().isRepositoryContext()
                        && jobContextManager.isOriginalParameter(param.getName())) {
                    jobContextManager.setModified(true);
                    manager.fireContextsChangedEvent();
                }
            }
        }
    }

    private Command updateRelatedNodeCommand(final Object[] objs, final String[] properties) {
        return new Command() {

            public void execute() {
                getParentMode().getViewer().update(objs, properties);
            }
        };
    }
}
