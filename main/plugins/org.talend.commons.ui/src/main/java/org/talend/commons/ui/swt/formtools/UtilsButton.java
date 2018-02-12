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
package org.talend.commons.ui.swt.formtools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Dimensionned Button ( defined width & height OR use FillLayout).
 * 
 * $Id: UtilsButton.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class UtilsButton {

    Button button;

    /**
     * create Button width a RowData.
     * 
     * @param Composite parent
     * @param String string
     * @param int width
     * @param int height
     * 
     * @return Button
     */
    public UtilsButton(Composite parent, String string, int width, int height) {
        parent.setLayout(new RowLayout());
        createButton(parent, string, false);
        button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    }

    /**
     * create Button width a possible FillLayout.
     * 
     * @param Composite parent
     * @param String string
     * @param boolean useFillLayout (use full space)
     * 
     * @return Button
     */
    public UtilsButton(Composite parent, String string, boolean useFillLayout) {
        createButton(parent, string, useFillLayout);
    }

    /**
     * create Button width image less FillLayout.
     * 
     * @param Composite parent
     * @param Image image
     * @param String string
     */
    public UtilsButton(Composite parent, Image image, String string) {
        button = new Button(parent, SWT.PUSH);
        button.setImage(image);
        button.setToolTipText(string);
    }

    /**
     * 
     * create Button with related layoutData.
     * 
     * @param parent
     * @param string
     * @param layoutData
     */
    public UtilsButton(Composite parent, String string, Object layoutData) {

        button = new Button(parent, SWT.PUSH);
        button.setText(string);
        button.setLayoutData(layoutData);

    }

    /**
     * create Button width a possible FillLayout.
     * 
     * @param Composite parent
     * @param String string
     * @param boolean useFillLayout (use full space)
     * 
     * @return Button
     */
    private void createButton(Composite parent, String string, boolean useFillLayout) {
        button = new Button(parent, SWT.PUSH);
        button.setText(string);
        if (useFillLayout) {
            FillLayout fillLayout = new FillLayout();
            parent.setLayout(fillLayout);
        }
    }

    /**
     * button.setEnabled().
     * 
     * @param boolean
     */
    public void setEnabled(boolean b) {
        button.setEnabled(b);
    }

    /**
     * setImage button.
     * 
     * @param image
     */
    public void setImage(Image image) {
        button.setImage(image);
    }

    /**
     * button.addSelectionListener().
     * 
     * @param adapter
     */
    public void addSelectionListener(SelectionAdapter adapter) {
        button.addSelectionListener(adapter);
    }

    /**
     * button.forceFocus().
     */
    public void forceFocus() {
        button.forceFocus();
    }

    /**
     * button.getEnabled ().
     * 
     * @return
     */
    public boolean getEnabled() {
        return button.getEnabled();
    }

    /**
     * button.setToolTipText().
     * 
     * @param string
     */
    public void setToolTipText(String string) {
        button.setToolTipText(string);
    }

    /**
     * button.setVisible().
     * 
     * @param boolean
     */
    public void setVisible(boolean visible) {
        button.setVisible(visible);
    }

    /**
     * button.setText().
     * 
     * @param string
     */
    public void setText(String string) {
        button.setText(string);
    }

    /**
     * button.addMouseListener().
     * 
     * @param adapter
     */
    public void addMouseListener(MouseAdapter adapter) {
        button.addMouseListener(adapter);
    }

    /**
     * button.getText().
     * 
     * @return string
     */
    public String getText() {
        return button.getText();
    }

    public Control getControl() {
        return button;
    }
}
