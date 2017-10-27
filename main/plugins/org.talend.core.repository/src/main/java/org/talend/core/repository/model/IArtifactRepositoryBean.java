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
package org.talend.core.repository.model;


/**
 * created by cmeng on Mar 15, 2017
 * Detailled comment
 *
 */
public interface IArtifactRepositoryBean {

    public String getNexusUrl();

    public String getNexusUsername();

    public String getNexusPassword();

    public String getNexusDefaultReleaseRepoUrl();

    public String getNexusDefaultReleaseRepo();

    public String getNexusDefaultSnapshotRepoUrl();

    public String getNexusDefaultSnapshotRepo();

    public String getNexusDefaultGroupID();
}
