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
package org.talend.commons.runtime.model.expressionbuilder;

/**
 * Represents to the varialbes can be list in the table viewer.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: Variable.java 下午04:07:16 2007-6-25 +0000 (2007-6-25) yzhang $
 * 
 */
public class Variable {

    private String name;

    private String value;

    private String talendType;

    private boolean nullable;

    /**
     * yzhang Variable constructor comment.
     */
    public Variable() {
    }

    /**
     * yzhang Variable constructor comment.
     */
    public Variable(int n) {
        name = "name" + String.valueOf(n); //$NON-NLS-1$
        value = "value" + String.valueOf(n); //$NON-NLS-1$
    }

    /**
     * yzhang Variable constructor comment.
     * 
     * @param name
     * @param value
     */
    public Variable(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * yzhang Variable constructor comment.
     * 
     * @param name
     * @param value
     */
    public Variable(String name, String value, String type, boolean nullable) {
        this.name = name;
        this.value = value;
        this.talendType = type;
        this.nullable = nullable;
    }

    /**
     * Getter for name.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for value.
     * 
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * yzhang Comment method "getTalendType".
     * 
     * @return
     */
    public String getTalendType() {
        return this.talendType;
    }

    /**
     * yzhang Comment method "isNullable".
     * 
     * @return
     */
    public boolean isNullable() {
        return this.nullable;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the value.
     * 
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * yzhang Comment method "setTalendType".
     * 
     * @param typeId
     */
    public void setTalendType(String typeId) {
        this.talendType = typeId;
    }

    /**
     * yzhang Comment method "setNullable".
     * 
     * @param nullable
     */
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
}
