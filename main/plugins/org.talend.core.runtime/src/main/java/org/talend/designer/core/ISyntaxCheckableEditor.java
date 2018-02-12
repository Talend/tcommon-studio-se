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
package org.talend.designer.core;

/**
 * 
 * If the syntax of the text in the editor used in the multiple page Talend editor need to be checked, this interface
 * should be implemented.
 * 
 * For example the condition below: When switch the tab from the designer to code in the multiple page Talend editor,
 * the syntax of the code need to be validated by calling the method validateSyntax.
 * 
 * yzhang class global comment. Detailled comment <br/>
 * 
 * 
 * $Id: ICheckable.java 1 2007-1-18 涓嬪?�3:52:46 +0000 (涓嬪?�3:52:46, 2007-1-18 2007) yzhang $
 * 
 */
public interface ISyntaxCheckableEditor {

    /**
     * Validate the syntax of the code.
     * 
     * DOC yzhang Comment method "checkCode".
     */
    public void validateSyntax();

    /**
     * To see if this editor had been disposed.
     * 
     * DOC yzhang Comment method "isDisposed".
     * 
     * @return
     */
    public boolean isDisposed();

}
