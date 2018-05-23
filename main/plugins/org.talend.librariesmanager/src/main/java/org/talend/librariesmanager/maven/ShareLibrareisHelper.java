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
package org.talend.librariesmanager.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.i18n.Messages;

/**
 * created by Talend on 2015年7月31日 Detailled comment
 *
 */
public abstract class ShareLibrareisHelper {

    private final String TYPE_NEXUS = "nexus";

    protected MavenArtifactsHandler deployer = new MavenArtifactsHandler();

    public IStatus shareLibs(Job job, IProgressMonitor monitor) {
        Map<ModuleNeeded, File> filesToShare = null;
        IStatus status = Status.OK_STATUS;
        // deploy to maven if needed and share to custom nexus
        try {
            int searchLimit = 50;
            setJobName(job, Messages.getString("ShareLibsJob.message", TYPE_NEXUS));
            final List<MavenArtifact> searchResults = new ArrayList<MavenArtifact>();
            ArtifactRepositoryBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
            IRepositoryArtifactHandler customerRepHandler = RepositoryArtifactHandlerManager
                    .getRepositoryHandler(customNexusServer);
            if (customerRepHandler != null) {
                filesToShare = getFilesToShare(monitor);
                if (filesToShare == null) {
                    return Status.CANCEL_STATUS;
                }
                SubMonitor mainSubMonitor = SubMonitor.convert(monitor, filesToShare.size());

                // collect groupId to search
                Set<String> groupIds = new HashSet<String>();
                for (ModuleNeeded module : filesToShare.keySet()) {
                    if (module.getMavenUri() != null) {
                        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(module.getMavenUri());
                        if (parseMvnUrl != null) {
                            groupIds.add(parseMvnUrl.getGroupId());
                        }
                    }
                }
                for (String groupId : groupIds) {
                    searchResults.addAll(customerRepHandler.search(groupId, null, null, true, true));
                }

                int limit = searchLimit;
                int shareIndex = 0;
                Iterator<ModuleNeeded> iterator = filesToShare.keySet().iterator();
                while (iterator.hasNext()) {
                    if (monitor.isCanceled()) {
                        return Status.CANCEL_STATUS;
                    }
                    shareIndex++;
                    if (shareIndex == limit) {
                        limit += searchLimit;
                    }

                    ModuleNeeded next = iterator.next();
                    File file = filesToShare.get(next);
                    String name = file.getName();
                    MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(next.getMavenUri());
                    if (artifact == null) {
                        continue;
                    }

                    boolean eixst = false;
                    String groupId = artifact.getGroupId();
                    String artifactId = artifact.getArtifactId();
                    String version = artifact.getVersion();
                    for (MavenArtifact remoteAtifact : searchResults) {
                        String rGroup = remoteAtifact.getGroupId();
                        String rArtifact = remoteAtifact.getArtifactId();
                        String rVersion = remoteAtifact.getVersion();
                        if (groupId != null && artifactId != null && version != null && groupId.equals(rGroup)
                                && artifactId.equals(rArtifact) && version.equals(rVersion)) {
                            eixst = true;
                            break;
                        }
                    }
                    if (eixst) {
                        continue;
                    }
                    mainSubMonitor.setTaskName(Messages.getString("ShareLibsJob.sharingLibraries", name));

                    try {
                        shareToRepository(file, artifact);
                        mainSubMonitor.worked(1);
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            status = new Status(IStatus.ERROR, "unknown", IStatus.ERROR, "Share libraries failed !", e);
        }

        return status;

    }

    private void setJobName(Job job, String jobName) {
        if (job != null) {
            job.setName(jobName);
        }
    }

    public abstract Map<ModuleNeeded, File> getFilesToShare(IProgressMonitor monitor);

    public abstract void shareToRepository(File jarFile, MavenArtifact module) throws Exception;
}
