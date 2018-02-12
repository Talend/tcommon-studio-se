// ============================================================================
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
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

import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.model.general.Project;
import org.talend.repository.ProjectManager;

import us.monoid.json.JSONObject;

/**
 * created by cmeng on May 5, 2016
 * Detailled comment
 *
 */
public abstract class AutoSaveTokenCollector extends AbstractTokenCollector {

    abstract protected IPreferenceStore getPreferenceStore() throws Exception;

    abstract protected String getPreferenceKey() throws Exception;

    abstract protected JSONObject getTokenDetailsForCurrentProject() throws Exception;

    @Override
    public void priorCollect() throws Exception {
        // for all projects
        JSONObject allProjectRecords = null;

        IPreferenceStore preferenceStore = getPreferenceStore();
        String records = preferenceStore.getString(getPreferenceKey());
        try {
            // reset
            allProjectRecords = new JSONObject(records);
        } catch (Exception e) {
            // the value is not set, or is empty
            allProjectRecords = new JSONObject();
        }

        JSONObject currentProjectObject = getTokenDetailsForCurrentProject();
        if (currentProjectObject == null) {
            return;
        }

        Project currentProject = ProjectManager.getInstance().getCurrentProject();
        allProjectRecords.put(currentProject.getTechnicalLabel(), currentProjectObject);

        preferenceStore.setValue(getPreferenceKey(), allProjectRecords.toString());
    }

    @Override
    public JSONObject collect() throws Exception {
        JSONObject mergedData = new JSONObject();

        IPreferenceStore preferenceStore = getPreferenceStore();
        String records = preferenceStore.getString(getPreferenceKey());
        JSONObject allProjectRecords = null;
        try {
            allProjectRecords = new JSONObject(records);
        } catch (Exception e) {
            // the value is not set, or is empty
            allProjectRecords = new JSONObject();
        }
        Iterator<String> keys = allProjectRecords.keys();
        while (keys.hasNext()) {
            String projectName = keys.next();
            JSONObject object = (JSONObject) allProjectRecords.get(projectName);
            if (object != null) {
                TokenInforUtil.mergeJSON(object, mergedData);
            }
        }

        return getMergedToken(mergedData);
    }

    protected JSONObject getMergedToken(JSONObject mergedData) throws Exception {
        JSONObject finalToken = new JSONObject();
        finalToken.put(PROJECTS_REPOSITORY.getKey(), mergedData);
        return finalToken;
    }
}
