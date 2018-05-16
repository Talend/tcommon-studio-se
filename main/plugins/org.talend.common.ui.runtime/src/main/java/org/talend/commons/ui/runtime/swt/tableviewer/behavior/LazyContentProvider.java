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
package org.talend.commons.ui.runtime.swt.tableviewer.behavior;

import java.util.Collection;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.talend.commons.ui.runtime.swt.tableviewer.TableViewerCreatorNotModifiable;

/**
 * created by talend2 on 2012-8-24 Detailled comment
 * 
 */
public class LazyContentProvider implements ILazyContentProvider {

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private Object[] elements;

    public LazyContentProvider(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    @Override
    public void dispose() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput != null) {
            elements = ((Collection) newInput).toArray();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ILazyContentProvider#updateElement(int)
     */
    @Override
    public void updateElement(int index) {
        TableViewer tableViewer = tableViewerCreator.getTableViewer();
        if (tableViewer.getInput() != null) {
            elements = ((Collection) tableViewer.getInput()).toArray();
        }
        if (elements != null && index < elements.length) {
            tableViewer.replace(elements[index], index);
        }
    }
}
