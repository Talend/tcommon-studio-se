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
package org.talend.updates.runtime.model;

import org.eclipse.core.runtime.IStatus;

/**
 * created by ggu on Jul 23, 2014 Detailled comment
 *
 */
public class StatusException extends Exception {

    private static final long serialVersionUID = -71237725085948744L;

    private IStatus status;

    public StatusException(IStatus status) {
        super(status.getMessage(), status.getException());
        this.status = status;
    }

    public IStatus getStatus() {
        return this.status;
    }

}
