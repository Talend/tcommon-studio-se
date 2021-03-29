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
package org.talend.updates.runtime.engine.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentSyncManager;
import org.talend.updates.runtime.storage.impl.NexusComponentStorage;

/**
 * DOC cmeng class global comment. Detailled comment
 */
public class ComponentsLocalNexusInstallFactory extends ComponentsNexusInstallFactory {

    private static boolean isTalendDebug = CommonsPlugin.isDebugMode();

    private static Logger log = Logger.getLogger(ComponentsLocalNexusInstallFactory.class);

    private ComponentSyncManager syncManager;

    public ComponentsLocalNexusInstallFactory(ArtifactRepositoryBean serverBean) {
        syncManager = new ComponentSyncManager(serverBean);
    }

    @Override
    protected Set<ExtraFeature> getAllExtraFeatures(IProgressMonitor monitor) {
        IProgressMonitor progress = monitor;
        if (progress == null) {
            progress = new NullProgressMonitor();
        }
        try {
            return retrieveComponentsFromIndex(monitor, null);
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
            return Collections.emptySet();
        }
    }

    @Override
    protected Set<ExtraFeature> retrieveComponentsFromIndex(IProgressMonitor monitor, ArtifactRepositoryBean artifactBean,
            boolean ignoreUncompatibleProduct) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        if (!syncManager.isRepositoryServerAvailable(monitor, getIndexArtifact())) {
            if (isTalendDebug) {
                log.info("Failed to sync component index file: " + getIndexArtifact());
            }
            return Collections.EMPTY_SET;
        }
        File indexFile = null;
        try {
            indexFile = syncManager.downloadIndexFile(monitor, getIndexArtifact());
        } catch (FileNotFoundException e) {
            if (isTalendDebug) {
                log.info(e.getMessage(), e);
            }
            return Collections.EMPTY_SET;
        }
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(indexFile);

        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        final Set<ExtraFeature> p2Features = createFeatures(monitor, artifactBean, doc, ignoreUncompatibleProduct);
        return p2Features;
    }

    @Override
    protected ExtraFeature createFeature(IProgressMonitor monitor, ArtifactRepositoryBean serverBean, ComponentIndexBean b) {
        ExtraFeature feature = null;
        try {
            feature = super.createFeature(monitor, serverBean, b);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (feature != null) {
            MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(feature.getMvnUri());
            Function<MavenArtifact, File> downloader = (ma) -> {
                try {
                    return syncManager.resolve(monitor, artifact);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            NexusComponentStorage storage = new NexusComponentStorage(downloader, feature.getMvnUri(), feature.getImageMvnUri());
            feature.setStorage(storage);
        }
        return feature;
    }

}
