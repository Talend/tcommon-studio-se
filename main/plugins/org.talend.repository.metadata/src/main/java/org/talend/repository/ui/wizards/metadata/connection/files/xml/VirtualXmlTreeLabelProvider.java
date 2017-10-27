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
package org.talend.repository.ui.wizards.metadata.connection.files.xml;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.talend.datatools.xml.utils.ATreeNode;

/**
 * created by nrousseau on May 9, 2013 Detailled comment
 * 
 */
public class VirtualXmlTreeLabelProvider extends LabelProvider implements IColorProvider {

    private Color namespaceColor;

    /**
     * DOC nrousseau VirtualXmlTreeLabelProvider constructor comment.
     */
    public VirtualXmlTreeLabelProvider() {
        super();
        namespaceColor = new Color(Display.getDefault(), new RGB(0, 130, 0));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        ATreeNode treeNode = (ATreeNode) element;
        int type = treeNode.getType();
        String text;
        if (type == ATreeNode.NAMESPACE_TYPE) {
            if ("".equals(treeNode.getDataType())) { //$NON-NLS-1$
                text = "xmlns=" + treeNode.getLabel(); //$NON-NLS-1$
            } else {
                text = "xmlns:" + treeNode.getDataType() + "=" + treeNode.getLabel(); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else if (type == ATreeNode.ATTRIBUTE_TYPE) {
            text = "@" + treeNode.getLabel(); //$NON-NLS-1$
        } else {
            text = treeNode.getLabel();
        }
        return text;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
     */
    @Override
    public Color getForeground(Object element) {
        ATreeNode treeNode = (ATreeNode) element;
        int type = treeNode.getType();
        if (type == ATreeNode.NAMESPACE_TYPE) {
            return namespaceColor;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
     */
    @Override
    public Color getBackground(Object element) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.BaseLabelProvider#dispose()
     */
    @Override
    public void dispose() {
        namespaceColor.dispose();
        super.dispose();
    }

}
