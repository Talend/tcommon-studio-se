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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.services.ICoreTisService;
import org.talend.updates.runtime.UpdatesRuntimePlugin;
import org.talend.updates.runtime.utils.UpdateTools;

public class PatchP2InstallManager {

    private static PatchP2InstallManager instance;

    private IProvisioningAgent agent;

    private ProvisioningSession session;

    private IPhaseSet talendPhaseSet;

    private PatchP2InstallManager() {
        agent = UpdatesRuntimePlugin.getDefault().getProvisioningAgent();
        agent.registerService(IProvisioningAgent.INSTALLER_AGENT, agent);
        // DirectorApplication.PROP_P2_PROFILE
        agent.registerService("eclipse.p2.profile", IProfileRegistry.SELF);//$NON-NLS-1$
        talendPhaseSet = PhaseSetFactory.createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST });
    }

    public static PatchP2InstallManager getInstance() {
        if (instance == null) {
            synchronized (PatchP2InstallManager.class) {
                if (instance == null) {
                    instance = new PatchP2InstallManager();
                }
            }
        }
        return instance;
    }

    public String installP2(IProgressMonitor monitor, Logger log, File installingPatchFolder,
            List<String> invalidBundleInfoList) throws Exception {
        String newProductVersion = ""; //$NON-NLS-1$
        Set<IInstallableUnit> toInstall = queryFromP2Repository(monitor, QueryUtil.createIUAnyQuery(),
                Arrays.asList(installingPatchFolder.toURI()));
        // show the installation unit
        log.debug("ius to be installed:" + toInstall);
        UpdateTools.setIuSingletonToFalse(toInstall);
        Set<IInstallableUnit> installed = UpdateTools.makeInstalledIuSingletonFrom(toInstall, agent);
        File featureIndexFile = new File(installingPatchFolder, UpdateTools.FILE_EXTRA_FEATURE_INDEX);
        Map<String, String> extraBundles = new HashMap<>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreTisService.class)) {
            ICoreTisService coreTisService = GlobalServiceRegister.getDefault().getService(ICoreTisService.class);
            try {
                extraBundles.putAll(coreTisService.getExtraBundleInfo4Patch(featureIndexFile));
            } catch (IOException e) {
                throw new ProvisionException(e.getMessage(), e.getCause());
            }
        }
        Set<String> installedBundles = installed.stream().map(IInstallableUnit::getId).collect(Collectors.toSet());

        Set<IInstallableUnit> validInstall = new HashSet<>();
        Set<IInstallableUnit> invalidInstall = new HashSet<>();
        toInstall.stream().forEach(iu -> {
            if (installedBundles.contains(iu.getId()) || extraBundles.containsKey(iu.getId())) {
                validInstall.add(iu);
            } else {
                invalidInstall.add(iu);
                if (invalidBundleInfoList != null) {
                    invalidBundleInfoList.add(iu.toString());
                }
            }
        });
        if (!invalidInstall.isEmpty()) {
            log.error("Some patches are not compatible with current product, then won't install them:\n       " + invalidInstall);
        }
        // install
        InstallOperation installOperation = new InstallOperation(getProvisioningSession(), validInstall);
        IStatus installResolvedStatus = installOperation.resolveModal(monitor);
        if (installResolvedStatus.getSeverity() == IStatus.ERROR) {
            log.error("error installing new plugins :" + installOperation.getResolutionDetails());
            throw new ProvisionException(installResolvedStatus);
        }
        ProfileModificationJob provisioningJob = (ProfileModificationJob) installOperation.getProvisioningJob(monitor);
        if (provisioningJob == null) {
            log.error("error installing new plugins :" + installOperation.getResolutionDetails());
            throw new ProvisionException(installResolvedStatus);
        }
        provisioningJob.setPhaseSet(talendPhaseSet);
        IStatus status = provisioningJob.run(monitor);
        log.info("provisionning status is :" + status);
        if (status != null) {
            switch (status.getSeverity()) {
            case IStatus.OK:
            case IStatus.INFO:
            case IStatus.WARNING:
                newProductVersion = UpdateTools.readProductVersionFromPatch(installingPatchFolder);
                UpdateTools.syncExtraFeatureIndex(installingPatchFolder);
                P2Manager.getInstance().clearOsgiCache();
                UpdateTools.syncLibraries(installingPatchFolder);
                UpdateTools.syncM2Repository(installingPatchFolder);
                UpdateTools.installCars(monitor, installingPatchFolder, false);
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreTisService.class)) {
                    ICoreTisService coreTisService = GlobalServiceRegister.getDefault().getService(ICoreTisService.class);
                    UpdateTools.collectDropBundles(validInstall, extraBundles, coreTisService.getDropBundleInfo());
                }
                break;
            }
        }
        return newProductVersion;
    }

    public Set<IInstallableUnit> queryFromP2Repository(IProgressMonitor monitor, IQuery<IInstallableUnit> query,
            List<URI> allRepoUris) {
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
        return metadataManager.query(query, monitor).toUnmodifiableSet();
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
