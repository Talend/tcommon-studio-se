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
package org.talend.core.repository.model.ldap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.talend.core.repository.i18n.Messages;

/**
 * This class is used for verifying CA for LDAP connection.
 *
 * @author ftang, 10/09/2007
 *
 */
public class LDAPCATruster implements X509TrustManager {

    private String certStore;

    private char certStorePwd[];

    private X509TrustManager trustManager;

    private boolean isSaveCA = true;

    private KeyStore ks;

    String defaultName = "talendcecerts"; //$NON-NLS-1$

    private static Logger log = Logger.getLogger(LDAPCATruster.class);

    /**
     * LDAPCATruster constructor comment.
     *
     * @param certStorePath
     */
    public LDAPCATruster(String certStorePath) {
        certStore = null;
        certStorePwd = null;
        trustManager = null;
        ks = null;
        if (certStorePath == null) {
            isSaveCA = false;
            certStore = defaultName;
        } else {
            certStore = certStorePath;
        }
        certStorePwd = "changeit".toCharArray(); //$NON-NLS-1$
        init();
    }

    /**
     * Comment method "deleteCert".
     *
     * @param id
     * @return
     */
    private boolean deleteCert(String id) {
        try {
            ks.deleteEntry(id);
        } catch (KeyStoreException ex) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sun.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        if (trustManager == null)
            return null;
        else
            return trustManager.getAcceptedIssuers();
    }

    /**
     * Comment method "getCACert".
     *
     * @param chain
     * @return
     */
    private X509Certificate getCACert(X509Certificate chain[]) {
        X509Certificate ca = chain[chain.length - 1];
        if (ca.getSubjectDN().equals(ca.getIssuerDN()))
            return ca;
        else
            return null;
    }

    /**
     * Comment method "init".
     */
    private void init() {
        try {
            if (certStore.endsWith(".p12")) //$NON-NLS-1$
                ks = KeyStore.getInstance("PKCS12"); //$NON-NLS-1$
            else
                ks = KeyStore.getInstance("JKS"); //$NON-NLS-1$
        } catch (KeyStoreException e) {
            log.error(Messages.getString("LDAPCATruster.failedCreateCert") + e.getMessage()); //$NON-NLS-1$
            return;
        }
        InputStream in = null;
        if (certStore.indexOf("://") == -1) //$NON-NLS-1$
            try {
                in = new FileInputStream(certStore);
            } catch (FileNotFoundException ex) {
                // log.error(ex.getMessage());
            }
        else
            try {
                URL url = new URL(certStore);
                URLConnection con = url.openConnection();
                in = con.getInputStream();
            } catch (MalformedURLException e) {
                log.error(Messages.getString("LDAPCATruster.locationInvalid") + e.getMessage()); //$NON-NLS-1$
            } catch (IOException ex) {
            }
        try {
            ks.load(in, certStorePwd);
        } catch (Exception e) {
            log.error(Messages.getString("LDAPCATruster.failedLoadCert") + e.getMessage()); //$NON-NLS-1$
            return;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
        }
        try {
            trustManager = initTrustManager(ks);
        } catch (Exception e) {
            log.error(Messages.getString("LDAPCATruster.failedInitialTrust") + e.getMessage()); //$NON-NLS-1$
            return;
        }
    }

    /**
     * Comment method "initTrustManager".
     *
     * @param ks
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    private X509TrustManager initTrustManager(KeyStore ks) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory trustManagerFactory = null;
        trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); //$NON-NLS-1$
        trustManagerFactory.init(ks);
        TrustManager trusts[] = trustManagerFactory.getTrustManagers();
        return (X509TrustManager) trusts[0];
    }

    private boolean isAccepted(X509Certificate caCert) {
        X509Certificate certs[] = getAcceptedIssuers();
        if (certs == null)
            return false;
        for (int i = 0; i < certs.length; i++)
            if (caCert.equals(certs[i]))
                return true;

        return false;
    }

    @Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    	if (trustManager == null)
            throw new CertificateException("Trust manager is not initialized");
        else
            trustManager.checkClientTrusted(chain, authType);
	}

    @Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		if (trustManager != null) {
			try {
				trustManager.checkServerTrusted(chain, authType);
				return;
			} catch(Exception e) {
			}
		}
        X509Certificate ca = getCACert(chain);
        if (ca != null) {
            if (!isAccepted(ca)) {
                System.err.println("SSL Error:Server certificate chain verification failed.");
                throw new CertificateException("Server certificate chain verification failed.");
            }
            String id = String.valueOf(System.currentTimeMillis());
            X509TrustManager tmpTrustManager = null;
            try {
                ks.setCertificateEntry(id, ca);
                tmpTrustManager = initTrustManager(ks);
            } catch (Exception e) {
                System.err.println("ASF Truster: Failed to create tmp trust store : " + e.getMessage());
                throw new CertificateException(e);
            }
            try{
            	tmpTrustManager.checkServerTrusted(chain, authType);
                if (this.isSaveCA) {
                    saveStore();
                    trustManager = tmpTrustManager;
                }
                return;
            } catch(CertificateException e) {
                System.err.println("SSL Error:Server certificate chain verification failed and \\nthe CA is missing.");
                throw e;
            }
        } else {
            System.err
                    .println("SSL Error:CA certificate is not in the server certificate chain.\nPlease use the keytool command to import the server certificate.");
            throw new CertificateException("CA certificate is not in the server certificate chain.\\nPlease use the keytool command to import the server certificate.");
        }
	}

    /**
     * Comment method "saveStore".
     *
     * @return
     */
    private boolean saveStore() {
        OutputStream out = null;
        try {
            try {
                if (certStore.indexOf("://") == -1) { //$NON-NLS-1$
                    out = new FileOutputStream(certStore);
                } else {
                    URL url = new URL(certStore);
                    URLConnection con = url.openConnection();
                    con.setDoOutput(true);
                    out = con.getOutputStream();
                }
                ks.store(out, certStorePwd);
                return true;
            } catch (Exception e) {
                log.error(Messages.getString("LDAPCATruster.failedSaveTrust") + e.getMessage()); //$NON-NLS-1$
            }
            return false;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
        }
    }

}
