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
package org.talend.core.ui.context.model.tree;

import java.util.List;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.utils.ContextParameterUtils;
import org.talend.core.ui.context.model.ContextProviderProxy;

/**
 * cli class global comment. Detailled comment
 * 
 */
public class GroupByContextProvider extends ContextProviderProxy {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof IContext) {
            if (columnIndex == 0) {
                // Context column
                return ((IContext) element).getName();
            }
        } else {
            IContextParameter para = (IContextParameter) element;
            switch (columnIndex) {
            case 1:
                // variable column
                return para.getName();
            case 3:
                // prompt column
                return para.getPrompt();
            case 4:
                // value column
                return ContextParameterUtils.checkAndHideValue(para);
            }
        }
        return ""; //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ui.context.ConextTreeValuesComposite.ProviderProxy#getColumnImage(java.lang.Object, int)
     */
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        if (columnIndex == 2 && (element instanceof IContextParameter)) {
            IContextParameter para = (IContextParameter) element;
            if (para.isPromptNeeded()) {
                return ImageProvider.getImage(EImage.CHECKED_ICON);
            } else {
                return ImageProvider.getImage(EImage.UNCHECKED_ICON);
            }

        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        List<IContext> contexts = (List<IContext>) inputElement;
        return contexts.toArray();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     */
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof IContext) {
            return ((IContext) parentElement).getContextParameterList().toArray();
        }
        return new Object[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     */
    public Object getParent(Object element) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     */
    public boolean hasChildren(Object element) {
        if (element instanceof IContext) {
            return !((IContext) element).getContextParameterList().isEmpty();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    public Color getForeground(Object element, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    public Color getBackground(Object element, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }
}
