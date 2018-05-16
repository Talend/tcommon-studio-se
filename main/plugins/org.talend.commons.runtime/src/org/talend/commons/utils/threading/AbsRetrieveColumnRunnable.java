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
package org.talend.commons.utils.threading;

/**
 * created by wchen on 2014-6-6 Detailled comment
 * 
 */
public abstract class AbsRetrieveColumnRunnable implements Runnable {

    volatile boolean isCanceled = false;

    private Object columnObject;

    /**
     * Getter for columnObject.
     * 
     * @return the columnObject
     */
    public Object getColumnObject() {
        return this.columnObject;
    }

    /**
     * Sets the columnObject.
     * 
     * @param columnObject the columnObject to set
     */
    public void setColumnObject(Object columnObject) {
        this.columnObject = columnObject;
    }

    public void setCanceled(boolean cancel) {
        this.isCanceled = cancel;
    }

    /**
     * Getter for isCanceled.
     * 
     * @return the isCanceled
     */
    public boolean isCanceled() {
        return this.isCanceled;
    }

}
