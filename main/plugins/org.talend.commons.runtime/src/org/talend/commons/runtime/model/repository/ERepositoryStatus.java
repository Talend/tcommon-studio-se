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
package org.talend.commons.runtime.model.repository;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public enum ERepositoryStatus {
    NEW(true, true),
    LOCK_BY_USER(true, false),
    LOCK_BY_OTHER(false, false),
    DEFAULT(false, true),
    NOT_UP_TO_DATE(false, false),
    DELETED(false, false),
    READ_ONLY(false, false),
    // PTODO SML [FOLDERS++] Only to manage folders: temporary code
    EDITABLE(true, false),
    // be used by information level in Property.
    WARN(false, true),
    ERROR(false, true);

    private boolean editable;

    private boolean potentiallyEditable;

    ERepositoryStatus(boolean editable, boolean potentiallyEditable) {
        this.editable = editable;
        this.potentiallyEditable = potentiallyEditable;
    }

    /**
     * Getter for editable.
     * 
     * @return the editable
     */
    public boolean isEditable() {
        return this.editable;
    }

    /**
     * Getter for potentiallyEditable.
     * 
     * @return the potentiallyEditable
     */
    public boolean isPotentiallyEditable() {
        return this.potentiallyEditable;
    }

    /**
     * 
     * Judge current status is lock status
     * 
     * @param es The status of current
     * @return true if current is lock status else false
     */
    public static boolean isLock(ERepositoryStatus es) {
        if (LOCK_BY_USER == es || LOCK_BY_OTHER == es) {
            return true;
        }
        return false;
    }

}
