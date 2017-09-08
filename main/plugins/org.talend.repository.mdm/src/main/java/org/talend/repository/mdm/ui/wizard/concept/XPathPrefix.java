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
package org.talend.repository.mdm.ui.wizard.concept;

/**
 * DOC talend class global comment. Detailled comment
 */
public enum XPathPrefix {

    USER_DEFINED_ITEM("USER_DEFINED_ITEM", ""), //$NON-NLS-1$ //$NON-NLS-2$
    NONE_ITEM("NONE_ITEM", "\"\""), //$NON-NLS-1$ //$NON-NLS-2$
    TRIGGER_ITEM("TRIGGER_ITEM", "\"/exchange/item\""), //$NON-NLS-1$ //$NON-NLS-2$
    PROCESS_ITEM("PROCESS_ITEM", "\"/item\""); //$NON-NLS-1$ //$NON-NLS-2$

    private String prefix;

    private String displayName;

    XPathPrefix(String prefix, String displayName) {
        this.prefix = prefix;
        this.displayName = displayName;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getDisplayName() {
        return this.displayName;
    }

}
