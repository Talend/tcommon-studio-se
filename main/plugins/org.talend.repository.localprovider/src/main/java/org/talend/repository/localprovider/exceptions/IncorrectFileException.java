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
package org.talend.repository.localprovider.exceptions;

import org.talend.commons.exception.PersistenceException;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class IncorrectFileException extends PersistenceException {

    private static final long serialVersionUID = -4594420267927643967L;

    public IncorrectFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncorrectFileException(Throwable cause) {
        super(cause);
    }
}
