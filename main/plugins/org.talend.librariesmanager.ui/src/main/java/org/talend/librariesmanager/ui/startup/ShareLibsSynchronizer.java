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

import org.eclipse.ui.IStartup;

import java.util.logging.Logger;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;

/**
 * created by wchen on 2015-6-15 Detailled comment
 *
 */
public class ShareLibsSynchronizer implements IStartup {

    private static final Logger LOGGER = Logger.getLogger(ShareLibsSynchronizer.class.getCanonicalName());

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    @Override
    public void earlyStartup() {
        if (LibrariesManagerUtils.shareLibsAtStartup()) {
            ShareLibsJob job = new ShareLibsJob();
            job.schedule();
        } else {
            LOGGER.info("Skip sharing libraries");
        }
    }
}
