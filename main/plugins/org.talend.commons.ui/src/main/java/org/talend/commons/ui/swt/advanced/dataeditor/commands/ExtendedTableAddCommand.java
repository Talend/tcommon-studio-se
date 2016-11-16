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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

public class ExtendedTableAddCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private Integer indexStartAdd;

    private List beansToAdd;

    public static final String LABEL = Messages.getString("ExtendedTableAddCommand.Add.Label"); //$NON-NLS-1$

    public ExtendedTableAddCommand(ExtendedTableModel extendedTable, List beansToAdd, Integer indexStartAdd) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.indexStartAdd = indexStartAdd;
        this.beansToAdd = beansToAdd;
    }

    public ExtendedTableAddCommand(List beansToAdd, ExtendedTableModel extendedTable) {
        this(extendedTable, beansToAdd, null);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableAddCommand(ExtendedTableModel extendedTable, Integer indexStartAdd, Object beanToAdd) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.indexStartAdd = indexStartAdd;
        ArrayList list = new ArrayList(1);
        if (beanToAdd != null) {
            list.add(beanToAdd);
        }
        this.beansToAdd = list;
    }

    public ExtendedTableAddCommand(ExtendedTableModel extendedTable, Object beanToAdd) {
        this(extendedTable, (Integer) null, beanToAdd);
    }

    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void execute() {
        extendedTable.addAll(indexStartAdd, beansToAdd);

    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void redo() {
        execute();
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void undo() {
        extendedTable.removeAll(beansToAdd);
    }

}
