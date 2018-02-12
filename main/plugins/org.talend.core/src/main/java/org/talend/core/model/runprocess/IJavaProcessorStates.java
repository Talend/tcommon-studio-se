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
package org.talend.core.model.runprocess;

import org.eclipse.core.runtime.IPath;

/**
 * There are two kinds of java project status, one is editor status for the use of Talend java editor and another is run
 * time status for the use of run process.
 * 
 * 
 * DOC yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: JavaStatus.java JavaStatus 2007-1-23 下午05:12:45 +0000 (下午05:12:45, 2007-1-23 2007) yzhang $
 * 
 */
public interface IJavaProcessorStates {

    /**
     * Return the code path.
     * 
     * DOC yzhang Comment method "getCodePath".
     */
    public IPath getCodePath();

    /**
     * Return the context path.
     * 
     * DOC yzhang Comment method "getContextPath".
     */
    public IPath getContextPath();

    /**
     * Return the context path.
     * 
     * DOC yzhang Comment method "getDataSetPath".
     */
    public IPath getDataSetPath();

}
