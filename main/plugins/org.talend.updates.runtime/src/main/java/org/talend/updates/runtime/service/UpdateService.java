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
package org.talend.updates.runtime.service;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.service.IUpdateService;
import org.talend.updates.runtime.engine.component.InstallComponentMessages;
import org.talend.updates.runtime.engine.factory.ComponentsLocalNexusInstallFactory;
import org.talend.updates.runtime.maven.MavenRepoSynchronizer;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.nexus.component.NexusServerManager;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

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
    public void syncComponentM2Jars(IProgressMonitor monitor) {
        final File tempM2RepoFolder = PathUtils.getComponentsM2TempFolder();
        try {
            if (tempM2RepoFolder != null && tempM2RepoFolder.exists()) {
                MavenRepoSynchronizer synchronizer = new MavenRepoSynchronizer(tempM2RepoFolder);
                synchronizer.sync();
            }
        } finally {
            // clean
            FilesUtils.deleteFolder(tempM2RepoFolder, true);
        }
    }

}
