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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.IuP2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeature;

/**
 * DOC Talend class global comment. Detailled comment
 */
public class TOSDynamicExtraFeaturesFactory extends AbstractExtraUpdatesFactory {

    private static final String TALEND_PRODUCTS_P2_PROP = "org.talend.authorized.products"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(TOSDynamicExtraFeaturesFactory.class);

    public Set<P2ExtraFeature> getAllExtraFeatures(SubMonitor mainSubMonitor) {
        try {
            String acronym = getAcronym();
            String key = "default.update.site.url";
            String url = System.getProperty(key);
            if (url == null) {
                return Collections.EMPTY_SET;
            }
            IuP2ExtraFeature p2ExtraFeature = new IuP2ExtraFeature(url);
            String updateSiteUrl = p2ExtraFeature.getP2RepositoryURI(key, true).toString();
            return retrieveAllExtraFeature(mainSubMonitor, updateSiteUrl, acronym);
        } catch (ProvisionException | OperationCanceledException | URISyntaxException e) {
            log.error(Messages.getString("DynamicExtraFeaturesFactory.failed.to.retreive.features"), e); //$NON-NLS-1$
            return Collections.EMPTY_SET;
        }
    }

    /**
     * compute all extra features in the current licence and only return those that are not installed in the current
     * Studio
     * 
     * @param progress to report progress or cancel
     * @param availableExtraFeatures
     * @return the set of uninstall extra feature the user may install or empty if none found or progress canceled by
     * the user.
     */
    @Override
    public void retrieveUninstalledExtraFeatures(IProgressMonitor progress, Set<ExtraFeature> uninstalledExtraFeatures)
            throws Exception {
        SubMonitor mainSubMonitor = SubMonitor.convert(progress, 5);
        mainSubMonitor.setTaskName(Messages.getString("ExtraFeaturesFactory.checking..feature.to.install")); //$NON-NLS-1$
        mainSubMonitor.worked(1);
        Set<P2ExtraFeature> allExtraFeatures = getAllExtraFeatures(mainSubMonitor);
        mainSubMonitor.worked(1);
        if (mainSubMonitor.isCanceled()) {
            return;
        }
        SubMonitor checkSubMonitor = SubMonitor.convert(mainSubMonitor.newChild(1), allExtraFeatures.size() * 2);
        for (P2ExtraFeature extraF : allExtraFeatures) {
            try {
                if (!extraF.isInstalled(checkSubMonitor.newChild(1))) {
                    if (checkSubMonitor.isCanceled()) {
                        return;
                    }
                    addToSet(uninstalledExtraFeatures, extraF);
                    checkSubMonitor.worked(1);
                } else {// else already installed so try to find updates
                    ExtraFeature updateFeature = extraF.createFeatureIfUpdates(checkSubMonitor.newChild(1));
                    if (updateFeature != null) {
                        addToSet(uninstalledExtraFeatures, updateFeature);
                    }
                }
            } catch (Exception e) {
                // could not determine if feature is already installed so do not consider it all
                log.error(e);
            }
        }
    }

    /**
     * DOC sgandon Comment method "retreiveAllExtraFeatureForThisProduct".
     * 
     * @param mainSubMonitor
     * @param updateSiteUrl
     * @param productAcronym
     * @return
     * @throws URISyntaxException
     * @throws OperationCanceledException
     * @throws ProvisionException
     */
    public Set<P2ExtraFeature> retrieveAllExtraFeature(SubMonitor mainSubMonitor, String updateSiteUrl, String acronym)
            throws ProvisionException, OperationCanceledException, URISyntaxException {
        Set<IInstallableUnit> allAvailableFeatures = getInstallableIU(updateSiteUrl, mainSubMonitor, acronym);
        // create P2ExtraFeature from those IUs
        Set<P2ExtraFeature> extraFeatures = new HashSet(allAvailableFeatures.size());
        for (IInstallableUnit iu : allAvailableFeatures) {
            extraFeatures.add(new IuP2ExtraFeature(iu, updateSiteUrl));
        }
        return extraFeatures;
    }

    public Set<IInstallableUnit> getInstallableIU(String updateSiteUrl, IProgressMonitor progress, String acronym)
            throws URISyntaxException, ProvisionException, OperationCanceledException {

        // we are not using this bundles context caus it fails to be aquired in junit test
        Bundle bundle = FrameworkUtil.getBundle(org.eclipse.equinox.p2.query.QueryUtil.class);
        BundleContext context = bundle.getBundleContext();
        ServiceReference sr = context.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
        if (sr == null) {
            throw new ProvisionException("Failed to get provisioning agent service");
        }
        IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider) context.getService(sr);
        if (agentProvider == null) {
            throw new ProvisionException("Failed to get provisioning agent provider");
        }

        IProvisioningAgent agent = null;
        agent = agentProvider.createAgent(getP2AgentUri());

        // get the repository managers and add our repository
        IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
                .getService(IMetadataRepositoryManager.SERVICE_NAME);
        IArtifactRepositoryManager artifactManager = (IArtifactRepositoryManager) agent
                .getService(IArtifactRepositoryManager.SERVICE_NAME);

        // create the IU query for IU that have the key org.talend.authorized.products
        IQuery<IInstallableUnit> iuQuery = QueryUtil.createMatchQuery("properties[\"" + TALEND_PRODUCTS_P2_PROP + "\"] != null");

        URI uri = URI.create(updateSiteUrl);
        metadataManager.addRepository(uri);
        artifactManager.addRepository(uri);
        IQueryResult<IInstallableUnit> queryResult = metadataManager.query(iuQuery, progress);
        return filterResultWithProduct(queryResult, acronym);
    }

    /**
     * This check the IU product property to only return the IU for the current product.
     * 
     * @param productAcronym
     * 
     * @param queryResult
     * @return
     */
    private Set<IInstallableUnit> filterResultWithProduct(IQueryResult<IInstallableUnit> setToFilter, String acronym) {
        Set<IInstallableUnit> filteredSet = new HashSet<IInstallableUnit>();
        for (IInstallableUnit iu : setToFilter) {
            String talend_products = iu.getProperty(TALEND_PRODUCTS_P2_PROP);
            if (talend_products != null) {
                String[] products = talend_products.split(",");
                for (String product : products) {
                    if (product.equalsIgnoreCase(acronym)) {
                        filteredSet.add(iu);
                        break;
                    }
                }
            }
            // if (iu.getProperty(TALEND_PRODUCTS_P2_PROP).contains(acronym)) {
            // filteredSet.add(iu);
            // }
        }
        return filteredSet;
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

}
