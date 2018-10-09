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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.service.ITaCoKitService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class UpdatesHelper {

    public static final String FOLDER_PLUGINS = "plugins";

    public static final String FOLDER_OSGI_INF = "OSGI-INF";

    public static final String FILE_ARTIFACTS = "artifacts.xml";

    public static final String FILE_CONTENT = "content.xml";

    public static final String FILE_JAR_ARTIFACTS = "artifacts.jar";

    public static final String FILE_JAR_CONTENT = "content.jar";

    public static final String FILE_XZ_ARTIFACTS = "artifacts.xml.xz";

    public static final String FILE_XZ_CONTENT = "content.xml.xz";

    public static final String FILE_CONFIG_INI = "config.ini";

    public static final String FILE_ECLIPSE_PRODUCT = ".eclipseproduct";

    public static final String COMPONENT_SUFFIX = "_java.xml";

    public static final String NEW_COMPONENT_PREFIX = "installer$$";

    public static boolean existArtifacts(File base) {
        return new File(base, FILE_ARTIFACTS).exists() || new File(base, FILE_JAR_ARTIFACTS).exists()
                || new File(base, FILE_XZ_ARTIFACTS).exists();
    }

    public static boolean isArtifacts(String path) {
        return path.endsWith(FILE_ARTIFACTS) || path.endsWith(FILE_JAR_ARTIFACTS) || path.endsWith(FILE_XZ_ARTIFACTS);
    }

    public static boolean existContent(File base) {
        return new File(base, FILE_CONTENT).exists() || new File(base, FILE_JAR_CONTENT).exists()
                || new File(base, FILE_XZ_CONTENT).exists();
    }

    public static boolean isContent(String path) {
        return path.endsWith(FILE_CONTENT) || path.endsWith(FILE_JAR_CONTENT) || path.endsWith(FILE_XZ_CONTENT);
    }

    /**
     * 
     * will check that existed the plugins folder only.
     */
    public static boolean isPlainUpdate(File file) {
        if (file != null && file.exists()) {
            if (file.isFile() && file.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {
                ZipFileStatus status = new ZipFileStatus(file);
                status.check();
                // don't contain "artifacts.xml" and "content.xml", but has "plugins" folder.
                if (!status.hasArtfacts && !status.hasContents && status.hasPlugins) {
                    return true;
                }
            } else if (file.isDirectory()) {// folder
                // not update site
                if (!existArtifacts(file) && !existContent(file)) {
                    File pluginsFolder = new File(file, FOLDER_PLUGINS);
                    if (pluginsFolder.exists() && pluginsFolder.isDirectory()) { // contained plugins folder
                        final String[] list = pluginsFolder.list();
                        return list != null && list.length > 0; // have one file at least
                    }
                }
            }
        }
        return false;
    }

    /**
     * means it's a patch updatesite or tcompV0 updatesite<br>
     * need check that contain "artifacts.xml", "content.xml" and "plugins" folder in same place.
     */
    public static boolean isUpdateSite(File file) {
        if (file != null && file.exists()) {
            if (file.isFile() && file.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {

                ZipFileStatus status = new ZipFileStatus(file);
                status.check();
                // contained "artifacts.xml", "content.xml" and "plugins" folder
                if (status.hasArtfacts && status.hasContents && status.hasPlugins) {
                    return true;
                }

            } else if (file.isDirectory()) { // folder
                // contained "artifacts.xml" and "content.xml"
                if (existArtifacts(file) && existContent(file)) {
                    File pluginsFolder = new File(file, FOLDER_PLUGINS);
                    if (pluginsFolder.exists() && pluginsFolder.isDirectory()) { // contained plugins folder
                        String[] jarFiles = pluginsFolder.list(new FilenameFilter() {

                            @Override
                            public boolean accept(File dir, String name) {
                                // if update site, should contain the jars
                                return name.endsWith(FileExtensions.JAR_FILE_SUFFIX);
                            }
                        });
                        // have one jar at least
                        return jarFiles != null && jarFiles.length > 0;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isTaCoKitCar(File file) {
        boolean isTaCoKitCar = false;
        if (file != null && file.exists()) {
            try {
                ITaCoKitService.getInstance().isTaCoKitCar(file, new NullProgressMonitor());
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return isTaCoKitCar;
    }

    /**
     * 
     * Find the validated updates files.
     * 
     * if zip, will return the zip path directly, if folder, will find the real valid path of update(if plain, is parent
     * of plugins folder, if update site, is parent of artifacts.xml, content.xml and plugins folder )
     */
    public static File[] findUpdateFiles(File baseFile) {
        Set<File> foundUpdateFiles = new HashSet<File>();
        if (baseFile != null && baseFile.exists()) {
            findUpdateBaseFile(foundUpdateFiles, baseFile);
        }
        return foundUpdateFiles.toArray(new File[0]);
    }

    private static void findUpdateBaseFile(Set<File> foundUpdateFiles, File baseFile) {
        if (isPlainUpdate(baseFile) || isUpdateSite(baseFile) && !isComponentUpdateSite(baseFile)) {
            foundUpdateFiles.add(baseFile);
        } else if (baseFile.isDirectory()) {
            final File[] listFiles = baseFile.listFiles();
            if (listFiles != null) {
                for (File subFile : listFiles) {
                    findUpdateBaseFile(foundUpdateFiles, subFile);
                }
            }
        }
    }

    public static boolean isComponentUpdateSite(File file) {
        if (file != null && file.exists()) {
            if (file.isFile() && file.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {
                ZipFileStatus status = new ZipFileStatus(file) {

                    String componentEntryName = null;

                    boolean containComponent = false;

                    @Override
                    protected void judgeZipEntry(ZipFile zipFile, ZipEntry entry) throws IOException {
                        super.judgeZipEntry(zipFile, entry);
                        String path = entry.getName();
                        if (path.endsWith(FileExtensions.JAR_FILE_SUFFIX)) { // is jar
                            // if it's bundle, not from other folder, like lib, m2 repository.
                            IPath p = new Path(path);
                            // must be in plugins
                            if (p.segmentCount() > 1 && p.removeLastSegments(1).lastSegment().equals(FOLDER_PLUGINS)) {
                                InputStream inputStream = zipFile.getInputStream(entry);
                                if (isComponentJar(inputStream)) {
                                    containComponent = true;
                                    componentEntryName = path;
                                }
                            }
                        }
                    }

                    @Override
                    protected void judgePathes() {
                        super.judgePathes();
                        if (containComponent) {
                            IPath componentBundleBasePath = new Path(componentEntryName).removeLastSegments(1);
                            IPath pluginsBasePath = new Path(pluginsEntryName);
                            if (!componentBundleBasePath.equals(pluginsBasePath)) {
                                containComponent = false;
                                hasArtfacts = false;
                                hasContents = false;
                                hasPlugins = false;
                            }

                        } else { // no component, set all false
                            hasArtfacts = false;
                            hasContents = false;
                            hasPlugins = false;
                        }
                    }

                };
                status.check();
                if (status.hasArtfacts && status.hasContents && status.hasPlugins) {
                    return true;
                }
            } else if (file.isDirectory()) {// folder
                if (existArtifacts(file) && existContent(file)) {
                    File pluginsFolder = new File(file, FOLDER_PLUGINS);
                    if (pluginsFolder.exists() && pluginsFolder.isDirectory()) { // contained plugins folder
                        final File[] files = pluginsFolder.listFiles();
                        if (files != null) {
                            for (File f : files) {
                                try {
                                    if (f.getName().endsWith(FileExtensions.JAR_FILE_SUFFIX) // is jar
                                            && isComponentJar(new FileInputStream(f))) {
                                        return true; // have one component at least
                                    }
                                } catch (FileNotFoundException e) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;

    }

    public static boolean isComponentJar(InputStream inputStream) {
        if (inputStream != null) {
            JarInputStream jarStream = null;
            try {
                jarStream = new JarInputStream(inputStream);
                JarEntry jarEntry = null;
                while ((jarEntry = jarStream.getNextJarEntry()) != null) {
                    IPath entryPath = new Path(jarEntry.getName());
                    String xmlName = entryPath.lastSegment();
                    // OSGI-INF/installer$$XXXX.xml
                    if (xmlName.endsWith(FileExtensions.XML_FILE_SUFFIX) && xmlName.startsWith(NEW_COMPONENT_PREFIX)
                            && entryPath.segmentCount() > 1) {
                        String osgiInfFolder = entryPath.removeLastSegments(1).lastSegment();
                        if (osgiInfFolder.equals(FOLDER_OSGI_INF)) {
                            return true;
                        }
                    }

                }
            } catch (IOException e) {
                //
            } finally {
                try {
                    if (jarStream != null) {
                        jarStream.close();
                    }
                } catch (IOException e) {
                    //
                }
            }
        }
        return false;
    }

    public static boolean isOldComponent(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            File[] listFiles = f.listFiles();
            if (listFiles != null) {
                for (File subFile : listFiles) {
                    if (subFile.getName().equals(f.getName() + COMPONENT_SUFFIX)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
