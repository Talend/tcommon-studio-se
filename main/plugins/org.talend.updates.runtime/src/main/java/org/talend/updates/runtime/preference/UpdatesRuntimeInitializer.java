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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class UpdatesRuntimeInitializer extends AbstractPreferenceInitializer {

    @SuppressWarnings("nls")
    @Override
    public void initializeDefaultPreferences() {
        UpdatesRuntimePreference preference = UpdatesRuntimePreference.getInstance();
        preference.setDefault(UpdatesRuntimePreferenceConstants.LAST_CHECK_UPDATE_TIME, "");
        preference.setDefault(UpdatesRuntimePreferenceConstants.CHECK_UPDATE_PER_DAYS, 2);
        preference.setDefault(UpdatesRuntimePreferenceConstants.AUTO_CHECK_UPDATE, true);
        preference.setDefault(UpdatesRuntimePreferenceConstants.AUTO_SHARE_FEATURES, true);
        preference.setDefault(UpdatesRuntimePreferenceConstants.SHOW_WARN_DIALOG_WHEN_INSTALLING_FEATURES, true);
    }

}
