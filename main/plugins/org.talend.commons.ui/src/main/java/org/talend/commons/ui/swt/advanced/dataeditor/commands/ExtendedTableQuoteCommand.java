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

public abstract class ExtendedTableQuoteCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private List beansToQuote;
    
    private int[]  selectionIndices;
    
    private String quote;
    
    boolean isAddingQuote;
    

    public static final String LABEL = Messages.getString("ExtendedTableQuoteCommand.Quote.Label"); //$NON-NLS-1$

    /**
     * DOC ExtendedTableQuoteCommand constructor comment.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableQuoteCommand(ExtendedTableModel extendedTable, List beansToQuote, int[] selectionIndices, String quote, boolean isAddingQuote) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.beansToQuote = new ArrayList(beansToQuote);
        this.selectionIndices = selectionIndices;
        this.quote = quote;
        this.isAddingQuote = isAddingQuote;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        toQuote(extendedTable, beansToQuote, selectionIndices, quote, isAddingQuote);
        
    }
    
    public abstract void toQuote(ExtendedTableModel extendedTable, List copiedObjectsList, int[] selectionIndices, String quote, boolean isAddingQuote);

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
