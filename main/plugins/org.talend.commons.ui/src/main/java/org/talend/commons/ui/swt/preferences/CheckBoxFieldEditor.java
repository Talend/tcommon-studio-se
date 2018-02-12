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
package org.talend.commons.ui.swt.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * ggu class global comment. Detailled comment
 */
public class CheckBoxFieldEditor extends BooleanFieldEditor {

    private Composite parent;

    public CheckBoxFieldEditor(String name, String label, Composite parent) {
        super(name, label, parent);
        this.parent = parent;
    }

    public void setChecked(boolean check) {
        getButton().setSelection(check);
    }

    public Button getButton() {
        return getChangeControl(parent);
    }
}
