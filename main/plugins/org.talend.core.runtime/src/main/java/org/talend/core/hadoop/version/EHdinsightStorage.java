// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.hadoop.version;

import java.util.ArrayList;
import java.util.List;

/**
 * created by hcyi on May 27, 2020
 * Detailled comment
 *
 */
public enum EHdinsightStorage {

    ADLS_GEN2("ADLS Gen2"), //$NON-NLS-1$

    AZURE_STORAGE("Azure storage"); //$NON-NLS-1$

    private String displayName;

    EHdinsightStorage(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static List<String> getAllHdinsightStorageDisplayNames() {
        return getAllHdinsightStorageNames(true);
    }

    public static List<String> getAllHdinsightStorageNames(boolean display) {
        List<String> names = new ArrayList<String>();
        EHdinsightStorage[] values = values();
        for (EHdinsightStorage storage : values) {
            if (display) {
                names.add(storage.getDisplayName());
            } else {
                names.add(storage.getName());
            }
        }
        return names;
    }

    public static EHdinsightStorage getHdinsightStoragenByDisplayName(String name) {
        return getHdinsightStorageByName(name, true);
    }

    public static EHdinsightStorage getHdinsightStorageByName(String name, boolean display) {
        if (name != null) {
            for (EHdinsightStorage storage : values()) {
                if (display) {
                    if (name.equalsIgnoreCase(storage.getDisplayName())) {
                        return storage;
                    }
                } else {
                    if (name.equalsIgnoreCase(storage.getName())) {
                        return storage;
                    }
                }
            }
        }
        return null;
    }
}
