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

import java.util.Arrays;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.advanced.dataeditor.commands.ExtendedTableCopyCommand;
import org.talend.commons.ui.swt.extended.table.AbstractExtendedTableViewer;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class CopyPushButtonForExtendedTable extends CopyPushButton implements IExtendedTablePushButton {

    private EnableStateListenerForTableButton enableStateHandler;

    /**
     * DOC amaumont SchemaTargetAddPushButton constructor comment.
     * 
     * @param parent
     * @param extendedControlViewer
     */
    public CopyPushButtonForExtendedTable(Composite parent, AbstractExtendedTableViewer extendedTableViewer) {
        super(parent, extendedTableViewer);
        this.enableStateHandler = new EnableStateListenerForTableButton(this);
    }

    @Override
    protected Command getCommandToExecute() {
        AbstractExtendedTableViewer extendedTableViewer = (AbstractExtendedTableViewer) extendedControlViewer;
        TableViewer tableViewer = extendedTableViewer.getTableViewerCreator().getTableViewer();
        ISelection selection = tableViewer.getSelection();
        StructuredSelection structuredSelection = (StructuredSelection) selection;
        Object[] objects = structuredSelection.toArray();
        return new ExtendedTableCopyCommand(Arrays.asList(objects));
    }

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
