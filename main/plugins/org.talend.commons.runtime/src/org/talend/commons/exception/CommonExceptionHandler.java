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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.talend.commons.i18n.internal.Messages;

/**
 * Implementation of exception handling strategy.<br/>
 * 
 * $Id: ExceptionHandler.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public final class CommonExceptionHandler {

    private static Logger log = Logger.getLogger(CommonExceptionHandler.class);

    /**
     * Log message relative to ex param. Log level depends on exception type.
     * 
     * @param ex - exception to log
     */
    public static void process(Throwable ex) {
        Priority priority = getPriority(ex);
        process(ex, priority);
    }

    public static void process(Throwable ex, Priority priority) {
        String message = ex.getMessage();

        log.log(priority, message, ex);

    }

    public static void log(String message) {
        log.log(Level.INFO, message);
    }

    public static void warn(String message) {
        log.log(Level.WARN, message);
    }

    /**
     * Return priority corresponding to the exception implementation.
     * 
     * @param ex - the exception to evaluate priority
     * @return the priority corresponding to the exception implementation
     */
    public static Priority getPriority(Throwable ex) {
        if (ex == null) {
            throw new IllegalArgumentException(Messages.getString("ExceptionHandler.Parameter.BeNull")); //$NON-NLS-1$
        }

        if (ex instanceof BusinessException) {
            return Level.INFO;
        } else if (ex instanceof FatalException) {
            return Level.FATAL;
        } else if (ex instanceof SystemException) {
            return Level.WARN;
        } else {
            return Level.ERROR;
        }
    }
}
