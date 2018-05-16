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
package org.talend.commons.exception;

import org.talend.commons.i18n.internal.Messages;

/**
 * Defines system exception - Use or extends this class when a system problem occurs and affects a fonctionnality but
 * application can keep running.<br/>
 * 
 * $Id$
 * 
 */
public class SystemException extends Exception {

    @SuppressWarnings("unused")//$NON-NLS-1$
    private static final long serialVersionUID = 1L;

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    public SystemException(String message) {
        super(message);
    }
}
