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
package org.talend.core.runtime.repository.build;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC ggu class global comment. Detailled comment
 */
public interface IMavenPomCreator {

    void create(IProgressMonitor monitor) throws Exception;

    // only need to syncCodesPoms for main job
    void setSyncCodesPoms(boolean isMainJob);
}
