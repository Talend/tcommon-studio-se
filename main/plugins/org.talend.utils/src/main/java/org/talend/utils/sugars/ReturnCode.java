// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.utils.sugars;

/**
 * @author scorreia
 *
 * This class can be used when one wants to return more than a simple boolean in a method.
 */
public class ReturnCode {

    private Boolean ok;

    private String message;

    /**
     * ReturnCode constructor.
     *
     * @param mess the message to return
     * @param retCode the boolean to return
     */
    public ReturnCode(String mess, Boolean retCode) {
        this.message = mess;
        this.ok = retCode;
    }

    /**
     * ReturnCode default constructor. Message is set to null and return code is true.
     */
    public ReturnCode() {
        this.message = null;
        this.ok = true;
    }

    /**
     * ReturnCode constructor. Message is set to null.
     *
     * @param isOk the return boolean by default.
     */
    public ReturnCode(Boolean isOk) {
        this.message = null;
        this.ok = isOk;
    }

    /**
     * Method "setReturnCode" set the message and return code in one call.
     *
     * @param mess
     * @param retCode
     */
    public void setReturnCode(String mess, Boolean retCode) {
        this.setMessage(mess);
        this.setOk(retCode);
    }

    /**
     * Getter for ok.
     *
     * @return the ok
     */
    public Boolean isOk() {
        return this.ok;
    }

    /**
     * Sets the ok.
     *
     * @param ok the ok to set
     */
    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    /**
     * Getter for message.
     *
     * @return the message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the message.
     *
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return ok ? "OK" : "KO: " + message;
    }

}
