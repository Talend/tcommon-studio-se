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
package org.talend.librariesmanager.nexus;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.NexusServerBean;
import org.talend.core.nexus.TalendMavenResolver;

/**
 * created by wchen on Aug 2, 2017 Detailled comment
 *
 */
public abstract class AbstractArtifactRepositoryHandler implements IRepositoryArtifactHandler {

    private String PAX_PID = "org.ops4j.pax.url.mvn";

    private String PROPERTY_REPOSITORIES = "repositories";

    protected NexusServerBean serverBean;

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IArtifacRepositoryHandler#setArtifactServerBean(org.talend.core.nexus.NexusServerBean)
     */
    @Override
    public void setArtifactServerBean(NexusServerBean serverBean) {
        this.serverBean = serverBean;
    }

    /**
     * Getter for serverBean.
     * 
     * @return the serverBean
     */
    @Override
    public NexusServerBean getArtifactServerBean() {
        return this.serverBean;
    }

    @Override
    public void updateMavenResolver(Dictionary<String, String> props) {
        if (props == null) {
            props = new Hashtable<String, String>();
        }
        String repositories = null;
        String custom_server = serverBean.getServer();
        String custom_user = serverBean.getUserName();
        String custom_pass = serverBean.getPassword();
        String release_rep = serverBean.getRepositoryId();
        String snapshot_rep = serverBean.getSnapshotRepId();
        if (custom_server.endsWith(NexusConstants.SLASH)) {
            custom_server = custom_server.substring(0, custom_server.length() - 1);
        }
        if (custom_user != null && !"".equals(custom_user)) {//$NON-NLS-1$
            String[] split = custom_server.split("://");//$NON-NLS-1$
            custom_server = split[0] + "://" + custom_user + ":" + custom_pass + "@"//$NON-NLS-1$
                    + split[1] + getRepositoryPrefixPath();
        } else {
            custom_server = custom_server + getRepositoryPrefixPath();
        }
        if (release_rep != null) {
            String releaseUrl = custom_server + release_rep + "@id=" + release_rep;//$NON-NLS-1$
            repositories = releaseUrl;
        }
        if (snapshot_rep != null) {
            String snapshotUrl = custom_server + snapshot_rep + "@id=" + snapshot_rep + NexusConstants.SNAPSHOTS;//$NON-NLS-1$
            if (repositories != null) {
                repositories = repositories + "," + snapshotUrl;
            } else {
                repositories = snapshotUrl;
            }
        }

        if (repositories != null) {
            props.put(PAX_PID + '.' + PROPERTY_REPOSITORIES, repositories);
        }
        props.put("org.ops4j.pax.url.mvn.globalUpdatePolicy", "always");
        try {
            TalendMavenResolver.updateMavenResolver(props);
        } catch (Exception e) {
            throw new RuntimeException("Failed to modifiy the service properties"); //$NON-NLS-1$
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IRepositoryArtifactHandler#getRepositoryURI(boolean)
     */
    @Override
    public String getRepositoryURL(boolean isRelease) {
        String repositoryId = "";
        if (isRelease) {
            repositoryId = serverBean.getRepositoryId();
        } else {
            repositoryId = serverBean.getSnapshotRepId();
        }
        String repositoryBaseURI = serverBean.getServer();
        if (repositoryBaseURI.endsWith(NexusConstants.SLASH)) {
            repositoryBaseURI = repositoryBaseURI.substring(0, repositoryBaseURI.length() - 1);
        }
        repositoryBaseURI += getRepositoryPrefixPath();
        repositoryBaseURI += repositoryId + NexusConstants.SLASH;
        return repositoryBaseURI;
    }

    protected abstract String getRepositoryPrefixPath();

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.core.nexus.IArtifacRepositoryHandler#resolve(java.lang.String)
     */
    @Override
    public File resolve(String mvnUrl) throws Exception {
        return TalendMavenResolver.resolve(mvnUrl);
    }

    @Override
    public abstract IRepositoryArtifactHandler clone();
}
