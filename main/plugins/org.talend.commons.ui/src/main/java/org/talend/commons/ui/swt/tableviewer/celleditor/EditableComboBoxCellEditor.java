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
package org.talend.commons.ui.swt.tableviewer.celleditor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * created by ggu on Jul 28, 2014 Detailled comment
 *
 */
public class EditableComboBoxCellEditor extends ComboBoxCellEditor {

    private CCombo comboList;

    public EditableComboBoxCellEditor(Composite parent, String[] items) {
        super(parent, items);
    }

    @Override
    protected Control createControl(Composite parent) {
        Control control = super.createControl(parent);
        if (control instanceof CCombo) {
            comboList = ((CCombo) control);
            comboList.setEditable(true);
        }
        return control;
    }

    @Override
    protected Object doGetValue() {
        Object curValue = super.doGetValue();
        if (curValue instanceof Integer) {
            Integer index = (Integer) curValue;
            if (index == -1) { // new value, so -1
                curValue = comboList.getText();
            } else { // in the list, try to find out.
                String[] items = this.getItems();
                if (items.length > index) {
                    curValue = items[index];
                }
            }
        }
        return curValue;
    }

    @Override
    protected void doSetValue(Object newvalue) {
        // only deal with the String value.
        Assert.isTrue(comboList != null && (newvalue instanceof String));
        String value = newvalue.toString();
        int index = findValueIndex(getItems(), value);
        super.doSetValue(index); // make sure update the selection, even -1
        if (index < 0) { // not found
            comboList.setText(value);
        }
    }

    private int findValueIndex(String[] items, String fValue) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(fValue)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void dispose() {
        super.dispose();
        comboList = null;
    }
}
