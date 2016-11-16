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

import org.eclipse.swt.widgets.Item;

public class ItemExtremityDescriptor implements IExtremityLink<Item, Object> {

    private Item item;

    private Object dataObject;

    public ItemExtremityDescriptor(Item item, Object dataObject) {
        super();
        this.item = item;
        this.dataObject = dataObject;
    }

    public Item getGraphicalObject() {
        return item;
    }

    public Object getDataItem() {
        return dataObject;
    }

    public void setDataItem(Object dataItem) {
        this.dataObject = dataItem;
    }

    public void setGraphicalObject(Item graphicalItem) {
        this.item = graphicalItem;
    }

}
