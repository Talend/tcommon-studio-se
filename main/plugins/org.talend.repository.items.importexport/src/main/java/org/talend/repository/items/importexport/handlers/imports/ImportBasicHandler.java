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
package org.talend.repository.items.importexport.handlers.imports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Priority;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.runtime.utils.io.FileCopyUtils;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.context.Context;
import org.talend.core.context.RepositoryContext;
import org.talend.core.model.metadata.builder.connection.Connection;
import org.talend.core.model.metadata.builder.connection.ConnectionPackage;
import org.talend.core.model.migration.IMigrationToolService;
import org.talend.core.model.properties.BusinessProcessItem;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.ConnectionItem;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.FileItem;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ItemState;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.LinkDocumentationItem;
import org.talend.core.model.properties.LinkType;
import org.talend.core.model.properties.MigrationTask;
import org.talend.core.model.properties.NotationHolder;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Project;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.ReferenceFileItem;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SnippetItem;
import org.talend.core.model.properties.User;
import org.talend.core.model.relationship.RelationshipItemBuilder;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.FakePropertyImpl;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.model.utils.MigrationUtil;
import org.talend.core.repository.constants.FileConstants;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.utils.ProjectDataJsonProvider;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.runtime.services.IGenericDBService;
import org.talend.core.runtime.services.IGenericWizardService;
import org.talend.core.utils.WorkspaceUtils;
import org.talend.designer.business.model.business.BusinessPackage;
import org.talend.designer.business.model.business.BusinessProcess;
import org.talend.designer.core.model.utils.emf.talendfile.ProcessType;
import org.talend.designer.core.model.utils.emf.talendfile.TalendFilePackage;
import org.talend.designer.joblet.model.JobletPackage;
import org.talend.designer.joblet.model.JobletProcess;
import org.talend.model.emf.CwmResource;
import org.talend.repository.ProjectManager;
import org.talend.repository.RepositoryWorkUnit;
import org.talend.repository.items.importexport.handlers.HandlerUtil;
import org.talend.repository.items.importexport.handlers.cache.RepositoryObjectCache;
import org.talend.repository.items.importexport.handlers.model.ImportItem;
import org.talend.repository.items.importexport.handlers.model.ImportItem.State;
import org.talend.repository.items.importexport.i18n.Messages;
import org.talend.repository.items.importexport.manager.ResourcesManager;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ImportBasicHandler extends AbstractImportExecutableHandler {

    /**
     * set by extension point, will be the base path which relative to import project.
     * 
     * for example, for job designer, will be "process"; for xml file connections, will be "metadata/fileXml"
     */
    protected final Set<String> checkedBasePathes = new HashSet<String>();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement
     * , java.lang.String, java.lang.Object)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        if (data != null && data instanceof Map) {
            Map parametersMap = (Map) data;
            Object object = parametersMap.get(IImportConstants.PARAM_PATH);
            if (object != null) {
                String[] pathes = object.toString().split(IImportConstants.SEP_COMMA);
                for (String path : pathes) {
                    if (StringUtils.isNotEmpty(path)) {
                        path = path.trim();
                        if (StringUtils.isNotEmpty(path)) {
                            checkedBasePathes.add(path);
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * check the file is valid to import or not.
     */
    protected boolean isValidFile(IPath path) {
        return ImportCacheHelper.getInstance().getXmiResourceManager().isPropertyFile(path.lastSegment());
    }

    /**
     * Check the path is valid to import or ignore.
     */
    @Override
    public boolean valid(ResourcesManager resManager, IPath path) {
        boolean valid = isValidFile(path);
        if (valid) {
            IPath projectFilePath = HandlerUtil.getValidProjectFilePath(resManager, path);
            if (projectFilePath != null) {
                // remove the last segments "talend.project"
                IPath projectRootPath = projectFilePath.removeLastSegments(1);
                // relative to import project
                IPath relativePath = path.makeRelativeTo(projectRootPath);
                return validRelativePath(relativePath);
            } else {
                valid = false;
            }

        }
        return valid;
    }

    /**
     * 
     * Check the relative path (relative import project).
     */
    protected boolean validRelativePath(IPath relativePath) {
        for (String baseFolder : checkedBasePathes) {
            if (StringUtils.isNotEmpty(baseFolder)) {
                IPath typeFolder = new Path(baseFolder);
                // the relativePath is under the base folder.
                if (typeFolder.isPrefixOf(relativePath)) {
                    return true;
                }
            }
        }
        //
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#valid(org.talend.repository.items
     * .importexport.handlers.model.ImportItem)
     */
    @Override
    public boolean valid(ImportItem importItem) {
        return importItem != null && importItem.getItem() != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#createImportItem(org.eclipse.
     * core. runtime.IProgressMonitor,
     * org.talend.repository.items.importexport.ui.wizard.imports.managers.ResourcesManager,
     * org.eclipse.core.runtime.IPath, boolean, java.util.List)
     */
    @Override
    public ImportItem createImportItem(IProgressMonitor progressMonitor, ResourcesManager resManager, IPath resourcePath,
            boolean overwrite, List<ImportItem> existeditems) throws Exception {

        ImportItem importItem = computeImportItem(resManager, resourcePath);
        if (progressMonitor.isCanceled()) {
            return null;
        }
        if (importItem != null && importItem.getProperty() != null) {
            if (checkItem(resManager, importItem, overwrite)) {
                if (progressMonitor.isCanceled()) {
                    return null;
                }
                checkAndSetProject(resManager, importItem);
            }
            // set the import handler
            importItem.setImportHandler(this);
            return importItem;
        }
        return null;

    }

    @Override
    public ImportItem generateImportItem(IProgressMonitor monitor, ResourcesManager resManager, IPath resourcePath,
            boolean overwrite, List<ImportItem> existedImportItems) throws Exception {
        ImportItem importItem = computeImportItem(resManager, resourcePath);
        if (monitor.isCanceled()) {
            return null;
        }
        if (importItem != null && importItem.getProperty() != null) {
            // set the import handler
            importItem.setImportHandler(this);
            return importItem;
        }
        return null;
    }

    public ImportItem computeImportItem(ResourcesManager resManager, IPath path) {
        ImportItem importItem = new ImportItem(path);
        computeProperty(resManager, importItem);
        return importItem;
    }

    protected void computeProperty(ResourcesManager manager, ImportItem importItem) {
        Resource resource = loadResource(manager, importItem);
        if (resource != null) {
            importItem.setProperty((Property) EcoreUtil.getObjectByType(resource.getContents(),
                    PropertiesPackage.eINSTANCE.getProperty()));
        } else {
            ImportCacheHelper
                    .getInstance()
                    .getImportErrors()
                    .add(Messages.getString("ImportBasicHandler_LoadEMFResourceError", importItem.getPath().lastSegment(),
                            HandlerUtil.getValidItemRelativePath(manager, importItem.getPath())));
            log.error(Messages.getString("ImportBasicHandler_ErrorCreateEmfResource") + " - " + HandlerUtil.getValidItemRelativePath(manager, importItem.getPath())); //$NON-NLS-1$
        }
    }

    /**
     * mzhao load resource with item record.
     * 
     * @param manager
     * @param importItem
     * @param resource
     * @return
     */
    protected Resource loadResource(ResourcesManager manager, ImportItem importItem) {
        try {
            return HandlerUtil.loadResource(manager, importItem);
        } catch (Exception e) {
            // ignore, must be one invalid or unknown item
        }
        return null;
    }

    protected Resource createResource(ImportItem importItem, IPath path, boolean byteArrayResource) throws FileNotFoundException {
        Resource resource;
        if (byteArrayResource) {
            resource = HandlerUtil.createResource(importItem, path, true);
        } else {
            String[] exts = getResourceNeededExtensions();
            if (ArrayUtils.contains(exts, path.getFileExtension())) {
                ResourceSet resourceSet = importItem.getResourceSet();
                final URI pathUri = HandlerUtil.getURI(path);
                resource = createItemResource(pathUri);
                resourceSet.getResources().add(resource);
            } else {
                resource = HandlerUtil.createResource(importItem, path, false);
            }
        }
        return resource;
    }

    /**
     * 
     * mzhao get extensions needed to create a resource.
     * 
     * @return
     */
    protected String[] getResourceNeededExtensions() {
        return new String[] { FileConstants.ITEM_EXTENSION };
    }

    protected Resource createItemResource(URI pathUri) {
        // related to the ImportItemUtil.createResource
        return new CwmResource(pathUri);
    }

    /**
     * it's same item or not.
     */
    public boolean isSame(ImportItem importItem1, ImportItem importItem2) {
        return StringUtils.equals(importItem1.getProperty().getId(), importItem2.getProperty().getId())
                && StringUtils.equals(importItem1.getProperty().getVersion(), importItem2.getProperty().getVersion());
    }
    
    public String getPropertyLabel(String name) {
        String label = name;
        for (String toReplace : RepositoryConstants.ITEM_FORBIDDEN_IN_LABEL) {
            label = label.replace(toReplace, "_"); //$NON-NLS-1$
        }
        return label;
    }

    /**
     * 
     * check the item is valid or not。
     */
    public boolean checkItem(ResourcesManager resManager, ImportItem importItem, boolean overwrite) {
        try {
            Item item = importItem.getItem();
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);
            if (itemType == null) {
                importItem.addError(Messages.getString("AbstractImportHandler_unsupportItem")); //$NON-NLS-1$
                return false; // can't import this item.
            }
            if (item.getState() == null) {
                importItem.addError(Messages.getString("AbstractImportHandler_unsupportItem"));//$NON-NLS-1$
                return false;
            }

            final RepositoryObjectCache repObjectcache = ImportCacheHelper.getInstance().getRepObjectcache();

            IRepositoryViewObject itemWithSameIdObj = null;
            IRepositoryViewObject itemWithSameNameObj = null;

            List<ERepositoryObjectType> allTypesOfProcess = ERepositoryObjectType.getAllTypesOfProcess();
            if (allTypesOfProcess.contains(itemType)) {
                for (ERepositoryObjectType curProcessType : allTypesOfProcess) {
                    repObjectcache.initialize(curProcessType);
                }
            } else {
                repObjectcache.initialize(itemType);
            }

            boolean isSameRepositoryType = true;
            boolean bothProcessType = false;
            org.talend.core.model.general.Project currentProject = ProjectManager.getInstance().getCurrentProject();
            Map<String, List<IRepositoryViewObject>> nameCache = repObjectcache.getNameItemChache();
            final Property property = importItem.getProperty();
            List<IRepositoryViewObject> nameItems = nameCache.keySet().stream()
                    .filter(name -> name.equalsIgnoreCase(property.getLabel())).flatMap(key -> nameCache.get(key).stream())
                    .collect(Collectors.toList());

            if (nameItems != null) {
                for (IRepositoryViewObject current : nameItems) {
                    boolean isInCurrentProject = ProjectManager.getInstance().isInMainProject(currentProject,
                            current.getProperty());
                    if (isInCurrentProject && isSameName(importItem, current)) {
                        itemWithSameNameObj = current;
                        ERepositoryObjectType repType = current.getRepositoryObjectType();
                        IGenericDBService dbService = null;
                        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
                            dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(IGenericDBService.class);
                        }
                        if (dbService != null && dbService.getExtraTypes().contains(repType)) {
                            repType = dbService.getExtraDBType(repType);
                        }
                        ERepositoryObjectType importType = importItem.getRepositoryType();
                        bothProcessType = allTypesOfProcess.contains(repType) && allTypesOfProcess.contains(importType);
                        isSameRepositoryType = isSameRepType(repType, importType);
                        if (!isSameRepositoryType && !bothProcessType) {
                        	// no need to keep track of item with same name if the type is different
                        	itemWithSameNameObj = null;
                        }
                    }
                }
            }
            Map<String, List<IRepositoryViewObject>> idCache = repObjectcache.getIdItemChache();
            List<IRepositoryViewObject> idItems = idCache.get(property.getId());
            if (idItems != null) {
                for (IRepositoryViewObject current : idItems) {
                    if (property.getId() != null && property.getId().equals(current.getId())) {
                        itemWithSameIdObj = current;
                    }
                }
            }
            boolean nameAvailable = itemWithSameNameObj == null;
            boolean idAvailable = itemWithSameIdObj == null;

            if (nameAvailable) {
                if (!idAvailable) {
                    // same id but different name,no need to care overwrite cause the item will be considered as a
                    // different one,see bug 20445
                    importItem.setState(State.ID_EXISTED);
                    importItem.setExistingItemWithSameId(itemWithSameIdObj);
                    // TDI-29125:remove the error here since it is not a real error and it blocked show the item ,keep
                    // same as before
                    // if (!overwrite) {
                    //                        itemRecord.addError(Messages.getString("AbstractImportHandler_nameUsed")); //$NON-NLS-1$
                    // }
                }
            } else {
                if (idAvailable) {
                    // same name but different id
                    importItem.setState(State.NAME_EXISTED);

                    if (overwrite && isSameRepositoryType) {
                        // if anything system, don't replace the source item if same name.
                        // if not from system, can overwrite.
                        importItem.setExistingItemWithSameId(itemWithSameNameObj);
                        importItem.setExistingItemWithSameName(itemWithSameNameObj);
                        // TDI-21399,TDI-21401
                        // if item is locked, cannot overwrite
                        if (itemWithSameNameObj != null) {
                            ERepositoryStatus status = itemWithSameNameObj.getRepositoryStatus();
                            if (status == ERepositoryStatus.LOCK_BY_OTHER || status == ERepositoryStatus.LOCK_BY_USER) {
                                importItem.addError(Messages.getString("AbstractImportHandler_itemLocked")); //$NON-NLS-1$
                            }
                        }
                    }
                } else {
                    boolean isSameItem = StringUtils.equals(itemWithSameNameObj.getId(), itemWithSameIdObj.getId());
                    if (isSameItem) {
                        importItem.setState(State.NAME_AND_ID_EXISTED);
                    } else {
                        importItem.setState(State.NAME_AND_ID_EXISTED_BOTH);
                    }
                    importItem.setExistingItemWithSameId(itemWithSameIdObj);
                    importItem.setExistingItemWithSameName(itemWithSameNameObj);
                }
                if (isSameRepositoryType) {
                    if (!overwrite) {
                        importItem.addError(Messages.getString("AbstractImportHandler_nameUsed")); //$NON-NLS-1$
                    }
                } else {
                	if(bothProcessType){
                		importItem.addError(Messages.getString("AbstractImportHandler_nameUsed.differentRepositoryType")); //$NON-NLS-1$
                	}
                }
            }

            if (overwrite
                    && (importItem.getState() == State.NAME_AND_ID_EXISTED || importItem.getState() == State.NAME_AND_ID_EXISTED_BOTH)
                    && isSameRepositoryType) {
                // if item is locked, cannot overwrite
                if (checkIfLocked(importItem)) {
                    importItem.addError(Messages.getString("AbstractImportHandler_itemLocked")); //$NON-NLS-1$
                }

            }

        } catch (PersistenceException e) {
            log.error("Error when checking item :" + importItem.getPath(), e); //$NON-NLS-1$
        }
        return true;
    }

    /**
     * 
     * item with same name.
     */
    protected boolean isSameName(ImportItem importItem, IRepositoryViewObject repObject) {
        final Property property = importItem.getProperty();
        String currentLabel = null;
        if (repObject instanceof RepositoryViewObject) {
            RepositoryViewObject object = (RepositoryViewObject) repObject;
            currentLabel = object.getTechnicalLabel();
        } else {
            currentLabel = repObject.getLabel();
        }
        boolean isSameLabel = property.getLabel().equalsIgnoreCase(currentLabel);
        boolean nameIsOK = WorkspaceUtils.checkNameIsOK(property.getLabel());
        if(!nameIsOK && !isSameLabel){
            isSameLabel = getPropertyLabel(property.getLabel()).equalsIgnoreCase(currentLabel);
        }
        if ((property.getLabel() != null && isSameLabel) // same label
        ) {
            ERepositoryObjectType importType = importItem.getRepositoryType();
            ERepositoryObjectType repType = repObject.getRepositoryObjectType();
            IGenericDBService dbService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericDBService.class)) {
                dbService = (IGenericDBService) GlobalServiceRegister.getDefault().getService(IGenericDBService.class);
            }
            if (dbService != null && dbService.getExtraTypes().contains(repType)) {
                repType = dbService.getExtraDBType(repType);
            }
            // if support mult name
            if (importType != null && importType.equals(repType) && importType.isAllowMultiName()) {
                String importPath = importItem.getProperty().getItem().getState().getPath();
                String repPath = repObject.getPath();
                // Check the path, if same path, should return true
                if (importPath == null && repPath == null || importPath != null && importPath.equals(repPath)) {
                    return true;
                }
                return false; // in different path. should be not same name, so return false.
            }
            return true;
        }
        return false;
    }

    /**
     * Check if both type are the same type.
     */
    protected boolean isSameRepType(ERepositoryObjectType type1, ERepositoryObjectType type2) {
        if (type1 == null || type2 == null) {
            return false;
        }
        if (type1 == type2) {
            return true;
        }
        IGenericWizardService wizardService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IGenericWizardService.class)) {
            wizardService = (IGenericWizardService) GlobalServiceRegister.getDefault().getService(IGenericWizardService.class);
        }
        if (wizardService != null) {
            if (wizardService.isGenericType(type1) && !wizardService.isGenericType(type2)) {
                return type1.equals(wizardService.getNewRepType(type2.getType()));
            }
            if (wizardService.isGenericType(type2) && !wizardService.isGenericType(type1)) {
                return type2.equals(wizardService.getNewRepType(type1.getType()));
            }
        }
        return false;
    }

    /**
     * DOC hcw Comment method "checkIfLocked".
     * 
     * @param importItem
     * @return
     * @throws PersistenceException
     */
    protected boolean checkIfLocked(ImportItem importItem) throws PersistenceException {
        final RepositoryObjectCache repObjectcache = ImportCacheHelper.getInstance().getRepObjectcache();
        Boolean lockState = repObjectcache.getItemLockState(importItem);
        if (lockState != null) {
            return lockState.booleanValue();
        }

        List<IRepositoryViewObject> list = repObjectcache.findObjectsByItem(importItem);

        for (IRepositoryViewObject obj : list) {
            ERepositoryStatus status = obj.getRepositoryStatus();
            if (status == ERepositoryStatus.LOCK_BY_OTHER || status == ERepositoryStatus.LOCK_BY_USER) {
                importItem.setLocked(true);
                repObjectcache.setItemLockState(importItem, true);
                return true;
            }
        }

        repObjectcache.setItemLockState(importItem, false);
        return false;
    }

    public void checkAndSetProject(ResourcesManager resManager, ImportItem importItem) {
        InternalEObject author = (InternalEObject) importItem.getProperty().getAuthor();
        URI uri = null;
        if (author != null) {
            uri = author.eProxyURI();
        }

        IPath projectFilePath = HandlerUtil.getValidProjectFilePath(resManager, importItem.getPath());
        if (projectFilePath != null) {
            Project project = computeProject(resManager, importItem, projectFilePath);
            if (checkProject(project, importItem)) {
                // set item project into record.
                importItem.setItemProject(project);
                // we can try to import item
                // and we will try to resolve user
                if (uri != null) {
                    User user = (User) project.eResource().getEObject(uri.fragment());
                    importItem.getProperty().setAuthor(user);
                }
            }
        } else {
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(importItem.getItem());
            if (itemType.isDIItemType()) {
                importItem.addError(Messages.getString("AbstractImportHandler_projectNotFound")); //$NON-NLS-1$
            }
        }

    }

    protected Project computeProject(ResourcesManager manager, ImportItem importItem, IPath path) {
        InputStream stream = null;
        Map<IPath, Project> pathWithProjects = ImportCacheHelper.getInstance().getPathWithProjects();
        try {
            if (!pathWithProjects.containsKey(path)) {
                stream = manager.getStream(path, importItem);
                Resource resource = createResource(importItem, path, false);
                resource.load(stream, null);
                Project project = (Project) EcoreUtil.getObjectByType(resource.getContents(),
                        PropertiesPackage.eINSTANCE.getProject());
                IPath projectRootPath = path.removeLastSegments(1);
                ProjectDataJsonProvider.loadProjectData(project, projectRootPath, manager);
                // EmfHelper.loadResource(resource, stream, null);
                pathWithProjects.put(path, project);
            }
            return pathWithProjects.get(path);
        } catch (IOException | PersistenceException e) {
            // ignore
            if (Platform.inDebugMode()) {
                ExceptionHandler.process(e);
            }
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return null;
    }

    protected boolean checkProject(Project project, ImportItem importItem) {
        boolean checkProject = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMigrationToolService.class)) {
            IMigrationToolService migrationService = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            Set<Project> updatedProjects = ImportCacheHelper.getInstance().getUpdatedProjects();
            // update the old project which hasn't adapted to the new migration task system.
            if (!updatedProjects.contains(project)) {
                migrationService.updateMigrationSystem(project, false);
                updatedProjects.add(project);
            }

            Project currentProject = ProjectManager.getInstance().getCurrentProject().getEmfProject();

            if (project != null) {
                if (checkMigrationTasks(currentProject, project, importItem)) {
                    checkProject = true;
                }
            } else {
                importItem.addError(Messages.getString("AbstractImportHandler_projectNotFound")); //$NON-NLS-1$
            }
        }

        return checkProject;
    }

    /**
     * DOC ycbai Comment method "checkMigrationTasks".
     * 
     * @param currentProject
     * @param importedProject
     * @param importItem
     * @return
     */
    protected boolean checkMigrationTasks(Project currentProject, Project importedProject, ImportItem importItem) {

        Map<String, Boolean> migrationTasksStatusPerProject = ImportCacheHelper.getInstance().getMigrationTasksStatusPerProject();
        Map<String, List<MigrationTask>> migrationTasksToApplyPerProject = ImportCacheHelper.getInstance()
                .getMigrationTasksToApplyPerProject();

        String importedProjectLabel = importedProject.getTechnicalLabel();
        if (migrationTasksStatusPerProject.containsKey(importedProjectLabel)) {
            if (migrationTasksStatusPerProject.get(importedProjectLabel)) {
                importItem.setMigrationTasksToApply(migrationTasksToApplyPerProject.get(importedProjectLabel));
                return true;
            } else {
                String message = Messages.getString("AbstractImportHandler_cannotImportMessage", importedProjectLabel); //$NON-NLS-1$
                importItem.addError(message);
                return false;
            }
        }

        boolean canApplyMigration = false;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMigrationToolService.class)) {
            IMigrationToolService migrationService = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            List<MigrationTask> migrationTasks = new ArrayList<MigrationTask>();
            if (migrationService.checkMigrationTasks(importedProject)) {
                List<MigrationTask> currentProjectMigrationTasks = new ArrayList<MigrationTask>(currentProject.getMigrationTask());
                List<MigrationTask> importedProjectMigrationTasks = new ArrayList<MigrationTask>(
                        importedProject.getMigrationTask());
                MigrationUtil.removeMigrationTaskByIds(importedProjectMigrationTasks, getOptionnalMigrationTasks());
                MigrationUtil.removeMigrationTaskById(importedProjectMigrationTasks,
                        "org.talend.repository.model.migration.AutoUpdateRelationsMigrationTask"); //$NON-NLS-1$
                MigrationUtil.removeMigrationTaskByMigrationTasks(currentProjectMigrationTasks, importedProjectMigrationTasks);
                importItem.setMigrationTasksToApply(currentProjectMigrationTasks);
                migrationTasks = currentProjectMigrationTasks;
                canApplyMigration = true;
                migrationTasksStatusPerProject.put(importedProjectLabel, true);
            } else {
                String message = Messages.getString("AbstractImportHandler_cannotImportMessage", importedProjectLabel); //$NON-NLS-1$
                importItem.addError(message);
                log.info("'" + importItem.getItemName() + "' " + message); //$NON-NLS-1$ //$NON-NLS-2$
                migrationTasksStatusPerProject.put(importedProjectLabel, false);
            }
            migrationTasksToApplyPerProject.put(importedProjectLabel, migrationTasks);
        }
        return canApplyMigration;
    }

    private List<String> getOptionnalMigrationTasks() {
        List<String> toReturn = new ArrayList<String>();

        toReturn.add("org.talend.repository.documentation.migrationtask.generatejobdocmigrationtask"); //$NON-NLS-1$
        // old task, added for an old version of TOS, not used anymore.
        toReturn.add("org.talend.repository.migration.ReplaceOldContextScriptCodeMigrationTask"); //$NON-NLS-1$
        toReturn.add("org.talend.designer.core.model.process.migration.SynchronizeSchemaOnlyForPerlDemo"); //$NON-NLS-1$
        toReturn.add("org.talend.repository.model.migration.RenametFSFilterRow"); //$NON-NLS-1$        
        return toReturn;
    }

    protected void logError(Exception e) {
        ImportCacheHelper.getInstance().setImportingError(true);
        ExceptionHandler.process(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#importItemRecord(org.eclipse.core
     * .runtime .IProgressMonitor, org.talend.repository.items.importexport.manager.ResourcesManager,
     * org.talend.repository.items.importexport.handlers.model.ImportItem, boolean, org.eclipse.core.runtime.IPath,
     * java.util.Set, java.util.Set)
     */
    @Override
    public void doImport(IProgressMonitor monitor, ResourcesManager resManager, ImportItem selectedImportItem, boolean overwrite,
            IPath destinationPath, Set<String> overwriteDeletedItems, Set<String> idDeletedBeforeImport) throws Exception {
        monitor.subTask(Messages.getString("AbstractImportHandler_importing", selectedImportItem.getItemName())); //$NON-NLS-1$
        resolveItem(resManager, selectedImportItem);
        if (!selectedImportItem.isValid()) {
            return;
        }
        doImportItem(monitor, resManager, selectedImportItem, overwrite, destinationPath, overwriteDeletedItems,
                idDeletedBeforeImport);

        String label = selectedImportItem.getLabel();
        TimeMeasure.step("importItemRecords", "Import item: " + label); //$NON-NLS-1$ //$NON-NLS-2$

        applyMigrationTasks(selectedImportItem, monitor);
        TimeMeasure.step("importItemRecords", "applyMigrationTasks: " + label); //$NON-NLS-1$//$NON-NLS-2$
    }

    protected void doImportItem(IProgressMonitor monitor, ResourcesManager resManager, ImportItem selectedImportItem,
            boolean overwrite, IPath destinationPath, Set<String> overwriteDeletedItems, Set<String> idDeletedBeforeImport) {
        final Item item = selectedImportItem.getItem();
        if (item != null) {
            final ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
            ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(item);

            IPath path = checkAndCreatePath(selectedImportItem, destinationPath);

            try {
                Item tmpItem = item;

                // delete existing items before importing, this should be done
                // once for a different id
                String id = selectedImportItem.getProperty().getId();

                IRepositoryViewObject lastVersion = selectedImportItem.getExistingItemWithSameId();
                if (selectedImportItem.getState() == State.NAME_EXISTED
                        || selectedImportItem.getState() == State.NAME_AND_ID_EXISTED_BOTH) {
                    lastVersion = selectedImportItem.getExistingItemWithSameName();
                }
                if (lastVersion != null
                        && overwrite
                        && !selectedImportItem.isLocked()
                        && (selectedImportItem.getState() == State.ID_EXISTED
                                || selectedImportItem.getState() == State.NAME_EXISTED
                                || selectedImportItem.getState() == State.NAME_AND_ID_EXISTED || selectedImportItem.getState() == State.NAME_AND_ID_EXISTED_BOTH)
                        && !ImportCacheHelper.getInstance().getDeletedItems().contains(id)) {

                    if (overwriteDeletedItems != null && !overwriteDeletedItems.contains(id)) { // bug 10520.
                        ERepositoryStatus status = repFactory.getStatus(lastVersion);
                        if (status == ERepositoryStatus.DELETED) {
                            repFactory.restoreObject(lastVersion, path); // restore first.
                        }
                        overwriteDeletedItems.add(id);
                    }

                    /* only delete when name exsit rather than id exist */
                    if (selectedImportItem.getState().equals(ImportItem.State.NAME_EXISTED)
                            || selectedImportItem.getState().equals(ImportItem.State.NAME_AND_ID_EXISTED)
                            || selectedImportItem.getState().equals(ImportItem.State.NAME_AND_ID_EXISTED_BOTH)) {
                        final IRepositoryViewObject lastVersionBackup = lastVersion;
                        if (idDeletedBeforeImport != null && !idDeletedBeforeImport.contains(id)) {
                            // TDI-19535 (check if exists, delete all items with same id)
                            final List<IRepositoryViewObject> allVersionToDelete = repFactory.getAllVersion(ProjectManager
                                    .getInstance().getCurrentProject(), lastVersionBackup.getId(), false);
                            String importingLabel = selectedImportItem.getProperty().getLabel();
                            String existLabel = lastVersionBackup.getProperty().getLabel();
                            final boolean isDeleteOnRemote = isNeedDeleteOnRemote(importingLabel, existLabel);
                            RepositoryWorkUnit repositoryWorkUnit = new RepositoryWorkUnit(
                                    Messages.getString("ImportExportHandlersManager_deletingItemsMessage")) {

                                @Override
                                public void run() throws PersistenceException {
                                    for (IRepositoryViewObject currentVersion : allVersionToDelete) {
                                        repFactory.forceDeleteObjectPhysical(lastVersionBackup, currentVersion.getVersion(),
                                                isDeleteOnRemote);
                                    }
                                }
                            };
                            if (isDeleteOnRemote) {
                                repositoryWorkUnit.setForceTransaction(true);
                            } else {
                                repositoryWorkUnit.setForceTransaction(false);
                            }

                            repositoryWorkUnit.setRefreshRepository(false);
                            ProxyRepositoryFactory.getInstance().executeRepositoryWorkUnit(repositoryWorkUnit);
                            idDeletedBeforeImport.add(id);
                        }
                    }
                    lastVersion = null;
                }

                User author = selectedImportItem.getProperty().getAuthor();
                if (author != null) {
                    if (!repFactory.setAuthorByLogin(tmpItem, author.getLogin())) {
                        // author will be the logged user in create method
                        tmpItem.getProperty().setAuthor(null);
                    }
                }

                beforeCreatingItem(selectedImportItem);

                final RepositoryObjectCache repObjectcache = ImportCacheHelper.getInstance().getRepObjectcache();
                if (lastVersion == null || selectedImportItem.getState().equals(ImportItem.State.ID_EXISTED)
                        || selectedImportItem.getState().equals(ImportItem.State.NAME_AND_ID_EXISTED_BOTH)) {
                    repFactory.create(tmpItem, path, true);

                    afterCreatedItem(resManager, selectedImportItem);

                    selectedImportItem.setImported(true);

                } else if (VersionUtils.compareTo(lastVersion.getProperty().getVersion(), tmpItem.getProperty().getVersion()) < 0) {
                    repFactory.forceCreate(tmpItem, path);

                    afterForceCreatedItem(resManager, selectedImportItem);

                    selectedImportItem.setImported(true);
                } else {
                    PersistenceException e = new PersistenceException(Messages.getString(
                            "AbstractImportHandler_persistenceException", tmpItem.getProperty())); //$NON-NLS-1$
                    selectedImportItem.addError(e.getMessage());
                    logError(e);
                }
                if (selectedImportItem.isImported()) {
                    selectedImportItem.setImportPath(path.toPortableString());
                    selectedImportItem.setRepositoryType(itemType);
                    selectedImportItem.setItemId(selectedImportItem.getProperty().getId());
                    selectedImportItem.setItemVersion(selectedImportItem.getProperty().getVersion());

                    repObjectcache.addToCache(tmpItem);
                }

                if (tmpItem.getState() != null && itemType != null) {
                    final Set<String> folders = ImportCacheHelper.getInstance().getRestoreFolder().getFolders(itemType);
                    if (folders != null) {
                        for (String folderPath : folders) {
                            if (folderPath != null && folderPath.equals(path.toString())) {
                                FolderItem folderItem = repFactory.getFolderItem(
                                        ProjectManager.getInstance().getCurrentProject(), itemType, path);
                                if (folderItem != null) {
                                    folderItem.getState().setDeleted(false);

                                    while (!(folderItem.getParent() instanceof Project)) {
                                        folderItem = (FolderItem) folderItem.getParent();
                                        if (folderItem.getType() == FolderType.SYSTEM_FOLDER_LITERAL) {
                                            break;
                                        }
                                        folderItem.getState().setDeleted(false);
                                    }

                                }
                                break;
                            }
                        }
                    }

                }

            } catch (Exception e) {
                selectedImportItem.addError(selectedImportItem.getItemName() + ";" + e.getMessage() + ";" + path);//$NON-NLS-1$
                logError(e);
            }
        }
    }

    private boolean isNeedDeleteOnRemote(String importingLabel, String existLabel) {
        if (importingLabel != null && importingLabel.equalsIgnoreCase(importingLabel) && !importingLabel.equals(existLabel)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * DOC ggu Comment method "checkAndCreatePath". if the path is not existed, will create.
     * 
     * @param selectedImportItem
     * @param destinationPath
     * @param contentType
     * @return
     */
    protected IPath checkAndCreatePath(ImportItem selectedImportItem, IPath destinationPath) {
        final ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        final Item item = selectedImportItem.getItem();
        final ERepositoryObjectType curItemType = ERepositoryObjectType.getItemType(item);

        IPath path = new Path(item.getState().getPath());
        if (destinationPath != null && curItemType.isResouce()) {
            IPath typePath = new Path(curItemType.getFolder());
            // only process the same type of items.
            if (typePath.isPrefixOf(destinationPath)) {
                IPath newDesPath = destinationPath.makeRelativeTo(typePath);
                path = newDesPath.append(path);
            }
        }

        try {
            // TDI-29841 , if win, need try to find the existed folder item which is case insensitive.
            FolderItem folderItem = getFolderItem(selectedImportItem, curItemType, path);
            if (folderItem != null) {
                // if the item is not deleted, will restore folders.
                if (!selectedImportItem.getItem().getState().isDeleted()) {
                    restoreFolderItem(folderItem); // restore the parent folders from recycle bin.
                }

                String pathStr = folderItem.getState().getPath();
                if (pathStr == null) {
                    pathStr = ""; //$NON-NLS-1$
                }
                /*
                 * FIXME,TUP-2422, temp, won't reset the item path for DQ items only.
                 * 
                 * In fact, need unify the type of folder with FOLDER/SYSTEM_FOLDER/STABLE_SYSTEM_FOLDER. So means, if
                 * the ERepositoryObjectType is resource, the type should be STABLE_SYSTEM_FOLDER. Else it's "system"
                 * folder, will be SYSTEM_FOLDER, like "system" of routine. "Generic","Hiv","Generic/system",
                 * "Generic/UserDefined", etc for SQL Templates.
                 * 
                 * Also for DQ, the "TDQ_Libraries" and "TDQ_Libraries\Indicators" are STABLE_SYSTEM_FOLDER. but for the
                 * "TDQ_Libraries/Indicators/System Indicators" and "TDQ_Libraries/Indicators/User Defined Indicators",
                 * etc. should be SYSTEM_FOLDER.
                 */
                if (!curItemType.isDQItemType() || curItemType.isSharedType()) { // only for pure DQ items.

                    if (!path.isEmpty()) {
                        // if have path, reset the path, especially for win os with case insensitive.
                        path = new Path(pathStr).append(folderItem.getProperty().getLabel());
                    }// else{ // if root path "", keep it, and folderItem will be root node of type.
                }
            }
            repFactory.createParentFoldersRecursively(ProjectManager.getInstance().getCurrentProject(), curItemType, path, true);
        } catch (Exception e) {
            logError(e);
            path = new Path(""); //$NON-NLS-1$
        }
        return path;
    }

    private FolderItem getFolderItem(final ImportItem selectedImportItem, final ERepositoryObjectType curItemType, IPath path) {
        final ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
        FolderItem folderItem = repFactory.getFolderItem(ProjectManager.getInstance().getCurrentProject(), curItemType, path);
        if (folderItem == null) {
            // if this folder does not exists (and it's parents), it will check if the folder was originally
            // deleted in source project.
            // if yes, it will set back the delete status to the folder, to keep the same as the original
            // project when import.
            // Without this code, deleted folders of items imported will not be in the recycle bin after import.
            // delete status is set finally in the function checkDeletedFolders
            IPath curPath = path;
            EList deletedFoldersFromOriginalProject = selectedImportItem.getItemProject().getDeletedFolders();
            while (folderItem == null && !curPath.isEmpty() && !curPath.isRoot()) {
                if (deletedFoldersFromOriginalProject.contains(new Path(curItemType.getFolder()).append(
                        curPath.toPortableString()).toPortableString())) {
                    final Map<ERepositoryObjectType, Set<String>> foldersCreated = ImportCacheHelper.getInstance()
                            .getFoldersCreated();
                    if (!foldersCreated.containsKey(curItemType)) {
                        foldersCreated.put(curItemType, new HashSet<String>());
                    }
                    foldersCreated.get(curItemType).add(curPath.toPortableString());
                }
                if (curPath.segments().length > 0) {
                    curPath = curPath.removeLastSegments(1);
                    folderItem = repFactory.getFolderItem(ProjectManager.getInstance().getCurrentProject(), curItemType, curPath);
                }
            }
        }
        return folderItem;
    }

    /**
     * 
     * most like the RestoreAction for folder item.
     */
    private void restoreFolderItem(FolderItem folderItem) {
        ItemState state = folderItem.getState();
        if (state.isDeleted()) {
            state.setDeleted(false);
        }
        EObject parent = folderItem.getParent();
        if (parent instanceof FolderItem) {
            FolderItem parentFolder = (FolderItem) parent;
            restoreFolderItem(parentFolder);
        }
    }

    protected void beforeCreatingItem(ImportItem selectedImportItem) {
        // noting to do specially.
    }

    protected void afterCreatedItem(ResourcesManager resManager, ImportItem selectedImportItem) throws Exception {

        // connections from migrations (from 4.0.x or previous version) doesn't support reference or
        // screenshots
        // so no need to call this code.

        // It's needed to avoid to call the save method mainly just before or after the copy of the old
        // connection since it will

        boolean haveRef = copyReferenceFiles(resManager, selectedImportItem);
        if (haveRef) {
            ProxyRepositoryFactory repFactory = ProxyRepositoryFactory.getInstance();
            repFactory.save(selectedImportItem.getItem(), true);
        }

    }

    protected void afterForceCreatedItem(ResourcesManager resManager, ImportItem selectedImportItem) throws Exception {
        // nothing to do
    }

    protected void afterApplyMigrationTasks(ImportItem selectedImportItem) throws Exception {
        // do nothing by default
    }

    protected boolean copyReferenceFiles(ResourcesManager manager, ImportItem selectedImportItem) throws IOException {
        OutputStream os = null;
        InputStream is = null;

        boolean haveRef = false;
        Item tmpItem = selectedImportItem.getItem();
        List<ReferenceFileItem> refItems = tmpItem.getReferenceResources();
        URI propertyResourceURI = EcoreUtil.getURI(tmpItem.getProperty());
        for (ReferenceFileItem refItem : refItems) {
            haveRef = true;
            URI relativePlateformDestUri = getReferenceFileURI(propertyResourceURI, refItem);
            try {
                URL fileURL = FileLocator.toFileURL(new java.net.URL(
                        "platform:/resource" + relativePlateformDestUri.toPlatformString(true))); //$NON-NLS-1$
                os = new FileOutputStream(fileURL.getFile());
                is = manager.getStream(getReferenceItemPath(selectedImportItem.getPath(), refItem), selectedImportItem);
                FileCopyUtils.copyStreams(is, os);
            } finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }
        }
        return haveRef;
    }

    public void resolveItem(ResourcesManager manager, ImportItem importItem) {
        if (importItem.isResolved() || importItem.getProperty() instanceof FakePropertyImpl) {
            return;
        }

        InputStream stream = null;

        try {
            final Item item = importItem.getItem();
            boolean byteArray = (item instanceof FileItem);
            IPath itemPath = HandlerUtil.getItemPath(importItem.getPath(), item);
            IPath itemRelativePath = HandlerUtil.getValidItemRelativePath(manager, itemPath);
            // Set<IPath> paths = manager.getPaths();
            // check the item file
            if (!manager.isContainsPath(itemPath)) {
                importItem.addError(Messages.getString("ImportBasicHandler_MissingItemError", importItem.getItemName(),
                        itemPath.lastSegment(), itemRelativePath));
                log.error(importItem.getItemName()
                        + " " + Messages.getString("ImportBasicHandler_MissingItemFile") + " - " + itemRelativePath); //$NON-NLS-1$
                return;
            }
            stream = manager.getStream(itemPath, importItem);
            Resource resource = createResource(importItem, itemPath, byteArray);

            if (byteArray) {
                // TDI-24612
                // This part fixes a problem of import of routines from .tar.gz.
                // Seems either the Tar stream or emf got problems to read this.
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int i = 0;
                while ((i = stream.read(buf)) != -1) {
                    baos.write(buf, 0, i);
                }
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                resource.load(bais, null);
            } else {
                resource.load(stream, null);
            }

            for (ReferenceFileItem rfItem : (List<ReferenceFileItem>) item.getReferenceResources()) {
                itemPath = getReferenceItemPath(importItem.getPath(), rfItem);
                stream = manager.getStream(itemPath, importItem);
                Resource rfResource = createResource(importItem, itemPath, true);
                rfResource.load(stream, null);
            }

            Iterator<EObject> itRef = item.eCrossReferences().iterator();
            IPath parentPath = importItem.getPath().removeLastSegments(1);
            while (itRef.hasNext()) {
                EObject object = itRef.next();
                String linkedFile = EcoreUtil.getURI(object).toFileString();
                IPath linkedPath = parentPath.append(linkedFile);
                if (!manager.isContainsPath(linkedPath)) {
                    if (linkedFile != null && !linkedFile.equals(itemPath.lastSegment())
                            && linkedFile.endsWith(itemPath.getFileExtension())) {
                        if (object.eIsProxy()) {
                            // if original href of the item point to some missing item file
                            // and if we can get the original item file from the name, recover it, but add a warning
                            ((EObjectImpl) object).eSetProxyURI(URI.createFileURI(itemPath.lastSegment()));
                            log.warn(importItem.getItemName()
                                    + " " + Messages.getString("ImportBasicHandler_NotHrefCurrentItemFile") + " - " + itemRelativePath); //$NON-NLS-1$
                        }
                    }
                }
                EcoreUtil.resolve(object, resource);
            }
        } catch (IOException e) {
            // ignore
        } catch (Exception ex) {
            ExceptionHandler.process(ex, Priority.WARN);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        importItem.setResolved(true);
    }

    /**
     * 
     * cLi Comment method "resetItemReference".
     * 
     * resolve the encode some special character(bug 6252), maybe, It's not better to resolve this by manually.
     * 
     * such as, "[" is "%5B", "]" is "%5D", etc.
     */
    @SuppressWarnings("unchecked")
    private void resetItemReference(ImportItem importItem, Resource resource) {
        Item item = importItem.getItem();
        EList<EObject> contents = resource.getContents();
        /*
         * ignore job. no need, because it can't be allowed input special char for name.
         */
        if (item instanceof ProcessItem) {
            ((ProcessItem) item).setProcess((ProcessType) EcoreUtil.getObjectByType(contents,
                    TalendFilePackage.eINSTANCE.getProcessType()));
        } else
        /*
         * ignore joblet. no need, because it can't be allowed input special char for name.
         */
        if (item instanceof JobletProcessItem) {
            JobletProcessItem jobletProcessItem = (JobletProcessItem) item;

            jobletProcessItem.setJobletProcess((JobletProcess) EcoreUtil.getObjectByType(contents,
                    JobletPackage.eINSTANCE.getJobletProcess()));
            // jobletProcessItem
            // .setIcon((ByteArray) EcoreUtil.getObjectByType(contents, PropertiesPackage.eINSTANCE.getByteArray()));
        } else
        // connectionItem
        if (item instanceof ConnectionItem) {
            ((ConnectionItem) item).setConnection((Connection) EcoreUtil.getObjectByType(contents,
                    ConnectionPackage.eINSTANCE.getConnection()));
        } else
        // context
        if (item instanceof ContextItem) {
            EList contexts = ((ContextItem) item).getContext();
            contexts.clear();
            contexts.addAll(EcoreUtil.getObjectsByType(contents, TalendFilePackage.eINSTANCE.getContextType()));
        } else
        // file
        if (item instanceof FileItem) {
            /*
             * ignore routine, no need, because it can't be allowed input special char for name.
             */
            if (item instanceof RoutineItem) {
                return;
            }
            FileItem fileItem = (FileItem) item;
            fileItem.setContent((ByteArray) EcoreUtil.getObjectByType(contents, PropertiesPackage.eINSTANCE.getByteArray()));
        } else
        // snippet
        if (item instanceof SnippetItem) {
            EList variables = ((SnippetItem) item).getVariables();
            variables.clear();
            variables.addAll(EcoreUtil.getObjectsByType(contents, PropertiesPackage.eINSTANCE.getSnippetVariable()));
        } else
        // link doc
        if (item instanceof LinkDocumentationItem) {
            ((LinkDocumentationItem) item).setLink((LinkType) EcoreUtil.getObjectByType(contents,
                    PropertiesPackage.eINSTANCE.getLinkType()));
        } else
        // business
        if (item instanceof BusinessProcessItem) {
            BusinessProcessItem businessProcessItem = (BusinessProcessItem) item;

            businessProcessItem.setSemantic((BusinessProcess) EcoreUtil.getObjectByType(contents,
                    BusinessPackage.eINSTANCE.getBusinessProcess()));

            businessProcessItem.setNotationHolder((NotationHolder) EcoreUtil.getObjectByType(contents,
                    PropertiesPackage.eINSTANCE.getNotationHolder()));
        }

    }

    /**
     * DOC ycbai Comment method "applyMigrationTasks".
     * 
     * @param importItem
     * @param monitor
     */
    protected void applyMigrationTasks(ImportItem importItem, IProgressMonitor monitor) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IMigrationToolService.class)) {
            IMigrationToolService migrationService = (IMigrationToolService) GlobalServiceRegister.getDefault().getService(
                    IMigrationToolService.class);
            Context ctx = CoreRuntimePlugin.getInstance().getContext();
            RepositoryContext repositoryContext = (RepositoryContext) ctx.getProperty(Context.REPOSITORY_CONTEXT_KEY);
            org.talend.core.model.general.Project project = repositoryContext.getProject();
            ERepositoryObjectType repositoryType = importItem.getRepositoryType();
            Item item = null;
            try {
                List<IRepositoryViewObject> allVersion = ProxyRepositoryFactory.getInstance().getAllVersion(
                        ProjectManager.getInstance().getCurrentProject(), importItem.getItemId(), importItem.getImportPath(),
                        repositoryType);
                for (IRepositoryViewObject repositoryObject : allVersion) {
                    if (repositoryObject.getProperty().getVersion().equals(importItem.getItemVersion())) {
                        item = repositoryObject.getProperty().getItem();
                    }
                }
                if (item == null) {
                    return;
                }
                migrationService.executeMigrationTasksForImport(project, item, importItem.getMigrationTasksToApply(), monitor);
                importItem.setExistingItemWithSameId(null);
                // importItem.clear();
                importItem.setProperty(item.getProperty());

                afterApplyMigrationTasks(importItem);
            } catch (Exception e) {
                logError(e);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.items.importexport.handlers.imports.IImportItemsHandler#afterImportingItemRecords(org.talend
     * .repository.items.importexport.ui.wizard.imports.models.ItemRecord)
     */
    @Override
    public void afterImportingItems(IProgressMonitor monitor, ResourcesManager resManager, ImportItem importItem) {
        if (importItem == null) {
            return;
        }
        final ProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        IRepositoryViewObject object;
        try {
            Property property = importItem.getProperty();
            if (property == null) {
                object = factory.getSpecificVersion(importItem.getItemId(), importItem.getItemVersion(), true);
                property = object.getProperty();
            }
            RelationshipItemBuilder.getInstance().addOrUpdateItem(property.getItem(), true);
            // importItem.setProperty(null);
            // factory.unloadResources(property);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }

    }

    protected IPath getReferenceItemPath(IPath importItemPath, ReferenceFileItem rfItem) {
        return HandlerUtil.getReferenceItemPath(importItemPath, rfItem.getExtension());
    }

    protected URI getReferenceFileURI(URI propertyResourceURI, ReferenceFileItem refItem) {
        return propertyResourceURI.trimFileExtension().appendFileExtension(refItem.getExtension());
    }

}
