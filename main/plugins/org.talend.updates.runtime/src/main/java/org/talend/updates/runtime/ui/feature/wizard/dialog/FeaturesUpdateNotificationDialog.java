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
package org.talend.updates.runtime.ui.feature.wizard.dialog;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.feature.form.FeaturesUpdatesNotificationForm;
import org.talend.updates.runtime.ui.feature.form.FeaturesUpdatesNotificationForm.AbstractNotificationButtonListener;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.feature.wizard.FeaturesManagerWizard;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesUpdateNotificationDialog extends Dialog {

    private FeaturesUpdatesNotificationForm notificationForm;

    private FeaturesManagerRuntimeData runtimeData;

    public FeaturesUpdateNotificationDialog(Shell parentShell, FeaturesManagerRuntimeData runtimeData) {
        super(parentShell);
        this.runtimeData = runtimeData;
        this.runtimeData.setDialog(this);
    }

    @Override
    protected Control createContents(Composite parent) {
        parent.setLayout(new FillLayout());
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new FillLayout());
        notificationForm = new FeaturesUpdatesNotificationForm(panel, SWT.NONE, runtimeData,
                runtimeData.getFeaturesManager().createUpdateNotificationItem(true), false) {
            @Override
            protected Color getBackgroundColor() {
                return FeaturesUpdateNotificationDialog.this.getBackgroundColor();
            };
        };
        runtimeData.setUpdateNotificationButtonListener(new AbstractNotificationButtonListener() {

            @Override
            public void onInstallUpdatesButtonClicked(SelectionEvent e, FeaturesUpdatesNotificationForm form) {
                try {
                    form.setExecuting(true);
                    form.enableButtons(false);
                    IProgressMonitor monitor = form.showProgress();
                    Exception exception[] = new Exception[1];
                    ModalContext.run(new IRunnableWithProgress() {

                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            try {
                                installUpdates(monitor, runtimeData, form);
                            } catch (Exception e) {
                                exception[0] = e;
                            }
                        }
                    }, true, monitor, Display.getDefault());
                    if (exception[0] != null) {
                        throw exception[0];
                    }
                } catch (Exception ex) {
                    ExceptionHandler.process(ex);
                    openExceptionDialog(form, ex);
                } finally {
                    if (!form.isDisposed()) {
                        form.hideProgress();
                        form.setExecuting(false);
                        form.enableButtons(true);
                    }
                }
            }

            @Override
            public void onShowUpdatesButtonClicked(SelectionEvent e, FeaturesUpdatesNotificationForm form) {
                final FeaturesManagerRuntimeData data = runtimeData;
                data.getDialog().close();

                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        data.setFirstShowTab(FeaturesManagerRuntimeData.TAB_UPDATE);
                        FeaturesManagerWizard wizard = new FeaturesManagerWizard(data);
                        wizard.show(DisplayUtils.getDefaultShell());
                    }
                });
            }

            @Override
            public void close() {
                runtimeData.getDialog().close();
            }
        });
        return panel;
    }

    private Color getBackgroundColor() {
        return Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
    }

    @Override
    public boolean close() {
        if (notificationForm.isExecuting()) {
            MessageDialog.openInformation(getShell(),
                    Messages.getString("ComponentsManager.form.updates.notification.dialog.label"), //$NON-NLS-1$
                    Messages.getString("ComponentsManager.form.updates.notification.dialog.description")); //$NON-NLS-1$
            return false;
        } else {
            return super.close();
        }
    }
}
