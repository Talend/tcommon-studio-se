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
package org.talend.librariesmanager.ui.startup;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

/**
 * created by wchen on 2015年6月15日 Detailled comment
 *
 */
public class ShareLibsJob extends Job {

    private final String TYPE_NEXUS = "nexus";

    private final String TYPE_SVN = "svn";

    /**
     * DOC Talend ShareLibsJob constructor comment.
     * 
     * @param name
     */
    public ShareLibsJob() {
        super("");
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected IStatus run(IProgressMonitor monitor) {
        ShareMavenArtifactsOnStartup shareLib = new ShareMavenArtifactsOnStartup();
        return shareLib.shareLibs(this, monitor);
    }

}
