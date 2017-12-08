// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.aether;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbsDynamicProgressMonitor implements IDynamicMonitor {

    private IProgressMonitor progressMonitor;

    public AbsDynamicProgressMonitor(IProgressMonitor progressMonitor) {
        this.progressMonitor = progressMonitor;
    }

    @Override
    public void beginTask(String name, int totalWork) {
        progressMonitor.beginTask(name, totalWork);
    }

    @Override
    public void done() {
        progressMonitor.done();
    }

    @Override
    public void internalWorked(double work) {
        progressMonitor.internalWorked(work);
    }

    @Override
    public boolean isCanceled() {
        return progressMonitor.isCanceled();
    }

    @Override
    public void setCanceled(boolean value) {
        progressMonitor.setCanceled(value);
    }

    @Override
    public void setTaskName(String name) {
        progressMonitor.setTaskName(name);
    }

    @Override
    public void subTask(String name) {
        progressMonitor.subTask(name);
    }

    @Override
    public void worked(int work) {
        progressMonitor.worked(work);
    }

}
