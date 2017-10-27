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

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Create Label and Combo.
 * 
 * $Id: LabelledCombo.java 7496 2007-12-07 12:26:00Z mhelleboid $
 * 
 */
public class LabelledCombo {

    /**
     * 
     */

    private Label label;

    private Combo combo;

    public static final int MAX_VISIBLE_ITEM_COUNT = 25;

    private static final int DEFAULT_COMBO_STYLE = SWT.READ_ONLY;

    private static final int DEFAULT_LABEL_STYLE = SWT.LEFT;

    public LabelledCombo(Label label, Combo combo) {
        this.label = label;
        this.combo = combo;
    }

    /**
     * create Label and Combo width a List of String.
     * 
     * @param Composite parent
     * @param String label
     * 
     * @param String tip
     * @param List <String> item
     * 
     * @return Combo
     */
    public LabelledCombo(Composite parent, String labelStr, String tip, List<String> item) {
        String[] items = null;
        if (item != null && !item.isEmpty()) {
            items = new String[item.size()];
            Iterator<String> iterate = item.iterator();
            int i = 0;
            while (iterate.hasNext()) {
                items[i] = iterate.next();
                i++;
            }
        }
        createLabelledCombo(parent, labelStr, tip, items, 1, false, DEFAULT_COMBO_STYLE, DEFAULT_LABEL_STYLE);
    }

    public LabelledCombo(Composite parent, String labelStr, String tip, List<String> item, boolean isFill) {
        String[] items = null;
        if (item != null && !item.isEmpty()) {
            items = new String[item.size()];
            Iterator<String> iterate = item.iterator();
            int i = 0;
            while (iterate.hasNext()) {
                items[i] = iterate.next();
                i++;
            }
        }
        createLabelledCombo(parent, labelStr, tip, items, 1, isFill, DEFAULT_COMBO_STYLE, DEFAULT_LABEL_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param String [] item
     * @param int horizontalSpan
     * @param boolean isFill
     * 
     * @return Combo
     */
    public LabelledCombo(final Composite parent, final String labelStr, final String tip, final String[] item,
            int horizontalSpan, boolean isFill) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, isFill, DEFAULT_COMBO_STYLE, DEFAULT_LABEL_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param String [] item
     * @param int horizontalSpan
     * 
     * @return Combo
     */
    public LabelledCombo(Composite parent, String labelStr, String tip, String[] item, int horizontalSpan) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, false, DEFAULT_COMBO_STYLE, DEFAULT_LABEL_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param String [] item
     * 
     * @return Combo
     */
    public LabelledCombo(Composite parent, String labelStr, String tip, String[] item) {
        createLabelledCombo(parent, labelStr, tip, item, 1, false, DEFAULT_COMBO_STYLE, DEFAULT_LABEL_STYLE);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param List <String> item
     * @param int horizontalSpan
     * @param boolean isFill
     * @param int style
     */
    public LabelledCombo(final Composite parent, final String labelStr, final String tip, final String[] item,
            final int horizontalSpan, final boolean isFill, final int comboStyle) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, isFill, comboStyle, SWT.LEFT);
    }

    /**
     * create Label and Combo width a Array of String.
     * 
     * @param Composite parent
     * @param String label
     * @param String tip
     * @param List <String> item
     * @param int horizontalSpan
     * @param boolean isFill
     * @param int comboStyle
     * @param int labelStyle
     */

    public LabelledCombo(final Composite parent, final String labelStr, final String tip, final String[] item,
            final int horizontalSpan, final boolean isFill, final int comboStyle, int labelStyle) {
        createLabelledCombo(parent, labelStr, tip, item, horizontalSpan, isFill, comboStyle, labelStyle);
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
     * @param comboStyle
     * @param labelStyle
     */
    private void createLabelledCombo(final Composite parent, final String labelStr, final String tip, final String[] item,
            final int horizontalSpan, final boolean isFill, final int comboStyle, final int labelStyle) {

        label = new Label(parent, labelStyle);
        if (labelStr != null) {
            label.setText(labelStr);
        }
        label.addListener(SWT.MouseEnter, new Listener() {

            public void handleEvent(final Event e) {
                combo.forceFocus();
            }
        });

        combo = new Combo(parent, comboStyle);
        layoutWidgets(horizontalSpan, isFill);

        if (item != null) {
            for (int i = 0; i < item.length; i++) {
                combo.add(item[i]);
            }
        }

        if (combo.getItemCount() > MAX_VISIBLE_ITEM_COUNT) {
            combo.setVisibleItemCount(MAX_VISIBLE_ITEM_COUNT);
        }

        if (tip != null) {
            combo.setToolTipText(tip);
        }
    }

    /**
     * DOC YeXiaowei Comment method "layoutWidgets".
     * 
     * @param horizontalSpan
     * @param isFill
     */
    private void layoutWidgets(final int horizontalSpan, final boolean isFill) {
        label.setLayoutData(new GridData());

        GridData gridData = null;
        if (isFill) {
            gridData = new GridData(GridData.FILL_HORIZONTAL);
        } else {
            gridData = new GridData();
        }
        gridData.horizontalSpan = horizontalSpan;
        combo.setLayoutData(gridData);
    }

    /**
     * 
     * DOC YeXiaowei Comment method "hideWidgets".
     * 
     * @param hide
     */
    public void setHideWidgets(final boolean hide) {

        GridData dataLabel = (GridData) label.getLayoutData();
        dataLabel.exclude = hide;
        label.setLayoutData(dataLabel);
        GridData dataCombo = (GridData) combo.getLayoutData();
        dataCombo.exclude = hide;
        combo.setLayoutData(dataCombo);

        setVisible(!hide);// Must also set exclude and visible can affect the
        // layout

        if (combo.getParent() != null)
            combo.getParent().layout();
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
     * addModifyListener to the combo.
     * 
     * @param listener
     */
    public void addModifyListener(ModifyListener listener) {
        combo.addModifyListener(listener);
    }

    /**
     * forceFocus.
     */
    public void forceFocus() {
        combo.forceFocus();
    }

    /**
     * addKeyListener to the combo.
     * 
     * @param adapter
     */
    public void addKeyListener(KeyAdapter adapter) {
        combo.addKeyListener(adapter);
    }

    public String getText() {
        if (combo.getSelectionIndex() < -1) {
            return combo.getItem(combo.getSelectionIndex());
        } else {
            return getCombo().getText();
        }
    }

    /**
     * clearSelection to the combo.
     */
    public void clearSelection() {
        combo.clearSelection();
        combo.setSelection(new Point(1, 1));
    }

    /**
     * deselectAll to the combo.
     */
    public void deselectAll() {
        combo.deselectAll();
    }

    /**
     * removeAll to the combo.
     */
    public void removeAll() {
        combo.removeAll();
    }

    /**
     * setEnabled to the label and the combo.
     */
    public void setEnabled(Boolean b) {
        label.setEnabled(b);
        combo.setEnabled(b);
    }

    public boolean isEnabled() {
        return combo.isEnabled();
    }

    /**
     * combo.getItemCount().
     * 
     * @return
     */
    public int getItemCount() {
        return combo.getItemCount();
    }

    /**
     * combo.add().
     * 
     * @param string
     */
    public void add(String string) {
        combo.add(string);
    }

    /**
     * combo.setText (use less SWT.READ_ONLY).
     * 
     * @param escapeCharComboOldValue
     */
    public void setText(String string) {
        if (string != null) {
            combo.setText(string);
        } else {
            combo.setText(""); //$NON-NLS-1$
        }
    }

    /**
     * label.getText().
     * 
     * @return string
     */
    public String getLabel() {
        return label.getText();
    }

    /**
     * DOC ocarbone Comment method "setReadOnly".
     * 
     * @param b
     */
    public void setReadOnly(boolean visible) {
        label.setEnabled(true);
        combo.setEnabled(!visible);
    }

    public void setVisible(boolean b) {
        label.setVisible(b);
        combo.setVisible(b);
    }

}
