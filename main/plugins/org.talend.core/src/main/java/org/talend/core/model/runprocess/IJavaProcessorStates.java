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
package org.talend.core.model.runprocess;

import org.eclipse.core.runtime.IPath;

/**
 * There are two kinds of java project status, one is editor status for the use of Talend java editor and another is run
 * time status for the use of run process.
 */
public interface IJavaProcessorStates {

    public IPath getCodePath();

    public IPath getContextPath();

    public IPath getDataSetPath();

}
