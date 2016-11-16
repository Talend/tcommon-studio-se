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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

public abstract class SaveAsGenericSchemaPushButtonForExtendedTable extends SaveAsGenericSchemaPushButton implements
        IExtendedTablePushButton {

    private String dbmsId;

    public SaveAsGenericSchemaPushButtonForExtendedTable(Composite parent,
            final AbstractExtendedTableViewer extendedTableViewer, String dbmsId) {
        super(parent, extendedTableViewer);
        this.dbmsId = dbmsId;
        this.enableStateHandler = new EnableStateListenerForTableButton(this);
    }

    private EnableStateListenerForTableButton enableStateHandler;

    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }

    public boolean getEnabledState() {
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
        boolean enabled = false;
        if (extendedTableModel != null && extendedTableModel.isDataRegistered()
                && extendedTableModel.getBeansList().size() > 0) {
            enabled = true;
        }
        return super.getEnabledState() && enabled;
    }

    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        return getCommandToExecute(extendedTableViewer.getExtendedTableModel(), this.dbmsId);
    }

    protected abstract Command getCommandToExecute(ExtendedTableModel extendedTableModel, String dbmsId);
}
