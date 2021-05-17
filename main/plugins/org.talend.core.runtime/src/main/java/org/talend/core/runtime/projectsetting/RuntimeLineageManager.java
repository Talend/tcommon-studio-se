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
package org.talend.core.runtime.projectsetting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static final String RUNTIMELINEAGE_OUTPUT_PATH = "-Druntime.lineage.outputpath="; //$NON-NLS-1$

    public static final String OUTPUT_PATH = "output.path"; //$NON-NLS-1$

    private List<String> selectedJobIds = new ArrayList<String>();

    private ProjectPreferenceManager prefManager = null;

    private boolean useRuntimeLineageAll = false;

    private String outputPath = null;

    public RuntimeLineageManager() {
        if (prefManager == null) {
            prefManager = new ProjectPreferenceManager(RUNTIMELINEAGE_RESOURCES, true);
        }
        useRuntimeLineageAll = prefManager.getBoolean(RUNTIMELINEAGE_ALL);
        outputPath = prefManager.getValue(OUTPUT_PATH);
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
                    while (sortedKeys.hasNext()) {
                        String key = (String) sortedKeys.next();
                        if (JOB_ID.equals(key)) {
                            jobId = jobJson.getString(key);
                        }
                    }
                    if (jobId != null) {
                        selectedJobIds.add(jobId);
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
                    if (!jobsJson.toString().contains(node.getId())) {
                        jobJson.put(JOB_ID, node.getId());
                        jobsJson.put(jobJson);
                    }
                }
            }
            prefManager.setValue(RUNTIMELINEAGE_ALL, all);
            prefManager.setValue(RUNTIMELINEAGE_SELECTED, jobsJson.toString());
            prefManager.setValue(OUTPUT_PATH, outputPath);
            prefManager.save();
        } catch (Exception e) {
            ExceptionHandler.process(e);
        }
    }

    public boolean isRuntimeLineageSetting(String id) {
        return selectedJobIds.contains(id);
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

    public boolean isUseRuntimeLineageAll() {
        return this.useRuntimeLineageAll;
    }

    public ProjectPreferenceManager getPrefManager() {
        return this.prefManager;
    }

    public List<String> getSelectedJobIds() {
        return this.selectedJobIds;
    }

    public void setSelectedJobIds(List<String> selectedJobIds) {
        this.selectedJobIds = selectedJobIds;
    }

    public String getOutputPath() {
        return this.outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
