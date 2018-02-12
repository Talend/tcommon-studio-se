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

import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.talend.commons.CommonsPlugin;

/**
 * Implementation of exception handling strategy.<br/>
 * 
 * $Id: ExceptionHandler.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ExceptionHandler {

    /**
     * Log message relative to ex param. Log level depends on exception type.
     * 
     * @param ex - exception to log
     */
    public static void process(Throwable ex) {
        Priority priority = CommonExceptionHandler.getPriority(ex);
        process(ex, priority);
    }

    public static void log(String message) {
        CommonExceptionHandler.log(message);
    }

    public static void process(Throwable ex, Priority priority) {
        CommonExceptionHandler.process(ex, priority);

        if (priority == Level.FATAL && !CommonsPlugin.isHeadless()) {
            ExceptionService service = CommonsPlugin.getDefault().getExceptionService();
            if (service != null) {
                service.showExceptionInMessgeBox(ex);
            }
        }
    }

    /**
     * bug 17654: import the xml file as the schema will throw error.
     * 
     * DOC yhch Comment method "processForSchemaImportXml".
     * 
     * @param ex
     */
    public static void processForSchemaImportXml(Throwable ex) {

        ExceptionService service = CommonsPlugin.getDefault().getExceptionService();
        if (service != null) {
            service.showMessageForSchemaImportXml(ex);
        }
    }
}
