// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.librariesmanager.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.PluginChecker;
import org.talend.core.database.conn.version.EDatabaseVersion4Drivers;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.language.LanguageManager;
import org.talend.core.model.component_cache.ComponentCachePackage;
import org.talend.core.model.component_cache.ComponentInfo;
import org.talend.core.model.component_cache.ComponentsCache;
import org.talend.core.model.component_cache.util.ComponentCacheResourceFactoryImpl;
import org.talend.core.model.components.ComponentCategory;
import org.talend.core.model.components.ComponentManager;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsFactory;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.general.ILibrariesService.IChangedLibrariesListener;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.JobletProcessItem;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.ProjectReference;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.properties.RoutinesJarItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.CodesJarInfo;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.core.runtime.process.TalendProcessOptionConstants;
import org.talend.core.utils.CodesJarResourceCache;
import org.talend.core.utils.TalendCacheUtils;
import org.talend.core.utils.TalendQuoteUtils;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.model.service.CustomUriManager;
import org.talend.librariesmanager.model.service.LibrariesIndexManager;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryService;

/**
 * @TODO to be refactored to avoid those ugly static methods everywhere. (SG)
 *
 * $Id: ModulesNeededProvider.java 1893 2007-02-07 11:33:35Z mhirt $
 *
 */
public class ModulesNeededProvider {

    /**
     * TalendHookAdaptor.ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP
     */
    public static final String ORG_TALEND_EXTERNAL_LIB_FOLDER_SYS_PROP = "talend.library.path"; //$NON-NLS-1$

    private static final String PLUGINS_CONTEXT_KEYWORD = "plugin:";

    private static Set<ModuleNeeded> componentImportNeedsList = new HashSet<>();

    /**
     * a module list include modules needed and unused.
     */
    private static Set<ModuleNeeded> allManagedModules = new HashSet<>();

    private static boolean isCreated = false;

    private static boolean cleanDone = false;

    private static final String TALEND_COMPONENT_CACHE = "ComponentsCache.";

    private static final String TALEND_FILE_NAME = "cache";

    private static final List<IChangedLibrariesListener> listeners = new ArrayList<>();

    private static IRepositoryService repositoryService = null;

    private static List<ModuleNeeded> importNeedsListForRoutes;

    private static List<ModuleNeeded> importNeedsListForBeans;

    private static ILibraryManagerService libManagerService = null;

    private static volatile boolean installModuleForRountine = false;

    private static Set<ModuleNeeded> modulesForRountine = new HashSet<>();

    private static List<ModuleNeeded> systemModules = null;

    static {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            repositoryService = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        }
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault()
                    .getService(ILibraryManagerService.class);
        }
    }

    public static Set<ModuleNeeded> getModulesNeeded() {
        if (componentImportNeedsList.isEmpty()) {
            componentImportNeedsList.addAll(getRunningModules(true));
            componentImportNeedsList.addAll(getModulesNeededForApplication());
            if (PluginChecker.isMetadataPluginLoaded()) {
                componentImportNeedsList.addAll(getModulesNeededForDBConnWizard());
            }
            if (!org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
                componentImportNeedsList.addAll(getModulesNeededForComponents());
            }
        }

        return componentImportNeedsList;
    }

    public static Set<ModuleNeeded> getAllManagedModules() {
        if (allManagedModules.isEmpty()) {
            allManagedModules.addAll(getModulesNeeded());
            try {
                // get moudles installed but not used by any component/routine......
                Set<String> modulesNeededNames = getModulesNeededNames();
                // lib/java
                String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
                File libJavaDir = new File(librariesPath);
                if (libJavaDir.exists()) {
                    List<File> jarFiles = FilesUtils.getJarFilesFromFolder(libJavaDir, null);
                    for (File jarFile : jarFiles) {
                        if (!modulesNeededNames.contains(jarFile.getName())) {
                            addUnknownModules(jarFile.getName(), null, false);

                        }
                    }
                }
                // Custom URI Mapping
                Set<String> modulesNeededMVNURIs = getModulesNeededDefaultMVNURIs();
                CustomUriManager.getInstance().reloadCustomMapping();
                for (String mvnURIKey : new HashSet<String>(CustomUriManager.getInstance().keySet())) {
                    if (!modulesNeededMVNURIs.contains(mvnURIKey)) {
                        // use the key(defulat value) to create the unknown module ,no need to set the custom maven uri
                        // ,it can be get from the index
                        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mvnURIKey);
                        addUnknownModules(artifact.getArtifactId() + "." + artifact.getType(), mvnURIKey, false);
                    }
                }
            } catch (MalformedURLException e) {
                ExceptionHandler.process(e);
            }

        }

        return allManagedModules;
    }

    /**
     * DOC sgandon Comment method "getModulesNeeded".
     *
     * @param moduleName, must not be null
     * @return all modules needed matching the module name
     */
    public static List<ModuleNeeded> getModulesNeededForName(String moduleName) {
        ArrayList<ModuleNeeded> modulesMatching = new ArrayList<ModuleNeeded>();
        for (ModuleNeeded modNeed : getAllManagedModules()) {
            if (moduleName.equals(modNeed.getModuleName())) {
                modulesMatching.add(modNeed);
            }
        }
        return modulesMatching;
    }

    public static ModuleNeeded getModuleNeededById(String id) {
        ModuleNeeded result = null;

        Set<ModuleNeeded> modulesNeeded = getModulesNeeded();
        for (ModuleNeeded moduleNeeded : modulesNeeded) {
            if (id.startsWith(MavenUrlHelper.MVN_PROTOCOL)) {
                if (id.equals(moduleNeeded.getMavenUri())) {
                    result = moduleNeeded;
                    break;
                }
            } else {
                if (id.equals(moduleNeeded.getId())) {
                    result = moduleNeeded;
                    break;
                }
            }
        }

        return result;
    }

    public static Set<String> getModulesNeededNames() {
        Set<String> componentImportNeedsListNames = new HashSet<String>();
        for (ModuleNeeded m : getModulesNeeded()) {
            componentImportNeedsListNames.add(m.getModuleName());
        }
        return componentImportNeedsListNames;
    }

    public static Set<String> getAllManagedModuleNames() {
        Set<String> moduleNames = new HashSet<String>();
        for (ModuleNeeded m : getAllManagedModules()) {
            moduleNames.add(m.getModuleName());
        }
        return moduleNames;
    }

    public static Set<String> getAllModuleNamesFromIndex() {
        Set<String> moduleNames = new HashSet<String>();
        moduleNames.addAll(LibrariesIndexManager.getInstance().getMavenLibIndex().getJarsToRelativePath().keySet());
        moduleNames.addAll(LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath().keySet());
        return moduleNames;
    }

    public static Set<String> getModulesNeededDefaultMVNURIs() {
        Set<String> mvnURIs = new HashSet<String>();
        for (ModuleNeeded m : getModulesNeeded()) {
            mvnURIs.add(m.getDefaultMavenURI());
        }
        return mvnURIs;
    }

    public static void reset() {
        // clean the cache
        ExtensionModuleManager.getInstance().clearCache();
        getModulesNeeded().clear();
        getAllManagedModules().clear();
        systemModules = null;
    }

    /**
     * ftang Comment method "resetCurrentJobNeededModuleList".
     *
     * @param process
     */
    public static void resetCurrentJobNeededModuleList(IProcess process) {
        // Step 1: remove all modules for current job;
        List<ModuleNeeded> moduleForCurrentJobList = new ArrayList<ModuleNeeded>(5);
        for (ModuleNeeded module : getModulesNeeded()) {
            if (module.getContext().equals("Job " + process.getName())) { //$NON-NLS-1$
                moduleForCurrentJobList.add(module);
            }
        }
        getModulesNeeded().removeAll(moduleForCurrentJobList);
        getAllManagedModules().removeAll(moduleForCurrentJobList);

        Set<ModuleNeeded> neededLibraries = process.getNeededModules(TalendProcessOptionConstants.MODULES_DEFAULT);

        if (neededLibraries != null) {
            for (ModuleNeeded neededLibrary : neededLibraries) {
                boolean alreadyInImports = false;
                for (ModuleNeeded module : getModulesNeeded()) {
                    if (module != null && module.getModuleName() != null
                            && module.getModuleName().equals(neededLibrary.getModuleName())) {
                        if (StringUtils.equals(module.getMavenUri(), neededLibrary.getMavenUri())) {
                            alreadyInImports = true;
                            break;
                        }
                    }
                }
                if (alreadyInImports) {
                    continue;
                }

                // Step 2: re-add specific modules
                ModuleNeeded toAdd = null;

                if (neededLibrary.getMavenUri() != null) {
                    toAdd = new ModuleNeeded("Job " + process.getName(), "Required for the job " + process.getName() + ".", true,
                            neededLibrary.getMavenUri());


                } else {
                    toAdd = new ModuleNeeded("Job " + process.getName(), neededLibrary.getModuleName(), //$NON-NLS-1$
                            "Required for the job " + process.getName() + ".", true); //$NON-NLS-1$ //$NON-NLS-2$
                }

                getModulesNeeded().add(toAdd);
                getAllManagedModules().add(toAdd);
            }

        }
    }

    private static List<ModuleNeeded> getModulesNeededForComponents() {
        initCache();
        if (isCreated) {
            List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
            ComponentsCache cache = ComponentManager.getComponentCache();
            EMap<String, EList<ComponentInfo>> map = cache.getComponentEntryMap();
            Set<String> set = map.keySet();
            Iterator it = set.iterator();
            Map<String, Boolean> bundlesAvailable = new HashMap<String, Boolean>();
            while (it.hasNext()) {
                String key = (String) it.next();
                EList<ComponentInfo> value = map.get(key);
                for (ComponentInfo info : value) {
                    Boolean available = bundlesAvailable.get(info.getSourceBundleName());
                    if (available == null) {
                        available = Platform.getBundle(info.getSourceBundleName()) != null;
                        bundlesAvailable.put(info.getSourceBundleName(), available);
                    }
                    if (!available) {
                        continue;
                    }

                    EList emfImportList = info.getImportType();
                    for (int i = 0; i < emfImportList.size(); i++) {
                        IMPORTType importType = (IMPORTType) emfImportList.get(i);
                        collectModuleNeeded(key, importType, importNeedsList);
                    }
                }
            }
            return importNeedsList;
        } else {
            List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
                IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault()
                        .getService(IComponentsService.class);
                IComponentsFactory compFac = service.getComponentsFactory();
                for (IComponent comp : compFac.readComponents()) {
                    importNeedsList.addAll(comp.getModulesNeeded());
                }
            }
            return importNeedsList;
        }
    }

    public static void collectModuleNeeded(String context, IMPORTType importType, List<ModuleNeeded> importNeedsList) {
        List<ModuleNeeded> importModuleFromExtension = ExtensionModuleManager.getInstance().getModuleNeededForComponent(context,
                importType);
        boolean foundModule = importModuleFromExtension.size() > 0;
        if (!foundModule) { // If cannot find the jar from extension point then do it like before.
            createModuleNeededForComponent(context, importType, importNeedsList);
        } else {
            importNeedsList.addAll(importModuleFromExtension);
        }
    }

    private static boolean createModuleNeededForComponentFromExtension(String context, IMPORTType importType,
            List<ModuleNeeded> importNeedsList) {
        List<ModuleNeeded> importModuleNeeded = ExtensionModuleManager.getInstance().getModuleNeededForComponent(context,
                importType);
        importNeedsList.addAll(importModuleNeeded);

        return importModuleNeeded.size() > 0;
    }

    public static void createModuleNeededForComponent(String context, IMPORTType importType, List<ModuleNeeded> importNeedsList) {
        if (importType.getMODULE() == null) {
            if (importType.getMODULEGROUP() != null) {
                CommonExceptionHandler.warn("Missing module group definition: " + importType.getMODULEGROUP());
            }
            return;
        }
        String msg = importType.getMESSAGE();
        if (msg == null) {
            msg = Messages.getString("modules.required"); //$NON-NLS-1$
        }
        List<String> list = getInstallURL(importType);
        ModuleNeeded moduleNeeded = new ModuleNeeded(context, importType.getMODULE(), msg, importType.isREQUIRED(), list,
                importType.getREQUIREDIF(), importType.getMVN());
        moduleNeeded.setUseReleaseVersion(true);
        initBundleID(importType, moduleNeeded);
        moduleNeeded.setMrRequired(importType.isMRREQUIRED());
        moduleNeeded.setShow(importType.isSHOW());
        moduleNeeded.setModuleLocaion(importType.getUrlPath());
        importNeedsList.add(moduleNeeded);
    }

    public static List<String> getInstallURL(IMPORTType importType) {
        List<String> list = new ArrayList<String>();
        EList emfInstall = importType.getURL();
        for (int j = 0; j < emfInstall.size(); j++) {
            String installtype = (String) emfInstall.get(j);
            list.add(installtype);
        }
        return list;
    }

    protected static void initBundleID(IMPORTType importType, ModuleNeeded componentImportNeeds) {
        String bundleID = importType.getBundleID();
        if (bundleID != null) {
            String bundleName = null;
            String bundleVersion = null;
            if (bundleID.contains(":")) {
                String[] nameAndVersion = bundleID.split(":");
                bundleName = nameAndVersion[0];
                bundleVersion = nameAndVersion[1];
            } else {
                bundleName = bundleID;
            }
            componentImportNeeds.setBundleName(bundleName);
            componentImportNeeds.setBundleVersion(bundleVersion);
        }
    }

    private static void initCache() {
        String installLocation = new Path(Platform.getConfigurationLocation().getURL().getPath()).toFile().getAbsolutePath();
        boolean isNeedClean = !cleanDone && TalendCacheUtils.isSetCleanComponentCache();
        cleanDone = true;
        isCreated = hasComponentFile(installLocation) && !isNeedClean;
        ComponentsCache cache = ComponentManager.getComponentCache();
        try {
            if (isCreated) {
                if (cache.getComponentEntryMap().isEmpty()) {
                    ComponentsCache loadCache = loadComponentResource(installLocation);
                    cache.getComponentEntryMap().putAll(loadCache.getComponentEntryMap());
                }
            } else {
                cache.getComponentEntryMap().clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
            cache.getComponentEntryMap().clear();
            isCreated = false;
        }
    }

    private static boolean hasComponentFile(String installLocation) {
        String filePath = ModulesNeededProvider.TALEND_COMPONENT_CACHE
                + LanguageManager.getCurrentLanguage().toString().toLowerCase() + ModulesNeededProvider.TALEND_FILE_NAME;
        File file = new File(new Path(installLocation).append(filePath).toString());
        return file.exists();
    }

    private static ComponentsCache loadComponentResource(String installLocation) throws IOException {
        String filePath = ModulesNeededProvider.TALEND_COMPONENT_CACHE
                + LanguageManager.getCurrentLanguage().toString().toLowerCase() + ModulesNeededProvider.TALEND_FILE_NAME;
        URI uri = URI.createFileURI(installLocation).appendSegment(filePath);
        ComponentCacheResourceFactoryImpl compFact = new ComponentCacheResourceFactoryImpl();
        Resource resource = compFact.createResource(uri);
        Map optionMap = new HashMap();
        optionMap.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
        optionMap.put(XMLResource.OPTION_USE_PARSER_POOL, new XMLParserPoolImpl());
        optionMap.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, new HashMap());
        optionMap.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.FALSE);
        resource.load(optionMap);
        ComponentsCache cache = (ComponentsCache) EcoreUtil.getObjectByType(resource.getContents(),
                ComponentCachePackage.eINSTANCE.getComponentsCache());
        return cache;
    }

    /**
     * @deprecated
     */
    public static List<ModuleNeeded> getModulesNeededForJobs() {
        IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        try {
            importNeedsList = repositoryFactory.getModulesNeededForJobs();
        } catch (PersistenceException e) {
            CommonExceptionHandler.process(e);
        }
        return importNeedsList;
    }

    public static Set<ModuleNeeded> getCodesModulesNeededForProcess(ProcessItem processItem, IProcess process,
            boolean includeCodesJar) {
        Set<ModuleNeeded> modulesNeeded = new HashSet<ModuleNeeded>();
        modulesNeeded.addAll(getCodesModulesNeededForProcess(processItem, ERepositoryObjectType.ROUTINES));
        if (includeCodesJar) {
            modulesNeeded.addAll(getCodesJarModulesNeededForProcess(processItem));
        }
        if (ComponentCategory.CATEGORY_4_CAMEL.getName().equals(process.getComponentsType())) {
            // route do not save any relationship with beans , so add all for now
            modulesNeeded.addAll(getCodesModuleNeededs(ERepositoryObjectType.BEANS));
            modulesNeeded.addAll(getModulesNeededForRoutes());
        }
        return modulesNeeded;
    }

    @SuppressWarnings("unchecked")
    private static List<ModuleNeeded> getCodesModulesNeededForProcess(ProcessItem processItem, ERepositoryObjectType type) {
        List<ModuleNeeded> importNeedsList = new ArrayList<>();
        if (processItem != null && processItem.getProcess() != null && processItem.getProcess().getParameters() != null
                && processItem.getProcess().getParameters().getRoutinesParameter() != null) {
            Set<String> systemRoutines = new HashSet<>();
            Set<String> userRoutines = new HashSet<>();
            if (repositoryService != null) {
                try {
                    List<IRepositoryViewObject> routines = repositoryService.getProxyRepositoryFactory().getAll(type, true);
                    for (RoutinesParameterType infor : (List<RoutinesParameterType>) processItem.getProcess().getParameters()
                            .getRoutinesParameter()) {
                        Property property = findRoutinesPropery(infor.getId(), infor.getName(), routines, type);
                        if (property != null) {
                            if (((RoutineItem) property.getItem()).isBuiltIn()) {
                                systemRoutines.add(infor.getId());
                            } else {
                                userRoutines.add(infor.getId());
                            }
                        }
                    }
                } catch (PersistenceException e) {
                    CommonExceptionHandler.process(e);
                }
            }
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = GlobalServiceRegister.getDefault()
                        .getService(ILibraryManagerUIService.class);
                if (!systemRoutines.isEmpty()) {
                    List<IRepositoryViewObject> systemRoutineItems = libUiService.collectRelatedRoutines(systemRoutines, true,
                            type);
                    importNeedsList.addAll(collectCodesModuleNeeded(systemRoutineItems, systemRoutines, true));
                }
                if (!userRoutines.isEmpty()) {
                    List<IRepositoryViewObject> collectUserRoutines = libUiService.collectRelatedRoutines(userRoutines, false,
                            type);
                    importNeedsList.addAll(collectCodesModuleNeeded(collectUserRoutines, userRoutines, false));
                }
            }
        }
        return importNeedsList;
    }

    public static Set<ModuleNeeded> getCodesJarModulesNeededForProcess(Item item) {
        Set<ModuleNeeded> importNeedsList = new HashSet<>();
        if (org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
            return importNeedsList;
        }
        EList<RoutinesParameterType> routinesParameterTypes = null;
        if (item instanceof ProcessItem) {
            if (((ProcessItem) item).getProcess() != null && ((ProcessItem) item).getProcess().getParameters() != null) {
                routinesParameterTypes = ((ProcessItem) item).getProcess().getParameters().getRoutinesParameter();
            }
        } else if (item instanceof JobletProcessItem) {
            if (((JobletProcessItem) item).getJobletProcess() != null
                    && ((JobletProcessItem) item).getJobletProcess().getParameters() != null) {
                routinesParameterTypes = ((JobletProcessItem) item).getJobletProcess().getParameters().getRoutinesParameter();
            }
        }
        if (routinesParameterTypes == null) {
            return importNeedsList;
        }
        routinesParameterTypes.stream().filter(r -> r.getType() != null)
                .map(r -> CodesJarResourceCache.getCodesJarById(r.getId())).filter(info -> info != null)
                .forEach(info -> importNeedsList.addAll(createModuleNeededFromCodesJar(info)));

        checkInstallStatus(importNeedsList);

        return importNeedsList;
    }

    private static Property findRoutinesPropery(String id, String name, List<IRepositoryViewObject> routines,
            ERepositoryObjectType type) {
        if (repositoryService != null) {
            IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
            Set<Project> visited = new HashSet<Project>();
            getRefRoutines(routines, ProjectManager.getInstance().getCurrentProject(), type, visited);
            for (IRepositoryViewObject current : routines) {
                if (repositoryFactory.getStatus(current) != ERepositoryStatus.DELETED) {
                    Item item = current.getProperty().getItem();
                    RoutineItem routine = (RoutineItem) item;
                    Property property = routine.getProperty();
                    if (property.getId().equals(id) || property.getLabel().equals(name)) {
                        return property;
                    }
                }
            }
        }
        return null;
    }

    private static List<ModuleNeeded> collectCodesModuleNeeded(List<IRepositoryViewObject> routineItems, Set<String> routineIdOrNames,
            boolean system) {
        List<ModuleNeeded> importNeedsList = new ArrayList<>();
        if (org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
            return importNeedsList;
        }
        if (!routineItems.isEmpty()) {
            for (IRepositoryViewObject object : routineItems) {
                if (routineIdOrNames.contains(object.getLabel()) && system
                        || routineIdOrNames.contains(object.getId()) && !system) {
                    importNeedsList.addAll(createModuleNeededFromCodeItem(object.getProperty().getItem()));
                }
            }
        }

        // add modules which internal system routine(which under system folder and don't have item) need.
        if (system) {
            if (systemModules == null) {
                systemModules = new ArrayList<>();
                if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                    ILibraryManagerUIService libUiService = GlobalServiceRegister.getDefault()
                            .getService(ILibraryManagerUIService.class);
                    Set<String> routinesName = new HashSet<>();
                    for (IRoutinesProvider routineProvider : libUiService.getRoutinesProviders(ECodeLanguage.JAVA)) {
                        Stream.concat(routineProvider.getSystemRoutines().stream(), routineProvider.getTalendRoutines().stream())
                                .forEach(url -> routinesName.add(StringUtils
                                        .substringBeforeLast(StringUtils.substringAfterLast(url.toString(), "/"), "."))); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    Map<String, List<LibraryInfo>> routineAndJars = libUiService.getRoutineAndJars();
                    Iterator<Map.Entry<String, List<LibraryInfo>>> iter = routineAndJars.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, List<LibraryInfo>> entry = iter.next();
                        String routineName = entry.getKey();
                        if (!routinesName.contains(routineName)) {
                            continue;
                        }
                        List<LibraryInfo> needJars = entry.getValue();
                        for (LibraryInfo jar : needJars) {
                            ModuleNeeded toAdd = new ModuleNeeded("Routine " + routineName, jar.getLibName(), //$NON-NLS-1$
                                    "", true);
                            String bundleId = jar.getBundleId();
                            if (bundleId != null) {
                                String bundleName = null;
                                String bundleVersion = null;
                                if (bundleId.contains(":")) { //$NON-NLS-1$
                                    String[] nameAndVersion = bundleId.split(":"); //$NON-NLS-1$
                                    bundleName = nameAndVersion[0];
                                    bundleVersion = nameAndVersion[1];
                                } else {
                                    bundleName = bundleId;
                                }
                                toAdd.setBundleName(bundleName);
                                toAdd.setBundleVersion(bundleVersion);
                            }
                            systemModules.add(toAdd);
                        }
                    }
                }
            }
            importNeedsList.addAll(systemModules);
        }

        Set<String> dedupModulesList = new HashSet<>();
        Iterator<ModuleNeeded> it = importNeedsList.iterator();
        while (it.hasNext()) {
            ModuleNeeded module = it.next();
            // try to keep only real files (with extension, no matter be jar or other)
            // in some case it's not a real library, but just a text.
            String coordinate = MavenUrlHelper.getCoordinate(module.getMavenUri());
            if (!module.getModuleName().contains(".") || dedupModulesList.contains(coordinate)) { //$NON-NLS-1$
                it.remove();
            } else {
                dedupModulesList.add(coordinate);
            }
        }

        return new ArrayList<>(importNeedsList);
    }

    @SuppressWarnings("unchecked")
    private static Set<ModuleNeeded> createModuleNeededFromCodeItem(Item item) {
        Set<ModuleNeeded> importNeedsList = new HashSet<ModuleNeeded>();
        RoutineItem routine = (RoutineItem) item;
        String label = ERepositoryObjectType.getItemType(routine).getLabel();
        String context = label + " " + routine.getProperty().getLabel();
        List<IMPORTType> imports = routine.getImports();
        for (IMPORTType currentImport : imports) {
            String value = currentImport.getMVN() != null ? currentImport.getMVN() : currentImport.getMODULE();
            boolean isRequired = currentImport.isREQUIRED();
            ModuleNeeded toAdd = ModuleNeeded.newInstance(context, value, currentImport.getMESSAGE(), isRequired);
            if (!isRequired) {
                toAdd.getExtraAttributes().put("IS_OSGI_EXCLUDED", Boolean.TRUE);
                if ("RoutineItem".equals(item.eClass().getName())) {
                    toAdd.setExcluded(true);
                }
            }
            // toAdd.setStatus(ELibraryInstallStatus.INSTALLED);
            importNeedsList.add(toAdd);
        }
        return importNeedsList;
    }

    private static Set<ModuleNeeded> createModuleNeededFromCodesJar(CodesJarInfo info) {
        Set<ModuleNeeded> importNeedsList = new HashSet<ModuleNeeded>();
        String context = info.getType().getLabel() + " " + info.getLabel();
        List<IMPORTType> imports = info.getImports();
        for (IMPORTType currentImport : imports) {
            String value = currentImport.getMVN() != null ? currentImport.getMVN() : currentImport.getMODULE();
            boolean isRequired = currentImport.isREQUIRED();
            ModuleNeeded toAdd = ModuleNeeded.newInstance(context, value, currentImport.getMESSAGE(), isRequired);
            if (!isRequired) {
                toAdd.getExtraAttributes().put("IS_OSGI_EXCLUDED", Boolean.TRUE);
                if (info.getType() == ERepositoryObjectType.ROUTINESJAR) {
                    toAdd.setExcluded(true);
                }
            }
            importNeedsList.add(toAdd);
        }
        return importNeedsList;
    }

    public static List<ModuleNeeded> getModulesNeededForRoutines(ERepositoryObjectType type) {
        List<ModuleNeeded> importNeedsList = new ArrayList<>();
        if (repositoryService != null && type != null) {
            IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
            try {
                List<IRepositoryViewObject> routines = repositoryFactory.getAll(type, true);
                Set<Project> visited = new HashSet<Project>();
                getRefRoutines(routines, ProjectManager.getInstance().getCurrentProject(), type, visited);
                for (IRepositoryViewObject current : routines) {
                    if (!current.isDeleted()) {
                        importNeedsList.addAll(createModuleNeededFromCodeItem(current.getProperty().getItem()));
                    }
                }
            } catch (PersistenceException e) {
                CommonExceptionHandler.process(e);
            }
        }
        checkInstallStatus(importNeedsList);
        return importNeedsList;
    }

    public static ModuleNeeded getComponentModuleById(String palettType, String moduleId) {
        if (repositoryService != null) {
            for (IComponent c : repositoryService.getComponentsFactory().getComponents()) {
                for (ModuleNeeded m : c.getModulesNeeded()) {
                    String pt = c.getPaletteType();
                    if ((palettType == null || palettType.equalsIgnoreCase(pt)) && m.getId() != null
                            && m.getId().equalsIgnoreCase(moduleId)) {
                        return m;
                    }
                }
            }
        }
        return null;
    }

    public static List<ModuleNeeded> getModulesNeededForRoutes() {
        if (importNeedsListForRoutes == null) {
            importNeedsListForRoutes = new ArrayList<ModuleNeeded>();
            importNeedsListForRoutes.add(getComponentModuleById("CAMEL", "camel-core"));
            importNeedsListForRoutes.add(getComponentModuleById("CAMEL", "camel-spring"));
            importNeedsListForRoutes.add(getComponentModuleById("CAMEL", "spring-context"));
            importNeedsListForRoutes.add(getComponentModuleById("CAMEL", "spring-beans"));
            importNeedsListForRoutes.add(getComponentModuleById("CAMEL", "spring-core"));
            if (System.getProperty("java.version") != null && System.getProperty("java.version").startsWith("11")) {
                getModulesNeededForRoutesJava11();
            }
            for (ModuleNeeded mod : importNeedsListForRoutes) {
                if (ELibraryInstallStatus.INSTALLED != mod.getStatus()) {
                    installModuleForRountine = true;
                }
            }
        }
        return importNeedsListForRoutes;
    }

    private static void getModulesNeededForRoutesJava11() {
        importNeedsListForRoutes.add(getModuleNeededForName("istack-commons-runtime-3.0.8.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("jakarta.xml.bind-api-2.3.2.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("jakarta.xml.soap-api-1.4.1.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("javax.activation-1.2.0.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("javax.annotation-api-1.3.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("jaxb-runtime-2.3.2.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("stax-ex-1.8.1.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("txw2-2.3.2.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("jakarta.jws-api-1.1.1.jar"));
        importNeedsListForRoutes.add(getModuleNeededForName("org.apache.servicemix.specs.jaxws-api-2.2-2.9.0.jar"));
    }

    /**
     * Get first ModelNeed by name
     * @param moduleName
     * @return
     */
    public static ModuleNeeded getModuleNeededForName(String moduleName) {
        List<ModuleNeeded> modulesNeeded = getModulesNeededForName(moduleName);
        if (modulesNeeded.size() > 0) {
            return modulesNeeded.get(0);
        }
        return null;
    }

    public static List<ModuleNeeded> getModulesNeededForBeans() {
        if (importNeedsListForBeans == null) {
            importNeedsListForBeans = getModulesNeededForRoutes();
            importNeedsListForBeans.add(getComponentModuleById("CAMEL", "camel-cxf"));
            importNeedsListForBeans.add(getComponentModuleById("CAMEL", "cxf-core"));
            importNeedsListForBeans.add(getComponentModuleById("CAMEL", "javax.ws.rs-api"));
            for (ModuleNeeded need : importNeedsListForBeans) {
                need.setRequired(false);
            }
        }
        return importNeedsListForBeans;
    }

    private static void getRefRoutines(List<IRepositoryViewObject> routines, Project mainProject, ERepositoryObjectType type,
            Set<Project> projects) {
        if (repositoryService != null) {
            IProxyRepositoryFactory repositoryFactory = repositoryService.getProxyRepositoryFactory();
            try {
                // no referenced project or already visited
                if (mainProject.getProjectReferenceList().size() == 0 || projects.contains(mainProject)) {
                    return;
                }
                projects.add(mainProject);

                for (ProjectReference referencedProject : mainProject.getProjectReferenceList()) {
                    Project p = new Project(referencedProject.getReferencedProject());
                    routines.addAll(repositoryFactory.getAll(p, type, true));
                    getRefRoutines(routines, p, type, projects);
                }
            } catch (PersistenceException e) {
                CommonExceptionHandler.process(e);
            }
        }
    }

    public static List<ModuleNeeded> getModulesNeededForApplication() {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();

        List<IConfigurationElement> extension = getAllModulesNeededExtensions();

        for (IConfigurationElement current : extension) {
            ModuleNeeded module = createModuleNeededInstance(current);
            importNeedsList.add(module);
        }

        return importNeedsList;
    }

    /**
     * DOC sgandon Comment method "createModuleNeededInstance".
     *
     * @param current
     * @return
     */
    public static ModuleNeeded createModuleNeededInstance(IConfigurationElement current) {
        String id = current.getAttribute(ExtensionModuleManager.ID_ATTR);
        String context = current.getAttribute(ExtensionModuleManager.CONTEXT_ATTR);
        String name = current.getAttribute(ExtensionModuleManager.NAME_ATTR);
        String message = current.getAttribute(ExtensionModuleManager.MESSAGE_ATTR);
        boolean required = new Boolean(current.getAttribute(ExtensionModuleManager.REQUIRED_ATTR));
        String uripath = current.getAttribute(ExtensionModuleManager.URIPATH_ATTR);
        uripath = ExtensionModuleManager.getInstance().getFormalModulePath(uripath, current);
        String mvn_rui = current.getAttribute(ExtensionModuleManager.MVN_URI_ATTR);
        ModuleNeeded module = new ModuleNeeded(context, name, message, required);
        module.setUseReleaseVersion(true);
        if (uripath != null && libManagerService.checkJarInstalledFromPlatform(uripath)) {
            module.setModuleLocaion(uripath);
        }
        module.setId(id);
        module.setBundleName(current.getAttribute(ExtensionModuleManager.BUNDLEID_ATTR));
        if (!StringUtils.isEmpty(mvn_rui)) {
            module.setMavenUri(mvn_rui);
        }
        module.setMavenUri(mvn_rui);
        String excludeDependencies = current.getAttribute(ExtensionModuleManager.EXCLUDE_DEPENDENCIES_ATTR);
        module.setExcludeDependencies(Boolean.valueOf(excludeDependencies));
        return module;
    }

    /**
     *
     * @return the list of all extensions implementing org.talend.core.runtime.librariesNeeded/libraryNeeded
     */
    public static List<IConfigurationElement> getAllModulesNeededExtensions() {
        IExtensionPointLimiter actionExtensionPoint = new ExtensionPointLimiterImpl(ExtensionModuleManager.EXT_ID,
                ExtensionModuleManager.MODULE_ELE);
        List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(actionExtensionPoint);
        return extension;
    }

    /**
     * @return the list all extension implementing org.talend.core.runtime.librariesNeeded/libraryNeeded, that define a
     * bundle(plugin) required jar. they are defined using the "context" attribute that starts with the keyword
     * "plugin:"
     */
    public static List<ModuleNeeded> getAllModulesNeededExtensionsForPlugin() {
        List<ModuleNeeded> allPluginsRequiredModules = new ArrayList<ModuleNeeded>();

        List<IConfigurationElement> extension = getAllModulesNeededExtensions();

        for (IConfigurationElement current : extension) {
            String context = current.getAttribute(ExtensionModuleManager.CONTEXT_ATTR); // $NON-NLS-1$
            if (context != null && context.startsWith(PLUGINS_CONTEXT_KEYWORD)) {
                ModuleNeeded module = createModuleNeededInstance(current);
                if (module.isRequired()) {
                    allPluginsRequiredModules.add(module);
                }
            }
        }
        return allPluginsRequiredModules;
    }

    /**
     * this method checks if each required library is installed without any caching to be careful when using this. if
     * monitor is canceled, then the return value will be empty
     *
     * @return the list all extension implementing org.talend.core.runtime.librariesNeeded/libraryNeeded, that define a
     * bundle(plugin) required jar. they are defined using the "context" attribute that starts with the keyword
     * "plugin:" and that also are not present in the java.lib library
     */
    public static List<ModuleNeeded> getAllNoInstalledModulesNeededExtensionsForPlugin(IProgressMonitor monitor) {
        List<ModuleNeeded> allPluginsRequiredModules = getAllModulesNeededExtensionsForPlugin();
        List<ModuleNeeded> allUninstalledModules = new ArrayList<ModuleNeeded>(allPluginsRequiredModules.size());
        SubMonitor subMonitor = SubMonitor.convert(monitor, allPluginsRequiredModules.size());

        for (ModuleNeeded module : allPluginsRequiredModules) {
            if (module.getStatus() == ELibraryInstallStatus.NOT_INSTALLED) {
                allUninstalledModules.add(module);
            }
            if (subMonitor.isCanceled()) {
                return Collections.EMPTY_LIST;
            } // else keep going
            subMonitor.worked(1);
        }
        return allUninstalledModules;
    }

    /**
     * filer in the moduleList the modeules required for the bundleSymbolicName. It first checks that the module is
     * indeed a bundle module and then check if the symbolic name matches.
     *
     * @param bundleSymbolicName
     * @param moduleList
     * @return list of modules required for the bundle named "bundleSymbolicName" among the "moduleList"
     */
    public static List<ModuleNeeded> filterRequiredModulesForBundle(String bundleSymbolicName, List<ModuleNeeded> moduleList) {
        List<ModuleNeeded> pluginRequiredModules = new ArrayList<ModuleNeeded>();
        for (ModuleNeeded module : moduleList) {
            String context = module.getContext();
            if (context != null && context.startsWith(PLUGINS_CONTEXT_KEYWORD) && context.endsWith(":" + bundleSymbolicName)) {
                pluginRequiredModules.add(module);
            }
        }
        return pluginRequiredModules;
    }

    public static List<ModuleNeeded> filterOutRequiredModulesForBundle(List<ModuleNeeded> moduleList) {
        List<ModuleNeeded> pluginRequiredModules = new ArrayList<ModuleNeeded>();
        List<String> requiredModuleNames = new ArrayList<String>();
        for (ModuleNeeded module : moduleList) {
            String context = module.getContext();
            if (context != null && !context.startsWith(PLUGINS_CONTEXT_KEYWORD) && module.isRequired()) {
                requiredModuleNames.add(module.getModuleName());
            }
        }
        for (ModuleNeeded module : moduleList) {
            if (requiredModuleNames.contains(module.getModuleName())) {
                continue;
            }
            String context = module.getContext();
            if (context == null || !context.startsWith(PLUGINS_CONTEXT_KEYWORD) || !module.isRequired()) {
                pluginRequiredModules.add(module);
            } // else ignor
        }
        return pluginRequiredModules;
    }

    private static List<ModuleNeeded> getModulesNeededForDBConnWizard() {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        EDatabaseVersion4Drivers[] dbVersions = EDatabaseVersion4Drivers.values();
        String message = Messages.getString("ModulesNeededProvider.ModulesForDBConnWizard"); //$NON-NLS-1$ ;
        for (EDatabaseVersion4Drivers temp : dbVersions) {
            Set<String> drivers = temp.getProviderDrivers();
            for (String driver : drivers) {
                importNeedsList.add(new ModuleNeeded(temp.name(), driver, message, true));
            }
        }
        return importNeedsList;
    }

    // TODO change the name of this method to getModulesNeededForComponent()
    public static List<ModuleNeeded> getModulesNeeded(String componentName) {
        List<ModuleNeeded> toReturn = new ArrayList<ModuleNeeded>();
        for (ModuleNeeded current : getModulesNeeded()) {
            if (componentName.equals(current.getContext())) {
                toReturn.add(current);
            }
        }

        return toReturn;
    }

    /**
     * return the list of uninstalled modules needed by the Studio.
     *
     * @return the list uninstalled modules
     */
    public static List<ModuleNeeded> getUnistalledModulesNeeded() {
        List<ModuleNeeded> modulesNeeded = new ArrayList<ModuleNeeded>(getModulesNeeded());
        List<ModuleNeeded> uninstalledModules = new ArrayList<ModuleNeeded>(modulesNeeded.size());
        for (ModuleNeeded module : modulesNeeded) {
            if (module.getStatus() == ELibraryInstallStatus.NOT_INSTALLED) {
                uninstalledModules.add(module);
            } // else installed or unknow state so ignor.
        }
        return uninstalledModules;
    }

    public static void addUnknownModules(String name, String mvnURI, boolean fireLibraryChange) {
        getAllManagedModules().add(createUnknownModule(name, mvnURI));
        if (fireLibraryChange) {
            fireLibrariesChanges();
        }
    }

    private static ModuleNeeded createUnknownModule(String name, String mvnURI) {
        String message = ModuleNeeded.UNKNOWN;
        ModuleNeeded unknownModule = new ModuleNeeded(message, name, message, false);
        unknownModule.setMavenUri(mvnURI);
        return unknownModule;
    }

    public static void userRemoveUnusedModules(String urlOrName) {
        ModuleNeeded needed = null;
        for (ModuleNeeded module : allManagedModules) {
            if (module.getModuleName().equals(urlOrName) || module.getContext().equals(urlOrName)) {
                needed = module;
                break;
            }
        }
        if (needed != null) {
            getAllManagedModules().remove(needed);
        }
    }

    public static Set<ModuleNeeded> getSystemRunningModules() {
        // only routine have system item
        return new HashSet<>(collectCodesModuleNeeded(new ArrayList<IRepositoryViewObject>(), new HashSet<String>(), true));
    }

    public static Set<ModuleNeeded> getRunningModules(boolean includeCodesJar) {
        Set<ModuleNeeded> runningModules = new HashSet<ModuleNeeded>();
        runningModules.addAll(getCodesModuleNeededs(ERepositoryObjectType.ROUTINES));
        runningModules.addAll(getCodesModuleNeededs(ERepositoryObjectType.BEANS));
        // FIXME for now should not add custom jar modules to all list, later add unless make sure all safe
        if (includeCodesJar) {
            // runningModules.addAll(getAllCodesJarModuleNeededs());
        }
        return runningModules;
    }

    public static Set<ModuleNeeded> getCodesModuleNeededs(ERepositoryObjectType type) {
        if (type == null) {
            return Collections.emptySet();
        }
        Set<ModuleNeeded> codesModules = new HashSet<>();
        if (type.equals(ERepositoryObjectType.ROUTINES)) {
            codesModules.addAll(getSystemRunningModules());
        }
        codesModules.addAll(getModulesNeededForRoutines(type));
        return codesModules;
    }

    /**
     * get ModuleNeeded for all codesjars
     */
    public static Set<ModuleNeeded> getAllCodesJarModuleNeededs() {
        Set<ModuleNeeded> modules = new HashSet<>();
        CodesJarResourceCache.getAllCodesJars().stream().forEach(info -> modules.addAll(createModuleNeededFromCodesJar(info)));
        return modules;
    }

    /**
     * get ModuleNeeded for target codesjar
     */
    public static Set<ModuleNeeded> getCodesJarModuleNeededs(CodesJarInfo info) {
        return createModuleNeededFromCodesJar(info);
    }

    public static Set<ModuleNeeded> updateModulesNeededForRoutine(Item routineItem) {
        String label = ERepositoryObjectType.getItemType(routineItem).getLabel();
        String currentContext = label + " " + routineItem.getProperty().getLabel();
        Set<ModuleNeeded> modulesNeeded = new HashSet<>();
        if (routineItem instanceof RoutineItem) {
            modulesNeeded.addAll(createModuleNeededFromCodeItem(routineItem));
        } else if (routineItem instanceof RoutinesJarItem) {
            modulesNeeded.addAll(createModuleNeededFromCodesJar(CodesJarInfo.create(routineItem.getProperty())));
        }
        Set<String> routinModuleNames = new HashSet<String>();
        for (ModuleNeeded module : modulesNeeded) {
            routinModuleNames.add(module.getModuleName());
        }
        // remove old modules from the two lists
        Set<ModuleNeeded> oldModules = new HashSet<ModuleNeeded>();
        for (ModuleNeeded existingModule : getModulesNeeded()) {
            if (currentContext.equals(existingModule.getContext()) || (ModuleNeeded.UNKNOWN.equals(existingModule.getContext())
                    && routinModuleNames.contains(existingModule.getModuleName()))) {
                oldModules.add(existingModule);
            }
        }
        getModulesNeeded().removeAll(oldModules);
        oldModules = new HashSet<ModuleNeeded>();
        for (ModuleNeeded existingModule : getAllManagedModules()) {
            if (currentContext.equals(existingModule.getContext()) || (ModuleNeeded.UNKNOWN.equals(existingModule.getContext())
                    && routinModuleNames.contains(existingModule.getModuleName()))) {
                oldModules.add(existingModule);
            }
        }
        getAllManagedModules().removeAll(oldModules);

        getModulesNeeded().addAll(modulesNeeded);
        getAllManagedModules().addAll(modulesNeeded);
        return modulesNeeded;
    }

    public static void addChangeLibrariesListener(IChangedLibrariesListener listener) {
        listeners.add(listener);
    }

    public static void removeChangeLibrariesListener(IChangedLibrariesListener listener) {
        listeners.remove(listener);
    }

    public static void fireLibrariesChanges() {
        for (IChangedLibrariesListener current : new ArrayList<>(listeners)) {
            current.afterChangingLibraries();
        }
    }

    public static String getOrignalMvnURIIfCustom(String mvnPath) {
        String mvnUri = TalendQuoteUtils.removeQuotesIfExist(mvnPath);
        if (MavenUrlHelper.isMvnUrl(mvnUri)) {
            for (String key : CustomUriManager.getInstance().keySet()) {
                String value = CustomUriManager.getInstance().get(key);
                if (mvnUri.equals(value)) {
                    return key;
                }
            }
        }
        return mvnPath;
    }

    private static void checkInstallStatus(Collection<ModuleNeeded> importNeedsList) {
        if (!importNeedsList.isEmpty()) {
            for (ModuleNeeded mod : importNeedsList) {
                if (ELibraryInstallStatus.INSTALLED != mod.getStatus()) {
                    installModuleForRountine = true;
                }
            }
            modulesForRountine.addAll(importNeedsList);
        }
    }

    public static boolean installModuleForRoutineOrBeans() {
        return installModuleForRountine;
    }

    public static void setInstallModuleForRoutineOrBeans() {
        boolean allSet = true;
        for (ModuleNeeded mod : modulesForRountine) {
            if (ELibraryInstallStatus.INSTALLED != mod.getStatus()) {
                allSet = false;
            }
        }
        if (allSet) {
            installModuleForRountine = false;
        }
    }
}
