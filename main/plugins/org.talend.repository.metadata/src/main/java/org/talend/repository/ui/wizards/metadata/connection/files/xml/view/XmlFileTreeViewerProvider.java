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
package org.talend.repository.ui.wizards.metadata.connection.files.xml.view;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.Attribute;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.FOXTreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.node.NameSpaceNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.TreeUtil;

public class XmlFileTreeViewerProvider extends LabelProvider implements ITableLabelProvider, ITreeContentProvider {

    public Object[] getChildren(Object parentElement) {
        FOXTreeNode treeNode = (FOXTreeNode) parentElement;
        List<FOXTreeNode> children = treeNode.getChildren();
        return children.toArray();
    }

    public Object getParent(Object element) {
        FOXTreeNode treeNode = (FOXTreeNode) element;
        return treeNode.getParent();
    }

    public boolean hasChildren(Object element) {
        FOXTreeNode treeNode = (FOXTreeNode) element;
        return !treeNode.getChildren().isEmpty();
    }

    public Object[] getElements(Object inputElement) {
        List list = (List) inputElement;
        return list.toArray();
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub

    }

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        FOXTreeNode treeNode = (FOXTreeNode) element;
        switch (columnIndex) {
        case 0:
            return treeNode.getLabelForViewer();
        case 1:
            return treeNode.getColumnLabel();
        case 2:
            if (treeNode instanceof Attribute) {
                return "-"; //$NON-NLS-1$
            } else if (treeNode instanceof NameSpaceNode) {
                return "-"; //$NON-NLS-1$
            } else if (treeNode.isGroup()) {
                return "group element";
            } else if (treeNode.isLoop()) {
                return "loop element";
            } else if (TreeUtil.isSubLoopNode(treeNode)) {
                return "group";
            } else {
                return ""; //$NON-NLS-1$
            }
        case 3:
            return treeNode.getDefaultValue();
        default:
            return ""; //$NON-NLS-1$
        }
    }
}
