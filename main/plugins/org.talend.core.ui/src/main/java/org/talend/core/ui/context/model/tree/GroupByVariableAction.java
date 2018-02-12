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
package org.talend.core.ui.context.model.tree;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.talend.core.ui.context.ContextTreeValuesComposite;
import org.talend.core.ui.i18n.Messages;

/**
 * cli class global comment. Detailled comment
 * 
 * This action is used to group the text by variables.
 */
public class GroupByVariableAction extends Action {

    private ContextTreeValuesComposite parentModel;

    public GroupByVariableAction(ContextTreeValuesComposite parentModel) {
        super(Messages.getString("GroupByVariableAction.groupVariable"), IAction.AS_RADIO_BUTTON); //$NON-NLS-1$
        this.parentModel = parentModel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        if (parentModel == null) {
            return;
        }
        parentModel.getPreferenceStore().setValue(ContextTreeConstants.PRESENTATION_TYPE_KEY,
                ContextTreeConstants.VARIABLE_COLUMN_PROPERTY);
        parentModel.getColumn1st().setText(ContextTreeConstants.getVariableColumnName());
        parentModel.getColumn2nd().setText(ContextTreeConstants.getContextColumnName());

        TreeViewer viewer = parentModel.getViewer();

        viewer.setColumnProperties(ContextTreeConstants.GROUP_BY_VARIABLE_COLUMN_PROPERTIES);
        GroupByVariableProvider labelprovider = new GroupByVariableProvider(parentModel.getContextModelManager());
        parentModel.setProviderProxy(labelprovider);
        Tree tree = viewer.getTree();
        viewer.setCellEditors(new CellEditor[] { null, null, new CheckboxCellEditor(tree), new TextCellEditor(tree),
                new TextCellEditor(tree) });
        viewer.refresh();
        viewer.expandAll();
        parentModel.checkItemValueErrors(tree.getItems());
    }
}
