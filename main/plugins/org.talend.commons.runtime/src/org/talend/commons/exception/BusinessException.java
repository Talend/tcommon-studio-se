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
package org.talend.commons.exception;

/**
 * Defines business exception - Use or extends this class when a erros related to business occurs. Such problems should
 * appears in spec.<br/>
 * 
 * $Id$
 * 
 */
public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    private String key;

    private Object[] args;

    private String additonalMessage;

    public BusinessException() {
        super();
    }

    public BusinessException(String key, Object... args) {
        this(null, key, args);
    }

    public BusinessException(String key) {
        this(key, new Object[0]);
    }

    public BusinessException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public BusinessException(String key, Throwable cause) {
        this(cause, key, new Object[0]);
    }

    public BusinessException(Throwable cause, String key, Object... args) {
        super(key, cause);
        this.key = key;
        this.args = args;
    }

    /**
     * getArgs.
     * 
     * @return the arguments
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * getKey.
     * 
     * @return the keys
     */
    public String getKey() {
        return key;
    }

    public void setAdditonalMessage(String additonalMessage) {
        this.additonalMessage = additonalMessage;
    }

    public String getAdditonalMessage() {
        return additonalMessage;
    }

}
