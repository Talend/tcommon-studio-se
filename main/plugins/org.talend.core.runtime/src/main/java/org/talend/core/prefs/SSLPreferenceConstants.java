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
package org.talend.core.prefs;

/**
 * 
 * created by wchen on Sep 30, 2016 Detailled comment
 *
 */
public interface SSLPreferenceConstants {

    // http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyStore

    String[] KEYSTORE_TYPES = new String[] { "JKS", "JCEKS", "PKCS12" };

    // File of the keystore
    String KEYSTORE_FILE = "javax.net.ssl.keyStore"; //$NON-NLS-1$

    // Password to unlock the keystore.
    String KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword"; //$NON-NLS-1$

    // Type of file used for the keystore.
    String KEYSTORE_TYPE = "javax.net.ssl.keyStoreType"; //$NON-NLS-1$

    // File of the truststore
    String TRUSTSTORE_FILE = "javax.net.ssl.trustStore"; //$NON-NLS-1$

    // Password to unlock the truststore.
    String TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword"; //$NON-NLS-1$

    // Type of file used for the truststore.
    String TRUSTSTORE_TYPE = "javax.net.ssl.trustStoreType"; //$NON-NLS-1$

    static final String FILE_CONFIG_INI = "config.ini";//$NON-NLS-1$

}
