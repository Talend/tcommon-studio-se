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
package org.talend.commons.ui.swt.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.ui.runtime.i18n.Messages;

/**
 * DOC zqin class global comment. Detailled comment
 */
public class ModelSelectionDialog extends SelectionDialog {

    public enum EEditSelection {
        SHOW_SCHEMA,
        SHOW_QUERY,
        BUILDIN,
        REPOSITORY;
    }

    public enum ESelectionType {
        SCHEMA,
        QUERY,
        NORMAL;
    }

    private static final String TITLE = Messages.getString("ModelSelectionDialog.Title"); //$NON-NLS-1$

    private static final String MESSAGE = Messages.getString("ModelSelectionDialog.Message"); //$NON-NLS-1$

    private EEditSelection optionValue;

    private Button showSchema, showQuery, buildIn, repository;

    private Boolean readOnlyJob;

    private ESelectionType selectionType;

    /**
     * DOC yzhang ModelSelectionDialog constructor comment.
     */

    public ModelSelectionDialog(Shell parentShell, ESelectionType selectionType) {
        this(parentShell, selectionType, false);
    }

    public ModelSelectionDialog(Shell parentShell, ESelectionType selectionType, boolean isReadOnly) {
        super(parentShell);
        setHelpAvailable(false);
        setTitle(TITLE);
        setMessage(MESSAGE);
        this.selectionType = selectionType;
        this.readOnlyJob = isReadOnly;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        createMessageArea(composite);
        Label titleBarSeparator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
        titleBarSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        createOptionArea(composite);
        return composite;
    }

    protected Control createOptionArea(Composite composite) {
        Composite inner = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        inner.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.minimumWidth = 300;
        inner.setLayoutData(gridData);

        Group group = new Group(inner, SWT.NONE);
        group.setText(Messages.getString("ModelSelectionDialog.Option")); //$NON-NLS-1$
        gridLayout = new GridLayout();
        gridLayout.horizontalSpacing = 10;
        group.setLayout(gridLayout);
        group.setLayoutData(gridData);
        if (selectionType == ESelectionType.SCHEMA) {
            showSchema = new Button(group, SWT.RADIO);
            showSchema.setText(Messages.getString("ModelSelectionDialog.newViewSchema")); //$NON-NLS-1$
        }
        if (selectionType == ESelectionType.QUERY) {
            showQuery = new Button(group, SWT.RADIO);
            showQuery.setText(Messages.getString("ModelSelectionDialog.ViewQuery")); //$NON-NLS-1$
        }

        buildIn = new Button(group, SWT.RADIO);
        buildIn.setText(Messages.getString("ModelSelectionDialog.BuiltIn")); //$NON-NLS-1$

        repository = new Button(group, SWT.RADIO);
        repository.setText(Messages.getString("ModelSelectionDialog.Update")); //$NON-NLS-1$

        configControlStatus();

        return inner;
    }

    /**
     * yzhang Comment method "initControl".
     */
    private void configControlStatus() {
        if (readOnlyJob) {
            buildIn.setEnabled(false);
            repository.setEnabled(false);
        }
        // set default selection
        if (selectionType == ESelectionType.SCHEMA) {
            showSchema.setSelection(true);
        } else if (selectionType == ESelectionType.QUERY) {
            showQuery.setSelection(true);
        } else {
            repository.setSelection(true);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        if (buildIn.getSelection())
            setOptionValue(EEditSelection.BUILDIN);
        if (repository.getSelection())
            setOptionValue(EEditSelection.REPOSITORY);
        if (selectionType == ESelectionType.SCHEMA) {
            if (showSchema.getSelection())
                setOptionValue(EEditSelection.SHOW_SCHEMA);
        }
        if (selectionType == ESelectionType.QUERY) {
            if (showQuery.getSelection())
                setOptionValue(EEditSelection.SHOW_QUERY);
        }

        super.okPressed();
    }

    /**
     * Getter for optionValue.
     * 
     * @return the optionValue
     */
    public EEditSelection getOptionValue() {
        return this.optionValue;
    }

    /**
     * Sets the optionValue.
     * 
     * @param optionValue the optionValue to set
     */
    public void setOptionValue(EEditSelection optionValue) {
        this.optionValue = optionValue;
    }

}
