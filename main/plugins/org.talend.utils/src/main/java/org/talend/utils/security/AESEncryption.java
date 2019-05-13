package org.talend.utils.security;

// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

public class AESEncryption {

    //TODO We should remove default key after implements master key encryption algorithm 
    private static final String ENCRYPTION_KEY = "Talend_TalendKey";// The length of key should be 16, 24 or 32.

    private static Encryption defaultEncryption;

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
        return new Encryption(KeySources.fixedKey(key), CipherSources.aes());
    }
}
