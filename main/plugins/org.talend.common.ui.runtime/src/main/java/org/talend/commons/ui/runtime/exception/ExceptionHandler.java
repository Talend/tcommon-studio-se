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

import org.apache.log4j.Priority;

/**
 * Implementation of exception handling strategy.<br/>
 * 
 * $Id: ExceptionHandler.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 * @deprecated use {@link org.talend.commons.exception.ExceptionHandler} instead
 */
public final class ExceptionHandler {

    /**
     * Log message relative to ex param. Log level depends on exception type.
     * 
     * @param ex - exception to log
     */
    public static void process(Throwable ex) {
        org.talend.commons.exception.ExceptionHandler.process(ex);
    }

    public static void log(String message) {
        org.talend.commons.exception.ExceptionHandler.log(message);
    }

    public static void process(Throwable ex, Priority priority) {
        org.talend.commons.exception.ExceptionHandler.process(ex, priority);
    }

    /**
     * bug 17654: import the xml file as the schema will throw error.
     * 
     * DOC yhch Comment method "processForSchemaImportXml".
     * 
     * @param ex
     * 
     */
    public static void processForSchemaImportXml(Throwable ex) {
        org.talend.commons.exception.ExceptionHandler.processForSchemaImportXml(ex);
    }
}
