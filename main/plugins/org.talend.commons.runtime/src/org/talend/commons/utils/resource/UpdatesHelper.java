// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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
import java.io.FilenameFilter;
import java.util.HashSet;
import java.util.Set;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class UpdatesHelper {

    public static final String FOLDER_PLUGINS = "plugins";

    public static final String FILE_ARTIFACTS = "artifacts.xml";

    public static final String FILE_CONTENT = "content.xml";

    public static final String FILE_CONFIG_INI = "config.ini";

    public static final String FILE_ECLIPSE_PRODUCT = ".eclipseproduct";

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
                if (!new File(file, FILE_ARTIFACTS).exists() && !new File(file, FILE_CONTENT).exists()) {
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
                if (new File(file, FILE_ARTIFACTS).exists() && new File(file, FILE_CONTENT).exists()) {
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
        if (isPlainUpdate(baseFile) || isUpdateSite(baseFile)) {
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

}
