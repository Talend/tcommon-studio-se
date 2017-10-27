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
package org.talend.repository.ui.wizards.metadata.connection.database;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.repository.metadata.i18n.Messages;

/**
 * let user to confirm reload the connection or not when modify it.
 */
public class ConfirmReloadConnectionDialog extends Dialog {

    /**
     * DOC xqliu ConfirmReloadDatabaseDialog constructor comment.
     * 
     * @param parentShell
     */
    public ConfirmReloadConnectionDialog(Shell parentShell) {
        super(parentShell);
    }

    private String reloadFlag;

    public String getReloadFlag() {
        return this.reloadFlag;
    }

    public void setReloadFlag(String reloadFlag) {
        this.reloadFlag = reloadFlag;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ConfirmReloadConnectionDialog.title")); //$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout(1, true));
        comp.setLayoutData(new GridData(GridData.FILL_BOTH));

        Label label = new Label(comp, SWT.NONE);
        label.setText(Messages.getString("ConfirmReloadConnectionDialog.desc")); //$NON-NLS-1$

        Button[] radios = new Button[2];

        radios[0] = new Button(comp, SWT.RADIO);
        radios[0].setSelection(true);
        setReloadFlag(DatabaseWizard.RELOAD_FLAG_TRUE);
        radios[0].setText(Messages.getString("ConfirmReloadConnectionDialog.reload")); //$NON-NLS-1$
        radios[0].addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
                widgetSelected(arg0);
            }

            public void widgetSelected(SelectionEvent arg0) {
                setReloadFlag(DatabaseWizard.RELOAD_FLAG_TRUE);
            }

        });

        radios[1] = new Button(comp, SWT.RADIO);
        radios[1].setText(Messages.getString("ConfirmReloadConnectionDialog.unreload")); //$NON-NLS-1$
        radios[1].addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
                widgetSelected(arg0);
            }

            public void widgetSelected(SelectionEvent arg0) {
                setReloadFlag(DatabaseWizard.RELOAD_FLAG_FALSE);
            }

        });

        GridData radioGD = new GridData(GridData.FILL_BOTH);
        radios[0].setLayoutData(radioGD);
        radios[1].setLayoutData(radioGD);

        return comp;
    }

}
