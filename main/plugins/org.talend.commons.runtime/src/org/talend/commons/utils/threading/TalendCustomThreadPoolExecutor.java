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
package org.talend.commons.utils.threading;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * cli class global comment. Detailled comment
 */
public class TalendCustomThreadPoolExecutor extends ThreadPoolExecutor {

    public TalendCustomThreadPoolExecutor(int queueCapacity) {
        super(5, 10, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueCapacity));
    }

    public TalendCustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public TalendCustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public TalendCustomThreadPoolExecutor(int queueCapacity, RejectedExecutionHandler handler) {
        super(5, 10, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueCapacity), handler);
    }

    public boolean hasThreadRunning() {
        return getQueue().size() != 0;
    }

    /**
     * 
     * cli Comment method "clearThreads".
     * 
     * (bug 6976)
     */
    public void clearThreads() {
        try {
            shutdownNow();
            shutdown();
            // purge();
        } catch (SecurityException e) {
            e.printStackTrace(); // only for debug
        }
    }
}
