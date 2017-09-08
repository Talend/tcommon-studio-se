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
package org.talend.commons.ui.runtime.utils;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.talend.commons.ui.runtime.ws.WindowSystem;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TableUtils.java 7183 2007-11-23 11:03:36Z amaumont $
 * 
 */
public class TableUtils {

    public static int getColumnIndex(Table table, Point pointCursor) {

        // searching current column index
        int currentColumnIndex = -1;
        TableColumn[] columns = table.getColumns();
        for (int i = 0, width = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            int widthColumn = column.getWidth();
            if (pointCursor.x >= width
                    && pointCursor.x <= width + widthColumn
                    && ((!WindowSystem.isGTK() && pointCursor.y > table.getHeaderHeight() && pointCursor.y < table
                            .getHeaderHeight()
                            + table.getItemCount() * table.getItemHeight()) || (WindowSystem.isGTK() && pointCursor.y > 0 && pointCursor.y < table
                            .getItemCount()
                            * table.getItemHeight() + table.getItemHeight()))) {
                currentColumnIndex = i;
                break;
            }
            width += widthColumn;
        }

        return currentColumnIndex;
    }

    public static TableColumn getTableColumn(Table table, Point pointCursor) {
        // searching current column index
        TableColumn[] columns = table.getColumns();
        int columnIndex = getColumnIndex(table, pointCursor);
        if (columnIndex != -1) {
            return columns[columnIndex];
        }
        return null;
    }

    public static int getItemIndex(Table table, Point pointCursor) {
        // searching current item index
        TableItem tableItemUnderCursor = table.getItem(pointCursor);
        TableItem[] tableItems = table.getItems();
        int currentItemIndex = -1;
        for (int i = 0; i < tableItems.length; i++) {
            if (tableItemUnderCursor == tableItems[i]) {
                currentItemIndex = i;
                break;
            }
        }
        return currentItemIndex;
    }

    /**
     * 
     * DOC amaumont Comment method "getTableItemFromPosition".
     * 
     * @param cursorPosition
     * @return
     */
    public static TableItem getTableItemFromDraggingPosition(Table table, Point cursorPosition) {
        Point pointCursor = table.toControl(cursorPosition.x, cursorPosition.y);
        return table.getItem(pointCursor);
    }

    public static TableItem getTableItem(Table table, Point pointCursor) {
        // searching current column index
        TableItem[] items = table.getItems();
        int itemIndex = getItemIndex(table, pointCursor);
        if (itemIndex != -1) {
            return items[itemIndex];
        }
        return null;
    }

    /**
     * DOC amaumont Comment method "getCursorPositionFromTableOrigin".
     * 
     * @param event
     * @return
     */
    public static Point getCursorPositionFromTableOrigin(Table table, Event event) {
        Point pointCursor = new Point(event.x, event.y);

        Widget widget = event.widget;
        if (widget instanceof TableItem) {
            widget = ((TableItem) widget).getParent();
        }

        if (widget != table && widget instanceof Control) {
            pointCursor = table.getDisplay().map((Control) widget, table, pointCursor);
        }
        return pointCursor;
    }

    /**
     * 
     * DOC amaumont Comment method "getItemIndexWhereInsertFromPosition".
     * 
     * @param table
     * @param cursorPosition
     * @return
     */
    public static int getItemIndexWhereInsertFromPosition(Table table, Point cursorPositionPrm) {
        Point cursorPosition = new Point(cursorPositionPrm.x, cursorPositionPrm.y);
        int startInsertAtThisIndex = 0;
        Point pointCursor = table.toControl(cursorPosition.x, cursorPosition.y);
        TableItem[] tableItems = table.getItems();
        TableItem tableItemBehindCursor = getTableItemFromDraggingPosition(table, cursorPosition);
        if (tableItemBehindCursor != null) {
            for (int i = 0; i < tableItems.length; i++) {
                if (tableItems[i] == tableItemBehindCursor) {
                    Rectangle boundsItem = tableItemBehindCursor.getBounds();
                    startInsertAtThisIndex = i;
                    if (pointCursor.y > boundsItem.y + table.getItemHeight()) {// table.getItemHeight()/2
                        startInsertAtThisIndex = i + 1;
                    }
                    break;
                }
            }
        } else if (pointCursor.y < table.getHeaderHeight()) {
            startInsertAtThisIndex = 0;
        } else {
            startInsertAtThisIndex = tableItems.length;
        }
        return startInsertAtThisIndex;
    }

    /**
     * DOC amaumont Comment method "getTableItem".
     * 
     * @param target
     */
    public static TableItem getTableItem(Table table, Object dataOfTableItem) {
        int itemIndex = getItemIndex(table, dataOfTableItem);
        if (itemIndex == -1) {
            return null;
        } else {
            return table.getItem(itemIndex);
        }
    }

    /**
     * DOC amaumont Comment method "getTableItem".
     * 
     * @param target
     */
    public static int getItemIndex(Table table, Object dataOfTableItem) {
        TableItem[] tableItems = table.getItems();
        for (int i = 0; i < tableItems.length; i++) {
            TableItem item = tableItems[i];
            if (item.getData() == dataOfTableItem) {
                return i;
            }
        }
        return -1;
    }

    // public static Rectangle getTableItemBounds(TableItem tableItem) {
    // Rectangle bounds = tableItem.getBounds();
    // if(WindowSystem.isGTK()) {
    // // bounds.y +
    // }
    // return null;
    // }

}
