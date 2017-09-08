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
package org.talend.librariesmanager.model.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.routines.IRoutinesProvider;
import org.talend.core.utils.TalendCacheUtils;
import org.talend.designer.codegen.PigTemplate;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.librariesmanager.i18n.Messages;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;

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
                "resources/java/" + SOURCE_JAVA_BEANS_FOLDER + "/__TEMPLATE__.java"); //$NON-NLS-1$ //$NON-NLS-2$
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
        // check and install system jars for routines
        repositoryBundleService.deployModules(ModulesNeededProvider.getSystemRunningModules(), null);
        Set<String> existLibraries = repositoryBundleService.list();
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

    private File getStorageDirectory() {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        return storageDir;
    }

    @Override
    public void syncLibraries(IProgressMonitor... monitorWrap) {
        // TODO system routine libraries seems no use ,need to check more...
        // deploy system routine libraries
        if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
            ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                    ILibraryManagerUIService.class);
            libUiService.initializeSystemLibs();
        }

        if (TalendCacheUtils.cleanComponentCache()) {
            repositoryBundleService.clearCache();
        }
        // fix for TDI-34878
        IEclipsePreferences prefSetting = ConfigurationScope.INSTANCE.getNode(DesignerMavenPlugin.PLUGIN_ID);
        final String lastRepType = prefSetting.get("Maven_Setting_Type", null);
        String configSetting = System.getProperty("maven.repository", "local"); //$NON-NLS-1$
        final boolean isLocalRepository = !"global".equalsIgnoreCase(configSetting); //$NON-NLS-1$
        String currentRepType = isLocalRepository ? "local" : "global";
        if (!currentRepType.equals(lastRepType)) {
            repositoryBundleService.clearCache();
        }
        if (lastRepType == null || !currentRepType.equals(lastRepType)) {
            try {
                prefSetting.put("Maven_Setting_Type", currentRepType);
                prefSetting.flush();
            } catch (BackingStoreException e) {
                ExceptionHandler.process(e);
            }
        }
        // Add a new system file, if exists, means all components libs are already setup, so no need to do again.
        // if clean the component cache, it will automatically recheck all libs still.
        if (!repositoryBundleService.isInitialized()) {
            // 2. Components libraries and libraries from extension
            repositoryBundleService.deployComponentAndExtensionLibs(monitorWrap);
            repositoryBundleService.setInitialized();
        }

        checkInstalledLibraries();

        // clean the temp library of job needed in .java\lib
        cleanLibs();

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
        // TODO sync project/lib will be done in migration
        // for feature 12877
        // if (PluginChecker.isSVNProviderPluginLoaded()) {
        // String path = new Path(Platform.getInstanceLocation().getURL().getPath()).toFile().getPath();
        // String projectLabel = ProjectManager.getInstance().getCurrentProject().getTechnicalLabel();
        // path = path + File.separatorChar + projectLabel + File.separatorChar
        // + ERepositoryObjectType.getFolderName(ERepositoryObjectType.LIBS);
        // File libsTargetFile = new File(path);
        // repositoryBundleService.deploy(libsTargetFile.toURI(), monitorWrap);
        // }
    }

}
