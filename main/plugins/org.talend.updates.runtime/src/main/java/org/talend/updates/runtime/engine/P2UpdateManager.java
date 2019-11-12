// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.engine.query.UserVisibleRootQuery;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.updates.runtime.P2UpdateConstants;
import org.talend.updates.runtime.UpdatesRuntimePlugin;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeatureException;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.utils.P2UpdateHelper;

public class P2UpdateManager {

    private Logger logger = Logger.getLogger(P2UpdateManager.class);

    private static P2UpdateManager instance;

    private IProvisioningAgent agent;

    private ProvisioningSession session;

    private IPhaseSet talendPhaseSet;

    private P2UpdateManager() {
        agent = UpdatesRuntimePlugin.getDefault().getProvisioningAgent();
        agent.registerService(IProvisioningAgent.INSTALLER_AGENT, agent);
        // DirectorApplication.PROP_P2_PROFILE
        agent.registerService("eclipse.p2.profile", IProfileRegistry.SELF);//$NON-NLS-1$
        talendPhaseSet = PhaseSetFactory.createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST });
    }

    public static P2UpdateManager getInstance() {
        if (instance == null) {
            synchronized (P2UpdateManager.class) {
                if (instance == null) {
                    instance = new P2UpdateManager();
                }
            }
        }
        return instance;
    }

    public IStatus execute(final IProgressMonitor monitor) throws ExtraFeatureException { // check p2 repository url
        if (P2UpdateHelper.getP2RepositoryURI() == null) {
            return Messages.createErrorStatus(null, "P2UpdateManager.p2.uri.null"); //$NON-NLS-1$
        }
        // check features to install/update or TODO filter available features in license
        List<String> featuresToInstall = P2UpdateHelper.getConfigFeatures(P2UpdateConstants.KEY_FEATURES_TO_INSTALL);
        List<String> featuresToUpdate = P2UpdateHelper.getConfigFeatures(P2UpdateConstants.KEY_FEATURES_TO_UPDATE);
        if (featuresToInstall.isEmpty() && featuresToUpdate.isEmpty()) {
            return Messages.createErrorStatus(null, "P2UpdateManager.p2.feature.null"); //$NON-NLS-1$
        }
        IStatus result = null;
        File configIniBackupFile = null;
        try {
            // backup the config.ini
            configIniBackupFile = P2UpdateHelper.backupConfigFile();
            if (!featuresToInstall.isEmpty()) {
                result = doInstall(monitor, featuresToInstall);
                return result;
            }
            result = doUpdate(monitor, featuresToUpdate);
        } catch (IOException e) {
            throw new ExtraFeatureException(
                    new ProvisionException(Messages.createErrorStatus(e, "ExtraFeaturesFactory.restore.config.error"))); //$NON-NLS-1$
        } finally {
            boolean success = false;
            if (result != null) {
                switch (result.getSeverity()) {
                case IStatus.OK:
                case IStatus.INFO:
                case IStatus.WARNING:
                    success = true;
                    break;
                default:
                    success = false;
                    break;
                }
            }
            P2UpdateHelper.clearOsgiCache();
            if (success) {
                // restore the config.ini
                if (configIniBackupFile != null) {
                    try {
                        P2UpdateHelper.restoreConfigFile(configIniBackupFile);
                    } catch (IOException e) {
                        throw new P2ExtraFeatureException(
                                new ProvisionException(Messages.createErrorStatus(e, "ExtraFeaturesFactory.back.config.error"))); //$NON-NLS-1$
                    }
                }
                if (!featuresToInstall.isEmpty()) {
                    P2UpdateHelper.clearConfigFeatures(P2UpdateConstants.KEY_FEATURES_TO_INSTALL);
                } else if (!featuresToUpdate.isEmpty()) {
                    P2UpdateHelper.clearConfigFeatures(P2UpdateConstants.KEY_FEATURES_TO_UPDATE);
                }
            }
        }
        return result;
    }

    private IStatus doInstall(IProgressMonitor monitor, List<String> featuresToInstall) throws P2ExtraFeatureException {
        SubMonitor subMonitor = SubMonitor.convert(monitor, 4);
        subMonitor.setTaskName(Messages.getString("P2UpdateManager.update.product")); //$NON-NLS-1$
        // reset isInstalled to make is compute the next time is it used
        List<URI> allRepoUris = new ArrayList<>();
        allRepoUris.add(P2UpdateHelper.getP2RepositoryURI());
        subMonitor.setTaskName(Messages.getString("ExtraFeature.searching.talend.features.label")); //$NON-NLS-1$
        Set<IInstallableUnit> availableIUs = queryFromP2Repository(subMonitor.newChild(1), allRepoUris);
        // show the installation unit
        List<IInstallableUnit> toInstall = availableIUs.stream().filter(iu -> featuresToInstall.contains(iu.getId()))
                .collect(Collectors.toList());
        logger.debug("IUs to install:" + toInstall); //$NON-NLS-1$
        if (toInstall.isEmpty()) {
            return Messages.createErrorStatus(null, "P2UpdateManager.no.iu.available");
        }
        if (subMonitor.isCanceled()) {
            return Messages.createCancelStatus("P2UpdateManager.user.cancel"); //$NON-NLS-1$
        }
        InstallOperation installOperation = new InstallOperation(getProvisioningSession(), toInstall);
        IStatus installResolvedStatus = installOperation.resolveModal(subMonitor.newChild(1));
        if (installResolvedStatus.getSeverity() == IStatus.ERROR) {
            return Messages.createErrorStatus(null, "P2UpdateManager.update.error", //$NON-NLS-1$
                    installOperation.getResolutionDetails());
        }
        ProfileModificationJob provisioningJob = (ProfileModificationJob) installOperation
                .getProvisioningJob(subMonitor.newChild(1));
        if (subMonitor.isCanceled()) {
            return Messages.createCancelStatus("P2UpdateManager.user.cancel"); //$NON-NLS-1$
        }
        provisioningJob.setPhaseSet(talendPhaseSet);
        IStatus status = provisioningJob.run(subMonitor.newChild(1));
        logger.debug("installed features with status :" + status); //$NON-NLS-1$
        return Messages.createOkStatus("P2UpdateManager.update.sucess"); //$NON-NLS-1$
    }

    private IStatus doUpdate(IProgressMonitor monitor, List<String> featuresToUpdate) throws P2ExtraFeatureException {
        SubMonitor subMonitor = SubMonitor.convert(monitor, 5);
        subMonitor.setTaskName(Messages.getString("P2UpdateManager.update.product")); //$NON-NLS-1$
        // reset isInstalled to make is compute the next time is it used
        IProfile p2Profile = getProfile(IProfileRegistry.SELF);
        List<URI> allRepoUris = new ArrayList<>();
        allRepoUris.add(P2UpdateHelper.getP2RepositoryURI());
        Set<IInstallableUnit> currentIUs = p2Profile.query(new UserVisibleRootQuery(), subMonitor.newChild(1))
                .toUnmodifiableSet();
        // show the installation unit
        List<IInstallableUnit> iusToUpdate = currentIUs.stream().filter(iu -> featuresToUpdate.contains(iu.getId()))
                .collect(Collectors.toList());
        logger.debug("IUs to update:" + iusToUpdate); //$NON-NLS-1$
        if (iusToUpdate.isEmpty()) {
            return Messages.createErrorStatus(null, "P2UpdateManager.no.iu.available"); //$NON-NLS-1$
        }
        // update
        UpdateOperation updateOperation = new UpdateOperation(getProvisioningSession(), iusToUpdate);
        updateOperation.getProvisioningContext().setArtifactRepositories(allRepoUris.toArray(new URI[allRepoUris.size()]));
        updateOperation.getProvisioningContext().setMetadataRepositories(allRepoUris.toArray(new URI[allRepoUris.size()]));
        updateOperation.setProfileId(IProfileRegistry.SELF);
        IStatus result = updateOperation.resolveModal(subMonitor.newChild(1));
        if (subMonitor.isCanceled()) {
            return Messages.createCancelStatus("P2UpdateManager.user.cancel"); //$NON-NLS-1$
        }
        if (result.getSeverity() == IStatus.ERROR) {
            return Messages.createErrorStatus(null, "P2UpdateManager.update.error", //$NON-NLS-1$
                    updateOperation.getResolutionDetails());
        }
        ProfileModificationJob provisioningJob = (ProfileModificationJob) updateOperation
                .getProvisioningJob(subMonitor.newChild(1));
        if (subMonitor.isCanceled()) {
            return Messages.createCancelStatus("P2UpdateManager.user.cancel"); //$NON-NLS-1$
        }
        if (provisioningJob == null) {
            return Messages.createErrorStatus(null, "P2UpdateManager.update.error", //$NON-NLS-1$
                    updateOperation.getResolutionDetails());
        }
        provisioningJob.setPhaseSet(talendPhaseSet);
        IStatus status = provisioningJob.run(subMonitor.newChild(1));
        if (subMonitor.isCanceled()) {
            return Messages.createCancelStatus("P2UpdateManager.user.cancel"); //$NON-NLS-1$
        }
        logger.debug("Updated product with status: " + status); //$NON-NLS-1$
        if (featuresToUpdate.contains(P2UpdateConstants.STUDIO_CORE_FEATURE_ID)) {
            // TODO change to the new studio.core feature.
            try {
                P2UpdateHelper.updateProductVersion(subMonitor.newChild(1), getProfile(IProfileRegistry.SELF));
                // remove .syncMarker to force to sync maven repository
                String filePath = System.getProperty("m2.syncmarker.path"); //$NON-NLS-1$
                if (filePath != null) {
                    File markerFile = new File(filePath);
                    if (markerFile.exists()) {
                        markerFile.delete();
                    }
                }
            } catch (IOException e) {
                ExceptionHandler.process(e);
            }
        }
        return Messages.createOkStatus("P2UpdateManager.update.sucess"); //$NON-NLS-1$
    }

    public Set<IInstallableUnit> queryFromP2Repository(IProgressMonitor monitor, List<URI> allRepoUris) {
        // get the repository managers and add our repository
        IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
                .getService(IMetadataRepositoryManager.SERVICE_NAME);
        IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
                .getService(IArtifactRepositoryManager.SERVICE_NAME);
        // remove existing repositories
        for (URI existingRepUri : metadataManager.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL)) {
            metadataManager.removeRepository(existingRepUri);
        }
        for (URI existingRepUri : artifactManager.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL)) {
            metadataManager.removeRepository(existingRepUri);
        }
        for (URI repoUri : allRepoUris) {
            metadataManager.addRepository(repoUri);
            artifactManager.addRepository(repoUri);
        }
        return metadataManager.query(QueryUtil.createLatestQuery(QueryUtil.createIUGroupQuery()), monitor).toUnmodifiableSet();
    }

    public ProvisioningSession getProvisioningSession() {
        if (session == null) {
            session = new ProvisioningSession(agent);
        }
        return session;
    }

    public IProfileRegistry getProfileRegistry() {
        return (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
    }

    public IProfile getProfile(String profilId) {
        return getProfileRegistry().getProfile(profilId);
    }

}
