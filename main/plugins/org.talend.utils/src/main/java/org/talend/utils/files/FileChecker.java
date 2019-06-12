// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.files;

import java.io.File;

import org.talend.utils.sugars.ReturnCode;

/**
 * DOC stephane class global comment. Detailled comment
 */
public class FileChecker {

    public static final String NO_RIGHT_TO_WRITE = "No right to write";

    public static final String PATH_MUST_BE_A_FILE = "Path must be a file";

    public static final String PATH_MUST_BE_A_DIRECTORY = "Path must be a directory";

    public static final String PATH_DOES_NOT_EXIST = "Path does not exist";

    public static void evaluateFilePath(ReturnCode returnCode, File file, boolean mustBeFile, boolean mustBeFolder) {
        if (!file.exists()) {
            returnCode.setOk(false);
            returnCode.setMessage(PATH_DOES_NOT_EXIST);
        } else if (!file.isDirectory() && mustBeFolder) {
            returnCode.setOk(false);
            returnCode.setMessage(PATH_MUST_BE_A_DIRECTORY);
        } else if (!file.isFile() && mustBeFile) {
            returnCode.setOk(false);
            returnCode.setMessage(PATH_MUST_BE_A_FILE);
        } else if (!file.canWrite()) {
            returnCode.setOk(false);
            returnCode.setMessage(NO_RIGHT_TO_WRITE);
        } else {
            returnCode.setOk(true);
        }
    }

}
