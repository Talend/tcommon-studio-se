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
package org.talend.core.model.update.extension;

import org.talend.core.model.update.IUpdateItemType;

/**
 * DOC ggu class global comment. Detailled comment
 * 
 * this is from extension point.
 */
public class ExtensionUpdateItemType implements IUpdateItemType {

    private final String name, bundleId;

    private String displayLabel, description;

    public ExtensionUpdateItemType(String name, String bundlId) {
        super();
        this.name = name;
        this.bundleId = bundlId;
    }

    public ExtensionUpdateItemType(String name, String bundleId, String displayLabel) {
        this(name, bundleId);
        this.displayLabel = displayLabel;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayLabel() {
        return this.displayLabel;
    }

    public String getBundleId() {
        return this.bundleId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
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
        if (!(obj instanceof ExtensionUpdateItemType)) {
            return false;
        }
        ExtensionUpdateItemType other = (ExtensionUpdateItemType) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
