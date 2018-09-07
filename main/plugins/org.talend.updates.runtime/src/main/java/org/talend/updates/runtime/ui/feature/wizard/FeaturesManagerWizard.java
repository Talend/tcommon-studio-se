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
package org.talend.updates.runtime.ui.feature.wizard;

import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.updates.runtime.engine.P2Manager;
import org.talend.updates.runtime.feature.ImageFactory;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.feature.wizard.dialog.FeaturesManagerWizardDialog;
import org.talend.updates.runtime.ui.feature.wizard.page.FeaturesManagerPage;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManagerWizard extends Wizard {

    private FeaturesManagerRuntimeData runtimeData;

    public FeaturesManagerWizard(FeaturesManagerRuntimeData runtimeData) {
        setWindowTitle(Messages.getString("ComponentsManager.wizard.title")); //$NON-NLS-1$
        this.runtimeData = runtimeData;
        setNeedsProgressMonitor(true);
        setForcePreviousAndNextButtons(true);
    }

    @Override
    public void addPages() {
        addPage(new FeaturesManagerPage(getRuntimeData()));
    }

    @Override
    public boolean canFinish() {
        IWizardPage currentPage = getContainer().getCurrentPage();
        if (currentPage instanceof FeaturesManagerPage) {
            return ((FeaturesManagerPage) currentPage).canFinish();
        } else {
            return super.canFinish();
        }
    }

    @Override
    public boolean performFinish() {
        checkInstallation();
        return true;
    }

    @Override
    public boolean performCancel() {
        checkInstallation();
        return true;
    }

    private void checkInstallation() {
        Collection<ExtraFeature> installedFeatures = getRuntimeData().getInstalledFeatures();
        boolean needRestart = false;
        for (ExtraFeature feature : installedFeatures) {
            if (feature.needRestart()) {
                needRestart = true;
                break;
            }
        }
        if (needRestart) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    boolean restart = MessageDialog.openQuestion(getShell(),
                            Messages.getString("ComponentsManager.form.install.dialog.restart.title"), //$NON-NLS-1$
                            Messages.getString("ComponentsManager.form.install.dialog.restart.message")); //$NON-NLS-1$
                    if (restart) {
                        PlatformUI.getWorkbench().restart();
                    }
                }
            });
        }
    }

    public int show(Shell shell) {
        clear();
        Rectangle clientArea = shell.getMonitor().getClientArea();
        FeaturesManagerWizardDialog dialog = new FeaturesManagerWizardDialog(shell, this);
        getRuntimeData().setDialog(dialog);
        dialog.setHelpAvailable(false);
        dialog.setPageSize(clientArea.width * 8 / 10, clientArea.height * 7 / 10);
        return dialog.open();
    }

    private FeaturesManagerRuntimeData getRuntimeData() {
        return this.runtimeData;
    }

    @Override
    public void dispose() {
        super.dispose();
        clear();
    }

    private void clear() {
        ImageFactory.getInstance().disposeFeatureImages();
        getRuntimeData().getFeaturesManager().clear();
        P2Manager.getInstance().clear();
    }
}
