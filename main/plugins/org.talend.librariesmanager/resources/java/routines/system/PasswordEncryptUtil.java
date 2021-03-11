// ============================================================================
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.talend.daikon.crypto.CipherSources;
import org.talend.daikon.crypto.Encryption;
import org.talend.daikon.crypto.KeySource;

/**
 * DOC chuang class global comment. Detailled comment
 */
public class PasswordEncryptUtil {

    private static final String PREFIX_PASSWORD = "enc:"; //$NON-NLS-1$

    private static final String SEPARATOR_PASSWORD = ":";

    public static final String PASSWORD_FOR_LOGS_VALUE = "...";

    private PasswordEncryptUtil() {
    }

    public static String encryptPassword(String input) throws Exception {
        if (input == null) {
            return input;
        }
        String keyName = JobKeySourceProvider.getInstance().getLatestKeyName();
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_PASSWORD).append(keyName).append(SEPARATOR_PASSWORD).append(getEncryption(keyName).encrypt(input));
        return sb.toString();
    }

    public static String decryptPassword(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }
        if (input.startsWith(PREFIX_PASSWORD)) {
            String[] splitData = input.split("\\:");
            try {
                return getEncryption(splitData[1]).decrypt(splitData[2]);
            } catch (Exception e) {
                // do nothing
            }
        }
        return input;
    }

    private static Encryption getEncryption(final String keyName) throws Exception {
        return new Encryption(JobKeySourceProvider.getInstance().getKeySource(keyName), CipherSources.getDefault());
    }

    private static class JobKeySourceProvider {

        private static final String ENCRYPTION_KEY_PATH_PROP = "encryption.keys.file";

        private static InputStreamKeySources defaultKeySources;

        private static InputStreamKeySources fileKeySources;

        private static class JobKeySourceProviderHolder {

            private static final JobKeySourceProvider instance = new JobKeySourceProvider();
        }

        private JobKeySourceProvider() {
            String keyFilePath = System.getProperty(ENCRYPTION_KEY_PATH_PROP);
            if (keyFilePath != null && keyFilePath.length() > 0 && new File(keyFilePath).exists()) {
                try {
                    fileKeySources = new InputStreamKeySources(new FileInputStream(new File(keyFilePath)));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            try {
                defaultKeySources = new InputStreamKeySources(PasswordEncryptUtil.class.getResourceAsStream("keys.properties"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public static JobKeySourceProvider getInstance() {
            return JobKeySourceProviderHolder.instance;
        }

        public KeySource getKeySource(String keyName) {
            if (fileKeySources != null && fileKeySources.getAllKeyNames().contains(keyName)) {
                return fileKeySources.getKeySource(keyName);
            }
            return defaultKeySources.getKeySource(keyName);
        }

        public String getLatestKeyName() {
            List<String> keyList = new ArrayList<String>();
            keyList.addAll(defaultKeySources.getAllKeyNames());
            if (fileKeySources != null) {
                keyList.addAll(fileKeySources.getAllKeyNames());
            }

            return keyList.stream().max(Comparator.comparing(e -> getVersion(e))).get();
        }

        private Integer getVersion(String version) {
            int num = 0;
            if (version != null) {
                num = Integer.parseInt(version.substring(version.toLowerCase().lastIndexOf("v") + 1));
            }
            return num;
        }
    }

    private static class InputStreamKeySources {

        private static final String KEY_PREFIX = "routine.encryption.key";

        private Map<String, KeySource> keySourceMap = new HashMap<String, KeySource>();

        private InputStream input;

        public InputStreamKeySources(InputStream input) throws Exception {
            this.input = input;
            init();
        }

        private void init() throws Exception {
            if (input != null) {
                try {
                    Properties props = new Properties();
                    props.load(input);
                    Enumeration<?> en = props.propertyNames();
                    while (en.hasMoreElements()) {
                        String key = (String) en.nextElement();
                        if (key.startsWith(KEY_PREFIX)) {
                            String value = props.getProperty(key);
                            if (value != null) {
                                final byte[] keyValue = Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8));
                                keySourceMap.put(key, new KeySource() {

                                    @Override
                                    public byte[] getKey() throws Exception {
                                        return keyValue;
                                    }
                                });
                            }
                        }
                    }
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }
        }

        Set<String> getAllKeyNames() {
            return keySourceMap.keySet();
        }

        KeySource getKeySource(String keyName) {
            return keySourceMap.get(keyName);
        }
    }
}
