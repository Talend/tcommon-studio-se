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
package org.talend.registration.wizards.license;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.talend.registration.i18n.Messages;

/**
 * DOC mhirt class global comment. Detailled comment <br/>
 * 
 * $Id: LicenseWizardDialog.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public final class LicenseWizardDialog extends WizardDialog {

    /**
     * DOC mhirt LicenseWizardDialog constructor comment.
     * 
     * @param parentShell
     * @param newWizard
     */
    public LicenseWizardDialog(Shell parentShell, IWizard newWizard) {
        super(parentShell, newWizard);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.wizard.WizardDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        Button button = getButton(IDialogConstants.FINISH_ID);
        if (button != null) {
            ((GridData) button.getParent().getLayoutData()).horizontalAlignment = SWT.CENTER;
            button.setText(Messages.getString("LicenseWizard.accept")); //$NON-NLS-1$
            GridData data = new GridData(250, -1);
            button.setLayoutData(data);
        }
        Button buttonCancle = getButton(IDialogConstants.CANCEL_ID);
        if (buttonCancle != null) {
            buttonCancle.setText(Messages.getString("LicenseWizard.btnCancle")); //$NON-NLS-1$
            GridData data = new GridData(-1, -1);
            buttonCancle.setLayoutData(data);
        }
    }
}
