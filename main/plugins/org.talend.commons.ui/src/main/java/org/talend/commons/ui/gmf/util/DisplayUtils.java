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
package org.talend.commons.ui.gmf.util;

import java.util.concurrent.Semaphore;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;

/**
 * Utility methods to work with Display object
 * 
 * @author aboyko
 * @since 1.2
 */
public class DisplayUtils {

    /**
     * Returns a non-null instance of Display object. Tries to find the Display object for the current thread first and
     * if it fails tries to get: <li>Workbench display if the workbench running <li>Default display object
     * 
     * @return non-null Display object
     * @since 1.2
     */
    public static Display getDisplay() {
        Display display = Display.getCurrent();
        if (display == null && PlatformUI.isWorkbenchRunning()) {
            display = PlatformUI.getWorkbench().getDisplay();
        }
        return display != null ? display : Display.getDefault();
    }

    /**
     * <b> !! ATTENTION !! <br/>
     * If it returns the progress dialog shell of eclipse by Display.getDefault().getActiveShell(), may have risk to be
     * closed automatically when progress dialog is closed! </b><br/>
     * <br/>
     * Attempts to return the default shell. If it cannot return the default shell, it returns the shell of the first
     * workbench window that has shell.
     * 
     * @return The shell
     * @since 1.2
     */
    public static Shell getDefaultShell() {
        Shell shell = null;

        try {
            shell = Display.getDefault().getActiveShell();
        } catch (Exception e) {
            // ignore
        }

        try {
            if (shell == null) {
                if (!PlatformUI.isWorkbenchRunning()) {
                    return new Shell();
                }
                IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                if (activeWindow != null) {
                    shell = activeWindow.getShell();
                }

            }
        } catch (Exception e) {
            // ignore
        }

        if (shell == null) {
            if (!PlatformUI.isWorkbenchRunning()) {
                return new Shell();
            }
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            for (int i = 0; shell == null && i < windows.length; i++) {
                shell = windows[i].getShell();
            }
        }

        return shell;
    }

    /**
     * Clear the event queue
     * 
     * @since 1.2
     */
    public static void clearEventLoop() {
        while (getDisplay().readAndDispatch()) {
            ;
        }
    }

    /**
     * Simply run in a new created UI thread<br>
     * <br>
     * <b>NOTE!!</b> The runnable should be simple, can <b>NOT</b> call any UI element belongs to other UI thread.
     * 
     * @param runnable
     * @throws Exception
     */
    public static void syncExecInNewUIThread(Runnable runnable) throws Exception {
        syncExecInNewUIThread(runnable, null);
    }

    public static void syncExecInNewUIThread(Runnable runnable, DeviceData deviceData) throws Exception {
        final Semaphore semaphore = new Semaphore(1, true);
        semaphore.acquire();
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Display display = null;
                if (deviceData == null) {
                    display = new Display();
                } else {
                    display = new Display(deviceData);
                }
                try {
                    Thread currentThread = Thread.currentThread();
                    boolean releasedLock = false;
                    while (!currentThread.isInterrupted()) {
                        try {
                            if (!display.readAndDispatch()) {
                                if (!releasedLock) {
                                    semaphore.release();
                                    releasedLock = true;
                                }
                                Thread.sleep(50);
                            }
                        } catch (InterruptedException e) {
                            throw e;
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                } catch (InterruptedException e) {
                    // ignore
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                } finally {
                    if (semaphore.availablePermits() <= 0) {
                        semaphore.release();
                    }
                    display.dispose();
                }
            }
        });
        thread.start();

        semaphore.acquire();
        semaphore.release();
        Display display = Display.findDisplay(thread);
        display.syncExec(runnable);
        thread.interrupt();
    }

}
