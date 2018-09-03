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
package org.talend.updates.runtime.ui.feature.wizard.page;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.updates.runtime.EUpdatesImage;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.feature.form.FeaturesManagerForm;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManagerPage extends WizardPage {

    private FeaturesManagerRuntimeData runtimeData;

    private FeaturesManagerForm managerForm;

    public FeaturesManagerPage(FeaturesManagerRuntimeData runtimeData) {
        super(Messages.getString("ComponentsManager.page.manager.title")); //$NON-NLS-1$
        setDescription(Messages.getString("ComponentsManager.page.manager.desc")); //$NON-NLS-1$
        this.runtimeData = runtimeData;
        this.runtimeData.setCheckListener(createCheckListener());
    }

    @Override
    public void createControl(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        this.setControl(panel);
        panel.setLayout(new FillLayout());
        managerForm = new FeaturesManagerForm(panel, SWT.NONE, getRuntimeData());
    }

    public boolean canFinish() {
        if (managerForm != null) {
            return managerForm.canFinish();
        } else {
            return false;
        }
    }

    @Override
    public Image getImage() {
        return ImageProvider.getImage(EUpdatesImage.COMPONENTS_MANAGER_BANNER);
    }

    private FeaturesManagerRuntimeData getRuntimeData() {
        return this.runtimeData;
    }

    private ICheckListener createCheckListener() {
        ICheckListener listener = new ICheckListener() {

            @Override
            public void updateButtons() {
                getContainer().updateButtons();
            }

            @Override
            public void showMessage(String message, int level) {
                setMessage(message, level);
            }

            @Override
            public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws Exception {
                getContainer().run(fork, cancelable, runnable);
            }

            @Override
            public String getMessage() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        return listener;
    }
}
