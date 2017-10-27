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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.utils.TalendPropertiesUtil;
import org.talend.librariesmanager.ui.LibManagerUiPlugin;
import org.talend.librariesmanager.ui.i18n.Messages;
import org.talend.librariesmanager.utils.RemoteModulesHelper;

/**
 * created by WCHEN on 2012-9-18 Detailled comment
 * 
 */
public class ModuleLicenseDialog extends Dialog {

    private Browser clufTextBrowser;

    private Text clufText;

    private String licenseUrl;

    private String licenseType;

    private String name;

    /**
     * DOC WCHEN ModuleLicenseDialog constructor comment.
     * 
     * @param parentShell
     */
    public ModuleLicenseDialog(Shell parentShell, String licenseType, String license, String name) {
        super(parentShell);
        setShellStyle(SWT.CLOSE | SWT.MAX | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
        this.licenseType = licenseType;
        this.licenseUrl = license;
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ModuleLicenseDialog.license"));//$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        ((GridData) parent.getLayoutData()).minimumWidth = 600;
        ((GridData) parent.getLayoutData()).heightHint = 500;
        GridData data = new GridData(GridData.FILL_BOTH);
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.marginTop = 10;
        layout.marginLeft = 5;
        layout.marginRight = 5;
        container.setLayout(layout);
        container.setLayoutData(data);
        data = new GridData(GridData.FILL_HORIZONTAL);
        Label subTitleLabel = new Label(container, SWT.NONE);
        String desc = Messages.getString("ModuleLicenseDialog.license.notFound", name);//$NON-NLS-1$
        subTitleLabel.setText(desc);
        subTitleLabel.setLayoutData(data);

        Control culfTextControl = null;
        if (TalendPropertiesUtil.isEnabledUseBrowser()) {
            clufTextBrowser = new Browser(container, SWT.MULTI | SWT.WRAP | SWT.LEFT | SWT.BORDER);
            culfTextControl = clufTextBrowser;

            if (licenseUrl != null) {
                clufTextBrowser.setUrl(licenseUrl);
            } else {
                clufTextBrowser.setText(desc);
            }
        } else {
            clufText = new Text(container, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
            culfTextControl = clufText;

            String licenseContent = RemoteModulesHelper.getInstance().getLicenseContentByUrl(licenseUrl);
            if (licenseUrl != null) {
                if (licenseContent != null) {
                    clufText.setText(licenseContent);
                } else {
                    clufText.setText(Messages.getString("AcceptModuleLicensesWizardPage.licenseContent.defaultDesc")); //$NON-NLS-1$
                }
            } else {
                clufText.setText(desc);
            }
        }
        if (culfTextControl != null) {
            culfTextControl.setBackground(new Color(null, 255, 255, 255));
            culfTextControl.setLayoutData(new GridData(GridData.FILL_BOTH));
        }
        return parent;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        getButton(IDialogConstants.OK_ID).setText(Messages.getString("ModuleLicenseDialog.license.accept"));//$NON-NLS-1$
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        LibManagerUiPlugin.getDefault().getPreferenceStore().setValue(licenseType, true);
        super.okPressed();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
     */
    @Override
    protected void cancelPressed() {
        super.cancelPressed();
    }

}
