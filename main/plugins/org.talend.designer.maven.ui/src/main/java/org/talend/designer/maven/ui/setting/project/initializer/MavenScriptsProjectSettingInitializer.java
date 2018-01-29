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
package org.talend.designer.maven.ui.setting.project.initializer;

import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.core.runtime.projectsetting.IProjectSettingTemplateConstants;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.ui.DesignerMavenUiPlugin;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenScriptsProjectSettingInitializer extends AbstractProjectPreferenceInitializer {

    @Override
    protected IPreferenceStore getPreferenceStore() {
        return DesignerMavenUiPlugin.getDefault().getProjectPreferenceManager().getPreferenceStore();
    }

    @Override
    protected void initializeFields(IPreferenceStore preferenceStore) {
        super.initializeFields(preferenceStore);

        try {
            setDefault(preferenceStore, IProjectSettingPreferenceConstants.TEMPLATE_PROJECT_POM, DesignerMavenPlugin.PLUGIN_ID,
                    IProjectSettingTemplateConstants.PATH_GENERAL + '/'
                            + IProjectSettingTemplateConstants.PROJECT_TEMPLATE_FILE_NAME);

        } catch (Exception e) {
            ExceptionHandler.process(e);
        }

    }

}
