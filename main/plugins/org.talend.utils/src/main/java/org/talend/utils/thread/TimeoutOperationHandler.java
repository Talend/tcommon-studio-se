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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Handler for an operation which processes with a timeout.
 *
 * @param <R> type of result
 */
public abstract class TimeoutOperationHandler<R> {

    private static Logger log = LoggerFactory.getLogger(TimeoutOperationHandler.class);

    private R result;

    private boolean timeoutReached;

    private String labelOperation;

    private long timeout;

    private Throwable operationError;

    public TimeoutOperationHandler(long timeout) {
        super();
        this.timeout = timeout;
    }

    public TimeoutOperationHandler(long timeout, String labelOperation) {
        super();
        this.timeout = timeout;
        this.labelOperation = labelOperation;
    }

    public void start() {

        internalStart();

    }

    protected void internalStart() {
        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    result = TimeoutOperationHandler.this.internalRun();
                } catch (Throwable t) {
                    log.error(t.getMessage(), t);
                    operationError = t;
                } finally {
                    finalizeOperation();
                }
            }

        };

        Thread thread = null;
        if (labelOperation != null) {
            thread = new Thread(runnable, labelOperation);
        } else {
            thread = new Thread(runnable);
        }
        thread.start();

        long timeStart = System.currentTimeMillis();

        while (true) {

            if (System.currentTimeMillis() - timeStart > timeout) {
                timeoutReached = true;
            }

            if (timeoutReached || !thread.isAlive()) {
                break;
            }
            ThreadUtils.waitTimeBool(50);
        }
    }

    protected R internalRun() {
        return run();
    }

    public boolean hasValidResultOperation(R result) {
        return result != null;
    }

    public abstract R run();

    public R getResult() {
        return result;
    }

    public Throwable getOperationError() {
        return operationError;
    }

    public void finalizeOperation() {

    }

    public void setLabelOperation(String labelOperation) {
        this.labelOperation = labelOperation;
    }

}
