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
package org.talend.commons.ui.swt.dnd;

import java.util.ArrayList;
import java.util.List;

/**
 * bqian A data container for LocalDataTransfer.<br/>
 * 
 * $Id$
 * 
 */
public class LocalDraggedData {

    private List<Object> transferableEntryList = new ArrayList<Object>();

    private Object table;

    /**
     * @param o
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Object o) {
        return this.transferableEntryList.add(o);
    }

    /**
     * 
     * DOC wzhang Comment method "getTable".
     * 
     * @return
     */
    public Object getTable() {
        return this.table;
    }

    /**
     * 
     * DOC wzhang Comment method "setTable".
     * 
     * @param table
     */
    public void setTable(Object table) {
        this.table = table;
    }

    /**
     * Getter for transferableEntryList.
     * 
     * @return the transferableEntryList
     */
    public List<Object> getTransferableEntryList() {
        return this.transferableEntryList;
    }
}
