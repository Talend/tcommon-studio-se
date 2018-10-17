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
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.updates.runtime.feature.model.Type;
import org.talend.updates.runtime.model.interfaces.ITaCoKitCarFeature;
import org.talend.updates.runtime.service.ITaCoKitUpdateService;
import org.talend.updates.runtime.utils.PathUtils;
import org.talend.utils.io.FilesUtils;

/**
 *
 * created by ycbai on 2017年5月23日 Detailled comment
 *
 */
public class ComponentsDeploymentManager {

    private IRepositoryArtifactHandler repositoryHandler;

    private final ComponentIndexManager indexManager;

    private File workFolder;

    public ComponentsDeploymentManager() {
        super();
        indexManager = new ComponentIndexManager();
    }

    public boolean deployComponentsToLocalNexus(IProgressMonitor progress, File componentZipFile) throws IOException {
        ArtifactRepositoryBean localNexusServer = NexusServerManager.getInstance().getLocalNexusServer();
        if (localNexusServer == null) {
            return false;
        }
        NexusShareComponentsManager nexusShareComponentsManager = new NexusShareComponentsManager(localNexusServer);
        if (nexusShareComponentsManager.getNexusTransport().isAvailable()) {
            boolean deployed = nexusShareComponentsManager.deployComponent(progress, componentZipFile);
            if (deployed) {
                moveToSharedFolder(componentZipFile);
                return true;
            }
        }
        return false;
    }

    public boolean deployComponentsToArtifactRepository(IProgressMonitor progress, File componentFile) {
        if (componentFile == null || !componentFile.exists() || !componentFile.isFile()) {
            return false;
        }
        IRepositoryArtifactHandler handler = getRepositoryHandler();
        if (handler == null) {
            return false;
        }
        if (!handler.checkConnection(true, false)) {
            return false;
        }
        boolean isCar = false;
        ITaCoKitUpdateService taCoKitService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ITaCoKitUpdateService.class)) {
            try {
                taCoKitService = (ITaCoKitUpdateService) GlobalServiceRegister.getDefault()
                        .getService(ITaCoKitUpdateService.class);
                isCar = taCoKitService.isCar(componentFile, progress);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        boolean isPlainPatch = UpdatesHelper.isPlainUpdate(componentFile);
        boolean isUpdateSite = UpdatesHelper.isUpdateSite(componentFile);
        boolean isTcompv0UpdateSite = UpdatesHelper.isComponentUpdateSite(componentFile);
        boolean isPatchUpdateSite = isUpdateSite && !isTcompv0UpdateSite;
        if (!isTcompv0UpdateSite && !isCar && !isPlainPatch && !isPatchUpdateSite) {
            return false;
        }
        ComponentIndexBean compIndexBean = null;
        if (isPlainPatch) {
            compIndexBean = indexManager.createIndexBean4Patch(componentFile, Type.PLAIN_ZIP);
        } else if (isPatchUpdateSite) {
            compIndexBean = indexManager.createIndexBean4Patch(componentFile, Type.P2_PATCH);
        } else if (isCar) {
            try {
                compIndexBean = new ComponentIndexBean();
                ITaCoKitCarFeature feature = taCoKitService.generateExtraFeature(componentFile, progress);
                String mvnUri = feature.getMvnUri();
                boolean set = compIndexBean.setRequiredFieldsValue(feature.getName(), feature.getId(), feature.getVersion(),
                        mvnUri);
                if (!set) {
                    return false;
                }
                Collection<Type> types = feature.getTypes();
                compIndexBean.setValue(ComponentIndexNames.types, PathUtils.convert2StringTypes(types));
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        } else {
            // tcompv0
            compIndexBean = indexManager.create(componentFile);
            if (compIndexBean == null) {
                return false;
            }
        }
        MavenArtifact mvnArtifact = compIndexBean.getMavenArtifact();
        if (mvnArtifact == null) {
            return false;
        }
        try {
            handler.deploy(componentFile, mvnArtifact.getGroupId(), mvnArtifact.getArtifactId(), mvnArtifact.getClassifier(),
                    mvnArtifact.getType(), mvnArtifact.getVersion());

            MavenArtifact indexArtifact = indexManager.getIndexArtifact();
            File indexFile = null;

            try {
                ArtifactRepositoryBean artifactServerBean = handler.getArtifactServerBean();
                char[] passwordChars = null;
                String password = artifactServerBean.getPassword();
                if (password != null) {
                    passwordChars = password.toCharArray();
                }

                /**
                 * don't use mvn.resolve to get the index file here, since the resolved file may come from local mvn
                 * repository instead of nexus server
                 */
                final NexusComponentsTransport transport = new NexusComponentsTransport(artifactServerBean.getRepositoryURL(),
                        artifactServerBean.getUserName(), passwordChars);
                if (transport.isAvailable(progress, indexArtifact)) {
                    if (progress.isCanceled()) {
                        throw new OperationCanceledException();
                    }
                    indexFile = File.createTempFile("index", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
                    transport.downloadFile(progress, indexArtifact, indexFile);
                } else {
                    indexFile = new File(getWorkFolder(), indexArtifact.getFileName(false));
                    boolean created = indexManager.createIndexFile(indexFile, compIndexBean);
                    if (!created) {
                        return false;
                    }
                }

                if (indexFile != null && indexFile.exists()) {
                    boolean updated = indexManager.updateIndexFile(indexFile, compIndexBean);
                    if (!updated) {
                        return false;
                    }
                }
            } catch (Exception e) {
                throw e;
            }
            handler.deploy(indexFile, indexArtifact.getGroupId(), indexArtifact.getArtifactId(), indexArtifact.getClassifier(),
                    indexArtifact.getType(), indexArtifact.getVersion());

            moveToSharedFolder(componentFile);
            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    private void moveToSharedFolder(File componentZipFile) throws IOException {
        File sharedCompFile = new File(PathUtils.getComponentsSharedFolder(), componentZipFile.getName());
        if (!componentZipFile.equals(sharedCompFile)) { // not in same folder
            FilesUtils.copyFile(componentZipFile, sharedCompFile);
            boolean deleted = componentZipFile.delete();
            if (!deleted) {// failed to delete in time
                componentZipFile.deleteOnExit(); // try to delete when exit
            }
        }
    }

    private File getWorkFolder() {
        if (workFolder == null) {
            workFolder = org.talend.utils.files.FileUtils.createTmpFolder("test", "index"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return workFolder;
    }

    private IRepositoryArtifactHandler getRepositoryHandler() {
        ArtifactRepositoryBean artifactRepisotory = NexusServerManager.getInstance().getArtifactRepositoryFromTac();
        if (artifactRepisotory == null) {
            return null;
        }
        String repositoryId = NexusServerManager.getInstance().getRepositoryIdForShare();
        if (StringUtils.isBlank(repositoryId)) {
            return null;
        }
        artifactRepisotory.setRepositoryId(repositoryId);
        if (repositoryHandler == null) {
            repositoryHandler = RepositoryArtifactHandlerManager.getRepositoryHandler(artifactRepisotory);
            repositoryHandler.updateMavenResolver(null);
        }
        return repositoryHandler;
    }

}
