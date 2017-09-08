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
package org.talend.commons.ui.swt.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

/**
 * DOC tguiu class global comment. Detailled comment <br/>
 * 
 * $Id: TableEditor.java 7038 2007-11-15 14:05:48Z plegall $
 * 
 */
public abstract class TableEditor extends FieldEditor {

    protected TableViewer viewer;

    private List<String> list;

    /**
     * The button box containing the Add, Remove, Up, and Down buttons; <code>null</code> if none (before creation or
     * after disposal).
     */
    private Composite buttonBox;

    /**
     * The Add button.
     */
    protected Button addButton;

    /**
     * The Remove button.
     */
    protected Button removeButton;

    /**
     * The Up button.
     */
    protected Button upButton;

    /**
     * The Down button.
     */
    protected Button downButton;

    /**
     * The selection listener.
     */
    private SelectionListener selectionListener;

    public TableEditor(String name, String labelText, Composite parent) {
        init(name, labelText);
        createControl(parent);
    }

    /**
     * Notifies that the Add button has been pressed.
     */
    private void addPressed() {
        setPresentsDefaultValue(false);
        String input = getNewInputObject();

        if (input != null) {
            int index = viewer.getTable().getSelectionIndex();
            if (index >= 0) {
                list.add(index + 1, input);
            } else {
                list.add(input);
            }
            viewer.refresh();
            selectionChanged();
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void adjustForNumColumns(int numColumns) {
        Control control = getLabelControl();
        ((GridData) control.getLayoutData()).horizontalSpan = numColumns;
        ((GridData) viewer.getTable().getLayoutData()).horizontalSpan = numColumns - 1;
    }

    /**
     * Creates the Add, Remove, Up, and Down button in the given button box.
     * 
     * @param box the box for the buttons
     */
    protected void createButtons(Composite box) {
        addButton = createPushButton(box, "ListEditor.add"); //$NON-NLS-1$
        removeButton = createPushButton(box, "ListEditor.remove"); //$NON-NLS-1$
        upButton = createPushButton(box, "ListEditor.up"); //$NON-NLS-1$
        downButton = createPushButton(box, "ListEditor.down"); //$NON-NLS-1$
    }

    /**
     * Helper method to create a push button.
     * 
     * @param parent the parent control
     * @param key the resource name used to supply the button's label text
     * @return Button
     */
    protected Button createPushButton(Composite parent, String key) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(JFaceResources.getString(key));
        button.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        int widthHint = convertHorizontalDLUsToPixels(button, IDialogConstants.BUTTON_WIDTH);
        data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        button.setLayoutData(data);
        button.addSelectionListener(getSelectionListener());
        return button;
    }

    /**
     * Creates a selection listener.
     */
    public void createSelectionListener() {
        selectionListener = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent event) {
                Widget widget = event.widget;
                if (widget == addButton) {
                    addPressed();
                } else if (widget == removeButton) {
                    removePressed();
                } else if (widget == upButton) {
                    upPressed();
                } else if (widget == downButton) {
                    downPressed();
                    // } else if (widget == viewer.getTable()) {
                    // selectionChanged();
                }
            }
        };
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        Control control = getLabelControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = numColumns;
        control.setLayoutData(gd);

        viewer = getTableControl(parent);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.verticalAlignment = GridData.FILL;
        gd.horizontalSpan = numColumns - 1;
        gd.grabExcessHorizontalSpace = true;
        viewer.getTable().setLayoutData(gd);

        buttonBox = getButtonBoxControl(parent);
        gd = new GridData();
        gd.verticalAlignment = GridData.BEGINNING;
        buttonBox.setLayoutData(gd);
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doLoad() {
        if (viewer != null) {
            String s = getPreferenceStore().getString(getPreferenceName());
            if (s != null && !"".equals(s)) { //$NON-NLS-1$
                for (String tmp : readString(s)) {
                    list.add(tmp);
                }
            }
            viewer.setInput(list);
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doLoadDefault() {
        if (viewer != null) {
            list.clear();
            String s = getPreferenceStore().getDefaultString(getPreferenceName());
            if (s != null && !"".equals(s)) { //$NON-NLS-1$
                for (String tmp : readString(s)) {
                    list.add(tmp);
                }
            }
            viewer.setInput(list);
        }
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    protected void doStore() {
        String s = writeString(list);
        if (s != null) {
            getPreferenceStore().setValue(getPreferenceName(), s);
        }
    }

    /**
     * Notifies that the Down button has been pressed.
     */
    private void downPressed() {
        swap(false);
    }

    /**
     * Returns this field editor's button box containing the Add, Remove, Up, and Down button.
     * 
     * @param parent the parent control
     * @return the button box
     */
    public Composite getButtonBoxControl(Composite parent) {
        if (buttonBox == null) {
            buttonBox = new Composite(parent, SWT.NULL);
            GridLayout layout = new GridLayout();
            layout.marginWidth = 0;
            buttonBox.setLayout(layout);
            createButtons(buttonBox);
            buttonBox.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent event) {
                    addButton = null;
                    removeButton = null;
                    upButton = null;
                    downButton = null;
                    buttonBox = null;
                }
            });

        } else {
            checkParent(buttonBox, parent);
        }

        selectionChanged();
        return buttonBox;
    }

    /**
     * Returns this field editor's list control.
     * 
     * @param parent the parent control
     * @return the list control
     */
    protected TableViewer getTableControl(Composite parent) {
        if (viewer == null) {
            list = new ArrayList<String>();
            Table table = createTable(parent);
            viewer = new TableViewer(table);
            viewer.setContentProvider(createContentProvider());
            viewer.setLabelProvider(createLabelProvider());
            table.setFont(parent.getFont());
            viewer.addSelectionChangedListener(new ISelectionChangedListener() {

                public void selectionChanged(SelectionChangedEvent event) {
                    TableEditor.this.selectionChanged();
                }
            });
            viewer.addDoubleClickListener(new IDoubleClickListener() {

                public void doubleClick(DoubleClickEvent event) {
                    editItem(event.getSelection());
                }
            });
        }
        return viewer;
    }

    protected void editItem(ISelection sel) {
        IStructuredSelection selection = (IStructuredSelection) sel;
        String existing = (String) selection.getFirstElement();
        // String value = getExistingInputObject(existing.replace(" ", ""));
        String value = getExistingInputObject(existing);
        if (value != null) {
            int indexOf = list.indexOf(existing);
            list.remove(existing);
            list.add(indexOf, value);
            viewer.refresh();
        }
    }

    protected abstract IStructuredContentProvider createContentProvider();

    protected abstract ITableLabelProvider createLabelProvider();

    protected abstract Table createTable(Composite parent);

    /**
     * Creates and returns a new item for the list.
     * <p>
     * Subclasses must implement this method.
     * </p>
     * 
     * @return a new item
     */
    protected abstract String getNewInputObject();

    protected abstract String getExistingInputObject(String obj);

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    public int getNumberOfControls() {
        return 2;
    }

    /**
     * Returns this field editor's selection listener. The listener is created if nessessary.
     * 
     * @return the selection listener
     */
    private SelectionListener getSelectionListener() {
        if (selectionListener == null) {
            createSelectionListener();
        }
        return selectionListener;
    }

    /**
     * Returns this field editor's shell.
     * <p>
     * This method is internal to the framework; subclassers should not call this method.
     * </p>
     * 
     * @return the shell
     */
    protected Shell getShell() {
        if (addButton == null) {
            return null;
        }
        return addButton.getShell();
    }

    /**
     * Combines the given list of items into a single string. This method is the converse of <code>parseString</code>.
     * <p>
     * Subclasses must implement this method.
     * </p>
     * 
     * @param items the list of items
     * @return the combined string
     * @see #readString
     */
    protected abstract String writeString(List<String> items);

    /**
     * Splits the given string into a list of strings. This method is the converse of <code>createList</code>.
     * <p>
     * Subclasses must implement this method.
     * </p>
     * 
     * @param stringList the string
     * @return an array of <code>String</code>
     * @see #writeString
     */
    protected abstract List<String> readString(String stringList);

    /**
     * Notifies that the Remove button has been pressed.
     */
    private void removePressed() {
        setPresentsDefaultValue(false);
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        if (selection.isEmpty()) {
            return;
        }
        for (Object obj : selection.toArray()) {
            list.remove(obj);
        }
        viewer.refresh();
        selectionChanged();
    }

    /**
     * Notifies that the list selection has changed.
     */
    protected void selectionChanged() {
        int index = viewer.getTable().getSelectionIndex();
        int size = viewer.getTable().getItemCount();
        setControlEnable(removeButton, index >= 0);
        setControlEnable(upButton, size > 1 && index > 0);
        setControlEnable(downButton, size > 1 && index >= 0 && index < size - 1);
    }

    /*
     * (non-Javadoc) Method declared on FieldEditor.
     */
    @Override
    public void setFocus() {
        if (viewer != null) {
            viewer.getTable().setFocus();
        }
    }

    /**
     * Moves the currently selected item up or down.
     * 
     * @param up <code>true</code> if the item should move up, and <code>false</code> if it should move down
     */
    private void swap(boolean up) {
        setPresentsDefaultValue(false);
        int index = viewer.getTable().getSelectionIndex();
        int target = up ? index - 1 : index + 1;

        if (index >= 0) {
            Collections.swap(list, index, target);
            // viewer.setSelection(target);
        }
        viewer.refresh();
        selectionChanged();
    }

    /**
     * Notifies that the Up button has been pressed.
     */
    private void upPressed() {
        swap(true);
    }

    /*
     * @see FieldEditor.setEnabled(boolean,Composite).
     */
    @Override
    public void setEnabled(boolean enabled, Composite parent) {
        super.setEnabled(enabled, parent);
        setControlEnable(getTableControl(parent).getTable(), enabled);
        setControlEnable(addButton, enabled);
        setControlEnable(removeButton, enabled);
        setControlEnable(upButton, enabled);
        setControlEnable(downButton, enabled);
    }

    protected void setControlEnable(Control control, boolean enable) {
        if (control != null && !control.isDisposed()) {
            control.setEnabled(enable);
        }
    }

    /**
     * Getter for list.
     * 
     * @return the list
     */
    public List<String> getList() {
        return this.list;
    }

}
