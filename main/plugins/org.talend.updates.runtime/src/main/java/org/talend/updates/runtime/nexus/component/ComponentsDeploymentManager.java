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
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.model.general.INexusService;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.RepositoryArtifactHandlerManager;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.updates.runtime.UpdatesRuntimePlugin;
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

    private ProjectPreferenceManager prefManager;

    public ComponentsDeploymentManager() {
        super();
        indexManager = new ComponentIndexManager();
        prefManager = new ProjectPreferenceManager(UpdatesRuntimePlugin.BUNDLE_ID);
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

    public boolean deployComponentsToArtifactRepository(IProgressMonitor progress, File componentFile) throws IOException {
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
        if (!UpdatesHelper.isComponentUpdateSite(componentFile) && !isCar) {
            return false;
        }
        ComponentIndexBean compIndexBean = null;
        if (!isCar) {
            compIndexBean = indexManager.create(componentFile);
            if (compIndexBean == null) {
                return false;
            }
        } else {
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
                indexFile = handler.resolve(MavenUrlHelper.generateMvnUrl(indexArtifact));
                if (indexFile != null && indexFile.exists()) {
                    boolean updated = indexManager.updateIndexFile(indexFile, compIndexBean);
                    if (!updated) {
                        return false;
                    }
                }
            } catch (Exception e) {
                indexFile = new File(getWorkFolder(), indexArtifact.getFileName(false));
                boolean created = indexManager.createIndexFile(indexFile, compIndexBean);
                if (!created) {
                    return false;
                }
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
        boolean enableShare = prefManager.getBoolean("repository.share.enable"); //$NON-NLS-1$
        if (!enableShare) {
            return null;
        }
        String repositoryId = prefManager.getValue("repository.share.repository.id"); //$NON-NLS-1$
        if (StringUtils.isBlank(repositoryId)) {
            return null;
        }
        INexusService nexusService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(INexusService.class)) {
            nexusService = (INexusService) GlobalServiceRegister.getDefault().getService(INexusService.class);
        }
        if (nexusService == null) {
            return null;
        }
        ArtifactRepositoryBean artifactRepisotory = nexusService.getArtifactRepositoryFromServer();
        if (artifactRepisotory == null) {
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
