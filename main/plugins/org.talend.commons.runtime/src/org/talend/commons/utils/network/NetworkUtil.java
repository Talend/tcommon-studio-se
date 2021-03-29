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
package org.talend.commons.utils.network;

import java.lang.reflect.Field;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.PasswordAuthentication;
import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.runtime.utils.io.FileCopyUtils;

/**
 * ggu class global comment. Detailled comment
 */
public class NetworkUtil {

    private static final String[] windowsCommand = { "ipconfig", "/all" }; //$NON-NLS-1$ //$NON-NLS-2$

    private static final String[] linuxCommand = { "/sbin/ifconfig", "-a" }; //$NON-NLS-1$ //$NON-NLS-2$

    private static final Pattern macPattern = Pattern
            .compile(".*((:?[0-9a-f]{2}[-:]){5}[0-9a-f]{2}).*", Pattern.CASE_INSENSITIVE); //$NON-NLS-1$

    private static final String TALEND_DISABLE_INTERNET = "talend.disable.internet";//$NON-NLS-1$

    private static final String HTTP_NETWORK_URL = "https://talend-update.talend.com";

    private static final int DEFAULT_TIMEOUT = 4000;

    private static final int DEFAULT_NEXUS_TIMEOUT = 20000;// same as preference value

    public static final String ORG_TALEND_DESIGNER_CORE = "org.talend.designer.core"; //$NON-NLS-1$

    private static final String PROP_DISABLEDSCHEMES_USE_DEFAULT = "talend.studio.jdk.http.auth.tunneling.disabledSchemes.useDefault";

    private static final String PROP_JRE_DISABLEDSCHEMES = "jdk.http.auth.tunneling.disabledSchemes";

    private static final String PROP_JRE_DISABLEDSCHEMES_DFAULT = "";

    private static final String PROP_HTTP_PROXY_SET = "http.proxySet";

    public static boolean isNetworkValid() {
        return isNetworkValid(DEFAULT_TIMEOUT);
    }

    public static boolean isNetworkValid(Integer timeout) {
        String disableInternet = System.getProperty(TALEND_DISABLE_INTERNET);
        if ("true".equals(disableInternet)) { //$NON-NLS-1$
            return false;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(HTTP_NETWORK_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDefaultUseCaches(false);
            conn.setUseCaches(false);
            int conntimeout = timeout != null ? timeout.intValue() : DEFAULT_TIMEOUT;
            conn.setConnectTimeout(conntimeout);
            conn.setReadTimeout(conntimeout);
            conn.setRequestMethod("HEAD"); //$NON-NLS-1$
            String strMessage = conn.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) { //$NON-NLS-1$
                return false;
            }
            if (strMessage.equals("OK")) { //$NON-NLS-1$
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            conn.disconnect();
        }
        return true;
    }

    public static boolean isNetworkValid(String url, Integer timeout) {
        if (url == null) {
            return isNetworkValid(timeout);
        }
        return checkValidWithHttp(url, timeout);
    }

    private static boolean checkValidWithHttp(String urlString, Integer timeout) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDefaultUseCaches(false);
            conn.setUseCaches(false);
            int conntimeout = timeout != null ? timeout.intValue() : DEFAULT_TIMEOUT;
            conn.setConnectTimeout(conntimeout);
            conn.setReadTimeout(conntimeout);
            conn.setRequestMethod("HEAD"); //$NON-NLS-1$
            conn.getResponseMessage();
        } catch (Exception e) {
            // if not reachable , will throw exception(time out/unknown host) .So if catched exception, make it a
            // invalid server
            return false;
        } finally {
            conn.disconnect();
        }
        return true;
    }

    public static int getNexusTimeout() {
        int timeout = DEFAULT_NEXUS_TIMEOUT;
        try {
            IEclipsePreferences node = InstanceScope.INSTANCE.getNode(ORG_TALEND_DESIGNER_CORE);
            timeout = node.getInt(ITalendNexusPrefConstants.NEXUS_TIMEOUT, DEFAULT_NEXUS_TIMEOUT);
        } catch (Throwable e) {
            ExceptionHandler.process(e);
        }

        return timeout;
    }

    public static Authenticator getDefaultAuthenticator() {
        try {
            Field theAuthenticatorField = Authenticator.class.getDeclaredField("theAuthenticator");
            if (theAuthenticatorField != null) {
                theAuthenticatorField.setAccessible(true);
                Authenticator setAuthenticator = (Authenticator) theAuthenticatorField.get(null);
                return setAuthenticator;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void loadAuthenticator() {
        // get parameter from System.properties.
        if (Boolean.getBoolean("http.proxySet")) {//$NON-NLS-1$
            // authentification for the url by using username and password
            Authenticator.setDefault(new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String httpProxyHost = System.getProperty("http.proxyHost"); //$NON-NLS-1$
                    String httpProxyPort = System.getProperty("http.proxyPort"); //$NON-NLS-1$
                    String httpsProxyHost = System.getProperty("https.proxyHost"); //$NON-NLS-1$
                    String httpsProxyPort = System.getProperty("https.proxyPort"); //$NON-NLS-1$
                    String requestingHost = getRequestingHost();
                    int requestingPort = getRequestingPort();
                    String proxyHost = null;
                    String proxyPort = null;
                    boolean isHttp = false;
                    if ("http".equalsIgnoreCase(getRequestingScheme())) {
                        isHttp = true;
                    }
                    if (isHttp && StringUtils.isNotBlank(httpProxyHost)) {
                        proxyHost = httpProxyHost;
                        proxyPort = httpProxyPort;
                    } else {
                        proxyHost = httpsProxyHost;
                        proxyPort = httpsProxyPort;
                    }
                    if (!StringUtils.equals(proxyHost, requestingHost) || !StringUtils.equals(proxyPort, "" + requestingPort)) {
                        return null;
                    }
                    String httpProxyUser = System.getProperty("http.proxyUser"); //$NON-NLS-1$
                    String httpProxyPassword = System.getProperty("http.proxyPassword"); //$NON-NLS-1$
                    String httpsProxyUser = System.getProperty("https.proxyUser"); //$NON-NLS-1$
                    String httpsProxyPassword = System.getProperty("https.proxyPassword"); //$NON-NLS-1$
                    String proxyUser = null;
                    char[] proxyPassword = new char[0];
                    if (StringUtils.isNotEmpty(httpProxyUser)) {
                        proxyUser = httpProxyUser;
                        if (StringUtils.isNotEmpty(httpProxyPassword)) {
                            proxyPassword = httpProxyPassword.toCharArray();
                        }
                    } else if (StringUtils.isNotEmpty(httpsProxyUser)) {
                        proxyUser = httpsProxyUser;
                        if (StringUtils.isNotEmpty(httpsProxyPassword)) {
                            proxyPassword = httpsProxyPassword.toCharArray();
                        }
                    }
                    if (StringUtils.isBlank(proxyUser)) {
                        return null;
                    } else {
                        return new PasswordAuthentication(proxyUser, proxyPassword);
                    }
                }

            });
        } else {
            Authenticator.setDefault(null);
        }
        checkProxyAuthSupport();
    }

    public static void checkProxyAuthSupport() {
        if (!Boolean.getBoolean(PROP_DISABLEDSCHEMES_USE_DEFAULT)) {
            if (Boolean.getBoolean(PROP_HTTP_PROXY_SET)) {
                if (!System.getProperties().containsKey(PROP_JRE_DISABLEDSCHEMES)) {
                    System.setProperty(PROP_JRE_DISABLEDSCHEMES, PROP_JRE_DISABLEDSCHEMES_DFAULT);
                }
            } else {
                if (PROP_JRE_DISABLEDSCHEMES_DFAULT.equals(System.getProperty(PROP_JRE_DISABLEDSCHEMES))) {
                    System.getProperties().remove(PROP_JRE_DISABLEDSCHEMES);
                }
            }
        }
    }

    public static void updateSvnkitConfigureFile(String srcFilePath, String destFilePath) {
        // SVNFileUtil getSystemApplicationDataPath C:\ProgramData\\Application Data
        // Note:ProgramData:Starting with Windows 10,this setting can no longer be used in provisioning packages.
        String osName = System.getProperty("os.name");//$NON-NLS-1$
        String osNameLC = osName == null ? null : osName.toLowerCase();
        boolean windows = osName != null && osNameLC.indexOf("windows") >= 0;//$NON-NLS-1$
        if (windows && Boolean.getBoolean("http.proxySet")) {//$NON-NLS-1$
            FileCopyUtils.copy(srcFilePath + "\\servers", destFilePath + "\\servers");//$NON-NLS-1$//$NON-NLS-2$
        }
    }

    /**
     * encode url
     *
     * @param urlStr url not encoded yet!
     * @return
     * @throws Exception
     */
    public static URL encodeUrl(String urlStr) throws Exception {
        try {
            // String decodedURL = URLDecoder.decode(urlStr, "UTF-8"); //$NON-NLS-1$
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(),
                    url.getRef());
            return uri.toURL();
        } catch (Exception e) {
            throw e;
        }
    }

    public static List<String> getLocalLoopbackAddresses(boolean wrapIpV6) {
        Set<String> addresses = new LinkedHashSet<>();
        try {
            addresses.add(getIp(InetAddress.getLoopbackAddress(), wrapIpV6));
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress.isLoopbackAddress()) {
                        addresses.add(getIp(inetAddress, wrapIpV6));
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

        if (addresses.isEmpty()) {
            addresses.add("127.0.0.1");
            String ipv6Loopback = "::1";
            if (wrapIpV6) {
                ipv6Loopback = "[" + ipv6Loopback + "]";
            }
            addresses.add(ipv6Loopback);
        }

        return new ArrayList<>(addresses);
    }

    private static String getIp(InetAddress inetAddress, boolean wrapIpV6) {
        if (wrapIpV6 && Inet6Address.class.isInstance(inetAddress)) {
            String addr = inetAddress.getHostAddress();
            if (!addr.startsWith("[") || !addr.endsWith("]")) {
                addr = "[" + addr + "]";
            }
            return addr;
        } else {
            return inetAddress.getHostAddress();
        }
    }

    public static boolean isSelfAddress(String addr) {
        if (addr == null || addr.isEmpty()) {
            return false; // ?
        }

        try {
            final InetAddress sourceAddress = InetAddress.getByName(addr);
            if (sourceAddress.isLoopbackAddress()) {
                // final String hostAddress = sourceAddress.getHostAddress();
                // // if addr is localhost, will be 127.0.0.1 also
                // if (hostAddress.equals("127.0.0.1") || hostAddress.equals("localhost") ) {
                return true;
                // }
            } else {
                // check all ip configs
                InetAddress curAddr = null;
                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();
                    while (address.hasMoreElements()) {
                        curAddr = address.nextElement();
                        if (addr.equals(curAddr.getHostAddress())) {
                            return true;
                        }
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return false;
    }
}
