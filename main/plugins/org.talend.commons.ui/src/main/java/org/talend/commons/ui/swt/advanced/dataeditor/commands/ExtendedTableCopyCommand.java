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
package org.talend.commons.ui.swt.advanced.dataeditor.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.utils.SimpleClipboard;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class ExtendedTableCopyCommand extends Command implements IExtendedTableCommand {

    private List beansToCopy;

    public static final String LABEL = Messages.getString("ExtendedTableCopyCommand.Copy.Label"); //$NON-NLS-1$

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableCopyCommand(List beansToCopy) {
        super(LABEL);
        this.beansToCopy = new ArrayList(beansToCopy);
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableCopyCommand(Object beanToCopy) {
        super(LABEL);
        beansToCopy = new ArrayList(1);
        beansToCopy.add(beanToCopy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @Override
    public void execute() {
        SimpleClipboard.getInstance().setData(beansToCopy);
    }

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
