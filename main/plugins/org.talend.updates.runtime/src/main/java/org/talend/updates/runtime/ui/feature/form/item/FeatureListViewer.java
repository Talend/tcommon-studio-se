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
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.listviewer.ControlListItem;
import org.talend.commons.ui.swt.listviewer.ControlListViewer;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;
import org.talend.updates.runtime.ui.feature.model.IFeatureDetail;
import org.talend.updates.runtime.ui.feature.model.IFeatureNavigator;
import org.talend.updates.runtime.ui.feature.model.IFeatureProgress;
import org.talend.updates.runtime.ui.feature.model.IFeatureTitle;
import org.talend.updates.runtime.ui.feature.model.IFeatureUpdate;
import org.talend.updates.runtime.ui.feature.model.IFeatureUpdateNotification;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureListViewer extends ControlListViewer {

    private FeaturesManagerRuntimeData runtimeData;

    private ICheckListener checkListener;

    public FeatureListViewer(Composite parent, FeaturesManagerRuntimeData runtimeData, int style) {
        super(parent, style);
        this.runtimeData = runtimeData;
    }

    @Override
    protected ControlListItem<?> doCreateItem(Composite parent, Object element) {
        AbstractControlListItem<?> featureItem = null;
        if (element instanceof IFeatureDetail) {
            featureItem = new FeatureListInstallItem(parent, SWT.NONE, getRuntimeData(), (IFeatureDetail) element);
        } else if (element instanceof IFeatureTitle) {
            featureItem = new FeatureListTitle(parent, SWT.NONE, getRuntimeData(), (IFeatureTitle) element);
        } else if (element instanceof IFeatureProgress) {
            featureItem = new FeatureListProgress(parent, SWT.NONE, getRuntimeData(), (IFeatureProgress) element);
        } else if (element instanceof IFeatureNavigator) {
            featureItem = new FeatureListNavigator(parent, SWT.NONE, getRuntimeData(), (IFeatureNavigator) element);
        } else if (element instanceof IFeatureUpdateNotification) {
            featureItem = new FeatureUpdateNotificationItem(parent, SWT.NONE, getRuntimeData(), (IFeatureUpdateNotification) element);
        } else if (element instanceof IFeatureUpdate) {
            featureItem = new FeatureListUpdateItem(parent, SWT.NONE, getRuntimeData(), (IFeatureUpdate) element);
        } else {
            throw new UnsupportedOperationException("Unknown data type: " + element);
        }
        return featureItem;
    }

    public ICheckListener getCheckListener() {
        return this.checkListener;
    }

    public void setCheckListener(ICheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public FeaturesManagerRuntimeData getRuntimeData() {
        return this.runtimeData;
    }

    public void setRuntimeData(FeaturesManagerRuntimeData runtimeData) {
        this.runtimeData = runtimeData;
    }

}
