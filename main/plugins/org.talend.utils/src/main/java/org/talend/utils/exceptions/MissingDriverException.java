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
package org.talend.utils.exceptions;


/**
 * created by zshen on Jun 4, 2013
 * Detailled comment
 *
 */
public class MissingDriverException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String errorMessage = ""; //$NON-NLS-1$

    public MissingDriverException() {
        super();
    }

    public MissingDriverException(String message) {
        super(message);
        errorMessage = message;
    }

    public MissingDriverException(Throwable cause, String message) {
        super(cause);
        errorMessage = message;
    }

    public MissingDriverException(Throwable cause) {
        super(cause);
        errorMessage = cause.getMessage();
    }

    /**
     * Getter for errorMessage.
     *
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return this.errorMessage;
    }

}
