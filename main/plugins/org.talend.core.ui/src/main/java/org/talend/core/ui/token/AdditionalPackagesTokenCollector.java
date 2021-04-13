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

import org.talend.commons.utils.time.PropertiesCollectorUtil;

import us.monoid.json.JSONObject;

public class AdditionalPackagesTokenCollector extends AbstractTokenCollector {

    public AdditionalPackagesTokenCollector() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public JSONObject collect() throws Exception {
        String records = PropertiesCollectorUtil.getAdditionalPackageRecording();

        JSONObject allRecords;
        try {
            allRecords = new JSONObject(records);
        } catch (Exception e) {
            // the value is not set, or is empty
            allRecords = new JSONObject();
            allRecords.put(PropertiesCollectorUtil.getAdditionalPackagePreferenceNode(), new JSONObject());
        }

        return allRecords;
    }
}
