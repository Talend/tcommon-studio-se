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
package org.talend.repository.items.importexport.ui.wizard.imports.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.talend.repository.items.importexport.wizard.models.ImportNode;

public class ImportItemsViewerContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof Object[]) {
            return (Object[]) inputElement;
        }
        if (inputElement instanceof Collection) {
            return ((Collection) inputElement).toArray();
        }
        return new Object[0];
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof ImportNode) {
            List<ImportNode> newChildren = new ArrayList<ImportNode>(((ImportNode) parentElement).getChildren());

            Iterator<ImportNode> iterator = newChildren.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().isVisibleWithChildren()) {
                    iterator.remove();
                }
            }

            return newChildren.toArray();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof ImportNode) {
            return ((ImportNode) element).getParentNode();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        return getChildren(element).length > 0;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }

    @Override
    public void dispose() {
    }
}
