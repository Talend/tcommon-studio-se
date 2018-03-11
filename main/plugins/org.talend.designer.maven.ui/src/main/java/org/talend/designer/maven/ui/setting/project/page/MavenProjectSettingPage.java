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

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.core.runtime.projectsetting.AbstractProjectSettingPage;
import org.talend.designer.maven.tools.AggregatorPomsHelper;
import org.talend.designer.maven.ui.i18n.Messages;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class MavenProjectSettingPage extends AbstractProjectSettingPage {

    public MavenProjectSettingPage() {
        noDefaultAndApplyButton();
    }

    @Override
    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();
        parent.setLayout(new GridLayout());
        Button button = new Button(parent, SWT.NONE);
        button.setText(Messages.getString("ProjectPomProjectSettingPage.syncAllPomsButtonText")); //$NON-NLS-1$
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                try {
                    new AggregatorPomsHelper().syncAllPoms();
                } catch (Exception e) {
                    ExceptionHandler.process(e);
                }
            }

        });
    }

}
