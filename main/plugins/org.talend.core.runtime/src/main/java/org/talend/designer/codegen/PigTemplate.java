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
package org.talend.designer.codegen;

/**
 * created by Administrator on 2013-3-14 Detailled comment
 * 
 */
public enum PigTemplate {
    EVALFUNC("Eval Function", "__PIGUDF_EVALFUNC__"), //$NON-NLS-1$ //$NON-NLS-2$
    STOREFUNC("Store Function", "__PIGUDF_STOREFUNC__"), //$NON-NLS-1$ //$NON-NLS-2$
    LOADFUNC("Load Function", "__PIGUDF_LOADFUNC__"), //$NON-NLS-1$ //$NON-NLS-2$
    FILTERFUNC("Filter Function", "__PIGUDF_FILTERFUNC__"); //$NON-NLS-1$ //$NON-NLS-2$

    private String displayName;

    private String fileName;

    PigTemplate(String displayName, String fileName) {
        this.displayName = displayName;
        this.fileName = fileName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public static String[] getPigTemplateToDispaly() {
        String[] dispalyNames = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            dispalyNames[i] = values()[i].getDisplayName();
        }
        return dispalyNames;
    }
}
