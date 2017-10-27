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
package org.talend.commons.ui.swt.formtools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Create Label, checkbox and Combo.
 * 
 * $Id: LabelledCheckboxCombo.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class LabelledCheckboxCombo {

    /**
     * 
     */
    private static final int A_20 = 20;

    /**
     * 
     */
    private static final int A_100 = 100;

    private Label label;

    private Combo combo;

    private Button checkbox;

    private static final int MAX_VISIBLE_ITEM_COUNT = 25;

    private static final int DEFAULT_FIELD_STYLE = SWT.READ_ONLY;

    /**
     * create Label and Combo width a List of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param List<String> item
     * @param int horizontalSpan
     * @param boolean isFill
     */
    public LabelledCheckboxCombo(final Composite parent, final String labelStr, final String tip, final String[] item,
            final int horizontalSpan, final boolean isFill, final int style) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, isFill, style);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param String[] item
     * @param int horizontalSpan
     * @param boolean isFill
     * 
     * @return Combo
     */
    public LabelledCheckboxCombo(final Composite parent, final String labelStr, final String tip, final String[] item,
            int horizontalSpan, boolean isFill) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, isFill, DEFAULT_FIELD_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param String[] item
     * @param int horizontalSpan
     * 
     * @return Combo
     */
    public LabelledCheckboxCombo(Composite parent, String labelStr, String tip, String[] item, int horizontalSpan) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, false, DEFAULT_FIELD_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param String[] item
     * 
     * @return Combo
     */
    public LabelledCheckboxCombo(Composite parent, String labelStr, String tip, String[] item) {
        createLabelledCombo(parent, labelStr, tip, item, 1, false, DEFAULT_FIELD_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param parent
     * @param labelStr
     * @param tip
     * @param item
     * @param horizontalSpan
     * @param isFill
     * @param style
     */
    private void createLabelledCombo(final Composite parent, final String labelStr, final String tip,
            final String[] item, int horizontalSpan, boolean isFill, int style) {

        // label
        label = new Label(parent, SWT.LEFT);
        label.setText(labelStr);
        label.addListener(SWT.MouseEnter, new Listener() {

            public void handleEvent(final Event e) {
                checkbox.forceFocus();
            }
        });

        // checkbox
        checkbox = new Button(parent, SWT.CHECK);

        // combo
        GridData gridData;
        if (isFill) {
            gridData = new GridData(GridData.FILL_HORIZONTAL);
        } else {
            gridData = new GridData();
        }
        gridData.horizontalSpan = horizontalSpan;
        combo = new Combo(parent, style);
        combo.setLayoutData(gridData);
        combo.setSize(A_100, A_20);

        for (int i = 0; i < item.length; i++) {
            combo.add(item[i]);
        }
        int visibleItemCount = combo.getItemCount();
        if (visibleItemCount > MAX_VISIBLE_ITEM_COUNT) {
            visibleItemCount = MAX_VISIBLE_ITEM_COUNT;
        }
        combo.setVisibleItemCount(visibleItemCount);

        if (tip != null) {
            combo.setToolTipText(tip);
        }
        combo.setEnabled(false);

        addControls(parent);
    }

    /**
     * addControls to Checkbox and Combo.
     */
    private void addControls(Composite parent) {

        checkbox.addSelectionListener(new SelectionAdapter() {

            private String oldValue = ""; //$NON-NLS-1$

            public void widgetSelected(final SelectionEvent e) {
                if (checkbox.getSelection()) {
                    // is checked
                    combo.setEnabled(true);
                    combo.setText(oldValue);
                    combo.forceFocus();
                } else {
                    // isn't checked
                    oldValue = combo.getText();
                    combo.setEnabled(false);
                    combo.setText(""); //$NON-NLS-1$
                }
            }
        });
    }

    /**
     * return Object Combo.
     * 
     * @return Combo
     */
    public Combo getCombo() {
        return combo;
    }

    /**
     * return Object Checkbox.
     * 
     * @return Button Checkbox
     */
    public Button getCheckbox() {
        return checkbox;
    }

    /**
     * return SelectionIndex of the combo.
     * 
     * @return int
     */
    public int getSelectionIndex() {
        return combo.getSelectionIndex();
    }

    /**
     * select a item of the combo.
     * 
     * @param index
     */
    public void select(int index) {
        checkbox.setSelection(true);
        combo.setEnabled(true);
        combo.select(index);
    }

    /**
     * setVisibleItemCount of the combo.
     * 
     * @param size
     */
    public void setVisibleItemCount(int size) {
        combo.setVisibleItemCount(size);
    }

    /**
     * getItem of the combo.
     * 
     * @param index
     * @return int
     */
    public String getItem(int index) {
        return combo.getItem(index);
    }

    /**
     * addModifyListener to the combo and the checkbox.
     * 
     * @param listener
     */
    public void addModifyListener(ModifyListener listener) {
        combo.addModifyListener(listener);
    }

    /**
     * addModifyListener to the combo and the checkbox.
     * 
     * @param listener
     */
    public void addSelectionListener(SelectionListener listener) {
        checkbox.addSelectionListener(listener);
    }

    /**
     * deselect All Item of the combo.
     */
    public void deselectAll() {
        checkbox.setSelection(false);
        combo.setEnabled(false);
        combo.deselectAll();
    }

    /**
     * deselect a Item of the combo.
     * 
     * @param index
     */
    public void deselect(Integer index) {
        combo.deselect(index);
    }

    /**
     * forceFocus.
     * 
     * @return
     */
    public void forceFocus() {
        if (combo.getEnabled()) {
            combo.forceFocus();
        } else {
            checkbox.forceFocus();
        }
    }

    /**
     * addKeyListener to the combo.
     * 
     * @param adapter
     */
    public void addKeyListener(KeyAdapter adapter) {
        combo.addKeyListener(adapter);
    }

    /**
     * getText to the combo.
     * 
     * @return string
     */
    public String getText() {
        return combo.getText();
    }

    /**
     * clearSelection to the combo.
     * 
     * @return
     */
    public void clearSelection() {
        combo.clearSelection();
    }

    /**
     * isInteger.
     * 
     * @return boolean
     */
    public Boolean isInteger() {
        String s = combo.getText();
        if ("".equals(s)) { //$NON-NLS-1$
            return false;
        } else {
            for (int f = 0; f < s.length(); f++) {
                char c = s.charAt(f);
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * isEmpty.
     * 
     * @return boolean
     */
    public Boolean isEmpty() {
        return combo.getText().equals(""); //$NON-NLS-1$
    }

    /**
     * DOC ocarbone Comment method "getLabelText".
     * 
     * @return string
     */
    public String getLabelText() {
        return label.getText();
    }

    /**
     * checkbox.getSelection().
     * 
     * @return boolean
     */
    public boolean isChecked() {
        return checkbox.getSelection();
    }

    /**
     * combo.setText() and checkbox.setSelection(true).
     * 
     * @param i
     */
    public void setText(String string) {
        checkbox.setSelection(true);
        combo.setEnabled(true);
        combo.setText(string);
    }

    /**
     * DOC ocarbone Comment method "setReadOnly".
     * 
     * @param b
     */
    public void setReadOnly(boolean visible) {
        label.setEnabled(!visible);
        combo.setEnabled(!visible);
        checkbox.setEnabled(!visible);
    }
}
