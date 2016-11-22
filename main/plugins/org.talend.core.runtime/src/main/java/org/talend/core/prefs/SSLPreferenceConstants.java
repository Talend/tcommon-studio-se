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
package org.talend.core.prefs;

/**
 * 
 * created by wchen on Sep 30, 2016 Detailled comment
 *
 */
public interface SSLPreferenceConstants {

    // http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore

    String[] KEYSTORE_TYPES = new String[] { "JKS", "JCEKS", "PKCS12" };//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

    String[] SSL_ALGORITHMS = new String[] { "TLS", "SSL" };//$NON-NLS-1$//$NON-NLS-2$

    // File of the keystore
    String KEYSTORE_FILE = "talend.ssl.keystoreFile"; //$NON-NLS-1$

    // Password to unlock the keystore.
    String KEYSTORE_PASSWORD = "talend.ssl.keystorePassword"; //$NON-NLS-1$

    // Type of file used for the keystore.
    String KEYSTORE_TYPE = "talend.ssl.keystoreType"; //$NON-NLS-1$

    // File of the truststore
    String TRUSTSTORE_FILE = "talend.ssl.truststoreFile"; //$NON-NLS-1$

    // Password to unlock the truststore.
    String TRUSTSTORE_PASSWORD = "talend.ssl.truststorePassword"; //$NON-NLS-1$

    // Type of file used for the truststore.
    String TRUSTSTORE_TYPE = "talend.ssl.truststoreType"; //$NON-NLS-1$

    // verifyHostname:
    String VERIFY_HOSTNAME = "talend.ssl.verifyHostname"; //$NON-NLS-1$

    // aligorithm of create sslContext
    String SSL_ALGORITHM = "talend.ssl.sslAlgorithm"; //$NON-NLS-1$
}
