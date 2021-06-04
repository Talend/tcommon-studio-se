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

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.talend.commons.runtime.service.ICollectDataService;

import us.monoid.json.JSONObject;

public class AMCUsageTokenCollector extends AbstractTokenCollector {

    @Override
    public JSONObject collect() throws Exception {
        Properties props = new Properties();
        ICollectDataService instance = ICollectDataService.getInstance("amc");
        if (instance != null) {
            props = instance.getCollectedData();
        } else {
            IScopeContext[] contexts = new IScopeContext[] { InstanceScope.INSTANCE, ConfigurationScope.INSTANCE,
                    DefaultScope.INSTANCE };
            String plugin = "org.talend.amc";
            for (IScopeContext context : contexts) {
                IEclipsePreferences amc = context.getNode(plugin);
                if (amc != null) {
                    if (amc.getBoolean(ICollectDataService.AMC_FILE_TYPE_USED, false)) {
                        props.setProperty(ICollectDataService.AMC_PREVIEW_KEY, ICollectDataService.AMC_PREVIEW_FILEVALUE);
                    } else if (amc.getBoolean(ICollectDataService.AMC_DATABASE_TYPE_USED, false)) {
                        props.setProperty(ICollectDataService.AMC_PREVIEW_KEY, ICollectDataService.AMC_PREVIEW_DATABASEVALUE);
                    }
                    break;
                }
            }

        }
        JSONObject finalToken = new JSONObject();
        finalToken.put(ICollectDataService.AMC_PREVIEW_KEY, "<Empty>");
        for (Object key : props.keySet()) {
            finalToken.put((String) key, props.get(key));
        }
        return finalToken;
    }
}
