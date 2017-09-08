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
package org.talend.updates.runtime.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.URIUtil;
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
import org.talend.commons.utils.VersionUtils;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.utils.io.FilesUtils;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * created by sgandon on 19 févr. 2013 This class represent an extra feature defined in the License in the document :
 * https://wiki.talend.com/x/YoVL
 *
 */
public class P2ExtraFeature implements ExtraFeature {

    protected static Logger log = Logger.getLogger(P2ExtraFeature.class);

    protected String p2IuId;// p2 installable unit id

    protected String baseRepoUriStr;// default url of the remote repo where to look for the feature to install

    protected String name;// name to be displayed to the user.

    protected String description;// Description displayed to the user.

    protected String version;// version of the p2 IU

    protected Boolean isInstalled;// true is already installed in the current Studio

    protected boolean mustBeInstalled;

    protected boolean useLegacyP2Install;

    protected FeatureCategory parentCategory;

    protected P2ExtraFeature() {
        // do nothing be authorise subclasses to have a constructor
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#isInstalled(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public boolean isInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
        try {
            return checkP2IuIsInstalled(getP2IuId(), progress);
        } catch (ProvisionException e) {// just convert the exception
            throw new P2ExtraFeatureException(e);
        }
    }

    /**
     * check if the current p2 profile contains the P2 Installable Unit or not
     *
     * @param p2IuId2, never null
     * @return true if unit is installed or false in any other cases (even errors or canceled)
     * @throws ProvisionException
     */
    private boolean checkP2IuIsInstalled(String p2IuId2, IProgressMonitor progress) throws ProvisionException {
        Set<IInstallableUnit> installedIUs = getInstalledIUs(p2IuId2, progress);
        return !installedIUs.isEmpty();
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

    /**
     * Getter for p2 installable unit id.
     *
     * @return the p2 installable unit id.
     */
    public String getP2IuId() {
        return this.p2IuId;
    }

    /**
     * Sets the p2 installable unit id.
     *
     * @param p2IuId the p2 installable unit id to set
     */
    public void setP2IuId(String p2IuId) {
        this.p2IuId = p2IuId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#getName()
     */
    @Override
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#getDescription()
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#getVersion()
     */
    @Override
    public String getVersion() {
        return this.version;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#install(org.eclipse.core.runtime.IProgressMonitor, java.util.List)
     */
    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
        IStatus doInstallStatus = null;
        File configIniBackupFile = null;
        try {
            if (!useLegacyP2Install) {
                // backup the config.ini
                configIniBackupFile = copyConfigFile(null);
            } // else legacy p2 install will update the config.ini
            doInstallStatus = doInstall(progress, allRepoUris);
        } catch (IOException e) {
            throw new P2ExtraFeatureException(new ProvisionException(Messages.createErrorStatus(e,
                    "ExtraFeaturesFactory.restore.config.error"))); //$NON-NLS-1$
        } finally {
            if (doInstallStatus != null && doInstallStatus.isOK()) {
                afterInstall();
                storeInstalledFeatureMessage();
            }
            // restore the config.ini
            if (configIniBackupFile != null) { // must existed backup file.
                try {
                    copyConfigFile(configIniBackupFile);
                } catch (IOException e) {
                    throw new P2ExtraFeatureException(new ProvisionException(Messages.createErrorStatus(e,
                            "ExtraFeaturesFactory.back.config.error"))); //$NON-NLS-1$
                }
            }
        }
        return doInstallStatus;
    }

    protected void afterInstall() {

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

    protected IStatus doInstall(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
        SubMonitor subMonitor = SubMonitor.convert(progress, 5);
        subMonitor.setTaskName(Messages.getString("ExtraFeature.installing.feature", getName())); //$NON-NLS-1$
        // reset isInstalled to make is compute the next time is it used
        isInstalled = null;
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
        subMonitor.setTaskName(Messages.getString("ExtraFeature.searching.talend.features", getName())); //$NON-NLS-1$
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.p2IuId == null) ? 0 : this.p2IuId.hashCode());
        result = prime * result + ((this.version == null) ? 0 : this.version.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        P2ExtraFeature other = (P2ExtraFeature) obj;
        if (this.p2IuId == null) {
            if (other.p2IuId != null) {
                return false;
            }
        } else if (!this.p2IuId.equals(other.p2IuId)) {
            return false;
        }
        if (this.version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!this.version.equals(other.version)) {
            return false;
        }
        return true;
    }

    /**
     * this is the base URI set in the license.
     *
     * @return the defaultRepoUriStr
     */
    public String getBaseRepoUriString() {
        return this.baseRepoUriStr;
    }

    public URI getP2RepositoryURI() {
        return getP2RepositoryURI(null, false);
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
            org.osgi.framework.Version studioVersion = new org.osgi.framework.Version(VersionUtils.getTalendVersion());
            String version = studioVersion.getMajor() + "." + studioVersion.getMinor() + "." + studioVersion.getMicro();
            if (uriString == null) {
                return URI.create(version);
            }
            uriString = uriString + (uriString.endsWith("/") ? "" : "/") + version; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }
        return URI.create(uriString);
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

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#updateSiteCompatibleTypes()
     */
    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        return EnumSet.allOf(UpdateSiteLocationType.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.updates.model.ExtraFeature#mustBeInstalled()
     */
    @Override
    public boolean mustBeInstalled() {
        return mustBeInstalled;
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
            p2ExtraFeatureUpdate.version = selectedUpdates[0].replacement.getVersion().getOriginal();
            return p2ExtraFeatureUpdate;
        } finally {
            if (agent != null) {// agent creation did not fail
                removeAllRepositories(agent, allRepoUris);
                agent.stop();
            }
        }
    }

    /**
     * DOC sgandon Comment method "copy".
     *
     * @param p2ExtraFeature
     * @param p2ExtraFeatureUpdate
     */
    public void copyFieldInto(P2ExtraFeature p2ExtraFeatureUpdate) {
        p2ExtraFeatureUpdate.name = name;
        p2ExtraFeatureUpdate.description = description;
        p2ExtraFeatureUpdate.version = version;
        p2ExtraFeatureUpdate.p2IuId = p2IuId;
        p2ExtraFeatureUpdate.baseRepoUriStr = baseRepoUriStr;
        p2ExtraFeatureUpdate.mustBeInstalled = mustBeInstalled;
        p2ExtraFeatureUpdate.useLegacyP2Install = useLegacyP2Install;
    }

    /**
     * Getter for useLegacyP2Install.
     *
     * @return the useLegacyP2Install
     */
    public boolean isUseLegacyP2Install() {
        return this.useLegacyP2Install;
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
            URL configLocation = new URL("platform:/config/config.ini"); //$NON-NLS-1$
            URL fileUrl = FileLocator.toFileURL(configLocation);
            File configurationFile = URIUtil.toFile(new URI(fileUrl.getProtocol(), fileUrl.getPath(), fileUrl.getQuery()));
            if (toResore != null) {
                FilesUtils.copyFile(new FileInputStream(toResore), configurationFile);
            } else {
                tempFile = File.createTempFile("config.ini", null); //$NON-NLS-1$
                FilesUtils.copyFile(new FileInputStream(configurationFile), tempFile);
            }
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
        return tempFile;
    }

    @Override
    public boolean needRestart() {
        return true;
    }

    /**
     * Sets the name.
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for parentCategory.
     * 
     * @return the parentCategory
     */
    public FeatureCategory getParentCategory() {
        return this.parentCategory;
    }

    /**
     * Sets the parentCategory.
     * 
     * @param parentCategory the parentCategory to set
     */
    public void setParentCategory(FeatureCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    public P2ExtraFeature getInstalledFeature(IProgressMonitor progress) throws P2ExtraFeatureException {
        P2ExtraFeature extraFeature = null;
        try {
            if (!this.isInstalled(progress)) { // new
                extraFeature = this;
            } else {// else already installed so try to find updates
                ExtraFeature updateFeature = this.createFeatureIfUpdates(progress);
                if (updateFeature != null && updateFeature instanceof P2ExtraFeature) {
                    extraFeature = (P2ExtraFeature) updateFeature;
                }
            }
        } catch (Exception e) {
            throw new P2ExtraFeatureException(e);
        }
        return extraFeature;
    }

    public boolean canBeInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
        return getInstalledFeature(progress) != null;
    }

}
