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
package org.talend.commons.runtime.service;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface PatchComponent extends P2InstallComponent {

    static final String FOLDER_PATCHES = "patches"; //$NON-NLS-1$

    boolean install(IProgressMonitor monitor, File patchFile) throws Exception;

    boolean isPlainZipInstalled(IProgressMonitor monitor, String patchName) throws Exception;
}
