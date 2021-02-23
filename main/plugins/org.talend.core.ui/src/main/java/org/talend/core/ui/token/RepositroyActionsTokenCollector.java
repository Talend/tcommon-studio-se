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
package org.talend.core.ui.token;

import java.util.Map.Entry;
import java.util.Properties;

import org.talend.commons.utils.time.PropertiesFileUtil;
import org.talend.repository.token.RepositoryActionLogger;

import us.monoid.json.JSONObject;

/**
 * DOC sbliu  class global comment. Detailled comment
 */
public class RepositroyActionsTokenCollector extends AbstractTokenCollector {
    @Override
    public JSONObject collect() throws Exception {
        JSONObject tokenStudioObject = new JSONObject();
        //
        JSONObject jsonObjectIOInfo = new JSONObject();
        Properties props = PropertiesFileUtil.read(RepositoryActionLogger.getRecordingFile(), false);
        if(!props.isEmpty()) {
            for(Entry<Object, Object> entry: props.entrySet()) {
                jsonObjectIOInfo.put((String)entry.getKey(), entry.getValue());
            }
        }
        
        tokenStudioObject.put("actions", jsonObjectIOInfo);
        
        return tokenStudioObject;
    }
}
