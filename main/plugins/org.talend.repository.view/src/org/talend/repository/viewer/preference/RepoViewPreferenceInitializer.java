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
package org.talend.repository.viewer.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.core.model.repository.IRepositoryPrefConstants;
import org.talend.repository.RepositoryViewPlugin;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class RepoViewPreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences() {
        final IPreferenceStore preferenceStore = RepositoryViewPlugin.getDefault().getPreferenceStore();
        // perspective filter
        preferenceStore.setDefault(IRepositoryPrefConstants.USE_PERSPECTIVE_FILTER, true);
    }

}
