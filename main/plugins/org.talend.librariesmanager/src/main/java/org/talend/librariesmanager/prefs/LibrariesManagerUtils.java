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
package org.talend.librariesmanager.prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.process.INode;
import org.talend.designer.core.IDesignerCoreService;
import org.talend.librariesmanager.model.ModulesNeededProvider;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 *
 */
public class LibrariesManagerUtils {

    public static final String BUNDLE_DI = "org.talend.librariesmanager";

    public static final String TALEND_LIBRARY_PATH = "talend.library.path"; //$NON-NLS-1$

    /**
     * should be same as OsgiLoaderActivator.LIB_JAVA_SUB_FOLDER
     */
    public static final String LIB_JAVA_SUB_FOLDER = "lib/java"; //$NON-NLS-1$

    public static String getLibrariesPath() {
        String libPath = System.getProperty(TALEND_LIBRARY_PATH);
        if (libPath != null) {
            return libPath;
        }
        try {
            return Platform.getConfigurationLocation().getDataArea(LIB_JAVA_SUB_FOLDER).getFile();
        } catch (IOException e) {
            //
        }
        return Platform.getConfigurationLocation().getURL().getFile() + LIB_JAVA_SUB_FOLDER;
    }

    public static String getLibrariesPath(ECodeLanguage language) {
        return getLibrariesPath();
    }

    /**
     *
     * @deprecated should use getNotInstalledModules(INode) instead.
     */
    @Deprecated
    public static List<ModuleNeeded> getNotInstalledModules(List<ModuleNeeded> modules) {
        List<ModuleNeeded> updatedModules = new ArrayList<ModuleNeeded>();
        // get module from provider incase it is rested
        Set<ModuleNeeded> modulesNeeded = ModulesNeededProvider.getModulesNeeded();
        if (modules != null) {
            for (ModuleNeeded module : modules) {
                for (ModuleNeeded fromProvider : modulesNeeded) {
                    if (fromProvider.getModuleName().equals(module.getModuleName())
                            && ELibraryInstallStatus.NOT_INSTALLED == fromProvider.getStatus()) {
                        updatedModules.add(module);
                        break;
                    }
                }
            }
        }
        return updatedModules;
    }

    public static List<ModuleNeeded> getNotInstalledModules(INode node) {
        List<ModuleNeeded> updatedModules = new ArrayList<ModuleNeeded>();

        List<ModuleNeeded> nodeModulesList = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IDesignerCoreService.class)) {
            IDesignerCoreService service = (IDesignerCoreService) GlobalServiceRegister.getDefault().getService(
                    IDesignerCoreService.class);
            Set<ModuleNeeded> neededLibraries = service.getNeededModules(node, false);
            nodeModulesList = new ArrayList<ModuleNeeded>(neededLibraries);
        } else {
            nodeModulesList = node.getModulesNeeded();
        }

        for (ModuleNeeded module : nodeModulesList) {
            if (!module.isDynamic() && module.getStatus() == ELibraryInstallStatus.NOT_INSTALLED
                    && module.isRequired(node.getElementParameters())) {
                updatedModules.add(module);
            }

        }
        return updatedModules;
    }
}
