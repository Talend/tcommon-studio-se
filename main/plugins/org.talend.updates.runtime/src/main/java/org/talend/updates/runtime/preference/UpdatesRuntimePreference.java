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

    public Date getDate(String key) throws Exception {
        String str = getValue(key);
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
        setValue(key, dateStr);
    }
}
