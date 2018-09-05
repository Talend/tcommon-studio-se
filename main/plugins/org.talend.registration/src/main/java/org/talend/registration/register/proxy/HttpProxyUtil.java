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
package org.talend.registration.register.proxy;

import org.eclipse.core.internal.net.ProxyManager;
import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.core.runtime.CoreException;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.registration.RegistrationPlugin;

/**
 * created by ggu on 23 Jan 2015 Detailled comment
 *
 */
public class HttpProxyUtil {

    public static void setHttpProxyInfo(boolean enable, String httpProxyServer, String httpProxyPort) {
        IProxyService service = RegistrationPlugin.getDefault().getProxyService();
        if (service == null) {
            return;
        }
        // Make sure that the proxy service is enabled if needed but don't disable the
        // service if the http proxy is being disabled
        if (enable && !service.isProxiesEnabled()) {
            service.setProxiesEnabled(enable);
        }

        if (service.isProxiesEnabled()) {
            IProxyData data = service.getProxyData(IProxyData.HTTP_PROXY_TYPE);
            if (data != null) {
                data.setHost(httpProxyServer);
                if (httpProxyPort == null || httpProxyPort.equals("80")) {
                    data.setPort(-1);
                } else {
                    try {
                        int port = Integer.parseInt(httpProxyPort);
                        data.setPort(port);
                    } catch (NumberFormatException e) {
                        ExceptionHandler.process(e);
                    }
                }
                try {
                    service.setProxyData(new IProxyData[] { data });
                } catch (CoreException e) {
                    ExceptionHandler.process(e);
                }
            }
        }
    }

    public static void initializeHttpProxy() {
        ProxyManager proxyManager = null;
        IProxyService service = RegistrationPlugin.getDefault().getProxyService();
        if (service != null && service instanceof ProxyManager) {
            proxyManager = (ProxyManager) service;
        }
        if (proxyManager == null) {
            proxyManager = (ProxyManager) ProxyManager.getProxyManager();
            proxyManager.initialize();
        }
    }
}
