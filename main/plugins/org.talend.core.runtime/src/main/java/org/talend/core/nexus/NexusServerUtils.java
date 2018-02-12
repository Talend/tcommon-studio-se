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
package org.talend.core.nexus;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.runtime.maven.MavenArtifact;

/**
 * created by wchen on 2015-5-12 Detailled comment
 *
 */
public class NexusServerUtils {

    /**
     * 
     */
    public static final String ORG_TALEND_DESIGNER_CORE = "org.talend.designer.core"; //$NON-NLS-1$

    public static final int CONNECTION_OK = 200;

    // the max search result is 200 by defult from nexus
    private static final int MAX_SEARCH_COUNT = 200;

    /**
     * 
     * DOC check if the repository exist or not
     * 
     * @param nexusUrl
     * @param repositoryId
     * @param userName
     * @param password
     * @return
     */
    private static int getTimeout() {
        IEclipsePreferences node = InstanceScope.INSTANCE.getNode(ORG_TALEND_DESIGNER_CORE);
        int timeout = node.getInt(ITalendCorePrefConstants.NEXUS_TIMEOUT, 10000);
        return timeout;
    }

    public static boolean checkConnectionStatus(String nexusUrl, String repositoryId, final String userName, final String password) {
        if (StringUtils.isEmpty(nexusUrl) || StringUtils.isEmpty(repositoryId)) {
            return false;
        }

        String newUrl = nexusUrl;
        if (newUrl.endsWith(NexusConstants.SLASH)) {
            newUrl = newUrl.substring(0, newUrl.length() - 1);
        }
        String urlToCheck = newUrl + NexusConstants.CONTENT_REPOSITORIES + repositoryId;

        return checkConnectionStatus(urlToCheck, userName, password);
    }

    public static boolean checkConnectionStatus(String nexusURL, String username, String password) {
        if (StringUtils.isEmpty(nexusURL)) {
            return false;
        }
        final boolean status[] = { false };
        try {
            NullProgressMonitor monitor = new NullProgressMonitor();
            new HttpClientTransport(nexusURL, username, password) {

                @Override
                protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                        throws Exception {
                    httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout());
                    HttpHead httpHead = new HttpHead(targetURI);
                    HttpResponse response = httpClient.execute(httpHead);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        status[0] = true;
                    }
                    return response;
                }

            }.doRequest(monitor, new URI(nexusURL));

        } catch (Exception e) {
            status[0] = false;
        }
        return status[0];
    }

    public static List<MavenArtifact> search(String nexusUrl, String userName, String password, String repositoryId,
            String groupIdToSearch, String versionToSearch) throws Exception {
        List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        search(nexusUrl, userName, password, repositoryId, groupIdToSearch, null, versionToSearch, MAX_SEARCH_COUNT, artifacts);

        return artifacts;

    }

    public static List<MavenArtifact> search(String nexusUrl, String userName, String password, String repositoryId,
            String groupIdToSearch, String artifactId, String versionToSearch) throws Exception {
        List<MavenArtifact> artifacts = new ArrayList<MavenArtifact>();
        search(nexusUrl, userName, password, repositoryId, groupIdToSearch, artifactId, versionToSearch, MAX_SEARCH_COUNT,
                artifacts);

        return artifacts;

    }

    private static void search(String nexusUrl, final String userName, final String password, String repositoryId,
            String groupIdToSearch, String artifactId, String versionToSearch, int searchCount, List<MavenArtifact> artifacts)
            throws Exception {
        int totalCount = 0;
        String service = NexusConstants.SERVICES_SEARCH
                + getSearchQuery(repositoryId, groupIdToSearch, artifactId, versionToSearch, 0, searchCount);

        URI requestURI = getSearchURI(nexusUrl, service);
        Document document = downloadDocument(requestURI, userName, password);
        if (document != null) {
            // test
            // writeDocument(document, new File("D:/search.txt"));
            Node countNode = document.selectSingleNode("/searchNGResponse/totalCount");
            if (countNode != null) {
                try {
                    totalCount = Integer.parseInt(countNode.getText());
                } catch (NumberFormatException e) {
                    totalCount = 0;
                }
            }
            int searchDone = readDocument(document, artifacts);
            while (searchDone < totalCount) {
                service = NexusConstants.SERVICES_SEARCH
                        + getSearchQuery(repositoryId, groupIdToSearch, artifactId, versionToSearch, searchDone, searchCount);
                requestURI = getSearchURI(nexusUrl, service);

                document = downloadDocument(requestURI, userName, password);
                searchDone = searchDone + readDocument(document, artifacts);
            }
        }

    }

    public static Document downloadDocument(final URI requestURI, String userName, String password) throws Exception {
        final Document[] toReturn = { null };
        NullProgressMonitor monitor = new NullProgressMonitor();
        new HttpClientTransport("", userName, password) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                HttpGet httpGet = new HttpGet(requestURI);
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout());
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    SAXReader saxReader = new SAXReader();
                    Document document = saxReader.read(inputStream);
                    inputStream.close();
                    toReturn[0] = document;
                }
                return response;
            }

        }.doRequest(monitor, requestURI);

        return toReturn[0];
    }

    private static int readDocument(Document document, List<MavenArtifact> artifacts) throws Exception {
        List<Node> list = document.selectNodes("/searchNGResponse/data/artifact");//$NON-NLS-1$
        for (Node arNode : list) {
            MavenArtifact artifact = new MavenArtifact();
            artifacts.add(artifact);
            artifact.setGroupId(arNode.selectSingleNode("groupId").getText());//$NON-NLS-1$
            artifact.setArtifactId(arNode.selectSingleNode("artifactId").getText());//$NON-NLS-1$
            artifact.setVersion(arNode.selectSingleNode("version").getText());//$NON-NLS-1$
            Node descNode = arNode.selectSingleNode("description");//$NON-NLS-1$
            if (descNode != null) {
                artifact.setDescription(descNode.getText());
            }
            Node urlNode = arNode.selectSingleNode("url");//$NON-NLS-1$
            if (urlNode != null) {
                artifact.setUrl(urlNode.getText());
            }
            Node licenseNode = arNode.selectSingleNode("license");//$NON-NLS-1$
            if (licenseNode != null) {
                artifact.setLicense(licenseNode.getText());
            }

            Node licenseUrlNode = arNode.selectSingleNode("licenseUrl");//$NON-NLS-1$
            if (licenseUrlNode != null) {
                artifact.setLicenseUrl(licenseUrlNode.getText());
            }

            List<Node> artLinks = arNode.selectNodes("artifactHits/artifactHit/artifactLinks/artifactLink");//$NON-NLS-1$
            for (Node link : artLinks) {
                Node extensionElement = link.selectSingleNode("extension");//$NON-NLS-1$
                String extension = null;
                String classifier = null;
                if (extensionElement != null) {
                    if ("pom".equals(extensionElement.getText())) {//$NON-NLS-1$
                        continue;
                    }
                    extension = extensionElement.getText();
                }
                Node classifierElement = link.selectSingleNode("classifier");//$NON-NLS-1$
                if (classifierElement != null) {
                    classifier = classifierElement.getText();
                }
                artifact.setType(extension);
                artifact.setClassifier(classifier);
            }
        }
        return list.size();
    }

    public static String resolveSha1(String nexusUrl, final String userName, final String password, String repositoryId,
            String groupId, String artifactId, String version, String type) throws Exception {
        final String[] toReturn = { null };
        String service = NexusConstants.SERVICES_RESOLVE + "a=" + artifactId + "&g=" + groupId + "&r=" + repositoryId + "&v="
                + version + "&p=" + type;
        final URI requestURI = getSearchURI(nexusUrl, service);
        NullProgressMonitor monitor = new NullProgressMonitor();
        new HttpClientTransport(nexusUrl, userName, password) {

            @Override
            protected HttpResponse execute(IProgressMonitor monitor, DefaultHttpClient httpClient, URI targetURI)
                    throws Exception {
                HttpGet httpGet = new HttpGet(requestURI);
                httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getTimeout());
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    SAXReader saxReader = new SAXReader();
                    Document document = saxReader.read(inputStream);
                    if (document != null) {
                        Node sha1Node = document.selectSingleNode("/artifact-resolution/data/sha1");
                        String sha1 = null;
                        if (sha1Node != null) {
                            sha1 = sha1Node.getText();
                            toReturn[0] = sha1;
                        }
                    }
                }
                return response;
            }

        }.doRequest(monitor, requestURI);

        return toReturn[0];
    }

    private static String getSearchQuery(String repositoryId, String groupId, String artifactId, String version, int from,
            int count) {
        String query = "";//$NON-NLS-1$
        if (repositoryId != null) {
            query = "repositoryId=" + repositoryId;//$NON-NLS-1$
        }
        if (groupId != null) {
            if (!"".equals(query)) {
                query = query + "&";
            }
            query = query + "g=" + groupId;//$NON-NLS-1$
        }
        if (artifactId != null) {
            if (!"".equals(query)) {
                query = query + "&";
            }
            query = query + "a=" + artifactId;//$NON-NLS-1$
        }

        if (version != null) {
            if (!"".equals(query)) {
                query = query + "&";
            }
            query = query + "v=" + version;//$NON-NLS-1$
        }

        return query + "&from=" + from + "&count=" + count;//$NON-NLS-1$ //$NON-NLS-2$
    }

    private static URI getSearchURI(String nexusUrl, String relativePath) throws URISyntaxException {
        if (!nexusUrl.endsWith(NexusConstants.SLASH)) {
            nexusUrl = nexusUrl + NexusConstants.SLASH;
        }
        URI url = new URI(nexusUrl + relativePath);
        return url;
    }

    private static void writeDocument(Document document, File file) throws IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = null;
        try {
            writer = new XMLWriter(new FileWriter(file), format);
            writer.write(document);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

}
