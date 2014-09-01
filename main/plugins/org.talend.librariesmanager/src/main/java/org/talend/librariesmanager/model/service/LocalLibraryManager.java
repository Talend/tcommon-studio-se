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
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EMap;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.librariesmanager.emf.librariesindex.LibrariesIndex;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class LocalLibraryManager implements ILibraryManagerService {

    private Set<String> jarList = new HashSet<String>();

    // map jar to file path.
    // this will set in memory all the modules from the extension points.
    private Map<String, String> jarsFromExtensions = new HashMap<String, String>();

    // map uri to absolute path
    // key = null, means uri not tested yet....
    // value is null = jar not existing
    // value set = absolute path of the jar
    private Map<String, String> uriJarInstalled = new HashMap<String, String>();

    private Set<String> jarsNeededForComponents = new HashSet<String>();

    /**
     * DOC nrousseau LocalLibraryManager constructor comment.
     */
    public LocalLibraryManager() {
        super();

        List<ModuleNeeded> modulesNeededForApplication = ModulesNeededProvider.getModulesNeededForApplication();
        for (ModuleNeeded moduleNeeded : modulesNeededForApplication) {
            installJarFromPlatformIfExists(moduleNeeded.getModuleName(), moduleNeeded.getModuleLocaion());
        }
        listToUpdate = true;
    }

    boolean listToUpdate;

    // private long totalSizeCanBeReduced = 0;

    @Override
    public boolean isInitialized() {
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
            return LibrariesIndexManager.getInstance().getIndex().isInitialized();
        }
        return false;
    }

    @Override
    public void setInitialized() {
        LibrariesIndexManager.getInstance().loadResource();
        LibrariesIndexManager.getInstance().getIndex().setInitialized(true);
        LibrariesIndexManager.getInstance().saveResource();
    }

    /**
     * @param jarFileUri : file:/.....
     */
    @Override
    public void deploy(URI jarFileUri, IProgressMonitor... monitorWrap) {
        String installLocation = getStorageDirectory().getAbsolutePath();

        try {
            File file = new File(jarFileUri);
            if (file == null || !file.exists()) {
                return;
            }
            listToUpdate = true;
            if (file.isDirectory()) {
                FilesUtils.copyFolder(new File(jarFileUri), getStorageDirectory(), false,
                        FilesUtils.getExcludeSystemFilesFilter(), FilesUtils.getAcceptJARFilesFilter(), false, monitorWrap);
            } else {
                File target = new File(installLocation, file.getName());
                FilesUtils.copyFile(file, target);
            }
        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#deploy(java.util.Collection,
     * org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void deploy(Collection<URI> jarFileUris, IProgressMonitor... monitorWrap) {
        if (jarFileUris == null || jarFileUris.size() == 0) {
            return;
        }
        for (URI uri : jarFileUris) {
            deploy(uri, monitorWrap);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#retrieve(java.lang.String, java.lang.String)
     */
    @Override
    public boolean retrieve(String jarNeeded, String pathToStore, IProgressMonitor... monitorWrap) {
        return retrieve(jarNeeded, pathToStore, true, monitorWrap);
    }

    private File getJarFile(String jarNeeded) throws MalformedURLException {
        // priority to the jars from the /lib/java folder.
        List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), jarNeeded);
        if (jarFiles.size() > 0) {
            File jarFile = jarFiles.get(0);
            return jarFile;
        }
        String uriPath;
        // try to get the jars from the extension points.
        if (jarsFromExtensions.containsKey(jarNeeded)) {
            uriPath = jarsFromExtensions.get(jarNeeded);
        } else {
            // get jars file path from index
            EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();
            uriPath = jarsToRelative.get(jarNeeded);
        }
        if (uriPath != null && uriPath.startsWith("platform:/")) {
            if (checkJarInstalledFromPlatform(uriPath)) {
                return new File(uriJarInstalled.get(uriPath));
            }
        }
        return null;
    }

    @Override
    public boolean retrieve(String jarNeeded, String pathToStore, boolean popUp, IProgressMonitor... monitorWrap) {
        // MOD TDQ-9357 msjian: fix a java.io.FileNotFoundException
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
        }
        // TDQ-9357~
        String sourcePath = null, targetPath = pathToStore;
        try {
            File jarFile = getJarFile(jarNeeded);
            if (jarFile == null) {
                if (popUp && !CommonsPlugin.isHeadless()) {
                    // popup dialog if needed to download the jar.
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                        ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault()
                                .getService(ILibraryManagerUIService.class);

                        libUiService.installModules(new String[] { jarNeeded });
                    }
                    jarFile = getJarFile(jarNeeded);
                    if (jarFile == null) {
                        // jar not found even after the popup > stop here
                        return false;
                    }
                    // jar found > reset the modules just after install the jars
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                                ILibrariesService.class);
                        librariesService.resetModulesNeeded();
                    }
                } else {
                    // jar not found, and no popup at this point
                    return false;
                }
            }
            File target = new File(StringUtils.trimToEmpty(pathToStore));
            if (!target.exists()) {
                target.mkdirs();
            }
            sourcePath = jarFile.getAbsolutePath();
            FilesUtils.copyFile(jarFile, new File(pathToStore, jarNeeded));
            return true;
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        } catch (IOException e) {
            CommonExceptionHandler.process(new Exception("Can not copy: " + sourcePath + " to :" + targetPath, e));
        }
        return false;

    }

    @Override
    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, boolean showDialog,
            IProgressMonitor... monitorWrap) {
        if (jarsNeeded == null || jarsNeeded.size() == 0) {
            return false;
        }
        List<String> jarNotFound = new ArrayList<String>();

        boolean allIsOK = true;
        for (String jar : jarsNeeded) {
            if (!retrieve(jar, pathToStore, false, monitorWrap)) {
                jarNotFound.add(jar);
                allIsOK = false;
            }
        }
        if (showDialog && !jarNotFound.isEmpty() && !CommonsPlugin.isHeadless()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault().getService(
                        ILibraryManagerUIService.class);
                libUiService.installModules(jarNotFound.toArray(new String[jarNotFound.size()]));
                jarsNeeded = new ArrayList<String>(jarNotFound);
                jarNotFound.clear();
                allIsOK = true;
                boolean needResetModulesNeeded = false;
                for (String jar : jarsNeeded) {
                    if (!retrieve(jar, pathToStore, false, monitorWrap)) {
                        jarNotFound.add(jar);
                        allIsOK = false;
                    } else {
                        needResetModulesNeeded = true;
                    }
                }
                if (needResetModulesNeeded) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault().getService(
                                ILibrariesService.class);
                        librariesService.resetModulesNeeded();
                    }
                }
            }
        }

        return allIsOK;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#retrieve(java.util.Collection, java.lang.String,
     * org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, IProgressMonitor... monitorWrap) {
        return retrieve(jarsNeeded, pathToStore, true, monitorWrap);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#list(org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public Set<String> list(IProgressMonitor... monitorWrap) {
        if (!jarList.isEmpty() && !listToUpdate) {
            return jarList;
        }
        listToUpdate = false;

        jarList = new HashSet<String>();
        try {
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
            if (jarFiles.size() > 0) {
                for (File file : jarFiles) {
                    jarList.add(file.getName());
                }
            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }

        LibrariesIndexManager.getInstance().loadResource();

        // fix for TDI-25474 ,some libraries are removed form tos,only list jars exist
        EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();

        for (String jarName : jarsToRelative.keySet()) {
            String relativePath = jarsToRelative.get(jarName);
            boolean jarFound = false;
            if (relativePath != null && relativePath.startsWith("platform:/")) {
                jarFound = checkJarInstalledFromPlatform(relativePath);
            }
            if (jarFound) {
                jarList.add(jarName);
            }
        }
        return jarList;
    }

    @Override
    public Set<String> list(boolean withComponent, IProgressMonitor... monitorWrap) {
        return list(monitorWrap);
    }

    @Override
    public Set<String> listAllDllFiles() {
        Set<String> names = new HashSet<String>();
        try {
            List<File> dllFiles = FilesUtils.getDllFilesFromFolder(getStorageDirectory(), null);
            if (dllFiles.size() > 0) {
                for (File file : dllFiles) {
                    names.add(file.getName());
                }
            } else {

            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }

        return names;
    }

    private File getStorageDirectory() {
        String librariesPath = LibrariesManagerUtils.getLibrariesPath(ECodeLanguage.JAVA);
        File storageDir = new File(librariesPath);
        return storageDir;
    }

    @Override
    public void clearCache() {
        if (isInitialized()) {
            LibrariesIndexManager.getInstance().loadResource();
            LibrariesIndexManager.getInstance().getIndex().setInitialized(false);
            LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath().clear();
            LibrariesIndexManager.getInstance().saveResource();
        }
        listToUpdate = true;
    }

    @Override
    public boolean contains(String jarName) {
        return jarList.contains(jarName);
    }

    /*
     * (non-Jsdoc)
     * 
     * @see org.talend.core.ILibraryManagerService#delete(java.lang.String)
     */
    @Override
    public boolean delete(String jarName) {
        // only delete jar from lib/java, do not delete jars from original components providers.

        try {
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
            if (jarFiles.size() > 0) {
                for (File file : jarFiles) {
                    if (file.getName().equals(jarName)) {
                        file.delete();
                        jarList.remove(jarName);
                        return true;
                    }
                }
            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#deploy(java.util.Set, org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void deploy(Set<IComponent> componentList, IProgressMonitor... monitorWrap) {
        List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();

        for (IComponent component : componentList) {
            modules.addAll(component.getModulesNeeded());
        }
        deploy(modules, monitorWrap);
    }

    private Set<String> urlWarned = new HashSet<String>();

    @Override
    public void deploy(List<ModuleNeeded> modules, IProgressMonitor... monitorWrap) {
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
        }
        boolean modified = false;
        LibrariesIndex index = LibrariesIndexManager.getInstance().getIndex();
        EMap<String, String> jarsToRelativePath = index.getJarsToRelativePath();
        for (ModuleNeeded module : modules) {
            if (jarsFromExtensions.containsKey(module.getModuleName())) {
                // already installed from extension point.
                continue;
            }
            String moduleLocation = module.getModuleLocaion();
            if (moduleLocation != null && moduleLocation.startsWith("platform:/")) {
                if (jarsToRelativePath.containsKey(module.getModuleName())) {
                    String relativePath = jarsToRelativePath.get(module.getModuleName());
                    if (relativePath.equals(moduleLocation)) {
                        continue;
                    } else {
                        if (!urlWarned.contains(moduleLocation)) {
                            System.out.println(module.getModuleName() + " was already defined with:" + relativePath
                                    + " but redefined now with:" + moduleLocation);
                            urlWarned.add(moduleLocation);
                        }
                    }
                }
                if (checkJarInstalledFromPlatform(moduleLocation)) {
                    jarsToRelativePath.put(module.getModuleName(), moduleLocation);
                    modified = true;
                }
            } else {
                jarsNeededForComponents.add(module.getModuleName());
            }
        }
        if (modified) {
            LibrariesIndexManager.getInstance().saveResource();
        }
    }

    @Override
    public String getJarPath(String jarName) {
        String libPath = null;
        List<File> jarFiles = null;
        try {
            jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), jarName);
            if (jarFiles.size() > 0) {
                File file = jarFiles.get(0);
                libPath = file.getAbsolutePath();
            } else {
                EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();
                String relativePath = jarsToRelative.get(jarName);
                if (relativePath != null && relativePath.startsWith("platform:/")) { //$NON-NLS-1$
                    boolean jarFound = checkJarInstalledFromPlatform(relativePath);
                    if (jarFound) {
                        libPath = uriJarInstalled.get(relativePath);
                    }
                }
            }

        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }
        return libPath;
    }

    @Override
    public boolean checkJarInstalledFromPlatform(String uriPath) {
        if (uriJarInstalled.containsKey(uriPath)) {
            return uriJarInstalled.get(uriPath) != null;
        }
        String absolutePath = null;
        boolean jarFound = false;

        try {
            if (uriPath.startsWith("platform:/plugin/")) {
                String plugin = uriPath.substring(17);
                plugin = plugin.substring(0, plugin.indexOf("/"));
                String path = uriPath.substring(17 + plugin.length());

                URL url = FileLocator.find(Platform.getBundle(plugin), new Path(path), null);
                if (url != null) {
                    URL url2 = FileLocator.toFileURL(url);
                    File file = new File(url2.getFile());
                    if (file.exists()) {
                        jarFound = true;
                        absolutePath = file.getAbsolutePath();
                    }
                }
            }
        } catch (Exception e) {
            // do nothing
        }

        if (!jarFound) {
            try {
                URI uri = new URI(uriPath);
                URL url = FileLocator.toFileURL(uri.toURL());
                File file = new File(url.getFile());
                if (file.exists()) {
                    jarFound = true;
                    absolutePath = file.getAbsolutePath();
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        uriJarInstalled.put(uriPath, absolutePath);
        return jarFound;
    }

    private void installJarFromPlatformIfExists(String moduleName, String uriPath) {
        if (jarsFromExtensions.containsValue(uriPath)) {
            // nothing to do.
            return;
        }
        String jarName = null;
        try {
            if (uriJarInstalled.containsKey(uriPath)) {
                File file = new File(uriJarInstalled.get(uriPath));
                if (file.exists()) {
                    jarName = file.getName();
                }
            } else {
                URI uri = new URI(uriPath);
                URL url = FileLocator.toFileURL(uri.toURL());
                File file = new File(url.getFile());
                if (file.exists()) {
                    jarName = file.getName();
                }
            }
        } catch (Exception e) {
            // do nothing
        }
        if (jarName != null) {
            jarsFromExtensions.put(jarName, uriPath);
            // just in case the jarName is different from the moduleName.
            jarsFromExtensions.put(moduleName, uriPath);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#deploy(java.util.Map, org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void deploy(Map<String, String> libsToRelativePath, IProgressMonitor... monitorWrap) {
        File indexFile = new File(LibrariesIndexManager.getInstance().getIndexFilePath());
        if (indexFile.exists()) {
            LibrariesIndexManager.getInstance().loadResource();
        }
        EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getIndex().getJarsToRelativePath();
        for (String key : libsToRelativePath.keySet()) {
            if (checkJarInstalledFromPlatform(libsToRelativePath.get(key))) {
                jarsToRelative.put(key, libsToRelativePath.get(key));
            }
        }
        LibrariesIndexManager.getInstance().saveResource();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#forceListUpdate()
     */
    @Override
    public void forceListUpdate() {
        listToUpdate = true;
    }

}
