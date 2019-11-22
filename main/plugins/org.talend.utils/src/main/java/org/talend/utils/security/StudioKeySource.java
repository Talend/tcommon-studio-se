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
package org.talend.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.talend.daikon.crypto.KeySource;
import org.talend.utils.StudioKeysFileCheck;


/*
* Created by bhe on Oct 30, 2019
*/
public class StudioKeySource implements KeySource {

    private static final Logger LOGGER = Logger.getLogger(StudioKeySource.class);

    public static final String KEY_SYSTEM_PREFIX = "system.encryption.key.v";

    public static final String KEY_FIXED = "routine.encryption.key";

    // TODO: this fixed key will be removed shortly, jira:TUP-22966
    private static final String FIXED_ENCRYPTION_KEY_DATA = "Talend_TalendKey";

    private String keyName;

    private final boolean isEncrypt;

    private final Properties availableKeys;

    private StudioKeySource(Properties allKeys, String keyName, boolean isMaxVersion) {
        this.availableKeys = allKeys;
        this.keyName = keyName;
        this.isEncrypt = isMaxVersion;

        if (this.isEncrypt && this.keyName.startsWith(KEY_SYSTEM_PREFIX)) {
            // return highest version for system encryption key
            this.keyName = availableKeys.stringPropertyNames().stream().filter(e -> e.startsWith(KEY_SYSTEM_PREFIX))
                    .max(Comparator.comparing(e -> getVersion(e))).get();
        }
    }

    /**
     * <p>
     * always get encryption key, key name format: {keyname}.{version}
     * </p>
     * <p>
     * for example, system.encryption.key.v1
     * </p>
     * 
     * @param allKeys all of keys
     * @param keyName requested encryption key name
     * @param isEncrypt indicate whether the encryption key is used for encryption
     */
    public static StudioKeySource key(Properties allKeys, String keyName, boolean isEncrypt) {
        return new StudioKeySource(allKeys, keyName, isEncrypt);
    }

    @Override
    public byte[] getKey() throws Exception {
        String keyToLoad = this.getKeyName();
        
        // load key
        String key = availableKeys.getProperty(keyToLoad);
        if (key == null) {
            LOGGER.warn("Can not load " + keyToLoad);
            throw new IllegalArgumentException("Invalid encryption key: " + keyToLoad);
        } else {
            LOGGER.debug("Loaded " + keyToLoad);
            return Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    private static int getVersion(String keyName) {
        int idx = keyName.lastIndexOf('.');
        try {
            return Integer.parseInt(keyName.substring(idx + 2));
        } catch (NumberFormatException e) {
            LOGGER.warn("Parse version of encryption key error, key: " + keyName);
        }
        return 0;
    }

    /**
     * Get key name corresponding to the key source
     */
    public String getKeyName() {
        return this.keyName;
    }

    /**
     * Load all of keys into properties
     */
    public static Properties loadAllKeys() {
        Properties allKeys = new Properties();
        // load default keys from jar
        try (InputStream fi = StudioKeySource.class.getResourceAsStream(StudioKeysFileCheck.ENCRYPTION_KEY_FILE_NAME)) {
            allKeys.load(fi);
        } catch (IOException e) {
            LOGGER.error(e);
        }

        Properties tempProperty = new Properties();

        // load from file set in system property, so as to override default keys
        String keyPath = System.getProperty(StudioKeysFileCheck.ENCRYPTION_KEY_FILE_SYS_PROP);
        if (keyPath != null) {
            File keyFile = new File(keyPath);
            if (keyFile.exists()) {
                try (InputStream fi = new FileInputStream(keyFile)) {
                    tempProperty.load(fi);
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }

        // load system key data from System properties
        tempProperty.putAll(System.getProperties());

        // filter out non system encryption keys
        tempProperty.forEach((k, v) -> {
            String key = String.valueOf(k);
            if (key.startsWith(KEY_SYSTEM_PREFIX)) {
                allKeys.put(key, v);
            }
        });

        // add fixed key
        allKeys.put(KEY_FIXED, Base64.getEncoder().encodeToString(FIXED_ENCRYPTION_KEY_DATA.getBytes()));

        if (LOGGER.isDebugEnabled() || LOGGER.isTraceEnabled()) {
            allKeys.stringPropertyNames().forEach((k) -> LOGGER.debug(k));
        }
        return allKeys;
    }

}
