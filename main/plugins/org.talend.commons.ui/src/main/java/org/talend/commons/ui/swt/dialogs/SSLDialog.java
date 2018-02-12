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
package org.talend.commons.ui.swt.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.ui.runtime.i18n.Messages;

public class SSLDialog extends Dialog {

    private Text keyStoreText;

    private Text trustStoreText;

    private Text passwordText;

    private Button okButton;

    private Button browseKeyStoreButton;

    private Button browseTrustStoreButton;

    private String keyStorePath;

    private String trustStorePath;

    private String password;

    /**
     * DOC Administrator FetchDialog constructor comment.
     * 
     * @param parentShell
     */
    public SSLDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("SSL parameters"); //$NON-NLS-1$
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        this.okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        applyDialogFont(composite);

        Group group = new Group(composite, SWT.NONE);
        GridLayout layout = new GridLayout(3, false);
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setText("Please provide your SSL information"); //$NON-NLS-1$

        new Label(group, SWT.NONE).setText("KeyStore File"); //$NON-NLS-1$
        keyStoreText = new Text(group, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        keyStoreText.setLayoutData(data);
        if (keyStorePath != null) {
            keyStoreText.setText(keyStorePath);
        }
        browseKeyStoreButton = new Button(group, SWT.PUSH);
        browseKeyStoreButton.setText("Browse");
        setButtonLayoutData(browseKeyStoreButton);
        browseKeyStoreButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleKeyStoreDirectoryButtonPressed();
            }
        });

        new Label(group, SWT.NONE).setText("TrustStore File"); //$NON-NLS-1$
        trustStoreText = new Text(group, SWT.BORDER);
        GridData data1 = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 250;
        trustStoreText.setLayoutData(data1);
        if (trustStorePath != null) {
            trustStoreText.setText(trustStorePath);
        }
        browseTrustStoreButton = new Button(group, SWT.PUSH);
        browseTrustStoreButton.setText("Browse");
        setButtonLayoutData(browseTrustStoreButton);
        browseTrustStoreButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                handleTrustDirectoryButtonPressed();
            }
        });

        new Label(group, SWT.NONE).setText("SSL Password"); //$NON-NLS-1$
        passwordText = new Text(group, SWT.BORDER);
        passwordText.setEchoChar('*');
        passwordText.setLayoutData(data);
        if (password != null) {
            passwordText.setText(password);
        }

        return composite;
    }

    @Override
    protected void cancelPressed() {
        keyStorePath = keyStoreText.getText();
        trustStorePath = trustStoreText.getText();
        password = passwordText.getText();
        super.cancelPressed();
    }

    @Override
    protected void okPressed() {
        if (keyStoreText.getText().equals("")) {
            MessageDialog.openError(new Shell(), getShell().getText(), Messages.getString("SSLConfirmDialog.KeyStore")); //$NON-NLS-1$
            return;
        }
        if (passwordText.getText().equals("")) {
            MessageDialog.openError(new Shell(), getShell().getText(), Messages.getString("SSLConfirmDialog.TrustStore")); //$NON-NLS-1$
            return;
        }
        if (trustStoreText.getText().equals("")) {
            MessageDialog.openError(new Shell(), getShell().getText(), Messages.getString("SSLConfirmDialog.Password")); //$NON-NLS-1$
            return;
        }
        keyStorePath = keyStoreText.getText();
        password = passwordText.getText();
        trustStorePath = trustStoreText.getText().trim();
        super.okPressed();
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public void setKeyStorePath(String keyStorePath) {
        this.keyStorePath = keyStorePath;
    }

    public String getTrustStorePath() {
        return trustStorePath;
    }

    public void setTrustStorePath(String trustStorePath) {
        this.trustStorePath = trustStorePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private void handleKeyStoreDirectoryButtonPressed() {

        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterPath("");
        dialog.setText("select keyStore");
        dialog.setFileName("");
        dialog.setFilterExtensions(new String[] { "*.jks" });// 设置文件扩展名
        String fileName = dialog.open();//

        if (fileName != null) {
            keyStoreText.setText(fileName);
        }
    }

    private void handleTrustDirectoryButtonPressed() {

        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
        dialog.setFilterPath("");
        dialog.setText("select trustStore");
        dialog.setFileName("");
        dialog.setFilterExtensions(new String[] { "*.jks" });// 设置文件扩展名
        String fileName = dialog.open();//
        if (fileName != null) {
            trustStoreText.setText(fileName);
        }
    }
}
