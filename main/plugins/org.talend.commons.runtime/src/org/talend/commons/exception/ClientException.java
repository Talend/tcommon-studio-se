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

public class ClientException extends PersistenceException {
    
    private Integer httpCode;

    public ClientException(String message) {
        super(message);
    }

    public ClientException(Integer httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(Integer httpCode, String message, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public ClientException(Throwable cause) {
        super(cause);
    }

    public ClientException(Integer httpCode, Throwable cause) {
        super(cause);
        this.httpCode = httpCode;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    @Override
    public String toString() {
        return getLocalizedMessage();
    }
}
