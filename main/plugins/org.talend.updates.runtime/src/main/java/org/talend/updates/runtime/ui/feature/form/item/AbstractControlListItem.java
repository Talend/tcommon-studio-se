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

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.swt.listviewer.ControlListItem;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public abstract class AbstractControlListItem<T> extends ControlListItem<T> {

    private FeaturesManagerRuntimeData runtimeData;

    private T featureItem;

    private Composite panel;

    public AbstractControlListItem(Composite parent, int style, FeaturesManagerRuntimeData runtimeData, T element) {
        super(parent, style, element);
        this.runtimeData = runtimeData;
        this.featureItem = element;
        init();
    }

    protected void init() {
        FormLayout layout = new FormLayout();
        this.setLayout(layout);

        panel = createPanel();
        initControl(panel);
        layoutControl();
        initData();
        addListeners();
    }

    abstract protected Composite createPanel();

    protected void initControl(Composite panel) {
        // nothing to do
    }

    protected void layoutControl() {
        // nothing to do
    }

    protected void initData() {
        // nothing to do
    }

    public void reload() {
        // nothing to do
    }

    protected void addListeners() {
        // nothing to do
    }

    @Override
    protected void refresh() {
        // nothing to do
    }

    protected void showError(String msg) {
        // nothing to do
    }

    protected void clearError() {
        // nothing to do
    }

    protected T getFeatureItem() {
        return featureItem;
    }

    public FeaturesManagerRuntimeData getRuntimeData() {
        return this.runtimeData;
    }

    public void setRuntimeData(FeaturesManagerRuntimeData runtimeData) {
        this.runtimeData = runtimeData;
    }

    public ICheckListener getCheckListener() {
        return getRuntimeData().getCheckListener();
    }

    protected int getHorizonAlignWidth() {
        return 5;
    }

    protected int getVerticalAlignHeight() {
        return 5;
    }

    protected Composite getPanel() {
        return panel;
    }

    protected Font getTitleFont() {
        final String titleFontKey = AbstractControlListItem.class.getName() + ".titleFont"; //$NON-NLS-1$
        FontRegistry fontRegistry = JFaceResources.getFontRegistry();
        if (!fontRegistry.hasValueFor(titleFontKey)) {
            FontDescriptor fontDescriptor = FontDescriptor.createFrom(JFaceResources.getDialogFont()).setHeight(12)
                    .setStyle(SWT.BOLD);
            fontRegistry.put(titleFontKey, fontDescriptor.getFontData());
        }
        return fontRegistry.get(titleFontKey);
    }

    protected Font getInstallButtonFont() {
        final String installBtnFontKey = AbstractControlListItem.class.getName() + ".installButtonFont"; //$NON-NLS-1$
        FontRegistry fontRegistry = JFaceResources.getFontRegistry();
        if (!fontRegistry.hasValueFor(installBtnFontKey)) {
            FontDescriptor fontDescriptor = FontDescriptor.createFrom(JFaceResources.getDialogFont()).setStyle(SWT.BOLD);
            fontRegistry.put(installBtnFontKey, fontDescriptor.getFontData());
        }
        return fontRegistry.get(installBtnFontKey);
    }

}
