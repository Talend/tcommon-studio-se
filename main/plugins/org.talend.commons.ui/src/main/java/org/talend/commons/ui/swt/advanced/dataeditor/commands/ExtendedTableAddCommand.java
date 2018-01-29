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
package org.talend.commons.ui.swt.advanced.dataeditor.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExtendedTableAddCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private Integer indexStartAdd;

    private List beansToAdd;

    public static final String LABEL = Messages.getString("ExtendedTableAddCommand.Add.Label"); //$NON-NLS-1$

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableAddCommand(ExtendedTableModel extendedTable, List beansToAdd, Integer indexStartAdd) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.indexStartAdd = indexStartAdd;
        this.beansToAdd = beansToAdd;
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableAddCommand(List beansToAdd, ExtendedTableModel extendedTable) {
        this(extendedTable, beansToAdd, null);
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
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

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableAddCommand(ExtendedTableModel extendedTable, Object beanToAdd) {
        this(extendedTable, (Integer) null, beanToAdd);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public void execute() {

        // for (Object object : beansToAdd) {
        // extendedTable.add(object);
        // }

        extendedTable.addAll(indexStartAdd, beansToAdd);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#canUndo()
     */
    @Override
    public boolean canUndo() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#redo()
     */
    @Override
    public void redo() {
        execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#undo()
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void undo() {

        extendedTable.removeAll(beansToAdd);

    }

}
