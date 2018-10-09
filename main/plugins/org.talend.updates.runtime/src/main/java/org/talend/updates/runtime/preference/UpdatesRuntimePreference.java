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
package org.talend.updates.runtime.preference;

import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.runtime.projectsetting.ProjectPreferenceManager;
import org.talend.updates.runtime.Constants;
import org.talend.updates.runtime.UpdatesRuntimePlugin;

/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class UpdatesRuntimePreference {

    private IPreferenceStore preferenceStore;

    private static UpdatesRuntimePreference instance;

    public static UpdatesRuntimePreference getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (UpdatesRuntimePreference.class) {
            if (instance == null) {
                instance = new UpdatesRuntimePreference();
            }
        }
        return instance;
    }

    private UpdatesRuntimePreference() {
        preferenceStore = UpdatesRuntimePlugin.getDefault().getPreferenceStore();
    }

    public ProjectPreferenceManager createProjectPreferenceManager() {
        return new ProjectPreferenceManager(Constants.PLUGIN_ID);
    }

    public void setValue(String key, String value) {
        setValue(key, value, false);
    }

    public void setDefault(String key, String value) {
        setValue(key, value, true);
    }

    public void setValue(String key, String value, boolean setDefault) {
        if (setDefault) {
            preferenceStore.setDefault(key, value);
        } else {
            preferenceStore.setValue(key, value);
        }
    }

    public String getValue(String key) {
        return getValue(key, false);
    }

    public String getDefault(String key) {
        return getValue(key, true);
    }

    public String getValue(String key, boolean fromDefault) {
        if (fromDefault) {
            return preferenceStore.getDefaultString(key);
        } else {
            return preferenceStore.getString(key);
        }
    }

    public boolean getBoolean(String key, boolean fromDefault) {
        if (fromDefault) {
            return preferenceStore.getDefaultBoolean(key);
        } else {
            return preferenceStore.getBoolean(key);
        }
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getDefaultBoolean(String key) {
        return getBoolean(key, true);
    }

    public void setBoolean(String key, boolean value, boolean setDefault) {
        if (setDefault) {
            preferenceStore.setDefault(key, value);
        } else {
            preferenceStore.setValue(key, value);
        }
    }

    public void setValue(String key, boolean value) {
        setBoolean(key, value, false);
    }

    public void setDefault(String key, boolean value) {
        setBoolean(key, value, true);
    }

    public int getInt(String key, boolean fromDefault) {
        if (fromDefault) {
            return preferenceStore.getDefaultInt(key);
        } else {
            return preferenceStore.getInt(key);
        }
    }

    public int getInt(String key) {
        return getInt(key, false);
    }

    public int getDefaultInt(String key) {
        return getInt(key, true);
    }

    public void setInt(String key, int value, boolean setDefault) {
        if (setDefault) {
            preferenceStore.setDefault(key, value);
        } else {
            preferenceStore.setValue(key, value);
        }
    }

    public void setValue(String key, int value) {
        setInt(key, value, false);
    }

    public void setDefault(String key, int value) {
        setInt(key, value, true);
    }

    public Date getDate(String key) throws Exception {
        String str = getValue(key);
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return DateFormat.getInstance().parse(str);
    }

    public Date getDefaultDate(String key) throws Exception {
        String str = getDefault(key);
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return DateFormat.getInstance().parse(str);
    }

    public void setDate(String key, Date date) {
        String dateStr = null;
        if (date != null) {
            dateStr = DateFormat.getInstance().format(date);
        }
        setValue(key, dateStr, false);
    }

    public void setDefault(String key, Date date) {
        String dateStr = null;
        if (date != null) {
            dateStr = DateFormat.getInstance().format(date);
        }
        setValue(key, dateStr, true);
    }
}
