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
package org.talend.metadata.managment.ui.model;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

public class TableViewerContentProvider implements IStructuredContentProvider, ITableLabelProvider {

    public Object[] getElements(Object inputElement) {
        // HashMap<ColumnValue, ColumnValue> input = (HashMap<ColumnValue, ColumnValue>) inputElement;
        ArrayList input = (ArrayList) inputElement;
        return input.size() > 0 ? input.toArray() : new Object[] {};
    }

    public void dispose() {
        // TODO Auto-generated method stub

    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub

    }

    /* ########## methods implemented from label provider interface ####### */

    public Image getColumnImage(Object element, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (columnIndex == 0) {
            return element.toString();
        }
        return null;

    }

    public void addListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub

    }

    public boolean isLabelProperty(Object element, String property) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub

    }

}
