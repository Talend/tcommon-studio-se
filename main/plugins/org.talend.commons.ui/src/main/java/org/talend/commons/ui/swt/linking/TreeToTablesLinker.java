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
package org.talend.commons.ui.swt.linking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.runtime.utils.TableUtils;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.ui.swt.drawing.background.IBackgroundRefresher;
import org.talend.commons.ui.swt.drawing.background.IBgDrawableComposite;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.IDrawableLink;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.IStyleLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinksManager;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.utils.TreeUtils;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 * @param <D1> the data item of extremety 1
 * @param <D2> the data item of extremety 2
 */
public class TreeToTablesLinker<D1, D2> extends BgDrawableComposite implements IBgDrawableComposite, IControlsLinker {

    protected LinksManager<TreeItem, D1, Table, D2> linksManager = new LinksManager<TreeItem, D1, Table, D2>();

    private IStyleLink defaultStyleLink;

    private IStyleLink selectedStyleLink;

    private IStyleLink unselectedStyleLink;

    private Comparator<LinkDescriptor<TreeItem, D1, Table, D2>> drawingLinksComparator;

    private IBackgroundRefresher backgroundRefresher;

    private Display display;

    private Tree tree;

    private List<Table> tables;

    private LinkableTree linkableTree;

    private List<LinkableTable> linkableTableList;

    /**
     * DOC amaumont TreeToTableLinker constructor comment.
     * 
     * @param tree
     * @param table
     */
    public TreeToTablesLinker(Composite commonParent) {
        super(commonParent);
        linkableTableList = new ArrayList<LinkableTable>();
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param tree
     * @param tables
     * @param backgroundRefresher
     */
    public void init(Tree tree, Table[] tables, IBackgroundRefresher backgroundRefresher) {
        this.display = tree.getDisplay();
        this.backgroundRefresher = backgroundRefresher;
        for (Table table : tables) {
            LinkableTable linkableTable = new LinkableTable(this, backgroundRefresher, table, this, false);
            linkableTableList.add(linkableTable);
        }
        linkableTree = new LinkableTree(this, backgroundRefresher, tree, this, true);
        this.tables = Arrays.asList(tables);
        this.tree = tree;
    }

    public void init(Tree tree, Table[] tables, IControlsLinker controlsLinker, IBackgroundRefresher backgroundRefresher) {
        this.display = tree.getDisplay();
        this.backgroundRefresher = backgroundRefresher;
        for (Table table : tables) {
            LinkableTable linkableTable = new LinkableTable(controlsLinker, backgroundRefresher, table, this, false);
            linkableTableList.add(linkableTable);
        }
        linkableTree = new LinkableTree(controlsLinker, backgroundRefresher, tree, this, true);
        this.tables = Arrays.asList(tables);
        this.tree = tree;
    }

    protected IStyleLink getDefaultStyleLink() {
        if (defaultStyleLink == null) {
            StyleLink styleLink = new StyleLink();
            styleLink.setDrawableLink(new BezierHorizontalLink(styleLink));
            styleLink.setForegroundColor(display.getSystemColor(SWT.COLOR_BLACK));
            styleLink.setLineWidth(2);
            this.defaultStyleLink = styleLink;
        }
        return this.defaultStyleLink;
    }

    /**
     * DOC amaumont Comment method "createLinkSorter".
     */
    protected void createLinksComparators() {

        this.drawingLinksComparator = getDrawingLinksComparator();

    }

    /**
     * Define a comparator to draw links.
     */
    protected Comparator<LinkDescriptor<TreeItem, D1, Table, D2>> getDrawingLinksComparator() {
        if (this.drawingLinksComparator == null) {
            this.drawingLinksComparator = new Comparator<LinkDescriptor<TreeItem, D1, Table, D2>>() {

                public int compare(LinkDescriptor<TreeItem, D1, Table, D2> link1, LinkDescriptor<TreeItem, D1, Table, D2> link2) {
                    IStyleLink link1StyleLink = link1.getStyleLink();
                    IStyleLink link2StyleLink = link2.getStyleLink();
                    if (link1StyleLink == link2StyleLink) {
                        return 0;
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
     * @see org.talend.commons.ui.swt.drawing.link.BackgroundRefresher#drawBackground(org.eclipse.swt.graphics.GC)
     */
    @Override
    public void drawBackground(GC gc) {

        List<LinkDescriptor<TreeItem, D1, Table, D2>> links = linksManager.getLinks();
        int lstSize = links.size();

        int xStartBezierLink = findXRightStartBezierLink(tree.getItems(), 0);

        Point treeToCommonPoint = display.map(tree, getBgDrawableComposite(), new Point(0, 0));
        // System.out.println("treeToCommonPoint=" + treeToCommonPoint);

        int treeItemHeight = tree.getItemHeight();
        Rectangle treeBounds = tree.getBounds();

        if (WindowSystem.isGTK()) {
            gc.setAdvanced(false);
        } else {
            gc.fillRectangle(treeToCommonPoint.x, treeToCommonPoint.y, treeBounds.width - tree.getBorderWidth(),
                    treeBounds.height - tree.getBorderWidth());
            // System.out.println(isAntialiasAllowed());
            if (backgroundRefresher.isAntialiasAllowed()) {
                gc.setAntialias(SWT.ON);
            } else {
                gc.setAntialias(SWT.OFF);
            }
        }

        for (int i = 0; i < lstSize; i++) {
            LinkDescriptor<TreeItem, D1, Table, D2> link = links.get(i);

            Table table = link.getExtremity2().getGraphicalObject();
            Point tableToCommonPoint = display.map(table, getBgDrawableComposite(), new Point(0, 0));

            IDrawableLink drawableLink = link.getStyleLink().getDrawableLink();
            if (drawableLink == null) {
                drawableLink = getDefaultStyleLink().getDrawableLink();
            }
            drawableLink.getStyle().apply(gc);

            IExtremityLink<TreeItem, D1> extremity1 = link.getExtremity1();
            IExtremityLink<Table, D2> extremity2 = link.getExtremity2();

            TreeItem treeItem = getTreeItem(tree, extremity1.getDataItem(), extremity2.getDataItem());

            TreeItem firstExpandedAscTreeItem = TreeUtils.findFirstVisibleItemAscFrom(treeItem);

            Rectangle treeItemBounds = firstExpandedAscTreeItem.getBounds();

            int yStraight = treeToCommonPoint.y + treeItemHeight / 2 + treeItemBounds.y;
            Point pointStartStraight = new Point(treeToCommonPoint.x + treeItemBounds.x + treeItemBounds.width, yStraight);
            Point pointEndStraight = new Point(treeToCommonPoint.x + xStartBezierLink, yStraight);

            TableItem tableItem = TableUtils.getTableItem(table, extremity2.getDataItem());

            if (tableItem == null) {
                continue;
            }

            Rectangle tableItemBounds = tableItem.getBounds(1); // FIX for issue 1225 ("1" parameter added)
            Rectangle tableBounds = table.getBounds();

            Point pointEndCentralCurve = backgroundRefresher.convertPointToCommonParentOrigin(new Point(tableItemBounds.x - 2,
                    tableItemBounds.y + table.getItemHeight() / 2 + table.getBorderWidth()), table);

            boolean lineStyleDot = false;

            Point point = display.map(tree, getBgDrawableComposite(), new Point(0, 0));

            if (yStraight < point.y || yStraight > point.y + treeBounds.height) {
                lineStyleDot = true;
            }

            if (pointEndCentralCurve.y < tableToCommonPoint.y) {
                pointEndCentralCurve.y = tableToCommonPoint.y;
                lineStyleDot = true;
            }

            if (pointEndCentralCurve.y > tableToCommonPoint.y + tableBounds.height) {
                pointEndCentralCurve.y = tableToCommonPoint.y + tableBounds.height - 2 * table.getBorderWidth();
                lineStyleDot = true;
            }

            if (firstExpandedAscTreeItem == treeItem && !lineStyleDot) {
                gc.setLineStyle(SWT.LINE_SOLID);
            } else {
                gc.setLineStyle(SWT.LINE_DOT);
            }

            Point offset = getOffset();

            gc.drawLine(pointStartStraight.x + offset.x, pointStartStraight.y + offset.y, pointEndStraight.x + offset.x,
                    pointEndStraight.y + offset.y);

            pointEndStraight.x += offset.x;
            pointEndStraight.y += offset.y;

            pointEndCentralCurve.x += offset.x;
            pointEndCentralCurve.y += offset.y;

            drawableLink.setPoint1(pointEndStraight);
            drawableLink.setPoint2(pointEndCentralCurve);
            drawableLink.draw(gc);

        }

    }

    protected TreeItem getTreeItem(Tree tree, Object dataOfTreeItem, Object dataOfTableItem) {
        return TreeUtils.getTreeItem(tree, dataOfTreeItem);
    }

    /**
     * DOC amaumont Comment method "findMaxWidth".
     * 
     * @param items
     * @param maxWidth
     * @return
     */
    private int findXRightStartBezierLink(TreeItem[] items, int maxWidth) {
        for (TreeItem item2 : items) {
            TreeItem item = item2;
            if (!item.isDisposed()) {
                Rectangle bounds = item.getBounds();
                maxWidth = Math.max(maxWidth, bounds.x + bounds.width);
                if (item.getExpanded()) {
                    maxWidth = findXRightStartBezierLink(item.getItems(), maxWidth);
                }
            }
        }
        return maxWidth;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.commons.ui.swt.linking.IControlsLinker#updateLinksStyleAndControlsSelection(org.eclipse.swt.widgets
     * .Control, java.lang.Boolean)
     */
    public void updateLinksStyleAndControlsSelection(Control currentControl, Boolean flag) {
        boolean isTable = false;
        if (currentControl instanceof Table) {
            isTable = true;
        } else if (currentControl instanceof Tree) {
            isTable = false;
        } else {
            throw new IllegalArgumentException(Messages.getString("TreeToTablesLinker.Type.Unsupported")); //$NON-NLS-1$
        }

        HashSet selectedItems = new HashSet();

        if (isTable) {
            for (Table table : tables) {
                if (table != currentControl) {
                    table.deselectAll();
                }
            }

            TableItem[] selection = ((Table) currentControl).getSelection();
            for (TableItem tableItem : selection) {
                selectedItems.add(tableItem.getData());
            }

        } else {
            tree.deselectAll();

            TreeItem[] selection = tree.getSelection();
            for (TreeItem treeItem : selection) {
                selectedItems.add(treeItem.getData());
            }

        }

        List<LinkDescriptor<TreeItem, D1, Table, D2>> links = linksManager.getLinks();
        for (LinkDescriptor<TreeItem, D1, Table, D2> link : links) {
            IStyleLink styleLink = null;
            IExtremityLink extremity = null;
            if (isTable) {
                extremity = link.getExtremity2();
            } else {
                extremity = link.getExtremity1();
            }
            boolean currentItemIsSelected = selectedItems.contains(extremity.getDataItem());
            if (currentItemIsSelected) {
                styleLink = selectedStyleLink;
            } else {
                styleLink = unselectedStyleLink;
            }
            if (styleLink == null) {
                styleLink = getDefaultStyleLink();
            }
            link.setStyleLink(styleLink);

        }
        linksManager.sortLinks(getDrawingLinksComparator());
        backgroundRefresher.refreshBackground();
    }

    /**
     * Getter for selectedStyleLink.
     * 
     * @return the selectedStyleLink
     */
    public IStyleLink getSelectedStyleLink() {
        return this.selectedStyleLink;
    }

    /**
     * Sets the selectedStyleLink.
     * 
     * @param selectedStyleLink the selectedStyleLink to set
     */
    public void setSelectedStyleLink(IStyleLink selectedStyleLink) {
        this.selectedStyleLink = selectedStyleLink;
    }

    /**
     * Getter for unselectedStyleLink.
     * 
     * @return the unselectedStyleLink
     */
    public IStyleLink getUnselectedStyleLink() {
        return this.unselectedStyleLink;
    }

    /**
     * Sets the unselectedStyleLink.
     * 
     * @param unselectedStyleLink the unselectedStyleLink to set
     */
    public void setUnselectedStyleLink(IStyleLink unselectedStyleLink) {
        this.unselectedStyleLink = unselectedStyleLink;
    }

    /**
     * Getter for backgroundRefresher.
     * 
     * @return the backgroundRefresher
     */
    public IBackgroundRefresher getBackgroundRefresher() {
        return this.backgroundRefresher;
    }

    /**
     * Sets the backgroundRefresher.
     * 
     * @param backgroundRefresher the backgroundRefresher to set
     */
    public void setBackgroundRefresher(IBackgroundRefresher backgroundRefresher) {
        this.backgroundRefresher = backgroundRefresher;
    }

    /**
     * Getter for tables.
     * 
     * @return the tables
     */
    public List<Table> getTables() {
        return this.tables;
    }

    /**
     * Getter for tree.
     * 
     * @return the tree
     */
    public Tree getTree() {
        return this.tree;
    }

    protected LinkableTree getLinkableTree() {
        return this.linkableTree;
    }

    /**
     * Getter for linksManager.
     * 
     * @return the linksManager
     */
    protected LinksManager<TreeItem, D1, Table, D2> getLinksManager() {
        return this.linksManager;
    }

    public void dispose() {
        if (linkableTree != null) {
            linkableTree.dispose();
        }
        if (linkableTableList != null) {
            Iterator<LinkableTable> iter = linkableTableList.iterator();
            while (iter.hasNext()) {
                LinkableTable linkableTable = iter.next();
                linkableTable.dispose();
            }
            linkableTableList.clear();
        }
    }

}
