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
package org.talend.metadata.managment.ui.wizard.documentation;

import org.eclipse.core.runtime.IPath;

/**
 * DOC chuger class global comment. Detailled comment <br/>
 * 
 * $Id: IDocumentationContext.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public interface IDocumentationContext {

    IPath getDocFilePath();

    void setDocFilePath(IPath docFilePath);

    boolean isDocNameEditable();

    boolean isDocVersionEditable();

    String getDocOriginalName();

    void setDocOriginalName(String docOriginalName);

    String getDocOriginalExtension();

    void setDocOriginalExtension(String docOriginalExtension);
}
