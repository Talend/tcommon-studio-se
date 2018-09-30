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
package org.talend.updates.runtime.engine.factory;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.updates.runtime.engine.component.ComponentNexusP2ExtraFeature;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureCategory;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.nexus.component.NexusComponentsTransport;
import org.talend.updates.runtime.nexus.component.NexusServerManager;
import org.talend.updates.runtime.storage.impl.NexusFeatureStorage;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class ComponentsNexusInstallFactory extends AbstractExtraUpdatesFactory implements IComponentUpdatesFactory {

    private ComponentIndexManager indexManager = new ComponentIndexManager();

    protected ArtifactRepositoryBean serverSetting;

    public ComponentsNexusInstallFactory() {
        super();
    }

    public ArtifactRepositoryBean getServerSetting() {
        if (serverSetting == null) {
            serverSetting = NexusServerManager.getInstance().getPropertyNexusServer();
        }
        return serverSetting;
    }

    protected Set<ExtraFeature> getAllExtraFeatures(IProgressMonitor monitor) {
        try {
            return retrieveComponentsFromIndex(monitor, getServerSetting());
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
            return Collections.emptySet();
        }
    }

    protected Set<ExtraFeature> getLocalNexusFeatures(IProgressMonitor monitor) {
        IProgressMonitor progress = monitor;
        if (progress == null) {
            progress = new NullProgressMonitor();
        }
        try {
            ArtifactRepositoryBean localNexusServer = NexusServerManager.getInstance().getLocalNexusServer();
            if (localNexusServer == null) {
                return Collections.emptySet();
            }
            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            return retrieveComponentsFromIndex(monitor, localNexusServer);
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
            return Collections.emptySet();
        }
    }

    protected Set<ExtraFeature> retrieveComponentsFromIndex(IProgressMonitor monitor,
            ArtifactRepositoryBean artifactBean) throws Exception {
        return retrieveComponentsFromIndex(monitor, artifactBean, false);
    }

    protected Set<ExtraFeature> retrieveComponentsFromIndex(IProgressMonitor monitor, ArtifactRepositoryBean artifactBean,
            boolean ignoreUncompatibleProduct) throws Exception {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        char[] passwordChars = null;
        String password = artifactBean.getPassword();
        if (password != null) {
            passwordChars = password.toCharArray();
        }
        final NexusComponentsTransport transport = new NexusComponentsTransport(artifactBean.getRepositoryURL(),
                artifactBean.getUserName(), passwordChars);
        if (transport.isAvailable(monitor, getIndexArtifact())) {
            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            final Document doc = transport.downloadXMLDocument(monitor, getIndexArtifact());

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            final Set<ExtraFeature> p2Features = createFeatures(monitor, artifactBean, doc, ignoreUncompatibleProduct);
            return p2Features;
        }
        return Collections.emptySet();
    }

    Set<ExtraFeature> createFeatures(IProgressMonitor monitor, ArtifactRepositoryBean serverBean, Document doc,
            boolean ignoreUncompatibleProduct) {
        if (doc == null) {
            return Collections.emptySet();
        }
        Set<ExtraFeature> features = new LinkedHashSet<>();
        if (doc != null) {
            final List<ComponentIndexBean> indexBeans = indexManager.parse(doc);
            for (ComponentIndexBean b : indexBeans) {
                final String[] products = b.getProducts();
                if (products != null && products.length > 0) {
                    String acronym = getAcronym();
                    if (!ignoreUncompatibleProduct && !Arrays.asList(products).contains(acronym)) {
                        continue; // ignore it in product
                    }
                }

                final ExtraFeature feature = createFeature(monitor, serverBean, b);
                if (feature != null) {
                    features.add(feature);
                }
            }
        }
        return features;
    }

    @Override
    protected ExtraFeature createFeature(IProgressMonitor monitor, ArtifactRepositoryBean serverBean,
            ComponentIndexBean b) {
        ExtraFeature feature = null;
        try {
            feature = super.createFeature(monitor, serverBean, b);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (feature != null) {
            NexusFeatureStorage storage = new NexusFeatureStorage(serverBean, feature.getMvnUri(), feature.getImageMvnUri());
            feature.setStorage(storage);
        }
        return feature;
    }

    @Override
    protected ExtraFeature createTcompv0Feature(IProgressMonitor monitor, ComponentIndexBean b) throws Exception {
        return new ComponentNexusP2ExtraFeature(b);
    }

    @Override
    public void retrieveAllExtraFeatures(IProgressMonitor monitor, Set<ExtraFeature> features) throws Exception {
        Assert.isNotNull(features);
        Set<ExtraFeature> allExtraFeatures = getAllExtraFeatures(monitor);
        if (allExtraFeatures != null && !allExtraFeatures.isEmpty()) {
            features.addAll(allExtraFeatures);
        }
    }

    @Override
    public void retrieveUninstalledExtraFeatures(IProgressMonitor progress, Set<ExtraFeature> uninstalledExtraFeatures)
            throws Exception {
        SubMonitor mainSubMonitor = SubMonitor.convert(progress, 5);
        mainSubMonitor.worked(1);
        Set<ExtraFeature> allExtraFeatures = getAllExtraFeatures(mainSubMonitor);
        List<ExtraFeature> sortedFeatures = new LinkedList<>();
        if (allExtraFeatures != null) {
            sortedFeatures.addAll(allExtraFeatures);
            Collections.sort(sortedFeatures);
        }
        mainSubMonitor.worked(1);
        if (mainSubMonitor.isCanceled()) {
            return;
        }
        FeatureCategory category = new FeatureCategory();
        SubMonitor checkSubMonitor = SubMonitor.convert(mainSubMonitor.newChild(1), sortedFeatures.size() * 2);
        for (ExtraFeature extraF : sortedFeatures) {
            try {
                ExtraFeature extraFeature = extraF.getInstalledFeature(checkSubMonitor.newChild(1));
                if (extraFeature != null) {
                    addToCategory(category, extraFeature);
                }
                checkSubMonitor.worked(1);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        int componentsSize = category.getChildren().size();
        if (componentsSize > 0) {
            category.setName(Messages.getString("ComponentsNexusInstallFactory.categorytitile", componentsSize)); //$NON-NLS-1$
            addToSet(uninstalledExtraFeatures, category);
        }
    }

    public MavenArtifact getIndexArtifact() {
        return indexManager.getIndexArtifact();
    }

}
