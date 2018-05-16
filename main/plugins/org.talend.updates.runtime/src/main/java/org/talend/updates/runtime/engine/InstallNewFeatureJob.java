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
package org.talend.updates.runtime.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureRepositories;
import org.talend.updates.runtime.ui.dialog.ErrorDialogWithDetailAreaAndTryAgainButton;

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
        SubMonitor subMon = SubMonitor.convert(progress, featuresToInstall.size());
        subMon.setTaskName(Messages.getString("InstallNewFeatureJob.installing.talend.new.features")); //$NON-NLS-1$
        MultiStatus multiStatus = new MultiStatus(Messages.getPlugiId(), IStatus.OK, null, null);
        // back the config.ini cause the p2 update will modify it but we do not want that
        installFeature(featuresToInstall, multiStatus, subMon);
        return multiStatus;
    }

    private void installFeature(Set<ExtraFeature> featuresToInstall, final MultiStatus multiStatus, final SubMonitor subMon) {
        final Map<ExtraFeature, Exception> failedFeature = new HashMap<ExtraFeature, Exception>();
        for (ExtraFeature newFeature : featuresToInstall) {
            try {
                // launch the update
                multiStatus.merge(newFeature.install(subMon.newChild(1), featureRepositories.getAllRepoUris(newFeature)));
                if (subMon.isCanceled()) {// user canceled so stop the loop and return
                    multiStatus.add(Messages.createCancelStatus("InstallNewFeatureJob.user.cancel.installation.of.feature", //$NON-NLS-1$
                            newFeature.getName()));
                    break;
                }
            } catch (Exception e) {
                failedFeature.put(newFeature, e);
                multiStatus.add(Messages.createErrorStatus(e, "InstallNewFeatureJob.failed.to.install", newFeature.getName())); //$NON-NLS-1$
            }

        }
        if (!failedFeature.isEmpty()) {
            final StringBuffer detailesMessage = new StringBuffer();
            for (Exception exception : failedFeature.values()) {
                detailesMessage.append(ExceptionUtils.getFullStackTrace(exception));
            }
            Display.getDefault().syncExec(new Runnable() {

                @Override
                public void run() {
                    ErrorDialogWithDetailAreaAndTryAgainButton errorDialog = new ErrorDialogWithDetailAreaAndTryAgainButton(
                            new Shell(), "org.talend.updates.runtime", Messages
                                    .getString("InstallNewFeatureJob.failed.dialog.tryagin"), detailesMessage.toString());
                    if (Window.OK == errorDialog.getCodeOfButton()) {
                        installFeature(failedFeature.keySet(), multiStatus, subMon);
                    }
                }
            });

        }
    }
}
