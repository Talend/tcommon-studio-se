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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.daikon.crypto.CipherSource;
import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySources;
import org.talend.utils.StudioKeysFileCheck;

public class StudioEncryption {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudioEncryption.class);

    private static final String ENCRYPTION_KEY_FILE_NAME = StudioKeysFileCheck.ENCRYPTION_KEY_FILE_NAME;

    private static final String ENCRYPTION_KEY_FILE_SYS_PROP = StudioKeysFileCheck.ENCRYPTION_KEY_FILE_SYS_PROP;

    private static final String PREFIX_PASSWORD_M3 = "ENC:[";

    public static final String PREFIX_PASSWORD = "enc:"; //$NON-NLS-1$

    private static final Pattern REG_ENCRYPTED_DATA_SYSTEM = Pattern
            .compile("^enc\\:system\\.encryption\\.key\\.v\\d\\:\\p{Print}+");

    private static final Pattern REG_ENCRYPTED_DATA_MIGRATION = Pattern
            .compile("^enc\\:migration\\.token\\.encryption\\.key\\:\\p{Print}+");

    private static final Pattern REG_ENCRYPTED_DATA_ROUTINE = Pattern
            .compile("^enc\\:routine\\.encryption\\.key\\.v\\d\\:\\p{Print}+");

    private EncryptionKeyName keyName;

    private String securityProvider;

    public enum EncryptionKeyName {

        SYSTEM(StudioKeyName.KEY_SYSTEM_DEFAULT),
        ROUTINE(StudioKeyName.KEY_ROUTINE),
        MIGRATION_TOKEN(StudioKeyName.KEY_MIGRATION_TOKEN),
        MIGRATION(StudioKeyName.KEY_MIGRATION); // This key only use to process migration data. Only for DES algorithm

        private final String name;

        EncryptionKeyName(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    static {
        // set up key file
        updateConfig();
    }

    private static final ThreadLocal<Map<StudioKeyName, String>> LOCALCACHEDALLKEYS = ThreadLocal.withInitial(() -> {
        return StudioKeySource.loadAllKeys();
    });

    private StudioEncryption(EncryptionKeyName encryptionKeyName, String providerName) {
        this.keyName = encryptionKeyName;
        this.securityProvider = providerName;
    }

    public static StudioKeySource getKeySource(String encryptionKeyName, boolean isEncrypt) {
        Map<StudioKeyName, String> allKeys = LOCALCACHEDALLKEYS.get();

        StudioKeySource ks = StudioKeySource.key(allKeys, encryptionKeyName, isEncrypt);

        try {
            if (ks.getKey() != null) {
                return ks;
            }
        } catch (Exception e) {
            LOGGER.error("Can not load encryption key: " + encryptionKeyName, e);
        }
        RuntimeException e = new RuntimeException("Can not load encryption key: " + encryptionKeyName);
        LOGGER.error("Can not load encryption key: " + encryptionKeyName, e);
        throw e;
    }

    private Encryption getEncryption(StudioKeySource ks) {
        CipherSource cs = null;
        if (this.securityProvider != null && !this.securityProvider.isEmpty()) {
            Provider p = Security.getProvider(this.securityProvider);
            cs = CipherSources.aesGcm(12, 16, p);
        }

        if (cs == null) {
            cs = CipherSources.getDefault();
        }
        return new Encryption(ks, cs);
    }

    public String encrypt(String src) {
        // backward compatibility
        if (src == null || hasEncryptionSymbol(src)) {
            return src;
        }
        try {
            StudioKeySource ks = getKeySource(this.keyName.name, true);
            StringBuilder sb = new StringBuilder();
            sb.append(PREFIX_PASSWORD);
            sb.append(ks.getKeyName());
            sb.append(":");
            sb.append(getEncryption(ks).encrypt(src));
            return sb.toString();
        } catch (Exception e) {
            // backward compatibility
            LOGGER.error("encrypt error", e);
        }
        return null;
    }

    public String decrypt(String src) {
        // backward compatibility
        if (!hasEncryptionSymbol(src)) {
            return src;
        }
        try {
            if (src.startsWith(PREFIX_PASSWORD)) {
                String[] srcData = src.split("\\:");
                StudioKeySource ks = getKeySource(srcData[1], false);
                return this.getEncryption(ks).decrypt(srcData[2]);
            }
            // decrypt by default key: system.encryption.key.v1 or migration.token.encryption.key
            StudioKeySource ks = getKeySource(this.keyName.name, false);
            return this.getEncryption(ks).decrypt(src.substring(PREFIX_PASSWORD_M3.length(), src.length() - 1));
        } catch (Exception e) {
            // backward compatibility
            LOGGER.error("decrypt error", e);
        }

        return null;
    }


    /**
     * Get instance of StudioEncryption with given encryption key name
     * 
     * keyName - see {@link StudioEncryption.EncryptionKeyName}, {@link StudioEncryption.EncryptionKeyName.SYSTEM} by
     * default
     */
    public static StudioEncryption getStudioEncryption(EncryptionKeyName keyName) {
        return new StudioEncryption(keyName, null);
    }

    /**
     * Get instance of StudioEncryption with given encryption key name, security provider is "BC"
     * 
     * keyName - see {@link StudioEncryption.EncryptionKeyName}
     */
    public static StudioEncryption getStudioBCEncryption(EncryptionKeyName keyName) {
        return new StudioEncryption(keyName, "BC");
    }

    public static boolean hasEncryptionSymbol(String input) {
        return input != null
                && (REG_ENCRYPTED_DATA_SYSTEM.matcher(input).matches() || REG_ENCRYPTED_DATA_MIGRATION.matcher(input).matches()
                        || REG_ENCRYPTED_DATA_ROUTINE.matcher(input).matches()
                        || (input.startsWith(PREFIX_PASSWORD_M3) && input.endsWith("]")));
    }

    private static void updateConfig() {
        String keyPath = System.getProperty(ENCRYPTION_KEY_FILE_SYS_PROP);
        if (keyPath != null) {
            if (isStudio()) {
                File keyFile = new File(keyPath);
                if (!keyFile.exists()) {
                    // load default keys
                    Properties p = new Properties();
                    try (InputStream fi = StudioEncryption.class.getResourceAsStream(ENCRYPTION_KEY_FILE_NAME)) {
                        p.load(fi);
                    } catch (IOException e) {
                        LOGGER.error("load encryption keys error", e);
                    }
                    // EncryptionKeyName.MIGRATION_TOKEN and MIGRATION are not allowed to be updated
                    p.remove(EncryptionKeyName.MIGRATION.name);
                    p.remove(EncryptionKeyName.MIGRATION_TOKEN.name);
                    // persist keys to ~configuration/studio.keys
                    try (OutputStream fo = new FileOutputStream(keyFile)) {
                        p.store(fo, "studio encryption keys");
                    } catch (IOException e) {
                        LOGGER.error("persist encryption keys error", e);
                    }
                    LOGGER.info("updateConfig, studio environment, key file setup completed");
                } else {
                    // key file exists, check whether to generate custom encryption keys
                    try {
                        if (generateEncryptionKeys(keyFile)) {
                            LOGGER.info("Customized encryption keys generated, please synchronize key file " + keyFile
                                    + " to Administrator and Jobserver");
                        }
                    } catch (Exception e) {
                        LOGGER.error("Generate customized encryption keys error", e);
                    }
                }
            } else {
                LOGGER.info("updateConfig, non studio environment, skip setup of key file");
            }
        }
    }

    private static boolean isStudio() {
        String osgiFramework = System.getProperty("osgi.framework");
        return osgiFramework != null && osgiFramework.contains("eclipse");
    }

    /**
     * Generate new encryption keys if encryption key is null or empty
     * 
     * @param keyFile input key file
     */
    public static boolean generateEncryptionKeys(File keyFile) throws Exception {
        boolean propertyChanged = false;
        Properties p = new Properties();
        try (FileInputStream fi = new FileInputStream(keyFile)) {
            p.load(fi);
        }

        Set<Entry<Object, Object>> entries = p.entrySet();
        for (Entry<Object, Object> entry : entries) {
            try {
                StudioKeyName keyName = new StudioKeyName(entry.getKey().toString());
                if (keyName.isSystemKey() || keyName.isRoutineKey()) {
                    if (entry.getValue() == null || entry.getValue().toString().isEmpty()) {
                        // default routine key is not allowed to be customized, reset it
                        if (keyName.isDefaultRoutineKey()) {
                            Properties defaulKeys = StudioKeySource.loadDefaultKeys();
                            entry.setValue(defaulKeys.getProperty(keyName.getKeyName()));
                            LOGGER.warn(keyName.getKeyName() + " customization is not allowed");
                        } else {
                            entry.setValue(Base64.getEncoder().encodeToString(KeySources.random(32).getKey()));
                            propertyChanged = true;
                            LOGGER.debug("Customized encryption key is generated for " + entry.getKey().toString());
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                // log invalid key
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (propertyChanged) {
            try (FileOutputStream fo = new FileOutputStream(keyFile)) {
                p.store(fo, "Generated customized encryption keys");
            }
        }
        return propertyChanged;
    }
}
