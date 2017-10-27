// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
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
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class P2Installer {

    private static Logger log = Logger.getLogger(P2Installer.class);

    private File tmpInstallFolder;

    protected File getTmpInstallFolder() throws IOException {
        if (tmpInstallFolder == null || !tmpInstallFolder.exists()) {
            tmpInstallFolder = File.createTempFile("P2Installer", "updatesite"); //$NON-NLS-1$ //$NON-NLS-2$
            tmpInstallFolder.delete();
            tmpInstallFolder.mkdirs();
        }
        return tmpInstallFolder;
    }

    protected void copyConfigFile(boolean restore) throws IOException {
        File configrationFile = new File(Platform.getConfigurationLocation().getURL().getPath(), UpdatesHelper.FILE_CONFIG_INI);
        File tempConfigrationFile = new File(getTmpInstallFolder(), UpdatesHelper.FILE_CONFIG_INI);
        if (restore) {
            if (tempConfigrationFile.exists()) {
                FilesUtils.copyFile(new FileInputStream(tempConfigrationFile), configrationFile);
            }
        } else {
            if (configrationFile.exists()) {
                FilesUtils.copyFile(new FileInputStream(configrationFile), tempConfigrationFile);
            }
        }
    }

    public Set<InstalledUnit> installPatchFile(File updatesiteZip, boolean keepChangeConfigIni) throws Exception {
        if (updatesiteZip != null && updatesiteZip.exists() && updatesiteZip.isFile()
                && updatesiteZip.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {

            final File tmpFolder = org.talend.utils.files.FileUtils.createTmpFolder("p2Installer", "update"); //$NON-NLS-1$  //$NON-NLS-2$
            try {
                FilesUtils.unzip(updatesiteZip.getAbsolutePath(), tmpFolder.getAbsolutePath());
                final File[] updateFolders = findUpdateFolders(tmpFolder);
                Set<InstalledUnit> installed = new HashSet<InstalledUnit>();
                if (updateFolders != null && updateFolders.length > 0) {
                    for (File f : updateFolders) {
                        if (f.isDirectory()) { // only deal with dir, because have been de-compress before.
                            installed.addAll(installPatchFolder(f, keepChangeConfigIni));
                        }
                    }
                }
                return installed;
            } finally {
                FilesUtils.deleteFolder(tmpFolder, true);
            }
        }
        return Collections.emptySet();
    }

    protected File[] findUpdateFolders(File baseFolder) {
        return UpdatesHelper.findUpdateFiles(baseFolder);
    }

    public Set<InstalledUnit> installPatchFolder(File updatesiteFolder, boolean keepChangeConfigIni) throws IOException,
            ProvisionException {
        if (updatesiteFolder != null && updatesiteFolder.exists() && updatesiteFolder.isDirectory()) {
            if (!keepChangeConfigIni) {
                copyConfigFile(false);
            }
            try {
                /** The metadata repository and the artifact repository should be same **/
                return installPatchRepository(updatesiteFolder, updatesiteFolder);
            } finally {// to be sure to place the file back untouched.
                if (!keepChangeConfigIni) {
                    copyConfigFile(true);
                    FilesUtils.deleteFolder(getTmpInstallFolder(), true);
                }
            }
        }
        return Collections.emptySet();
    }

    public void clean() {
        try {
            FilesUtils.deleteFolder(getTmpInstallFolder(), true);
            tmpInstallFolder = null;
        } catch (IOException e) {
            //
        }
    }

    /**
     * 
     * FIXME, Copied from the class @link NexusUpdateSiteManager#installPatchesByP2
     */
    protected Set<InstalledUnit> installPatchRepository(File metadataRepository, File artifactRepository)
            throws ProvisionException {
        NullProgressMonitor nullProgressMonitor = new NullProgressMonitor();
        String newProductVersion = "";
        Bundle bundle = FrameworkUtil.getBundle(this.getClass());
        BundleContext context = bundle.getBundleContext();

        ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);

        IProvisioningAgentProvider agentProvider = null;
        IProvisioningAgent agent = null;
        if (sr == null) {
            return Collections.emptySet();
        }
        agentProvider = (IProvisioningAgentProvider) context.getService(sr);

        try {
            agent = agentProvider.createAgent(null);
            log.debug("agent created:" + agent);
            // get the repository managers and define our repositories
            IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent
                    .getService(IMetadataRepositoryManager.SERVICE_NAME);
            IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
                    .getService(IArtifactRepositoryManager.SERVICE_NAME);

            manager.addRepository(metadataRepository.toURI());
            artifactManager.addRepository(artifactRepository.toURI());

            IMetadataRepository metadataRepo = manager.loadRepository(metadataRepository.toURI(), nullProgressMonitor);
            Set<IInstallableUnit> toInstall = metadataRepo.query(QueryUtil.createIUAnyQuery(), nullProgressMonitor)
                    .toUnmodifiableSet();
            // show the installation unit
            log.debug("ius to be installed:" + toInstall);
            if (!toInstall.isEmpty()) {
                newProductVersion = toInstall.iterator().next().getVersion().toString();
            }

            setIuSingletonToFalse(toInstall);
            makeInstalledIuSingletonFrom(toInstall, agent);

            // install
            InstallOperation installOperation = new InstallOperation(new ProvisioningSession(agent), toInstall);
            IStatus installResolvedStatus = installOperation.resolveModal(new NullProgressMonitor());
            if (installResolvedStatus.getSeverity() == IStatus.ERROR) {
                log.error("error installing new plugins :" + installOperation.getResolutionDetails());
                throw new ProvisionException(installResolvedStatus);
            }// else perform the installlation

            IPhaseSet talendPhaseSet = PhaseSetFactory
                    .createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST });

            ProfileModificationJob provisioningJob = (ProfileModificationJob) installOperation
                    .getProvisioningJob(new NullProgressMonitor());
            if (provisioningJob == null) {
                log.error("error installing new plugins :" + installOperation.getResolutionDetails());
                throw new ProvisionException(installResolvedStatus);
            }
            provisioningJob.setPhaseSet(talendPhaseSet);
            IStatus status = provisioningJob.run(new NullProgressMonitor());
            log.info("provisionning status is :" + status);
            // }// else IUs cannot be installed so tell the user.
            Set<InstalledUnit> installedUnits = new HashSet<InstalledUnit>();
            for (IInstallableUnit unit : toInstall) {
                installedUnits.add(new InstalledUnit(unit.getId(), unit.getVersion().toString()));
            }
            return installedUnits;
        } finally {
            agent.stop();
        }
    }

    /**
     * look for all {@link IInstallableUnit} and check that they are of type {@link InstallableUnit}. If that is so,
     * then their singletom state is set to false. WARNING : internal APIs of p2 are used because I could not find any
     * way around the limitation of P2 that does not allow 2 singletons to be deployed at the same time
     * 
     * @param toInstall a set of {@link IInstallableUnit} to be set as not a singleton
     */
    private void setIuSingletonToFalse(Set<IInstallableUnit> toInstall) {
        for (IInstallableUnit iu : toInstall) {
            if (iu instanceof InstallableUnit) {
                ((InstallableUnit) iu).setSingleton(false);
            }// else not a IU supporting singleton so ignor.
        }
    }

    /**
     * look for all {@link IInstallableUnit} in the current installed p2 profile that have the same id as the toInstall
     * IUs. then their state is forced to be singleton=false so that multiple singleton may be installed.
     * 
     * @param toInstall a set of {@link IInstallableUnit} to get the Id from
     * @param agent to get the current installed IUs
     */
    private void makeInstalledIuSingletonFrom(Set<IInstallableUnit> toInstall, IProvisioningAgent agent) {
        IProfileRegistry profRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
        IProfile profile = profRegistry.getProfile("_SELF_"); //$NON-NLS-1$
        HashSet<IQuery<IInstallableUnit>> queryCollection = new HashSet<IQuery<IInstallableUnit>>();
        for (IInstallableUnit toBeInstalled : toInstall) {
            IQuery<IInstallableUnit> iuQuery = QueryUtil.createIUQuery(toBeInstalled.getId());
            queryCollection.add(iuQuery);
        }
        IQueryResult<IInstallableUnit> profileIUToBeUpdated = profile.query(
                QueryUtil.createCompoundQuery(queryCollection, false), new NullProgressMonitor());
        setIuSingletonToFalse(profileIUToBeUpdated.toSet());
    }
}
