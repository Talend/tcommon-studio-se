// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.updates.runtime.model;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.talend.commons.utils.resource.FileExtensions;
import org.talend.commons.utils.resource.UpdatesHelper;

/**
 * This represent the repository that are used to fetch the extra feature to install
 * 
 */
public class FeatureRepositories {

    public static final String URI_JAR_SCHEMA = "jar"; //$NON-NLS-1$

    public static final String URI_JAR_SUFFIX = "!/"; //$NON-NLS-1$

    public static final String URI_FILE_SCHEMA = "file"; //$NON-NLS-1$

    private UpdateSiteLocationType updateSiteLocationType;

    private String remoteRepoUriStr;

    private String localRepoPathStr;

    /**
     * DOC sgandon FeatureRepositories constructor comment.
     */
    public FeatureRepositories() {
        updateSiteLocationType = UpdateSiteLocationType.DEFAULT_REPO;
    }

    /**
     * return the list of p2 repo URIs according to the kind of repo selected
     * 
     * @return
     * @throws URISyntaxException
     */
    public List<URI> getAllRepoUris(ExtraFeature ef) throws URISyntaxException {
        List<URI> allReposUris = new ArrayList<URI>();
        switch (updateSiteLocationType) {
        case DEFAULT_REPO: // use the feature defauld uri
            // return empty list so default will be used
            break;
        case REMOTE_REPO: // use the provided remote uri
            allReposUris.add(new URI(getRemoteRepoUriStr()));
            break;
        case LOCAL_FOLDER: // parse the folder to find a p2 site or to find some p2 zipped sites
            File localFolder = new File(getLocalRepoPathStr());
            allReposUris.addAll(getP2RepoUris4LocalFolder(localFolder));
            break;

        default:
            throw new IllegalArgumentException("all enum from FeatureRepositories.POLICY should be handled here"); //$NON-NLS-1$

        }
        return allReposUris;
    }

    private List<URI> getP2RepoUris4LocalFolder(File localFolder) throws URISyntaxException {
        List<URI> allReposUris = new ArrayList<URI>();

        if (localFolder.isDirectory()) {
            if (UpdatesHelper.isUpdateSite(localFolder)) { // update site folder in root
                allReposUris.add(localFolder.toURI());
            } else { // check the zip files
                File[] allZippedFiles = localFolder.listFiles(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(FileExtensions.ZIP_FILE_SUFFIX);
                    }
                });
                if (allZippedFiles != null) {
                    for (File f : allZippedFiles) {
                        if (UpdatesHelper.isUpdateSite(f)) {
                            // the following URI is a special P2 uri for local zipped files
                            allReposUris.add(new URI(URI_JAR_SCHEMA + ':' + f.toURI().toString() + URI_JAR_SUFFIX));
                        }
                    }
                }
            }

            // recursive for all
            File[] dirs = localFolder.listFiles(new FileFilter() {

                @Override
                public boolean accept(File file) {
                    return file.isDirectory();
                }
            });
            for (File d : dirs) {
                allReposUris.addAll(getP2RepoUris4LocalFolder(d));
            }
        }

        return allReposUris;
    }

    /**
     * Getter for updateSiteLocationType.
     * 
     * @return the policy
     */
    public UpdateSiteLocationType getUpdateSiteLocationType() {
        return this.updateSiteLocationType;
    }

    /**
     * Sets the updateSiteLocationType.
     * 
     * @param updateSiteLocationType the update Site Location Type to set
     */
    public void setUpdateSiteLocationType(UpdateSiteLocationType updateSiteLocationType) {
        this.updateSiteLocationType = updateSiteLocationType;
    }

    /**
     * Getter for remoteRepoUriStr.
     * 
     * @return the remoteRepoUriStr
     */
    public String getRemoteRepoUriStr() {
        return this.remoteRepoUriStr;
    }

    /**
     * Sets the remoteRepoUriStr.
     * 
     * @param remoteRepoUriStr the remoteRepoUriStr to set
     */
    public void setRemoteRepoUriStr(String remoteRepoUriStr) {
        this.remoteRepoUriStr = remoteRepoUriStr;
    }

    /**
     * Getter for localRepoPathStr.
     * 
     * @return the localRepoPathStr
     */
    public String getLocalRepoPathStr() {
        return this.localRepoPathStr;
    }

    /**
     * Sets the localRepoPathStr.
     * 
     * @param localRepoPathStr the localRepoPathStr to set
     */
    public void setLocalRepoPathStr(String localRepoPathStr) {
        this.localRepoPathStr = localRepoPathStr;
    }
}
