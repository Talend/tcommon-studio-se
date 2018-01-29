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
package org.talend.repository.ui.wizards.metadata.connection.ldap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.talend.core.runtime.CoreRuntimePlugin;

/**
 * The HistoryUtils are used to save and load the history of input fields.
 * 
 * @author ftang, 18/09/2007
 */
public class HistoryUtils {

    /**
     * Saves the the given value under the given key in the dialog settings.
     * 
     * @param key the key
     * @param value the value
     */
    public static void save(String key, String value) {
        // get current history
        String[] history = load(key);
        List<String> list = new ArrayList<String>(Arrays.asList(history));

        // add new value or move to first position
        if (list.contains(value)) {
            list.remove(value);
        }
        list.add(0, value);

        // check history size
        while (list.size() > 20) {
            list.remove(list.size() - 1);
        }

        // save
        history = list.toArray(new String[list.size()]);
        CoreRuntimePlugin.getInstance().getRepositoryService().getDialogSettings().put(key, history);

    }

    /**
     * Loads the value of the given key from the dialog settings
     * 
     * @param key the key
     * @return the value
     */
    public static String[] load(String key) {
        String[] history = CoreRuntimePlugin.getInstance().getRepositoryService().getDialogSettings().getArray(key);
        if (history == null) {
            history = new String[0];
        }
        return history;
    }

}
