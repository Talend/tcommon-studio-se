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
package org.talend.core.model.genhtml;


/**
 * This class is used for copying file from one place to the other.
 * 
 * $Id: CopyFileUtils.java 2007-3-9,下午07:28:36 ftang $
 * 
 */
public class FileCopyUtils {

    /**
     * This method is used for coping file from one place to the other.
     * 
     * @param srcFilePath
     * @param destFilePath
     * @throws Exception
     */
    public static void copy(String srcFilePath, String destFilePath) {
        org.talend.commons.runtime.utils.io.FileCopyUtils.copy(srcFilePath, destFilePath);
    }

    public static void copyFolder(String oldPath, String newPath) {
        org.talend.commons.runtime.utils.io.FileCopyUtils.copyFolder(oldPath, newPath);
    }
}
