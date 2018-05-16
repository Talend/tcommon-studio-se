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

import java.io.File;
import java.io.FileFilter;

import org.talend.commons.utils.io.FilesUtils;

/**
 * bqian class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class PropertiesCollector {

    public static void main(String[] args) throws Exception {
        File file = new File(""); //$NON-NLS-1$
        File desFile = new File("c:/talend_properites"); //$NON-NLS-1$
        desFile.mkdir();
        File workspace = new File(file.getAbsolutePath() + "/../"); //$NON-NLS-1$
        FileFilter folderFilter = new FileFilter() {

            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                if (fileName.equals("bin") || fileName.equals("class") || fileName.equals("classes")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    return false;
                }

                return true;
            }
        };

        FileFilter fileFilter = new FileFilter() {

            public boolean accept(File pathname) {
                String fileName = pathname.getName();
                if (fileName.equals("build.properties")) { //$NON-NLS-1$
                    return false;
                }
                return pathname.getName().endsWith(".properties"); //$NON-NLS-1$
            }
        };

        FilesUtils.copyFolder(workspace, desFile, true, folderFilter, fileFilter, true);
        FilesUtils.removeEmptyFolder(desFile);
    }
}
