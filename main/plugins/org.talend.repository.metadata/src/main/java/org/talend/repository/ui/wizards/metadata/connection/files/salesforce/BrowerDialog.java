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
package org.talend.repository.ui.wizards.metadata.connection.files.salesforce;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * DOC zwzhao class global comment. Detailled comment
 */
public class BrowerDialog extends Dialog {

    private String url;

    private Browser broswer;

    /**
     * DOC zwzhao BrowerDialog constructor comment.
     * 
     * @param parentShell
     */
    protected BrowerDialog(Shell parentShell, String url) {
        super(parentShell);
        this.url = url;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new GridLayout());
        broswer = new Browser(composite, SWT.NONE);
        broswer.setUrl(url);
        broswer.setLayoutData(new GridData(GridData.FILL_BOTH));
        broswer.redraw();
        composite.redraw();
        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#getInitialSize()
     */
    @Override
    protected Point getInitialSize() {
        // TODO Auto-generated method stub
        return new Point(1000, 800);
    }

    /**
     * Getter for broswer.
     * 
     * @return the broswer
     */
    public Browser getBroswer() {
        return this.broswer;
    }

    /**
     * Sets the broswer.
     * 
     * @param broswer the broswer to set
     */
    public void setBroswer(Browser broswer) {
        this.broswer = broswer;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

}
