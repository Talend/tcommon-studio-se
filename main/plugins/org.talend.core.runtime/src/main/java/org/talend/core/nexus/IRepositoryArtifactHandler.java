// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
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

import org.talend.core.runtime.maven.MavenArtifact;

/**
 * created by wchen on Jul 31, 2017 Detailled comment
 *
 */
public interface IRepositoryArtifactHandler {

    public void setArtifactServerBean(NexusServerBean serverBean);

    public NexusServerBean getArtifactServerBean();

    public boolean checkConnection();

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

    public void updateMavenResolver(Dictionary<String, String> props);

    public File resolve(String mvnUrl) throws Exception;

    public IRepositoryArtifactHandler clone();

    public String getRepositoryURL(boolean isRelease);
}
