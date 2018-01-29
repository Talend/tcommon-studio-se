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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.ITableColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.IColumnSortedListener;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.TableViewerCreatorSorter;

/**
 * DOC amaumont class global comment. Detailled comment <br/> $Id: DefaultHeaderColumnSelectionListener.java,v 1.3
 * 2006/06/02 15:24:10 amaumont Exp $
 */
public class DefaultHeaderColumnSelectionListener implements ITableColumnSelectionListener {

    private TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn;

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private List<IColumnSortedListener> sortListenerList = new ArrayList<IColumnSortedListener>();

    public DefaultHeaderColumnSelectionListener(TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn,
            TableViewerCreatorNotModifiable tableViewerCreator) {
        super();
        this.tableViewerCreatorColumn = tableViewerCreatorColumn;
        this.tableViewerCreator = tableViewerCreator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e) {
        TableViewerCreatorSorter viewerSorter = (TableViewerCreatorSorter) tableViewerCreator.getTableViewer()
                .getSorter();
        if (viewerSorter != null) {
            viewerSorter.prepareSort(tableViewerCreator, tableViewerCreatorColumn);
            tableViewerCreator.getTableViewer().refresh();
            fireColumnSorted();
        }
    }

    /**
     * DOC amaumont Comment method "fireColumnSorted".
     */
    private void fireColumnSorted() {
        for (IColumnSortedListener columnSortedListener : sortListenerList) {
            columnSortedListener.handle();
        }
    }

    public void addColumnSortedListener(IColumnSortedListener columnSortedListener) {
        sortListenerList.add(columnSortedListener);
    }

    public void removeColumnSortedListener(IColumnSortedListener columnSortedListener) {
        sortListenerList.remove(columnSortedListener);
    }

    public TableViewerCreatorColumnNotModifiable getTableViewerCreatorColumn() {
        return this.tableViewerCreatorColumn;
    }

    public void setTableViewerCreatorColumn(TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn) {
        this.tableViewerCreatorColumn = tableViewerCreatorColumn;
    }

    public TableViewerCreatorNotModifiable getTableViewerCreator() {
        return this.tableViewerCreator;
    }

    public void setTableViewerCreator(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;
    }

}
