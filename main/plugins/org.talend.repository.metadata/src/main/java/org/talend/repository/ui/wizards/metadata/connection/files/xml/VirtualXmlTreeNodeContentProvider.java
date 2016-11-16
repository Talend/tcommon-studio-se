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

import org.eclipse.jface.viewers.ILazyTreeContentProvider;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.talend.datatools.xml.utils.ATreeNode;

public class VirtualXmlTreeNodeContentProvider extends TreeNodeContentProvider implements ILazyTreeContentProvider {

    public VirtualXmlTreeNodeContentProvider(TreeViewer viewer) {
        super();
        this.viewer = viewer;
    }

    private Object[] elements;

    private TreeViewer viewer;

    @Override
    public void updateElement(Object parent, int index) {
        Object element;
        if (parent instanceof ATreeNode) {
            element = ((ATreeNode) parent).getChildren()[index];
        } else {
            element = elements[index];
        }
        viewer.replace(parent, index, element);
        updateChildCount(element, -1);
    }

    @Override
    public void updateChildCount(Object element, int currentChildCount) {
        int length = 0;
        if (element instanceof ATreeNode) {
            ATreeNode node = (ATreeNode) element;
            length = node.getChildren().length;
        } else if (element == elements) {
            length = elements.length;
        }
        if (length != currentChildCount) {
            // no need to update the viewer if it's to set the same value !
            viewer.setChildCount(element, length);
        }
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof ATreeNode) {
            return ((ATreeNode) element).getParent();
        }
        return elements;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        this.elements = (Object[]) newInput;
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
