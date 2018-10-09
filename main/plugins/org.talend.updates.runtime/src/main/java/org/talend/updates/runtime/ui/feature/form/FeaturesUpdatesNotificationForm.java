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
package org.talend.updates.runtime.ui.feature.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
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
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.commons.ui.runtime.exception.ExceptionMessageDialog;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.updates.runtime.EUpdatesImage;
import org.talend.updates.runtime.feature.ImageFactory;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.preference.UpdatesRuntimePreference;
import org.talend.updates.runtime.preference.UpdatesRuntimePreferenceConstants;
import org.talend.updates.runtime.ui.feature.model.IFeatureUpdateNotification;
import org.talend.updates.runtime.ui.feature.model.Message;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.util.UIUtils;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesUpdatesNotificationForm extends Composite {

    private Object compImageLock;

    private Label imageLabel;

    private Label titleLabel;

    /**
     * used to center the image
     */
    private Label verticalImageBaseLine;

    private Label horizonLine;

    private StyledText descText;

    private Button dontShownAgainButton;

    private Button showUpdatesButton;

    private Button cancelButton;

    private Image compImage;

    private Composite panel;

    private Composite contentPanel;

    private StackLayout stackLayout;

    private ProgressMonitorPart progressBar;

    private FeaturesManagerRuntimeData runtimeData;

    private IFeatureUpdateNotification update;

    private boolean isExecuting = false;

    private boolean isEmbeded = false;

    public FeaturesUpdatesNotificationForm(Composite parent, int style, FeaturesManagerRuntimeData runtimeData,
            IFeatureUpdateNotification update, boolean isEmbeded) {
        super(parent, style);
        this.runtimeData = runtimeData;
        this.update = update;
        this.isEmbeded = isEmbeded;
        init();
    }

    protected void init() {
        FormLayout layout = new FormLayout();
        this.setLayout(layout);
        this.setBackground(getBackgroundColor());
        panel = createPanel();
        initControl(panel);
        layoutControl();
        initData();
        addListeners();
    }

    protected Composite createPanel() {
        Composite cPanel = new Composite(this, SWT.NONE);

        FormLayout layout = new FormLayout();
        layout.marginWidth = 5;
        cPanel.setLayout(layout);

        FormData layoutData = new FormData();
        layoutData.height = 150;
        layoutData.left = new FormAttachment(0, 0);
        layoutData.right = new FormAttachment(100, 0);
        cPanel.setLayoutData(layoutData);

        cPanel.setBackground(getBackgroundColor());
        return cPanel;
    }

    protected void initControl(Composite panel) {
        verticalImageBaseLine = new Label(panel, SWT.NONE);
        // horizonLine = new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL);
        horizonLine = new Label(panel, SWT.HORIZONTAL);
        imageLabel = new Label(panel, SWT.CENTER);
        imageLabel.setBackground(getBackgroundColor());

        titleLabel = new Label(panel, SWT.NONE);
        titleLabel.setFont(getTitleFont());
        titleLabel.setBackground(getBackgroundColor());

        contentPanel = new Composite(panel, SWT.NONE);
        contentPanel.setBackground(getBackgroundColor());
        stackLayout = new StackLayout();
        contentPanel.setLayout(stackLayout);
        descText = new StyledText(contentPanel, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.NO_FOCUS);
        descText.setCursor(Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW));
        descText.setEditable(false);
        descText.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            @Override
            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_LABEL;
            }
        });
        descText.setBackground(getBackgroundColor());
        progressBar = new ProgressMonitorPart(contentPanel, null, true) {

            @Override
            protected void initialize(Layout layout, int progressIndicatorHeight) {
                super.initialize(layout, progressIndicatorHeight);
                fLabel.setBackground(getBackgroundColor());
            }
        };
        progressBar.attachToCancelComponent(null);
        progressBar.setBackground(getBackgroundColor());

        if (!isEmbeded()) {
            cancelButton = new Button(panel, SWT.NONE);
            cancelButton.setText(Messages.getString("ComponentsManager.form.showUpdate.label.button.cancel")); //$NON-NLS-1$
            cancelButton.setFont(getInstallButtonFont());
            cancelButton.setBackground(getBackgroundColor());

            dontShownAgainButton = new Button(panel, SWT.CHECK);
            dontShownAgainButton.setText(Messages.getString("ComponentsManager.form.showUpdate.label.button.dontShowAgain")); //$NON-NLS-1$
            dontShownAgainButton.setFont(getInstallButtonFont());
            dontShownAgainButton.setBackground(getBackgroundColor());
        }

        showUpdatesButton = new Button(panel, SWT.NONE);
        showUpdatesButton.setText(Messages.getString("ComponentsManager.form.showUpdate.label.button.showUpdates")); //$NON-NLS-1$
        showUpdatesButton.setFont(getInstallButtonFont());
        showUpdatesButton.setBackground(getBackgroundColor());

    }

    protected void layoutControl() {
        final int horizonAlignWidth = getHorizonAlignWidth();
        final int verticalAlignHeight = getVerticalAlignHeight();
        FormData formData = null;

        formData = new FormData();
        formData.top = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        horizonLine.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(titleLabel, 0, SWT.TOP);
        formData.bottom = new FormAttachment(contentPanel, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(0, 0);
        verticalImageBaseLine.setLayoutData(formData);

        formData = new FormData();
        formData.left = new FormAttachment(0, horizonAlignWidth);
        formData.top = new FormAttachment(verticalImageBaseLine, 0, SWT.CENTER);
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
        formData.bottom = new FormAttachment(showUpdatesButton, -1 * verticalAlignHeight, SWT.TOP);
        contentPanel.setLayoutData(formData);

        if (isEmbeded()) {
            formData = new FormData();
            formData.right = new FormAttachment(100, -1 * horizonAlignWidth);
            formData.bottom = new FormAttachment(horizonLine, -1 * verticalAlignHeight, SWT.TOP);
            showUpdatesButton.setLayoutData(formData);
        } else {
            formData = new FormData();
            formData.top = new FormAttachment(showUpdatesButton, 0, SWT.CENTER);
            formData.left = new FormAttachment(0, horizonAlignWidth);
            dontShownAgainButton.setLayoutData(formData);

            int buttonWidth = 0;
            cancelButton.pack();
            showUpdatesButton.pack();
            Point installBtnSize = cancelButton.getSize();
            Point showBtnSize = showUpdatesButton.getSize();
            if (installBtnSize.x < showBtnSize.x) {
                buttonWidth = showBtnSize.x;
            } else {
                buttonWidth = installBtnSize.x;
            }
            buttonWidth = buttonWidth + horizonAlignWidth;
            formData = new FormData();
            formData.right = new FormAttachment(100, -1 * horizonAlignWidth);
            formData.bottom = new FormAttachment(horizonLine, -1 * verticalAlignHeight, SWT.TOP);
            formData.width = buttonWidth;
            cancelButton.setLayoutData(formData);
            formData = new FormData();
            formData.right = new FormAttachment(cancelButton, -1 * horizonAlignWidth, SWT.LEFT);
            formData.bottom = new FormAttachment(cancelButton, 0, SWT.CENTER);
            formData.width = buttonWidth;
            showUpdatesButton.setLayoutData(formData);
        }
    }

    protected void initData() {
        loadData();
    }

    protected void addListeners() {
        if (dontShownAgainButton != null) {
            dontShownAgainButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    onDontShowAgainButtonClicked(e);
                }
            });
        }
        if (cancelButton != null) {
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    onCancelButtonClicked(e);
                }
            });
        }
        showUpdatesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onShowUpdatesButtonClicked(e);
            }
        });
    }

    private void loadData() {
        stackLayout.topControl = descText;
        compImageLock = new Object();
        if (dontShownAgainButton != null) {
            dontShownAgainButton.setSelection(!getRuntimeData().getFeaturesManager().getProjectPrefStore()
                    .getBoolean(UpdatesRuntimePreferenceConstants.AUTO_CHECK_UPDATE));
        }
        final IFeatureUpdateNotification cd = getUpdate();
        if (cd != null) {
            titleLabel.setText(cd.getTitle());
            updateDescText(cd);
            loadImage(cd);
        }
    }

    @SuppressWarnings("nls")
    private void updateDescText(IFeatureUpdateNotification cd) {
        if (cd == null) {
            return;
        }
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(cd.getDescription());
        Collection<StyleRange> styleRanges = new ArrayList<>();
        if (!isEmbeded()) {
            Collection<Message> messages = cd.getMessages();
            if (messages != null && !messages.isEmpty()) {
                strBuff.append("\n");
                for (Message msg : messages) {
                    strBuff.append("\n");
                    UIUtils.appendMessage(strBuff, styleRanges, msg);
                }
            }
        }
        descText.setText(strBuff.toString());
        descText.setStyleRanges(styleRanges.toArray(new StyleRange[0]));
    }

    private void setImage(Image image) {
        if (imageLabel == null || imageLabel.isDisposed()) {
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

    private void loadImage(final IFeatureUpdateNotification cd) {

        if (compImage != null) {
            setImage(compImage);
        } else {
            synchronized (compImageLock) {
                if (compImage == null) {
                    Point imageSize = getImageSize();
                    Image image = ImageProvider.getImage(EUpdatesImage.UPDATE_BIG);
                    Rectangle originalImageBound = image.getBounds();
                    if (imageSize.x < originalImageBound.width || imageSize.y < originalImageBound.height) {
                        compImage = UIUtils.scaleImage(image, imageSize.x, imageSize.y);
                        ImageFactory.getInstance().registFeatureImage(compImage);
                    } else {
                        // keep original size
                        compImage = image;
                    }
                }
            }
            setImage(compImage);
        }

    }

    public IProgressMonitor showProgress() {
        IProgressMonitor monitor = this.progressBar;
        this.stackLayout.topControl = this.progressBar;
        this.contentPanel.layout();
        return monitor;
    }

    public void hideProgress() {
        this.stackLayout.topControl = this.descText;
        this.contentPanel.layout();
    }

    private boolean isEmbeded() {
        return this.isEmbeded;
    }

    public void enableButtons(boolean enable) {
        if (this.cancelButton != null) {
            this.cancelButton.setEnabled(enable);
        }
        this.showUpdatesButton.setEnabled(enable);
    }

    protected Color getBackgroundColor() {
        return Display.getDefault().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
    }

    private void onShowUpdatesButtonClicked(SelectionEvent e) {
        getRuntimeData().getUpdateNotificationButtonListener().onShowUpdatesButtonClicked(e, this);
    }

    private void onCancelButtonClicked(SelectionEvent e) {
        getRuntimeData().getUpdateNotificationButtonListener().close();
    }

    private void onDontShowAgainButtonClicked(SelectionEvent e) {
        if (dontShownAgainButton != null) {
            final ProjectPreferenceManager projectPreferenceManager = UpdatesRuntimePreference.getInstance()
                    .createProjectPreferenceManager();
            projectPreferenceManager.getPreferenceStore().setValue(UpdatesRuntimePreferenceConstants.AUTO_CHECK_UPDATE,
                    !dontShownAgainButton.getSelection());
            if (projectPreferenceManager.getPreferenceStore().needsSaving()) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ProxyRepositoryFactory.getInstance()
                                .executeRepositoryWorkUnit(new RepositoryWorkUnit<Object>(
                                        ProjectManager.getInstance().getCurrentProject(),
                                        Messages.getString("ComponentsManager.repositoryWorkUnit.commitChanges.dontShowAgain")) { //$NON-NLS-1$

                                    @Override
                                    protected void run() throws LoginException, PersistenceException {
                                        projectPreferenceManager.save();
                                    }
                                });
                    }
                }).start();
            }
        }
    }

    protected IFeatureUpdateNotification getUpdate() {
        return this.update;
    }

    protected FeaturesManagerRuntimeData getRuntimeData() {
        return this.runtimeData;
    }

    public void setExecuting(boolean executing) {
        this.isExecuting = executing;
    }

    public boolean isExecuting() {
        return this.isExecuting;
    }

    protected int getHorizonAlignWidth() {
        return 5;
    }

    protected int getVerticalAlignHeight() {
        return 5;
    }

    public static abstract class AbstractNotificationButtonListener {

        public void onShowUpdatesButtonClicked(SelectionEvent e, FeaturesUpdatesNotificationForm form) {
            // nothing to do
        }

        public void onInstallUpdatesButtonClicked(SelectionEvent e, FeaturesUpdatesNotificationForm form) {
            // nothing to do
        }

        public void close() {
            // nothing to do
        }

        protected void openExceptionDialog(FeaturesUpdatesNotificationForm form, Exception ex) {
            ExceptionMessageDialog.openError(getActiveShell(form),
                    Messages.getString("ComponentsManager.form.updates.notification.execute.exception.title"), //$NON-NLS-1$
                    Messages.getString("ComponentsManager.form.updates.notification.execute.exception.description"), ex); //$NON-NLS-1$
        }

        protected void installUpdates(IProgressMonitor monitor, FeaturesManagerRuntimeData runtimeData,
                FeaturesUpdatesNotificationForm form) throws Exception {
            monitor.beginTask(Messages.getString("ComponentsManager.form.updates.notification.execute.label"), //$NON-NLS-1$
                    IProgressMonitor.UNKNOWN);
            Map<ExtraFeature, IStatus> resultMap = runtimeData.getFeaturesManager().installUpdates(monitor);
            if (resultMap != null) {
                boolean needResart = false;
                boolean hasSucceedUpdate = false;
                boolean hasFailedUpdate = false;
                StringBuffer succeedBuffer = new StringBuffer();
                StringBuffer failedBuffer = new StringBuffer();
                succeedBuffer.append("\n"); //$NON-NLS-1$
                failedBuffer.append("\n"); //$NON-NLS-1$
                for (Map.Entry<ExtraFeature, IStatus> entry : resultMap.entrySet()) {

                    ExtraFeature update = entry.getKey();
                    IStatus result = entry.getValue();
                    if (result != null && update != null) {
                        switch (result.getSeverity()) {
                        case IStatus.OK:
                        case IStatus.INFO:
                        case IStatus.WARNING:
                            hasSucceedUpdate = true;
                            succeedBuffer.append("\t").append(result.getMessage()).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
                            if (update.needRestart()) {
                                // only need to check restart for successful ones
                                needResart = true;
                            }
                            break;
                        default:
                            hasFailedUpdate = true;
                            failedBuffer.append("\t").append(result.getMessage()).append("\n"); //$NON-NLS-1$//$NON-NLS-2$
                            break;
                        }
                    }
                }
                if (!hasFailedUpdate) {
                    form.setExecuting(false);
                    Display.getDefault().syncExec(new Runnable() {

                        @Override
                        public void run() {
                            close();
                        }
                    });
                }
                if (hasSucceedUpdate || hasFailedUpdate) {
                    String message = null;
                    if (hasSucceedUpdate && !hasFailedUpdate) {
                        message = Messages
                                .getString("ComponentsManager.form.updates.notification.execute.succeed.description.allSucceed"); //$NON-NLS-1$
                    } else {
                        message = Messages
                                .getString("ComponentsManager.form.updates.notification.execute.succeed.description.hasFailure"); //$NON-NLS-1$
                    }
                    String details = Messages.getString(
                            "ComponentsManager.form.updates.notification.execute.succeed.description.result", //$NON-NLS-1$
                            succeedBuffer.toString(), failedBuffer.toString());
                    String[] buttons = null;
                    if (needResart) {
                        message = message + "\n\n" + Messages.getString( //$NON-NLS-1$
                                "ComponentsManager.form.updates.notification.execute.succeed.description.restart"); //$NON-NLS-1$
                        buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };
                    } else {
                        buttons = new String[] { IDialogConstants.OK_LABEL };
                    }
                    final String msg = message;
                    final String[] btns = buttons;
                    final boolean restart = needResart;
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            ExceptionMessageDialog dialog = new ExceptionMessageDialog(getActiveShell(form),
                                    Messages.getString("ComponentsManager.form.updates.notification.execute.succeed.title"), null, //$NON-NLS-1$
                                    msg, MessageDialog.INFORMATION, btns, 0, null);
                            dialog.setExceptionString(details);
                            int userChoice = dialog.open();
                            if (restart) {
                                if (userChoice == 0) {
                                    PlatformUI.getWorkbench().restart();
                                }
                            }
                        }
                    });
                }
            }
        }

        protected Shell getActiveShell(FeaturesUpdatesNotificationForm form) {
            if (form != null && form.isDisposed()) {
                return DisplayUtils.getDefaultShell();
            } else {
                return form.getShell();
            }
        }
    }
}
