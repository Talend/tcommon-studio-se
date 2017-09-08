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
package org.talend.core.ui.metadata.dialog;

import java.util.ArrayList;
import java.util.List;

/**
 * qzhang class global comment. Detailled comment <br/>
 * 
 */
public class DbTableSelectorObject {

    private ObjectType type;

    /**
     * qzhang DbTableSelectorObject class global comment. Detailled comment. <br/>
     * 
     */
    public enum ObjectType {
        TABLE,
        COLUMN,
        DB;

    }

    private String label;

    private List<DbTableSelectorObject> children;

    private DbTableSelectorObject parent;

    /**
     * qzhang DbTableSelectorDialog.TreeObject constructor comment.
     */
    public DbTableSelectorObject() {

    }

    public List<DbTableSelectorObject> getChildren() {
        return this.children;
    }

    public void setChildren(List<DbTableSelectorObject> children) {
        this.children = children;
    }

    public String getLabel() {
        return this.label;
    }

    public void addChildren(DbTableSelectorObject object) {
        if (children == null) {
            children = new ArrayList<DbTableSelectorObject>();
        }
        object.setParent(this);
        children.add(object);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ObjectType getType() {
        return this.type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public DbTableSelectorObject getParent() {
        return this.parent;
    }

    public void setParent(DbTableSelectorObject parent) {
        this.parent = parent;
    }
}
