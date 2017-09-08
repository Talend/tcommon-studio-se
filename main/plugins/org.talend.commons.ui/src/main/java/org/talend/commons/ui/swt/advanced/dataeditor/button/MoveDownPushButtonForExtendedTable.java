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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableMoveCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class MoveDownPushButtonForExtendedTable extends MoveDownPushButton implements IExtendedTablePushButton {

    private EnableStateListenerForTableButton enableStateHandler;

    /**
     * DOC amaumont SchemaTargetAddPushButton constructor comment.
     * 
     * @param parent
     * @param extendedControlViewer
     */
    public MoveDownPushButtonForExtendedTable(Composite parent, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
        this.enableStateHandler = new EnableStateListenerForTableButton(this);
    }

    public boolean getEnabledState() {
        return super.getEnabledState() && this.enableStateHandler.getEnabledState();
    }

    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer viewer = (AbstractExtendedTableViewer) getExtendedControlViewer();
        return new ExtendedTableMoveCommand(viewer.getExtendedTableModel(), false, viewer.getTableViewerCreator()
                .getTable().getSelectionIndices());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.button.IExtendedTablePushButton#getExtendedTableViewer()
     */
    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }

}
