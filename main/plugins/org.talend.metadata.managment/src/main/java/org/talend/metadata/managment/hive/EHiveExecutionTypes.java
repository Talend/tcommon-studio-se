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
package org.talend.metadata.managment.hive;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * created by ycbai on 2015年5月5日 Detailled comment
 *
 */
public enum EHiveExecutionTypes {

    MR("MapReduce"), //$NON-NLS-1$

    TEZ("Tez"), //$NON-NLS-1$

    ;

    private String label;

    EHiveExecutionTypes(String label) {
        this.label = label;
    }

    private String getName() {
        return this.name();
    }

    public String getLabel() {
        return this.label;
    }

    public String getValue() {
        return getName().toLowerCase();
    }

    public static String[] getExecutionTypeLabels() {
        List<String> typeLabels = new ArrayList<String>();
        EHiveExecutionTypes[] executionTypes = values();
        for (EHiveExecutionTypes executionType : executionTypes) {
            typeLabels.add(executionType.getLabel());
        }
        return typeLabels.toArray(new String[0]);
    }

    public static EHiveExecutionTypes getTypeFromLabel(String label) {
        return getType(label, true);
    }

    public static EHiveExecutionTypes getTypeFromValue(String value) {
        return getType(value, false);
    }

    private static EHiveExecutionTypes getType(String labelOrValue, boolean isLabel) {
        for (EHiveExecutionTypes exeType : values()) {
            String refer;
            if (isLabel) {
                refer = exeType.getLabel();
            } else {
                refer = exeType.getValue();
            }
            if (refer.equals(labelOrValue)) {
                return exeType;
            }
        }
        return null;
    }

}
