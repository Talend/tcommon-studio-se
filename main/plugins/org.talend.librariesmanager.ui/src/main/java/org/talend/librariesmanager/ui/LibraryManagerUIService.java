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
package org.talend.librariesmanager.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.swt.dialogs.IConfigModuleDialog;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.general.LibraryInfo;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.core.model.routines.RoutineLibraryMananger;
import org.talend.core.model.routines.RoutinesUtil;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.librariesmanager.ui.dialogs.ConfigModuleDialog;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.ui.service.RoutineProviderManager;
import org.talend.librariesmanager.utils.ModulesInstaller;

/**
 * created by wchen on 2013-1-24 Detailled comment
 * 
 */
public class LibraryManagerUIService implements ILibraryManagerUIService {

    @Override
    public void installModules(final String[] jarNames) {
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                ModulesInstaller.installModules(new Shell(shell), jarNames);
            }
        });

    }

    @Override
    public void installModules(final Collection<ModuleNeeded> requiredModules) {
        PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

            @Override
            public void run() {
                Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
                ModulesInstaller.installModules(new Shell(shell), requiredModules);
            }
        });

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#getRoutinesProviders()
     */
    @Override
    public Collection<IRoutinesProvider> getRoutinesProviders(ECodeLanguage language) {
        return RoutineProviderManager.getInstance().getProviders(language);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#initializeSystemLibs()
     */
    @Override
    public void initializeSystemLibs() {
        RoutineLibraryMananger.getInstance().initializeSystemLibs();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#getRoutineAndJars()
     */
    @Override
    public Map<String, List<LibraryInfo>> getRoutineAndJars() {
        return RoutineLibraryMananger.getInstance().getRoutineAndJars();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#collectRelatedRoutines()
     */
    @Override
    public List<IRepositoryViewObject> collectRelatedRoutines(Set<String> includeRoutineIdOrNames, boolean system,
            ERepositoryObjectType type) {
        return RoutinesUtil.collectRelatedRoutines(includeRoutineIdOrNames, system, type);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#getLibrariesPath(org.talend.core.language.ECodeLanguage)
     */
    @Override
    public String getLibrariesPath(ECodeLanguage language) {
        return LibrariesManagerUtils.getLibrariesPath(language);
    }

    @Override
    public List<String> getNeedInstallModuleForBundle(String bundleName) {
        List<ModuleNeeded> allModulesNeededExtensionsForPlugin = ModulesNeededProvider.getAllModulesNeededExtensionsForPlugin();
        List<ModuleNeeded> requiredModulesForBundle = ModulesNeededProvider.filterRequiredModulesForBundle(bundleName,
                allModulesNeededExtensionsForPlugin);
        List<String> requiredJars = new ArrayList<String>(requiredModulesForBundle.size());
        ILibraryManagerService service;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            service = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(ILibraryManagerService.class);
            for (ModuleNeeded module : requiredModulesForBundle) {
                String moduleName = module.getModuleName();
                if (!service.contains(moduleName)) {
                    requiredJars.add(moduleName);
                }
            }
        }
        return requiredJars;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#isModuleInstalledForBundle(java.lang.String)
     */
    @Override
    public boolean isModuleInstalledForBundle(String bundleName) {
        if (LibManagerUiPlugin.getDefault().getBundle().getBundleContext().getProperty("osgi.dev") != null) {
            return true;
        }
        List<ModuleNeeded> allModulesNeededExtensionsForPlugin = ModulesNeededProvider.getAllModulesNeededExtensionsForPlugin();
        List<ModuleNeeded> requiredModulesForBundle = ModulesNeededProvider.filterRequiredModulesForBundle(bundleName,
                allModulesNeededExtensionsForPlugin);
        ILibraryManagerService service;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerService.class)) {
            service = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(ILibraryManagerService.class);
            for (ModuleNeeded module : requiredModulesForBundle) {
                String moduleName = module.getModuleName();
                if (!service.contains(moduleName)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerUIService#confirmDialog(org.talend.core.runtime.maven.MavenArtifact,
     * java.lang.String)
     */
    @Override
    public boolean confirmDialog(String originalJarFileName) {
        String mvnUrlForJarName = MavenUrlHelper.generateMvnUrlForJarName(originalJarFileName, true, true);
        return MessageDialog
                .openQuestion(new Shell(),
                        Messages.getString("ArtifactsDeployer.uploadJarEncounterMvnRepositroySameName.Title"), //$NON-NLS-1$
                        Messages.getString(
                                "ArtifactsDeployer.uploadJarEncounterMvnRepositroySameName.MessageContent", originalJarFileName, mvnUrlForJarName));//$NON-NLS-1$
    }

    public IConfigModuleDialog getConfigModuleDialog(Shell parentShell, String initValue) {
        return new ConfigModuleDialog(parentShell, initValue);
    }
}
