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

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.prefs.SSLPreferenceConstants;
import org.talend.core.runtime.CoreRuntimePlugin;
import org.talend.daikon.security.SSLContextProvider;
import org.talend.utils.security.CryptoHelper;

/**
 * 
 * created by wchen on Oct 24, 2016 Detailled comment
 *
 */
public class StudioSSLContextProvider {

    private static SSLContext context;

    private static final IPreferenceStore store = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore();

    public static X509HostnameVerifier getHostnameVerifier() {
        boolean verify = store.getBoolean(SSLPreferenceConstants.VERIFY_HOSTNAME);
        if (verify) {
            return SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
        } else {
            return SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        }
    }

    public static synchronized SSLContext getContext() {
        if (null == context) {
            buildContext();
        }
        return context;
    }

    public static synchronized void buildContext() {
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
        try {
            context = SSLContextProvider.buildContext(algorithm, keypath, keypass, keytype, trustpath, trustpass, trusttype);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}
