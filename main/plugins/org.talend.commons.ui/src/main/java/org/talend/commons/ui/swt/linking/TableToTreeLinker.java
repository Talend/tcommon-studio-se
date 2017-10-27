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

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.ui.swt.drawing.background.IBackgroundRefresher;
import org.talend.commons.ui.swt.drawing.background.IBgDrawableComposite;
import org.talend.commons.ui.swt.drawing.link.BezierHorizontalLink;
import org.talend.commons.ui.swt.drawing.link.ExtremityEastArrow;
import org.talend.commons.ui.swt.drawing.link.IDrawableLink;
import org.talend.commons.ui.swt.drawing.link.IExtremityLink;
import org.talend.commons.ui.swt.drawing.link.IStyleLink;
import org.talend.commons.ui.swt.drawing.link.LinkDescriptor;
import org.talend.commons.ui.swt.drawing.link.LinksManager;
import org.talend.commons.ui.swt.drawing.link.StyleLink;
import org.talend.commons.ui.utils.TreeUtils;

/**
 * bqian class global comment. Detailled comment <br/>
 * 
 * $Id: TableToTreeLinker.java,v 1.1 2007/06/12 07:20:38 gke Exp $
 * 
 * @param <D1> the data item of extremety 1
 * @param <D2> the data item of extremety 2
 */
public class TableToTreeLinker<D1, D2> extends BgDrawableComposite implements IBgDrawableComposite, IControlsLinker {

    protected LinksManager<Item, D1, Tree, D2> linksManager = new LinksManager<Item, D1, Tree, D2>();

    private IStyleLink defaultStyleLink;

    private IStyleLink selectedStyleLink;

    private IStyleLink unselectedStyleLink;

    private Comparator<LinkDescriptor<Item, D1, Tree, D2>> drawingLinksComparator;

    private IBackgroundRefresher backgroundRefresher;

    private Display display;

    private Table source;

    private Tree target;

    private DataToTableItemCache dataToTableItemCache;

    private Integer xStartBezierLink;

    /**
     * DOC amaumont TreeToTableLinker constructor comment.
     * 
     * @param source
     * @param table
     */
    public TableToTreeLinker(Composite commonParent) {
        super(commonParent);
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param sourceTable
     * @param targetTree
     * @param backgroundRefresher
     */
    public void init(Table sourceTable, Tree targetTree, IBackgroundRefresher backgroundRefresher) {
        this.display = sourceTable.getDisplay();
        this.backgroundRefresher = backgroundRefresher;

        LinkableTree linkableTree = new LinkableTree(this, backgroundRefresher, targetTree, this, false);
        targetTree.removeSelectionListener(linkableTree.getSelectionListener());

        new LinkableTable(this, backgroundRefresher, sourceTable, this, true);
        this.target = targetTree;
        this.source = sourceTable;

        dataToTableItemCache = new DataToTableItemCache(sourceTable);

    }

    protected IStyleLink getDefaultStyleLink() {
        if (defaultStyleLink == null) {
            this.defaultStyleLink = createStandardLink(display.getSystemColor(SWT.COLOR_BLACK));
        }
        return this.defaultStyleLink;
    }

    protected StyleLink createStandardLink(Color color) {
        StyleLink styleLink = new StyleLink();
        BezierHorizontalLink link = new BezierHorizontalLink(styleLink);
        // LineLinkWithHorizontalConnectors link = new LineLinkWithHorizontalConnectors(styleLink);
        // link.setConnectorWidth(40);
        styleLink.setDrawableLink(link);
        styleLink.setForegroundColor(color);
        styleLink.setLineWidth(2);
        styleLink.setExtremity2(new ExtremityEastArrow(styleLink, 0, 0));
        return styleLink;
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
    protected Comparator<LinkDescriptor<Item, D1, Tree, D2>> getDrawingLinksComparator() {
        if (this.drawingLinksComparator == null) {
            this.drawingLinksComparator = new Comparator<LinkDescriptor<Item, D1, Tree, D2>>() {

                public int compare(LinkDescriptor<Item, D1, Tree, D2> link1, LinkDescriptor<Item, D1, Tree, D2> link2) {
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

        if (gc == null) {
            return;
        }

        // TimeMeasure.measureActive = true;
        // TimeMeasure.display = true;
        //
        // TimeMeasure.begin("drawBackground");

        List<LinkDescriptor<Item, D1, Tree, D2>> links = linksManager.getLinks();
        int lstSize = links.size();

        if (xStartBezierLink == null || xStartBezierLink < 10) {
            xStartBezierLink = findXRightStartBezierLink(source.getItems(), 0);
        }

        Point sourceToCommonPoint = display.map(source, getBgDrawableComposite(), new Point(0, 0));

        int treeItemHeight = source.getItemHeight();

        Rectangle tableBounds = source.getBounds();

        if (WindowSystem.isGTK()) {
            gc.setAdvanced(false);
        } else {
            gc.fillRectangle(sourceToCommonPoint.x, sourceToCommonPoint.y, tableBounds.width - source.getBorderWidth(),
                    tableBounds.height - source.getBorderWidth());
            if (backgroundRefresher.isAntialiasAllowed()) {
                gc.setAntialias(SWT.ON);
            } else {
                gc.setAntialias(SWT.OFF);
            }
        }

        // int countStraight = 0;
        // int drawnLinks = 0;

        // System.out.println("Drawing:" + gc.handle);

        // TimeMeasure.step("drawBackground", "before loop");

        Point pointStartStraight = new Point(0, 0);
        Point pointEndStraight = new Point(0, 0);

        for (int i = 0; i < lstSize; i++) {
            // TimeMeasure.begin("loop");
            LinkDescriptor<Item, D1, Tree, D2> link = links.get(i);

            Tree tree = link.getExtremity2().getGraphicalObject();
            Point tableToCommonPoint = display.map(tree, getBgDrawableComposite(), new Point(0, 0));

            IDrawableLink drawableLink = link.getStyleLink().getDrawableLink();
            if (drawableLink == null) {
                drawableLink = getDefaultStyleLink().getDrawableLink();
            }
            drawableLink.getStyle().apply(gc);

            IExtremityLink<Item, D1> extremity1 = link.getExtremity1();
            IExtremityLink<Tree, D2> extremity2 = link.getExtremity2();
            // see bug 7360
            dataToTableItemCache.clear();
            TableItem tableItem = dataToTableItemCache.getTableItem(extremity1.getDataItem());

            Rectangle tableItemBounds = tableItem.getBounds();

            int yStraight = sourceToCommonPoint.y + treeItemHeight / 2 + tableItemBounds.y;
            pointEndStraight.x = sourceToCommonPoint.x + xStartBezierLink;
            if (Platform.OS_MACOSX.equals(Platform.getOS())) {
                pointStartStraight.x = sourceToCommonPoint.x + tableItem.getParent().getBounds().width;
                pointEndStraight.x = pointStartStraight.x;
            } else {
                pointStartStraight.x = sourceToCommonPoint.x + tableItemBounds.x + tableItemBounds.width;
            }
            pointStartStraight.y = yStraight;
            pointEndStraight.y = yStraight;

            TreeItem treeItem = getFirstVisibleTreeItemOfPath(extremity2.getDataItem());
            Rectangle treeItemBounds;
            if (treeItem != null) {
                treeItemBounds = treeItem.getBounds();
            } else {
                treeItemBounds = new Rectangle(0, 0, 0, 0);
            }
            Rectangle treeBounds = tree.getBounds();

            int pointY = treeItemBounds.y + tree.getItemHeight() / 2 + tree.getBorderWidth();
            if (tree.getHeaderVisible()) {
                pointY += tree.getHeaderHeight();
            }

            Point pointEndCentralCurve = null;

            pointEndCentralCurve = backgroundRefresher.convertPointToCommonParentOrigin(new Point(treeBounds.x - 10, pointY),
                    tree);

            Point point = display.map(source, getBgDrawableComposite(), new Point(0, 0));

            Point offset = getOffset();

            int yStartStraight = pointStartStraight.y + offset.y;

            boolean isStartOutOfView = false;
            boolean isEndOutOfView = false;

            if (yStraight < point.y || yStraight > point.y + tableBounds.height) {
                isStartOutOfView = true;
            } else {
                // countStraight++;
            }

            if (pointEndCentralCurve.y < tableToCommonPoint.y) {
                pointEndCentralCurve.y = tableToCommonPoint.y;
                isEndOutOfView = true;
            }

            if (pointEndCentralCurve.y > tableToCommonPoint.y + treeBounds.height) {
                pointEndCentralCurve.y = tableToCommonPoint.y + treeBounds.height - 2 * tree.getBorderWidth();
                isEndOutOfView = true;
            }

            // TimeMeasure.step("loop", "middle");
            if (!(isStartOutOfView && isEndOutOfView)) {

                boolean lineStyleDot = isStartOutOfView || isEndOutOfView;

                if ((treeItem != null && treeItem.getData() == extremity2.getDataItem()) && !lineStyleDot) {
                    gc.setLineStyle(SWT.LINE_SOLID);
                } else {
                    gc.setLineStyle(SWT.LINE_DOT);
                }

                gc.drawLine(pointStartStraight.x + offset.x, yStartStraight, pointEndStraight.x + offset.x, yStartStraight);

                if (WindowSystem.isGTK()) {
                    pointStartStraight.x += -15;
                }

                pointEndStraight.x += offset.x;
                pointEndStraight.y += offset.y;

                pointEndCentralCurve.x += offset.x - 6;
                pointEndCentralCurve.y += offset.y;

                // Added by Marvin Wang on Nov. 28, 2012 for bug TDI-23378. This is not the best way to fix this issue,
                // but till now I have not found the root cause.
                if (Platform.OS_LINUX.equals(Platform.getOS())) {
                    pointEndCentralCurve.y = pointEndCentralCurve.y - tableItem.getBounds().height;
                }
                if (Platform.OS_MACOSX.equals(Platform.getOS())) {
                    pointEndCentralCurve.y = pointEndCentralCurve.y + tableItem.getBounds(0).height;
                }
                drawableLink.setPoint1(pointEndStraight);
                drawableLink.setPoint2(pointEndCentralCurve);

                drawableLink.draw(gc);
                // drawnLinks++;
            }
            // TimeMeasure.end("loop");
        }

        // TimeMeasure.end("drawBackground");

        // System.out.println("countStraight=" + countStraight);
        // System.out.println("drawnLinks=" + drawnLinks);

    }

    /**
     * DOC nrousseau Comment method "getFirstVisibleTreeItemOfPath".
     * 
     * @param dataItem
     * @return
     */
    protected TreeItem getFirstVisibleTreeItemOfPath(D2 dataItem) {
        return TreeUtils.getTreeItem(this.target, dataItem);
    }

    /**
     * amaumont Comment method "findMaxWidth".
     * 
     * @param items
     * @param maxWidth
     * @return
     */
    private int findXRightStartBezierLink(TableItem[] items, int maxWidth) {
        for (TableItem item2 : items) {
            TableItem item = item2;
            if (!item.isDisposed()) {
                Rectangle bounds = item.getBounds();
                maxWidth = Math.max(maxWidth, bounds.x + bounds.width);
            }
        }
        return maxWidth;
    }

    public void updateLinksStyleAndControlsSelection(Control currentControl, Boolean flag) {

        boolean isSource = false;
        if (currentControl == this.getSource()) {
            isSource = true;
        } else if (currentControl == this.getTarget()) {
            isSource = false;
        } else {
            throw new IllegalArgumentException("This type of currentControl is unsupported"); //$NON-NLS-1$
        }

        HashSet selectedItems = new HashSet();

        if (isSource) {
            getTarget().deselectAll();

            TreeItem[] selection = getTarget().getSelection();
            for (TreeItem tableItem : selection) {
                selectedItems.add(tableItem.getData());
            }

        } else {
            getSource().deselectAll();

            TableItem[] selection = getSource().getSelection();
            for (TableItem treeItem : selection) {
                selectedItems.add(treeItem.getData());
            }

        }

        List<LinkDescriptor<Item, D1, Tree, D2>> links = linksManager.getLinks();
        for (LinkDescriptor<Item, D1, Tree, D2> link : links) {
            IStyleLink styleLink = null;
            IExtremityLink extremity = null;
            if (isSource) {
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
    public Tree getTarget() {
        return this.target;
    }

    /**
     * Getter for tree.
     * 
     * @return the tree
     */
    public Table getSource() {
        return this.source;
    }

    /**
     * Getter for linksManager.
     * 
     * @return the linksManager
     */
    protected LinksManager<Item, D1, Tree, D2> getLinksManager() {
        return this.linksManager;
    }
}