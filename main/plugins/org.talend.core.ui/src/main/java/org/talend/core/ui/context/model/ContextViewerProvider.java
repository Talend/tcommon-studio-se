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
package org.talend.core.ui.context.model;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * A label and content provider for the treeviewer which groups the Contexts by variable.
 * 
 */
public class ContextViewerProvider extends LabelProvider implements ITreeContentProvider, ITableLabelProvider,
        ITableColorProvider {

    ContextProviderProxy provider = null;

    public ContextViewerProvider() {
        super();
    }

    public Image getColumnImage(Object element, int columnIndex) {
        return provider.getColumnImage(element, columnIndex);
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public String getColumnText(Object element, int columnIndex) {
        return provider.getColumnText(element, columnIndex);
    }

    public Object[] getChildren(Object parentElement) {
        return provider.getChildren(parentElement);
    }

    public Object getParent(Object element) {
        return provider.getParent(element);
    }

    public boolean hasChildren(Object element) {
        return provider.hasChildren(element);
    }

    public Object[] getElements(Object inputElement) {
        return provider.getElements(inputElement);
    }

    public void setProvider(ContextProviderProxy provider) {
        this.provider = provider;
    }

    public Color getForeground(Object element, int columnIndex) {
        return this.provider.getForeground(element, columnIndex);
    }

    public Color getBackground(Object element, int columnIndex) {
        return this.provider.getBackground(element, columnIndex);
    }
}
