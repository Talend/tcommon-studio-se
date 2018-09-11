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

import java.util.Collection;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.interfaces.IP2Feature;
import org.talend.updates.runtime.ui.feature.form.item.AbstractFeatureListInfoItem;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractFeatureForm extends Composite {

    private FeaturesManagerRuntimeData runtimeData;

    public AbstractFeatureForm(Composite parent, int style, FeaturesManagerRuntimeData runtimeData) {
        super(parent, style);
        this.runtimeData = runtimeData;
        init();
    }

    protected void init() {
        FormLayout panelLayout = new FormLayout();
        this.setLayout(panelLayout);
        this.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        initControl(this);
        initLayout();
        initData();
        addListeners();
    }

    protected void initControl(Composite parent) {
        // nothing to do
    }

    protected void initLayout() {
        // nothing to do
    }

    protected void initData() {
        // nothing to do
    }

    protected void addListeners() {
        // nothing to do
    }

    public void onTabSelected() {
        // nothing to do
    }

    public boolean canFinish() {
        return true;
    }

    public ICheckListener getCheckListener() {
        return getRuntimeData().getCheckListener();
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

    protected int getComboWidth() {
        return 100;
    }

    protected Collection<ExtraFeature> checkFeatures(Collection<ExtraFeature> features) throws Exception {
        setUseP2Cache(features, true);
        return features;
    }

    protected void setUseP2Cache(Collection<ExtraFeature> features, boolean useP2Cache) {
        if (features != null) {
            for (ExtraFeature feature : features) {
                if (feature instanceof IP2Feature) {
                    ((IP2Feature) feature).setUseP2Cache(useP2Cache);
                }
            }
        }
    }

    protected Font getInstallButtonFont() {
        final String installBtnFontKey = AbstractFeatureListInfoItem.class.getName() + ".installButtonFont"; //$NON-NLS-1$
        FontRegistry fontRegistry = JFaceResources.getFontRegistry();
        if (!fontRegistry.hasValueFor(installBtnFontKey)) {
            FontDescriptor fontDescriptor = FontDescriptor.createFrom(JFaceResources.getDialogFont()).setStyle(SWT.BOLD);
            fontRegistry.put(installBtnFontKey, fontDescriptor.getFontData());
        }
        return fontRegistry.get(installBtnFontKey);
    }
}
