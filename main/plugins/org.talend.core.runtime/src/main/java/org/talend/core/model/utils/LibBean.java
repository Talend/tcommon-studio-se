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
package org.talend.core.model.utils;

/**
 * created by cmeng on Apr 7, 2017 Detailled comment
 *
 */
public class LibBean {

    private String url;

    private String username;

    private String password;

    private String type;

    private String repositoryReleases;

    private String repositorySnapshots;

    private String defaultGroupID;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepositoryReleases() {
        return this.repositoryReleases;
    }

    public void setRepositoryReleases(String repositoryReleases) {
        this.repositoryReleases = repositoryReleases;
    }

    public String getRepositorySnapshots() {
        return this.repositorySnapshots;
    }

    public void setRepositorySnapshots(String repositorySnapshots) {
        this.repositorySnapshots = repositorySnapshots;
    }

    public String getDefaultGroupID() {
        return this.defaultGroupID;
    }

    public void setDefaultGroupID(String defaultGroupID) {
        this.defaultGroupID = defaultGroupID;
    }

}
