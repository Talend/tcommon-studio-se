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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.libraries.ui.SWTFacade;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id: MouseTableSelectionHelper.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class MouseTableSelectionHelper {

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private boolean firstColumnMasked;

    private boolean draggingOnSelectionColumn;

    private int itemIndexAtDraggingStart;

    private Point cursorPositionAtMouseDown;

    private Cursor tableCursor;

    private Image imageSelectionCursor;

    // private int[] selectionnableColumns;

    private Table table;

    private ImageData imageDataCursor;

    private ImageDescriptor imageDescriptor;

    // /**
    // * DOC amaumont MouseTableSelectionHelper constructor comment.
    // *
    // * @param table
    // */
    // public MouseTableSelectionHelper(TableViewerCreator tableViewerCreator, boolean firstColumnMasked, int[]
    // selectionnableColumns) {
    // this.tableViewerCreator = tableViewerCreator;
    // this.firstColumnMasked = firstColumnMasked;
    // this.selectionnableColumns = selectionnableColumns;
    // init();
    // }
    //
    /**
     * DOC amaumont MouseTableSelectionHelper constructor comment.
     * 
     * @param table
     */
    public MouseTableSelectionHelper(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;
        this.firstColumnMasked = tableViewerCreator.isFirstColumnMasked();
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     */
    private void init() {

        this.table = tableViewerCreator.getTable();

        final Listener storeCursorPositionListener = new Listener() {

            public void handleEvent(Event event) {
                cursorPositionAtMouseDown = new Point(event.x, event.y);
            }

        };
        table.addListener(SWT.MouseDown, storeCursorPositionListener);

        Listener dragDetectlistener = new Listener() {

            public void handleEvent(Event event) {

                int columnIndex = getColumnIndex(cursorPositionAtMouseDown);

                if (isColumnSelection(columnIndex)) {
                    draggingOnSelectionColumn = true;
                    itemIndexAtDraggingStart = getItemIndex(cursorPositionAtMouseDown);
                    setShellCursor(true);
                }
            }

        };
        table.addListener(SWT.DragDetect, dragDetectlistener);

        final Listener resetDraggingListener = new Listener() {

            public void handleEvent(Event event) {
                if (draggingOnSelectionColumn) {
                    draggingOnSelectionColumn = false;
                    Point pointCursor = getCursorPositionFromTableOrigin(event);
                    int columnIndex = getColumnIndex(pointCursor);
                    if (!isColumnSelection(columnIndex)) {
                        setShellCursor(false);
                    }
                }
            }

        };

        table.addListener(SWT.FocusOut, resetDraggingListener);
        table.getDisplay().addFilter(SWT.MouseUp, resetDraggingListener);

        final Listener mouseMoveListener = new Listener() {

            public void handleEvent(Event event) {
                Point pointCursor = getCursorPositionFromTableOrigin(event);

                int columnIndex = getColumnIndex(pointCursor);
                // System.out.println("handleEvent " + draggingOnSelectionColumn + " " + columnIndex);
                if (!draggingOnSelectionColumn) {
                    if (event.widget != table) {
                        if (!(event.widget instanceof Table)) {
                            // System.out.println("isColumnSelection");
                            setShellCursor(false);
                        }
                        return;
                    }
                    if (isColumnSelection(columnIndex)) {
                        setShellCursor(true);
                    } else {
                        // System.out.println("setShellCursor(false)");
                        setShellCursor(false);
                    }

                } else {

                    setShellCursor(true);
                    if (columnIndex == -1) {
                        pointCursor.x = 0;
                    }

                    int currentItemIndexUnderCursor = getItemIndex(pointCursor);
                    if (currentItemIndexUnderCursor != -1) {
                        int indexStart = 0;
                        int indexEnd = 0;
                        if (currentItemIndexUnderCursor >= itemIndexAtDraggingStart) {
                            indexStart = itemIndexAtDraggingStart;
                            indexEnd = currentItemIndexUnderCursor;
                        } else {
                            indexStart = currentItemIndexUnderCursor;
                            indexEnd = itemIndexAtDraggingStart;
                        }

                        int countSelected = indexEnd - indexStart + 1;
                        int[] selection = new int[countSelected];
                        for (int i = 0; i < countSelected; i++) {
                            selection[i] = i + indexStart;
                        }

                        boolean selectionHasChanged = !Arrays.equals(tableViewerCreator.getTable().getSelectionIndices(),
                                selection);
                        if (selectionHasChanged) {
                            tableViewerCreator.getSelectionHelper().setSelection(selection);
                        }
                    }
                }
            }

        };

        table.getDisplay().addFilter(SWTFacade.MouseMove, mouseMoveListener);

        table.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent e) {
                table.removeListener(SWT.MouseDown, storeCursorPositionListener);
                table.removeListener(SWT.MouseDown, resetDraggingListener);
                table.removeListener(SWT.FocusOut, resetDraggingListener);
                table.removeDisposeListener(this);
                table.getDisplay().removeFilter(SWT.MouseUp, resetDraggingListener);
                table.getDisplay().removeFilter(SWTFacade.MouseMove, mouseMoveListener);
                if (tableCursor != null) {
                    tableCursor.dispose();
                }
                if (imageSelectionCursor != null) {
                    imageSelectionCursor.dispose();
                }

            }

        });

    }

    /**
     * DOC amaumont Comment method "setTableCursor".
     */
    protected void setShellCursor(boolean cursorSelection) {
        if (!SWTFacade.isRAP()) {

            Cursor cursor = null;
            if (cursorSelection) {
                if (imageDescriptor == null) {
                    imageDescriptor = ImageDescriptor.createFromFile(MouseTableSelectionHelper.class, "/icons/right_arrow.ico"); //$NON-NLS-1$
                    imageDataCursor = imageDescriptor.getImageData();
                }
                if (imageDataCursor != null) {
                    // use reflection to compile within RAP environment
                    // cursor = new Cursor(table.getDisplay(), imageDataCursor, 15, 9);
                    Constructor<Cursor> constructor;
                    try {
                        constructor = Cursor.class.getConstructor(Device.class, ImageData.class, int.class, int.class);
                        cursor = constructor.newInstance(table.getDisplay(), imageDataCursor, 15, 9);
                    } catch (SecurityException e) {// throws a runtime exception but should never happend
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException(e);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                cursor = null;
            }
            if (this.tableCursor != null) {
                this.tableCursor.dispose();
            }
            this.tableCursor = cursor;

            table.getShell().setCursor(this.tableCursor);
        }
    }

    public int getColumnIndex(Point pointCursor) {

        // searching current column index
        int currentColumnIndex = -1;
        TableColumn[] columns = table.getColumns();
        int minY = 0;
        int maxY = table.getHeaderHeight()
                + (table.getItemCount() * (table.getItemHeight() + table.getBorderWidth() + (WindowSystem.isGTK() ? 2 : 0)));

        for (int i = 0, width = 0; i < columns.length; i++) {
            TableColumn column = columns[i];
            int widthColumn = column.getWidth();
            if (pointCursor.x >= width && pointCursor.x <= width + widthColumn && pointCursor.y > minY && pointCursor.y < maxY) {
                currentColumnIndex = i;
                break;
            }
            width += widthColumn;
        }

        return currentColumnIndex;
    }

    public int getItemIndex(Point pointCursor) {
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

    public boolean isDraggingOnSelectionColumn() {
        return this.draggingOnSelectionColumn;
    }

    /**
     * DOC amaumont Comment method "getCursorPositionFromTableOrigin".
     * 
     * @param event
     * @return
     */
    private Point getCursorPositionFromTableOrigin(Event event) {
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
     * DOC amaumont Comment method "isColumnSelection".
     * 
     * @param columnIndex
     * @return
     */
    private boolean isColumnSelection(int columnIndex) {
        return columnIndex == 0 && !firstColumnMasked || columnIndex == 1 && firstColumnMasked;
    }

}
