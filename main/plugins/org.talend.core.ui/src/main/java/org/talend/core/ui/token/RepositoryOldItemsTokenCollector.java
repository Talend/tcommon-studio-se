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

import java.io.IOException;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.talend.core.repository.CoreRepositoryPlugin;

import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;

public class RepositoryOldItemsTokenCollector extends AbstractTokenCollector {

    protected static final String KEY_REPOSITORY_OLDITEM = "repositroy.olditems";

    protected static final String KEY_OLDITEM = "old.items";

    public static void record(String label) {
        if (label == null) {
            return;
        }

        try {
            final IPreferenceStore preferenceStore = CoreRepositoryPlugin.getDefault().getPreferenceStore();
            JSONObject records = getRecords();
            if (records.has(KEY_OLDITEM)) {
                JSONObject jsonObject = records.getJSONObject(KEY_OLDITEM);
                if (jsonObject.has(label)) {
                    int num = jsonObject.getInt(label);
                    jsonObject.put(label, num + 1);
                } else {
                    jsonObject.put(label, 1);
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(label, 1);
                records.put(KEY_OLDITEM, jsonObject);
            }
            preferenceStore.setValue(KEY_REPOSITORY_OLDITEM, records.toString());
            if (preferenceStore instanceof ScopedPreferenceStore) {
                try {
                    ((ScopedPreferenceStore) preferenceStore).save();
                } catch (IOException e) {
                }
            }
        } catch (JSONException e) {
        }
    }

    private static JSONObject getRecords() {
        final IPreferenceStore preferenceStore = CoreRepositoryPlugin.getDefault().getPreferenceStore();
        String records = preferenceStore.getString(KEY_REPOSITORY_OLDITEM);
        JSONObject allRecords;
        try {
            allRecords = new JSONObject(records);
        } catch (Exception e) {
            // the value is not set, or is empty
            allRecords = new JSONObject();
        }
        
        return allRecords;
    }

    @Override
    public JSONObject collect() throws Exception {
        JSONObject finalToken = new JSONObject();

        JSONObject records = getRecords();

        finalToken.put(PROJECTS_REPOSITORY.getKey(), records);
        return finalToken;
    }
}
