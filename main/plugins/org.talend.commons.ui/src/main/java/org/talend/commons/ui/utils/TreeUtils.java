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
package org.talend.commons.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeUtils {

    public static TreeItem getTreeItem(Tree tree, Object dataOfTableItem) {
        TreeItem[] treeItems = tree.getItems();

        return getTreeItemFromData(dataOfTableItem, treeItems);

    }

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

}
