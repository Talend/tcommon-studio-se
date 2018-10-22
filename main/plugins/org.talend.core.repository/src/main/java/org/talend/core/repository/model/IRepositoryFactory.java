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
package org.talend.core.repository.model;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.FolderItem;
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
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.repository.RepositoryWorkUnit;

/**
 * Defines all methods that a repository provider plug-in must provides to client.<br/>
 * 
 * $Id: IRepositoryFactory.java 46606 2010-08-11 08:33:54Z cli $
 * 
 */
public interface IRepositoryFactory {

    public static int OPTION_NOT_INCLUDE_CHILDRENS = 1;

    public static int OPTION_ONLY_LAST_VERSION = 1 << 1;

    public static int OPTION_DYNAMIC_OBJECTS = 1 << 2;

    public static int OPTION_SKIP_DELETED = 1 << 3;

    public static int OPTION_ALL_VERSION = 1 << 4;

    public String getId();

    public void setId(String id);

    public String getName();

    public void setName(String name);

    public List<DynamicFieldBean> getFields();

    public void setFields(List<DynamicFieldBean> fields);

    public List<DynamicButtonBean> getButtons();

    public List<DynamicChoiceBean> getChoices();

    public boolean isAuthenticationNeeded();

    public void setAuthenticationNeeded(boolean aBnthenticationNeeded);

    public void initialize() throws PersistenceException;

    public void logOnProject(Project project) throws LoginException, PersistenceException;

    public String getNextId();

    public Project createProject(Project projectInfor) throws PersistenceException;

    public Project createProject(User authUser, String authPassword, Project projectInfor) throws PersistenceException;

    public void saveProject(Project project) throws PersistenceException;

    // public boolean doesLoggedUserExist() throws PersistenceException;

    // public void createUser() throws PersistenceException;

    public Project[] readProject() throws PersistenceException, BusinessException;

    public Project[] readProject(boolean local, boolean reuseExisting, IProject... eclipseProjects) throws PersistenceException;

    // for export project ,no need to unload resource when read projects
    public Project[] readProject(boolean unloadResource) throws PersistenceException, BusinessException;

    public Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    public Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label, boolean isImportItem)
            throws PersistenceException;

    /**
     * Returns if the name is used by another item of the same type. Type, name and id of item are used to test label
     * availability.
     * 
     * Implementations should be updated when folder are treated as Items.
     * 
     * This function will take some time as it will retrieve all items from the repository. It is possible also to give
     * in parameter the list of items, to avoid to have this function too slow.
     * 
     * @param item
     * @param name TODO
     * @return <code>true</code> if the name is not used an so is available.
     * @throws PersistenceException
     */
    public boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryViewObject>... givenList)
            throws PersistenceException;

    public boolean isPathValid(Project project, ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    public void deleteFolder(Project project, ERepositoryObjectType type, IPath path) throws PersistenceException;

    public void deleteFolder(Project project, ERepositoryObjectType type, IPath path, boolean fromEmptyRecycleBin)
            throws PersistenceException;

    public void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException;

    public void renameFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException;

    /**
     * Returns all version of an object given its id.
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return a list (may be empty) of all version
     * @throws PersistenceException
     */
    public List<IRepositoryViewObject> getAllVersion(Project project, String id, boolean avoidSaveProject)
            throws PersistenceException;

    public List<IRepositoryViewObject> getAllVersion(Project project, String id, String relativeFolder, ERepositoryObjectType type)
            throws PersistenceException;

    /**
     * Returns last version of an object given its id.
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return the most recent version of object with this id or <code>null</code> if no object with this id has been
     * found
     * @throws PersistenceException
     */
    public IRepositoryViewObject getLastVersion(Project project, String id) throws PersistenceException;

    /**
     * Returns last version of an object given its id. If folder and repository type is given it can be faster (mostly
     * usefull for imports)
     * 
     * @param project - the current project
     * @param id - the id to look for
     * @return the most recent version of object with this id or <code>null</code> if no object with this id has been
     * found
     * @throws PersistenceException
     */
    public IRepositoryViewObject getLastVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException;

    /**
     * Returns all object of a given type.<br/>
     * 
     * @param project - the current project
     * @param type - the type
     * @param withDeleted - with deleted items
     * @param allVersions - all versions for one item
     * @return a list of all objects of type <code>type</code> in the repository in the project
     * @throws PersistenceException
     */
    public List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted,
            boolean allVersions) throws PersistenceException;

    /**
     * Deletes logically the given object. <code>isDeleted</code> on this object will now returned <code>true</code>.
     * 
     * @param project - the current project
     * @param objToDelete - the objet to delete
     * @param deletionAuthor - the user perfom the deletion (only for logging in this version)
     * @throws PersistenceException
     */
    public void deleteObjectLogical(Project project, IRepositoryViewObject objToDelete) throws PersistenceException;

    /**
     * Deletes physically the given object. Object cannot be retrieved.
     * 
     * @param project - the current project
     * @param objToDelete - the objet to delete
     * @param deletionAuthor - the user perfom the deletion (only for logging in this version)
     * @throws PersistenceException
     */
    public void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, boolean isDeleteOnRemote)
            throws PersistenceException;

    public void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, String version, boolean isDeleteOnRemote)
            throws PersistenceException;

    public void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, String version,
            boolean fromEmptyRecycleBin, boolean isDeleteOnRemote) throws PersistenceException;

    /**
     * Restore a logically deleted object. <code>isDeleted</code> on this object will now returned <code>false</code>.
     * 
     * @param project - the current project
     * @param objToRestore - the object to restore
     * @param path - the path to restore the object. Cannot be null. Path is relative to root type folder.
     * @param restorationAuthor - the user perfom the restoration (only for logging in this version)
     * @throws PersistenceException
     */
    public void restoreObject(IRepositoryViewObject objToRestore, IPath path) throws PersistenceException;

    public void restoreObject(Project project, IRepositoryViewObject objToRestore, IPath path) throws PersistenceException;

    public void moveObject(IRepositoryViewObject objToMove, IPath newPath) throws PersistenceException;

    public void moveObjectMulti(IRepositoryViewObject[] objToMove, IPath newPath) throws PersistenceException;

    public boolean lock(Item item) throws PersistenceException, LoginException;

    public boolean unlock(Item item) throws PersistenceException, LoginException;

    public ERepositoryStatus getStatus(Item item);

    List<Status> getTechnicalStatus() throws PersistenceException;

    List<Status> getDocumentationStatus() throws PersistenceException;

    // TODO SML Remove this method
    List<SpagoBiServer> getSpagoBiServer() throws PersistenceException;

    void setTechnicalStatus(List<Status> list) throws PersistenceException;

    void setDocumentationStatus(List<Status> list) throws PersistenceException;

    void setSpagoBiServer(List<SpagoBiServer> list) throws PersistenceException;

    void setMigrationTasksDone(Project project, List<MigrationTask> list) throws PersistenceException;

    public String isServerValid() throws BusinessException;

    public void create(Project project, Item item, IPath path, boolean... isImportItem) throws PersistenceException;

    public void save(Project project, Item item) throws PersistenceException;

    public void save(Project project, Property property) throws PersistenceException;

    public Item copy(Item item, IPath path) throws PersistenceException, BusinessException;

    public Item copy(Item item, IPath path, boolean changeLabelWithCopyPrefix) throws PersistenceException, BusinessException;

    public Item copy(Item sourceItem, IPath targetPath, String newItemLabel) throws PersistenceException, BusinessException;

    /**
     * DOC mhelleboid Comment method "cancel".
     * 
     * @param property
     * @throws PersistenceException
     */
    public Property reload(Property property) throws PersistenceException;

    /**
     * Unload property and reload it with file
     */
    public Property reload(Property property, IFile file) throws PersistenceException;

    public List<IRepositoryViewObject> getRecycleBinItems(Project project, boolean... options) throws PersistenceException;

    /**
     * gather all the metadata connections (file / db / etc ...).
     */
    List<ConnectionItem> getMetadataConnectionsItem(Project project) throws PersistenceException;

    /**
     * get all context items.
     * 
     * @return
     * @throws PersistenceException
     */
    List<ContextItem> getContextItem(Project project) throws PersistenceException;

    public List<org.talend.core.model.properties.Project> getReferencedProjects(Project project);

    public Boolean hasChildren(Object parent);

    public void setDisplayToUser(boolean bool);

    public boolean isDisplayToUser();

    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException;

    public RootContainer<String, IRepositoryViewObject> getRoutineFromProject(Project project) throws PersistenceException;

    public void updateItemsPath(ERepositoryObjectType type, IPath targetPath) throws PersistenceException;

    public boolean setAuthorByLogin(Item item, String login) throws PersistenceException;

    public Property getUptodateProperty(Project project, Property property) throws PersistenceException;
    
    public void initProjectRepository(Project project, String branchForMainProject) throws PersistenceException;

    public void beforeLogon(Project project) throws PersistenceException, LoginException;

    public boolean isUserReadOnlyOnCurrentProject();

    public void checkAvailability() throws PersistenceException;

    @SuppressWarnings("unchecked")
    public void executeRepositoryWorkUnit(RepositoryWorkUnit workUnit);

    /**
     * Catch only the next repositoryWorkUnit operation, once workUnit is finished, listener is removed.
     * 
     * @param listener
     */
    public void addRepositoryWorkUnitListener(IRepositoryWorkUnitListener listener);

    public void logOffProject();

    public void unloadResources();

    public void unloadResources(Property property);

    /**
     * 
     * DOC mzhao feature 9207 unload and remove the specification resource from the resource set.
     * 
     * @param uriString the uri sting of resource.
     */
    public void unloadResources(String uriString);

    public FolderItem getFolderItem(Project project, ERepositoryObjectType itemType, IPath path);

    public void reloadProject(Project project) throws PersistenceException;

    public boolean isLocalConnectionProvider() throws PersistenceException;

    public boolean enableSandboxProject() throws PersistenceException;

    public RootContainer<String, IRepositoryViewObject> getMetadata(Project project, ERepositoryObjectType type,
            boolean... options) throws PersistenceException;

    /**
     * 
     * DOC zshen Comment method "getMetadataByFolder".
     * 
     * @param itemType
     * @param path
     * @return all of object under path.
     */
    public List<IRepositoryViewObject> getMetadataByFolder(Project project, ERepositoryObjectType itemType, IPath path);

    public XmiResourceManager getResourceManager();

    /**
     * DOC xqliu Comment method "getTdqRepositoryViewObjects".
     * 
     * @param project
     * @param type
     * @param folderName
     * @param options
     * @return
     * @throws PersistenceException
     */
    public RootContainer<String, IRepositoryViewObject> getTdqRepositoryViewObjects(Project project, ERepositoryObjectType type,
            String folderName, boolean[] options) throws PersistenceException;

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

    public boolean canLock(Item item) throws PersistenceException;

    public boolean canUnlock(Item item) throws PersistenceException;

    public void executeMigrations(Project mainProject, boolean beforeLogon, SubMonitor monitorWrap) throws PersistenceException;

    public RootContainer<String, IRepositoryViewObject> getRootContainerFromType(Project project, ERepositoryObjectType type);

    /**
     * Item will be considered as locked, won't check if item is locked here.
     * 
     * @param item
     * 
     * @return
     */
    public LockInfo getLockInfo(Item item);

    /**
     * DOC ycbai Comment method "getNavigatorViewDescription".
     * 
     * @return
     */
    public String getNavigatorViewDescription();

    /**
     * 
     * DOC ggu Comment method "updateLockStatus".
     * 
     * update the lock status
     */
    public void updateLockStatus() throws PersistenceException;

    public boolean isModified(Object property);

    public void afterLogon(IProgressMonitor monitor);

    public void setStorage(String storage);

    public String getStorage();

    public List<ILockBean> getAllRemoteLocks();

    public void loadProjectAndSetContext(IProject eclipseProject) throws PersistenceException;
    
    public byte[] getReferenceSettingContent(Project project, String branch) throws PersistenceException;

    public boolean isRepositoryBusy();

    public RepositoryWorkUnit getWorkUnitInProgress();
}
