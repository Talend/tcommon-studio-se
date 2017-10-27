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
package org.talend.core.exception;

import java.sql.SQLException;

/**
 * created by ycbai on 2014年11月17日 Detailled comment
 *
 */
public class WarningSQLException extends SQLException {

    private static final long serialVersionUID = 6001838275838161372L;

    public WarningSQLException(String reason) {
        super(reason);
    }

    public WarningSQLException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public WarningSQLException(Throwable cause) {
        super(cause);
    }

}
