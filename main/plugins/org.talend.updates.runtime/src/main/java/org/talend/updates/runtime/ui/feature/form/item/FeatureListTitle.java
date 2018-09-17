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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.talend.commons.ui.swt.composites.GradientCanvas;
import org.talend.updates.runtime.ui.feature.model.IFeatureTitle;
import org.talend.updates.runtime.ui.feature.model.Message;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.util.UIUtils;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListTitle extends AbstractControlListItem<IFeatureTitle> {

    private Label verticalLine;

    private Label titleLabel;

    private StyledText messageText;

    private GradientCanvas titleBackgroundPanel;

    public FeatureListTitle(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureTitle element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createPanel() {
        Composite panel = new Composite(this, SWT.NONE);

        FormData formData = new FormData();
        formData.height = 50;
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        panel.setLayoutData(formData);

        FormLayout layout = new FormLayout();
        panel.setLayout(layout);
        return panel;
    }

    @Override
    protected void initControl(Composite parent) {
        super.initControl(parent);

        FormColors formColors = UIUtils.getFormColors();
        titleBackgroundPanel = new GradientCanvas(parent, SWT.NONE);
        titleBackgroundPanel.setSeparatorVisible(true);
        titleBackgroundPanel.setSeparatorAlignment(SWT.TOP);
        titleBackgroundPanel.setBackgroundGradient(new Color[] { formColors.getColor(IFormColors.H_GRADIENT_END),
                formColors.getColor(IFormColors.H_GRADIENT_START) }, new int[] { 100 }, true);
        titleBackgroundPanel.putColor(GradientCanvas.H_BOTTOM_KEYLINE1, formColors.getColor(IFormColors.H_GRADIENT_END));
        titleBackgroundPanel.putColor(GradientCanvas.H_BOTTOM_KEYLINE2, formColors.getColor(IFormColors.H_GRADIENT_START));

        verticalLine = new Label(titleBackgroundPanel, SWT.NONE);

        titleLabel = new Label(titleBackgroundPanel, SWT.NONE);
        titleLabel.setFont(getTitleFont());

        messageText = new StyledText(titleBackgroundPanel, SWT.RIGHT | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.NO_FOCUS);
        messageText.setCursor(Display.getDefault().getSystemCursor(SWT.CURSOR_ARROW));
        messageText.setEditable(false);
        messageText.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

            @Override
            public void getRole(AccessibleControlEvent e) {
                e.detail = ACC.ROLE_LABEL;
            }
        });
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        titleBackgroundPanel.setLayout(new FormLayout());

        final int horizonAlignWidth = getHorizonAlignWidth();

        FormData formData = null;

        formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        titleBackgroundPanel.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(0, 0);
        verticalLine.setLayoutData(formData);

        formData = new FormData();
        formData.top = new FormAttachment(verticalLine, 0, SWT.CENTER);
        formData.left = new FormAttachment(verticalLine, 10, SWT.RIGHT);
        titleLabel.setLayoutData(formData);

        formData = new FormData();
        formData.bottom = new FormAttachment(titleLabel, 0, SWT.BOTTOM);
        formData.left = new FormAttachment(titleLabel, horizonAlignWidth, SWT.RIGHT);
        formData.right = new FormAttachment(100, -10);
        messageText.setLayoutData(formData);
    }

    @Override
    protected void initData() {
        super.initData();
        titleLabel.setText(getData().getTitle());
        updateMessage();
    }

    @SuppressWarnings("nls")
    private void updateMessage() {
        IFeatureTitle featureTitle = getData();
        if (featureTitle == null) {
            return;
        }
        Collection<Message> messages = featureTitle.getMessages();
        if (messages == null || messages.isEmpty()) {
            return;
        }
        StringBuffer strBuff = new StringBuffer();
        Collection<StyleRange> styles = new ArrayList<>();
        for (Message message : messages) {
            if (0 < strBuff.length()) {
                strBuff.append("\n");
            }
            UIUtils.appendMessage(strBuff, styles, message);
        }
        messageText.setText(strBuff.toString());
        messageText.setStyleRanges(styles.toArray(new StyleRange[0]));
    }

}
