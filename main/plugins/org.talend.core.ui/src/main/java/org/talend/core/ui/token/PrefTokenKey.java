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
package org.talend.core.ui.token;

/**
 * ggu class global comment. Detailled comment
 */
public class PrefTokenKey extends TokenKey {

    private String prefKey;

    public PrefTokenKey(String key, String prefKey) {
        super(key);
        this.prefKey = prefKey;
    }

    public String getPrefKey() {
        return prefKey;
    }

}
