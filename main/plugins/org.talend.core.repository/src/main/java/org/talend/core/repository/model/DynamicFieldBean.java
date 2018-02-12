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
package org.talend.core.repository.model;

/**
 * Use to manage dynamic fields in connections managment dialog. <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ven., 29 sept. 2006) nrousseau $
 * 
 */
public class DynamicFieldBean {

    private String id;

    private String name;

    private String defaultValue;

    private boolean required;

    private boolean password;

    private boolean readonly;

    public DynamicFieldBean(String id, String name, boolean required, boolean password) {
        this(id, name, null, required, password, false);
    }

    public DynamicFieldBean(String id, String name, String defaultValue, boolean required, boolean password, boolean readonly) {
        super();
        this.id = id;
        this.name = name;
        this.defaultValue = defaultValue;
        this.required = required;
        this.password = password;
        this.readonly = readonly;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isReadonly() {
        return this.readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

}
