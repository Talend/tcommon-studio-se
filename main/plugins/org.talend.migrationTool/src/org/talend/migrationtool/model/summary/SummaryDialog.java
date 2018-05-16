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
package org.talend.migrationtool.model.summary;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.migration.IProjectMigrationTask;
import org.talend.migrationtool.i18n.Messages;

/**
 * DOC smallet class global comment. Detailled comment <br/>
 * 
 */
public class SummaryDialog extends TitleAreaDialog {

    private static final int DIALOG_HEIGHT = 350;

    private List<IProjectMigrationTask> tasks;

    public SummaryDialog(Shell parentShell, List<IProjectMigrationTask> tasks) {
        super(parentShell);
        this.tasks = tasks;
        setHelpAvailable(false);
        // setShellStyle(SWT.Resize | getShellStyle());
    }

    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        this.setMessage(Messages.getString("migrationTasksRecapDialog.descriptions")); //$NON-NLS-1$
        return control;
    }

    @Override
    protected Control createDialogArea(final Composite parent) {
        int height = DIALOG_HEIGHT - getTitleImageLabel().getSize().y;
        return new SummaryComposite(parent, SWT.NONE, tasks, height);
    }

    @Override
    protected void configureShell(final Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("migrationTasksRecapDialog.title")); //$NON-NLS-1$
        newShell.setSize(600, DIALOG_HEIGHT);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }
}
