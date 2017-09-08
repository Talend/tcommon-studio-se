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
package org.talend.commons.ui.runtime.exception;

import org.talend.commons.exception.FatalException;

/**
 * Exception handling by throwing a runtime exception.<br/>
 * 
 * $Id: RuntimeExceptionHandler.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class RuntimeExceptionHandler {

    /**
     * Empty constructor.
     */
    private RuntimeExceptionHandler() {
    }

    /**
     * Log the exception then trow a FatalException wich cause is param ex.
     * 
     * @param ex - exception to log
     */
    public static void process(Throwable ex) {
        ExceptionHandler.process(ex);
        throw new FatalException(ex);
    }
}
