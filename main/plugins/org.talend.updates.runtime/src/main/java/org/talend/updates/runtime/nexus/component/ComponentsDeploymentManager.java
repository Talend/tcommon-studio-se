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
package org.talend.updates.runtime.nexus.component;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

/**
 *
 * created by ycbai on 2017年5月23日 Detailled comment
 *
 */
public class ComponentsDeploymentManager {

    public boolean deployComponentsToLocalNexus(IProgressMonitor progress, File componentZipFile) throws IOException {
        ArtifactRepositoryBean localNexusServer = NexusServerManager.getInstance().getLocalNexusServer();
        if (localNexusServer == null) {
            return false;
        }
        NexusShareComponentsManager nexusShareComponentsManager = new NexusShareComponentsManager(localNexusServer);
        if (nexusShareComponentsManager.getNexusTransport().isAvailable()) {
            boolean deployed = nexusShareComponentsManager.deployComponent(progress, componentZipFile);
            if (deployed) {
                moveToSharedFolder(componentZipFile);
                return true;
            }
        }
        return false;
    }

    private void moveToSharedFolder(File componentZipFile) throws IOException {
        File sharedCompFile = new File(PathUtils.getComponentsSharedFolder(), componentZipFile.getName());
        if (!componentZipFile.equals(sharedCompFile)) { // not in same folder
            FilesUtils.copyFile(componentZipFile, sharedCompFile);
            boolean deleted = componentZipFile.delete();
            if (!deleted) {// failed to delete in time
                componentZipFile.deleteOnExit(); // try to delete when exit
            }
        }
    }

}
