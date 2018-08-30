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
package org.talend.updates.runtime.engine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.eclipse.core.runtime.IProgressMonitor;
import org.junit.Assert;
import org.junit.Test;
import org.talend.commons.exception.BusinessException;
import org.talend.commons.utils.resource.FileExtensions;
import org.talend.core.nexus.HttpClientTransport;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.updates.runtime.nexus.component.ComponentIndexManager;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class HttpClientTransportTest {

    class HttpClientTransportTestClass extends HttpClientTransport {

        public HttpClientTransportTestClass(String baseURI, String username, String password) {
            super(baseURI, username, password);
        }

        @Override
        protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI) throws Exception {
            return null;
        }

    }

    @Test
    public void test_createURI_nullArtifact() throws URISyntaxException {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "admin", null);
        final URI uri = transport.createURI(null);
        Assert.assertNull(uri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_createURI_nullBaseURI() throws URISyntaxException {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(null, null, null);

        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId(ComponentIndexManager.COMPONENT_GROUP_ID);
        artifact.setArtifactId(ComponentIndexManager.INDEX);
        artifact.setVersion("6.4.1");
        artifact.setType(FileExtensions.XML_EXTENSION);

        transport.createURI(artifact);
    }

    @Test(expected = URISyntaxException.class)
    public void test_createURI_wrongURI() throws URISyntaxException {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass("://abc", "admin", null);

        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId(ComponentIndexManager.COMPONENT_GROUP_ID);
        artifact.setArtifactId(ComponentIndexManager.INDEX);
        artifact.setVersion("6.4.1");
        artifact.setType(FileExtensions.XML_EXTENSION);

        transport.createURI(artifact);
    }

    @Test
    public void test_createURI_withSlash() throws URISyntaxException {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "admin", null);

        doTestCreateURI(transport);
    }

    @Test
    public void test_createURI_withoutSlash() throws URISyntaxException {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components", "admin", null);

        doTestCreateURI(transport);
    }

    private void doTestCreateURI(HttpClientTransport transport) throws URISyntaxException {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId(ComponentIndexManager.COMPONENT_GROUP_ID);
        artifact.setArtifactId(ComponentIndexManager.INDEX);
        artifact.setVersion("6.4.1");
        artifact.setType(FileExtensions.XML_EXTENSION);

        final URI uri = transport.createURI(artifact);
        Assert.assertNotNull(uri);
        Assert.assertEquals(new URI(
                "http://localhost:8081/nexus/content/repositories/components/org/talend/components/index/6.4.1/index-6.4.1.xml"),
                uri);
    }

    @Test
    public void test_doRequest_withUser_nullPass() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "admin", null) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNotNull(credentials);
                Assert.assertEquals("admin", credentials.getUserPrincipal().getName());
                Assert.assertNull(credentials.getPassword());
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId(ComponentIndexManager.COMPONENT_GROUP_ID);
        artifact.setArtifactId(ComponentIndexManager.INDEX);
        artifact.setVersion("6.4.1");
        artifact.setType(FileExtensions.XML_EXTENSION);

        transport.doRequest(null, transport.createURI(artifact));
    }

    @Test
    public void test_doRequest_withUser_emptyPass() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "admin", "") {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNotNull(credentials);
                Assert.assertEquals("admin", credentials.getUserPrincipal().getName());
                Assert.assertEquals("", credentials.getPassword());
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        doTestRequest(transport);
    }

    private void doTestRequest(HttpClientTransport transport) throws Exception {
        MavenArtifact artifact = new MavenArtifact();
        artifact.setGroupId(ComponentIndexManager.COMPONENT_GROUP_ID);
        artifact.setArtifactId(ComponentIndexManager.INDEX);
        artifact.setVersion("6.4.1");
        artifact.setType(FileExtensions.XML_EXTENSION);

        transport.doRequest(null, transport.createURI(artifact));
    }

    @Test
    public void test_doRequest_withUser_withPass() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "admin", "1234") {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNotNull(credentials);
                Assert.assertEquals("admin", credentials.getUserPrincipal().getName());
                Assert.assertEquals("1234", credentials.getPassword());
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        doTestRequest(transport);
    }

    @Test
    public void test_doRequest_withoutUser_withPass() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, "123") {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNull(credentials);
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        doTestRequest(transport);
    }

    @Test
    public void test_doRequest_emptyUser() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "", "123") {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNull(credentials);
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        doTestRequest(transport);
    }

    @Test
    public void test_doRequest_spaceUser() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", "  ", "123") {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNull(credentials);
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        doTestRequest(transport);
    }

    @Test
    public void test_doRequest_withoutUserPass() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, null) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                final Credentials credentials = httpClient.getCredentialsProvider().getCredentials(
                        new AuthScope(targetURI.getHost(), targetURI.getPort()));
                Assert.assertNull(credentials);
                return null;
            }

            @Override
            public void processResponseCode(HttpResponse response) throws BusinessException {
                // nothing to do
            }

        };

        doTestRequest(transport);
    }

    @Test
    public void test_processResponseCode_ServerErr500() throws BusinessException {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, null);

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(500);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        try {
            transport.processResponseCode(httpResponse);
        } catch (Exception e) {
            Assert.assertTrue("Won't be here", false);
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void test_processResponseCode_UnAuth401() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, null);

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(401);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        try {
            transport.processResponseCode(httpResponse);

            Assert.assertTrue("Won't be here, should have exception", false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void test_processResponseCode_UnAuth404() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, null);

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(404);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        try {
            transport.processResponseCode(httpResponse);

            Assert.assertTrue("Won't be here, should have exception", false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test(expected = BusinessException.class)
    public void test_processResponseCode_others() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, null);

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(444);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        try {
            transport.processResponseCode(httpResponse);

            Assert.assertTrue("Won't be here, should have exception", false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Test
    public void test_processResponseCode_OK200() throws Exception {
        HttpClientTransportTestClass transport = new HttpClientTransportTestClass(
                "http://localhost:8081/nexus/content/repositories/components/", null, null);

        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        try {
            transport.processResponseCode(httpResponse);
        } catch (Exception e) {
            Assert.assertTrue("Won't be here", false);
            throw e;
        }
    }
}
