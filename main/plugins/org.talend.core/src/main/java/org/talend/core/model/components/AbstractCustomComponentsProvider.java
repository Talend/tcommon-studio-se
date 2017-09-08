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
package org.talend.core.model.components;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractCustomComponentsProvider extends AbstractComponentsProvider {

    private static Logger logger = Logger.getLogger(AbstractCustomComponentsProvider.class);

    protected FileFilter ff = new FileFilter() {

        @Override
        public boolean accept(File pathname) {
            if (FilesUtils.isSVNFolder(pathname)) {
                return false;
            }
            return true;
        }

    };

    @Override
    public void preComponentsLoad() throws IOException {
        File installationFolder = getInstallationFolder();
        if (installationFolder.exists()) {
            FilesUtils.removeFolder(installationFolder, true);
        }
        org.talend.utils.io.FilesUtils.createFoldersIfNotExists(installationFolder.getAbsolutePath(), false);
        FileFilter ff = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (FilesUtils.isSVNFolder(pathname)) {
                    return false;
                }
                return true;
            }

        };

        // 1. copy old CF components
        // if components in user component path include some shared components , replace it
        File externalComponentsLocation = getExternalComponentsLocation();
        if (externalComponentsLocation != null) {
            if (externalComponentsLocation.exists()) {
                File[] listFiles = externalComponentsLocation.listFiles(ff);
                if (listFiles != null) {
                    for (File f : listFiles) {
                        if (f.isFile() && UpdatesHelper.isComponentUpdateSite(f)) {
                            // have installed already (start product) or will install later(set user component path), so
                            // ignore it.
                            continue;
                        } else if (UpdatesHelper.isOldComponent(f)) {
                            try {
                                FilesUtils.copyFolder(f, new File(installationFolder.getAbsolutePath(), f.getName()), true, ff,
                                        null, true, false);
                            } catch (IOException e) {
                                ExceptionHandler.process(e);
                            }
                        }
                    }
                }

            } else {
                logger.warn(Messages
                        .getString("AbstractComponentsProvider.folderNotExist", externalComponentsLocation.toString())); //$NON-NLS-1$
            }
        }
    }
}
