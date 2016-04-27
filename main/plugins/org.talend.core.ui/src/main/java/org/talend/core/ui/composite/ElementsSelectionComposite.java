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
package org.talend.core.ui.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.talend.core.ui.advanced.composite.FilteredCheckboxTree;
import org.talend.core.ui.advanced.composite.PatternFilter;

/**
 * created by ycbai on 2015年10月8日 Detailled comment
 *
 */
public class ElementsSelectionComposite<T> extends Composite {

    private boolean multipleSelection = true;

    private CheckboxTreeViewer viewer;

    private List<T> viewerData;

    public ElementsSelectionComposite(Composite parent) {
        this(parent, true);
    }

    public ElementsSelectionComposite(Composite parent, boolean multipleSelection) {
        super(parent, SWT.NONE);
        this.multipleSelection = multipleSelection;
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        setLayout(gridLayout);
        createControl();
    }

    private void createControl() {
        int styles = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL;
        if (multipleSelection) {
            styles = styles | SWT.MULTI;
        }
        FilteredCheckboxTree filteredCheckboxTree = new FilteredCheckboxTree(this, styles, new PatternFilter());
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
        treeGridData.heightHint = 270;
    }

    public void setCheckedState() {
        List<String> selectedElementLabels = getSelectedElementLabels();
        if (selectedElementLabels != null) {
            List<T> selectedElements = getInitSelectedElements(selectedElementLabels);
            if (selectedElements != null) {
                viewer.setCheckedElements(selectedElements.toArray());
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

    public List<T> getSelectedElements() {
        List<T> nls = new ArrayList<>();
        Object[] checkedElements = viewer.getCheckedElements();
        for (Object obj : checkedElements) {
            T nl = (T) obj;
            nls.add(nl);
        }
        return nls;
    }

}
