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
package org.talend.core.service;

import java.util.Map;

import org.talend.core.IService;

/**
 * created by HHB on 2013-12-23 Detailled comment
 *
 */
public interface IMDMWebServiceHook extends IService {

    void preRequestSendingHook(Map<String, Object> requestContext, String userName);

    String buildStudioToken(String username);

    String getTokenKey();
}
