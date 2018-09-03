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

import org.talend.updates.runtime.feature.FeaturesManager;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.ui.feature.form.listener.ICheckListener;
import org.talend.updates.runtime.ui.feature.job.FeaturesCheckUpdateJob;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class FeaturesManagerRuntimeData {

    private FeaturesManager featuresManager;

    private ICheckListener checkListener;

    private Collection<ExtraFeature> installedFeatures = Collections.synchronizedList(new ArrayList<>());

    private FeaturesCheckUpdateJob checkUpdateJob;

    private Object checkUpdateJobLock = new Object();

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
