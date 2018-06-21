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
package org.talend.librariesmanager.model.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.emf.common.util.EMap;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.CommonExceptionHandler;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.io.FilesUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.ILibraryManagerUIService;
import org.talend.core.language.ECodeLanguage;
import org.talend.core.model.components.ComponentProviderInfo;
import org.talend.core.model.components.IComponent;
import org.talend.core.model.components.IComponentsService;
import org.talend.core.model.general.ILibrariesService;
import org.talend.core.model.general.ILibrariesService.IChangedLibrariesListener;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.ModuleNeeded.ELibraryInstallStatus;
import org.talend.core.model.general.ModuleStatusProvider;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.NexusServerUtils;
import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.utils.PomUtil;
import org.talend.librariesmanager.maven.MavenArtifactsHandler;
import org.talend.librariesmanager.model.ExtensionModuleManager;
import org.talend.librariesmanager.model.ModulesNeededProvider;
import org.talend.librariesmanager.prefs.LibrariesManagerUtils;
import org.talend.osgi.hook.notification.JarMissingObservable;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class LocalLibraryManager implements ILibraryManagerService, IChangedLibrariesListener {

    private static Logger log = Logger.getLogger(LocalLibraryManager.class);

    private Set<String> jarList = new HashSet<String>();

    // map platform_uri/maven_uri to absolute path
    // key = null, means uri not tested yet....
    // value is null = jar not existingb
    // value set = absolute path of the jar
    private Map<String, String> studioJarInstalled = new HashMap<String, String>();

    // rules same as studioJarInstalled
    // key maven uri , value jar path
    // only check the existing status in list() incase of performance problem
    private Map<String, String> mavenJarInstalled = new HashMap<String, String>();

    private Set<String> urlWarned = new HashSet<String>();

    private JarMissingObservable missingJarObservable;

    private MavenArtifactsHandler deployer;

    /**
     * DOC nrousseau LocalLibraryManager constructor comment.
     */
    public LocalLibraryManager() {
        super();
        deployer = new MavenArtifactsHandler();
        ModulesNeededProvider.addChangeLibrariesListener(this);
    }

    @Override
    public boolean isInitialized() {
        File indexFile = new File(LibrariesIndexManager.getInstance().getStudioIndexPath());
        File mvnIndexFile = new File(LibrariesIndexManager.getInstance().getMavenIndexPath());
        if (indexFile.exists() && mvnIndexFile.exists()) {
            return LibrariesIndexManager.getInstance().getStudioLibIndex().isInitialized();
        }
        return false;
    }

    @Override
    public void setInitialized() {
        LibrariesIndexManager.getInstance().getStudioLibIndex().setInitialized(true);
        LibrariesIndexManager.getInstance().saveStudioIndexResource();
        LibrariesIndexManager.getInstance().getMavenLibIndex().setInitialized(true);
        LibrariesIndexManager.getInstance().saveMavenIndexResource();
    }

    @Override
    public void deploy(URI jarFileUri, IProgressMonitor... monitorWrap) {
        deploy(jarFileUri, null, monitorWrap);
    }

    @Override
    public void deploy(URI jarFileUri, String mavenUri, IProgressMonitor... monitorWrap) {
        deploy(jarFileUri, mavenUri, true, monitorWrap);
    }

    @Override
    public void deploy(URI jarFileUri, String mavenUri, boolean updateNexusJar, IProgressMonitor... monitorWrap) {
        if (jarFileUri.isOpaque()) {
            return;
        }
        File file = new File(jarFileUri);
        if (file == null || !file.exists()) {
            return;
        }
        install(file, mavenUri, updateNexusJar, monitorWrap);
    }

    /**
     * 
     * DOC wchen Comment method "deployFile".
     * 
     * @param file
     * @param mavenUri snaopshot mvn uri
     * @param monitorWrap
     */
    private void install(File file, String mavenRUI, boolean updateRemoteJar, IProgressMonitor... monitorWrap) {
        try {
            if (file.isDirectory()) {
                List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null);
                Map<String, String> sourceAndMavenUri = new HashMap<String, String>();
                if (!jarFiles.isEmpty()) {
                    for (File jarFile : jarFiles) {
                        if (mavenRUI == null) {
                            guessMavenRUIFromIndex(jarFile, sourceAndMavenUri);
                        } else {
                            sourceAndMavenUri.put(mavenRUI, jarFile.getAbsolutePath());
                        }
                    }
                    deployer.install(sourceAndMavenUri, updateRemoteJar);

                    updateInstalledMvnUri(sourceAndMavenUri.keySet());
                }
            } else {
                Map<String, String> sourceAndMavenUri = new HashMap<String, String>();
                if (mavenRUI == null) {
                    guessMavenRUIFromIndex(file, sourceAndMavenUri);
                } else {
                    sourceAndMavenUri.put(mavenRUI, file.getAbsolutePath());
                }
                deployer.install(sourceAndMavenUri, updateRemoteJar);
                updateInstalledMvnUri(sourceAndMavenUri.keySet());
            }

        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }
    }

    private void guessMavenRUIFromIndex(File jarFile, Map<String, String> sourceAndMavenUri) {
        // TODO????? should deploy with all versions
        String urisFromIndex = LibrariesIndexManager.getInstance().getMavenLibIndex().getJarsToRelativePath()
                .get(jarFile.getName());
        boolean deployAsDefault = true;
        if (urisFromIndex != null) {
            final String[] mvnUris = urisFromIndex.split(MavenUrlHelper.MVN_INDEX_SPLITER);
            for (String uri : mvnUris) {
                String customMavenURI = getCustomMavenURI(uri);
                if (customMavenURI != null) {
                    uri = customMavenURI;
                }
                sourceAndMavenUri.put(uri, jarFile.getAbsolutePath());
                if (deployAsDefault) {
                    MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uri);
                    if (parseMvnUrl != null
                            && jarFile.getName().equals(parseMvnUrl.getArtifactId() + "." + parseMvnUrl.getType())) {
                        deployAsDefault = false;
                    }
                }
            }
        }
        // deploy as defaultMavenUri in case jar name is diffrent from artifactId in mvnuri from
        // index
        if (deployAsDefault) {
            String defaultMavenUri = MavenUrlHelper.generateMvnUrlForJarName(jarFile.getName());
            String customMavenURI = getCustomMavenURI(defaultMavenUri);
            if (customMavenURI != null) {
                defaultMavenUri = customMavenURI;
            }
            sourceAndMavenUri.put(defaultMavenUri, jarFile.getAbsolutePath());
        }

    }

    /**
     * 
     * update the mavenJarInstalled cache after deploy jars
     * 
     * @param installedUris
     */
    private void updateInstalledMvnUri(Collection<String> installedUris) {
        for (String uri : installedUris) {
            checkJarInstalledInMaven(uri);
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
        String jarPath = getJarPath(jarNeeded);
        if (jarPath != null) {
            File file = new File(jarPath);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }

    @Override
    public boolean retrieve(String jarNeeded, String pathToStore, boolean popUp, IProgressMonitor... monitorWrap) {
        ModuleNeeded testModule = new ModuleNeeded("", jarNeeded, "", true);
        boolean refresh = true;
        return retrieve(testModule, pathToStore, popUp, refresh);
    }

    private boolean retrieve(ModuleNeeded module, String pathToStore, boolean showDialog, boolean refresh) {
        String jarNeeded = module.getModuleName();
        String sourcePath = null;
        File jarFile = null;
        try {
            jarFile = retrieveJarFromLocal(module);
            // retrieve form custom nexus server automatically
            TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
            ArtifactRepositoryBean customNexusServer = manager.getCustomNexusServer();
            if (customNexusServer != null) {
                Set<String> toResolve = guessMavenURI(module);
                for (String uri : toResolve) {
                    if (isResolveAllowed(uri)) {
                        MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uri);
                        if (jarFile == null || parseMvnUrl.getVersion().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
                            File resolvedJar = resolveJar(customNexusServer, uri);
                            if (resolvedJar != null) {
                                jarFile = resolvedJar;
                                break;
                            }
                        }
                    }

                }

            }
        } catch (Exception e) {
            CommonExceptionHandler
                    .process(new Exception(getClass().getSimpleName() + " resolve " + module.getModuleName() + " failed !"));
        }
        try {
            // try the jar name if can't get jar with uri.
            if (jarFile == null) {
                jarFile = getJarFile(jarNeeded);
            }
            if (jarFile == null) {
                if (showDialog && !CommonsPlugin.isHeadless()) {
                    // popup dialog if needed to download the jar.
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                        ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault()
                                .getService(ILibraryManagerUIService.class);

                        libUiService.installModules(new String[] { jarNeeded });
                    }
                    jarFile = retrieveJarFromLocal(module);
                    if (jarFile == null) {
                        // jar not found even after the popup > stop here
                        return false;
                    }
                    // jar found > reset the modules just after install the jars
                    if (refresh && GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                .getService(ILibrariesService.class);
                        librariesService.checkLibraries();
                    }
                }
            }
            if (jarFile == null) {
                return false;
            }
            // jar found ,and no need to copy ,return true
            if (pathToStore == null) {
                return true;
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
            CommonExceptionHandler.process(new Exception("Can not copy: " + sourcePath + " to :" + pathToStore, e));
        }
        return false;
    }

    private File retrieveJarFromLocal(ModuleNeeded module) {
        File jarFile = null;
        Set<String> toResolve = guessMavenURI(module);
        // retrieve from local
        for (String uri : toResolve) {
            String filePath = getJarPathFromMaven(uri);
            if (filePath != null) {
                jarFile = new File(filePath);
                break;
            }
        }
        return jarFile;
    }

    private Set<String> guessMavenURI(ModuleNeeded module) {
        String jarNeeded = module.getModuleName();
        EMap<String, String> mvnURIIndex = LibrariesIndexManager.getInstance().getMavenLibIndex().getJarsToRelativePath();
        Set<String> toResolve = new HashSet<String>();
        if (module.getCustomMavenUri() != null) {
            toResolve.add(module.getCustomMavenUri());
        } else if (module.getMavenURIFromConfiguration() == null && mvnURIIndex.get(jarNeeded) != null) {
            String mavenUriFromIndex = mvnURIIndex.get(jarNeeded);
            final String[] split = mavenUriFromIndex.split(MavenUrlHelper.MVN_INDEX_SPLITER);
            for (String mvnUri : split) {
                toResolve.add(mvnUri);
            }
        } else {
            toResolve.add(module.getDefaultMavenURI());
        }
        return toResolve;
    }

    /**
     * DOC nrousseau Comment method "resolveJar".
     * 
     * @param jarNeeded
     * @param jarFile
     * @param customNexusServer
     * @param uri
     * @return
     * @throws Exception
     * @throws IOException
     */
    @Override
    public File resolveJar(final ArtifactRepositoryBean customNexusServer, String uri) throws Exception, IOException {
        File resolvedFile = TalendMavenResolver.resolve(uri);
        if (resolvedFile != null) {
            // reset module status
            ModuleStatusProvider.putStatus(uri, ELibraryInstallStatus.INSTALLED);
            ModuleStatusProvider.putDeployStatus(uri, ELibraryInstallStatus.DEPLOYED);
            // update installed path
            mavenJarInstalled.put(uri, resolvedFile.getAbsolutePath());
        }
        updateLastResolveDate(uri);
        // resolve the pom
        if (resolvedFile != null) {
            try {
                String pomPath = resolvedFile.getParent();
                String name = resolvedFile.getName();
                int indexOf = name.lastIndexOf(".");
                if (indexOf != -1) {
                    pomPath = pomPath + "/" + name.substring(0, indexOf) + "." + MavenConstants.PACKAGING_POM;
                } else {
                    pomPath = pomPath + name + "." + MavenConstants.PACKAGING_POM;
                }
                File pomFile = new File(pomPath);
                if (!pomFile.exists()) {
                    MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uri);
                    File generatedPom = new File(PomUtil.generatePom(parseMvnUrl));
                    FilesUtils.copyFile(generatedPom, pomFile);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        return resolvedFile;
    }

    private Map<String, Date> lastResolveDate;

    private String LAST_UPDATE_KEY = "lastUpdate"; //$NON-NLS-1$

    public long daysBetween(final Calendar startDate, final Calendar endDate) {
        // assert: startDate must be before endDate
        int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
        long endInstant = endDate.getTimeInMillis();
        int presumedDays = (int) ((endInstant - startDate.getTimeInMillis()) / MILLIS_IN_DAY);
        Calendar cursor = (Calendar) startDate.clone();
        cursor.add(Calendar.DAY_OF_YEAR, presumedDays);
        long instant = cursor.getTimeInMillis();
        if (instant == endInstant) {
            return presumedDays;
        }

        final int step = instant < endInstant ? 1 : -1;
        do {
            cursor.add(Calendar.DAY_OF_MONTH, step);
            presumedDays += step;
        } while (cursor.getTimeInMillis() <= endInstant);
        return presumedDays - 1;
    }

    /**
     * DOC nrousseau Comment method "isResolveAllowed".
     * 
     * @param uri
     * @return
     */
    public boolean isResolveAllowed(String uri) {
        IEclipsePreferences node = InstanceScope.INSTANCE.getNode(NexusServerUtils.ORG_TALEND_DESIGNER_CORE);
        int refreshTime = node.getInt(ITalendCorePrefConstants.NEXUS_REFRESH_FREQUENCY, 0);
        if (refreshTime == 0) {
            return true;
        }
        if (refreshTime == -1) {
            return false;
        }

        if (lastResolveDate == null) {
            lastResolveDate = new HashMap<String, Date>();
            IEclipsePreferences prefSetting = ConfigurationScope.INSTANCE.getNode("org.talend.librariesmanager");
            String lastUpdate = prefSetting.get(LAST_UPDATE_KEY, null);
            if (lastUpdate != null) {
                byte[] lastUpdateStream = DatatypeConverter.parseHexBinary(lastUpdate);
                ByteArrayInputStream bais = new ByteArrayInputStream(lastUpdateStream);
                ObjectInputStream ois;
                try {
                    ois = new ObjectInputStream(bais);
                    lastResolveDate = (HashMap) ois.readObject();
                    ois.close();
                    bais.close();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
        Date lastDate = lastResolveDate.get(uri);
        if (lastDate == null) {
            return true;
        }
        Calendar before = Calendar.getInstance();
        before.setTime(lastDate);
        Calendar after = Calendar.getInstance();
        after.setTime(new Date());
        long days = daysBetween(before, after);
        if (days >= refreshTime) {
            return true;
        }
        return false;
    }

    public void updateLastResolveDate(String uri) {
        if (lastResolveDate == null) {
            lastResolveDate = new HashMap<String, Date>();
        }
        lastResolveDate.put(uri, new Date());
        IEclipsePreferences prefSetting = ConfigurationScope.INSTANCE.getNode("org.talend.librariesmanager");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(lastResolveDate);
            oos.close();
            String lastUpdate = DatatypeConverter.printHexBinary(baos.toByteArray());
            prefSetting.put(LAST_UPDATE_KEY, lastUpdate);
            prefSetting.flush();
            baos.close();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    public boolean retrieve(Collection<String> jarsNeeded, String pathToStore, boolean showDialog,
            IProgressMonitor... monitorWrap) {
        if (jarsNeeded == null || jarsNeeded.size() == 0) {
            return false;
        }
        List<ModuleNeeded> modulesNotFound = new ArrayList<>();

        boolean allIsOK = true;
        for (String jar : jarsNeeded) {
            ModuleNeeded moduleNeeded = null;
            if (jar != null && !jar.toUpperCase().endsWith(".JAR")) {
                moduleNeeded = ModulesNeededProvider.getModuleNeededById(jar);
            }
            boolean found = false;
            if (moduleNeeded != null) {
                found = retrieve(moduleNeeded, pathToStore, false, monitorWrap);
            } else {
                found = retrieve(jar, pathToStore, false, monitorWrap);
                moduleNeeded = new ModuleNeeded("", jar, "", true);
            }
            if (!found) {
                modulesNotFound.add(moduleNeeded);
                allIsOK = false;
            }
        }
        if (showDialog && !modulesNotFound.isEmpty() && !CommonsPlugin.isHeadless()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault()
                        .getService(ILibraryManagerUIService.class);
                libUiService.installModules(modulesNotFound);
                List<ModuleNeeded> retrievedModules = new ArrayList<>(modulesNotFound);
                modulesNotFound.clear();
                allIsOK = true;
                boolean needResetModulesNeeded = false;
                for (ModuleNeeded module : retrievedModules) {
                    if (!retrieve(module, pathToStore, false, false)) {
                        modulesNotFound.add(module);
                        allIsOK = false;
                    } else {
                        needResetModulesNeeded = true;
                    }
                }
                if (needResetModulesNeeded) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                .getService(ILibrariesService.class);
                        librariesService.checkLibraries();
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
     * @see org.talend.core.ILibraryManagerService#retrieve(java.util.Set, java.lang.String,
     * org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public boolean retrieve(Set<ModuleNeeded> modulesNeeded, String pathToStore, boolean showDialog,
            IProgressMonitor... monitorWrap) {
        if (modulesNeeded == null || modulesNeeded.size() == 0) {
            return false;
        }
        Set<ModuleNeeded> jarNotFound = new HashSet<ModuleNeeded>();
        boolean allIsOK = true;
        for (ModuleNeeded jar : modulesNeeded) {
            if (!retrieve(jar, pathToStore, false, false)) {
                jarNotFound.add(jar);
                allIsOK = false;
            }
        }
        if (showDialog && !jarNotFound.isEmpty() && !CommonsPlugin.isHeadless()) {
            if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibraryManagerUIService.class)) {
                ILibraryManagerUIService libUiService = (ILibraryManagerUIService) GlobalServiceRegister.getDefault()
                        .getService(ILibraryManagerUIService.class);
                libUiService.installModules(jarNotFound);
                modulesNeeded = new HashSet<ModuleNeeded>(jarNotFound);
                jarNotFound.clear();
                allIsOK = true;
                boolean needResetModulesNeeded = false;
                for (ModuleNeeded jar : modulesNeeded) {
                    if (!retrieve(jar, pathToStore, false, false)) {
                        jarNotFound.add(jar);
                        allIsOK = false;
                    } else {
                        needResetModulesNeeded = true;
                    }
                }
                if (needResetModulesNeeded) {
                    if (GlobalServiceRegister.getDefault().isServiceRegistered(ILibrariesService.class)) {
                        ILibrariesService librariesService = (ILibrariesService) GlobalServiceRegister.getDefault()
                                .getService(ILibrariesService.class);
                        librariesService.checkLibraries();
                    }
                }
            }
        }

        return allIsOK;
    }

    @Override
    public boolean retrieve(ModuleNeeded module, String pathToStore, boolean showDialog, IProgressMonitor... monitorWrap) {
        return retrieve(module, pathToStore, showDialog, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.IRepositoryBundleService#list(org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public Set<String> list(IProgressMonitor... monitorWrap) {
        if (!jarList.isEmpty()) {
            return jarList;
        }
        for (ModuleNeeded module : ModulesNeededProvider.getAllManagedModules()) {
            if (module.getStatus() == ELibraryInstallStatus.INSTALLED) {
                jarList.add(module.getModuleName());
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
            LibrariesIndexManager.getInstance().clearAll();
            ModuleStatusProvider.reset();
        }
        jarList.clear();
    }

    @Override
    public boolean contains(String jarName) {
        Set<String> list = list();
        boolean contained = list.contains(jarName);
        if (!contained) {
            try {
                File jarFile = getJarFile(jarName);
                if (jarFile != null) {
                    list.add(jarName);
                    contained = true;
                }
            } catch (MalformedURLException e) {
                contained = false;
            }
        }
        return contained;
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

    /**
     * Get jars from platform and install in local maven , don't publish it to remote server
     */
    @Override
    public void installModules(Collection<ModuleNeeded> modules, IProgressMonitor monitorWrap) {
        EMap<String, String> libIndex = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
        for (ModuleNeeded module : modules) {
            File fileToDeploy = null;
            String moduleLocation = module.getModuleLocaion();
            Set<String> toDeploy = guessMavenURI(module);
            for (String mavenUri : toDeploy) {
                if (checkJarInstalledInMaven(mavenUri)) {
                    continue;
                }
                boolean found = false;
                if (moduleLocation != null && moduleLocation.startsWith("platform:/")) {
                    if (checkJarInstalledFromPlatform(moduleLocation)) {
                        found = true;
                        fileToDeploy = new File(studioJarInstalled.get(moduleLocation));
                    } else if (libIndex.containsKey(module.getModuleName())) {
                        String relativePath = libIndex.get(module.getModuleName());
                        if (!relativePath.equals(moduleLocation)) {
                            if (!urlWarned.contains(moduleLocation)) {
                                System.out.println(module.getModuleName() + " was already defined with:" + relativePath
                                        + " but redefined now with:" + moduleLocation);
                                urlWarned.add(moduleLocation);
                            }
                            moduleLocation = relativePath;
                            found = true;
                            fileToDeploy = new File(studioJarInstalled.get(moduleLocation));
                        }
                    }
                }
                if (!found) {
                    EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getStudioLibIndex()
                            .getJarsToRelativePath();
                    String relativePath = jarsToRelative.get(module.getModuleName());
                    if (relativePath != null && checkJarInstalledFromPlatform(relativePath)) {
                        found = true;
                        fileToDeploy = new File(studioJarInstalled.get(relativePath));
                    }
                }
                if (!found) {
                    try {
                        fileToDeploy = getJarFile(module.getModuleName());
                    } catch (MalformedURLException e) {
                        ExceptionHandler.process(e);
                    }
                    if (fileToDeploy != null) {
                        found = true;
                    }
                }
                if (fileToDeploy != null && !fileToDeploy.exists()) {
                    fileToDeploy = null;
                    found = false;
                }
                if (!found) {
                    ExceptionHandler.log("missing jar:" + module.getModuleName());
                }
                if (fileToDeploy != null) {
                    install(fileToDeploy, mavenUri, false, monitorWrap);
                }
            }
        }

    }

    @Override
    public String getJarPath(String jarName) {
        if (jarName == null) {
            return null;
        }
        String libPath = null;
        try {
            // maven
            libPath = getJarPathFromMaven(jarName);
            if (libPath != null) {
                return libPath;
            }

            // java/lib folder
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), jarName);
            if (jarFiles.size() > 0) {
                File file = jarFiles.get(0);
                libPath = file.getAbsolutePath();
            }
            // studio
            EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
            String relativePath = jarsToRelative.get(jarName);
            if (relativePath != null && relativePath.startsWith("platform:/")) { //$NON-NLS-1$
                boolean jarFound = checkJarInstalledFromPlatform(relativePath);
                if (jarFound) {
                    libPath = studioJarInstalled.get(relativePath);
                }
            }
        } catch (IOException e) {
            CommonExceptionHandler.process(e);
        } catch (Exception e) {
            CommonExceptionHandler.process(e);
        }

        return libPath;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#getJarPathFromMaven(java.lang.String)
     */
    @Override
    public String getJarPathFromMaven(String jarNameOrMavenUri) {
        if (jarNameOrMavenUri == null) {
            return null;
        }
        String libPath = null;
        Set<String> mvnUris = new HashSet<String>();
        if (MavenUrlHelper.isMvnUrl(jarNameOrMavenUri)) {
            mvnUris.add(jarNameOrMavenUri);
        } else {
            ModuleNeeded testModule = new ModuleNeeded("", jarNameOrMavenUri, "", true);
            mvnUris.addAll(guessMavenURI(testModule));
        }
        for (String uriToCheck : mvnUris) {
            if (checkJarInstalledInMaven(uriToCheck)) {
                libPath = mavenJarInstalled.get(uriToCheck);
                if (libPath != null) {
                    return libPath;
                }
            }
        }

        return null;
    }

    public boolean checkJarInstalledInMaven(String mvnUri) {
        if (mavenJarInstalled.containsKey(mvnUri)) {
            if (!new File(mavenJarInstalled.get(mvnUri)).exists()) {
                mavenJarInstalled.remove(mvnUri);
                return false;
            }
            return mavenJarInstalled.get(mvnUri) != null;
        }
        File resovledJar = resolveStatusLocally(mvnUri);
        if (resovledJar != null) {
            return true;
        }
        return false;
    }

    @Override
    public File resolveStatusLocally(String mvnUriStatusKey) {
        if (ModuleStatusProvider.getDeployStatus(mvnUriStatusKey) == ELibraryInstallStatus.NOT_DEPLOYED) {
            return null;
        }
        // then try to resolve locally
        String localMavenUri = mvnUriStatusKey.replace("mvn:", "mvn:" + MavenConstants.LOCAL_RESOLUTION_URL + "!"); //$NON-NLS-1$ //$NON-NLS-2$
        try {
            File resolvedJar = TalendMavenResolver.getMavenResolver().resolve(localMavenUri);
            if (resolvedJar != null) {
                mavenJarInstalled.put(mvnUriStatusKey, resolvedJar.getAbsolutePath());
                ModuleStatusProvider.putStatus(mvnUriStatusKey, ELibraryInstallStatus.INSTALLED);
                ModuleStatusProvider.putDeployStatus(mvnUriStatusKey, ELibraryInstallStatus.DEPLOYED);
            } else {
                ModuleStatusProvider.putDeployStatus(mvnUriStatusKey, ELibraryInstallStatus.NOT_DEPLOYED);
            }
            return resolvedJar;
        } catch (Exception e) {
            ModuleStatusProvider.putDeployStatus(mvnUriStatusKey, ELibraryInstallStatus.NOT_DEPLOYED);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#checkModuleStatus(org.talend.core.model.general.ModuleNeeded)
     */
    @Override
    public void checkModuleStatus(ModuleNeeded module) {
        String mvnUriStatusKey = module.getMavenUri();
        final ELibraryInstallStatus status = ModuleStatusProvider.getStatus(mvnUriStatusKey);
        if (status == null) {
            // check from maven
            File resolvedJar = resolveStatusLocally(mvnUriStatusKey);
            if (resolvedJar == null || !resolvedJar.exists()) {
                String customMavenUri = module.getCustomMavenUri();
                // if customer uri set ,only check in maven repository and don't use jar in the platform anymore
                if (customMavenUri == null) {
                    if (module.getModuleLocaion() != null) {
                        // check from platfrom
                        String platformPath = getJarPathFromPlatform(module.getModuleLocaion());
                        if (platformPath != null) {
                            resolvedJar = new File(platformPath);
                        }
                    }
                    /**
                     * if not found from moduleLocation, need to try to find it from lib/java, since the moduleLocation
                     * may be not exist for some reasons
                     */
                    if (resolvedJar == null) {
                        // check the lib/java
                        List<File> jarFiles;
                        try {
                            jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
                            for (File jar : jarFiles) {
                                if (jar.getName().equals(module.getModuleName())) {
                                    resolvedJar = jar;
                                    break;
                                }
                            }
                        } catch (MalformedURLException e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
                if (resolvedJar != null) {
                    ModuleStatusProvider.putStatus(mvnUriStatusKey, ELibraryInstallStatus.INSTALLED);
                } else {
                    ModuleStatusProvider.putStatus(mvnUriStatusKey, ELibraryInstallStatus.NOT_INSTALLED);
                }
            }
        } else if (status == ELibraryInstallStatus.NOT_INSTALLED) {
            // fix for cached ModuleNeeded with status NOT_INSTALLED
            File resolvedJar = null;
            if (module.getModuleLocaion() != null) {
                // check from platfrom
                String platformPath = getJarPathFromPlatform(module.getModuleLocaion());
                if (platformPath != null) {
                    resolvedJar = new File(platformPath);
                }
                if (resolvedJar != null) {
                    ModuleStatusProvider.putStatus(mvnUriStatusKey, ELibraryInstallStatus.INSTALLED);
                } else {
                    ModuleStatusProvider.putStatus(mvnUriStatusKey, ELibraryInstallStatus.NOT_INSTALLED);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#checkJarInstalledFromMaven(java.lang.String)
     */

    @Override
    public boolean checkJarInstalledFromPlatform(String uriPath) {
        return getJarPathFromPlatform(uriPath) != null;
    }

    private String getJarPathFromPlatform(String urlPath) {
        if (studioJarInstalled.containsKey(urlPath)) {
            return studioJarInstalled.get(urlPath);
        }
        if (urlPath.startsWith(MavenUrlHelper.MVN_PROTOCOL)) {
            return null;
        }
        String absolutePath = null;
        boolean jarFound = false;

        // prevent the missing jar manager to issu notifications is the lib is not found
        JarMissingObservable missingJarObservableService = getMissingJarObservableService();
        if (missingJarObservableService != null) {
            missingJarObservableService.prenventNotificationLock.lock();
        }
        try {
            try {
                if (urlPath.startsWith(ExtensionModuleManager.URIPATH_PREFIX)) {
                    String plugin = urlPath.substring(17);
                    plugin = plugin.substring(0, plugin.indexOf("/"));
                    String path = urlPath.substring(17 + plugin.length());
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
                    URI uri = new URI(urlPath);
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
            studioJarInstalled.put(urlPath, absolutePath);
        } finally {
            if (missingJarObservableService != null) {
                missingJarObservableService.prenventNotificationLock.unlock();
            }
        }
        return studioJarInstalled.get(urlPath);
    }

    private void calculateModulesIndexFromExtension(Map<String, String> libsToRelativePath, Set<String> duplicateLocationJar,
            Map<String, String> libsToMavenUri, Set<String> duplicateMavenUri) {
        List<ModuleNeeded> modulesNeededForApplication = ModulesNeededProvider.getModulesNeededForApplication();
        calculateModulesIndex(modulesNeededForApplication, libsToRelativePath, duplicateLocationJar, libsToMavenUri,
                duplicateMavenUri);
    }

    @Override
    public void createModulesIndexFromComponentAndExtension(IProgressMonitor... monitorWrap) {
        // key: moduleName, value: platformURL
        Map<String, String> platformURLMap = new HashMap<String, String>();
        // key: moduleName, value: mvn uri
        Map<String, String> mavenURIMap = new HashMap<String, String>();
        List<ModuleNeeded> modules = new ArrayList<ModuleNeeded>();
        Set<String> duplicateLocationJar = new HashSet<String>();
        Set<String> duplicateMavenUri = new HashSet<String>();
        // TDQ-11125 TOP doesn't have IComponentsService.avoid NPE.
        IComponentsService service = null;
        if (GlobalServiceRegister.getDefault().isServiceRegistered(IComponentsService.class)) {
            service = (IComponentsService) GlobalServiceRegister.getDefault().getService(IComponentsService.class);
        }
        if (service != null) {
            for (IComponent component : service.getComponentsFactory().readComponents()) {
                try {
                    modules.addAll(component.getModulesNeeded());
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                    continue;
                }
            }
        }
        calculateModulesIndex(modules, platformURLMap, duplicateLocationJar, mavenURIMap, duplicateMavenUri);

        calculateModulesIndexFromExtension(platformURLMap, duplicateLocationJar, mavenURIMap, duplicateMavenUri);

        if (CommonsPlugin.isDebugMode()) {
            if (!duplicateLocationJar.isEmpty()) {
                warnDuplicated(modules, duplicateLocationJar, "Library:");
            }
            if (!duplicateMavenUri.isEmpty()) {
                warnDuplicated(modules, duplicateMavenUri, "Maven Uri:");
            }
        }

        if (service != null) {
            deployLibsFromComponentFolder(service, platformURLMap);
        }

        saveMavenIndex(mavenURIMap, monitorWrap);
        savePlatfromURLIndex(platformURLMap, monitorWrap);

    }

    /**
     * 
     * The old components might use some jars in component folder and theres jars are not configured with platfrom URL
     * 
     * @param service
     * @param libsWithoutUri
     * @param platformURLMap
     */
    private void deployLibsFromComponentFolder(IComponentsService service, Map<String, String> platformURLMap) {
        Set<File> needToDeploy = new HashSet<File>();
        List<ComponentProviderInfo> componentsFolders = service.getComponentsFactory().getComponentsProvidersInfo();
        for (ComponentProviderInfo providerInfo : componentsFolders) {
            String contributeID = providerInfo.getContributer();
            String id = providerInfo.getId();
            try {
                File file = new File(providerInfo.getLocation());
                if ("org.talend.designer.components.model.UserComponentsProvider".equals(id)
                        || "org.talend.designer.components.exchange.ExchangeComponentsProvider".equals(id)) {
                    if (file.isDirectory()) {
                        List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null);
                        if (jarFiles.size() > 0) {
                            for (File jarFile : jarFiles) {
                                String name = jarFile.getName();
                                if (platformURLMap.get(name) != null) {
                                    continue;
                                }
                                needToDeploy.add(jarFile);
                            }
                        }
                    } else {
                        if (platformURLMap.get(file.getName()) != null) {
                            continue;
                        }
                        needToDeploy.add(file);
                    }
                } else {
                    // for other component provider ,add jars to the platform url index
                    List<File> jarFiles = FilesUtils.getJarFilesFromFolder(file, null, "ext");
                    if (jarFiles.size() > 0) {
                        for (File jarFile : jarFiles) {
                            String name = jarFile.getName();
                            String path = platformURLMap.get(name);
                            int lengthBasePath = new Path(file.getParentFile().getAbsolutePath()).toPortableString().length();
                            String relativePath = new Path(jarFile.getAbsolutePath()).toPortableString()
                                    .substring(lengthBasePath);
                            String moduleLocation = "platform:/plugin/" + contributeID + relativePath;
                            if (path != null) {
                                if (path.equals(moduleLocation)) {
                                    continue;
                                } else {
                                    if (CommonsPlugin.isDebugMode()) {
                                        CommonExceptionHandler
                                                .warn(name + " is duplicated, locations:" + path + " and:" + moduleLocation);
                                    }
                                    continue;
                                }
                            }
                            platformURLMap.put(name, moduleLocation);
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
                continue;
            }
        }

        // deploy needed jars for User and Exchange component providers
        if (!needToDeploy.isEmpty()) {
            for (File file : needToDeploy) {
                try {
                    deploy(file.toURI());
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    private void warnDuplicated(List<ModuleNeeded> modules, Set<String> duplicates, String type) {
        for (String lib : duplicates) {
            Set<String> components = new HashSet<String>();
            Set<String> locations = new HashSet<String>();
            for (ModuleNeeded module : modules) {
                if (lib != null && lib.equals(module.getModuleName())) {
                    components.add(module.getContext());
                    locations.add(module.getModuleLocaion());
                }
            }
            if (locations.size() > 1) {
                CommonExceptionHandler.warn("Library:" + lib + " was defined with different locations.\n"
                        + "Components who define these jars are:" + components + "\n" + "Locations:" + locations);
            }
        }
    }

    private void calculateModulesIndex(List<ModuleNeeded> modules, Map<String, String> libsToRelativePath,
            Set<String> duplicateLocationJar, Map<String, String> libsToMavenUri, Set<String> duplicateMavenUri) {

        for (ModuleNeeded module : modules) {
            String moduleLocation = module.getModuleLocaion();
            // take maven uri from configuration to save in the index , don't generate by module name automatically
            String mavenUrl = module.getMavenURIFromConfiguration();
            if (mavenUrl != null && mavenUrl.startsWith(MavenUrlHelper.MVN_PROTOCOL)) {
                String existUri = libsToMavenUri.get(module.getModuleName());
                if (existUri != null && !existUri.equals(mavenUrl)) {
                    duplicateMavenUri.add(module.getModuleName());
                    final String[] mvnUris = existUri.split(MavenUrlHelper.MVN_INDEX_SPLITER);
                    boolean found = false;
                    for (String uri : mvnUris) {
                        if (uri.equals(mavenUrl)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        String newUriToSave = existUri + MavenUrlHelper.MVN_INDEX_SEPARATOR + mavenUrl;
                        libsToMavenUri.put(module.getModuleName(), newUriToSave);
                    }

                } else {
                    libsToMavenUri.put(module.getModuleName(), mavenUrl);
                }
            }
            if (moduleLocation != null && moduleLocation.startsWith("platform:/")) {
                String relativePath = libsToRelativePath.get(module.getModuleName());
                if (relativePath != null && !relativePath.equals(moduleLocation)) {
                    if (!duplicateLocationJar.contains(moduleLocation)) {
                        duplicateLocationJar.add(module.getModuleName());
                    }
                }
                if (checkJarInstalledFromPlatform(moduleLocation)) {
                    libsToRelativePath.put(module.getModuleName(), moduleLocation);
                } else {
                    // fix the module location if not exist
                    module.setModuleLocaion(null);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#deploy(java.util.Map, org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void savePlatfromURLIndex(Map<String, String> libsToRelativePath, IProgressMonitor... monitorWrap) {
        EMap<String, String> jarsToRelative = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
        boolean modified = false;
        for (String key : libsToRelativePath.keySet()) {
            if (checkJarInstalledFromPlatform(libsToRelativePath.get(key))) {
                jarsToRelative.put(key, libsToRelativePath.get(key));
                modified = true;
            }
        }
        if (modified) {
            LibrariesIndexManager.getInstance().saveStudioIndexResource();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#deployMavenIndex(java.util.Map,
     * org.eclipse.core.runtime.IProgressMonitor[])
     */
    @Override
    public void saveMavenIndex(Map<String, String> libsMavenUriToDeploy, IProgressMonitor... monitorWrap) {
        EMap<String, String> jarsToMavenuri = LibrariesIndexManager.getInstance().getMavenLibIndex().getJarsToRelativePath();
        boolean modified = false;
        for (String key : libsMavenUriToDeploy.keySet()) {
            String mvnUri = libsMavenUriToDeploy.get(key);
            if (!jarsToMavenuri.containsKey(key)
                    || mvnUri != null && jarsToMavenuri.containsKey(key) && !mvnUri.equals(jarsToMavenuri.get(key))) {
                String valueFromIndex = jarsToMavenuri.get(key);
                if (valueFromIndex == null) {
                    jarsToMavenuri.put(key, mvnUri);
                    modified = true;
                } else {
                    // merge the two mvnuri value if needed
                    String newUri = mvnUri;
                    final String[] indexUris = valueFromIndex.split(MavenUrlHelper.MVN_INDEX_SPLITER);
                    final String[] toDeployUris = mvnUri.split(MavenUrlHelper.MVN_INDEX_SPLITER);
                    for (String indexUri : indexUris) {
                        boolean found = false;
                        for (String todeploy : toDeployUris) {
                            if (indexUri.equals(todeploy)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            mvnUri = mvnUri + MavenUrlHelper.MVN_INDEX_SEPARATOR + indexUri;
                        }
                    }
                    jarsToMavenuri.put(key, newUri);
                    modified = true;
                }
            }
        }
        if (modified) {
            LibrariesIndexManager.getInstance().saveMavenIndexResource();
        }
    }

    JarMissingObservable getMissingJarObservableService() {
        if (missingJarObservable == null) {
            BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
            if (bundleContext != null) {
                ServiceReference<?> serviceReference = bundleContext
                        .getServiceReference(JarMissingObservable.class.getCanonicalName());
                if (serviceReference != null) {
                    missingJarObservable = (JarMissingObservable) bundleContext.getService(serviceReference);
                } else {// could not find the hook registry service so log it
                    log.error("Could not find a registered OSGI service for : " + JarMissingObservable.class);
                }
            } else {// bundleContext is null should never happend but log it
                log.error("Could not get bundle context for : " + this.getClass());
            }
        }
        return missingJarObservable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#synToLocalMaven()
     */
    @Override
    public void synToLocalMaven() {
        File libDirectory = getStorageDirectory();
        for (File svnLibFile : libDirectory.listFiles()) {
            if (svnLibFile.isFile()) {
                String jarName = svnLibFile.getName();
                ModuleNeeded testModule = new ModuleNeeded("", jarName, "", true);
                Set<String> toDeploy = guessMavenURI(testModule);
                for (String uriToDeploy : toDeploy) {
                    MavenArtifact parseMvnUrl = MavenUrlHelper.parseMvnUrl(uriToDeploy);
                    // only can redeploy for snapshot
                    if (!parseMvnUrl.getVersion().endsWith(MavenUrlHelper.VERSION_SNAPSHOT)) {
                        continue;
                    }
                    boolean installed = checkJarInstalledInMaven(uriToDeploy);
                    if (installed) {
                        File jarInMaven = new File(mavenJarInstalled.get(uriToDeploy));
                        try {
                            String mavenLibMD5 = DigestUtils.md5Hex(new FileInputStream(jarInMaven));
                            String svnLibMD5 = DigestUtils.md5Hex(new FileInputStream(svnLibFile));
                            // check the md5 to see if jar is updated
                            if (mavenLibMD5 != null && !mavenLibMD5.equals(svnLibMD5)) {
                                deployer.install(svnLibFile.getAbsolutePath(), uriToDeploy);
                            }
                        } catch (FileNotFoundException e) {
                            ExceptionHandler.process(e);
                        } catch (IOException e) {
                            ExceptionHandler.process(e);
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#getMavenUriFromIndex()
     */
    @Override
    public String getMavenUriFromIndex(String jarName) {
        EMap<String, String> jarsToMavenuri = LibrariesIndexManager.getInstance().getMavenLibIndex().getJarsToRelativePath();
        return jarsToMavenuri.get(jarName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#setCustomMavenURI(java.lang.String, java.lang.String)
     */
    @Override
    public void setCustomMavenURI(String orignalURI, String customURI) {
        CustomUriManager.getInstance().put(orignalURI, customURI);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#getCustomMavenURI(java.lang.String)
     */
    @Override
    public String getCustomMavenURI(String orignalURI) {
        return CustomUriManager.getInstance().get(orignalURI);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService#saveCustomMavenURIMap()
     */
    @Override
    public void saveCustomMavenURIMap() {
        CustomUriManager.getInstance().saveCustomURIMap();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.ILibraryManagerService#getPlatformURLFromIndex(java.lang.String)
     */
    @Override
    public String getPlatformURLFromIndex(String jarName) {
        EMap<String, String> platformURLMap = LibrariesIndexManager.getInstance().getStudioLibIndex().getJarsToRelativePath();
        return platformURLMap.get(jarName);
    }

    @Override
    public boolean isJarExistInLibFolder(File jarFile) {
        if (jarFile == null) {
            return false;
        }
        File jarInLib = null;
        try {
            List<File> jarFiles = FilesUtils.getJarFilesFromFolder(getStorageDirectory(), null);
            for (File file : jarFiles) {
                if (file.getName().equals(jarFile.getName())) {
                    jarInLib = file;
                    break;
                }
            }
        } catch (MalformedURLException e) {
            CommonExceptionHandler.process(e);
        }
        return isSameFile(jarFile, jarInLib);
    }

    private boolean isSameFile(File f1, File f2) {
        if (f1 == null || f2 == null) {
            return false;
        }
        String f1Sha1 = getSha1OfFile(f1);
        String f2Sha1 = getSha1OfFile(f2);
        return StringUtils.equalsIgnoreCase(f1Sha1, f2Sha1);
    }

    private String getSha1OfFile(File file) {
        String sha1 = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            sha1 = DigestUtils.shaHex(fileInputStream);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace(); // Just print the exception for debug.
                }
            }
        }
        return sha1;
    }

    private boolean isLocalJarSameAsNexus(String jarUri) {
        TalendLibsServerManager manager = TalendLibsServerManager.getInstance();
        final ArtifactRepositoryBean customNexusServer = manager.getCustomNexusServer();
        return isLocalJarSameAsNexus(manager, customNexusServer, jarUri);
    }

    @Override
    public boolean isLocalJarSameAsNexus(TalendLibsServerManager manager, final ArtifactRepositoryBean customNexusServer,
            String jarUri) {
        if (manager == null || customNexusServer == null || jarUri == null) {
            return false;
        }
        String mavenUri = jarUri;
        if (!MavenUrlHelper.isMvnUrl(mavenUri)) {
            mavenUri = MavenUrlHelper.generateMvnUrlForJarName(mavenUri);
        }
        MavenArtifact artifact = MavenUrlHelper.parseMvnUrl(mavenUri);
        String remoteSha1 = null;
        try {
            if (artifact != null) {
                String repositoryId = customNexusServer.getRepositoryId();
                if (artifact.getVersion() != null && artifact.getVersion().endsWith(MavenConstants.SNAPSHOT)) {
                    repositoryId = customNexusServer.getSnapshotRepId();
                }
                remoteSha1 = manager.resolveSha1(customNexusServer.getServer(), customNexusServer.getUserName(),
                        customNexusServer.getPassword(), repositoryId, artifact.getGroupId(), artifact.getArtifactId(),
                        artifact.getVersion(), artifact.getType());
            }
            if (remoteSha1 != null) {
                String localFilePath = getJarPathFromMaven(mavenUri);
                if (localFilePath != null) {
                    File localFile = new File(localFilePath);
                    String localSha1 = getSha1OfFile(localFile);
                    boolean isSha1Same = StringUtils.equalsIgnoreCase(remoteSha1, localSha1);
                    if (!isSha1Same) {
                        org.talend.utils.io.FilesUtils.deleteFolder(localFile.getParentFile(), true);
                    }
                    return isSha1Same;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    @Override
    public boolean isJarNeedToBeDeployed(File jarFile) {
        if (TalendLibsServerManager.getInstance().getCustomNexusServer() != null && !isLocalJarSameAsNexus(jarFile.getName())) {
            return true;
        }

        String mvnUri = MavenUrlHelper.generateMvnUrlForJarName(jarFile.getName());
        if (!checkJarInstalledInMaven(mvnUri)) {
            return true;
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.model.general.ILibrariesService.IChangedLibrariesListener#afterChangingLibraries()
     */
    @Override
    public void afterChangingLibraries() {
        jarList.clear();
    }

}
