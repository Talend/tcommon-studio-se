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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
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

    private static ExtensionModuleManager manager = new ExtensionModuleManager();

    private ILibraryManagerService libManagerService;

    private ExtensionModuleManager() {
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            libManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerService.class);
        }
    }

    public static synchronized final ExtensionModuleManager getInstance() {
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
        IExtensionPointLimiter extensionPoint = new ExtensionPointLimiterImpl(EXT_ID, MODULE_ELE);
        List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(extensionPoint);
        // Find the module definition by id first.
        ModuleNeeded moduleNeeded = collectModuleAndMap(extension, id);
        if (moduleNeeded == null) { // If cannot find it then find it by name.
            moduleNeeded = collectModuleAndMap(extension, id, true);
        }
        if (moduleNeeded != null) {
            importNeedsList.add(moduleNeeded);
        }
    }

    private ModuleNeeded collectModuleAndMap(List<IConfigurationElement> extension, String id) {
        return collectModuleAndMap(extension, id, false);
    }

    private ModuleNeeded collectModuleAndMap(List<IConfigurationElement> extension, String id, boolean byName) {
        ModuleNeeded moduleNeeded = null;
        for (IConfigurationElement configElement : extension) {
            String moduleIdOrName = configElement.getAttribute(ID_ATTR);
            if (byName) {
                moduleIdOrName = configElement.getAttribute(NAME_ATTR);
            }
            if (id.equals(moduleIdOrName)) {
                moduleNeeded = ModulesNeededProvider.createModuleNeededInstance(configElement);
                break;
            }
        }
        return moduleNeeded;
    }

    private void collectGroupModules(String groupId, List<ModuleNeeded> importNeedsList) {
        if (groupId == null || importNeedsList == null) {
            return;
        }
        IExtensionPointLimiter extensionPoint = new ExtensionPointLimiterImpl(EXT_ID, MODULE_GROUP_ELE);
        List<IConfigurationElement> extension = ExtensionImplementationProvider.getInstanceV2(extensionPoint);
        for (IConfigurationElement configElement : extension) {
            String moduleGroupId = configElement.getAttribute(ID_ATTR);
            if (groupId.equals(moduleGroupId)) {
                IConfigurationElement[] childrenEle = configElement.getChildren();
                for (IConfigurationElement childEle : childrenEle) {
                    String eleName = childEle.getName();
                    if (LIBRARY_ELE.equals(eleName)) {
                        collectSingleModule(childEle.getAttribute(ID_ATTR), importNeedsList);
                    } else if (GROUP_ELE.equals(eleName)) {
                        collectGroupModules(childEle.getAttribute(ID_ATTR), importNeedsList);
                    }
                }
            }
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

}
