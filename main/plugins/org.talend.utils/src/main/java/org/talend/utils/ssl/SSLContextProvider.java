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
package org.talend.utils.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * created by wchen on Oct 24, 2016 Detailled comment
 *
 */
public class SSLContextProvider {

    private static final TrustManager TRUST_ALL = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

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

    public synchronized static SSLContext buildContext(String algorithm, String keypath, String keypass, String keytype,
            String trustpath, String trustpass, String trusttype) {
        try {
            KeyManager[] kms = buildKeyManagers(keypath, keypass, keytype);
            TrustManager[] tms = buildTrustManagers(trustpath, trustpass, trusttype);
            SSLContext sslcontext = SSLContext.getInstance(algorithm);
            sslcontext.init(kms, tms, null);
            return sslcontext;
        } catch (Exception e) {
            throw new SecurityException(e.getMessage(), e);
        }
    }

}
