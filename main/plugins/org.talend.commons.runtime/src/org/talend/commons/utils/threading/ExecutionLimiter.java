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

/**
 * 
 * Limit the execution of threads by verifying at call of <code>startIfExecutable</code> if the process can be
 * executable at this moment. If not the current process is aborted and the <code>execute</code> method is not called.
 * If yes the current process is executed by call the <code>execute</code> method. <br/>
 * 
 * @deprecated use ExecutionLimiterImproved instead
 */
public abstract class ExecutionLimiter {

    private boolean inExecution;

    private long timeBeforeNewExecution;

    private long startTime;

    private boolean finalExecute;

    private FinalExecution finalExecution;

    private Thread executeAtEndOfTimeThread;

    private FinalExecution finalExecutionThreadWait;

    public ExecutionLimiter() {
        super();
    }

    public ExecutionLimiter(long timeBeforeNewExecute) {
        this.timeBeforeNewExecution = timeBeforeNewExecute;
    }

    /**
     * 
     * DOC amaumont ExecutionLimiter constructor comment.
     * 
     * @param timeBeforeNewExecute time max between executions
     * @param finalExecute execute at end of time the treatment to ensure it is executed a least one time after last
     * call of startIfExecutable()
     */
    public ExecutionLimiter(long timeBeforeNewExecute, boolean finalExecute) {
        this.timeBeforeNewExecution = timeBeforeNewExecute;
        this.finalExecute = finalExecute;
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
     * @param data TODO
     * @return true if executable, false else
     */
    public boolean startIfExecutable(boolean executeAtEndOfTime, final Object data) {
        boolean executable = false;
        executable = isExecutable(executeAtEndOfTime);
        if (executable) {
            inExecution = true;
            if (executeAtEndOfTime) {
                (new Thread() {

                    @Override
                    public void run() {
                        try {
                            // System.out.println("1 HASHCODE = " + ExecutionLimiter.this.hashCode() + " " +
                            // this.hashCode());
                            // Thread.sleep(timeBeforeNewExecution);
                            synchronized (this) {
                                // System.out.println("2 HASHCODE = " + ExecutionLimiter.this.hashCode() + " " +
                                // this.hashCode());
                                executeAtEndOfTimeThread = this;
                                this.wait(timeBeforeNewExecution);
                            }
                            // System.out.println("Call executed: executeAtEndOfTime" + ExecutionLimiter.this.hashCode()
                            // + " " + this.hashCode());
                            callExecute(data);
                        } catch (InterruptedException e) {
                            // System.out.println("=======> executeAtEndOfTime interrupted" +
                            // ExecutionLimiter.this.hashCode() + " " + this.hashCode());
                            return;
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } finally {
                            inExecution = false;
                        }
                    }
                }).start();
            } else {
                // //System.out.println( "Call executed : now");
                callExecute(data);
            }
        } else {
            // //System.out.println( "Call rejected");
        }
        if (finalExecute) {
            // System.out.println("startThreadForFinalExecution();");
            startThreadForFinalExecution(data);
        }
        if (executable && !executeAtEndOfTime) {
            inExecution = false;
        }
        return executable;
    }

    private void callExecute(Object data) {
        startTime = System.currentTimeMillis();
        execute(false, data);
    }

    /**
     * DOC amaumont Comment method "startThreadForFinalExecution".
     * @param data 
     */
    private void startThreadForFinalExecution(final Object data) {
        (new Thread() {

            @Override
            public void run() {
                FinalExecution finalThread = new FinalExecution(data);
                if (finalExecutionThreadWait != null && !finalExecutionThreadWait.isInterrupted()) {
                    finalExecutionThreadWait.interrupt();
                }
                finalExecutionThreadWait = finalThread;
                finalThread.start();
            }

        }).start();
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
     * 
     * DOC amaumont Comment method "execute".
     * @param isFinalExecution
     * @param data can be null
     */
    protected abstract void execute(boolean isFinalExecution, Object data);

    private boolean isExecutable(boolean executeAtEndOfTime) {
        boolean returnValue = false;
        if (executeAtEndOfTime) {
            returnValue = !inExecution;
        } else {
            if (timeBeforeNewExecution == 0) {
                returnValue = !inExecution;
            } else {
                returnValue = System.currentTimeMillis() - startTime >= timeBeforeNewExecution;
                // System.out.println(System.currentTimeMillis() - startTime + " >= " + timeBeforeNewExecution + " " +
                // returnValue);
            }
        }
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
}
