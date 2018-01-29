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
public abstract class AbstractExtendedTableResetDBTypesCommand extends Command implements IExtendedTableCommand {

    private ExtendedTableModel extendedTable;

    private List<String> oldDbTypes;

    private List removedBeansIndices;

    private String dbmsId;

    public static final String LABEL = Messages.getString("ExtendedTableResetDBTypesCommand.ResetDBTypes.Label"); //$NON-NLS-1$

    /**
     * DOC amaumont ExtendedTableAddCommand constructor comment.
     * 
     * @param dbmsId
     */
    public AbstractExtendedTableResetDBTypesCommand(ExtendedTableModel extendedTable, String dbmsId) {
        super(LABEL);
        this.extendedTable = extendedTable;
        this.dbmsId = dbmsId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#execute()
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public void execute() {
        // JavaType javaType = JavaTypesManager.getJavaTypeFromId(oldTalendType);
        // String typeName = JavaTypesManager.getShortNameFromJavaType(javaType);

        List beansList = extendedTable.getBeansList();
        for (Object object : beansList) {
            // if (object instanceof IMet)
        }
        //
        // if (indexItemToRemove != null) {
        // removedBeans = new ArrayList(1);
        // extendedTable.remove((int) indexItemToRemove);
        // removedBeans.add(removedBeans);
        // removedBeansIndices = new ArrayList(1);
        // removedBeansIndices.add(indexItemToRemove);
        // }
        // if (indexItemsToRemove != null) {
        // removedBeans = new ArrayList(extendedTable.remove(indexItemsToRemove));
        // removedBeansIndices = Arrays.asList(ArrayUtils.toObject(indexItemsToRemove));
        // }
        // if (beansToRemove != null) {
        //
        // int lstSize = beansToRemove.size();
        // removedBeansIndices = new ArrayList();
        // List beansToRemove2 = new ArrayList(beansToRemove);
        // for (int i = 0; i < lstSize; i++) {
        // int index = beansList.indexOf(beansToRemove2.get(i));
        // if (index != -1) {
        // removedBeansIndices.add(index);
        // } else {
        // beansToRemove.remove(i);
        // }
        // }
        // if (extendedTable.removeAll((Collection) beansToRemove)) {
        // removedBeans = new ArrayList(beansToRemove);
        // } else {
        // removedBeansIndices.clear();
        // }
        // }
        // if (beanToRemove != null) {
        // int index = beansList.indexOf(beanToRemove);
        // if (extendedTable.remove(beanToRemove)) {
        // removedBeans = new ArrayList(1);
        // removedBeans.add(beanToRemove);
        // removedBeansIndices = new ArrayList(1);
        // removedBeansIndices.add(index);
        // }
        // }

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
    public synchronized void redo() {
        execute();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#undo()
     */
    @SuppressWarnings("unchecked")//$NON-NLS-1$
    @Override
    public synchronized void undo() {

        // extendedTable.addAll(removedBeansIndices, removedBeans);

    }

}
