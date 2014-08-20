// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.ConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.DefaultComparator;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.DetailGlazedListsEventLayer;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsSortModel;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.tree.GlazedListTreeData;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.tree.GlazedListTreeRowModel;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupExpandCollapseLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupModel.ColumnGroup;
import org.eclipse.nebula.widgets.nattable.group.ColumnGroupReorderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.RowHideShowLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.command.ColumnHideCommand;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.painter.layer.NatGridLayerPainter;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.RowSelectionProvider;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortConfigAttributes;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.tree.SortableTreeComparator;
import org.eclipse.nebula.widgets.nattable.tree.TreeLayer;
import org.eclipse.nebula.widgets.nattable.tree.config.DefaultTreeLayerConfiguration;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.service.IMetadataManagmentUiService;
import org.talend.core.ui.context.model.ContextTabChildModel;
import org.talend.core.ui.context.model.table.ContextTableConstants;
import org.talend.core.ui.context.model.table.ContextTableTabParentModel;
import org.talend.core.ui.context.nattableTree.ContextNatTableBackGroudPainter;
import org.talend.core.ui.context.nattableTree.ContextNatTableConfiguration;
import org.talend.core.ui.context.nattableTree.ContextRowDataListFixture;
import org.talend.core.ui.context.nattableTree.ContextTextPainter;
import org.talend.core.ui.context.nattableTree.ExtendedContextColumnPropertyAccessor;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IRepositoryNode.ENodeType;
import org.talend.repository.model.RepositoryNode;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.TreeList;

/**
 * created by ldong on Jul 10, 2014 Detailled comment
 * 
 */
public class ContextTreeTable {

    private NatTable natTable;

    private IStructuredSelection currentNatTabSel;

    private final static String TREE_CONTEXT_ROOT = "root";

    private final static String TREE_DEFAULT_NODE = "node";

    // by default sort by the model id
    private final static String TREE_CONTEXT_ID = "orderId";

    public ContextTreeTable() {
    }

    public TControl createTable(Composite parentContainer, IContextModelManager manager,
            List<ContextTableTabParentModel> listOfData) {
        TControl retObj = createTableControl(parentContainer, manager, listOfData);
        retObj.setControl(retObj.getControl());
        return retObj;
    }

    public IStructuredSelection getSelection() {
        return currentNatTabSel;
    }

    public List<IContext> getContexts(IContextManager contextManger) {
        List<IContext> contexts = new ArrayList<IContext>();
        if (contextManger != null) {
            contexts = contextManger.getListContext();
        }
        return contexts;
    }

    public void refresh() {
        if (natTable == null) {
            return;
        }
        natTable.refresh();
    }

    /**
     * create the context NatTable
     * 
     * @param parent
     * @param data
     * @return
     */
    private TControl createTableControl(Composite parent, final IContextModelManager manager,
            List<ContextTableTabParentModel> listOfData) {
        ConfigRegistry configRegistry = new ConfigRegistry();
        ColumnGroupModel columnGroupModel = new ColumnGroupModel();
        configRegistry.registerConfigAttribute(SortConfigAttributes.SORT_COMPARATOR, new DefaultComparator());
        String[] propertyNames = ContextRowDataListFixture.getPropertyNames(manager);
        int comWidth = parent.getParent().getParent().getClientArea().width;
        // the data source for the context
        if (propertyNames.length > 0) {
            treeNodes.clear();
            contructContextTrees(manager, listOfData);
            EventList<ContextTreeNode> eventList = GlazedLists.eventList(treeNodes.values());
            SortedList<ContextTreeNode> sortedList = new SortedList<ContextTreeNode>(eventList, null);
            // init Column header layer
            IColumnPropertyAccessor<ContextTreeNode> columnPropertyAccessor = new ExtendedContextColumnPropertyAccessor<ContextTreeNode>(
                    propertyNames, columnGroupModel);

            IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(propertyNames);
            DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);

            // init context tree model layer for the body layer
            ISortModel sortModel = new GlazedListsSortModel(sortedList, columnPropertyAccessor, configRegistry,
                    columnHeaderDataLayer);

            final TreeList<ContextTreeNode> treeList = new TreeList(sortedList, new ContextTreeFormat(sortModel),
                    new ContextExpansionModel());
            GlazedListTreeData<ContextTreeNode> treeData = new ContextTreeData(treeList);

            final GlazedListsDataProvider<ContextTreeNode> bodyDataProvider = new GlazedListsDataProvider(treeList,
                    columnPropertyAccessor);
            // the main dataLayer
            DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

            DetailGlazedListsEventLayer<ContextTreeNode> glazedListsEventLayer = new DetailGlazedListsEventLayer<ContextTreeNode>(
                    bodyDataLayer, treeList);

            // set up Body layer
            ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(glazedListsEventLayer);
            ColumnGroupReorderLayer columnGroupReorderLayer = new ColumnGroupReorderLayer(columnReorderLayer, columnGroupModel);
            ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(columnGroupReorderLayer);
            // context columns hide or show for the column group
            ColumnGroupExpandCollapseLayer columnGroupExpandCollapseLayer = new ColumnGroupExpandCollapseLayer(
                    columnHideShowLayer, columnGroupModel);

            RowHideShowLayer rowHideShowLayer = new RowHideShowLayer(columnGroupExpandCollapseLayer);

            final TreeLayer treeLayer = new TreeLayer(rowHideShowLayer, new GlazedListTreeRowModel<ContextTreeNode>(treeData),
                    false);

            SelectionLayer selectionLayer = new SelectionLayer(treeLayer);

            ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

            // set up Cloumn group layer
            ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

            ColumnGroupHeaderLayer columnGroupHeaderLayer = new ColumnGroupHeaderLayer(columnHeaderLayer, selectionLayer,
                    columnGroupModel);
            AddContextColumnGroupsBehaviour(columnGroupHeaderLayer,
                    ContextRowDataListFixture.getContexts(manager.getContextManager()));

            // Register labels
            SortHeaderLayer<ContextTreeNode> sortHeaderLayer = new SortHeaderLayer<ContextTreeNode>(columnGroupHeaderLayer,
                    sortModel, false);

            // set up Row header layer
            DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
            DefaultRowHeaderDataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
            RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, viewportLayer, selectionLayer);

            // set up Corner layer
            DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider,
                    rowHeaderDataProvider);
            DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
            CornerLayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, sortHeaderLayer);

            // set up the final Grid layer
            final GridLayer gridLayer = new GridLayer(viewportLayer, sortHeaderLayer, rowHeaderLayer, cornerLayer);

            // config the column edit configuration
            ColumnOverrideLabelAccumulator labelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
            bodyDataLayer.setConfigLabelAccumulator(labelAccumulator);
            registerColumnLabels(labelAccumulator, ContextRowDataListFixture.getContexts(manager.getContextManager()));

            natTable = new NatTable(parent, NatTable.DEFAULT_STYLE_OPTIONS | SWT.BORDER, gridLayer, false);
            natTable.setConfigRegistry(configRegistry);

            addCustomStylingBehaviour(bodyDataProvider, columnGroupModel, manager.getContextManager());

            natTable.addConfiguration(new DefaultTreeLayerConfiguration(treeLayer));

            // hide the prompt column by default if the checkbox totally no check
            List<Integer> hideColumnsPos = addCustomHideColumnsBehaviour(manager, columnGroupModel, bodyDataLayer);

            List<Integer> checkColumnPos = getAllCheckPosBehaviour(manager, columnGroupModel);

            int dataColumnsWidth = bodyDataLayer.getWidth();

            int maxWidth = (comWidth > dataColumnsWidth) ? comWidth : dataColumnsWidth;

            // for caculate the suitable column size for when maxmum or minmum the context tab

            addCustomColumnsResizeBehaviour(bodyDataLayer, hideColumnsPos, checkColumnPos, cornerLayer.getWidth(), maxWidth);

            NatGridLayerPainter layerPainter = new NatGridLayerPainter(natTable);
            natTable.setLayerPainter(layerPainter);

            attachCheckColumnTip(natTable);

            natTable.configure();

            GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);

            this.natTable.addMouseListener(new MouseListener() {

                @Override
                public void mouseDoubleClick(MouseEvent e) {
                    // get the row position for the click in the NatTable
                    int rowPos = natTable.getRowPositionByY(e.y);
                    int rowIndex = natTable.getRowIndexByPosition(rowPos);
                    ContextTreeNode treeNode = bodyDataProvider.getRowObject(rowIndex);
                    if (treeNode != null && (treeNode.getChildren().size() != 0 || treeNode.getParent() != null)) {
                        String repositoryContextName = (treeNode.getChildren().size() != 0) ? treeNode.getName() : treeNode
                                .getParent().getName();
                        List<IRepositoryViewObject> contextObjs;
                        try {
                            contextObjs = ProxyRepositoryFactory.getInstance().getAll(
                                    ProjectManager.getInstance().getCurrentProject(), ERepositoryObjectType.CONTEXT);
                            for (IRepositoryViewObject contextObj : contextObjs) {
                                if (contextObj.getProperty().getLabel().equals(repositoryContextName)) {
                                    RepositoryNode relateNode = new RepositoryNode(contextObj, null, ENodeType.REPOSITORY_ELEMENT);
                                    contextObj.setRepositoryNode(relateNode);
                                    if (GlobalServiceRegister.getDefault().isServiceRegistered(IMetadataManagmentUiService.class)) {
                                        IMetadataManagmentUiService mmUIService = (IMetadataManagmentUiService) GlobalServiceRegister
                                                .getDefault().getService(IMetadataManagmentUiService.class);
                                        mmUIService.openRepositoryContextWizard(relateNode);
                                    }
                                }
                            }
                        } catch (PersistenceException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                @Override
                public void mouseDown(MouseEvent e) {

                }

                @Override
                public void mouseUp(MouseEvent e) {

                }

            });

            // add selection listener for the context NatTable
            ISelectionProvider selectionProvider = new RowSelectionProvider<ContextTreeNode>(selectionLayer, bodyDataProvider,
                    false);// selectionLayer columnHeaderLayer.getSelectionLayer()

            selectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {

                @Override
                public void selectionChanged(SelectionChangedEvent event) {
                    currentNatTabSel = (IStructuredSelection) event.getSelection();
                }
            });

            TControl retObj = new TControl();
            retObj.setControl(natTable);
            return retObj;
        }
        return null;
    }

    private List<Integer> getAllCheckPosBehaviour(IContextModelManager manager, ColumnGroupModel contextGroupModel) {
        List<Integer> checkPos = new ArrayList<Integer>();
        if (manager.getContextManager() != null) {
            List<IContext> contexts = manager.getContextManager().getListContext();
            for (IContext envContext : contexts) {
                ColumnGroup group = contextGroupModel.getColumnGroupByName(envContext.getName());
                int checkIndex = group.getMembers().get(1);
                checkPos.add(checkIndex);
            }
        }
        return checkPos;
    }

    private void addCustomColumnsResizeBehaviour(DataLayer dataLayer, List<Integer> hideColumnsPos,
            List<Integer> checkColumnsPos, int cornerWith, int maxWidth) {
        dataLayer.setColumnsResizableByDefault(true);
        int dataColumnsCount = dataLayer.getPreferredColumnCount();

        int fixedCheckBoxWidth = 30;

        int fixedTypeWidth = 90;

        int typeColumnPos = dataLayer.getColumnPositionByIndex(1);

        int leftWidth = maxWidth - cornerWith - fixedTypeWidth - fixedCheckBoxWidth * checkColumnsPos.size();

        int currentColumnsCount = dataColumnsCount - hideColumnsPos.size() - checkColumnsPos.size() - 1;
        int averageWidth = leftWidth / currentColumnsCount;
        for (int i = 0; i < dataLayer.getColumnCount(); i++) {
            boolean findHide = false;
            boolean findCheck = false;
            boolean findType = false;
            if (typeColumnPos == i) {
                findType = true;
                dataLayer.setColumnWidthByPosition(i, fixedTypeWidth);
            }
            for (int hidePos : hideColumnsPos) {
                if (hidePos == i) {
                    findHide = true;
                    dataLayer.setColumnWidthByPosition(i, 0);
                }
            }
            for (int checkPos : checkColumnsPos) {
                if (checkPos == i) {
                    findCheck = true;
                    dataLayer.setColumnWidthByPosition(i, fixedCheckBoxWidth);
                }
            }
            if (!findHide && !findCheck && !findType) {
                dataLayer.setColumnWidthByPosition(i, averageWidth);
            }
        }
    }

    private List<Integer> addCustomHideColumnsBehaviour(IContextModelManager manager, ColumnGroupModel contextGroupModel,
            DataLayer dataLayer) {
        List<Integer> hidePos = new ArrayList<Integer>();
        if (manager.getContextManager() != null) {
            List<IContext> contexts = manager.getContextManager().getListContext();
            for (IContext envContext : contexts) {
                boolean needHidePrompt = true;
                ColumnGroup group = contextGroupModel.getColumnGroupByName(envContext.getName());
                // get every context's prompt to see if need to hide or not,decide by the check of prompt
                int promptIndex = group.getMembers().get(2);
                List<IContextParameter> list = envContext.getContextParameterList();
                if (list != null && list.size() > 0) {
                    for (IContextParameter contextPara : list) {
                        if (contextPara.isPromptNeeded()) {
                            needHidePrompt = false;
                            break;
                        }
                    }
                }
                if (needHidePrompt) {
                    int hidePosition = dataLayer.getColumnPositionByIndex(promptIndex);
                    hidePos.add(hidePosition);
                    natTable.doCommand(new ColumnHideCommand(dataLayer, hidePosition));
                }
            }
        }
        return hidePos;
    }

    private void addCustomStylingBehaviour(final GlazedListsDataProvider<ContextTreeNode> bodyDataProvider,
            ColumnGroupModel groupModel, IContextManager manager) {
        DefaultNatTableStyleConfiguration natTableConfiguration = new DefaultNatTableStyleConfiguration();
        // for the repository context's background.we control its backgroup colour is gray
        natTableConfiguration.cellPainter = new ContextNatTableBackGroudPainter(new ContextTextPainter(false, false, false),
                bodyDataProvider);

        natTable.addConfiguration(natTableConfiguration);

        // add configuration for the context columns to do the column edit,color,style,etc.
        natTable.addConfiguration(new ContextNatTableConfiguration(bodyDataProvider, groupModel, manager));

        // natTable.addConfiguration(new ContextSelectBindings());
    }

    private void addCustomSelectionBehaviour(final IContextModelManager manager, final ColumnGroupModel contextGroupModel,
            final GlazedListsDataProvider<ContextTreeNode> bodyDataProvider) {

        this.natTable.addLayerListener(new ILayerListener() {

            @Override
            public void handleLayerEvent(ILayerEvent event) {
            }
        });
    }

    private void registerColumnLabels(ColumnOverrideLabelAccumulator columnLabelAccumulator, List<IContext> contexts) {
        columnLabelAccumulator.registerColumnOverrides(0, new String[] { ContextTableConstants.COLUMN_NAME_PROPERTY });
        columnLabelAccumulator.registerColumnOverrides(1, new String[] { ContextTableConstants.COLUMN_TYPE_PROPERTY });
        // the columns after "type" will caculated by the contexts
        int j = 2;
        for (int i = 0; i < contexts.size(); i++) {
            columnLabelAccumulator.registerColumnOverrides(j++, new String[] { ContextTableConstants.COLUMN_CONTEXT_VALUE });
            columnLabelAccumulator.registerColumnOverrides(j++, new String[] { ContextTableConstants.COLUMN_CHECK_PROPERTY });
            columnLabelAccumulator.registerColumnOverrides(j++, new String[] { ContextTableConstants.COLUMN_PROMPT_PROPERTY });
        }
    }

    private void AddContextColumnGroupsBehaviour(ColumnGroupHeaderLayer columnHeaderLayer, List<IContext> contexts) {
        int i = 1;
        for (IContext context : contexts) {
            String evnContext = context.getName();
            columnHeaderLayer.addColumnsIndexesToGroup(evnContext, new int[] { ++i, ++i, ++i });
        }
    }

    private static class ContextTreeFormat implements TreeList.Format<ContextTreeNode> {

        private final ISortModel sortModel;

        public ContextTreeFormat(ISortModel sortModel) {
            this.sortModel = sortModel;
        }

        @Override
        public boolean allowsChildren(ContextTreeNode element) {
            return true;
        }

        @Override
        public Comparator<ContextTreeNode> getComparator(int depth) {
            return new SortableTreeComparator<ContextTreeNode>(GlazedLists.beanPropertyComparator(ContextTreeNode.class,
                    TREE_CONTEXT_ID), sortModel);
        }

        /*
         * (non-Javadoc)
         * 
         * @see ca.odell.glazedlists.TreeList.Format#getPath(java.util.List, java.lang.Object)
         */
        @Override
        public void getPath(List<ContextTreeNode> path, ContextTreeNode element) {
            path.add(element);
            ContextTreeTable.ContextTreeNode parent = element.getParent();
            while (parent != null) {
                path.add(parent);
                parent = parent.getParent();
            }
            Collections.reverse(path);
        }
    }

    private static class ContextTreeData extends GlazedListTreeData<ContextTreeNode> {

        public ContextTreeData(TreeList<ContextTreeNode> treeList) {
            super(treeList);
        }

        @Override
        public String formatDataForDepth(int depth, ContextTreeNode object) {
            return object.getName();
        }

    }

    private static class ContextExpansionModel implements TreeList.ExpansionModel<ContextTreeNode> {

        @Override
        public boolean isExpanded(ContextTreeNode element, List<ContextTreeNode> path) {
            return true;
        }

        @Override
        public void setExpanded(ContextTreeNode element, List<ContextTreeNode> path, boolean expanded) {
        }
    }

    /**
     * A control and it's width.
     */
    public class TControl {

        Control control;

        Integer width;

        /**
         * Getter for control.
         * 
         * @return the control
         */
        public Control getControl() {
            return this.control;
        }

        /**
         * Sets the control.
         * 
         * @param control the control to set
         */
        public void setControl(Control control) {
            this.control = control;
        }

        /**
         * Getter for width.
         * 
         * @return the width
         */
        public Integer getWidth() {
            return this.width;
        }

        /**
         * Sets the width.
         * 
         * @param width the width to set
         */
        public void setWidth(Integer width) {
            this.width = width;
        }
    }

    private Map<String, ContextTreeNode> treeNodes = new HashMap<String, ContextTreeNode>();

    private void createContextTreeNode(int orderId, IContextModelManager manager, Object data, String parent,
            String currentNodeName) {
        ContextTreeNode datum = new ContextTreeNode(orderId, manager, data, treeNodes.get(parent), currentNodeName);
        treeNodes.put(currentNodeName, datum);
    }

    public class ContextTreeNode implements Comparable<ContextTreeNode> {

        private IContextModelManager manager;

        private Object treeData;

        private final ContextTreeNode parent;

        private final List<ContextTreeNode> children = new ArrayList<ContextTreeNode>();

        private final String name;

        private final int orderId;

        public ContextTreeNode(int orderId, IContextModelManager manager, Object data, ContextTreeNode parent, String name) {
            this.orderId = orderId;
            this.manager = manager;
            this.treeData = data;
            this.parent = parent;
            if (parent != null) {
                parent.addChild(this);
            }

            this.name = name;
        }

        public ContextTreeNode getParent() {
            return parent;
        }

        public IContextModelManager getManager() {
            return manager;
        }

        public Object getTreeData() {
            return treeData;
        }

        public void addChild(ContextTreeNode child) {
            children.add(child);
        }

        public List<ContextTreeNode> getChildren() {
            return children;
        }

        public ContextTreeNode getSelf() {
            return this;
        }

        public String getName() {
            return name;
        }

        public int getOrderId() {
            return this.orderId;
        }

        /**
         * Comparison is based on name only
         */
        @Override
        public int compareTo(ContextTreeNode o) {
            if (this.orderId > o.orderId) {
                return 1;
            } else if (this.orderId < o.orderId) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    private void contructContextTrees(IContextModelManager manager, List<ContextTableTabParentModel> listOfData) {
        if (listOfData.size() > 0) {
            for (ContextTableTabParentModel contextModel : listOfData) {
                if (contextModel.hasChildren()) {
                    createContextTreeNode(contextModel.getOrder(), manager, contextModel, TREE_CONTEXT_ROOT,
                            contextModel.getSourceName());
                    List<ContextTabChildModel> childModels = contextModel.getChildren();
                    for (ContextTabChildModel childModel : childModels) {
                        createContextTreeNode(contextModel.getOrder(), manager, childModel, contextModel.getSourceName(),
                                childModel.getContextParameter().getName());
                    }
                } else {
                    createContextTreeNode(contextModel.getOrder(), manager, contextModel, TREE_CONTEXT_ROOT, contextModel
                            .getContextParameter().getName());
                }
            }
        } else {
            // if empty data,we still need construct the empty tree
            for (int i = 0; i < 8; i++) {
                ContextTableTabParentModel emptyLevelNode = new ContextTableTabParentModel();
                createContextTreeNode(i, manager, emptyLevelNode, TREE_CONTEXT_ROOT, TREE_DEFAULT_NODE + i);
            }
        }
    }

    private void attachCheckColumnTip(NatTable nt) {
        DefaultToolTip toolTip = new ContextNatTableToolTip(nt);
        toolTip.setBackgroundColor(natTable.getDisplay().getSystemColor(7));
        toolTip.setPopupDelay(500);
        toolTip.activate();
        toolTip.setShift(new Point(10, 10));
    }

    private class ContextNatTableToolTip extends DefaultToolTip {

        private NatTable nt;

        public ContextNatTableToolTip(NatTable natTable) {
            super(natTable, 2, false);
            this.nt = natTable;
        }

        @Override
        protected Object getToolTipArea(Event event) {
            int col = this.nt.getColumnPositionByX(event.x);
            int row = this.nt.getRowPositionByY(event.y);

            Object cellValue = this.nt.getDataValueByPosition(col, row);

            if (cellValue instanceof Boolean) {
                return new Point(col, row);
            }
            return null;
        }

        @Override
        protected String getText(Event event) {
            return "activate prompt on variable";
        }

        @Override
        protected Composite createToolTipContentArea(Event event, Composite parent) {
            return super.createToolTipContentArea(event, parent);
        }
    }
}
