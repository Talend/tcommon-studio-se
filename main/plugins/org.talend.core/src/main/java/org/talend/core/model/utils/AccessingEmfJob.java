// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.model.utils;

import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

/***/
public abstract class AccessingEmfJob extends Job {

    private static final int SLEEP = 100;

    private static final int TIMEOUT = 120000 / SLEEP; // two minutes

    private static int jobs = 0;

    public AccessingEmfJob(String name) {
        super(name);
    }

    public static boolean isAccessed() {
        return jobs > 0;
    }

    public static void waitForJobs() throws InterruptedException, TimeoutException {
        int i = 0;

        while (isAccessed() && i++ < TIMEOUT) {
            Thread.sleep(SLEEP);
        }

        if (isAccessed()) {
            throw new TimeoutException();
        }
    }

    protected final IStatus run(IProgressMonitor monitor) {
        jobs++;

        IStatus status;
        try {
            status = doRun(monitor);
        } catch (RuntimeException e) {
            throw e;
        } finally {
            jobs--;
        }

        return status;
    }

    protected abstract IStatus doRun(IProgressMonitor monitor);

}
