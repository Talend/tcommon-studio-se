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

import java.io.File;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedControlViewer;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedControlEvent;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.swt.extended.table.IExtendedControlListener;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableListEvent;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ExportPushButtonForExtendedTable extends ExportPushButton implements IExtendedTablePushButton {

    private File file;

    /**
     * DOC amaumont SchemaTargetAddPushButton constructor comment.
     * 
     * @param parent
     * @param extendedControlViewer
     */
    public ExportPushButtonForExtendedTable(Composite parent, final AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
        extendedTableViewer.addListener(new IExtendedControlListener() {

            public void handleEvent(ExtendedControlEvent event) {
                if (event.getType() == AbstractExtendedControlViewer.EVENT_TYPE.MODEL_CHANGED) {
                    ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
                    registerListListener(extendedTableModel);
                }
            }
        });

        ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
        registerListListener(extendedTableModel);

    }

    /**
     * DOC amaumont Comment method "registerListListener".
     * 
     * @param extendedTableModel
     */
    private void registerListListener(ExtendedTableModel extendedTableModel) {
        if (extendedTableModel != null) {
            extendedTableModel.addAfterOperationListListener(new IListenableListListener() {

                public void handleEvent(ListenableListEvent event) {
                    if (event.type == ListenableListEvent.TYPE.CLEARED
                            || event.type == ListenableListEvent.TYPE.REMOVED
                            || event.type == ListenableListEvent.TYPE.ADDED) {
                        if (!getButton().isDisposed()) {
                            getButton().setEnabled(getEnabledState());
                        }
                    }
                }

            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#beforeCommandExecution()
     */
    @Override
    protected void beforeCommandExecution() {

        FileDialog dial = new FileDialog(getButton().getShell(), SWT.SAVE);
        dial.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
        String fileName = dial.open();
        if ((fileName != null) && (!fileName.equals(""))) { //$NON-NLS-1$
            this.file = new File(fileName);
        }

    }

    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        Table table = extendedTableViewer.getTableViewerCreator().getTable();
        return getCommandToExecute(extendedTableViewer.getExtendedTableModel(), file);
    }

    protected abstract Command getCommandToExecute(ExtendedTableModel model, File file);

    private void openMessageError(String errorText) {
        MessageBox msgBox = new MessageBox(getButton().getShell());
        msgBox.setText(Messages.getString("ExportPushButtonForExtendedTable.ErrorMsg.Text")); //$NON-NLS-1$
        msgBox.setMessage(errorText);
        msgBox.open();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.button.IExtendedTablePushButton#getExtendedTableViewer()
     */
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

}
