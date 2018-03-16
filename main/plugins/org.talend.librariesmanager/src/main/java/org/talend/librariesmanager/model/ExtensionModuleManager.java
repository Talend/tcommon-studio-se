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
package org.talend.librariesmanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.extensions.ExtensionImplementationProvider;
import org.talend.commons.utils.workbench.extensions.ExtensionPointLimiterImpl;
import org.talend.commons.utils.workbench.extensions.IExtensionPointLimiter;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.designer.core.model.utils.emf.component.IMPORTType;
import org.talend.librariesmanager.i18n.Messages;

/**
 *
 * created by ycbai on 2014-5-7 Detailled comment
 *
 */
public class ExtensionModuleManager {

    public final static String EXT_ID = "org.talend.core.runtime.librariesNeeded"; //$NON-NLS-1$

    public final static String MODULE_ELE = "libraryNeeded"; //$NON-NLS-1$

    public final static String MODULE_GROUP_ELE = "libraryNeededGroup"; //$NON-NLS-1$

    public final static String LIBRARY_ELE = "library"; //$NON-NLS-1$

    public final static String GROUP_ELE = "group"; //$NON-NLS-1$

    public final static String ID_ATTR = "id"; //$NON-NLS-1$

    public final static String NAME_ATTR = "name"; //$NON-NLS-1$

    public final static String CONTEXT_ATTR = "context"; //$NON-NLS-1$

    public final static String MESSAGE_ATTR = "message"; //$NON-NLS-1$

    public final static String REQUIRED_ATTR = "required"; //$NON-NLS-1$

    public final static String URIPATH_ATTR = "uripath"; //$NON-NLS-1$

    public final static String MVN_URI_ATTR = "mvn_uri"; //$NON-NLS-1$

    public final static String BUNDLEID_ATTR = "bundleID"; //$NON-NLS-1$

    public final static String EXCLUDE_DEPENDENCIES_ATTR = "excludeDependencies";

    public final static String DEPENDENCY_TYPE_NONE = "NONE";

    public final static String DEPENDENCY_TYPE_TOP = "TOP";

    public final static String DEPENDENCY_TYPE_CHILD = "CHILD";

    public final static String DESC_ATTR = "description"; //$NON-NLS-1$

    public final static String PATH_SEP = "/"; //$NON-NLS-1$

    public final static String URIPATH_PREFIX = "platform:/plugin/"; //$NON-NLS-1$

    public final static String URIPATH_PREFIX_INTERNAL = "platform:/base/plugins/"; //$NON-NLS-1$

    public final static String DEFAULT_LIB_FOLDER = "lib"; //$NON-NLS-1$

    // cache
    public List<IConfigurationElement> moduleGroupElementsCache = null;

    public List<IConfigurationElement> moduleElementsCache = null;
    
    public Map<String, List<ModuleNeeded>> groupMapLibraryCache = new HashMap<String, List<ModuleNeeded>>();

    public Map<String, ModuleNeeded> modulesNameLibraryCache = new HashMap<String, ModuleNeeded>();

    public Map<String, ModuleNeeded> modulesIdLibraryCache = new HashMap<String, ModuleNeeded>();
    
    private static ExtensionModuleManager manager = null;

    private ILibraryManagerService libManagerService;

    private ExtensionModuleManager() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
        }
    }

    public static final ExtensionModuleManager getInstance() {
        if (manager == null) {
            manager = new ExtensionModuleManager();
        }
        return manager;
    }

    public List<ModuleNeeded> getModuleNeeded(String id) {
        return getModuleNeeded(id, false);
    }

    public List<ModuleNeeded> getModuleNeeded(String id, boolean isGroup) {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        if (id == null) {
            return importNeedsList;
        }
        if (isGroup) {
            collectGroupModules(id, importNeedsList);
        } else {
            collectSingleModule(id, importNeedsList);
        }

        return importNeedsList;
    }

    public List<ModuleNeeded> getModuleNeededForComponent(String context, IMPORTType importType) {
        List<ModuleNeeded> importNeedsList = new ArrayList<ModuleNeeded>();
        String id = null;
        boolean isGroup = false;
        String module = StringUtils.trimToNull(importType.getMODULE());
        String moduleGroup = StringUtils.trimToNull(importType.getMODULEGROUP());
        if (module != null) {
            id = module;
        } else if (moduleGroup != null) {
            id = moduleGroup;
            isGroup = true;
        }
        List<ModuleNeeded> modulesNeeded = getModuleNeeded(id, isGroup);
        for (ModuleNeeded moduleNeeded : modulesNeeded) {
            String msg = importType.getMESSAGE();
            if (msg == null) {
                msg = Messages.getString("modules.required"); //$NON-NLS-1$
            }
            moduleNeeded.setContext(context);
            moduleNeeded.setInformationMsg(msg);
            moduleNeeded.setRequired(importType.isREQUIRED());
            moduleNeeded.setMrRequired(importType.isMRREQUIRED());
            moduleNeeded.setRequiredIf(importType.getREQUIREDIF());
            moduleNeeded.setShow(importType.isSHOW());
            if (!StringUtils.isEmpty(importType.getMVN())) {
                moduleNeeded.setMavenUri(importType.getMVN());
            }
            if (importType.getUrlPath() != null && libManagerService.checkJarInstalledFromPlatform(importType.getUrlPath())) {
                moduleNeeded.setModuleLocaion(importType.getUrlPath());
            }
            ModulesNeededProvider.initBundleID(importType, moduleNeeded);
            importNeedsList.add(moduleNeeded);
        }

        return importNeedsList;
    }


    private void collectSingleModule(String id, List<ModuleNeeded> importNeedsList) {
        if (id == null || importNeedsList == null) {
            return;
        }
        ModuleNeeded moduleNeeded = this.getModuleIdMapCache().get(id);
        if (moduleNeeded == null) {
            moduleNeeded = this.getModuleNameMapCache().get(id);
        }
        if (moduleNeeded != null) {
            importNeedsList.add(moduleNeeded);
        }
    }

    /**
     * DOC xlwang Comment method "collectGroupModules".
     * 
     * @param groupId
     * @param importNeedsList
     */
    private void collectGroupModules(String groupId, List<ModuleNeeded> importNeedsList) {
        List<ModuleNeeded> list = this.getModuleGroupMapCache().get(groupId);
        if (list != null && !list.isEmpty()) {
            importNeedsList.addAll(list);
        }
    }

    /**
     * If uripath is null we will seek the jar from /lib/${jarName} from the contributor plugin of the lib extension
     * point.
     * <p>
     *
     * DOC ycbai Comment method "getFormalModulePath".
     *
     * @param uriPath
     * @param current
     * @return the formal module path which start with "platform:/plugin/" or "platform:/base/plugins/".
     */
    public String getFormalModulePath(String uriPath, IConfigurationElement current) {
        StringBuffer expectJarPath = new StringBuffer();
        if (StringUtils.trimToNull(uriPath) == null) {
            String jarName = current.getAttribute(NAME_ATTR);
            IContributor contributor = current.getContributor();
            expectJarPath.append(URIPATH_PREFIX);
            expectJarPath.append(contributor.getName());
            expectJarPath.append(PATH_SEP);
            expectJarPath.append(DEFAULT_LIB_FOLDER);
            expectJarPath.append(PATH_SEP);
            expectJarPath.append(jarName);
        } else {
            if (!uriPath.startsWith(URIPATH_PREFIX) && !uriPath.startsWith(URIPATH_PREFIX_INTERNAL)) {
                expectJarPath.append(URIPATH_PREFIX);
            }
            expectJarPath.append(uriPath);
        }

        return expectJarPath.toString();
    }
    
    /**
     * DOC xlwang Comment method "clearCache".
     */
    public void clearCache() {
        groupMapLibraryCache.clear();
        modulesNameLibraryCache.clear();
        modulesIdLibraryCache.clear();
        moduleGroupElementsCache = null; // moduleGroupElemCache
        moduleElementsCache = null;// moduleElemCache
    }

    private List<IConfigurationElement> getModuleElementsCache() {
        if (moduleElementsCache == null) {
            IExtensionPointLimiter extensionPointLibraryNeeded = new ExtensionPointLimiterImpl(EXT_ID, MODULE_ELE);
            moduleElementsCache = ExtensionImplementationProvider.getInstanceV2(extensionPointLibraryNeeded);
        }
        return moduleElementsCache;
    }

    private List<IConfigurationElement> getModuleGroupElementsCache() {
        if (moduleGroupElementsCache == null) {
            IExtensionPointLimiter extensionPointLibraryNeededGroup = new ExtensionPointLimiterImpl(EXT_ID, MODULE_GROUP_ELE);
            moduleGroupElementsCache = ExtensionImplementationProvider.getInstanceV2(extensionPointLibraryNeededGroup);
        }
        return moduleGroupElementsCache;
    }

    private Map<String, ModuleNeeded> getModuleIdMapCache() {
        if (modulesIdLibraryCache == null || modulesIdLibraryCache.isEmpty()) {
            initModuleGroupMapCache();
        }
        return modulesIdLibraryCache;
    }

    private Map<String, ModuleNeeded> getModuleNameMapCache() {
        if (modulesNameLibraryCache == null || modulesNameLibraryCache.isEmpty()) {
            initModuleGroupMapCache();
        }
        return modulesNameLibraryCache;
    }

    private Map<String, List<ModuleNeeded>> getModuleGroupMapCache() {
        if (groupMapLibraryCache == null || groupMapLibraryCache.isEmpty()) {
            initGroupMapLibraryCache();
        }
        return groupMapLibraryCache;
    }

    private void initModuleGroupMapCache() {
        ModuleNeeded moduleNeeded = null;
        for (IConfigurationElement configElement : moduleElementsCache) {
            String moduleId = configElement.getAttribute(ID_ATTR);
            String moduleName = configElement.getAttribute(NAME_ATTR);
            moduleNeeded = ModulesNeededProvider.createModuleNeededInstance(configElement);
            if (moduleNeeded != null) {
                if (StringUtils.isNotBlank(moduleName)) {
                    modulesNameLibraryCache.put(moduleName, moduleNeeded);
                }
                if (StringUtils.isNotBlank(moduleId)) {
                    modulesNameLibraryCache.put(moduleId, moduleNeeded);
                }
            }
        }
    }

    private void initGroupMapLibraryCache() {
        groupMapLibraryCache.clear();
        List<IConfigurationElement> extension = getModuleGroupElementsCache();
        Map<String, List<String>> groupContainOthers = new HashMap<String, List<String>>();
        for (IConfigurationElement configElement : extension) {
            List<ModuleNeeded> importNeedsList = new LinkedList<ModuleNeeded>();
            ArrayList<String> otherGroupList = new ArrayList<String>();
            String moduleGroupId = configElement.getAttribute(ID_ATTR);
            if (moduleGroupId != null && !moduleGroupId.isEmpty()) {
                IConfigurationElement[] childrenEle = configElement.getChildren();
                for (IConfigurationElement childEle : childrenEle) {
                    String eleName = childEle.getName();
                    if (LIBRARY_ELE.equals(eleName)) {
                        collectSingleModule(childEle.getAttribute(ID_ATTR), importNeedsList);
                    } else if (GROUP_ELE.equals(eleName)) {
                        otherGroupList.add(eleName);
                    }
                }
                groupContainOthers.put(moduleGroupId,otherGroupList);//cache the sub group
                groupMapLibraryCache.put(moduleGroupId, importNeedsList);//cache the library
            }
        }

        getGroupLibrary(groupContainOthers, groupMapLibraryCache);

    }

    public void getGroupLibrary(Map<String, List<String>> groupContainOthers,
            Map<String, List<ModuleNeeded>> groupMapLibraryCache) {
        Map<String, Set<ModuleNeeded>> resultMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<String>> entry : groupContainOthers.entrySet()) {
            String groupName = entry.getKey();
            Set<ModuleNeeded> newLibs = new HashSet<>();
            List<ModuleNeeded> findedList = groupMapLibraryCache.get(groupName);
            if (findedList != null && !findedList.isEmpty()) {
                newLibs.addAll(findedList);
            }
            for (String subGroupName : entry.getValue()) {
                Set<String> recordedGroups = new HashSet<>();
                recordedGroups.add(groupName);

                Set<ModuleNeeded> subGroupLibs = getLibs(subGroupName, groupContainOthers, groupMapLibraryCache, recordedGroups);
                if (subGroupLibs != null && !subGroupLibs.isEmpty()) {
                    newLibs.addAll(subGroupLibs);
                }
            }
            resultMap.put(groupName, newLibs);
        }
        for (Map.Entry<String, List<ModuleNeeded>> entry : groupMapLibraryCache.entrySet()) {
            String groupName = entry.getKey();
            if (!resultMap.containsKey(groupName)) {
                groupMapLibraryCache.put(groupName, new ArrayList<ModuleNeeded>(resultMap.get(groupName)));// replace
            }
        }

    }

    static Set<ModuleNeeded> getLibs(String groupName, Map<String, List<String>> groupContainOthers,
            Map<String, List<ModuleNeeded>> groupMapLibraryCache, Set<String> recordedGroups) {
        if (recordedGroups.contains(groupName)) {
            ExceptionHandler.log("Module group [" + groupName + "] was cycle referenced!");
            return Collections.EMPTY_SET;
        }
        recordedGroups.add(groupName);
        Set<ModuleNeeded> resultList = new HashSet<>();
        List<ModuleNeeded> findedList = groupMapLibraryCache.get(groupName);
        if (findedList != null && !findedList.isEmpty()) {
            resultList.addAll(findedList);
        }
        List<String> groupList = groupContainOthers.get(groupName);
        if (groupList != null && !groupList.isEmpty()) {
            for (String subGroupName : groupList) {
                Set<ModuleNeeded> subGroupList = getLibs(subGroupName, groupContainOthers, groupMapLibraryCache, recordedGroups);
                resultList.addAll(subGroupList);
            }
        }
        return resultList;
    }

}
