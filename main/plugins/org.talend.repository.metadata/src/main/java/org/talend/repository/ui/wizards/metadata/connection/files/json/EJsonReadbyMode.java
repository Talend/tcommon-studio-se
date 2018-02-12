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
package org.talend.repository.ui.wizards.metadata.connection.files.json;

import java.util.ArrayList;
import java.util.List;

/**
 * created by cmeng on Jul 10, 2015 Detailled comment
 *
 */
public enum EJsonReadbyMode {
    XPATH("Xpath", "XPATH"), //$NON-NLS-1$ //$NON-NLS-2$
    JSONPATH("JsonPath", "JSONPATH"); //$NON-NLS-1$ //$NON-NLS-2$

    private String displayName;

    private String value;

    private EJsonReadbyMode(String displayName, String value) {
        this.displayName = displayName;
        this.value = value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static EJsonReadbyMode getEJsonReadbyModeByDisplayName(String displayName) {
        EJsonReadbyMode[] eValues = EJsonReadbyMode.values();
        for (EJsonReadbyMode eValue : eValues) {
            if (eValue.getDisplayName().equals(displayName)) {
                return eValue;
            }
        }
        return null;
    }

    public static EJsonReadbyMode getEJsonReadbyModeByValue(String value) {
        EJsonReadbyMode[] eValues = EJsonReadbyMode.values();
        for (EJsonReadbyMode eValue : eValues) {
            if (eValue.getValue().equals(value)) {
                return eValue;
            }
        }
        return null;
    }

    public static List<String> getUsableReadbyModeValues() {
        EJsonReadbyMode[] eValues = EJsonReadbyMode.values();
        List<String> list = new ArrayList<String>(eValues.length);
        for (EJsonReadbyMode eValue : eValues) {
            list.add(eValue.getDisplayName());
        }
        return list;
    }
}
