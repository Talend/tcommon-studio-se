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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class TimeoutTask.
 */
public class TimeoutTask<R> {

    private AtomicInteger counter = new AtomicInteger();

    /**
     * TimeoutTask constructor comment.
     */
    public TimeoutTask() {
        this(null);
    }

    /**
     * TimeoutTask constructor comment.
     */
    public TimeoutTask(final String taskName) {
        super();
        if (taskName != null) {
            ThreadFactory threadFactory = new ThreadFactory() {

                ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();

                public Thread newThread(Runnable r) {
                    Thread newThread = defaultThreadFactory.newThread(r);
                    String threadName = TimeoutTask.this.getClass().getSimpleName() + (taskName != null ? "_" + taskName : "")
                            + "_" + newThread.getName();
                    newThread.setName(threadName);
                    return newThread;
                }

            };
            threadPool = Executors.newCachedThreadPool(threadFactory);
        }

    }

    private ExecutorService threadPool;

    /**
     * Method "run" .
     *
     * @param task
     * @param timeoutMs
     * @param cancelTaskIfTimeout
     * @param interruptIfTimeout
     * @return
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws ExecutionException
     * @deprecated Use {@link #run(Callable<R>,long,boolean)} instead
     */
    public FutureTask<R> run(Callable<R> task, long timeoutMs, boolean cancelTaskIfTimeout, boolean interruptIfTimeout)
            throws TimeoutException, InterruptedException, ExecutionException {
                return run(task, timeoutMs, cancelTaskIfTimeout);
            }

    /**
     * Method "run" .
     *
     * @param task
     * @param timeoutMs
     * @param cancelTaskIfTimeout
     * @return
     * @throws TimeoutException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public FutureTask<R> run(Callable<R> task, long timeoutMs, boolean cancelTaskIfTimeout)
            throws TimeoutException, InterruptedException, ExecutionException {
        FutureTask<R> futureTask = new FutureTask<R>(task);
        run(futureTask, timeoutMs, cancelTaskIfTimeout);
        return futureTask;
    }

    /**
     * Method "run".
     *
     * @param futureTask
     * @param timeoutMs
     * @param cancelAndInterruptTaskIfTimeout
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    private R run(FutureTask<R> futureTask, long timeoutMs, boolean cancelAndInterruptTaskIfTimeout)
            throws InterruptedException, ExecutionException, TimeoutException {
        threadPool.execute(futureTask);
        try {
            futureTask.get(timeoutMs, TimeUnit.MILLISECONDS);
        } finally {
            if (cancelAndInterruptTaskIfTimeout) {
                futureTask.cancel(true);
            }
        }
        return futureTask.get();
    }

    public void release() {
        threadPool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                threadPool.awaitTermination(10, TimeUnit.SECONDS);
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            threadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }
}
