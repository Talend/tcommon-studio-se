// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.context.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Added by Marvin Wang on Mar.7, 2012 for job context view tabs as abstract parent model, all the first level node
 * model of tabs like "Variables", "Values as tree" and "Values as table" in job Contexts view should be extended this.
 */
public abstract class ContextTabParentModel {

    // This name is variable name, do not use this to store other values.
    private String name;

    // Only used for "Group By Source"
    private String sourceId;

    private List<ContextTabChildModel> children;

    public ContextTabParentModel() {
        children = new ArrayList<ContextTabChildModel>();
    }

    public ContextTabParentModel(String sourceId, String name) {
        this();
        this.sourceId = sourceId;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContextTabChildModel> getChildren() {
        return this.children;
    }

    public void setChildren(List<ContextTabChildModel> children) {
        this.children = children;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean hasChildren() {
        boolean has = false;
        if (children != null && children.size() > 0)
            has = true;
        return has;
    }

    public void addChild(ContextTabChildModel child) {
        if (children == null)
            children = new ArrayList<ContextTabChildModel>();
        children.add(child);
    }

}
