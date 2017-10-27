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

import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionService;

public class ExceptionServiceImpl implements ExceptionService {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.exception.ExceptionService#showExceptionInMessgeBox(java.lang.Throwable)
     */
    @Override
    public void showExceptionInMessgeBox(Throwable ex) {
        MessageBoxExceptionHandler.showMessage(ex, Display.getCurrent().getActiveShell());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.commons.exception.ExceptionService#showMessageForSchemaImportXml(java.lang.Throwable)
     */
    @Override
    public void showMessageForSchemaImportXml(Throwable ex) {
        MessageBoxExceptionHandler.showMessageForSchemaImportXml(ex, Display.getCurrent().getActiveShell());
    }
}
