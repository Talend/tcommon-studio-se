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
package org.talend.updates.runtime.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.talend.commons.runtime.service.ComponentsInstallComponent;
import org.talend.commons.runtime.service.PatchComponent;

/**
 * created by ycbai on 2017年5月23日 Detailled comment
 *
 */
public class PathUtils {

    public static final String FOLDER_COMPS = "components"; //$NON-NLS-1$

    public static final String FOLDER_INSTALLED = "installed"; //$NON-NLS-1$

    public static final String FOLDER_SHARED = "shared"; //$NON-NLS-1$

    public static final String FOLDER_DOWNLOADED = "downloaded"; //$NON-NLS-1$

    public static final String FOLDER_M2TEMP = "m2temp"; //$NON-NLS-1$

    public static final String FOLDER_PATCHES = PatchComponent.FOLDER_PATCHES;

    public static final String FOLDER_M2_REPOSITORY = ComponentsInstallComponent.FOLDER_M2_REPOSITORY;

    private static final String P2_REP_FILE_URI_PATTERN = "^jar:(.+)!\\/$"; //$NON-NLS-1$

    public static File getComponentsFolder() throws IOException {
        File componentsFolder = new File(Platform.getConfigurationLocation().getDataArea(FOLDER_COMPS).getPath());
        if (!componentsFolder.exists()) {
            componentsFolder.mkdirs();
        }
        return componentsFolder;
    }

    private static File createComponentFolder(String subName) {
        File folder = null;
        try {
            folder = new File(getComponentsFolder(), subName);
        } catch (IOException e) {
            folder = new File(System.getProperty("user.dir"), subName); //$NON-NLS-1$
        }
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }

    public static File getComponentsInstalledFolder() {
        return createComponentFolder(FOLDER_INSTALLED);
    }

    public static File getComponentsSharedFolder() {
        return createComponentFolder(FOLDER_SHARED);
    }

    public static File getComponentsDownloadedFolder() {
        return createComponentFolder(FOLDER_DOWNLOADED);
    }

    public static File getComponentsM2TempFolder() {
        return createComponentFolder(FOLDER_M2TEMP);
    }

    public static File getPatchesFolder() {
        try {
            return new File(Platform.getInstallLocation().getDataArea(FOLDER_PATCHES).getPath());
        } catch (IOException e) {
            //
        }
        return new File(System.getProperty("user.dir"), FOLDER_PATCHES); //$NON-NLS-1$
    }

    public static URI getP2RepURIFromCompFile(File compFile) {
        if (compFile == null) {
            return null;
        }
        return URI.create("jar:" + compFile.toURI().toString() + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public static File getCompFileFromP2RepURI(URI p2RepURI) throws MalformedURLException {
        if (p2RepURI == null) {
            return null;
        }
        String filePath = p2RepURI.toString();
        Matcher matcher = Pattern.compile(P2_REP_FILE_URI_PATTERN).matcher(filePath);
        if (matcher.find()) {
            filePath = matcher.group(1);
        }
        return new File(URI.create(filePath).toURL().getFile());
    }

}
