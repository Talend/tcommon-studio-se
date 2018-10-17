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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.updates.runtime.ui.feature.form.FeaturesUpdatesNotificationForm;
import org.talend.updates.runtime.ui.feature.model.IFeatureUpdateNotification;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureUpdateNotificationItem extends AbstractControlListItem<IFeatureUpdateNotification> {

    private FeaturesUpdatesNotificationForm notificationForm;

    public FeatureUpdateNotificationItem(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureUpdateNotification element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createPanel() {
        Composite cPanel = new Composite(this, SWT.NONE);

        FormLayout layout = new FormLayout();
        cPanel.setLayout(layout);

        FormData layoutData = new FormData();
        layoutData.left = new FormAttachment(0, 0);
        layoutData.right = new FormAttachment(100, 0);
        layoutData.top = new FormAttachment(0, 0);
        layoutData.bottom = new FormAttachment(100, 0);
        cPanel.setLayoutData(layoutData);
        return cPanel;
    }

    @Override
    protected void initControl(Composite panel) {
        super.initControl(panel);
        notificationForm = new FeaturesUpdatesNotificationForm(panel, SWT.NONE, getRuntimeData(), getFeatureItem(), true);
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        FormData formData = null;
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        notificationForm.setLayoutData(formData);
    }
}
