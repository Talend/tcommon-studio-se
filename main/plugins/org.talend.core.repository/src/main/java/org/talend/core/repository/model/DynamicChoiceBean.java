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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 */
public class DynamicChoiceBean {

    private String id;

    private String name;

    private Map<String, String> choices = new LinkedHashMap<String, String>();

    public DynamicChoiceBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addChoice(String value, String label) {
        choices.put(value, label);
    }

    public Map<String, String> getChoices() {
        return choices;
    }

    public String getChoiceValue(int index) {
        int i = 0;
        for (String value : choices.keySet()) {
            if (index == i++) {
                return value;
            }
        }
        return null;
    }

    public int getChoiceIndex(String value) {
        int i = 0;
        for (String value2 : choices.keySet()) {
            if (value2.equals(value)) {
                return i;
            }
            i++;
        }
        return 0; // select first by default

    }
}
