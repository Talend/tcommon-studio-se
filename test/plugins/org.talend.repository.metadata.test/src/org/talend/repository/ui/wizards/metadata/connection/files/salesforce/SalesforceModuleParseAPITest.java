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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * DOC Administrator class global comment. Detailled comment
 */
public class SalesforceModuleParseAPITest {

    String proxyType = "http";

    String proxyHost = "127.0.0.1";

    String proxyPort = "8080";

    String proxyUsername = "Talend";

    String proxyPassword = "talend";

    boolean httpProxy;

    boolean socksProxy;

    boolean httpsProxy;

    /**
     * DOC Administrator Comment method "setUp".
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModuleParseAPITest#setProxy()}
     * .
     */
    @Test
    public void testSetProxy() {
        Properties properties = System.getProperties();
        SalesforceModuleParseAPI api = new SalesforceModuleParseAPI();
        api.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, true, false, false);
        Assert.assertEquals(proxyHost, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_HOST));
        Assert.assertEquals(proxyUsername, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_USER));
        Assert.assertEquals(proxyPort, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_PORT));
        Assert.assertEquals(proxyPassword, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD));

        api.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, false, true, false);
        Assert.assertEquals(proxyHost, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_HOST));
        Assert.assertEquals(proxyUsername, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME));
        Assert.assertEquals(proxyPort, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_PORT));
        Assert.assertEquals(proxyPassword, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD));

        api.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, false, false, true);
        Assert.assertEquals(proxyHost, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_HOST));
        Assert.assertEquals(proxyUsername, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_USER));
        Assert.assertEquals(proxyPort, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_PORT));
        Assert.assertEquals(proxyPassword, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD));

    }

    /**
     * Test method for
     * {@link org.talend.repository.ui.wizards.metadata.connection.files.salesforce.SalesforceModuleParseAPITest#resetAllProxy()}
     * .
     */
    @Test
    public void testResetAllProxy() {
        Properties properties = System.getProperties();
        SalesforceModuleParseAPI api = new SalesforceModuleParseAPI();
        // if before did not set any proxy ,then reset will remove all
        api.resetAllProxy();
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_HOST));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_USER));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_PORT));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD));

        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_HOST));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_PORT));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD));

        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_HOST));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_USER));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_PORT));
        Assert.assertEquals(null, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD));

        // if set twice ,reset should set back to the first time
        api.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, true, false, false);
        api.setProxy("hello", "hello", "hello", "hello", true, false, false);
        api.resetAllProxy();
        Assert.assertEquals(proxyHost, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_HOST));
        Assert.assertEquals(proxyUsername, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_USER));
        Assert.assertEquals(proxyPort, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_PORT));
        Assert.assertEquals(proxyPassword, properties.getProperty(SalesforceModuleParseAPI.HTTP_PROXY_PASSWORD));

        api.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, false, true, false);
        api.setProxy("hello", "hello", "hello", "hello", false, true, false);
        api.resetAllProxy();
        Assert.assertEquals(proxyHost, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_HOST));
        Assert.assertEquals(proxyUsername, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_USERNAME));
        Assert.assertEquals(proxyPort, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_PORT));
        Assert.assertEquals(proxyPassword, properties.getProperty(SalesforceModuleParseAPI.SOCKS_PROXY_PASSWORD));

        api.setProxy(proxyHost, proxyPort, proxyUsername, proxyPassword, false, false, true);
        api.setProxy("hello", "hello", "hello", "hello", false, false, true);
        api.resetAllProxy();
        Assert.assertEquals(proxyHost, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_HOST));
        Assert.assertEquals(proxyUsername, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_USER));
        Assert.assertEquals(proxyPort, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_PORT));
        Assert.assertEquals(proxyPassword, properties.getProperty(SalesforceModuleParseAPI.HTTPS_PROXY_PASSWORD));

    }

}
