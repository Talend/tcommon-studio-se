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
package org.talend.core.ui.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.tooltip.AbstractTreeTooltip;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.context.model.ContextValueErrorChecker;
import org.talend.core.ui.context.model.ContextViewerProvider;
import org.talend.core.ui.context.model.table.ContextTableCellModifier;
import org.talend.core.ui.context.model.table.ContextTableConstants;
import org.talend.core.ui.context.model.table.GroupByNothingTableProvider;
import org.talend.core.ui.context.model.table.GroupBySourceTableProvider;
import org.talend.core.ui.i18n.Messages;

/**
 * DOC zwang class global comment. Detailled comment <br/>
 * 
 */
public class ContextTableValuesComposite extends AbstractContextTabEditComposite {

    public static final int CONTEXT_COLUMN_WIDTH = 200;

    private TreeViewer viewer;

    private ContextViewerProvider provider;

    private IContextModelManager modelManager = null;

    private ContextTableCellModifier cellModifier;

    private DefaultCellEditorFactory cellFactory;

    private ConfigureContextAction configContext;

    private ToolItem contextConfigButton;

    private CellEditor[] cellEditors;

    private ContextValueErrorChecker valueChecker;

    private static final int VALUES_INDEX = 1;

    /**
     * Constructor.
     * 
     * @param parent
     * @param style
     */
    public ContextTableValuesComposite(Composite parent, IContextModelManager manager) {
        super(parent, SWT.NONE);
        modelManager = manager;
        cellFactory = new DefaultCellEditorFactory(this);
        this.setBackground(parent.getBackground());
        this.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).create());
        initializeUI();
    }

    /**
     * zwang Comment method "initializeUI".
     * 
     * @param viewer
     */
    private void initializeUI() {
        final ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.NO_BACKGROUND);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.TOP).applyTo(toolBar);

        createToolBar(toolBar);

        viewer = new TreeViewer(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        Tree tree = viewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        createColumnsAndCellEditors(tree, getContexts());
        tree.layout();

        boolean isRepositoryContext = (modelManager instanceof ContextComposite)
                && ((ContextComposite) modelManager).isRepositoryContext();
        cellModifier = new ContextTableCellModifier(this, isRepositoryContext);
        viewer.setCellModifier(cellModifier);

        provider = new ContextViewerProvider();
        changeContextProvider();

        final TreeEditor treeEditor = new TreeEditor(viewer.getTree());

        viewer.getTree().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                if (modelManager.isReadOnly()) {
                    return;
                }
                Point pt = new Point(e.x, e.y);
                if (e.x > 0 && e.x < (viewer.getTree().getColumnCount()) * ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH) {
                    createEditorListener(treeEditor, e.x / CONTEXT_COLUMN_WIDTH);
                }
                TreeItem item = viewer.getTree().getItem(pt);
                // deactivate the current cell editor
                if (cellEditor != null && !cellEditor.getControl().isDisposed()) {
                    deactivateCellEditor(treeEditor, e.x / CONTEXT_COLUMN_WIDTH);
                }
                if (item != null && !item.isDisposed()) {
                    Rectangle rect = item.getBounds(viewer.getTree().getColumnCount() - 1);

                    if (e.x > 0 && e.x < (viewer.getTree().getColumnCount()) * ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH) {
                        handleSelect(item, viewer.getTree(), treeEditor, viewer.getTree().getColumnCount() - 1, e.x
                                / CONTEXT_COLUMN_WIDTH);
                    }
                }

            }
        });
        valueChecker = new ContextValueErrorChecker(viewer);
        if (LanguageManager.getCurrentLanguage() == ECodeLanguage.PERL) {
            createTreeTooltip(tree);
        }
    }

    /**
     * cli Comment method "changeContextProvider".
     */
    private void changeContextProvider() {
        boolean groupBySource = false;
        IPreferenceStore preferenceStore = getPreferenceStore();
        if (preferenceStore != null) {
            groupBySource = preferenceStore.getBoolean(ITalendCorePrefConstants.CONTEXT_GROUP_BY_SOURCE);
        }
        if (groupBySource) {
            provider.setProvider(new GroupBySourceTableProvider(this));
        } else {
            provider.setProvider(new GroupByNothingTableProvider(this));
        }

        viewer.setLabelProvider(provider);
        viewer.setContentProvider(provider);
        addSorter(viewer);
    }

    /**
     * bqian Comment method "createTreeTooltip".
     * 
     * @param tree
     */
    protected void createTreeTooltip(final Tree tree) {
        new AbstractTreeTooltip(tree) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.tooltip.AbstractTreeTooltip#getTooltipContent(org.eclipse.swt.widgets.TreeItem)
             */
            @Override
            public String getTooltipContent(TreeItem item) {

                String property = ""; //$NON-NLS-1$
                if (properties != null && properties.length > VALUES_INDEX) {
                    property = properties[VALUES_INDEX];
                }

                IContextParameter para = cellModifier.getRealParameter(property, item.getData());
                if (para.getType().equalsIgnoreCase(PerlTypesManager.STRING)) {
                    return Messages.getString("PromptDialog.stringTip"); //$NON-NLS-1$
                }

                return null;
            }
        };
    }

    private void addSorter(final TreeViewer viewer2) {
        final Tree table = viewer2.getTree();
        Listener sortListener = new Listener() {

            private int direction = 1;

            @Override
            public void handleEvent(Event e) {
                final TreeColumn column = (TreeColumn) e.widget;

                if (column == table.getSortColumn()) {
                    direction = -direction;
                }
                if (direction == 1) {
                    table.setSortDirection(SWT.UP);
                } else {
                    table.setSortDirection(SWT.DOWN);
                }

                table.setSortColumn(column);
                viewer2.setSorter(new ViewerSorter() {

                    int index = 0;

                    @Override
                    public void sort(Viewer viewer, Object[] elements) {
                        boolean found = false;
                        // find the sort column index
                        for (index = 0; index < table.getColumns().length; index++) {
                            if (table.getColumn(index) == table.getSortColumn()) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            index = 0; // first one as default
                        }
                        super.sort(viewer, elements);
                    }

                    @Override
                    public int compare(Viewer viewer, Object e1, Object e2) {
                        ITableLabelProvider labelProvider = (ITableLabelProvider) viewer2.getLabelProvider();
                        String columnText = labelProvider.getColumnText(e1, index) != null ? labelProvider.getColumnText(e1,
                                index) : ""; //$NON-NLS-1$
                        String columnText2 = labelProvider.getColumnText(e2, index) != null ? labelProvider.getColumnText(e2,
                                index) : ""; //$NON-NLS-1$
                        return getComparator().compare(columnText, columnText2) * direction;
                    }
                });
            }
        };
        table.getColumn(0).addListener(SWT.Selection, sortListener);
        if (getContexts().size() > 0) {
            for (int i = 0; i < getContexts().size(); i++) {
                table.getColumn(i + 1).addListener(SWT.Selection, sortListener);
            }
        }
        table.setSortColumn(table.getColumn(0));
        table.setSortDirection(SWT.UP);
    }

    private void activateCellEditor(final TreeItem item, final Tree tree, final TreeEditor treeEditor, int columnIndex, int column) {

        IContextParameter para = cellModifier.getRealParameter(properties[column], item.getData());

        if (para == null) {
            return;
        }
        valueChecker.checkErrors(item, column, para);
        if (!para.isBuiltIn()) {
            // not built-in
            return;
        }
        cellEditor = cellFactory.getCustomCellEditor(para, tree);

        if (cellEditor == null) {
            // unable to create the editor
            return;
        }

        // activate the cell editor
        cellEditor.activate();
        // if the cell editor has no control we can stop now
        Control control = cellEditor.getControl();
        if (control == null) {
            cellEditor.deactivate();
            cellEditor = null;
            return;
        }
        Text textControl = valueChecker.getTextControl(control);
        if (textControl != null) {
            if (ContextParameterUtils.isPasswordType(para)) {
                textControl.setEchoChar('*');
            } else {
                textControl.setEchoChar((char) 0);
            }
        }

        valueChecker.register(control);
        // add our editor listener
        cellEditor.addListener(createEditorListener(treeEditor, column));

        // set the layout of the tree editor to match the cell editor
        CellEditor.LayoutData layout = cellEditor.getLayoutData();
        treeEditor.horizontalAlignment = layout.horizontalAlignment;
        treeEditor.grabHorizontal = layout.grabHorizontal;
        treeEditor.minimumWidth = layout.minimumWidth;

        treeEditor.setEditor(control, item, column);
        // give focus to the cell editor
        cellEditor.setFocus();

    }

    protected void handleSelect(final TreeItem item, final Tree tree, final TreeEditor treeEditor, int columnIndex, int column) {
        // get the new selection
        activateCellEditor(item, tree, treeEditor, columnIndex, column);
    }

    @Override
    public boolean isGroupBySource() {
        boolean isRepositoryContext = false;
        if (modelManager != null) {
            isRepositoryContext = (modelManager instanceof ContextComposite)
                    && ((ContextComposite) modelManager).isRepositoryContext();
        }
        boolean value = getPreferenceStore().getBoolean(ITalendCorePrefConstants.CONTEXT_GROUP_BY_SOURCE);
        return value && !isRepositoryContext;
    }

    private void deactivateCellEditor(final TreeEditor tableEditor, int columnIndex) {
        tableEditor.setEditor(null, null, columnIndex);
        if (cellEditor != null) {
            Control control = cellEditor.getControl();
            if (control != null) {
                valueChecker.unregister(control);
            }
            cellEditor.deactivate();
            cellEditor.removeListener(editorListener);
            cellEditor = null;
        }
    }

    private ICellEditorListener createEditorListener(final TreeEditor tableEditor, final int columnIndex) {
        editorListener = new ICellEditorListener() {

            @Override
            public void cancelEditor() {
                deactivateCellEditor(tableEditor, columnIndex);
            }

            @Override
            public void editorValueChanged(boolean oldValidState, boolean newValidState) {
            }

            @Override
            public void applyEditorValue() {
                editing = true;
            }
        };
        return editorListener;
    }

    /**
     * bqian Comment method "createMenuBar".
     * 
     * @param toolBar
     */
    private void createToolBar(final ToolBar toolBar) {
        configContext = new ConfigureContextAction(modelManager, this.getShell());
        contextConfigButton = new ToolItem(toolBar, SWT.PUSH);

        contextConfigButton.setImage(ImageProvider.getImage(configContext.getImageDescriptor()));
        contextConfigButton.setToolTipText(configContext.getText());
        contextConfigButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                configContext.run();
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        configContext.setEnabled(enabled);

    }

    private ICellEditorListener editorListener;

    private CellEditor cellEditor;

    private String[] properties;

    private boolean editing;

    /**
     * bqian Comment method "getContexts".
     * 
     * @return
     */
    public List<IContext> getContexts() {
        List<IContext> contexts = new ArrayList<IContext>();
        IContextManager cm = modelManager.getContextManager();
        if (cm != null) {
            contexts = cm.getListContext();
        }
        return contexts;
    }

    @Override
    public IContextModelManager getContextModelManager() {
        return this.modelManager;
    }

    @Override
    public TreeViewer getViewer() {
        return this.viewer;
    }

    public ContextValueErrorChecker getValueChecker() {
        return this.valueChecker;
    }

    @Override
    public void refresh() {
        if (editing) {
            viewer.refresh();
            editing = false;
            return;
        }
        final Tree tree = viewer.getTree();
        TreeColumn[] columns = tree.getColumns();
        for (TreeColumn tableColumn : columns) {
            tableColumn.dispose();
        }
        List<IContext> contextList = getContexts();

        createColumnsAndCellEditors(tree, contextList);

        changeContextProvider();

        List<IContextParameter> contextTemplate = ContextTemplateComposite.computeContextTemplate(contextList);
        viewer.setInput(contextTemplate);
        viewer.expandAll();
        contextConfigButton.setEnabled(!modelManager.isReadOnly());
        // (feature 1597)
        checkItemValueErrors(tree.getItems());
    }

    /**
     * cli Comment method "createColumnsAndCellEditors".
     */
    private void createColumnsAndCellEditors(final Tree tree, List<IContext> contextList) {
        TreeColumn column = new TreeColumn(tree, SWT.NONE);
        column.setText(Messages.getString("ConextTableValuesComposite.nameLabel")); //$NON-NLS-1$
        column.setWidth(ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH);

        for (IContext context : contextList) {
            column = new TreeColumn(tree, SWT.NONE);
            column.setText(context.getName());
            column.setWidth(ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH);
        }

        cellEditors = new CellEditor[getContexts().size() + 1];
        for (int i = 0; i < getContexts().size() + 1; i++) {
            if (i == 0) {
                cellEditors[i] = null;
            } else {
                cellEditors[i] = new TextCellEditor(tree);
            }
        }
        properties = new String[contextList.size() + 1];
        properties[0] = ContextTableConstants.COLUMN_NAME_PROPERTY;
        for (int i = 0; i < contextList.size(); i++) {
            properties[i + 1] = contextList.get(i).getName();
        }
        viewer.setColumnProperties(properties);
        viewer.setCellEditors(cellEditors);
    }

    public String[] getColumnProperties() {
        return this.properties;
    }

    private void checkItemValueErrors(final TreeItem[] items) {
        if (items == null) {
            return;
        }
        for (TreeItem item : items) {
            for (int i = 1; i < viewer.getColumnProperties().length; i++) {
                IContextParameter para = cellModifier.getRealParameter((String) viewer.getColumnProperties()[i], item.getData());
                if (para != null && para instanceof IContextParameter) {
                    valueChecker.checkErrors(item, i, para);
                }
            }
            checkItemValueErrors(item.getItems());
        }
    }

    /**
     * Clear the data in this viewer.
     * 
     * @param jobContextManager2
     */
    public void clear() {
        final Tree tree = viewer.getTree();
        TreeColumn[] columns = tree.getColumns();
        for (TreeColumn tableColumn : columns) {
            tableColumn.dispose();
        }
        viewer.setInput(Collections.EMPTY_LIST);
    }

    /**
     * DOC zli ContextCompare class global comment. Detailled comment
     */
    private class ContextCompare implements java.util.Comparator<IContext> {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(IContext o1, IContext o2) {
            String name1 = o1.getName().toUpperCase();
            String name2 = o2.getName().toUpperCase();
            return name1.compareTo(name2);
        }
    }

}
