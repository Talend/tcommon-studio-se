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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.ITableColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.IColumnSortedListener;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.TableViewerCreatorSorter;

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

    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

    }

    public void widgetSelected(SelectionEvent e) {
        TableViewerCreatorSorter viewerSorter = (TableViewerCreatorSorter) tableViewerCreator.getTableViewer()
                .getSorter();
        if (viewerSorter != null) {
            viewerSorter.prepareSort(tableViewerCreator, tableViewerCreatorColumn);
            tableViewerCreator.getTableViewer().refresh();
            fireColumnSorted();
        }
    }

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
