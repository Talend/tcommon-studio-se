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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySource;
import org.talend.daikon.crypto.KeySources;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class PasswordEncryptUtil {

    public static final String ENCRYPT_KEY = "Encrypt"; //$NON-NLS-1$

    private  static final String PREFIX_PASSWORD = "ENC:["; //$NON-NLS-1$

    private  static final String POSTFIX_PASSWORD = "]"; //$NON-NLS-1$

    private static Encryption defaultEncryption;

    private static KeySource keySource = null;

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

    private static Encryption getEncryption() throws Exception {
        if (defaultEncryption == null) {
            defaultEncryption = new Encryption(getKeySource(), CipherSources.getDefault());
        }
        return defaultEncryption;
    }

    private static KeySource getKeySource() throws Exception {
        if (keySource == null) {
            keySource = KeySources.systemProperty("routine.encryption.key");
            try {
                if (keySource != null && keySource.getKey() != null) {
                    return keySource;
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        InputStream inputStream = PasswordEncryptUtil.class.getResourceAsStream("keys.properties");
        keySource = new InputStreamKeySource(inputStream, "routine.encryption.key");
        return keySource;
    }

    public static final String PASSWORD_FOR_LOGS_VALUE = "...";

}

class InputStreamKeySource implements KeySource {

    private InputStream inputStream;

    private String keyName;

    private byte[] keyValue = null;

    public InputStreamKeySource(InputStream inputStream, String keyName) {
        this.inputStream = inputStream;
        this.keyName = keyName;
    }

    @Override
    public byte[] getKey() throws Exception {
        if (keyValue != null) {
            return keyValue;
        }
        if (inputStream == null) {
            throw new Exception("Input stream should not be null.");
        }
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore here
            }
        }
        String value = p.getProperty(keyName);
        if (value != null) {
            keyValue = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new Exception("Can't find key name: " + keyName);
        }
        return keyValue;
    }
}
