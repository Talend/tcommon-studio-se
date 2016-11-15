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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.talend.core.model.process.IContext;
import org.talend.designer.runprocess.ProcessorException;

public interface IJavaProcessor {

    public void initPaths(IContext context) throws ProcessorException;

    public void generateCode(IContext context, boolean statistics, boolean trace, boolean javaProperties)
            throws ProcessorException;

    public String getJavaContext();

    public IPath getCodePath();

    public IPath getContextPath();

    public IProject getJavaProject();

    public int getLineNumber(String nodeName);

}
