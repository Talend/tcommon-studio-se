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
package org.talend.commons.ui.utils.workbench.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Create a combo field editor to add in the preferences. <br/>
 * 
 * $Id: ComboFieldEditor.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class ComboFieldEditor extends FieldEditor {

    /**
     * The <code>Combo</code> widget.
     */
    protected Combo fCombo;

    /**
     * The value (not the name) of the currently selected item in the Combo widget.
     */
    private String fValue;

    /**
     * The names (labels) and underlying values to populate the combo widget. These should be arranged as: { {name1,
     * value1}, {name2, value2}, ...}
     */
    private String[][] fEntryNamesAndValues;

    public ComboFieldEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
        init(name, labelText);
        Assert.isTrue(checkArray(entryNamesAndValues));
        fEntryNamesAndValues = entryNamesAndValues;
        createControl(parent);
    }

    private boolean checkArray(String[][] table) {
        if (table == null) {
            return false;
        }
        for (int i = 0; i < table.length; i++) {
            String[] array = table[i];
            if (array == null || array.length != 2) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void adjustForNumColumns(int numColumns) {
        Control control = getLabelControl();
        if (control != null) {
            ((GridData) control.getLayoutData()).horizontalSpan = numColumns;
        }
        ((GridData) fCombo.getLayoutData()).horizontalSpan = numColumns;
    }

    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        Control control = getLabelControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = numColumns;
        control.setLayoutData(gd);
        control = getComboBoxControl(parent);
        gd = new GridData();
        gd.horizontalSpan = numColumns;
        control.setLayoutData(gd);
    }

    @Override
    protected void doLoad() {
        updateComboForValue(getPreferenceStore().getString(getPreferenceName()));
    }

    @Override
    protected void doLoadDefault() {
        updateComboForValue(getPreferenceStore().getDefaultString(getPreferenceName()));
    }

    @Override
    protected void doStore() {
        if (fValue == null) {
            getPreferenceStore().setToDefault(getPreferenceName());
            return;
        }

        getPreferenceStore().setValue(getPreferenceName(), fValue);
    }

    @Override
    public int getNumberOfControls() {
        return 1;
    }

    public Combo getComboBoxControl(Composite parent) {
        if (fCombo == null) {
            fCombo = new Combo(parent, SWT.READ_ONLY);
            for (int i = 0; i < fEntryNamesAndValues.length; i++) {
                fCombo.add(fEntryNamesAndValues[i][0], i);
            }
            fCombo.setFont(parent.getFont());
            fCombo.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent evt) {
                    String oldValue = fValue;
                    String name = fCombo.getText();
                    fValue = getValueForName(name);
                    setPresentsDefaultValue(false);
                    fireValueChanged(VALUE, oldValue, fValue);
                }
            });
        }
        return fCombo;
    }

    public String getFieldValue() {
        return fValue;
    }

    /**
     * Given the name (label) of an entry, return the corresponding value.
     */
    protected String getValueForName(String name) {
        for (int i = 0; i < fEntryNamesAndValues.length; i++) {
            String[] entry = fEntryNamesAndValues[i];
            if (name.equals(entry[0])) {
                return entry[1];
            }
        }
        return fEntryNamesAndValues[0][0];
    }

    /**
     * Set the name in the combo widget to match the specified value.
     */
    protected void updateComboForValue(String value) {
        fValue = value;
        for (int i = 0; i < fEntryNamesAndValues.length; i++) {
            if (value.equals(fEntryNamesAndValues[i][1])) {
                fCombo.setText(fEntryNamesAndValues[i][0]);
                return;
            }
        }
        if (fEntryNamesAndValues.length > 0) {
            fValue = fEntryNamesAndValues[0][1];
            fCombo.setText(fEntryNamesAndValues[0][0]);
        }
    }

    public Control getControl() {
        return fCombo;
    }
}
