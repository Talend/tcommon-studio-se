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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * DOC hcyi class global comment. Detailled comment
 */
public class SSLUtils {
    /**
     * {@value}
     * <p>
     * The default client keystore file name.
     */
    public static final String TAC_SSL_KEYSTORE = "clientKeystore.jks"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * The default truststore file name.
     */
    public static final String TAC_SSL_TRUSTSTORE = "clientTruststore.jks"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property key of client keystore file path.
     */
    public static final String TAC_SSL_CLIENT_KEY = "tac.net.ssl.ClientKeyStore"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property key of client truststore file path.
     */
    public static final String TAC_SSL_CLIENT_TRUST_KEY = "tac.net.ssl.ClientTrustStore"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property of client keystore password.
     */
    public static final String TAC_SSL_KEYSTORE_PASS = "tac.net.ssl.KeyStorePass"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * System property of client truststore password.
     */
    public static final String TAC_SSL_TRUSTSTORE_PASS = "tac.net.ssl.TrustStorePass"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * Enable host name verification, the default value is <b>true</b>.
     */
    public static final String TAC_SSL_ENABLE_HOST_NAME_VERIFICATION = "tac.net.ssl.EnableHostNameVerification"; //$NON-NLS-1$

    /**
     * {@value}
     * <p>
     * Accept all certificates if don't setup tac.net.ssl.ClientTrustStore, the default value is <b>false</b>.
     */
    public static final String TAC_SSL_ACCEPT_ALL_CERTS_IF_NO_TRUSTSTORE = "tac.net.ssl.AcceptAllCertsIfNoTruststore"; //$NON-NLS-1$

    private static SSLUtils instance;

    private static HostnameVerifier hostnameVerifier;

    private static KeyManager[] keystoreManagers;

    private static TrustManager[] truststoreManagers;

    /**
     * Get SSLUtils instance
     * @param userDir- The default keystore file folder, Once SSLUtils initialized, we should not use different value
     * @return SSLUtils instance
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     * @throws CertificateException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static synchronized SSLUtils getInstance(String userDir) throws NoSuchAlgorithmException, KeyStoreException,
            UnrecoverableKeyException, CertificateException, FileNotFoundException, IOException {
        if (instance == null) {
            instance = new SSLUtils(userDir);
        } 
        return instance;
    }

    private SSLUtils(String userDir) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException,
            CertificateException, FileNotFoundException, IOException {
        Init(userDir);
    }

    private void Init(String userDir) throws NoSuchAlgorithmException, KeyStoreException, CertificateException,
            FileNotFoundException, IOException, UnrecoverableKeyException {
        String keystorePath = System.getProperty(TAC_SSL_CLIENT_KEY);
        String trustStorePath = System.getProperty(TAC_SSL_CLIENT_TRUST_KEY);
        String keystorePass = System.getProperty(TAC_SSL_KEYSTORE_PASS);
        String truststorePass = System.getProperty(TAC_SSL_TRUSTSTORE_PASS);
        boolean acceptAllCertsIfNoTrustStore = Boolean.parseBoolean(System.getProperty(TAC_SSL_ACCEPT_ALL_CERTS_IF_NO_TRUSTSTORE));

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

        if (keystorePath != null) {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream(keystorePath), keystorePass == null ? null : keystorePass.toCharArray());
            kmf.init(ks, keystorePass == null ? null : keystorePass.toCharArray());
            keystoreManagers = kmf.getKeyManagers();
        }

        if (trustStorePath != null) {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
            KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
            tks.load(new FileInputStream(trustStorePath), truststorePass.toCharArray());
            tmf.init(tks);
            truststoreManagers = tmf.getTrustManagers();
        }

        if (truststoreManagers == null) {
            if (acceptAllCertsIfNoTrustStore) {
                truststoreManagers = new TrustManager[] { new TrustAnyTrustManager() };
            } else {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
                tmf.init((KeyStore) null);
                truststoreManagers = tmf.getTrustManagers();
            }
        }

        boolean openHttpHostNameVerification = Boolean
                .parseBoolean(System.getProperty(TAC_SSL_ENABLE_HOST_NAME_VERIFICATION, Boolean.TRUE.toString()));
        if (openHttpHostNameVerification) {
            hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
        } else {
            hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
    }

    public SSLContext getSSLContext() throws Exception {
        SSLContext sslcontext = SSLContext.getInstance("SSL"); //$NON-NLS-1$
        sslcontext.init(keystoreManagers, truststoreManagers, null);
        return sslcontext;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }
    
    /**
     * 
     * DOC hcyi Comment method "getContent".
     * 
     * @param buffer
     * @param url
     * 
     * @return
     * @throws AMCPluginException
     */
    public String getContent(StringBuffer buffer, URL url) throws Exception {
        BufferedReader in = null;
        if (("https").equals(url.getProtocol())) {
            boolean openHttpHostNameVerification = Boolean.parseBoolean(System.getProperty(TAC_SSL_ENABLE_HOST_NAME_VERIFICATION));
            final SSLSocketFactory socketFactory = getSSLContext().getSocketFactory();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setSSLSocketFactory(socketFactory);
            if (openHttpHostNameVerification) {
                httpsCon.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            } else {
                httpsCon.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            }
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
