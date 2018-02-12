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
package org.talend.commons.utils.help;

import java.io.File;
import java.io.IOException;

/**
 * DOC xqliu class global comment. Detailled comment
 */
public class UnitTestBuildCommonHelper {

    /**
     * create a new file under the parent.
     * 
     * @param parentPath parent path, if it is null create a new file under the default location
     * @param fileName file name with extension (don't include the path)
     * @param createIt create it if it is not exists
     * @return
     */
    public static File createRealFile(String parentPath, String fileName, boolean createIt) {
        String path = parentPath == null ? System.getProperty("user.dir") : parentPath; //$NON-NLS-1$
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        File file = new File(path + fileName);
        if (createIt) {
            if (!file.exists()) {
                File folder = new File(path);
                if (folder.exists()) {
                    if (!folder.isDirectory()) {
                        folder.delete();
                        folder.mkdirs();
                    }
                } else {
                    folder.mkdirs();
                }
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * create a new folder under the parent.
     * 
     * @param parentPath parent path, if it is null create a new folder under the default location
     * @param folderName file name (don't include the path)
     * @param createIt create it if it is not exists
     * @return
     */
    public static File createRealFolder(String parentPath, String folderName, boolean createIt) {
        String path = parentPath == null ? System.getProperty("user.dir") : parentPath; //$NON-NLS-1$
        if (!path.endsWith(File.separator)) {
            path += File.separator;
        }
        File file = new File(path + folderName);
        if (createIt) {
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return file;
    }
}
