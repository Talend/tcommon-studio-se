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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.updates.runtime.ui.feature.model.IFeatureProgress;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListProgress extends AbstractControlListItem<IFeatureProgress> {

    private ProgressMonitorPart progressBar;

    public FeatureListProgress(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, IFeatureProgress element) {
        super(parent, style, runtimeData, element);
    }

    @Override
    protected Composite createPanel() {
        Composite panel = new Composite(this, SWT.NONE);
        FormData formData = new FormData();
        formData.top = new FormAttachment(0, 0);
        formData.bottom = new FormAttachment(100, 0);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        panel.setLayoutData(formData);
        FormLayout layout = new FormLayout();
        layout.marginWidth = getHorizonAlignWidth();
        layout.marginHeight = getVerticalAlignHeight();
        panel.setLayout(layout);
        return panel;
    }

    @Override
    protected void initControl(Composite parent) {
        super.initControl(parent);
        progressBar = new ProgressMonitorPart(parent, null, true);
        progressBar.attachToCancelComponent(null);
    }

    @Override
    protected void layoutControl() {
        super.layoutControl();
        FormData formData = null;
        final int verticalAlignHeight = getVerticalAlignHeight();

        formData = new FormData();
        formData.top = new FormAttachment(0, verticalAlignHeight);
        formData.bottom = new FormAttachment(100, -1 * verticalAlignHeight);
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        progressBar.setLayoutData(formData);

    }

    public IProgressMonitor getProgressMonitor() {
        return progressBar;
    }

    @Override
    protected void initData() {
        super.initData();
        getData().setProgressMonitor(getProgressMonitor());
    }

}
