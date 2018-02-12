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
package org.talend.core.utils;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
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

    public static synchronized SSLContext getContext() throws Exception {
        if (null == context) {
            buildContext();
        }
        return context;
    }

    public static synchronized void buildContext() throws Exception {
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
            if (StringUtils.isEmpty(keypath) && StringUtils.isEmpty(trustpath)) {
                context = null;
            } else {
                context = SSLContextProvider.buildContext("SSL", keypath, keypass, keytype, trustpath, trustpass, trusttype);
            }
        } catch (Exception e) {
            context = null;
            throw e;
        }
    }

    public static boolean setSSLSystemProperty(boolean isPreference) {
        try {
            buildContext();
            if (!isPreference && context == null) {
                return false;
            }
            changeProperty();
            Executor.unregisterScheme("https");
            SSLSocketFactory factory = new SSLSocketFactory(context, SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            Executor.registerScheme(new Scheme("https", 443, factory));
        } catch (Exception e) {
            if (isPreference) {
                changeProperty();
                Executor.unregisterScheme("https");
            }
            ExceptionHandler.process(new Exception("Please check the SSL settings in Preference>Talend>SSL", e));
            return false;
        }
        return true;
    }

    private static void changeProperty() {
        final IPreferenceStore sslStore = CoreRuntimePlugin.getInstance().getCoreService().getPreferenceStore();
        CryptoHelper cryptoHelper = CryptoHelper.getDefault();
        String keyStore = sslStore.getString(SSLPreferenceConstants.KEYSTORE_FILE);
        if (keyStore != null && !"".equals(keyStore.trim())) {
            System.setProperty(SSLPreferenceConstants.KEYSTORE_FILE, keyStore);
            System.setProperty(SSLPreferenceConstants.KEYSTORE_PASSWORD,
                    cryptoHelper.decrypt(sslStore.getString(SSLPreferenceConstants.KEYSTORE_PASSWORD)));
            System.setProperty(SSLPreferenceConstants.KEYSTORE_TYPE, sslStore.getString(SSLPreferenceConstants.KEYSTORE_TYPE));
        } else {
            System.clearProperty(SSLPreferenceConstants.KEYSTORE_FILE);
            System.clearProperty(SSLPreferenceConstants.KEYSTORE_PASSWORD);
            System.clearProperty(SSLPreferenceConstants.KEYSTORE_TYPE);
        }
        String trustStore = sslStore.getString(SSLPreferenceConstants.TRUSTSTORE_FILE);
        if (trustStore != null && !"".equals(trustStore.trim())) {
            System.setProperty(SSLPreferenceConstants.TRUSTSTORE_FILE, trustStore);
            System.setProperty(SSLPreferenceConstants.TRUSTSTORE_PASSWORD,
                    cryptoHelper.decrypt(sslStore.getString(SSLPreferenceConstants.TRUSTSTORE_PASSWORD)));
            System.setProperty(SSLPreferenceConstants.TRUSTSTORE_TYPE, sslStore.getString(SSLPreferenceConstants.TRUSTSTORE_TYPE));
        } else {
            System.clearProperty(SSLPreferenceConstants.TRUSTSTORE_FILE);
            System.clearProperty(SSLPreferenceConstants.TRUSTSTORE_PASSWORD);
            System.clearProperty(SSLPreferenceConstants.TRUSTSTORE_TYPE);
        }
    }

    public static boolean setSSLSystemProperty() {
        return setSSLSystemProperty(false);
    }
}
