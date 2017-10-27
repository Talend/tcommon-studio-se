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
package org.talend.metadata.managment.hive;

import org.talend.commons.exception.BusinessException;

/**
 * Created by Marvin Wang on Mar 25, 2013.
 */
public class HiveConnectionException extends BusinessException {

    /**
     * 
     */
    private static final long serialVersionUID = 7632407286326416971L;

    /**
     * DOC marvin HiveConnectionException constructor comment.
     */
    public HiveConnectionException() {
    }

    /**
     * DOC marvin HiveConnectionException constructor comment.
     * 
     * @param key
     * @param args
     */
    public HiveConnectionException(String key, Object... args) {
        super(key, args);
    }

    /**
     * DOC marvin HiveConnectionException constructor comment.
     * 
     * @param key
     */
    public HiveConnectionException(String key) {
        super(key);
    }

    /**
     * DOC marvin HiveConnectionException constructor comment.
     * 
     * @param cause
     */
    public HiveConnectionException(Throwable cause) {
        super(cause);
    }

    /**
     * DOC marvin HiveConnectionException constructor comment.
     * 
     * @param key
     * @param cause
     */
    public HiveConnectionException(String key, Throwable cause) {
        super(key, cause);
    }

    /**
     * DOC marvin HiveConnectionException constructor comment.
     * 
     * @param cause
     * @param key
     * @param args
     */
    public HiveConnectionException(Throwable cause, String key, Object... args) {
        super(cause, key, args);
    }

}
