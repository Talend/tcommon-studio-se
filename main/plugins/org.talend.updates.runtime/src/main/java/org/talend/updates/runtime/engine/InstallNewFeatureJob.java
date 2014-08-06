// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.engine;

import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureRepositories;
import org.talend.updates.runtime.model.StatusException;

/**
 * created by sgandon on 28 févr. 2013 Detailled comment
 * 
 */
public class InstallNewFeatureJob extends Job {

    private final Set<ExtraFeature> featuresToInstall;

    private final FeatureRepositories featureRepositories;

    /**
     * DOC sgandon InstallNewFeatureJob constructor comment.
     * 
     * @param name
     */
    public InstallNewFeatureJob(Set<ExtraFeature> featuresToInstall, FeatureRepositories featureRepositories) {
        super(Messages.getString("InstallNewFeatureJob.installing.talend.new.features")); //$NON-NLS-1$
        this.featuresToInstall = featuresToInstall;
        this.featureRepositories = featureRepositories;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    protected IStatus run(IProgressMonitor progress) {
        SubMonitor subMon = SubMonitor.convert(progress,
                Messages.getString("InstallNewFeatureJob.installing.talend.new.features"), featuresToInstall.size()); //$NON-NLS-1$
        MultiStatus multiStatus = new MultiStatus(Messages.getPlugiId(), IStatus.OK, null, null);
        try {
            ExtraFeaturesUpdatesFactory updatesFactory = new ExtraFeaturesUpdatesFactory();
            updatesFactory.beforeInstall();
            try {
                // back the config.ini cause the p2 update will modify it but we do not want that
                for (ExtraFeature newFeature : featuresToInstall) {
                    try {
                        // launch the update
                        multiStatus.merge(newFeature.install(subMon.newChild(1), featureRepositories.getAllRepoUris(newFeature)));
                        if (subMon.isCanceled()) {// user canceled so stop the loop and return
                            multiStatus.add(Messages.createCancelStatus(
                                    "InstallNewFeatureJob.user.cancel.installation.of.feature", //$NON-NLS-1$
                                    newFeature.getName()));
                            break;
                        }
                    } catch (Exception e) {
                        multiStatus.add(Messages.createErrorStatus(e,
                                "InstallNewFeatureJob.failed.to.install", newFeature.getName())); //$NON-NLS-1$
                    }

                }
            } finally {
                try {
                    updatesFactory.afterInstall();
                } catch (StatusException e) {
                    multiStatus.add(e.getStatus());
                }
            }
        } catch (StatusException e) {
            multiStatus.add(e.getStatus());
        }
        return multiStatus;
    }

}
