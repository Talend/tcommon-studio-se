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

import org.talend.commons.exception.PersistenceException;

/**
 * DOC zshen  class global comment. Detailled comment
 */
public class TalendInternalPersistenceException extends PersistenceException {


    private static final long serialVersionUID = 6242818542268554273L;

    public TalendInternalPersistenceException(String msg) {
        super(msg);
    }
}
