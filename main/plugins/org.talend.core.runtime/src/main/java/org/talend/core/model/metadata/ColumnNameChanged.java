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
package org.talend.core.model.metadata;

public class ColumnNameChanged {

    private String oldName;

    private String newName;

    public ColumnNameChanged(String oldName, String newName) {
        super();
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getNewName() {
        return this.newName;
    }

    public String getOldName() {
        return this.oldName;
    }

    @Override
    public String toString() {
        return "Column changed : " + oldName + "->" + newName; //$NON-NLS-1$//$NON-NLS-2$
    }

}
