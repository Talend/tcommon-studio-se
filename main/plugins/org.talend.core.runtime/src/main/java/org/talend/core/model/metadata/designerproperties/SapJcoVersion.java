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
package org.talend.core.model.metadata.designerproperties;

/**
 * DOC talend class global comment. Detailled comment
 */
public enum SapJcoVersion {
    SAP2("sap jco 2.*", "sapjco.jar"), //$NON-NLS-1$
    SAP3("sap jco 3.*", "sapjco3.jar");//$NON-NLS-1$

    private String displayName;

    private String modulName;

    private SapJcoVersion(String displayName, String modulName) {
        this.displayName = displayName;
        this.modulName = modulName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getModulName() {
        return this.modulName;
    }

    public static String[] getAllItemsDisplay() {
        SapJcoVersion[] values = values();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            SapJcoVersion version = values[i];
            names[i] = version.getDisplayName();
        }
        return names;
    }

    public static String[] getAllNeededModuls() {
        SapJcoVersion[] values = values();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            SapJcoVersion version = values[i];
            names[i] = version.getModulName();
        }
        return names;
    }
}
