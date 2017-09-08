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
package org.talend.designer.core;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.talend.core.IService;

/**
 * created by hcyi on Aug 17, 2016 Detailled comment
 *
 */
public interface IMDMService extends IService {

    public SSLContext getSSLContext();

    public HostnameVerifier getDefaultHostnameVerifier();

}
