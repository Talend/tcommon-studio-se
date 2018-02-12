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
import org.eclipse.swt.widgets.Tree;
import org.talend.core.ui.context.ContextTreeValuesComposite;
import org.talend.core.ui.i18n.Messages;

/**
 * cli class global comment. Detailled comment
 * 
 * This action is used to group the text by Context.
 */
public class GroupByContextAction extends Action {

    private ContextTreeValuesComposite parentModel;

    public GroupByContextAction(ContextTreeValuesComposite parentModel) {
        super(Messages.getString("GroupByContextAction.groupContext"), IAction.AS_RADIO_BUTTON); //$NON-NLS-1$
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
                ContextTreeConstants.CONTEXT_COLUMN_PROPERTY);
        parentModel.getColumn1st().setText(ContextTreeConstants.getContextColumnName());
        parentModel.getColumn2nd().setText(ContextTreeConstants.getVariableColumnName());
        parentModel.getViewer().setColumnProperties(ContextTreeConstants.GROUP_BY_CONTEXT_COLUMN_PROPERTIES);
        GroupByContextProvider labelprovider = new GroupByContextProvider();
        parentModel.setProviderProxy(labelprovider);
        Tree tree = parentModel.getViewer().getTree();
        parentModel.getViewer()
                .setCellEditors(
                        new CellEditor[] { null, null, new CheckboxCellEditor(tree), new TextCellEditor(tree),
                                new TextCellEditor(tree) });
        parentModel.getViewer().refresh();
        parentModel.getViewer().expandAll();
        parentModel.checkItemValueErrors(tree.getItems());
    }
}
