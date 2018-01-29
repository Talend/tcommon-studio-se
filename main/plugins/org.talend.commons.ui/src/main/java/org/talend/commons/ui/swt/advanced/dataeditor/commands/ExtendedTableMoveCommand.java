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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.utils.data.list.ListenableList;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExtendedTableMoveCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private boolean moveUp;

    private int[] entriesIndices;

    private ArrayList<Integer> indicesOrigin;

    private ArrayList<Integer> indicesTarget;

    private ListenableList list;

    public static final String LABEL = Messages.getString("ExtendedTableMoveCommand.MoveLabel"); //$NON-NLS-1$

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTableMoveCommand(ExtendedTableModel extendedTable, boolean moveUp, int[] entriesIndices) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.moveUp = moveUp;
        this.entriesIndices = entriesIndices;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {

        list = (ListenableList) extendedTable.getBeansList();
        Set<Integer> setIndicesSelectedMoved = new HashSet<Integer>();
        int increment;
        int startIndex;
        int endIndex;
        indicesOrigin = new ArrayList<Integer>();
        indicesTarget = new ArrayList<Integer>();
        if (this.moveUp) {
            increment = -1;
            startIndex = 0;
            endIndex = entriesIndices.length;

        } else {
            increment = 1;
            startIndex = entriesIndices.length - 1;
            endIndex = -1;
        }

        for (int i = startIndex; i != endIndex; i -= increment) {
            int indice = entriesIndices[i];
            int newIndice = indice + increment;
            if (newIndice < 0) {
                newIndice = 0;
            }
            if (newIndice >= list.size()) {
                newIndice = list.size() - 1;
            }
            if (!setIndicesSelectedMoved.contains(newIndice)) {
                indicesOrigin.add(indice);
                indicesTarget.add(newIndice);
                setIndicesSelectedMoved.add(newIndice);
            } else {
                indicesOrigin.add(indice);
                indicesTarget.add(indice);
                setIndicesSelectedMoved.add(indice);
            }
        }

        extendedTable.swapElements(indicesOrigin, indicesTarget);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#canExecute()
     */
    @Override
    public boolean canExecute() {
        return true;
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
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void redo() {
        undo();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#undo()
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void undo() {
        Collections.reverse(indicesTarget);
        Collections.reverse(indicesOrigin);
        extendedTable.swapElements(indicesTarget, indicesOrigin);
    }

}
