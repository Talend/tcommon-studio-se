// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.util.Arrays;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

public abstract class CasePushButtonForExtendedTable extends CasePushButton implements IExtendedTablePushButton {

    private EnableStateListenerForTableButton enableStateHandler;
    private boolean isUpperCase = true;

    /**
     * DOC  CasePushButtonForExtendedTable constructor comment.
     *
     * @param parent
     * @param extendedControlViewer
     */
    public CasePushButtonForExtendedTable(Composite parent, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
        this.enableStateHandler = new EnableStateListenerForTableButton(this);
    }
    
    @Override
    protected void afterCommandExecution(Command executedCommand) {
       super.afterCommandExecution(executedCommand);
       if (isUpperCase) {
           this.getButton().setToolTipText( Messages.getString("CasePushButton.CaseButton.Tip"));
           this.getButton().setImage(ImageProvider.getImage(EImage.LOWERCASE_ICON));
           this.isUpperCase = false;
       } else {
           this.getButton().setToolTipText( Messages.getString("CasePushButton.CaseButton.Tip"));
           this.getButton().setImage(ImageProvider.getImage(EImage.UPPERCASE_ICON));
           this.isUpperCase = true;
       }
    }

    @Override
    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        ExtendedTableModel extendedTableModel = extendedTableViewer.getExtendedTableModel();
        TableViewer tableViewer = extendedTableViewer.getTableViewerCreator().getTableViewer();
        ISelection selection = tableViewer.getSelection();
        StructuredSelection structuredSelection = (StructuredSelection) selection;
        Object[] objects = structuredSelection.toArray();
        int[] selectionIndices = extendedTableViewer.getTableViewerCreator().getTable().getSelectionIndices();
        return getCommandToExecute(extendedTableModel, Arrays.asList(objects), selectionIndices, isUpperCase);
    }
    
    protected abstract Command getCommandToExecute(ExtendedTableModel extendedTable, List beansToUppercase, int[] selectionIndices, boolean isUpperCase);

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
        return super.getEnabledState() && this.enableStateHandler.getEnabledState();
    }

}
