// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class AddAllPushButtonForExtendedTable extends AddAllPushButton {

    public AddAllPushButtonForExtendedTable(Composite parent, AbstractExtendedControlViewer extendedViewer) {
        super(parent, extendedViewer);
    }

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

    @Override
    protected List<Object> getObjectToAdd() {
        return null;
    }

    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }

}
