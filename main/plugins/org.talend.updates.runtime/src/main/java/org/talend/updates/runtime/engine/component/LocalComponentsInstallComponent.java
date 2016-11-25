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
package org.talend.updates.runtime.engine.component;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.FatalException;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.updates.runtime.engine.P2Installer;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class LocalComponentsInstallComponent implements ComponentsInstallComponent {

    private boolean needRelaunch;

    public boolean needRelaunch() {
        return needRelaunch;
    }

    private void reset() {
        needRelaunch = false;
    }

    /**
     * If have patches to be installed, will ask restart.
     */
    public boolean install() {
        if (Platform.inDevelopmentMode()) { // for dev, no need install patches.
            return false;
        }
        reset();

        try {
            // FIXME For Test install the new components
            final IPath componentsPath = new Path(Platform.getConfigurationLocation().getURL().getPath()).append("components");
            final File file = componentsPath.toFile();
            if (file.exists() && file.isDirectory()) {
                boolean success = false;
                final File[] updateFiles = UpdatesHelper.findUpdateFiles(file);
                if (updateFiles != null && updateFiles.length > 0) {
                    for (File f : updateFiles) {
                        if (f.isFile()) {
                            if (installUpdateSiteComponent(f)) {
                                success = true;
                            }
                        }
                    }
                }
                if (success) { // have one component install ok.
                    needRelaunch = true;
                }
                return true;
            }
        } catch (Exception e) {
            // make sure to popup error dialog for studio
            ExceptionHandler.process(new FatalException(e));
        }
        return false;
    }

    protected boolean installUpdateSiteComponent(File localZipFile) {
        final File tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("components", "updatesite");
        try {
            boolean success = false;
            FilesUtils.unzip(localZipFile.getAbsolutePath(), tmpFolder.getAbsolutePath());

            final File[] updateFiles = UpdatesHelper.findUpdateFiles(tmpFolder);
            if (updateFiles != null && updateFiles.length > 0) {
                P2Installer installer = new P2Installer();
                for (File f : updateFiles) {
                    if (UpdatesHelper.isUpdateSite(f)) {
                        String version = installer.installPatchesByP2(f, false);
                        if (version != null) {
                            success = true;
                        }
                    }
                }
            }
            return success;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            FilesUtils.deleteFolder(tmpFolder, true);
        }
        return false;
    }
}
