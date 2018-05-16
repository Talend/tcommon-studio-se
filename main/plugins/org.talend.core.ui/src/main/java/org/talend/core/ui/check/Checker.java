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
package org.talend.core.ui.check;

import org.eclipse.core.runtime.IStatus;

/**
 * 
 * created by ycbai on 2015年10月9日 Detailled comment
 *
 */
public class Checker implements IChecker {

    private ICheckListener listener;

    private int statusLevel = IStatus.OK;

    private String status;

    @Override
    public void setListener(ICheckListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateStatus(int level, final String statusLabelText) {
        this.statusLevel = level;
        if (statusLabelText != null) {
            this.status = statusLabelText;
        }
        if (listener != null) {
            listener.checkPerformed(this);
        }
    }

    @Override
    public boolean isStatusOnError() {
        return this.statusLevel == IStatus.ERROR;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public int getStatusLevel() {
        return this.statusLevel;
    }

}
