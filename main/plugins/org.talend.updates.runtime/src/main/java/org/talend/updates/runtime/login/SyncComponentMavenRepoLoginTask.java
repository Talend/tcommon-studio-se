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
package org.talend.updates.runtime.login;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.login.AbstractLoginTask;
import org.talend.updates.runtime.maven.MavenRepoSynchronizer;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class SyncComponentMavenRepoLoginTask extends AbstractLoginTask {

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.login.ILoginTask#getOrder()
     */
    @Override
    public Date getOrder() {
        GregorianCalendar gc = new GregorianCalendar(2017, 5, 3, 12, 0, 0);
        return gc.getTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.login.ILoginTask#isCommandlineTask()
     */
    @Override
    public boolean isCommandlineTask() {
        return true; // work for commandline also.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.login.ILoginTask#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        final File tempM2RepoFolder = PathUtils.getComponentsM2TempFolder();
        try {
            if (tempM2RepoFolder != null && tempM2RepoFolder.exists()) {
                MavenRepoSynchronizer synchronizer = new MavenRepoSynchronizer(tempM2RepoFolder);
                synchronizer.sync();
            }
        } finally {
            // clean
            FilesUtils.deleteFolder(tempM2RepoFolder, true);
        }
    }

}
