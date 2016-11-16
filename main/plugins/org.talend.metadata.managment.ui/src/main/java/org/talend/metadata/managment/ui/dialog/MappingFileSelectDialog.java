// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.metadata.managment.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.metadata.managment.ui.editor.MetadataTalendTypeEditor;
import org.talend.metadata.managment.ui.i18n.Messages;

public class MappingFileSelectDialog extends TitleAreaDialog {

    private String selectId = null;

    private MetadataTalendTypeEditor editor = null;

    public MappingFileSelectDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.setLayout(new GridLayout());

        Composite bgComposite = new Composite(parent, SWT.NONE);
        bgComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        bgComposite.setLayout(new GridLayout());

        editor = new MetadataTalendTypeEditor(
                Messages.getString("MappingFileSelectDialog.name"), Messages.getString("MappingFileSelectDialog.mappingFileList"), bgComposite); //$NON-NLS-1$ //$NON-NLS-2$
        editor.forceLoad();

        setTitle(Messages.getString("MappingFileSelectDialog.selectMappingFile")); //$NON-NLS-1$
        setMessage(Messages.getString("MappingFileSelectDialog.setMessage")); //$NON-NLS-1$

        return bgComposite;
    }

    @Override
    protected void configureShell(Shell newShell) {
        newShell.setSize(new Point(500, 600));
        super.configureShell(newShell);
    }

    @Override
    protected void okPressed() {
        if (editor != null) {
            editor.forceStore();
        }
        selectId = editor.getSelectId();

        super.okPressed();
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Only need OK button
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    public String getSelectId() {
        return this.selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

}
