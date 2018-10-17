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
package org.talend.commons.ui.runtime.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.talend.commons.ui.runtime.i18n.Messages;

/***/
public class ExceptionMessageDialog extends MessageDialog {

    private Throwable ex;

    private String exceptionString = null;

    public ExceptionMessageDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage,
            int dialogImageType, String[] dialogButtonLabels, int defaultIndex, Throwable ex) {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
        this.ex = ex;
    }

    @Override
    protected Control createCustomArea(Composite parent) {
        if (exceptionString == null || exceptionString.isEmpty()) {
            if (ex != null) {
                StringWriter stringWriter = new StringWriter();
                ex.printStackTrace(new PrintWriter(stringWriter));
                exceptionString = stringWriter.toString();
            } else {
                exceptionString = "";
            }
        }

        ExpandableComposite errorComposite = new ExpandableComposite(parent, ExpandableComposite.COMPACT);
        errorComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1));
        errorComposite.setText(Messages.getString("ExceptionMessageDialog.log")); //$NON-NLS-1$
        errorComposite.addExpansionListener(new IExpansionListener() {

            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                int delta = 300;
                if (!e.getState()) {
                    delta = -delta;
                }
                Point shellSize = getShell().getSize();
                Point newSize = new Point(shellSize.x, shellSize.y + delta);
                getShell().setSize(newSize);
            }

            @Override
            public void expansionStateChanging(ExpansionEvent e) {
            }

        });

        Text text = new Text(errorComposite, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        text.setText(exceptionString);
        text.setEditable(false);
        errorComposite.setClient(text);

        return errorComposite;
    }

    public static boolean openConfirm(Shell parent, String title, String message, Throwable ex) {
        MessageDialog dialog = new ExceptionMessageDialog(parent, title, null, message, QUESTION, new String[] {
                Messages.getString("ExceptionMessageDialog.OK"), Messages.getString("ExceptionMessageDialog.Cancel") }, 0, ex); //$NON-NLS-1$ //$NON-NLS-2$
        return dialog.open() == 0;
    }

    public static void openError(Shell parent, String title, String message, Throwable ex) {
        MessageDialog dialog = new ExceptionMessageDialog(parent, title, null, message, ERROR,
                new String[] { Messages.getString("ExceptionMessageDialog.OK") }, 0, ex); //$NON-NLS-1$
        dialog.open();
        return;
    }

    public static void openInformation(Shell parent, String title, String message, Throwable ex) {
        MessageDialog dialog = new ExceptionMessageDialog(parent, title, null, message, INFORMATION,
                new String[] { Messages.getString("ExceptionMessageDialog.OK") }, 0, ex); //$NON-NLS-1$
        dialog.open();
        return;
    }

    public static boolean openQuestion(Shell parent, String title, String message, Throwable ex) {
        MessageDialog dialog = new ExceptionMessageDialog(parent, title, null, message, QUESTION, new String[] {
                Messages.getString("ExceptionMessageDialog.Yes"), Messages.getString("ExceptionMessageDialog.No") }, //$NON-NLS-1$ //$NON-NLS-2$
                0, ex);
        return dialog.open() == 0;
    }

    public static void openWarning(Shell parent, String title, String message, Throwable ex) {
        MessageDialog dialog = new ExceptionMessageDialog(parent, title, null, message, WARNING,
                new String[] { Messages.getString("ExceptionMessageDialog.OK") }, 0, ex); //$NON-NLS-1$
        dialog.open();
        return;
    }

    public String getExceptionString() {
        return this.exceptionString;
    }

    public void setExceptionString(String exceptionString) {
        this.exceptionString = exceptionString;
    }

}
