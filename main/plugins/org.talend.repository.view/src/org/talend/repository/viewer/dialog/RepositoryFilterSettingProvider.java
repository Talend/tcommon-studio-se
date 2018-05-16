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
package org.talend.repository.viewer.dialog;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.navigator.CommonViewer;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepositoryFilterSettingProvider extends LabelProvider implements ITreeContentProvider {

    private CommonViewer viewer;

    public RepositoryFilterSettingProvider(CommonViewer viewer) {
        super();
        this.viewer = viewer;
    }

    protected ITreeContentProvider getContentProvider() {
        final IContentProvider contentProvider = viewer.getContentProvider();
        if (contentProvider instanceof ITreeContentProvider) {
            return (ITreeContentProvider) contentProvider;
        }
        return null;
    }

    protected ILabelProvider getLabelProvider() {
        final IBaseLabelProvider labelProvider = viewer.getLabelProvider();
        if (labelProvider instanceof ILabelProvider) {
            return (ILabelProvider) labelProvider;
        }
        return null;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }

    @Override
    public Object[] getElements(Object inputElement) {
        // final ITreeContentProvider contentProvider = getContentProvider();
        // if (contentProvider != null) {
        // return contentProvider.getElements(inputElement);
        // }
        // return null;
        return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        // final ITreeContentProvider contentProvider = getContentProvider();
        // if (contentProvider != null) {
        // return contentProvider.getChildren(parentElement);
        // }
        if (parentElement instanceof RepositoryNode) {
            return ((RepositoryNode) parentElement).getChildren().toArray();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        // final ITreeContentProvider contentProvider = getContentProvider();
        // if (contentProvider != null) {
        // return contentProvider.getParent(element);
        // }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        // final ITreeContentProvider contentProvider = getContentProvider();
        // if (contentProvider != null) {
        // return contentProvider.hasChildren(element);
        // }
        final Object[] children = getChildren(element);
        if (children != null && children.length > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Image getImage(Object element) {
        final ILabelProvider labelProvider = getLabelProvider();
        if (labelProvider != null) {
            return labelProvider.getImage(element);
        }
        return super.getImage(element);
    }

    @Override
    public String getText(Object element) {
        final ILabelProvider labelProvider = getLabelProvider();
        if (labelProvider != null) {
            return labelProvider.getText(element);
        }
        return super.getText(element);
    }

}
