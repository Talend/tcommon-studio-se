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
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.Item;

/**
 * created by ldong on Sep 12, 2014 Detailled comment
 * 
 */
public class AddContextGroupRelationCommand extends Command {

    private IContextParameter param;

    private IContextManager contextManager;

    private Item sourceItem;

    private IContextParameter originaParam;

    public AddContextGroupRelationCommand(IContextManager contextManager, IContextParameter param, Item sourceItem) {
        super();
        this.contextManager = contextManager;
        this.param = param;
        this.sourceItem = sourceItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        boolean isChange = false;
        String sourceId = sourceItem.getProperty().getId();
        originaParam = param;
        if (contextManager != null) {
            List<IContext> contexts = contextManager.getListContext();
            for (IContext envContext : contexts) {
                List<IContextParameter> list = envContext.getContextParameterList();
                if (list != null && list.size() > 0) {
                    for (IContextParameter contextPara : list) {
                        String tempContextParaName = contextPara.getName();
                        if (tempContextParaName.equals(param.getName())) {
                            contextPara.setSource(sourceId);
                            isChange = true;
                        }
                    }
                }
            }
        }
        if (isChange) {
            updateRelation();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#undo()
     */
    @Override
    public void undo() {
        String sourceId = originaParam.getSource();
        if (contextManager != null) {
            List<IContext> contexts = contextManager.getListContext();
            for (IContext envContext : contexts) {
                List<IContextParameter> list = envContext.getContextParameterList();
                if (list != null && list.size() > 0) {
                    for (IContextParameter contextPara : list) {
                        String tempContextParaName = contextPara.getName();
                        if (tempContextParaName.equals(originaParam.getName())) {
                            contextPara.setSource(sourceId);
                        }
                    }
                }
            }
        }
    }

    private void updateRelation() {
        if (param != null) {
            if (contextManager != null && contextManager instanceof JobContextManager) {
                JobContextManager jobContextManager = (JobContextManager) contextManager;
                if (jobContextManager.isOriginalParameter(param.getName())) {
                    jobContextManager.setModified(true);
                    contextManager.fireContextsChangedEvent();
                }
            }
        }
    }

}
