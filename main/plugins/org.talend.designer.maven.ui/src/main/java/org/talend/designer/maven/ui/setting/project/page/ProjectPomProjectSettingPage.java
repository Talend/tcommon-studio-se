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
package org.talend.designer.maven.ui.setting.project.page;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.GlobalServiceRegister;
import org.talend.core.runtime.process.ITalendProcessJavaProject;
import org.talend.core.runtime.projectsetting.IProjectSettingPreferenceConstants;
import org.talend.designer.maven.tools.ProjectPomManager;
import org.talend.designer.maven.ui.DesignerMavenUiPlugin;
import org.talend.designer.maven.ui.i18n.Messages;
import org.talend.designer.runprocess.IRunProcessService;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class ProjectPomProjectSettingPage extends AbstractPersistentProjectSettingPage {

    private String oldScriptContent;

    public ProjectPomProjectSettingPage() {
        super();
        this.oldScriptContent = this.getScriptContent();
    }

    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        return DesignerMavenUiPlugin.getDefault().getProjectPreferenceManager().getPreferenceStore();
    }

    @Override
    protected String getPreferenceKey() {
        return IProjectSettingPreferenceConstants.TEMPLATE_PROJECT_POM;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.repository.preference.AbstractScriptPreferencePage#getHeadTitle()
     */
    @Override
    protected String getHeadTitle() {
        return Messages.getString("ProjectPomProjectSettingPage_Titile"); //$NON-NLS-1$
    }

    @Override
    protected void performApply() {
        super.performApply();
        // reset from modification
        this.oldScriptContent = this.getScriptContent();
    }

    @Override
    public boolean performOk() {
        boolean performOk = super.performOk();

        String newContent = this.getScriptContent();
        if (!newContent.equals(oldScriptContent)) { // not same
            MessageDialog.openWarning(this.getShell(), Messages.getString("ProjectPomProjectSettingPage_ConfirmTitle"), //$NON-NLS-1$
                    Messages.getString("ProjectPomProjectSettingPage_ConfirmMessage")); //$NON-NLS-1$

            // Update project pom file.
            ProjectPomManager projectPomManager = new ProjectPomManager();
            try {
                projectPomManager.updateFromTemplate(null);
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }

        return performOk;
    }

}
