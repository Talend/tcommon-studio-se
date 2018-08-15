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
package org.talend.updates.runtime.nexus.component;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class NexusShareComponentsManager {

    private final ArtifactRepositoryBean compNexusServer;

    private final ComponentIndexManager indexManager;

    private final NexusComponentsTransport nexusTransport;

    private File workFolder;

    /**
     * for NexusServerBean, the repositoryId , userName and password are required.
     *
     */
    public NexusShareComponentsManager(final ArtifactRepositoryBean compNexusServer) {
        this.compNexusServer = compNexusServer;
        this.indexManager = new ComponentIndexManager();

        this.nexusTransport = createNexusComponentsTransport();
    }

    NexusComponentsTransport createNexusComponentsTransport() {
        return new NexusComponentsTransport(this.compNexusServer.getRepositoryURL(), this.compNexusServer.getUserName(),
                this.compNexusServer.getPassword() != null ? this.compNexusServer.getPassword().toCharArray() : null);
    }

    public ComponentIndexManager getIndexManager() {
        return indexManager;
    }

    public NexusComponentsTransport getNexusTransport() {
        return nexusTransport;
    }

    File getWorkFolder() {
        if (workFolder == null) {
            workFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "index"); //$NON-NLS-1$  //$NON-NLS-2$
        }
        return workFolder;
    }

    /**
     *
     * after uses this manager, better do clean.
     */
    public void clean() {
        if (workFolder != null) {
            FilesUtils.deleteFolder(workFolder, true);
        }
    }

    public MavenArtifact getIndexArtifact() {
        return indexManager.getIndexArtifact();
    }

    public List<ComponentIndexBean> retrieveComponents(IProgressMonitor monitor) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        return indexManager.parse(downloadIndexFile(monitor));
    }

    File downloadIndexFile(IProgressMonitor monitor) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        final MavenArtifact indexArtifact = getIndexArtifact();
        File indexFile = new File(getWorkFolder(), getIndexArtifact().getFileName(false));

        try {
            nexusTransport.doHttpDownload(monitor, indexFile, indexArtifact);

            monitor.worked(1);
            if (indexFile.exists()) {
                return indexFile;
            }
        } catch (Exception e) {
            if (CommonsPlugin.isDebugMode()) {
                ExceptionHandler.process(e);
            }
        }
        return null;
    }

    /**
     *
     * DOC ggu Comment method "deployComponent".
     *
     * deploy the component zip file to remote nexus, and will read some index details from zip file.
     *
     */
    public boolean deployComponent(IProgressMonitor monitor, File componentZipFile) {
        return deployComponent(monitor, componentZipFile, null);
    }

    /**
     *
     * DOC ggu Comment method "deployComponent".
     *
     * deploy the component zip file to remote nexus, if index bean is null, will try to retrieve from zip file.
     */
    public boolean deployComponent(IProgressMonitor monitor, File componentZipFile, ComponentIndexBean compIndexBean) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        SubMonitor subMonitor = SubMonitor.convert(monitor, 3);
        if (subMonitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (componentZipFile == null || !componentZipFile.exists() || !componentZipFile.isFile()
                || !UpdatesHelper.isComponentUpdateSite(componentZipFile)) {
            return false;
        }

        // 0. if no index bean, retrieve from zip file directly.
        if (compIndexBean == null) {
            // build on index from component zip file directly.
            compIndexBean = getIndexManager().create(componentZipFile);
            if (compIndexBean == null) {
                return false;
            }
            subMonitor.worked(1);
        }

        // 1. deploy new component
        MavenArtifact mvnArtifact = MavenUrlHelper.parseMvnUrl(compIndexBean.getMvnURI());
        if (mvnArtifact == null) {
            return false;
        }
        try {
            nexusTransport.doHttpUpload(monitor, componentZipFile, mvnArtifact);
            subMonitor.worked(1);
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return false;
        }
        if (subMonitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        // 2. update index

        // if not existed in nexus server, should try to upload new index
        final MavenArtifact indexArtifact = getIndexArtifact();
        File indexFile = null;
        if (nexusTransport.isAvailable(subMonitor, indexArtifact)) {
            indexFile = downloadIndexFile(monitor);
            if (indexFile == null) { // download failure
                return false;
            }
            boolean updated = indexManager.updateIndexFile(indexFile, compIndexBean);
            if (!updated) {
                return false;
            }
        } else { // create new
            indexFile = new File(getWorkFolder(), indexArtifact.getFileName(false));
            boolean created = indexManager.createIndexFile(indexFile, compIndexBean);
            if (!created) {
                return false;
            }
        }
        subMonitor.worked(1);
        if (subMonitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        if (!indexFile.exists()) { // update failure or didn't create
            return false;
        }
        // 3. upload updated index
        try {

            nexusTransport.doHttpUpload(monitor, indexFile, indexArtifact);
            subMonitor.worked(1);
        } catch (Exception e) {
            ExceptionHandler.process(e);
            return false;
        }
        return true;
    }

    /**
     *
     * DOC ggu Comment method "deployComponents".
     *
     * will share all components under the folder, and return the shared files.
     */
    public List<File> deployComponents(IProgressMonitor monitor, File compFolder) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (compFolder == null || !compFolder.exists()) {
            return Collections.emptyList();
        }
        File[] zipFile = compFolder.listFiles(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return UpdatesHelper.isComponentUpdateSite(f);
            }
        });
        if (zipFile == null) {
            return Collections.emptyList();
        }
        SubMonitor subMonitor = SubMonitor.convert(monitor, zipFile.length);
        if (subMonitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        List<File> sharedComponents = new ArrayList<File>();

        for (File componentZipFile : zipFile) {
            boolean deployed = deployComponent(subMonitor, componentZipFile);
            subMonitor.worked(1);
            if (subMonitor.isCanceled()) {
                throw new OperationCanceledException();
            }

            if (deployed) {
                sharedComponents.add(componentZipFile);
            }
        }

        return sharedComponents;
    }

    public ComponentStatus checkComponent(IProgressMonitor monitor, ComponentIndexBean compIndexBean) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }
        if (compIndexBean == null) {
            return ComponentStatus.NONE;
        }

        final List<ComponentIndexBean> remoteComponents = retrieveComponents(monitor);
        return checkComponent(monitor, remoteComponents, compIndexBean);
    }

    public ComponentStatus checkComponent(IProgressMonitor monitor, final List<ComponentIndexBean> remoteComponents,
            ComponentIndexBean compIndexBean) {
        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        if (monitor.isCanceled()) {
            throw new OperationCanceledException();
        }

        if (remoteComponents == null || compIndexBean == null) {
            return ComponentStatus.NONE;
        }
        if (remoteComponents.isEmpty()) {
            return new ComponentStatus(compIndexBean, ComponentStatus.NEW_LATEST);
        }
        // find all existed
        List<ComponentIndexBean> existedComps = new ArrayList<ComponentIndexBean>();
        for (ComponentIndexBean bean : remoteComponents) {
            if (bean.sameComponent(compIndexBean)) {
                existedComps.add(bean);
            }
        }

        //
        if (!existedComps.isEmpty()) {
            Collections.sort(existedComps, new Comparator<ComponentIndexBean>() {

                @Override
                public int compare(ComponentIndexBean b1, ComponentIndexBean b2) {
                    return b1.compareVersion(b2); // only sort via version
                }
            });
        }

        int statusCode = 0;
        if (existedComps.isEmpty()) { // empty,
            statusCode = ComponentStatus.NEW_LATEST;
        } else {
            boolean existed = existedComps.contains(compIndexBean); // check required settings
            // the latest existed one
            final ComponentIndexBean lastBean = existedComps.get(existedComps.size() - 1);
            final int compareVersion = compIndexBean.compareVersion(lastBean);
            if (compareVersion == 0) { // same version
                if (existed) {
                    statusCode = ComponentStatus.EXIST_LATEST;
                } else {// same version, but different other required settings
                        // PTODO???
                }
            } else if (compareVersion == -1) { // current one is lower
                if (existed) {
                    statusCode = ComponentStatus.EXIST_LOWER;
                } else { // current is old version,but not existed
                    statusCode = ComponentStatus.NEW_LOWER;
                }
            } else if (compareVersion == 1) { // current one is new latest version
                if (existed) {// should never happen
                    //
                } else {
                    statusCode = ComponentStatus.NEW_LATEST;
                }
            }// else {//-2 different component
        }

        ComponentStatus status = new ComponentStatus(compIndexBean, statusCode);
        status.getExitedComponents().addAll(existedComps);
        return status;
    }

}
