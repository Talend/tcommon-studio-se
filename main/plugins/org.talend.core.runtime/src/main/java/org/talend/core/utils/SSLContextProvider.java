// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.prefs.SSLPreferenceConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.utils.security.CryptoHelper;

/**
 * 
 * created by wchen on Oct 24, 2016 Detailled comment
 *
 */
public class SSLContextProvider {

    private static SSLContext context;

    private static final IPreferenceStore store = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore();

    private static KeyManager[] buildKeyManagers(String path, String storePass, String keytype) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        InputStream stream = null;
        try {
            if (StringUtils.isEmpty(path)) {
                return null;
            }
            if (!new File(path).exists()) {
                throw new KeyStoreException("Keystore not exist");
            }
            stream = new FileInputStream(path);

            KeyStore tks = KeyStore.getInstance(keytype);
            tks.load(stream, storePass.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
            kmf.init(tks, storePass.toCharArray());

            return kmf.getKeyManagers();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static TrustManager[] buildTrustManagers(String path, String storePass, String trusttype) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        InputStream stream = null;
        try {
            if (StringUtils.isEmpty(path)) {
                return null;
            }
            if (StringUtils.isEmpty(path) || !new File(path).exists()) {
                throw new KeyStoreException("Trust store not exist");
            }
            stream = new FileInputStream(path);

            KeyStore tks = KeyStore.getInstance(trusttype);
            tks.load(stream, storePass.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
            tmf.init(tks);

            return tmf.getTrustManagers();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static X509HostnameVerifier getHostnameVerifier() {
        boolean verify = store.getBoolean(SSLPreferenceConstants.VERIFY_HOSTNAME);
        if (verify) {
            return SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
        } else {
            return SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
    }

    public synchronized static void buildContext(String algorithm, String keypath, String keypass, String keytype,
            String trustpath, String trustpass, String trusttype) {
        try {
            KeyManager[] kms = buildKeyManagers(keypath, keypass, keytype);
            TrustManager[] tms = buildTrustManagers(trustpath, trustpass, trusttype);
            context = SSLContext.getInstance(algorithm);
            context.init(kms, tms, null);
        } catch (Exception e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }

    public static synchronized SSLContext getContext() {
        if (null == context) {
            buildContext();
        }
        return context;
    }

    private static void buildContext() {
        String algorithm = store.getString(SSLPreferenceConstants.SSL_ALGORITHM);
        String keypath = store.getString(SSLPreferenceConstants.KEYSTORE_FILE);
        String keypass = store.getString(SSLPreferenceConstants.KEYSTORE_PASSWORD);
        String keytype = store.getString(SSLPreferenceConstants.KEYSTORE_TYPE);
        String trustpath = store.getString(SSLPreferenceConstants.TRUSTSTORE_FILE);
        String trustpass = store.getString(SSLPreferenceConstants.TRUSTSTORE_PASSWORD);
        String trusttype = store.getString(SSLPreferenceConstants.TRUSTSTORE_TYPE);
        CryptoHelper cryptoHelper = CryptoHelper.getDefault();
        keypass = cryptoHelper.decrypt(keypass);
        trustpass = cryptoHelper.decrypt(trustpass);
        buildContext(algorithm, keypath, keypass, keytype, trustpath, trustpass, trusttype);
    }
}
