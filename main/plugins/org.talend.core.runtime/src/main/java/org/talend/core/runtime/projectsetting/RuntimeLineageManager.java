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
package org.talend.core.runtime.projectsetting;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.talend.commons.exception.PersistenceException;
import org.talend.commons.ui.runtime.exception.ExceptionHandler;
import org.talend.commons.utils.workbench.resources.ResourceUtils;
import org.talend.repository.ProjectManager;
import org.talend.repository.model.RepositoryConstants;
import org.talend.repository.model.RepositoryNode;
import org.talend.utils.json.JSONObject;

import us.monoid.json.JSONArray;

/**
 * created by hcyi on Jul 27, 2020
 * Detailled comment
 *
 */
public class RuntimeLineageManager {

    public static final String RUNTIMELINEAGE_RESOURCES = "org.talend.runtimelineage"; //$NON-NLS-1$

    public static final String RUNTIMELINEAGE_ALL = "runtimelineage.all"; //$NON-NLS-1$

    public static final String RUNTIMELINEAGE_SELECTED = "runtimelineage.selected"; //$NON-NLS-1$

    public static final String JOB_ID = "id"; //$NON-NLS-1$

    public static final String JOB_VERSION = "version"; //$NON-NLS-1$

    private Map<String, String> dynamicFields = new HashMap<String, String>();

    private ProjectPreferenceManager prefManager = null;

    private boolean useRuntimeLineageAll = false;

    public RuntimeLineageManager() {
        if (prefManager == null) {
            prefManager = new ProjectPreferenceManager(RUNTIMELINEAGE_RESOURCES, true);
        }
        useRuntimeLineageAll = prefManager.getBoolean(RUNTIMELINEAGE_ALL);
    }

    public void load() {
        try {
            String jobsJsonStr = prefManager.getValue(RUNTIMELINEAGE_SELECTED);
            if (StringUtils.isNotBlank(jobsJsonStr)) {
                JSONArray jobsJsonArray = new JSONArray(jobsJsonStr);
                for (int i = 0; i < jobsJsonArray.length(); i++) {
                    Object jobJsonObj = jobsJsonArray.get(i);
                    JSONObject jobJson = new JSONObject(String.valueOf(jobJsonObj));
                    Iterator sortedKeys = jobJson.sortedKeys();
                    String jobId = null;
                    String jobVersion = null;
                    while (sortedKeys.hasNext()) {
                        String key = (String) sortedKeys.next();
                        if (JOB_ID.equals(key)) {
                            jobId = jobJson.getString(key);
                        } else if (JOB_VERSION.equals(key)) {
                            jobVersion = jobJson.getString(key);
                        }
                    }
                    if (jobId != null && jobVersion != null) {
                        dynamicFields.put(jobId, jobVersion);
                    }
                }
            }
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public void save(List<RepositoryNode> checkedObjects, boolean all) {
        try {
            JSONArray jobsJson = new JSONArray();
            if (!all) {
                for (RepositoryNode node : checkedObjects) {
                    JSONObject jobJson = new JSONObject();
                    jobJson.put(JOB_ID, node.getId());
                    jobJson.put(JOB_VERSION, node.getObject().getVersion());
                    jobsJson.put(jobJson);
                }
            }
            prefManager.setValue(RUNTIMELINEAGE_ALL, all);
            prefManager.setValue(RUNTIMELINEAGE_SELECTED, jobsJson.toString());
            prefManager.save();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public boolean isRuntimeLineageSetting(String id, String version) {
        return dynamicFields.containsKey(id) && dynamicFields.containsValue(version);
    }

    public boolean isRuntimeLineagePrefsExist() {
        try {
            IProject project = ResourceUtils.getProject(ProjectManager.getInstance().getCurrentProject());
            IFolder prefSettingFolder = ResourceUtils.getFolder(project, RepositoryConstants.SETTING_DIRECTORY, false);
            IFile presRuntimeLineageFile = prefSettingFolder.getFile(RUNTIMELINEAGE_RESOURCES + ".prefs"); //$NON-NLS-1$
            if (presRuntimeLineageFile.exists()) {
                return true;
            }
        } catch (PersistenceException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<String, String> getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(Map<String, String> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    public boolean isUseRuntimeLineageAll() {
        return this.useRuntimeLineageAll;
    }
}
