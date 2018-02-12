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
package org.talend.core.runtime.repository.build;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IBuildPomCreatorParameters {

    static final String PROCESSOR = "Processor"; //$NON-NLS-1$

    static final String FILE_POM = "PomFile"; //$NON-NLS-1$

    static final String FILE_ASSEMBLY = "AssemblyFile"; //$NON-NLS-1$

    static final String CP_WIN = "WindowsClasspath"; //$NON-NLS-1$

    static final String CP_LINUX = "LinuxClasspath"; //$NON-NLS-1$

    static final String ARGUMENTS_MAP = "ArgumentsMap"; //$NON-NLS-1$

    static final String OVERWRITE_POM = "OverwritePom"; //$NON-NLS-1$
}
