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

import org.apache.commons.lang3.StringUtils;
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
import org.talend.updates.runtime.nexus.component.NexusServerManager;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeature extends ComponentP2ExtraFeature {

    private String nexusURL, nexusUser;

    private char[] nexusPass;

    protected ArtifactRepositoryBean serverSetting;

    public ComponentNexusP2ExtraFeature() {
        super();
    }

    public ArtifactRepositoryBean getServerSetting() {
        if (serverSetting == null) {
            serverSetting = NexusServerManager.getInstance().getPropertyNexusServer();
        }
        return serverSetting;
    }

    public ComponentNexusP2ExtraFeature(ComponentIndexBean indexBean) {
        super(indexBean);
    }

    public ComponentNexusP2ExtraFeature(String name, String version, String description, String product, String mvnURI,
            String p2IuId) {
        super(name, version, description, product, mvnURI, p2IuId);
    }

    public String getNexusURL() {
        if (nexusURL == null && getServerSetting() != null) {
            setNexusURL(getServerSetting().getRepositoryURL());
        }
        return nexusURL;
    }

    public void setNexusURL(String nexusURL) {
        this.nexusURL = nexusURL;
        this.baseRepoUriStr = nexusURL; // same url
    }

    public String getNexusUser() {
        if (nexusUser == null && getServerSetting() != null) {
            setNexusUser(getServerSetting().getUserName());
        }
        return nexusUser;
    }

    public void setNexusUser(String nexusUser) {
        this.nexusUser = nexusUser;
    }

    public char[] getNexusPass() {
        if (nexusPass == null && getServerSetting() != null) {
            final String pass = getServerSetting().getPassword();
            if (StringUtils.isNotEmpty(pass)) {
                setNexusPass(pass.toCharArray());
            }
        }
        return nexusPass;
    }

    public void setNexusPass(char[] nexusPass) {
        this.nexusPass = nexusPass;
    }

    public void setNexusPass(String nexusPass) {
        if (StringUtils.isNotEmpty(nexusPass)) {
            setNexusPass(nexusPass.toCharArray());
        } else {
            setNexusPass((char[]) null);
        }
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
            NexusComponentsTransport transport = new NexusComponentsTransport(getNexusURL(), getNexusUser(), getNexusPass());
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

}
