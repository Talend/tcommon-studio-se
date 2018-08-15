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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.P2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.nexus.component.ComponentIndexBean;
import org.talend.updates.runtime.nexus.component.NexusComponentsTransport;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeature extends ComponentP2ExtraFeature {

    private ArtifactRepositoryBean serverBean;

    public ComponentNexusP2ExtraFeature() {
        super();
    }

    public ComponentNexusP2ExtraFeature(ComponentIndexBean indexBean) {
        super(indexBean);
    }

    public ComponentNexusP2ExtraFeature(String name, String version, String description, String product, String mvnURI,
            String imageMvnURI, String p2IuId) {
        super(name, version, description, product, mvnURI, imageMvnURI, p2IuId);
    }

    @Override
    public P2ExtraFeature getInstalledFeature(IProgressMonitor progress) throws P2ExtraFeatureException {
        P2ExtraFeature extraFeature = null;
        try {
            if (!this.isInstalled(progress)) {
                extraFeature = this;
            } else {// else already installed so try to find updates
                boolean isUpdate = true;
                org.eclipse.equinox.p2.metadata.Version currentVer = Version.create(this.getVersion());
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
            throw new P2ExtraFeatureException(e);
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
        final File workFolder = PathUtils.getComponentsDownloadedFolder();
        FilesUtils.deleteFolder(workFolder, false); // empty the folder
        if (!workFolder.exists()) {
            workFolder.mkdirs();
        }

        String reletivePath = PomUtil.getArtifactPath(getArtifact());
        if (reletivePath == null) {
            return Messages.createErrorStatus(null, "Can't install"); //$NON-NLS-1$
        }

        String compFileName = new Path(reletivePath).lastSegment();
        final File target = new File(workFolder, compFileName);

        try {
            ArtifactRepositoryBean serverBean = getServerBean();
            char[] passwordChars = null;
            String password = serverBean.getPassword();
            if (password != null) {
                passwordChars = password.toCharArray();
            }
            NexusComponentsTransport transport = new NexusComponentsTransport(serverBean.getRepositoryURL(),
                    serverBean.getUserName(), passwordChars);
            transport.downloadFile(monitor, getMvnURI(), target);

            if (monitor.isCanceled()) {
                throw new OperationCanceledException();
            }
            if (!target.exists()) {
                return Messages.createErrorStatus(null, "failed.install.of.feature", "Download the failure for " + getMvnURI()); //$NON-NLS-1$ //$NON-NLS-2$
            }

            List<URI> repoUris = new ArrayList<>(1);
            repoUris.add(PathUtils.getP2RepURIFromCompFile(target));

            return super.install(monitor, repoUris);
        } catch (Exception e) {
            return Messages.createErrorStatus(e);
        } finally {
            if (target.exists()) {
                target.delete();
            }
        }
    }

    public ArtifactRepositoryBean getServerBean() {
        return this.serverBean;
    }

    public void setServerBean(ArtifactRepositoryBean artifactBean) {
        this.serverBean = artifactBean;
    }

}
