// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.commons.utils.time;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.talend.commons.exception.CommonExceptionHandler;

/**
 * DOC sbliu  class global comment. Detailled comment
 */
public class PropertiesFileUtil {
    public static synchronized Properties read(File recordFile, boolean createIfNotExist) {
        Properties props = new Properties();
        if (recordFile != null && exist(recordFile, createIfNotExist)) {
            FileInputStream inStream = null;
            try {
                inStream = new FileInputStream(recordFile);
                props.load(inStream);
            } catch (Exception e) {
                CommonExceptionHandler.log(e.getMessage());
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {//
                    }
                }
            }
        }

        return props;
    }

    public static synchronized void store(File recordFile, Properties props) {
        if (props == null) {
            return;
        }

        if (recordFile != null && exist(recordFile, true)) {
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(recordFile);
                props.store(outputStream, "");
            } catch (IOException e) {
                CommonExceptionHandler.log(e.getMessage());
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
        }
    }

    public static boolean exist(File recordFile, boolean createIfNotExist) {
        boolean exists = recordFile.exists();
        if (!exists && createIfNotExist) {
            try {
                if(!recordFile.getParentFile().exists()) {
                    recordFile.getParentFile().mkdirs();
                }
                exists = recordFile.createNewFile();
                if (!exists) {
                    throw new FileNotFoundException(recordFile.getName());
                }
            } catch (Exception e) {
                CommonExceptionHandler.log(e.getMessage());
                return false;
            }
        }

        return exists;
    }
}
