// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class LazyContentProvider implements ILazyContentProvider {

    private TableViewerCreatorNotModifiable tableViewerCreator;

    private Object[] elements;

    public LazyContentProvider(TableViewerCreatorNotModifiable tableViewerCreator) {
        this.tableViewerCreator = tableViewerCreator;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (newInput != null) {
            elements = ((Collection) newInput).toArray();
        }
    }

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
