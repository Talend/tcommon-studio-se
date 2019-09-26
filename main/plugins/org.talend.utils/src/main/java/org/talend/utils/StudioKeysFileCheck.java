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
package org.talend.utils;

import java.io.File;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

/*
* Created by bhe on Sep 25, 2019
*/
public class StudioKeysFileCheck {

    public static final String ENCRYPTION_KEY_FILE_SYS_PROP = "encryption.keys.file";

    public static final String ENCRYPTION_KEY_FILE_NAME = "studio.keys";

    private static final Logger LOGGER = Logger.getLogger(StudioKeysFileCheck.class);

    private StudioKeysFileCheck() {

    }

    /**
     * Check whether system property: encryption.keys.file is set, if not then set value of encryption.keys.file
     */
    public static void check(File confDir) {
        if (confDir == null) {
            IllegalArgumentException e = new IllegalArgumentException("Encryption keys file path invalid");
            LOGGER.error(e);
            throw e;
        }
        String keyFile = System.getProperty(ENCRYPTION_KEY_FILE_SYS_PROP);
        if (keyFile == null || keyFile.isEmpty() || !new File(keyFile).exists()) {
            keyFile = Paths.get(confDir.getAbsolutePath(), ENCRYPTION_KEY_FILE_NAME).toString();
            System.setProperty(ENCRYPTION_KEY_FILE_SYS_PROP, keyFile);
        }
        LOGGER.info("encryptionKeyFilePath: " + keyFile);
    }

}
