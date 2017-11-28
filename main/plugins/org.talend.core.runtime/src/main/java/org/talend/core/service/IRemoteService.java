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
package org.talend.core.service;

import org.talend.commons.exception.LoginException;
import org.talend.commons.exception.PersistenceException;
import org.talend.core.IService;
import org.talend.core.nexus.NexusServerBean;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * DOC hywang class global comment. Detailled comment
 */
public interface IRemoteService extends IService {

    public JSONObject getLicenseKey(String user, String password, String url, String projectLabel) throws PersistenceException,
            LoginException;

    public NexusServerBean getUpdateRepositoryUrl(String user, String password, String url) throws PersistenceException,
            LoginException;

    public NexusServerBean getLibNexusServer(String user, String password, String url) throws PersistenceException,
            LoginException, JSONException;

    boolean isAuthorized(String value);
}
