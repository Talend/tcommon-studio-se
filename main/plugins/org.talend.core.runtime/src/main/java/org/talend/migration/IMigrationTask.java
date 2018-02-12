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
package org.talend.migration;

import java.util.Date;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public interface IMigrationTask {

    public String getId();

    public void setId(String id);

    public String getName();

    public void setName(String name);

    /**
     * DOC ycbai Comment method "getVersion". This is the Talend version when this migration task was introduced.
     * 
     * @return
     */
    public String getVersion();

    public void setVersion(String version);

    /**
     * DOC ycbai Comment method "getBreaks". this is the version of the latest version of Studio that will not be able
     * to load the data migrated with the current task. default value is considered : 5.1.9
     * 
     * @return
     */
    public String getBreaks();

    public void setBreaks(String breaks);

    public void setStatus(ExecutionResult status);

    public ExecutionResult getStatus();

    // Use to manage task order. Tasks are sorted (ASC) by this date, then executed following this order.
    public Date getOrder();

    // In case the migration is not used anymore, it won't execute the migration at all, but it will keep in list for
    // already migrated
    public boolean isDeprecated();

    /**
     * Represents the execution status of this task.
     */
    public enum ExecutionResult {
        SUCCESS_WITH_ALERT, // task successfully done, will be show to user in summary pop-up
        SUCCESS_NO_ALERT, // task successfully done, will NOT be show to user in summary pop-up (only in error log)
        NOTHING_TO_DO, // nothing to do in the current context, will NOT be show to user in summary pop-up neither in
        // error log
        FAILURE, // task failed (stacktrace in error log). will be retry on next login
        SKIPPED; // task not failed, not show to user in summary pop-up. will be retry on next login
    }

}
