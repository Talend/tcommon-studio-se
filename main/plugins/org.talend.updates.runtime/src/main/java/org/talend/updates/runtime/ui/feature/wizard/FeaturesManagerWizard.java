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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.ImageFactory;
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
    public boolean performFinish() {
        return false;
    }

    public int show(Shell shell) {
        Rectangle clientArea = shell.getMonitor().getClientArea();
        FeaturesManagerWizardDialog dialog = new FeaturesManagerWizardDialog(shell, this);
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
        ImageFactory.getInstance().disposeFeatureImages();
        ImageFactory.getInstance().clearThreadPool();
    }
}
