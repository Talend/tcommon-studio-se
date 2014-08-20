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
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.generation.JavaUtils;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.PluginChecker;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.core.utils.TalendCacheUtils;
import org.talend.designer.codegen.PigTemplate;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.repository.ProjectManager;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 * $Id$
 * 
 */
public class JavaLibrariesService extends AbstractLibrariesService {

    private static Logger log = Logger.getLogger(JavaLibrariesService.class);

    private static ILibraryManagerService repositoryBundleService = (ILibraryManagerService) GlobalServiceRegister.getDefault()
            .getService(ILibraryManagerService.class);

    public static final String SOURCE_JAVA_ROUTINES_FOLDER = "routines"; //$NON-NLS-1$

    public static final String SOURCE_JAVA_PIGUDF_FOLDER = "pigudf"; //$NON-NLS-1$

    public static final String SOURCE_JAVA_BEANS_FOLDER = "beans"; //$NON-NLS-1$

    private static boolean isLibSynchronized;

    @Override
    public URL getRoutineTemplate() {
        return Platform.getBundle(LibrariesManagerUtils.BUNDLE_DI).getEntry(
                "resources/java/" + SOURCE_JAVA_ROUTINES_FOLDER + "/__TEMPLATE__.java"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Override
    public URL getPigudfTemplate(PigTemplate template) {
        Bundle bundle = Platform.getBundle(LibrariesManagerUtils.BUNDLE_DI);
        return bundle.getEntry("templates/" + SOURCE_JAVA_PIGUDF_FOLDER + "/" + template.getFileName() + ".java"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    @Override
    public URL getBeanTemplate() {
        return Platform.getBundle(LibrariesManagerUtils.BUNDLE_DI).getEntry(
                "resources/java/" + SOURCE_JAVA_BEANS_FOLDER + "/__BEAN_TEMPLATE__.java"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#getSqlPatternTemplate()
     */
    @Override
    public URL getSqlPatternTemplate() {
        return Platform.getBundle(LibrariesManagerUtils.BUNDLE_DI).getEntry(
                "resources/java/" + SOURCE_SQLPATTERN_FOLDER + "/__TEMPLATE__" + TEMPLATE_SUFFIX); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#getSystemRoutines()
     */
    @Override
    public List<URL> getSystemRoutines() {
        List<URL> toReturn = new ArrayList<URL>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);

            for (IRoutinesProvider routineProvider : libUiService.getRoutinesProviders(ECodeLanguage.JAVA)) {
                toReturn.addAll(routineProvider.getSystemRoutines());
            }
        }
        return toReturn;
    }

    @Override
    public List<URL> getSystemSQLPatterns() {
        return FilesUtils.getFilesFromFolder(Platform.getBundle(LibrariesManagerUtils.BUNDLE_DI),
                "resources/java/" + SOURCE_SQLPATTERN_FOLDER, //$NON-NLS-1$
                SQLPATTERN_FILE_SUFFIX, false, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#getTalendRoutines()
     */
    @Override
    public List<URL> getTalendRoutinesFolder() throws IOException {
        List<URL> toReturn = new ArrayList<URL>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
            for (IRoutinesProvider routineProvider : libUiService.getRoutinesProviders(ECodeLanguage.JAVA)) {
                toReturn.add(routineProvider.getTalendRoutinesFolder());
            }
        }

        return toReturn;
    }

    @Override
    public List<URL> getTalendBeansFolder() throws IOException {
        List<URL> toReturn = new ArrayList<URL>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
            for (IRoutinesProvider routineProvider : libUiService.getRoutinesProviders(ECodeLanguage.JAVA)) {
                toReturn.add(routineProvider.getTalendRoutinesFolder());
            }
        }
        return toReturn;
    }

    @Override
    public List<URL> getTalendRoutines() {
        List<URL> toReturn = new ArrayList<URL>();
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
            for (IRoutinesProvider routineProvider : libUiService.getRoutinesProviders(ECodeLanguage.JAVA)) {
                toReturn.addAll(routineProvider.getTalendRoutines());
            }
        }

        return toReturn;
    }

    @Override
    public void checkInstalledLibraries() {
        List<ModuleNeeded> toCheck = ModulesNeededProvider.getModulesNeeded();
        Set<String> existLibraries = repositoryBundleService.list();
        for (ModuleNeeded current : toCheck) {
            if (existLibraries.contains(current.getModuleName())) {
                current.setStatus(ELibraryInstallStatus.INSTALLED);
            } else {
                current.setStatus(ELibraryInstallStatus.NOT_INSTALLED);
            }
        }

        List<String> modulesNeededNames = ModulesNeededProvider.getModulesNeededNames();
        ModulesNeededProvider.getUnUsedModules().clear();
        for (String library : existLibraries) {
            if (!modulesNeededNames.contains(library)) {
                ModulesNeededProvider.userAddUnusedModules("Unknown", library); //$NON-NLS-1$
            }
        }
    }

    @Override
    public void syncLibrariesFromApp(IProgressMonitor... monitorWrap) {
        // do nothing
    }

    private void deployComponentsLibs(IProgressMonitor... monitorWrap) throws IOException {
        Set<String> libsNeededForComponents = new HashSet<String>();
        Map<String, String> libsToRelativePath = new HashMap<String, String>();

        IComponentsService service = (IComponentsService) GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        deploy(service.getComponentsFactory().getComponents(), libsNeededForComponents, libsToRelativePath, monitorWrap);

        Map<String, File> componentsFolders = service.getComponentsFactory().getComponentsProvidersFolder();
        Set<String> contributeIdSet = componentsFolders.keySet();
        for (String contributeID : contributeIdSet) {
            File file = new File(componentsFolders.get(contributeID).toURI());
            if ("org.talend.designer.components.model.UserComponentsProvider".contains(contributeID)
                    || "org.talend.designer.components.exchange.ExchangeComponentsProvider".contains(contributeID)) {
                File target = new File(getStorageDirectory(), file.getName());
                if (file.isDirectory()) {
                    List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null);
                    if (jarFiles.size() > 0) {
                        for (File jarFile : jarFiles) {
                            String name = jarFile.getName();
                            if (!libsNeededForComponents.contains(name)) {
                                continue;
                            }
                            FilesUtils.copyFile(jarFile, target);
                        }
                    }
                } else {
                    FilesUtils.copyFile(file, target);
                }
            } else {
                List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null);
                if (jarFiles.size() > 0) {
                    for (File jarFile : jarFiles) {
                        String name = jarFile.getName();
                        if (!libsNeededForComponents.contains(name)) {
                            continue;
                        }
                        String path = libsToRelativePath.get(name);
                        int lengthBasePath = new Path(file.getParentFile().getAbsolutePath()).toPortableString().length();
                        String relativePath = new Path(jarFile.getAbsolutePath()).toPortableString().substring(lengthBasePath);
                        String moduleLocation = "platform:/plugin/" + contributeID + relativePath;
                        if (path != null) {
                            if (path.equals(moduleLocation)) {
                                continue;
                            } else {
                                CommonExceptionHandler
                                        .warn(name + " is duplicated, locations:" + path + " and:" + moduleLocation);
                                continue;
                            }
                        }
                        libsToRelativePath.put(name, moduleLocation);
                    }
                }
            }
        }
        repositoryBundleService.deploy(libsToRelativePath, monitorWrap);
    }

    private File getStorageDirectory() {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        return storageDir;
    }

    private void deploy(Set<IComponent> componentList, Set<String> libsNeededForComponents,
            Map<String, String> libsToRelativePath, IProgressMonitor... monitorWrap) {
        List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();
        Set<String> duplicateLocationJar = new HashSet<String>();

        for (IComponent component : componentList) {
            modules.addAll(component.getModulesNeeded());
        }
        deploy(modules, libsNeededForComponents, libsToRelativePath, duplicateLocationJar, monitorWrap);
        if (!duplicateLocationJar.isEmpty()) {
            for (String lib : duplicateLocationJar) {
                Set<String> components = new HashSet<String>();
                Set<String> locations = new HashSet<String>();
                for (ModuleNeeded module : modules) {
                    if (module.getModuleName().equals(lib)) {
                        components.add(module.getContext());
                        locations.add(module.getModuleLocaion());
                    }
                }
                CommonExceptionHandler.warn("Library:" + lib + " was defined with different locations.\n"
                        + "Components who define these jars are:" + components + "\n" + "Locations:" + locations);
            }
        }
    }

    private void deploy(List<ModuleNeeded> modules, Set<String> libsNeededForComponents, Map<String, String> libsToRelativePath,
            Set<String> duplicateLocationJar, IProgressMonitor... monitorWrap) {
        for (ModuleNeeded module : modules) {
            String moduleLocation = module.getModuleLocaion();
            if (moduleLocation != null && moduleLocation.startsWith("platform:/")) {
                String relativePath = libsToRelativePath.get(module.getModuleName());
                if (relativePath != null) {
                    if (relativePath.equals(moduleLocation)) {
                        continue;
                    } else {
                        if (!duplicateLocationJar.contains(moduleLocation)) {
                            duplicateLocationJar.add(module.getModuleName());
                        }
                    }
                    if (!repositoryBundleService.checkJarInstalledFromPlatform(moduleLocation)) {
                        continue;
                    }
                }
                libsToRelativePath.put(module.getModuleName(), moduleLocation);
            } else {
                libsNeededForComponents.add(module.getModuleName());
            }
        }
    }

    @Override
    public void syncLibraries(IProgressMonitor... monitorWrap) {
        // check in the libs directory of the project and add the jar with other ones.
        syncLibrariesFromLibs(monitorWrap);

        // deploy system routine libraries
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
            libUiService.initializeSystemLibs();
        }

        if (TalendCacheUtils.cleanComponentCache()) {
            repositoryBundleService.clearCache();
        }
        // Add a new system file, if exists, means all components libs are already setup, so no need to do again.
        // if clean the component cache, it will automatically recheck all libs still.
        if (!repositoryBundleService.isInitialized()) {
            // 2. Components libraries
            if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
                try {
                    deployComponentsLibs(monitorWrap);
                } catch (IOException e) {
                    ExceptionHandler.process(e);
                }
                repositoryBundleService.setInitialized();
            }
        }

        checkInstalledLibraries();

        // clean the temp library of job needed in .java\lib
        cleanTempProLib();

        log.debug(Messages.getString("JavaLibrariesService.synchronization")); //$NON-NLS-1$
        isLibSynchronized = true;
    }

    protected void updateProjectLib(String libName) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#isLibSynchronized()
     */
    @Override
    public boolean isLibSynchronized() {
        return this.isLibSynchronized;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#getPerlLibrariesPath()
     */
    @Override
    public String getPerlLibrariesPath() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.core.model.general.ILibrariesService#syncLibrariesFromLibs(org.eclipse.core.runtime.IProgressMonitor
     * [])
     */
    @Override
    public void syncLibrariesFromLibs(IProgressMonitor... monitorWrap) {
        // for feature 12877
        if (PluginChecker.isSVNProviderPluginLoaded()) {
            String path = new Path(Platform.getInstanceLocation().getURL().getPath()).toFile().getPath();
            String projectLabel = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
            path = path + File.separatorChar + projectLabel + File.separatorChar
                    + ERepositoryObjectType.getFolderName(ERepositoryObjectType.LIBS);
            File libsTargetFile = new File(path);
            repositoryBundleService.deploy(libsTargetFile.toURI(), monitorWrap);
        }
    }

    public void cleanTempProLib() {
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IProject prj = root.getProject(JavaUtils.JAVA_PROJECT_NAME);
        if (prj.exists()) {
            IPath libPath = prj.getLocation().append(JavaUtils.JAVA_LIB_DIRECTORY);
            if (libPath.toFile().exists()) {
                FilesUtils.emptyFolder(libPath.toFile());
            }
        }
    }
}
