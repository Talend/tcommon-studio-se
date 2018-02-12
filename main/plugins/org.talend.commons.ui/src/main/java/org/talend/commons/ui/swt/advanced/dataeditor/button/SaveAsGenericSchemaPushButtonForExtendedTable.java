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

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

/**
 * DOC Administrator class global comment. Detailled comment <br/>
 * 
 */
public abstract class SaveAsGenericSchemaPushButtonForExtendedTable extends SaveAsGenericSchemaPushButton implements
        IExtendedTablePushButton {

    private String dbmsId;

    public SaveAsGenericSchemaPushButtonForExtendedTable(Composite parent,
            final AbstractExtendedTableViewer extendedTableViewer, String dbmsId) {
        super(parent, extendedTableViewer);
        this.dbmsId = dbmsId;
        this.enableStateHandler = new EnableStateListenerForTableButton(this);
        // extendedTableViewer.addListener(new IExtendedControlListener() {
        //
        // public void handleEvent(ExtendedControlEvent event) {
        // if (event.getType() == AbstractExtendedControlViewer.EVENT_TYPE.MODEL_CHANGED) {
        // ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
        // registerListListener(extendedTableModel);
        // }
        // }
        //
        // });
        //
        // ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
        // registerListListener(extendedTableModel);
    }

    // /**
    // * DOC amaumont Comment method "registerListListener".
    // *
    // * @param extendedTableModel
    // */
    // private void registerListListener(ExtendedTableModel extendedTableModel) {
    // if (extendedTableModel != null) {
    // extendedTableModel.addAfterOperationListListener(new IListenableListListener() {
    //
    // public void handleEvent(ListenableListEvent event) {
    // if (event.type == ListenableListEvent.TYPE.CLEARED
    // || event.type == ListenableListEvent.TYPE.REMOVED
    // || event.type == ListenableListEvent.TYPE.ADDED) {
    // getButton().setEnabled(getEnabledState());
    // }
    // }
    //
    // });
    // }
    // }

    private EnableStateListenerForTableButton enableStateHandler;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.extended.button.IExtendedTablePushButton#getExtendedTableViewer()
     */
    public AbstractExtendedTableViewer getExtendedTableViewer() {
        return (AbstractExtendedTableViewer) getExtendedControlViewer();
    }

    // /*
    // * (non-Javadoc)
    // *
    // * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#beforeCommandExecution()
    // */
    // @Override
    // protected void beforeCommandExecution() {
    //
    // FileDialog dial = new FileDialog(getButton().getShell(), SWT.SAVE);
    // dial.setFilterExtensions(new String[] { "*.xml" });
    // String fileName = dial.open();
    // if ((fileName != null) && (!fileName.equals(""))) { //$NON-NLS-1$
    // this.file = new File(fileName);
    // }
    //
    // }

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
