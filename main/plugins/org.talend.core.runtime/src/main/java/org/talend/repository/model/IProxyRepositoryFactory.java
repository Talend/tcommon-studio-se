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
package org.talend.repository.model;

import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.IRepositoryWorkUnitListener;
import org.talend.core.model.repository.LockInfo;
import org.talend.repository.RepositoryWorkUnit;

/**
 * DOC qian class global comment. Repository factory use by client. Based on implementation provide by extension point
 * system. This class contains all commons treatments done by repository whatever implementation. <br/>
 * 
 * $Id: talend-code-templates.xml 1 2006-09-29 17:06:40 +0000 (星期五, 29 九月 2006) nrousseau $
 * 
 */
public interface IProxyRepositoryFactory {

    public static final String BRANCH_SELECTION = "branchSelection"; //$NON-NLS-1$

    public abstract RepositoryContext getRepositoryContext();

    public void refreshJobPictureFolder(String picFolder);

    public void refreshDocumentationFolder(String docFolder);

    public void addPropertyChangeListener(PropertyChangeListener l);

    public void removePropertyChangeListener(PropertyChangeListener l);

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.repository.model.IMetadataFactory#getMetadataConnections(org.talend.core.model.general.Project)
     */
    public abstract List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException;

    public abstract List<ContextItem> getContextItem() throws PersistenceException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isValid(org.talend.core .model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public abstract boolean isNameAvailable(Item item, String name, List<IRepositoryViewObject>... givenList)
            throws PersistenceException;

    public abstract boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryViewObject>... givenList)
            throws PersistenceException;

    public abstract boolean isPathValid(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    public abstract boolean isPathValid(Project proejct, ERepositoryObjectType type, IPath path, String label)
            throws PersistenceException;

    /**
     * @param label
     * @param description
     * @param language
     * @param author
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#createProject(Project projectInfor)
     */
    public abstract Project createProject(Project projectInfor) throws PersistenceException;

    public abstract Project createProject(User authUser, String authPassword, Project projectInfor) throws PersistenceException;

    public abstract void saveProject(Project project) throws PersistenceException;

    /**
     * @param project
     * @param type
     * @param path
     * @param label
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#createFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public abstract Folder createFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    public abstract Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label)
            throws PersistenceException;

    public abstract Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label,
            boolean isImportItem) throws PersistenceException;

    /**
     * @param project
     * @param type
     * @param path
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#deleteFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath)
     */
    public abstract void deleteFolder(ERepositoryObjectType type, IPath path) throws PersistenceException;

    public abstract void deleteFolder(Project project, ERepositoryObjectType type, IPath path) throws PersistenceException;

    public abstract void deleteFolder(Project project, ERepositoryObjectType type, IPath path, boolean fromEmptyRecycleBin)
            throws PersistenceException;

    /**
     * @param project
     * @param type
     * @param sourcePath
     * @param targetPath
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#moveFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath,
     * org.eclipse.core.runtime.IPath)
     */
    public abstract void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException;

    /**
     * @param project
     * @return
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IBusinessProcessFactory#getBusinessProcess(org.talend.core.model.general.Project)
     */

    public abstract String getNextId();

    /**
     * yzhang Comment method "getRecycleBinItems".
     * 
     * @return
     * @throws PersistenceException
     */
    public abstract List<IRepositoryViewObject> getRecycleBinItems() throws PersistenceException;

    public abstract List<IRepositoryViewObject> getRecycleBinItems(Project project, boolean... options)
            throws PersistenceException;

    /**
     * @param server
     * @param username
     * @param password
     * @return
     * @throws PersistenceException
     * @throws BusinessException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#readProject(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    // TODO SML Rename in getProjects
    public abstract Project[] readProject() throws PersistenceException, BusinessException;

    // for export project ,no need to unload resource when read projects
    public abstract Project[] readProject(boolean unloadResource) throws PersistenceException, BusinessException;

    /**
     * @param project
     * @param type
     * @param path
     * @param label
     * @throws PersistenceException
     * @see org.talend.core.model.repository.factories.IRepositoryFactory#renameFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    public abstract void renameFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#deleteObject(org.talend .core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryViewObject)
     */
    public abstract void deleteObjectLogical(IRepositoryViewObject objToDelete) throws PersistenceException, BusinessException;

    public abstract void deleteObjectLogical(Project project, IRepositoryViewObject objToDelete) throws PersistenceException,
            BusinessException;

    public abstract void deleteObjectLogical(Project project, IRepositoryViewObject objToDelete, boolean needCheckAvailability)
            throws PersistenceException, BusinessException;

    public abstract void deleteObjectPhysical(IRepositoryViewObject objToDelete) throws PersistenceException;

    public abstract void deleteObjectPhysical(IRepositoryViewObject objToDelete, String version) throws PersistenceException;

    public abstract void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete) throws PersistenceException,
            BusinessException;

    public abstract void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, String version)
            throws PersistenceException;

    public abstract void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, String version,
            boolean fromEmptyRecycleBin) throws PersistenceException;

    public abstract void restoreObject(IRepositoryViewObject objToRestore, IPath path) throws PersistenceException,
            BusinessException;

    public abstract void restoreObject(Project project, IRepositoryViewObject objToRestore, IPath path)
            throws PersistenceException, BusinessException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#moveObject(org.talend. core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryViewObject)
     */
    public abstract void moveObject(IRepositoryViewObject objToMove, IPath path, IPath... sourcePath)
            throws PersistenceException, BusinessException;

    public abstract void moveObjectMulti(IRepositoryViewObject[] objToMove, IPath path, Map<IRepositoryViewObject, IPath> map)
            throws PersistenceException, BusinessException;

    public abstract void lock(IRepositoryViewObject obj) throws PersistenceException, BusinessException;

    /**
     * @param item
     * @throws PersistenceException
     * @throws BusinessException
     * @see org.talend.repository.model.IRepositoryFactory#lock(org.talend.core.model.properties.Item)
     */
    public abstract boolean lock(Item item) throws PersistenceException, LoginException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getAllVersion(org.talend .core.model.general.Project, int)
     */
    public abstract List<IRepositoryViewObject> getAllVersion(Project project, String id, boolean avoidSaveProject)
            throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAllVersion(Project project, String id, String folderPath,
            ERepositoryObjectType type) throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAllVersion(String id) throws PersistenceException;

    public abstract IRepositoryViewObject getLastVersion(Project project, String id, String relativeFolder,
            ERepositoryObjectType type) throws PersistenceException;

    public abstract IRepositoryViewObject getLastVersion(Project project, String id) throws PersistenceException;

    public abstract IRepositoryViewObject getLastVersion(String id) throws PersistenceException;

    public abstract IRepositoryViewObject getSpecificVersion(Project project, String id, String version, boolean avoidSaveProject)
            throws PersistenceException;

    public abstract IRepositoryViewObject getSpecificVersion(String id, String version, boolean avoidSaveProject)
            throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAll(ERepositoryObjectType type) throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAll(ERepositoryObjectType type, boolean withDeleted)
            throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAll(ERepositoryObjectType type, boolean withDeleted, boolean allVersions)
            throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type) throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted)
            throws PersistenceException;

    public abstract List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted,
            boolean allVersions) throws PersistenceException;

    public abstract List<String> getFolders(ERepositoryObjectType type) throws PersistenceException;

    /**
     * @return
     * @see org.talend.repository.model.IRepositoryFactory#getDocumentationStatus()
     */
    public abstract List<Status> getDocumentationStatus() throws PersistenceException;

    /**
     * @return
     * @see org.talend.repository.model.IRepositoryFactory#getTechnicalStatus()
     */
    public abstract List<Status> getTechnicalStatus() throws PersistenceException;

    /**
     * @return
     * @see org.talend.repository.model.IRepositoryFactory#getSpagoBiServer()
     */
    // TODO SML Remove
    // public abstract List<SpagoBiServer> getSpagoBiServer() throws
    // PersistenceException;
    /**
     * @param list
     * @see org.talend.repository.model.IRepositoryFactory#setDocumentationStatus(java.util.List)
     */
    public abstract void setDocumentationStatus(List<Status> list) throws PersistenceException;

    /**
     * @param list
     * @see org.talend.repository.model.IRepositoryFactory#setTechnicalStatus(java.util.List)
     */
    public abstract void setTechnicalStatus(List<Status> list) throws PersistenceException;

    /**
     * @param list
     * @see org.talend.repository.model.IRepositoryFactory#setSpagoBiServer(java.util.List)
     */
    public abstract void setSpagoBiServer(List<SpagoBiServer> list) throws PersistenceException;

    public abstract void setMigrationTasksDone(Project project, List<MigrationTask> list) throws PersistenceException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isServerValid()
     */
    // public abstract String isServerValid();
    public abstract void create(Item item, IPath path, boolean... isImportItem) throws PersistenceException;

    public abstract void create(Project project, Item item, IPath path, boolean... isImportItem) throws PersistenceException;

    public abstract void save(Item item, boolean... isMigrationTask) throws PersistenceException;

    public abstract void save(Project project, Item item, boolean... isMigrationTask) throws PersistenceException;

    public abstract void save(Property property, String... originalNameAndVersion) throws PersistenceException;

    public abstract void save(Project project, Property property, String... originalNameAndVersion) throws PersistenceException;

    public abstract Item copy(Item item, IPath path) throws PersistenceException, BusinessException;

    public abstract Item copy(Item item, IPath path, boolean changeLabelWithCopyPrefix) throws PersistenceException,
            BusinessException;

    public abstract Item copy(Item sourceItem, IPath targetPath, String newItemLabel) throws PersistenceException,
            BusinessException;

    public abstract void saveCopy(Item item, Item targetItem) throws PersistenceException;

    public abstract Property reload(Property property) throws PersistenceException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core .model.general.Project,
     * org.talend.core.model.repository.IRepositoryViewObject)
     */
    public abstract void unlock(IRepositoryViewObject obj) throws PersistenceException, LoginException;

    /**
     * @param obj
     * @throws PersistenceException
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.properties.Item)
     */
    public abstract void unlock(Item item) throws PersistenceException, LoginException;

    // public abstract boolean doesLoggedUserExist() throws
    // PersistenceException;

    // public abstract void createUser() throws PersistenceException;

    public abstract void initialize() throws PersistenceException;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatus(org.talend.core .model.properties.Item)
     */
    public abstract ERepositoryStatus getStatus(IRepositoryViewObject obj);

    public abstract ERepositoryStatus getStatus(Item item);

    public abstract ERepositoryStatus getStatus(InformationLevel level);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatusAndLockIfPossible
     * (org.talend.core.model.properties.Item)
     */
    public abstract boolean isEditableAndLockIfPossible(Item item);

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isEditable(org.talend.
     * core.model.repository.IRepositoryViewObject)
     */
    public abstract boolean isEditableAndLockIfPossible(IRepositoryViewObject obj);

    public org.talend.core.model.properties.Project getProject(Item item);

    /*
     * (non-Javadoc)
     * 
     * 
     * @seeorg.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org .talend.core.model.repository.
     * IRepositoryViewObject)
     */
    public abstract boolean isPotentiallyEditable(IRepositoryViewObject obj);

    @Deprecated
    public boolean isDeleted(MetadataTable table);

    public boolean isUserReadOnlyOnCurrentProject();

    public abstract List<org.talend.core.model.properties.Project> getReferencedProjects(Project project);

    public Boolean hasChildren(Object parent);

    public abstract RootContainer<String, IRepositoryViewObject> getMetadata(Project project, ERepositoryObjectType type,
            boolean... options) throws PersistenceException;

    public abstract RootContainer<String, IRepositoryViewObject> getMetadata(ERepositoryObjectType type)
            throws PersistenceException;

    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException;

    public void forceCreate(Item item, IPath path) throws PersistenceException;

    public void forceCreate(Project project, Item item, IPath path) throws PersistenceException;

    public void createParentFoldersRecursively(ERepositoryObjectType itemType, IPath path) throws PersistenceException;

    public void createParentFoldersRecursively(Project project, ERepositoryObjectType itemType, IPath path)
            throws PersistenceException;

    public void createParentFoldersRecursively(Project project, ERepositoryObjectType itemType, IPath path, boolean isImportItem)
            throws PersistenceException;

    public void forceDeleteObjectPhysical(IRepositoryViewObject objToDelete, String version, boolean isDeleteOnRemote) throws PersistenceException;

    public Property getUptodateProperty(Project project, Property property) throws PersistenceException;

    public Property getUptodateProperty(Property property) throws PersistenceException;

    public void executeRepositoryWorkUnit(RepositoryWorkUnit workUnit);

    /**
     * Catch only the next repositoryWorkUnit operation, once workUnit is finished, listener is removed.
     * 
     * @param listener
     */
    public void addRepositoryWorkUnitListener(IRepositoryWorkUnitListener listener);

    public List<IRepositoryViewObject> getAllVersion(String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException;

    public FolderItem getFolderItem(Project project, ERepositoryObjectType itemType, IPath path);

    public boolean enableSandboxProject() throws PersistenceException;

    public boolean isLocalConnectionProvider() throws PersistenceException;

    /**
     * 
     * DOC zshen Comment method "getMetadataByFolder".
     * 
     * @param project
     * @param itemType
     * @param path
     * @return all of object under path.
     */
    public List<IRepositoryViewObject> getMetadataByFolder(Project project, ERepositoryObjectType itemType, IPath path);

    public List<IRepositoryViewObject> getMetadataByFolder(ERepositoryObjectType itemType, IPath path);

    public LockInfo getLockInfo(Item item);

    /**
     * DOC ycbai Comment method "getNavigatorViewDescription".
     * 
     * @return
     */
    public String getNavigatorViewDescription();

    public void updateLockStatus() throws PersistenceException;

    public boolean isModified(Object property);

    /**
     * DOC nrousseau Comment method "getObjectFromFolder".
     * 
     * @param project
     * @param type
     * @param folderName
     * @param options (options possible: OPTION_ONLY_LAST_VERSION / OPTION_DYNAMIC_OBJECTS / OPTION_RECURSIVE_CALL /
     * OPTION_SKIP_DELETED)
     * @return
     * @throws PersistenceException
     */
    public RootContainer<String, IRepositoryViewObject> getObjectFromFolder(Project project, ERepositoryObjectType type,
            String folderName, int options) throws PersistenceException;

    public void unloadResources(Property property) throws PersistenceException;

    public Object getXmiResourceManager();

    public abstract HashMap<String, Boolean> getFolderItems(ERepositoryObjectType type) throws PersistenceException;

}
