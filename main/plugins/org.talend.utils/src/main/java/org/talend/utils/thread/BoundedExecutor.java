// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * BoundedExecutor. Useful to bound the executor at the bound value.
 */
public class BoundedExecutor {

    private final ExecutorService exec;

    private final Semaphore semaphore;

    public BoundedExecutor(ExecutorService exec, int bound) {
        this.exec = exec;
        this.semaphore = new Semaphore(bound);
    }

    /**
     *
     * BoundedExecutor constructor.
     *
     * @param poolName for the pool to create internally
     * @param bound
     */
    public BoundedExecutor(String poolName, int bound) {
        this.exec = intializeBoundedPool(poolName, bound);
        this.semaphore = new Semaphore(bound);
    }

    protected ThreadPoolExecutor intializeBoundedPool(final String poolName, int poolSize) {
        LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 0L, TimeUnit.SECONDS, workQueue,
                new ThreadFactory() {

                    ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

                    public Thread newThread(Runnable r) {
                        Thread newThread = defaultThreadFactory.newThread(r);
                        newThread.setName(poolName + "_" + newThread.getName());
                        return newThread;
                    }

                });
        return threadPoolExecutor;
    }

    public void submitTask(final Runnable command) throws InterruptedException, RejectedExecutionException {
        semaphore.acquire();
        try {
            exec.execute(new Runnable() {

                public void run() {
                    try {
                        command.run();
                    } finally {
                        semaphore.release();
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            semaphore.release();
            throw e;
        }
    }

    /**
     * Getter for exec.
     *
     * @return the exec
     */
    public ExecutorService getExecutorService() {
        return exec;
    }

}
