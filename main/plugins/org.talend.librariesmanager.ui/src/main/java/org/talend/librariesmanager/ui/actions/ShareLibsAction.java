// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
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

import org.eclipse.jface.action.Action;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
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
        this.setImageDescriptor(ImageProvider.getImageDesc(ECoreImage.IMPORT_JAR));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        ShareLibsJob job = new ShareLibsJob();
        job.schedule();
    }

}
