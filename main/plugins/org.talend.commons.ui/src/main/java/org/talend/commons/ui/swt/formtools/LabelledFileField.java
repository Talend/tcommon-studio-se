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

import java.util.concurrent.Callable;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.exception.ExceptionHandler;
import org.talend.commons.ui.runtime.i18n.Messages;
import org.talend.commons.ui.utils.PathUtils;

/**
 * Create a Label and a Text.
 * 
 * $Id: LabelledFileField.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public class LabelledFileField {

    private Text text;

    private Label label;

    private Button button;

    private Composite compositeButton;

    private String result;

    private static final int DEFAULT_FIELD_STYLE = SWT.BORDER | SWT.SINGLE;

    /**
     * will be called after clicked finish button of browse file dialog
     */
    private Callable afterSetNewValueCallable;

    /**
     * Create a Label and a Text.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     */
    public LabelledFileField(Composite composite, String string, String[] extensions) {
        createFileField(composite, string, extensions, 1, DEFAULT_FIELD_STYLE, true);
    }

    /**
     * Create a Label and a Text.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     * @param isFill
     */
    public LabelledFileField(Composite composite, String string, String[] extensions, boolean isFill) {
        createFileField(composite, string, extensions, 1, DEFAULT_FIELD_STYLE, isFill);
    }

    /**
     * Create a Label and a Text width specific styleField.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     * @param int horizontalSpan
     */
    public LabelledFileField(Composite composite, String string, String[] extensions, int horizontalSpan) {
        createFileField(composite, string, extensions, horizontalSpan, DEFAULT_FIELD_STYLE, true);
    }

    /**
     * Create a Label and a Text width specific styleField.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     * @param int horizontalSpan
     * @param styleField
     */
    public LabelledFileField(Composite composite, String string, String[] extensions, int horizontalSpan, int styleField) {
        createFileField(composite, string, extensions, horizontalSpan, styleField, true);
    }

    /**
     * Create a Label and a Text width Gridata option FILL.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     * @param styleField
     * @param int horizontalSpan
     * @param isFill
     */
    public LabelledFileField(Composite composite, String string, String[] extensions, int horizontalSpan, boolean isFill) {
        createFileField(composite, string, extensions, horizontalSpan, DEFAULT_FIELD_STYLE, isFill);
    }

    /**
     * Create a Label and a Text width specific styleField and Gridata option FILL.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     * @param int horizontalSpan
     * @param styleField
     * @param isFill
     */
    public LabelledFileField(Composite composite, String string, String[] extensions, int horizontalSpan, int styleField,
            boolean isFill) {
        createFileField(composite, string, extensions, horizontalSpan, styleField, isFill);
    }

    /**
     * Create a Label and a Text width specific styleField and Gridata option FILL.
     * 
     * @param composite
     * @param string
     * @param String[] extensions
     * @param int horizontalSpan
     * @param styleField
     * @param isFill
     */
    private void createFileField(final Composite composite, String string, final String[] extensions, int horizontalSpan,
            int styleField, boolean isFill) {
        label = new Label(composite, SWT.LEFT);
        label.setText(string);
        label.addListener(SWT.MouseEnter, new Listener() {

            public void handleEvent(final Event e) {
                button.forceFocus();
            }
        });
        GridDataFactory.swtDefaults().applyTo(label);

        text = new Text(composite, styleField);
        text.selectAll(); // enable fast erase use
        int gridDataStyle = SWT.NONE;
        if (isFill) {
            gridDataStyle = SWT.FILL;
        }
        GridData gridData = new GridData(gridDataStyle, SWT.CENTER, true, false);
        gridData.horizontalSpan = horizontalSpan;
        text.setLayoutData(gridData);

        compositeButton = new Composite(composite, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.marginBottom = 0;
        gridLayout.marginLeft = 0;
        gridLayout.marginRight = 0;
        gridLayout.marginTop = 0;
        gridLayout.marginWidth = 0;

        compositeButton.setLayout(gridLayout);
        compositeButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        button = new Button(compositeButton, SWT.PUSH);
        GridDataFactory.swtDefaults().applyTo(button);
        button.setText(Messages.getString("LabelledFileField.BrowseButton.Text")); //$NON-NLS-1$

        // The action of the button "Browse..."
        button.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent e) {
                displayDialog(composite, extensions);
            }

        });

        compositeButton.addListener(SWT.MouseDown, new Listener() {

            // Event FocusIn
            public void handleEvent(final Event e) {
                if (!button.getEnabled()) {
                    text.forceFocus();
                    if (text.getEditable()) {
                        displayDialog(composite, extensions);
                    }
                }
            }
        });
    }

    /**
     * displayDialog to choose a file path.
     * 
     * @param composite
     * @param String[] extensions
     */
    protected void displayDialog(Composite composite, String[] extensions) {
        FileDialog fileDialog = new FileDialog(composite.getShell(), SWT.OPEN);
        fileDialog.setFileName(new Path(text.getText()).toOSString());
        fileDialog.setText(Messages.getString("LabelledFileField.FileDialog.Text") + label.getText()); //$NON-NLS-1$
        if (extensions != null) {
            fileDialog.setFilterExtensions(extensions);
        }
        // fileDialog.setFilterNames(filterNames);
        result = fileDialog.open();
        setFileFieldValue(result);
    }

    protected void setFileFieldValue(String result) {
        if (result != null) {
            text.setText(PathUtils.getPortablePath(result));
        }
        if (afterSetNewValueCallable != null) {
            try {
                afterSetNewValueCallable.call();
            } catch (Exception e) {
                ExceptionHandler.process(e);
            }
        }
    }

    /**
     * setTextLimit to Text Object.
     * 
     * @param limit
     */
    public void setTextLimit(final int limit) {
        text.setTextLimit(limit);
    }

    /**
     * setToolTipText to Text Object.
     * 
     * @param string
     */
    public void setToolTipText(final String string) {
        text.setToolTipText(string);
        button.setToolTipText(string);
    }

    /**
     * getText to Text Object.
     * 
     * @return string
     */
    public String getText() {
        if (text.getText() == null) {
            return ""; //$NON-NLS-1$
        }
        return text.getText().replace("\\", "\\\\"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * setText to Text Object.
     * 
     * @param string
     */
    public void setText(final String string) {
        if (string != null) {
            text.setText(string);
        } else {
            text.setText(""); //$NON-NLS-1$
        }
    }

    /**
     * setText to Label Object.
     * 
     * @param string
     */
    public void setLabelText(final String string) {
        if (string != null) {
            label.setText(string);
        } else {
            label.setText(""); //$NON-NLS-1$
        }
    }

    public String getLabelText() {
        return label.getText();
    }

    /**
     * setEditable to Text and Label Object.
     * 
     * @param boolean
     */
    public void forceFocus() {
        setEditable(true);
        button.forceFocus();
    }

    /**
     * setEditable to Text and Label Object.
     * 
     * @param boolean
     */
    public void setEditable(final boolean visible) {
        text.setEditable(visible);
        label.setEnabled(visible);
        button.setEnabled(visible);
    }

    /**
     * setVisible to Text and Label Object.
     * 
     * @param boolean
     */
    public void setVisible(final boolean visible) {
        text.setVisible(visible);
        label.setVisible(visible);
        button.setVisible(visible);
    }

    public void hide() {
        Control[] controls = new Control[] { label, text, compositeButton, button };
        for (Control control : controls) {
            control.setVisible(false);
            if (control.getLayoutData() instanceof GridData) {
                ((GridData) control.getLayoutData()).exclude = true;
            }
        }
    }

    public void show() {
        Control[] controls = new Control[] { label, text, compositeButton, button };
        for (Control control : controls) {
            control.setVisible(true);
            if (control.getLayoutData() instanceof GridData) {
                ((GridData) control.getLayoutData()).exclude = false;
            }
        }
    }

    /**
     * addListener to Text Object.
     * 
     * @param eventType
     * @param listener
     */
    public void addListener(int eventType, Listener listener) {
        text.addListener(eventType, listener);
    }

    /**
     * addModifyListener to Text Object.
     * 
     * @param listener
     */
    public void addModifyListener(ModifyListener listener) {
        text.addModifyListener(listener);
    }

    /**
     * addSelectionListener to Button Object.
     * 
     * @param listener
     */
    public void addSelectionListener(SelectionListener listener) {
        button.addSelectionListener(listener);
    }

    /**
     * getEditable to Text Object.
     * 
     * @return boolean
     */
    public boolean getEditable() {
        return text.getEditable();
    }

    /**
     * getCharCount to Text Object.
     * 
     * @return int
     */
    public int getCharCount() {
        return text.getCharCount();
    }

    /**
     * DOC ocarbone Comment method "setReadOnly".
     * 
     * @param b
     */
    public void setReadOnly(boolean visible) {
        label.setEnabled(true);
        text.setEnabled(!visible);
        button.setEnabled(!visible);
    }

    public Text getTextControl() {
        return this.text;
    }

    public Button getButtonControl() {
        return this.button;
    }

    public String getResult() {
        return this.result;
    }

    public void setAfterSetNewValueCallable(Callable callable) {
        this.afterSetNewValueCallable = callable;
    }
}
