// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
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

import org.eclipse.nebula.widgets.nattable.edit.editor.AbstractCellEditor;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer.MoveDirectionEnum;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.widget.EditModeEnum;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.utils.PasswordEncryptUtil;
import org.talend.core.model.process.IContextParameter;

public class CustomTextCellEditor extends AbstractCellEditor {

    private final IContextParameter realPara;

    private final IStyle cellStyle;

    private final boolean commitOnUpDown;

    private final boolean moveSelectionOnEnter;

    protected boolean freeEdit = false;

    protected boolean commitOnEnter = true;

    /*
     * The wrapped editor control.
     */
    private ContextValuesNatText buttonText;

    /**
     * if password, the value will be * always. should find out the real value.
     */
    private Object recordOriginalCanonicalValue;

    public CustomTextCellEditor(IContextParameter realPara, IStyle cellStyle, boolean commitOnUpDown, boolean moveSelectionOnEnter) {
        this.realPara = realPara;
        this.cellStyle = cellStyle;
        this.commitOnUpDown = commitOnUpDown;
        this.moveSelectionOnEnter = moveSelectionOnEnter;
    }

    @Override
    public Control createEditorControl(Composite parentComp) {
        int style = this.editMode == EditModeEnum.INLINE ? SWT.NONE : SWT.BORDER;
        if (!this.freeEdit) {
            style |= SWT.READ_ONLY;
        }
        final ContextValuesNatText text = new ContextValuesNatText(parentComp, cellStyle, realPara, style);

        addTextListener(text);
        return text;

    }

    protected void addTextListener(final ContextValuesNatText text) {
        text.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (commitOnEnter && (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)) {
                    MoveDirectionEnum move = MoveDirectionEnum.NONE;
                    if (moveSelectionOnEnter && editMode == EditModeEnum.INLINE) {
                        if (event.stateMask == 0) {
                            move = MoveDirectionEnum.DOWN;
                        } else if (event.stateMask == SWT.SHIFT) {
                            move = MoveDirectionEnum.UP;
                        }
                    }

                    commit(move);
                } else if (event.keyCode == SWT.ESC && event.stateMask == 0) {
                    close();
                } else if (commitOnUpDown && editMode == EditModeEnum.INLINE) {
                    if (event.keyCode == SWT.ARROW_UP) {
                        commit(MoveDirectionEnum.UP);
                    } else if (event.keyCode == SWT.ARROW_DOWN) {
                        commit(MoveDirectionEnum.DOWN);
                    }
                }
            }

        });

        // text.addFocusListener(new FocusAdapter() {
        //
        // @Override
        // public void focusLost(FocusEvent e) {
        // commit(MoveDirectionEnum.NONE, editMode == EditModeEnum.INLINE);
        // }
        // });
    }

    @Override
    public Control getEditorControl() {
        return this.buttonText;
    }

    @Override
    public Object getEditorValue() {
        return this.buttonText.getValue();
    }

    @Override
    public void setEditorValue(Object value) {
        this.buttonText.setValue(value != null && value.toString().length() > 0 ? value.toString() : ""); //$NON-NLS-1$

    }

    @Override
    public Control activateCell(Composite parentComp, Object originalCanonicalValue) {
        this.recordOriginalCanonicalValue = originalCanonicalValue;
        if (PasswordEncryptUtil.isPasswordType(realPara.getType())) { // if pasword, get the real one.
            this.recordOriginalCanonicalValue = realPara.getValue(); // correct the value.
        }

        this.buttonText = (ContextValuesNatText) createEditorControl(parentComp);
        // use the real value.
        setCanonicalValue(this.recordOriginalCanonicalValue);

        Text text = buttonText.getText();
        if (buttonText.getButton() == null) {
            text.forceFocus();
        }
        text.setSelection(0, text.getText().length());

        return this.buttonText;
    }

    public boolean isFreeEdit() {
        return this.freeEdit;
    }

    public void setFreeEdit(boolean freeEdit) {
        this.freeEdit = freeEdit;
    }

}
