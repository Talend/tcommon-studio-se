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
package org.talend.commons.ui.swt.drawing.link;

import org.eclipse.swt.widgets.TreeItem;

public class TreeExtremityDescriptor implements IExtremityLink<TreeItem, Object> {

    private TreeItem treeItem;

    private Object dataObject;

    public TreeExtremityDescriptor(TreeItem treeItem, Object dataObject) {
        super();
        this.treeItem = treeItem;
        this.dataObject = dataObject;
    }

    public TreeItem getGraphicalObject() {
        return treeItem;
    }

    public Object getDataItem() {
        return dataObject;
    }

    public void setDataItem(Object dataItem) {
        this.dataObject = dataItem;
    }

    public void setGraphicalObject(TreeItem graphicalItem) {
        this.treeItem = graphicalItem;
    }

}
