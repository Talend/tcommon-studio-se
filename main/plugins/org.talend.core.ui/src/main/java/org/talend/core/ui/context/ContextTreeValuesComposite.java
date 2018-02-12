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

import java.util.Collections;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.tooltip.AbstractTreeTooltip;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.metadata.types.PerlTypesManager;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.ContextProviderProxy;
import org.talend.core.ui.context.model.ContextValueErrorChecker;
import org.talend.core.ui.context.model.ContextViewerProvider;
import org.talend.core.ui.context.model.tree.ContextTreeCellModifier;
import org.talend.core.ui.context.model.tree.ContextTreeConstants;
import org.talend.core.ui.context.model.tree.GroupByContextAction;
import org.talend.core.ui.context.model.tree.GroupByVariableAction;
import org.talend.core.ui.context.model.tree.GroupByVariableProvider;
import org.talend.core.ui.i18n.Messages;

/**
 * DOC bqian class global comment. Detailled comment <br/>
 * 
 */
public class ContextTreeValuesComposite extends AbstractContextTabEditComposite {

    private TreeViewer viewer;

    private TreeColumn column1st;

    private TreeColumn column2nd;

    private ContextViewerProvider provider;

    private IContextModelManager modelManager;

    private GroupByVariableAction groupByVariable;

    private GroupByContextAction groupByContext;

    private ContextTreeCellModifier cellModifier;

    private DefaultCellEditorFactory cellFactory;

    private ConfigureContextAction configContext;

    private ToolItem contextConfigButton;

    private ContextValueErrorChecker valueChecker;

    /**
     * bqian ConextTemplateComposite constructor comment.
     * 
     * @param parent
     * @param style
     */
    public ContextTreeValuesComposite(Composite parent, IContextModelManager manager) {
        super(parent, SWT.NONE);
        this.setBackground(parent.getBackground());
        modelManager = manager;
        cellFactory = new DefaultCellEditorFactory(this);
        this.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).create());
        initializeUI();
    }

    /**
     * bqian Comment method "initializeUI".
     */
    private void initializeUI() {
        Composite toolbarContainer = new Composite(this, SWT.NONE);
        toolbarContainer.setLayout(GridLayoutFactory.swtDefaults().spacing(0, 0).margins(0, 0).numColumns(2).create());
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.TOP).grab(true, false).applyTo(toolbarContainer);
        final ToolBar toolBar = new ToolBar(toolbarContainer, SWT.FLAT | SWT.NO_BACKGROUND);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.TOP).applyTo(toolBar);

        createToolBar(toolBar);

        final ToolBar menuBar = new ToolBar(toolbarContainer, SWT.FLAT | SWT.NO_BACKGROUND);
        GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.TOP).applyTo(menuBar);
        createMenuBar(menuBar);

        viewer = new TreeViewer(this, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        final Tree tree = viewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        column1st = new TreeColumn(tree, SWT.NONE);
        column1st.setText(ContextTreeConstants.getVariableColumnName());
        column1st.setWidth(ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH);
        column2nd = new TreeColumn(tree, SWT.NONE);
        column2nd.setText(ContextTreeConstants.getContextColumnName());
        column2nd.setWidth(ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH);

        TreeColumn column = new TreeColumn(tree, SWT.NONE);
        column.setResizable(false);
        column.setWidth(20);

        column = new TreeColumn(tree, SWT.NONE);
        column.setText(Messages.getString("ContextTreeConstants.promptName")); //$NON-NLS-1$
        column.setWidth(ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH);
        column = new TreeColumn(tree, SWT.NONE);
        column.setText(Messages.getString("ContextTreeConstants.valueName")); //$NON-NLS-1$
        column.setWidth(ContextTableValuesComposite.CONTEXT_COLUMN_WIDTH);

        boolean isRepositoryContext = (modelManager instanceof ContextComposite)
                && ((ContextComposite) modelManager).isRepositoryContext();

        viewer.setCellEditors(new CellEditor[] { null, null, new CheckboxCellEditor(tree), new TextCellEditor(tree),
                new TextCellEditor(tree) });
        cellModifier = new ContextTreeCellModifier(this, isRepositoryContext);
        viewer.setCellModifier(cellModifier);

        provider = new ContextViewerProvider();
        provider.setProvider(new GroupByVariableProvider(modelManager));
        viewer.setLabelProvider(provider);
        viewer.setContentProvider(provider);

        addSorter(viewer);

        setDefaultPresentationType();

        final TreeEditor treeEditor = new TreeEditor(tree);
        createEditorListener(treeEditor);

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseDown(MouseEvent e) {
                if (modelManager.isReadOnly()) {
                    return;
                }
                Point pt = new Point(e.x, e.y);
                TreeItem item = tree.getItem(pt);
                // deactivate the current cell editor
                if (cellEditor != null && !cellEditor.getControl().isDisposed()) {
                    deactivateCellEditor(treeEditor);
                }
                if (item != null && !item.isDisposed()) {
                    Rectangle rect = item.getBounds(ContextTreeConstants.VARIABLE_COLUMN_INDEX);
                    if (rect.contains(pt)) {
                        handleSelect(item, tree, treeEditor);
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
     * DOC bqian Comment method "createTreeTooltip".
     * 
     * @param tree
     */
    protected void createTreeTooltip(Tree tree) {
        new AbstractTreeTooltip(tree) {

            /*
             * (non-Javadoc)
             * 
             * @see
             * org.talend.commons.ui.swt.tooltip.AbstractTreeTooltip#getTooltipContent(org.eclipse.swt.widgets.TreeItem)
             */
            @Override
            public String getTooltipContent(TreeItem item) {

                IContextParameter para = cellModifier.getRealParameter(item.getData());
                // see the third note of the bug 3749.
                if (para != null && para.getType().equalsIgnoreCase(PerlTypesManager.STRING)) {
                    return Messages.getString("PromptDialog.stringTip"); //$NON-NLS-1$
                }

                return null;
            }
        };

    }

    /**
     * DOC bqian Comment method "addSorter".
     * 
     * @param viewer2
     */
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
                        while (index < table.getColumns().length && table.getColumn(index) != column) {
                            index++;
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
                viewer2.expandAll();
            }
        };
        table.getColumn(0).addListener(SWT.Selection, sortListener);
        table.getColumn(1).addListener(SWT.Selection, sortListener);
        table.setSortColumn(table.getColumn(0));
        table.setSortDirection(SWT.UP);
    }

    @Override
    public void setEnabled(boolean enabled) {
        configContext.setEnabled(enabled);
    }

    @Override
    public TreeViewer getViewer() {
        return this.viewer;
    }

    @Override
    public IContextModelManager getContextModelManager() {
        return this.modelManager;
    }

    public ContextValueErrorChecker getValueChecker() {
        return this.valueChecker;
    }

    private void activateCellEditor(final TreeItem item, final Tree tree, final TreeEditor treeEditor) {
        IContextParameter para = cellModifier.getRealParameter(item.getData());
        if (para == null) {
            return;
        }
        valueChecker.checkErrors(item, ContextTreeConstants.VARIABLE_COLUMN_INDEX, para);
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
        cellEditor.addListener(createEditorListener(treeEditor));

        // set the layout of the tree editor to match the cell editor
        CellEditor.LayoutData layout = cellEditor.getLayoutData();
        treeEditor.horizontalAlignment = layout.horizontalAlignment;
        treeEditor.grabHorizontal = layout.grabHorizontal;
        treeEditor.minimumWidth = layout.minimumWidth;
        treeEditor.setEditor(control, item, ContextTreeConstants.VARIABLE_COLUMN_INDEX);
        // give focus to the cell editor
        cellEditor.setFocus();

    }

    protected void handleSelect(final TreeItem item, final Tree tree, final TreeEditor treeEditor) {
        // get the new selection
        activateCellEditor(item, tree, treeEditor);
    }

    private void deactivateCellEditor(final TreeEditor tableEditor) {
        tableEditor.setEditor(null, null, ContextTreeConstants.VARIABLE_COLUMN_INDEX);
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

    private ICellEditorListener createEditorListener(final TreeEditor tableEditor) {
        editorListener = new ICellEditorListener() {

            @Override
            public void cancelEditor() {
                deactivateCellEditor(tableEditor);
            }

            @Override
            public void editorValueChanged(boolean oldValidState, boolean newValidState) {
            }

            @Override
            public void applyEditorValue() {
            }
        };
        return editorListener;
    }

    private ICellEditorListener editorListener;

    private CellEditor cellEditor;

    /**
     * bqian Comment method "createMenuBar".
     * 
     * @param toolBar
     */
    private void createToolBar(final ToolBar toolBar) {
        configContext = new ConfigureContextAction(modelManager, this.getShell());
        contextConfigButton = new ToolItem(toolBar, SWT.PUSH);
        // contextConfigButton.setDisabledImage();
        contextConfigButton.setImage(ImageProvider.getImage(configContext.getImageDescriptor()));
        contextConfigButton.setToolTipText(configContext.getText());
        contextConfigButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                configContext.run();
            }
        });
    }

    private void createMenuBar(final ToolBar menuBar) {
        ToolItem pullDownButton = new ToolItem(menuBar, SWT.PUSH);
        // Image hoverImage = WorkbenchImages.getImage(IWorkbenchGraphicConstants.IMG_LCL_RENDERED_VIEW_MENU);
        Image hoverImage = PlatformUI.getWorkbench().getSharedImages().getImage("IMG_LCL_RENDERED_VIEW_MENU"); //$NON-NLS-1$
        pullDownButton.setDisabledImage(hoverImage);
        pullDownButton.setImage(hoverImage);
        // pullDownButton.setToolTipText(WorkbenchMessages.Menu);
        pullDownButton.setToolTipText(Messages.getString("ContextTreeValuesComposite.Menu")); //$NON-NLS-1$
        pullDownButton.setWidth(5);

        MenuManager menuManager = new MenuManager("Context Presentation"); //$NON-NLS-1$

        groupByVariable = new GroupByVariableAction(this);
        groupByContext = new GroupByContextAction(this);

        menuManager.add(groupByVariable);
        menuManager.add(groupByContext);

        final Menu aMenu = menuManager.createContextMenu(menuBar.getParent());

        pullDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                Point toolbarSize = menuBar.getSize();
                toolbarSize = menuBar.toDisplay(0, toolbarSize.y);
                aMenu.setLocation(toolbarSize);
                aMenu.setVisible(true);
            }
        });
    }

    @Override
    public void refresh() {
        IContextManager cm = modelManager.getContextManager();
        viewer.setInput(cm.getListContext());
        // viewer.expandAll();
        contextConfigButton.setEnabled(!modelManager.isReadOnly());

        // (feature 1597)
        checkItemValueErrors(viewer.getTree().getItems());
    }

    public void checkItemValueErrors(final TreeItem[] items) {
        if (items == null) {
            return;
        }
        for (TreeItem item : items) {
            IContextParameter para = cellModifier.getRealParameter(item.getData());
            if (para != null && para instanceof IContextParameter) {
                valueChecker.checkErrors(item, ContextTreeConstants.VARIABLE_COLUMN_INDEX, para);
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
        viewer.setInput(Collections.EMPTY_LIST);
    }

    public TreeColumn getColumn1st() {
        return this.column1st;
    }

    public TreeColumn getColumn2nd() {
        return this.column2nd;
    }

    public void setProviderProxy(ContextProviderProxy providerProxy) {
        this.provider.setProvider(providerProxy);
    }

    private void setDefaultPresentationType() {
        IPreferenceStore store = getPreferenceStore();
        String presentationType = store.getString(ContextTreeConstants.PRESENTATION_TYPE_KEY);
        if (presentationType == null
                || presentationType.length() == 0
                || store.getString(ContextTreeConstants.PRESENTATION_TYPE_KEY).equals(
                        ContextTreeConstants.VARIABLE_COLUMN_PROPERTY)) {
            groupByVariable.setChecked(true);
            groupByContext.setChecked(false);
            groupByVariable.run();
        } else if (store.getString(ContextTreeConstants.PRESENTATION_TYPE_KEY).equals(
                ContextTreeConstants.CONTEXT_COLUMN_PROPERTY)) {
            groupByContext.setChecked(true);
            groupByVariable.setChecked(false);
            groupByContext.run();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.AbstractContextTabEditComposite#isGroupBySource()
     */
    @Override
    public boolean isGroupBySource() {
        return false;
    }

}
