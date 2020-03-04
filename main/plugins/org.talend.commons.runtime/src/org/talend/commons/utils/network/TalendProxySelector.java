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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
import org.eclipse.core.internal.net.ProxyManager;
import org.eclipse.core.net.proxy.IProxyService;
import org.talend.commons.CommonsPlugin;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.i18n.internal.Messages;
import org.talend.daikon.sandbox.properties.ClassLoaderIsolatedSystemProperties;

import sun.net.spi.DefaultProxySelector;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class TalendProxySelector extends ProxySelector {

    public static final String PROP_PRINT_LOGS = "talend.studio.proxy.printLogs";

    private static final String ECLIPSE_PROXY_SELECTOR = ".EclipseProxySelector"; //$NON-NLS-1$

    private static final String PROP_ALLOW_PROXY_REDIRECT = "talend.studio.proxy.allowProxyRedirect";

    private static final String PROP_ALLOW_PROXY_REDIRECT_EXCLUDE = "talend.studio.proxy.redirect.whiteList";

    private static final String PROP_EXECUTE_CONNECTION_FAILED = "talend.studio.proxy.executeConnectionFailed";

    private static final String PROP_UPDATE_SYSTEM_PROPERTIES_FOR_JRE = "talend.studio.proxy.jre.updateSystemProperties";

    private static final String PROP_CHECK_PROXY = "talend.studio.proxy.checkProxy";

    private static final String PROP_VALIDATE_URI = "talend.studio.proxy.validateUri";

    private static final String PROP_PROXY_SELECTOR = "talend.studio.proxy.selector";

    private static final String PROP_PROXY_SELECTOR_DEFAULT = "default";

    private static final String PROP_PROXY_SELECTOR_JRE = "jre";

    private static final String PROP_PROXY_HOST_MAP = "talend.studio.proxy.hostMap";

    private static final String PROP_DISABLE_DEFAULT_SELECTOR_PROVIDER = "talend.studio.proxy.disableDefaultSelectorProvider";

    /**
     * Example: update.talend.com,socket:http,https:http;nexus.talend.com,socket,http;,socket:http
     */
    private static final String PROP_PROXY_MAP_HOST_DEFAULT = "";

    /**
     * Example: svn.company.com;nexus.company.com
     */
    private static final String PROP_ALLOW_PROXY_REDIRECT_EXCLUDE_DEFAULT = "";

    private static final String KEY_DEFAULT = ":default:";

    private static Field uriHostField;

    private static Method proxyManagerUpdateSystemPropertiesFunc;

    private static boolean checkProxy = Boolean.valueOf(System.getProperty(PROP_CHECK_PROXY, Boolean.TRUE.toString()));

    /**
     * Note: eclipse default selector may be different between TOS and TIS, TOS may use jre one, TIS may use egit one
     */
    private ProxySelector eclipseDefaultSelector;

    private ProxySelector jreDefaultSelector;

    private EProxySelector eProxySelector;

    final private Map<Object, Collection<IProxySelectorProvider>> selectorProviders;

    private Map<String, Map<String, String>> hostMap;

    private Set<String> redirectWhiteList;

    private volatile static TalendProxySelector instance;

    private static Object instanceLock = new Object();

    private boolean printProxyLog = false;

    private boolean allowProxyRedirect = false;

    private boolean disableDefaultSelectorProvider = false;

    private boolean validateUri = true;

    private boolean executeConnectionFailed = true;

    private boolean updateSystemPropertiesForJre = true;

    private TalendProxySelector(final ProxySelector eclipseDefaultSelector) {
        this.eclipseDefaultSelector = eclipseDefaultSelector;
        this.jreDefaultSelector = new DefaultProxySelector();

        selectorProviders = Collections.synchronizedMap(new HashMap<>());
        allowProxyRedirect = Boolean.valueOf(System.getProperty(PROP_ALLOW_PROXY_REDIRECT, Boolean.FALSE.toString()));
        disableDefaultSelectorProvider = Boolean
                .valueOf(System.getProperty(PROP_DISABLE_DEFAULT_SELECTOR_PROVIDER, Boolean.FALSE.toString()));
        printProxyLog = Boolean.valueOf(System.getProperty(PROP_PRINT_LOGS, Boolean.FALSE.toString()));
        validateUri = Boolean.valueOf(System.getProperty(PROP_VALIDATE_URI, Boolean.TRUE.toString()));
        executeConnectionFailed = Boolean.valueOf(System.getProperty(PROP_EXECUTE_CONNECTION_FAILED, Boolean.TRUE.toString()));
        updateSystemPropertiesForJre = Boolean
                .valueOf(System.getProperty(PROP_UPDATE_SYSTEM_PROPERTIES_FOR_JRE, Boolean.TRUE.toString()));

        switch (System.getProperty(PROP_PROXY_SELECTOR, PROP_PROXY_SELECTOR_DEFAULT).toLowerCase()) {
        case PROP_PROXY_SELECTOR_JRE:
            this.eProxySelector = EProxySelector.jre;
            break;
        default:
            this.eProxySelector = EProxySelector.eclipse_default;
            break;
        }

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
                    try {
                        uriHostField = URI.class.getDeclaredField("host");
                        uriHostField.setAccessible(true);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                    try {
                        proxyManagerUpdateSystemPropertiesFunc = ProxyManager.class.getDeclaredMethod("updateSystemProperties");
                        proxyManagerUpdateSystemPropertiesFunc.setAccessible(true);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
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
            if (instance.getEclipseDefaultSelector() == null
                    || (proxySelector != null && proxySelector.getClass().getName().endsWith(ECLIPSE_PROXY_SELECTOR))) {
                instance.setEclipseDefaultSelector(proxySelector);
            }
        }

        return instance;
    }

    public static void checkProxy() {
        if (!checkProxy) {
            return;
        }
        try {
            TalendProxySelector.getInstance();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    @Override
    public List<Proxy> select(final URI uri) {
        if (printProxyLog) {
            ExceptionHandler.log("TalendProxySelector.select " + uri);
        }
        if (uri == null) {
            return Collections.EMPTY_LIST;
        }
        URI validatedUri = validateUri(uri);
        Set<Proxy> results = new LinkedHashSet<>();

        try {
            final Set<Proxy> resultFromProviders = getProxysFromProviders(validatedUri);
            if (resultFromProviders != null && !resultFromProviders.isEmpty()) {
                results.addAll(resultFromProviders);
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
        if (printProxyLog) {
            ExceptionHandler.log("TalendProxySelector.resultFromProviders " + results);
        }

        ProxySelector defaultProxySelector = getDefaultProxySelector();
        if (printProxyLog) {
            ExceptionHandler.log("TalendProxySelector.defaultProxySelector " + defaultProxySelector);
        }
        if (defaultProxySelector != null) {
            /**
             * don't validate uri here, so that we can know whether it is an issue uri
             */
            URI newUri = getNewUri(validatedUri, false);
            List<Proxy> defaultProxys = null;
            if (validateUri && StringUtils.isBlank(newUri.getHost())) {
                /**
                 * If host is blank, force to use jre proxy selector to avoid the eclipse proxy selector bug
                 */
                defaultProxys = getJreProxySelector().select(newUri);
            } else {
                defaultProxys = defaultProxySelector.select(newUri);
            }
            if (printProxyLog) {
                ExceptionHandler.log("TalendProxySelector.defaultProxys " + defaultProxys);
            }
            try {
                results.addAll(filterProxys(validatedUri, defaultProxys));
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

    private URI validateUri(URI uri) {
        if (!validateUri) {
            return uri;
        }

        URI validatedUri = null;
        try {
            /**
             * DON'T use URI.create(), MUST use the conductor which requires authority
             */
            validatedUri = new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), uri.getQuery(), uri.getFragment());

            /**
             * Validate the host, if the host is empty, it will cause the eclipse selector to return dirrect
             */
            if (StringUtils.isBlank(validatedUri.getHost())) {
                String authority = validatedUri.getAuthority();
                if (StringUtils.isNotBlank(authority)) {
                    // example: https://u:p@www.company.com:8081/path/a?param=b
                    String host = null;
                    int userInfoIndex = authority.indexOf('@');
                    if (0 <= userInfoIndex) {
                        authority = authority.substring(userInfoIndex + 1);
                    }
                    int portIndex = authority.lastIndexOf(':');
                    if (0 <= portIndex) {
                        host = authority.substring(0, portIndex);
                    }
                    try {
                        uriHostField.set(validatedUri, host);
                    } catch (Exception e) {
                        ExceptionHandler.process(e);
                    }
                }

            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
            if (validatedUri == null) {
                validatedUri = uri;
            }
        }
        if (printProxyLog) {
            ExceptionHandler.log("After validate: " + uri + " -> " + validatedUri);
        }
        return validatedUri;
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

    private URI getNewUri(URI uri, boolean validateUri) {
        URI newUri = uri;
        if (newUri != null) {
            try {
                // get host before new URI, because the host may be set manually due to URI issue
                String host = newUri.getHost();
                newUri = new URI(newUri.getScheme(), newUri.getAuthority(), newUri.getPath(), newUri.getQuery(),
                        newUri.getFragment());
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
                            /**
                             * Note: MUST use the constructor which requires authority, because some uri may be illegal,
                             * then host info will be stored in authority field instead of host filed
                             */
                            newUri = new URI(preferedProtocol, newUri.getAuthority(), newUri.getPath(), newUri.getQuery(),
                                    newUri.getFragment());
                        }
                    }
                }
            } catch (URISyntaxException e) {
                if (printProxyLog) {
                    ExceptionHandler.process(
                            new Exception(Messages.getString("TalendProxySelector.exception.proxySelectionError", uri), e),
                            Priority.WARN);
                }
            }
        }
        if (validateUri) {
            newUri = validateUri(newUri);
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
        switch (eProxySelector) {
        case jre:
            return getJreProxySelector();
        default:
            return eclipseDefaultSelector;
        }
    }

    private ProxySelector getJreProxySelector() {
        try {
            /**
             * for tcompv0, daikon may create an isolated system properties for it, so proxies may be ignored in the new
             * system properties; here we try to call the method to add proxies into the isolated system properties
             */
            if (updateSystemPropertiesForJre && ClassLoaderIsolatedSystemProperties.getInstance()
                    .isIsolated(Thread.currentThread().getContextClassLoader())) {
                if (printProxyLog) {
                    ExceptionHandler.log("Before update jre proxy system properties for the isolated classloader, http.proxyHost="
                            + System.getProperty("http.proxyHost"));
                }
                IProxyService proxyService = CommonsPlugin.getProxyService();
                proxyManagerUpdateSystemPropertiesFunc.invoke(proxyService);
                if (printProxyLog) {
                    ExceptionHandler.log("After updated jre proxy system properties for the isolated classloader, http.proxyHost="
                            + System.getProperty("http.proxyHost"));
                }
            }
        } catch (Throwable e) {
            ExceptionHandler.process(e);
        }
        return this.jreDefaultSelector;
    }

    public ProxySelector getEclipseDefaultSelector() {
        return eclipseDefaultSelector;
    }

    public void setEclipseDefaultSelector(ProxySelector eclipseDefaultSelector) {
        this.eclipseDefaultSelector = eclipseDefaultSelector;
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

        if (executeConnectionFailed) {
            /**
             * Just try to make the behavior of jre proxy selector same like eclipse proxy selector
             */
            ProxySelector defaultProxySelector = getDefaultProxySelector();
            if (defaultProxySelector != null) {
                defaultProxySelector.connectFailed(uri, sa, ioe);
            }
        }
    }

    public IProxySelectorProvider createDefaultProxySelectorProvider() {
        if (disableDefaultSelectorProvider) {
            return null;
        }
        return new DefaultProxySelectorProvider(Thread.currentThread());
    }

    public IProxySelectorProvider createDefaultProxySelectorProvider(String host) {
        if (disableDefaultSelectorProvider) {
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

    private enum EProxySelector {
        eclipse_default,
        jre
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
            if (disableDefaultSelectorProvider) {
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
