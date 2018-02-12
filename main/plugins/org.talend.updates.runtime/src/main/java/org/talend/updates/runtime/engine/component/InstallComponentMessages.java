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
package org.talend.updates.runtime.engine.component;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.osgi.framework.FrameworkUtil;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class InstallComponentMessages {

    private String installedMessage;

    private String failureMessage;

    private MultiStatus multiStatus;

    private boolean needRestart = false;

    public InstallComponentMessages() {
        reset();
    }

    public void reset() {
        installedMessage = null;
        failureMessage = null;
        multiStatus = new MultiStatus(FrameworkUtil.getBundle(this.getClass()).getSymbolicName(), IStatus.OK, null, null);
        needRestart = false;
    }

    public boolean isNeedRestart() {
        return needRestart;
    }

    public void setNeedRestart(boolean needRestart) {
        if (needRestart) { // only true, will be set, means keep restart flag always
            this.needRestart = needRestart;
        }
    }

    public String getInstalledMessage() {
        return installedMessage;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void analyzeStatus(IStatus status) {
        if (status == null) {
            return;
        }
        multiStatus.merge(status);

        switch (status.getSeverity()) {
        case IStatus.ERROR:
        case IStatus.CANCEL:
            failureMessage = StringUtils.trimToEmpty(failureMessage).concat(System.lineSeparator()).concat(status.getMessage());
            break;
        default:
            installedMessage = StringUtils.trimToEmpty(installedMessage).concat(System.lineSeparator())
                    .concat(status.getMessage());
            break;
        }
    }

    public boolean isOk() {
        // have set the status
        if (multiStatus.getChildren() != null && multiStatus.getChildren().length > 0) {
            return multiStatus.isOK();
        }
        return false;
    }
}
