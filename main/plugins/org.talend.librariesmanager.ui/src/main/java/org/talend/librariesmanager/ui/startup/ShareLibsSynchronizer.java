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
package org.talend.librariesmanager.ui.startup;

import org.eclipse.ui.IStartup;

import java.util.logging.Logger;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.NetworkUtil;
import org.talend.core.prefs.ITalendCorePrefConstants;

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

        // share ci jars anyway
        ShareCILibsJob ciJob = new ShareCILibsJob();
        ciJob.schedule();

        if (shareLibsAtStartup()) {
            ShareLibsJob job = new ShareLibsJob();
            job.schedule();
        }
    }

    public boolean shareLibsAtStartup() {
        boolean ret = ITalendCorePrefConstants.NEXUS_SHARE_LIBS_DEFAULT;
        try {
            IEclipsePreferences node = InstanceScope.INSTANCE.getNode(NetworkUtil.ORG_TALEND_DESIGNER_CORE);
            ret = node.getBoolean(ITalendCorePrefConstants.NEXUS_SHARE_LIBS, ITalendCorePrefConstants.NEXUS_SHARE_LIBS_DEFAULT);
        } catch (Throwable e) {
            ExceptionHandler.process(e);
        }
        LOGGER.info("shareLibsAtStartup: " + ret);
        return ret;
    }
}
