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
package org.talend.commons.ui.swt.dnd;

import java.util.ArrayList;
import java.util.List;

/**
 * A data container for LocalDataTransfer.
 */
public class LocalDraggedData {

    private List<Object> transferableEntryList = new ArrayList<Object>();

    private Object table;

    public boolean add(Object o) {
        return this.transferableEntryList.add(o);
    }

    public Object getTable() {
        return this.table;
    }

    public void setTable(Object table) {
        this.table = table;
    }

    public List<Object> getTransferableEntryList() {
        return this.transferableEntryList;
    }

}
