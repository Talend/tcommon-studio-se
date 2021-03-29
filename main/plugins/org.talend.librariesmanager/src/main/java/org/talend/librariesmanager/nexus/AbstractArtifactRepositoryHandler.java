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
package org.talend.librariesmanager.nexus;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;
import org.eclipse.m2e.core.MavenPlugin;
import org.talend.core.nexus.ArtifactRepositoryBean;
import org.talend.core.nexus.IRepositoryArtifactHandler;
import org.talend.core.nexus.NexusConstants;
import org.talend.core.nexus.TalendMavenResolver;
import org.talend.core.runtime.maven.MavenArtifact;
import org.talend.core.runtime.maven.MavenUrlHelper;
import org.talend.designer.maven.aether.RepositorySystemFactory;
import org.talend.utils.string.StringUtilities;

/**
 * created by wchen on Aug 2, 2017 Detailled comment
 *
 */
public abstract class AbstractArtifactRepositoryHandler implements IRepositoryArtifactHandler {

    private String PAX_PID = "org.ops4j.pax.url.mvn";

    private String PROPERTY_REPOSITORIES = "repositories";

    protected ArtifactRepositoryBean serverBean;

    private String localRepositoryPath;

    /*
     * (non-Javadoc)
     *
     * @see org.talend.core.nexus.IArtifacRepositoryHandler#setArtifactServerBean(org.talend.core.nexus.NexusServerBean)
     */
    @Override
    public void setArtifactServerBean(ArtifactRepositoryBean serverBean) {
        this.serverBean = serverBean;
    }

    /**
     * Getter for serverBean.
     *
     * @return the serverBean
     */
    @Override
    public ArtifactRepositoryBean getArtifactServerBean() {
        return this.serverBean;
    }

    @Override
    public void updateMavenResolver(String resolverKey, Dictionary<String, String> props) {
        if (!TalendMavenResolver.needUpdate(resolverKey)) {
            return;
        }
        if (props == null) {
            props = new Hashtable<String, String>();
        }

        String custom_user = serverBean.getUserName();
        String custom_pass = serverBean.getPassword();
        try {
            if (StringUtils.isNotBlank(custom_user)) {
                custom_user = URLEncoder.encode(custom_user, StandardCharsets.UTF_8.toString());
            }
            if (StringUtils.isNotBlank(custom_pass)) {
                custom_pass = URLEncoder.encode(custom_pass, StandardCharsets.UTF_8.toString());
            }
        } catch (UnsupportedEncodingException e1) {
            throw new RuntimeException(e1);
        }

        String repositories = null;
        String custom_server = serverBean.getServer();
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
            String snapshotUrl = custom_server + snapshot_rep + "@id=" + snapshot_rep + NexusConstants.SNAPSHOTS //$NON-NLS-1$
                    + NexusConstants.DISALLOW_RELEASES;
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
        props.put("org.ops4j.pax.url.mvn.proxySupport", "true");
        try {
            TalendMavenResolver.updateMavenResolver(resolverKey, props);
        } catch (Exception e) {
            throw new RuntimeException("Failed to modifiy the service properties"); //$NON-NLS-1$
        }

    }

    @Override
    public File resolve(MavenArtifact ma) throws Exception {
        String version = ma.getVersion();
        boolean isRelease = !version.endsWith(MavenUrlHelper.VERSION_SNAPSHOT);
        return resolve(ma, isRelease);
    }

    protected File resolve(MavenArtifact ma, boolean isRelease) throws Exception {
        String repositoryId = "";
        String version = ma.getVersion();
        if (isRelease) {
            repositoryId = serverBean.getRepositoryId();
        } else {
            repositoryId = serverBean.getSnapshotRepId();
        }
        String repositoryurl = getRepositoryURL(isRelease);
        String localRepository = getLocalRepositoryPath();
        return RepositorySystemFactory.resolve(localRepository, repositoryId, repositoryurl, serverBean.getUserName(),
                serverBean.getPassword(), ma.getGroupId(), ma.getArtifactId(), ma.getClassifier(), ma.getType(), version);
    }

    @Override
    public void setLocalRepositoryPath(String localRepositoryPath) {
        this.localRepositoryPath = localRepositoryPath;
    }

    @Override
    public String getLocalRepositoryPath() {
        if (StringUtils.isBlank(this.localRepositoryPath)) {
            return MavenPlugin.getMaven().getLocalRepositoryPath();
        } else {
            return this.localRepositoryPath;
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
        if (repositoryBaseURI == null) {
            repositoryBaseURI = ""; //$NON-NLS-1$
        }
        repositoryBaseURI = StringUtilities.removeEndingString(repositoryBaseURI, "/"); //$NON-NLS-1$
        if (repositoryId == null) {
            repositoryId = ""; //$NON-NLS-1$
        }
        if (repositoryBaseURI.isEmpty()) {
            return addEndSlash(repositoryId);
        }
        repositoryId = StringUtilities.removeStartingString(repositoryId, "/"); //$NON-NLS-1$
        if (repositoryId.isEmpty()) {
            return addEndSlash(repositoryBaseURI);
        }

        repositoryBaseURI += getRepositoryPrefixPath();
        repositoryBaseURI += repositoryId + NexusConstants.SLASH;

        return addEndSlash(repositoryBaseURI);
    }

    private String addEndSlash(String URL) {
        if (!URL.endsWith("/")) {
            URL = URL + "/";
        }
        return URL;
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
    
    @Override
    public String resolveRemoteSha1(MavenArtifact artifact, boolean fromRelease) throws Exception {
        return artifact.getSha1();
    }
        
}
