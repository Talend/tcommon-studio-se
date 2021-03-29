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
package org.talend.librariesmanager.ui.startup;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

/*
* Created by bhe on Dec 24, 2020
*/
public class ShareCILibsJob extends Job {

    /**
     * @param name
     */
    public ShareCILibsJob() {
        super("");
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        ShareCIJarsOnStartup task = new ShareCIJarsOnStartup();
        return task.shareLibs(this, monitor);
    }

}
