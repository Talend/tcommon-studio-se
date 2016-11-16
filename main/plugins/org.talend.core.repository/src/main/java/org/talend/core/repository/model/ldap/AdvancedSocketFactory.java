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
package org.talend.core.repository.model.ldap;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.Security;
import java.util.Hashtable;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.Logger;
import org.talend.core.repository.i18n.Messages;

import com.sun.net.ssl.KeyManagerFactory;
import com.sun.net.ssl.SSLContext;
import com.sun.net.ssl.TrustManager;
import com.sun.net.ssl.internal.ssl.Provider;

/**
 * This class is used for LDAP. <br/>
 * 
 * @author ftang, 19/09/2007
 * 
 */
public class AdvancedSocketFactory extends SSLSocketFactory {

    private SSLSocketFactory factory;

    private static TrustManager trustManagers[] = null;

    private static AdvancedSocketFactory defaultFactory = null;

    private static Hashtable factories = null;

    private static String certStorePath = null;

    private static Logger log = Logger.getLogger(AdvancedSocketFactory.class);

    /**
     * AdvancedSocketFactory constructor comment.
     */
    protected AdvancedSocketFactory() {
        factory = null;
        init(null, null);
    }

    /**
     * AdvancedSocketFactory constructor comment.
     * 
     * @param in
     * @param keyStore
     * @param password
     * @throws Exception
     */
    protected AdvancedSocketFactory(InputStream in, String keyStore, String password) throws Exception {
        factory = null;
        KeyStore ks = null;
        if (keyStore.endsWith(".p12")) //$NON-NLS-1$
            ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
        else
            ks = KeyStore.getInstance("JKS"); //$NON-NLS-1$
        char pwd[] = password.toCharArray();
        ks.load(in, pwd);
        init(ks, pwd);
    }

    /**
     * AdvancedSocketFactory constructor comment.
     * 
     * @param keyStore
     * @param passphrase
     */
    protected AdvancedSocketFactory(String keyStore, String passphrase) {
        factory = null;
        init(null, null);
    }

    /**
     * Comment method "closeStream".
     * 
     * @param in
     */
    private static void closeStream(InputStream in) {
        if (in == null)
            return;
        try {
            in.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return factory.createSocket(host, port);
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException,
            UnknownHostException {
        return factory.createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(InetAddress host, int port) throws IOException, UnknownHostException {
        return factory.createSocket(host, port);
    }

    public Socket createSocket(InetAddress host, int port, InetAddress clientHost, int clientPort) throws IOException,
            UnknownHostException {
        return factory.createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoclose) throws IOException, UnknownHostException {
        return factory.createSocket(socket, host, port, autoclose);
    }

    public static synchronized SocketFactory getDefault() {
        return getDefaultFactory();
    }

    public static void setCertStorePath(String path) {
        AdvancedSocketFactory.certStorePath = path;
    }

    public String[] getDefaultCipherSuites() {
        return factory.getDefaultCipherSuites();
    }

    private static SocketFactory getDefaultFactory() {
        if (defaultFactory == null)
            defaultFactory = new AdvancedSocketFactory();
        return defaultFactory;
    }

    private TrustManager[] getDefaultTrustManager() {
        if (trustManagers == null)
            trustManagers = (new LDAPCATruster[] { new LDAPCATruster(AdvancedSocketFactory.certStorePath) });
        return trustManagers;
    }

    public String[] getSupportedCipherSuites() {
        return factory.getSupportedCipherSuites();
    }

    private void init(KeyStore ks, char password[]) {
        SSLContext ctx = null;
        com.sun.net.ssl.KeyManager keyManagers[] = null;
        TrustManager trustManagers[] = null;
        try {
            if (ks != null) {
                KeyManagerFactory kmf = null;
                kmf = KeyManagerFactory.getInstance("SunX509"); //$NON-NLS-1$
                kmf.init(ks, password);
                keyManagers = kmf.getKeyManagers();
            }
            ctx = SSLContext.getInstance("TLS"); //$NON-NLS-1$
            trustManagers = getDefaultTrustManager();
            ctx.init(keyManagers, trustManagers, null);
            factory = ctx.getSocketFactory();
        } catch (Exception e) {
            log.error(Messages.getString("AdvancedSocketFactory.failedInitial") + e.getMessage()); //$NON-NLS-1$
        }
    }

    static {
        Security.addProvider(new Provider());
    }
}
