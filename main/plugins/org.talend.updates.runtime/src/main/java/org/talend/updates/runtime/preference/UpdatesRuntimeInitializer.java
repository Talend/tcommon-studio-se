// ============================================================================
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
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
    }

}
