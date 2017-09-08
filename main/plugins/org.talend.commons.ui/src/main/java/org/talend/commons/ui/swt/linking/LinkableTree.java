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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.talend.commons.ui.runtime.ws.WindowSystem;
import org.talend.commons.ui.swt.drawing.background.IBackgroundRefresher;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class LinkableTree implements ILinkableControl {

    private Tree tree;

    private IBackgroundRefresher backgroundRefresher;

    private IControlsLinker controlsLinker;

    private BgDrawableComposite bgDrawableComposite;

    private SelectionListener selectionListener;

    private boolean forceDrawLinksGtk;

    private ControlListener controlListener;

    private SelectionListener scrollListener;

    private TreeListener treeListener;

    private Listener paintListener;

    /**
     * DOC amaumont LinkableTable constructor comment.
     * 
     * @param tree
     * @param bgDrawableComposite
     */
    public LinkableTree(IControlsLinker controlsLinker, IBackgroundRefresher backgroundRefresher, Tree tree,
            BgDrawableComposite bgDrawableComposite) {
        this(controlsLinker, backgroundRefresher, tree, bgDrawableComposite, true);
    }

    /**
     * 
     * DOC amaumont LinkableTree constructor comment.
     * 
     * @param controlsLinker
     * @param backgroundRefresher
     * @param tree
     * @param bgDrawableComposite
     * @param forceDrawLinksGtk useful only for GTK systems, it forces redraw links at each paint event on tree, default
     * is true
     */
    public LinkableTree(IControlsLinker controlsLinker, IBackgroundRefresher backgroundRefresher, Tree tree,
            BgDrawableComposite bgDrawableComposite, boolean forceDrawLinksGtk) {
        super();
        this.tree = tree;
        this.controlsLinker = controlsLinker;
        this.bgDrawableComposite = bgDrawableComposite;

        // setBackgroundMode to correct graphic bug when background is update with ExecutionLimiter
        this.tree.setBackgroundMode(SWT.INHERIT_NONE);

        this.backgroundRefresher = backgroundRefresher;
        this.forceDrawLinksGtk = forceDrawLinksGtk;
        init();
    }

    /**
     * DOC amaumont Comment method "init".
     * 
     * @param forceDrawLinksGtk2
     */
    private void init() {
        addListeners();
    }

    /**
     * Getter for table.
     * 
     * @return the table
     */
    public Tree getTree() {
        return this.tree;
    }

    /**
     * DOC amaumont Comment method "addListeners".
     * 
     * @param forceDrawLinksGtk2
     */
    private void addListeners() {
        if (WindowSystem.isGTK() && forceDrawLinksGtk) {
            paintListener = new Listener() {

                public void handleEvent(Event event) {
                    // paintEvent(event);
                }

            };
            tree.addListener(SWT.Paint, paintListener);
        }

        controlListener = new ControlListener() {

            public void controlMoved(ControlEvent e) {
                // updateBackgroundWithLimiter();
            }

            public void controlResized(ControlEvent e) {
                backgroundRefresher.refreshBackgroundWithLimiter();
            }

        };

        tree.addControlListener(controlListener);

        treeListener = new TreeListener() {

            public void treeCollapsed(TreeEvent e) {
                backgroundRefresher.refreshBackgroundWithLimiter();
            }

            public void treeExpanded(TreeEvent e) {
                backgroundRefresher.refreshBackgroundWithLimiter();
            }

        };

        tree.addTreeListener(treeListener);
        selectionListener = new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {

                controlsLinker.updateLinksStyleAndControlsSelection(tree, true);
            }

        };
        tree.addSelectionListener(selectionListener);

        ScrollBar vBarTree = tree.getVerticalBar();
        ScrollBar hBarTree = tree.getHorizontalBar();

        scrollListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                // updateBackgroundWithLimiter();
                backgroundRefresher.refreshBackgroundWithLimiter();
            }
        };
        vBarTree.addSelectionListener(scrollListener);
        hBarTree.addSelectionListener(scrollListener);

    }

    private void removeListeners() {
        if (paintListener != null) {
            tree.removeListener(SWT.Paint, paintListener);
        }
        tree.removeControlListener(controlListener);
        tree.removeTreeListener(treeListener);
        tree.removeSelectionListener(selectionListener);
        ScrollBar vBarTree = tree.getVerticalBar();
        vBarTree.removeSelectionListener(scrollListener);
        ScrollBar hBarTree = tree.getHorizontalBar();
        hBarTree.removeSelectionListener(scrollListener);
    }

    /**
     * Getter for selectionListener.
     * 
     * @return the selectionListener
     */
    public SelectionListener getSelectionListener() {
        return this.selectionListener;
    }

    protected void paintEvent(Event event) {
        Point offsetPoint = event.display.map(bgDrawableComposite.getBgDrawableComposite(), tree, new Point(0, 0));
        bgDrawableComposite.setOffset(offsetPoint);
        bgDrawableComposite.drawBackground(event.gc);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.ui.swt.linking.ILinkableControl#dispose()
     */
    public void dispose() {
        removeListeners();
    }

}
