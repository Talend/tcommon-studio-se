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
package org.talend.core.runtime.projectsetting;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractProjectSettingPage extends FieldEditorPreferencePage {

    private String prefNodeId;

    public AbstractProjectSettingPage() {
        super();

    }

    public String getPrefNodeId() {
        return prefNodeId;
    }

    public void setPrefNodeId(String prefNodeId) {
        this.prefNodeId = prefNodeId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
     */
    @Override
    protected IPreferenceStore doGetPreferenceStore() {
        String preferenceName = getPreferenceName();
        if (preferenceName != null) {
            ProjectPreferenceManager projectPreferenceManager = new ProjectPreferenceManager(preferenceName);
            // set the project preference
            return projectPreferenceManager.getPreferenceStore();
        }
        return super.doGetPreferenceStore();
    }

    protected String getPreferenceName() {
        return null;
    }

    @Override
    protected void createFieldEditors() {
        // nothing to do
    }

    protected Composite createLabelComposite(Composite parent, String title) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout messageLayout = new GridLayout();
        messageLayout.numColumns = 2;
        messageLayout.marginWidth = 0;
        messageLayout.marginHeight = 0;
        composite.setLayout(messageLayout);
        composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
        composite.setFont(parent.getFont());

        final Label lbl = new Label(composite, SWT.BOLD);
        lbl.setText(title);//$NON-NLS-1$
        lbl.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
        lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

        return composite;
    }
}
