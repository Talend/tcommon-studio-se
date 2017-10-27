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
package org.talend.core.repository.model.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;

/**
 * DOC hywang class global comment. Detailled comment
 */
public class AbstractMetadataExtractorViewProvider extends LabelProvider implements ITreeContentProvider, ITableLabelProvider {

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object[] getElements(Object inputElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object[] getChildren(Object parentElement) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasChildren(Object element) {
        return false;
    }

}
