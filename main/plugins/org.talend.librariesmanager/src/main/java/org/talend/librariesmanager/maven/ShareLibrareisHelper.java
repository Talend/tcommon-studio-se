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
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ISVNProviderServiceInCoreRuntime;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * created by Talend on 2015年7月31日 Detailled comment
 *
 */
public abstract class ShareLibrareisHelper {

    private final String TYPE_NEXUS = "nexus";

    private final String TYPE_SVN = "svn";

    protected MavenArtifactsHandler deployer = new MavenArtifactsHandler();

    public IStatus shareLibs(Job job, IProgressMonitor monitor) {
        Map<ModuleNeeded, File> filesToShare = null;
        IStatus status = Status.OK_STATUS;
        IProxyRepositoryFactory factory = CoreRuntimePlugin.getInstance().getProxyRepositoryFactory();
        ISVNProviderServiceInCoreRuntime service = null;
        boolean shareToSvn = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ISVNProviderServiceInCoreRuntime.class)) {
            service = (ISVNProviderServiceInCoreRuntime) GlobalServiceRegister.getDefault().getService(
                    ISVNProviderServiceInCoreRuntime.class);
            shareToSvn = service.isSvnLibSetupOnTAC() && factory.getRepositoryContext() != null
                    && !factory.getRepositoryContext().isOffline();
        }
        if (shareToSvn) {
            service.syncLibs(monitor);
            setJobName(job, Messages.getString("ShareLibsJob.message", TYPE_SVN));
            int shareLimit = 5;
            // share to svn lib
            int limit = shareLimit;
            filesToShare = getFilesToShare(monitor);
            if (filesToShare == null) {
                return Status.CANCEL_STATUS;
            }
            SubMonitor mainSubMonitor = SubMonitor.convert(monitor, filesToShare.size());
            Iterator<ModuleNeeded> iterator = filesToShare.keySet().iterator();
            int index = 0;
            while (iterator.hasNext()) {
                if (monitor.isCanceled()) {
                    return Status.CANCEL_STATUS;
                }
                List<String> jars = new ArrayList<String>();
                String jarName = "";
                while (index < limit && index < filesToShare.size()) {
                    ModuleNeeded next = iterator.next();
                    File file = filesToShare.get(next);
                    String installLocation = getStorageDirectory().getAbsolutePath();
                    File target = new File(installLocation, next.getModuleName());
                    // if already eixst in lib svn , don't replace it .
                    if (!target.exists()) {
                        try {
                            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(next.getMavenUri());
                            if (artifact != null) {
                                // to .m2
                                shareToRepository(file, artifact);
                            }
                            FilesUtils.copyFile(file, target);
                            jars.add(target.getAbsolutePath());
                            jarName += next.getModuleName();
                            if (index < limit - 1) {
                                jarName += ",";
                            }
                        } catch (Exception e) {
                            continue;
                        }
                    }
                    index++;
                }
                limit += shareLimit;
                try {
                    if (jars.size() > 0) {
                        mainSubMonitor.setTaskName(Messages.getString("ShareLibsJob.sharingLibraries", jarName)); //$NON-NLS-1$
                        // share to svn lib
                        service.deployNewJar(jars);
                    }
                    mainSubMonitor.worked(limit);
                } catch (Exception e) {
                    ExceptionHandler.process(new Exception("Share libraries :" + jarName + " failed !", e));
                    status = new Status(IStatus.ERROR, "unknown", IStatus.ERROR, "Share libraries :" + jarName + " failed !", e);
                    continue;
                }
            }
        } else {
            // deploy to maven if needed and share to custom nexus
            try {
                int searchLimit = 50;
                setJobName(job, Messages.getString("ShareLibsJob.message", TYPE_NEXUS));
                final List<MavenArtifact> searchResults = new ArrayList<MavenArtifact>();
                NexusServerBean customNexusServer = TalendLibsServerManager.getInstance().getCustomNexusServer();
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
        }

        return status;

    }

    private void setJobName(Job job, String jobName) {
        if (job != null) {
            job.setName(jobName);
        }
    }

    private File getStorageDirectory() {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        return storageDir;
    }

    public abstract Map<ModuleNeeded, File> getFilesToShare(IProgressMonitor monitor);

    public abstract void shareToRepository(File jarFile, MavenArtifact module) throws Exception;
}
