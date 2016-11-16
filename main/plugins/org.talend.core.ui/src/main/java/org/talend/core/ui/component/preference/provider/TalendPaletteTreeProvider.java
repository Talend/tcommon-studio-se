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
package org.talend.core.ui.component.preference.provider;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TalendPaletteTreeProvider implements ITreeContentProvider {

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof List) {
            return ((List) parentElement).toArray();
        }
        if (parentElement instanceof IPaletteItem) {
            List children = ((IPaletteItem) parentElement).getChildren();
            if (!children.isEmpty()) {
                return children.toArray();
            }
        }
        return null;

    }

    @Override
    public Object getParent(Object element) {
        return ((IPaletteItem) element).getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        return getChildren(element) != null;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof List) {
            return ((List) inputElement).toArray();
        }

        Object[] elements = getChildren(inputElement);
        if (elements == null) {
            elements = new Object[0];
        }
        return elements;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

}
