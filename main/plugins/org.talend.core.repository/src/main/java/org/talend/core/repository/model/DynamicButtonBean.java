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
package org.talend.core.repository.model;

import org.eclipse.swt.events.SelectionListener;

/**
 */
public class DynamicButtonBean {

    private String id;

    private String name;

    private SelectionListener listener;

    public DynamicButtonBean(String id, String name, SelectionListener listener) {
        this.id = id;
        this.name = name;
        this.listener = listener;
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

    public SelectionListener getListener() {
        return listener;
    }

    public void setListener(SelectionListener listener) {
        this.listener = listener;
    }

}
