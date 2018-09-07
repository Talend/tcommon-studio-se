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
package org.talend.updates.runtime.ui.startup;

import java.util.Collection;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.job.FeaturesCheckUpdateJob;
import org.talend.updates.runtime.ui.feature.model.runtime.FeaturesManagerRuntimeData;
import org.talend.updates.runtime.ui.feature.wizard.dialog.FeaturesUpdateNotificationDialog;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeatureCheckUpdateStartup implements IStartup {

    @Override
    public void earlyStartup() {
        if (CommonsPlugin.isHeadless()) {
            return;
        }
        final FeaturesManagerRuntimeData runtimeData = new FeaturesManagerRuntimeData();
        runtimeData.setFeaturesManager(new FeaturesManager());
        FeaturesCheckUpdateJob job = runtimeData.getCheckUpdateJob();
        try {
            job.join();
            Exception exception = job.getException();
            if (exception != null) {
                throw exception;
            }
            SearchResult searchResult = job.getSearchResult();
            if (searchResult == null) {
                return;
            }
            Collection<ExtraFeature> updateFeatures = searchResult.getCurrentPageResult();
            if (updateFeatures == null || updateFeatures.isEmpty()) {
                return;
            }
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    showNotificationDialog(runtimeData);
                }
            });
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

    private void showNotificationDialog(FeaturesManagerRuntimeData runtimeData) {
        FeaturesUpdateNotificationDialog notificationDialog = new FeaturesUpdateNotificationDialog(DisplayUtils.getDefaultShell(),
                runtimeData);
        notificationDialog.open();
    }

}
