// ============================================================================
//
// Copyright (C) 2006-2014 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.core.ui.context.nattableTree;

import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.core.model.process.IContextParameter;

/**
 * created by ldong on Jul 29, 2014 Detailled comment
 * 
 */
public class ContextValuesNatText extends Composite {

    protected final IStyle cellStyle;

    private IContextParameter realPara;

    /**
     * The text control of this NatText, allowing to enter values directly.
     */
    protected Text text;

    protected Shell buttonShell;

    protected Button button;

    protected final int style;

    protected boolean freeEdit;

    private NatTableCellEditorFactory cellFactory;

    /**
     * Flag to determine whether the button was hidden on focus lost.
     */
    private boolean hideByFocusLost = false;

    public ContextValuesNatText(Composite parent, IStyle cellStyle, IContextParameter realPara, int style) {
        this(parent, cellStyle, style, realPara);
    }

    public ContextValuesNatText(Composite parent, IStyle cellStyle, int style, IContextParameter realPara) {
        super(parent, SWT.NONE);

        this.cellStyle = cellStyle;

        this.style = style;

        this.freeEdit = true;

        this.realPara = realPara;

        this.cellFactory = new NatTableCellEditorFactory();

        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);

        createTextAndButtonControl(style);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (button != null) {
            button.dispose();
        }
        text.dispose();
    }

    protected void createTextAndButtonControl(int style) {
        int widgetStyle = style | HorizontalAlignmentEnum.getSWTStyle(cellStyle);
        if (NatTableCellEditorFactory.isPassword(realPara.getType())) {
            widgetStyle = SWT.PASSWORD | HorizontalAlignmentEnum.getSWTStyle(cellStyle);
        }
        text = new Text(this, widgetStyle);
        text.setBackground(cellStyle.getAttributeValue(CellStyleAttributes.BACKGROUND_COLOR));
        text.setForeground(cellStyle.getAttributeValue(CellStyleAttributes.FOREGROUND_COLOR));
        text.setFont(cellStyle.getAttributeValue(CellStyleAttributes.FONT));

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        text.setLayoutData(gridData);

        if (cellFactory.isSpecialType(realPara)) {
            button = new Button(this, widgetStyle);
            Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            button.setSize(size);
            button.setVisible(true);
            gridData = new GridData(GridData.BEGINNING, SWT.FILL, false, true);
            button.setLayoutData(gridData);

            button.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseDown(MouseEvent e) {
                    updateTextControl(true);
                }

            });

            button.addFocusListener(new FocusAdapter() {

                @Override
                public void focusLost(FocusEvent e) {
                    hideButtonCanvaControl(true);
                    if (freeEdit) {
                        text.forceFocus();
                    }
                }
            });
        }
    }

    protected void createButtonControl(int style) {
        buttonShell = new Shell(getShell(), SWT.MODELESS);
        buttonShell.setLayout(new FillLayout());

        int buttonStyle = SWT.NULL;
    }

    protected void updateTextControl(boolean focusOnText) {
        text.setText(getTransformedTextForDialog(focusOnText));
        // realPara.setValue(getValue());
        if (focusOnText) {
            hideButtonCanvaControl(true);
        }
    }

    /**
     * Hide the buttonCanva of cell
     */
    public void hideButtonCanvaControl(boolean visible) {
        button.setVisible(visible);
    }

    protected void calculateBounds() {
        if (buttonShell != null && !buttonShell.isDisposed()) {
            Point size = getSize();
            button.setSize(size.x, size.y);

            Point textPosition = text.toDisplay(text.getLocation());

            buttonShell.setBounds(textPosition.x, textPosition.y + text.getBounds().height, size.x, size.y);
        }
    }

    public String getValue() {
        return this.text.getText();
    }

    public void setValue(Object value) {
        this.text.setText(value != null && value.toString().length() > 0 ? value.toString() : "");
    }

    @Override
    public void addKeyListener(KeyListener listener) {
        this.text.addKeyListener(listener);
    }

    @Override
    public void addMouseListener(MouseListener listener) {
    }

    @Override
    public void notifyListeners(int eventType, Event event) {
        // this.button.notifyListeners(eventType, event);
    }

    public void addSelectionListener(SelectionListener listener) {
        // this.button.addSelectionListener(listener);
    }

    public void addShellListener(ShellListener listener) {
        this.buttonShell.addShellListener(listener);
    }

    /**
     * Transforms the subDialog's result.
     */
    protected String getTransformedSelection(boolean focusOnText) {
        Object result = null;
        result = cellFactory.openCustomCellEditor(realPara, getShell());
        String finalResult = "";
        if (result == null || result.equals("")) {
            result = this.text.getText();
        }
        if (result != null) {
            if (result instanceof String[]) {
                // for the list value the text show display value
                finalResult = realPara.getDisplayValue();
            } else {
                finalResult = (String) result;
            }
        }
        return finalResult;
    }

    protected String getTransformedTextForDialog(boolean focusOnText) {
        String result = "";
        result = getTransformedSelection(focusOnText);
        return result;
    }

}
