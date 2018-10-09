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
package org.talend.updates.runtime.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.updates.runtime.engine.P2Manager;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.InstallationStatus.Status;
import org.talend.updates.runtime.model.interfaces.IP2Feature;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.service.ITaCoKitUpdateService.ICarInstallationResult;
import org.talend.updates.runtime.storage.AbstractFeatureStorage;
import org.talend.updates.runtime.storage.IFeatureStorage;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.updates.runtime.utils.TaCoKitCarUtils;
import org.talend.utils.files.FileUtils;
import org.talend.utils.io.FilesUtils;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * created by sgandon on 19 févr. 2013 This class represent an extra feature defined in the License in the document :
 * https://wiki.talend.com/x/YoVL
 *
 */
public class P2ExtraFeature extends AbstractExtraFeature implements IP2Feature {

    private String baseRepoUriStr;// default url of the remote repo where to look for the feature to install

    /**
     * default is false, in case cache can't be refreshed
     */
    private boolean useP2Cache = false;

    public P2ExtraFeature() {
        this(null, null, null, null, null, null, null, null, null, null, null, false, null, false, false);
        setNeedRestart(false);
    }

    public P2ExtraFeature(ComponentIndexBean indexBean) {
        this(indexBean.getName(), indexBean.getVersion(), indexBean.getDescription(), indexBean.getMvnURI(),
                indexBean.getImageMvnURI(), indexBean.getProduct(), indexBean.getCompatibleStudioVersion(),
                indexBean.getBundleId(), PathUtils.convert2Types(indexBean.getTypes()),
                PathUtils.convert2Categories(indexBean.getCategories()), Boolean.valueOf(indexBean.getDegradable()));
    }

    public P2ExtraFeature(final File zipFile) {
        this(new ComponentIndexManager().create(zipFile));
        setStorage(new AbstractFeatureStorage() {

            @Override
            protected File downloadImageFile(IProgressMonitor monitor) throws Exception {
                return null;
            }

            @Override
            protected File downloadFeatureFile(IProgressMonitor monitor) throws Exception {
                return zipFile;
            }
        });
    }

    public P2ExtraFeature(String name, String version, String description, String mvnUri, String imageMvnUri, String product,
            String compatibleStudioVersion, String p2IuId, Collection<Type> types, Collection<Category> categories,
            boolean degradable) {
        this(p2IuId, name, version, description, mvnUri, imageMvnUri, product, compatibleStudioVersion, null, types, categories,
                degradable, null, false, true);
    }

    public P2ExtraFeature(String p2IuId, String name, String version, String description, String mvnUri, String imageMvnUri,
            String product, String compatibleStudioVersion, FeatureCategory parentCategory, Collection<Type> types,
            Collection<Category> categories, boolean degradable, String baseRepoUriStr, boolean mustBeInstalled,
            boolean useLegacyP2Install) {
        super(p2IuId, name, version, description, mvnUri, imageMvnUri, product, compatibleStudioVersion, parentCategory, types,
                categories, degradable, mustBeInstalled, useLegacyP2Install);
        this.baseRepoUriStr = baseRepoUriStr;
    }

    public Set<IInstallableUnit> getInstalledIUs(String p2IuId2, IProgressMonitor progress) throws ProvisionException {

        SubMonitor subMonitor = SubMonitor.convert(progress, 2);
        subMonitor.setTaskName(Messages.getString("ExtraFeature.checking.is.installed", getName())); //$NON-NLS-1$
        Bundle bundle = FrameworkUtil.getBundle(org.eclipse.equinox.p2.query.QueryUtil.class);// not using this context
                                                                                              // caus junit fails
        BundleContext context = bundle.getBundleContext();

        ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
        if (sr == null) {
            throw new ProvisionException(Messages.getString("ExtraFeature.p2.agent.service.not.found"));//$NON-NLS-1$
        }

        IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) context.getService(sr);
        if (agentProvider == null) {
            throw new ProvisionException(Messages.getString("ExtraFeature.p2.agent.provider.not.found")); //$NON-NLS-1$
        }
        IProvisioningAgent agent = null;
        try {

            IQuery<IInstallableUnit> iuQuery = QueryUtil.createIUQuery(p2IuId2);
            boolean interrupted = false;
            IProfile profile = null;
            // there seems to be a bug because if the agent is created too quickly then the profile is empty.
            // so we loop until we get a proper profile
            final String p2ProfileId = getP2ProfileId();
            do {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
                if (agent != null) {
                    agent.stop();
                }
                agent = agentProvider.createAgent(getP2AgentUri());
                IProfileRegistry profRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
                profile = profRegistry.getProfile(p2ProfileId);
            } while (profile != null && profile.getTimestamp() == 0 && !interrupted && !subMonitor.isCanceled());

            if (profile == null || subMonitor.isCanceled()) {
                throw new ProvisionException("Could not find the p2 profile named " + p2ProfileId); //$NON-NLS-1$
            }
            subMonitor.worked(1);
            IQueryResult<IInstallableUnit> iuQueryResult = profile.available(iuQuery, subMonitor.newChild(1));
            if (subMonitor.isCanceled()) {
                return Collections.EMPTY_SET;
            }
            return iuQueryResult.toSet();
        } finally {
            if (agent != null) {
                agent.stop();
            }
        }
    }

    /**
     * created for JUnit test so that profile Id can be changed by overriding
     *
     * @return
     */
    public String getP2ProfileId() {
        return "_SELF_"; //$NON-NLS-1$
    }

    /**
     * create for JUnit test so that URI can be change to some other P2 repo
     *
     * @return null for using the current defined area, but may be overriden ot point to another area for JUnitTests
     */
    public URI getP2AgentUri() {
        return null;
    }

    public URI getP2RepositoryURI() {
        IFeatureStorage storage = getStorage();
        if (storage != null) {
            try {
                File featureFile = storage.getFeatureFile(new NullProgressMonitor());
                if (featureFile != null) {
                    URI repositoryURI = PathUtils.getP2RepURIFromCompFile(featureFile);
                    if (repositoryURI != null) {
                        return repositoryURI;
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return getP2RepositoryURI(null, false);
    }

    /**
     * this is the base URI set in the license.
     *
     * @return the defaultRepoUriStr
     */
    public String getBaseRepoUriString() {
        return this.baseRepoUriStr;
    }

    public void setBaseRepoUriString(String baseRepoUriStr) {
        this.baseRepoUriStr = baseRepoUriStr;
    }

    public URI getP2RepositoryURI(String key, boolean isTOS) {
        String uriString = getBaseRepoUriString();
        if (key == null) {
            key = "talend.p2.repo.url"; //$NON-NLS-1$
        }
        String p2RepoUrlFromProp = System.getProperty(key);
        if (!isTOS && p2RepoUrlFromProp != null) {
            uriString = p2RepoUrlFromProp;
        } else {
            String version = PathUtils.getTalendVersionStr();
            if (uriString == null) {
                return URI.create(version);
            }
            uriString = uriString + (uriString.endsWith("/") ? "" : "/") + version; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        return URI.create(uriString);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#install(org.eclipse.core.runtime.IProgressMonitor, java.util.List)
     */
    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws ExtraFeatureException {
        IStatus doInstallStatus = null;
        File configIniBackupFile = null;
        Map<File, File> unzippedPatches = new HashMap<>();
        try {
            if (!isUseLegacyP2Install()) {
                // backup the config.ini
                configIniBackupFile = copyConfigFile(null);
            } // else legacy p2 install will update the config.ini
            doInstallStatus = installP2(progress, allRepoUris);
            if (doInstallStatus == null || !doInstallStatus.isOK()) {
                return doInstallStatus;
            }
            unzippedPatches = unzipPatches(progress, allRepoUris);
            storeInstalledFeatureMessage();
        } catch (IOException e) {
            throw new ExtraFeatureException(
                    new ProvisionException(Messages.createErrorStatus(e, "ExtraFeaturesFactory.restore.config.error"))); //$NON-NLS-1$
        } finally {
            boolean isInstalled = false;
            if (doInstallStatus != null) {
                switch (doInstallStatus.getSeverity()) {
                case IStatus.OK:
                case IStatus.INFO:
                case IStatus.WARNING:
                    isInstalled = true;
                    break;
                default:
                    isInstalled = false;
                    break;
                }
            }
            if (isInstalled) {
                try {
                    afterInstallP2(progress, unzippedPatches);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            // restore the config.ini
            if (configIniBackupFile != null) { // must existed backup file.
                try {
                    copyConfigFile(configIniBackupFile);
                } catch (IOException e) {
                    throw new P2ExtraFeatureException(
                            new ProvisionException(Messages.createErrorStatus(e, "ExtraFeaturesFactory.back.config.error"))); //$NON-NLS-1$
                }
            }
            if (isInstalled) {
                try {
                    afterRestoreConfigFile(progress, unzippedPatches);
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
            if (unzippedPatches != null && !unzippedPatches.isEmpty()) {
                for (Map.Entry<File, File> patchEntry : unzippedPatches.entrySet()) {
                    FilesUtils.deleteFolder(patchEntry.getValue(), true);
                }
            }
        }
        return doInstallStatus;
    }

    protected void afterInstallP2(IProgressMonitor progress, Map<File, File> unzippedPatchMap) throws P2ExtraFeatureException {
        P2Manager.getInstance().clearOsgiCache();
    }

    protected void afterRestoreConfigFile(IProgressMonitor progress, Map<File, File> unzippedPatchMap)
            throws P2ExtraFeatureException {
        installCars(progress, unzippedPatchMap);
    }

    protected void storeInstalledFeatureMessage() {
        IPreferenceStore preferenceStore = PlatformUI.getPreferenceStore();
        String addons = preferenceStore.getString("ADDONS"); //$NON-NLS-1$
        JSONObject allAddons = null;
        try {
            allAddons = new JSONObject(addons);
        } catch (Exception e) {
            // the value is not set, or is empty
            allAddons = new JSONObject();
        }
        try {
            allAddons.put(getName(), ""); //$NON-NLS-1$
            preferenceStore.setValue("ADDONS", allAddons.toString()); //$NON-NLS-1$
        } catch (JSONException e) {
            ExceptionHandler.process(e);
        }
    }

    protected IStatus installP2(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
        SubMonitor subMonitor = SubMonitor.convert(progress, 5);
        subMonitor.setTaskName(Messages.getString("ExtraFeature.installing.feature", getName())); //$NON-NLS-1$
        // reset isInstalled to make is compute the next time is it used
        setIsInstalled(null);
        // we are not using this bundles context caus it fails to be aquired in junit test
        Bundle bundle = FrameworkUtil.getBundle(org.eclipse.equinox.p2.query.QueryUtil.class);
        BundleContext context = bundle.getBundleContext();

        ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
        if (sr == null) {
            return Messages.createErrorStatus(null, "ExtraFeature.p2.agent.service.not.found", getName(), //$NON-NLS-1$
                    getVersion());
        }
        IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) context.getService(sr);
        if (agentProvider == null) {
            return Messages.createErrorStatus(null, "ExtraFeature.p2.agent.provider.not.found", getName(), getVersion()); //$NON-NLS-1$
        }

        IProvisioningAgent agent = null;
        try {
            agent = agentProvider.createAgent(getP2AgentUri());

            updateRoamingProp(agent, agentProvider);

            Set<IInstallableUnit> toInstall = getInstallableIU(agent, allRepoUris, subMonitor.newChild(1));
            if (subMonitor.isCanceled()) {
                return Messages.createCancelStatus("user.cancel.installation.of.feature", //$NON-NLS-1$
                        getName());
            }
            // show the installation unit
            log.debug("ius to be installed:" + toInstall); //$NON-NLS-1$
            if (toInstall.isEmpty()) {
                return Messages.createErrorStatus(null, "ExtraFeature.could.not.find.feature", getName(), getP2IuId(), //$NON-NLS-1$
                        Arrays.toString(allRepoUris.toArray(new URI[allRepoUris.size()])));
            }

            // install
            InstallOperation installOperation = new InstallOperation(new ProvisioningSession(agent), toInstall);
            installOperation.setProfileId(getP2ProfileId());
            IStatus installResolvedStatus = installOperation.resolveModal(subMonitor.newChild(1));
            if (subMonitor.isCanceled()) {
                return Messages.createCancelStatus("user.cancel.installation.of.feature", //$NON-NLS-1$
                        getName());
            }
            if (installResolvedStatus.getSeverity() == IStatus.ERROR) {
                return Messages.createErrorStatus(null, "ExtraFeature.error.installing.new.feature", //$NON-NLS-1$
                        installOperation.getResolutionDetails());
            } // else perform the installlation
            IPhaseSet talendPhaseSet = PhaseSetFactory
                    .createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST });

            ProfileModificationJob provisioningJob = (ProfileModificationJob) installOperation.getProvisioningJob(subMonitor
                    .newChild(1));
            if (subMonitor.isCanceled()) {
                return Messages.createCancelStatus("user.cancel.installation.of.feature", //$NON-NLS-1$
                        getName());
            }
            if (provisioningJob == null) {
                return Messages.createErrorStatus(null, "ExtraFeature.error.installing.new.feature", //$NON-NLS-1$
                        installOperation.getResolutionDetails());
            }
            provisioningJob.setPhaseSet(talendPhaseSet);
            IStatus status = provisioningJob.run(subMonitor.newChild(1));
            if (subMonitor.isCanceled()) {
                return Messages.createCancelStatus("user.cancel.installation.of.feature", //$NON-NLS-1$
                        getName());
            }

            log.debug("installed extra feature " + getName() + " (" + getVersion() + ") with status :" + status); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
        } catch (URISyntaxException e) {
            return Messages.createErrorStatus(e, "ExtraFeature.error.installing.feature.uri.exception", getName(), getVersion()); //$NON-NLS-1$
        } catch (ProvisionException e) {
            return Messages.createErrorStatus(e, "ExtraFeature.error.installing.feature.uri.exception", getName(), getVersion()); //$NON-NLS-1$
        } finally {
            if (agent != null) {// agent creation did not fail
                removeAllRepositories(agent, allRepoUris);
                agent.stop();
            }
        }
        return Messages.createOkStatus("sucessfull.install.of.feature", getName()); //$NON-NLS-1$
    }

    /**
     * DOC sgandon Comment method "removeAllRepositories".
     *
     * @param agent
     * @param allRepoUris
     */
    protected void removeAllRepositories(IProvisioningAgent agent, List<URI> allRepoUris) {
        // get the repository managers and remove all repositories
        IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
                .getService(IMetadataRepositoryManager.SERVICE_NAME);
        IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
                .getService(IArtifactRepositoryManager.SERVICE_NAME);
        for (URI repoUri : allRepoUris) {
            metadataManager.removeRepository(repoUri);
            artifactManager.removeRepository(repoUri);
            // metadataManager.loadRepository(repoUri, subMonitor.newChild(1));
        }
    }

    /**
     * add the feauture repo URI to the p2 engine and return the P2 installable units by looking at each repo
     * sequentially.
     *
     * @param agent
     * @return the metadata repo to install anything in it.
     * @throws URISyntaxException if the feature remote p2 site uri is bad
     * @throws OperationCanceledException if installation was canceled
     * @throws ProvisionException if p2 repository could not be loaded
     */
    protected Set<IInstallableUnit> getInstallableIU(IProvisioningAgent agent, List<URI> allRepoUris, IProgressMonitor progress)
            throws URISyntaxException, ProvisionException, OperationCanceledException {

        if (allRepoUris.isEmpty()) {// if repo list is empty use the default URI
            allRepoUris.add(getP2RepositoryURI());
        }
        SubMonitor subMonitor = SubMonitor.convert(progress, allRepoUris.size());
        subMonitor.setTaskName(Messages.getString("ExtraFeature.searching.talend.features.label", getName())); //$NON-NLS-1$
        // get the repository managers and add our repository
        IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
                .getService(IMetadataRepositoryManager.SERVICE_NAME);
        IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
                .getService(IArtifactRepositoryManager.SERVICE_NAME);

        // create the feature query
        IQuery<IInstallableUnit> iuQuery = QueryUtil.createLatestQuery(QueryUtil.createIUQuery(getP2IuId()));

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
        if (subMonitor.isCanceled()) {
            return Collections.EMPTY_SET;
        }
        return metadataManager.query(iuQuery, progress).toUnmodifiableSet();
    }

    /**
     * Created for JUnit test so that external P2 data area does not depend on absolute location
     *
     * @param agent
     * @param agentProvider
     * @throws ProvisionException
     */
    protected void updateRoamingProp(IProvisioningAgent agent, IProvisioningAgentProvider agentProvider)
            throws ProvisionException {
        agent.registerService(IProvisioningAgent.INSTALLER_AGENT, agentProvider.createAgent(null));
        agent.registerService("eclipse.p2.profile", getP2ProfileId());//$NON-NLS-1$
    }

    /**
     * Check that the remote update site has a different version from the one already installed. If that is the case
     * then a new instance of P2ExtraFeature is create, it returns null otherwhise.
     *
     * @param progress
     * @return a new P2ExtraFeature if the update site contains a new version of the feature or null.
     */
    @Override
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws ProvisionException {
        return createFeatureIfUpdates(progress, Collections.singletonList(getP2RepositoryURI()));
    }

    /**
     * Check that the remote update site has a different version from the one already installed. If that is the case
     * then a new instance of P2ExtraFeature is create, it returns null otherwhise.
     *
     * @param allRepoUrisn list of the repos to look for an update
     * @param progress
     * @return a new P2ExtraFeature if the update site contains a new version of the feature or null.
     */
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress, List<URI> allRepoUris) throws ProvisionException {
        if (progress.isCanceled()) {
            return null;
        }
        // get the installed IUs
        SubMonitor subMonitor = SubMonitor.convert(progress, Messages.getString("ExtraFeature.checking.need.update", getName()), //$NON-NLS-1$
                2);
        subMonitor.setTaskName(Messages.getString("ExtraFeature.checking.need.update", getName())); //$NON-NLS-1$
        Bundle bundle = FrameworkUtil.getBundle(org.eclipse.equinox.p2.query.QueryUtil.class);// not using this context
                                                                                              // caus junit fails
        BundleContext context = bundle.getBundleContext();

        ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
        if (sr == null) {
            throw new ProvisionException(Messages.getString("ExtraFeature.p2.agent.service.not.found"));//$NON-NLS-1$
        }

        IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) context.getService(sr);
        if (agentProvider == null) {
            throw new ProvisionException(Messages.getString("ExtraFeature.p2.agent.provider.not.found")); //$NON-NLS-1$
        }
        IProvisioningAgent agent = null;
        try {

            IQuery<IInstallableUnit> iuQuery = QueryUtil.createIUQuery(getP2IuId());
            boolean interrupted = false;
            IProfile profile = null;
            // there seems to be a bug because if the agent is created too quickly then the profile is empty.
            // so we loop until we get a proper profile
            do {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    interrupted = true;
                }
                if (agent != null) {
                    agent.stop();
                }
                agent = agentProvider.createAgent(getP2AgentUri());
                updateRoamingProp(agent, agentProvider);
                IProfileRegistry profRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
                profile = profRegistry.getProfile(getP2ProfileId());
            } while (profile != null && profile.getTimestamp() == 0 && !interrupted && !progress.isCanceled());

            if (profile == null || subMonitor.isCanceled()) {
                throw new ProvisionException("Could not find the p2 profile named _SELF_"); //$NON-NLS-1$
            }
            subMonitor.worked(1);
            IQueryResult<IInstallableUnit> iuQueryResult = profile.available(iuQuery, subMonitor.newChild(1));
            if (subMonitor.isCanceled() || iuQueryResult.isEmpty()) {
                return null;
            }
            // now we have the IU to be updated then we check is any update is available
            log.debug("ius to be be checked for updates:" + iuQueryResult.toSet()); //$NON-NLS-1$

            // install
            UpdateOperation updateOperation = new UpdateOperation(new ProvisioningSession(agent), iuQueryResult.toSet());
            updateOperation.getProvisioningContext().setArtifactRepositories(allRepoUris.toArray(new URI[allRepoUris.size()]));
            updateOperation.getProvisioningContext().setMetadataRepositories(allRepoUris.toArray(new URI[allRepoUris.size()]));
            updateOperation.setProfileId(getP2ProfileId());
            updateOperation.resolveModal(subMonitor.newChild(1));
            if (subMonitor.isCanceled()) {
                return null;
            }

            P2ExtraFeature p2ExtraFeatureUpdate = new P2ExtraFeature();
            copyFieldInto(p2ExtraFeatureUpdate);
            Update[] selectedUpdates = updateOperation.getSelectedUpdates();
            if (selectedUpdates == null || selectedUpdates.length == 0) {
                return null;
            } // else at least one udpate is availalble.
              // take the first update.
            p2ExtraFeatureUpdate.setVersion(selectedUpdates[0].replacement.getVersion().getOriginal());
            return p2ExtraFeatureUpdate;
        } finally {
            if (agent != null) {// agent creation did not fail
                removeAllRepositories(agent, allRepoUris);
                agent.stop();
            }
        }
    }

    @Override
    public void copyFieldInto(AbstractExtraFeature p2ExtraFeatureUpdate) {
        super.copyFieldInto(p2ExtraFeatureUpdate);
        if (p2ExtraFeatureUpdate instanceof P2ExtraFeature) {
            ((P2ExtraFeature) p2ExtraFeatureUpdate).baseRepoUriStr = baseRepoUriStr;
        }
    }

    /**
     * copy the config.ini to a temporary file or vise versa is toRestore is not null
     *
     * @param toResore file to be copied to config.ini
     * @return the temporary file to restore or null if toRestore is not null
     * @throws IOException
     */
    protected File copyConfigFile(File toResore) throws IOException {
        File tempFile = null;
        try {
            File configurationFile = PathUtils.getStudioConfigFile();
            if (toResore != null) {
                FilesUtils.copyFile(new FileInputStream(toResore), configurationFile);
            } else {
                tempFile = File.createTempFile("config.ini", null); //$NON-NLS-1$
                FilesUtils.copyFile(new FileInputStream(configurationFile), tempFile);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
        return tempFile;
    }

    @Override
    public ExtraFeature getInstalledFeature(IProgressMonitor progress) throws ExtraFeatureException {
        ExtraFeature extraFeature = null;
        try {
            if (!this.isInstalled(progress)) { // new
                extraFeature = this;
            } else {// else already installed so try to find updates
                extraFeature = this.createFeatureIfUpdates(progress);
            }
        } catch (Exception e) {
            throw new ExtraFeatureException(e);
        }
        return extraFeature;
    }

    protected Map<File, File> unzipPatches(IProgressMonitor progress, List<URI> allRepoUris) throws ExtraFeatureException {
        if (allRepoUris == null || allRepoUris.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        if (progress.isCanceled()) {
            throw new OperationCanceledException();
        }
        Map<File, File> unzippedMap = new HashMap<>();
        progress.beginTask(Messages.getString("P2ExtraFeature.progress.unzipPatch"), allRepoUris.size()); //$NON-NLS-1$
        try {
            for (URI uri : allRepoUris) {
                File compFile = PathUtils.getCompFileFromP2RepURI(uri);
                if (compFile != null && compFile.exists()) {
                    // sync the component libraries
                    File tempUpdateSiteFolder = getTempUpdateSiteFolder();
                    FilesUtils.unzip(compFile.getAbsolutePath(), tempUpdateSiteFolder.getAbsolutePath());
                    unzippedMap.put(compFile, tempUpdateSiteFolder);
                    progress.worked(1);
                }
            }
            return unzippedMap;
        } catch (Exception e) {
            throw new P2ExtraFeatureException(e);
        }
    }

    protected void installCars(IProgressMonitor progress, Map<File, File> unzippedPatchMap) throws P2ExtraFeatureException {
        if (unzippedPatchMap == null || unzippedPatchMap.isEmpty()) {
            return;
        }
        if (progress.isCanceled()) {
            throw new OperationCanceledException();
        }
        try {
            for (Map.Entry<File, File> patchEntry : unzippedPatchMap.entrySet()) {
                File patchFile = patchEntry.getKey();
                File unzippedPatchFolder = patchEntry.getValue();
                installCars(progress, patchFile, unzippedPatchFolder);
            }
        } catch (Exception e) {
            throw new P2ExtraFeatureException(e);
        }
    }

    protected void installCars(IProgressMonitor progress, File zipFile, File unzippedFolder) throws Exception {
        try {
            File carFolder = new File(unzippedFolder, ITaCoKitUpdateService.FOLDER_CAR); // car
            ICarInstallationResult result = TaCoKitCarUtils.installCars(carFolder, progress);
            if (result != null) {
                if (result.needRestart()) {
                    setNeedRestart(result.needRestart());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    protected File getTempUpdateSiteFolder() {
        return FileUtils.createTmpFolder("p2updatesite", null); //$NON-NLS-1$
    }

    @Override
    public boolean canBeInstalled(IProgressMonitor progress) throws ExtraFeatureException {
        try {
            InstallationStatus installationStatus = getInstallationStatus(progress);
            return installationStatus.canBeInstalled();
        } catch (Exception e) {
            throw new ExtraFeatureException(e);
        }
    }

    @Override
    public InstallationStatus getInstallationStatus(IProgressMonitor monitor) throws Exception {
        Collection<IInstallableUnit> installedP2s = P2Manager.getInstance().getInstalledP2Feature(monitor, getP2IuId(), null,
                useP2Cache());
        if (installedP2s != null && !installedP2s.isEmpty()) {
            Version installedLatestVersion = null;
            for (IInstallableUnit installedP2 : installedP2s) {
                Version installedVersion = installedP2.getVersion();
                if (installedVersion != null) {
                    if (installedLatestVersion == null) {
                        installedLatestVersion = installedVersion;
                    } else {
                        if (installedLatestVersion.compareTo(installedVersion) < 0) {
                            installedLatestVersion = installedVersion;
                        }
                    }
                }
            }

            String installedLastestVersionStr = null;
            String version = getVersion();
            if (installedLatestVersion != null) {
                installedLastestVersionStr = installedLatestVersion.toString();
            }

            InstallationStatus status = PathUtils.getInstallationStatus(installedLastestVersionStr, version);
            status.setRequiredStudioVersion(getCompatibleStudioVersion());
            return status;

        }
        InstallationStatus status = new InstallationStatus(Status.INSTALLABLE);
        status.setRequiredStudioVersion(getCompatibleStudioVersion());
        return status;
    }

    @Override
    public void setUseP2Cache(boolean useP2Cache) {
        this.useP2Cache = useP2Cache;
    }

    @Override
    public boolean useP2Cache() {
        return this.useP2Cache;
    }
}
