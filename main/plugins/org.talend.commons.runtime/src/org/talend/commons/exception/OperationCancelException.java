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
package org.talend.commons.exception;

/**
 * created by Talend on Aug 7, 2014 Detailled comment
 * 
 */
public class OperationCancelException extends PersistenceException {

    private static final long serialVersionUID = 3556433676899745391L;

    public OperationCancelException(String message, Throwable cause) {
        super(message, cause);
    }

    public OperationCancelException(String message) {
        super(message);
    }

    public OperationCancelException(Throwable cause) {
        super(cause);
    }

}
