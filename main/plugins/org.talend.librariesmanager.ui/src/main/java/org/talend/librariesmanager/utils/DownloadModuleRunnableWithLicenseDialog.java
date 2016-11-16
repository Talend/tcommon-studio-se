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
package org.talend.librariesmanager.utils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.librariesmanager.ui.dialogs.ModuleLicenseDialog;

public class DownloadModuleRunnableWithLicenseDialog extends DownloadModuleRunnable {

    private final Shell shell;

    public DownloadModuleRunnableWithLicenseDialog(List<ModuleToInstall> toDownload, Shell shell) {
        super(toDownload);
        this.shell = shell;

    }

    @Override
    protected boolean acceptLicence(final ModuleToInstall module) {
        final AtomicBoolean accepted = new AtomicBoolean(false);// just use atomic to have a final object.
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                String licenseUrl = module.getLicenseUrl();
                ModuleLicenseDialog licenseDialog = new ModuleLicenseDialog(shell, module.getLicenseType(), licenseUrl, module
                        .getName());
                if (licenseDialog.open() != Window.OK) {
                    downloadFailed.add(module.getName());
                    accepted.set(false);
                } else {
                    accepted.set(true);
                }
            }
        });
        return accepted.get();
    }

}
