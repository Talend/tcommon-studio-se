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
package org.talend.metadata.managment.ui.wizard.metadata.xml.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * DOC talend class global comment. Detailled comment
 */
public class AddElementDialog extends Dialog {

    public static final String CREATE_AS_SUBELEMENT = "sub-element"; //$NON-NLS-1$

    public static final String CREATE_AS_ATTRIBUTE = "attribute"; //$NON-NLS-1$

    public static final String CREATE_AS_NAME_SPACE = "name-space"; //$NON-NLS-1$

    private String value = CREATE_AS_SUBELEMENT;

    public AddElementDialog(Shell parentShell) {
        super(parentShell);
    }

    public String getSelectValue() {
        return value;
    }

    /*
     * (non-Javadoc) Method declared on Dialog.
     */
    protected Control createDialogArea(Composite parent) {
        // create composite
        Composite composite = (Composite) super.createDialogArea(parent);
        // composite.setLayout(new GridLayout());
        // String[][] namevalues = new String[][] { { "Create as sub-element of target node", CREATE_AS_SUBELEMENT },
        // { "Create as attribute of target node", CREATE_AS_ATTRIBUTE }, { "Add linker to target node", CREATE_AS_TEXT
        // } };

        String[][] namevalues = null;

        namevalues = new String[][] { { "Create as sub-element", CREATE_AS_SUBELEMENT },
                { "Create as attribute ", CREATE_AS_ATTRIBUTE }, { "Create as name space", CREATE_AS_NAME_SPACE } };

        RadioGroupFieldEditor rgfe = new RadioGroupFieldEditor("", "Select the operation:", 1, namevalues, composite, true);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 12;
        composite.setLayout(layout);

        rgfe.setPropertyChangeListener(new IPropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                value = event.getNewValue().toString();
            }
        });

        applyDialogFont(composite);
        return composite;
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Selection");
    }
}
