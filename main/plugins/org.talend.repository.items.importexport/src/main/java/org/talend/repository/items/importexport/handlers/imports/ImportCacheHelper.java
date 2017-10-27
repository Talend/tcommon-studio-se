// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.items.importexport.handlers.imports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.recyclebin.RecycleBinManager;
import org.talend.core.repository.ui.actions.RestoreFolderUtil;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.repository.ProjectManager;
import org.talend.repository.items.importexport.handlers.cache.RepositoryObjectCache;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.model.IProxyRepositoryFactory;

/**
 * DOC ggu class global comment. Detailled comment
 */
public final class ImportCacheHelper {

    private static final ImportCacheHelper instance = new ImportCacheHelper();

    public static ImportCacheHelper getInstance() {
        return instance;
    }

    /**
     * caches
     */
    private final Map<IPath, Project> pathWithProjects = new HashMap<IPath, Project>();

    private final Set<Project> updatedProjects = new HashSet<Project>();

    private final RepositoryObjectCache repObjectcache = new RepositoryObjectCache();

    private Map<String, Boolean> migrationTasksStatusPerProject = new HashMap<String, Boolean>();

    private Map<String, List<MigrationTask>> migrationTasksToApplyPerProject = new HashMap<String, List<MigrationTask>>();

    private Map<ERepositoryObjectType, Set<String>> foldersCreated = new HashMap<ERepositoryObjectType, Set<String>>();

    private final Set<String> deletedItems = new HashSet<String>();

    private final XmiResourceManager xmiResourceManager = new XmiResourceManager();

    private RestoreFolderUtil restoreFolder = new RestoreFolderUtil();

    private boolean hasImportingError = false;

    private final List<String> importErrors = new ArrayList<String>();

    private List<ImportItem> importedItemRecords = new ArrayList<ImportItem>();

    public RepositoryObjectCache getRepObjectcache() {
        return this.repObjectcache;
    }

    public Map<IPath, Project> getPathWithProjects() {
        return this.pathWithProjects;
    }

    public Set<Project> getUpdatedProjects() {
        return this.updatedProjects;
    }

    public Map<String, Boolean> getMigrationTasksStatusPerProject() {
        return this.migrationTasksStatusPerProject;
    }

    public Map<String, List<MigrationTask>> getMigrationTasksToApplyPerProject() {
        return this.migrationTasksToApplyPerProject;
    }

    public boolean hasImportingError() {
        return this.hasImportingError;
    }

    public synchronized void setImportingError(boolean hasImportingError) {
        this.hasImportingError = hasImportingError;
    }

    public List<String> getImportErrors() {
        return this.importErrors;
    }

    public synchronized XmiResourceManager getXmiResourceManager() {
        return this.xmiResourceManager;
    }

    public synchronized Map<ERepositoryObjectType, Set<String>> getFoldersCreated() {
        return this.foldersCreated;
    }

    public synchronized RestoreFolderUtil getRestoreFolder() {
        return this.restoreFolder;
    }

    public synchronized Set<String> getDeletedItems() {
        return this.deletedItems;
    }

    public synchronized void beforePopulateItems() {
        clearAll();
    }

    public synchronized void afterPopulateItems() {
        //
    }

    private void clearAll() {
        // clean
        pathWithProjects.clear();
        updatedProjects.clear();
        foldersCreated.clear();
        deletedItems.clear();

        migrationTasksStatusPerProject.clear();
        migrationTasksToApplyPerProject.clear();

        if ((!CommonsPlugin.isSameProjectLogonCommline() && CommonsPlugin.isHeadless()) || !CommonsPlugin.isHeadless()
                || !ProjectManager.getInstance().getCurrentProject().isLocal()) {
            repObjectcache.clear();
        }
        xmiResourceManager.unloadResources();
        xmiResourceManager.resetResourceSet();

        restoreFolder.clear();
        importedItemRecords.clear();
        importErrors.clear();
    }

    public synchronized void beforeImportItems() {
        setImportingError(false);
        // clean the import record
        importedItemRecords.clear();
    }

    public synchronized void afterImportItems() {
        setImportingError(false);
        clearAll();
    }

    public synchronized void checkDeletedFolders() {
        ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        if (!foldersCreated.isEmpty()) {
            for (ERepositoryObjectType itemType : foldersCreated.keySet()) {
                for (String folder : foldersCreated.get(itemType)) {
                    FolderItem folderItem = repFactory.getFolderItem(ProjectManager.getInstance().getCurrentProject(), itemType,
                            new Path(folder));
                    if (folderItem != null) {
                        folderItem.getState().setDeleted(true);
                    }
                }
            }
            foldersCreated.clear();
        }
    }

    public synchronized void checkDeletedItems() {
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        try {
            for (ImportItem itemRecord : importedItemRecords) {
                if (itemRecord.isValid() && itemRecord.getItemId() != null) {
                    IRepositoryViewObject obj = factory.getLastVersion(itemRecord.getItemId());
                    if (obj != null && obj.getProperty() != null) {
                        Item item = obj.getProperty().getItem();
                        ERepositoryStatus status = factory.getStatus(item);
                        if (status != null && status == ERepositoryStatus.DELETED) {
                            RecycleBinManager.getInstance().addToRecycleBin(ProjectManager.getInstance().getCurrentProject(),
                                    item);
                        }
                    }
                }
            }
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
    }

    public synchronized List<ImportItem> getImportedItemRecords() {
        return this.importedItemRecords;
    }

}
