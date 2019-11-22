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
package org.talend.commons.utils.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Priority;
import org.talend.commons.exception.ExceptionHandler;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TalendProxySelector extends ProxySelector {

    private static final String ECLIPSE_PROXY_SELECTOR = ".EclipseProxySelector"; //$NON-NLS-1$

    public static final String PROP_PRINT_LOGS = "talend.studio.proxy.printLogs";

    private ProxySelector defaultSelector;

    private volatile static TalendProxySelector instance;

    private static Object instanceLock = new Object();

    private boolean printProxyLog = false;

    private TalendProxySelector(final ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
        printProxyLog = Boolean.valueOf(System.getProperty(PROP_PRINT_LOGS, "false"));
    }

    public static TalendProxySelector getInstance() {
        final ProxySelector proxySelector = AccessController.doPrivileged(new PrivilegedAction<ProxySelector>() {

            @Override
            public ProxySelector run() {
                return ProxySelector.getDefault();
            }
        });
        if (instance == null) {
            synchronized (instanceLock) {
                if (instance == null) {
                    instance = new TalendProxySelector(proxySelector);
                }
            }
        }
        if (proxySelector != instance) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {

                @Override
                public Void run() {
                    ProxySelector.setDefault(instance);
                    return null;
                }
            });
            if (instance.getDefaultProxySelector() == null
                    || (proxySelector != null && proxySelector.getClass().getName().endsWith(ECLIPSE_PROXY_SELECTOR))) {
                instance.setDefaultProxySelector(proxySelector);
            }
        }

        return instance;
    }

    @Override
    public List<Proxy> select(final URI uri) {
        List<Proxy> result = new ArrayList<>();
        try {
            ProxySelector defaultProxySelector = getDefaultProxySelector();
            if (defaultProxySelector != null) {
                List<Proxy> defaultProxys = defaultProxySelector.select(uri);
                if (defaultProxys != null && !defaultProxys.isEmpty()) {
                    Proxy proxy = defaultProxys.get(0);
                    SocketAddress addr = null;
                    Proxy.Type proxyType = null;
                    if (proxy != null) {
                        proxyType = proxy.type();
                        addr = proxy.address();
                    }
                    if (Proxy.Type.DIRECT == proxyType
                            || (addr != null && StringUtils.equals(uri.getHost(), ((InetSocketAddress) addr).getHostString()))) {
                        result.add(Proxy.NO_PROXY);
                    } else {
                        result.addAll(defaultProxys);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (printProxyLog) {
            String proxys = result.toString();
            ExceptionHandler.log("Selected proxys for " + uri + ", " + proxys);
            ExceptionHandler.process(new Exception("Proxy call stacks"), Priority.INFO);
        }
        return result;
    }

    public ProxySelector getDefaultProxySelector() {
        return defaultSelector;
    }

    public void setDefaultProxySelector(final ProxySelector selector) {
        defaultSelector = selector;
    }

    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
        ProxySelector defaultProxySelector = getDefaultProxySelector();
        if (defaultProxySelector != null) {
            defaultProxySelector.connectFailed(uri, sa, ioe);
        }
    }

    public PasswordAuthentication getHttpPasswordAuthentication() {
        String[] schemas = new String[] { "http", "https" };
        for (String schema : schemas) {
            String proxyUser = System.getProperty(schema + ".proxyUser");
            String proxyPassword = System.getProperty(schema + ".proxyPassword");

            if (StringUtils.isNotBlank(proxyUser)) {
                char[] pwdChars = new char[0];
                if (proxyPassword != null && !proxyPassword.isEmpty()) {
                    pwdChars = proxyPassword.toCharArray();
                }
                return new PasswordAuthentication(proxyUser, pwdChars);
            }
        }
        return null;
    }

}
