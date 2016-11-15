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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.datatools.xml.utils.ATreeNode;

public class ATreeNodeContentProvider implements ITreeContentProvider {

    private Object[] elements;

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.elements = (Object[]) newInput;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        return this.elements;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) parentElement;
            return node.getChildren();
        } else if (parentElement == elements) {
            return ((ATreeNode) elements[0]).getChildren();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) element;
            return node.getParent();
        } else if (element == elements) {
            return null;
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) element;
            return node.getChildren().length > 0;
        } else if (element == elements) {
            return ((ATreeNode) elements[0]).getChildren().length > 0;
        }
        return false;
    }

}
