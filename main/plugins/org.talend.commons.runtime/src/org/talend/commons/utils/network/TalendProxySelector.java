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
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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

    public static final String PROP_PRINT_LOGS = "talend.studio.proxy.printLogs";

    private static final String ECLIPSE_PROXY_SELECTOR = ".EclipseProxySelector"; //$NON-NLS-1$

    private static final String PROP_ALLOW_PROXY_REDIRECT = "talend.studio.proxy.allowProxyRedirect";

    private static final String PROP_ALLOW_PROXY_REDIRECT_EXCLUDE = "talend.studio.proxy.redirect.whiteList";

    private static final String PROP_PROXY_HOST_MAP = "talend.studio.proxy.hostMap";

    private static final String PROP_DISABLE_DEFAULT_SELECTOR = "talend.studio.proxy.disableDefaultSelector";

    /**
     * Example: update.talend.com,socket:http,https:http;nexus.talend.com,socket,http;,socket:http
     */
    private static final String PROP_PROXY_MAP_HOST_DEFAULT = "";

    /**
     * Example: svn.company.com;nexus.company.com
     */
    private static final String PROP_ALLOW_PROXY_REDIRECT_EXCLUDE_DEFAULT = "";

    private static final String KEY_DEFAULT = ":default:";

    private ProxySelector defaultSelector;

    final private Map<Object, Collection<IProxySelectorProvider>> selectorProviders;

    private Map<String, Map<String, String>> hostMap;

    private Set<String> redirectWhiteList;

    private volatile static TalendProxySelector instance;

    private static Object instanceLock = new Object();

    private boolean printProxyLog = false;

    private boolean allowProxyRedirect = false;

    private boolean disableDefaultSelector = false;

    private TalendProxySelector(final ProxySelector defaultSelector) {
        this.defaultSelector = defaultSelector;

        selectorProviders = Collections.synchronizedMap(new HashMap<>());
        allowProxyRedirect = Boolean.valueOf(System.getProperty(PROP_ALLOW_PROXY_REDIRECT, Boolean.FALSE.toString()));
        disableDefaultSelector = Boolean.valueOf(System.getProperty(PROP_DISABLE_DEFAULT_SELECTOR, Boolean.FALSE.toString()));
        printProxyLog = Boolean.valueOf(System.getProperty(PROP_PRINT_LOGS, Boolean.FALSE.toString()));

        initHostMap();
        initRedirectList();
    }

    private void initHostMap() {
        try {
            hostMap = new HashMap<>();
            String property = System.getProperty(PROP_PROXY_HOST_MAP, PROP_PROXY_MAP_HOST_DEFAULT);
            if (StringUtils.isEmpty(property)) {
                return;
            }
            String[] splits = property.split(";");
            for (String split : splits) {
                try {
                    int index = split.indexOf(',');
                    String uri = split.substring(0, index);
                    String key = StringUtils.strip(uri);
                    if (StringUtils.isBlank(key)) {
                        key = KEY_DEFAULT;
                    }
                    key = key.toLowerCase();
                    Map<String, String> protocolMap = hostMap.get(key);
                    if (protocolMap == null) {
                        protocolMap = new HashMap<>();
                        hostMap.put(key, protocolMap);
                    }
                    int protocolMapIndex = index + 1;
                    String protocolMapStr = split.substring(protocolMapIndex);
                    String[] entry = protocolMapStr.split(",");
                    for (String pMap : entry) {
                        try {
                            String[] mapEntry = pMap.split(":");
                            if (mapEntry.length != 2) {
                                ExceptionHandler.process(
                                        new Exception(Messages.getString("TalendProxySelector.exception.badUriMap", pMap)));
                                continue;
                            }
                            protocolMap.put(mapEntry[0].toLowerCase(), mapEntry[1].toLowerCase());
                        } catch (Exception e) {
                            ExceptionHandler.process(e);
                        }
                    }
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    private void initRedirectList() {
        try {
            redirectWhiteList = new HashSet<>();
            String property = System.getProperty(PROP_ALLOW_PROXY_REDIRECT_EXCLUDE, PROP_ALLOW_PROXY_REDIRECT_EXCLUDE_DEFAULT);
            if (StringUtils.isEmpty(property)) {
                return;
            }
            String[] split = property.split(";");
            for (String host : split) {
                host = StringUtils.strip(host);
                if (StringUtils.isBlank(host)) {
                    host = KEY_DEFAULT;
                }
                redirectWhiteList.add(host);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
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
        Set<Proxy> results = new LinkedHashSet<>();

        try {
            final Set<Proxy> resultFromProviders = getProxysFromProviders(uri);
            if (resultFromProviders != null && !resultFromProviders.isEmpty()) {
                results.addAll(resultFromProviders);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        ProxySelector defaultProxySelector = getDefaultProxySelector();
        if (defaultProxySelector != null) {
            URI newUri = getNewUri(uri);
            List<Proxy> defaultProxys = defaultProxySelector.select(newUri);
            try {
                results.addAll(filterProxys(uri, defaultProxys));
            } catch (Exception e) {
                results.addAll(defaultProxys);
                ExceptionHandler.process(e);
            }
        }
        if (printProxyLog) {
            String proxys = results.toString();
            ExceptionHandler.log("Selected proxys for " + uri + ", " + proxys);
            ExceptionHandler.process(new Exception("Proxy call stacks"), Priority.INFO);
        }
        return new LinkedList<Proxy>(results);
    }

    private List<Proxy> filterProxys(final URI uri, List<Proxy> defaultProxys) {
        List<Proxy> result = new ArrayList<>();
        if (defaultProxys != null && !defaultProxys.isEmpty()) {
            for (Proxy proxy : defaultProxys) {
                SocketAddress addr = null;
                Proxy.Type proxyType = null;
                if (proxy != null) {
                    proxyType = proxy.type();
                    addr = proxy.address();
                }

                boolean redirect = true;
                if (!allowProxyRedirect) {
                    String host = uri.getHost();
                    if (host == null) {
                        host = "";
                    }
                    host = StringUtils.strip(host).toLowerCase();
                    if (this.redirectWhiteList.contains(host) || this.redirectWhiteList.contains(KEY_DEFAULT)) {
                        redirect = true;
                    } else if (Proxy.Type.DIRECT == proxyType
                            || (addr != null && StringUtils.equals(uri.getHost(), ((InetSocketAddress) addr).getHostString()))) {
                        redirect = false;
                    }
                }
                if (redirect) {
                    result.add(proxy);
                } else {
                    result.add(Proxy.NO_PROXY);
                }
            }
        }
        return result;
    }

    private URI getNewUri(URI uri) {
        URI newUri = uri;
        if (newUri != null) {
            String host = newUri.getHost();
            Map<String, String> protocolMap = null;
            if (StringUtils.isNotBlank(host)) {
                protocolMap = hostMap.get(host.toLowerCase());
            }
            if (protocolMap == null) {
                protocolMap = hostMap.get(KEY_DEFAULT);
            }

            if (protocolMap != null) {
                String schema = newUri.getScheme();
                if (schema != null) {
                    String lowercasedProtocol = schema.toLowerCase();
                    String preferedProtocol = protocolMap.get(lowercasedProtocol);
                    if (StringUtils.isNotBlank(preferedProtocol)) {
                        try {
                            newUri = new URI(preferedProtocol, newUri.getUserInfo(), newUri.getHost(), newUri.getPort(),
                                    newUri.getPath(), newUri.getQuery(), newUri.getFragment());
                        } catch (URISyntaxException e) {
                            if (printProxyLog) {
                                ExceptionHandler.process(new Exception(
                                        Messages.getString("TalendProxySelector.exception.proxySelectionError", newUri), e),
                                        Priority.WARN);
                            }
                        }
                    }
                }
            }
        }
        return newUri;
    }

    private Set<Proxy> getProxysFromProviders(final URI uri) {
        final Set<Proxy> resultFromProviders = new LinkedHashSet<>();
        Collection<IProxySelectorProvider> providers = getCustomProviders(uri);
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
        return resultFromProviders;
    }

    private Collection<IProxySelectorProvider> getCustomProviders(final URI uri) {
        Collection<IProxySelectorProvider> providers = Collections.EMPTY_LIST;
        Collection<Object> possibleKeys = getPossibleKeys(uri);
        for (Object key : possibleKeys) {
            providers = this.selectorProviders.get(key);
            if (providers != null) {
                break;
            }
        }
        return providers;
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

    public boolean addProxySelectorProvider(IProxySelectorProvider provider) {
        try {
            Object key = provider.getKey();
            Collection<IProxySelectorProvider> collection = this.selectorProviders.get(key);
            if (collection == null) {
                synchronized (this.selectorProviders) {
                    collection = this.selectorProviders.get(key);
                    if (collection == null) {
                        collection = Collections.synchronizedList(new LinkedList<>());
                        this.selectorProviders.put(key, collection);
                    }
                }
            }
            collection.add(provider);
            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    public boolean removeProxySelectorProvider(IProxySelectorProvider provider) {
        try {
            Object key = provider.getKey();
            Collection<IProxySelectorProvider> collection = this.selectorProviders.get(key);
            if (collection != null) {
                synchronized (this.selectorProviders) {
                    collection = this.selectorProviders.get(key);
                    if (collection != null) {
                        collection.remove(provider);
                        if (collection.isEmpty()) {
                            this.selectorProviders.remove(key);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        return false;
    }

    public ProxySelector getDefaultProxySelector() {
        return defaultSelector;
    }

    public void setDefaultProxySelector(final ProxySelector selector) {
        defaultSelector = selector;
    }

    @Override
    public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
        Collection<IProxySelectorProvider> providers = getCustomProviders(uri);
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

    public IProxySelectorProvider createDefaultProxySelectorProvider() {
        if (disableDefaultSelector) {
            return null;
        }
        return new DefaultProxySelectorProvider(Thread.currentThread());
    }

    public IProxySelectorProvider createDefaultProxySelectorProvider(String host) {
        if (disableDefaultSelector) {
            return null;
        }
        return new DefaultProxySelectorProvider(host);
    }

    public static interface IProxySelectorProvider {

        Object getKey();

        boolean canHandle(final URI uri);

        List<Proxy> select(final URI uri);

        void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe);

    }

    public static abstract class AbstractProxySelectorProvider implements IProxySelectorProvider {

        private boolean isDebugMode = CommonsPlugin.isDebugMode();

        @Override
        public Object getKey() {
            return this;
        }

        @Override
        public void connectFailed(final URI uri, final SocketAddress sa, final IOException ioe) {
            if (isDebugMode) {
                ExceptionHandler.process(ioe);
            }
        }

    }

    public static Collection<Object> getPossibleKeys(URI uri) {
        Collection<Object> possibleKeys = new ArrayList<>();
        possibleKeys.add(Thread.currentThread());
        if (uri != null) {
            String uriHost = uri.getHost();
            if (StringUtils.isNotBlank(uriHost)) {
                possibleKeys.add(uriHost);
            }
        }
        return possibleKeys;
    }

    private class DefaultProxySelectorProvider extends TalendProxySelector.AbstractProxySelectorProvider {

        private Thread currentThread = null;

        private String host = null;

        public DefaultProxySelectorProvider(Thread thread) {
            this.currentThread = thread;
        }

        public DefaultProxySelectorProvider(String host) {
            this.host = host;
            if (StringUtils.isNotBlank(this.host)) {
                this.host = this.host.toLowerCase();
            }
        }

        @Override
        public Object getKey() {
            if (this.currentThread != null) {
                return currentThread;
            }
            if (this.host != null) {
                return this.host;
            }
            return super.getKey();
        }

        @Override
        public boolean canHandle(URI uri) {
            if (disableDefaultSelector) {
                return false;
            }
            if (currentThread != null && Thread.currentThread() == currentThread) {
                return true;
            }
            if (host != null) {
                if (uri == null) {
                    return false;
                }
                String uriHost = uri.getHost();
                if (StringUtils.isNotBlank(uriHost)) {
                    return this.host.equals(uriHost.toLowerCase());
                }
                return false;
            }
            return false;
        }

        @Override
        public List<Proxy> select(URI uri) {
            List<Proxy> result = new ArrayList<>();
            try {
                ProxySelector defaultProxySelector = getDefaultProxySelector();
                if (defaultProxySelector != null) {
                    List<Proxy> defaultProxys = defaultProxySelector.select(uri);
                    if (defaultProxys != null && !defaultProxys.isEmpty()) {
                        for (Proxy proxy : defaultProxys) {
                            SocketAddress addr = null;
                            Proxy.Type proxyType = null;
                            if (proxy != null) {
                                proxyType = proxy.type();
                                addr = proxy.address();
                            }
                            if (Proxy.Type.DIRECT == proxyType || (addr != null
                                    && StringUtils.equals(uri.getHost(), ((InetSocketAddress) addr).getHostString()))) {
                                result.add(Proxy.NO_PROXY);
                            } else {
                                result.add(proxy);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
            return result;
        }

    }

}
