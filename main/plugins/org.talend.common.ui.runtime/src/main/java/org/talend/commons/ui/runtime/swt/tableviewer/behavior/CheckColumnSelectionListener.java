// ============================================================================
//
// Talend Community Edition
//
// // Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import java.util.List;

import org.eclipse.swt.events.SelectionEvent;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.ITableColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.IColumnSortedListener;
import org.talend.commons.utils.data.bean.IBeanPropertyAccessors;

public class CheckColumnSelectionListener implements ITableColumnSelectionListener {

    private TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn;

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private boolean checked;

    /**
     * zhangyi CheckColumnSelectionListener constructor comment.
     */
    public CheckColumnSelectionListener(TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn,
            TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreatorColumn = tableViewerCreatorColumn;
        this.tableViewerCreator = tableViewerCreator;
        this.checked = true;
    }

    @Override
    public void addColumnSortedListener(IColumnSortedListener columnSortedListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public TableViewerCreatorNotModifiable getTableViewerCreator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TableViewerCreatorColumnNotModifiable getTableViewerCreatorColumn() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeColumnSortedListener(IColumnSortedListener columnSortedListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTableViewerCreator(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;

    }

    @Override
    public void setTableViewerCreatorColumn(TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn) {
        this.tableViewerCreatorColumn = tableViewerCreatorColumn;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        if (tableViewerCreator != null && tableViewerCreator.isReadOnly()) {
            return;
        }
        String columnId = tableViewerCreatorColumn.getId();
        IBeanPropertyAccessors accessor = tableViewerCreator.getColumn(columnId).getBeanPropertyAccessors();

        List inputList = tableViewerCreator.getInputList();
        for (int i = 0; i < inputList.size(); i++) {
            accessor.set(inputList.get(i), checked ? false : true);
        }

        if (checked) {
            checked = false;
        } else {
            checked = true;
        }
        tableViewerCreatorColumn.getTableColumn().setImage(
                checked ? ImageProvider.getImage(EImage.CHECKED_ICON) : ImageProvider.getImage(EImage.UNCHECKED_ICON));
        tableViewerCreator.refreshTableEditorControls();
    }
}
