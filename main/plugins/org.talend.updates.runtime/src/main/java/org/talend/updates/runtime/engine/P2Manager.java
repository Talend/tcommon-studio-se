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

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.update.PreferenceKeys;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.updates.runtime.i18n.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class P2Manager {

    private static P2Manager instance;

    private String p2ProfileId;

    private URI p2AgentUri;

    private IProvisioningAgent p2Agent = null;

    private IProfile p2Profile = null;

    private final Object p2ProfileLock = new Object();

    private final Object clearOsgiLock = new Object();

    private P2Manager() {
        reset();
    }

    public static P2Manager getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (P2Manager.class) {
            if (instance == null) {
                instance = new P2Manager();
            }
        }
        return instance;
    }

    /**
     * get all p2 features(not only talend p2 features)
     */
    public Collection<IInstallableUnit> getInstalledP2Features(IProgressMonitor monitor, boolean useCache2ImprovePerformance)
            throws Exception {
        IQuery<IInstallableUnit> iuQuery = QueryUtil.ALL_UNITS;
        if (!useCache2ImprovePerformance) {
            clear();
        }
        return getP2Profile(monitor).available(iuQuery, monitor).toSet();
    }

    public Collection<IInstallableUnit> getInstalledP2Feature(IProgressMonitor monitor, String p2Id, VersionRange versionRange,
            boolean useCache2ImprovePerformance) throws Exception {
        IQuery<IInstallableUnit> iuQuery = null;
        if (versionRange != null) {
            iuQuery = QueryUtil.createIUQuery(p2Id, versionRange);
        } else {
            iuQuery = QueryUtil.createIUQuery(p2Id);
        }
        if (!useCache2ImprovePerformance) {
            clear();
        }
        return getP2Profile(monitor).available(iuQuery, monitor).toSet();
    }

    private IProfile getP2Profile(IProgressMonitor monitor) throws Exception {
        if (p2Profile != null) {
            return p2Profile;
        }
        synchronized (p2ProfileLock) {
            releaseP2Agent();
            SubMonitor subMonitor = SubMonitor.convert(monitor, 2);

            Bundle bundle = FrameworkUtil.getBundle(org.eclipse.equinox.p2.query.QueryUtil.class);
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
                p2Profile = profile;
                return p2Profile;
            } finally {
                // nothing to do
            }
        }
    }

    /**
     * created for JUnit test so that profile Id can be changed by overriding
     *
     * @return
     */
    public String getP2ProfileId() {
        return p2ProfileId;
    }

    public void setP2ProfileId(String p2ProfileId) {
        this.p2ProfileId = p2ProfileId;
    }

    /**
     * create for JUnit test so that URI can be change to some other P2 repo
     *
     * @return null for using the current defined area, but may be overriden ot point to another area for JUnitTests
     */
    public URI getP2AgentUri() {
        return p2AgentUri;
    }

    public void setP2AgentUri(URI p2AgentUri) {
        this.p2AgentUri = p2AgentUri;
    }

    public String getP2Version(File p2PatchZipFile) {
        String version = null; // $NON-NLS-1$
        File patchFolder = null;
        try {
            patchFolder = File.createTempFile("PatchInstaller", ""); //$NON-NLS-1$ //$NON-NLS-2$
            patchFolder.delete();
            patchFolder.mkdirs();
            FilesUtils.unzip(p2PatchZipFile.getAbsolutePath(), patchFolder.getAbsolutePath());
            Bundle bundle = FrameworkUtil.getBundle(this.getClass());
            BundleContext context = bundle.getBundleContext();
            ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
            if (sr == null) {
                return version;
            }
            IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) context.getService(sr);
            IProvisioningAgent agent = agentProvider.createAgent(null);
            IMetadataRepositoryManager manager = (IMetadataRepositoryManager) agent
                    .getService(IMetadataRepositoryManager.SERVICE_NAME);
            IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
                    .getService(IArtifactRepositoryManager.SERVICE_NAME);
            manager.addRepository(patchFolder.toURI());
            artifactManager.addRepository(patchFolder.toURI());
            IProgressMonitor monitor = new NullProgressMonitor();
            IMetadataRepository metadataRepo = manager.loadRepository(patchFolder.toURI(), monitor);
            Set<IInstallableUnit> toInstall = metadataRepo.query(QueryUtil.createIUAnyQuery(), monitor).toUnmodifiableSet();
            if (!toInstall.isEmpty()) {
                version = toInstall.iterator().next().getVersion().toString();
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (patchFolder != null && patchFolder.exists()) {
                patchFolder.delete();
            }
        }
        return version;
    }

    public void clear() {
        VersionUtils.clearCache();
        synchronized (p2ProfileLock) {
            p2Profile = null;
            releaseP2Agent();
        }
    }

    public void reset() {
        p2ProfileId = "_SELF_"; //$NON-NLS-1$
        p2AgentUri = null;
        clear();
    }

    private void releaseP2Agent() {
        if (p2Agent != null) {
            p2Agent.stop();
            p2Agent = null;
        }
    }

    public void clearOsgiCache() {
        synchronized (clearOsgiLock) {
            PlatformUI.getPreferenceStore().setValue(PreferenceKeys.NEED_OSGI_CLEAN, true);
        }
    }
}
