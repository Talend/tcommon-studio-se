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
package org.talend.designer.core;

/**
 * If the syntax of the text in the editor used in the multiple page Talend editor need to be checked, this interface
 * should be implemented.
 * 
 * For example the condition below: When switch the tab from the designer to code in the multiple page Talend editor,
 * the syntax of the code need to be validated by calling the method validateSyntax.
 */
public interface ISyntaxCheckableEditor {

    /**
     * Validate the syntax of the code.
     */
    public void validateSyntax();

    /**
     * To see if this editor had been disposed.
     * 
     * @return
     */
    public boolean isDisposed();

}
