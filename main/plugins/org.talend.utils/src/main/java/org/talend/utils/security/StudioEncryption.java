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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.talend.daikon.crypto.CipherSource;
import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.utils.StudioKeysFileCheck;

public class StudioEncryption {

    private static final Logger LOGGER = Logger.getLogger(StudioEncryption.class);

    private static final String ENCRYPTION_KEY_FILE_NAME = StudioKeysFileCheck.ENCRYPTION_KEY_FILE_NAME;

    private static final String ENCRYPTION_KEY_FILE_SYS_PROP = StudioKeysFileCheck.ENCRYPTION_KEY_FILE_SYS_PROP;

    private static final String PREFIX_PASSWORD_M3 = "ENC:[";

    private static final String PREFIX_PASSWORD = "enc:"; //$NON-NLS-1$

    private static final Pattern REG_ENCRYPTED_DATA_SYSTEM = Pattern
            .compile("^enc\\:system\\.encryption\\.key\\.v\\d\\:\\p{Print}+");

    private static final Pattern REG_ENCRYPTED_DATA_MIGRATION = Pattern
            .compile("^enc\\:migration\\.token\\.encryption\\.key\\:\\p{Print}+");

    private static final Pattern REG_ENCRYPTED_DATA_ROUTINE = Pattern.compile("^enc\\:routine\\.encryption\\.key\\:\\p{Print}+");

    // Encryption key name shipped in M3
    private static final String KEY_SYSTEM_M3 = StudioKeySource.KEY_SYSTEM_PREFIX + "1";

    private static final String KEY_MIGRATION_TOKEN = "migration.token.encryption.key";

    // TODO: this fixed key will be removed
    private static final String KEY_ROUTINE = StudioKeySource.KEY_FIXED;

    private EncryptionKeyName keyName;

    private String securityProvider;

    public enum EncryptionKeyName {
        SYSTEM(KEY_SYSTEM_M3),
        ROUTINE(KEY_ROUTINE),
        MIGRATION_TOKEN(KEY_MIGRATION_TOKEN);

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

    private static final ThreadLocal<Properties> LOCALCACHEDALLKEYS = ThreadLocal.withInitial(() -> {
        return StudioKeySource.loadAllKeys();
    });

    private StudioEncryption(EncryptionKeyName encryptionKeyName, String providerName) {
        this.keyName = encryptionKeyName;
        this.securityProvider = providerName;
    }

    private static StudioKeySource getKeySource(String encryptionKeyName, boolean isEncrypt) {
        Properties allKeys = LOCALCACHEDALLKEYS.get();

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
            StudioKeySource ks = getKeySource(KEY_SYSTEM_M3, false);
            // compatible with M3, decrypt by default key: system.encryption.key.v1
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
            File keyFile = new File(keyPath);
            if (!keyFile.exists()) {
                if (isStudio()) {
                    // load all keys
                    Properties p = new Properties();
                    try (InputStream fi = StudioEncryption.class.getResourceAsStream(ENCRYPTION_KEY_FILE_NAME)) {
                        p.load(fi);
                    } catch (IOException e) {
                        LOGGER.error("load encryption keys error", e);
                    }
                    // EncryptionKeyName.MIGRATION_TOKEN are not allowed to be updated
                    p.remove(EncryptionKeyName.MIGRATION_TOKEN.name);

                    // persist keys to ~configuration/studio.keys
                    try (OutputStream fo = new FileOutputStream(keyFile)) {
                        p.store(fo, "studio encryption keys");
                    } catch (IOException e) {
                        LOGGER.error("persist encryption keys error", e);
                    }
                    LOGGER.info("updateConfig, studio environment, key file setup completed");
                } else {
                    LOGGER.info("updateConfig, non studio environment, skip setup of key file");
                }
            }
        }
    }

    private static boolean isStudio() {
        String osgiFramework = System.getProperty("osgi.framework");
        return osgiFramework != null && osgiFramework.contains("eclipse");
    }
}
