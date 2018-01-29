// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.prefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPersistentPreferenceStore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.utils.Hex;
import org.talend.core.model.general.ConnectionBean;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.utils.json.JSONArray;
import org.talend.utils.json.JSONException;
import org.talend.utils.json.JSONObject;

/**
 * Used to store connections / users for the login dialog <br/>
 * 
 * $Id: PreferenceManipulator.java 44184 2010-06-17 04:01:35Z cli $
 * 
 */
public final class PreferenceManipulator implements ITalendCorePrefConstants {

    private static final String JSON_CONNECTIONS = "Connections.new";

    private static final String PREF_DELIMITER = "|"; //$NON-NLS-1$

    private static final String LOCAL_MODE = "local";

    /** The preference store manipulated. */
    private IPreferenceStore store;

    private String PROJECT_BRANCH_MANAGEMENT = "localBranchManagement";

    /**
     * Constructs a new PreferenceManipulator.
     * 
     * @param store The preference store manipulated.
     * @deprecated The preferences are only used to store specific connections, so will be forced to a specific
     * preference store, no matter the parameter
     */
    public PreferenceManipulator(IPreferenceStore store) {
        super();

        this.store = PlatformUI.getPreferenceStore();
    }

    /**
     * Constructs a new PreferenceManipulator.
     * 
     * @param store The preference store manipulated.
     */
    public PreferenceManipulator() {
        super();

        this.store = PlatformUI.getPreferenceStore();
    }

    /**
     * Read a string array in the preference store.
     * 
     * @param prefName Name of the preference to be read.
     * @return an array of strings.
     */
    private String[] readStringArray(final String prefName) {
        List<String> strings = readStringList(prefName);

        String[] array = new String[strings.size()];
        array = strings.toArray(array);
        return array;
    }

    private JSONArray readJsonArray(final String prefName) {
        String prefs = store.getString(prefName);

        if (prefs != null) {
            try {
                return new JSONArray(prefs);
            } catch (JSONException e) {
                //
            }
        }
        return new JSONArray();
    }

    private List<String> readStringList(final String prefName) {
        List<String> strings = new ArrayList<String>();
        String prefs = store.getString(prefName);
        if (prefs != null) {
            StringTokenizer st = new StringTokenizer(prefs, PREF_DELIMITER);
            while (st.hasMoreTokens()) {
                strings.add(st.nextToken());
            }
        }
        return strings;
    }

    /**
     * Save a string array in the preference store.
     * 
     * @param prefArray Preferences to be saved.
     * @param prefName Name of the preference to be saved.
     */
    private void saveStringArray(final String[] prefArray, String prefName) {
        StringBuffer prefs = new StringBuffer(256);
        for (int i = 0; i < prefArray.length; i++) {
            prefs.append(prefArray[i]);
            if (i < prefArray.length - 1) {
                prefs.append(PREF_DELIMITER);
            }
        }
        store.setValue(prefName, prefs.toString());
        save();
    }

    private void saveJsonArray(final JSONArray prefArray, String prefName) {
        store.setValue(prefName, prefArray.toString());
        save();
    }

    /**
     * Add a string to an array preference.
     * 
     * @param s String to be added.
     * @param prefName Name of the preference to add.
     */
    private void addStringToArray(String s, String prefName) {
        String[] array = readStringArray(prefName);
        List<String> l = new ArrayList<String>();
        l.addAll(Arrays.asList(array));

        if (!l.contains(s)) {
            l.add(s);

            array = new String[l.size()];
            array = l.toArray(array);
            saveStringArray(array, prefName);
        }
    }

    public List<ConnectionBean> readConnections() {
        List<ConnectionBean> toReturn = new ArrayList<ConnectionBean>();
        for (String currentConnectionToLoad : readStringArray(CONNECTIONS)) {
            toReturn.add(ConnectionBean.writeFromString(currentConnectionToLoad));
        }
        // store.setValue(CONNECTIONS, "");

        JSONArray jsonArray = readJsonArray(JSON_CONNECTIONS);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object object = jsonArray.get(i);
                if (object instanceof JSONObject) {
                    toReturn.add(ConnectionBean.writeFromJSON((JSONObject) object));
                }
            } catch (JSONException e) {
                //
            }
        }
        return toReturn;
    }

    public void saveConnections(List<ConnectionBean> cons) {
        JSONArray array = new JSONArray();
        for (ConnectionBean currentConnection : cons) {
            array.put(currentConnection.getConDetails());
        }
        // clear the old value
        store.setValue(CONNECTIONS, "");
        saveJsonArray(array, JSON_CONNECTIONS);
    }

    public void addConnection(ConnectionBean con) {
        JSONArray array = new JSONArray();
        array.put(con.getConDetails());
        // clear the old value
        store.setValue(CONNECTIONS, "");
        saveJsonArray(array, JSON_CONNECTIONS);
    }

    /**
     * Read all known users in the preference store.
     * 
     * @return all known users.
     */
    public String[] readUsers() {
        return readStringArray(USERS);
    }

    /**
     * Save all known users in the preference store.
     * 
     * @param users all known users.
     */
    public void saveUsers(String[] users) {
        saveStringArray(users, USERS);
    }

    public void addUser(String user) {
        addStringToArray(user, USERS);
    }

    public String getLastConnection() {
        return store.getString(LAST_USED_CONNECTION);
    }

    public void setLastConnection(String connection) {
        store.setValue(LAST_USED_CONNECTION, connection);
        save();
    }

    public String getLastProject() {
        return store.getString(LAST_USED_PROJECT);
    }

    public void setLastProject(String project) {
        store.setValue(LAST_USED_PROJECT, project);
        save();
    }

    public String getLastSVNBranch(String projectUrl, String projectName) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName).getBytes());
        String string = store.getString(hexKey);
        // just unified for null
        if (string != null && "".equals(string.trim())) { //$NON-NLS-1$
            return null;
        }
        return string;
    }

    public void setLastSVNBranch(String projectUrl, String projectName, String branch) {
        if (branch != null && branch.startsWith("/")) { //$NON-NLS-1$
            branch = branch.substring(1, branch.length());
        }
        String hexKey = Hex.encodeHexString((projectUrl + projectName).getBytes());
        store.setValue(hexKey, branch);
        save();
    }

    @Deprecated
    public int getLastLogonMode(String projectUrl, String projectName, String branch) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName + branch).getBytes());
        int mode = store.getInt(hexKey);
        return mode;
    }

    @Deprecated
    public void setLastLogonMode(String projectUrl, String projectName, String branch, int mode) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName + branch).getBytes());
        store.setValue(hexKey, mode);
        save();
    }

    public String getLastUser() {
        return store.getString(LAST_USED_USER);
    }

    public void setLastUser(String user) {
        store.setValue(LAST_USED_USER, user);
        save();
    }

    public List<String> readWorkspaceTasksDone() {
        return readStringList(WORKSPACE_TASKS_DONE);
    }

    public void addWorkspaceTaskDone(String task) {
        addStringToArray(task, WORKSPACE_TASKS_DONE);
    }

    public boolean getBoolean(String name) {
        return this.store.getBoolean(name);
    }

    public void setValue(String name, boolean value) {
        this.store.setValue(name, value);
    }

    /**
     * 
     * DOC ggu Comment method "save".
     */
    public void save() {
        if (store != null && store instanceof IPersistentPreferenceStore && store.needsSaving()) {
            try {
                ((IPersistentPreferenceStore) store).save();
            } catch (IOException e) {
                //
            }
        }
    }

    public void setNewCreateBranchObjectId(String projectUrl, String projectName, String branch, String id) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName + LOCAL_MODE + branch).getBytes());
        store.setValue(hexKey, id);
        save();
    }

    public String getNewCreateBranchObjectId(String projectUrl, String projectName, String branch) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName + LOCAL_MODE + branch).getBytes());
        return store.getString(hexKey);
    }

    public JSONObject getLogonLocalBranchStatus(String projectUrl, String projectName) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName + PROJECT_BRANCH_MANAGEMENT).getBytes());
        String jsonString = store.getString(hexKey);
        if (jsonString == null || "".equals(jsonString))
            return null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ExceptionHandler.process(e);
        }
        return null;
    }

    public void setLogonLocalBranchStatus(String projectUrl, String projectName, JSONObject json) {
        String hexKey = Hex.encodeHexString((projectUrl + projectName + PROJECT_BRANCH_MANAGEMENT).getBytes());
        store.setValue(hexKey, json.toString());
        save();
    }
    
    public void setAllLocalRefBranchSetting(JSONObject allBranchSetting) {
        if (allBranchSetting == null) {
            allBranchSetting = new JSONObject();
        }
        this.store.setValue(IRepositoryPrefConstants.REF_PROJECT_BRANCH_SETTING, allBranchSetting.toString());

        save();
    }

    public JSONObject getAllLocalRefBranchSetting() {

        String strValue = this.store.getString(IRepositoryPrefConstants.REF_PROJECT_BRANCH_SETTING);
        JSONObject allBranchSetting = null;
        try {
            allBranchSetting = new JSONObject(strValue);
        } catch (Exception e) {
            allBranchSetting = new JSONObject();
        }
        return allBranchSetting;
    }
}
