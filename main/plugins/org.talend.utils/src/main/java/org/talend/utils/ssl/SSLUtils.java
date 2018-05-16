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
package org.talend.utils.ssl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class SSLUtils {

    private static SSLContext sslcontext;

    private static final String TAC_SSL_KEYSTORE = "clientKeystore.jks"; //$NON-NLS-1$

    private static final String TAC_SSL_TRUSTSTORE = "clientTruststore.jks"; //$NON-NLS-1$

    private static final String TAC_SSL_CLIENT_KEY = "tac.net.ssl.ClientKeyStore"; //$NON-NLS-1$

    private static final String TAC_SSL_CLIENT_TRUST_KEY = "tac.net.ssl.ClientTrustStore"; //$NON-NLS-1$

    private static final String TAC_SSL_KEYSTORE_PASS = "tac.net.ssl.KeyStorePass"; //$NON-NLS-1$

    private static final String TAC_SSL_TRUSTSTORE_PASS = "tac.net.ssl.TrustStorePass"; //$NON-NLS-1$

    /**
     * 
     * DOC hcyi Comment method "getContent".
     * 
     * @param buffer
     * @param url
     * @return
     * @throws AMCPluginException
     */
    public static String getContent(StringBuffer buffer, URL url, String userDir) throws Exception {
        BufferedReader in = null;
        if (("https").equals(url.getProtocol())) {
            final SSLSocketFactory socketFactory = getSSLContext(userDir).getSocketFactory();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setSSLSocketFactory(socketFactory);
            httpsCon.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
            httpsCon.connect();
            in = new BufferedReader(new InputStreamReader(httpsCon.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        }
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            buffer.append(inputLine);
        }
        in.close();
        return buffer.toString();
    }

    public static SSLContext getSSLContext(String userDir) throws Exception {
        if (sslcontext == null) {
            String keystorePath = System.getProperty(TAC_SSL_CLIENT_KEY);
            String trustStorePath = System.getProperty(TAC_SSL_CLIENT_TRUST_KEY);
            String keystorePass = System.getProperty(TAC_SSL_KEYSTORE_PASS);
            String truststorePass = System.getProperty(TAC_SSL_TRUSTSTORE_PASS);
            if (keystorePath == null) {
                // if user does not set the keystore path in the .ini,we need to look for the keystore file under
                // the root dir of product
                File keystorePathFile = new File(userDir + TAC_SSL_KEYSTORE);
                if (keystorePathFile.exists()) {
                    keystorePath = keystorePathFile.getAbsolutePath();
                }
            }
            if (trustStorePath == null) {
                File trustStorePathFile = new File(userDir + TAC_SSL_TRUSTSTORE);
                if (trustStorePathFile.exists()) {
                    trustStorePath = trustStorePathFile.getAbsolutePath();
                }
            }
            if (keystorePass == null) {
                // if user does not set the password in the talend.ini,we only can make it empty by
                // default,but not sure the ssl can connect
                keystorePass = ""; //$NON-NLS-1$
            }
            if (truststorePass == null) {
                // if user does not set the password in the talend.ini,we only can make it empty by
                // default,but not sure the ssl can connect
                truststorePass = ""; //$NON-NLS-1$
            }

            sslcontext = SSLContext.getInstance("SSL"); //$NON-NLS-1$
            KeyManager[] keystoreManagers = null;
            if (keystorePath != null) {
                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                ks.load(new FileInputStream(keystorePath), keystorePass.toCharArray());
                kmf.init(ks, keystorePass.toCharArray());
                keystoreManagers = kmf.getKeyManagers();
            }

            TrustManager[] truststoreManagers = null;
            if (trustStorePath != null) {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
                KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
                tks.load(new FileInputStream(trustStorePath), truststorePass.toCharArray());
                tmf.init(tks);
                truststoreManagers = tmf.getTrustManagers();
            } else {
                truststoreManagers = new TrustManager[] { new TrustAnyTrustManager() };
            }
            sslcontext.init(keystoreManagers, truststoreManagers, null);
        }
        return sslcontext;
    }

    // accept all certificate
    private static class TrustAnyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
}
