// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.ui.startup.ShareLibsJob;


/*
* Created by bhe on Dec 17, 2020
*/
public class ShareLibsAction extends Action {

    public ShareLibsAction() {
        super();
        this.setText(Messages.getString("Module.view.sharelibsAction.title")); //$NON-NLS-1$
        this.setDescription(Messages.getString("Module.view.sharelibsAction.title")); //$NON-NLS-1$
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.SHARE_LIBS));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {

        ShareLibsJob job = new ShareLibsJob();
        job.addJobChangeListener(new IJobChangeListener() {

            @Override
            public void scheduled(IJobChangeEvent event) {
                ShareLibsAction.this.setEnabled(false);
            }

            @Override
            public void running(IJobChangeEvent event) {
                ShareLibsAction.this.setEnabled(false);
            }

            @Override
            public void done(IJobChangeEvent event) {
                ShareLibsAction.this.setEnabled(true);
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        MessageDialog.open(MessageDialog.INFORMATION, Display.getDefault().getActiveShell(), "",
                                Messages.getString("Module.view.sharelibsAction.info"), SWT.NONE);
                    }
                });
            }

            @Override
            public void awake(IJobChangeEvent event) {
                ShareLibsAction.this.setEnabled(false);
            }

            @Override
            public void aboutToRun(IJobChangeEvent event) {
                ShareLibsAction.this.setEnabled(false);
            }

            @Override
            public void sleeping(IJobChangeEvent event) {
                ShareLibsAction.this.setEnabled(false);
            }
        });
        job.schedule();
    }

    public boolean show() {
        boolean ret = false;
        try {
            ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager
                    .getRepositoryHandler(customNexusServer);
            if (customerRepHandler != null) {
                ret = customerRepHandler.checkConnection();
            }
        } catch (Exception e) {

        }
        return ret;
    }

}
