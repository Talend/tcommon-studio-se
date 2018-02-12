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
package org.talend.core.ui.workspace;

import java.io.File;
import java.net.URL;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * This class stores the information behind the "Launch Workspace" dialog. The class is able to read and write itself to
 * a well known configuration file.
 */
public class ChooseWorkspaceData {

    private static Logger log = Logger.getLogger(ChooseWorkspaceData.class);

    /**
     * 
     */
    private static final String RECENT_WORKSPACES_PREF = "RECENT_WORKSPACES"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String MAX_RECENT_WORKSPACE_PREF = "MAX_RECENT_WORKSPACES"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String SHOW_WORKSPACE_SELECTION_DIALOG_PREF = "SHOW_WORKSPACE_SELECTION_DIALOG"; //$NON-NLS-1$

    /**
     * 
     */
    public static final String ORG_TALEND_WORKSPACE_PREF_NODE = "org.eclipse.ui.ide"; //$NON-NLS-1$

    /**
     * 
     */
    private static final String WORSPACE_PATH_SEPARATOR = "\n"; //$NON-NLS-1$

    /**
     * The default max length of the recent workspace mru list.
     */
    private static final int RECENT_MAX_LENGTH = 5;

    private boolean showDialog = true;

    private String initialDefault;

    private String selection;

    private String[] recentWorkspaces;

    /**
     * Creates a new instance, loading persistent data if its found.
     */
    public ChooseWorkspaceData(String initialDefault) {
        readPersistedData();
        setInitialDefault(initialDefault);
    }

    /**
     * Creates a new instance, loading persistent data if its found.
     */
    public ChooseWorkspaceData(URL instanceUrl) {
        readPersistedData();
        if (instanceUrl != null) {
            setInitialDefault(new File(instanceUrl.getFile()).toString());
        }
    }

    /**
     * Return the folder to be used as a default if no other information exists. Does not return null.
     */
    public String getInitialDefault() {
        if (initialDefault == null) {
            setInitialDefault(System.getProperty("user.dir") //$NON-NLS-1$
                    + File.separator + "workspace"); //$NON-NLS-1$
        }
        return initialDefault;
    }

    /**
     * Set this data's initialDefault parameter to a properly formatted version of the argument directory string. The
     * proper format is to the platform appropriate separator character without meaningless leading or trailing
     * separator characters.
     */
    private void setInitialDefault(String dir) {
        if (dir == null || dir.length() <= 0) {
            initialDefault = null;
            return;
        }

        String defaultDir = new Path(dir).toOSString();
        while (defaultDir.charAt(defaultDir.length() - 1) == File.separatorChar) {
            defaultDir = defaultDir.substring(0, defaultDir.length() - 1);
        }
        initialDefault = defaultDir;
    }

    /**
     * Return the currently selected workspace or null if nothing is selected.
     */
    public String getSelection() {
        return selection;
    }

    /**
     * Return the currently selected workspace or null if nothing is selected.
     */
    public boolean getShowDialog() {
        return showDialog;
    }

    /**
     * Return an array of recent workspaces sorted with the most recently used at the start.
     */
    public String[] getRecentWorkspaces() {
        return recentWorkspaces;
    }

    /**
     * The argument workspace has been selected, update the receiver. Does not persist the new values.
     */
    public void workspaceSelected(String dir) {
        // this just stores the selection, it is not inserted and persisted
        // until the workspace is actually selected
        selection = dir;
    }

    /**
     * Toggle value of the showDialog persistent setting.
     */
    public void toggleShowDialog() {
        setShowDialog(!getShowDialog());
    }

    /**
     * Sets the list of recent workspaces.
     */
    public void setRecentWorkspaces(String[] workspaces) {
        if (workspaces == null) {
            recentWorkspaces = new String[0];
        } else {
            recentWorkspaces = workspaces;
        }
    }

    /**
     * Update the persistent store. Call this function after the currently selected value has been found to be ok.
     */
    public void writePersistedData() {
        // 1. get config pref node
        Preferences node = new ConfigurationScope().getNode(ORG_TALEND_WORKSPACE_PREF_NODE);

        // 2. get value for showDialog
        node.putBoolean(SHOW_WORKSPACE_SELECTION_DIALOG_PREF, getShowDialog());

        // 3. use value of numRecent to create proper length array
        node.putInt(MAX_RECENT_WORKSPACE_PREF, recentWorkspaces.length);

        // move the new selection to the front of the list
        if (selection != null) {
            File newFolder = new File(selection);
            String oldEntry = recentWorkspaces[0];
            recentWorkspaces[0] = selection;
            for (int i = 1; i < recentWorkspaces.length && oldEntry != null; ++i) {
                File oldFolder = new File(oldEntry);
                // If selection represents a file location we already have, don't store it
                if (newFolder.compareTo(oldFolder) == 0) {
                    break;
                }
                String tmp = recentWorkspaces[i];
                recentWorkspaces[i] = oldEntry;
                oldEntry = tmp;
            }
        }

        // 4. store values of recent workspaces into array
        String encodedRecentWorkspaces = encodeStoredWorkspacePaths(recentWorkspaces);
        node.put(RECENT_WORKSPACES_PREF, encodedRecentWorkspaces);

        // 5. store the node
        try {
            node.flush();
        } catch (BackingStoreException e) {
            log.error("failed to store workspace location in preferences :", e); //$NON-NLS-1$
        }
    }

    /**
     * Look in the config area preference store for the list of recently used workspaces.
     */
    public void readPersistedData() {
        IPreferenceStore store = new ScopedPreferenceStore(new ConfigurationScope(), ORG_TALEND_WORKSPACE_PREF_NODE);

        // 2. get value for showDialog
        setShowDialog(store.getBoolean(SHOW_WORKSPACE_SELECTION_DIALOG_PREF));

        // 3. use value of numRecent to create proper length array
        int max = store.getInt(MAX_RECENT_WORKSPACE_PREF);
        max = Math.max(max, RECENT_MAX_LENGTH);

        // 4. load values of recent workspaces into array
        String workspacePathPref = store.getString(RECENT_WORKSPACES_PREF);
        recentWorkspaces = decodeStoredWorkspacePaths(max, workspacePathPref);
    }

    /**
     * The the list of recent workspaces must be stored as a string in the preference node.
     */
    private static String encodeStoredWorkspacePaths(String[] recent) {
        StringBuffer buff = new StringBuffer();

        String path = null;
        for (String element : recent) {
            if (element == null) {
                break;
            }

            if (path != null) {
                buff.append(WORSPACE_PATH_SEPARATOR);
            }

            path = element;
            buff.append(path);
        }

        return buff.toString();
    }

    /**
     * The the preference for recent workspaces must be converted from the storage string into an array.
     */
    private static String[] decodeStoredWorkspacePaths(int max, String prefValue) {
        String[] paths = new String[max];
        if (prefValue == null || prefValue.length() <= 0) {
            return paths;
        }
        StringTokenizer tokenizer = new StringTokenizer(prefValue, WORSPACE_PATH_SEPARATOR);
        for (int i = 0; i < paths.length && tokenizer.hasMoreTokens(); ++i) {
            paths[i] = tokenizer.nextToken();
        }

        return paths;
    }

    /**
     * Sets the showDialog.
     * 
     * @param showDialog the showDialog to set
     */
    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

}
