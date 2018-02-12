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
 * This class is used for storing Encryption method for LDAP schema. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public enum EEncryptionMethod {

    NO_ENCRYPTION_METHOD("LDAP"), //$NON-NLS-1$
    SSL_ENCRYPTION_METHOD("LDAPS(SSL)"), //$NON-NLS-1$
    STARTTSL_EXTENSION_METHOD("TLS"); //$NON-NLS-1$

    /**
     * 
     */
    private String methodName;

    /**
     * EEncryptionMethod constructor comment.
     * 
     * @param methodName
     */
    private EEncryptionMethod(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Comment method "getName".
     * 
     * @return
     */
    public String getName() {
        return this.methodName;
    }

}
