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
package org.talend.commons.utils.threading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 
 * Limit the execution of threads by verifying at call of <code>startIfExecutable</code> if the process can be
 * executable at this moment. If not the current process is aborted and the <code>execute</code> method is not called.
 * If yes the current process is executed by calling the <code>execute</code> method. <br/>
 * 
 */
public abstract class ExecutionLimiterImproved {

    private boolean inExecution;

    private long timeBeforeNewExecution;

    private long startTime;

    private boolean finalExecute;

    private Thread executeAtEndOfTimeThread;

    private FinalExecution finalExecutionThreadWait;

    private ExecutorService executor;

    private ThreadFactory threadFactory = Executors.defaultThreadFactory();

    private Locker locker = new Locker();

    private boolean atLeastOneCallRefused;

    public ExecutionLimiterImproved() {
        this(0, false);
    }

    public ExecutionLimiterImproved(long timeBeforeNewExecute) {
        this(timeBeforeNewExecute, false);
    }

    /**
     * 
     * DOC amaumont ExecutionLimiter constructor comment.
     * 
     * @param timeBeforeNewExecute time max between executions
     * @param finalExecute execute at end of time the treatment to ensure it is executed a least one time after last
     * call of startIfExecutable()
     */
    public ExecutionLimiterImproved(long timeBeforeNewExecute, boolean finalExecute) {
        this(timeBeforeNewExecute, finalExecute, null);
    }

    public ExecutionLimiterImproved(long timeBeforeNewExecute, boolean finalExecute, final String callerInfo) {
        this.timeBeforeNewExecution = timeBeforeNewExecute;
        this.finalExecute = finalExecute;
        executor = Executors.newSingleThreadExecutor(new ThreadFactory() {

            public Thread newThread(Runnable r) {
                Thread newThread = threadFactory.newThread(r);
                newThread.setName(ExecutionLimiterImproved.class.getSimpleName()
                        + "_" + (callerInfo != null ? callerInfo + "_" : "") + newThread.getName()); //$NON-NLS-1$
                return newThread;
            }

        });

    }

    public boolean startIfExecutable() {
        return startIfExecutable(false, null);
    }

    public boolean startIfExecutable(Object data) {
        return startIfExecutable(false, data);
    }

    /**
     * Start execution if executable, after <code>timeBeforeNewExecute</code> is elapsed if
     * <code>executeAtEndOfTime</code> is true.
     * 
     * @param executeAtEndOfTime if true call <code>execute()</code> now, else call <code>execute()</code> at end of
     * <code>timeBeforeNewExecute</code>
     * @return true if executable, false else
     */
    @SuppressWarnings("unchecked")
    public boolean startIfExecutable(final boolean executeAtEndOfTime, final Object data) {

        // System.out.println("startIfExecutable: " + System.currentTimeMillis());

        boolean locked = false;
        boolean executable = false;

        synchronized (this) {
            locked = locker.lockIfUnlocked(ExecutionLimiterImproved.this);
            if (locked) {
                // System.out.println("locked");
                atLeastOneCallRefused = false;
            }

            executable = isExecutable(executeAtEndOfTime);
            if (locked && executable) {
                inExecution = true;
                startTime = System.currentTimeMillis();
            }
        }

        if (locked) {

            if (executable) {
                executor.execute(new Runnable() {

                    public void run() {
                        try {
                            if (executeAtEndOfTime) {

                                try {
                                    // System.out.println("1 HASHCODE = " + ExecutionLimiter.this.hashCode() + " " +
                                    // this.hashCode());
                                    // Thread.sleep(timeBeforeNewExecution);
                                    synchronized (this) {
                                        // System.out.println("2 HASHCODE = " + ExecutionLimiter.this.hashCode() + " " +
                                        // this.hashCode());
                                        executeAtEndOfTimeThread = Thread.currentThread();
                                        this.wait(timeBeforeNewExecution);
                                    }
                                    // System.out.println("Call executed: executeAtEndOfTime" +
                                    // ExecutionLimiter.this.hashCode() + " " + this.hashCode());
                                    callExecute(data);
                                    callFinalExecute(data);
                                } catch (InterruptedException e) {
                                    // System.out.println("=======> executeAtEndOfTime interrupted" +
                                    // ExecutionLimiter.this.hashCode() + " " + this.hashCode());
                                    return;
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            } else {
                                // System.out.println( "Call executed : now");
                                callExecute(data);
                                callFinalExecute(data);
                            }
                        } finally {
                            inExecution = false;
                            // System.out.println("UNlocked");
                            locker.unlock(ExecutionLimiterImproved.this);
                        }
                    }
                });
                return true;
            } else {
                // System.out.println("#####################################");
                // System.out.println("Execution rejected: not executable");
                // System.out.println("#####################################");
                atLeastOneCallRefused = true;
                return false;
            }
        } else {
            // System.out.println("#####################################");
            // System.out.println("Execution rejected: locked");
            // System.out.println("#####################################");
            atLeastOneCallRefused = true;
            return false;
        }
    }

    /**
     * 
     * DOC amaumont ExecutionLimiter class global comment. Detailled comment <br/>
     * 
     */
    class FinalExecution extends Thread {

        private Object data;

        public FinalExecution(Object data) {
            this.data = data;
        }

        public void run() {
            try {
                synchronized (this) {
                    // finalExecutionThreadWait = this;
                    this.wait(timeBeforeNewExecution);
                }
                // Thread.sleep(timeBeforeNewExecution);
            } catch (InterruptedException e) {
                // System.out.println("FinalExecution Interrupted " + ExecutionLimiter.this.hashCode() + " " +
                // this.hashCode());
                return;
            }
            // System.out.println("FinalExecution Not Interrupted " + ExecutionLimiter.this.hashCode() + " " +
            // this.hashCode());
            // System.out.println("Final thread executed");
            execute(true, data);
        }

    }

    /**
     * DOC amaumont Comment method "callFinalExecute".
     * 
     * @param data
     */
    private void callFinalExecute(Object data) {
        // System.out.println("before test final execute");
        if (finalExecute) {
            // System.out.println("try to final execution");

            while (true) {
                try {
                    synchronized (this) {
                        this.wait(timeBeforeNewExecution);
                    }
                } catch (InterruptedException e) {
                    return;
                }
                if (!atLeastOneCallRefused) {
                    // System.out.println("Final execution");
                    execute(true, data);
                    atLeastOneCallRefused = false;
                    break;
                }
                atLeastOneCallRefused = false;
                // System.out.println("Intermediate execution");
                execute(false, data);
            }
        }

    }

    private void callExecute(Object data) {
        execute(false, data);
    }

    protected abstract void execute(boolean isFinalExecution, Object data);

    private boolean isExecutable(boolean executeAtEndOfTime) {
        boolean returnValue = false;
        if (executeAtEndOfTime) {
            returnValue = !inExecution;
        } else {
            if (timeBeforeNewExecution == 0) {
                returnValue = !inExecution;
            } else {
                returnValue = !inExecution && System.currentTimeMillis() - startTime >= timeBeforeNewExecution;
                // System.out.println(System.currentTimeMillis() - startTime + " >= " + timeBeforeNewExecution + " " +
                // returnValue);
            }
        }

        // System.out.println("IS EXECUTABLE: " + returnValue);
        return returnValue;
    }

    public long getTimeBeforeNewExecution() {
        return timeBeforeNewExecution;
    }

    public void setTimeBeforeNewExecution(long timeBeforeNewExecute) {
        this.timeBeforeNewExecution = timeBeforeNewExecute;
    }

    public void resetTimer() {
        // System.out.println("############### RESET timer");
        startTime = System.currentTimeMillis();
        if (executeAtEndOfTimeThread != null && !executeAtEndOfTimeThread.isInterrupted()) {
            executeAtEndOfTimeThread.interrupt();
        }
        if (finalExecutionThreadWait != null && !finalExecutionThreadWait.isInterrupted()) {
            finalExecutionThreadWait.interrupt();
        }
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }

}
