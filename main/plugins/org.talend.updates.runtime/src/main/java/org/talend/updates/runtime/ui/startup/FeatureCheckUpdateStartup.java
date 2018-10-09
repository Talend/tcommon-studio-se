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

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.gmf.util.DisplayUtils;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.feature.FeaturesManager.SearchResult;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.preference.UpdatesRuntimePreference;
import org.talend.updates.runtime.preference.UpdatesRuntimePreferenceConstants;
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

        if (!needCheckUpdate()) {
            return;
        }

        final FeaturesManagerRuntimeData runtimeData = new FeaturesManagerRuntimeData();
        runtimeData.setFeaturesManager(new FeaturesManager());
        FeaturesCheckUpdateJob job = runtimeData.getCheckUpdateJob();
        boolean checkUpdateSucceed = false;
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
            checkUpdateSucceed = true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (checkUpdateSucceed) {
                saveCurrentCheckUpdateTime();
            }
        }

    }

    private boolean needCheckUpdate() {
        if (Boolean.getBoolean(Constants.ATTR_FORCE_CHECK_UPDATE)) {
            return true;
        }
        try {
            IPreferenceStore preference = UpdatesRuntimePreference.getInstance().createProjectPreferenceManager()
                    .getPreferenceStore();
            if (!preference.getBoolean(UpdatesRuntimePreferenceConstants.AUTO_CHECK_UPDATE)) {
                return false;
            }
            Date lastCheckUpdateTime = UpdatesRuntimePreference.getInstance()
                    .getDate(UpdatesRuntimePreferenceConstants.LAST_CHECK_UPDATE_TIME);
            if (lastCheckUpdateTime == null) {
                return true;
            }
            LocalDate lastLocalDate = lastCheckUpdateTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate currentLocalDate = Calendar.getInstance().getTime().toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDate();
            long days = ChronoUnit.DAYS.between(lastLocalDate, currentLocalDate);
            final int perDays = preference.getInt(UpdatesRuntimePreferenceConstants.CHECK_UPDATE_PER_DAYS);
            if (perDays <= days) {
                return true;
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    private void saveCurrentCheckUpdateTime() {
        UpdatesRuntimePreference.getInstance().setDate(UpdatesRuntimePreferenceConstants.LAST_CHECK_UPDATE_TIME,
                Calendar.getInstance().getTime());
    }

    private void showNotificationDialog(FeaturesManagerRuntimeData runtimeData) {
        FeaturesUpdateNotificationDialog notificationDialog = new FeaturesUpdateNotificationDialog(DisplayUtils.getDefaultShell(),
                runtimeData);
        notificationDialog.open();
    }

}
