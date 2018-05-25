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
package org.talend.repository.viewer.ui;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.talend.repository.i18n.Messages;


/**
 * DOC cmeng  class global comment. Detailled comment
 */
public class RunInBackgroundProgressMonitorDialog extends ProgressMonitorDialog {

    public static final int USER_CHOICE_RUN_IN_BACKGROUND = 1;

    public static final int USER_CHOICE_CANCEL = 2;

    private Button waitInBackground = null;

    private Integer userChoice;

    public RunInBackgroundProgressMonitorDialog(Shell parent) {
        super(parent);
    }

    @Override
    protected void createButtonsForButtonBar(org.eclipse.swt.widgets.Composite parent) {
        waitInBackground = createButton(parent, IDialogConstants.OK_ID,
                Messages.getString("RunInBackgroundProgressMonitorDialog.waitInBackground"), true); //$NON-NLS-1$
        if (arrowCursor == null) {
            arrowCursor = new Cursor(waitInBackground.getDisplay(), SWT.CURSOR_ARROW);
        }
        waitInBackground.setCursor(arrowCursor);
        super.createButtonsForButtonBar(parent);
        waitInBackground.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                userChoice = USER_CHOICE_RUN_IN_BACKGROUND;
                setReturnCode(OK);
                finishedRun();
            }
        });
    }

    @Override
    protected void cancelPressed() {
        userChoice = USER_CHOICE_CANCEL;
        super.cancelPressed();
    }

    /**
     * get the user choice
     * 
     * @return null: user hasn't choose yet;<br/>
     * USER_CHOICE_RUN_IN_BACKGROUND: user chooses to run in background;<br/>
     * USER_CHOICE_CANCEL: user cancels.
     */
    public Integer getUserChoice() {
        return userChoice;
    }

}
