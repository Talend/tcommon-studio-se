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
package org.talend.librariesmanager.ui.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.system.EclipseCommandLine;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleToInstall;
import org.talend.librariesmanager.utils.DownloadModuleRunnableWithLicenseDialog;
import org.talend.librariesmanager.utils.RemoteModulesHelper;

/**
 * adds a progress bar to Modules dialog. Mostly inspired by org.eclipse.jface.wizard.WizardDialog.
 * 
 */
@SuppressWarnings("rawtypes")
public class ExternalModulesInstallDialogWithProgress extends ExternalModulesInstallDialog {

    private Button closeButton;

    private SelectionAdapter closeListener;

    private ProgressMonitorPart progressMonitorPart;

    private Cursor waitCursor;

    private static final String FOCUS_CONTROL = "focusControl"; //$NON-NLS-1$

    private boolean fork = true;

    /**
     * The time in milliseconds where the last job finished. 'Enter' key presses are ignored for the next
     * {@link #RESTORE_ENTER_DELAY} milliseconds.
     * <p>
     * The value <code>-1</code> indicates that the traverse listener needs to be installed.
     * </p>
     * 
     * @since 3.6
     */
    private long timeWhenLastJobFinished = -1;

    /**
     * A delay in milliseconds that reduces the risk that the user accidentally triggers a button by pressing the
     * 'Enter' key immediately after a job has finished.
     * 
     * @since 3.6
     */
    private static final int RESTORE_ENTER_DELAY = 500;

    // The number of long running operation executed from the dialog.
    private long activeRunningOperations = 0;

    /**
     * runnable launched whe the dialog is openned
     */
    private IRunnableWithProgress initialRunnable;

    /**
     * DOC sgandon ExternalModulesInstallDialogWithProgress constructor comment.
     * 
     * @param shell
     * @param text
     * @param title
     */
    public ExternalModulesInstallDialogWithProgress(Shell shell, String text, String title, int extraSheelStyle) {
        super(shell, text, title);
        setShellStyle(getShellStyle() | extraSheelStyle);
        // since VAJava can't initialize an instance var with an anonymous
        // class outside a constructor we do it here:
        closeListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                closePressed();
            }
        };
    }

    /**
     * DOC sgandon ExternalModulesInstallDialogWithProgress constructor comment.
     * 
     * @param shell
     * @param text
     * @param title
     */
    public ExternalModulesInstallDialogWithProgress(Shell shell, String text, String title) {
        this(shell, text, title, 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialog#createDialogArea(org.eclipse.swt.widgets.
     * Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        // Insert a progress monitor
        GridLayout pmlayout = new GridLayout();
        pmlayout.numColumns = 1;
        progressMonitorPart = createProgressMonitorPart(composite, pmlayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        progressMonitorPart.setLayoutData(gridData);
        progressMonitorPart.setVisible(false);
        return composite;
    }

    /**
     * Create the progress monitor part in the receiver.
     * 
     * @param composite
     * @param pmlayout
     * @return ProgressMonitorPart
     */
    protected ProgressMonitorPart createProgressMonitorPart(Composite composite, GridLayout pmlayout) {
        return new ProgressMonitorPart(composite, pmlayout, true) {

            String currentTask = null;

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.wizard.ProgressMonitorPart#setBlocked(org.eclipse.core.runtime.IStatus)
             */
            @Override
            public void setBlocked(IStatus reason) {
                super.setBlocked(reason);
                getBlockedHandler().showBlocked(getShell(), this, reason, currentTask);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.wizard.ProgressMonitorPart#clearBlocked()
             */
            @Override
            public void clearBlocked() {
                super.clearBlocked();
                getBlockedHandler().clearBlocked();
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.wizard.ProgressMonitorPart#beginTask(java.lang.String, int)
             */
            @Override
            public void beginTask(String name, int totalWork) {
                super.beginTask(name, totalWork);
                currentTask = name;
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.wizard.ProgressMonitorPart#setTaskName(java.lang.String)
             */
            @Override
            public void setTaskName(String name) {
                super.setTaskName(name);
                currentTask = name;
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.jface.wizard.ProgressMonitorPart#subTask(java.lang.String)
             */
            @Override
            public void subTask(String name) {
                super.subTask(name);
                // If we haven't got anything yet use this value for more
                // context
                if (currentTask == null) {
                    currentTask = name;
                }
            }
        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.TrayDialog#close() can be called when the user click on close system button
     */
    @Override
    public boolean close() {
        getProgressMonitor().setCanceled(true);
        if (activeRunningOperations <= 0) {
            return super.close();
        } else {
            return false;
        }
    }

    /*
     * called when the close button is pressed
     */

    protected void closePressed() {
        getProgressMonitor().setCanceled(true);
        if (activeRunningOperations <= 0) {
            // Close the dialog. The check whether the dialog can be
            // closed or not is done in <code>okToClose</code>.
            // This ensures that the check is also evaluated when the user
            // presses the window's close button.
            setReturnCode(CANCEL);
            close();
        } else {
            closeButton.setEnabled(false);
        }
    }

    @Override
    protected Button getButton(int id) {
        if (id == IDialogConstants.CLOSE_ID) {
            return closeButton;
        }
        return super.getButton(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
     */
    @Override
    protected void buttonPressed(int buttonId) {
        if (IDialogConstants.CLOSE_ID == buttonId) {
            closePressed();
        }// else cancel button has a listener already
    }

    protected IProgressMonitor getProgressMonitor() {
        return progressMonitorPart;
    }

    /**
     * This implementation of IRunnableContext#run(boolean, boolean, IRunnableWithProgress) blocks until the runnable
     * has been run, regardless of the value of <code>fork</code>. It is recommended that <code>fork</code> is set to
     * true in most cases. If <code>fork</code> is set to <code>false</code>, the runnable will run in the UI thread and
     * it is the runnable's responsibility to call <code>Display.readAndDispatch()</code> to ensure UI responsiveness.
     * 
     * UI state is saved prior to executing the long-running operation and is restored after the long-running operation
     * completes executing. Any attempt to change the UI state of the wizard in the long-running operation will be
     * nullified when original UI state is restored.
     * 
     */
    public void run(IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
        // The operation can only be canceled if it is executed in a separate
        // thread.
        // Otherwise the UI is blocked anyway.
        Object state = null;
        if (activeRunningOperations == 0) {
            state = aboutToStart();
        }
        activeRunningOperations++;
        progressMonitorPart.attachToCancelComponent(null);// nasty hack to enable the cancel button
        try {
            ModalContext.run(runnable, fork, getProgressMonitor(), getShell().getDisplay());
        } finally {
            // explicitly invoke done() on our progress monitor so that its
            // label does not spill over to the next invocation, see bug 271530
            if (getProgressMonitor() != null) {
                getProgressMonitor().done();
            }
            // Stop if this is the last one
            if (state != null) {
                timeWhenLastJobFinished = System.currentTimeMillis();
                stopped(state);
            }
            activeRunningOperations--;
        }
    }

    /**
     * About to start a long running operation triggered through the wizard. Shows the progress monitor and disables the
     * wizard's buttons and controls.
     * 
     * @param enableCancelButton <code>true</code> if the Cancel button should be enabled, and <code>false</code> if it
     * should be disabled
     * @return the saved UI state
     */
    @SuppressWarnings("unchecked")
    private Object aboutToStart() {
        Map savedState = null;
        if (getShell() != null) {
            // Save focus control
            Control focusControl = getShell().getDisplay().getFocusControl();
            if (focusControl != null && focusControl.getShell() != getShell()) {
                focusControl = null;
            }

            // Set the busy cursor to all shells.
            Display d = getShell().getDisplay();
            waitCursor = new Cursor(d, SWT.CURSOR_WAIT);
            setDisplayCursor(waitCursor);

            // Deactivate shell
            savedState = saveUIState();
            if (focusControl != null) {
                savedState.put(FOCUS_CONTROL, focusControl);
            }
            progressMonitorPart.setVisible(true);

            // Install traverse listener once in order to implement 'Enter' and 'Space' key blocking
            if (timeWhenLastJobFinished == -1) {
                timeWhenLastJobFinished = 0;
                getShell().addTraverseListener(new TraverseListener() {

                    @Override
                    public void keyTraversed(TraverseEvent e) {
                        if (e.detail == SWT.TRAVERSE_RETURN || (e.detail == SWT.TRAVERSE_MNEMONIC && e.keyCode == 32)) {
                            // We want to ignore the keystroke when we detect that it has been received within the
                            // delay period after the last operation has finished. This prevents the user from
                            // accidentally
                            // hitting "Enter" or "Space", intending to cancel an operation, but having it processed
                            // exactly
                            // when the operation finished, thus traversing the wizard. If there is another operation
                            // still
                            // running, the UI is locked anyway so we are not in this code. This listener should fire
                            // only
                            // after the UI state is restored (which by definition means all jobs are done.
                            // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=287887
                            if (timeWhenLastJobFinished != 0
                                    && System.currentTimeMillis() - timeWhenLastJobFinished < RESTORE_ENTER_DELAY) {
                                e.doit = false;
                                return;
                            }
                            timeWhenLastJobFinished = 0;
                        }
                    }
                });
            }
        }
        return savedState;
    }

    /**
     * Creates the Cancel button for this wizard dialog. Creates a standard (<code>SWT.PUSH</code>) button and registers
     * for its selection events. Note that the number of columns in the button bar composite is incremented. The Cancel
     * button is created specially to give it a removeable listener.
     * 
     * @param parent the parent button bar
     * @return the new Cancel button
     */
    private Button createCloseButton(Composite parent) {
        // increment the number of columns in the button bar
        ((GridLayout) parent.getLayout()).numColumns++;
        Button button = new Button(parent, SWT.PUSH);
        button.setText(IDialogConstants.CLOSE_LABEL);
        setButtonLayoutData(button);
        button.setFont(parent.getFont());
        button.setData(new Integer(IDialogConstants.CLOSE_ID));
        button.addSelectionListener(closeListener);
        return button;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialog#createButtonsForButtonBar(org.eclipse.swt
     * .widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        closeButton = createCloseButton(parent);// make the cancel button the most left
        super.createButtonsForButtonBar(parent);
    }

    /**
     * Sets the given cursor for all shells currently active for this window's display.
     * 
     * @param c the cursor
     */
    private void setDisplayCursor(Cursor c) {
        Shell[] shells = getShell().getDisplay().getShells();
        for (Shell shell2 : shells) {
            shell2.setCursor(c);
        }
    }

    /**
     * Captures and returns the enabled/disabled state of the wizard dialog's buttons and the tree of controls for the
     * currently showing page. All these controls are disabled in the process, with the possible exception of the Cancel
     * button.
     * 
     * @param keepCancelEnabled <code>true</code> if the Cancel button should remain enabled, and <code>false</code> if
     * it should be disabled
     * @return a map containing the saved state suitable for restoring later with <code>restoreUIState</code>
     * @see #restoreUIState
     */
    private Map saveUIState() {
        Map savedState = new HashMap(10);
        saveEnableStateAndSet(closeButton, savedState, "close", true); //$NON-NLS-1$
        //        saveEnableStateAndSet(tableViewerCreator.getTable(), savedState, "table", false); //$NON-NLS-1$
        saveEnableStateAndSet(installAllBtn, savedState, "install", false); //$NON-NLS-1$
        return savedState;
    }

    /**
     * Saves the enabled/disabled state of the given control in the given map, which must be modifiable.
     * 
     * @param w the control, or <code>null</code> if none
     * @param h the map (key type: <code>String</code>, element type: <code>Boolean</code>)
     * @param key the key
     * @param enabled <code>true</code> to enable the control, and <code>false</code> to disable it
     * @see #restoreEnableState(Control, Map, String)
     */
    @SuppressWarnings("unchecked")
    private void saveEnableStateAndSet(Control w, Map h, String key, boolean enabled) {
        if (w != null) {
            h.put(key, w.getEnabled() ? Boolean.TRUE : Boolean.FALSE);
            w.setEnabled(enabled);
        }
    }

    /**
     * Restores the enabled/disabled state of the wizard dialog's buttons and the tree of controls for the currently
     * showing page.
     * 
     * @param state a map containing the saved state as returned by <code>saveUIState</code>
     * @see #saveUIState
     */
    private void restoreUIState(Map state) {
        restoreEnableState(closeButton, state, "close"); //$NON-NLS-1$
        restoreEnableState(tableViewerCreator.getTable(), state, "table"); //$NON-NLS-1$
        restoreEnableState(installAllBtn, state, "install"); //$NON-NLS-1$
    }

    /**
     * Restores the enabled/disabled state of the given control.
     * 
     * @param w the control
     * @param h the map (key type: <code>String</code>, element type: <code>Boolean</code>)
     * @param key the key
     * @see #saveEnableStateAndSet
     */
    private void restoreEnableState(Control w, Map h, String key) {
        if (w != null) {
            Boolean b = (Boolean) h.get(key);
            if (b != null) {
                w.setEnabled(b.booleanValue());
            }
        }
    }

    /**
     * A long running operation triggered through the wizard was stopped either by user input or by normal end. Hides
     * the progress monitor and restores the enable state wizard's buttons and controls.
     * 
     * @param savedState the saved UI state as returned by <code>aboutToStart</code>
     * @see #aboutToStart
     */
    private void stopped(Object savedState) {
        if (getShell() != null && !getShell().isDisposed()) {
            progressMonitorPart.setVisible(false);
            progressMonitorPart.removeFromCancelComponent(closeButton);
            Map state = (Map) savedState;
            restoreUIState(state);
            setDisplayCursor(null);
            waitCursor.dispose();
            waitCursor = null;
            Control focusControl = (Control) state.get(FOCUS_CONTROL);
            if (focusControl != null && !focusControl.isDisposed()) {
                focusControl.setFocus();
            }
        }
    }

    void setInitialRunnable(final IRunnableWithProgress initialRunnable) {
        this.initialRunnable = initialRunnable;
    }

    /**
     * called when the initial runnable is finished or when the dialog is openned with no initial runnable
     */
    protected void initialRunnableDone() {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                tableViewerCreator.init(inputList);
                addInstallButtons();
                updateInstallModulesButtonState();
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#create()
     */
    @Override
    public void create() {
        super.create();
        startInitialRunnable();

    }

    @Override
    protected void addListeners() {

        installAllBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {

                List<ModuleToInstall> toInstall = getModulesToBeInstalled();

                installAllBtn.setEnabled(false);
                try {
                    run(new DownloadModuleRunnableWithLicenseDialog(toInstall, getShell()));
                    // close the dialog box when the download is done if it has not been canceled
                    if (!getProgressMonitor().isCanceled()) {
                        setReturnCode(CANCEL);
                        close();
                    }
                } catch (InvocationTargetException e) {
                    // an error occured when fetching the modules, so report it to the user.
                    ExceptionHandler.process(e);
                } catch (InterruptedException e) {
                    // the thread was interupted
                    ExceptionHandler.process(e);
                } finally {
                    if (!installAllBtn.isDisposed()) {
                        installAllBtn.setEnabled(true);
                    }
                }
            }
        });
    }

    /**
     * start initial runnable and block until is has finished. it then update the dialog window with the retreived data
     * by calling initialRunnableDone(). if the initialRunnable is null then the initialRunnableDone()
     */
    public void startInitialRunnable() {
        if (initialRunnable != null) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        ExternalModulesInstallDialogWithProgress.this.run(initialRunnable);
                        initialRunnableDone();
                    } catch (InvocationTargetException e) {
                        // an error occured when fetching the modules, so report it to the user.
                        ExceptionHandler.process(e);
                    } catch (InterruptedException e) {
                        // the thread was interupted
                        ExceptionHandler.process(e);
                    }
                }
            });
        } else {// modules data are already in cache so show the dialog as this
            initialRunnableDone();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialog#launchIndividualDownload(java.util.concurrent
     * .atomic.AtomicInteger, org.talend.core.model.general.ModuleToInstall)
     */
    @Override
    protected void launchIndividualDownload(final AtomicInteger enabledButtonCount, ModuleToInstall data, final Button button) {
        button.setEnabled(false);
        enabledButtonCount.decrementAndGet();
        DownloadModuleRunnableWithLicenseDialog downloadModuleRunnable = new DownloadModuleRunnableWithLicenseDialog(
                Collections.singletonList(data), getShell());
        try {
            run(downloadModuleRunnable);
        } catch (InvocationTargetException e) {
            individualDownloadFailed(enabledButtonCount, button);
            // an error occured when fetching the modules, so report it to the user.
            ExceptionHandler.process(e);
        } catch (InterruptedException e) {
            individualDownloadFailed(enabledButtonCount, button);
            // the thread was interupted
            ExceptionHandler.process(e);
        } finally {// if button canceled then enable button
            if (getProgressMonitor().isCanceled()) {
                individualDownloadFailed(enabledButtonCount, button);
            } else {// keep button disabled and make download all button disabled if that was the last
                Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        if (!installAllBtn.isDisposed() && enabledButtonCount.get() == 0) {
                            installAllBtn.setEnabled(false);
                        }
                    }
                });
            }
            if (enabledButtonCount.get() == 0) {
                close();
            }
        }
    }

    /**
     * DOC sgandon Comment method "individualDownloadFailed".
     * 
     * @param enabledButtonCount
     * @param button
     */
    protected void individualDownloadFailed(final AtomicInteger enabledButtonCount, final Button button) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (!button.isDisposed()) {
                    button.setEnabled(true);
                    enabledButtonCount.incrementAndGet();
                    installAllBtn.setEnabled(true);
                }
            }
        });
    }

    /**
     * show the dialog
     * 
     * @param block, whether the this method is blocked until the dialog is closed
     * @param requiredJars, list of required jars
     */
    public void showDialog(boolean block, String[] requiredJars) {
        if (ArrayUtils.contains(Platform.getApplicationArgs(),
                EclipseCommandLine.TALEND_DISABLE_EXTERNAL_MODULE_INSTALL_DIALOG_COMMAND)) {
            CommonExceptionHandler.warn("missing jars: " + ArrayUtils.toString(requiredJars)); //$NON-NLS-1$
            return;
        }

        List<ModuleNeeded> requiredModules = new ArrayList<ModuleNeeded>();
        for (String jarName : requiredJars) {
            // create module without mvnuri and handle it in RemoteModulesHelper.getNotInstalledModulesRunnable
            requiredModules.add(new ModuleNeeded(null, jarName, null, true));
        }
        showDialog(block, requiredModules);
    }

    public void showDialog(boolean block, Collection<ModuleNeeded> requiredModules) {
        if (ArrayUtils.contains(Platform.getApplicationArgs(),
                EclipseCommandLine.TALEND_DISABLE_EXTERNAL_MODULE_INSTALL_DIALOG_COMMAND)) {
            CommonExceptionHandler.warn("missing jars: " + ArrayUtils.toString(requiredModules)); //$NON-NLS-1$
            return;
        }
        // fork = !block;
        // remove duplicated
        List<ModuleNeeded> required = new ArrayList<ModuleNeeded>(requiredModules);
        IRunnableWithProgress notInstalledModulesRunnable = RemoteModulesHelper.getInstance().getNotInstalledModulesRunnable(
                required, inputList, true);
        setBlockOnOpen(block);
        setInitialRunnable(notInstalledModulesRunnable);
        open();

    }

}
