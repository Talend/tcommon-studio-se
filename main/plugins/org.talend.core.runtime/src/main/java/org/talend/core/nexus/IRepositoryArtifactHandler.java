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
package org.talend.core.nexus;

import java.io.File;
import java.util.Dictionary;
import java.util.List;

import org.apache.http.HttpResponse;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.utils.sugars.TypedReturnCode;

/**
 * created by wchen on Jul 31, 2017 Detailled comment
 *
 */
public interface IRepositoryArtifactHandler {

    public void setArtifactServerBean(ArtifactRepositoryBean serverBean);

    public ArtifactRepositoryBean getArtifactServerBean();

    public boolean checkConnection();

    public TypedReturnCode<HttpResponse> getConnectionResultAndCode();

    public boolean checkConnection(boolean checkRelease, boolean checkSnapshot);

    /**
     *
     * DOC wchen Comment method "search".
     *
     * @param groupIdToSearch
     * @param artifactId
     * @param versionToSearch
     * @param fromRelease search from release libraries repository if true
     * @param fromSnapshot search from snapshot libraries repository if true
     * @return
     * @throws Exception
     */
    public List<MavenArtifact> search(String groupIdToSearch, String artifactId, String versionToSearch, boolean fromRelease,
            boolean fromSnapshot) throws Exception;

    public void deploy(File content, String groupId, String artifactId, String classifier, String extension, String version)
            throws Exception;

    public void deployWithPOM(File content, File pomFile, String groupId, String artifactId, String classifier, String extension,
            String version) throws Exception;

    public File resolve(MavenArtifact ma) throws Exception;

    public void setLocalRepositoryPath(String localRepositoryPath);

    public String getLocalRepositoryPath();

    public void updateMavenResolver(String resolverKey, Dictionary<String, String> props);

    public File resolve(String mvnUrl) throws Exception;

    public IRepositoryArtifactHandler clone();

    public String getRepositoryURL(boolean isRelease);
    
    public String resolveRemoteSha1(MavenArtifact artifact, boolean fromRelease) throws Exception;

    public List<MavenArtifact> search(String name, boolean fromSnapshot) throws Exception;

}
