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
package org.talend.designer.maven.aether.util;

import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.ProxySelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.network.TalendProxySelector;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TalendAetherProxySelector implements ProxySelector {

    private boolean isTalendDebug = false;

    public TalendAetherProxySelector() {
        isTalendDebug = Boolean.valueOf(System.getProperty(TalendProxySelector.PROP_PRINT_LOGS, "false"));
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
                        proxyStr = proxy.getType() + " " + proxy.toString() + ", proxy user: "
                                + (proxy.getAuthentication() != null ? "..." : "<empty>");
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
        String[] schemas = new String[] { "http", "https" };
        for (String schema : schemas) {
            Proxy proxy = createProxy(schema);
            if (proxy != null) {
                proxySelector.add(proxy, System.getProperty(schema + ".nonProxyHosts"));
            }
        }
    }

    private org.eclipse.aether.repository.Proxy createProxy(String schema) {
        String proxyHost = System.getProperty(schema + ".proxyHost");
        if (proxyHost == null) {
            return null;
        }
        String proxyUser = System.getProperty(schema + ".proxyUser");
        String proxyPassword = System.getProperty(schema + ".proxyPassword");
        int proxyPort = Integer.parseInt(System.getProperty(schema + ".proxyPort", "8080"));

        Authentication authentication = createAuthentication(proxyUser, proxyPassword);
        org.eclipse.aether.repository.Proxy proxyObj = new org.eclipse.aether.repository.Proxy(schema, proxyHost, proxyPort,
                authentication);
        return proxyObj;
    }

    private Authentication createAuthentication(String proxyUser, String proxyPassword) {
        Authentication authentication = null;
        if (proxyUser != null) {
            authentication = new AuthenticationBuilder().addUsername(proxyUser).addPassword(proxyPassword).build();
        }
        return authentication;
    }

}
