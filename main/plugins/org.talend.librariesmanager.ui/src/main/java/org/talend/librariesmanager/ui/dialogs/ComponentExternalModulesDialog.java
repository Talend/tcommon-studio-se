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
package org.talend.librariesmanager.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;

/**
 * created by Administrator on 2012-9-18 Detailled comment
 * 
 */
public class ComponentExternalModulesDialog extends ExternalModulesInstallDialogWithProgress {

    private Button doNotShowBtn;

    private String coponentName;

    public String getCoponentName() {
        return this.coponentName;
    }

    public void setCoponentName(String coponentName) {
        this.coponentName = coponentName;
    }

    public ComponentExternalModulesDialog(Shell shell, String text, String title) {
        super(shell, text, title);
    }

    @Override
    protected void createFooter(Composite parent) {
        doNotShowBtn = new Button(parent, SWT.CHECK);
        doNotShowBtn.setText(Messages.getString("ComponentExternalModulesDialog.doNotShow"));
        doNotShowBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                LibManagerUiPlugin.getDefault().getPreferenceStore().setValue(DO_NOT_SHOW_EXTERNALMODULESINSTALLDIALOG, true);
            }
        });
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.librariesmanager.ui.dialogs.ExternalModulesInstallDialog#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(text);
    }

}
