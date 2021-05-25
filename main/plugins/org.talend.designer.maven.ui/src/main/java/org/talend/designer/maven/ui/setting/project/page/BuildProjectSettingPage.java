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
package org.talend.designer.maven.ui.setting.project.page;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.core.runtime.maven.MavenConstants;
import org.talend.core.runtime.projectsetting.EmptyProjectSettingPage;
import org.talend.designer.maven.DesignerMavenPlugin;
import org.talend.designer.maven.ui.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class BuildProjectSettingPage extends EmptyProjectSettingPage {

    private IPreferenceStore preferenceStore;

    private Button allowRecursiveJobsCheckbox;

    public BuildProjectSettingPage() {
        super();
    }

    @Override
    protected String getPreferenceName() {
        return DesignerMavenPlugin.PLUGIN_ID;
    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();
        parent.setLayout(new GridLayout());
        preferenceStore = getPreferenceStore();
        allowRecursiveJobsCheckbox = new Button(parent, SWT.CHECK);
        allowRecursiveJobsCheckbox.setText(Messages.getString("BuildProjectSettingPage.allowRecursiveJobs")); //$NON-NLS-1$
        allowRecursiveJobsCheckbox.setSelection(!preferenceStore.getBoolean(MavenConstants.SKIP_LOOP_DEPENDENCY_CHECK));
    }

    @Override
    public boolean performOk() {
        boolean performOk = super.performOk();
        if (preferenceStore != null) {
            preferenceStore.setValue(MavenConstants.SKIP_LOOP_DEPENDENCY_CHECK, !allowRecursiveJobsCheckbox.getSelection());
        }
        return performOk;
    }
}
