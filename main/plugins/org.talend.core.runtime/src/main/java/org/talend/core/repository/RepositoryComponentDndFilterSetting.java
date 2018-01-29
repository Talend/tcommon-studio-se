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
package org.talend.core.repository;

/**
 * ggu class global comment. Detailled comment
 */
public class RepositoryComponentDndFilterSetting {

    private String id, name;

    private int level;

    private IRepositoryComponentDndFilter filter;

    public RepositoryComponentDndFilterSetting() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public IRepositoryComponentDndFilter getFilter() {
        return filter;
    }

    public void setFilter(IRepositoryComponentDndFilter filter) {
        this.filter = filter;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RepositoryComponentDndFilterSetting)) {
            return false;
        }
        RepositoryComponentDndFilterSetting other = (RepositoryComponentDndFilterSetting) obj;

        return other.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return this.getName() + "      \n" + this.getId();
    }

}
