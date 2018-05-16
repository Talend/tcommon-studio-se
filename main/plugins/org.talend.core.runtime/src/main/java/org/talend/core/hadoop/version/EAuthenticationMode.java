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
package org.talend.core.hadoop.version;

import java.util.ArrayList;
import java.util.List;

/**
 * created by ycbai on 2013-3-15 Detailled comment
 * 
 */
public enum EAuthenticationMode {

    USERNAME("Username"), //$NON-NLS-1$

    KRB("Kerberos"), //$NON-NLS-1$

    UGI("Usergroup"); //$NON-NLS-1$

    private String displayName;

    EAuthenticationMode(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name();
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public static List<String> getAllAuthenticationDisplayNames() {
        return getAllAuthenticationNames(true);
    }

    public static List<String> getAllAuthenticationNames(boolean display) {
        List<String> names = new ArrayList<String>();
        EAuthenticationMode[] values = values();
        for (EAuthenticationMode mode : values) {
            if (display) {
                names.add(mode.getDisplayName());
            } else {
                names.add(mode.getName());
            }
        }

        return names;
    }

    public static EAuthenticationMode getAuthenticationByDisplayName(String name) {
        return getAuthenticationByName(name, true);
    }

    public static EAuthenticationMode getAuthenticationByName(String name, boolean display) {
        if (name != null) {
            for (EAuthenticationMode mode : values()) {
                if (display) {
                    if (name.equalsIgnoreCase(mode.getDisplayName())) {
                        return mode;
                    }
                } else {
                    if (name.equalsIgnoreCase(mode.getName())) {
                        return mode;
                    }
                }
            }
        }

        return null;
    }

}
