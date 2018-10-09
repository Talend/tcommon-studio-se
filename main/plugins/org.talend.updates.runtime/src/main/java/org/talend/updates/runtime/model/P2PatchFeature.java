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
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.helper.PatchComponentHelper;
import org.talend.commons.runtime.service.PatchComponent;
import org.talend.commons.utils.VersionUtils;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;
import org.talend.updates.runtime.storage.AbstractFeatureStorage;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class P2PatchFeature extends P2ExtraFeature {

    public P2PatchFeature(ComponentIndexBean indexBean) {
        super(indexBean);
    }

    public P2PatchFeature(final File zipFile) {
        this(new ComponentIndexManager().createIndexBean4Patch(zipFile, Type.P2_PATCH));
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
        return PathUtils.getInstallationStatus(VersionUtils.getInternalVersion(), getVersion());
    }

    @Override
    public ExtraFeature getInstalledFeature(IProgressMonitor progress) throws ExtraFeatureException {
        return this;
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
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws ExtraFeatureException {
        try {
            PatchComponent patchComponent = PatchComponentHelper.getPatchComponent();
            boolean installed = patchComponent.install(progress, getStorage().getFeatureFile(progress));
            int security = 0;
            String message = ""; //$NON-NLS-1$
            if (installed) {
                security = IStatus.OK;
                message = Messages.getString("ComponentsManager.p2PatchFeature.succeed"); //$NON-NLS-1$
            } else {
                security = IStatus.ERROR;
                message = Messages.getString("ComponentsManager.p2PatchFeature.failed"); //$NON-NLS-1$
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
