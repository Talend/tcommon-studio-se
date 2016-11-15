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
package org.talend.commons.utils.threading;

public abstract class AbsRetrieveColumnRunnable implements Runnable {

    volatile boolean isCanceled = false;

    private Object columnObject;

    public Object getColumnObject() {
        return this.columnObject;
    }

    public void setColumnObject(Object columnObject) {
        this.columnObject = columnObject;
    }

    public void setCanceled(boolean cancel) {
        this.isCanceled = cancel;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

}
