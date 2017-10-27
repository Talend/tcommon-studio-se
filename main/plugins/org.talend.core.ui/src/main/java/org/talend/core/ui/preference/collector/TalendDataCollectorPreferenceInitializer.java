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
package org.talend.core.ui.preference.collector;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.prefs.ITalendCorePrefConstants;
import org.talend.core.ui.CoreUIPlugin;

/**
 * ggu class global comment. Detailled comment
 */
public class TalendDataCollectorPreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore preferenceStore = CoreUIPlugin.getDefault().getPreferenceStore();
        preferenceStore.setDefault(ITalendCorePrefConstants.DATA_COLLECTOR_ENABLED, true);
        preferenceStore.setDefault(ITalendCorePrefConstants.DATA_COLLECTOR_UPLOAD_PERIOD, 10);

    }

}
