// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.service;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.util.SharedStudioUtils;
import org.talend.core.service.IUpdateService;
import org.talend.updates.runtime.engine.component.InstallComponentMessages;
import org.talend.updates.runtime.engine.factory.ComponentsLocalNexusInstallFactory;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.nexus.component.NexusServerManager;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.updates.runtime.utils.UpdateTools;

public class UpdateService implements IUpdateService {

    private static Logger log = Logger.getLogger(UpdateService.class);
    @Override
    public boolean checkComponentNexusUpdate() {
        IProgressMonitor monitor = new NullProgressMonitor();
        try {
            if (!ComponentIndexManager.isEnableShareComponent()) {
                log.info("Component share is disabled, won't check component update.");
                return false;
            }
            ArtifactRepositoryBean artifactRepisotory = NexusServerManager.getInstance().getPropertyNexusServer();
            ComponentsLocalNexusInstallFactory compInstallFactory = new ComponentsLocalNexusInstallFactory(artifactRepisotory);

            Set<ExtraFeature> uninstalledExtraFeatures = new LinkedHashSet<>();
            InstallComponentMessages messages = new InstallComponentMessages();

            compInstallFactory.retrieveUninstalledExtraFeatures(monitor, uninstalledExtraFeatures);
            for (ExtraFeature feature : uninstalledExtraFeatures) {
                install(monitor, feature, messages);
            }
            if (messages.isOk()) {
                System.out.println("------------------------------");
                System.out.println(messages.getInstalledMessage());
                System.out.println("------------------------------");
                log.info(messages.getInstalledMessage());
                return messages.isNeedRestart();
            }
            if (StringUtils.isNotEmpty(messages.getFailureMessage())) {
                System.out.println(messages.getFailureMessage());
                log.error(messages.getFailureMessage());
            }
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }

    private void install(IProgressMonitor monitor, ExtraFeature feature, InstallComponentMessages messages) throws Exception {
        if (feature instanceof FeatureCategory) {
            Set<ExtraFeature> children = ((FeatureCategory) feature).getChildren();
            for (ExtraFeature f : children) {
                install(monitor, f, messages);
            }
        } else {
            if (feature.canBeInstalled(monitor)) {
                messages.analyzeStatus(feature.install(monitor, null));
                messages.setNeedRestart(feature.needRestart());
            }
        }
    }

    @Override
    public boolean syncSharedStudioLibraryInPatch(IProgressMonitor monitor) throws Exception {
        boolean isNeedRestart = false;
        if (SharedStudioUtils.isSharedStudioMode()) {
            File studioPatch = SharedStudioPatchInfoProvider.getInstance().getNeedInstallStudioPatchFiles();
            if (studioPatch != null && studioPatch.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {
                File tmpInstallFolder = File.createTempFile("StudioPatchInstaller", "");
                if (tmpInstallFolder.exists()) {
                    tmpInstallFolder.delete();
                }
                tmpInstallFolder.mkdirs();
                FilesUtils.unzip(studioPatch.getAbsolutePath(), tmpInstallFolder.getAbsolutePath());
                UpdateTools.syncLibraries(tmpInstallFolder);
                UpdateTools.syncM2Repository(tmpInstallFolder);
                File carFolder = new File(tmpInstallFolder, ITaCoKitUpdateService.FOLDER_CAR);
                UpdateTools.deployCars(monitor, carFolder, false);
                SharedStudioPatchInfoProvider.getInstance().installedStudioPatch(studioPatch.getName());
                tmpInstallFolder.delete();
                isNeedRestart = true;
            }
            List<File> carFiles = SharedStudioPatchInfoProvider.getInstance().getNeedInstallCarFiles();
            if (carFiles.size() > 0) {
                File tmpInstallFolder = File.createTempFile("CarPatchInstaller", "");
                if (tmpInstallFolder.exists()) {
                    tmpInstallFolder.delete();
                }
                tmpInstallFolder.mkdirs();
                for (File carFile : carFiles) {
                    FileUtils.copyFile(carFile, new File (tmpInstallFolder, carFile.getName()));
                    SharedStudioPatchInfoProvider.getInstance().installedCarPatch(carFile.getName());
                }
                UpdateTools.deployCars(monitor, tmpInstallFolder, false);
                tmpInstallFolder.delete();
            }
        }
        return isNeedRestart;
    }

    @Override
    public String getSharedStudioMissingPatchVersion() {
        File patchFolder = PathUtils.getPatchesFolder();
        String patchFileName = SharedStudioPatchInfoProvider.getInstance().getStudioInstalledLatestPatchFileName();
        if (patchFileName != null && !SharedStudioPatchInfoProvider.getInstance().isInstalled(patchFileName, SharedStudioPatchInfoProvider.PATCH_TYPE_STUDIO)) {
            File studioPatchFile = new File (patchFolder, patchFileName);
            if (studioPatchFile != null && !studioPatchFile.exists()) {
                return patchFileName;
            }
        }
        return null;
    }
    
    @Override
    public boolean updateArtifactsFileSha256Hex(IProgressMonitor monitor, String studioArtifactsFileShaCodeHex) {
        return SharedStudioPatchInfoProvider.getInstance().updateArtifactsFileSha256Hex(studioArtifactsFileShaCodeHex);
    }
}

