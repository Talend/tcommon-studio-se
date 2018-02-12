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

import java.util.Date;

/**
 * created by cmeng on Mar 15, 2017
 * Detailled comment
 *
 */
public interface ILockBean {

    public String getApplicationName();

    public String getLockerLoginId();

    public String getItemId();

    public String getItemLabel();

    public Date getLockDate();
}
