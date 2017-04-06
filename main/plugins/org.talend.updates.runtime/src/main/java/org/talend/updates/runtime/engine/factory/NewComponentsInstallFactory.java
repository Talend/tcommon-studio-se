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
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.talend.commons.runtime.helper.LocalComponentInstallHelper;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.updates.runtime.i18n.Messages;
import org.talend.updates.runtime.model.ExtraFeature;
import org.talend.updates.runtime.model.FeatureRepositories;
import org.talend.updates.runtime.model.P2ExtraFeature;
import org.talend.updates.runtime.model.P2ExtraFeatureException;
import org.talend.updates.runtime.model.UpdateSiteLocationType;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class NewComponentsInstallFactory extends AbstractExtraUpdatesFactory {

    // private static Logger log = Logger.getLogger(NewComponentsInstallFactory.class);

    static class ComponentInstallP2ExtraFeature extends P2ExtraFeature {

        private boolean needRestart;

        private File workFolder;

        ComponentInstallP2ExtraFeature() {
            name = Messages.getString("installing.new.components.name"); //$NON-NLS-1$
            description = Messages.getString("installing.new.components.description"); //$NON-NLS-1$
            version = VersionUtils.getDisplayVersion();

            p2IuId = "org.talend.components." + System.currentTimeMillis(); // make sure it will do always.
            useLegacyP2Install = true; // enable to modify the config.ini
            mustBeInstalled = false;
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
        public EnumSet<UpdateSiteLocationType> getUpdateSiteCompatibleTypes() {
            // return super.getUpdateSiteCompatibleTypes();
            return EnumSet.of(UpdateSiteLocationType.LOCAL_FOLDER);
        }

        @Override
        public IStatus install(IProgressMonitor progress, List<URI> allRepoUris) throws P2ExtraFeatureException {
            if (progress == null) {
                progress = new NullProgressMonitor();
            }
            SubMonitor subMonitor = SubMonitor.convert(progress, 100);
            try {
                for (URI uri : allRepoUris) {
                    final String host = uri.getHost();
                    if (host != null) { // remote site?
                        // return super.install(progress, allRepoUris); // install
                    } else { // local? jar:file:/xxx/yy.zip!/
                        if (subMonitor.isCanceled()) {
                            return new Status(IStatus.CANCEL, Messages.getPlugiId(), "Cancel"); //$NON-NLS-1$
                        }
                        prepareForLocalURI(uri);
                    }
                }
                subMonitor.worked(20);
                if (subMonitor.isCanceled()) {
                    return new Status(IStatus.CANCEL, Messages.getPlugiId(), "Cancel"); //$NON-NLS-1$
                }
                return doInstallFromFolder();
            } catch (IOException e) {
                throw new P2ExtraFeatureException(new ProvisionException("Can't copy file", e));
            } finally {
                subMonitor.setWorkRemaining(10);
                afterInstall();
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
        protected void afterInstall() {
            if (getWorkFolder() != null) {
                FilesUtils.deleteFolder(getWorkFolder(), true);
            }
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
