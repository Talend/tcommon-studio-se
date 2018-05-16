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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Extends WizardDialog to customize the label Finish. <br/>
 * 
 * $Id: CustomWizardDialog.java 38013 2010-03-05 14:21:59Z mhirt $
 * 
 */
public class CustomWizardDialog extends WizardDialog {

    private String finishLabel = "Finish"; //$NON-NLS-1$

    /**
     * Constructor WizardDialog.
     * 
     * @param parentShell
     * @param newWizard
     */
    public CustomWizardDialog(Shell parentShell, IWizard newWizard, String finishLabel) {
        super(parentShell, newWizard);
        this.finishLabel = finishLabel;
    }

    public void createButtonsForButtonBar() {
        Button button = getButton(IDialogConstants.FINISH_ID);
        if (button != null) {
            button.setText(finishLabel);
        }
    }

}
