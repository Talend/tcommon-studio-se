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
package org.talend.updates.runtime.ui.feature.form.item;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.updates.runtime.EUpdatesImage;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.ImageFactory;
import org.talend.updates.runtime.ui.feature.model.IFeatureDetail;
import org.talend.updates.runtime.ui.feature.model.IFeatureItem;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.util.UIUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListItem extends AbstractControlListItem<IFeatureDetail> {

    private Label imageLabel;

    private Label titleLabel;

    /**
     * used to center the image
     */
    private Label verticalLine;

    private Label horizonLine;

    private StyledText descText;

    private Button installButton;

    private Image compImage;

    private Object compImageLock = new Object();

    public FeatureListItem(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureDetail element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createPanel() {
        Composite cPanel;
        boolean useNewPanel = true;
        if (useNewPanel) {
            cPanel = new Composite(this, SWT.NONE);

            FormLayout layout = new FormLayout();
            layout.marginWidth = 5;
            cPanel.setLayout(layout);

            FormData layoutData = new FormData();
            layoutData.height = 150;
            layoutData.left = new FormAttachment(0, 0);
            layoutData.right = new FormAttachment(100, 0);
            cPanel.setLayoutData(layoutData);
        } else {
            cPanel = this;
        }
        return cPanel;
    }

    @Override
    protected void initControl(Composite panel) {
        super.initControl(panel);
        verticalLine = new Label(panel, SWT.NONE);
        horizonLine = new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL);
        imageLabel = new Label(panel, SWT.CENTER);

        titleLabel = new Label(panel, SWT.NONE);
        titleLabel.setFont(getTitleFont());

        descText = new StyledText(panel, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.NO_FOCUS);
        descText.setCursor(Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW));
        descText.setEditable(false);
        descText.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            @Override
            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_LABEL;
            }
        });
        installButton = new Button(panel, SWT.NONE);
        installButton.setText(Messages.getString("ComponentsManager.form.install.label.install")); //$NON-NLS-1$
        installButton.setFont(getInstallButtonFont());
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        final int horizonAlignWidth = getHorizonAlignWidth();
        final int verticalAlignHeight = getVerticalAlignHeight();
        FormData formData = null;

        formData = new FormData();
        formData.bottom = new FormAttachment(100, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        horizonLine.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(titleLabel, 0, SWT.TOP);
        formData.bottom = new FormAttachment(descText, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(0, 0);
        verticalLine.setLayoutData(formData);

        formData = new FormData();
        formData.left = new FormAttachment(0, horizonAlignWidth);
        formData.top = new FormAttachment(verticalLine, 0, SWT.CENTER);
        Point imageSize = getImageSize();
        formData.height = imageSize.y;
        formData.width = imageSize.x;
        imageLabel.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(0, verticalAlignHeight);
        formData.left = new FormAttachment(imageLabel, horizonAlignWidth, SWT.RIGHT);
        formData.right = new FormAttachment(100, 0);
        titleLabel.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(titleLabel, verticalAlignHeight, SWT.BOTTOM);
        formData.left = new FormAttachment(titleLabel, 0, SWT.LEFT);
        formData.right = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(installButton, -1 * verticalAlignHeight, SWT.TOP);
        descText.setLayoutData(formData);

        formData = new FormData();
        formData.right = new FormAttachment(100, -1 * horizonAlignWidth);
        formData.bottom = new FormAttachment(horizonLine, -1 * verticalAlignHeight, SWT.TOP);
        installButton.setLayoutData(formData);
    }

    @Override
    protected void addListeners() {
        super.addListeners();
        installButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onInstallButtonClicked(e);
            }
        });
    }

    private void onInstallButtonClicked(SelectionEvent e) {
        try {
            final IFeatureItem featureItem = getData();
            getCheckListener().run(true, true, new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    installFeature(monitor, featureItem);
                }
            });
        } catch (Exception ex) {
            ExceptionHandler.process(ex);
        }
    }

    @Override
    protected void refresh() {
        super.refresh();
        final IFeatureDetail cd = getData();
        if (cd != null) {
            titleLabel.setText(cd.getTitle());
            descText.setText(cd.getDescription());
            if (compImage != null) {
                setImage(compImage);
            } else {
                setImage(ImageProvider.getImage(EUpdatesImage.LOADING));
                ImageFactory.getInstance().getThreadPoolExecutor().execute(new Runnable() {

                    @Override
                    public void run() {
                        loadImage(cd);
                    }
                });
            }
        }
    }

    private void setImage(Image image) {
        if (imageLabel.isDisposed()) {
            return;
        }
        imageLabel.setImage(image);
    }
    
    private Point getImageSize() {
        return new Point(74, 74);
    }

    private Font getTitleFont() {
        final String titleFontKey = this.getClass().getName() + ".titleFont"; //$NON-NLS-1$
        FontRegistry fontRegistry = JFaceResources.getFontRegistry();
        if (!fontRegistry.hasValueFor(titleFontKey)) {
            FontDescriptor fontDescriptor = FontDescriptor.createFrom(JFaceResources.getDialogFont()).setHeight(12)
                    .setStyle(SWT.BOLD);
            fontRegistry.put(titleFontKey, fontDescriptor.getFontData());
        }
        return fontRegistry.get(titleFontKey);
    }

    private Font getInstallButtonFont() {
        final String installBtnFontKey = this.getClass().getName() + ".installButtonFont"; //$NON-NLS-1$
        FontRegistry fontRegistry = JFaceResources.getFontRegistry();
        if (!fontRegistry.hasValueFor(installBtnFontKey)) {
            FontDescriptor fontDescriptor = FontDescriptor.createFrom(JFaceResources.getDialogFont()).setStyle(SWT.BOLD);
            fontRegistry.put(installBtnFontKey, fontDescriptor.getFontData());
        }
        return fontRegistry.get(installBtnFontKey);
    }

    private void loadImage(final IFeatureDetail cd) {
        synchronized (compImageLock) {
            if (compImage == null) {
                try {
                    Image image = cd.getImage(new NullProgressMonitor());
                    if (image != null) {
                        Point imageSize = getImageSize();
                        Rectangle originalImageBound = image.getBounds();
                        if (imageSize.x < originalImageBound.width || imageSize.y < originalImageBound.height) {
                            compImage = UIUtils.scaleImage(image, imageSize.x, imageSize.y);
                            ImageFactory.getInstance().registFeatureImage(compImage);
                        } else {
                            // keep original size
                            compImage = image;
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        if (compImage != null) {
            if (Thread.interrupted()) {
                return;
            }
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    setImage(compImage);
                }
            });
        }
    }

    private void installFeature(IProgressMonitor monitor, final IFeatureItem featureItem) {
        ExtraFeature feature = featureItem.getFeature();
        monitor.beginTask(Messages.getString("ComponentsManager.form.install.progress.start", feature.getName()), //$NON-NLS-1$
                IProgressMonitor.UNKNOWN);
        IStatus installStatus = null;
        try {
            if (feature.canBeInstalled(monitor)) {
                installStatus = feature.install(monitor, null);
            } else {
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        MessageDialog.openInformation(getShell(),
                                Messages.getString("ComponentsManager.form.install.dialog.alreadyInstalled.title"), //$NON-NLS-1$
                                Messages.getString("ComponentsManager.form.install.dialog.alreadyInstalled.message", //$NON-NLS-1$
                                        feature.getName()));
                    }
                });
            }
        } catch (final Exception ex) {
            ExceptionHandler.process(ex);
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    ExceptionMessageDialog.openError(getShell(),
                            Messages.getString("ComponentsManager.form.install.dialog.exceptionOccur.title"), //$NON-NLS-1$
                            Messages.getString("ComponentsManager.form.install.dialog.exceptionOccur.message", feature.getName()), //$NON-NLS-1$
                            ex);
                }
            });
        }
        monitor.setTaskName(""); //$NON-NLS-1$
        if (installStatus != null) {
            final IStatus status = installStatus;
            switch (status.getSeverity()) {
            case IStatus.OK:
            case IStatus.INFO:
            case IStatus.WARNING:
                if (feature.needRestart()) {
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            MessageDialog.openQuestion(getShell(),
                                    Messages.getString("ComponentsManager.form.install.dialog.restart.title"), //$NON-NLS-1$
                                    Messages.getString("ComponentsManager.form.install.dialog.restart.message", //$NON-NLS-1$
                                            status.getMessage()));
                        }
                    });
                }
                break;
            default:
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        ExceptionMessageDialog.openError(getShell(),
                                Messages.getString("ComponentsManager.form.install.dialog.failed.title"), //$NON-NLS-1$
                                Messages.getString("ComponentsManager.form.install.dialog.failed.message", feature.getName(), //$NON-NLS-1$
                                        status.getMessage()),
                                status.getException());
                    }
                });
                break;
            }
        }
    }
}
