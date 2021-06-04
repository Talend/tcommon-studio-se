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
package org.talend.core.ui.token;

import java.util.Properties;

import org.talend.commons.runtime.service.ICollectDataService;

import us.monoid.json.JSONObject;

public class AMCUsageTokenCollector extends AbstractTokenCollector {

    @Override
    public JSONObject collect() throws Exception {
        Properties props = new Properties();
        ICollectDataService instance = ICollectDataService.getInstance("amc");
        if (instance != null) {
            props = instance.getCollectedData();
        }
        JSONObject finalToken = new JSONObject();

        for (Object key : props.keySet()) {
            finalToken.put((String) key, props.get(key));
        }
        return finalToken;
    }
}
