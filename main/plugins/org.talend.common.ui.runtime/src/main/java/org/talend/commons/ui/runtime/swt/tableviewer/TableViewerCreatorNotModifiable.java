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
package org.talend.commons.ui.runtime.swt.tableviewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.swt.proposal.IShowInvisibleCellEditorMethods;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.DefaultHeaderColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.DefaultStructuredContentProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.DefaultTableLabelProvider;
import org.talend.commons.ui.runtime.swt.tableviewer.behavior.TableViewerCreatorLayout;
import org.talend.commons.ui.runtime.swt.tableviewer.data.AccessorUtils;
import org.talend.commons.ui.runtime.swt.tableviewer.data.ModifiedObjectInfo;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.ITableColumnSelectionListener;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.MouseTableSelectionHelper;
import org.talend.commons.ui.runtime.swt.tableviewer.selection.SelectionHelper;
import org.talend.commons.ui.runtime.swt.tableviewer.sort.TableViewerCreatorSorter;
import org.talend.commons.ui.runtime.swt.tableviewer.tableeditor.ITableEditorManagerListener;
import org.talend.commons.ui.runtime.swt.tableviewer.tableeditor.TableEditorManager;
import org.talend.commons.ui.runtime.swt.tableviewer.tableeditor.TableEditorManager.EVENT_TYPE;
import org.talend.commons.ui.runtime.swt.tableviewer.tableeditor.TableEditorManagerEvent;
import org.talend.commons.ui.runtime.thread.AsynchronousThreading;
import org.talend.commons.ui.runtime.utils.TableUtils;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.utils.data.list.ListenableList;
import org.talend.libraries.ui.SWTFacade;

/**
 * A concrete Table viewer based on the JFace <code>TableViewer</code> and the SWT <code>Table</code> control.
 * <p>
 * This class is intended to make easier creation and use of a table.
 * 
 * For a basic usage you need create columns with <code>TableViewerCreatorColumn</code> and init a addedObjects of
 * objects. The addedObjects will be introspected to retrieve values of each its objects. Each object of the
 * addedObjects will represent one line in the Table.
 * 
 * The following functions are already implemented : sort, automatic resize of columns (see layoutMode), and basic
 * functions.
 * 
 * You can access directly to instanciated <code>TableViewer</code> and <code>Table</code> to manage, add custom
 * listeners or other usages.
 * 
 * Filtering is not currently implemented, but you can add manually yours.
 * 
 * <p>
 * You can add CellEditor using <code>TableViewerCreatorColumn</code> and <code>setCellEditor()</code> method. According
 * case you will have to override certain methods of <code>CellEditorValueAdapter</code>.
 * </p>
 * 
 * <p>
 * You can add TableEditor using <code>TableViewerCreatorColumn</code> and <code>setTableEditor()</code> method.
 * According case you will have to override certain methods of <code>TableEditorInitializer</code>.
 * </p>
 * 
 * Read the following steps to create a reflect table :
 * <p>
 * 1) instanciate <code>TableViewerCreator</code>
 * </p>
 * <p>
 * 2) configure, the methods below are used to configure internally style of <code>Table</code> object, so if you want
 * use these methods call them before <code>createTable</code> : <br/>
 * - <code>setLineSelection</code> <br/>
 * - <code>setShowSelection</code> <br/>
 * - <code>setCheckboxInFirstColumn</code> <br/>
 * - <code>setBorderVisible</code> <br/>
 * - <code>setHorizontalScroll</code> <br/>
 * - <code>setVerticalScroll</code> <br/>
 * - <code>setHeaderVisible</code> <br/>
 * - <code>setLinesVisible</code>
 * </p>
 * 
 * <p>
 * 3) optionally call <code>createTable</code> if you need initialize <code>Table</code>'s children components before
 * <code>init</code> call.
 * </p>
 * <p>
 * 4) create <code>TableViewerCreatorColumn</code> columns and configure them. <BR/>
 * Description of the main parameters : <BR/>
 * - <code>beanPropertyName</code> represents the property of each object of your addedObjects which will be read (and
 * write). <BR/>
 * - <code>idProperty</code> (optional) represents the <b>unique id</b> of the column, it is by default the value of
 * <code>beanPropertyName</code>. If the unicity is not respected, a assertion is thrown. <BR/>
 * - set a <code>width</code> or a <code>weight</code> value. <BR/>
 * - see others parameters in <code>TableViewerCreatorColumn</code>
 * </p>
 * <p>
 * 5) set commons value for columns as you want, call them before <code>init</code> : <br/>
 * - <code>setAllColumnsMoveable</code> <br/>
 * - <code>setAllColumnsResizable</code> <br/>
 * - <code>setAllColumnsSortable</code> <br/>
 * </p>
 * 
 * <p>
 * 6) call <code>init</code> method with your addedObjects of objects in parameter.
 * </p>
 * 
 * @see org.eclipse.jface.viewers#TableViewer
 * @see org.eclipse.swt.widgets#Table <br/>
 * 
 * <br/>
 * 
 * $Id: TableViewerCreator.java 7183 2007-11-23 11:03:36Z amaumont $
 * 
 * @param <B> type of objects in the input list of <code>TableViewer</code>
 */
public class TableViewerCreatorNotModifiable<B> {

    private static Logger log = Logger.getLogger(TableViewerCreatorNotModifiable.class);

    private static final String ID_MASKED_COLUMN = "__MASKED_COLUMN__"; //$NON-NLS-1$

    private final Composite compositeParent;

    private List<TableViewerCreatorColumnNotModifiable> columns = new ArrayList<TableViewerCreatorColumnNotModifiable>();

    private TableViewer tableViewer;

    private LINE_SELECTION lineSelection = LINE_SELECTION.MULTI;

    private SHOW_ROW_SELECTION showLineSelection = SHOW_ROW_SELECTION.FULL;

    private boolean keyboardManagementForCellEdition = true;

    private boolean editorActivate = true;

    /**
     * @see Table#setLinesVisible(boolean)
     */
    private boolean linesVisible = true;

    /**
     * 
     */
    private Color emptyZoneColor;

    /**
     * SWT.BORDER style applied to <code>Table</code>.
     * 
     * @see SWT#CHECK
     */
    private boolean borderVisible;

    /**
     * @see Table#setHeaderVisible(boolean)
     */
    private boolean headerVisible = true;

    /**
     * SWT.CHECK style applied to <code>Table</code>.
     * 
     * @see SWT#CHECK
     */
    private boolean checkboxInFirstColumn;

    private boolean horizontalScroll;

    private boolean verticalScroll;

    private boolean columnsMoveableByDefault;

    private boolean columnsResizableByDefault;

    private boolean columnsSortableByDefault;

    private ICellModifier cellModifier;

    private ITableLabelProvider labelProvider;

    private IStructuredContentProvider contentProvider;

    private ControlListener tableParentResizedListener;

    private Layout layout;

    private int adjustWidthValue;

    private Map<String, TableViewerCreatorColumnNotModifiable> idToTableViewerCreatorColumn;

    private Table table;

    private TableViewerCreatorSorter tableViewerCreatorSorter;

    private LAYOUT_MODE layoutMode = LAYOUT_MODE.NONE;

    private boolean firstColumnMasked;

    private boolean initCalled;

    private TableEditorManager tableEditorManager;

    private ModifiedObjectInfo<B> modifiedObjectInfo;

    private boolean firstVisibleColumnIsSelection;

    private SelectionHelper selectionHelper;

    private MouseTableSelectionHelper mouseTableSelectionHelper;

    private TableViewerCreatorColumnNotModifiable defaultOrderedColumn;

    private SORT defaultOrderBy;

    private Color backgroundColor;

    private Color foregroundColor;

    private Color bgColorSelectedLine;

    private Color fgColorSelectedLine;

    private Color bgColorSelectedLineWhenUnactive;

    private Color fgColorSelectedLineWhenUnactive;

    private Listener eraseItemListener;

    private boolean useCustomItemColoring;

    protected List list;

    private boolean readOnly;

    protected Object controlClicked;

    protected int keyPressed;

    private ListenableList<B> listenableList;

    private boolean lazyLoad;

    /**
     * Constructor.
     * 
     * @param compositeParent used to initialize <code>Table</code>.
     */
    public TableViewerCreatorNotModifiable(Composite compositeParent) {
        super();
        this.compositeParent = compositeParent;
        this.emptyZoneColor = compositeParent.getDisplay().getSystemColor(SWT.COLOR_WHITE);

    }

    /**
     * 
     * <p>
     * Create a new instance of <code>Table</code> with its <code>TableColumn</code> and create also a new instance of
     * <code>TableViewer</code> if these objects doesn't exist already.
     * </p>
     * 
     * <p>
     * Initialize the <code>TableViewer</code> with by default the <code>DefaultStructuredContentProvider</code>, the
     * <code>DefaultTableLabelProvider</code> and the <code>DefaultTableViewerSorter</code>.
     * </p>
     * 
     */
    public void init() {
        init(null);
    }

    /**
     * 
     * <p>
     * Create a new instance of <code>Table</code> with its <code>TableColumn</code>s and create also a new instance of
     * <code>TableViewer</code> if these objects doesn't exist already.
     * </p>
     * 
     * <p>
     * Initialize the <code>TableViewer</code> with by default the <code>DefaultStructuredContentProvider</code>, the
     * <code>DefaultTableLabelProvider</code> and the <code>DefaultTableViewerSorter</code>.
     * </p>
     * 
     * <p>
     * If you use the default ContentProvider, you must set as input an object which extends <code>Collection</code>.
     * </p>
     * 
     * 
     * <p>
     * <b> If it is the second or more time than you call this method, only the input object is loaded. Call methods of
     * <code>TableViewer</code> for others operations. </b>
     * </p>
     * 
     * @param input by default a object which extends <code>Collection</code>.
     */
    public void init(List list) {

        if (!initCalled) {
            initCalled = true;
            if (this.table == null) {
                createTable();
            }
            attachLabelProvider();
            buildAndLayoutTable();
            attachViewerSorter();
            attachContentProvider();
            attachCellEditors();
            addListeners();
        }

        boolean hasChanged = false;
        if (this.list != list) {
            hasChanged = true;
        }

        setInputList(list);
        if (tableEditorManager != null && list != null) {
            tableEditorManager.init(this.listenableList);
        }

        if (hasChanged && !isLazyLoad()) {
            refreshTableEditorControls();
        }

    }

    @SuppressWarnings("unchecked")
    public List<B> getInputList() {
        return (List<B>) tableViewer.getInput();
    }

    public void setInputList(List list) {

        if (list instanceof ListenableList) {
            this.listenableList = (ListenableList) list;
        } else {
            if (this.listenableList == null) {
                this.listenableList = new ListenableList<B>(list);
            } else {
                this.listenableList.registerList(list);
            }
        }

        this.list = list;
        tableViewer.setInput(list);
    }

    /**
     * 
     * Instantiate the <code>Table</code> with the <code>compositeParent</code> as parent, with pre-configured styles
     * and options.
     * 
     * @return
     */
    public Table createTable() {
        if (this.table != null) {
            this.table.dispose();
        }
        this.table = new Table(compositeParent, checkTableStyles());

        // new TableEditor(table);
        tableViewer = new TableViewer(table) {

            @Override
            protected void handleDispose(DisposeEvent event) {
                if (this.isCellEditorActive()) {
                    CellEditor[] cellEditors = this.getCellEditors();
                    for (CellEditor cellEditor : cellEditors) {
                        if (cellEditor != null && cellEditor.isActivated()
                                && cellEditor instanceof IShowInvisibleCellEditorMethods) {
                            ((IShowInvisibleCellEditorMethods) cellEditor).fireApplyEditorValue();
                        }
                    }
                }
                super.handleDispose(event);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.TableViewer#add(java.lang.Object)
             */
            @Override
            public void add(Object element) {
                super.add(element);
                // tableEditorManager.redrawControls();
                // refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.TableViewer#add(java.lang.Object[])
             */
            @Override
            public void add(Object[] elements) {
                super.add(elements);
                // tableEditorManager.redrawControls();
                // refreshTableEditorControls();
            }

            @Override
            public void insert(Object element, int position) {
                super.insert(element, position);
                // tableEditorManager.redrawControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.TableViewer#remove(java.lang.Object[])
             */
            @Override
            public void remove(Object[] elements) {
                // removeEraseListener();
                super.remove(elements);
                // addEraseItemListener();
                // refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.TableViewer#replace(java.lang.Object, int)
             */
            @Override
            public void replace(Object element, int index) {
                super.replace(element, index);
                refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.StructuredViewer#refresh()
             */
            @Override
            public void refresh() {
                // boolean itemHasBeenRemoved = getInputList() != null && getInputList().size() < table.getItemCount();
                // if (itemHasBeenRemoved) {
                // // table.removeListener(SWT.EraseItem, eraseItemListener);
                // }
                super.refresh();
                // if (itemHasBeenRemoved) {
                // // table.addListener(SWT.EraseItem, eraseItemListener);
                // }
                // refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.StructuredViewer#refresh(boolean)
             */
            @Override
            public void refresh(boolean updateLabels) {
                // boolean itemHasBeenRemoved = getInputList() != null && getInputList().size() < table.getItemCount();
                // if (itemHasBeenRemoved) {
                // // table.removeListener(SWT.EraseItem, eraseItemListener);
                // }
                super.refresh(updateLabels);
                // if (itemHasBeenRemoved) {
                // // table.addListener(SWT.EraseItem, eraseItemListener);
                // }
                // refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.StructuredViewer#refresh(java.lang.Object, boolean)
             */
            @Override
            public void refresh(Object element, boolean updateLabels) {
                super.refresh(element, updateLabels);
                refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.viewers.StructuredViewer#refresh(java.lang.Object)
             */
            @Override
            public void refresh(Object element) {
                if (table.isDisposed()) {
                    return;
                }
                for (TableItem item : table.getItems()) {
                    if (item.isDisposed()) {
                        return;
                    }
                }
                // if (eraseItemListener != null) {
                // table.removeListener(SWT.EraseItem, eraseItemListener);
                // }
                super.refresh(element);
                // if (eraseItemListener != null) {
                // table.addListener(SWT.EraseItem, eraseItemListener);
                // }
                // refreshTableEditorControls();
            }

            /*
             * (non-Javadoc)
             * 
             * @seeorg.eclipse.jface.viewers.ColumnViewer#triggerEditorActivationEvent(org.eclipse.jface.viewers.
             * ColumnViewerEditorActivationEvent)
             */
            @Override
            protected void triggerEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                if (editorActivate) {
                    super.triggerEditorActivationEvent(event);
                }
            }

            // @Override
            // protected void internalRefresh(Object element, boolean updateLabels) {
            // int oldCount = tableViewer.getTable().getItemCount();
            // if (getInputList() != null && oldCount != getInputList().size()) {
            // setItemCount(getInputList().size());
            // }
            // super.internalRefresh(element, updateLabels);
            // };

            @Override
            protected void hookControl(Control control) {
                super.hookControl(control);
                table.addListener(SWT.SetData, new Listener() {

                    @Override
                    public void handleEvent(Event event) {
                        TableItem item = (TableItem) event.item;
                        int index = table.indexOf(item);

                        if (index == -1) {
                            return;
                        }
                        B element = getInputList().get(index);
                        // if (element != item.getData()) {
                        associate(element, item);
                        updateItem(item, element);
                        refreshTableEditorColumn(index);
                        // }
                    }

                });
            };

        };
        setTablePreferences();

        // initCellModifier();

        return table;
    }

    /**
     * DOC amaumont Comment method "initCellModifier".
     */
    // protected void initCellModifier() {
    // if (cellModifier == null) {
    // cellModifier = new DefaultCellModifier(this);
    // }
    // tableViewer.setCellModifier(cellModifier);
    // }

    protected void setTablePreferences() {
        table.setHeaderVisible(headerVisible);
        table.setLinesVisible(linesVisible);

        if (this.emptyZoneColor != null && !WindowSystem.isGTK() && !WindowSystem.isOSX()) {
            Listener paintListener = new Listener() {

                @Override
                public void handleEvent(Event event) {
                    GC gc = event.gc;
                    Rectangle area = table.getClientArea();

                    Color previousBgColor = gc.getBackground();

                    gc.setBackground(emptyZoneColor);
                    int starty = table.getHeaderHeight() + table.getItemCount() * table.getItemHeight()
                            - table.getVerticalBar().getSelection() * table.getItemHeight();

                    // System.out.println(starty + " < " + area.height);
                    // System.out.println("area.width= " + area.width);
                    if (starty < area.height) {
                        // System.out.println("0, starty, area.width, area.height");
                        // gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_BLUE));
                        gc.fillRectangle(0, starty, area.width, area.height);
                        // gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_RED));
                    }
                    TableColumn[] tableColumns = table.getColumns();
                    int widthColumns = 0;
                    for (TableColumn tableColumn : tableColumns) {
                        widthColumns += tableColumn.getWidth();
                    }
                    if (widthColumns < area.width) {
                        // System.out.println("widthColumns + 1, 0, area.width, area.height");
                        gc.fillRectangle(widthColumns + 1, 0, area.width, area.height);
                    }

                    gc.setBackground(previousBgColor);

                }
            };
            table.addListener(SWTFacade.Paint, paintListener);
        }

        setBackgroundColor(backgroundColor != null ? backgroundColor : table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        setForegroundColor(foregroundColor != null ? foregroundColor : table.getDisplay().getSystemColor(SWT.COLOR_BLACK));

        if (useCustomItemColoring) {
            setUseCustomItemColoring(true);
        }

        if (this.firstVisibleColumnIsSelection) {
            this.mouseTableSelectionHelper = new MouseTableSelectionHelper(this);
        }

    }

    /**
     * 
     * Initiate the style intended for instanciate <code>Table</code>.
     * 
     * @return int style
     */
    protected int checkTableStyles() {
        int style = SWT.NONE;
        if (lineSelection != null) {
            style |= lineSelection.getSwtStyle();
        }
        if (showLineSelection != null) {
            style |= showLineSelection.getSwtStyle();
        }
        if (checkboxInFirstColumn) {
            style |= SWT.CHECK;
        }
        if (borderVisible) {
            style |= SWT.BORDER;
        }
        if (horizontalScroll) {
            style |= SWT.H_SCROLL;
        }
        if (verticalScroll) {
            style |= SWT.V_SCROLL;
        }
        if (isLazyLoad()) {
            style |= SWT.VIRTUAL;
        }
        return style;
    }

    protected void addListeners() {

        if (useCustomItemColoring) {
            addEraseItemListener();
        }

        final MouseListener mouseListener = new MouseListener() {

            @Override
            public void mouseDoubleClick(MouseEvent e) {

            }

            @Override
            public void mouseDown(MouseEvent mouseEvent) {
                controlClicked = mouseEvent.getSource();
            }

            @Override
            public void mouseUp(MouseEvent e) {

            }

        };

        final TraverseListener traverseListenerForControls = new TraverseListener() {

            @Override
            public void keyTraversed(TraverseEvent e) {
                if (!keyboardManagementForCellEdition) {
                    return;
                }
                int key = e.detail;
                if (key == SWT.TRAVERSE_TAB_NEXT || key == SWT.TRAVERSE_TAB_PREVIOUS) {
                    keyPressed = key;
                    e.doit = false;
                    editOtherEditor((Control) e.getSource());
                } else if (key == SWT.TRAVERSE_RETURN) {
                    keyPressed = key;
                    if (getTable().getSelectionIndex() != -1 && getTable().isFocusControl()) {
                        editOtherEditor(null);
                    } else {
                        forceTableFocus();
                    }
                } else if (key == SWT.TRAVERSE_ESCAPE) {
                    forceTableFocus();
                } else {
                    // e.doit = false;
                }
            }

            /**
             * DOC amaumont Comment method "forceTableFocus".
             */
            private void forceTableFocus() {
                new AsynchronousThreading(10, false, getTable().getDisplay(), new Runnable() {

                    @Override
                    public void run() {
                        if (!getTable().isDisposed()) {
                            getTable().forceFocus();
                        }
                    }
                }).start();
            }
        };

        final TraverseListener traverseListenerForTable = new TraverseListener() {

            @Override
            public void keyTraversed(TraverseEvent e) {
                if (!keyboardManagementForCellEdition) {
                    return;
                }
                int key = e.detail;
                if (key == SWT.TRAVERSE_TAB_NEXT || key == SWT.TRAVERSE_TAB_PREVIOUS) {
                    keyPressed = key;
                    e.doit = false;
                    editOtherEditor(null);
                } else if (key == SWT.TRAVERSE_RETURN && getTable().getSelectionIndex() != -1 && getTable().isFocusControl()) {
                    keyPressed = key;
                    editOtherEditor(null);
                }
            }
        };
        getTable().addTraverseListener(traverseListenerForTable);

        final KeyListener keyListenerForTable = new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!keyboardManagementForCellEdition) {
                    return;
                }
                int key = e.keyCode;
                if (key == SWT.F2) {
                    keyPressed = key;
                    editOtherEditor(null);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

        };
        getTable().addKeyListener(keyListenerForTable);

        getTable().addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (tableEditorManager != null) {
                    tableEditorManager.release();
                }

            }

        });

        if (tableEditorManager != null) {
            tableEditorManager.addListener(new ITableEditorManagerListener() {

                @Override
                public void notifyEvent(TableEditorManagerEvent event) {
                    Control editor = event.getTableEditor().getEditor();
                    if (event.getEventType() == EVENT_TYPE.CONTROL_CREATED) {
                        editor.addMouseListener(mouseListener);
                        editor.addTraverseListener(traverseListenerForControls);
                    }
                }
            });
        }

        for (TableViewerCreatorColumnNotModifiable column : columns) {

            CellEditor cellEditor = column.getCellEditor();
            if (cellEditor != null && cellEditor.getControl() != null) {
                addTraverseListenerRecursivly(cellEditor.getControl(), traverseListenerForControls);
            }
        }
        if (isLazyLoad()) {
            getTable().addPaintListener(new PaintListener() {

                @Override
                public void paintControl(PaintEvent event) {
                    if (tableEditorManager != null) {
                        tableEditorManager.paintVisibleAreaEditor(event);
                    }
                }
            });
        }
    }

    /**
     * DOC amaumont Comment method "addTraversListenerRecursivly".
     * 
     * @param control
     * @param traverseListenerForControls
     */
    private void addTraverseListenerRecursivly(Control control, TraverseListener traverseListenerForControls) {
        if (control instanceof Composite) {
            control.addTraverseListener(traverseListenerForControls);
            Composite composite = (Composite) control;
            Control[] children = composite.getChildren();
            for (Control controlChild : children) {
                addTraverseListenerRecursivly(controlChild, traverseListenerForControls);
            }
        } else {
            control.addTraverseListener(traverseListenerForControls);
        }
    }

    /**
     * Edit a cell.
     * 
     * @param currentControl current control before change, if null the first valid cell is edited or focused.
     */
    private void editOtherEditor(final Control currentControl) {

        int indexColumnStart = -1;
        int itemIndexStart = -1;
        if (currentControl == null) {
            indexColumnStart = 0;
            TableViewerCreatorColumnNotModifiable previousModifiedColumn = getModifiedObjectInfo().getPreviousModifiedColumn();
            if (previousModifiedColumn != null) {
                indexColumnStart = previousModifiedColumn.getIndex();
            }
            itemIndexStart = getTable().getSelectionIndex();
            if (itemIndexStart == -1) {
                itemIndexStart = 0;
            }
        } else {

            boolean controlLostFocusIsTableEditor = false;

            if (tableEditorManager != null) {
                List<TableEditor> tableEditorList = tableEditorManager.getTableEditorList();
                for (TableEditor editor : tableEditorList) {
                    if (editor.getEditor() == currentControl) {
                        controlLostFocusIsTableEditor = true;
                        indexColumnStart = editor.getColumn();
                        itemIndexStart = getTable().indexOf(editor.getItem());
                        break;
                    }
                }
            }

            if (!controlLostFocusIsTableEditor) {
                for (TableViewerCreatorColumnNotModifiable column : columns) {
                    if (column.getCellEditor() != null && isEqualsOrChildOf(currentControl, column.getCellEditor().getControl())) {
                        indexColumnStart = column.getIndex();
                        itemIndexStart = getTable().getSelectionIndex();
                        break;
                    }
                }
            }
        }

        if (indexColumnStart != -1 && itemIndexStart != -1) {

            final int indexColumnStartFinal = indexColumnStart;
            final int itemIndexStartFinal = itemIndexStart;

            controlClicked = null;

            Runnable runnable = new Runnable() {

                @Override
                public void run() {

                    boolean continueRun = (keyPressed == SWT.TRAVERSE_TAB_NEXT || keyPressed == SWT.TRAVERSE_TAB_PREVIOUS
                            || keyPressed == SWT.TRAVERSE_RETURN || keyPressed == SWT.F2);

                    if (!continueRun || getTable().isDisposed()) {
                        return;
                    }

                    List<B> inputList = TableViewerCreatorNotModifiable.this.getInputList();

                    int currentIndexColumn = indexColumnStartFinal;
                    int currentItemIndex = itemIndexStartFinal;

                    CellEditor cellEditorToActivate = null;
                    Control controlToFocusIn = null;

                    boolean found = false;

                    boolean firstLoop = true;

                    while (true) {

                        if (keyPressed == SWT.TRAVERSE_TAB_NEXT || !firstLoop
                                && (keyPressed == SWT.F2 || keyPressed == SWT.TRAVERSE_RETURN)) {
                            currentIndexColumn++;
                            if (currentIndexColumn >= columns.size()) {
                                currentIndexColumn = 0;
                                currentItemIndex++;
                                if (currentItemIndex >= inputList.size()) {
                                    break;
                                }
                            }
                        } else if (keyPressed == SWT.TRAVERSE_TAB_PREVIOUS) {
                            currentIndexColumn--;
                            if (currentIndexColumn < 0) {
                                currentIndexColumn = columns.size() - 1;
                                currentItemIndex--;
                                if (currentItemIndex < 0) {
                                    break;
                                }
                            }
                        } else if (keyPressed == SWT.TRAVERSE_RETURN) {

                        }
                        TableViewerCreatorColumnNotModifiable tableViewerCreatorColumn = columns.get(currentIndexColumn);
                        if (tableViewerCreatorColumn.getTableEditorContent() != null) {
                            TableItem tableItem = getTable().getItem(currentItemIndex);
                            if (tableEditorManager != null) {
                                List<TableEditor> tableEditorList = tableEditorManager.getTableEditorList();
                                for (TableEditor editor : tableEditorList) {
                                    if (editor.getColumn() == currentIndexColumn && editor.getItem() == tableItem) {
                                        controlToFocusIn = editor.getEditor();
                                        found = true;
                                        break;
                                    }
                                }
                            }
                        } else if (tableViewerCreatorColumn.getCellEditor() != null) {
                            cellEditorToActivate = tableViewerCreatorColumn.getCellEditor();
                            found = true;
                        }
                        firstLoop = false;

                        if (found) {

                            if (controlToFocusIn != null) {
                                getTable().setSelection(currentItemIndex);
                                controlToFocusIn.setFocus();
                                break;
                            } else if (getCellModifier() != null
                                    && getCellModifier().canModify(inputList.get(currentItemIndex),
                                            getColumns().get(currentIndexColumn).getId()) && cellEditorToActivate != null) {
                                TableViewerCreatorNotModifiable.this.getTableViewer().editElement(
                                        inputList.get(currentItemIndex), currentIndexColumn);
                                break;
                            } else {
                                found = false;
                            }
                        }
                    }
                    keyPressed = 0;
                }
            };

            new AsynchronousThreading(10, false, getTable().getDisplay(), runnable).start();
        }

    }

    /**
     * DOC amaumont Comment method "isEqualsOrChildOf".
     * 
     * @param currentControl
     * @param control
     * @return
     */
    private boolean isEqualsOrChildOf(Control equalsOrChild, Control controlRef) {

        if (equalsOrChild == controlRef) {
            return true;
        } else if (controlRef instanceof Composite) {
            Composite composite = (Composite) controlRef;
            Control[] children = composite.getChildren();
            for (Control controlChildRef : children) {
                return isEqualsOrChildOf(equalsOrChild, controlChildRef);
            }
        } else {
            return false;
        }

        return false;
    }

    /**
     * 
     * This method initialize erase listener to go round the following SWT bug
     * https://bugs.eclipse.org/bugs/show_bug.cgi?id=50163 "Table doesn't respect transparency in column images when
     * using a different row background color" .
     * 
     * Unfortunately, use this listener implies that : - automatic tooltip in Table doesn't work anymore - bug when
     * {@link TableViewer#refresh()} or {@link TableViewer#refresh(boolean)} are called and possibly others...
     * 
     */
    protected void createEraseItemListener() {

        if (eraseItemListener != null) {
            return;
        }

        eraseItemListener = new Listener() {

            @Override
            public void handleEvent(Event event) {

                // System.out.println(event);
                // if(true) {
                // return;
                // }

                // System.out.println("EraseItem event.detail=" + EventUtil.getEventNameFromDetail(event.detail) +
                // "event.widget="
                // + event.widget.hashCode());
                TableItem tableItem = (TableItem) event.item;

                if (table.isDisposed() || tableItem.isDisposed()) {
                    return;
                }

                boolean selectedState = (event.detail & SWT.SELECTED) != 0;
                boolean focusedState = table.isFocusControl();

                if (selectedState || event.detail == 22) {

                    GC gc = event.gc;

                    Rectangle rect = event.getBounds();

                    Color previousBackground = gc.getBackground();
                    Color previousForeground = gc.getForeground();

                    Color bgColor = null;
                    Color fgColor = null;
                    if (focusedState) {
                        if (bgColorSelectedLine != null) {
                            bgColor = bgColorSelectedLine;
                        } else {
                            bgColor = table.getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
                        }
                        if (fgColorSelectedLine != null) {
                            fgColor = fgColorSelectedLine;
                        } else {
                            fgColor = table.getDisplay().getSystemColor(SWT.COLOR_BLACK);
                        }
                    } else {
                        if (bgColorSelectedLineWhenUnactive != null) {
                            bgColor = bgColorSelectedLineWhenUnactive;
                        } else {
                            bgColor = table.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
                        }
                        if (fgColorSelectedLineWhenUnactive != null) {
                            fgColor = fgColorSelectedLineWhenUnactive;
                        } else {
                            fgColor = table.getDisplay().getSystemColor(SWT.COLOR_BLACK);
                        }
                    }
                    gc.setBackground(bgColor);
                    gc.setForeground(fgColor);

                    gc.fillRectangle(rect);

                    gc.setBackground(previousBackground);
                    gc.setForeground(previousForeground);

                    event.detail &= ~SWT.SELECTED;

                } else if (event.detail == 24 || event.detail == 28) {
                    /**
                     * To color cells or rows selected by using
                     * org.eclipse.swt.widgets.TableItem#setBackground(int,Color) or
                     * org.eclipse.swt.widgets.TableItem#setBackground(Color)
                     */

                    // System.out.println("#########################" +event.detail);
                    GC gc = event.gc;

                    Rectangle rect = event.getBounds();

                    int columnIndex = TableUtils.getColumnIndex(table, new Point(event.x, event.y));
                    Color currentBackgroundColumn = tableItem.getBackground(columnIndex);

                    Color parentBg = tableItem.getParent().getBackground();
                    // System.out.println("currentBackgroundColumn="+currentBackgroundColumn);
                    // System.out.println("tableItem.getBackground()="+tableItem.getBackground());
                    if (currentBackgroundColumn == parentBg && tableItem.getBackground() != null) {
                        currentBackgroundColumn = tableItem.getBackground();
                    }

                    Color background = gc.getBackground();

                    gc.setBackground(currentBackgroundColumn);

                    gc.fillRectangle(rect);

                    gc.setBackground(background);

                }

            }

        };

    }

    protected void attachViewerSorter() {
        tableViewer.setSorter(this.tableViewerCreatorSorter);
        if (isSortable() && defaultOrderedColumn != null && defaultOrderBy != null && defaultOrderedColumn.isSortable()) {
            this.tableViewerCreatorSorter.prepareSort(this, defaultOrderedColumn, defaultOrderBy);
        }
    }

    protected void attachLabelProvider() {
        if (this.labelProvider == null) {
            this.labelProvider = new DefaultTableLabelProvider(this);
        }
        tableViewer.setLabelProvider(this.labelProvider);
    }

    protected void attachContentProvider() {
        if (this.contentProvider == null) {
            this.contentProvider = new DefaultStructuredContentProvider(this);
        }
        tableViewer.setContentProvider(this.contentProvider);
    }

    protected TableViewer buildAndLayoutTable() {
        idToTableViewerCreatorColumn = new HashMap<String, TableViewerCreatorColumnNotModifiable>(columns.size());

        if (this.layoutMode == LAYOUT_MODE.DEFAULT || this.layoutMode == LAYOUT_MODE.FILL_HORIZONTAL
                || this.layoutMode == LAYOUT_MODE.CONTINUOUS) {
            TableViewerCreatorLayout currentTableLayout = new TableViewerCreatorLayout(this);
            currentTableLayout.setWidthAdjustValue(this.adjustWidthValue);
            currentTableLayout.setFillHorizontal(this.layoutMode == LAYOUT_MODE.FILL_HORIZONTAL);
            currentTableLayout.setContinuousLayout(this.layoutMode == LAYOUT_MODE.FILL_HORIZONTAL
                    || this.layoutMode == LAYOUT_MODE.CONTINUOUS);
            this.layout = currentTableLayout;
        }

        if (firstColumnMasked || columns.size() == 0) {
            TableViewerCreatorColumnNotModifiable maskedTableViewerCreatorColumn = new TableViewerCreatorColumnNotModifiable();
            maskedTableViewerCreatorColumn.setId(ID_MASKED_COLUMN);
            columns.add(0, maskedTableViewerCreatorColumn);
        }

        int size = columns.size();
        Layout tempLayout = table.getLayout();
        table.setLayout(null);

        if (tableEditorManager != null) {
            tableEditorManager.release();
            tableEditorManager = null;
        }

        for (int i = 0; i < size; i++) {
            final TableViewerCreatorColumnNotModifiable column = columns.get(i);
            if (column.getTableEditorContent() != null) {
                tableEditorManager = new TableEditorManager(this);
                break;
            }
        }

        for (int i = 0; i < size; i++) {
            final TableViewerCreatorColumnNotModifiable column = columns.get(i);
            column.setIndex(i);
            TableColumn tableColumn = column.getTableColumn();
            if ((WindowSystem.isGTK() || WindowSystem.isOSX()) && column.getWidth() == 0 && column.getWeight() == 0) {
                // bug with GTK for cell edition when width == 0
                column.setWidth(1);
            }
            initColumnLayout(column);
            if (tableColumn == null) {
                tableColumn = createTableColumn(column);
                column.setTableColumn(tableColumn);
            } else {
                Assert.isTrue(tableColumn.getParent() == this.table,
                        Messages.getString("TableViewerCreator.TableColumn.AssertMsg", column.getId())); //$NON-NLS-1$
            }
            Assert.isTrue(idToTableViewerCreatorColumn.get(column.getId()) == null,
                    Messages.getString("TableViewerCreator.IdProperty.AssertMsg")); //$NON-NLS-1$

            idToTableViewerCreatorColumn.put(column.getId(), column);
        }

        if (layout != null) {
            table.setLayout(layout);
        } else {
            table.setLayout(tempLayout);
        }
        // table.layout();
        return tableViewer;
    }

    /**
     * DOC amaumont Comment method "initColumnLayout".
     * 
     * @param column
     */
    protected void initColumnLayout(final TableViewerCreatorColumnNotModifiable column) {
        ColumnLayoutData columnLayoutData = null;
        if (column.getWeight() > 0) {
            columnLayoutData = new ColumnWeightData(column.getWeight(), column.getMinimumWidth(), column.isResizable());
        } else {
            columnLayoutData = new ColumnPixelData(column.getWidth(), column.isResizable(), false);
        }
        if (layout instanceof TableViewerCreatorLayout) {
            ((TableViewerCreatorLayout) layout).addColumnData(columnLayoutData);
        } else if (layout instanceof TableLayout) {
            ((TableLayout) layout).addColumnData(columnLayoutData);
        }
    }

    /**
     * Create a <code>TableColumn</code> and intialize it from <code>TableViewerCreatorColumn</code>'s properties.
     * 
     * @param column
     */
    protected TableColumn createTableColumn(final TableViewerCreatorColumnNotModifiable column) {
        boolean isMaskedColumn = false;
        if (column.getId().equals(ID_MASKED_COLUMN)) {
            isMaskedColumn = true;
        }
        TableColumn tableColumn;
        tableColumn = new TableColumn(this.table, column.getAlignment().getSwtAlignment());
        if (isMaskedColumn) {
            tableColumn.setMoveable(false);
            tableColumn.setResizable(false);
        } else {
            tableColumn.setImage(column.getImageHeader());
            tableColumn.setText(column.getTitle() != null ? column.getTitle() : column.getId());
            tableColumn.setToolTipText(column.getToolTipHeader());

            tableColumn.setMoveable(column.isMoveable());
            tableColumn.setResizable(column.isResizable());
            ITableColumnSelectionListener columnSelectionListener = null;
            if (column.getTableColumnSelectionListener() == null && column.isSortable()) {
                columnSelectionListener = new DefaultHeaderColumnSelectionListener(column, this);
                column.setTableColumnSelectionListener(columnSelectionListener);
            } else {
                columnSelectionListener = column.getTableColumnSelectionListener();
            }
            if (columnSelectionListener != null) {
                tableColumn.addSelectionListener(columnSelectionListener);
            }
        }
        return tableColumn;
    }

    public void attachCellEditors() {
        String[] properties = new String[columns.size()];
        CellEditor[] cellEditors = new CellEditor[columns.size()];
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            TableViewerCreatorColumnNotModifiable column = columns.get(i);
            properties[i] = column.getId();
            cellEditors[i] = column.getCellEditor();

            if (column.getCellEditor() != null && column.getBeanPropertyAccessors() == null) {
                log.warn(Messages.getString("TableViewerCreator.columnNoIBeanProperty", //$NON-NLS-1$
                        column.getId(), column.getTitle()));
            }

        }
        tableViewer.setColumnProperties(properties);
        tableViewer.setCellEditors(cellEditors);
    }

    public TableViewerCreatorColumnNotModifiable getColumn(String idProperty) {
        return idToTableViewerCreatorColumn.get(idProperty);
    }

    /**
     * Unlike <code>Table</code> header is visible by default.
     * 
     * @return true if table has header visible
     */
    public boolean isHeaderVisible() {
        if (table != null) {
            return table.getHeaderVisible();
        }
        return headerVisible;
    }

    /**
     * Unlike <code>Table</code> header is visible by default.
     * 
     * @param headerVisible
     */
    public void setHeaderVisible(boolean headerVisible) {
        if (table != null) {
            table.setHeaderVisible(headerVisible);
        } else {
            this.headerVisible = headerVisible;
        }
    }

    /**
     * 
     * DOC amaumont Comment method "addColumn".
     * 
     * @param tableEditorColumn
     */
    public void addColumn(TableViewerCreatorColumnNotModifiable tableEditorColumn) {
        String id = tableEditorColumn.getId();
        if (id == null) {
            throw new IllegalArgumentException(
                    Messages.getString("TableViewerCreator.columnNullId", tableEditorColumn.getTitle())); //$NON-NLS-1$
        }
        int columnsListSize = columns.size();
        boolean columnWithSameId = false;
        for (int i = 0; i < columnsListSize; i++) {
            TableViewerCreatorColumnNotModifiable column = columns.get(i);
            if (id.equals(column.getId())) {
                columnWithSameId = true;
                break;
            }
        }
        if (!columnWithSameId) {
            columns.add(tableEditorColumn);
        }
    }

    /**
     * Line selection mode for SWT Table. <br/>
     * 
     * @see SWT.SINGLE
     * @see SWT.MULTI
     * 
     */
    public enum LINE_SELECTION implements ISwtStyle {
        /**
         * Only one line is selectionnable.
         */
        SINGLE(SWT.SINGLE),
        /**
         * All line are selectionnable.
         */
        MULTI(SWT.MULTI);

        private int swtStyle = SWT.NONE;

        /**
         * 
         * DOC amaumont LINE_SELECTION constructor comment.
         * 
         * @param swtStyle
         */
        LINE_SELECTION(int swtStyle) {
            this.swtStyle = swtStyle;
        }

        @Override
        public int getSwtStyle() {
            return swtStyle;
        }
    };

    /**
     * Show selection mode of a <code>Table</code>'s row. <br/>
     * 
     * @see SWT.FULL_SELECTION
     * @see SWT.HIDE_SELECTION
     * 
     */
    public enum SHOW_ROW_SELECTION implements ISwtStyle {
        /**
         * Show selection for full row.
         */
        FULL(SWT.FULL_SELECTION),
        /**
         * Don't show selection.
         */
        HIDE(SWTFacade.HIDE_SELECTION);

        private int swtStyle = SWT.NONE;

        SHOW_ROW_SELECTION(int swtStyle) {
            this.swtStyle = swtStyle;
        }

        @Override
        public int getSwtStyle() {
            return swtStyle;
        }
    }

    /**
     * Layout mode of the <code>TableViewer</code>.
     * 
     */
    public enum LAYOUT_MODE {
        /**
         * Layout mode. Default layout based on <code>TableLayout</code> behavior :
         * <p>
         * - Use width and weight to initialize columns size, but don't resize columns when table is resized
         * </p>
         */
        DEFAULT(),

        /**
         * Layout mode.
         * <p>
         * - Use width and weight to initialize columns size, but don't resize columns when table is resized
         * </p>
         * <p>
         * - Fill all empty space at initialization
         * </p>
         * <p>
         * - Change columns size dynamically when table is resized (only if weight is set in column)
         * </p>
         */
        CONTINUOUS,

        /**
         * Layout mode.
         * <p>
         * - Use width and weight to initialize columns size, but don't resize columns when table is resized
         * </p>
         * <p>
         * - Fill all empty space at initialization
         * </p>
         * <p>
         * - Change columns size dynamically when table is resized (only if weight is set in column)
         * </p>
         * <p>
         * - Fill empty space with last column when columns are resized
         * </p>
         */
        FILL_HORIZONTAL,

        NONE();

        LAYOUT_MODE() {
        }
    };

    public boolean isCheckboxInFirstColumn() {
        return this.checkboxInFirstColumn;
    }

    public void setCheckboxInFirstColumn(boolean checkboxInFirstColumn) {
        this.checkboxInFirstColumn = checkboxInFirstColumn;
    }

    public List<TableViewerCreatorColumnNotModifiable> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public void setColumns(List<TableViewerCreatorColumnNotModifiable> columns) {
        this.columns = columns;
    }

    public LINE_SELECTION getLineSelection() {
        return lineSelection;
    }

    /**
     * 
     * <code>LINE_SELECTION.MULTI</code> is the default value.
     * 
     * @param lineSelection
     */
    public void setLineSelection(LINE_SELECTION lineSelection) {
        this.lineSelection = lineSelection;
    }

    /**
     * Unlike <code>Table</code> lines are visible by default.
     * 
     * @see Table#getLinesVisible()
     */
    public boolean isLinesVisible() {
        if (table != null) {
            return table.getLinesVisible();
        }
        return this.linesVisible;
    }

    /**
     * Unlike <code>Table</code> lines are visible by default.
     * 
     * @see Table#setLinesVisible(boolean)
     */
    public void setLinesVisible(boolean linesVisible) {
        if (table != null) {
            table.setLinesVisible(linesVisible);
        } else {
            this.linesVisible = linesVisible;
        }
    }

    /**
     * By default <code>showSelection</code> has <code>SHOW_SELECTION.FULL</code> value.
     * 
     * @return
     */
    public SHOW_ROW_SELECTION getShowLineSelection() {
        return showLineSelection;
    }

    /**
     * 
     * By default <code>showSelection</code> has <code>SHOW_SELECTION.FULL</code> value.
     * 
     * @param showLineSelection
     */
    public void setShowLineSelection(SHOW_ROW_SELECTION showLineSelection) {
        this.showLineSelection = showLineSelection;
    }

    /**
     * 
     * The <code>TableViewer</code> is instanciate at same time than <code>Table</code>.
     * 
     * @return
     */
    public TableViewer getTableViewer() {
        return tableViewer;
    }

    public void setTableViewer(TableViewer tableViewer) {
        this.tableViewer = tableViewer;
    }

    public boolean isBorderVisible() {
        return borderVisible;
    }

    public void setBorderVisible(boolean borderVisible) {
        this.borderVisible = borderVisible;
    }

    /**
     * @return
     */
    public boolean isHorizontalScroll() {
        return horizontalScroll;
    }

    /**
     * Note: has no effects for Windows sytem, scrollbar are always visible. Call this method before call createTable().
     * 
     * @param horizontalScroll
     */
    public void setHorizontalScroll(boolean horizontalScroll) {
        this.horizontalScroll = horizontalScroll;
    }

    /**
     * @return
     */
    public boolean isVerticalScroll() {
        return verticalScroll;
    }

    /**
     * Note: has no effects for Windows sytem, scrollbar are always visible. Call this method before call createTable().
     * 
     * @param verticalScroll
     */
    public void setVerticalScroll(boolean verticalScroll) {
        this.verticalScroll = verticalScroll;
    }

    public ICellModifier getCellModifier() {
        if (tableViewer != null && tableViewer.getCellModifier() != null) {
            return tableViewer.getCellModifier();
        }
        return cellModifier;
    }

    public void setCellModifier(ICellModifier cellModifier) {
        if (tableViewer != null && tableViewer.getCellModifier() != cellModifier) {
            tableViewer.setCellModifier(cellModifier);
        }
        this.cellModifier = cellModifier;
    }

    public IBaseLabelProvider getLabelProvider() {
        if (tableViewer != null && tableViewer.getLabelProvider() != null) {
            return tableViewer.getLabelProvider();
        }
        return labelProvider;
    }

    public void setLabelProvider(ITableLabelProvider tableLabelProvider) {
        if (tableViewer != null && tableViewer.getLabelProvider() != tableLabelProvider) {
            tableViewer.setLabelProvider(tableLabelProvider);
        }
        this.labelProvider = tableLabelProvider;
    }

    public IContentProvider getContentProvider() {
        if (tableViewer != null && tableViewer.getContentProvider() != null) {
            return tableViewer.getContentProvider();
        }
        return contentProvider;
    }

    public void setContentProvider(IStructuredContentProvider contentProvider) {
        if (tableViewer != null && tableViewer.getContentProvider() != contentProvider) {
            tableViewer.setContentProvider(contentProvider);
        }
        this.contentProvider = contentProvider;
    }

    public Table getTable() {
        return table;
    };

    /**
     * Sort mode. <br/>
     * 
     */
    public enum SORT {
        ASC,
        DESC,
        NONE,
    }

    public ControlListener getTableParentResizedListener() {
        return this.tableParentResizedListener;
    }

    public void setTableParentResizedListener(ControlListener tableParentResizedListener) {
        this.tableParentResizedListener = tableParentResizedListener;
    }

    public void packColumns() {
        for (int i = 0, n = table.getColumnCount(); i < n; i++) {
            table.getColumn(i).pack();
        }
    }

    public boolean isColumnsMoveableByDefault() {
        return this.columnsMoveableByDefault;
    }

    public void setColumnsMoveableByDefault(boolean allColumnsMoveable) {
        this.columnsMoveableByDefault = allColumnsMoveable;
    }

    public boolean isColumnsResizableByDefault() {
        return this.columnsResizableByDefault;
    }

    public void setColumnsResizableByDefault(boolean allColumnsResizable) {
        this.columnsResizableByDefault = allColumnsResizable;
    }

    public boolean isColumnsSortableByDefault() {
        return this.columnsSortableByDefault;
    }

    public void setColumnsSortableByDefault(boolean allColumnsSortable) {
        this.columnsSortableByDefault = allColumnsSortable;
    }

    public int getAdjustWidthValue() {
        return this.adjustWidthValue;
    }

    /**
     * Call this method before {@link #init(List)}.
     * 
     * @param defaultOrderedColumn
     * @param defaultOrderBy
     */
    public void setDefaultSort(TableViewerCreatorColumnNotModifiable defaultOrderedColumn, SORT defaultOrderBy) {
        this.defaultOrderedColumn = defaultOrderedColumn;
        this.defaultOrderBy = defaultOrderBy;
        setSortable(true);
    }

    /**
     * Change sort properties and refresh table. You can call this method if you have already call init(List).
     * 
     * @param orderedColumn
     * @param orderBy
     */
    public void setSort(TableViewerCreatorColumnNotModifiable orderedColumn, SORT orderBy) {
        setSortable(true);
        this.tableViewerCreatorSorter.prepareSort(this, orderedColumn, orderBy);
        this.tableViewer.refresh();
    }

    public void setAdjustWidthValue(int adjustWidthValue) {
        this.adjustWidthValue = adjustWidthValue;
    }

    public void setSorter(TableViewerCreatorSorter tableViewerCreatorSorter) {
        this.tableViewerCreatorSorter = tableViewerCreatorSorter;
    }

    public TableViewerCreatorSorter getSorter() {
        return this.tableViewerCreatorSorter;
    }

    public LAYOUT_MODE getLayoutMode() {
        return this.layoutMode;
    }

    public void setLayoutMode(LAYOUT_MODE layoutMode) {
        this.layoutMode = layoutMode;
    }

    public void layout() {
        if (table == null) {
            throw new IllegalStateException(Messages.getString("TableViewerCreator.Table.BeNull")); //$NON-NLS-1$
        }
        if (table.isDisposed()) {
            return;
        }
        table.layout();
        // Layout currentLayout = table.getLayout();
        // if (currentLayout instanceof TableViewerCreatorLayout) {
        // ((TableViewerCreatorLayout) currentLayout).forceLayout(table);
        // } else if (currentLayout instanceof TableLayout) {
        // ((TableLayout) currentLayout).layout(table, true);
        // }
    }

    public Layout getLayout() {
        return this.layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    /**
     * This method is useful for mask first column on a Windows Table because the first column display a blank space at
     * left border. By default first column is masked.
     * 
     * @param firstColumnMasked
     */
    public void setFirstColumnMasked(boolean firstColumnMasked) {
        this.firstColumnMasked = firstColumnMasked;
    }

    public boolean isFirstColumnMasked() {
        return this.firstColumnMasked;
    }

    /**
     * DOC amaumont Comment method "getModifiedObjectInfo".
     * 
     * @return always a instance of ModifiedObjectInfo
     */
    public ModifiedObjectInfo<B> getModifiedObjectInfo() {
        if (this.modifiedObjectInfo == null) {
            this.modifiedObjectInfo = new ModifiedObjectInfo<B>();
        }
        return this.modifiedObjectInfo;
    }

    public Color getEmptyZoneColor() {
        return this.emptyZoneColor;
    }

    public void setBgColorForEmptyArea(Color emptyZoneColor) {
        this.emptyZoneColor = emptyZoneColor;
    }

    public void refreshTableEditorControls() {
        if (tableEditorManager != null) {
            tableEditorManager.refresh();
            tableEditorManager.redrawControls();
        }
    }

    public void refreshTableEditorColumn(int index) {
        if (tableEditorManager != null && isLazyLoad()) {
            tableEditorManager.refreshColumn(index);
        }
    }

    public void redrawTableEditorControls() {
        if (tableEditorManager != null) {
            tableEditorManager.redrawControls();
        }
    }

    /**
     * Setter for firstVisibleColumnIsSelection.
     * 
     * @param firstVisibleColumnIsSelection
     */
    public void setFirstVisibleColumnIsSelection(boolean firstVisibleColumnIsSelection) {
        this.firstVisibleColumnIsSelection = firstVisibleColumnIsSelection;
    }

    /**
     * Getter for firstVisibleColumnIsSelection.
     * 
     * @return the firstVisibleColumnIsSelection
     */
    public boolean isFirstVisibleColumnIsSelection() {
        return this.firstVisibleColumnIsSelection;
    }

    public SelectionHelper getSelectionHelper() {
        if (this.selectionHelper == null) {
            this.selectionHelper = new SelectionHelper(this, mouseTableSelectionHelper);
        }
        return this.selectionHelper;
    }

    // /**
    // * You must use DefaultCellModifier or a class which extends it to use this method. You can call this method only
    // if
    // * you have already called createTable().
    // *
    // *
    // * @param tableCellValueModifiedListener
    // * @throws UnsupportedOperationException if current CellModifier is not DefaultCellModifier or a class which
    // extends
    // * it @
    // */
    // public void addCellValueModifiedListener(ITableCellValueModifiedListener tableCellValueModifiedListener) {
    // if (this.cellModifier == null) {
    //            throw new IllegalStateException(Messages.getString("TableViewerCreator.CallMethod.ErrorMsg")); //$NON-NLS-1$
    // }
    // if (this.cellModifier instanceof DefaultCellModifier) {
    // ((DefaultCellModifier) this.cellModifier).addCellEditorAppliedListener(tableCellValueModifiedListener);
    // } else {
    // throw new UnsupportedOperationException(Messages.getString(
    //                    "TableViewerCreator.CellModifier.ExError", DefaultCellModifier.class)); //$NON-NLS-1$
    // }
    // }

    /**
     * You must use DefaultCellModifier or a class which extends it to use this method. You can call this method only if
     * you have already called createTable().
     * 
     * @param tableCellValueModifiedListener
     * @throws UnsupportedOperationException if current CellModifier is not DefaultCellModifier or a class which extends
     * it
     */
    // public void removeCellValueModifiedListener(ITableCellValueModifiedListener tableCellValueModifiedListener) {
    // if (this.cellModifier == null) {
    //            throw new IllegalStateException(Messages.getString("TableViewerCreator.CallMethod.ErrorMsg")); //$NON-NLS-1$
    // }
    // if (this.cellModifier instanceof DefaultCellModifier) {
    // ((DefaultCellModifier) this.cellModifier).removeCellEditorAppliedListener(tableCellValueModifiedListener);
    // } else {
    // throw new UnsupportedOperationException(Messages.getString(
    //                    "TableViewerCreator.CellModifier.ExError", DefaultCellModifier.class)); //$NON-NLS-1$
    // }
    // }

    /**
     * <p>
     * Use custom coloring allow by default to correct bad rendering of transparent images in Table and for first
     * column.
     * </p>
     * <p>
     * Note : only available on Win32
     * </p>
     * Warnings :
     * <ul>
     * <li>automatic tooltip which appears when a cell is too small to display its content won't work.</li>
     * <li>automatic tooltip behavior can't be found again if you unactive custom coloring due to Table._addListener()
     * and Table.removeListener().</li>
     * </ul>
     * 
     * @param useCustomColoring
     */
    public void setUseCustomItemColoring(boolean useCustomColoring) {
        if (WindowSystem.isWIN32()) {
            this.useCustomItemColoring = useCustomColoring;
            if (table != null) {
                if (useCustomColoring) {
                    createEraseItemListener();
                } else {
                    removeEraseItemListener();
                }
            }
        }
    }

    /**
     * DOC amaumont Comment method "removePaintListener".
     */
    protected void removeEraseItemListener() {
        if (eraseItemListener != null) {
            table.removeListener(SWTFacade.EraseItem, eraseItemListener);
            eraseItemListener = null;
        }
    }

    /**
     * Get background color of selected line.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @return the color set by user (not the default color of selection)
     */
    public Color getBgColorSelectedLine() {
        return this.bgColorSelectedLine;
    }

    /**
     * Set background color of selected line.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @param lineSelectionBackgroundColor
     * @see TableViewerCreatorNotModifiable#setUseCustomItemColoring(boolean)
     */
    public void setBgColorSelectedLine(Color lineSelectionBackgroundColor) {
        this.bgColorSelectedLine = lineSelectionBackgroundColor;
    }

    /**
     * Get foreground color of selected line.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @return the color set by user (not the default color of selection)
     */
    public Color getFgColorSelectedLine() {
        return this.fgColorSelectedLine;
    }

    /**
     * Set foreground color of selected line.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @param lineSelectionForegroundColor
     * @see TableViewerCreatorNotModifiable#setUseCustomItemColoring(boolean)
     */
    public void setFgColorSelectedLine(Color lineSelectionForegroundColor) {
        this.fgColorSelectedLine = lineSelectionForegroundColor;
    }

    /**
     * Get background color of selected line if Table is unactive.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @return the color set by user (not the default color of selection)
     */
    public Color getBgColorSelectedLineWhenUnactive() {
        return this.bgColorSelectedLineWhenUnactive;
    }

    /**
     * Set background color of selected line if Table is unactive.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @param bgColorSelectedLineWhenUnactive
     * @see TableViewerCreatorNotModifiable#setUseCustomItemColoring(boolean)
     */
    public void setBgColorSelectedLineWhenUnactive(Color bgColorSelectedLineWhenUnactive) {
        this.bgColorSelectedLineWhenUnactive = bgColorSelectedLineWhenUnactive;
    }

    /**
     * Get foreground color of selected line if Table is unactive.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @return the color set by user (not the default color of selection)
     */
    public Color getFgColorSelectedLineWhenUnactive() {
        return this.fgColorSelectedLineWhenUnactive;
    }

    /**
     * Set foreground color of selected line if Table is unactive.
     * <p>
     * Note : only available on Win32
     * </p>
     * 
     * @param fgColorSelectedLineWhenUnactive
     * @see TableViewerCreatorNotModifiable#setUseCustomItemColoring(boolean)
     */
    public void setFgColorSelectedLineWhenUnactive(Color fgColorSelectedLineWhenUnactive) {
        this.fgColorSelectedLineWhenUnactive = fgColorSelectedLineWhenUnactive;
    }

    @SuppressWarnings("unchecked")
    public B getBeanValue(TableViewerCreatorColumnNotModifiable column, Object currentRowObject) {
        return (B) AccessorUtils.get(currentRowObject, column);
    }

    /**
     * Getter for compositeParent.
     * 
     * @return the compositeParent
     */
    public Composite getCompositeParent() {
        return this.compositeParent;
    }

    // /**
    // * DOC amaumont Comment method "removeEraseListener".
    // */
    // private void removeEraseListener() {
    // if (eraseItemListener != null) {
    // table.removeListener(SWT.EraseItem, eraseItemListener);
    // }
    // }

    /**
     * DOC amaumont Comment method "addEraseListener".
     */
    private void addEraseItemListener() {
        if (useCustomItemColoring) {
            createEraseItemListener();
        }
        if (eraseItemListener != null) {
            table.addListener(SWTFacade.EraseItem, eraseItemListener);
        }
    }

    /**
     * Getter for readOnly.
     * 
     * @return the readOnly
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Sets the readOnly.
     * 
     * @param readOnly the readOnly to set
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(Color bgColor) {
        this.backgroundColor = bgColor;
        if (table != null) {
            table.setBackground(bgColor);
        }
    }

    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    public void setForegroundColor(Color fgColor) {
        this.foregroundColor = fgColor;
        if (table != null) {
            table.setForeground(fgColor);
        }
    }

    public TableEditorManager getTableEditorManager() {
        return this.tableEditorManager;
    }

    public boolean isKeyboardManagementForCellEdition() {
        return this.keyboardManagementForCellEdition;
    }

    public void setKeyboardManagementForCellEdition(boolean enableKeysForCellsEdition) {
        this.keyboardManagementForCellEdition = enableKeysForCellsEdition;
    }

    public boolean isSortable() {
        return this.tableViewerCreatorSorter != null;
    }

    public void setSortable(boolean sortable) {
        if (sortable && !isSortable()) {
            setSorter(new TableViewerCreatorSorter());
        } else if (!sortable && isSortable()) {
            setSorter(null);
            tableViewer.setSorter(null);
        }
    }

    /**
     * DOC amaumont Comment method "refresh".
     */
    public void refresh() {
        if (tableViewer != null) {
            tableViewer.refresh();
        }
        if (tableEditorManager != null) {
            tableEditorManager.refresh();
        }
    }

    /**
     * DOC amaumont Comment method "applyActivatedCellEditor".
     */
    // public void applyActivatedCellEditor() {
    // TableViewer tableViewer = getTableViewer();
    // if (tableViewer != null && !tableViewer.getTable().isDisposed()) {
    // CellEditor activatedCellEditor = null;
    // if (tableViewer.isCellEditorActive()) {
    // CellEditor[] cellEditors = tableViewer.getCellEditors();
    // for (int i = 0; i < cellEditors.length; i++) {
    // CellEditor cellEditor = cellEditors[i];
    // if (cellEditor != null && cellEditor.isActivated()
    // && cellEditor instanceof ExtendedTextCellEditorWithProposal) {
    // ((ExtendedTextCellEditorWithProposal) cellEditor).fireApplyEditorValue();
    // activatedCellEditor = cellEditor;
    // }
    // }
    // }
    // if (activatedCellEditor != null) {
    // Object currentModifiedBean = getModifiedObjectInfo().getCurrentModifiedBean();
    // tableViewer.refresh(currentModifiedBean, true);
    // }
    // }
    //
    // }

    public void setTriggerEditorActivate(boolean editorActivate) {
        this.editorActivate = editorActivate;
    }

    public boolean isLazyLoad() {
        return this.lazyLoad;
    }

    public void setLazyLoad(boolean lazyLoad) {
        if (!Boolean.getBoolean("talend.table.disableLazyLoading")) {
            this.lazyLoad = lazyLoad;
        }
        // for bug TUP-15924, lazyload always be false by default,we have a system property like
        // "disableLazyLoading" and if set to true we won't set virtual here. (user would set it manually).
    }
 
}
