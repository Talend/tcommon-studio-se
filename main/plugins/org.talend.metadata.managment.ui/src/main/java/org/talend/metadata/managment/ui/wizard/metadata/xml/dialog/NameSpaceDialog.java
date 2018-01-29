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
package org.talend.metadata.managment.ui.wizard.metadata.xml.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.talend.commons.ui.swt.formtools.LabelledText;
import org.talend.metadata.managment.ui.wizard.metadata.xml.utils.StringUtil;

/**
 * wzhang class global comment. Detailled comment
 */
public class NameSpaceDialog extends TitleAreaDialog {

    private LabelledText prefixLabel;

    private LabelledText nsValueLabel;

    private static final String DEFAULT = "Input the prefix and value of the namespace";

    private String prefixValue = "";

    private String nsValue;

    /**
     * wzhang NameSpaceDialog constructor comment.
     * 
     * @param parentShell
     */
    public NameSpaceDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    protected Control createContents(Composite parent) {
        super.createContents(parent);
        this.getShell().setText("Namespace dialog");
        this.setTitle("Input new namespace");
        this.setMessage("Input the prefix and value of the namespace");
        return parent;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        super.createDialogArea(parent);
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(6, true));
        prefixLabel = new LabelledText(composite, "Prefix", 5); //$NON-NLS-1$
        prefixLabel.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                prefixValue = prefixLabel.getText();
                validateField();
            }
        });

        nsValueLabel = new LabelledText(composite, "Namespace Value", 5); //$NON-NLS-1$
        nsValueLabel.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                nsValue = nsValueLabel.getText();
                validateField();
            }

        });
        return parent;

    }

    private void validateField() {
        if (!StringUtil.validateLabelForNameSpace(prefixValue)) {
            setMessage("Prefix value is invalid!", IMessageProvider.ERROR); //$NON-NLS-1$
            return;
        } else {
            setMessage(DEFAULT);
        }
        if (!StringUtil.validateLabelForFixedValue(nsValue)) {
            setMessage("NameSpace value is invalid!", IMessageProvider.ERROR); //$NON-NLS-1$
            return;
        } else {
            setMessage(DEFAULT);
        }
    }

    @Override
    public void setMessage(String newMessage, int newType) {
        super.setMessage(newMessage, newType);
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null && !button.isDisposed()) {
            if (!DEFAULT.equals(newMessage)) { // error
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }
    }

    public String getPrefix() {
        return this.prefixValue;
    }

    public String getNSValue() {
        return this.nsValue;
    }

}
