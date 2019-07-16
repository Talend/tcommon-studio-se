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
 * @param <T> the type of object returned in method.
 */
public class TypedReturnCode<T> extends ReturnCode {

    private T object;

    /**
     * ReturnCode constructor.
     *
     * @param mess the message to return
     * @param retCode the boolean to return
     */
    public TypedReturnCode(String mess, boolean retCode) {
        super(mess, retCode);
    }

    /**
     * ReturnCode default constructor. Message is set to null and return code is true.
     */
    public TypedReturnCode() {
        super();
    }

    /**
     * ReturnCode constructor.
     *
     * @param isOk the boolean to return
     */
    public TypedReturnCode(boolean isOk) {
        super(isOk);
    }

    /**
     * Method "getObject".
     *
     * @return the object when everything is ok
     */
    public T getObject() {
        return this.object;
    }

    /**
     * Method "setObject".
     *
     * @param obj the object to return.
     */
    public void setObject(T obj) {
        this.object = obj;
    }

    public void setReturnCode(String mess, boolean retCode, T obj) {
        super.setReturnCode(mess, retCode);
        this.setObject(obj);
    }
}
