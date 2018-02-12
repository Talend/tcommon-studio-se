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
package org.talend.core.model.metadata.connection.hive;

/**
 * DOC ggu class global comment. Detailled comment
 */
public enum HiveModeInfo {
    STANDALONE("Standalone"), //$NON-NLS-1$
    EMBEDDED("Embedded"), //$NON-NLS-1$
    ;

    private String display;

    private HiveModeInfo(String diaplyName) {
        this.display = diaplyName;
    }

    public String getName() {
        return name();
    }

    public String getDisplayName() {
        return display;
    }

    public static HiveModeInfo getByDisplay(String display) {
        for (HiveModeInfo m : HiveModeInfo.values()) {
            if (m.getDisplayName().equals(display)) {
                return m;
            }
        }
        return null;
    }

    public static HiveModeInfo get(String name) {
        for (HiveModeInfo m : HiveModeInfo.values()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }
}
