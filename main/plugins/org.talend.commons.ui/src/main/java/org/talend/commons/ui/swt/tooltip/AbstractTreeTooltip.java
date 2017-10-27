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
package org.talend.commons.ui.swt.tooltip;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Abstract tooltip for TreeItem
 */
public abstract class AbstractTreeTooltip {

    protected Tree table;

    private Listener labelListener;

    private Listener tableListener;

    public AbstractTreeTooltip(Tree table) {
        this.table = table;
        init();
    }

    /**
     * bqian Comment method "init".
     */
    private void init() {
        // Disable native tooltip
        table.setToolTipText(""); //$NON-NLS-1$
        iniLabelListener();
        initTableListener();
        table.addListener(SWT.Dispose, tableListener);
        table.addListener(SWT.KeyDown, tableListener);
        table.addListener(SWT.MouseMove, tableListener);
        table.addListener(SWT.MouseHover, tableListener);

    }

    /**
     * DOC bqian Comment method "initTableListener".
     */
    private void initTableListener() {
        tableListener = new Listener() {

            Shell tip = null;

            Label label = null;

            public void handleEvent(Event event) {
                switch (event.type) {
                case SWT.Dispose:
                case SWT.KeyDown:
                case SWT.MouseMove: {
                    if (tip == null)
                        break;
                    tip.dispose();
                    tip = null;
                    label = null;
                    break;
                }
                case SWT.MouseHover: {
                    TreeItem item = table.getItem(new Point(event.x, event.y));
                    if (item != null) {
                        if (tip != null && !tip.isDisposed()) {
                            tip.dispose();
                        }
                        String content = getTooltipContent(item);
                        // add a line
                        content = cutStr(content, 30);
                        if (content == null) {
                            return;
                        }
                        tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.TOOL);
                        tip.setLayout(new FillLayout());
                        label = new Label(tip, SWT.NONE);
                        label.setForeground(table.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
                        label.setBackground(table.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
                        label.setData("_TABLEITEM", item); //$NON-NLS-1$
                        label.setText(content);
                        label.addListener(SWT.MouseExit, labelListener);
                        label.addListener(SWT.MouseDown, labelListener);
                        checkShellBounds(tip, event);
                        tip.setVisible(true);
                    }
                }
                }
            }

        };

    }

    /*
     * author by hfchang
     * 
     * make a string line wrap autom
     */
    private String cutStr(String str, int len) {
        String s = "";
        if (str == null || "".equals(str)) {
            return null;
        }
        if (str.length() < len) {
            s += str;
        }
        for (int i = 0; i < str.length(); i++) {
            int r = i % len;
            if (i != 0 && i <= len) {
                if (r == 0) {
                    s += str.substring(i - len, len) + "\n";
                }
            } else if (i > len) {
                String laststr = str.substring(i - 1);
                if (laststr.length() > len) {
                    s += cutStr(laststr, len);
                } else {
                    s += laststr;
                }
                break;
            }
        }
        return s;
    }

    protected void checkShellBounds(Shell tip, Event event) {
        TreeItem item = table.getItem(new Point(event.x, event.y));
        if (item != null) {
            Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            Rectangle rect = item.getBounds(0);
            Point pt = table.toDisplay(rect.x, rect.y);
            tip.setBounds(pt.x + 32, pt.y - 16, size.x, size.y);
        }
    }

    public abstract String getTooltipContent(TreeItem item);

    private void iniLabelListener() {
        // Implement a "fake" tooltip
        labelListener = new Listener() {

            public void handleEvent(Event event) {
                Label label = (Label) event.widget;
                Shell shell = label.getShell();
                switch (event.type) {
                case SWT.MouseDown:
                    // Event e = new Event();
                    // e.item = (TableItem) label.getData("_TABLEITEM");
                    // Assuming table is single select, set the selection as if
                    // the mouse down event went through to the table
                    // table.setSelection(new TableItem[] { (TableItem) e.item });
                    // table.notifyListeners(SWT.Selection, e);
                    // fall through
                case SWT.MouseExit:
                    shell.dispose();
                    break;
                }
            }
        };
    }

}
