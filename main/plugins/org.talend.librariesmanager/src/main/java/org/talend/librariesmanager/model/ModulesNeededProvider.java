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
package org.talend.librariesmanager.model;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.runtime.model.repository.ERepositoryStatus;
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
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.Project;
import org.talend.core.model.process.IProcess;
import org.talend.core.model.properties.Item;
import org.talend.core.model.properties.ProcessItem;
import org.talend.core.model.properties.Property;
import org.talend.core.model.properties.RoutineItem;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.core.utils.TalendCacheUtils;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.designer.core.model.utils.emf.talendfile.RoutinesParameterType;
import org.talend.librariesmanager.i18n.Messages;
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

    /**
     * 
     */
    private static final String PLUGINS_CONTEXT_KEYWORD = "plugin:";

    private static List<ModuleNeeded> componentImportNeedsList = new ArrayList<ModuleNeeded>();;

    private static List<ModuleNeeded> unUsedModules = new ArrayList<ModuleNeeded>();

    private static boolean isCreated = false;

    private static boolean cleanDone = false;

    private static final String TALEND_COMPONENT_CACHE = "ComponentsCache.";

    private static final String TALEND_FILE_NAME = "cache";

    private static IRepositoryService service = null;
    static {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IRepositoryService.class)) {
            service = (IRepositoryService) GlobalServiceRegister.getDefault().getService(IRepositoryService.class);
        }
    }

    public static List<ModuleNeeded> getModulesNeeded() {
        // TimeMeasure.measureActive = true;
        // TimeMeasure.display = true;
        //        TimeMeasure.begin(Messages.getString("ModulesNeededProvider.0")); //$NON-NLS-1$

        /*
         * TimeMeasure.begin("ModulesNeededProvider.getModulesNeededForRoutines");
         * TimeMeasure.pause("ModulesNeededProvider.getModulesNeededForRoutines");
         * 
         * TimeMeasure.begin("ModulesNeededProvider.getModulesNeededForComponents");
         * TimeMeasure.pause("ModulesNeededProvider.getModulesNeededForComponents");
         * 
         * TimeMeasure.begin("ModulesNeededProvider.getModulesNeededForApplication");
         * TimeMeasure.pause("ModulesNeededProvider.getModulesNeededForApplication");
         * 
         * TimeMeasure.begin("ModulesNeededProvider.getModulesNeededForJobs");
         * TimeMeasure.pause("ModulesNeededProvider.getModulesNeededForJobs");
         */
        if (componentImportNeedsList.isEmpty()) {
            // TimeMeasure.step("ModulesNeededProvider.getModulesNeededForRoutines");
            componentImportNeedsList.addAll(getRunningModules());
            //            TimeMeasure.step(Messages.getString("ModulesNeededProvider.1"), "ModulesNeededProvider.getModulesNeededForRoutines"); //$NON-NLS-1$ //$NON-NLS-2$

            // TimeMeasure.begin("ModulesNeededProvider.getModulesNeededForApplication");
            componentImportNeedsList.addAll(getModulesNeededForApplication());
            if (PluginChecker.isMetadataPluginLoaded()) {
                componentImportNeedsList.addAll(getModulesNeededForDBConnWizard());
            }
            //            TimeMeasure.step("ModulesNeededProvider.getAllMoudlesNeeded", "ModulesNeededProvider.getModulesNeededForApplication"); //$NON-NLS-1$ //$NON-NLS-2$

            // TimeMeasure.resume("ModulesNeededProvider.getModulesNeededForJobs");
            // if (LanguageManager.getCurrentLanguage().equals(ECodeLanguage.JAVA)) {
            // componentImportNeedsList.addAll(getModulesNeededForJobs());
            // }
            //            TimeMeasure.step("ModulesNeededProvider.getAllMoudlesNeeded", "ModulesNeededProvider.getModulesNeededForJobs"); //$NON-NLS-1$ //$NON-NLS-2$

            // TimeMeasure.resume("ModulesNeededProvider.getModulesNeededForComponents");
            // MOD qiongli TOP NO nedd to add the related components ModuleNeeded
            if (!org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
                componentImportNeedsList.addAll(getModulesNeededForComponents());
            }
            //            TimeMeasure.step("ModulesNeededProvider.getAllMoudlesNeeded", "ModulesNeededProvider.getModulesNeededForComponents"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        /*
         * TimeMeasure.end("ModulesNeededProvider.getModulesNeededForRoutines");
         * 
         * TimeMeasure.end("ModulesNeededProvider.getModulesNeededForComponents");
         * 
         * TimeMeasure.end("ModulesNeededProvider.getModulesNeededForApplication");
         * 
         * TimeMeasure.end("ModulesNeededProvider.getModulesNeededForJobs");
         */// TimeMeasure.measureActive = false;
           // TimeMeasure.display = false;
           //        TimeMeasure.end("ModulesNeededProvider.getAllMoudlesNeeded"); //$NON-NLS-1$
        return componentImportNeedsList;
    }

    /**
     * DOC sgandon Comment method "getModulesNeeded".
     * 
     * @param moduleName, must not be null
     * @return all modules needed matching the module name
     */
    public static List<ModuleNeeded> getModulesNeededForName(String moduleName) {
        ArrayList<ModuleNeeded> modulesMatching = new ArrayList<ModuleNeeded>();
        for (ModuleNeeded modNeed : getModulesNeeded()) {
            if (moduleName.equals(modNeed.getModuleName())) {
                modulesMatching.add(modNeed);
            }
        }
        return modulesMatching;
    }

    public static ModuleNeeded getModuleNeededById(String id) {
        ModuleNeeded result = null;

        List<ModuleNeeded> modulesNeeded = getModulesNeeded();
        for (ModuleNeeded moduleNeeded : modulesNeeded) {
            if (id.equals(moduleNeeded.getId())) {
                result = moduleNeeded;
                break;
            }
        }

        return result;
    }

    public static List<String> getModulesNeededNames() {
        List<String> componentImportNeedsListNames = new ArrayList<String>();
        for (ModuleNeeded m : getModulesNeeded()) {
            componentImportNeedsListNames.add(m.getModuleName());
        }
        return componentImportNeedsListNames;
    }

    public static void reset() {
        componentImportNeedsList.clear();
    }

    /**
     * ftang Comment method "resetCurrentJobNeededModuleList".
     * 
     * @param process
     */
    public static void resetCurrentJobNeededModuleList(IProcess process) {
        // Step 1: remove all modules for current job;
        List<ModuleNeeded> moduleForCurrentJobList = new ArrayList<ModuleNeeded>(5);
        for (ModuleNeeded module : componentImportNeedsList) {
            if (module.getContext().equals("Job " + process.getName())) { //$NON-NLS-1$
                moduleForCurrentJobList.add(module);
            }
        }
        componentImportNeedsList.removeAll(moduleForCurrentJobList);

        Set<String> neededLibraries = process.getNeededLibraries(false);
        if (neededLibraries != null) {
            for (String neededLibrary : neededLibraries) {
                boolean alreadyInImports = false;
                for (ModuleNeeded module : componentImportNeedsList) {
                    if (module.getModuleName().equals(neededLibrary)) {
                        alreadyInImports = true;
                    }
                }
                if (alreadyInImports) {
                    continue;
                }

                // Step 2: re-add specific modules
                ModuleNeeded toAdd = new ModuleNeeded("Job " + process.getName(), neededLibrary, //$NON-NLS-1$
                        "Required for the job " + process.getName() + ".", true); //$NON-NLS-1$ //$NON-NLS-2$

                componentImportNeedsList.add(toAdd);

                // Step 3: remove added modules from unusedModule list
                ModuleNeeded unusedModule = null;
                for (ModuleNeeded module : unUsedModules) {
                    if (module.getModuleName().equals(neededLibrary)) {
                        unusedModule = module;
                    }
                }
                if (unusedModule != null) {
                    unUsedModules.remove(unusedModule);
                }
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
                IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(
                        IComponentsService.class);
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
        // filter modules configured in extension but jar do not exsit in the UrlPath
        ILibraryManagerService libManagerService = null;
        if (importType.getMODULEGROUP() == null
                && GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
            final Iterator<ModuleNeeded> iterator = importModuleFromExtension.iterator();
            while (iterator.hasNext()) {
                final ModuleNeeded next = iterator.next();
                if (next.getModuleLocaion() != null && !libManagerService.checkJarInstalledFromPlatform(next.getModuleLocaion())) {
                    iterator.remove();
                }
            }
        }
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

    public static List<ModuleNeeded> getModulesNeededForJobs() {
        IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();

        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();

        try {
            importNeedsList = repositoryFactory.getModulesNeededForJobs();
        } catch (PersistenceException e) {
            CommonExceptionHandler.process(e);
        }

        return importNeedsList;
    }

    public static Set<ModuleNeeded> getModulesNeededForProcess(ProcessItem processItem, IProcess process) {
        Set<ModuleNeeded> modulesNeeded = new HashSet<ModuleNeeded>();
        List<ModuleNeeded> modulesNeededForRoutines = ModulesNeededProvider.getModulesNeededForRoutines(processItem,
                ERepositoryObjectType.ROUTINES);
        modulesNeeded.addAll(modulesNeededForRoutines);
        List<ModuleNeeded> modulesNeededForPigudf = ModulesNeededProvider.getModulesNeededForRoutines(processItem,
                ERepositoryObjectType.PIG_UDF);
        modulesNeeded.addAll(modulesNeededForPigudf);
        if (ComponentCategory.CATEGORY_4_CAMEL.getName().equals(process.getComponentsType())) {
            // route do not save any relateionship with beans , so add all for now
            Set<ModuleNeeded> modulesNeededForBean = ModulesNeededProvider.getCodesModuleNeededs(
                    ERepositoryObjectType.getType("BEANS"), false);
            modulesNeeded.addAll(modulesNeededForBean);
        }
        return modulesNeeded;
    }

    public static List<ModuleNeeded> getModulesNeededForRoutines(ProcessItem processItem, ERepositoryObjectType type) {
        return getModulesNeededForRoutines(new ProcessItem[] { processItem }, type);
    }

    /**
     * 
     * ggu Comment method "getModulesNeededForRoutines".
     * 
     */
    @SuppressWarnings("unchecked")
    public static List<ModuleNeeded> getModulesNeededForRoutines(ProcessItem[] processItems, ERepositoryObjectType type) {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();

        if (processItems != null) {
            Set<String> systemRoutines = new HashSet<String>();
            Set<String> userRoutines = new HashSet<String>();
            if (service != null) {
                IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();

                try {
                    List<IRepositoryViewObject> routines = repositoryFactory.getAll(type, true);

                    for (ProcessItem p : processItems) {
                        if (p == null || p.getProcess() == null || p.getProcess().getParameters() == null
                                || p.getProcess().getParameters().getRoutinesParameter() == null) {
                            continue;
                        }
                        for (RoutinesParameterType infor : (List<RoutinesParameterType>) p.getProcess().getParameters()
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
                    }
                } catch (PersistenceException e) {
                    CommonExceptionHandler.process(e);
                }
            }
            ILibraryManagerUIService libUiService = null;
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerUIService.class);
            }

            if (!systemRoutines.isEmpty() && libUiService != null) {
                List<IRepositoryViewObject> systemRoutineItems = libUiService.collectRelatedRoutines(systemRoutines, true, type);
                importNeedsList.addAll(collectModuleNeeded(systemRoutineItems, systemRoutines, true));
            }

            if (!userRoutines.isEmpty() && libUiService != null) {
                List<IRepositoryViewObject> collectUserRoutines = libUiService.collectRelatedRoutines(userRoutines, false, type);
                importNeedsList.addAll(collectModuleNeeded(collectUserRoutines, userRoutines, false));
            }
        }

        return importNeedsList;
    }

    private static Property findRoutinesPropery(String id, String name, List<IRepositoryViewObject> routines,
            ERepositoryObjectType type) {
        if (service != null) {
            IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
            getRefRoutines(routines, ProjectManager.getInstance().getCurrentProject().getEmfProject(), type);
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

    private static List<ModuleNeeded> collectModuleNeeded(List<IRepositoryViewObject> routineItems, Set<String> routineIdOrNames,
            boolean system) {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        if (org.talend.commons.utils.platform.PluginChecker.isOnlyTopLoaded()) {
            return importNeedsList;
        }
        if (!routineItems.isEmpty()) {
            for (IRepositoryViewObject object : routineItems) {
                if (routineIdOrNames.contains(object.getLabel()) && system || routineIdOrNames.contains(object.getId())
                        && !system) {
                    Item item = object.getProperty().getItem();
                    if (item instanceof RoutineItem) {
                        RoutineItem routine = (RoutineItem) item;
                        importNeedsList.addAll(createModuleNeededFromRoutine(routine));
                    }
                }
            }
        }

        // add modules which internal system routine(which under system folder and don't have item) need.
        if (system) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerUIService.class);
                Set<String> routinesName = new HashSet<>();
                for (IRoutinesProvider routineProvider : libUiService.getRoutinesProviders(ECodeLanguage.JAVA)) {
                    for (URL url : routineProvider.getSystemRoutines()) {
                        String[] fragments = url.toString().split("/"); //$NON-NLS-1$
                        String label = fragments[fragments.length - 1];
                        String[] tmp = label.split("\\."); //$NON-NLS-1$
                        String routineName = tmp[0];
                        routinesName.add(routineName);
                    }
                    for (URL url : routineProvider.getTalendRoutines()) {
                        String[] fragments = url.toString().split("/"); //$NON-NLS-1$
                        String label = fragments[fragments.length - 1];
                        String[] tmp = label.split("\\."); //$NON-NLS-1$
                        String routineName = tmp[0];
                        routinesName.add(routineName);
                    }
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
                        importNeedsList.add(toAdd);
                    }
                }
            }
        }

        Set<String> dedupModulesList = new HashSet<String>();
        Iterator<ModuleNeeded> it = importNeedsList.iterator();
        while (it.hasNext()) {
            ModuleNeeded module = it.next();
            // try to keep only real files (with extension, no matter be jar or other)
            // in some case it's not a real library, but just a text.
            if (!module.getModuleName().contains(".")) { //$NON-NLS-1$
                it.remove();
            } else if (dedupModulesList.contains(module.getModuleName())) {
                it.remove();
            } else {
                dedupModulesList.add(module.getModuleName());
            }
        }

        return new ArrayList<ModuleNeeded>(importNeedsList);
    }

    private static List<ModuleNeeded> createModuleNeededFromRoutine(RoutineItem routine) {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        if (routine != null) {
            EList imports = routine.getImports();
            for (Object o : imports) {
                IMPORTType currentImport = (IMPORTType) o;
                // FIXME SML i18n
                ModuleNeeded toAdd = new ModuleNeeded("Routine " + currentImport.getNAME(), currentImport.getMODULE(), //$NON-NLS-1$
                        currentImport.getMESSAGE(), currentImport.isREQUIRED());
                // toAdd.setStatus(ELibraryInstallStatus.INSTALLED);
                importNeedsList.add(toAdd);
            }
        }
        return importNeedsList;
    }

    public static List<ModuleNeeded> getModulesNeededForRoutines(ERepositoryObjectType type) {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        if (service != null && type != null) {
            IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
            try {
                List<IRepositoryViewObject> routines = repositoryFactory.getAll(type, true);
                getRefRoutines(routines, ProjectManager.getInstance().getCurrentProject().getEmfProject(), type);
                for (IRepositoryViewObject current : routines) {
                    if (!current.isDeleted()) {
                        Item item = current.getProperty().getItem();
                        RoutineItem routine = (RoutineItem) item;
                        importNeedsList.addAll(createModuleNeededFromRoutine(routine));
                    }
                }
            } catch (PersistenceException e) {
                CommonExceptionHandler.process(e);
            }
        }
        return importNeedsList;
    }

    private static void getRefRoutines(List<IRepositoryViewObject> routines,
            org.talend.core.model.properties.Project mainProject, ERepositoryObjectType type) {
        if (service != null) {
            IProxyRepositoryFactory repositoryFactory = service.getProxyRepositoryFactory();
            try {
                if (mainProject.getReferencedProjects() != null) {
                    for (Project referencedProject : ProjectManager.getInstance().getAllReferencedProjects()) {
                        routines.addAll(repositoryFactory.getAll(referencedProject, type, true));
                    }
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
        module.setModuleLocaion(uripath);
        module.setId(id);
        module.setBundleName(current.getAttribute(ExtensionModuleManager.BUNDLEID_ATTR));
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
     * */
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
     * */
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
            }// else keep going
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
            }// else ignor
        }
        return pluginRequiredModules;
    }

    private static List<ModuleNeeded> getModulesNeededForDBConnWizard() {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        EDatabaseVersion4Drivers[] dbVersions = EDatabaseVersion4Drivers.values();
        String message = Messages.getString("ModulesNeededProvider.ModulesForDBConnWizard"); //$NON-NLS-1$;
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
            if (current.getContext().equals(componentName)) {
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
            }// else installed or unknow state so ignor.
        }
        return uninstalledModules;
    }

    /**
     * qiang.zhang Comment method "getImportModules".
     * 
     * @param name
     * @param context
     */
    public static void userAddImportModules(String context, String name, ELibraryInstallStatus status) {
        boolean required = true;
        String message = Messages.getString("ModulesNeededProvider.importModule"); //$NON-NLS-1$
        ModuleNeeded needed = new ModuleNeeded(context, name, message, required, status);
        componentImportNeedsList.add(needed);
    }

    public static void userAddUnusedModules(String context, String name) {
        boolean required = false;
        String message = Messages.getString("ModulesNeededProvider.unusedModule"); //$NON-NLS-1$
        ModuleNeeded needed = new ModuleNeeded(context, name, message, required, ELibraryInstallStatus.UNUSED);
        unUsedModules.add(needed);
    }

    public static void userRemoveUnusedModules(String urlOrName) {
        ModuleNeeded needed = null;
        for (ModuleNeeded module : unUsedModules) {
            if (module.getModuleName().equals(urlOrName) || module.getContext().equals(urlOrName)) {
                needed = module;
                break;
            }
        }
        if (needed != null) {
            unUsedModules.remove(needed);
        }
    }

    /**
     * Getter for unUsedModules.
     * 
     * @return the unUsedModules
     */
    public static List<ModuleNeeded> getUnUsedModules() {
        return unUsedModules;
    }

    public static Set<ModuleNeeded> getSystemRunningModules() {
        // only routine have system item
        return getCodesModuleNeededs(ERepositoryObjectType.ROUTINES, true);
    }

    public static Set<ModuleNeeded> getRunningModules() {
        Set<ModuleNeeded> runningModules = new HashSet<ModuleNeeded>();

        runningModules.addAll(getCodesModuleNeededs(ERepositoryObjectType.ROUTINES, false));
        runningModules.addAll(getCodesModuleNeededs(ERepositoryObjectType.getType("BEANS"), false)); //$NON-NLS-1$
        runningModules.addAll(getCodesModuleNeededs(ERepositoryObjectType.PIG_UDF, false));
        return runningModules;
    }

    public static Set<ModuleNeeded> getCodesModuleNeededs(ERepositoryObjectType type, boolean systemOnly) {
        if (type == null) {
            return Collections.emptySet();
        }
        Set<ModuleNeeded> codesModules = new HashSet<ModuleNeeded>();

        if (type.equals(ERepositoryObjectType.ROUTINES)) {
            // add the system routines modules
            codesModules.addAll(collectModuleNeeded(new ArrayList<IRepositoryViewObject>(), new HashSet<String>(), true));

        }
        if (!systemOnly || !type.equals(ERepositoryObjectType.ROUTINES)) {
            codesModules.addAll(getModulesNeededForRoutines(type));
        }
        return codesModules;
    }

    private static List<ILibrariesService.IChangedLibrariesListener> changedLibrariesListeners = new ArrayList<ILibrariesService.IChangedLibrariesListener>();

    public static boolean addChangedLibrariesListener(ILibrariesService.IChangedLibrariesListener listener) {
        return changedLibrariesListeners.add(listener);
    }

    public static boolean removeChangedLibrariesListener(ILibrariesService.IChangedLibrariesListener listener) {
        return changedLibrariesListeners.remove(listener);
    }

    public static void fireChangedLibrariesListener() {
        for (ILibrariesService.IChangedLibrariesListener listener : changedLibrariesListeners) {
            listener.afterChangingLibraries();
        }
    }
}
