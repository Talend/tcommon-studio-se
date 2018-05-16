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
package org.talend.designer.runprocess;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: ProcessorException.java 1 2006-09-29 17:06:40 +0000 (鏄熸湡浜� 29 涔?鏈�2006) nrousseau $
 * 
 */
public class ProcessorException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * DOC chuger ProcessorException constructor comment.
     */
    public ProcessorException() {
    }

    /**
     * DOC chuger ProcessorException constructor comment.
     * 
     * @param arg0
     */
    public ProcessorException(String arg0) {
        super(arg0);
    }

    /**
     * DOC chuger ProcessorException constructor comment.
     * 
     * @param arg0
     */
    public ProcessorException(Throwable arg0) {
        super(arg0);
    }

    /**
     * DOC chuger ProcessorException constructor comment.
     * 
     * @param arg0
     * @param arg1
     */
    public ProcessorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
