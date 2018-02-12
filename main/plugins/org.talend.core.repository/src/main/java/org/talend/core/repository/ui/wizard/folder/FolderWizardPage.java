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
package org.talend.core.repository.ui.wizard.folder;

import java.util.regex.Pattern;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.talend.core.repository.CoreRepositoryPlugin;
import org.talend.core.repository.i18n.Messages;
import org.talend.repository.model.RepositoryConstants;

/**
 * Page for new project details. <br/>
 * 
 * $Id: FolderWizardPage.java 83889 2012-05-19 08:18:10Z nrousseau $
 * 
 */
public class FolderWizardPage extends WizardPage {

    private static final String DESC = Messages.getString("NewFolderWizard.description"); //$NON-NLS-1$

    private static final String RENAME_DESC = Messages.getString("RenameFolderAction.description"); //$NON-NLS-1$

    private Text nameText;

    private IStatus nameStatus;

    private final String defaultLabel;

    private boolean isPlainFolder = false;

    /**
     * Constructs a new NewProjectWizardPage.
     * 
     */
    public FolderWizardPage(String defaultLabel, boolean isPlainFolder) {
        super("WizardPage"); //$NON-NLS-1$
        this.defaultLabel = defaultLabel;
        this.isPlainFolder = isPlainFolder;
        setTitle(Messages.getString("NewFolderWizard.title")); //$NON-NLS-1$
        if (defaultLabel == null) {
            setDescription(DESC);
        } else {
            setDescription(RENAME_DESC);
        }

        nameStatus = createOkStatus();
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);

        // Name
        Label nameLab = new Label(container, SWT.NONE);
        nameLab.setText(Messages.getString("NewFolderWizard.label")); //$NON-NLS-1$

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        setControl(container);
        addListeners();
        setPageComplete(false);
        setDefaultValues();
    }

    private void setDefaultValues() {
        if (defaultLabel != null) {
            setName(defaultLabel);
        }
    }

    private void addListeners() {
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                checkFieldsValue();
            }
        });
    }

    /**
     * DOC ocarbone Comment method "checkField".
     */
    protected void checkFieldsValue() {
        // Field Name
        if (isPlainFolder) {
            nameStatus = ResourcesPlugin.getWorkspace().validateName(nameText.getText(), IResource.FOLDER);
            if (nameStatus.isOK() && (defaultLabel == null || !defaultLabel.equals(nameText.getText()))
                    && !((FolderWizard) getWizard()).isValid(nameText.getText())) {
                nameStatus = new Status(IStatus.ERROR, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages.getString(
                        "NewFolderWizard.nameInvalid", nameText.getText()), null); //$NON-NLS-1$
            }
        } else {
            if (nameText.getText().length() == 0) {
                nameStatus = new Status(IStatus.ERROR, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK,
                        Messages.getString("NewFolderWizard.nameEmpty"), null); //$NON-NLS-1$
            } else if (!Pattern.matches(RepositoryConstants.FOLDER_PATTERN, nameText.getText())) {
                nameStatus = new Status(IStatus.ERROR, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK,
                        Messages.getString("NewFolderWizard.nameIncorrect"), null); //$NON-NLS-1$
            } else if ((defaultLabel == null || !defaultLabel.equals(nameText.getText()))
                    && !((FolderWizard) getWizard()).isValid(nameText.getText())) {
                nameStatus = new Status(IStatus.ERROR, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK, Messages.getString(
                        "NewFolderWizard.nameInvalid", nameText.getText()), null); //$NON-NLS-1$
            } else {
                nameStatus = createOkStatus();
            }
        }
        updatePageStatus();
    }

    private void updatePageStatus() {
        setMessage(findMostSevere());
        setPageComplete(findMostSevere().getSeverity() != IStatus.ERROR);
    }

    private IStatus findMostSevere() {
        return nameStatus;
    }

    private void setMessage(IStatus status) {
        if (IStatus.ERROR == status.getSeverity()) {
            setErrorMessage(status.getMessage());
            setMessage(""); //$NON-NLS-1$
        } else {
            if (defaultLabel != null) {
                setMessage(RENAME_DESC);
            }
            setErrorMessage(null);
        }
    }

    public String getName() {
        return nameText.getText();
    }

    public void setName(String name) {
        nameText.setText(name);
    }

    private static IStatus createOkStatus() {
        return new Status(IStatus.OK, CoreRepositoryPlugin.PLUGIN_ID, IStatus.OK, "", null); //$NON-NLS-1$
    }
}
