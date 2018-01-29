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

import org.talend.core.repository.i18n.Messages;

/**
 * This class is used for storing Authentication method for LDAP schema. <br/>
 * 
 * @author ftang, 18/09/2007
 * 
 */
public enum EAuthenticationMethod {
    ANONYMOUS("EAuthenticationMethod.anonymousAuth"), //$NON-NLS-1$
    SIMPLE("EAuthenticationMethod.simpleAuth"); //$NON-NLS-1$

    /**
     * 
     */
    private String methodName;

    /**
     * EAuthenticationMethod constructor comment.
     * 
     * @param methodName
     */
    private EAuthenticationMethod(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Comment method "getName".
     * 
     * @return
     */
    public String getName() {
        return Messages.getString(this.methodName);
    }
}
