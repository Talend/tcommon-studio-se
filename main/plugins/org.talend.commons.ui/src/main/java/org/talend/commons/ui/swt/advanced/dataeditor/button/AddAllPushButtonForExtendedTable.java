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
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendTableAddAllCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

/**
 * DOC YeXiaowei class global comment. Detailled comment <br/>
 * 
 */
public class AddAllPushButtonForExtendedTable extends AddAllPushButton {

    /**
     * DOC YeXiaowei AddAllPushButtonForExtendedTable constructor comment.
     * 
     * @param parent
     * @param extendedViewer
     * @param tooltip
     * @param image
     */
    public AddAllPushButtonForExtendedTable(Composite parent, AbstractExtendedControlViewer extendedViewer) {
        super(parent, extendedViewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#getCommandToExecute()
     */
    @Override
    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer abstractExtendedTableViewer = ((AbstractExtendedTableViewer) getExtendedControlViewer());
        ExtendedTableModel extendedTableModel = abstractExtendedTableViewer.getExtendedTableModel();
        int[] selection = abstractExtendedTableViewer.getTableViewerCreator().getTable().getSelectionIndices();
        Integer indexWhereInsert = null;
        if (selection.length > 0) {
            indexWhereInsert = selection[selection.length - 1] + 1;
        }
        return new ExtendTableAddAllCommand(extendedTableModel, getObjectToAdd(), indexWhereInsert);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.button.AddAllPushButton#getObjectToAdd()
     */
    @Override
    protected List<Object> getObjectToAdd() {
        return null;
    }

    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }

}
