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
package org.talend.repository.items.importexport.ui.handlers.manager;

import org.talend.repository.items.importexport.ui.handlers.IImportExportItemsActionHelper;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ActionHelperRegistry {

    private final String id;

    private final IImportExportItemsActionHelper actionHelper;

    private String description;

    private String overrideId;

    public ActionHelperRegistry(String id, IImportExportItemsActionHelper actionHelper) {
        super();
        this.id = id;
        this.actionHelper = actionHelper;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOverrideId() {
        return this.overrideId;
    }

    public void setOverrideId(String overrideId) {
        this.overrideId = overrideId;
    }

    public IImportExportItemsActionHelper getActionHelper() {
        return this.actionHelper;
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
        ActionHelperRegistry other = (ActionHelperRegistry) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.id;
    }

}
