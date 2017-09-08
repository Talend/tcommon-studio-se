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
package org.talend.commons.ui.runtime.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.eclipse.swt.widgets.Display;

/**
 * This class is useful to execute code after a given time. <br/>
 * 
 * Samples:
 * 
 * new AsynchronousThread(50, false, dataMapTableView.getDisplay(), new Runnable() { public void run() { // calls of
 * Widget methods } }).start();
 * 
 * new AsynchronousThread(50, new Runnable() { public void run() { // calls of methods except Widget methods }
 * }).start();
 * 
 */
public class AsynchronousThreading {

    private long sleepingTime;

    private boolean synchronousDisplayExecution;

    private Runnable target;

    private Display display;

    private Thread thread;

    private static ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {

        public Thread newThread(Runnable r) {
            Thread newThread = threadFactory.newThread(r);
            newThread.setName(AsynchronousThreading.class.getSimpleName() + "_" + newThread.getName()); //$NON-NLS-1$
            return newThread;
        }

    });

    /**
     * 
     * DOC amaumont AsynchronousThreading constructor comment.
     * 
     * @param sleepingTime before execution in ms
     * @param synchronousDisplayExecution
     * @param display
     * @param target
     */
    public AsynchronousThreading(long sleepingTime, boolean synchronousDisplayExecution, Display display, Runnable target) {
        this.sleepingTime = sleepingTime;
        this.synchronousDisplayExecution = synchronousDisplayExecution;
        this.target = target;
        this.display = display;
    }

    /**
     * 
     * DOC amaumont AsynchronousThreading constructor comment.
     * 
     * @param sleepingTime before execution in ms
     * @param target
     */
    public AsynchronousThreading(long sleepingTime, Runnable target) {
        this.sleepingTime = sleepingTime;
        this.target = target;
    }

    public void start() {

        executor.execute(new Runnable() {

            // @Override
            public void run() {
                if (sleepingTime > 0) {
                    try {
                        synchronized (this) {
                            this.wait(sleepingTime);
                        }
                    } catch (InterruptedException e) {
                        // System.out.println("interrupted");
                        return;
                    }
                }
                if (display == null) {
                    target.run();
                } else {
                    if (display.isDisposed()) {
                        return;
                    }
                    if (synchronousDisplayExecution) {
                        display.syncExec(new Runnable() {

                            public void run() {
                                target.run();
                            }
                        });
                    } else {
                        display.asyncExec(new Runnable() {

                            public void run() {
                                target.run();
                            }
                        });
                    }
                }
            }

        });

    }

    public void interrupt() {
        if (thread != null && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }

}
