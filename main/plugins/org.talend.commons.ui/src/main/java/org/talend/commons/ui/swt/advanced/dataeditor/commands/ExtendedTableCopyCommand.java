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
import org.talend.commons.ui.utils.SimpleClipboard;

public class ExtendedTableCopyCommand extends Command implements IExtendedTableCommand {

    private List beansToCopy;

    public static final String LABEL = Messages.getString("ExtendedTableCopyCommand.Copy.Label"); //$NON-NLS-1$

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableCopyCommand(List beansToCopy) {
        super(LABEL);
        this.beansToCopy = new ArrayList(beansToCopy);
    }

    @SuppressWarnings("unchecked")//$NON-NLS-1$
    public ExtendedTableCopyCommand(Object beanToCopy) {
        super(LABEL);
        beansToCopy = new ArrayList(1);
        beansToCopy.add(beanToCopy);
    }

    @Override
    public void execute() {
        SimpleClipboard.getInstance().setData(beansToCopy);
    }

    @Override
    public boolean canUndo() {
        return false;
    }

    @Override
    public void redo() {
    }

    @Override
    public void undo() {
    }

}
