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
package org.talend.commons.ui.swt.advanced.dataeditor.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

public abstract class AbstractExtendedTableResetDBTypesCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private List<String> oldDbTypes;

    private List removedBeansIndices;

    private String dbmsId;

    public static final String LABEL = Messages.getString("ExtendedTableResetDBTypesCommand.ResetDBTypes.Label"); //$NON-NLS-1$

    public AbstractExtendedTableResetDBTypesCommand(ExtendedTableModel extendedTable, String dbmsId) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.dbmsId = dbmsId;
    }

    @Override
    public void execute() {
        List beansList = extendedTable.getBeansList();
        for (Object object : beansList) {
        }
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public synchronized void redo() {
        execute();
    }

    @Override
    public synchronized void undo() {
    }

}
