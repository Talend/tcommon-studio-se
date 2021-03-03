package org.talend.librariesmanager.nexus.nexus3.handler;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.runtime.maven.MavenArtifact;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Nexus3ScriptSearchHandler extends AbsNexus3SearchHandler {

    private String SEARCH_SERVICE = "service/rest/v1/script/search/run"; //$NON-NLS-1$

    public Nexus3ScriptSearchHandler(ArtifactRepositoryBean serverBean) {
        super(serverBean);
    }

    @Override
    public List<MavenArtifact> search(String repositoryId, String groupIdToSearch, String artifactId, String versionToSearch)
            throws Exception {
        String searchUrl = getSearchUrl();
        Request request = this.getRequest(searchUrl);
        List<MavenArtifact> resultList = new ArrayList<MavenArtifact>();
        JSONObject body = new JSONObject();
        body.put("repositoryId", repositoryId); //$NON-NLS-1$
        if (groupIdToSearch != null) {
            body.put("g", groupIdToSearch); //$NON-NLS-1$
        }
        if (artifactId != null) {
            body.put("a", artifactId); //$NON-NLS-1$
        }
        if (versionToSearch != null) {
            body.put("v", versionToSearch); //$NON-NLS-1$
        }
        request.bodyString(body.toString(),
                ContentType.create(ContentType.APPLICATION_JSON.getMimeType(), StandardCharsets.UTF_8));
        HttpResponse response = request.execute().returnResponse();
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String content = EntityUtils.toString(response.getEntity());
            if (content.isEmpty()) {
                return resultList;
            }
            JSONObject responseObject = JSONObject.fromObject(content);
            String resultStr = responseObject.getString("result"); //$NON-NLS-1$
            JSONArray resultArray = null;
            try {
                resultArray = JSONArray.fromObject(resultStr);
            } catch (Exception e) {
                throw new Exception(resultStr);
            }
            if (resultArray != null) {
                for (int i = 0; i < resultArray.size(); i++) {
                    JSONObject jsonObject = resultArray.getJSONObject(i);
                    MavenArtifact artifact = new MavenArtifact();
                    artifact.setGroupId(jsonObject.getString("groupId")); //$NON-NLS-1$
                    artifact.setArtifactId(jsonObject.getString("artifactId")); //$NON-NLS-1$
                    artifact.setVersion(jsonObject.getString("version")); //$NON-NLS-1$
                    artifact.setType(jsonObject.getString("extension")); //$NON-NLS-1$
                    artifact.setDescription(jsonObject.getString("description")); //$NON-NLS-1$
                    artifact.setLastUpdated(jsonObject.getString("last_updated")); //$NON-NLS-1$
                    resultList.add(artifact);
                }
            }
            return resultList;
        } else {
            throw new Exception(response.toString());
        }
    }

    protected Request getRequest(String searchUrl) {
        Request request = Request.Post(searchUrl);
        Header authority = new BasicHeader("Authorization", getAuthenticationItem()); //$NON-NLS-1$
        Header contentType = new BasicHeader("Content-Type", "text/plain"); //$NON-NLS-1$ //$NON-NLS-2$
        request.addHeader(contentType);
        request.addHeader(authority);
        request.socketTimeout(this.getNexus3SocketTimeout());
        return request;
    }

    protected String getSearchUrl() {
        return this.getServerUrl() + SEARCH_SERVICE;
    }

    public String getHandlerVersion() {
        return "Nexus3.script"; //$NON-NLS-1$
    }

}
