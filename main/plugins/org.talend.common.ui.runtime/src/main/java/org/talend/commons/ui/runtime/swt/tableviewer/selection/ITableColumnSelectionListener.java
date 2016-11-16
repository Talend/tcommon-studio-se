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
package org.talend.commons.ui.runtime.swt.tableviewer.selection;

import org.eclipse.swt.events.SelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.IColumnSortedListener;

public interface ITableColumnSelectionListener extends SelectionListener {

    public TableViewerCreatorColumnNotModifiable getTableViewerCreatorColumn();

    public void setTableViewerCreatorColumn(TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn);

    public TableViewerCreatorNotModifiable getTableViewerCreator();

    public void setTableViewerCreator(TableViewerCreatorNotModifiable tableViewerCreator);

    public void addColumnSortedListener(IColumnSortedListener columnSortedListener);

    public void removeColumnSortedListener(IColumnSortedListener columnSortedListener);

}
