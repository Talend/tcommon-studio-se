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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.Provider;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.talend.daikon.crypto.CipherSource;
import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySource;
import org.talend.daikon.crypto.KeySources;
import org.talend.utils.StudioKeysFileCheck;

public class StudioEncryption {

    private static final Logger logger = Logger.getLogger(StudioEncryption.class);

    // TODO We should remove default key after implements master key encryption algorithm
    private static final String ENCRYPTION_KEY = "Talend_TalendKey";// The length of key should be 16, 24 or 32.

    private static final String ENCRYPTION_KEY_FILE_NAME = StudioKeysFileCheck.ENCRYPTION_KEY_FILE_NAME;

    private static final String ENCRYPTION_KEY_FILE_SYS_PROP = StudioKeysFileCheck.ENCRYPTION_KEY_FILE_SYS_PROP;

    public static final String PREFIX_PASSWORD = "ENC:["; //$NON-NLS-1$

    public static final String POSTFIX_PASSWORD = "]"; //$NON-NLS-1$

    // Encryption key property names
    public static final String KEY_SYSTEM = "system.encryption.key.v1";

    public static final String KEY_MIGRATION_TOKEN = "migration.token.encryption.key";

    public static final String KEY_ROUTINE = "routine.encryption.key";

    public enum EnryptionKeyName {
        SYSTEM(KEY_SYSTEM),
        ROUTINE(KEY_ROUTINE),
        MIGRATION_TOKEN(KEY_MIGRATION_TOKEN);

        private final String name;

        EnryptionKeyName(String name) {
            this.name = name;
        }
    }

    static {
        // set up key file
        updateConfig();
    }

    private Encryption defaultEncryption;

    private static final ThreadLocal<Map<EnryptionKeyName, KeySource>> LOCALCACHEDKEYSOURCES = ThreadLocal.withInitial(() -> {
        Map<EnryptionKeyName, KeySource> cachedKeySources = new HashMap<EnryptionKeyName, KeySource>();
        EnryptionKeyName[] keyNames = { EnryptionKeyName.SYSTEM, EnryptionKeyName.MIGRATION_TOKEN };
        for (EnryptionKeyName keyName : keyNames) {
            KeySource ks = loadKeySource(keyName);
            if (ks != null) {
                cachedKeySources.put(keyName, ks);
            }
        }
        cachedKeySources.put(EnryptionKeyName.ROUTINE, KeySources.fixedKey(ENCRYPTION_KEY));
        return cachedKeySources;
    });

    private StudioEncryption(EnryptionKeyName encryptionKeyName, String providerName) {
        if (encryptionKeyName == null) {
            encryptionKeyName = EnryptionKeyName.SYSTEM;
        }

        KeySource ks = LOCALCACHEDKEYSOURCES.get().get(encryptionKeyName);

        if (ks == null) {
            ks = loadKeySource(encryptionKeyName);
            if (ks != null) {
                LOCALCACHEDKEYSOURCES.get().put(encryptionKeyName, ks);
            }
        }
        if (ks == null) {
            RuntimeException e = new IllegalArgumentException("Can not load encryption key data: " + encryptionKeyName.name);
            logger.error(e);
            throw e;
        }

        CipherSource cs = null;
        if (providerName != null && !providerName.isEmpty()) {
            Provider p = Security.getProvider(providerName);
            cs = CipherSources.aes(p);
        }

        if (cs == null) {
            cs = CipherSources.getDefault();
        }

        defaultEncryption = new Encryption(ks, cs);
    }

    private static KeySource loadKeySource(EnryptionKeyName encryptionKeyName) {
        if (encryptionKeyName == null) {
            encryptionKeyName = EnryptionKeyName.SYSTEM;
        }

        KeySource ks = KeySources.systemProperty(encryptionKeyName.name);

        try {
            if (ks.getKey() != null) {
                return ks;
            }
        } catch (Exception e) {
            logger.debug("StudioEncryption, can not get encryption key from system property: " + encryptionKeyName.name);
            ks = KeySources.file(encryptionKeyName.name);
            try {
                if (ks.getKey() != null) {
                    return ks;
                }
            } catch (Exception ex) {
                logger.info("StudioEncryption, can not get encryption key from file: " + encryptionKeyName.name);
            }
        }

        // for others, tac,jobserver etc, load default keys from jars if they are not found in system properties
        if (!isStudio()) {
            try (InputStream fi = StudioEncryption.class.getResourceAsStream(ENCRYPTION_KEY_FILE_NAME)) {
                Properties p = new Properties();
                p.load(fi);
                String key = p.getProperty(encryptionKeyName.name);
                if (key != null) {
                    byte[] keyData = Base64.getDecoder().decode(key.getBytes(StandardCharsets.UTF_8));
                    return () -> keyData;
                }
            } catch (IOException e) {
                logger.error("non studio, load keysource error", e);
            }
        }

        return null;
    }

    public String encrypt(String src) {
        // backward compatibility
        if (src == null) {
            return src;
        }
        try {
            if (!hasEncryptionSymbol(src)) {
                return PREFIX_PASSWORD + defaultEncryption.encrypt(src) + POSTFIX_PASSWORD;
            }
        } catch (Exception e) {
            // backward compatibility
            logger.error("encrypt error", e);
            return null;
        }
        return src;
    }

    public String decrypt(String src) {
        // backward compatibility
        if (src == null || src.isEmpty()) {
            return src;
        }
        try {
            if (hasEncryptionSymbol(src)) {
                return defaultEncryption
                        .decrypt(src.substring(PREFIX_PASSWORD.length(), src.length() - POSTFIX_PASSWORD.length()));
            } else {
                return defaultEncryption.decrypt(src);
            }
        } catch (Exception e) {
            // backward compatibility
            logger.error("decrypt error", e);
            return null;
        }
    }


    /**
     * Get instance of StudioEncryption with given encryption key name
     * 
     * keyName - see {@link StudioEncryption.EnryptionKeyName}, {@link StudioEncryption.EnryptionKeyName.SYSTEM} by
     * default
     */
    public static StudioEncryption getStudioEncryption(EnryptionKeyName keyName) {
        return new StudioEncryption(keyName, null);
    }

    /**
     * Get instance of StudioEncryption with given encryption key name, security provider is "BC"
     * 
     * keyName - see {@link StudioEncryption.EnryptionKeyName}
     */
    public static StudioEncryption getStudioBCEncryption(EnryptionKeyName keyName) {
        return new StudioEncryption(keyName, "BC");
    }

    public static boolean hasEncryptionSymbol(String input) {
        if (input == null || input.length() == 0) {
            return false;
        }
        return input.startsWith(PREFIX_PASSWORD) && input.endsWith(POSTFIX_PASSWORD);
    }

    private static void updateConfig() {
        String keyPath = System.getProperty(ENCRYPTION_KEY_FILE_SYS_PROP);
        if (keyPath != null) {
            File keyFile = new File(keyPath);
            if (!keyFile.exists()) {
                if (isStudio()) {
                    // set up keys
                    try (InputStream fi = StudioEncryption.class.getResourceAsStream(ENCRYPTION_KEY_FILE_NAME)) {
                        Files.copy(fi, keyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        logger.error("updateConfig error", e);
                    }
                    logger.info("updateConfig, studio environment, key file setup completed");
                } else {
                    logger.info("updateConfig, non studio environment, skip setup of key file");
                }
            }
        }
    }

    private static boolean isStudio() {
        String osgiFramework = System.getProperty("osgi.framework");
        if (osgiFramework != null && osgiFramework.contains("eclipse")) {
            return true;
        }
        return false;
    }
}
