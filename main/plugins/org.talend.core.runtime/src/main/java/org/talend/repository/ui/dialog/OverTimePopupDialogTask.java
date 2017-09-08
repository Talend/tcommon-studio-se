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
package org.talend.repository.ui.dialog;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.core.runtime.i18n.Messages;

/**
 * Can execute a task in background, if this task is overtime, then can show a progress dialog for user to cancel it
 */
public abstract class OverTimePopupDialogTask<T> {

    public static final int ENDLESS_WAIT_TIME = -1;

    public static final int DEFAULT_WAIT_TIME = 60;

    private static final String OVERTIME_POPUP_DIALOG_TASK_THREADGROUP_NAME = OverTimePopupDialogTask.class.getName();

    private String dialogTitle = Messages.getString("OverTimePopupDialogTask.title"); //$NON-NLS-1$

    private String dialogMessage = Messages.getString("OverTimePopupDialogTask.message"); //$NON-NLS-1$

    private String waitingFinishMessage = Messages.getString("OverTimePopupDialogTask.waitingFinishMessage.default"); //$NON-NLS-1$

    private Throwable exception;

    private FutureTask<T> futureTask;

    private Thread executeThread;

    private ThreadGroup threadGroup;

    private T executeResult;

    private boolean isUserCancelled = false;

    private volatile boolean kill = false;

    private int dialogProgressTimeout = ENDLESS_WAIT_TIME;

    private int overtime = 3000;

    boolean needWaitingProgressJob = true;

    abstract protected T run() throws Throwable;

    public final T runTask() throws Throwable {
        futureTask = new FutureTask<T>(new Callable<T>() {

            @Override
            public T call() throws Exception {
                T result = null;
                try {
                    result = run();
                } catch (Throwable e) {
                    exception = e;
                }
                return result;
            }
        });

        ThreadGroup parentThreadGroup = Thread.currentThread().getThreadGroup();
        boolean isInOverTimePopupDialogTask = OVERTIME_POPUP_DIALOG_TASK_THREADGROUP_NAME.equals(parentThreadGroup.getName());
        if (isInOverTimePopupDialogTask) {
            threadGroup = parentThreadGroup;
            executeThread = new Thread(threadGroup, futureTask);
            executeThread.start();
            try {
                executeResult = futureTask.get();
            } catch (Throwable e) {
                if (exception == null) {
                    exception = e;
                }
            }
            if (exception != null) {
                throw exception;
            }
            return executeResult;
        } else {
            threadGroup = new ThreadGroup(OVERTIME_POPUP_DIALOG_TASK_THREADGROUP_NAME);
            executeThread = new Thread(threadGroup, futureTask);
            executeThread.start();
            boolean isTimeout = false;
            try {
                executeResult = futureTask.get(overtime, TimeUnit.MILLISECONDS);
            } catch (TimeoutException timeoutException) {
                isTimeout = true;
            } catch (Throwable e) {
                if (exception == null) {
                    exception = e;
                }
            }

            if (exception != null) {
                throw exception;
            }
            if (!isTimeout) {
                return executeResult;
            }

            DisplayUtils.getDisplay().syncExec(new Runnable() {

                @Override
                public void run() {
                    ProgressMonitorDialog monitorDialog = new ProgressMonitorDialog(DisplayUtils.getDefaultShell()) {

                        @Override
                        protected void cancelPressed() {
                            isUserCancelled = true;
                            OverTimePopupDialogTask.this.kill();
                            super.cancelPressed();
                        }
                    };

                    try {
                        monitorDialog.run(true, true, new IRunnableWithProgress() {

                            @Override
                            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                                boolean isEndless = false;
                                int futureTaskTimeout = 500;
                                monitor.setTaskName(dialogTitle);
                                if (ENDLESS_WAIT_TIME == dialogProgressTimeout || dialogProgressTimeout <= 0) {
                                    isEndless = true;
                                    dialogProgressTimeout = futureTaskTimeout;
                                    monitor.beginTask(dialogMessage, IProgressMonitor.UNKNOWN);
                                } else {
                                    monitor.beginTask(dialogMessage,
                                            (int) Math.ceil(dialogProgressTimeout * 1.0 / futureTaskTimeout));
                                }
                                // in case executeResult returns null value
                                boolean executeSuccess = false;
                                for (int remainTime = dialogProgressTimeout; 0 < remainTime; remainTime = (isEndless ? dialogProgressTimeout
                                        : remainTime - futureTaskTimeout)) {
                                    try {
                                        if (kill) {
                                            break;
                                        }
                                        if (remainTime < futureTaskTimeout) {
                                            futureTaskTimeout = remainTime;
                                        }
                                        monitor.worked(1);
                                        executeResult = futureTask.get(futureTaskTimeout, TimeUnit.MILLISECONDS);
                                        executeSuccess = true;
                                        break;
                                    } catch (TimeoutException timeoutException) {
                                        continue;
                                    } catch (Throwable e) {
                                        exception = e;
                                        break;
                                    }
                                }
                                if (!kill && exception == null && !executeSuccess) {
                                    kill();
                                    exception = new TimeoutException(Messages.getString("OverTimePopupDialogTask.executeTimeout")); //$NON-NLS-1$
                                } else if (kill) {
                                    exception = new TimeoutException(Messages.getString("OverTimePopupDialogTask.killed")); //$NON-NLS-1$
                                }
                                monitor.done();
                            }
                        });
                    } catch (Throwable e) {
                        exception = e;
                    }
                }
            });

            if (exception != null) {
                throw exception;
            }

            return executeResult;

        }
    }

    public void kill() {
        threadGroup.interrupt();
        kill = true;
        addJob();
    }

    private void addJob() {
        if (!needWaitingProgressJob) {
            return;
        }
        StackTraceElement stElement = null;
        StackTraceElement stackTraceElements[] = executeThread.getStackTrace();
        if (stackTraceElements != null && 0 < stackTraceElements.length) {
            stElement = stackTraceElements[0];
        }
        String currentMethod;
        String title = ""; //$NON-NLS-1$
        if (stElement != null) {
            currentMethod = stElement.getClassName() + "." + stElement.getMethodName(); //$NON-NLS-1$
            title = waitingFinishMessage
                    + Messages.getString(
                            "OverTimePopupDialogTask.CheckingConnectionJob.waitingFinish", new Object[] { currentMethod }); //$NON-NLS-1$
        } else {
            title = waitingFinishMessage + Messages.getString("OverTimePopupDialogTask.CheckingConnectionJob.emptyWaitingfinish"); //$NON-NLS-1$
        }
        CheckingConnectionJob<T> checkingConnectionJob = new CheckingConnectionJob<T>(title, futureTask);
        checkingConnectionJob.setUser(false);
        checkingConnectionJob.setPriority(Job.DECORATE);
        checkingConnectionJob.schedule();
    }

    public boolean isUserCancelled() {
        return this.isUserCancelled;
    }

    public String getDialogTitle() {
        return this.dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getDialogMessage() {
        return this.dialogMessage;
    }

    public void setDialogMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
    }

    public Throwable getException() {
        return this.exception;
    }

    public int getDialogProgressTimeout() {
        return this.dialogProgressTimeout;
    }

    public void setDialogProgressTimeout(int dialogProgressTimeout) {
        this.dialogProgressTimeout = dialogProgressTimeout;
    }

    public int getOvertime() {
        return this.overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public boolean isNeedWaitingProgressJob() {
        return this.needWaitingProgressJob;
    }

    public void setNeedWaitingProgressJob(boolean needWaitingProgressJob) {
        this.needWaitingProgressJob = needWaitingProgressJob;
    }

    private static class CheckingConnectionJob<T> extends Job {

        protected FutureTask<T> futureTask;

        public CheckingConnectionJob(String name, FutureTask<T> _futureTask) {
            super(name);
            this.futureTask = _futureTask;
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            try {
                futureTask.get();
            } catch (Throwable e) {
                // nothing need to do since all will be done in ARunnableWithProgressCancel
            }
            return Status.OK_STATUS;
        }

    }
}
