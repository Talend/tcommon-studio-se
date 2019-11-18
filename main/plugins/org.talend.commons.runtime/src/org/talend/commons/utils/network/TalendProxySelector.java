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
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Priority;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.i18n.internal.Messages;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TalendProxySelector extends ProxySelector {

    private static final String ECLIPSE_PROXY_SELECTOR = ".EclipseProxySelector"; //$NON-NLS-1$

    public static final String PROP_PRINT_LOGS = "talend.studio.proxy.printLogs";

    private ProxySelector defaultSelector;

    final private List<IProxySelectorProvider> selectorProviders;

    private volatile static TalendProxySelector instance;

    private static Object instanceLock = new Object();

    private boolean printProxyLog = false;

    private TalendProxySelector(final ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;
        selectorProviders = new ArrayList<>();
        selectorProviders.add(createDefaultProxySelectorProvider());
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
        final Set<Proxy> resultFromProviders = new HashSet<>();
        List<IProxySelectorProvider> providers = getProxySelectorProviders();
        if (providers != null) {
            providers.stream().forEach(p -> {
                if (instance == p) {
                    return;
                }
                if (p.canHandle(uri)) {
                    try {
                        List<Proxy> proxys = p.select(uri);
                        if (proxys != null && !proxys.isEmpty()) {
                            resultFromProviders.addAll(proxys);
                        }
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }
            });
        }
        List<Proxy> result = new ArrayList<>();

        if (resultFromProviders != null && !resultFromProviders.isEmpty()) {
            result.addAll(resultFromProviders);
        }

        ProxySelector defaultProxySelector = getDefaultProxySelector();
        if (defaultProxySelector != null) {
            List<Proxy> defaultProxys = defaultProxySelector.select(uri);
            if (defaultProxys != null && !defaultProxys.isEmpty()) {
                result.addAll(defaultProxys);
            }
        }
        if (printProxyLog) {
            String proxys = result.toString();
            ExceptionHandler.log("Selected proxys for " + uri + ", " + proxys);
            ExceptionHandler.process(new Exception("Proxy call stacks"), Priority.INFO);
        }
        return result;
    }

    public boolean addProxySelectorProvider(IProxySelectorProvider provider) {
        List<IProxySelectorProvider> proxySelectorProviders = getProxySelectorProviders();
        if (!proxySelectorProviders.contains(provider)) {
            return proxySelectorProviders.add(provider);
        }
        return false;
    }

    public boolean removeProxySelectorProvider(IProxySelectorProvider provider) {
        return getProxySelectorProviders().remove(provider);
    }

    private List<IProxySelectorProvider> getProxySelectorProviders() {
        return selectorProviders;
    }

    public ProxySelector getDefaultProxySelector() {
        return defaultSelector;
    }

    public void setDefaultProxySelector(final ProxySelector selector) {
        defaultSelector = selector;
    }

    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
        List<IProxySelectorProvider> providers = getProxySelectorProviders();
        if (providers != null) {
            providers.stream().forEach(p -> {
                if (p.canHandle(uri)) {
                    p.connectFailed(uri, sa, ioe);
                }
            });
        }

        ProxySelector defaultProxySelector = getDefaultProxySelector();
        if (defaultProxySelector != null) {
            defaultProxySelector.connectFailed(uri, sa, ioe);
        }
    }

    public static abstract class AbstractProxySelectorProvider implements IProxySelectorProvider {

        private boolean isDebugMode = CommonsPlugin.isDebugMode();

        @Override
        public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
            if (isDebugMode) {
                ExceptionHandler.process(ioe);
            }
        }

    }

    public static interface IProxySelectorProvider {

        boolean canHandle(final URI uri);

        List<Proxy> select(final URI uri);

        void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe);

    }

    private IProxySelectorProvider createDefaultProxySelectorProvider() {
        IProxySelectorProvider proxySelectorProvider = new TalendProxySelector.AbstractProxySelectorProvider() {

            private static final String PROP_PROXY_MAP = "talend.studio.proxy.protocol.map";

            private static final String PROP_PROXY_MAP_DEFAULT = "socket:https,";

            private Map<String, String> protocolMap;

            {

                try {
                    protocolMap = new HashMap<>();
                    String property = System.getProperty(PROP_PROXY_MAP, PROP_PROXY_MAP_DEFAULT);
                    String[] splits = property.split(",");
                    for (String split : splits) {
                        String[] entry = split.split(":");
                        if (entry.length != 2) {
                            ExceptionHandler.process(
                                    new Exception(Messages.getString("TalendProxySelector.exception.badProtocolMap", split)));
                            continue;
                        }
                        protocolMap.put(entry[0], entry[1]);
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }

            }

            @Override
            public List<Proxy> select(URI uri) {
                // return Collections.EMPTY_LIST;

                List<Proxy> newProxys = new ArrayList<>();
                if (uri == null) {
                    return newProxys;
                }

                String schema = uri.getScheme();
                if (schema != null) {
                    String lowercasedProtocol = schema.toLowerCase();
                    String preferedProtocol = protocolMap.get(lowercasedProtocol);
                    if (StringUtils.isNotBlank(preferedProtocol)) {
                        try {
                            URI newUri = new URI(preferedProtocol, uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(),
                                    uri.getQuery(), uri.getFragment());
                            List<Proxy> proxys = getDefaultProxySelector().select(newUri);
                            if (proxys != null && !proxys.isEmpty()) {
                                newProxys.addAll(proxys);
                            }
                        } catch (URISyntaxException e) {
                            if (printProxyLog) {
                                ExceptionHandler.process(
                                        new Exception(
                                                Messages.getString("TalendProxySelector.exception.proxySelectionError", uri), e),
                                        Priority.WARN);
                            }
                        }
                    }
                }
                return newProxys;

            }

            @Override
            public boolean canHandle(URI uri) {
                return true;
            }

        };
        return proxySelectorProvider;
    }

}
