package org.talend.utils.security;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.log4j.Logger;
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
import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySources;

public class StudioEncryption {

    //TODO We should remove default key after implements master key encryption algorithm
    private static final String ENCRYPTION_KEY = "Talend_TalendKey";// The length of key should be 16, 24 or 32.

    private static Encryption defaultEncryption;

    private static Encryption externalKeyEncryption;

    private static Logger logger = Logger.getLogger(StudioEncryption.class);

    public static final String ENCRYPTION_KEY_PROPERTY = "encryption.key";

    public static final String ENCRYPTION_KEY_FILE_PROPERTY = "encryption.key.file";

    static {
        // get encryption from system property firstly
        String keyDataStr = System.getProperty(ENCRYPTION_KEY_PROPERTY);
        if (keyDataStr == null) {
            String keyFilePath = System.getProperty(ENCRYPTION_KEY_FILE_PROPERTY, "");
            byte[] keyData = readKeyFile(keyFilePath);
            externalKeyEncryption = getStudioEncryption(keyData);
        } else {
            externalKeyEncryption = getStudioEncryption(decode64(keyDataStr));
        }

    }

    private StudioEncryption() {
    }

    public static String encryptPassword(String input, String key) throws Exception {
        Encryption encryption = getEncryption(key);
        return encryption.encrypt(input);
    }

    public static String encryptPassword(String input) throws Exception {
        Encryption encryption = getEncryption();
        return encryption.encrypt(input);
    }

    public static String decryptPassword(String input, String key) throws Exception {
        Encryption encryption = getEncryption(key);
        return encryption.decrypt(input);
    }

    public static String decryptPassword(String input) throws Exception {
        Encryption encryption = getEncryption();
        return encryption.decrypt(input);
    }

    private static Encryption getEncryption() {
        if (defaultEncryption == null) {
            defaultEncryption = getEncryption(ENCRYPTION_KEY);
        }
        return defaultEncryption;
    }

    private static Encryption getEncryption(String key) {
        return new Encryption(KeySources.fixedKey(key), CipherSources.getDefault());
    }

    public static String encrypt(String src) {
        // backward compatibility
        if (src == null) {
            return null;
        }
        try {
            return externalKeyEncryption.encrypt(src);
        } catch (Exception e) {
            // backward compatibility
            logger.error("encrypt error", e);
        }
        return null;
    }

    public static String decrypt(String src) {
        // backward compatibility
        if (src == null) {
            return null;
        }
        try {
            return externalKeyEncryption.decrypt(src);
        } catch (Exception e) {
            // backward compatibility
            logger.error("decrypt error", e);
        }
        return null;
    }

    /**
     * Read AES key data from given file
     */
    public static byte[] readKeyFile(String keyFilePath) {
        File f = new File(keyFilePath);
        if (!f.exists()) {
            IllegalArgumentException e = new IllegalArgumentException("Invalid key file: " + keyFilePath);
            logger.error("readKeyFile error", e);
            throw e;
        }

        Path path = Paths.get(f.getAbsolutePath());

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            logger.error("readKeyFile error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Get AES encryption according to the given AES key Data
     */
    public static Encryption getStudioEncryption(byte[] keyData) {
        return new Encryption(() -> keyData, CipherSources.getDefault());
    }

    /**
     * Base64 encode given array to String
     */
    public static String encode64(byte[] src) {
        return Base64.getEncoder().encodeToString(src);
    }

    /**
     * Base64 decode given string to byte array
     */
    public static byte[] decode64(String src) {
        return Base64.getDecoder().decode(src);
    }
}
