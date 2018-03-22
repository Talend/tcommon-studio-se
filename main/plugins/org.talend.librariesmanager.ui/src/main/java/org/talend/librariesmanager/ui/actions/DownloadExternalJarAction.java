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
package org.talend.librariesmanager.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.EImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialogWithProgress;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.ui.views.ModulesView;

/**
 * created by Administrator on 2012-9-20 Detailled comment
 * 
 */
public class DownloadExternalJarAction extends Action {
	
	private ModulesView parentView = null;

    public DownloadExternalJarAction(ModulesView parentView) {
        super();
        this.setText(Messages.getString("Module.view.download.external.modules.action.text"));
        this.setDescription(Messages.getString("Module.view.download.external.modules.action.description"));
        this.setImageDescriptor(ImageProvider.getImageDesc(EImage.DOWNLOAD_MODULE));
        this.parentView = parentView;
    }

    @Override
    public void run() {
        String title = Messages.getString("download.external.dialog.title");
        String text = Messages.getString("download.external.dialog.desciption");
        List<ModuleNeeded> updatedModules = getUpdatedModules();
        if(updatedModules.isEmpty()){
        	MessageDialog.openWarning(parentView.getSite().getShell(), Messages.getString("download.external.dialog.warning"), 
        			Messages.getString("download.external.dialog.message"));
        	return;
        }
        ExternalModulesInstallDialogWithProgress dialog = new ExternalModulesInstallDialogWithProgress(PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getShell(), text, title);
        dialog.showDialog(true, updatedModules);
    }
    
    private List<ModuleNeeded> getUpdatedModules(){
    	List<ModuleNeeded> updatedModules = new ArrayList<ModuleNeeded>();
    	TableItem [] items = parentView.getSelection();
    	for(TableItem item : items){
    		if((item.getData() instanceof ModuleNeeded) && ((ModuleNeeded)item.getData()).getStatus() == ELibraryInstallStatus.NOT_INSTALLED){
    			updatedModules.add((ModuleNeeded)item.getData());
    		}
    	}
    	return updatedModules;
    }

}
