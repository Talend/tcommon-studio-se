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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.custom.StackLayout;
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
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.updates.runtime.EUpdatesImage;
import org.talend.updates.runtime.feature.ImageFactory;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.ui.feature.model.IFeatureUpdateNotification;
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
    private Label verticalLine;

    private Label horizonLine;

    private StyledText descText;

    private Button installUpdatesButton;

    private Button showUpdatesButton;

    private Image compImage;

    private Composite panel;

    private Composite contentPanel;

    private StackLayout stackLayout;

    private ProgressMonitorPart progressBar;

    private FeaturesManagerRuntimeData runtimeData;

    private IFeatureUpdateNotification update;

    public FeaturesUpdatesNotificationForm(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureUpdateNotification update) {
        super(parent, style);
        this.runtimeData = runtimeData;
        this.update = update;
        init();
    }

    protected void init() {
        FormLayout layout = new FormLayout();
        this.setLayout(layout);
        FormData formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.bottom = new FormAttachment(100, 0);
        this.setLayoutData(formData);
        this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
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
        return cPanel;
    }

    protected void initControl(Composite panel) {
        verticalLine = new Label(panel, SWT.NONE);
        // horizonLine = new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL);
        horizonLine = new Label(panel, SWT.HORIZONTAL);
        imageLabel = new Label(panel, SWT.CENTER);

        titleLabel = new Label(panel, SWT.NONE);
        titleLabel.setFont(getTitleFont());

        contentPanel = new Composite(panel, SWT.NONE);
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
        progressBar = new ProgressMonitorPart(contentPanel, null, true);
        progressBar.attachToCancelComponent(null);

        installUpdatesButton = new Button(panel, SWT.NONE);
        installUpdatesButton.setText(Messages.getString("ComponentsManager.form.showUpdate.label.button.updateNow")); //$NON-NLS-1$
        installUpdatesButton.setFont(getInstallButtonFont());

        showUpdatesButton = new Button(panel, SWT.NONE);
        showUpdatesButton.setText(Messages.getString("ComponentsManager.form.showUpdate.label.button.showUpdates")); //$NON-NLS-1$
        showUpdatesButton.setFont(getInstallButtonFont());
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
        formData.bottom = new FormAttachment(installUpdatesButton, -1 * verticalAlignHeight, SWT.TOP);
        contentPanel.setLayoutData(formData);

        int buttonWidth = 0;
        installUpdatesButton.pack();
        showUpdatesButton.pack();
        Point installBtnSize = installUpdatesButton.getSize();
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
        installUpdatesButton.setLayoutData(formData);
        formData = new FormData();
        formData.right = new FormAttachment(installUpdatesButton, -1 * horizonAlignWidth, SWT.LEFT);
        formData.bottom = new FormAttachment(installUpdatesButton, 0, SWT.CENTER);
        formData.width = buttonWidth;
        showUpdatesButton.setLayoutData(formData);
    }

    protected void initData() {
        loadData();
    }

    protected void addListeners() {
        installUpdatesButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                onInstallUpdatesButtonClicked(e);
            }
        });
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
        final IFeatureUpdateNotification cd = getUpdate();
        if (cd != null) {
            titleLabel.setText(cd.getTitle());
            descText.setText(cd.getDescription());
            loadImage(cd);
        }
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

    private void onShowUpdatesButtonClicked(SelectionEvent e) {
        hideProgress();
    }

    private void onInstallUpdatesButtonClicked(SelectionEvent e) {
        IProgressMonitor showProgress = showProgress();
        showProgress.beginTask("Installing updates...", IProgressMonitor.UNKNOWN);
    }

    protected IFeatureUpdateNotification getUpdate() {
        return this.update;
    }

    protected FeaturesManagerRuntimeData getRuntimeData() {
        return this.runtimeData;
    }

    protected int getHorizonAlignWidth() {
        return 5;
    }

    protected int getVerticalAlignHeight() {
        return 5;
    }
}
