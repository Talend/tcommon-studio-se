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

import org.eclipse.swt.widgets.TableItem;

public class TableExtremityDescriptor implements IExtremityLink<TableItem, Object> {

    private TableItem tableItem;

    private Object dataObject;

    public TableExtremityDescriptor(TableItem treeItem, Object dataObject) {
        super();
        this.tableItem = treeItem;
        this.dataObject = dataObject;
    }

    public TableItem getGraphicalObject() {
        return tableItem;
    }

    public Object getDataItem() {
        return dataObject;
    }

    public void setDataItem(Object dataItem) {
        this.dataObject = dataItem;
    }

    public void setGraphicalObject(TableItem graphicalItem) {
        this.tableItem = graphicalItem;
    }

}
