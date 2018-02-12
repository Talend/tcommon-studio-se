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
package org.talend.commons.ui.swt.advanced.dataeditor.button;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ClipboardEvent;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.utils.IClipoardListener;
import org.talend.commons.ui.utils.SimpleClipboard;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class PastePushButtonForExtendedTable extends PastePushButton implements IExtendedTablePushButton {

    /**
     * DOC amaumont SchemaTargetAddPushButton constructor comment.
     * 
     * @param parent
     * @param extendedControlViewer
     */
    public PastePushButtonForExtendedTable(Composite parent, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
        final IClipoardListener clipoardListener = new IClipoardListener() {

            public void handleEvent(ClipboardEvent event) {
                getButton().setEnabled(getEnabledState());
            }

        };
        SimpleClipboard.getInstance().addListener(clipoardListener);
        getButton().addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                SimpleClipboard.getInstance().removeListener(clipoardListener);
            }

        });
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
        return getCommandToExecute(extendedTableModel, indexWhereInsert);
    }

    protected abstract Command getCommandToExecute(ExtendedTableModel extendedTableModel, Integer indexWhereInsert);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.button.IExtendedTablePushButton#getExtendedTableViewer()
     */
    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }

    @Override
    public boolean getEnabledState() {
        Object data = SimpleClipboard.getInstance().getData();
        return super.getEnabledState() && data != null && data instanceof List && !getExtendedControlViewer().isReadOnly();
    }
}
