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

import org.apache.http.client.fluent.Executor;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
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

    public static synchronized SSLContext getContext() {
        if (null == context) {
            buildContext();
        }
        return context;
    }

    public static synchronized void buildContext() {
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
            context = SSLContextProvider.buildContext("SSL", keypath, keypass, keytype, trustpath, trustpass, trusttype);
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public static void unregisterHttpsScheme() {
        try {
            Executor.unregisterScheme("https");
            SSLContext sslcontext = StudioSSLContextProvider.getContext();
            SSLSocketFactory factory = new SSLSocketFactory(sslcontext, SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            Executor.registerScheme(new Scheme("https", 443, factory));
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }
}
