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
package org.talend.core.repository.ui.dialog;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.talend.core.repository.model.ItemReferenceBean;
import org.talend.core.ui.images.RepositoryImageProvider;

public class ItemReferenceViewProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        ItemReferenceBean bean = (ItemReferenceBean) element;
        if (columnIndex == 0) {
            if (bean.isHost()) {
                return RepositoryImageProvider.getImage(bean.getItemType());
            }
        } else if (columnIndex == 1) {
            if (!bean.isHost()) {
                return RepositoryImageProvider.getImage(bean.getReferenceItemType());
            }
        }
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        ItemReferenceBean bean = (ItemReferenceBean) element;
        if (columnIndex == 0) {
            if (bean.isHost()) {
                return bean.getWholeItemName();
            }
        } else if (columnIndex == 1) {
            if (!bean.isHost()) {
                return bean.getWholeRefItemName();
            }
        } else if (columnIndex == 2) {
            if (!bean.isHost()) {
                return String.valueOf(bean.getNodeNum());
            }
        } else if (columnIndex == 3) {
            return StringUtils.trimToEmpty(bean.getReferenceProjectName());
        }

        return ItemReferenceBean.EMPTY_STRING;
    }

    public Object[] getChildren(Object parentElement) {
        return ((ItemReferenceBean) parentElement).getChildren().toArray();
    }

    public Object getParent(Object element) {
        return null;
    }

    public boolean hasChildren(Object element) {
        return getChildren(element).length != 0;
    }

    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof List) {
            return ((List) inputElement).toArray();
        }
        if (inputElement instanceof Object[]) {
            return (Object[]) inputElement;
        }
        return ItemReferenceBean.EMPTY_ARRAY;
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

}
