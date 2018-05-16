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

/**
 * created by root on 16 janv. 2013 Detailled comment
 * 
 */
public interface ExceptionService {

    public void showExceptionInMessgeBox(Throwable ex);

    public void showMessageForSchemaImportXml(Throwable ex);

}
