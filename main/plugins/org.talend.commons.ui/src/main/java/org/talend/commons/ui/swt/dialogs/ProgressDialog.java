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
package org.talend.commons.ui.swt.dialogs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.runtime.thread.AsynchronousThreading;

/**
 * DOC amaumont class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class ProgressDialog {

    private Shell parentShell;

    private long timeBeforeShowDialog;

    /**
     * 
     * Show progress dialog when executeProcess() is called.
     * 
     * @param parentShell
     */
    public ProgressDialog(Shell parentShell) {
        super();
        this.parentShell = parentShell;
    }

    /**
     * 
     * Show progress dialog when executeProcess() is called and <code>timeBeforeShowDialog</code> is elapsed.
     * 
     * @param parentShell
     * @param timeBeforeShowDialog time before show dialog
     */
    public ProgressDialog(Shell parentShell, int timeBeforeShowDialog) {
        super();
        this.parentShell = parentShell;
        this.timeBeforeShowDialog = timeBeforeShowDialog;
    }

    public void executeProcess() throws InvocationTargetException, InterruptedException {
        executeProcess(false);
    }

    public void executeProcess(boolean useAsync) throws InvocationTargetException, InterruptedException {
        Display display2 = null;
        if (parentShell != null) {
            display2 = parentShell.getDisplay();
        }
        final Display display = display2;
        final InvocationTargetException[] iteHolder = new InvocationTargetException[1];
        try {
            final IRunnableWithProgress op = new IRunnableWithProgress() {

                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    final InvocationTargetException[] iteHolder1 = new InvocationTargetException[1];
                    try {
                        ProgressDialog.this.run(monitor);
                    } catch (InvocationTargetException e) {
                        // Pass it outside the workspace runnable
                        iteHolder1[0] = e;
                    } catch (InterruptedException e) {
                        // Re-throw as OperationCanceledException, which will be
                        // caught and re-thrown as InterruptedException below.
                        throw new OperationCanceledException(e.getMessage());
                    }
                    // CoreException and OperationCanceledException are propagated

                    // Re-throw the InvocationTargetException, if any occurred
                    if (iteHolder1[0] != null) {
                        throw iteHolder1[0];
                    }
                }
            };

            if (useAsync) {
                display.asyncExec(new Runnable() {

                    public void run() {
                        final ProgressMonitorDialog progressMonitorDialog = newProgressMonitorDialog(parentShell);
                        if (timeBeforeShowDialog > 0) {
                            if (progressMonitorDialog != null) {
                                progressMonitorDialog.setOpenOnRun(false);
                            }
                            // for bug 16801
                            AsynchronousThreading asynchronousThreading = new AsynchronousThreading(timeBeforeShowDialog, true,
                                    display, new Runnable() {

                                        public void run() {
                                            openDialog(progressMonitorDialog);
                                        }
                                    });
                            asynchronousThreading.start();
                        }

                        try {
                            dialogRun(progressMonitorDialog, op);
                        } catch (InvocationTargetException e) {
                            // Pass it outside the workspace runnable
                            iteHolder[0] = e;
                        } catch (InterruptedException e) {
                            // Re-throw as OperationCanceledException, which will be
                            // caught and re-thrown as InterruptedException below.
                            throw new OperationCanceledException(e.getMessage());
                        }
                    }
                });
            } else {
                display.syncExec(new Runnable() {

                    public void run() {
                        final ProgressMonitorDialog progressMonitorDialog = newProgressMonitorDialog(parentShell);
                        if (timeBeforeShowDialog > 0) {
                            if (progressMonitorDialog != null) {
                                progressMonitorDialog.setOpenOnRun(false);
                            }
                            // for bug 16801
                            AsynchronousThreading asynchronousThreading = new AsynchronousThreading(timeBeforeShowDialog, true,
                                    display, new Runnable() {

                                        public void run() {
                                            openDialog(progressMonitorDialog);
                                        }
                                    });
                            asynchronousThreading.start();
                        }

                        try {
                            dialogRun(progressMonitorDialog, op);
                        } catch (InvocationTargetException e) {
                            // Pass it outside the workspace runnable
                            iteHolder[0] = e;
                        } catch (InterruptedException e) {
                            // Re-throw as OperationCanceledException, which will be
                            // caught and re-thrown as InterruptedException below.
                            throw new OperationCanceledException(e.getMessage());
                        }
                    }
                });
            }

        } catch (OperationCanceledException e) {
            throw new InterruptedException(e.getMessage());
        }
        // Re-throw the InvocationTargetException, if any occurred
        if (iteHolder[0] != null) {
            throw iteHolder[0];
        }
    }

    protected ProgressMonitorDialog newProgressMonitorDialog(Shell shell) {
        return new ProgressMonitorDialog(shell);
    }

    protected void openDialog(ProgressMonitorDialog dialog) {
        dialog.open();
    }

    protected void dialogRun(ProgressMonitorDialog dialog, IRunnableWithProgress op)
            throws InvocationTargetException, InterruptedException {
        dialog.run(false, true, op);
    }

    public abstract void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException;

}
