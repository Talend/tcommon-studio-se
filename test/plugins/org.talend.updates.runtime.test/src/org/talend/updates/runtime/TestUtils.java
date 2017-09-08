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
package org.talend.updates.runtime;

import java.net.URI;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UninstallOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class TestUtils {

    private static final NullProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();

    static public void uninstallIU(URI agentLocation, String profileId, String iuId) throws ProvisionException {
        Bundle bundle = FrameworkUtil.getBundle(org.eclipse.equinox.p2.query.QueryUtil.class);
        BundleContext context = bundle.getBundleContext();

        ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
        IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) context.getService(sr);

        IProvisioningAgent agent = agentProvider.createAgent(agentLocation);
        IQuery<IInstallableUnit> iuQuery = QueryUtil.createIUQuery(iuId);
        IProfileRegistry profRegistry = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
        IProfile profile = profRegistry.getProfile(profileId);
        IQueryResult<IInstallableUnit> toUninstall = profile.query(iuQuery, NULL_PROGRESS_MONITOR);
        if (toUninstall.isEmpty()) {
            throw new ProvisionException("Could not find the feature with id :" + iuId + "."); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // uninstall
        ProvisioningSession provisioningSession = new ProvisioningSession(agent);
        UninstallOperation unInstallOperation = new UninstallOperation(provisioningSession, toUninstall.toSet());
        unInstallOperation.setProfileId(profileId);
        IStatus installResolvedStatus = unInstallOperation.resolveModal(NULL_PROGRESS_MONITOR);
        if (installResolvedStatus.getSeverity() == IStatus.ERROR) {
            throw new ProvisionException(installResolvedStatus);
        }// else perform the installlation

        ProfileModificationJob provisioningJob = (ProfileModificationJob) unInstallOperation
                .getProvisioningJob(NULL_PROGRESS_MONITOR);
        if (provisioningJob == null) {
            throw new ProvisionException(installResolvedStatus);
        }
        IStatus status = provisioningJob.run(NULL_PROGRESS_MONITOR);
        if (!status.isOK()) {
            throw new ProvisionException(status);
        }

    }

}
