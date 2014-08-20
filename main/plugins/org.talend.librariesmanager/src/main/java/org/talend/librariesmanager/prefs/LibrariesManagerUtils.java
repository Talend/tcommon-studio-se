// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.librariesmanager.model.ModulesNeededProvider;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 */
public class LibrariesManagerUtils {

    public static final String BUNDLE_DI = "org.talend.librariesmanager";

    public static final String TALEND_LIBRARY_PATH = "talend.library.path"; //$NON-NLS-1$

    public static String getLibrariesPath(ECodeLanguage language) {
        String libPath = System.getProperty(TALEND_LIBRARY_PATH);
        if (libPath != null) {
            return libPath;
        }
        return Platform.getInstallLocation().getURL().getFile() + "lib/java";
    }

    public static List<ModuleNeeded> getNotInstalledModules(List<ModuleNeeded> modules) {
        List<ModuleNeeded> updatedModules = new ArrayList<ModuleNeeded>();
        // get module from provider incase it is rested
        List<ModuleNeeded> modulesNeeded = ModulesNeededProvider.getModulesNeeded();
        if (modules != null) {
            for (ModuleNeeded module : modules) {
                for (ModuleNeeded fromProvider : modulesNeeded) {
                    if (fromProvider.getModuleName().equals(module.getModuleName())
                            && fromProvider.getContext().equals(module.getContext())
                            && ELibraryInstallStatus.NOT_INSTALLED == fromProvider.getStatus()
                            && fromProvider.getRequiredIf().equals(module.getRequiredIf())) {
                        updatedModules.add(fromProvider);
                        break;
                    }
                }
            }
        }
        return updatedModules;
    }
}
