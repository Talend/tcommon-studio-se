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
package org.talend.commons.utils.resource;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ZipFileStatus {

    static final String ENTRY_SEP = "/";

    boolean hasArtfacts;

    boolean hasContents;

    boolean hasPlugins;

    final File file;

    String artifactEntryName = null;

    String contentEntryName = null;

    String pluginsEntryName = null;

    public ZipFileStatus(File file) {
        super();
        this.file = file;
    }

    public void check() {
        if (file != null && file.exists() && file.isFile() && file.getName().endsWith(FileExtensions.ZIP_FILE_SUFFIX)) {
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(file);

                Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipFile.entries();
                while (enumeration.hasMoreElements()) {
                    judgeZipEntry(zipFile,enumeration.nextElement());
                }

                judgePathes();

            } catch (ZipException e) {
                ExceptionHandler.process(e);
            } catch (IOException e) {
                ExceptionHandler.process(e);
            } finally {
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }

        }
    }

    protected void judgeZipEntry(ZipFile zipFile,ZipEntry entry) throws IOException{
        String path = entry.getName();
        if (UpdatesHelper.isArtifacts(path)) {
            hasArtfacts = true;
            artifactEntryName = path;
        } else if (UpdatesHelper.isContent(path)) {
            hasContents = true;
            contentEntryName = path;
        } else {
            String basePluginsPath = getBasePluginsPath(path);
            if (!hasPlugins && basePluginsPath != null) { // only deal with the first plugins
                hasPlugins = true;
                pluginsEntryName = basePluginsPath;
            }
        }
    }
    protected void judgePathes(){
     // existed, and artifact and content should be in same path
        if (hasArtfacts && hasContents) {
            IPath artifactBasePath = new Path(artifactEntryName).removeLastSegments(1);
            IPath contentBasePath = new Path(contentEntryName).removeLastSegments(1);
            // if not in same folder, force to set to be invalid
            if (!artifactBasePath.equals(contentBasePath)) {
                hasArtfacts = false;
                hasContents = false;
            }

            // if the artifact and content are still in same path , means it's update site. will try to
            // check plugins folder also
            if (hasArtfacts && hasContents && hasPlugins) {
                IPath pluginsBasePath = new Path(pluginsEntryName).removeLastSegments(1);
                if (!artifactBasePath.equals(pluginsBasePath)) {
                    // force to set all to be invalid
                    hasArtfacts = false;
                    hasContents = false;
                    hasPlugins = false;
                }
            }
        }
    }

    public String getBasePluginsPath(String path) {
        if (path == null) {
            return null;
        }
        if (path.length() > 0 && !path.endsWith(ENTRY_SEP)) {
            path = path + ENTRY_SEP;
        }
        // espcially for winrar zip, the plugins folder is part of path
        if (path.startsWith(UpdatesHelper.FOLDER_PLUGINS + ENTRY_SEP)) {
            return UpdatesHelper.FOLDER_PLUGINS + ENTRY_SEP; // just plugins/
        } else {
            String pluginSegment = ENTRY_SEP + UpdatesHelper.FOLDER_PLUGINS + ENTRY_SEP;
            if (path.contains(pluginSegment)) { // in middle
                return path.substring(0, path.indexOf(pluginSegment) + pluginSegment.length());
            }
        }

        return null; // means no plugins
    }

}
