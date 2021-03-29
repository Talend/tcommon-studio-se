// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.resource.UpdatesHelper;
import org.talend.core.GlobalServiceRegister;
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

    private static boolean isTalendDebug = CommonsPlugin.isDebugMode();

    private static Logger log = Logger.getLogger(ComponentsDeploymentManager.class);

    private final ComponentIndexManager indexManager;

    private ComponentSyncManager syncManager;

    private File workFolder;

    public ComponentsDeploymentManager() {
        super();
        indexManager = new ComponentIndexManager();
        syncManager = new ComponentSyncManager(NexusServerManager.getInstance().getComponentShareRepositoryFromServer());
    }

    public boolean deployComponentsToArtifactRepository(IProgressMonitor progress, File componentFile) {
        if (componentFile == null || !componentFile.exists() || !componentFile.isFile()) {
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
            if (!syncManager.isRepositoryServerAvailable(progress, mvnArtifact)) {
                log.error("Failed to sync component: " + mvnArtifact);
                return false;
            }
            List<MavenArtifact> search = syncManager.search(mvnArtifact);
            if (search != null && search.size() > 0) {
                debugLog("Artifact already exists on server: " + mvnArtifact);
                return false;
            }
            syncManager.deploy(componentFile, mvnArtifact);

            MavenArtifact indexArtifact = indexManager.getIndexArtifact();
            File indexFile = null;

            try {
                try {
                    indexFile = syncManager.downloadIndexFile(progress, indexArtifact);
                } catch (FileNotFoundException e) {
                    if (isTalendDebug) {
                        log.error(e.getMessage(), e);
                    }
                    // FileNotFoundException is means that file is not exists on server, so need to create one
                    indexFile = new File(getWorkFolder(), indexArtifact.getFileName(false));
                    boolean created = indexManager.createIndexFile(indexFile, compIndexBean);
                    if (!created) {
                        debugLog("index file creation failed");
                        return false;
                    }
                }

                if (indexFile != null && indexFile.exists()) {
                    boolean updated = indexManager.updateIndexFile(indexFile, compIndexBean);
                    if (!updated) {
                        return false;
                    }
                } else {
                    debugLog("Can't get index file to update");
                }
            } catch (Exception e) {
                throw e;
            }
            syncManager.deploy(indexFile, indexArtifact);

            /**
             * components won't be moved to shared folder anymore since ticket
             * https://jira.talendforge.org/browse/TUP-23536
             */
            // moveToSharedFolder(componentFile);

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

    private void debugLog(String message) {
        if (isTalendDebug) {
            log.info(message);
        }
    }

}
