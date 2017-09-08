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
package org.talend.core.model.repository;

import java.util.Date;

/**
 * DOC nrousseau class global comment. Detailled comment
 */
public class LockInfo {

    private String user;

    private String application;

    private Date lockedDate;

    public LockInfo(String user, String application, Date lockedDate) {
        this.user = user;
        this.application = application;
        this.lockedDate = lockedDate;
    }

    public String getUser() {
        return user;
    }

    public String getApplication() {
        return application;
    }

    public Date getLockedDate() {
        return lockedDate;
    }

}
