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
package org.talend.commons.ui.swt.thread;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * bqian This class can start a thread to execute the backgroud job that user spcified, and have the ability to refresh
 * the SWT UI after finishing the job. Class must extend this class to provide specific funtionality.<br/>
 * 
 * $Id: SWTUIThreadProcessor.java 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) bqian $
 * 
 */
public abstract class SWTUIThreadProcessor {

    private volatile boolean isStopped = true;

    Thread thread = null;

    Exception exception = null;

    public boolean preProcessStart() {
        return true;
    }

    public void nonUIProcessInThread() {
    }

    public void updateUIInThreadIfThreadIsCanceled() {
    }

    public void updateUIInThreadIfThreadIsNotCanceled() {
    }

    public void updateUIInThreadIfThreadFinally() {
    }

    public void postProcessCancle() {
    }

    public void execute() {

        if (isStopped) {
            isStopped = false;
            exception = null;
            if (!preProcessStart()) {
                isStopped = true;
                return;
            }

            thread = new Thread() {

                public void run() {
                    // load data
                    nonUIProcessInThread();

                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {
                            if (!isStopped) {
                                updateUIInThreadIfThreadIsNotCanceled();
                            } else {
                                updateUIInThreadIfThreadIsCanceled();
                            }
                            updateUIInThreadIfThreadFinally();
                            isStopped = true;
                        }
                    });

                }
            };
            thread.start();
        } else {
            isStopped = true;
            forceStop();
            postProcessCancle();
        }
    }

    public void execute2() {

        if (isStopped) {
            isStopped = false;
            exception = null;

            thread = new Thread() {

                public void run() {
                    Display.getDefault().asyncExec(new Runnable() {

                        public void run() {

                            if (!isStopped) {
                                updateUIInThreadIfThreadIsNotCanceled();
                            } else {
                                updateUIInThreadIfThreadIsCanceled();
                            }
                            updateUIInThreadIfThreadFinally();
                            isStopped = true;
                        }
                    });

                }
            };
            thread.start();
        } else {
            isStopped = true;
            forceStop();
            postProcessCancle();
        }
    }

    /**
     * 
     * cli Comment method "forceStop". (bug 6976)
     */
    public void forceStop() {
        if (thread != null && thread.isAlive()) {
            isStopped = true;
            try {
                thread.interrupt();
            } catch (SecurityException e) {
                e.printStackTrace(); // only for debug
            }
        }
    }

    /**
     * Getter for exception.
     * 
     * @return the exception
     */
    public Exception getException() {
        return this.exception;
    }

    /**
     * Sets the exception.
     * 
     * @param exception the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Getter for isStopped.
     * 
     * @return the isStopped
     */
    public boolean isStopped() {
        return this.isStopped;
    }

    /**
     * ftang Comment method "handleErrorOutput".
     */
    protected void handleErrorOutput(Composite outputComposite, CTabFolder tabFolder, CTabItem outputTabItem, Exception... e) {

        // Dispose all existing controls.
        if (!outputComposite.isDisposed()) {
            Control[] children = outputComposite.getChildren();
            for (Control control : children) {
                if (!control.isDisposed()) {
                    control.dispose();
                }
            }
        }

        Font font = new Font(Display.getDefault(), "courier", 8, SWT.NONE); //$NON-NLS-1$

        StyledText text = new StyledText(outputComposite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        text.setLayoutData(gridData);
        outputComposite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

        Exception exception = getException();
        if (e != null && e.length == 1) {
            exception = e[0];
        }

        String errorInfo = exception.getMessage() + "\n"; //$NON-NLS-1$
        errorInfo = errorInfo + Messages.getString("FileStep2.previewFailure") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            errorInfo = errorInfo + stackTraceElement.toString() + "\n"; //$NON-NLS-1$

        }
        text.setText(errorInfo);
        text.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
        text.setFont(font);

        tabFolder.setSelection(outputTabItem);
        outputComposite.layout(true);
    }

}
