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
package routines.system;

import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySources;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class PasswordEncryptUtil {

    public static String ENCRYPT_KEY = "Encrypt"; //$NON-NLS-1$

    private static final String ENCRYPTION_KEY = "Talend_TalendKey";

    private static Encryption defaultEncryption;

    public static String encryptPassword(String input) throws Exception {
        if (input == null) {
            return input;
        }
        return getEncryption().encrypt(input);
    }

    public static String decryptPassword(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        try {
            return getEncryption().decrypt(input);
        } catch (Exception e) {
            // do nothing
        }
        return input;
    }

    private static Encryption getEncryption() {
        if (defaultEncryption == null) {
            defaultEncryption = new Encryption(KeySources.fixedKey(ENCRYPTION_KEY), CipherSources.aes());
        }
        return defaultEncryption;
    }

    public static final String PASSWORD_FOR_LOGS_VALUE = "...";

}
