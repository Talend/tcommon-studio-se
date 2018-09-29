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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.updates.runtime.EUpdatesImage;
import org.talend.updates.runtime.engine.P2Manager;
import org.talend.updates.runtime.feature.ImageFactory;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.InstallationStatus;
import org.talend.updates.runtime.model.InstallationStatus.Status;
import org.talend.updates.runtime.preference.UpdatesRuntimePreference;
import org.talend.updates.runtime.preference.UpdatesRuntimePreferenceConstants;
import org.talend.updates.runtime.ui.feature.model.IFeatureInfo;
import org.talend.updates.runtime.ui.feature.model.IFeatureItem;
import org.talend.updates.runtime.ui.feature.model.Message;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.util.UIUtils;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractFeatureListInfoItem<T extends IFeatureInfo> extends AbstractControlListItem<T> {

    protected static final String BLANK = " "; //$NON-NLS-1$

    private Object compImageLock;

    private Label imageLabel;

    private Label titleLabel;

    /**
     * used to center the image
     */
    private Label verticalLine;

    private Label horizonLine;

    private StyledText descText;

    private StyledText versionText;

    private StyledText requiredStudioVersionText;

    private Image compImage;

    private Composite installationPanel;

    public AbstractFeatureListInfoItem(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, T element) {
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
            // layoutData.height = 150;
            layoutData.height = 120;
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

        versionText = new StyledText(panel, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.NO_FOCUS);
        versionText.setCursor(Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW));
        versionText.setEditable(false);
        versionText.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            @Override
            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_LABEL;
            }
        });

        requiredStudioVersionText = new StyledText(panel, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.NO_FOCUS);
        requiredStudioVersionText.setCursor(Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW));
        requiredStudioVersionText.setEditable(false);
        requiredStudioVersionText.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            @Override
            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_LABEL;
            }
        });

        installationPanel = createInstallationPanel(panel);
    }

    abstract protected Composite createInstallationPanel(Composite panel);

    protected Composite getInstallationPanel() {
        return installationPanel;
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
        formData.left = new FormAttachment(imageLabel, horizonAlignWidth * 2, SWT.RIGHT);
        formData.right = new FormAttachment(100, 0);
        titleLabel.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(titleLabel, verticalAlignHeight, SWT.BOTTOM);
        formData.left = new FormAttachment(titleLabel, 0, SWT.LEFT);
        formData.right = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(installationPanel, -1 * verticalAlignHeight, SWT.TOP);
        descText.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(installationPanel, 0, SWT.CENTER);
        formData.left = new FormAttachment(descText, 0, SWT.LEFT);
        formData.right = new FormAttachment(requiredStudioVersionText, -1 * horizonAlignWidth, SWT.LEFT);
        versionText.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(installationPanel, 0, SWT.CENTER);
        formData.left = new FormAttachment(33, 0);
        formData.right = new FormAttachment(installationPanel, -1 * horizonAlignWidth, SWT.LEFT);
        requiredStudioVersionText.setLayoutData(formData);

        formData = new FormData();
        formData.left = new FormAttachment(66, 0);
        formData.right = new FormAttachment(100, -1 * horizonAlignWidth);
        formData.bottom = new FormAttachment(horizonLine, -1 * verticalAlignHeight, SWT.TOP);
        installationPanel.setLayoutData(formData);
    }

    @Override
    protected void initData() {
        super.initData();
        loadData();
    }

    private void loadData() {
        compImageLock = new Object();
        final T cd = getData();
        if (cd != null) {
            titleLabel.setText(cd.getTitle());
            descText.setText(cd.getDescription());
            updateVersion(null);
            loadImage(cd);
        }
    }

    protected void setImage(Image image) {
        if (imageLabel == null || imageLabel.isDisposed()) {
            return;
        }
        imageLabel.setImage(image);
    }

    protected Point getImageSize() {
        // return new Point(74, 74);
        return new Point(32, 32);
    }

    protected void loadImage(final T cd) {
        if (compImage != null) {
            setImage(compImage);
        } else {
            setImage(ImageProvider.getImage(EUpdatesImage.LOADING));
            execute(new Runnable() {

                @Override
                public void run() {
                    if (AbstractFeatureListInfoItem.this == null || AbstractFeatureListInfoItem.this.isDisposed()) {
                        return;
                    }
                    if (Thread.interrupted()) {
                        return;
                    }
                    if (AbstractFeatureListInfoItem.this.isDisposed()) {
                        return;
                    }
                    synchronized (compImageLock) {
                        if (compImage == null) {
                            try {
                                Image image = cd.getImage(new NullProgressMonitor());
                                if (Thread.interrupted()) {
                                    return;
                                }
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
            });
        }

    }

    abstract protected void execute(Runnable runnable);

    protected void executeInstall(IProgressMonitor monitor, boolean runInModelContext) {
        try {
            preInstall(monitor);
            IFeatureItem featureItem = getFeatureItem();
            IRunnableWithProgress installProgress = new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    installFeature(monitor, featureItem);
                }
            };
            if (runInModelContext) {
                getCheckListener().run(true, true, installProgress);
            } else {
                installProgress.run(monitor);
            }
            afterInstalled(monitor);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @SuppressWarnings("nls")
    protected void preInstall(IProgressMonitor monitor) throws Exception {
        if (getRuntimeData().isCheckWarnDialog()) {
            checkWarnDialog(monitor);
        }
    }

    @SuppressWarnings("nls")
    private void checkWarnDialog(IProgressMonitor monitor) throws Exception {
        boolean showWarnDialog = getRuntimeData().getFeaturesManager().getProjectPrefStore()
                .getBoolean(UpdatesRuntimePreferenceConstants.SHOW_WARN_DIALOG_WHEN_INSTALLING_FEATURES);
        if (!showWarnDialog) {
            return;
        }
        Collection<Message> defaultMessages = getRuntimeData().getFeaturesManager().createDefaultMessage();
        if (defaultMessages == null || defaultMessages.isEmpty()) {
            return;
        }
        final StringBuffer strBuff = new StringBuffer();
        for (Message message : defaultMessages) {
            UIUtils.appendMessage(strBuff, null, message);
            strBuff.append("\n");
        }
        final String messageDetail = Messages.getString("ComponentsManager.form.warn.dialog.message", strBuff.toString());
        final AtomicBoolean userAgreed = new AtomicBoolean(false);
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                MessageDialogWithToggle dialog = MessageDialogWithToggle.openOkCancelConfirm(getShell(),
                        Messages.getString("ComponentsManager.form.warn.dialog.title"), messageDetail,
                        Messages.getString("ComponentsManager.form.warn.dialog.dontShowAgain"), false, null, null);
                int returnCode = dialog.getReturnCode();
                if (IDialogConstants.OK_ID == returnCode) {
                    userAgreed.set(true);
                } else {
                    userAgreed.set(false);
                }
                final ProjectPreferenceManager projectPreferenceManager = UpdatesRuntimePreference.getInstance()
                        .createProjectPreferenceManager();
                projectPreferenceManager.getPreferenceStore().setValue(
                        UpdatesRuntimePreferenceConstants.SHOW_WARN_DIALOG_WHEN_INSTALLING_FEATURES, !dialog.getToggleState());
                if (projectPreferenceManager.getPreferenceStore().needsSaving()) {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(new RepositoryWorkUnit<Object>(
                                    ProjectManager.getInstance().getCurrentProject(),
                                    Messages.getString("ComponentsManager.repositoryWorkUnit.showWarnDialog.dontShowAgain")) {

                                @Override
                                protected void run() throws LoginException, PersistenceException {
                                    projectPreferenceManager.save();
                                }
                            });
                        }
                    }).start();
                }
            }
        });
        if (!userAgreed.get()) {
            throw new InterruptedException(Messages.getString("ComponentsManager.form.warn.dialog.cancelled"));
        }
    }

    public void afterInstalled(IProgressMonitor monitor) {
        P2Manager.getInstance().clear();
        InstallationStatus installationStatus = checkInstallation();
        if (installationStatus.getStatus().isInstalled()) {
            try {
                ExtraFeature feature = getFeatureItem().getFeature();
                feature.syncComponentsToInstalledFolder(monitor, feature.getStorage().getFeatureFile(monitor));
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    protected void installFeature(IProgressMonitor monitor, final IFeatureItem featureItem) {
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
                                Messages.getString("ComponentsManager.form.install.dialog.cantInstall.title"), //$NON-NLS-1$
                                Messages.getString("ComponentsManager.form.install.dialog.cantInstall.message", //$NON-NLS-1$
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
                getRuntimeData().getInstalledFeatures().add(feature);
                break;
            default:
                Display.getDefault().asyncExec(new Runnable() {

                    @Override
                    public void run() {
                        Throwable ex = status.getException();
                        String message = PathUtils.getMessage(status, true);

                        ExceptionMessageDialog.openError(getShell(),
                                Messages.getString("ComponentsManager.form.install.dialog.failed.title"), //$NON-NLS-1$
                                Messages.getString("ComponentsManager.form.install.dialog.failed.message", feature.getName(), //$NON-NLS-1$
                                        message),
                                ex);
                    }
                });
                break;
            }
        }
    }

    protected InstallationStatus checkInstallation() {
        ExtraFeature feature = getFeatureItem().getFeature();
        InstallationStatus result = null;
        try {
            result = feature.getInstallationStatus(new NullProgressMonitor());
        } catch (Exception e) {
            result = new InstallationStatus(Status.UNKNOWN);
            ExceptionHandler.process(e);
        }
        final InstallationStatus installationStatus = result;
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                if (getInstallationPanel().isDisposed()) {
                    return;
                }
                updateVersion(installationStatus);
                updateRequiredStudioVersion(installationStatus);
                updateInstallationButtons(installationStatus);
                getInstallationPanel().layout();
            }
        });

        return result;
    }

    protected void updateVersion(InstallationStatus installationStatus) {
        if (installationStatus == null) {
            installationStatus = new InstallationStatus(Status.UNKNOWN);
        }
        String versionLabel = Messages.getString("ComponentsManager.form.install.label.version") + BLANK; //$NON-NLS-1$
        Point versionLabelPosition = new Point(0, versionLabel.length());
        List<StyleRange> ranges = new ArrayList<>();
        Display display = Display.getDefault();
        StyleRange versionLabelStyle = new StyleRange(versionLabelPosition.x, versionLabelPosition.y,
                display.getSystemColor(SWT.COLOR_BLACK), null, SWT.BOLD);
        ranges.add(versionLabelStyle);
        Status status = installationStatus.getStatus();
        String version = versionLabel;
        ExtraFeature feature = getFeatureItem().getFeature();
        String newVersion = feature.getVersion();
        if (status.isInstalled() && status.canBeInstalled()) {
            Point newVersionPosition = new Point(version.length(), newVersion.length());
            StyleRange newVersionStyle = new StyleRange(newVersionPosition.x, newVersionPosition.y,
                    display.getSystemColor(SWT.COLOR_BLACK), null, SWT.NORMAL);
            ranges.add(newVersionStyle);
            version = version + newVersion;

            boolean isPatch = PathUtils.getAllTypeCategories(feature.getTypes()).contains(Type.PATCH);
            if (!isPatch) {
                /**
                 * Patch version string is too long so that it may has display issue, so just skip it
                 */
                String installedVersion = installationStatus.getInstalledVersion();
                if (installedVersion == null) {
                    installedVersion = BLANK;
                }
                version = version + BLANK;

                Point currentVersionPosition = new Point(version.length(), installedVersion.length());
                StyleRange currentVersionStyle = new StyleRange(currentVersionPosition.x, currentVersionPosition.y,
                        display.getSystemColor(SWT.COLOR_GRAY), null, SWT.NORMAL);
                currentVersionStyle.strikeout = true;
                ranges.add(currentVersionStyle);
                version = version + installedVersion;
            }
        } else {
            Point newVersionPosition = new Point(version.length(), newVersion.length());
            StyleRange newVersionStyle = new StyleRange(newVersionPosition.x, newVersionPosition.y,
                    display.getSystemColor(SWT.COLOR_BLACK), null, SWT.NORMAL);
            ranges.add(newVersionStyle);
            version = version + newVersion;
        }
        versionText.setText(version);
        versionText.setStyleRanges(ranges.toArray(new StyleRange[0]));
    }

    protected void updateRequiredStudioVersion(InstallationStatus installationStatus) {
        ExtraFeature feature = getFeatureItem().getFeature();
        boolean showText = !installationStatus.isCompatible();
        if (showText) {
            String compatibleStudioVersionStr = feature.getCompatibleStudioVersion() + " +"; //$NON-NLS-1$
            String requiredVersion = null;
            String versionLabel = Messages.getString("ComponentsManager.form.install.label.requiredStudioVersion") + BLANK; //$NON-NLS-1$
            requiredVersion = versionLabel;
            Point versionLabelPosition = new Point(0, versionLabel.length());
            List<StyleRange> ranges = new ArrayList<>();
            Display display = Display.getDefault();
            StyleRange versionLabelStyle = new StyleRange(versionLabelPosition.x, versionLabelPosition.y,
                    display.getSystemColor(SWT.COLOR_BLACK), null, SWT.BOLD);
            ranges.add(versionLabelStyle);

            Point currentVersionPosition = new Point(requiredVersion.length(), compatibleStudioVersionStr.length());
            StyleRange currentVersionStyle = new StyleRange(currentVersionPosition.x, currentVersionPosition.y,
                    display.getSystemColor(SWT.COLOR_RED), null, SWT.NORMAL);
            ranges.add(currentVersionStyle);
            requiredVersion = requiredVersion + compatibleStudioVersionStr;
            requiredStudioVersionText.setText(requiredVersion);
            requiredStudioVersionText.setVisible(true);
            requiredStudioVersionText.setStyleRanges(ranges.toArray(new StyleRange[0]));
        } else {
            requiredStudioVersionText.setVisible(false);
        }
    }

    abstract protected void updateInstallationButtons(InstallationStatus installationStatus);

    protected String getInstallationButtonLabel(InstallationStatus installationStatus) {
        if (installationStatus == null) {
            installationStatus = new InstallationStatus(Status.UNKNOWN);
        }
        if (!installationStatus.isCompatible()) {
            return Messages.getString("ComponentsManager.form.install.label.inCompatible"); //$NON-NLS-1$
        }
        String label = null;
        Status status = installationStatus.getStatus();
        switch (status) {
        case CANT_INSTALL:
            label = Messages.getString("ComponentsManager.form.install.label.cantInstall"); //$NON-NLS-1$
            break;
        case DEGRADABLE:
            label = Messages.getString("ComponentsManager.form.install.label.degrade"); //$NON-NLS-1$
            break;
        case INSTALLABLE:
            label = Messages.getString("ComponentsManager.form.install.label.install"); //$NON-NLS-1$
            break;
        case INSTALLED:
            label = Messages.getString("ComponentsManager.form.install.label.installed"); //$NON-NLS-1$
            break;
        case RE_INSTALLABLE:
            label = Messages.getString("ComponentsManager.form.install.label.reInstall"); //$NON-NLS-1$
            break;
        case UPDATABLE:
            label = Messages.getString("ComponentsManager.form.install.label.update"); //$NON-NLS-1$
            break;
        case UNKNOWN:
            label = Messages.getString("ComponentsManager.form.install.label.unknown"); //$NON-NLS-1$
        default:
            break;
        }
        if (label == null) {
            label = Messages.getString("ComponentsManager.form.install.label.unknown"); //$NON-NLS-1$
        }
        return label;
    }
}
