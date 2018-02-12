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
package org.talend.commons.ui.runtime.swt.tableviewer.tableeditor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.set.MapBackedSet;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.ITableColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.IColumnSortedListener;
import org.talend.commons.ui.runtime.thread.AsynchronousThreading;
import org.talend.commons.utils.data.list.IListenableListListener;
import org.talend.commons.utils.data.list.ListenableList;
import org.talend.commons.utils.data.list.ListenableListEvent;
import org.talend.commons.utils.data.list.ListenableListEvent.TYPE;
import org.talend.commons.utils.data.map.MultiLazyValuesMap;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * $Id: TableEditorManager.java,v 1.3 2006/06/02 15:24:10 amaumont Exp $
 */
public class TableEditorManager {

    /**
     *
     * Event type. <br/>
     *
     * $Id: TableEditorManager.java 7183 2007-11-23 11:03:36Z amaumont $
     *
     */
    public enum EVENT_TYPE implements ITableEditorManagerEventType {
        CONTROL_CREATED,
        CONTROL_DISPOSED,
    };

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private List<TableEditor> tableEditorList = new ArrayList<TableEditor>();

    List<ITableEditorManagerListener> listeners = new ArrayList<ITableEditorManagerListener>();

    private ListenableList listenableList;

    private List<TableViewerCreatorColumnNotModifiable> columns;

    private List<TableViewerCreatorColumnNotModifiable> columnsWithEditorContent;

    private HashSet<TableItem> previousItemsHash;

    // //////////////////////////////////
    // Warning: using identity comparison
    // //////////////////////////////////
    MultiLazyValuesMap dataToMultipleDataEditor = new MultiLazyValuesMap(new IdentityHashMap()) {

        @Override
        public Collection instanciateNewCollection() {
            return new ArrayList();
        }

    };

    private IListenableListListener listenableListListener;

    public void addListener(ITableEditorManagerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ITableEditorManagerListener listener) {
        listeners.remove(listener);
    }

    protected void fireEvent(TableEditorManagerEvent event) {
        int listenersListSize = listeners.size();
        for (int i = 0; i < listenersListSize; i++) {
            ITableEditorManagerListener listener = listeners.get(i);
            listener.notifyEvent(event);
        }
    }

    /**
     * DOC amaumont TableEditorManager constructor comment.
     *
     * @param tableViewerCreator
     */
    public TableEditorManager(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;
        columns = tableViewerCreator.getColumns();
        columnsWithEditorContent = new ArrayList<TableViewerCreatorColumnNotModifiable>(columns.size());

        for (int iCol = 0; iCol < columns.size(); iCol++) {
            TableViewerCreatorColumnNotModifiable column = columns.get(iCol);
            TableEditorContentNotModifiable tableEditorContent = column.getTableEditorContent();
            if (tableEditorContent != null) {
                columnsWithEditorContent.add(column);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void init(ListenableList listenableList) {
        if (listenableList != this.listenableList) {
            releaseListenableListListener();

            List<TableViewerCreatorColumnNotModifiable> columns = tableViewerCreator.getColumns();
            for (TableViewerCreatorColumnNotModifiable column : columns) {
                if (column.getTableColumnSelectionListener() instanceof ITableColumnSelectionListener) {
                    column.getTableColumnSelectionListener().addColumnSortedListener(new IColumnSortedListener() {

                        @Override
                        public void handle() {
                            refresh();
                        }

                    });
                }
            }

            if (this.listenableListListener == null) {

                this.listenableListListener = new IListenableListListener() {

                    @Override
                    public void handleEvent(ListenableListEvent event) {

                        if (event.type == TYPE.ADDED) {
                            handleAddedEventAsynchronous(event);
                        } else if (event.type == TYPE.REMOVED) {
                            handleRemovedEventAsynchronous(event);
                        } else if (event.type == TYPE.SWAPED) {
                            handleSwapedEvent(event);
                        } else if (event.type == TYPE.LIST_REGISTERED || event.type == TYPE.CLEARED
                                || event.type == TYPE.REPLACED) {
                            refresh();
                        }
                    }

                };
            }

            // System.out.println("TableEditor " + this.hashCode() + " Listener " + listenableListListener.hashCode()
            // + " added into listenableList " + listenableList.hashCode());

            listenableList.addPostOperationListener(listenableListListener);

        }

        this.listenableList = listenableList;

    }

    private void handleAddedEventAsynchronous(final ListenableListEvent event) {

        final Composite compositeParent = tableViewerCreator.getCompositeParent();

        if (!compositeParent.isDisposed()) {

            new AsynchronousThreading(10, true, compositeParent.getDisplay(), new Runnable() {

                @Override
                public void run() {
                    if (!compositeParent.isDisposed()) {
                        handleAddedEvent(event);
                    }
                }

            }).start();
        }
    }

    private void handleRemovedEventAsynchronous(final ListenableListEvent event) {

        final Composite compositeParent = tableViewerCreator.getCompositeParent();

        if (!compositeParent.isDisposed()) {

            new AsynchronousThreading(10, true, tableViewerCreator.getCompositeParent().getDisplay(), new Runnable() {

                @Override
                public void run() {
                    if (!compositeParent.isDisposed()) {
                        handleRemovedEvent(event);
                    }
                }

            }).start();

        }

    }

    private void handleAddedEvent(final ListenableListEvent event) {

        int indexStart = event.index;

        if (tableViewerCreator.getTable().isDisposed() || tableViewerCreator.isLazyLoad()) {
            return;
        }

        TableItem[] items = tableViewerCreator.getTable().getItems();

        List<TableEditor> addedTableEditor = new ArrayList<TableEditor>();

        for (TableItem item : items) {
            TableItem tableItem = item;
            if (previousItemsHash == null || !previousItemsHash.contains(tableItem)) {

                for (int iEditorCol = 0; iEditorCol < columnsWithEditorContent.size(); iEditorCol++) {
                    TableViewerCreatorColumnNotModifiable column = columnsWithEditorContent.get(iEditorCol);

                    TableEditorContentNotModifiable tableEditorContent = column.getTableEditorContent();

                    String idProperty = column.getId();

                    TableEditor tableEditor = addTableEditor(column, tableEditorContent, idProperty, tableItem);
                    if (tableEditor != null) {
                        addedTableEditor.add(tableEditor);
                    }

                }
            }
        }

        for (int i = indexStart; i < items.length; i++) {
            TableItem tableItem = items[i];
            Object data = tableItem.getData();
            Collection<TableEditor> tableEditorCollection = dataToMultipleDataEditor.getCollection(data);
            for (TableEditor tableEditor : tableEditorCollection) {
                tableEditor.setItem(tableItem);
            }
        }

        previousItemsHash = new HashSet<TableItem>(Arrays.asList(items));
    }

    private void handleRemovedEvent(final ListenableListEvent event) {

        Collection removedObjects = event.removedObjects;

        for (Object data : removedObjects) {
            Collection<TableEditor> collection = dataToMultipleDataEditor.getCollection(data);
            for (TableEditor tableEditor : collection) {
                tableEditorList.remove(tableEditor);
                disposeTableEditor(tableEditor);
            }
            dataToMultipleDataEditor.remove(data);
        }

        int tableEditorListSize = tableEditorList.size();
        for (int i = 0; i < tableEditorListSize; i++) {
            TableEditor tableEditor = tableEditorList.get(i);
            tableEditor.layout();
        }

    }

    private void disposeTableEditor(TableEditor tableEditor) {
        Control editor = tableEditor.getEditor();
        if (editor != null && !editor.isDisposed()) {
            editor.dispose();
        }
        tableEditor.dispose();
    }

    private void handleSwapedEvent(ListenableListEvent event) {

        if (tableViewerCreator.getTable().isDisposed()) {
            return;
        }

        Table table = tableViewerCreator.getTable();
        TableItem[] items = table.getItems();

        // //////////////////////////////////
        // Warning: using identity comparison
        // //////////////////////////////////
        Set dataHash = MapBackedSet.decorate(new IdentityHashMap());
        dataHash.addAll(Arrays.asList(event.swapedObjects));

        for (TableItem tableItem : items) {
            Object data = tableItem.getData();
            if (dataHash.contains(data)) {
                Collection<TableEditor> tableEditorCollection = dataToMultipleDataEditor.getCollection(data);
                for (TableEditor tableEditor : tableEditorCollection) {
                    tableEditor.setItem(tableItem);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    public void refresh() {
        if (tableEditorList == null) {// widget have been disposed and release method called so ignore the refresh
            return;
        }
        for (int i = 0; i < tableEditorList.size(); i++) {
            TableEditor tableEditor = tableEditorList.get(i);
            disposeTableEditor(tableEditor);
            fireEvent(new TableEditorManagerEvent(EVENT_TYPE.CONTROL_DISPOSED, tableEditor));
        }

        tableEditorList.clear();
        dataToMultipleDataEditor.clear();

        Table table = tableViewerCreator.getTable();
        if (table.isDisposed()) {
            return;
        }

        TableItem[] items = table.getItems();

        previousItemsHash = new HashSet<TableItem>(Arrays.asList(items));

        for (int iEditorCol = 0; iEditorCol < columnsWithEditorContent.size(); iEditorCol++) {
            TableViewerCreatorColumnNotModifiable column = columnsWithEditorContent.get(iEditorCol);

            TableEditorContentNotModifiable tableEditorContent = column.getTableEditorContent();

            String idProperty = column.getId();

            for (TableItem item : items) {
                addTableEditor(column, tableEditorContent, idProperty, item);
            }
        }

    }

    public void paintVisibleAreaEditor(PaintEvent event) {
        Table table = tableViewerCreator.getTable();
        if (table.isDisposed()) {
            return;
        }

        int topIndex = table.getTopIndex();
        int itemsCount = table.getItemCount();
        int itemHeight = table.getItemHeight();
        GC gc = event.gc;
        Rectangle clipping = gc.getClipping();
        int headerHeight = table.getHeaderHeight();
        int startIndex = (clipping.y - headerHeight) / itemHeight + topIndex;
        int endIndex = -1;
        if (startIndex < itemsCount) {
            endIndex = startIndex + (int) Math.ceil((float) clipping.height / itemHeight);
        }
        startIndex = Math.max(0, startIndex);
        endIndex = Math.min(endIndex, itemsCount - 1);
        // Set the cell editor if need
        for (int itemIndex = topIndex; itemIndex <= endIndex; itemIndex++) {
            TableItem item = table.getItem(itemIndex);
            if (item.isDisposed()) {
                continue;
            }
            for (int columnIndex = 0; columnIndex < columnsWithEditorContent.size(); columnIndex++) {
                TableViewerCreatorColumnNotModifiable column = columnsWithEditorContent.get(columnIndex);
                TableEditorContentNotModifiable tableEditorContent = column.getTableEditorContent();

                if (dataToMultipleDataEditor.containsKey(item.getData())) {
                    Collection<TableEditor> tableEditorCollection = dataToMultipleDataEditor.getCollection(item.getData());
                    for (TableEditor tableEditor : tableEditorCollection) {
                        tableEditor.setItem(item);
                        Control editor = tableEditor.getEditor();
                        if (editor != null) {
                            editor.redraw();
                        }
                    }
                    if (columnIndex < tableEditorCollection.size()) {
                        continue;
                    } else {
                        addTableEditor(column, tableEditorContent, column.getId(), item);
                    }
                } else {
                    addTableEditor(column, tableEditorContent, column.getId(), item);
                }
            }
        }
    }

    public void refreshColumn(int index) {
        Table table = tableViewerCreator.getTable();
        if (table.isDisposed() || index >= table.getItems().length) {
            return;
        }
       int topIndex = table.getTopIndex();
       int itemCount = table.getItemCount();
        int numVisibleLines = Math.min((table.getBounds().height / table.getItemHeight()) + 2, itemCount - topIndex);
       if (index < topIndex || index > topIndex + numVisibleLines) {
           return;
        }
        for (int iEditorCol = 0; iEditorCol < columnsWithEditorContent.size(); iEditorCol++) {
            TableViewerCreatorColumnNotModifiable column = columnsWithEditorContent.get(iEditorCol);

            TableEditorContentNotModifiable tableEditorContent = column.getTableEditorContent();

            String idProperty = column.getId();

            TableItem tableItem = table.getItems()[index];
            if (dataToMultipleDataEditor.containsKey(tableItem.getData())) {
                Collection<TableEditor> object = dataToMultipleDataEditor.getCollection(tableItem.getData());
                for (TableEditor tableEditor : object) {
                    tableEditor.setItem(tableItem);
                    Control editor = tableEditor.getEditor();
                    if (editor != null) {
                        editor.redraw();
                    }
                }
                if (object.size() == columnsWithEditorContent.size()) {
                    break;
                } else {
                    if (iEditorCol < object.size()) {
                        continue;
                    } else {
                        addTableEditor(column, tableEditorContent, idProperty, table.getItems()[index]);
                    }
                }
            } else {
                addTableEditor(column, tableEditorContent, idProperty, table.getItems()[index]);
            }
        }
    }

    private TableEditor addTableEditor(TableViewerCreatorColumnNotModifiable column,
            TableEditorContentNotModifiable tableEditorContent, String idProperty, TableItem tableItem) {
        if (tableItem.getData() == null) {
            return null;
        }
        if (tableEditorContent == null) {
            return null;
        }

        tableEditorContent.setLayoutEnabled(true);
        TableEditor tableEditor = tableEditorContent.createTableEditor(tableItem.getParent());
        tableEditorList.add(tableEditor);
        dataToMultipleDataEditor.put(tableItem.getData(), tableEditor);

        Object currentRowObject = tableItem.getData();
        Object value = null;
        if (column.getBeanPropertyAccessors() != null) {
            value = column.getBeanPropertyAccessors().get(tableItem.getData());
        } else {
            value = tableViewerCreator.getCellModifier().getValue(currentRowObject, idProperty);
        }

        Control control = tableEditorContent.initialize(tableItem.getParent(), tableEditor, column, currentRowObject, value);

        if (tableItem != null && !tableItem.isDisposed()) {
            tableEditor.setEditor(control, tableItem, column.getIndex());
            fireEvent(new TableEditorManagerEvent(EVENT_TYPE.CONTROL_CREATED, tableEditor));
        }

        return tableEditor;
    }

    /**
     * DOC amaumont Comment method "redrawAll".
     */
    public void redrawControls() {
        for (TableEditor tableEditor : tableEditorList) {
            Control editor = tableEditor.getEditor();
            if (editor != null) {
                editor.redraw();
            }
        }
    }

    public List<TableEditor> getTableEditorList() {
        return Collections.unmodifiableList(this.tableEditorList);
    }

    public void release() {

        releaseListenableListListener();
        listenableListListener = null;
        tableViewerCreator = null;
        int tableEditorListListSize = tableEditorList.size();
        for (int i = 0; i < tableEditorListListSize; i++) {
            TableEditor tableEditor = tableEditorList.get(i);
            disposeTableEditor(tableEditor);
        }
        tableEditorList = null;
        listeners = null;
        listenableList = null;
        columns = null;
        columnsWithEditorContent = null;
        previousItemsHash = null;
        dataToMultipleDataEditor = null;
    }

    private void releaseListenableListListener() {
        if (listenableList != null && listenableListListener != null) {
            listenableList.removeListener(listenableListListener);

            // System.out.println("TableEditor " + this.hashCode() + " Listener " + listenableListListener.hashCode()
            // + " REMOVED into listenableList " + listenableList.hashCode());

        }
    }

}
