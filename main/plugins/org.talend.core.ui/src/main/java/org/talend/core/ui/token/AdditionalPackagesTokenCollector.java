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

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.repository.CoreRepositoryPlugin;

import us.monoid.json.JSONObject;

public class AdditionalPackagesTokenCollector extends AbstractTokenCollector {

    // recorded in org.talend.updates.runtime/.../UpdateStudioWizard.java
    private static final String ROOT_NODE = "additional_packages_records";

    private static final String ADDITIONAL_PACKAGES = "AdditionalPackages";

    public AdditionalPackagesTokenCollector() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public JSONObject collect() throws Exception {
        final IPreferenceStore preferenceStore = CoreRepositoryPlugin.getDefault().getPreferenceStore();
        String records = preferenceStore.getString(ROOT_NODE);
        JSONObject allRecords;
        try {
            allRecords = new JSONObject(records);
        } catch (Exception e) {
            // the value is not set, or is empty
            allRecords = new JSONObject();
            allRecords.put(ADDITIONAL_PACKAGES, new JSONObject());
        }

        return allRecords;
    }

}
