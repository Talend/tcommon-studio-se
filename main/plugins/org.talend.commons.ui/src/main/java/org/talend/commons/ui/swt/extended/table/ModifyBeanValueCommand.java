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
package org.talend.commons.ui.swt.extended.table;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Table;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.tableviewer.data.AccessorUtils;
import org.talend.commons.ui.runtime.utils.TableUtils;
import org.talend.commons.ui.swt.tableviewer.ModifiedBeanEvent;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreator;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <B> modie
 */
public class ModifyBeanValueCommand<B> extends Command {

    private ModifiedBeanEvent<B> event;

    /**
     * DOC amaumont ModifiedBeanValueCommand constructor comment.
     * 
     * @param event
     */
    public ModifyBeanValueCommand(ModifiedBeanEvent<B> event) {
        this.event = event;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#getLabel()
     */
    @Override
    public String getLabel() {
        return Messages.getString("ModifyBeanValueCommand.ModifyCell.Label"); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#redo()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void redo() {
        TableViewerCreator tableViewerCreator = event.column.getTableViewerCreator();
        Table table = tableViewerCreator.getTable();
        if (table.isDisposed()) {
            // return;
            AccessorUtils.set(event.column, event.bean, event.newValue);
        } else {
            Object bean = getBean(tableViewerCreator);
            if (bean != null) {
                tableViewerCreator.setBeanValue(event.column, bean, event.newValue, false);
            }
        }
    }

    /**
     * DOC amaumont Comment method "getBean".
     * 
     * @param tableViewerCreator
     * @return
     */
    private Object getBean(TableViewerCreator tableViewerCreator) {
        B bean = this.event.bean;
        Table table = tableViewerCreator.getTable();
        int beanIndex = TableUtils.getItemIndex(table, bean);
        if (beanIndex != -1) {
            return bean;
        } else {
            if (event.index > 0 && event.index < table.getItemCount()) {
                return table.getItem(event.index).getData();
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.command.CommonCommand#undo()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void undo() {
        TableViewerCreator tableViewerCreator = event.column.getTableViewerCreator();
        Table table = tableViewerCreator.getTable();
        if (table.isDisposed()) {
            // return;
            AccessorUtils.set(event.column, event.bean, event.previousValue);
        } else {
            Object bean = getBean(tableViewerCreator);
            if (bean != null) {
                tableViewerCreator.setBeanValue(event.column, bean, event.previousValue, false);
            }
        }
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

}
