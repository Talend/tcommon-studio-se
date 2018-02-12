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
/***********************************************************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 **********************************************************************************************************************/
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorColumnNotModifiable;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.thread.AsynchronousThreading;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.utils.threading.ExecutionLimiter;

/**
 * A layout for a table. Call <code>addColumnData</code> to add columns.
 * 
 * DefaultTableReflectLayout is based on <code>TableLayout</code> class.
 * 
 * This supports dynamically resize of columns of table parent composite when you set true <code>continuousLayout</code>
 * .
 * 
 * You can now force the layout with the <code>forceLayout</code> method, indeed the current layout method is processed
 * by default only once time.
 * 
 * $Id: TableViewerCreatorLayout.java 7042 2007-11-15 15:13:48Z smallet $
 */
public class TableViewerCreatorLayout extends Layout {

    /**
     * The number of extra pixels taken as horizontal trim by the table column. To ensure there are N pixels available
     * for the content of the column, assign N+COLUMN_TRIM for the column width.
     * 
     * @since 3.1
     */
    private static final int COLUMN_TRIM = "carbon".equals(SWT.getPlatform()) ? 24 : 3; //$NON-NLS-1$

    /**
     * The list of column layout data (element type: <code>ColumnLayoutData</code>).
     */
    private List<ColumnLayoutData> columnsLayoutData = new ArrayList<ColumnLayoutData>();

    /**
     * Indicates whether <code>layout</code> has yet to be called.
     */
    private boolean firstTime = true;

    /**
     * Used to adjust parent width if necessary.
     */
    private int widthAdjustValue;

    /**
     * All layout calls are processed if true, only once if false.
     */
    private boolean continuousLayout;

    private ExecutionLimiter layoutExecutionLimiter;

    private boolean fillHorizontal;

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private boolean columnControlListenersInitialized;

    protected int referenceWidth;

    private boolean columnsResizingByLayout;

    private int lastDisplayedWidth;

    protected boolean manualResizing;

    protected Boolean mouseIsDown;

    private AsynchronousThreading asyncThreadingForManualColumnResizingFalse;

    /**
     * Creates a new table layout.
     */
    public TableViewerCreatorLayout(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init() {
        this.asyncThreadingForManualColumnResizingFalse = new AsynchronousThreading(500, false, tableViewerCreator
                .getCompositeParent().getDisplay(), new Runnable() {

            public void run() {
                // System.out.println("manualResizing = false !!!!!!!!!!!!!!!!!!!!!!!!!!");
                manualResizing = false;
                // tableViewerCreator.layout();
            }

        });

    }

    /**
     * Adds a new column of data to this table layout.
     * 
     * @param data the column layout data
     */
    public void addColumnData(ColumnLayoutData data) {
        columnsLayoutData.add(data);
    }

    /*
     * (non-Javadoc) Method declared on Layout.
     */
    public Point computeSize(Composite c, int wHint, int hHint, boolean flush) {
        if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
            return new Point(wHint, hHint);
        }

        Table table = (Table) c;
        // To avoid recursions.
        table.setLayout(null);
        // Use native layout algorithm
        Point result = table.computeSize(wHint, hHint, flush);
        table.setLayout(this);

        int width = computeAskedWidth();
        if (width > result.x) {
            result.x = width;
        }
        return result;
    }

    /**
     * 
     * Add columns width.
     * 
     * @return width of table
     */
    private int computeCurrentTableWidth() {
        TableColumn[] tableColumns = tableViewerCreator.getTable().getColumns();
        int width = 0;
        for (int i = 0; i < tableColumns.length; i++) {
            width += tableColumns[i].getWidth();
        }
        return width;
    }

    private int computeAskedWidth() {
        int width = 0;
        int size = columnsLayoutData.size();
        for (int i = 0; i < size; ++i) {
            ColumnLayoutData layoutData = (ColumnLayoutData) columnsLayoutData.get(i);
            if (layoutData instanceof ColumnPixelData) {
                ColumnPixelData col = (ColumnPixelData) layoutData;
                width += col.width;
                if (col.addTrim) {
                    width += COLUMN_TRIM;
                }
            } else if (layoutData instanceof ColumnWeightData) {
                ColumnWeightData col = (ColumnWeightData) layoutData;
                width += col.minimumWidth;
            } else {
                Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
            }

        }
        return width;
    }

    /*
     * (non-Javadoc) Method declared on Layout.
     */
    public void layout(final Composite c, boolean flush) {

        // System.out.println("manualResizing=" + manualResizing);
        // System.out.println("layout=" + this.hashCode());
        if (manualResizing) {
            return;
        }

        // System.out.println("TableViewerCreatorLayout layout " + toString() + " "+ (i++) );

        if (fillHorizontal || continuousLayout) {
            initColumnsControlListener();
        }

        if (layoutExecutionLimiter == null) {
            // layoutExecutionLimiter = new ExecutionLimiter(this.timeBetweenTwoLayouts, true) {
            //
            // @Override
            // public void execute(boolean isFinalExecution) {

            if (c.isDisposed()) {
                return;
            }

            // c.getDisplay().syncExec(new Runnable() {
            //
            // /* (non-Javadoc)
            // * @see java.lang.Runnable#run()
            // */
            // public void run() {
            //
            if (!firstTime && !continuousLayout) {
                return;
            }

            try {
                if (tableViewerCreator.getTable().isDisposed()) {
                    return;
                }
                tableViewerCreator.getTable().setLayout(null);

                layout(c);

            } finally {
                if (!tableViewerCreator.getTable().isDisposed()) {
                    tableViewerCreator.getTable().setLayout(TableViewerCreatorLayout.this);
                }
            }

        }

        // });
        // }
        //
        // };
        // }

        // layoutExecutionLimiter.startIfExecutable();

    }

    private void layout(final Composite c) {
        // System.out.println("Layout" + System.currentTimeMillis());
        final Table table = (Table) c;
        Rectangle bounds = table.getBounds();
        Rectangle clientArea = table.getClientArea();
        if (WindowSystem.isGTK()) {
            bounds = table.getClientArea();
        } else {
            bounds = table.getBounds();
        }

        int displayedWidth = 0;

        int heightFilledByRows = table.getItemCount() * table.getItemHeight() + table.getHeaderHeight();
        if (firstTime) {
            displayedWidth = bounds.width - 2 * c.getBorderWidth() + widthAdjustValue;
            if (bounds.height < heightFilledByRows) {
                displayedWidth += -table.getVerticalBar().getSize().x;
            }
        } else {
            if (WindowSystem.isGTK()) {
                heightFilledByRows -= table.getHeaderHeight();
            }

            if (fillHorizontal) {
                if (bounds.height < heightFilledByRows) {
                    displayedWidth = clientArea.width + widthAdjustValue;
                } else {
                    displayedWidth = bounds.width + widthAdjustValue;
                }
                referenceWidth = displayedWidth;
                lastDisplayedWidth = displayedWidth;

            } else {
                int newVisibleWidth = bounds.width + widthAdjustValue;
                if (bounds.height < heightFilledByRows) {
                    newVisibleWidth += -table.getVerticalBar().getSize().x;
                }

                displayedWidth = referenceWidth - 2 * c.getBorderWidth() - (lastDisplayedWidth - newVisibleWidth);
            }

        }

        if (firstTime) {
            referenceWidth = displayedWidth;
            lastDisplayedWidth = displayedWidth;
        }

        // XXX: Layout is being called with an invalid value the first time
        // it is being called on Linux. This method resets the
        // Layout to null so we make sure we run it only when
        // the value is OK.
        if (displayedWidth <= 1) {
            return;
        }

        Item[] tableColumns = getColumns(c);
        int size = Math.min(columnsLayoutData.size(), tableColumns.length);
        int[] widths = new int[size];
        int fixedWidth = 0;
        int numberOfWeightColumns = 0;
        int totalWeight = 0;

        // First calc space occupied by fixed columns
        for (int i = 0; i < size; i++) {
            ColumnLayoutData col = (ColumnLayoutData) columnsLayoutData.get(i);
            if (col instanceof ColumnPixelData) {
                ColumnPixelData cpd = (ColumnPixelData) col;
                int pixels = cpd.width;
                if (cpd.addTrim) {
                    pixels += COLUMN_TRIM;
                }
                widths[i] = pixels;
                fixedWidth += pixels;
            } else if (col instanceof ColumnWeightData) {
                ColumnWeightData cw = (ColumnWeightData) col;
                numberOfWeightColumns++;
                // first time, use the weight specified by the column data,
                // otherwise use the actual width as the weight
                // int weight = firstTime ? cw.weight :
                // tableColumns[i].getWidth();
                int weight = cw.weight;
                totalWeight += weight;
            } else {
                Assert.isTrue(false, "Unknown column layout data"); //$NON-NLS-1$
            }
        }

        // Do we have columns that have a weight
        if (numberOfWeightColumns > 0) {
            // Now distribute the rest to the columns with weight.
            int rest = displayedWidth - fixedWidth;
            int totalDistributed = 0;
            for (int i = 0; i < size; ++i) {
                ColumnLayoutData col = (ColumnLayoutData) columnsLayoutData.get(i);
                if (col instanceof ColumnWeightData) {
                    ColumnWeightData cw = (ColumnWeightData) col;
                    // calculate weight as above
                    // int weight = firstTime ? cw.weight :
                    // tableColumns[i].getWidth();
                    int weight = cw.weight;
                    int pixels = totalWeight == 0 ? 0 : weight * rest / totalWeight;
                    if (pixels < cw.minimumWidth) {
                        pixels = cw.minimumWidth;
                    }
                    totalDistributed += pixels;
                    widths[i] = pixels;
                }
            }

            // Distribute any remaining pixels to columns with weight.
            int diff = rest - totalDistributed;
            for (int i = 0; diff > 0; ++i) {
                if (i == size) {
                    i = 0;
                }
                ColumnLayoutData col = (ColumnLayoutData) columnsLayoutData.get(i);
                if (col instanceof ColumnWeightData) {
                    ++widths[i];
                    --diff;
                }
            }
        }

        columnsResizingByLayout = true;

        final boolean previousVisible = table.getVisible();
        if (previousVisible) {
            table.setVisible(false);
        }

        for (int i = 0; i < size; i++) {
            setWidth(tableColumns[i], widths[i]);
        }
        table.setVisible(previousVisible);
        columnsResizingByLayout = false;
        firstTime = false;

    }

    /**
     * 
     * DOC amaumont Comment method "initColumnsControlListener".
     */
    private void initColumnsControlListener() {

        if (columnControlListenersInitialized) {
            return;
        }

        columnControlListenersInitialized = true;

        ControlListener controlListener = new ControlListener() {

            public void controlMoved(ControlEvent e) {
                // System.out.println("COntrol moved");
            }

            public void controlResized(ControlEvent e) {
                // System.out.println("TableColumn controlResized");
                controlResizedExecute(e);
            }

        };

        final Table table = tableViewerCreator.getTable();
        TableColumn[] tableColumns = table.getColumns();
        for (int i = 0; i < tableColumns.length; i++) {
            tableColumns[i].addControlListener(controlListener);
        }

    }

    /**
     * Set the width of the item.
     * 
     * @param item
     * @param width
     */
    private void setWidth(Item item, int width) {
        if (item instanceof TreeColumn) {
            ((TreeColumn) item).setWidth(width);
        } else {
            ((TableColumn) item).setWidth(width);
        }

    }

    // /**
    // * Set the width of the item.
    // *
    // * @param item
    // * @param width
    // */
    // private int getWidth(Item item) {
    // if (item instanceof TreeColumn) {
    // return ((TreeColumn) item).getWidth();
    // } else {
    // return ((TableColumn) item).getWidth();
    // }
    //
    // }

    /**
     * Force the layout even if it is not the first layout.
     * 
     * @param composite
     * @param flush
     */
    public void forceLayout(Composite composite) {
        firstTime = true;
        layout(composite, true);
    }

    /**
     * Return the columns for the receiver.
     * 
     * @param composite
     * @return Item[]
     */
    private Item[] getColumns(Composite composite) {
        if (composite instanceof Tree) {
            return ((Tree) composite).getColumns();
        }
        return ((Table) composite).getColumns();
    }

    /**
     * Returns whether layout is really processed at each call.
     * 
     * @return <code>true</code> if layout is really processed at each call, and <code>false</code> otherwise
     */
    public boolean isContinuousLayout() {
        return this.continuousLayout;
    }

    /**
     * Sets if if layout must be really processed at each call or not.
     * 
     * @param continuousLayout <code>true</code> if layout must be really processed at each call, and <code>false</code>
     * otherwise
     */
    public void setContinuousLayout(boolean continuousLayout) {
        this.continuousLayout = continuousLayout;
    }

    /**
     * Returns widthAdjustValue which is used to adjust rendering.
     * 
     * @return current widthAdjustValue
     */
    public int getWidthAdjustValue() {
        return this.widthAdjustValue;
    }

    public void setWidthAdjustValue(int widthAdjustValue) {
        this.widthAdjustValue = widthAdjustValue;
    }

    /**
     * DOC amaumont Comment method "setShowAllColumns".
     * 
     * @param b
     */
    public void setFillHorizontal(boolean showAllColumns) {
        this.fillHorizontal = showAllColumns;
    }

    private Integer getColumnIndex(TableColumn tableColumn) {
        TableColumn[] tableColumns = tableColumn.getParent().getColumns();
        for (int i = 0; i < tableColumns.length; i++) {
            if (tableColumns[i] == tableColumn) {
                return i;
            }
        }
        return null;
    }

    private void changeColumnLayoutData(final TableColumn currentTableColumn, Rectangle bounds) {
        Integer columnIndex = getColumnIndex(currentTableColumn);
        ColumnLayoutData columnLayoutData = columnsLayoutData.get(columnIndex);
        if (columnLayoutData instanceof ColumnPixelData) {
            ColumnPixelData columnPixelData = (ColumnPixelData) columnLayoutData;
            columnPixelData.width = currentTableColumn.getWidth();
            // System.out.println("columnPixelData.width="+columnPixelData.width);
        } else if (columnLayoutData instanceof ColumnWeightData) {
            ColumnWeightData columnWeightData = (ColumnWeightData) columnLayoutData;
            Table table = currentTableColumn.getParent();
            TableColumn[] columns = table.getColumns();
            // int totalWidthWithoutWeight = 0;
            int totalWidthWithWeight = 0;
            int totalWeight = 0;
            for (int i = 0; i < columns.length; i++) {
                ColumnLayoutData data = columnsLayoutData.get(i);
                if (data instanceof ColumnWeightData) {
                    int weight = ((ColumnWeightData) data).weight;
                    totalWeight += weight;
                    totalWidthWithWeight += columns[i].getWidth();
                } else {
                    if (WindowSystem.isGTK()) {
                        totalWidthWithWeight += columns[i].getWidth();
                    }
                }
            }
            float coef = (float) currentTableColumn.getWidth() * (float) totalWeight / ((float) totalWidthWithWeight);
            columnWeightData.weight = Math.round(coef);
        }
    }

    /**
     * DOC amaumont Comment method "resizeControl".
     * 
     * @param e
     */
    private synchronized void controlResizedExecute(ControlEvent e) {
        final TableColumn currentTableColumn = (TableColumn) e.widget;
        if (!WindowSystem.isGTK() && !columnsResizingByLayout && (fillHorizontal || continuousLayout)) {
            // System.out.println("controlResizedExecute");
            if (continuousLayout && !fillHorizontal) {
                // asyncThreadingForManualColumnResizingFalse.interrupt();
                // if (!fillHorizontal) {
                // manualResizing = false;
                // }
            }
            if (!manualResizing) {
                manualResizing = true;
                final Table table = currentTableColumn.getParent();
                Rectangle bounds = table.getClientArea();
                // System.out.println("currentTableColumn.getWidth()=" + currentTableColumn.getWidth());
                // System.out.println("columnsResizingByLayout=" + columnsResizingByLayout);
                // System.out.println("currentTableColumn.hashCode()=" + currentTableColumn.hashCode());

                if (table.getHorizontalBar().getSelection() == 0) {
                    if (!WindowSystem.isGTK()) {
                        changeColumnLayoutData(currentTableColumn, bounds);
                    }

                    lastDisplayedWidth = bounds.width + widthAdjustValue;
                    // System.out.println("lastWidth="+lastDisplayedWidth);
                    // System.out.println("referenceWidth="+referenceWidth);

                    referenceWidth = computeCurrentTableWidth();

                    TableColumn[] tableColumns = table.getColumns();
                    if (fillHorizontal && tableColumns.length - 1 >= 0) {
                        int widthAll = referenceWidth;

                        int indexLastColumn = tableColumns.length - 1;
                        TableColumn lastTableColumn = tableColumns[indexLastColumn];
                        TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn = (TableViewerCreatorColumnNotModifiable) tableViewerCreator
                                .getColumns().get(indexLastColumn);

                        ColumnLayoutData columnLayoutData = columnsLayoutData.get(indexLastColumn);
                        int minimumWidth = 0;
                        if (columnLayoutData instanceof ColumnWeightData) {
                            minimumWidth = ((ColumnWeightData) columnLayoutData).minimumWidth;
                        } else if (columnLayoutData instanceof ColumnPixelData) {
                            minimumWidth = ((ColumnPixelData) columnLayoutData).width;
                        }

                        int widthLastColumn = lastTableColumn.getWidth();
                        int newColumnWidth = lastDisplayedWidth - (widthAll - widthLastColumn);
                        if (newColumnWidth > minimumWidth) {
                            if (referenceWidth - widthLastColumn < lastDisplayedWidth) {
                                if (newColumnWidth > 0) {
                                    // System.out.println("change");
                                    lastTableColumn.setWidth(newColumnWidth);
                                    changeColumnLayoutData(lastTableColumn, bounds);
                                }
                            } else {
                                int width = tableViewerCreatorColumn.getWidth();
                                // System.out.println("weight=" + weight);
                                // System.out.println("width=" + width);
                                if (columnLayoutData instanceof ColumnWeightData) {
                                    lastTableColumn.setWidth(width);
                                    changeColumnLayoutData(lastTableColumn, bounds);
                                }
                            }
                        }
                        referenceWidth = computeCurrentTableWidth() + widthAdjustValue;
                        // System.out.println("referenceWidth=" + referenceWidth);
                    }
                }
                if (continuousLayout && !fillHorizontal) {
                    // asyncThreadingForManualColumnResizingFalse.start();
                    Runnable runable = new Runnable() {

                        public void run() {
                            try {
                                synchronized (this) {
                                    wait(500);
                                    manualResizing = false;
                                }

                            } catch (InterruptedException e) {
                                manualResizing = false;
                            }

                        }

                    };
                    new Thread(runable).start();

                }
            }

            if (fillHorizontal) {
                manualResizing = false;
            }

            tableViewerCreator.redrawTableEditorControls();
        }
    }

}
