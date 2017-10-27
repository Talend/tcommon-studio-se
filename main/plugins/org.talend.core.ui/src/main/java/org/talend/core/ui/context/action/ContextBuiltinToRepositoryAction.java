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
package org.talend.core.ui.context.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.context.ContextTreeTable.ContextTreeNode;
import org.talend.core.ui.context.IContextModelManager;
import org.talend.core.ui.context.cmd.ContextBuiltinToRepositoryCommand;
import org.talend.core.ui.context.model.table.ContextTableTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;
import org.talend.core.ui.context.model.template.ContextVariableTabChildModel;
import org.talend.core.ui.context.model.template.ContextVariableTabParentModel;
import org.talend.core.ui.i18n.Messages;
import org.talend.repository.ui.actions.AContextualAction;

/**
 * DOC xye class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class ContextBuiltinToRepositoryAction extends AContextualAction {

    public final static String ID = "org.talend.core.ui.context.actions.ContextBuiltinToRepositoryAction"; //$NON-NLS-1$

    private TreeViewer viewer = null;

    private NatTable table = null;

    private final IContextModelManager modelManager; // modified by hyWang

    private List<IContextParameter> params = new ArrayList<IContextParameter>();

    private IContextManager contextManager; // added by hyWang

    private static final String LABEL = Messages.getString("ConextTemplateComposite.addToRepositoryContextAction.label"); //$NON-NLS-1$

    public ContextBuiltinToRepositoryAction(IContextModelManager modelManager) {
        super();
        this.setText(LABEL);
        this.setToolTipText(LABEL);
        this.modelManager = modelManager;
    }

    @Override
    protected void doRun() {
        if (contextManager != null) {
            ContextItem item = CoreRuntimePlugin.getInstance().getRepositoryService()
                    .openRepositoryReviewDialog(ERepositoryObjectType.CONTEXT, null, params, contextManager);
            if (modelManager.getCommandStack() != null) {
                modelManager.getCommandStack().execute(new ContextBuiltinToRepositoryCommand(params, contextManager, item));
            } else {
                new ContextBuiltinToRepositoryCommand(params, contextManager, item).execute();
            }
            modelManager.refresh();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.actions.ITreeContextualAction#init(org.eclipse.jface.viewers.TreeViewer,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    @Override
    public void init(TreeViewer viewer, IStructuredSelection selection) {
        this.viewer = viewer;
        this.contextManager = modelManager.getContextManager();
        boolean canWork = viewer != null && selection != null && selection.size() > 0;
        if (canWork) {
            for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
                Object object = iter.next();
                if (object instanceof ContextVariableTabParentModel) {
                    ContextVariableTabParentModel param = (ContextVariableTabParentModel) object;
                    if (!IContextParameter.BUILT_IN.equals(param.getSourceId())) {
                        setEnabled(false);
                        return;
                    } else {
                        params.add(param.getContextParameter());
                    }
                } else if (object instanceof ContextVariableTabChildModel) {
                    ContextVariableTabChildModel param = (ContextVariableTabChildModel) object;
                    if (!IContextParameter.BUILT_IN.equals(param.getContextParameter().getSource())) {
                        setEnabled(false);
                        return;
                    } else {
                        params.add(param.getContextParameter());
                    }
                } else if (object instanceof ContextVariableTabParentModel) {
                    ContextVariableTabParentModel param = (ContextVariableTabParentModel) object;
                    if (!IContextParameter.BUILT_IN.equals(param.getContextParameter().getSource())) {
                        setEnabled(false);
                        return;
                    } else {
                        params.add(param.getContextParameter());
                    }
                }
            }
        }
        setEnabled(canWork);
    }

    public void init(NatTable table, Object[] rowNodes) {
        this.table = table;
        this.contextManager = modelManager.getContextManager();
        boolean canWork = table != null && rowNodes != null;
        if (canWork) {
            for (Object rowNode : rowNodes) {
                Object rowData = ((ContextTreeNode) rowNode).getTreeData();
                if (rowData instanceof ContextTableTabParentModel) {
                    ContextTableTabParentModel param = (ContextTableTabParentModel) rowData;
                    if (!IContextParameter.BUILT_IN.equals(param.getSourceId())) {
                        setEnabled(false);
                        return;
                    } else {
                        params.add(param.getContextParameter());
                    }
                } else if (rowData instanceof ContextTableTabChildModel) {
                    ContextTableTabChildModel param = (ContextTableTabChildModel) rowData;
                    if (!IContextParameter.BUILT_IN.equals(param.getContextParameter().getSource())) {
                        setEnabled(false);
                        return;
                    } else {
                        params.add(param.getContextParameter());
                    }
                }
            }
        }
        setEnabled(canWork);
    }
}
