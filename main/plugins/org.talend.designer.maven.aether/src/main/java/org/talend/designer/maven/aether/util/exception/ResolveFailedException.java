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
package org.talend.designer.maven.aether.util.exception;

import java.util.List;

public class ResolveFailedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private List<Exception> exceptions;

    public ResolveFailedException(List<Exception> exceptions) {
        super();
        this.exceptions = exceptions;
    }

    @Override
    public String toString() {
        if (exceptions != null || exceptions.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (Exception ex : exceptions) {
                if (ex.getLocalizedMessage() != null) {
                    sb.append(ex.getLocalizedMessage());
                }
            }
            if (sb.length() > 0) {
                return sb.toString();
            }
        }
        return super.toString();
    }
}
