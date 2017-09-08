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
package org.talend.core.ui.context.model.table;

/**
 * ldong class global comment. Detailled comment
 */
public class ConectionAdaptContextVariableModel {

    private String name; // keep the connection parameter's name

    private String value;// keep the context variable name

    private String customValue; // keep the new context variable name which user defined

    public ConectionAdaptContextVariableModel(String name, String value, String customValue) {
        this.name = name;
        this.value = value;
        this.customValue = customValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }

}
