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

import java.security.Provider;
import java.security.Security;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.talend.daikon.crypto.CipherSource;
import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySource;
import org.talend.daikon.crypto.KeySources;

public class StudioEncryption {

    private static Logger logger = Logger.getLogger(StudioEncryption.class);
    // TODO We should remove default key after implements master key encryption algorithm
    private static final String ENCRYPTION_KEY = "Talend_TalendKey";// The length of key should be 16, 24 or 32.

    public static String PREFIX_PASSWORD = "ENC:["; //$NON-NLS-1$

    public static String POSTFIX_PASSWORD = "]"; //$NON-NLS-1$

    // Encryption key property names
    public static final String KEY_SYSTEM = "system.encryption.key.v1";

    public static final String KEY_PROPERTY = "properties.encryption.key.v1";

    public static final String KEY_NEXUS = "tac.nexus.encryption.key.v1";

    public static final String KEY_ROUTINE = "routine.encryption.key";

    private Encryption defaultEncryption;

    static {
        if (null == Security.getProvider("BC")) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private static final ThreadLocal<Map<String, KeySource>> localKeySources = ThreadLocal.withInitial(() -> {
        Map<String, KeySource> cachedKeySources = new HashMap<String, KeySource>();
        String[] keyNames = { KEY_SYSTEM, KEY_PROPERTY, KEY_NEXUS };
        for (String keyName : keyNames) {
            KeySource ks = loadKeySource(keyName);
            if (ks != null) {
                cachedKeySources.put(keyName, ks);
            }
        }
        cachedKeySources.put(KEY_ROUTINE, KeySources.fixedKey(ENCRYPTION_KEY));
        return cachedKeySources;
    });

    private StudioEncryption(String encryptionKeyName, String providerName) {

        if (encryptionKeyName == null) {
            encryptionKeyName = KEY_SYSTEM;
        }

        if (!encryptionKeyName.equals(KEY_SYSTEM) && !encryptionKeyName.equals(KEY_PROPERTY)
                && !encryptionKeyName.equals(KEY_NEXUS) && !encryptionKeyName.equals(KEY_ROUTINE)) {
            RuntimeException e = new IllegalArgumentException("Invalid encryption key name: " + encryptionKeyName);
            logger.error(e);
            throw e;
        }

        KeySource ks = localKeySources.get().get(encryptionKeyName);

        if (ks == null) {
            ks = loadKeySource(encryptionKeyName);
            if (ks != null) {
                localKeySources.get().put(encryptionKeyName, ks);
            }
        }
        if (ks == null) {
            RuntimeException e = new IllegalArgumentException("Can not load encryption key data: " + encryptionKeyName);
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

    private static KeySource loadKeySource(String encryptionKeyName) {
        if (encryptionKeyName == null) {
            encryptionKeyName = KEY_SYSTEM;
        }

        KeySource ks = KeySources.systemProperty(encryptionKeyName);

        try {
            if (ks.getKey() != null) {
                return ks;
            }
        } catch (Exception e) {
            logger.info("StudioEncryption, can not get encryption key from system property: " + encryptionKeyName);
            ks = KeySources.file(encryptionKeyName);
            try {
                if (ks.getKey() != null) {
                    return ks;
                }
            } catch (Exception ex) {
                logger.info("StudioEncryption, can not get encryption key from file: " + encryptionKeyName);
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
            if (!isEncypted(src)) {
                return PREFIX_PASSWORD + defaultEncryption.encrypt(src) + POSTFIX_PASSWORD;
            }
        } catch (Exception e) {
            // backward compatibility
            logger.error("encrypt error", e);
        }
        return src;
    }

    public String decrypt(String src) {
        // backward compatibility
        if (src == null || src.isEmpty()) {
            return src;
        }
        if (src.isEmpty()) {
            return "";
        }
        try {
            if (isEncypted(src)) {
                return defaultEncryption
                        .decrypt(src.substring(PREFIX_PASSWORD.length(), src.length() - POSTFIX_PASSWORD.length()));
            } else {
                return defaultEncryption.decrypt(src);
            }
        } catch (Exception e) {
            // backward compatibility
            logger.error("decrypt error", e);
        }
        return src;
    }


    /**
     * Get instance of StudioEncryption with given encryption key name
     * 
     * keyName - encryption key name, supported names are
     * StudioEncryption.KEY_SYSTEM,StudioEncryption.KEY_PROPERTY,StudioEncryption.KEY_NEXUS, by default, encrytion key
     * name is StudioEncryption.KEY_SYSTEM
     */
    public static StudioEncryption getStudioEncryption(String keyName) {
        return new StudioEncryption(keyName, null);
    }

    /**
     * Get instance of StudioEncryption with given encryption key name, security provider is "BC"
     * 
     * keyName - encryption key name, supported names are
     * StudioEncryption.KEY_SYSTEM,StudioEncryption.KEY_PROPERTY,StudioEncryption.KEY_NEXUS, by default, encrytion key
     * name is StudioEncryption.KEY_SYSTEM
     */
    public static StudioEncryption getStudioBCEncryption(String keyName) {
        return new StudioEncryption(keyName, "BC");
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

    public static boolean isEncypted(String input) {
        if (input == null || input.length() == 0) {
            return false;
        }
        if (input.startsWith(PREFIX_PASSWORD) && input.endsWith(POSTFIX_PASSWORD)) {
            return true;
        }
        return false;
    }
}
