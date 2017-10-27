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
package org.talend.utils.exceptions;

/**
 * DOC scorreia class global comment. Detailled comment
 */
public class TalendException extends Exception {

    private static final long serialVersionUID = -7462582102179127770L;

    /**
     * DOC scorreia TalendException constructor comment.
     */
    public TalendException() {
        super();
    }

    /**
     * DOC scorreia TalendException constructor comment.
     * 
     * @param message
     * @param cause
     */
    public TalendException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * DOC scorreia TalendException constructor comment.
     * 
     * @param message
     */
    public TalendException(String message) {
        super(message);
    }

    /**
     * DOC scorreia TalendException constructor comment.
     * 
     * @param cause
     */
    public TalendException(Throwable cause) {
        super(cause);
    }

}
