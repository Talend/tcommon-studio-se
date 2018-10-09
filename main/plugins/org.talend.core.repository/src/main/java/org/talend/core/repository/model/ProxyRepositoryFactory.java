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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.exception.SystemException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.runtime.service.ITaCoKitService;
import org.talend.commons.ui.runtime.CommonUIPlugin;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.AbstractDQModelService;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.IESBService;
import org.talend.core.ITDQRepositoryService;
import org.talend.core.PluginChecker;
import org.talend.core.context.CommandLineContext;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.exception.TalendInternalPersistenceException;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.MetadataTalendType;
import org.talend.core.model.metadata.builder.connection.AbstractMetadataObject;
import org.talend.core.model.metadata.builder.connection.MetadataTable;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobDocumentationItem;
import org.talend.core.model.properties.JobletDocumentationItem;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.SpagoBiServer;
import org.talend.core.model.properties.Status;
import org.talend.core.model.properties.User;
import org.talend.core.model.properties.impl.FolderItemImpl;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryContentHandler;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.IRepositoryWorkUnitListener;
import org.talend.core.model.repository.ISubRepositoryObject;
import org.talend.core.model.repository.LockInfo;
import org.talend.core.model.repository.RepositoryContentManager;
import org.talend.core.model.repository.RepositoryObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.repository.constants.Constant;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.recyclebin.RecycleBinManager;
import org.talend.core.repository.utils.RepositoryPathProvider;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.repository.item.ItemProductKeys;
import org.talend.core.runtime.services.IMavenUIService;
import org.talend.core.runtime.util.ItemDateParser;
import org.talend.core.runtime.util.JavaHomeUtil;
import org.talend.core.service.ICoreUIService;
import org.talend.cwm.helper.SubItemHelper;
import org.talend.cwm.helper.TableHelper;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.ReferenceProjectProblemManager;
import org.talend.repository.ReferenceProjectProvider;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.documentation.ERepositoryActionName;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;
import org.talend.utils.io.FilesUtils;

import orgomg.cwm.objectmodel.core.ModelElement;

/**
 * Repository factory use by client. Based on implementation provide by extension point system. This class contains all
 * commons treatments done by repository whatever implementation.<br/>
 * 
 * $Id: ProxyRepositoryFactory.java 46606 2010-08-11 08:33:54Z cli $
 * 
 */
/**
 * DOC Administrator class global comment. Detailled comment
 */
public final class ProxyRepositoryFactory implements IProxyRepositoryFactory {

    private static final int MAX_TASKS = 8;

    private static Logger log = Logger.getLogger(ProxyRepositoryFactory.class);

    private IRepositoryFactory repositoryFactoryFromProvider;

    private static ProxyRepositoryFactory singleton = null;

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private boolean fullLogonFinished;

    private final ProjectManager projectManager;

    private Map<String, org.talend.core.model.properties.Project> emfProjectContentMap = new HashMap<String, org.talend.core.model.properties.Project>();
    
    private boolean isCancelled;

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (l == null) {
            throw new IllegalArgumentException();
        }
        support.addPropertyChangeListener(l);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if (l != null) {
            support.removePropertyChangeListener(l);
        }
    }

    public void fireRepositoryPropertyChange(String property, Object oldValue, Object newValue) {
        if (support.hasListeners(property)) {
            support.firePropertyChange(property, oldValue, newValue);
        }
    }

    /**
     * DOC smallet ProxyRepositoryFactory constructor comment.
     */
    private ProxyRepositoryFactory() {
        projectManager = ProjectManager.getInstance();
    }

    public static synchronized ProxyRepositoryFactory getInstance() {
        if (singleton == null) {
            singleton = new ProxyRepositoryFactory();
        }
        return singleton;
    }

    private ICoreService getCoreService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreService.class)) {
            return (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);
        }
        return null;
    }
    
    private IRunProcessService getRunProcessService() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            return (IRunProcessService) GlobalServiceRegister.getDefault().getService(IRunProcessService.class);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#refreshJobPictureFolder()
     */
    @Override
    public void refreshJobPictureFolder(String picFolder) {
        IFolder folder = RepositoryPathProvider.getFolder(picFolder);
        try {
            folder.refreshLocal(1, null);
        } catch (Exception e) {
            // e.printStackTrace();
            ExceptionHandler.process(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#refreshJobPictureFolder()
     */
    @Override
    public void refreshDocumentationFolder(String docFolder) {
        IFolder folder = RepositoryPathProvider.getFolder(docFolder);
        if (folder != null) {
            try {
                folder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            } catch (Exception e) {
                // e.printStackTrace();
                ExceptionHandler.process(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRepositoryContext()
     */
    @Override
    public RepositoryContext getRepositoryContext() {
        Context ctx = CoreRuntimePlugin.getInstance().getContext();
        return (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRepositoryFactoryFromProvider()
     */
    public IRepositoryFactory getRepositoryFactoryFromProvider() {
        return this.repositoryFactoryFromProvider;
    }

    @Override
    public Object getXmiResourceManager() {
        return getRepositoryFactoryFromProvider().getResourceManager();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#setRepositoryFactoryFromProvider(org.talend.repository.model
     * .IRepositoryFactory)
     */
    public void setRepositoryFactoryFromProvider(IRepositoryFactory repositoryFactoryFromProvider) {
        this.repositoryFactoryFromProvider = repositoryFactoryFromProvider;
    }

    private void checkFileName(String fileName, String pattern) {
        if (!Pattern.matches(pattern, fileName)) {
            // i18n
            // throw new IllegalArgumentException("Label " + fileName + " does not match pattern " + pattern);
            throw new IllegalArgumentException(Messages.getString(
                    "ProxyRepositoryFactory.illegalArgumentException.labelNotMatchPattern", new String[] { fileName, pattern })); //$NON-NLS-1$
        }
    }

    /* hywang for 17295,need to migration refProjects when login using svn repository */
    private void executeMigrations(Project mainProject, boolean beforeLogon, SubMonitor monitorWrap) throws PersistenceException {
        this.repositoryFactoryFromProvider.executeMigrations(mainProject, beforeLogon, monitorWrap);
    }

    /**
     * DOC ycbai Comment method "checkProjectCompatibility".
     * 
     * @param project
     * @throws LoginException
     */
    private void checkProjectCompatibility(Project project) throws LoginException {
        IMigrationToolService migrationToolService = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                IMigrationToolService.class);
        // update migration system.
        migrationToolService.updateMigrationSystem(project.getEmfProject(), false);
        boolean isProjectCompatibility = migrationToolService.checkMigrationTasks(project.getEmfProject());
        if (!isProjectCompatibility) {
            throw new LoginException(Messages.getString(
                    "ProxyRepositoryFactory.projectCanNotOpen", migrationToolService.getTaskId())); //$NON-NLS-1$
        }
    }

    private boolean checkFileNameAndPath(Project project, Item item, String pattern, IPath path, boolean folder,
            boolean... isImportItem) throws PersistenceException {
        String fileName = item.getProperty().getLabel();
        checkFileName(fileName, pattern);
        // if the check comes from create item when import item, then no need to check the name availability
        // since we already checked before.
        if (isImportItem.length == 0) {
            checkIfHaveDuplicateName(project, item, path);
        }
        return false;
    }

    private boolean checkIfHaveDuplicateName(Project project, Item item, IPath path) throws PersistenceException {
        String name = item.getProperty().getLabel();

        ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);

        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            // no check needed for table
            return false;
        }

        List<IRepositoryViewObject> list;

        list = getAll(project, type, true, false);

        IRepositoryViewObject duplicateNameObject = null;

        for (IRepositoryViewObject current : list) {
            if (name.equalsIgnoreCase(current.getProperty().getLabel())
                    && item.getProperty().getId() != current.getProperty().getId()) {
                // To check SQLPattern in same path. see bug 0005038: unable to add a SQLPattern into repository.
                if (!type.isAllowMultiName()
                        || current.getProperty().getItem().getState().getPath().equals(path.toPortableString())) {
                    // TDI-18224
                    if (current.getProperty() != null && item.getProperty() != null
                            && !current.getProperty().getVersion().equals(item.getProperty().getVersion())) {
                        continue;
                    }
                    duplicateNameObject = current;
                    break;
                }
            }
        }
        if (duplicateNameObject != null) {
            if (CommonsPlugin.isHeadless()) {
                throw new IllegalArgumentException(Messages.getString(
                        "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { name })); //$NON-NLS-1$
            } else {

                Display display = Display.getCurrent();
                if (display == null) {
                    display = Display.getDefault();
                }

                ITDQRepositoryService tdqRepService = null;

                if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                    tdqRepService = (ITDQRepositoryService) GlobalServiceRegister.getDefault().getService(
                            ITDQRepositoryService.class);
                }

                boolean isThrow = true;
                if (tdqRepService != null && CoreRuntimePlugin.getInstance().isDataProfilePerspectiveSelected()) {
                    // change MessageBox to DeleteModelElementConfirmDialog
                    InputDialog inputDialog = tdqRepService.getInputDialog(item);
                    if (MessageDialog.OK == inputDialog.open()) {
                        String newName = inputDialog.getValue();
                        tdqRepService.changeElementName(item, newName);
                        isThrow = false;
                    }
                } else {
                    final Display tmpDisplay = display;
                    final boolean[] ok = new boolean[1];
                    tmpDisplay.syncExec(new Runnable() {

                        @Override
                        public void run() {
                            Shell currentShell = tmpDisplay.getActiveShell();
                            if (currentShell == null) {
                                currentShell = new Shell();
                            }
                            MessageBox box = new MessageBox(currentShell, SWT.ICON_WARNING | SWT.OK | SWT.CANCEL);
                            box.setText(Messages.getString("ProxyRepositoryFactory.JobNameErroe")); //$NON-NLS-1$
                            box.setMessage(Messages.getString("ProxyRepositoryFactory.Label") + " " + name + " " + Messages.getString("ProxyRepositoryFactory.ReplaceJob")); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$

                            if (box.open() == SWT.OK) {

                                ok[0] = true;
                            }
                        }
                    });

                    if (ok[0]) {
                        deleteObjectPhysical(duplicateNameObject);
                        isThrow = false;
                    }

                }
                if (isThrow) {
                    throw new TalendInternalPersistenceException(Messages.getString(
                            "ProxyRepositoryFactory.illegalArgumentException.labeAlreadyInUse", new String[] { name })); //$NON-NLS-1$
                }
                return true;
            }
        }
        return true;
    }

    private void checkFileNameAndPath(Project proejct, String label, String pattern, ERepositoryObjectType type, IPath path,
            boolean folder) throws PersistenceException {
        String fileName = label;
        checkFileName(fileName, pattern);
    }

    public List<ConnectionItem> getMetadataConnectionsItem(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadataConnectionsItem(project);
    }

    @Override
    public List<ConnectionItem> getMetadataConnectionsItem() throws PersistenceException {
        return getMetadataConnectionsItem(projectManager.getCurrentProject());
    }

    public List<ContextItem> getContextItem(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getContextItem(project);
    }

    @Override
    public List<ContextItem> getContextItem() throws PersistenceException {
        List<ContextItem> contextItems = getContextItem(projectManager.getCurrentProject());
        if (contextItems == null) {
            contextItems = new ArrayList<ContextItem>();
        }
        for (Project p : projectManager.getAllReferencedProjects()) {
            List<ContextItem> rContextItems = getContextItem(p);
            if (rContextItems != null) {
                contextItems.addAll(rContextItems);
            }
        }
        return contextItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isNameAvailable(org.talend.core.model.properties.Item,
     * java.lang.String)
     */
    @Override
    public boolean isNameAvailable(Item item, String name, List<IRepositoryViewObject>... givenList) throws PersistenceException {
        return isNameAvailable(projectManager.getCurrentProject(), item, name, givenList);
    }

    @Override
    public boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryViewObject>... givenList)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.isNameAvailable(project, item, name, givenList);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#isPathValid(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    @Override
    public boolean isPathValid(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return isPathValid(projectManager.getCurrentProject(), type, path, label);
    }

    @Override
    public boolean isPathValid(Project proejct, ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return this.repositoryFactoryFromProvider.isPathValid(proejct, type, path, label);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#createProject(java.lang.String, java.lang.String,
     * org.talend.core.model.temp.ECodeLanguage, org.talend.core.model.properties.User)
     */
    @Override
    public Project createProject(Project projectInfor) throws PersistenceException {
        RepositoryContext repCtx = getRepositoryContext();
        User user = repCtx.getUser();
        String password = repCtx.getClearPassword();
        return createProject(user, password, projectInfor);
    }

    @Override
    public Project createProject(User authUser, String authPassword, Project projectInfor) throws PersistenceException {
        checkFileName(projectInfor.getLabel(), RepositoryConstants.PROJECT_PATTERN);
        Project toReturn = this.repositoryFactoryFromProvider.createProject(authUser, authPassword, projectInfor);
        if (toReturn.isLocal()) {
            IMigrationToolService service = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            service.initNewProjectTasks(toReturn);
        }
        return toReturn;
    }

    @Override
    public void saveProject(Project project) throws PersistenceException {
        repositoryFactoryFromProvider.saveProject(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#createFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    @Override
    public Folder createFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return createFolder(projectManager.getCurrentProject(), type, path, label);
    }

    @Override
    public Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        return createFolder(project, type, path, label, false);
    }

    @Override
    public Folder createFolder(Project project, ERepositoryObjectType type, IPath path, String label, boolean isImportItem)
            throws PersistenceException {
        if (type.isDQItemType()) {
            checkFileNameAndPath(project, label, RepositoryConstants.TDQ_ALL_ITEM_PATTERN, type, path, true);
        } else if (type == ERepositoryObjectType.METADATA_FILE_XML) {
            checkFileNameAndPath(project, label, RepositoryConstants.SIMPLE_FOLDER_PATTERN, type, path, true);
        } else if (type.isAllowPlainFolder()) {
            if (org.eclipse.core.runtime.Status.OK_STATUS != ResourcesPlugin.getWorkspace().validateName(label, IResource.FOLDER)) {
                throw new IllegalArgumentException(Messages.getString(
                        "ProxyRepositoryFactory.illegalArgumentException.labelNotMatchPattern", label)); //$NON-NLS-1$
            }
        } else {
            checkFileNameAndPath(project, label, RepositoryConstants.FOLDER_PATTERN, type, path, true);
        }
        Folder createFolder = this.repositoryFactoryFromProvider.createFolder(project, type, path, label);
        fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_CREATE.getName(), path, new Object[] { createFolder, type });
        return createFolder;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath)
     */
    @Override
    public synchronized void deleteFolder(ERepositoryObjectType type, IPath path) throws PersistenceException {
        deleteFolder(projectManager.getCurrentProject(), type, path);
    }

    @Override
    public synchronized void deleteFolder(Project project, ERepositoryObjectType type, IPath path) throws PersistenceException {
        deleteFolder(project, type, path, false);
    }

    @Override
    public synchronized void deleteFolder(Project project, ERepositoryObjectType type, IPath path, boolean fromEmptyRecycleBin)
            throws PersistenceException {
        if (hasLockedItems(type, path)) {
            throw new PersistenceException(Messages.getString("ProxyRepositoryFactory.DeleteFolderContainsLockedItem")); //$NON-NLS-1$
        }
        this.repositoryFactoryFromProvider.deleteFolder(project, type, path, fromEmptyRecycleBin);
        if (type == ERepositoryObjectType.PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_DELETE.getName(), path, type);
        }
        if (ERepositoryObjectType.getAllTypesOfJoblet().contains(type)) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOBLET_FOLDER_DELETE.getName(), path, type);
        }
    }

    private boolean hasLockedItems(ERepositoryObjectType type, IPath path) throws PersistenceException {
        if (ERepositoryObjectType.COMPONENTS.equals(type)) {
            return false;
        }
        Project baseProject = getRepositoryContext().getProject();
        List<IRepositoryViewObject> objects = this.repositoryFactoryFromProvider.getMetadataByFolder(baseProject, type, path);
        for (IRepositoryViewObject repositoryObject : objects) {
            if (getStatus(repositoryObject.getProperty().getItem()) == ERepositoryStatus.LOCK_BY_USER
                    || getStatus(repositoryObject.getProperty().getItem()) == ERepositoryStatus.LOCK_BY_OTHER) {
                return true;
            }
        }
        // TUP-202 sizhaoliu 2012-10-3 recursively check lock status of items in sub folders
        FolderItem folderItem = repositoryFactoryFromProvider.getFolderItem(baseProject, type, path);
        if (folderItem != null) {
            for (Object child : folderItem.getChildren()) {
                if (child instanceof FolderItem) {
                    if (hasLockedItems(type, path.append(((FolderItem) child).getProperty().getLabel()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#moveFolder(org.talend.core.model.repository.ERepositoryObjectType
     * , org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IPath)
     */
    @Override
    public void moveFolder(ERepositoryObjectType type, IPath sourcePath, IPath targetPath) throws PersistenceException {
        if (hasLockedItems(type, sourcePath)) {
            throw new PersistenceException(Messages.getString("ProxyRepositoryFactory.MoveFolderContainsLockedItem")); //$NON-NLS-1$
        }
        this.repositoryFactoryFromProvider.moveFolder(type, sourcePath, targetPath);
        if (type == ERepositoryObjectType.PROCESS) {
            fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_MOVE.getName(), new IPath[] { sourcePath, targetPath },
                    type);
        }
        if (ERepositoryObjectType.getAllTypesOfJoblet().contains(type)) {
            fireRepositoryPropertyChange(ERepositoryActionName.JOBLET_FOLDER_MOVE.getName(),
                    new IPath[] { sourcePath, targetPath }, type);
        }
        this.repositoryFactoryFromProvider.updateItemsPath(type, targetPath.append(sourcePath.lastSegment()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getNextId()
     */
    @Override
    public String getNextId() {
        String nextId = this.repositoryFactoryFromProvider.getNextId();

        // i18n
        // log.trace("New ID generated on project [" + projectManager.getCurrentProject() + "] = " + nextId);
        String str[] = new String[] { projectManager.getCurrentProject() + "", nextId + "" };//$NON-NLS-1$ //$NON-NLS-2$
        log.trace(Messages.getString("ProxyRepositoryFactory.log.newIdGenerated", str)); //$NON-NLS-1$
        return nextId;
    }

    public RootContainer<String, IRepositoryViewObject> getRoutineFromProject(Project project) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRoutineFromProject(project);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getRecycleBinItems()
     */
    @Override
    public List<IRepositoryViewObject> getRecycleBinItems() throws PersistenceException {
        return getRecycleBinItems(projectManager.getCurrentProject());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#readProject()
     */
    @Override
    public Project[] readProject() throws PersistenceException, BusinessException {
        // mzhao initialize the DQ model packages.
        AbstractDQModelService dqModelService = CoreRuntimePlugin.getInstance().getDQModelService();
        if (dqModelService != null) {
            dqModelService.initTDQEMFResource();
        }

        return this.repositoryFactoryFromProvider.readProject();
    }

    @Override
    public Project[] readProject(boolean unloadResource) throws PersistenceException, BusinessException {
        return this.repositoryFactoryFromProvider.readProject(unloadResource);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#renameFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath, java.lang.String)
     */
    @Override
    public void renameFolder(ERepositoryObjectType type, IPath path, String label) throws PersistenceException {
        if (hasLockedItems(type, path)) {
            throw new PersistenceException(Messages.getString("ProxyRepositoryFactory.RenameFolderContainsLockedItem")); //$NON-NLS-1$
        }
        this.repositoryFactoryFromProvider.renameFolder(type, path, label);

        fireRepositoryPropertyChange(ERepositoryActionName.FOLDER_RENAME.getName(), path, new Object[] { label, type });

        this.repositoryFactoryFromProvider.updateItemsPath(type, path.removeLastSegments(1).append(label));
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteObjectLogical(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    @Override
    public void deleteObjectLogical(IRepositoryViewObject objToDelete) throws PersistenceException, BusinessException {
        deleteObjectLogical(projectManager.getCurrentProject(), objToDelete);
    }

    @Override
    public void deleteObjectLogical(Project project, IRepositoryViewObject objToDelete) throws PersistenceException,
            BusinessException {
        deleteObjectLogical(project, objToDelete, true);
    }

    @Override
    public void deleteObjectLogical(Project project, IRepositoryViewObject objToDelete, boolean needCheckAvailability)
            throws PersistenceException, BusinessException {
        // RepositoryViewObject is dynamic, so force to use in all case the RepositoryObject with fixed object.
        IRepositoryViewObject object = new RepositoryObject(objToDelete.getProperty());
        if (needCheckAvailability) {
            checkAvailability(object);
        }
        this.repositoryFactoryFromProvider.deleteObjectLogical(project, object);
        // unlock(objToDelete);
        // i18n
        // log.debug("Logical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { object + "", getRepositoryContext().getUser() + "" };//$NON-NLS-1$ //$NON-NLS-2$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.logicalDeletion", str)); //$NON-NLS-1$

        fireRepositoryPropertyChange(ERepositoryActionName.DELETE_TO_RECYCLE_BIN.getName(), null, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteObjectPhysical(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    @Override
    public void forceDeleteObjectPhysical(IRepositoryViewObject objToDelete, String version, boolean isDeleteOnRemote)
            throws PersistenceException {
        this.repositoryFactoryFromProvider.deleteObjectPhysical(projectManager.getCurrentProject(), objToDelete, version,
                isDeleteOnRemote);
        // i18n
        // log.info("Physical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { objToDelete.toString(), getRepositoryContext().getUser().toString() };
        log.info(Messages.getString("ProxyRepositoryFactory.log.physicalDeletion", str)); //$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#deleteObjectPhysical(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    @Override
    public void deleteObjectPhysical(IRepositoryViewObject objToDelete) throws PersistenceException {
        deleteObjectPhysical(objToDelete, null);
    }

    @Override
    public void deleteObjectPhysical(IRepositoryViewObject objToDelete, String version) throws PersistenceException {
        deleteObjectPhysical(projectManager.getCurrentProject(), objToDelete, version);
    }

    @Override
    public void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete) throws PersistenceException {
        deleteObjectPhysical(project, objToDelete, null);
    }

    @Override
    public void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, String version)
            throws PersistenceException {
        deleteObjectPhysical(project, objToDelete, version, false);
    }

    @Override
    public void deleteObjectPhysical(Project project, IRepositoryViewObject objToDelete, String version,
            boolean fromEmptyRecycleBin) throws PersistenceException {
        if (project == null || objToDelete == null || objToDelete.getProperty() == null) {
            return;
        }
        // RepositoryViewObject is dynamic, so force to use in all case the RepositoryObject with fixed object.
        IRepositoryViewObject object = new RepositoryObject(objToDelete.getProperty());
        boolean isExtendPoint = false;

        if (isFullLogonFinished()) {
            fireRepositoryPropertyChange(ERepositoryActionName.DELETE_FOREVER.getName(), null, object);
        }
        ERepositoryObjectType repositoryObjectType = object.getRepositoryObjectType();

        ICoreService coreService = getCoreService();
        if (coreService != null) {
            for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
                isExtendPoint = handler.isRepObjType(repositoryObjectType);
                if (isExtendPoint == true) {
                    if (repositoryObjectType == handler.getProcessType()) {
                        coreService.removeJobLaunch(object);
                    }
                    if (repositoryObjectType == handler.getCodeType()) {
                        try {
                            coreService.deleteBeanfile(object);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                    break;
                }
            }
            if (repositoryObjectType == ERepositoryObjectType.PROCESS) {
                // delete the job launch, for bug 8878
                coreService.removeJobLaunch(object);
            }
        }

        if (repositoryObjectType == ERepositoryObjectType.ROUTINES || repositoryObjectType == ERepositoryObjectType.PIG_UDF) {
            try {
                coreService.deleteRoutinefile(object);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        if (repositoryObjectType == ERepositoryObjectType.PROCESS && isFullLogonFinished()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IESBService.class)) {
                IESBService service = (IESBService) GlobalServiceRegister.getDefault().getService(IESBService.class);
                if (service != null) {
                    service.refreshOperationLabel(object.getProperty().getId());
                }
            }
        }

        this.repositoryFactoryFromProvider.deleteObjectPhysical(project, object, version, fromEmptyRecycleBin);
        // i18n
        // log.info("Physical deletion [" + objToDelete + "] by " + getRepositoryContext().getUser() + ".");
        String str[] = new String[] { object.toString(), getRepositoryContext().getUser().toString() };
        log.info(Messages.getString("ProxyRepositoryFactory.log.physicalDeletion", str)); //$NON-NLS-1$    }
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#restoreObject(org.talend.core.model.repository.
     * IRepositoryViewObject , org.eclipse.core.runtime.IPath)
     */
    @Override
    public void restoreObject(IRepositoryViewObject objToRestore, IPath path) throws PersistenceException, BusinessException {
        if (ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject()) {
            throw new BusinessException(Messages.getString("ProxyRepositoryFactory.bussinessException.itemNonModifiable")); //$NON-NLS-1$
        }

        restoreObject(ProjectManager.getInstance().getCurrentProject(), objToRestore, path);
    }

    @Override
    public void restoreObject(Project project, IRepositoryViewObject objToRestore, IPath path) throws PersistenceException,
            BusinessException {
        this.repositoryFactoryFromProvider.restoreObject(project, objToRestore, path);
        unlock(objToRestore);
        // i18n
        // log.debug("Restoration [" + objToRestore + "] by " + getRepositoryContext().getUser() + " to \"/" + path +
        // "\".");
        String str[] = new String[] { objToRestore + "", getRepositoryContext().getUser() + "", path + "" };//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.Restoration", str)); //$NON-NLS-1$
        boolean isExtendPoint = false;
        ERepositoryObjectType repositoryObjectType = objToRestore.getRepositoryObjectType();
        for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
            isExtendPoint = handler.isRepObjType(repositoryObjectType);
            if (isExtendPoint == true) {
                break;
            }
        }
        fireRepositoryPropertyChange(ERepositoryActionName.RESTORE.getName(), null, objToRestore);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#moveObject(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryViewObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#moveObject(org.talend.core.model.repository.IRepositoryViewObject
     * , org.eclipse.core.runtime.IPath)
     */
    @Override
    public void moveObject(IRepositoryViewObject objToMove, IPath targetPath, IPath... sourcePath) throws PersistenceException,
            BusinessException {
        checkAvailability(objToMove);
        // avoid to check the name, since it's only one object moved, from one folder to another one, of course the name
        // don't exist already.

        // Item item = objToMove.getProperty().getItem();
        // Project project = new Project(projectManager.getProject(item));
        // checkFileNameAndPath(project, item, RepositoryConstants.getPattern(objToMove.getType()), targetPath, false);
        this.repositoryFactoryFromProvider.moveObject(objToMove, targetPath);
        // i18n
        // log.debug("Move [" + objToMove + "] to \"" + path + "\".");
        String str[] = new String[] { objToMove + "", targetPath + "" }; //$NON-NLS-1$ //$NON-NLS-2$
        log.debug(Messages.getString("ProxyRepositoryFactory.log.move", str)); //$NON-NLS-1$
        // unlock(getItem(objToMove));

        if (sourcePath != null && sourcePath.length == 1) {
            fireRepositoryPropertyChange(ERepositoryActionName.MOVE.getName(), objToMove,
                    new IPath[] { sourcePath[0], targetPath });
        }
    }

    @Override
    public void moveObjectMulti(IRepositoryViewObject[] objToMoves, IPath targetPath, Map<IRepositoryViewObject, IPath> map)
            throws PersistenceException, BusinessException {
        for (IRepositoryViewObject objToMove : objToMoves) {
            checkAvailability(objToMove);
        }
        this.repositoryFactoryFromProvider.moveObjectMulti(objToMoves, targetPath);
        for (IRepositoryViewObject objToMove : objToMoves) {
            IPath sourcePath = map.get(objToMove);
            String str[] = new String[] { objToMove + "", targetPath + "" }; //$NON-NLS-1$ //$NON-NLS-2$
            log.debug(Messages.getString("ProxyRepositoryFactory.log.move", str)); //$NON-NLS-1$

            if (sourcePath != null) {
                fireRepositoryPropertyChange(ERepositoryActionName.MOVE.getName(), objToMove, new IPath[] { sourcePath,
                        targetPath });
            }
        }
    }

    // TODO SML Renommer et finir la m�thode et la plugger dans toutes les m�thodes
    private void checkAvailability(IRepositoryViewObject objToMove) throws BusinessException {
        if (!getRepositoryContext().isEditableAsReadOnly()
                && (!isPotentiallyEditable(objToMove) || ProxyRepositoryFactory.getInstance().isUserReadOnlyOnCurrentProject())) {
            throw new BusinessException(Messages.getString("ProxyRepositoryFactory.bussinessException.itemNonModifiable")); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#lock(org.talend.core.model.repository.IRepositoryViewObject)
     */
    @Override
    public void lock(IRepositoryViewObject obj) throws PersistenceException, LoginException {
        lock(getItem(obj));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#lock(org.talend.core.model.properties.Item)
     */
    @Override
    public boolean lock(Item item) throws PersistenceException, LoginException {
        boolean locked = false;
        boolean alreadyLockedBefore = false;
        boolean toLock = getStatus(item).isPotentiallyEditable();
        if (!toLock) {
            alreadyLockedBefore = getStatus(item).equals(ERepositoryStatus.LOCK_BY_USER);
        }

        // even if item is already locked, force to call the method to ensure the item is still locked
        if (toLock || alreadyLockedBefore) {
            locked = this.repositoryFactoryFromProvider.lock(item);
            if (locked && !alreadyLockedBefore) {
                notifyLock(item, true);
            }
            String str[] = new String[] { item.toString(), getRepositoryContext().getUser().toString() };
            log.debug(Messages.getString("ProxyRepositoryFactory.log.lock", str)); //$NON-NLS-1$
        }
        return locked;
    }

    /**
     * DOC sgandon Comment method "notifyLock".
     * 
     * @param item
     */
    private void notifyLock(Item item, boolean lock) {
        Bundle bundle = FrameworkUtil.getBundle(this.getClass());
        if (bundle != null) {
            BundleContext bundleContext = CoreRepositoryPlugin.getDefault().getBundle().getBundleContext();
            ServiceReference ref = bundleContext != null ? bundleContext.getServiceReference(EventAdmin.class.getName()) : null;
            if (ref != null) {
                @SuppressWarnings("null")
                EventAdmin eventAdmin = (EventAdmin) bundleContext.getService(ref);

                Dictionary<String, Object> properties = new Hashtable<String, Object>();
                properties.put(Constant.ITEM_EVENT_PROPERTY_KEY, item);

                Event lockEvent = new Event(Constant.REPOSITORY_ITEM_EVENT_PREFIX
                        + (lock ? Constant.ITEM_LOCK_EVENT_SUFFIX : Constant.ITEM_UNLOCK_EVENT_SUFFIX), properties);

                eventAdmin.sendEvent(lockEvent);
            }
        }// else no bundle for this, should never happend.
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getAllVersion(java.lang.String)
     */
    @Override
    public List<IRepositoryViewObject> getAllVersion(String id) throws PersistenceException {
        List<IRepositoryViewObject> allVersion = getAllRefVersion(projectManager.getCurrentProject(), id);
        return allVersion;
    }

    @Override
    public List<IRepositoryViewObject> getAllVersion(String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryViewObject> allVersion = getAllRefVersion(projectManager.getCurrentProject(), id, folderPath, type);
        return allVersion;
    }

    public List<IRepositoryViewObject> getAllRefVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryViewObject> allVersion = getAllVersion(project, id, false);
        if (allVersion.isEmpty()) {
            for (Project p : projectManager.getReferencedProjects(project)) {
                allVersion = getAllRefVersion(p, id);
                if (!allVersion.isEmpty()) {
                    break;
                }
            }
        }
        return allVersion;
    }

    public List<IRepositoryViewObject> getAllRefVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryViewObject> allVersion = getAllVersion(project, id, folderPath, type);
        if (allVersion.isEmpty()) {
            for (Project p : projectManager.getReferencedProjects(project)) {
                allVersion = getAllRefVersion(p, id, folderPath, type);
                if (!allVersion.isEmpty()) {
                    break;
                }
            }
        }
        return allVersion;
    }

    @Override
    public List<IRepositoryViewObject> getAllVersion(Project project, String id, boolean avoidSaveProject)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAllVersion(project, id, avoidSaveProject);
    }

    @Override
    public List<IRepositoryViewObject> getAllVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAllVersion(project, id, folderPath, type);
    }

    @Override
    public IRepositoryViewObject getLastVersion(Project project, String id) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(project, id);
    }

    @Override
    public IRepositoryViewObject getLastVersion(Project project, String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(project, id, folderPath, type);
    }

    public IRepositoryViewObject getLastVersion(String id, String folderPath, ERepositoryObjectType type)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getLastVersion(projectManager.getCurrentProject(), id, folderPath, type);
    }

    @Override
    public IRepositoryViewObject getLastVersion(String id) throws PersistenceException {
        IRepositoryViewObject lastRefVersion = getLastRefVersion(projectManager.getCurrentProject(), id);
        return lastRefVersion;

    }

    @Override
    public IRepositoryViewObject getLastRefVersion(Project project, String id) throws PersistenceException {
        IRepositoryViewObject lastVersion = getLastVersion(project, id);
        if (lastVersion == null) {
            for (Project p : projectManager.getReferencedProjects(project)) {
                lastVersion = getLastRefVersion(p, id);
                if (lastVersion != null) {
                    break;
                }
            }
        }
        return lastVersion;
    }

    @Override
    public List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type) throws PersistenceException {
        return getAll(project, type, false);
    }

    @Override
    public List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(project, type, withDeleted, false);
    }

    @Override
    public List<IRepositoryViewObject> getAll(Project project, ERepositoryObjectType type, boolean withDeleted,
            boolean allVersions) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(project, type, withDeleted, allVersions);
    }

    @Override
    public List<IRepositoryViewObject> getAll(ERepositoryObjectType type) throws PersistenceException {
        return getAll(projectManager.getCurrentProject(), type, false);
    }

    @Override
    public List<IRepositoryViewObject> getAll(ERepositoryObjectType type, boolean withDeleted) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(projectManager.getCurrentProject(), type, withDeleted, false);
    }

    @Override
    public List<IRepositoryViewObject> getAll(ERepositoryObjectType type, boolean withDeleted, boolean allVersions)
            throws PersistenceException {
        return this.repositoryFactoryFromProvider.getAll(projectManager.getCurrentProject(), type, withDeleted, allVersions);
    }

    @Override
    public List<String> getFolders(ERepositoryObjectType type) throws PersistenceException {
        return getFolders(projectManager.getCurrentProject(), type);
    }

    public List<String> getFolders(Project project, ERepositoryObjectType type) throws PersistenceException {
        HashMap<String, Boolean> folderItems = new HashMap<String, Boolean>();
        List<String> toReturn = new ArrayList<String>();
        String[] split = ERepositoryObjectType.getFolderName(type).split("/"); //$NON-NLS-1$
        String labelType = split[split.length - 1];

        FolderItem folderItem = getFolderItem(project, type, new Path(""));//$NON-NLS-1$
        addChildren(folderItems, toReturn, folderItem, labelType, ""); //$NON-NLS-1$

        return toReturn;

    }

    private void addChildren(HashMap<String, Boolean> folderItems, List<String> target, FolderItem source, String type,
            String path) {
        if (source.getType() == FolderType.FOLDER_LITERAL) {
            // for bug 9352: .svnlog folder should not be visible in wizards
            EObject obj = source.getParent();
            if (obj != null && obj instanceof FolderItemImpl) {
                String onePath = path + source.getProperty().getLabel();
                target.add(onePath);
                folderItems.put(onePath, source.getState().isDeleted());
                for (Object current : source.getChildren()) {
                    if (current instanceof FolderItem) {
                        addChildren(folderItems, target, (FolderItem) current, type, onePath + "/"); //$NON-NLS-1$
                    }
                }
            }

        }

        if (source.getType() == FolderType.SYSTEM_FOLDER_LITERAL || source.getType() == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
            boolean match = source.getProperty().getLabel().equals(type);

            for (Object current : source.getChildren()) {
                if (current instanceof FolderItem) {
                    FolderItem currentChild = (FolderItem) current;
                    if (currentChild.getType() == FolderType.FOLDER_LITERAL && match) {
                        addChildren(folderItems, target, currentChild, type, path);
                    } else if (currentChild.getType() == FolderType.SYSTEM_FOLDER_LITERAL
                            || currentChild.getType() == FolderType.STABLE_SYSTEM_FOLDER_LITERAL) {
                        addChildren(folderItems, target, currentChild, type, path);
                    }
                }
            }
        }
    }

    @Override
    public HashMap<String, Boolean> getFolderItems(ERepositoryObjectType type) throws PersistenceException {
        return getFolderItems(projectManager.getCurrentProject(), type);
    }

    public HashMap<String, Boolean> getFolderItems(Project project, ERepositoryObjectType type) throws PersistenceException {
        HashMap<String, Boolean> folderItems = new HashMap<String, Boolean>();
        List<String> toReturn = new ArrayList<String>();
        String[] split = ERepositoryObjectType.getFolderName(type).split("/"); //$NON-NLS-1$
        String labelType = split[split.length - 1];

        FolderItem folderItem = getFolderItem(project, type, new Path(""));//$NON-NLS-1$
        addChildren(folderItems, toReturn, folderItem, labelType, ""); //$NON-NLS-1$

        return folderItems;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getDocumentationStatus()
     */
    @Override
    public List<Status> getDocumentationStatus() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getDocumentationStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getTechnicalStatus()
     */
    @Override
    public List<Status> getTechnicalStatus() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getTechnicalStatus();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getTechnicalStatus()
     */
    // public List<SpagoBiServer> getSpagoBiServer() throws PersistenceException {
    // return this.repositoryFactoryFromProvider.getSpagoBiServer();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setDocumentationStatus(java.util.List)
     */
    @Override
    public void setDocumentationStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setDocumentationStatus(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#forceCreate(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    @Override
    public void forceCreate(Item item, IPath path) throws PersistenceException {
        forceCreate(projectManager.getCurrentProject(), item, path);
    }

    @Override
    public void forceCreate(Project project, Item item, IPath path) throws PersistenceException {
        this.repositoryFactoryFromProvider.create(project, item, path);
    }

    @Override
    public void createParentFoldersRecursively(ERepositoryObjectType itemType, IPath path) throws PersistenceException {
        createParentFoldersRecursively(projectManager.getCurrentProject(), itemType, path);
    }

    @Override
    public void createParentFoldersRecursively(Project project, ERepositoryObjectType itemType, IPath path)
            throws PersistenceException {
        createParentFoldersRecursively(project, itemType, path, false);
    }

    @Override
    public void createParentFoldersRecursively(Project project, ERepositoryObjectType itemType, IPath path, boolean isImportItem)
            throws PersistenceException {

        List<String> folders = getFolders(project, itemType);

        for (int i = 0; i < path.segmentCount(); i++) {
            IPath parentPath = path.removeLastSegments(path.segmentCount() - i);
            String folderLabel = path.segment(i);

            String folderName = parentPath.append(folderLabel).toString();
            boolean found = false;
            for (String existedFolder : folders) {
                if (folderName.toUpperCase().equals(existedFolder.toUpperCase())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                // TDI-29841, in win, case insensitive issue for folder. If not existed, create the upper case folder
                // always.
                folderName = folderName.toUpperCase();
                createFolder(project, itemType, parentPath, folderLabel);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setTechnicalStatus(java.util.List)
     */
    @Override
    public void setTechnicalStatus(List<Status> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setTechnicalStatus(list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#setSpagoBiServer(java.util.List)
     */
    @Override
    public void setSpagoBiServer(List<SpagoBiServer> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setSpagoBiServer(list);
    }

    @Override
    public void setMigrationTasksDone(Project project, List<MigrationTask> list) throws PersistenceException {
        this.repositoryFactoryFromProvider.setMigrationTasksDone(project, list);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isServerValid()
     */
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isServerValid()
     */
    // public String isServerValid() {
    // return this.repositoryFactoryFromProvider.isServerValid();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#create(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    @Override
    public void create(Item item, IPath path, boolean... isImportItem) throws PersistenceException {
        create(projectManager.getCurrentProject(), item, path, isImportItem);
    }

    @Override
    public void create(Project project, Item item, IPath path, boolean... isImportItem) throws PersistenceException {
        boolean isOherProcess = false;
        for (IRepositoryContentHandler handler : RepositoryContentManager.getHandlers()) {
            isOherProcess = handler.isProcess(item);
            if (isOherProcess) {
                break;
            }
        }
        ICoreService coreService = getCoreService();
        if (coreService != null && (isOherProcess || item instanceof ProcessItem)) {
            try {
                coreService.checkJob(item.getProperty().getLabel());
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }
        checkFileNameAndPath(project, item, RepositoryConstants.getPattern(ERepositoryObjectType.getItemType(item)), path, false,
                isImportItem);

        this.repositoryFactoryFromProvider.create(project, item, path, isImportItem);
        if (isImportItem.length == 0 || !isImportItem[0]) {
            // no listener if from import, or it will be too slow.
            fireRepositoryPropertyChange(ERepositoryActionName.CREATE.getName(), null, item);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#save(org.talend.core.model.properties.Item)
     */
    @Override
    public void save(Item item, boolean... isMigrationTask) throws PersistenceException {
        save(projectManager.getCurrentProject(), item, isMigrationTask);
    }

    @Override
    public void save(Project project, Item item, boolean... isMigrationTask) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(project, item);
        if (isMigrationTask == null || isMigrationTask.length == 0 || !isMigrationTask[0]) {
            boolean avoidGenerateProm = false;
            if (isMigrationTask != null && isMigrationTask.length == 2) {
                avoidGenerateProm = isMigrationTask[1];
            }
            fireRepositoryPropertyChange(ERepositoryActionName.SAVE.getName(), avoidGenerateProm, item);

        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#save(org.talend.core.model.properties.Property)
     */
    @Override
    public void save(Property property, String... originalNameAndVersion) throws PersistenceException {
        save(projectManager.getCurrentProject(), property, originalNameAndVersion);
    }

    @Override
    public void save(Project project, Property property, String... originalNameAndVersion) throws PersistenceException {
        this.repositoryFactoryFromProvider.save(project, property);
        fireRepositoryPropertyChange(ERepositoryActionName.PROPERTIES_CHANGE.getName(), originalNameAndVersion, property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#copy(org.talend.core.model.properties.Item,
     * org.eclipse.core.runtime.IPath)
     */
    @Override
    public Item copy(Item sourceItem, IPath targetPath) throws PersistenceException, BusinessException {
        ICoreService coreService = getCoreService();
        if (coreService != null && sourceItem instanceof ProcessItem) {
            try {
                coreService.checkJob(sourceItem.getProperty().getLabel());
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }

        Item targetItem = this.repositoryFactoryFromProvider.copy(sourceItem, targetPath);

        fireRepositoryPropertyChange(ERepositoryActionName.COPY.getName(), sourceItem, targetItem);
        return targetItem;

    }

    @Override
    public Item copy(Item sourceItem, IPath targetPath, boolean changeLabelWithCopyPrefix) throws PersistenceException,
            BusinessException {

        ICoreService coreService = getCoreService();
        if (coreService != null && sourceItem instanceof ProcessItem) {
            try {
                coreService.checkJob(sourceItem.getProperty().getLabel());
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }
        Item targetItem = this.repositoryFactoryFromProvider.copy(sourceItem, targetPath, changeLabelWithCopyPrefix);
        fireRepositoryPropertyChange(ERepositoryActionName.COPY.getName(), sourceItem, targetItem);
        return targetItem;

    }

    @Override
    public Item copy(Item sourceItem, IPath targetPath, String newItemLabel) throws PersistenceException, BusinessException {

        ICoreService coreService = getCoreService();
        if (coreService != null && sourceItem instanceof ProcessItem) {
            try {
                coreService.checkJob(sourceItem.getProperty().getLabel());
            } catch (BusinessException e) {
                throw new PersistenceException(e);
            } catch (RuntimeException e) {
                // don't do anything
            }
        }
        Item targetItem = this.repositoryFactoryFromProvider.copy(sourceItem, targetPath, newItemLabel);
        fireRepositoryPropertyChange(ERepositoryActionName.COPY.getName(), sourceItem, targetItem);
        return targetItem;

    }

    @Override
    public void saveCopy(Item sourceItem, Item targetItem) {
        fireRepositoryPropertyChange(ERepositoryActionName.COPY.getName(), sourceItem, targetItem);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#reload(org.talend.core.model.properties.Property)
     */
    @Override
    public Property reload(Property property) throws PersistenceException {
        return this.repositoryFactoryFromProvider.reload(property);
    }

    public Property reload(Property property, IFile file) throws PersistenceException {
        return this.repositoryFactoryFromProvider.reload(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#unlock(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.IRepositoryViewObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#unlock(org.talend.core.model.repository.IRepositoryViewObject
     * )
     */
    @Override
    public void unlock(IRepositoryViewObject obj) throws PersistenceException, LoginException {
        unlock(getItem(obj));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#unlock(org.talend.core.model.properties.Item)
     */
    @Override
    public void unlock(Item obj) throws PersistenceException, LoginException {
        if (!(obj instanceof FolderItem) && (obj.eResource() == null || obj.getProperty().eResource() == null)
                && getUptodateProperty(obj.getProperty()) != null) {
            // item has been unloaded
            obj = getUptodateProperty(obj.getProperty()).getItem();
        }
        if (getStatus(obj) == ERepositoryStatus.LOCK_BY_USER || obj instanceof JobletDocumentationItem
                || obj instanceof JobDocumentationItem) {
            Date commitDate = obj.getState().getCommitDate();
            Date modificationDate = ItemDateParser.parseAdditionalDate(obj.getProperty(), ItemProductKeys.DATE.getModifiedKey());
            if (modificationDate == null || commitDate == null || modificationDate.before(commitDate)) {
                boolean unlocked = this.repositoryFactoryFromProvider.unlock(obj);
                if (unlocked) {
                    notifyLock(obj, false);
                }
                String str[] = new String[] { obj.toString(), getRepositoryContext().getUser().toString() };
                log.debug(Messages.getString("ProxyRepositoryFactory.log.unlock", str)); //$NON-NLS-1$
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#findUser(org.talend.core.model.general.Project)
     */
    // public boolean doesLoggedUserExist() throws PersistenceException {
    // return this.repositoryFactoryFromProvider.doesLoggedUserExist();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#createUser(org.talend.core.model.general.Project)
     */
    // public void createUser() throws PersistenceException {
    // this.repositoryFactoryFromProvider.createUser();
    // }
    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#initialize()
     */
    @Override
    public void initialize() throws PersistenceException {
        this.repositoryFactoryFromProvider.initialize();
    }

    public void loadProjectAndSetContext(IProject eclipseProject) throws PersistenceException {
        this.repositoryFactoryFromProvider.loadProjectAndSetContext(eclipseProject);
    }

    /**
     * DOC smallet Comment method "emptyTempFolder".
     * 
     * @param project
     * @throws PersistenceException
     */
    private void emptyTempFolder(Project project) throws PersistenceException {
        String str = (System.getProperty("eclipse.home.location") + "temp").substring(5);
        FilesUtils.deleteFolder(new File(str), false);

        long start = System.currentTimeMillis();
        IProject fsProject = ResourceUtils.getProject(project);
        IFolder folder = ResourceUtils.getFolder(fsProject, RepositoryConstants.TEMP_DIRECTORY, false);
        if (folder.exists()) {
            int nbResourcesDeleted = ResourceUtils.emptyFolder(folder);
            long elapsedTime = System.currentTimeMillis() - start;
            log.trace(Messages.getString("ProxyRepositoryFactory.log.tempFolderEmptied", nbResourcesDeleted, elapsedTime)); //$NON-NLS-1$
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.repository.IRepositoryViewObject
     * )
     */
    @Override
    public ERepositoryStatus getStatus(IRepositoryViewObject obj) {
        if (obj instanceof ISubRepositoryObject) {
            ISubRepositoryObject subRepositoryObject = (ISubRepositoryObject) obj;
            if (SubItemHelper.isDeleted(subRepositoryObject.getAbstractMetadataObject())) {
                return ERepositoryStatus.DELETED;
            }
        }
        if (obj instanceof RepositoryViewObject) {
            return obj.getRepositoryStatus();
        }
        return getStatus(getItem(obj));
    }

    public void setSubItemDeleted(ConnectionItem item, AbstractMetadataObject abstractMetadataObject, boolean deleted) {
        SubItemHelper.setDeleted(abstractMetadataObject, deleted);
        if (deleted) {
            RecycleBinManager.getInstance().addToRecycleBin(ProjectManager.getInstance().getCurrentProject(), item);
        } else {
            if (!item.getState().isDeleted() && !ProjectRepositoryNode.getInstance().hasDeletedSubItem(item)) {
                RecycleBinManager.getInstance().removeFromRecycleBin(ProjectManager.getInstance().getCurrentProject(), item);
            } else {
                RecycleBinManager.getInstance().saveRecycleBin(ProjectManager.getInstance().getCurrentProject());
            }
        }
    }

    @Override
    @Deprecated
    public boolean isDeleted(MetadataTable table) {
        // TODO SML/MHE Remove when table are items
        if (TableHelper.isDeleted(table)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isUserReadOnlyOnCurrentProject() {
        return this.repositoryFactoryFromProvider.isUserReadOnlyOnCurrentProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.properties.Item)
     */
    @Override
    public ERepositoryStatus getStatus(Item item) {
        ERepositoryStatus toReturn;
        toReturn = this.repositoryFactoryFromProvider.getStatus(item);

        if (toReturn != ERepositoryStatus.DELETED
                && (isUserReadOnlyOnCurrentProject() || !projectManager.isInCurrentMainProject(item))) {
            return ERepositoryStatus.READ_ONLY;
        }

        return toReturn;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getStatus(org.talend.core.model.properties.InformationLevel)
     */
    @Override
    public ERepositoryStatus getStatus(InformationLevel level) {

        if (level.getValue() == InformationLevel.WARN) {
            return ERepositoryStatus.WARN;
        } else if (level.getValue() == InformationLevel.ERROR) {
            return ERepositoryStatus.ERROR;
        }
        return ERepositoryStatus.DEFAULT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryFactory#getStatusAndLockIfPossible(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#isEditableAndLockIfPossible(org.talend.core.model.properties
     * .Item)
     */
    @Override
    public boolean isEditableAndLockIfPossible(Item item) {
        if (!projectManager.isInCurrentMainProject(item)) {
            return false;
        }

        ERepositoryStatus status = getStatus(item);
        boolean potentiallyEditable = status.isPotentiallyEditable();
        if (potentiallyEditable) {
            if (getRepositoryContext().isEditableAsReadOnly()) {
                return true;
            }
            try {
                lock(item);
            } catch (PersistenceException e) {
                MessageBoxExceptionHandler.process(e);
            } catch (LoginException e) {
                MessageBoxExceptionHandler.process(e);
            }
            status = getStatus(item);
        }

        return status.isEditable();
    }

    // public boolean isLastVersion(Item item) {
    // if (item.getProperty() != null) {
    // try {
    // List<IRepositoryViewObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
    // item.getProperty().getId());
    // if (allVersion != null && !allVersion.isEmpty()) {
    // if (allVersion.get(allVersion.size() - 1).getVersion().equals(item.getProperty().getVersion())) {
    // return true;
    // }
    // }
    // } catch (PersistenceException e) {
    // ExceptionHandler.process(e);
    // }
    // }
    // return false;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IRepositoryFactory#isEditable(org.talend.core.model.repository.IRepositoryViewObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#isEditableAndLockIfPossible(org.talend.core.model.repository
     * .IRepositoryViewObject)
     */
    @Override
    public boolean isEditableAndLockIfPossible(IRepositoryViewObject obj) {
        if (obj instanceof ISubRepositoryObject) {
            AbstractMetadataObject abstractMetadataObject = ((ISubRepositoryObject) obj).getAbstractMetadataObject();
            if (SubItemHelper.isDeleted(abstractMetadataObject)) {
                return false;
            } else {
                return isEditableAndLockIfPossible(getItem(obj));
            }
        } else {
            return isEditableAndLockIfPossible(getItem(obj));
        }
    }

    @Override
    public org.talend.core.model.properties.Project getProject(Item item) {
        EObject object = EcoreUtil.getRootContainer(item);
        if (object != null && object instanceof org.talend.core.model.properties.Project) {
            return (org.talend.core.model.properties.Project) object;
        }
        return projectManager.getCurrentProject().getEmfProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#isPotentiallyEditable(org.talend.core.model.properties.Item)
     */
    private boolean isPotentiallyEditable(Item item) {
        if (!projectManager.isInCurrentMainProject(item)) {
            return false;
        }

        ERepositoryStatus status = getStatus(item);
        return status.isPotentiallyEditable();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IRepositoryFactory#isPotentiallyEditable(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#isPotentiallyEditable(org.talend.core.model.repository.
     * IRepositoryViewObject)
     */
    @Override
    public boolean isPotentiallyEditable(IRepositoryViewObject obj) {
        // referenced project items can't be edited.
        if (!projectManager.getCurrentProject().getLabel().equals(obj.getProjectLabel())) {
            return false;
        }
        if (obj instanceof ISubRepositoryObject) {
            AbstractMetadataObject abstractMetadataObject = ((ISubRepositoryObject) obj).getAbstractMetadataObject();
            if (SubItemHelper.isDeleted(abstractMetadataObject)) {
                return true;
            } else {
                if (obj instanceof RepositoryViewObject) {
                    return obj.getRepositoryStatus().isPotentiallyEditable();
                } else {
                    return isPotentiallyEditable(getItem(obj));
                }
            }
        } else {
            if (obj instanceof RepositoryViewObject) {
                return obj.getRepositoryStatus().isPotentiallyEditable();
            } else {
                return isPotentiallyEditable(getItem(obj));
            }
        }
    }

    private Item getItem(IRepositoryViewObject obj) {
        return obj.getProperty().getItem();
    }

    @Override
    public List<org.talend.core.model.properties.Project> getReferencedProjects(Project project) {
        return this.repositoryFactoryFromProvider.getReferencedProjects(project);
    }

    @Override
    public Boolean hasChildren(Object parent) {
        return repositoryFactoryFromProvider.hasChildren(parent);
    }

    @Override
    public synchronized List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException {
        return this.repositoryFactoryFromProvider.getModulesNeededForJobs();
    }

    @Override
    public void initEmfProjectContent() throws PersistenceException, BusinessException {
        Project[] projects = readProject();
        emfProjectContentMap.clear();
        if (projects != null && projects.length > 0) {
            for (Project p : projects) {
                emfProjectContentMap.put(p.getTechnicalLabel(), p.getEmfProject());
            }
        }
    }

    /**
     * DOC tang Comment method "logOnProject".
     * 
     * @param project
     * @param monitorWrap
     * @throws PersistenceException
     * @throws LoginException
     */
    public void logOnProject(Project project, IProgressMonitor monitor) throws LoginException, PersistenceException {
        try {
            TimeMeasure.display = CommonsPlugin.isDebugMode();
            TimeMeasure.displaySteps = CommonsPlugin.isDebugMode();
            TimeMeasure.measureActive = CommonsPlugin.isDebugMode();

            TimeMeasure.begin("logOnProject"); //$NON-NLS-1$
            try {
                System.getProperties().put("ReadOnlyUser", Boolean.FALSE.toString()); //$NON-NLS-1$

                // remove the auto-build to enhance the build speed and application's use
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceDescription description = workspace.getDescription();
                if (description.isAutoBuilding()) {
                    description.setAutoBuilding(false);
                    try {
                        workspace.setDescription(description);
                    } catch (CoreException e) {
                        // do nothing
                    }
                }
                isCancelled = false;
                fullLogonFinished = false;
                SubMonitor subMonitor = SubMonitor.convert(monitor, MAX_TASKS);
                SubMonitor currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.logonInProgress"), 1); //$NON-NLS-1$
                project.setReferenceProjectProvider(null);
                getRepositoryContext().setProject(null);
                initEmfProjectContent();
                if (getEmfProjectContent(project.getTechnicalLabel()) != null) {
                    project.setEmfProject(getEmfProjectContent(project.getTechnicalLabel()));
                }
                getRepositoryContext().setProject(project);

                currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.initializeProjectConnection"), 1); //$NON-NLS-1$
                ProjectManager.getInstance().getBeforeLogonRecords().clear();
                ProjectManager.getInstance().getUpdatedRemoteHandlerRecords().clear();
                ReferenceProjectProvider.clearTacReferenceList();
                ReferenceProjectProblemManager.getInstance().clearAll();
                this.repositoryFactoryFromProvider.beforeLogon(project);
                ProjectManager.getInstance().getBeforeLogonRecords().clear();
                ProjectManager.getInstance().getUpdatedRemoteHandlerRecords().clear();
                // Check reference project setting problems
                checkReferenceProjectsProblems(project);
                if (isCancelled) {
                    throw new OperationCanceledException(""); //$NON-NLS-1$
                }
                // monitorWrap.worked(1);
                TimeMeasure.step("logOnProject", "beforeLogon"); //$NON-NLS-1$ //$NON-NLS-2$

                // Check project compatibility
                checkProjectCompatibility(project);

                if (GlobalServiceRegister.getDefault().isServiceRegistered(IMavenUIService.class)) {
                    IMavenUIService mavenUIService = (IMavenUIService) GlobalServiceRegister.getDefault().getService(
                            IMavenUIService.class);
                    if (mavenUIService != null) {
                        mavenUIService.updateMavenResolver(true);
                    }
                }

                ICoreService coreService = getCoreService();
                if (coreService != null) {
                    currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                    currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.synchronizeLibraries"), 1); //$NON-NLS-1$
                    coreService.syncLibraries(currentMonitor);
                    TimeMeasure.step("logOnProject", "Sync components libraries"); //$NON-NLS-1$
                }

                currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                currentMonitor.beginTask("Execute before logon migrations tasks", 1); //$NON-NLS-1$
                ProjectManager.getInstance().getMigrationRecords().clear();
                executeMigrations(project, true, currentMonitor);
                ProjectManager.getInstance().getMigrationRecords().clear();
                // monitorWrap.worked(1);
                TimeMeasure.step("logOnProject", "executeMigrations(beforeLogonTasks)"); //$NON-NLS-1$ //$NON-NLS-2$

                currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.logonInProgress"), 1); //$NON-NLS-1$
                ProjectManager.getInstance().getLogonRecords().clear();
                this.repositoryFactoryFromProvider.logOnProject(project);
                ProjectManager.getInstance().getLogonRecords().clear();
                // monitorWrap.worked(1);
                TimeMeasure.step("logOnProject", "logOnProject"); //$NON-NLS-1$ //$NON-NLS-2$

                emptyTempFolder(project);

                currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.load.componnents"), 1); //$NON-NLS-1$
                ICoreUIService coreUiService = null;
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ICoreUIService.class)) {
                    coreUiService = (ICoreUIService) GlobalServiceRegister.getDefault().getService(ICoreUIService.class);
                }
                if (coreUiService != null) {
                    coreUiService.componentsReset();
                }

                fireRepositoryPropertyChange(ERepositoryActionName.PROJECT_PREFERENCES_RELOAD.getName(), null, null);

                IRunProcessService runProcessService = getRunProcessService();
                if (runProcessService != null) {
                    runProcessService.initMavenJavaProject(monitor, project);
                }

                currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.exec.migration.tasks"), 1); //$NON-NLS-1$
                ProjectManager.getInstance().getMigrationRecords().clear();
                executeMigrations(project, false, currentMonitor);
                ProjectManager.getInstance().getMigrationRecords().clear();
                TimeMeasure.step("logOnProject", "executeMigrations(afterLogonTasks)"); //$NON-NLS-1$ //$NON-NLS-2$
                if (monitor != null && monitor.isCanceled()) {
                    throw new OperationCanceledException(""); //$NON-NLS-1$
                }
                
                boolean isCommandLineLocalRefProject = false;
                CommandLineContext commandLineContext = (CommandLineContext) CoreRuntimePlugin.getInstance().getContext()
                        .getProperty(Context.COMMANDLINE_CONTEXT_KEY);
                if (commandLineContext != null && commandLineContext.isLogonRefProject()) {
                    isCommandLineLocalRefProject = true;
                }

                if (coreService != null) {
                    // clean workspace
                    currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.cleanWorkspace"), 1); //$NON-NLS-1$

                    String specifiedVersion = null;
                    String currentVersion = JavaUtils.getProjectJavaVersion();
                    String newVersion = null;
                    if (CommonUIPlugin.isFullyHeadless()) {
                        specifiedVersion = JavaHomeUtil.getSpecifiedJavaVersion();
                    }
                    if (specifiedVersion == null) {
                        newVersion = currentVersion != null ? currentVersion : JavaUtils.DEFAULT_VERSION;
                    } else {
                        newVersion = specifiedVersion;
                    }

                    JavaUtils.updateProjectJavaVersion(newVersion);
                    
                    TimeMeasure.step("logOnProject", "clean Java project"); //$NON-NLS-1$ //$NON-NLS-2$     

                    if (workspace instanceof Workspace) {
                        ((Workspace) workspace).getFileSystemManager().getHistoryStore().clean(currentMonitor);
                    }
                    TimeMeasure.step("logOnProject", "clean workspace history"); //$NON-NLS-1$ //$NON-NLS-2$

                    currentMonitor = subMonitor.newChild(1, SubMonitor.SUPPRESS_NONE);
                    currentMonitor.beginTask(Messages.getString("ProxyRepositoryFactory.synch.repo.items"), 1); //$NON-NLS-1$

                    if (!isCommandLineLocalRefProject) {
                        try {
                            coreService.syncAllRoutines();
                            // PTODO need refactor later, this is not good, I think
                            coreService.syncAllBeans();
                        } catch (SystemException e1) {
                            //
                        }
                    }
                }
                if (monitor != null && monitor.isCanceled()) {
                    throw new OperationCanceledException(""); //$NON-NLS-1$
                }
                // rules
                if (coreUiService != null && PluginChecker.isRulesPluginLoaded()) {
                    coreUiService.syncAllRules();
                }
                if (monitor != null && monitor.isCanceled()) {
                    throw new OperationCanceledException(""); //$NON-NLS-1$
                }
                TimeMeasure.step("logOnProject", "sync repository (routines/rules/beans)"); //$NON-NLS-1$ //$NON-NLS-2$

                // log4j prefs
                if (coreUiService != null && coreService != null) {
                    coreService.syncLog4jSettings(null);
                    TimeMeasure.step("logOnProject", "sync log4j"); //$NON-NLS-1$ //$NON-NLS-2$
                }

                try {
                    URL url = MetadataTalendType.getProjectForderURLOfMappingsFile();
                    if (url != null) {
                        // set the project mappings url
                        System.setProperty("talend.mappings.url", url.toString()); // $NON-NLS-1$
                    }
                } catch (SystemException e) {
                    // ignore
                }

                if (runProcessService != null && !isCommandLineLocalRefProject) {
                    runProcessService.initializeRootPoms(monitor);

                    TimeMeasure.step("logOnProject", "install / setup root poms"); //$NON-NLS-1$ //$NON-NLS-2$
                }
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ITDQRepositoryService.class)) {
                    ITDQRepositoryService tdqRepositoryService = (ITDQRepositoryService) GlobalServiceRegister.getDefault()
                            .getService(ITDQRepositoryService.class);
                    if (tdqRepositoryService != null) {
                        tdqRepositoryService.initProxyRepository();
                    }
                }

                /**
                 * Execute TaCoKit migration before fullLogonFinished
                 */
                try {
                    ITaCoKitService tacokitService = ITaCoKitService.getInstance();
                    if (tacokitService != null) {
                        tacokitService.checkMigration(monitor);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
                fullLogonFinished = true;
                this.repositoryFactoryFromProvider.afterLogon(monitor);
            } finally {
                TimeMeasure.end("logOnProject"); //$NON-NLS-1$
                TimeMeasure.display = false;
                TimeMeasure.displaySteps = false;
                TimeMeasure.measureActive = false;
            }
            String str[] = new String[] { getRepositoryContext().getUser() + "", projectManager.getCurrentProject() + "" }; //$NON-NLS-1$ //$NON-NLS-2$        
            log.info(Messages.getString("ProxyRepositoryFactory.log.loggedOn", str)); //$NON-NLS-1$
        } catch (LoginException e) {
            logOffProject();
            throw e;
        } catch (PersistenceException e) {
            logOffProject();
            throw e;
        } catch (BusinessException e) {
            logOffProject();
            throw new PersistenceException(e);
        } catch (RuntimeException e) {
            logOffProject();
            throw e;
        }
    }

    private void checkReferenceProjectsProblems(Project project) throws BusinessException, PersistenceException {
        if (ReferenceProjectProblemManager.getInstance().getInvalidProjectReferenceSet().size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (String technicalLabel : ReferenceProjectProblemManager.getInstance().getInvalidProjectReferenceSet()) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(technicalLabel);
            }
            PersistenceException missingRefException = new PersistenceException(
                    Messages.getString("ProxyRepositoryFactory.exceptionMissingReferencedProjects", sb.toString()));
            if (!CommonsPlugin.isHeadless()) {
                org.eclipse.swt.widgets.Display.getDefault().syncExec(new Runnable() {

                    @Override
                    public void run() {
                        org.eclipse.jface.dialogs.MessageDialog messageDialog = new org.eclipse.jface.dialogs.MessageDialog(
                                org.eclipse.swt.widgets.Display.getDefault().getActiveShell(),
                                Messages.getString("ProxyRepositoryFactory.titleWarning"), //$NON-NLS-1$
                                null, Messages.getString("ProxyRepositoryFactory.msgMissingReferencedProjects", sb),
                                MessageDialog.WARNING,
                                new String[] { Messages.getString("ProxyRepositoryFactory.btnLabelContinue"),
                                        IDialogConstants.CANCEL_LABEL },
                                1);
                        isCancelled = messageDialog.open() == IDialogConstants.CANCEL_ID;
                    }
                });
                ExceptionHandler.process(missingRefException, Level.ERROR);
            } else {
                throw missingRefException; // $NON-NLS-1$
            }
            log.error(Messages.getString("ProxyRepositoryFactory.exceptionMissingReferencedProjects", sb.toString()));//$NON-NLS-1$
        }

        if (!isCancelled) {
            Map<String, List<ProjectReference>> projectRefMap = new HashMap<String, List<ProjectReference>>();
            if (!ReferenceProjectProblemManager.checkCycleReference(project, projectRefMap)) {
                throw new PersistenceException(Messages.getString("ProxyRepositoryFactory.CycleReferenceError")); //$NON-NLS-1$
            }
            ReferenceProjectProblemManager.checkMoreThanOneBranch(projectRefMap);
        }
    }

    public void logOffProject() {
        // getRepositoryContext().setProject(null);
        repositoryFactoryFromProvider.logOffProject();
        if (!CommonsPlugin.isHeadless()) {
            ProjectRepositoryNode root = ProjectRepositoryNode.getInstance();
            if (root != null) {
                root.setEnableDisposed(true);
                root.dispose();
            }
        }
        IRunProcessService runProcessService = getRunProcessService();
        if (runProcessService != null) {
            runProcessService.clearProjectRelatedSettings();
        }
        ReferenceProjectProvider.clearTacReferenceList();
        ReferenceProjectProblemManager.getInstance().clearAll();
        fullLogonFinished = false;
    }

    public boolean setAuthorByLogin(Item item, String login) throws PersistenceException {
        return repositoryFactoryFromProvider.setAuthorByLogin(item, login);
    }

    @Override
    public Property getUptodateProperty(Project project, Property property) throws PersistenceException {
        return repositoryFactoryFromProvider.getUptodateProperty(project, property);
    }

    @Override
    public Property getUptodateProperty(Property property) throws PersistenceException {
        return getUptodateProperty(projectManager.getCurrentProject(), property);
    }

    @Override
    public RootContainer<String, IRepositoryViewObject> getMetadata(Project project, ERepositoryObjectType type,
            boolean... options) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getMetadata(project, type, options);
    }

    @Override
    public RootContainer<String, IRepositoryViewObject> getMetadata(ERepositoryObjectType type) throws PersistenceException {
        return getMetadata(projectManager.getCurrentProject(), type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getRecycleBinItems(org.talend.core.model.general.Project)
     */
    @Override
    public List<IRepositoryViewObject> getRecycleBinItems(Project project, boolean... options) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getRecycleBinItems(project, options);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getSpecificVersion(org.talend.core.model.general.Project,
     * java.lang.String, java.lang.String)
     */
    @Override
    public IRepositoryViewObject getSpecificVersion(Project project, String id, String version, boolean avoidSaveProject)
            throws PersistenceException {
        List<IRepositoryViewObject> objList = getAllVersion(project, id, avoidSaveProject);
        for (IRepositoryViewObject obj : objList) {
            if (obj.getVersion().equals(version)) {
                return obj;
            }
        }
        for (Project p : projectManager.getReferencedProjects(project)) {
            IRepositoryViewObject specVersion = getSpecificVersion(p, id, version, avoidSaveProject);
            if (specVersion != null) {
                return specVersion;
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#getSpecificVersion(java.lang.String, java.lang.String)
     */
    @Override
    public IRepositoryViewObject getSpecificVersion(String id, String version, boolean avoidSaveProject)
            throws PersistenceException {
        return getSpecificVersion(projectManager.getCurrentProject(), id, version, avoidSaveProject);
    }

    public void checkAvailability() throws PersistenceException {
        this.repositoryFactoryFromProvider.checkAvailability();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void executeRepositoryWorkUnit(RepositoryWorkUnit workUnit) {
        this.repositoryFactoryFromProvider.executeRepositoryWorkUnit(workUnit);
    }

    @Override
    public void unloadResources(Property property) throws PersistenceException {
        repositoryFactoryFromProvider.unloadResources(property);
    }

    /**
     * 
     * DOC mzhao Comment method "unloadResources".
     * 
     * @param uriString
     * @throws PersistenceException
     */
    public void unloadResources(String uriString) throws PersistenceException {
        repositoryFactoryFromProvider.unloadResources(uriString);
    }

    public void unloadResources() throws PersistenceException {
        repositoryFactoryFromProvider.unloadResources();
    }

    @Override
    public FolderItem getFolderItem(Project project, ERepositoryObjectType itemType, IPath path) {
        return repositoryFactoryFromProvider.getFolderItem(project, itemType, path);
    }

    /**
     * Getter for fullLogonFinished.
     * 
     * @return the fullLogonFinished
     */
    @Override
    public boolean isFullLogonFinished() {
        return this.fullLogonFinished;
    }

    /**
     * Sets the fullLogonFinished.
     * 
     * @param fullLogonFinished the fullLogonFinished to set
     */
    public void setFullLogonFinished(boolean fullLogonFinished) {
        this.fullLogonFinished = fullLogonFinished;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#addRepositoryWorkUnitListener(org.talend.core.model.repository
     * .IRepositoryWorkUnitListener)
     */
    @Override
    public void addRepositoryWorkUnitListener(IRepositoryWorkUnitListener listener) {
        repositoryFactoryFromProvider.addRepositoryWorkUnitListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#enableSandboxProject()
     */
    @Override
    public boolean enableSandboxProject() throws PersistenceException {
        return repositoryFactoryFromProvider.enableSandboxProject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IProxyRepositoryFactory#isLocalConnectionProvider()
     */
    @Override
    public boolean isLocalConnectionProvider() throws PersistenceException {
        return repositoryFactoryFromProvider.isLocalConnectionProvider();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getMetadataByFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, org.eclipse.core.runtime.IPath)
     */
    @Override
    public List<IRepositoryViewObject> getMetadataByFolder(Project project, ERepositoryObjectType itemType, IPath path) {
        return repositoryFactoryFromProvider.getMetadataByFolder(project, itemType, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.talend.repository.model.IProxyRepositoryFactory#getMetadataByFolder(org.talend.core.model.repository.
     * ERepositoryObjectType, org.eclipse.core.runtime.IPath)
     */
    @Override
    public List<IRepositoryViewObject> getMetadataByFolder(ERepositoryObjectType itemType, IPath path) {
        return repositoryFactoryFromProvider.getMetadataByFolder(projectManager.getCurrentProject(), itemType, path);
    }

    /**
     * DOC xqliu Comment method "getTdqRepositoryViewObjects".
     * 
     * @param itemType
     * @param folderName
     * @return
     * @throws PersistenceException
     */
    public RootContainer<String, IRepositoryViewObject> getTdqRepositoryViewObjects(ERepositoryObjectType itemType,
            String folderName) throws PersistenceException {
        return getTdqRepositoryViewObjects(projectManager.getCurrentProject(), itemType, folderName, true);
    }

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
            String folderName, boolean... options) throws PersistenceException {
        return this.repositoryFactoryFromProvider.getTdqRepositoryViewObjects(project, type, folderName, options);
    }

    /**
     * DOC bZhou Comment method "getProperty".
     * 
     * @param element
     * @return
     */
    public Property getProperty(ModelElement element) {
        Resource eResource = element.eResource();
        if (eResource != null) {
            URI uri = eResource.getURI();
            if (uri.isPlatform()) {
                URI propertyURI = uri.trimFileExtension().appendFileExtension(FileConstants.PROPERTIES_EXTENSION);
                XmiResourceManager resourceManager = this.repositoryFactoryFromProvider.getResourceManager();
                if (resourceManager != null && resourceManager.resourceSet != null) {
                    Resource propertyResource = resourceManager.resourceSet.getResource(propertyURI, true);
                    return (Property) EcoreUtil.getObjectByType(propertyResource.getContents(),
                            PropertiesPackage.eINSTANCE.getProperty());
                }
            }
        }

        return null;
    }

    @Override
    public LockInfo getLockInfo(Item item) {
        return repositoryFactoryFromProvider.getLockInfo(item);
    }

    @Override
    public String getNavigatorViewDescription() {
        return repositoryFactoryFromProvider.getNavigatorViewDescription();
    }

    @Override
    public void updateLockStatus() throws PersistenceException {
        this.repositoryFactoryFromProvider.updateLockStatus();

    }

    @Override
    public boolean isModified(Object property) {
        return repositoryFactoryFromProvider.isModified(property);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.model.IProxyRepositoryFactory#getObjectFromFolder(org.talend.core.model.general.Project,
     * org.talend.core.model.repository.ERepositoryObjectType, java.lang.String, int)
     */
    @Override
    public RootContainer<String, IRepositoryViewObject> getObjectFromFolder(Project project, ERepositoryObjectType type,
            String folderName, int options) throws PersistenceException {
        return repositoryFactoryFromProvider.getObjectFromFolder(project, type, folderName, options);
    }

    public List<ILockBean> getAllRemoteLocks() {
        return repositoryFactoryFromProvider.getAllRemoteLocks();
    }

    @Override
    public void updateEmfProjectContent(org.talend.core.model.properties.Project project) {
        emfProjectContentMap.put(project.getTechnicalLabel(), project);
    }

    @Override
    public org.talend.core.model.properties.Project getEmfProjectContent(String technicalLabel) throws PersistenceException {
        org.talend.core.model.properties.Project emfProject = emfProjectContentMap.get(technicalLabel);
        if (emfProject != null && emfProject.eResource() == null) {
            IProject iProject = ResourceUtils.getProject(emfProject.getTechnicalLabel());
            boolean isAvoidUnloadResource = repositoryFactoryFromProvider.getResourceManager().isAvoidUnloadResource();
            repositoryFactoryFromProvider.getResourceManager().setAvoidUnloadResource(true);
            emfProject = repositoryFactoryFromProvider.getResourceManager().loadProject(iProject);
            repositoryFactoryFromProvider.getResourceManager().setAvoidUnloadResource(isAvoidUnloadResource);
            updateEmfProjectContent(emfProject);
        }
        return emfProject;
    }

    @Override
    public byte[] getReferenceSettingContent(Project project, String branch) throws PersistenceException {
        return repositoryFactoryFromProvider.getReferenceSettingContent(project, branch);
    }

    public boolean isRepositoryBusy() {
        return repositoryFactoryFromProvider.isRepositoryBusy();
    }

    public RepositoryWorkUnit getWorkUnitInProgress() {
        return repositoryFactoryFromProvider.getWorkUnitInProgress();
    }
}
