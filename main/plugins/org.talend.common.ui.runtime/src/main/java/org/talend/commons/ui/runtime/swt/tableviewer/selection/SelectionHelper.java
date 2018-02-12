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
package org.talend.commons.ui.runtime.swt.tableviewer.selection;

import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: SelectionHelper.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class SelectionHelper {

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private ListenerList beforeSelectionListeners = new ListenerList();

    private ListenerList afterSelectionListeners = new ListenerList();

    private ISelectionChangedListener postSelectionChangedListener;

    private ISelectionChangedListener selectionChangedListener;

    private MouseTableSelectionHelper mouseTableSelectionHelper;

    private boolean activeFireSelectionChanged = true;

    /**
     * DOC amaumont SelectionHelper constructor comment.
     * 
     * @param tableViewerCreator
     * @param mouseTableSelectionHelper
     */
    public SelectionHelper(TableViewerCreatorNotModifiable tableViewerCreator, MouseTableSelectionHelper mouseTableSelectionHelper) {
        this.tableViewerCreator = tableViewerCreator;
        this.mouseTableSelectionHelper = mouseTableSelectionHelper;
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init() {
        // this.tableSelectionListener = new SelectionListener() {
        //
        // public void widgetDefaultSelected(SelectionEvent e) {
        //
        // }
        //
        // public void widgetSelected(SelectionEvent e) {
        // // LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        // // fireAfterSelectionChanged(lineSelectionEvent);
        // }
        //
        // };

        this.selectionChangedListener = new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
                lineSelectionEvent.source = tableViewerCreator;
                fireBeforeSelectionChanged(lineSelectionEvent);
            }

        };

        this.postSelectionChangedListener = new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
                lineSelectionEvent.source = tableViewerCreator;
                fireAfterSelectionChanged(lineSelectionEvent);
            }

        };

    }

    private void fireBeforeSelectionChanged(LineSelectionEvent lineSelectionEvent) {
        if (!this.activeFireSelectionChanged) {
            return;
        }
        Object[] listeners = beforeSelectionListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ILineSelectionListener l = (ILineSelectionListener) listeners[i];
            l.handle(lineSelectionEvent);
        }
    }

    private void fireAfterSelectionChanged(LineSelectionEvent lineSelectionEvent) {
        if (!this.activeFireSelectionChanged) {
            return;
        }
        Object[] listeners = afterSelectionListeners.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            final ILineSelectionListener l = (ILineSelectionListener) listeners[i];
            l.handle(lineSelectionEvent);
        }
    }

    /**
     * DOC amaumont Comment method "addListeners".
     */
    private void addListeners() {
        // this.tableViewerCreator.getTable().addSelectionListener(this.tableSelectionListener);
        this.tableViewerCreator.getTableViewer().addPostSelectionChangedListener(this.postSelectionChangedListener);
        this.tableViewerCreator.getTableViewer().addSelectionChangedListener(this.selectionChangedListener);
    }

    private void removeListeners() {
        // this.tableViewerCreator.getTable().removeSelectionListener(this.tableSelectionListener);
        this.tableViewerCreator.getTableViewer().removePostSelectionChangedListener(this.postSelectionChangedListener);
        this.tableViewerCreator.getTableViewer().removeSelectionChangedListener(this.selectionChangedListener);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param selection
     * @param reveal
     * @see TableViewer#setSelection(ISelection, boolean)
     */
    public void setSelection(ISelection selection, boolean reveal) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTableViewer().setSelection(selection, reveal);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param selection
     * @see TableViewer#setSelection(ISelection)
     */
    public void setSelection(ISelection selection) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTableViewer().setSelection(selection);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param start
     * @param end
     * @see Table#setSelection(int, int)
     */
    public void setSelection(int start, int end) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().setSelection(start, end);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param index
     * @see Table#setSelection(int)
     */
    public void setSelection(int index) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().setSelection(index);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param indices
     * @see Table#setSelection(int[])
     */
    public void setSelection(int[] indices) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().setSelection(indices);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param item
     * @see Table#setSelection(TableItem)
     */
    public void setSelection(TableItem item) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().setSelection(item);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "setSelection".
     * 
     * @param items
     * @see Table#setSelection(TableItem[])
     */
    public void setSelection(TableItem[] items) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().setSelection(items);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "deselect".
     * 
     * @param start
     * @param end
     * @see Table#deselect(int, int)
     */
    public void deselect(int start, int end) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().deselect(start, end);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "deselect".
     * 
     * @param index
     * @see Table#deselect(int)
     */
    public void deselect(int index) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().deselect(index);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "deselect".
     * 
     * @param indices
     * @see Table#deselect(int[])
     */
    public void deselect(int[] indices) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().deselect(indices);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "deselectAll".
     * 
     * @see Table#deselectAll()
     */
    public void deselectAll() {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().deselectAll();
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "select".
     * 
     * @param start
     * @param end
     * @see Table#select(int, int)
     */
    public void select(int start, int end) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().select(start, end);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "select".
     * 
     * @param index
     * @see Table#select(int)
     */
    public void select(int index) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().select(index);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "select".
     * 
     * @param indices
     * @see Table#select(int[])
     */
    public void select(int[] indices) {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().select(indices);
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "selectAll".
     * 
     * @see Table#selectAll()
     */
    public void selectAll() {
        LineSelectionEvent lineSelectionEvent = new LineSelectionEvent();
        lineSelectionEvent.selectionByMethod = true;
        lineSelectionEvent.source = tableViewerCreator;
        fireBeforeSelectionChanged(lineSelectionEvent);
        this.tableViewerCreator.getTable().selectAll();
        fireAfterSelectionChanged(lineSelectionEvent);
    }

    /**
     * 
     * DOC amaumont Comment method "addSelectionListener".
     * 
     * Listeners on <code>Table</code> and <code>TableViewer</code> are registered when number of listeners is 0.
     * 
     * @param lineSelectionListener
     */
    public void addBeforeSelectionListener(ILineSelectionListener lineSelectionListener) {
        if (beforeSelectionListeners.size() == 0 && afterSelectionListeners.size() == 0) {
            addListeners();
        }
        beforeSelectionListeners.add(lineSelectionListener);
    }

    /**
     * 
     * DOC amaumont Comment method "removeSelectionListener". Listeners on <code>Table</code> and
     * <code>TableViewer</code> are unregistered when number of listeners become 0.
     * 
     * @param lineSelectionListener
     */
    public void removeBeforeSelectionListener(ILineSelectionListener lineSelectionListener) {
        if (lineSelectionListener != null) {
            beforeSelectionListeners.remove(lineSelectionListener);
        }
        if (beforeSelectionListeners.size() == 0 && afterSelectionListeners.size() == 0) {
            removeListeners();
        }
    }

    /**
     * 
     * DOC amaumont Comment method "addSelectionListener".
     * 
     * Listeners on <code>Table</code> and <code>TableViewer</code> are registered when number of listeners is 0.
     * 
     * @param lineSelectionListener
     */
    public void addAfterSelectionListener(ILineSelectionListener lineSelectionListener) {
        if (beforeSelectionListeners.size() == 0 && afterSelectionListeners.size() == 0) {
            addListeners();
        }
        afterSelectionListeners.add(lineSelectionListener);
    }

    /**
     * 
     * DOC amaumont Comment method "removeSelectionListener". Listeners on <code>Table</code> and
     * <code>TableViewer</code> are unregistered when number of listeners become 0.
     * 
     * @param lineSelectionListener
     */
    public void removeAfterSelectionListener(ILineSelectionListener lineSelectionListener) {
        if (lineSelectionListener != null) {
            afterSelectionListeners.remove(lineSelectionListener);
        }
        if (beforeSelectionListeners.size() == 0 && afterSelectionListeners.size() == 0) {
            removeListeners();
        }
    }

    public boolean isMouseSelectionning() {
        if (mouseTableSelectionHelper == null) {
            return false;
        }
        return mouseTableSelectionHelper.isDraggingOnSelectionColumn();
    }

    /**
     * 
     * DOC amaumont Comment method "setActiveFireChanged". activeFireChanged is true by default.
     * 
     * @param activeFireChanged
     */
    public void setActiveFireSelectionChanged(boolean activeFireChanged) {
        this.activeFireSelectionChanged = activeFireChanged;
    }

    public boolean isActiveFireSelectionChanged() {
        return this.activeFireSelectionChanged;
    }

}
