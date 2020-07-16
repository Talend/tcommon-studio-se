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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.daikon.crypto.KeySource;
import org.talend.utils.StudioKeysFileCheck;


/*
* Created by bhe on Oct 30, 2019
*/
public class StudioKeySource implements KeySource {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudioKeySource.class);

    private StudioKeyName keyName;

    private final boolean isEncrypt;

    private final Map<StudioKeyName, String> availableKeys;

    private StudioKeySource(Map<StudioKeyName, String> allKeys, StudioKeyName keyName, boolean isMaxVersion) {
        this.availableKeys = allKeys;
        this.keyName = keyName;
        this.isEncrypt = isMaxVersion;

        if (this.isEncrypt && (this.keyName.isRoutineKey() || this.keyName.isSystemKey())) {
            // return highest version for multiple version encryption key
            this.keyName = availableKeys.keySet().stream()
                    .filter(e -> e.isRoutineKey() == keyName.isRoutineKey() && e.isSystemKey() == keyName.isSystemKey())
                    .max(Comparator.comparing(e -> e.getVersionNumber())).get();
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
    public static StudioKeySource key(Map<StudioKeyName, String> allKeys, String keyName, boolean isEncrypt) {
        return new StudioKeySource(allKeys, new StudioKeyName(keyName), isEncrypt);
    }

    @Override
    public byte[] getKey() throws Exception {
        StudioKeyName keyToLoad = this.getKeyName();
        
        // load key
        String key = availableKeys.get(keyToLoad);
        if (key == null) {
            LOGGER.warn("Can not load " + keyToLoad);
            throw new IllegalArgumentException("Invalid encryption key: " + keyToLoad);
        } else {
            LOGGER.debug("Loaded " + keyToLoad);
            return Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
        }
    }

    /**
     * Get key name corresponding to the key source
     */
    public StudioKeyName getKeyName() {
        return this.keyName;
    }

    /**
     * Load default encryption keys from jar file
     */
    public static Properties loadDefaultKeys() {
        Properties defaultKeys = new Properties();
        // load default keys from jar
        try (InputStream fi = StudioKeySource.class.getResourceAsStream(StudioKeysFileCheck.ENCRYPTION_KEY_FILE_NAME)) {
            defaultKeys.load(fi);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return defaultKeys;
    }
    /**
     * Load all of keys into properties
     */
    public static Map<StudioKeyName, String> loadAllKeys() {
        Map<StudioKeyName, String> retMap = new HashMap<StudioKeyName, String>();

        Properties allKeys = new Properties();
        allKeys.putAll(loadDefaultKeys());

        Properties tempProperty = new Properties();

        // load from file set in system property, so as to override default keys
        String keyPath = System.getProperty(StudioKeysFileCheck.ENCRYPTION_KEY_FILE_SYS_PROP);
        if (keyPath != null) {
            File keyFile = new File(keyPath);
            if (keyFile.exists()) {
                try (InputStream fi = new FileInputStream(keyFile)) {
                    tempProperty.load(fi);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }

        // load system key data from System properties
        tempProperty.putAll(System.getProperties());

        // filter out non system encryption keys
        tempProperty.forEach((k, v) -> {
            try {
                StudioKeyName key = new StudioKeyName(String.valueOf(k));
                if (key.isSystemKey() || key.isRoutineKey()) {
                    allKeys.put(key.getKeyName(), v);
                }
            } catch (IllegalArgumentException e) {
                // just ignore, since lots of system properties are not valid key
            }
        });

        if (LOGGER.isDebugEnabled() || LOGGER.isTraceEnabled()) {
            allKeys.stringPropertyNames().forEach((k) -> LOGGER.debug(k));
        }

        // construct key name
        allKeys.forEach((k, v) -> {
            try {
                retMap.put(new StudioKeyName(String.valueOf(k)), String.valueOf(v));
            } catch (IllegalArgumentException e) {
                // illegal key, just ignore and log error message
                LOGGER.error(e.getMessage(), e);
            }
        });

        return retMap;
    }

}
