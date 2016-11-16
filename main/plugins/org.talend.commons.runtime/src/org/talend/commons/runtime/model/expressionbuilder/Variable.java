// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.runtime.model.expressionbuilder;

/**
 * Represents the variables that can be listed in the table viewer.
 */
public class Variable {

    private String name;

    private String value;

    private String talendType;

    private boolean nullable;

    public Variable() {
    }

    public Variable(int n) {
        name = "name" + String.valueOf(n); //$NON-NLS-1$
        value = "value" + String.valueOf(n); //$NON-NLS-1$
    }

    public Variable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Variable(String name, String value, String type, boolean nullable) {
        this.name = name;
        this.value = value;
        this.talendType = type;
        this.nullable = nullable;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getTalendType() {
        return this.talendType;
    }

    public boolean isNullable() {
        return this.nullable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setTalendType(String typeId) {
        this.talendType = typeId;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
