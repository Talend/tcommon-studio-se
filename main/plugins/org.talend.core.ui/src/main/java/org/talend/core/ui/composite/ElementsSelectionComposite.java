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
package org.talend.core.ui.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.ui.advanced.composite.FilteredCheckboxTree;
import org.talend.core.ui.advanced.composite.PatternFilter;
import org.talend.core.ui.i18n.Messages;

/**
 * created by ycbai on 2015年10月8日 Detailled comment
 *
 */
public class ElementsSelectionComposite<T> extends Composite {

    private boolean multipleSelection = true;

    private boolean showToolbar;

    private FilteredCheckboxTree filteredCheckboxTree;

    private CheckboxTreeViewer viewer;

    private List<T> viewerData;

    public ElementsSelectionComposite(Composite parent) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
    }

    public ElementsSelectionComposite<T> create() {
        createControl();
        return this;
    }

    public ElementsSelectionComposite<T> setMultipleSelection(boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
        return this;
    }

    public ElementsSelectionComposite<T> setShowToolbar(boolean showToolbar) {
        this.showToolbar = showToolbar;
        return this;
    }

    private void createControl() {
        Composite composite = new Composite(this, SWT.NONE);
        GridLayout compositeLayout = new GridLayout();
        compositeLayout.marginHeight = 0;
        compositeLayout.marginWidth = 0;
        composite.setLayout(compositeLayout);
        composite.setFont(this.getFont());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createTreeArea(composite);
        if (showToolbar) {
            createToolbarArea(composite);
        }
    }

    private void createTreeArea(Composite parent) {
        int styles = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
        if (multipleSelection) {
            styles = styles | SWT.MULTI;
        }
        filteredCheckboxTree = new FilteredCheckboxTree(parent, styles, new PatternFilter());
        viewer = filteredCheckboxTree.getViewer();
        viewer.setContentProvider(getContentProvider());
        viewer.setLabelProvider(getLabelProvider());
        viewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                doSelectionChanged();
            }
        });
        GridData treeGridData = (GridData) filteredCheckboxTree.getLayoutData();
        treeGridData.heightHint = 250;
    }

    private void createToolbarArea(Composite parent) {
        Composite toolbarComposite = new Composite(parent, SWT.NONE);
        GridLayout toolbarLayout = new GridLayout(2, false);
        toolbarLayout.marginHeight = 0;
        toolbarLayout.marginWidth = 0;
        toolbarComposite.setLayout(toolbarLayout);
        toolbarComposite.setFont(this.getFont());

        Button selectAllBtn = new Button(toolbarComposite, SWT.PUSH);
        selectAllBtn.setText(Messages.getString("SelectRepositoryContextDialog.SelectAll")); //$NON-NLS-1$
        selectAllBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewer.setAllChecked(true);
                doSelectionChanged();
            }
        });

        Button deselectAllBtn = new Button(toolbarComposite, SWT.PUSH);
        deselectAllBtn.setText(Messages.getString("SelectRepositoryContextDialog.DeselectAll")); //$NON-NLS-1$
        deselectAllBtn.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                viewer.setAllChecked(false);
                doSelectionChanged();
            }
        });

        toolbarComposite.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    public void setCheckedState() {
        List<String> selectedElementLabels = getSelectedElementLabels();
        if (selectedElementLabels != null) {
            List<T> selectedElements = getInitSelectedElements(selectedElementLabels);
            if (selectedElements != null) {
                viewer.setCheckedElements(selectedElements.toArray());
                doSelectionChanged();
            }
        }
    }

    protected List<T> getInitSelectedElements(List<String> selectedElementLabels) {
        return new ArrayList<>();
    }

    protected List<String> getSelectedElementLabels() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateChildrenEnableStatus(this, enabled);
    }

    private void updateChildrenEnableStatus(Composite comp, boolean enabled) {
        Control[] children = comp.getChildren();
        for (Control control : children) {
            control.setEnabled(enabled);
            if (control instanceof Composite) {
                updateChildrenEnableStatus(((Composite) control), enabled);
            }
        }
    }

    protected void doSelectionChanged() {
        // Do nothing by default.
    }

    protected IContentProvider getContentProvider() {
        return new ITreeContentProvider() {

            @Override
            public Object[] getChildren(Object parentElement) {
                return null;
            }

            @Override
            public Object[] getElements(Object inputElement) {
                if (inputElement instanceof List) {
                    return ((List) inputElement).toArray();
                }
                return new Object[] { inputElement };
            }

            @Override
            public boolean hasChildren(Object element) {
                return false;
            }

            @Override
            public Object getParent(Object element) {
                return null;
            }

            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

        };
    }

    protected IBaseLabelProvider getLabelProvider() {
        return new LabelProvider() {

            @Override
            public String getText(Object obj) {
                return obj.toString();
            }

            @Override
            public Image getImage(Object obj) {
                return null;
            }
        };
    }

    public void setViewerData(List<T> nls) {
        this.viewerData = nls;
        viewer.setInput(nls);
    }

    public List<T> getViewerData() {
        return this.viewerData;
    }

    @SuppressWarnings("unchecked")
    public List<T> getSelectedElements() {
        Set<T> checkedElements = new HashSet<>();
        // If using filter
        for (Object obj : filteredCheckboxTree.getCheckedLeafNodes()) {
            checkedElements.add((T) obj);
        }
        // If does not use filter
        for (Object obj : viewer.getCheckedElements()) {
            checkedElements.add((T) obj);
        }

        return new ArrayList<>(checkedElements);
    }

}
