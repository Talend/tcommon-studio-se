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
package org.talend.core.classloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.ILibraryManagerService;
import org.talend.core.database.conn.ConnParameterKeys;
import org.talend.core.model.general.ModuleNeeded;
import org.talend.core.model.general.Project;
import org.talend.core.model.metadata.IMetadataConnection;
import org.talend.core.runtime.hd.IHDistribution;
import org.talend.core.runtime.hd.IHDistributionVersion;
import org.talend.core.runtime.hd.hive.HiveMetadataHelper;
import org.talend.repository.ProjectManager;
import org.talend.utils.io.FilesUtils;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class ClassLoaderFactory {

    public final static String EXTENSION_POINT_ID = "org.talend.core.runtime.classloader_provider"; //$NON-NLS-1$

    private static IConfigurationElement[] configurationElements = null;

    private static Map<String, DynamicClassLoader> classLoadersMap = null;

    public final static String SEPARATOR = ";"; //$NON-NLS-1$

    public final static String KEY_SEPARATOR = ":"; //$NON-NLS-1$

    private final static String PATH_SEPARATOR = "/"; //$NON-NLS-1$

    public final static String INDEX_ATTR = "index"; //$NON-NLS-1$

    public final static String LIB_ATTR = "libraries"; //$NON-NLS-1$

    public final static String PARENT_ATTR = "parent"; //$NON-NLS-1$

    /**
     * DOC ycbai Comment method "getClassLoader".
     * 
     * @param index
     * @return the classLoader by index
     */
    public static DynamicClassLoader getClassLoader(String index) {
        return getClassLoader(index, true);
    }

    public static DynamicClassLoader getClassLoader(String index, boolean showDownloadIfNotExist) {
        if (isCacheChanged()) {
            init();
        }
        DynamicClassLoader classLoader = classLoadersMap.get(index);
        if (classLoader == null) {
            classLoader = findLoader(index, null, showDownloadIfNotExist);
        }

        return classLoader;
    }

    public static DynamicClassLoader getClassLoader(String index, ClassLoader parentClassLoader) {
        if (isCacheChanged()) {
            init();
        }
        DynamicClassLoader classLoader = classLoadersMap.get(index);
        if (classLoader == null) {
            classLoader = findLoader(index, parentClassLoader, true);
        }

        return classLoader;
    }

    public static DynamicClassLoader getCustomClassLoader(String index, String jarsStr) {
        return getCustomClassLoader(index, jarsStr, SEPARATOR);
    }

    protected static DynamicClassLoader getCustomClassLoader(String index, String jars, String jarSeparator) {
        Set<String> jarSet = new HashSet<String>();
        if (StringUtils.isNotBlank(jars)) {
            String[] jarsArray = jars.split(jarSeparator);
            for (String jar : jarsArray) {
                jarSet.add(jar);
            }
        }

        return getCustomClassLoader(index, jarSet);
    }

    /**
     * DOC ycbai Comment method "getCustomClassLoader".
     * 
     * @param index
     * @param libraries
     * @return the classLoader by specific libraries
     */
    public static DynamicClassLoader getCustomClassLoader(String index, Set<String> libraries) {
        if (libraries == null || libraries.size() == 0) {
            return null;
        }

        DynamicClassLoader classLoader = getClassLoader(index);
        if (classLoader == null) {
            classLoader = createCustomClassLoader(index, libraries);
        } else {
            boolean changed;
            Set<String> oldLibraries = classLoader.getLibraries();
            Set<String> oldLibrariesClone = new HashSet<String>(oldLibraries);
            changed = oldLibrariesClone.retainAll(libraries);
            if (!changed) {
                Set<String> newLibrariesClone = new HashSet<String>(libraries);
                changed = newLibrariesClone.retainAll(oldLibraries);
            }
            if (changed) {
                File libFolder = new File(classLoader.getLibStorePath());
                if (libFolder.exists()) {
                    FilesUtils.removeFolder(libFolder, true);
                }
                classLoader = createCustomClassLoader(index, libraries);
            }
        }

        return classLoader;
    }

    private static DynamicClassLoader createCustomClassLoader(String index, Set<String> libraries) {
        DynamicClassLoader classLoader = new DynamicClassLoader();
        loadLibraries(classLoader, libraries.toArray(new String[0]), true);
        classLoadersMap.put(index, classLoader);

        return classLoader;
    }

    private static void init() {
        File tmpFolder = getTmpFolder();
        if (tmpFolder.exists()) {
            FilesUtils.removeFolder(tmpFolder, true);
        }
        classLoadersMap = new ConcurrentHashMap<String, DynamicClassLoader>();
    }

    public static IConfigurationElement findIndex(String index) {
        IConfigurationElement[] elements = getConfigurationElements();
        if (StringUtils.isNotEmpty(index) && elements != null) {
            for (IConfigurationElement current : elements) {
                String key = current.getAttribute(INDEX_ATTR);
                if (index.equals(key)) {
                    return current;
                }
            }
        }
        return null;
    }

    public static String[] getLibs(String index) {
        IConfigurationElement current = findIndex(index);
        return getLibs(current);
    }

    public static String[] getLibs(IConfigurationElement current) {
        if (current != null) {
            String libraries = current.getAttribute(LIB_ATTR);
            if (StringUtils.isNotEmpty(libraries)) {
                return libraries.split(SEPARATOR);
            }
        }
        return new String[0];
    }

    private static synchronized DynamicClassLoader findLoader(String index, ClassLoader parentLoader,
            boolean showDownloadIfNotExist) {
        IConfigurationElement current = findIndex(index);
        if (current != null) {
            // String key = current.getAttribute(INDEX_ATTR);
            // String libraries = current.getAttribute(LIB_ATTR);
            String parentKey = current.getAttribute(PARENT_ATTR);

            ClassLoader parentClassLoader = null;
            // take parent classlaoder in extensions first
            if (StringUtils.isNotEmpty(parentKey)) {
                parentClassLoader = getClassLoader(parentKey, showDownloadIfNotExist);
            }
            if (parentClassLoader == null) {
                parentClassLoader = parentLoader;
            }

            DynamicClassLoader classLoader = null;
            if (parentClassLoader == null) {
                classLoader = new DynamicClassLoader();
            } else {
                classLoader = new DynamicClassLoader(parentClassLoader);
            }
            boolean putInCache = true;
            String[] librariesArray = getLibs(current);
            if (librariesArray.length > 0) {
                putInCache = loadLibraries(classLoader, librariesArray, showDownloadIfNotExist);
            }
            if (putInCache) {
                // if any libraries can't be retreived , do not put it in cache
                classLoadersMap.put(index, classLoader);
            }
            return classLoader;
        }

        return null;
    }

    private static boolean loadLibraries(DynamicClassLoader classLoader, String[] driversArray, boolean showDownloadIfNotExist) {
        List<String> jarPathList = new ArrayList<String>();
        if (driversArray == null || driversArray.length == 0) {
            return true;
        }

        ILibraryManagerService librairesManagerService = (ILibraryManagerService) GlobalServiceRegister.getDefault().getService(
                ILibraryManagerService.class);
        String libPath = getLibPath();
        List<String> driverNotExist = new ArrayList<String>();
        for (String driverName : driversArray) {
            if (StringUtils.isNotEmpty(driverName)) {
                String jarPath = libPath + PATH_SEPARATOR + driverName;
                File jarFile = new File(jarPath);
                if (!jarFile.exists()) {
                    driverNotExist.add(driverName);
                }
                jarPathList.add(jarFile.getAbsolutePath());
            }
        }
        // retreive all needed libs in one time
        boolean putInCache = false;
        if (!driverNotExist.isEmpty()) {
            putInCache = librairesManagerService.retrieve(driverNotExist, libPath, showDownloadIfNotExist,
                    new NullProgressMonitor());
        }
        File libFolder = new File(libPath);
        File[] listFiles = libFolder.listFiles();
        if (listFiles != null && 0 < listFiles.length) {
            for (File file : listFiles) {
                String filePath = file.getAbsolutePath();
                if (!jarPathList.contains(filePath)) {
                    jarPathList.add(filePath);
                }
            }
        }

        classLoader.setLibStorePath(libPath);
        classLoader.addLibraries(jarPathList);
        return putInCache;
    }

    public static String getLibPath() {
        File tmpFolder = getTmpFolder();
        if (!tmpFolder.exists()) {
            tmpFolder.mkdirs();
        }
        try {
            tmpFolder = File.createTempFile("libs", null, tmpFolder); //$NON-NLS-1$
            if (tmpFolder.exists() && tmpFolder.isFile()) {
                tmpFolder.delete();
                tmpFolder.mkdirs();
            }
        } catch (IOException e) {
            // do nothing
        }
        return tmpFolder.getAbsolutePath();
    }

    private static File getTmpFolder() {
        Project project = ProjectManager.getInstance().getCurrentProject();
        IProject physProject;
        String tmpFolderPath = System.getProperty("user.dir"); //$NON-NLS-1$
        try {
            physProject = ResourceUtils.getProject(project.getTechnicalLabel());
            tmpFolderPath = physProject.getFolder("temp").getLocation().toPortableString(); //$NON-NLS-1$
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        tmpFolderPath = tmpFolderPath + "/libraries"; //$NON-NLS-1$

        return new File(tmpFolderPath);
    }

    public static String[] getDriverModuleList(IMetadataConnection metadataConn) {
        String[] moduleList;
        String distroKey = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_DISTRIBUTION);
        String distroVersion = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_VERSION);
        String hiveModel = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HIVE_MODE);

        IHDistribution distribution = HiveMetadataHelper.getDistribution(distroKey, false);
        if (distribution != null && distribution.useCustom()) {
            String jarsStr = (String) metadataConn.getParameter(ConnParameterKeys.CONN_PARA_KEY_HADOOP_CUSTOM_JARS);
            moduleList = jarsStr.split(";"); //$NON-NLS-1$
        } else {
            String index = "HIVE" + KEY_SEPARATOR + distroKey + KEY_SEPARATOR + distroVersion + KEY_SEPARATOR + hiveModel; //$NON-NLS-1$  //$NON-NLS-2$  //$NON-NLS-3$ //$NON-NLS-4$ 
            moduleList = getDriverModuleList(index);
        }
        return moduleList;
    }

    public static String[] getDriverModuleList(String connKeyString) {
        IConfigurationElement[] elements = getConfigurationElements();
        if (connKeyString != null && elements != null) {
            for (IConfigurationElement current : elements) {
                String key = current.getAttribute(INDEX_ATTR);
                if (connKeyString.equals(key)) {
                    String libraries = current.getAttribute(LIB_ATTR);
                    if (StringUtils.isNotEmpty(libraries)) {
                        String[] librariesArray = libraries.split(SEPARATOR);
                        return librariesArray;
                    }
                }
            }
        }
        return null;
    }

    public static DynamicClassLoader getClassLoader(IHDistributionVersion hdVersion) {
        String index = "HadoopModules" + KEY_SEPARATOR + hdVersion.getDistribution().getName() + KEY_SEPARATOR
                + hdVersion.getVersion();
        List<ModuleNeeded> modulesNeeded = hdVersion.getModulesNeeded();
        Set<String> libraries = new HashSet<String>();
        for (ModuleNeeded m : modulesNeeded) {
            libraries.add(m.getModuleName());
        }
        DynamicClassLoader hdClassLoader = getCustomClassLoader(index, libraries);
        return hdClassLoader;
    }

    private static synchronized IConfigurationElement[] getConfigurationElements() {
        if (isCacheChanged()) {
            IExtensionRegistry registry = Platform.getExtensionRegistry();
            configurationElements = registry.getConfigurationElementsFor(EXTENSION_POINT_ID);
        }
        return configurationElements;
    }

    private static boolean isCacheChanged() {
        if (configurationElements == null) {
            return true;
        }
        if (configurationElements != null) {
            for (IConfigurationElement configElement : configurationElements) {
                if (!configElement.isValid()) {
                    return true;
                }
            }
        }
        return false;
    }
}
