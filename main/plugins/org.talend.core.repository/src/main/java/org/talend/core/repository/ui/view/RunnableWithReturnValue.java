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
package org.talend.core.repository.ui.view;

import org.eclipse.core.resources.IWorkspaceRunnable;

/**
 * DOC hqzhang class global comment. Detailled comment
 */
public abstract class RunnableWithReturnValue implements IWorkspaceRunnable {

    public RunnableWithReturnValue(String taskName) {
        this.taskName = taskName;
    }

    private String taskName;

    private Object returnValue;

    public Object getReturnValue() {
        return this.returnValue;
    }

    protected void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    protected String getTaskName() {
        return this.taskName;
    }

    protected void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
