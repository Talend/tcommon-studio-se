// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

public class ProcessorException extends Exception {

    private static final long serialVersionUID = 1L;

    public ProcessorException() {
    }

    public ProcessorException(String arg0) {
        super(arg0);
    }

    public ProcessorException(Throwable arg0) {
        super(arg0);
    }

    public ProcessorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}
