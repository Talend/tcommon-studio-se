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
package org.talend.librariesmanager.ui.startup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.maven.ArtifactsDeployer;
import org.talend.librariesmanager.maven.ShareLibrareisHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.i18n.Messages;

public class ShareLibsJob extends Job {

    private final String TYPE_NEXUS = "nexus";

    private final String TYPE_SVN = "svn";

    public ShareLibsJob() {
        super("");
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        ShareLibsOnStartup shareLib = new ShareLibsOnStartup();
        return shareLib.shareLibs(this, monitor);
    }

    /**
     * Share libs from local maven to svn lib or nexus server depends on TAC setup
     */
    class ShareLibsOnStartup extends ShareLibrareisHelper {

        @Override
        public Map<ModuleNeeded, File> getFilesToShare(IProgressMonitor monitor) {
            Map<ModuleNeeded, File> files = new HashMap<ModuleNeeded, File>();
            SubMonitor mainSubMonitor = SubMonitor.convert(monitor, 1);
            mainSubMonitor.setTaskName(Messages.getString("ShareLibsJob.getFilesToShare")); //$NON-NLS-1$
            final List<ModuleNeeded> modulesNeeded = new ArrayList<ModuleNeeded>(ModulesNeededProvider.getModulesNeeded());
            ILibraryManagerService librariesService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            Set<String> filePaths = new HashSet<String>();
            for (ModuleNeeded module : modulesNeeded) {
                if (monitor.isCanceled()) {
                    return null;
                }

                final String jarPathFromMaven = librariesService.getJarPathFromMaven(module.getMavenUri(true));
                if (jarPathFromMaven == null) {
                    continue;
                }
                File jarFile = new File(jarPathFromMaven);
                if (jarFile.exists()) {
                    if (!filePaths.contains(jarPathFromMaven)) {
                        files.put(module, jarFile);
                    }
                    filePaths.add(jarPathFromMaven);
                }
            }
            mainSubMonitor.worked(1);
            return files;
        }

        @Override
        public void deployToLocalMaven(ArtifactsDeployer deployer, File jarFile, ModuleNeeded module) throws Exception {
            // nothing to do
        }

    }
}
