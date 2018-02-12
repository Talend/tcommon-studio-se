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
package org.talend.core.ui.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.core.ui.i18n.Messages;

/**
 * A dialog that configure the object value sets.
 * 
 * @param <B>
 */
public abstract class ObjectSelectionDialog<B> extends SelectionDialog {

    private LabelProvider labelProvider = null;

    private List<B> data;

    private List removeDataIter;

    protected TableViewer fTableViewer;

    private Button fNewButton;

    private Button fEditButton;

    protected Button fRemoveButton;

    private Button fUpButton;

    private Button fDownButton;

    protected Button fSelectAll;

    protected Button fDeselectAll;

    private Label msgLabel;

    private int nextButtonId = IDialogConstants.CLIENT_ID + 1;

    public ObjectSelectionDialog(Shell parentShell, String title, String message, LabelProvider labelProvider) {
        super(parentShell);
        setTitle(title);
        setMessage(message);
        this.labelProvider = labelProvider;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    public void setLabelProvider(LabelProvider labelProvider) {
        this.labelProvider = labelProvider;
    }

    public void setData(List<B> data) {
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Control createContents(Composite parent) {
        Control control = super.createContents(parent);
        updateButtonAvailability();
        return control;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setFont(parent.getFont());
        msgLabel = createMessageArea(composite);
        // createRemoveArea(composite);
        Composite inner = new Composite(composite, SWT.NONE);
        inner.setFont(composite.getFont());
        inner.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        inner.setLayout(layout);
        createTableViewer(inner);
        createOrderButtons(inner);
        createModifyButtons(composite);
        initTableInput();

        return composite;
    }

    protected void initTableInput() {
        fTableViewer.setInput(data);
    }

    protected void createTableViewer(Composite parent) {
        fTableViewer = new TableViewer(parent);
        initTableViewer(parent);
    }

    /**
     * DOC chuang Comment method "initTableViewer".
     * 
     * @param parent
     */
    protected void initTableViewer(Composite parent) {
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                updateButtonAvailability();
            }
        });

        GridData data = new GridData(GridData.FILL_BOTH);
        data.heightHint = convertHeightInCharsToPixels(20);
        data.widthHint = convertWidthInCharsToPixels(50);
        fTableViewer.getTable().setLayoutData(data);
        fTableViewer.getTable().setFont(parent.getFont());

        fTableViewer.setLabelProvider(labelProvider);
        fTableViewer.setContentProvider(new ArrayContentProvider());
    }

    private void createModifyButtons(Composite composite) {
        Composite buttonComposite = new Composite(composite, SWT.RIGHT);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        buttonComposite.setLayout(layout);
        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_END | GridData.GRAB_HORIZONTAL);
        data.grabExcessHorizontalSpace = true;
        composite.setData(data);

        // fNewButton = createButton(buttonComposite, nextButtonId++,
        // WorkingSetMessages.WorkingSetConfigurationDialog_new_label,
        // false);
        fNewButton = createButton(buttonComposite, nextButtonId++, Messages.getString("WorkingSetConfigurationDialog_new_label"), //$NON-NLS-1$
                false);
        fNewButton.setFont(composite.getFont());
        fNewButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                createElement();
            }
        });

        // fEditButton = createButton(buttonComposite, nextButtonId++,
        // WorkingSetMessages.WorkingSetConfigurationDialog_edit_label,
        // false);
        fEditButton = createButton(buttonComposite, nextButtonId++,
                Messages.getString("WorkingSetConfigurationDialog_edit_label"), false); //$NON-NLS-1$
        fEditButton.setFont(composite.getFont());
        fEditButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                editSelectedElement();
            }
        });

        // fRemoveButton = createButton(buttonComposite, nextButtonId++,
        // WorkingSetMessages.WorkingSetConfigurationDialog_remove_label, false);
        fRemoveButton = createButton(buttonComposite, nextButtonId++,
                Messages.getString("WorkingSetConfigurationDialog_remove_label"), false); //$NON-NLS-1$
        fRemoveButton.setFont(composite.getFont());
        fRemoveButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                removeSelectedContexts();
            }
        });
    }

    protected void createOrderButtons(Composite parent) {
        Composite buttons = new Composite(parent, SWT.NONE);
        buttons.setFont(parent.getFont());
        buttons.setLayoutData(new GridData(GridData.FILL_VERTICAL));
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        buttons.setLayout(layout);

        fUpButton = new Button(buttons, SWT.PUSH);
        // fUpButton.setText(WorkingSetMessages.WorkingSetConfigurationDialog_up_label);
        fUpButton.setText(Messages.getString("WorkingSetConfigurationDialog_up_label")); //$NON-NLS-1$
        fUpButton.setFont(parent.getFont());
        setButtonLayoutData(fUpButton);
        fUpButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveUp(((IStructuredSelection) fTableViewer.getSelection()).toList());
            }
        });

        fDownButton = new Button(buttons, SWT.PUSH);
        // fDownButton.setText(WorkingSetMessages.WorkingSetConfigurationDialog_down_label);
        fDownButton.setText(Messages.getString("WorkingSetConfigurationDialog_down_label")); //$NON-NLS-1$
        fDownButton.setFont(parent.getFont());
        setButtonLayoutData(fDownButton);
        fDownButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                moveDown(((IStructuredSelection) fTableViewer.getSelection()).toList());
            }
        });

        fSelectAll = new Button(buttons, SWT.PUSH);
        // fSelectAll.setText(WorkingSetMessages.WorkingSetConfigurationDialog_selectAll_label);
        fSelectAll.setText(Messages.getString("WorkingSetConfigurationDialog_selectAll_label")); //$NON-NLS-1$
        fSelectAll.setFont(parent.getFont());
        setButtonLayoutData(fSelectAll);
        fSelectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                selectAll();
            }
        });

        fDeselectAll = new Button(buttons, SWT.PUSH);
        // fDeselectAll.setText(WorkingSetMessages.WorkingSetConfigurationDialog_deselectAll_label);
        fDeselectAll.setText(Messages.getString("WorkingSetConfigurationDialog_deselectAll_label")); //$NON-NLS-1$
        fDeselectAll.setFont(parent.getFont());
        setButtonLayoutData(fDeselectAll);
        fDeselectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                deselectAll();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void okPressed() {
        List<B> newResult = getData();
        setResult(newResult);
        super.okPressed();
    }

    public List<B> getData() {
        return data;
    }

    public IStructuredSelection getSelection() {
        return ((IStructuredSelection) fTableViewer.getSelection());
    }

    public List getRemoveData() {
        return this.removeDataIter;
    }

    protected abstract void createElement();

    protected abstract void editSelectedElement();

    /**
     * Removes the selected working sets from the workbench.
     */
    protected void removeSelectedContexts() {
        ISelection selection = fTableViewer.getSelection();
        if (selection instanceof IStructuredSelection) {
            Iterator<B> iter = ((IStructuredSelection) selection).iterator();
            removeDataIter = new ArrayList();
            while (iter.hasNext()) {
                Object obj = iter.next();
                data.remove(obj);
                removeDataIter.add(obj);
            }
        }
        refreshViewer();
    }

    /**
     * bqian Comment method "refreshViewer".
     */
    public void refreshViewer() {
        fTableViewer.refresh();
    }

    /**
     * Updates the modify buttons' enabled state based on the current seleciton.
     */
    protected void updateButtonAvailability() {
        IStructuredSelection selection = (IStructuredSelection) fTableViewer.getSelection();
        boolean hasSelection = !selection.isEmpty();
        boolean hasSingleSelection = selection.size() == 1;
        fEditButton.setEnabled(hasSingleSelection);
        if (fUpButton != null) {
            fUpButton.setEnabled(canMoveUp());
        }
        if (fDownButton != null) {
            fDownButton.setEnabled(canMoveDown());
        }

        fRemoveButton.setEnabled(hasSelection);

    }

    protected void setDisplayMessage(String msg) {
        msgLabel.setText(msg);
    }

    private void moveUp(List toMoveUp) {
        if (toMoveUp.size() > 0) {
            setElements(moveUp(data, toMoveUp));
            fTableViewer.reveal(toMoveUp.get(0));
        }
    }

    private void moveDown(List toMoveDown) {
        if (toMoveDown.size() > 0) {
            setElements(reverse(moveUp(reverse(data), toMoveDown)));
            fTableViewer.reveal(toMoveDown.get(toMoveDown.size() - 1));
        }
    }

    private void setElements(List elements) {
        data = elements;
        fTableViewer.setInput(data);
        updateButtonAvailability();
    }

    private List moveUp(List elements, List move) {
        int nElements = elements.size();
        List res = new ArrayList(nElements);
        Object floating = null;
        for (int i = 0; i < nElements; i++) {
            Object curr = elements.get(i);
            if (move.contains(curr)) {
                res.add(curr);
            } else {
                if (floating != null) {
                    res.add(floating);
                }
                floating = curr;
            }
        }
        if (floating != null) {
            res.add(floating);
        }
        return res;
    }

    private List reverse(List p) {
        List reverse = new ArrayList(p.size());
        for (int i = p.size() - 1; i >= 0; i--) {
            reverse.add(p.get(i));
        }
        return reverse;
    }

    private boolean canMoveUp() {
        int[] indc = fTableViewer.getTable().getSelectionIndices();
        for (int i = 0; i < indc.length; i++) {
            if (indc[i] != i) {
                return true;
            }
        }
        return false;
    }

    private boolean canMoveDown() {
        int[] indc = fTableViewer.getTable().getSelectionIndices();
        int k = data.size() - 1;
        for (int i = indc.length - 1; i >= 0; i--, k--) {
            if (indc[i] != k) {
                return true;
            }
        }
        return false;
    }

    // ---- select / deselect --------------------------------------------------

    private void selectAll() {
        fTableViewer.setSelection(new StructuredSelection(data));
    }

    private void deselectAll() {
        fTableViewer.setSelection(StructuredSelection.EMPTY);
    }

}
