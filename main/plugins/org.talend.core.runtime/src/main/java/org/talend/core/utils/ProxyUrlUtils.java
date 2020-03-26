// ============================================================================
//
// Copyright (C) 2006-2020 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.utils;

import org.talend.core.nexus.TalendLibsServerManager;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;

public class ProxyUrlUtils {

    private static ProjectPreferenceManager prefManager = new ProjectPreferenceManager("org.talend.proxy.nexus", true);

    public static String getNexusUrl() {
        String sysPropUrl = System.getProperty("nexus.proxy.url");
        // the url in system property goes first
        if (sysPropUrl == null) {
            String prefProxyUrl = null;
            boolean enableProxy = prefManager.getBoolean(TalendLibsServerManager.ENABLE_PROXY_SETTING);
            if (enableProxy) {
                prefProxyUrl = prefManager.getValue(TalendLibsServerManager.NEXUS_PROXY_URL);
            }
            return prefProxyUrl;
        } else {
            return sysPropUrl;
        }
    }
}
