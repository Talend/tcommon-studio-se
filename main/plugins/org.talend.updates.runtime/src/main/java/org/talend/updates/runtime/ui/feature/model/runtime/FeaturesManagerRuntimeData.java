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
package org.talend.updates.runtime.ui.feature.model.runtime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.jface.dialogs.Dialog;
import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.form.FeaturesUpdatesNotificationForm.AbstractNotificationButtonListener;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;
import org.talend.updates.runtime.ui.feature.job.FeaturesCheckUpdateJob;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManagerRuntimeData {

    public static final String TAB_INSTALLATION = "installtionTab"; //$NON-NLS-1$

    public static final String TAB_UPDATE = "updateTab"; //$NON-NLS-1$

    private FeaturesManager featuresManager;

    private ICheckListener checkListener;

    private AbstractNotificationButtonListener updateNotificationButtonListener;

    private Collection<ExtraFeature> installedFeatures = Collections.synchronizedList(new ArrayList<>());

    private FeaturesCheckUpdateJob checkUpdateJob;

    private Dialog dialog;

    private String firstShowTab = TAB_INSTALLATION;

    private Object checkUpdateJobLock = new Object();

    private boolean checkWarnDialog = true;

    public FeaturesManager getFeaturesManager() {
        return this.featuresManager;
    }

    public void setFeaturesManager(FeaturesManager featuresManager) {
        this.featuresManager = featuresManager;
    }

    public Collection<ExtraFeature> getInstalledFeatures() {
        return this.installedFeatures;
    }

    public void setInstalledFeatures(Collection<ExtraFeature> installedFeatures) {
        this.installedFeatures = installedFeatures;
    }

    public ICheckListener getCheckListener() {
        return this.checkListener;
    }

    public void setCheckListener(ICheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public AbstractNotificationButtonListener getUpdateNotificationButtonListener() {
        return this.updateNotificationButtonListener;
    }

    public void setUpdateNotificationButtonListener(AbstractNotificationButtonListener buttonListener) {
        this.updateNotificationButtonListener = buttonListener;
    }

    public Dialog getDialog() {
        return this.dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public String getFirstShowTab() {
        return this.firstShowTab;
    }

    public void setFirstShowTab(String firstShowTab) {
        this.firstShowTab = firstShowTab;
    }

    public boolean isCheckWarnDialog() {
        return this.checkWarnDialog;
    }

    public void setCheckWarnDialog(boolean checkWarnDialog) {
        this.checkWarnDialog = checkWarnDialog;
    }

    public FeaturesCheckUpdateJob getCheckUpdateJob() {
        if (checkUpdateJob != null) {
            return checkUpdateJob;
        }
        synchronized (checkUpdateJobLock) {
            if (checkUpdateJob == null) {
                checkUpdateJob = new FeaturesCheckUpdateJob(getFeaturesManager());
                checkUpdateJob.schedule();
            }
        }
        return checkUpdateJob;
    }

    public void recheckUpdate() {
        if (checkUpdateJob == null) {
            return;
        }
        synchronized (checkUpdateJobLock) {
            if (checkUpdateJob != null) {
                checkUpdateJob.cancel();
                checkUpdateJob = null;
            }
        }
    }

}
