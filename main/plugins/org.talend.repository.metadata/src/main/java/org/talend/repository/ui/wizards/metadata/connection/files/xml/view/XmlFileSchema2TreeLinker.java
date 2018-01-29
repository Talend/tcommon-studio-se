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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.talend.commons.ui.runtime.utils.TableUtils;
import org.talend.commons.ui.swt.drawing.link.ExtremityLink;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.IStyleLink;
import org.talend.commons.ui.swt.drawing.link.ItemExtremityDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinksManager;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.swt.linking.TableToTreeLinker;
import org.talend.metadata.managment.ui.wizard.metadata.xml.XmlExtractorBgRefresher;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.repository.metadata.ui.wizards.form.AbstractXmlStepForm;

/**
 * wzhang class global comment. Detailled comment
 */
public class XmlFileSchema2TreeLinker extends TableToTreeLinker<Object, Object> {

    private TreeViewer xmlViewer;

    private Color selectedLoopLinkColor;

    private Color selectedRelativeLinkColor;

    private AbstractXmlStepForm form;

    private StyleLink selectedLoopStyleLink;

    private Comparator<LinkDescriptor<Item, Object, Tree, Object>> drawingLinksComparator;

    public XmlFileSchema2TreeLinker(Composite commonParent) {
        super(commonParent);
    }

    public void init(Table schemaTable, TreeViewer xmlViewer) {
        init(schemaTable, xmlViewer.getTree(), new XmlExtractorBgRefresher(this));
        this.xmlViewer = xmlViewer;
        init();
    }

    public void init(XmlFileOutputMetadataEmfTableEditorView xmlEmfTableEditorView, TreeViewer xmlViewer) {
        init(xmlEmfTableEditorView.getTable(), xmlViewer.getTree(), new XmlExtractorBgRefresher(this));
        this.xmlViewer = xmlViewer;
        init();
    }

    private void init() {
        Display display = getBgDrawableComposite().getDisplay();
        initColors(display);

        setUnselectedStyleLink(createStandardLink(display.getSystemColor(SWT.COLOR_BLUE)));

        getSelectedRelativeStyleLink();

        initListeners();
    }

    public AbstractXmlStepForm getForm() {
        return this.form;
    }

    public void setForm(AbstractXmlStepForm form) {
        this.form = form;
    }

    public TreeViewer getXMLViewer() {
        return this.xmlViewer;
    }

    private void initColors(Display display) {
        // selectedLoopLinkColor = new Color(display, 255, 131, 255);
        selectedLoopLinkColor = new Color(display, 110, 168, 255);// light blue
        selectedRelativeLinkColor = new Color(display, 110, 168, 0);
        getSource().addDisposeListener(new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                selectedLoopLinkColor.dispose();
                selectedRelativeLinkColor.dispose();
                getSource().removeDisposeListener(this);
            }
        });

    }

    private void getSelectedRelativeStyleLink() {
        setSelectedStyleLink(createStandardLink(selectedLoopLinkColor));
    }

    private void initListeners() {
        new XmlFileDragAndDropHandler(this);
    }

    public void valuedChanged(Widget widget) {
        onXPathValueChanged(widget);
    }

    public void onXPathValueChanged(Widget widget) {
        if (widget != null) {
            linksManager.removeLinksFromDataItem2(widget.getData());
        }
        createLinks();
    }

    public LinksManager<Item, Object, Tree, Object> getLinkManager() {
        return getLinksManager();
    }

    public void createLinks() {
        Map<String, TableItem> nameToTableItem = new HashMap<String, TableItem>();
        for (TableItem curTableItem : getSource().getItems()) {
            nameToTableItem.put(curTableItem.getText(), curTableItem);
        }
        removeAllLinks();
        getBackgroundRefresher().refreshBackground();
        List<FOXTreeNode> treeData = form.getTreeData();
        createLoopLinks(treeData, nameToTableItem);
        getBackgroundRefresher().refreshBackground();
    }

    private void createLoopLinks(List<FOXTreeNode> treeData, Map<String, TableItem> nameToTableItem) {
        for (FOXTreeNode treeNode : treeData) {
            if (treeNode.getColumn() != null) {
                TableItem tableItem = nameToTableItem.get(treeNode.getColumnLabel());
                if (tableItem == null) {
                    continue;
                }
                String path = tableItem.getText();
                if (path != null) {
                    addLoopLink(tableItem, tableItem.getData(), xmlViewer.getTree(), treeNode);
                }
            }
            createLoopLinks(treeNode.getChildren(), nameToTableItem);
        }
    }

    public void removeAllLinks() {
        getLinksManager().clearLinks();
    }

    public boolean isNoLinker() {
        List<LinkDescriptor<Item, Object, Tree, Object>> links = getLinksManager().getLinks();
        if (links == null || links.size() == 0) {
            return true;
        }
        return false;
    }

    public int linkSize() {
        return this.getLinksManager().getLinks().size();
    }

    private TableItem getItem(String path) {
        this.getSource().getItems();
        for (int i = 0; i < this.getSource().getItems().length; i++) {
            TableItem item = getSource().getItems()[i];
            if (path.equals(item.getText())) {
                return item;
            }

        }
        return this.getSource().getItems()[0];
    }

    @Override
    public void drawBackground(GC gc) {
        super.drawBackground(gc);
    }

    public LinkDescriptor<Item, Object, Tree, Object> addLoopLink(Item tableItem, Object dataItem1, Tree tree,
            FOXTreeNode dataItem2) {
        return addLink(tableItem, dataItem1, tree, dataItem2);
    }

    private LinkDescriptor<Item, Object, Tree, Object> addLink(Item tableItem, Object dataItem1, Tree tree, Object dataItem2) {
        LinkDescriptor<Item, Object, Tree, Object> link = new LinkDescriptor<Item, Object, Tree, Object>(
                new ItemExtremityDescriptor(tableItem, dataItem1), new ExtremityLink<Tree, Object>(tree, dataItem2));

        link.setStyleLink(getUnselectedStyleLink());
        getLinksManager().addLink(link);
        updateLinksStyleAndControlsSelection(tree);
        return link;
    }
    
    public TreeItem getTreeItem(TreeItem[] items, FOXTreeNode treeNode, boolean expandedOnly) {
        TreeItem item = null;
        for (TreeItem curItem : items) {
            // call getText method since we are in a lazy tree.
            // it will force to load this item in the GUI (since it should be displayed in all cases)
            // without this, the data will be null, and we can't retrieve anything
            curItem.getText();
            if (curItem.getData() != null) {
                if (curItem.getData().equals(treeNode)) {
                    return curItem;
                }
                // will check if one of the parent of the treeNode is this one.
                if (containsTreeNode((FOXTreeNode) curItem.getData(), treeNode)) {
                    if (!expandedOnly || curItem.getExpanded()) {
                        return getTreeItem(curItem.getItems(), treeNode, expandedOnly);
                    } else {
                        return curItem;
                    }
                }
            }
        }
        return item;
    }
    
    /**
     * DOC nrousseau Comment method "containsTreeNode".
     * 
     * @param data
     * @param treeNode
     * @return
     */
    private boolean containsTreeNode(FOXTreeNode currentNode, FOXTreeNode leafNode) {
        FOXTreeNode parentOfLeafNode = leafNode.getParent();
        if (parentOfLeafNode != null) {
            if (parentOfLeafNode.equals(currentNode)) {
                return true;
            }
            return containsTreeNode(currentNode, parentOfLeafNode);
        }
        return false;
    }

    public TreeItem getFirstVisibleTreeItemOfPath(FOXTreeNode foxTreeNode) {
        TreeItem[] items = xmlViewer.getTree().getItems();
        return getTreeItem(items, foxTreeNode, true);
    }

    public void updateLinksStyleAndControlsSelection(Control currentControl) {
        boolean isTarget = false;
        if (getSource() != currentControl) {
            isTarget = true;
        } else {
            isTarget = false;
        }

        HashSet selectedItems = new HashSet();
        Map itemsToSelect = new HashMap();

        if (isTarget) {
            getTarget().deselectAll();

            TreeItem[] selection = getTarget().getSelection();
            for (TreeItem tableItem : selection) {
                selectedItems.add(tableItem.getData());
            }
        } else {
            TableItem[] selection = getSource().getSelection();
            for (TableItem treeItem : selection) {
                selectedItems.add(treeItem.getData());
            }
        }

        List<LinkDescriptor<Item, Object, Tree, Object>> links = linksManager.getLinks();
        for (LinkDescriptor<Item, Object, Tree, Object> link : links) {
            IStyleLink styleLink = null;
            IExtremityLink extremity = null;
            IExtremityLink otherExtremity = null;
            if (isTarget) {
                extremity = link.getExtremity2();
                otherExtremity = link.getExtremity1();
            } else {
                extremity = link.getExtremity1();
                otherExtremity = link.getExtremity2();
            }
            boolean currentItemIsSelected = selectedItems.contains(extremity.getDataItem());

            if (extremity.getGraphicalObject() == xmlViewer.getTree()
                    || otherExtremity.getGraphicalObject() == xmlViewer.getTree()) {
                styleLink = getSelectedLoopStyleLink();
            } else {

                if (currentItemIsSelected) {
                    styleLink = getSelectedStyleLink();
                    if (isTarget) {

                        itemsToSelect.put(otherExtremity.getGraphicalObject(), null);

                    } else {

                        Table currentTable = (Table) otherExtremity.getGraphicalObject();
                        List<TableItem> tableItemsToSelect = (List<TableItem>) itemsToSelect.get(currentTable);

                        if (tableItemsToSelect == null) {
                            tableItemsToSelect = new ArrayList<TableItem>();
                            itemsToSelect.put(currentTable, tableItemsToSelect);
                        }
                        TableItem tableItem = TableUtils.getTableItem(currentTable, otherExtremity.getDataItem());
                        tableItemsToSelect.add(tableItem);
                    }
                } else {
                    styleLink = getUnselectedStyleLink();
                }
            }
            if (styleLink == null) {
                styleLink = getDefaultStyleLink();
            }
            link.setStyleLink(styleLink);

        }
        if (isTarget) {
            (getSource()).setSelection((TableItem[]) itemsToSelect.keySet().toArray(new TableItem[0]));
        } else {
            Set<Table> set = itemsToSelect.keySet();
            if (set.size() > 0) {
                for (Table table : set) {
                    ArrayList<TableItem> tableItemsToSelect = (ArrayList<TableItem>) itemsToSelect.get(table);
                    table.deselectAll();
                    TableItem[] tableItems = tableItemsToSelect.toArray(new TableItem[0]);
                    table.setSelection(tableItems);
                }
            } else {
                xmlViewer.getTree().deselectAll();
            }
        }
        getLinksManager().sortLinks(getDrawingLinksComparator());
    }

    public StyleLink getSelectedLoopStyleLink() {
        if (this.selectedLoopStyleLink == null) {
            this.selectedLoopStyleLink = createStandardLink(selectedLoopLinkColor);
        }
        return this.selectedLoopStyleLink;
    }

    @Override
    protected Comparator<LinkDescriptor<Item, Object, Tree, Object>> getDrawingLinksComparator() {
        if (this.drawingLinksComparator == null) {
            this.drawingLinksComparator = new Comparator<LinkDescriptor<Item, Object, Tree, Object>>() {

                @Override
                public int compare(LinkDescriptor<Item, Object, Tree, Object> link1,
                        LinkDescriptor<Item, Object, Tree, Object> link2) {
                    IStyleLink link1StyleLink = link1.getStyleLink();
                    IStyleLink link2StyleLink = link2.getStyleLink();
                    if (link1StyleLink == link2StyleLink) {
                        return 0;
                    }
                    if (link1StyleLink == getSelectedLoopStyleLink()) {
                        return 1;
                    }
                    if (link2StyleLink == getSelectedLoopStyleLink()) {
                        return -1;
                    }
                    if (link1StyleLink == getUnselectedStyleLink()) {
                        return -1;
                    }
                    return 1;
                }

            };
        }
        return this.drawingLinksComparator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.linking.TableToTreeLinker#getFirstVisibleTreeItemOfPath(java.lang.Object)
     */
    @Override
    protected TreeItem getFirstVisibleTreeItemOfPath(Object dataItem) {
        return getFirstVisibleTreeItemOfPath((FOXTreeNode) dataItem);
    }

}
