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
    
    public static String PREFIX_PASSWORD = "ENC:["; //$NON-NLS-1$
    
    public static String POSTFIX_PASSWORD = "]"; //$NON-NLS-1$

    private static Encryption defaultEncryption;

    public static String encryptPassword(String input) throws Exception {
        if (input == null) {
            return input;
        }
        return PREFIX_PASSWORD + getEncryption().encrypt(input) + POSTFIX_PASSWORD;
    }

    public static String decryptPassword(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        if (input.startsWith(PREFIX_PASSWORD) && input.endsWith(POSTFIX_PASSWORD)) {
            try {
                return getEncryption()
                        .decrypt(input.substring(PREFIX_PASSWORD.length(), input.length() - POSTFIX_PASSWORD.length()));
            } catch (Exception e) {
                // do nothing
            }
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
