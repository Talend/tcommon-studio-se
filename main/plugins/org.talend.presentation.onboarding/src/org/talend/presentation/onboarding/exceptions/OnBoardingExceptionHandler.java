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
package org.talend.presentation.onboarding.exceptions;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * created by cmeng on Sep 18, 2015 Detailled comment
 *
 */
public final class OnBoardingExceptionHandler {

    private static Logger log = Logger.getLogger(OnBoardingExceptionHandler.class);

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

    public static Priority getPriority(Throwable ex) {
        return Level.ERROR;
    }
}
