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
package org.talend.core.ui.context.model.template;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * ggu class global comment. Detailled comment
 */
public class ContextColumnSorterListener implements Listener {

    private TreeViewer sortViewer;

    private int direction = 1;

    /**
     * ggu ContextColumnSorterListener constructor comment.
     * 
     * @param viewer
     */
    public ContextColumnSorterListener(TreeViewer viewer) {
        super();
        this.sortViewer = viewer;
        if (sortViewer == null) {
            throw new NullPointerException();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event e) {
        if (sortViewer == null) {
            return;
        }
        final Tree table = sortViewer.getTree();
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
        sortViewer.setSorter(new ContextViewerSorter(sortViewer, column, direction));
    }

}
