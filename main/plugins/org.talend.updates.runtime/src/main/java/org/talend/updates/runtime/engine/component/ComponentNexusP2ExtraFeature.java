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
package org.talend.updates.runtime.engine.component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.talend.updates.runtime.feature.model.Category;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.ExtraFeatureException;
import org.talend.updates.runtime.model.P2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.utils.PathUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeature extends ComponentP2ExtraFeature {

    public ComponentNexusP2ExtraFeature() {
        super();
    }

    public ComponentNexusP2ExtraFeature(ComponentIndexBean indexBean) {
        super(indexBean);
    }

    public ComponentNexusP2ExtraFeature(String name, String version, String description, String mvnUri, String imageMvnUri,
            String product, String compatibleStudioVersion, String p2IuId, Collection<Type> types,
            Collection<Category> categories, boolean degradable) {
        super(name, version, description, mvnUri, imageMvnUri, product, compatibleStudioVersion, p2IuId, types, categories,
                degradable);
    }

    @Override
    public ExtraFeature getInstalledFeature(IProgressMonitor progress) throws ExtraFeatureException {
        P2ExtraFeature extraFeature = null;
        try {
            if (!this.isInstalled(progress)) {
                extraFeature = this;
            } else {// else already installed so try to find updates
                boolean isUpdate = true;
                org.eclipse.equinox.p2.metadata.Version currentVer = PathUtils.convert2Version(this.getVersion());
                Set<IInstallableUnit> installedIUs = getInstalledIUs(getP2IuId(), progress);
                for (IInstallableUnit iu : installedIUs) {
                    if (currentVer.compareTo(iu.getVersion()) <= 0) {
                        isUpdate = false;
                        break;
                    }
                }
                if (isUpdate) {
                    extraFeature = this;
                }
            }
        } catch (Exception e) {
            throw new ExtraFeatureException(e);
        }
        return extraFeature;
    }

    @Override
    public IStatus install(IProgressMonitor monitor, List<URI> allRepoUris) throws P2ExtraFeatureException {
        return this.install(monitor);
    }

    public IStatus install(IProgressMonitor monitor) throws P2ExtraFeatureException {
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        try {
            File featureFile = getStorage().getFeatureFile(monitor);
            if (featureFile == null || !featureFile.exists()) {
                throw new IOException(Messages.getString("failed.install.of.feature", "Download failure for " + getName())); //$NON-NLS-1$ //$NON-NLS-2$
            }
            List<URI> repoUris = new ArrayList<>(1);
            repoUris.add(PathUtils.getP2RepURIFromCompFile(featureFile));

            return super.install(monitor, repoUris);
        } catch (Exception e) {
            return Messages.createErrorStatus(e);
        } finally {
            // nothing to do
        }
    }

}
