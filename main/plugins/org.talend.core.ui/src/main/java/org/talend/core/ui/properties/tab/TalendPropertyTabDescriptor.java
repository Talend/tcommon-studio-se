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
package org.talend.core.ui.properties.tab;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.talend.core.model.process.EComponentCategory;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class TalendPropertyTabDescriptor implements IStructuredTabItem {

    private IDynamicProperty propertyComposite;

    private boolean isSubItem;

    private String label;

    private List<IStructuredTabItem> subItems;

    private Object data;

    private EComponentCategory category;

    /**
     * yzhang TalendPropertyTabDescriptor constructor comment.
     */
    public TalendPropertyTabDescriptor(EComponentCategory category) {
        this.category = category;
        this.label = category.getTitle();
        subItems = new ArrayList<IStructuredTabItem>();
    }

    /**
     * Getter for category.
     * 
     * @return the category
     */
    public EComponentCategory getCategory() {
        if (category.isAlias()) {
            return category.getAliasFor();
        }
        return this.category;
    }

    /**
     * Getter for element.
     * 
     * @return the element
     */
    public Object getData() {
        return this.data;
    }

    /**
     * Sets the element.
     * 
     * @param element the element to set
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * yzhang Comment method "addSubItme".
     * 
     * @param item
     */
    public void addSubItem(IStructuredTabItem item) {
        item.setSubItem(IStructuredTabItem.SUBITEM);
        subItems.add(item);

    }

    /**
     * Sets the isSubItem.
     * 
     * @param isSubItem the isSubItem to set
     */
    public void setSubItem(boolean isSubItem) {
        this.isSubItem = isSubItem;
    }

    /**
     * Sets the composite.
     * 
     * @param composite the composite to set
     */
    public void setPropertyComposite(IDynamicProperty part) {
        this.propertyComposite = part;
    }

    /**
     * Getter for composite.
     * 
     * @return the composite
     */
    public IDynamicProperty getPropertyComposite() {
        return this.propertyComposite;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.ITabItem#getImage()
     */
    public Image getImage() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.ITabItem#getText()
     */
    public String getText() {
        return label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.ITabItem#isIndented()
     */
    public boolean isIndented() {
        return isSubItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.views.properties.tabbed.ITabItem#isSelected()
     */
    public boolean isSelected() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IStructuredTabItem#getSubItems()
     */
    public IStructuredTabItem[] getSubItems() {
        return subItems.toArray(new IStructuredTabItem[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IStructuredTabItem#hasSubItems()
     */
    public boolean hasSubItems() {
        return subItems.size() > 0;
    }

    private boolean expaned;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IStructuredTabItem#isExpanded()
     */
    public boolean isExpanded() {
        return expaned;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IStructuredTabItem#setExpanded(boolean)
     */
    public void setExpanded(boolean expaned) {
        this.expaned = expaned;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.properties.tab.IStructuredTabItem#isSubItem()
     */
    public boolean isSubItem() {
        return isSubItem;
    }

}
