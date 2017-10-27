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
package org.talend.core.ui.context.nattableTree;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.model.table.ContextTableTabChildModel;

/**
 * created by ldong on Aug 15, 2014 Detailled comment
 * 
 */
public class ContextParaChangeModeCommand extends Command {

    IContextParameter param;

    IContextModelManager modelManager;

    ContextTreeNode treeNode;

    public ContextParaChangeModeCommand(IContextModelManager modelManager, ContextTreeNode treeNode, IContextParameter param) {
        super();
        this.modelManager = modelManager;
        this.treeNode = treeNode;
        this.param = param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        boolean isChange = false;
        IContextModelManager manager = treeNode.getManager();
        if (treeNode.getTreeData() instanceof ContextTableTabChildModel) {
            ContextTableTabChildModel paraModel = (ContextTableTabChildModel) treeNode.getTreeData();
            String sourceId = paraModel.getSourceId();
            if (manager.getContextManager() != null) {
                if (!sourceId.equals(IContextParameter.BUILT_IN)) {
                    String paraName = paraModel.getContextParameter().getName();
                    List<IContext> contexts = manager.getContextManager().getListContext();
                    for (IContext envContext : contexts) {
                        List<IContextParameter> list = envContext.getContextParameterList();
                        if (list != null && list.size() > 0) {
                            for (IContextParameter contextPara : list) {
                                String tempContextParaName = contextPara.getName();
                                if (tempContextParaName.equals(paraName)) {
                                    contextPara.setSource(IContextParameter.BUILT_IN);
                                    isChange = true;
                                }
                            }
                        }
                    }
                }
            }
            if (isChange) {
                updateRelation();
            }
            manager.refresh();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
    }

    private void updateRelation() {
        if (param != null) {
            IContextManager manager = modelManager.getContextManager();
            if (manager != null && manager instanceof JobContextManager) {
                JobContextManager jobContextManager = (JobContextManager) manager;
                // not added new
                if (!modelManager.isRepositoryContext() || modelManager.isRepositoryContext()
                        && jobContextManager.isOriginalParameter(param.getName())) {
                    jobContextManager.setModified(true);
                    manager.fireContextsChangedEvent();
                }
            }
        }
    }

}
