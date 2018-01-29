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
package org.talend.commons.ui.runtime.swt.tableviewer.celleditor;

import java.util.List;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.talend.commons.runtime.model.expressionbuilder.Variable;
import org.talend.commons.ui.runtime.expressionbuilder.IExpressionDataBean;
import org.talend.commons.ui.runtime.expressionbuilder.IExtendedCellEditorBehavior;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: ExtendedTextCellEditor.java 下午03:55:26 2007-8-1 +0000 (2007-8-1) yzhang $
 * 
 */
public class ExtendedTextCellEditor extends TextCellEditor implements IExpressionDataBean {

    private IExtendedCellEditorBehavior cellEditorBehavior;

    private ModifyListener modifyListener;

    private Object data;

    /**
     * State information for updating action enablement
     */
    private boolean isSelection = false;

    private boolean isDeleteable = false;

    private boolean isSelectable = false;

    private final Composite parent;

    private String expressionType;

    /**
     * yzhang ExtendedTextCellEditor constructor comment.
     * 
     * @param behavior
     */
    public ExtendedTextCellEditor(Composite parent, CellEditorDialogBehavior cellEditorBehavior) {
        super(parent);
        this.cellEditorBehavior = cellEditorBehavior;
        this.parent = parent;
        setStyle(SWT.SINGLE);
        cellEditorBehavior.setExtendedTextCellEditor(this);
        differedCreate(parent);
    }

    private void initFocusListener(Composite composite) {
        if (composite instanceof Shell) {
            Shell shell = (Shell) composite;
            for (Control control : shell.getChildren()) {
                addFocusListenerToControl(control);
            }
        } else {
            initFocusListener(composite.getParent());
        }

    }

    private void addFocusListenerToControl(Control control) {
        if (control instanceof Composite) {
            Composite composite = (Composite) control;
            for (Control con : composite.getChildren()) {
                addFocusListenerToControl(con);
            }
        }
        control.addFocusListener(new FocusAdapter() {

            /*
             * (non-Javadoc)
             * 
             * @see org.eclipse.swt.events.FocusAdapter#focusGained(org.eclipse.swt.events.FocusEvent)
             */
            @Override
            public void focusGained(FocusEvent e) {
                ExtendedTextCellEditor.this.focusLost();
            }
        });

    }

    /**
     * Warning: this method must not be implemented, see differedCreate(..) which is called by the constructor
     */
    @Override
    public final void create(Composite parent) {
    }

    protected void differedCreate(Composite parent) {
        super.create(parent);
        // initFocusListener(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.TextCellEditor#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createControl(Composite parent) {

        return cellEditorBehavior.createBehaviorControls(parent);

    }

    public Text getTextControl() {
        return text;
    }

    public Text createText(Composite parent) {

        text = new Text(parent, SWT.NONE);
        text.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                handleDefaultSelection(e);
            }
        });
        text.addKeyListener(new KeyAdapter() {

            // hook key pressed - see PR 14201
            @Override
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);

                // as a result of processing the above call, clients may have
                // disposed this cell editor
                if ((getControl() == null) || getControl().isDisposed()) {
                    return;
                }
                checkSelection(); // see explaination below
                checkDeleteable();
                checkSelectable();
            }
        });
        text.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });
        // We really want a selection listener but it is not supported so we
        // use a key listener and a mouse listener to know when selection
        // changes
        // may have occured
        text.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseUp(MouseEvent e) {
                checkSelection();
                checkDeleteable();
                checkSelectable();
            }
        });

        text.setFont(parent.getFont());
        text.setBackground(parent.getBackground());
        text.setText("");//$NON-NLS-1$
        text.addModifyListener(getModifyListener());
        return text;

    }

    /**
     * Return the modify listener.
     */
    public ModifyListener getModifyListener() {
        if (modifyListener == null) {
            modifyListener = new ModifyListener() {

                public void modifyText(ModifyEvent e) {
                    editOccured(e);
                }
            };
        }
        return modifyListener;
    }

    /**
     * yzhang Comment method "checkSelection".
     */
    public void checkSelection() {
        boolean oldIsSelection = isSelection;
        isSelection = text.getSelectionCount() > 0;
        if (oldIsSelection != isSelection) {
            fireEnablementChanged(COPY);
            fireEnablementChanged(CUT);
        }
    }

    /**
     * yzhang Comment method "checkDeleteable".
     */
    public void checkDeleteable() {
        boolean oldIsDeleteable = isDeleteable;
        isDeleteable = isDeleteEnabled();
        if (oldIsDeleteable != isDeleteable) {
            fireEnablementChanged(DELETE);
        }
    }

    /**
     * yzhang Comment method "checkSelectable".
     */
    public void checkSelectable() {
        boolean oldIsSelectable = isSelectable;
        isSelectable = isSelectAllEnabled();
        if (oldIsSelectable != isSelectable) {
            fireEnablementChanged(SELECT_ALL);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionConsumer#setConsumerExpression(java.lang.String)
     */
    public void setConsumerExpression(String expression) {
        text.setText(expression);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionDataBean#getExpression()
     */
    public String getExpression() {
        return text.getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionDataBean#getVariables()
     */
    public List<Variable> getVariables() {
        if (this.data instanceof List) {
            return (List<Variable>) this.data;
        }
        return null;
    }

    private String ownerId;

    /**
     * Sets the ownerId.
     * 
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionDataBean#getOwnerId()
     */
    public String getOwnerId() {
        return this.ownerId;
    }

    private String path;

    /**
     * yzhang Comment method "setData".
     * 
     * @param data
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * yzhang Comment method "getData".
     * 
     * @return
     */
    public Object getData() {
        return this.data;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.CellEditor#focusLost()
     */
    @Override
    public void focusLost() {
        super.focusLost();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionDataBean#getExpressionType()
     */
    public String getExpressionType() {
        return expressionType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.talend.expressionbuilder.IExpressionDataBean#setExpressionType(java.lang.String)
     */
    public void setExpressionType(String expressionType) {
        this.expressionType = expressionType;
    }

}
