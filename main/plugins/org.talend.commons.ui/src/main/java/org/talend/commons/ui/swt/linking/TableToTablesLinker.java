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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
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

/**
 * bqian class global comment. Detailled comment <br/>
 * 
 * $Id: TableToTablesLinker.java,v 1.1 2007/06/12 07:20:39 gke Exp $
 * 
 * @param <D1> the data item of extremety 1
 * @param <D2> the data item of extremety 2
 */
public class TableToTablesLinker<D1, D2> extends BgDrawableComposite implements IBgDrawableComposite, IControlsLinker {

    protected LinksManager<TableItem, D1, Table, D2> linksManager = new LinksManager<TableItem, D1, Table, D2>();

    private IStyleLink defaultStyleLink;

    private IStyleLink selectedStyleLink;

    private IStyleLink unselectedStyleLink;

    private Comparator<LinkDescriptor<TableItem, D1, Table, D2>> drawingLinksComparator;

    public IBackgroundRefresher backgroundRefresher;

    public Display display;

    public Table source;

    public List<Table> targets;

    /**
     * DOC amaumont TreeToTableLinker constructor comment.
     * 
     * @param source
     * @param table
     */
    public TableToTablesLinker(Composite commonParent) {
        super(commonParent);
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param sourceTable
     * @param targetTables
     * @param backgroundRefresher
     */
    public void init(Table sourceTable, Table[] targetTables, IBackgroundRefresher backgroundRefresher) {
        this.display = sourceTable.getDisplay();
        this.backgroundRefresher = backgroundRefresher;
        for (Table table : targetTables) {
            new LinkableTable(this, backgroundRefresher, table, (BgDrawableComposite) this, false);
        }
        new LinkableTable(this, backgroundRefresher, sourceTable, (BgDrawableComposite) this, true);
        this.targets = Arrays.asList(targetTables);
        this.source = sourceTable;
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
    protected Comparator<LinkDescriptor<TableItem, D1, Table, D2>> getDrawingLinksComparator() {
        if (this.drawingLinksComparator == null) {
            this.drawingLinksComparator = new Comparator<LinkDescriptor<TableItem, D1, Table, D2>>() {

                public int compare(LinkDescriptor<TableItem, D1, Table, D2> link1, LinkDescriptor<TableItem, D1, Table, D2> link2) {
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

        List<LinkDescriptor<TableItem, D1, Table, D2>> links = linksManager.getLinks();
        int lstSize = links.size();

        int xStartBezierLink = findXRightStartBezierLink(source.getItems(), 0);

        Point treeToCommonPoint = display.map(source, getBgDrawableComposite(), new Point(0, 0));
        // System.out.println("treeToCommonPoint=" + treeToCommonPoint);

        int treeItemHeight = source.getItemHeight();

        Rectangle treeBounds = source.getBounds();

        if (WindowSystem.isGTK()) {
            gc.setAdvanced(false);
        } else {
            gc.fillRectangle(treeToCommonPoint.x, treeToCommonPoint.y, treeBounds.width - source.getBorderWidth(),
                    treeBounds.height - source.getBorderWidth());
            // System.out.println(isAntialiasAllowed());
            if (backgroundRefresher.isAntialiasAllowed()) {
                gc.setAntialias(SWT.ON);
            } else {
                gc.setAntialias(SWT.OFF);
            }
        }

        for (int i = 0; i < lstSize; i++) {
            LinkDescriptor<TableItem, D1, Table, D2> link = links.get(i);

            Table table = link.getExtremity2().getGraphicalObject();
            Point tableToCommonPoint = display.map(table, getBgDrawableComposite(), new Point(0, 0));

            IDrawableLink drawableLink = link.getStyleLink().getDrawableLink();
            if (drawableLink == null) {
                drawableLink = getDefaultStyleLink().getDrawableLink();
            }
            drawableLink.getStyle().apply(gc);

            IExtremityLink<TableItem, D1> extremity1 = link.getExtremity1();
            IExtremityLink<Table, D2> extremity2 = link.getExtremity2();

            TableItem treeItem = TableUtils.getTableItem(source, (Object) extremity1.getDataItem());
            TableItem firstExpandedAscTreeItem = treeItem;

            Rectangle treeItemBounds = firstExpandedAscTreeItem.getBounds();

            int yStraight = treeToCommonPoint.y + treeItemHeight / 2 + treeItemBounds.y;
            Point pointStartStraight = new Point(treeToCommonPoint.x + treeItemBounds.x + treeItemBounds.width, yStraight);
            Point pointEndStraight = new Point(treeToCommonPoint.x + xStartBezierLink, yStraight);

            Rectangle tableItemBounds = TableUtils.getTableItem(table, (Object) extremity2.getDataItem()).getBounds();
            Rectangle tableBounds = table.getBounds();

            Point pointEndCentralCurve = backgroundRefresher.convertPointToCommonParentOrigin(new Point(tableItemBounds.x - 2,
                    tableItemBounds.y + table.getItemHeight() / 2 + table.getBorderWidth()), table);

            boolean lineStyleDot = false;

            Point point = display.map(source, getBgDrawableComposite(), new Point(0, 0));

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

            if (WindowSystem.isGTK()) {
                pointStartStraight.x += -15;
            }

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

    /**
     * DOC amaumont Comment method "findMaxWidth".
     * 
     * @param items
     * @param maxWidth
     * @return
     */
    public int findXRightStartBezierLink(TableItem[] items, int maxWidth) {
        for (int i = 0; i < items.length; i++) {
            TableItem item = items[i];
            if (!item.isDisposed()) {
                Rectangle bounds = item.getBounds();
                maxWidth = Math.max(maxWidth, bounds.x + bounds.width);
            }
        }
        return maxWidth;
    }

    public void updateLinksStyleAndControlsSelection(Control currentControl, Boolean flag) {

        boolean isTarget = false;
        if (this.getTargets().contains(currentControl)) {
            isTarget = true;
        } else if (currentControl == this.getSource()) {
            isTarget = false;
        } else {
            throw new IllegalArgumentException("This type of currentControl is unsupported"); //$NON-NLS-1$
        }

        HashSet selectedItems = new HashSet();

        if (isTarget) {
            for (Table table : targets) {
                if (table != currentControl) {
                    table.deselectAll();
                }
            }

            TableItem[] selection = ((Table) currentControl).getSelection();
            for (int i = 0; i < selection.length; i++) {
                TableItem tableItem = selection[i];
                selectedItems.add(tableItem.getData());
            }

        } else {
            source.deselectAll();

            TableItem[] selection = source.getSelection();
            for (int i = 0; i < selection.length; i++) {
                TableItem treeItem = selection[i];
                selectedItems.add(treeItem.getData());
            }

        }

        List<LinkDescriptor<TableItem, D1, Table, D2>> links = linksManager.getLinks();
        for (LinkDescriptor<TableItem, D1, Table, D2> link : links) {
            IStyleLink styleLink = null;
            IExtremityLink extremity = null;
            if (isTarget) {
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
    public List<Table> getTargets() {
        return this.targets;
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
    public LinksManager<TableItem, D1, Table, D2> getLinksManager() {
        return this.linksManager;
    }

    public Set<String> getInputSource() {
        return null;
    }

    public Set<String> getOutputSource() {
        return null;
    }

    public void addLinks(TableItem itemSource, Object data1, Table tableTarget, Object data2, String mark) {

    }

    public void onXPathValueChanged(Table sourceTable, Table targetTable, String oldValue, String newValue, String rowname,
            int itemIndex) {

    }

    public void clearLinks() {

    }
}
