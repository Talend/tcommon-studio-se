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
package org.talend.updates.runtime.model;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.helper.PatchComponentHelper;
import org.talend.commons.runtime.service.PatchComponent;
import org.talend.commons.utils.VersionUtils;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.storage.AbstractFeatureStorage;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class PlainZipFeature extends AbstractExtraFeature {

    public PlainZipFeature(ComponentIndexBean indexBean) {
        this(indexBean.getName(), indexBean.getVersion(), indexBean.getDescription(), indexBean.getMvnURI(),
                indexBean.getImageMvnURI(), indexBean.getProduct(), indexBean.getCompatibleStudioVersion(),
                indexBean.getBundleId(), PathUtils.convert2Types(indexBean.getTypes()),
                PathUtils.convert2Categories(indexBean.getCategories()), Boolean.valueOf(indexBean.getDegradable()));
    }

    public PlainZipFeature(final File zipFile) {
        this(new ComponentIndexManager().createIndexBean4Patch(zipFile, Type.PLAIN_ZIP));
        setStorage(new AbstractFeatureStorage() {

            @Override
            protected File downloadImageFile(IProgressMonitor monitor) throws Exception {
                return null;
            }

            @Override
            protected File downloadFeatureFile(IProgressMonitor monitor) throws Exception {
                return zipFile;
            }
        });
    }

    public PlainZipFeature(String name, String version, String description, String mvnUri, String imageMvnUri, String product,
            String compatibleStudioVersion, String p2IuId, Collection<Type> types, Collection<Category> categories,
            boolean degradable) {
        this(p2IuId, name, version, description, mvnUri, imageMvnUri, product, compatibleStudioVersion, null, types, categories,
                degradable, null, false, true);
    }

    public PlainZipFeature(String p2IuId, String name, String version, String description, String mvnUri, String imageMvnUri,
            String product, String compatibleStudioVersion, FeatureCategory parentCategory, Collection<Type> types,
            Collection<Category> categories, boolean degradable, String baseRepoUriStr, boolean mustBeInstalled,
            boolean useLegacyP2Install) {
        super(p2IuId, name, version, description, mvnUri, imageMvnUri, product, compatibleStudioVersion, parentCategory, types,
                categories, degradable, mustBeInstalled, useLegacyP2Install);
    }

    @Override
    public boolean isShareEnable() {
        return share;
    }

    @Override
    public void setShareEnable(boolean share) {
        this.share = share;
    }

    @Override
    public boolean canBeInstalled(IProgressMonitor progress) throws ExtraFeatureException {
        try {
            InstallationStatus installationStatus = getInstallationStatus(progress);
            return installationStatus.canBeInstalled();
        } catch (Exception e) {
            throw new ExtraFeatureException(e);
        }
    }

    @Override
    public InstallationStatus getInstallationStatus(IProgressMonitor monitor) throws Exception {
        boolean installed = PatchComponentHelper.getPatchComponent().isPlainZipInstalled(monitor, getVersion());
        InstallationStatus status = null;
        if (installed) {
            status = new InstallationStatus(InstallationStatus.Status.INSTALLED);
        } else {
            status = new InstallationStatus(InstallationStatus.Status.UPDATABLE);
        }
        status.setInstalledVersion(VersionUtils.getInternalVersion());
        return status;
    }

    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws Exception {
        try {
            PatchComponent patchComponent = PatchComponentHelper.getPatchComponent();
            boolean installed = patchComponent.install(progress, getStorage().getFeatureFile(progress));
            int security = 0;
            String message = ""; //$NON-NLS-1$
            if (installed) {
                security = IStatus.OK;
                message = Messages.getString("ComponentsManager.PlainZipFeature.succeed"); //$NON-NLS-1$
            } else {
                security = IStatus.ERROR;
                message = Messages.getString("ComponentsManager.PlainZipFeature.failed"); //$NON-NLS-1$
            }
            Status installationStatus = new Status(security, Constants.PLUGIN_ID, message);
            ExceptionHandler.log(patchComponent.getInstalledMessages());

            setNeedRestart(patchComponent.needRelaunch());
            return installationStatus;
        } catch (ExtraFeatureException e) {
            throw e;
        } catch (Exception e) {
            throw new ExtraFeatureException(e);
        } finally {
            // nothing to do
        }
    }

}
