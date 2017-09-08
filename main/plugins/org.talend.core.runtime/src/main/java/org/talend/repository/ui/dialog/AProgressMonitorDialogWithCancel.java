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
import org.eclipse.swt.widgets.Shell;
import org.talend.core.runtime.i18n.Messages;

/**
 * created by cmeng on Nov 21, 2014 Detailled comment
 *
 */
public abstract class AProgressMonitorDialogWithCancel<T> extends ProgressMonitorDialog {

    public static final int ENDLESS_WAIT_TIME = -1;

    public static final int DEFAULT_WAIT_TIME = 60;

    private ARunnableWithProgressCancel<T> runnableWithCancel;

    private boolean isUserCancelled = false;

    /**
     * DOC cmeng ProgressMonitorDialogWithCancel constructor comment.
     * 
     * @param parent
     */
    public AProgressMonitorDialogWithCancel(Shell parent) {
        super(parent);
    }

    abstract protected T runWithCancel(IProgressMonitor monitor) throws Throwable;

    @Override
    @Deprecated
    public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException,
            InterruptedException {
        throw new InvocationTargetException(new Throwable("Can not use this method, you should use another run method")); //$NON-NLS-1$
    }

    public void run() throws InvocationTargetException, InterruptedException {
        run(null, null, true, DEFAULT_WAIT_TIME);
    }

    public void run(String executeMessage, String waitingFinishMessage, boolean needWaitingProgressJob)
            throws InvocationTargetException, InterruptedException {
        run(executeMessage, waitingFinishMessage, needWaitingProgressJob, DEFAULT_WAIT_TIME);
    }

    public void run(String executeMessage, String waitingFinishMessage, boolean needWaitingProgressJob, int timeout)
            throws InvocationTargetException, InterruptedException {
        runnableWithCancel = new ARunnableWithProgressCancel<T>() {

            @Override
            protected T runnableWithCancel(IProgressMonitor monitor) throws Throwable {
                return runWithCancel(monitor);
            }
        };
        if (executeMessage != null) {
            runnableWithCancel.setExecuteMessage(executeMessage);
        }
        if (waitingFinishMessage != null) {
            runnableWithCancel.setWaitingFinishMessage(waitingFinishMessage);
        }
        runnableWithCancel.setNeedWaitingProgressJob(needWaitingProgressJob);
        runnableWithCancel.setTimeout(timeout);
        super.run(true, true, runnableWithCancel);
    }

    @Override
    protected void cancelPressed() {
        isUserCancelled = true;
        if (runnableWithCancel != null) {
            runnableWithCancel.kill();
        }
        super.cancelPressed();
    }

    public boolean isUserCanncelled() {
        return isUserCancelled;
    }

    public T getExecuteResult() {
        if (runnableWithCancel != null) {
            return runnableWithCancel.getExecuteResult();
        } else {
            return null;
        }
    }

    public Throwable getExecuteException() {
        if (runnableWithCancel != null) {
            return runnableWithCancel.getExecuteException();
        } else {
            return null;
        }
    }

    private static abstract class ARunnableWithProgressCancel<T> implements IRunnableWithProgress {

        protected FutureTask<T> futureTask;

        protected Thread executeThread;

        protected ThreadGroup threadGroup;

        protected T executeResult;

        protected Throwable executeException;

        protected String executeMessage = Messages.getString("ProgressMonitorDialogWithCancel.executeMessage.default"); //$NON-NLS-1$

        protected String waitingFinishMessage = Messages
                .getString("ProgressMonitorDialogWithCancel.waitingFinishMessage.default"); //$NON-NLS-1$

        protected int timeout = 30;

        volatile boolean kill = false;

        boolean needWaitingProgressJob = true;

        public ARunnableWithProgressCancel() {
        }

        abstract protected T runnableWithCancel(IProgressMonitor monitor) throws Throwable;

        /*
         * (non-Javadoc)
         * 
         * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
         */
        @Override
        public final void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
            futureTask = new FutureTask<T>(new Callable<T>() {

                @Override
                public T call() throws Exception {
                    T result = null;
                    try {
                        result = runnableWithCancel(monitor);
                    } catch (Throwable e) {
                        executeException = e;
                    }
                    return result;
                }
            });
            int iTimeout;
            boolean canGain = true;
            if (ENDLESS_WAIT_TIME == timeout || timeout <= 0) {
                iTimeout = 1;
                canGain = false;
                monitor.beginTask(executeMessage, IProgressMonitor.UNKNOWN);
            } else {
                iTimeout = timeout * 2;
                monitor.beginTask(executeMessage, iTimeout);
            }
            threadGroup = new ThreadGroup("ARunnableWithProgressCancel"); //$NON-NLS-1$
            executeThread = new Thread(threadGroup, futureTask);
            executeThread.start();
            // in case executeResult returns null value
            boolean executeSuccess = false;
            for (int i = 0; i < iTimeout; i = canGain ? i + 1 : i) {
                try {
                    if (kill) {
                        break;
                    }
                    monitor.worked(1);
                    executeResult = futureTask.get(500, TimeUnit.MILLISECONDS);
                    executeSuccess = true;
                    break;
                } catch (TimeoutException timeoutException) {
                    continue;
                } catch (Throwable e) {
                    executeException = e;
                    break;
                }
            }
            if (!kill && executeException == null && !executeSuccess) {
                executeException = new TimeoutException(Messages.getString("ProgressMonitorDialogWithCancel.executeTimeout")); //$NON-NLS-1$
                kill();
            }
            monitor.done();
        }

        /**
         * DOC cmeng Comment method "addJob".
         * 
         * @throws InterruptedException
         */
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
                        + Messages
                                .getString(
                                        "ProgressMonitorDialogWithCancel.CheckingConnectionJob.waitingFinish", new Object[] { currentMethod }); //$NON-NLS-1$
            } else {
                title = waitingFinishMessage
                        + Messages.getString("ProgressMonitorDialogWithCancel.CheckingConnectionJob.emptyWaitingfinish"); //$NON-NLS-1$
            }
            CheckingConnectionJob<T> checkingConnectionJob = new CheckingConnectionJob<T>(title, futureTask);
            checkingConnectionJob.setUser(false);
            checkingConnectionJob.setPriority(Job.DECORATE);
            checkingConnectionJob.schedule();
        }

        public void setNeedWaitingProgressJob(boolean need) {
            needWaitingProgressJob = need;
        }

        public void setExecuteMessage(String exeMsg) {
            executeMessage = exeMsg;
        }

        public void setWaitingFinishMessage(String waitingMsg) {
            waitingFinishMessage = waitingMsg;
        }

        public T getExecuteResult() {
            return executeResult;
        }

        public Throwable getExecuteException() {
            return executeException;
        }

        public void setTimeout(int _timeout) {
            timeout = _timeout;
        }

        public void kill() {
            threadGroup.interrupt();
            kill = true;
            addJob();
        }
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
