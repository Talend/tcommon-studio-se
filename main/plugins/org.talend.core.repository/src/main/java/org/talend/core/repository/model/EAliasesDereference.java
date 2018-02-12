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
package org.talend.core.repository.model;

/**
 *This class is used for storing aliases dereference for LDAP schema. <br/>
 *@author ftang, 18/09/2007
 * 
 */
public enum EAliasesDereference {

    ALWAYS("Always","always"), //$NON-NLS-1$ //$NON-NLS-2$
    NEVER("Never","never"), //$NON-NLS-1$ //$NON-NLS-2$
    FINDING("Finding","finding"), //$NON-NLS-1$ //$NON-NLS-2$
    SEARCHING("Searching","searching"); //$NON-NLS-1$ //$NON-NLS-2$

    private String displayName;
    
    private String repositoryName;

    /**
     * EAliasesDereference constructor comment.
     * @param displayName
     * @param repositoryName
     */
    private EAliasesDereference(String displayName, String repositoryName) {
        this.displayName = displayName;
        this.repositoryName = repositoryName;
    }

    /**
     * Comment method "getDisplayName".
     * @return
     */
    public String getDisplayName() {
        return this.displayName;
    }
    
    /**
     * Comment method "getRepositoryName".
     * @return
     */
    public String getRepositoryName(){
        return this.repositoryName;
    }
    
    
}
