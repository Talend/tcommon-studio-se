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
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class JsonTreeNodeContentProvider implements ITreeContentProvider {

    @Override
    public void dispose() {
        // nothing need to do

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // nothing need to do
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof List) {
            return ((List) inputElement).toArray();
        } else if (inputElement instanceof JsonTreeNode) {
            return ((JsonTreeNode) inputElement).getChildren();
        }
        return null;
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof JsonTreeNode) {
            JsonTreeNode node = (JsonTreeNode) parentElement;
            return node.getChildren();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof JsonTreeNode) {
            JsonTreeNode node = (JsonTreeNode) element;
            return node.getParent();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof JsonTreeNode) {
            JsonTreeNode node = (JsonTreeNode) element;
            return node.hasChildren();
        }
        return false;
    }

}
