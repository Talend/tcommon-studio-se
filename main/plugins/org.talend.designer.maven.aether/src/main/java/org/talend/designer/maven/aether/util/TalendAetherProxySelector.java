// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.designer.maven.aether.util;

import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TalendAetherProxySelector implements ProxySelector {

    private boolean isTalendDebug = false;

    public TalendAetherProxySelector() {
        isTalendDebug = CommonsPlugin.isDebugMode();
    }

    @Override
    public Proxy getProxy(RemoteRepository repository) {
        /**
         * Update each time in case the settings are changed
         */
        Proxy proxy = createProxySelector().getProxy(repository);
        if (isTalendDebug) {
            try {
                if (repository != null) {
                    String proxyStr = "";
                    if (proxy != null) {
                        proxyStr = proxy.toString() + ", proxy user: " + (proxy.getAuthentication() != null ? "..." : "<empty>");
                    }
                    ExceptionHandler.log("Aether proxy> host: " + repository.getHost() + ", proxy: " + proxyStr);
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
        return proxy;
    }

    private DefaultProxySelector createProxySelector() {
        DefaultProxySelector proxySelector = new DefaultProxySelector();
        javaDefaultProxy(proxySelector);
        return proxySelector;
    }

    private void javaDefaultProxy(DefaultProxySelector proxySelector) {
        String proxyHost = System.getProperty("https.proxyHost");
        String schema = proxyHost != null ? "https" : "http";
        if (proxyHost == null) {
            proxyHost = System.getProperty(schema + "." + "proxyHost");
        }
        if (proxyHost == null) {
            return;
        }
        String proxyUser = System.getProperty(schema + "." + "proxyUser");
        String proxyPassword = System.getProperty(schema + "." + "proxyPassword");
        int proxyPort = Integer.parseInt(System.getProperty(schema + "." + "proxyPort", "8080"));
        String nonProxyHosts = System.getProperty(schema + "." + "nonProxyHosts");

        Authentication authentication = createAuthentication(proxyUser, proxyPassword);
        org.eclipse.aether.repository.Proxy proxyObj = new org.eclipse.aether.repository.Proxy(schema, proxyHost, proxyPort,
                authentication);
        proxySelector.add(proxyObj, nonProxyHosts);
    }

    private Authentication createAuthentication(String proxyUser, String proxyPassword) {
        Authentication authentication = null;
        if (proxyUser != null) {
            authentication = new AuthenticationBuilder().addUsername(proxyUser).addPassword(proxyPassword).build();
        }
        return authentication;
    }

}
