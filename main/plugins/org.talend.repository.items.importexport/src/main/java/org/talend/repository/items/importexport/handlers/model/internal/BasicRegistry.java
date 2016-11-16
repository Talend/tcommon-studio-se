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
package org.talend.repository.items.importexport.handlers.model.internal;

public class BasicRegistry {

    private final String bundleId, id;

    private String name, description, overrideId;

    private EPriority priority = EPriority.NORMAL;

    public BasicRegistry(String bundleId, String id) {
        super();
        this.bundleId = bundleId;
        this.id = id;
    }

    public String getBundleId() {
        return this.bundleId;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EPriority getPriority() {
        return this.priority;
    }

    public void setPriority(EPriority priority) {
        this.priority = priority;
    }

    public String getOverrideId() {
        return this.overrideId;
    }

    public void setOverrideId(String overrideId) {
        this.overrideId = overrideId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BasicRegistry other = (BasicRegistry) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
