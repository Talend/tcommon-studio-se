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
package org.talend.commons.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: TableUtils.java 596 2006-11-17 15:12:15 +0000 (ven., 17 nov. 2006) amaumont $
 * 
 */
public class TreeUtils {

    // public static int getColumnIndex(Table table, Point pointCursor) {
    //
    // // searching current column index
    // int currentColumnIndex = -1;
    // TableColumn[] columns = table.getColumns();
    // for (int i = 0, width = 0; i < columns.length; i++) {
    // TableColumn column = columns[i];
    // int widthColumn = column.getWidth();
    // if (pointCursor.x >= width
    // && pointCursor.x <= width + widthColumn
    // && ((!WindowSystem.isGTK() && pointCursor.y > table.getHeaderHeight() && pointCursor.y < table.getHeaderHeight()
    // + table.getItemCount() * table.getItemHeight()) || (WindowSystem.isGTK() && pointCursor.y > 0 && pointCursor.y <
    // table
    // .getItemCount()
    // * table.getItemHeight()))) {
    // currentColumnIndex = i;
    // break;
    // }
    // width += widthColumn;
    // }
    //
    // return currentColumnIndex;
    // }
    //
    // public static TableColumn getTableColumn(Table table, Point pointCursor) {
    // // searching current column index
    // TableColumn[] columns = table.getColumns();
    // int columnIndex = getColumnIndex(table, pointCursor);
    // if (columnIndex != -1) {
    // return columns[columnIndex];
    // }
    // return null;
    // }
    //
    // public static int getItemIndex(Table table, Point pointCursor) {
    // // searching current item index
    // TableItem tableItemUnderCursor = table.getItem(pointCursor);
    // TableItem[] tableItems = table.getItems();
    // int currentItemIndex = -1;
    // for (int i = 0; i < tableItems.length; i++) {
    // if (tableItemUnderCursor == tableItems[i]) {
    // currentItemIndex = i;
    // break;
    // }
    // }
    // return currentItemIndex;
    // }
    //
    // /**
    // *
    // * DOC amaumont Comment method "getTableItemFromPosition".
    // *
    // * @param cursorPosition
    // * @return
    // */
    // public static TableItem getTableItemFromDraggingPosition(Table table, Point cursorPosition) {
    // Point pointCursor = table.toControl(cursorPosition.x, cursorPosition.y);
    // if (WindowSystem.isGTK()) {
    // pointCursor.y -= table.getHeaderHeight();
    // }
    // return table.getItem(pointCursor);
    // }
    //
    // public static TableItem getTableItem(Table table, Point pointCursor) {
    // // searching current column index
    // TableItem[] items = table.getItems();
    // int itemIndex = getItemIndex(table, pointCursor);
    // if (itemIndex != -1) {
    // return items[itemIndex];
    // }
    // return null;
    // }
    //
    // /**
    // * DOC amaumont Comment method "getCursorPositionFromTableOrigin".
    // *
    // * @param event
    // * @return
    // */
    // public static Point getCursorPositionFromTableOrigin(Table table, Event event) {
    // Point pointCursor = new Point(event.x, event.y);
    //
    // Widget widget = event.widget;
    // if (widget instanceof TableItem) {
    // widget = ((TableItem) widget).getParent();
    // }
    //
    // if (widget != table && widget instanceof Control) {
    // pointCursor = table.getDisplay().map((Control) widget, table, pointCursor);
    // }
    // return pointCursor;
    // }
    //
    // /**
    // *
    // * DOC amaumont Comment method "getItemIndexWhereInsertFromPosition".
    // *
    // * @param table
    // * @param cursorPosition
    // * @return
    // */
    // public static int getItemIndexWhereInsertFromPosition(Table table, Point cursorPosition) {
    // int startInsertAtThisIndex = 0;
    // Point pointCursor = table.toControl(cursorPosition.x, cursorPosition.y);
    // TableItem[] tableItems = table.getItems();
    // TableItem tableItemBehindCursor = getTableItemFromDraggingPosition(table, cursorPosition);
    // if (tableItemBehindCursor != null) {
    // for (int i = 0; i < tableItems.length; i++) {
    // if (tableItems[i] == tableItemBehindCursor) {
    // Rectangle boundsItem = tableItemBehindCursor.getBounds();
    // startInsertAtThisIndex = i;
    // if (pointCursor.y > boundsItem.y + table.getItemHeight() / 2 + (WindowSystem.isGTK() ? table.getHeaderHeight() :
    // 0)) {
    // startInsertAtThisIndex = i + 1;
    // }
    // break;
    // }
    // }
    // } else if (pointCursor.y < table.getHeaderHeight()) {
    // startInsertAtThisIndex = 0;
    // } else {
    // startInsertAtThisIndex = tableItems.length;
    // }
    // return startInsertAtThisIndex;
    // }

    /**
     * DOC amaumont Comment method "getTableItem".
     * 
     * @param target
     */
    public static TreeItem getTreeItem(Tree tree, Object dataOfTableItem) {
        TreeItem[] treeItems = tree.getItems();

        return getTreeItemFromData(dataOfTableItem, treeItems);

    }

    /**
     * DOC amaumont Comment method "getTreeItemFromData".
     * 
     * @param dataOfTableItem
     * @param treeItems
     */
    private static TreeItem getTreeItemFromData(Object dataOfTableItem, TreeItem[] treeItems) {
        TreeItem found = null;
        for (int i = 0; i < treeItems.length; i++) {
            if (dataOfTableItem == treeItems[i].getData()) {
                found = treeItems[i];
                break;
            } else if (treeItems[i].getItems().length != 0) {
                found = getTreeItemFromData(dataOfTableItem, treeItems[i].getItems());
                if (found != null) {
                    break;
                }
            }
        }
        return found;
    }

    /**
     * Find the next collapsed ascendant from the given item.
     * 
     * @param treeItem
     */
    private static TreeItem getNextCollapsedAscendant(TreeItem treeItem) {
        TreeItem parentItem = treeItem.getParentItem();
        if (parentItem == null) {
            return null;
        } else if (!parentItem.getExpanded()) {
            return parentItem;
        } else {
            TreeItem treeItemFound = getNextCollapsedAscendant(parentItem);
            if (treeItemFound != null) {
                return parentItem;
            } else {
                return null;
            }
        }

    }

    /**
     * Find the first visible ascendant item from the given item.
     * 
     * @param treeItem
     */
    public static TreeItem findFirstVisibleItemAscFrom(TreeItem treeItem) {
        TreeItem parentItem = treeItem.getParentItem();
        if (parentItem == null) {
            return treeItem;
        } else if (parentItem.getExpanded()) {
            TreeItem treeItemFound = TreeUtils.getNextCollapsedAscendant(parentItem);
            if (treeItemFound != null) {
                return findFirstVisibleItemAscFrom(treeItemFound);
            } else {
                return treeItem;
            }
        } else {
            return findFirstVisibleItemAscFrom(parentItem);
        }
    }

    public static List<TreeItem> collectAllItems(Tree tree) {
        List<TreeItem> list = new ArrayList<TreeItem>();

        TreeItem[] items = tree.getItems();
        for (TreeItem item : items) {
            list.add(item);
            list.addAll(collectAllItems(item));
        }
        return list;
    }

    public static List<TreeItem> collectAllItems(TreeItem treeItem) {
        List<TreeItem> list = new ArrayList<TreeItem>();

        list.add(treeItem);
        TreeItem[] items = treeItem.getItems();
        for (TreeItem item : items) {
            list.addAll(collectAllItems(item));
        }
        return list;
    }

    // /**
    // * DOC amaumont Comment method "getTableItem".
    // *
    // * @param target
    // */
    // public static int getItemIndex(Table table, Object dataOfTableItem) {
    // TableItem[] tableItems = table.getItems();
    // for (int i = 0; i < tableItems.length; i++) {
    // TableItem item = tableItems[i];
    // if (item.getData() == dataOfTableItem) {
    // return i;
    // }
    // }
    // return -1;
    // }

}
