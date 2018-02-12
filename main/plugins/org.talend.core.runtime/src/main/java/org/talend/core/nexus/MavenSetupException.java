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
package org.talend.core.nexus;

/**
 * created by Talend on 2015年5月4日 Detailled comment
 *
 */
public class MavenSetupException extends Exception {

    private static final long serialVersionUID = 1L;

    public MavenSetupException(String message) {
        super(message);
    }

    public MavenSetupException(String message, Throwable e) {
        super(message, e);
    }
}
