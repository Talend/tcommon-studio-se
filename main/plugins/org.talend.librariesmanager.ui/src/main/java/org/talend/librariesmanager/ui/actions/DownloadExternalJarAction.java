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
package org.talend.librariesmanager.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialogWithProgress;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by Administrator on 2012-9-20 Detailled comment
 * 
 */
public class DownloadExternalJarAction extends Action {

    public DownloadExternalJarAction() {
        super();
        this.setText(Messages.getString("Module.view.download.external.modules.action.text"));
        this.setDescription(Messages.getString("Module.view.download.external.modules.action.description"));
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DOWNLOAD_MODULE));
    }

    @Override
    public void run() {
        String title = Messages.getString("download.external.dialog.title");
        String text = Messages.getString("download.external.dialog.desciption");
        List<ModuleNeeded> updatedModules = new ArrayList<ModuleNeeded>();
        for (ModuleNeeded neededModule : ModulesNeededProvider.getModulesNeeded()) {
            if (neededModule.getStatus() != ELibraryInstallStatus.NOT_INSTALLED) {
                continue;
            }
            updatedModules.add(neededModule);
        }
        ExternalModulesInstallDialogWithProgress dialog = new ExternalModulesInstallDialogWithProgress(PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getShell(), text, title);
        dialog.showDialog(true, updatedModules);
    }

}
