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
package org.talend.commons.exception;

public class InformException extends PersistenceException {

    /**
     * Show message of info level.
     */
    private static final long serialVersionUID = 4181370042549074156L;

    public InformException(String message) {
        super(message);
    }

}
