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
import org.talend.commons.ui.swt.extended.table.ExtendedTableModel;
import org.talend.commons.ui.utils.SimpleClipboard;
import org.talend.commons.utils.data.list.UniqueStringGenerator;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ExtendedTablePasteCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private Integer indexStart;

    public static final String LABEL = Messages.getString("ExtendedTablePasteCommand.Paste.Label"); //$NON-NLS-1$

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTablePasteCommand(ExtendedTableModel extendedTable, Integer indexStartAdd) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.indexStart = indexStartAdd;
    }

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     */
    public ExtendedTablePasteCommand(ExtendedTableModel extendedTable) {
        this(extendedTable, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {

        Object data = SimpleClipboard.getInstance().getData();
        if (data instanceof List) {
            List list = new ArrayList((List) data);
            list = createPastableBeansList(extendedTable, list);
            extendedTable.addAll(indexStart, list);
        }

    }

    public abstract List createPastableBeansList(ExtendedTableModel extendedTable, List copiedObjectsList);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#canUndo()
     */
    @Override
    public boolean canUndo() {
        return false;
    }

    public String validateColumnName(final String columnName, List<String> labels) {
        String name = columnName;
        UniqueStringGenerator<String> uniqueStringGenerator = new UniqueStringGenerator<String>(name, labels) {

            @Override
            protected String getBeanString(String bean) {
                return bean;
            }

        };
        return uniqueStringGenerator.getUniqueString();
    }

}
