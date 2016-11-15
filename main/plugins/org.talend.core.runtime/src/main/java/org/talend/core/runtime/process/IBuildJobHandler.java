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
package org.talend.core.runtime.process;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.designer.runprocess.IProcessor;

public interface IBuildJobHandler {

    public IProcessor generateJobFiles(IProgressMonitor monitor) throws Exception;

    public void generateTestReports(IProgressMonitor monitor) throws Exception;

    public void generateItemFiles(boolean withDependencies, IProgressMonitor monitor) throws Exception;

    public void build(IProgressMonitor monitor) throws Exception;

    public IFile getJobTargetFile();

}
