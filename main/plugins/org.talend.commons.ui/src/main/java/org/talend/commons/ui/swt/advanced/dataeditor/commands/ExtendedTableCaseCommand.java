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
package org.talend.commons.ui.swt.advanced.dataeditor.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;

public abstract class ExtendedTableCaseCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private List beansToCovertCase;
    
    private int[]  selectionIndices;
    
    private boolean isUpperCase;
    

    public static final String LABEL = Messages.getString("ExtendedTableCaseCommand.case.Label"); //$NON-NLS-1$

    /**
     * DOC ExtendedTableCaseCommand constructor comment.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableCaseCommand(ExtendedTableModel extendedTable, List beansToCovertCase, int[] selectionIndices, boolean isUpperCase) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.beansToCovertCase = new ArrayList(beansToCovertCase);
        this.selectionIndices = selectionIndices;
        this.isUpperCase = isUpperCase;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        convertCase(extendedTable, beansToCovertCase, selectionIndices, isUpperCase);
        
    }
    
    public abstract void convertCase(ExtendedTableModel extendedTable, List copiedObjectsList, int[] selectionIndices, boolean isUpperCase);

    /*
     * (non-Javadoc)
     *
     * @see org.talend.commons.ui.command.CommonCommand#canUndo()
     */
    @Override
    public boolean canUndo() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.commons.ui.command.CommonCommand#redo()
     */
    @Override
    public void redo() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.talend.commons.ui.command.CommonCommand#undo()
     */
    @Override
    public void undo() {
    }

}
