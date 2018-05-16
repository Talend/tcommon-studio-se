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
package org.talend.core.model.update;

import org.talend.core.runtime.i18n.Messages;

/**
 * ggu class global comment. Detailled comment
 */
public enum EUpdateResult {
    BUIL_IN(Messages.getString("EUpdateResult.BuiltIn")), //$NON-NLS-1$
    UPDATE(Messages.getString("EUpdateResult.Update")), //$NON-NLS-1$
    RENAME(Messages.getString("EUpdateResult.Rename")), //$NON-NLS-1$
    RELOAD(Messages.getString("EUpdateResult.Reload")), //$NON-NLS-1$
    ADD(Messages.getString("EUpdateResult.Add")), //$NON-NLS-1$
    DELETE(Messages.getString("EUpdateResult.Delete")), //$NON-NLS-1$
    // for mapper
    PATH_UPDATE(Messages.getString("EUpdateResult.PathUpdate")), //$NON-NLS-1$
    STRUCTURE_PATH_UPDATE(Messages.getString("EUpdateResult.StructurePathUpdate")), //$NON-NLS-1$
    // for joblet
    JOBLET_UPDATE(Messages.getString("EUpdateResult.JobletUpdate")), //$NON-NLS-1$
    // for test container
    JOB_UPDATE(Messages.getString("EUpdateResult.JobUpdate")); //$NON-NLS-1$

    private String displayName;

    private EUpdateResult(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return this.toString();
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
