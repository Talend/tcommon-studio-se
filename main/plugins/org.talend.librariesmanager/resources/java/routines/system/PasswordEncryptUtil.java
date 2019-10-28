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

    private static final String PREFIX_PASSWORD = "ENC:["; //$NON-NLS-1$

    private static final String POSTFIX_PASSWORD = "]"; //$NON-NLS-1$

    public static final String PASSWORD_FOR_LOGS_VALUE = "...";

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
        return new Encryption(JobKeySourceProvider.getInstance().getKeySource(), CipherSources.getDefault());
    }

    private static class JobKeySourceProvider {

        private static KeySource keySource;

        private static class JobKeySourceProviderHolder {

            private static final JobKeySourceProvider instance = new JobKeySourceProvider();
        }

        private JobKeySourceProvider() {
            String keyName = "routine.encryption.key";
            KeySource ks = KeySources.systemProperty(keyName);
            try {
                if (ks != null && ks.getKey() != null) {
                    keySource = ks;
                    return;
                }
            } catch (Exception e) {
                // do nothing
            }
            keySource = new ClassPathKeySource(keyName);
        }

        public static JobKeySourceProvider getInstance() {
            return JobKeySourceProviderHolder.instance;
        }

        public KeySource getKeySource() {
            return keySource;
        }
    }

    private static class ClassPathKeySource implements KeySource {

        private final String keyName;

        private volatile static byte[] keyValue;

        ClassPathKeySource(String keyName) {
            this.keyName = keyName;
        }

        @Override
        public byte[] getKey() throws Exception {
            if (keyValue == null) {
                synchronized (ClassPathKeySource.class) {
                    if (keyValue == null) {
                        try (InputStream inputStream = PasswordEncryptUtil.class.getResourceAsStream("keys.properties")) {
                            if (inputStream == null) {
                                throw new Exception("Input stream should not be null.");
                            }
                            Properties p = new Properties();
                            p.load(inputStream);
                            String value = p.getProperty(keyName);
                            if (value != null) {
                                keyValue = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
                            } else {
                                throw new Exception("Can't find key name: " + keyName);
                            }
                        }
                    }
                }
            }
            return keyValue;
        }
    }
}
