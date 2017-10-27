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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.runtime.xml.XmlUtil;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinksManager;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.builder.ConvertionHelper;
import org.talend.core.model.metadata.builder.connection.MetadataColumn;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.metadata.builder.connection.XMLFileNode;
import org.talend.core.model.metadata.builder.connection.XmlFileConnection;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;
import org.talend.cwm.helper.ConnectionHelper;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.FoxNodeComboViewProvider;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Attribute;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Element;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.NameSpaceNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.TreeUtil;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlFileStepForm;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.CreateAttributeAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.CreateElementAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.CreateNameSpaceAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.DeleteNodeAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.DisconnectAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.FixValueAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.ImportTreeFromXMLAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.RemoveGroupAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.SetForLoopAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.action.SetGroupAction;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.buttons.AddTreeNodeButton;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.buttons.MoveDownTreeNodeButton;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.buttons.MoveUpTreeNodeButton;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.buttons.RemoveTreeNodeButton;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.XmlFileSchema2TreeLinker;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.XmlFileSchemaDialog;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.XmlFileTableViewerProvider;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.XmlFileTreeViewerProvider;
import org.talend.repository.ui.wizards.metadata.connection.files.xml.view.XmlTree2SchemaLinker;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileOutputStep2Form extends AbstractXmlFileStepForm {

    private SashForm mainSashFormComposite;

    private Button schemaButton;

    private ComboViewer rootComboViewer;

    private Combo rootCombo;

    private TableViewer schemaViewer;

    private TreeViewer xmlViewer;

    private IAction createAction;

    private IAction deleteAction;

    private IAction disconnectAction;

    private IAction fixValueAction;

    private IAction createAttributeAction;

    private IAction createNamespaceAction;

    private IAction importFromXMLAction;

    private IAction setLoopAction;

    private IAction setGroupAction;

    private IAction removeGroupAction;

    private XmlFileSchema2TreeLinker linker;

    private String selectedText;

    private List<FOXTreeNode> treeData = new ArrayList<FOXTreeNode>();

    private boolean creation;

    private AddTreeNodeButton addTreeNodeBtn;

    private RemoveTreeNodeButton removeNodeBtn;

    private MoveUpTreeNodeButton moveUpBtn;

    private MoveDownTreeNodeButton moveDown;

    public XmlFileOutputStep2Form(boolean creation, Composite parent, ConnectionItem connectionItem) {
        super(parent, connectionItem);
        this.creation = creation;
        setupForm();
    }

    @Override
    protected void adaptFormToReadOnly() {

    }

    @Override
    protected void addFields() {
        mainSashFormComposite = new SashForm(this, SWT.HORIZONTAL | SWT.SMOOTH);
        mainSashFormComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        mainSashFormComposite.setBackgroundMode(SWT.INHERIT_FORCE);
        addSchemaViewer(mainSashFormComposite, 300, 100);
        addXmlFileViewer(mainSashFormComposite, 400, 100);
        mainSashFormComposite.setWeights(new int[] { 40, 60 });

        linker = new XmlFileSchema2TreeLinker(mainSashFormComposite);
        linker.setForm(this);
        linker.init(schemaViewer.getTable(), xmlViewer);

        XmlTree2SchemaLinker oppositeLinker = new XmlTree2SchemaLinker(mainSashFormComposite);
        oppositeLinker.setConnection(getConnection());
        oppositeLinker.setDelegateLinker(linker);
        oppositeLinker.init(xmlViewer, schemaViewer);
    }

    private void initSchemaTable() {
        List<MetadataColumn> columnList = new ArrayList<MetadataColumn>();
        if (ConnectionHelper.getTables(getConnection()).size() > 0) {
            EList columns = ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0].getColumns();
            for (int i = 0; i < columns.size(); i++) {
                columnList.add((MetadataColumn) columns.get(i));
            }
        }
        schemaViewer.setInput(columnList);
        schemaViewer.refresh();
    }

    private void initRootCombo() {
        List<ATreeNode> rootNodes = ((XmlFileWizard) getPage().getWizard()).getRootNodes();
        if (rootNodes != null) {
            rootComboViewer.setInput(rootNodes);
            ATreeNode rootNode = getDefaultRootNode(rootNodes);
            rootCombo.select(rootNodes.indexOf(rootNode));
        }
    }

    public void resetRootCombo() {
        if (rootCombo != null) {
            rootCombo.deselectAll();
        }
    }

    @Override
    public void redrawLinkers() {
        int maxColumnsNumber = CoreUIPlugin.getDefault().getPreferenceStore()
                .getInt(ITalendCorePrefConstants.MAXIMUM_AMOUNT_OF_COLUMNS_FOR_XML);
        if (schemaViewer.getTable().getItems().length <= maxColumnsNumber + 1) {
            // linker.removeAllLinks();
            // TreeItem root = xmlViewer.getTree().getItem(0);
            // TableItem[] tableItems = schemaViewer.getTable().getItems();
            // initLinker(root, tableItems);
            // // if (linker.linkSize() == 0) {
            // linker.updateLinksStyleAndControlsSelection(xmlViewer.getTree(), true);
            // // }
            linker.createLinks();
        }
    }

    private void addXmlFileViewer(final Composite mainComposite, final int width, final int height) {
        final Group group = Form.createGroup(mainComposite, 1, "Linker Target", height);
        GridData data = new GridData(GridData.FILL_BOTH);
        Composite composite = new Composite(group, SWT.BORDER);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(data);

        rootComboViewer = new ComboViewer(composite, SWT.READ_ONLY);
        FoxNodeComboViewProvider comboProvider = new FoxNodeComboViewProvider();
        rootComboViewer.setLabelProvider(comboProvider);
        rootComboViewer.setContentProvider(comboProvider);
        rootCombo = rootComboViewer.getCombo();
        GridData comboData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        rootCombo.setLayoutData(comboData);

        xmlViewer = new TreeViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        data = new GridData(GridData.FILL_BOTH);
        xmlViewer.getControl().setLayoutData(data);
        xmlViewer.setUseHashlookup(true);
        Tree tree = xmlViewer.getTree();
        if (isReadOnly()) {
            tree.setEnabled(false);
        }
        tree.setLinesVisible(true);
        tree.setBackground(tree.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("XML Tree");
        column1.setWidth(120);

        TreeColumn column2 = new TreeColumn(tree, SWT.CENTER);
        column2.setText("Related Column");
        column2.setWidth(100);

        TreeColumn column3 = new TreeColumn(tree, SWT.CENTER);
        column3.setText("Node Status");
        column3.setWidth(100);

        TreeColumn column4 = new TreeColumn(tree, SWT.CENTER);
        column4.setText("Default Value");
        column4.setWidth(100);

        tree.setHeaderVisible(true);
        XmlFileTreeViewerProvider provider = new XmlFileTreeViewerProvider();
        xmlViewer.setLabelProvider(provider);

        xmlViewer.setCellModifier(new ICellModifier() {

            @Override
            public void modify(Object element, String property, Object value) {
                TreeItem treeItem = (TreeItem) element;
                FOXTreeNode node = (FOXTreeNode) treeItem.getData();
                if (property.equals("C1")) {
                    node.setLabel((String) value);
                }
                if (property.equals("C4")) {
                    node.setDefaultValue((String) value);
                }
                xmlViewer.refresh(node);
            }

            @Override
            public Object getValue(Object element, String property) {
                FOXTreeNode node = (FOXTreeNode) element;
                if (property.equals("C1")) { //$NON-NLS-1$
                    return node.getLabel();
                }
                if (property.equals("C4")) { //$NON-NLS-1$
                    return node.getDefaultValue();
                }

                return null;
            }

            @Override
            public boolean canModify(Object element, String property) {
                FOXTreeNode node = (FOXTreeNode) element;
                if (property.equals("C1")) {
                    if (node.getLabel() != null && node.getLabel().length() > 0) {
                        return true;
                    }
                }
                if (property.equals("C4")) {
                    if (node.getDefaultValue() != null && node.getDefaultValue().length() > 0) {
                        return true;
                    }
                }
                return false;
            }
        });

        xmlViewer.setColumnProperties(new String[] { "C1", "C2", "C3", "C4" });
        CellEditor editor = new TextCellEditor(xmlViewer.getTree());
        editor.addListener(new DialogErrorXMLLabelCellEditor(editor, "C1"));
        CellEditor editorDefault = new TextCellEditor(xmlViewer.getTree());
        editorDefault.addListener(new DialogErrorXMLLabelCellEditor(editorDefault, "C4"));

        xmlViewer.setCellEditors(new CellEditor[] { editor, null, null, editorDefault });

        xmlViewer.setContentProvider(provider);
        // xmlViewer.setInput(treeData);
        xmlViewer.expandAll();

        createAction();

        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {

            @Override
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(xmlViewer.getControl());
        xmlViewer.getControl().setMenu(menu);
        xmlViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {

            }

        });
        initToolBar(composite);

    }

    private void displayRootCombo(boolean visible) {
        if (rootCombo == null) {
            return;
        }
        rootCombo.setVisible(visible);
        GridData layoutData = (GridData) rootCombo.getLayoutData();
        layoutData.exclude = !visible;
        rootCombo.getParent().layout();
    }

    private void initToolBar(Composite parent) {
        // tool buttons
        Composite toolBarComp = new Composite(parent, SWT.BORDER);
        GridLayout layout = new GridLayout();
        layout.numColumns = 4;
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        toolBarComp.setLayout(layout);
        toolBarComp.setLayoutData(data);
        addTreeNodeBtn = new AddTreeNodeButton(toolBarComp, this);
        removeNodeBtn = new RemoveTreeNodeButton(toolBarComp, this);
        moveUpBtn = new MoveUpTreeNodeButton(toolBarComp, this);
        moveDown = new MoveDownTreeNodeButton(toolBarComp, this);
    }

    private void createAction() {
        createAction = new CreateElementAction(xmlViewer, this, "Add Sub-element");
        createAttributeAction = new CreateAttributeAction(xmlViewer, this, "Add Attribute");
        createNamespaceAction = new CreateNameSpaceAction(xmlViewer, this, "Add Name Space");
        deleteAction = new DeleteNodeAction(xmlViewer, this, "Delete");
        disconnectAction = new DisconnectAction(xmlViewer, this, "Disconnect Linker");
        fixValueAction = new FixValueAction(xmlViewer, this, "Set A Fix Value");
        importFromXMLAction = new ImportTreeFromXMLAction(xmlViewer, this, "Import XML Tree");
        setLoopAction = new SetForLoopAction(xmlViewer, this, "Set As Loop Element");
        setGroupAction = new SetGroupAction(xmlViewer, this, "Set As Group Element");
        removeGroupAction = new RemoveGroupAction(xmlViewer, "Remove Group Element", this);

    }

    private void addSchemaViewer(final Composite mainComposite, final int width, final int height) {
        final Group group = Form.createGroup(mainComposite, 1, "Linker Source", height);
        // group.setBackgroundMode(SWT.INHERIT_FORCE);
        schemaButton = new Button(group, SWT.PUSH);
        schemaButton.setText("Schema Management");
        schemaButton.setToolTipText("You can add or edit schema and save in 'Schema List' viewer");

        schemaViewer = new TableViewer(group);
        XmlFileTableViewerProvider provider = new XmlFileTableViewerProvider();
        schemaViewer.setContentProvider(provider);
        schemaViewer.setLabelProvider(provider);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        Table table = schemaViewer.getTable();
        if (isReadOnly()) {
            table.setEnabled(false);
        }
        table.setHeaderVisible(true);
        org.eclipse.swt.widgets.TableColumn column = new org.eclipse.swt.widgets.TableColumn(table, SWT.LEFT);
        column.setText("Schema List");
        column.setWidth(100);
        table.setLayoutData(gridData);

        table.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                addTreeNodeBtn.getButton().setEnabled(false);
                removeNodeBtn.getButton().setEnabled(false);
                moveUpBtn.getButton().setEnabled(false);
                moveDown.getButton().setEnabled(false);
            }
        });
    }

    private void fillContextMenu(IMenuManager manager) {
        if (!xmlViewer.getSelection().isEmpty()) {
            manager.add(createAction);
            manager.add(createAttributeAction);
            manager.add(createNamespaceAction);
            manager.add(new Separator());
            manager.add(deleteAction);
            manager.add(disconnectAction);
            manager.add(fixValueAction);
            manager.add(new Separator());
            manager.add(setLoopAction);
            manager.add(new Separator());
            manager.add(setGroupAction);
            manager.add(removeGroupAction);
            manager.add(new Separator());
            manager.add(importFromXMLAction);
        }
    }

    @Override
    protected void addFieldsListeners() {
        rootComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                ATreeNode node = (ATreeNode) selection.getFirstElement();
                List<FOXTreeNode> nodeList = getCorrespondingFoxTreeNodes(node, false);
                if (nodeList.size() == 0) {
                    return;
                }
                if (ConnectionHelper.getTables(getConnection()).size() > 0) {
                    EList schemaMetadataColumn = ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0]
                            .getColumns();
                    schemaMetadataColumn.clear();
                    initMetadataTable(nodeList, schemaMetadataColumn);
                }
                updateConnectionProperties(nodeList.get(0));
                initXmlTreeData();
                initSchemaTable();
                xmlViewer.setInput(treeData);
                xmlViewer.expandToLevel(3);
                redrawLinkers();
                if (!creation) {
                    checkFieldsValue();
                }
            }
        });
    }

    @Override
    protected void addUtilsButtonListeners() {
        schemaButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                XmlFileSchemaDialog dialog = new XmlFileSchemaDialog(mainSashFormComposite.getShell(),
                        XmlFileOutputStep2Form.this);
                if (dialog != null && dialog.open() == XmlFileSchemaDialog.OK) {
                    MetadataTable metadataTable = dialog.getMetadataTable();
                    EList columns = metadataTable.getColumns();
                    List<MetadataColumn> inputList = new ArrayList<MetadataColumn>();
                    for (int i = 0; i < columns.size(); i++) {
                        MetadataColumn column = (MetadataColumn) columns.get(i);
                        inputList.add(column);
                    }
                    schemaViewer.setInput(inputList);
                    schemaViewer.refresh();

                    EList columnList = ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0].getColumns();
                    columnList.clear();
                    columnList.addAll(inputList);

                    updateXmlTreeViewer(inputList);
                    redrawLinkers();
                    checkFieldsValue();
                }
            }
        });
    }

    private void updateXmlTreeViewer(List<MetadataColumn> metaColumns) {
        List<String> cloumnNames = new ArrayList<String>();
        for (MetadataColumn column : metaColumns) {
            cloumnNames.add(column.getName());
        }
        LinksManager<Item, Object, Tree, Object> linkManager = linker.getLinkManager();
        List<LinkDescriptor<Item, Object, Tree, Object>> links = linkManager.getLinks();
        for (int i = 0; i < links.size(); i++) {
            LinkDescriptor<Item, Object, Tree, Object> linkDescriptor = links.get(i);
            IExtremityLink<Item, Object> ex1 = linkDescriptor.getExtremity1();
            IExtremityLink<Tree, Object> ex2 = linkDescriptor.getExtremity2();
            MetadataColumn metaColumn = (MetadataColumn) ex1.getDataItem();
            FOXTreeNode node = (FOXTreeNode) ex2.getDataItem();
            if (!cloumnNames.contains(metaColumn.getName())) {
                node.setColumn(null);
            } else {
                node.setColumn(ConvertionHelper.convertToIMetaDataColumn(metaColumn));
            }
            node.setDataType(metaColumn.getTalendType());
        }
        xmlViewer.refresh();
        updateConnection();
    }

    @Override
    protected boolean checkFieldsValue() {
        int num = 0, rootNum = 0;
        StringBuffer msgError = new StringBuffer();
        List<FOXTreeNode> onLoopNodes = new ArrayList<FOXTreeNode>();
        for (FOXTreeNode node : treeData) {
            FOXTreeNode rootFOXTreeNode = TreeUtil.getRootFOXTreeNode(node);
            if (rootFOXTreeNode != null) {
                if (existedLoopNode(rootFOXTreeNode)) {
                    num++;
                } else {
                    onLoopNodes.add(rootFOXTreeNode);
                }
                rootNum++;
            }
        }
        if (num != rootNum) {
            msgError.append("Require set as loop\n");
        }
        if (linker.isNoLinker()) {
            msgError.append("No source and target linked");
        }
        if ("".equals(msgError.toString())) {
            if (this.order > 5000) { // order here is in fact the number of element
                updateStatus(IStatus.WARNING,
                        "The number of elements seems too important, please reduce the size of the tree.\nActually there is: "
                                + this.order + 1 + "elements.");
                return true;
            }
            updateStatus(IStatus.OK, null);
            return true;
        }

        updateStatus(IStatus.ERROR, msgError.toString());
        return false;
    }

    @Override
    public void updateStatus() {
        checkFieldsValue();
    }

    @Override
    protected void initialize() {
    }

    public IMetadataTable getIMetadataTable() {
        IMetadataTable metadataTable = null;
        if (ConnectionHelper.getTables(getConnection()) != null && !ConnectionHelper.getTables(getConnection()).isEmpty()) {
            metadataTable = ConvertionHelper
                    .convert(ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0]);
        } else {
            metadataTable = new org.talend.core.model.metadata.MetadataTable();
        }
        return metadataTable;
    }

    protected FOXTreeNode addElement(FOXTreeNode current, String currentPath, String newPath, String defaultValue) {
        String name = newPath.substring(newPath.lastIndexOf("/") + 1); //$NON-NLS-1$
        String parentPath = newPath.substring(0, newPath.lastIndexOf("/")); //$NON-NLS-1$
        FOXTreeNode temp = new Element(name, defaultValue);

        if (current == null) {// root node
            return temp;
        }

        if (currentPath.equals(parentPath)) {
            current.addChild(temp);
        } else {
            String[] nods = currentPath.split("/"); //$NON-NLS-1$
            String[] newNods = parentPath.split("/"); //$NON-NLS-1$
            int parentLevel = 0;
            int checkLength = nods.length < newNods.length ? nods.length : newNods.length;
            for (int i = 1; i < checkLength; i++) {
                if (nods[i].equals(newNods[i])) {
                    parentLevel = i;
                }
            }
            FOXTreeNode parent = current;
            for (int i = 0; i < nods.length - (parentLevel + 1); i++) {
                FOXTreeNode tmpParent = parent.getParent();
                if (tmpParent == null) {
                    break;
                }
                parent = tmpParent;
            }

            if (parent != null) {
                parent.addChild(temp);
            }
        }

        return temp;
    }

    private IMetadataColumn getColumn(String columnName) {
        EList columns = ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0].getColumns();
        for (int i = 0; i < columns.size(); i++) {
            MetadataColumn column = (MetadataColumn) columns.get(i);
            if (column.getLabel().equals(columnName)) {
                return ConvertionHelper.convertToIMetaDataColumn(column);
            }
        }
        return null;
    }

    private void initXmlTreeData() {
        // This function initialize the FOXTreeNode from the emf model.

        treeData.clear();
        // metadataColumnList.clear();
        EList root = getConnection().getRoot();
        EList loop = getConnection().getLoop();
        EList group = getConnection().getGroup();
        // initialMetadataColumn();

        FOXTreeNode rootNode = null;
        FOXTreeNode current = null;
        FOXTreeNode temp = null;
        FOXTreeNode mainNode = null;
        String mainPath = null;
        String currentPath = null;
        String defaultValue = null;
        int nodeOrder = 0;
        boolean haveOrder = true;

        // build root tree
        for (int i = 0; i < root.size(); i++) {
            XMLFileNode node = (XMLFileNode) root.get(i);
            String newPath = node.getXMLPath();
            defaultValue = node.getDefaultValue();
            String type = node.getType();
            String orderValue = String.valueOf(node.getOrder());
            if (orderValue == null || "".equals(orderValue)) {
                haveOrder = false;
            }
            if (haveOrder) {
                nodeOrder = node.getOrder();
            }
            if (node.getAttribute().equals("attri")) {
                temp = new Attribute(newPath);
                temp.setDefaultValue(defaultValue);
                temp.setAttribute(true);
                temp.setDataType(type);
                current.addChild(temp);
            } else if (node.getAttribute().equals("ns")) {
                temp = new NameSpaceNode(newPath);
                temp.setDefaultValue(defaultValue);
                temp.setNameSpace(true);
                temp.setDataType(type);
                current.addChild(temp);
            } else {
                temp = this.addElement(current, currentPath, newPath, defaultValue);
                temp.setDataType(type);
                if (rootNode == null) {
                    rootNode = temp;
                }
                if (node.getAttribute().equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                current = temp;
                currentPath = newPath;
            }
            if (haveOrder) {
                temp.setOrder(nodeOrder);
                if (this.order < nodeOrder) {
                    this.order = nodeOrder;
                }
            }
            String columnName = node.getRelatedColumn();
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(getColumn(columnName));
            }
        }

        // build group tree
        current = mainNode;
        currentPath = mainPath;
        boolean isFirst = true;
        for (int i = 0; i < group.size(); i++) {
            XMLFileNode node = (XMLFileNode) group.get(i);
            String newPath = node.getXMLPath();
            defaultValue = node.getDefaultValue();
            String type = node.getType();
            String orderValue = String.valueOf(node.getOrder());
            if (orderValue == null || "".equals(orderValue)) {
                haveOrder = false;
            }
            if (haveOrder) {
                nodeOrder = node.getOrder();
            }
            if (node.getAttribute().equals("attri")) {
                temp = new Attribute(newPath);
                temp.setDefaultValue(defaultValue);
                temp.setAttribute(true);
                temp.setDataType(type);
                current.addChild(temp);
            } else if (node.getAttribute().equals("ns")) {
                temp = new NameSpaceNode(newPath);
                temp.setDefaultValue(defaultValue);
                temp.setNameSpace(true);
                temp.setDataType(type);
                current.addChild(temp);
            } else {
                temp = this.addElement(current, currentPath, newPath, defaultValue);
                temp.setDataType(type);
                if (node.getAttribute().equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                if (isFirst) {
                    temp.setGroup(true);
                    isFirst = false;
                }
                current = temp;
                currentPath = newPath;
            }
            if (haveOrder) {
                temp.setOrder(nodeOrder);
            }
            String columnName = node.getRelatedColumn();
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(getColumn(columnName));
            }
        }

        // build loop tree
        current = mainNode;
        currentPath = mainPath;
        isFirst = true;
        for (int i = 0; i < loop.size(); i++) {
            XMLFileNode node = (XMLFileNode) loop.get(i);
            String newPath = node.getXMLPath();
            defaultValue = node.getDefaultValue();
            String type = node.getType();
            String orderValue = String.valueOf(node.getOrder());
            if (orderValue == null || "".equals(orderValue)) {
                haveOrder = false;
            }
            if (haveOrder) {
                nodeOrder = node.getOrder();
            }
            if (node.getAttribute().equals("attri")) {
                temp = new Attribute(newPath);
                temp.setDefaultValue(defaultValue);
                temp.setAttribute(true);
                temp.setDataType(type);
                temp.setDataType(type);
                current.addChild(temp);
            } else if (node.getAttribute().equals("ns")) {
                temp = new NameSpaceNode(newPath);
                temp.setDefaultValue(defaultValue);
                temp.setNameSpace(true);
                temp.setDataType(type);
                current.addChild(temp);
            } else {
                temp = this.addElement(current, currentPath, newPath, defaultValue);
                temp.setDataType(type);
                // fix for TDI-18802 , if root node is loop
                if (rootNode == null) {
                    rootNode = temp;
                }

                if (node.getAttribute().equals("main")) {
                    temp.setMain(true);
                    mainNode = temp;
                    mainPath = newPath;
                }
                if (isFirst) {
                    temp.setLoop(true);
                    isFirst = false;
                }
                current = temp;
                currentPath = newPath;
            }
            if (haveOrder) {
                temp.setOrder(nodeOrder);
            }
            String columnName = node.getRelatedColumn();
            if (columnName != null && columnName.length() > 0) {
                temp.setColumn(getColumn(columnName));
            }
        }

        if (rootNode == null) {
            rootNode = new Element("rootTag");
        }
        rootNode.setParent(null);
        if (haveOrder) {
            orderNode(rootNode);
        }
        treeData.add(rootNode);
        initNodeOrder(rootNode);
    }

    @Override
    public MetadataTable getMetadataTable() {
        return ConnectionHelper.getTables(getConnection()).toArray(new MetadataTable[0])[0];
    }

    @Override
    public TableViewer getSchemaViewer() {
        return this.schemaViewer;
    }

    @Override
    public void updateConnection() {
        // This function initialize the emf model from the data available in the FOXTreeNode elements

        ConnectionHelper.getTables(getConnection());
        EList root = getConnection().getRoot();
        EList loop = getConnection().getLoop();
        EList group = getConnection().getGroup();
        root.clear();
        loop.clear();
        group.clear();
        List<FOXTreeNode> node = (List<FOXTreeNode>) xmlViewer.getInput();
        FOXTreeNode foxTreeNode = node.get(0);
        if (foxTreeNode != null) {
            initNodeOrder(foxTreeNode);
            if (!foxTreeNode.isLoop()) {
                tableLoader((Element) foxTreeNode, "", root, foxTreeNode.getDefaultValue());
            }
            Element loopNode = (Element) TreeUtil.getLoopNode(foxTreeNode);
            if (loopNode != null) {
                String path = TreeUtil.getPath(loopNode);
                tableLoader(loopNode, path.substring(0, path.lastIndexOf("/")), loop, loopNode.getDefaultValue());
            }
            Element groupNode = (Element) TreeUtil.getGroupNode(foxTreeNode);
            if (groupNode != null) {
                String path = TreeUtil.getPath(groupNode);
                tableLoader(groupNode, path.substring(0, path.lastIndexOf("/")), group, groupNode.getDefaultValue());
            }
        }

    }

    private void orderNode(FOXTreeNode node) {
        // reset the order.
        if (node != null) {
            List<FOXTreeNode> firstSubChildren = node.getChildren();
            FOXTreeNode foundNode = null;
            for (FOXTreeNode childen : firstSubChildren) {
                if (childen.isLoop()) {
                    foundNode = childen;
                    sortOrder(foundNode, node);
                    break;
                } else if (childen.isGroup()) {
                    foundNode = childen;
                    sortOrder(foundNode, node);
                    orderNode(childen);
                } else {
                    orderNode(childen);
                }
            }
        }
    }

    private void sortOrder(FOXTreeNode treeNode, FOXTreeNode node) {
        if (node != null) {
            List<FOXTreeNode> children = node.getChildren();
            if (treeNode != null) {
                int tmpOrder = 0;
                int attrNsCount = 0;
                for (FOXTreeNode child : children) {
                    if (child.getOrder() < treeNode.getOrder()) {
                        tmpOrder++;
                    }
                    if (child.isAttribute() || child.isNameSpace()) {
                        attrNsCount++;
                    }
                }
                if (tmpOrder > -1) {
                    int oldOrder = children.indexOf(treeNode);
                    if (oldOrder != -1 && oldOrder != tmpOrder) {
                        node.removeChild(treeNode);
                        if (children.size() > tmpOrder) {
                            node.addChild(tmpOrder - attrNsCount, treeNode);
                        } else {
                            node.addChild(treeNode);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void setSelectedText(String label) {
        selectedText = label;
    }

    @Override
    public List<FOXTreeNode> getTreeData() {
        return treeData;
    }

    public XmlFileConnection getConn() {
        return getConnection();
    }

    private boolean existedLoopNode(FOXTreeNode node) {
        if (node != null) {
            if (node.isLoop()) {
                return true;
            }
            for (FOXTreeNode child : node.getChildren()) {
                if (existedLoopNode(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean existedGroupNode(FOXTreeNode node) {
        if (node != null) {
            if (node.isGroup()) {
                return true;
            }
            for (FOXTreeNode child : node.getChildren()) {
                if (existedGroupNode(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (super.isVisible()) {
            initXmlTreeData();
            initSchemaTable();
            boolean useXsd = isUseXsd();
            displayRootCombo(useXsd);
            if (useXsd) {
                initRootCombo();
            }
            xmlViewer.setInput(treeData);
            xmlViewer.expandToLevel(3);
            redrawLinkers();
            // if (!creation) {
            checkFieldsValue();
            // }
        }
    }

    private boolean isUseXsd() {
        return XmlUtil.isXSDFile(getConnection().getXmlFilePath());
    }

    /**
     * class global comment. Detailled comment
     */
    class DialogErrorXMLLabelCellEditor implements ICellEditorListener {

        CellEditor editor;

        String property;

        Boolean validateLabel;

        @Override
        public void applyEditorValue() {
            String text = getControl().getText();
            onValueChanged(text, true, property);
        }

        @Override
        public void cancelEditor() {
        }

        @Override
        public void editorValueChanged(boolean oldValidState, boolean newValidState) {
            onValueChanged(getControl().getText(), false, property);
        }

        private void onValueChanged(final String newValue, boolean showAlertIfError, String property) {
            final Text text = getControl();
            FOXTreeNode selectNode = null;
            ISelection selection = xmlViewer.getSelection();
            if (selection instanceof TreeSelection) {
                Object obj = ((TreeSelection) selection).getFirstElement();
                if (obj instanceof FOXTreeNode) {
                    selectNode = (FOXTreeNode) obj;
                }
            }
            String errorMessage = null;

            if ("C4".equals(property)) { //$NON-NLS-1$
                validateLabel = StringUtil.validateLabelForFixedValue(text.getText());
            }
            if ("C1".equals(property) && selectNode != null && selectNode instanceof NameSpaceNode) {
                validateLabel = StringUtil.validateLabelForNameSpace(text.getText());
            } else {
                validateLabel = StringUtil.validateLabelForXML(text.getText());
            }
            if (!validateLabel) {
                errorMessage = "Invalid string for XML Label. Label was not changed.";
            }

            if (errorMessage == null) {
                text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_WHITE));
            } else {
                text.setBackground(text.getDisplay().getSystemColor(SWT.COLOR_RED));
                if (showAlertIfError) {
                    text.setText(selectedText);
                    MessageDialog.openError(text.getShell(), "Invalid XML label.", errorMessage);
                }
            }
        }

        public DialogErrorXMLLabelCellEditor(CellEditor editor, String property) {
            super();
            this.property = property;
            this.editor = editor;
        }

        private Text getControl() {
            return (Text) editor.getControl();
        }

    }

    @Override
    public TreeViewer getTreeViewer() {
        return this.xmlViewer;
    }

}
