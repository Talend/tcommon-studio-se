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

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.service.IUpdateService;
import org.talend.updates.runtime.engine.component.InstallComponentMessages;
import org.talend.updates.runtime.engine.factory.ComponentsNexusInstallFactory;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.nexus.component.NexusServerManager;

public class UpdateService implements IUpdateService {

    private static Logger log = Logger.getLogger(UpdateService.class);

    @Override
    public boolean checkComponentNexusUpdate() {
        IProgressMonitor monitor = new NullProgressMonitor();
        try {
            ComponentsNexusInstallFactory compInstallFactory = new ComponentsNexusInstallFactory() {

                @Override
                protected Set<ExtraFeature> getAllExtraFeatures(IProgressMonitor monitor) {
                    IProgressMonitor progress = monitor;
                    if (progress == null) {
                        progress = new NullProgressMonitor();
                    }
                    try {
                        ArtifactRepositoryBean artifactRepisotory = NexusServerManager.getInstance().getPropertyNexusServer();
                        if (artifactRepisotory == null) {
                            return Collections.emptySet();
                        }
                        return retrieveComponentsFromIndex(monitor, artifactRepisotory);
                    } catch (Exception e) {
                        if (CommonsPlugin.isDebugMode()) {
                            ExceptionHandler.process(e);
                        }
                        return Collections.emptySet();
                    }
                }

            };

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

}
