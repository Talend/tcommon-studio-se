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
package org.talend.core.model.general;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class LibraryInfo {

    private String libName;

    private String bundleId;

    public String getLibName() {
        return this.libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getBundleId() {
        return this.bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.bundleId == null) ? 0 : this.bundleId.hashCode());
        result = prime * result + ((this.libName == null) ? 0 : this.libName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LibraryInfo other = (LibraryInfo) obj;
        if (this.bundleId == null) {
            if (other.bundleId != null)
                return false;
        } else if (!this.bundleId.equals(other.bundleId))
            return false;
        if (this.libName == null) {
            if (other.libName != null)
                return false;
        } else if (!this.libName.equals(other.libName))
            return false;
        return true;
    }

}
