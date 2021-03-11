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
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * DOC hcyi class global comment. Detailled comment
 * This class have 3 duplicate in studio, If you fix some bugs in this class, please synchronize others.
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

    private HostnameVerifier hostnameVerifier;

    private KeyManager[] keystoreManagers;

    private TrustManager[] truststoreManagers;

    private static Map<String, SSLUtils> userDirToInstanceMap = new HashMap<String, SSLUtils>();

    /**
     * Get SSLUtils instance
     *
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
        if (userDir == null) {
            userDir = "";
        }
        if (userDirToInstanceMap.containsKey(userDir)) {
            return userDirToInstanceMap.get(userDir);
        }
        SSLUtils newInstance = new SSLUtils(userDir);
        userDirToInstanceMap.put(userDir, newInstance);
        return newInstance;
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
        boolean acceptAllCertsIfNoTrustStore = Boolean
                .parseBoolean(System.getProperty(TAC_SSL_ACCEPT_ALL_CERTS_IF_NO_TRUSTSTORE));

        if (keystorePath == null) {
            // if user does not set the keystore path in the .ini,we need to look for the
            // keystore file under
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
            // if user does not set the password in the talend.ini,we only can make it empty
            // by
            // default,but not sure the ssl can connect
            keystorePass = ""; //$NON-NLS-1$
        }
        if (truststorePass == null) {
            // if user does not set the password in the talend.ini,we only can make it empty
            // by
            // default,but not sure the ssl can connect
            truststorePass = ""; //$NON-NLS-1$
        }

        if (keystorePath != null) {
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); //$NON-NLS-1$
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new FileInputStream(keystorePath), keystorePass == null ? null : keystorePass.toCharArray());
            kmf.init(ks, keystorePass == null ? null : keystorePass.toCharArray());
            keystoreManagers = kmf.getKeyManagers();
        }

        if (trustStorePath != null) {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); //$NON-NLS-1$
            KeyStore tks = KeyStore.getInstance(KeyStore.getDefaultType());
            tks.load(new FileInputStream(trustStorePath), truststorePass.toCharArray());
            tmf.init(tks);
            truststoreManagers = tmf.getTrustManagers();
        }

        if (truststoreManagers == null) {
            if (acceptAllCertsIfNoTrustStore) {
                truststoreManagers = new TrustManager[] { new TrustAnyTrustManager() };
            } else {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); //$NON-NLS-1$
                tmf.init((KeyStore) null);
                truststoreManagers = tmf.getTrustManagers();
            }
        }

        boolean enableHostnameVerification = Boolean
                .parseBoolean(System.getProperty(TAC_SSL_ENABLE_HOST_NAME_VERIFICATION, Boolean.TRUE.toString()));
        if (enableHostnameVerification) {
            hostnameVerifier = new BrowserCompatibleHostnameVerifier();
        } else {
            hostnameVerifier = new AllowAllHostnameVerifier();
        }
    }

    public SSLContext getSSLContext() throws Exception {
        SSLContext sslcontext = SSLContext.getInstance("TLS"); //$NON-NLS-1$
        sslcontext.init(keystoreManagers, truststoreManagers, null);
        return sslcontext;
    }

    public static SSLContext getSSLContext(String userDir) throws Exception {
        SSLUtils instance = SSLUtils.getInstance(userDir);
        return instance.getSSLContext();
    }

    public static String getContent(StringBuffer buffer, URL url, String userDir) throws Exception {
        SSLUtils instance = SSLUtils.getInstance(userDir);
        return instance.getContent(buffer, url);
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
            final SSLSocketFactory socketFactory = getSSLContext().getSocketFactory();
            HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
            httpsCon.setSSLSocketFactory(socketFactory);
            httpsCon.setHostnameVerifier(hostnameVerifier);
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

    // accept all certificates
    private class TrustAnyTrustManager implements X509TrustManager {

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

    private class AllowAllHostnameVerifier implements HostnameVerifier {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class BrowserCompatibleHostnameVerifier implements HostnameVerifier {

        private static final String[] BAD_COUNTRY_2LDS = { "ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg",
                "ne", "net", "or", "org" };

        private static final Pattern IPV4_PATTERN = Pattern.compile(
                "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

        private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

        private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern
                .compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

        private static final char COLON_CHAR = ':';

        // Must not have more than 7 colons (i.e. 8 fields)
        private static final int MAX_COLON_COUNT = 7;

        public boolean verify(String host, SSLSession session) {
            try {
                Certificate[] certs = session.getPeerCertificates();
                X509Certificate x509 = (X509Certificate) certs[0];
                verify(host, x509);
                return true;
            } catch (SSLException e) {
                return false;
            }
        }

        private void verify(String host, X509Certificate cert) throws SSLException {
            String[] cns = getCNs(cert);
            String[] subjectAlts = getSubjectAlts(cert, host);
            verify(host, cns, subjectAlts);
        }

        private void verify(final String host, final String[] cns, final String[] subjectAlts) throws SSLException {
            verify(host, cns, subjectAlts, false);
        }

        private void verify(final String host, final String[] cns, final String[] subjectAlts, final boolean strictWithSubDomains)
                throws SSLException {
            LinkedList<String> names = new LinkedList<String>();
            if (cns != null && cns.length > 0 && cns[0] != null) {
                names.add(cns[0]);
            }
            if (subjectAlts != null) {
                for (String subjectAlt : subjectAlts) {
                    if (subjectAlt != null) {
                        names.add(subjectAlt);
                    }
                }
            }

            if (names.isEmpty()) {
                String msg = "Certificate for <" + host + "> doesn't contain CN or DNS subjectAlt";
                throw new SSLException(msg);
            }

            StringBuilder buf = new StringBuilder();
            String hostName = host.trim().toLowerCase(Locale.US);
            boolean match = false;
            for (Iterator<String> it = names.iterator(); it.hasNext();) {

                String cn = it.next();
                cn = cn.toLowerCase(Locale.US);
                buf.append(" <");
                buf.append(cn);
                buf.append('>');
                if (it.hasNext()) {
                    buf.append(" OR");
                }

                String parts[] = cn.split("\\.");
                boolean doWildcard = parts.length >= 3 && parts[0].endsWith("*") && acceptableCountryWildcard(cn)
                        && !isIPAddress(host);

                if (doWildcard) {
                    String firstpart = parts[0];
                    if (firstpart.length() > 1) { // e.g. server*
                        String prefix = firstpart.substring(0, firstpart.length() - 1); // e.g. server
                        String suffix = cn.substring(firstpart.length()); // skip wildcard part from cn
                        String hostSuffix = hostName.substring(prefix.length()); // skip wildcard part from host
                        match = hostName.startsWith(prefix) && hostSuffix.endsWith(suffix);
                    } else {
                        match = hostName.endsWith(cn.substring(1));
                    }
                    if (match && strictWithSubDomains) {
                        match = countDots(hostName) == countDots(cn);
                    }
                } else {
                    match = hostName.equals(cn);
                }
                if (match) {
                    break;
                }
            }
            if (!match) {
                throw new SSLException("hostname in certificate didn't match: <" + host + "> !=" + buf);
            }
        }

        private String[] getCNs(X509Certificate cert) {
            LinkedList<String> cnList = new LinkedList<String>();
            String subjectPrincipal = cert.getSubjectX500Principal().toString();
            StringTokenizer st = new StringTokenizer(subjectPrincipal, ",+");
            while (st.hasMoreTokens()) {
                String tok = st.nextToken().trim();
                if (tok.length() > 3) {
                    if (tok.substring(0, 3).equalsIgnoreCase("CN=")) {
                        cnList.add(tok.substring(3));
                    }
                }
            }
            if (!cnList.isEmpty()) {
                String[] cns = new String[cnList.size()];
                cnList.toArray(cns);
                return cns;
            } else {
                return null;
            }
        }

        private boolean acceptableCountryWildcard(String cn) {
            String parts[] = cn.split("\\.");
            if (parts.length != 3 || parts[2].length() != 2) {
                return true;
            }
            return Arrays.binarySearch(BAD_COUNTRY_2LDS, parts[1]) < 0;
        }

        private boolean isIPAddress(final String hostname) {
            return hostname != null && (isIPv4Address(hostname) || isIPv6Address(hostname));
        }

        private boolean isIPv4Address(final String input) {
            return IPV4_PATTERN.matcher(input).matches();
        }

        private boolean isIPv6Address(final String input) {
            return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
        }

        private boolean isIPv6StdAddress(final String input) {
            return IPV6_STD_PATTERN.matcher(input).matches();
        }

        private boolean isIPv6HexCompressedAddress(final String input) {
            int colonCount = 0;
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == COLON_CHAR) {
                    colonCount++;
                }
            }
            return colonCount <= MAX_COLON_COUNT && IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
        }

        private int countDots(final String s) {
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '.') {
                    count++;
                }
            }
            return count;
        }

        private String[] getSubjectAlts(final X509Certificate cert, final String hostname) {
            int subjectType;
            if (isIPAddress(hostname)) {
                subjectType = 7;
            } else {
                subjectType = 2;
            }

            LinkedList<String> subjectAltList = new LinkedList<String>();
            Collection<List<?>> c = null;
            try {
                c = cert.getSubjectAlternativeNames();
            } catch (CertificateParsingException cpe) {
            }
            if (c != null) {
                for (List<?> aC : c) {
                    List<?> list = aC;
                    int type = ((Integer) list.get(0)).intValue();
                    if (type == subjectType) {
                        String s = (String) list.get(1);
                        subjectAltList.add(s);
                    }
                }
            }
            if (!subjectAltList.isEmpty()) {
                String[] subjectAlts = new String[subjectAltList.size()];
                subjectAltList.toArray(subjectAlts);
                return subjectAlts;
            } else {
                return null;
            }
        }
    }
}
