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
package org.talend.core.repository.model.preview;

import org.talend.core.utils.TalendQuoteUtils;

/**
 * This is a Java Bean class which used for storing LDAP schema. <br/>
 * 
 * @author ftang, 09/18/2007
 * 
 */
public class LDAPSchemaBean {

    private String host;

    private String port;

    private String authMethod;

    private String user;

    private String passwd;

    private String multiValueSeparator;

    private String countLimit;

    private String timeOutLimit;

    private String baseDN;

    private String aliasDereferenring;

    private String referrals;

    private String filter;

    private boolean isAuthen;

    private String encryMethod;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return this.passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getMultiValueSeparator() {
        return this.multiValueSeparator;
    }

    public void setMultiValueSeparator(String multiValueSeparator) {
        this.multiValueSeparator = multiValueSeparator;
    }

    public String getLimit() {
        return this.countLimit;
    }

    public void setLimit(String limit) {
        this.countLimit = limit;
    }

    public String getAuthMethod() {
        return this.authMethod;
    }

    public void setAuthMethod(String authMethod) {
        this.authMethod = authMethod;
    }

    public String getCountLimit() {
        return this.countLimit;
    }

    public void setCountLimit(String countLimit) {
        this.countLimit = countLimit;
    }

    public String getTimeOutLimit() {
        return this.timeOutLimit;
    }

    public void setTimeOutLimit(String timeOutLimit) {
        this.timeOutLimit = timeOutLimit;
    }

    public String getBaseDN() {
        return this.baseDN;
    }

    public void setBaseDN(String baseDN) {
        this.baseDN = baseDN;
    }

    public String getAliasDereferenring() {
        return this.aliasDereferenring;
    }

    public void setAliasDereferenring(String aliasDereferenring) {
        this.aliasDereferenring = aliasDereferenring;
    }

    public String getReferrals() {
        return this.referrals;
    }

    public void setReferrals(String referrals) {
        this.referrals = referrals;
    }

    public String getFilter() {
        return this.filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public boolean isAuthen() {
        return this.isAuthen;
    }

    public void setAuthen(boolean isAuthen) {
        this.isAuthen = isAuthen;
    }

    public String getEncryMethod() {
        return this.encryMethod;
    }

    public void setEncryMethod(String encryMethod) {
        String ldaps = TalendQuoteUtils.addQuotes("LDAPS(SSL)"); //$NON-NLS-1$

        // Repository value is different as UI's.
        if (encryMethod.equals(ldaps)) {
            this.encryMethod = "LDAPS"; //$NON-NLS-1$
        } else {
            this.encryMethod = encryMethod;
        }
    }

}
