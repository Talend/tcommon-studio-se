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
package org.talend.core.repository.model;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.VersionUtils;
import org.talend.commons.utils.data.container.Container;
import org.talend.commons.utils.data.container.RootContainer;
import org.talend.commons.utils.time.TimeMeasure;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ICoreService;
import org.talend.core.IStatusPreferenceInitService;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.EParameterFieldType;
import org.talend.core.model.properties.ByteArray;
import org.talend.core.model.properties.FolderItem;
import org.talend.core.model.properties.FolderType;
import org.talend.core.model.properties.Information;
import org.talend.core.model.properties.InformationLevel;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.PropertiesFactory;
import org.talend.core.model.properties.PropertiesPackage;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.SQLPatternItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.Folder;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.repository.LockInfo;
import org.talend.core.model.repository.RepositoryViewObject;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.utils.XmiResourceManager;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.core.ui.branding.IBrandingService;
import org.talend.designer.core.model.utils.emf.component.ComponentFactory;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.ElementParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.NodeType;
import org.talend.designer.runprocess.IRunProcessService;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.RepositoryConstants;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public abstract class AbstractEMFRepositoryFactory extends AbstractRepositoryFactory implements IRepositoryFactory {

    protected ICoreService coreSerivce = (ICoreService) GlobalServiceRegister.getDefault().getService(ICoreService.class);

    public XmiResourceManager xmiResourceManager = new XmiResourceManager();

    /**
     * Generates the next id for serializable. If no serializable returns 0.
     * 
     * @param project the project to scan
     * 
     * @return the next id for the project
     * @throws PersistenceException
     * @throws PersistenceException if processes cannot be retrieved
     */
    @Override
    public String getNextId() {
        return EcoreUtil.generateUUID();
    }

    @Override
    public RootContainer<String, IRepositoryViewObject> getMetadata(Project project, ERepositoryObjectType type,
            boolean... options) throws PersistenceException {
        return getObjectFromFolder(project, type, true, options);
    }

    public void setXmiResourceManager(XmiResourceManager xmiResourceManager) {
        this.xmiResourceManager = xmiResourceManager;
    }

    @Override
    public List<IRepositoryViewObject> getRecycleBinItems(Project project, boolean... options) throws PersistenceException {
        ERepositoryObjectType types[] = { ERepositoryObjectType.DOCUMENTATION, ERepositoryObjectType.METADATA_CONNECTIONS,
                ERepositoryObjectType.METADATA_SAPCONNECTIONS, ERepositoryObjectType.SQLPATTERNS,
                ERepositoryObjectType.METADATA_FILE_DELIMITED, ERepositoryObjectType.METADATA_FILE_POSITIONAL,
                ERepositoryObjectType.PROCESS, ERepositoryObjectType.CONTEXT, ERepositoryObjectType.SNIPPETS,
                ERepositoryObjectType.ROUTINES, ERepositoryObjectType.BUSINESS_PROCESS,
                ERepositoryObjectType.METADATA_FILE_REGEXP, ERepositoryObjectType.METADATA_FILE_XML,
                ERepositoryObjectType.METADATA_FILE_LDIF, ERepositoryObjectType.METADATA_FILE_EXCEL,
                ERepositoryObjectType.METADATA_LDAP_SCHEMA, ERepositoryObjectType.METADATA_GENERIC_SCHEMA,
                ERepositoryObjectType.METADATA_WSDL_SCHEMA, ERepositoryObjectType.METADATA_SALESFORCE_SCHEMA,
                ERepositoryObjectType.JOBLET, ERepositoryObjectType.METADATA_FILE_EBCDIC,
                ERepositoryObjectType.METADATA_FILE_RULES, ERepositoryObjectType.METADATA_FILE_HL7,
                ERepositoryObjectType.METADATA_FILE_FTP, ERepositoryObjectType.METADATA_FILE_BRMS,
                ERepositoryObjectType.METADATA_MDMCONNECTION, ERepositoryObjectType.METADATA_HEADER_FOOTER,
                ERepositoryObjectType.JOB_SCRIPT };

        List<IRepositoryViewObject> deletedItems = new ArrayList<IRepositoryViewObject>();
        for (ERepositoryObjectType type : types) {
            if (type != null) { // loaded
                RootContainer<String, IRepositoryViewObject> container = getObjectFromFolder(project, type, true, options);
                List<IRepositoryViewObject> repositoryObjects = container.getAbsoluteMembers().objects();
                for (IRepositoryViewObject object : repositoryObjects) {
                    if (object.isDeleted()) {
                        deletedItems.add(object);
                    }
                }
            }
        }
        return deletedItems;
    }

    /**
     * DOC smallet Comment method "convert".
     * 
     * @param toReturn
     * @param serializableAllVersion
     */
    protected List<IRepositoryViewObject> convert(List<IRepositoryViewObject> serializableAllVersion) {
        List<IRepositoryViewObject> toReturn = new ArrayList<IRepositoryViewObject>();
        for (IRepositoryViewObject current : serializableAllVersion) {
            toReturn.add(current);
        }
        return toReturn;
    }

    Map<String, String> lastFolderForItemMap = new HashMap<String, String>();

    Map<String, ERepositoryObjectType> lastRepositoryTypeForItemMap = new HashMap<String, ERepositoryObjectType>();

    protected void addToHistory(String id, ERepositoryObjectType itemType, String path) {
        lastFolderForItemMap.put(id, path);
        lastRepositoryTypeForItemMap.put(id, itemType);
    }

    protected List<IRepositoryViewObject> getSerializable(Project project, String id, boolean allVersion, boolean avoidSaveProject)
            throws PersistenceException {
        List<IRepositoryViewObject> toReturn = new ArrayList<IRepositoryViewObject>();

        if (lastFolderForItemMap.containsKey(id)) {
            ERepositoryObjectType itemType = lastRepositoryTypeForItemMap.get(id);
            String currentPath = lastFolderForItemMap.get(id);
            Object fullFolder = getFullFolder(project, itemType, currentPath);

            try {
                if (fullFolder != null && (fullFolder instanceof FolderItem || ((IFolder) fullFolder).exists())) {
                    List<IRepositoryViewObject> itemsFound = getSerializableFromFolder(project, fullFolder, id, itemType,
                            allVersion, false, true, avoidSaveProject);
                    if (!itemsFound.isEmpty()) { // add for items in recycle-bin
                        toReturn.addAll(itemsFound);
                        return toReturn;
                    }
                }
            } catch (PersistenceException e) {
                // do nothing.
                // if any exception happen or can't find the item, just try to look for it everywhere.
            }
        }

        // added
        for (ERepositoryObjectType repositoryObjectType : (ERepositoryObjectType[]) ERepositoryObjectType.values()) {
            if (!repositoryObjectType.isResourceItem()) {
                continue;
            }
            Object folder = getFolder(project, repositoryObjectType);
            if (folder != null) {
                List<IRepositoryViewObject> itemsFound = getSerializableFromFolder(project, folder, id, repositoryObjectType,
                        allVersion, true, true, avoidSaveProject);
                if (!itemsFound.isEmpty()) {
                    addToHistory(id, repositoryObjectType, itemsFound.get(0).getProperty().getItem().getState().getPath());
                    toReturn.addAll(itemsFound);
                    // all items from the same id are always in the same folder
                    // as we shouldn't find any other item with the same id in another folder.
                    return toReturn;
                }
            }
        }
        return toReturn;
    }

    protected abstract Object getFolder(Project project, ERepositoryObjectType repositoryObjectType) throws PersistenceException;

    @Override
    public List<IRepositoryViewObject> getAllVersion(Project project, String id, boolean avoidSaveProject)
            throws PersistenceException {
        List<IRepositoryViewObject> serializableAllVersion = null;
        serializableAllVersion = getSerializable(project, id, true, avoidSaveProject);
        return convert(serializableAllVersion);
    }

    @Override
    public List<IRepositoryViewObject> getAllVersion(Project project, String id, String relativeFolder, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryViewObject> serializableAllVersion = null;

        Object fullFolder = getFullFolder(project, type, relativeFolder);
        if (fullFolder != null) {
            serializableAllVersion = getSerializableFromFolder(project, fullFolder, id, type, true, false, false, true);
            if (serializableAllVersion.isEmpty()) {
                // look in all folders
                serializableAllVersion = getSerializable(project, id, true, false);
            }
            return convert(serializableAllVersion);
        }
        serializableAllVersion = new ArrayList<IRepositoryViewObject>();
        return serializableAllVersion;
    }

    @Override
    public boolean isNameAvailable(Project project, Item item, String name, List<IRepositoryViewObject>... givenList)
            throws PersistenceException {
        if (name == null) {
            name = item.getProperty().getLabel();
        }

        if (item instanceof FolderItem) {
            FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
            return !folderHelper.pathExists((FolderItem) item, name);
        }

        ERepositoryObjectType type = ERepositoryObjectType.getItemType(item);

        if (type == ERepositoryObjectType.METADATA_CON_TABLE) {
            return false;
        }
        boolean isAllowMultipleName = (type == ERepositoryObjectType.SQLPATTERNS || type == ERepositoryObjectType.METADATA_FILE_XML);
        String path = "";
        if (item.getState() != null) {
            path = item.getState().getPath();
        }

        List<IRepositoryViewObject> list;

        if (givenList.length == 0) {
            list = getAll(project, type, true, false);
        } else {
            list = givenList[0];
        }

        for (IRepositoryViewObject current : list) {
            if ((name.equalsIgnoreCase(current.getProperty().getLabel()) || name.equalsIgnoreCase(current.getProperty().getDisplayName()))
                    && item.getProperty().getId() != current.getProperty().getId()) {
                // To check SQLPattern in same path. see bug 0005038: unable to add a SQLPattern into repository.
                if (!isAllowMultipleName || current.getProperty().getItem().getState().getPath().equals(path)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected abstract List<IRepositoryViewObject> getSerializableFromFolder(Project project, Object folder, String id,
            ERepositoryObjectType type, boolean allVersion, boolean searchInChildren, boolean withDeleted,
            boolean avoidSaveProject, boolean... recursiveCall) throws PersistenceException;

    /**
     * Use instead the function with the parameter (int options)
     * 
     * @deprecated
     */
    @Deprecated
    protected abstract <K, T> RootContainer<K, T> getObjectFromFolder(Project project, ERepositoryObjectType type,
            boolean onlyLastVersion, boolean... options) throws PersistenceException;

    protected boolean hasOption(int value, int tested_option) {
        return (value & tested_option) != 0;
    }

    /**
     * Use instead the function with the parameter (int options)
     * 
     * @deprecated
     */
    @Deprecated
    protected RootContainer<String, IRepositoryViewObject> getObjectFromFolder(Project project, ERepositoryObjectType type,
            String folderName, boolean onlyLastVersion, boolean... options) throws PersistenceException {
        int options_as_int = 0;
        if (onlyLastVersion) {
            options_as_int = options_as_int | OPTION_ONLY_LAST_VERSION;
        } else if (options.length > 0 && options[0] == true) {
            options_as_int = options_as_int | OPTION_DYNAMIC_OBJECTS;
        }
        return getObjectFromFolder(project, type, folderName, options_as_int);
    }

    @Override
    public abstract RootContainer<String, IRepositoryViewObject> getObjectFromFolder(Project project, ERepositoryObjectType type,
            String folderName, int options) throws PersistenceException;

    /**
     * Use instead the function with the parameter (int options)
     * 
     * @deprecated
     */
    @Deprecated
    protected <K, T> void addFolderMembers(Project project, ERepositoryObjectType type, Container<K, T> toReturn,
            Object objectFolder, boolean onlyLastVersion, boolean... options) throws PersistenceException {
        int options_as_int = 0;
        if (onlyLastVersion) {
            options_as_int = options_as_int | OPTION_ONLY_LAST_VERSION;
        }
        if (options.length > 0 && options[0] == true) {
            options_as_int = options_as_int | OPTION_DYNAMIC_OBJECTS;
        }
        addFolderMembers(project, type, toReturn, objectFolder, options_as_int);
    }

    protected abstract <K, T> void addFolderMembers(Project project, ERepositoryObjectType type, Container<K, T> toReturn,
            Object objectFolder, int options) throws PersistenceException;

    protected abstract FolderHelper getFolderHelper(org.talend.core.model.properties.Project emfProject);

    protected Item copyFromResource(Resource createResource) throws PersistenceException, BusinessException {
        boolean changeLabelWithCopyPrefix = true;
        return copyFromResource(createResource, changeLabelWithCopyPrefix);
    }

    protected Item copyFromResource(Resource createResource, boolean changeLabelWithCopyPrefix) throws PersistenceException,
            BusinessException {
        Item newItem = (Item) EcoreUtil.getObjectByType(createResource.getContents(), PropertiesPackage.eINSTANCE.getItem());
        Property property = newItem.getProperty();
        property.setId(getNextId());
        property.setAuthor(getRepositoryContext().getUser());

        if (changeLabelWithCopyPrefix) {
            setPropNewName(property);
        }
        EcoreUtil.resolveAll(createResource);
        return newItem;
    }

    protected Item copyFromResource(Resource createResource, String newItemLabel) throws PersistenceException, BusinessException {
        if (newItemLabel != null) {
            Item newItem = (Item) EcoreUtil.getObjectByType(createResource.getContents(), PropertiesPackage.eINSTANCE.getItem());
            Property property = newItem.getProperty();
            property.setId(getNextId());
            property.setAuthor(getRepositoryContext().getUser());
            property.setLabel(newItemLabel);
            property.setDisplayName(newItemLabel);
            // BUG TDI-25050:If here throw exception,it will block the copy action
            // if (!isNameAvailable(getRepositoryContext().getProject(), property.getItem(), null)) {
            // throw new BusinessException(Messages.getString("AbstractEMFRepositoryFactory.cannotGenerateItem")); //$NON-NLS-1$
            // }
            EcoreUtil.resolveAll(createResource);
            return newItem;
        } else {
            boolean changeLabelWithCopyPrefix = true;
            return copyFromResource(createResource, changeLabelWithCopyPrefix);
        }
    }

    /**
     * Method "setPropNewName". Try first the new name with "[PROPERTY_NAME]_Copy", then, if it already exists, try
     * again with "[PROPERTY_NAME]_CopyN" where N is number between 1 and Integer.MAX.
     * 
     * @param copiedProperty
     * @throws PersistenceException
     * @throws BusinessException
     */
    private void setPropNewName(Property copiedProperty) throws PersistenceException, BusinessException {
        String originalLabel = copiedProperty.getDisplayName();
        String copy = "_Copy"; //$NON-NLS-1$
        String initialTry = originalLabel + copy;
        copiedProperty.setLabel(initialTry);
        // changed by hqzhang for TDI-19965
        copiedProperty.setDisplayName(initialTry);
        if (isNameAvailable(getRepositoryContext().getProject(), copiedProperty.getItem(), null)) {
            return;
        } else {
            long index = 1;
            while (!isNameAvailable(getRepositoryContext().getProject(), copiedProperty.getItem(), null)) {
                if (index < 0) {
                    throw new BusinessException(Messages.getString("AbstractEMFRepositoryFactory.cannotGenerateItem")); //$NON-NLS-1$
                }
                String nextTry = originalLabel + copy + (index++);
                copiedProperty.setLabel(nextTry);
                // changed by hqzhang for TDI-19965
                copiedProperty.setDisplayName(nextTry);
            }
        }
    }

    protected void createSystemRoutines() throws PersistenceException {
        ERepositoryObjectType routinesType = ERepositoryObjectType.ROUTINES;
        if (routinesType == null) {
            return; // don't load, nothing to do
        }
        ILibrariesService service = null;
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            return;
        }

        service = (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);

        Project project = getRepositoryContext().getProject();
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());

        List<URL> routines = service.getSystemRoutines();
        Path path = new Path(RepositoryConstants.SYSTEM_DIRECTORY);
        // will automatically set the children folders
        IPath systemRoutinePath = new Path(ERepositoryObjectType.getFolderName(routinesType));
        systemRoutinePath = systemRoutinePath.append(RepositoryConstants.SYSTEM_DIRECTORY);
        FolderItem folderItem = folderHelper.getFolder(systemRoutinePath);
        if (folderItem == null) {
            folderItem = folderHelper.createFolder(systemRoutinePath.toString());
        }
        IPath systemRoutineApiPath = new Path(ERepositoryObjectType.getFolderName(routinesType));
        systemRoutineApiPath = systemRoutinePath.append(RepositoryConstants.SYSTEM_DIRECTORY).append("api");
        FolderItem folderItemApi = folderHelper.getFolder(systemRoutineApiPath);
        if (folderItemApi == null) {
            folderItemApi = folderHelper.createFolder(systemRoutineApiPath.toString());
        }

        List<IRepositoryViewObject> repositoryObjects = getAll(project, routinesType, false, false);
        Map<String, List<LibraryInfo>> routineAndJars = coreSerivce.getRoutineAndJars();
        for (URL url : routines) {
            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$
            String routineLabel = tmp[0];

            if (routineLabel.equals(coreSerivce.getTemplateString())) {
                continue;
            }

            RoutineItem existingItem = null;
            for (IRepositoryViewObject object : repositoryObjects) {
                if (object.getLabel().equals(routineLabel) && object.getProperty().getItem() instanceof RoutineItem) {
                    existingItem = (RoutineItem) object.getProperty().getItem();
                    break;
                }
            }
            if (existingItem == null) {
                createRoutine(url, path, routineLabel, routineAndJars != null ? routineAndJars.get(routineLabel) : null);
            } else {
                updateRoutine(url, existingItem);
                existingItem.setParent(folderItem);
            }
        }
    }

    protected void createSystemSQLPatterns() throws PersistenceException {
        ERepositoryObjectType sqlpatternsType = ERepositoryObjectType.SQLPATTERNS;
        if (sqlpatternsType == null) {
            return; // don't load, nothing to do
        }
        ILibrariesService service = null;
        if (!GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
            return;
        }
        service = (ILibrariesService) GlobalServiceRegister.getDefault().getService(ILibrariesService.class);
        Project project = getRepositoryContext().getProject();
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
        // will automatically set the children folders
        // FolderItem folderItem = folderHelper.getFolder("sqlPatterns/system");
        // if (folderItem == null) {
        //            folderItem = folderHelper.createFolder("sqlPatterns/system"); //$NON-NLS-1$
        // }

        List<URL> routines = service.getSystemSQLPatterns();

        List<IRepositoryViewObject> repositoryObjects = getAll(project, sqlpatternsType, false, false);

        for (URL url : routines) {
            String[] fragments = url.toString().split("/"); //$NON-NLS-1$
            String label = fragments[fragments.length - 1];
            String[] tmp = label.split("\\."); //$NON-NLS-1$

            Path relativePath = new Path(url.getFile());

            // for instance: categoryName is Teradata; fileName is
            // Loadfile.sqlpattern
            String fileName = relativePath.segment(relativePath.segmentCount() - 1);
            String categoryName = relativePath.segment(relativePath.segmentCount() - 2);

            tmp = fileName.split("\\."); //$NON-NLS-1$

            String sqlPatternLabel = tmp[0];

            SQLPatternItem existingItem = null;
            for (IRepositoryViewObject object : repositoryObjects) {
                if (object.getLabel().equals(sqlPatternLabel) && object.getProperty().getItem() instanceof SQLPatternItem
                        && ((SQLPatternItem) object.getProperty().getItem()).getEltName().equals(categoryName)) {
                    existingItem = (SQLPatternItem) object.getProperty().getItem();
                    break;
                }
            }
            // check the folder for categoryName,system,UserDefined
            // set the item's relative path in the repository view
            IPath categoryPath = new Path(categoryName);
            IPath systemPath = categoryPath.append(RepositoryConstants.SYSTEM_DIRECTORY);
            IPath userPath = categoryPath.append(RepositoryConstants.USER_DEFINED);

            IPath parentPath = new Path(ERepositoryObjectType.getFolderName(sqlpatternsType));
            if (folderHelper.getFolder(parentPath.append(categoryPath)) == null) {
                createFolder(getRepositoryContext().getProject(), sqlpatternsType, new Path(""), categoryPath //$NON-NLS-1$
                        .lastSegment());
            }
            FolderItem systemFolder = folderHelper.getFolder(parentPath.append(systemPath));
            if (systemFolder == null) {
                Folder folder = createFolder(getRepositoryContext().getProject(), sqlpatternsType, categoryPath,
                        systemPath.lastSegment());
                ((FolderItem) folder.getProperty().getItem()).setType(FolderType.FOLDER_LITERAL);
            }
            if (folderHelper.getFolder(parentPath.append(userPath)) == null) {
                Folder folder = createFolder(getRepositoryContext().getProject(), sqlpatternsType, categoryPath,
                        userPath.lastSegment());
                ((FolderItem) folder.getProperty().getItem()).setType(FolderType.FOLDER_LITERAL);
            }
            //
            if (existingItem == null) {
                createSQLPattern(url, sqlPatternLabel, categoryName);
            } else {
                updateSQLPattern(url, existingItem);
                existingItem.setParent(systemFolder);
            }

        }
    }

    /**
     * DOC smallet Comment method "createRoutine".
     * 
     * @param url
     * @throws PersistenceException
     */
    private void createRoutine(URL url, IPath path, String label, List<LibraryInfo> neededJars) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());
            property.setLabel(label);

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);

            // String basePath = System.getProperty("user.dir") + File.separator + "plugins";

            RoutineItem routineItem = PropertiesFactory.eINSTANCE.createRoutineItem();
            routineItem.setProperty(property);
            routineItem.setContent(byteArray);
            routineItem.setBuiltIn(true);
            if (neededJars != null) {
                for (LibraryInfo jar : neededJars) {
                    IMPORTType type = ComponentFactory.eINSTANCE.createIMPORTType();
                    type.setMESSAGE("");
                    type.setNAME(label);
                    type.setREQUIRED(true);
                    type.setMODULE(jar.getLibName());
                    type.setBundleID(jar.getBundleId());
                    routineItem.getImports().add(type);
                }
            }

            if (!routineItem.getProperty().getLabel().equals(coreSerivce.getTemplateString())) {
                create(getRepositoryContext().getProject(), routineItem, path, true);
            }
        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

    private void updateRoutine(URL url, RoutineItem item) throws PersistenceException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();

            byte[] currentContent = item.getContent().getInnerContent();

            if (!Arrays.equals(innerContent, currentContent)) {
                item.getContent().setInnerContent(innerContent);
                Project project = getRepositoryContext().getProject();
                save(project, item);
            }

        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

    private void createSQLPattern(URL url, String sqlPatternLabel, String categoryName) throws PersistenceException {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        InputStream stream = null;
        try {
            Property property = PropertiesFactory.eINSTANCE.createProperty();
            property.setId(getNextId());

            property.setLabel(sqlPatternLabel);

            ByteArray byteArray = PropertiesFactory.eINSTANCE.createByteArray();
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();
            byteArray.setInnerContent(innerContent);

            SQLPatternItem sqlpatternItem = PropertiesFactory.eINSTANCE.createSQLPatternItem();
            sqlpatternItem.setProperty(property);
            sqlpatternItem.setEltName(categoryName);
            sqlpatternItem.setContent(byteArray);
            sqlpatternItem.setSystem(true);

            // set the item's relative path in the repository view
            IPath categoryPath = new Path(categoryName);
            IPath systemPath = categoryPath.append(RepositoryConstants.SYSTEM_DIRECTORY);

            create(getRepositoryContext().getProject(), sqlpatternItem, systemPath, true);

        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

    private void updateSQLPattern(URL url, SQLPatternItem item) throws PersistenceException {
        InputStream stream = null;
        try {
            stream = url.openStream();
            byte[] innerContent = new byte[stream.available()];
            stream.read(innerContent);
            stream.close();

            byte[] currentContent = item.getContent().getInnerContent();

            if (!Arrays.equals(innerContent, currentContent)) {
                item.getContent().setInnerContent(innerContent);
                Project project = getRepositoryContext().getProject();
                save(project, item);
            }

        } catch (IOException ioe) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new PersistenceException(ioe);
                }
            }
            throw new PersistenceException(ioe);
        }
    }

    @Override
    public void logOnProject(Project project) throws LoginException, PersistenceException {
        setLoggedOnProject(false);

        // TODO: review the prefs
        // new StatusPreferenceInitializer().initializeDefaultPreferences();
        IStatusPreferenceInitService statusPreferenceInitService = CoreRuntimePlugin.getInstance()
                .getStatusPreferenceInitService();
        if (statusPreferenceInitService != null) {
            statusPreferenceInitService.initStatusPreference();
        }
        String productVersion = VersionUtils.getVersion();
        IBrandingService brandingService = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IBrandingService.class)) {
            brandingService = (IBrandingService) GlobalServiceRegister.getDefault().getService(IBrandingService.class);
        }
        if (brandingService != null) {
            String version = brandingService.getFullProductName() + "-" + productVersion; //$NON-NLS-1$
            if (!version.equals(project.getEmfProject().getProductVersion())) {
                project.getEmfProject().setProductVersion(version);
                project.getEmfProject().getFolders().clear();
            }
        }
        // saveProject();

        setLoggedOnProject(true);
    }

    @Override
    public List<ModuleNeeded> getModulesNeededForJobs() throws PersistenceException {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        IProxyRepositoryFactory repositoryFactory = CoreRuntimePlugin.getInstance().getRepositoryService()
                .getProxyRepositoryFactory();
        ERepositoryObjectType jobType = ERepositoryObjectType.PROCESS;
        if (jobType != null) {
            List<IRepositoryViewObject> jobs = repositoryFactory.getAll(jobType, true);
            for (IRepositoryViewObject cur : jobs) {
                if (!cur.isDeleted()) {
                    ProcessItem item = (ProcessItem) cur.getProperty().getItem();
                    if (item == null || item.getProcess() == null) {
                        continue;
                    }
                    List<NodeType> nodes = item.getProcess().getNode();
                    for (NodeType node : nodes) {
                        List<ElementParameterType> elementParameter = node.getElementParameter();
                        for (ElementParameterType elementParam : elementParameter) {
                            if (elementParam.getField() != null
                                    && elementParam.getField().equals(EParameterFieldType.MODULE_LIST.getName())) {
                                String uniquename = coreSerivce.getParameterUNIQUENAME(node);
                                ModuleNeeded toAdd = new ModuleNeeded(
                                        Messages.getString("AbstractEMFRepositoryFactory.job") + item.getProperty().getLabel(), //$NON-NLS-1$
                                        elementParam.getValue(),
                                        Messages.getString("AbstractEMFRepositoryFactory.requiredComponent") + uniquename + ".", true); //$NON-NLS-1$ //$NON-NLS-2$
                                importNeedsList.add(toAdd);
                            }
                        }
                    }
                }
            }
        }

        return importNeedsList;
    }

    @Override
    public RootContainer<String, IRepositoryViewObject> getRoutineFromProject(Project project) throws PersistenceException {
        RootContainer<String, IRepositoryViewObject> toReturn = new RootContainer<String, IRepositoryViewObject>();
        ERepositoryObjectType type = ERepositoryObjectType.ROUTINES;
        if (type != null) {
            IProject fsProject = ResourceUtils.getProject(project);

            IFolder objectFolder = ResourceUtils.getFolder(fsProject, ERepositoryObjectType.getFolderName(type), true);

            addFolderMembers(project, type, toReturn, objectFolder, true);
            saveProject(project);
        }
        return toReturn;
    }

    @Override
    public IRepositoryViewObject getLastVersion(Project project, String id) throws PersistenceException {
        List<IRepositoryViewObject> serializableAllVersion = null;
        serializableAllVersion = getSerializable(project, id, false, false);
        int size = serializableAllVersion.size();
        if (size > 1) {
            String message = getItemsMessages(serializableAllVersion, size);

            throw new PersistenceException(Messages.getString(
                    "AbstractEMFRepositoryFactory.presistenceException.OnlyOneOccurenceMustbeFound", message)); //$NON-NLS-1$
        } else if (size == 1) {
            return serializableAllVersion.get(0);
        } else {
            return null;
        }
    }

    /**
     * DOC zli Comment method "getItemsMessages".
     * 
     * @param serializableAllVersion
     * @param size
     * @return
     */
    // for bug 9265
    private String getItemsMessages(List<IRepositoryViewObject> serializableAllVersion, int size) {
        String message = Messages.getString("AbstractEMFRepositoryFactory.presistenceException.whoCauseProblems");//$NON-NLS-1$
        int k = 0;
        for (IRepositoryViewObject object : serializableAllVersion) {
            message += object.getProperty().getLabel();
            k++;
            if (k < size) {
                message += ", ";//$NON-NLS-1$
            }
        }
        return message;
    }

    @Override
    public IRepositoryViewObject getLastVersion(Project project, String id, String relativeFolder, ERepositoryObjectType type)
            throws PersistenceException {
        List<IRepositoryViewObject> serializableAllVersion = null;
        Object fullFolder = getFullFolder(project, type, relativeFolder);
        serializableAllVersion = getSerializableFromFolder(project, fullFolder, id, type, false, false, true, true);
        if (serializableAllVersion.isEmpty()) {
            // look in all folders
            serializableAllVersion = getSerializable(project, id, false, false);
        }
        int size = serializableAllVersion.size();

        if (size > 1) {
            String message = getItemsMessages(serializableAllVersion, size);

            throw new PersistenceException(Messages.getString(
                    "AbstractEMFRepositoryFactory.presistenceException.OnlyOneOccurenceMustbeFound", message)); //$NON-NLS-1$
        } else if (size == 1) {
            return serializableAllVersion.get(0);
        } else {
            return null;
        }
    }

    protected void computePropertyMaxInformationLevel(Property property) {
        EList<Information> informations = property.getInformations();
        InformationLevel maxLevel = null;
        for (Information information : informations) {
            int value = information.getLevel().getValue();
            if (maxLevel == null || value > maxLevel.getValue()) {
                maxLevel = information.getLevel();
            }
        }
        property.setMaxInformationLevel(maxLevel);
    }

    protected Object getFullFolder(Project project, ERepositoryObjectType itemType, String path) throws PersistenceException {
        Object folder = getFolder(project, itemType);
        if (folder == null) {
            return null;
        }
        Object fullFolder;
        if (folder instanceof IFolder) {
            fullFolder = getFolder(project, itemType);
            if (path != null && !"".equals(path)) { //$NON-NLS-1$
                fullFolder = ((IFolder) fullFolder).getFolder(new Path(path));
            }
        } else {
            // FolderItem
            if (path != null && !"".equals(path)) { //$NON-NLS-1$
                // MOD mzhao feature 9207
                if (folder == null) {
                    fullFolder = ResourceUtils.getProject(project).getFolder(new Path(path));
                } else {
                    fullFolder = this.getFolderHelper(project.getEmfProject()).getFolder(
                            ((FolderItem) folder).getProperty().getLabel() + "/" + path); //$NON-NLS-1$
                }
            } else {
                fullFolder = folder;
            }
        }
        return fullFolder;
    }

    @Override
    public Property getUptodateProperty(Project project, Property property) throws PersistenceException {

        List<IRepositoryViewObject> allVersion = new ArrayList<IRepositoryViewObject>();
        getAllVersions(project, property, allVersion);
        for (IRepositoryViewObject repositoryObject : allVersion) {
            Property uptodateProperty = repositoryObject.getProperty();
            if (uptodateProperty!=null&&uptodateProperty.getVersion().equals(property.getVersion())) {
                return uptodateProperty;
            }
        }
        return null;
    }

    private void getAllVersions(Project project, Property property, List<IRepositoryViewObject> allVersion)
            throws PersistenceException {
        ERepositoryObjectType itemType = ERepositoryObjectType.getItemType(property.getItem());
        Object fullFolder = getFullFolder(project, itemType, property.getItem().getState().getPath());
        if (fullFolder != null) {
            allVersion
                    .addAll(getSerializableFromFolder(project, fullFolder, property.getId(), itemType, true, false, false, true));
            if (allVersion.size() == 0) {
                // if no item found in current directory, look for all directory
                allVersion.addAll(getAllVersion(project, property.getId(), false));
            }
        } else {
            allVersion.addAll(getAllVersion(project, property.getId(), false));
        }
        if (allVersion.size() == 0 && project.getEmfProject().getReferencedProjects().size() > 0) {
            String parentBranch = ProjectManager.getInstance().getMainProjectBranch(project);

            for (ProjectReference refProject : (List<ProjectReference>) project.getEmfProject().getReferencedProjects()) {
                if (refProject.getBranch() != null && !parentBranch.equals(refProject.getBranch())) {
                    continue;
                }
                org.talend.core.model.properties.Project emfProject = refProject.getReferencedProject();
                getAllVersions(new Project(emfProject), property, allVersion);
                if (allVersion.size() > 0) {
                    break;
                }
            }
        }
        // MOD mzhao Temporary return original object. In this case, the object hasn't been updated from svn server.
        if (allVersion.size() == 0) {
            allVersion.add(new RepositoryViewObject(property));
        }

    }

    @Override
    public FolderItem getFolderItem(Project project, ERepositoryObjectType itemType, IPath path) {
        if (project == null) {
            // add this to fix NPE of junit for logon
            return null;
        }
        FolderHelper folderHelper = getFolderHelper(project.getEmfProject());
        String pathStr = ERepositoryObjectType.getFolderName(itemType) + IPath.SEPARATOR + path;
        FolderItem folderItem = folderHelper.getFolder(pathStr);
        if (folderItem == null && itemType != null) {
            folderItem = folderHelper.createFolder(pathStr);
        }
        return folderItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.model.IRepositoryFactory#reloadProject(org.talend.core.model.general.Project)
     */
    @Override
    public void reloadProject(Project project) throws PersistenceException {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * DOC zshen Comment method "getMetadataByFolder".
     * 
     * @param itemType
     * @param path
     * @return all of object under path.
     */

    @Override
    public List<IRepositoryViewObject> getMetadataByFolder(Project project, ERepositoryObjectType itemType, IPath path) {
        return getMetadatasByFolder(project, itemType, path);
    }

    protected abstract <K, T> List<T> getMetadatasByFolder(Project project, ERepositoryObjectType type, IPath path);

    @Override
    public RootContainer<String, IRepositoryViewObject> getTdqRepositoryViewObjects(Project project, ERepositoryObjectType type,
            String folderName, boolean[] options) throws PersistenceException {
        String relativeFolder = folderName;
        if (type != null && type.hasFolder()) {
            // Compatible to the existing behavior which keep the full path relative to the project in DQ.
            String baseFolder = ERepositoryObjectType.getFolderName(type);
            relativeFolder = StringUtils.removeStart(relativeFolder, baseFolder);
        }
        return getObjectFromFolder(project, type, relativeFolder, OPTION_ONLY_LAST_VERSION | OPTION_DYNAMIC_OBJECTS);
    }

    @Override
    public boolean canLock(Item item) throws PersistenceException {
        return true;
    }

    @Override
    public boolean canUnlock(Item item) throws PersistenceException {
        return true;
    }

    @Override
    public RootContainer<String, IRepositoryViewObject> getRootContainerFromType(Project project, ERepositoryObjectType type) {
        if (project == null || type == null) {
            return null;
        }
        try {
            return getObjectFromFolder(project, type, true);
        } catch (PersistenceException e) {
            ExceptionHandler.process(e);
        }
        return null;
    }

    @Override
    public LockInfo getLockInfo(Item item) {
        if (item.getState().isLocked()) {
            return new LockInfo(item.getState().getLocker().getLogin(), "studio", item.getState().getLockDate());//$NON-NLS-1$
        }
        return new LockInfo("", "", null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.repository.model.IRepositoryFactory#afterLogon()
     */
    @Override
    public void afterLogon(IProgressMonitor monitor) {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRunProcessService.class)) {
            IRunProcessService runProcessService = (IRunProcessService) GlobalServiceRegister.getDefault()
                    .getService(IRunProcessService.class);
            try {
                runProcessService.getTalendProcessJavaProject().cleanMavenFiles(monitor);
                TimeMeasure.step("logOnProject", "clean Maven files"); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }
}
