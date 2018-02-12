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
package org.talend.core.runtime.repository.item;

/**
 * DOC ggu class global comment. Detailled comment
 */
public enum ItemProductKeys {

    FULLNAME("product_fullname"),
    DATE("date"),
    VERSION("product_version"), ;

    private String key;

    private ItemProductKeys(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getImportKey() {
        return "import_" + getKey();
    }

    public String getCreatedKey() {
        // should be same as the PropertyImpl.getCreationDate()
        return "created_" + getKey();
    }

    public String getModifiedKey() {
        // should be same as the PropertyImpl.getModificationDate()
        return "modified_" + getKey();
    }

}
