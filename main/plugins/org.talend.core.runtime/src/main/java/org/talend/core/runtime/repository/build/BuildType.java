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
package org.talend.core.runtime.repository.build;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BuildType {

    final String name, label;

    final int order;

    BuildType(String name, String label, int order) {
        super();
        this.name = name;
        this.label = label;
        this.order = order;
    }

    BuildType(String name, String label) {
        this(name, label, 0);
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        if (!(obj instanceof BuildType)) {
            return false;
        }
        BuildType other = (BuildType) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ExportType [name=" + name + ", label=" + label + "]";
    }

}
