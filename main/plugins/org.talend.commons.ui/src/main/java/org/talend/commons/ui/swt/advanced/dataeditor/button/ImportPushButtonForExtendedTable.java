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

import java.io.File;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ImportPushButtonForExtendedTable extends ImportPushButton implements IExtendedTablePushButton {

    private File file;

    /**
     * DOC amaumont SchemaTargetAddPushButton constructor comment.
     * 
     * @param parent
     * @param extendedControlViewer
     */
    public ImportPushButtonForExtendedTable(Composite parent, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.advanced.dataeditor.control.ExtendedPushButton#beforeCommandExecution()
     */
    @Override
    protected void beforeCommandExecution() {
        FileDialog dial = new FileDialog(getButton().getShell(), SWT.OPEN);
        dial.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
        String fileName = dial.open();
        if ((fileName != null) && (!fileName.equals(""))) { //$NON-NLS-1$
            file = new File(fileName);
        } else {
            file = null;
        }
    }

    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        Table table = extendedTableViewer.getTableViewerCreator().getTable();
        if (file != null) {
            return getCommandToExecute(extendedTableViewer.getExtendedTableModel(), file);
        } else {
            return null;
        }
    }

    protected abstract Command getCommandToExecute(ExtendedTableModel model, File file);

    private void openMessageError(String errorText) {
        MessageBox msgBox = new MessageBox(getButton().getShell());
        msgBox.setText(Messages.getString("ImportPushButtonForExtendedTable.ErrorMsg.Text")); //$NON-NLS-1$
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

}
