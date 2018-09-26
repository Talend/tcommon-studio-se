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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.talend.commons.runtime.helper.LocalComponentInstallHelper;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.updates.runtime.engine.component.RemoteComponentsTransport;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureRepositories;
import org.talend.updates.runtime.model.P2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.model.UpdateSiteLocationType;
import org.talend.updates.runtime.model.interfaces.IP2ComponentFeature;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class NewComponentsInstallFactory extends AbstractExtraUpdatesFactory implements IComponentUpdatesFactory {

    public static final String FILE_COMPONENTS_INDEX = "components.index";

    // private static Logger log = Logger.getLogger(NewComponentsInstallFactory.class);

    static class ComponentInstallP2ExtraFeature extends P2ExtraFeature implements IP2ComponentFeature {

        private boolean needRestart;

        private File workFolder;

        ComponentInstallP2ExtraFeature() {
            super("org.talend.components." + System.currentTimeMillis(), Messages.getString("installing.new.components.name"),
                    VersionUtils.getDisplayVersion(), Messages.getString("installing.new.components.description"), null, null,
                    null, null, null, null, null, false, null, false, true);
        }

        @Override
        public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
            if (System.getProperties().containsKey("components.updatesite.url")) { // support default url
                return EnumSet.of(UpdateSiteLocationType.DEFAULT_REPO, UpdateSiteLocationType.REMOTE_REPO);
            } else {
                return EnumSet.of(UpdateSiteLocationType.REMOTE_REPO);
            }
        }

        File getWorkFolder() {
            if (workFolder == null) {
                workFolder = org.talend.utils.files.FileUtils.createTmpFolder("NewComponents", "");//$NON-NLS-1$  //$NON-NLS-2$
            }
            return workFolder;
        }

        @Override
        public boolean isInstalled(IProgressMonitor progress) throws P2ExtraFeatureException {
            return false;
        }

        @Override
        public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
            if (progress == null) {
                progress = new NullProgressMonitor();
            }
            SubMonitor subMonitor = SubMonitor.convert(progress, 100);
            try {
                for (URI uri : allRepoUris) {
                    if (subMonitor.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    final String host = uri.getHost();
                    if (host != null) { // remote site?
                        // return super.install(progress, allRepoUris); // install
                        prepareForSite(progress, uri);
                    } else { // local? jar:file:/xxx/yy.zip!/
                        prepareForLocalURI(uri);
                    }
                }
                subMonitor.worked(20);
                if (subMonitor.isCanceled()) {
                    throw new OperationCanceledException();
                }
                return doInstallFromFolder();
            } catch (OperationCanceledException e) {
                return new Status(IStatus.CANCEL, Messages.getPlugiId(), "Cancel"); //$NON-NLS-1$
            } catch (Exception e) {
                throw new P2ExtraFeatureException(new ProvisionException("Install failure", e));
            } finally {
                subMonitor.setWorkRemaining(10);
                if (getWorkFolder() != null) {
                    FilesUtils.deleteFolder(getWorkFolder(), true);
                }
            }
        }

        void prepareForSite(IProgressMonitor monitor, URI base) throws Exception {
            RemoteComponentsTransport compsTransport = new RemoteComponentsTransport(getWorkFolder());
            compsTransport.download(monitor, base);

            final List<String> failedDownloadComponents = compsTransport.getFailedDownloadComponents();
            if (!failedDownloadComponents.isEmpty()) {
                StringBuffer failedCompStr = new StringBuffer();
                for (int i = 0; i < failedDownloadComponents.size(); i++) {
                    failedCompStr.append(failedDownloadComponents.get(i));
                    if (i < failedDownloadComponents.size() - 1) {
                        failedCompStr.append(',');
                        failedCompStr.append(' ');
                    }
                }
                throw new IOException("Download the following components failure: " + failedCompStr.toString());
            }
        }

        void prepareForLocalURI(URI uri) throws IOException {
            if (uri == null) {
                return;
            }
            final String scheme = uri.getScheme();
            if (scheme != null && FeatureRepositories.URI_JAR_SCHEMA.equals(scheme)) {// local zip file
                final String rawSchemeSpecificPart = uri.getRawSchemeSpecificPart();
                if (rawSchemeSpecificPart != null) {
                    String path = rawSchemeSpecificPart.substring(0, rawSchemeSpecificPart.length()
                            - FeatureRepositories.URI_JAR_SUFFIX.length());
                    path = path.substring(FeatureRepositories.URI_FILE_SCHEMA.length() + 1); // file:
                    final File file = new File(path);
                    if (file.exists() && UpdatesHelper.isComponentUpdateSite(file)) {
                        FilesUtils.copyFile(file, new File(getWorkFolder(), file.getName()));
                        //
                    }
                }
            }// else { // other file

        }

        Status doInstallFromFolder() {
            if (getWorkFolder() == null || getWorkFolder().list() == null || getWorkFolder().list().length == 0) {
                return null;
            }
            ComponentsInstallComponent installComponent = getInstallComponent();
            try {
                installComponent.setComponentFolder(getWorkFolder());

                boolean installed = installComponent.install();
                needRestart = installComponent.needRelaunch();

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
            } finally {
                FilesUtils.deleteFolder(getWorkFolder(), true);
                installComponent.setComponentFolder(null); // set back
            }
        }

        ComponentsInstallComponent getInstallComponent() {
            return LocalComponentInstallHelper.getComponent();
        }

        @Override
        public boolean needRestart() {
            return needRestart;
        }

        @Override
        public boolean isShareEnable() {
            return share;
        }

        @Override
        public void setShareEnable(boolean share) {
            this.share = share;
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.updates.runtime.engine.factory.AbstractExtraUpdatesFactory#retrieveUninstalledExtraFeatures(org.eclipse
     * .core.runtime.IProgressMonitor, java.util.Set)
     */
    @Override
    public void retrieveUninstalledExtraFeatures(IProgressMonitor monitor, Set<ExtraFeature> uninstalledExtraFeatures)
            throws Exception {
        addToSet(uninstalledExtraFeatures, new ComponentInstallP2ExtraFeature());
    }

}
