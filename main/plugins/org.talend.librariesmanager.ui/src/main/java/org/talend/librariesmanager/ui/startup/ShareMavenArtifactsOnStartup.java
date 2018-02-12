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
package org.talend.librariesmanager.ui.startup;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.librariesmanager.maven.ShareLibrareisHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.utils.io.FilesUtils;

/**
 * Share libs from local maven to svn lib or nexus server depends on TAC setup, created by wchen on 2015年7月31日 Detailled
 * comment
 *
 */
public class ShareMavenArtifactsOnStartup extends ShareLibrareisHelper {

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

            final String jarPathFromMaven = librariesService.getJarPathFromMaven(module.getMavenUri());
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.librariesmanager.utils.ShareLibrareisHelper#deployToLocalMavenOnExist(java.io.File,
     * org.talend.core.model.general.ModuleNeeded)
     */
    @Override
    public void shareToRepository(File file, MavenArtifact artifact) throws Exception {
        String name = file.getName();
        int indexOf = name.lastIndexOf(".");
        String pomPath = file.getParent();
        if (indexOf != -1) {
            pomPath = pomPath + "/" + name.substring(0, indexOf) + "." + MavenConstants.PACKAGING_POM;
        } else {
            pomPath = pomPath + name + "." + MavenConstants.PACKAGING_POM;
        }
        File pomFile = new File(pomPath);
        if (!pomFile.exists()) {
            File generatedPom = new File(PomUtil.generatePom(artifact));
            FilesUtils.copyFile(generatedPom, pomFile);
            FilesUtils.deleteFolder(generatedPom.getParentFile(), true);
        }
        deployer.deploy(file, artifact);
        // artifact.setType(MavenConstants.PACKAGING_POM);
        // deployer.deploy(pomFile, artifact);
    }

}
