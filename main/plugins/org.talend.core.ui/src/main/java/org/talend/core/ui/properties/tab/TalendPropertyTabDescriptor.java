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
package org.talend.core.ui.properties.tab;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.talend.core.model.process.EComponentCategory;

public class TalendPropertyTabDescriptor implements IStructuredTabItem {

    private IDynamicProperty propertyComposite;

    private boolean isSubItem;

    private String label;

    private List<IStructuredTabItem> subItems;

    private Object data;

    private EComponentCategory category;

    public TalendPropertyTabDescriptor(EComponentCategory category) {
        this.category = category;
        this.label = category.getTitle();
        subItems = new ArrayList<IStructuredTabItem>();
    }

    public EComponentCategory getCategory() {
        if (category.isAlias()) {
            return category.getAliasFor();
        }
        return this.category;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void addSubItem(IStructuredTabItem item) {
        item.setSubItem(IStructuredTabItem.SUBITEM);
        subItems.add(item);

    }

    public void setSubItem(boolean isSubItem) {
        this.isSubItem = isSubItem;
    }

    public void setPropertyComposite(IDynamicProperty part) {
        this.propertyComposite = part;
    }

    public IDynamicProperty getPropertyComposite() {
        return this.propertyComposite;
    }

    public Image getImage() {
        return null;
    }

    public String getText() {
        return label;
    }

    public boolean isIndented() {
        return isSubItem;
    }

    public boolean isSelected() {
        return false;
    }

    public IStructuredTabItem[] getSubItems() {
        return subItems.toArray(new IStructuredTabItem[0]);
    }

    public boolean hasSubItems() {
        return subItems.size() > 0;
    }

    private boolean expaned;

    public boolean isExpanded() {
        return expaned;
    }

    public void setExpanded(boolean expaned) {
        this.expaned = expaned;
    }

    public boolean isSubItem() {
        return isSubItem;
    }

}
