// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.talend.commons.runtime.helper.LocalComponentInstallHelper;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.components.IComponent;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.repository.model.IRepositoryService;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.model.UpdateSiteLocationType;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ComponentNexusP2ExtraFeature extends P2ExtraFeature {

    public static final String INDEX = "index";

    public static final String COMPONENT_GROUP_ID = "org.talend.components";

    /*
     * nexus
     */
    public static final String PROP_KEY_NEXUS_URL = "components.nexus.url";

    public static final String PROP_KEY_NEXUS_USER = "components.nexus.user";

    public static final String PROP_KEY_NEXUS_PASS = "components.nexus.pass";

    private String product, mvnURI;

    private boolean upgrade;

    private String nexusURL, nexusUser;

    private char[] nexusPass;

    protected File downloadFolder;

    protected boolean restartable;

    protected boolean keepDownloadFile = false;

    public ComponentNexusP2ExtraFeature() {
        //
    }

    public ComponentNexusP2ExtraFeature(String name, String version, String description, String product, String mvnURI) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.product = product;
        this.mvnURI = mvnURI;
        this.p2IuId = "org.talend.components." + name + '.' + version; //

        this.useLegacyP2Install = true; // enable to modify the config.ini
        this.mustBeInstalled = false;
    }

    public String getNexusURL() {
        if (nexusURL == null) {
            setNexusURL(System.getProperty(PROP_KEY_NEXUS_URL));
        }
        return nexusURL;
    }

    public void setNexusURL(String nexusURL) {
        this.nexusURL = nexusURL;
        this.baseRepoUriStr = nexusURL; // same url
    }

    public String getNexusUser() {
        if (nexusUser == null) {
            setNexusUser(System.getProperty(PROP_KEY_NEXUS_USER));
        }
        return nexusUser;
    }

    public void setNexusUser(String nexusUser) {
        this.nexusUser = nexusUser;
    }

    public char[] getNexusPass() {
        if (nexusPass == null) {
            final String pass = System.getProperty(PROP_KEY_NEXUS_PASS);
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

    public File getDownloadFolder() {
        return downloadFolder;
    }

    public void setDownloadFolder(File downloadFolder) {
        this.downloadFolder = downloadFolder;
    }

    public boolean isKeepDownloadFile() {
        return keepDownloadFile;
    }

    public void setKeepDownloadFile(boolean keepDownloadFile) {
        this.keepDownloadFile = keepDownloadFile;
    }

    public String getProduct() {
        return product;
    }

    public String getMvnURI() {
        return mvnURI;
    }

    public boolean isUpgrade() {
        return upgrade;
    }

    @Override
    public boolean needRestart() {
        return restartable;
    }

    @Override
    public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
        return EnumSet.of(UpdateSiteLocationType.DEFAULT_REPO);
    }

    public MavenArtifact getIndexArtifact() {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId(COMPONENT_GROUP_ID);
        artifact.setArtifactId(INDEX);
        artifact.setVersion(getTalendVersionStr());
        artifact.setType(FileExtensions.XML_EXTENSION);
        return artifact;
    }

    public MavenArtifact getArtifact() {
        return MavenUrlHelper.parseMvnUrl(mvnURI);
    }

    protected String getTalendVersionStr() {
        org.osgi.framework.Version studioVersion = new org.osgi.framework.Version(VersionUtils.getTalendVersion());

        StringBuffer result = new StringBuffer();
        result.append(studioVersion.getMajor());
        result.append('.');
        result.append(studioVersion.getMinor());
        result.append('.');
        result.append(studioVersion.getMicro());

        return result.toString();
    }

    String findExistedComponentVersion() {
        if (StringUtils.isNotEmpty(this.name) && GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            IRepositoryService service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(
                    IRepositoryService.class);
            if (service != null) {
                final Map<String, Set<IComponent>> map = service.getComponentsFactory().getComponentNameMap()
                        .get(this.name.toLowerCase());
                if (map != null) {
                    final Set<IComponent> set = map.get(this.name);
                    if (set != null) {
                        org.osgi.framework.Version maxVer = null;
                        final Iterator<IComponent> iterator = set.iterator();
                        while (iterator.hasNext()) {
                            final IComponent c = iterator.next();
                            if (StringUtils.isNotEmpty(c.getVersion())) {
                                org.osgi.framework.Version v = new org.osgi.framework.Version(c.getVersion());
                                if (maxVer == null || maxVer.compareTo(v) < 0) {
                                    maxVer = v;
                                }
                            }
                        }
                        return maxVer.toString();
                    }
                }

            }
        }
        return null;
    }

    @Override
    public boolean isInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
        final String findExistedComponentVersion = findExistedComponentVersion();
        if (findExistedComponentVersion != null) {
            if (StringUtils.isNotEmpty(findExistedComponentVersion) && StringUtils.isNotEmpty(this.version)) {
                org.osgi.framework.Version compVer = new org.osgi.framework.Version(findExistedComponentVersion);
                org.osgi.framework.Version ver = new org.osgi.framework.Version(this.version);
                final int compare = ver.compareTo(compVer);
                if (compare > 0) { // need upgrade
                    return false;
                } else {// //same version or low version, nothing to do
                    return true; // low version. no need to install
                }
            } else { // have installed one, but still try to install again.
                return false;
            }
        }// else // not existed
        return false;
    }

    public boolean needUpgrade(IProgressMonitor progress) throws P2ExtraFeatureException {
        if (StringUtils.isEmpty(this.version)) {
            return false;
        }
        final String findExistedComponentVersion = findExistedComponentVersion();
        if (findExistedComponentVersion != null) {
            if (StringUtils.isNotEmpty(findExistedComponentVersion)) {
                org.osgi.framework.Version compVer = new org.osgi.framework.Version(findExistedComponentVersion);
                org.osgi.framework.Version ver = new org.osgi.framework.Version(this.version);
                final int compare = ver.compareTo(compVer);
                if (compare > 0) { // need upgrade
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ExtraFeature createFeatureIfUpdates(IProgressMonitor progress) throws ProvisionException {
        this.upgrade = true;
        return this;
    }

    @Override
    public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
        if (progress.isCanceled()) {
            throw new OperationCanceledException();
        }
        final File workFolder = getDownloadFolder();
        FilesUtils.deleteFolder(workFolder, false); // empty the folder
        if (!workFolder.exists()) {
            workFolder.mkdirs();
        }

        String reletivePath = PomUtil.getArtifactPath(getArtifact());
        if (reletivePath == null) {
            return Messages.createErrorStatus(null, "Can't install");
        }

        String compFileName = new Path(reletivePath).lastSegment();
        final File target = new File(workFolder, compFileName);

        final ComponentsInstallComponent installComponent = LocalComponentInstallHelper.getComponent();
        try {
            ComponentsNexusTransport transport = new ComponentsNexusTransport(getNexusURL(), getNexusUser(), getNexusPass());
            transport.downloadFile(progress, mvnURI, target);

            if (progress.isCanceled()) {
                throw new OperationCanceledException();
            }
            if (!target.exists()) {
                return Messages.createErrorStatus(null, "failed.install.of.feature", "Download the failure for " + mvnURI); //$NON-NLS-1$ //$NON-NLS-2$
            }

            installComponent.setComponentFolder(workFolder);

            boolean installed = installComponent.install();
            this.restartable = installComponent.needRelaunch();

            final String installedMessages = installComponent.getInstalledMessages();
            final List<File> failedComponents = installComponent.getFailedComponents();
            final String failureMessage = installComponent.getFailureMessage();

            if ((failedComponents == null || failedComponents.isEmpty())) { // no error
                if (installed) { // all installed
                    return Messages.createOkStatus("sucessfull.install.of.feature", getName() + "\n" + installedMessages); //$NON-NLS-1$
                } else {
                    // no component to install.
                    return new Status(IStatus.INFO, Messages.getPlugiId(), "No any components to install"); //$NON-NLS-1$
                }
            } else {
                if (installed) { // some installed
                    return new Status(IStatus.ERROR, Messages.getPlugiId(), installedMessages + "\n\n" + failureMessage); //$NON-NLS-1$
                } else { // all failure
                    return Messages.createErrorStatus(null, "failed.install.of.feature", failureMessage); //$NON-NLS-1$
                }
            }

        } catch (Exception e) {
            return Messages.createErrorStatus(e);
        } finally {
            if (!isKeepDownloadFile() && target.exists()) {
                target.delete();
            }
            installComponent.setComponentFolder(null);
        }
    }
}
