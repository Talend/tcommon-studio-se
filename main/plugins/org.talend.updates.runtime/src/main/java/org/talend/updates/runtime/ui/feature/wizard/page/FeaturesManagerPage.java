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
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.commons.exception.ExceptionHandler;
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

    private Image pageTitleImage;

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
        if (pageTitleImage != null) {
            return pageTitleImage;
        }

        Image originalImage = ImageProvider.getImage(EUpdatesImage.COMPONENTS_MANAGER_BANNER);
        Rectangle originalImageSize = originalImage.getBounds();
        final int horizonWidth = 10;
        final int verticalHeight = 18;

        Image scaled = new Image(Display.getDefault(), originalImageSize.width + horizonWidth,
                originalImageSize.height + verticalHeight);
        GC gc = new GC(scaled);
        gc.setAntialias(SWT.ON);
        gc.setInterpolation(SWT.HIGH);
        gc.drawImage(originalImage, 0, 0, originalImage.getBounds().width, originalImage.getBounds().height, 0,
                verticalHeight / 2, originalImageSize.width, originalImageSize.height);

        ImageData imageData = scaled.getImageData();
        imageData.transparentPixel = imageData.palette.getPixel(new RGB(255, 255, 255));
        Image transparentImage = new Image(Display.getDefault(), imageData);

        scaled.dispose();
        gc.dispose();
        originalImage.dispose();

        pageTitleImage = transparentImage;
        return pageTitleImage;
    }

    @Override
    public void dispose() {
        try {
            super.dispose();
        } finally {
            if (pageTitleImage != null) {
                try {
                    pageTitleImage.dispose();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
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
