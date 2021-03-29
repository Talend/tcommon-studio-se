// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.tools.extension;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * This interface provides capabilities for third party bundles to contribute to the maven pom which is used to create
 * jobs jar files.
 *
 * @See {@link PomExtensionRegistry}
 * @author jclaude
 */
public interface IJobPomExtension {

    String KEY_PROCESS = "PROCESS"; //$NON-NLS-1$

    String KEY_ASSEMBLY_FILE = "ASSEMBLY_FILE"; //$NON-NLS-1$

    String KEY_CHILDREN_JOBS_GROUP_IDS = "KEY_CHILDREN_JOBS_GROUP_IDS"; //$NON-NLS-1$

    void updatePom(IProgressMonitor monitor, IFile pomFile, Map<String, Object> args);

}
