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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.ui.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;

/**
 * DOC ggu class global comment. Detailled comment
 */
public class SelectRepositoryContextGroupDialog extends SelectionDialog {

    private static final String DEFAULTMESAGE = Messages.getString("SelectRepositoryContextGroupDialog.Messages"); //$NON-NLS-1$

    private static final String TITILE = Messages.getString("SelectRepositoryContextGroupDialog.Title"); //$NON-NLS-1$

    private static final String LEFTBRACKET = " ("; //$NON-NLS-1$

    private static final String RIGHTBRACKET = ")"; //$NON-NLS-1$

    private static final String DEFAULTFLAG = LEFTBRACKET
            + Messages.getString("SelectRepositoryContextGroupDialog.Default") + RIGHTBRACKET; //$NON-NLS-1$ 

    private CheckboxTreeViewer treeViewer;

    private Button bSelectAll;

    private Button bDeselectAll;

    private List<ContextItem> contextItemList = new ArrayList<ContextItem>();

    private List<ContextItem> selectedContextItems = new ArrayList<ContextItem>();

    private IContextManager manager;

    private ContextManagerHelper helper;

    private Set<String> nameSet = new HashSet<String>();

    public SelectRepositoryContextGroupDialog(Shell parentShell, IContextManager manager, ContextManagerHelper helper,
            List<ContextItem> selectedContextItems) {
        super(parentShell);
        setBlockOnOpen(true);
        // setDefaultImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
        setTitle(TITILE);
        setHelpAvailable(false);
        setMessage(DEFAULTMESAGE);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        this.manager = manager;
        this.helper = helper;
        this.selectedContextItems = selectedContextItems;
        if (helper != null) {
            contextItemList.addAll(helper.getContextItems());
        }
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
        shell.setMinimumSize(300, 300);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setFont(parent.getFont());
        createMessageArea(composite);
        Group group = Form.createGroup(composite, 1, null, 150);
        Composite inner = new Composite(group, SWT.NONE);
        inner.setFont(composite.getFont());
        inner.setLayoutData(new GridData(GridData.FILL_BOTH));
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.horizontalSpacing = 10;
        inner.setLayout(layout);
        createTreeViewer(inner);
        createButtons(inner);

        return composite;
    }

    private void createTreeViewer(Composite parent) {
        treeViewer = new CheckboxTreeViewer(parent);
        treeViewer.setContentProvider(new ContextTreeContentProvider());
        treeViewer.setLabelProvider(new ContextTreeLabelProvider());
        treeViewer.setFilters(new ViewerFilter[] { new ContextViewerFilter() });
        treeViewer.setInput(contextItemList);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.widthHint = 100;
        treeViewer.getTree().setLayoutData(data);
        // listener
        treeViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                Object obj = event.getElement();
                treeViewer.setGrayed(obj, false);
                treeViewer.setSubtreeChecked(obj, event.getChecked());
                updateParentCheckedState(obj);
                updateSameNameContextType(obj);
            }
        });

    }

    private void createButtons(Composite parent) {

        Composite buttons = new Composite(parent, SWT.NONE);
        buttons.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_VERTICAL);
        buttons.setLayoutData(data);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 10;
        layout.marginWidth = 0;
        layout.marginRight = 0;

        buttons.setLayout(layout);

        bSelectAll = new Button(buttons, SWT.PUSH);
        bSelectAll.setText(Messages.getString("SelectRepositoryContextDialog.SelectAll")); //$NON-NLS-1$
        bSelectAll.setFont(parent.getFont());
        setButtonLayoutData(bSelectAll);
        bSelectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                selectAll(true);
            }
        });

        bDeselectAll = new Button(buttons, SWT.PUSH);
        bDeselectAll.setText(Messages.getString("SelectRepositoryContextDialog.DeselectAll")); //$NON-NLS-1$
        bDeselectAll.setFont(parent.getFont());
        setButtonLayoutData(bDeselectAll);
        bDeselectAll.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                selectAll(false);
            }
        });
        if (contextItemList != null && contextItemList.size() > 1) {
            new Label(buttons, SWT.NONE).setVisible(false);

            Button bExpand = new Button(buttons, SWT.PUSH);
            bExpand.setText(Messages.getString("SelectRepositoryContextDialog.ExpandAll")); //$NON-NLS-1$
            bExpand.setFont(parent.getFont());
            setButtonLayoutData(bExpand);
            bExpand.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    treeViewer.expandAll();
                }
            });

            Button bCollapse = new Button(buttons, SWT.PUSH);
            bCollapse.setText(Messages.getString("SelectRepositoryContextDialog.CollapseAll")); //$NON-NLS-1$
            bCollapse.setFont(parent.getFont());
            setButtonLayoutData(bCollapse);
            bCollapse.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    treeViewer.collapseAll();
                }
            });
        }
    }

    private void selectAll(boolean all) {
        // treeViewer.setAllChecked(all);
        if (contextItemList != null) {
            for (ContextItem item : contextItemList) {
                treeViewer.setSubtreeChecked(item, all);
            }
        }
    }

    private void updateParentCheckedState(Object obj) {
        if (obj == null && helper == null) {
            return;
        }
        Object parent = helper.getParentContextItem(obj);
        if (parent == null) {
            return;
        }

        Set siblings = helper.getSiblingContextObject(obj);
        if (siblings == null) {
            return;
        }
        int num = 0;
        int exitedNum = 0;
        for (Object sibling : siblings) {
            if (treeViewer.getChecked(sibling)) {
                num++;
            }
            if (sibling instanceof ContextType && isExistedContextGroup(((ContextType) sibling).getName())) {
                exitedNum++;
            }
        }

        if (num == 0) {
            treeViewer.setGrayChecked(parent, false);
        } else if (num + exitedNum == siblings.size()) {
            treeViewer.setChecked(parent, true);
            treeViewer.setGrayed(parent, false);
        } else {
            treeViewer.setGrayChecked(parent, true);
        }

    }

    private void updateSameNameContextType(Object obj) {
        if (obj == null && helper == null) {
            return;
        }
        boolean checked = treeViewer.getChecked(obj);
        /*
         * ContextItem
         */
        if (obj instanceof ContextItem) {
            ContextItem curItem = (ContextItem) obj;
            // record the ContextType name of current ContextItem.
            List<String> nameList = new ArrayList<String>();
            for (ContextType type : (List<ContextType>) curItem.getContext()) {
                nameList.add(type.getName());
            }
            //
            for (ContextItem contextItem : helper.getContextItems()) {
                // not update self yet.
                if (contextItem != curItem) {
                    boolean found = false;
                    ContextType tmpType = null;
                    for (ContextType type : (List<ContextType>) contextItem.getContext()) {
                        if (nameList.contains(type.getName())) {
                            tmpType = type; // record the last same name ContextType.
                            treeViewer.setChecked(type, checked);
                            found = true;
                        }
                    }
                    if (found && tmpType != null) {
                        // update parent (ContextItem state).
                        updateParentCheckedState(tmpType);
                    }
                }
            }
        }
        /*
         * ContextType
         */
        if (obj instanceof ContextType) {
            ContextType contextType = (ContextType) obj;
            Object parentObj = helper.getParentContextItem(contextType);
            if (parentObj != null && parentObj instanceof ContextItem) {
                ContextItem parentContextItem = (ContextItem) parentObj;
                for (ContextItem contextItem : helper.getContextItems()) {
                    // not update self yet.
                    if (contextItem != parentContextItem) {
                        for (ContextType type : (List<ContextType>) contextItem.getContext()) {
                            if (type.getName().equals(contextType.getName())) {
                                treeViewer.setChecked(type, checked);
                                // update parent (ContextItem state).
                                updateParentCheckedState(type);
                            }
                        }
                    }
                }
            }
        }
    }

    private void updateSelectedContextGroupName() {
        if (nameSet == null) {
            nameSet = new HashSet<String>();
        } else {
            nameSet.clear();
        }

        for (Object obj : treeViewer.getCheckedElements()) {
            if (obj instanceof ContextType) {
                nameSet.add(((ContextType) obj).getName());
            }
        }

    }

    public Set<String> getSelectedContextGroupName() {
        return nameSet;
    }

    @Override
    protected void okPressed() {
        updateSelectedContextGroupName();
        super.okPressed();
    }

    /**
     * 
     * DOC ggu ContextTreeContentProvider class global comment. Detailled comment
     */

    class ContextTreeContentProvider implements ITreeContentProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof ContextItem) {
                ContextItem item = (ContextItem) parentElement;
                return item.getContext().toArray();
            }
            return null;
        }

        @Override
        public Object getParent(Object element) {

            return helper.getParentContextItem(element);
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof ContextItem) {
                return true;
            }
            return false;
        }

        @Override
        public Object[] getElements(Object inputElement) {
            return ((List) inputElement).toArray();
        }

        @Override
        public void dispose() {

        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

    }

    /**
     * 
     * ggu ContextTreeLabelProvider class global comment. Detailled comment
     */
    class ContextTreeLabelProvider implements ILabelProvider {

        @Override
        public Image getImage(Object element) {
            // if (element instanceof ContextItem) {
            // return ImageProvider.getImageDesc(ECoreImage.CONTEXT_ICON).createImage();
            // }
            return null;
        }

        @Override
        public String getText(Object element) {
            if (element instanceof ContextItem) {
                ContextItem item = (ContextItem) element;
                return "Context: " + item.getProperty().getLabel(); //$NON-NLS-1$
            }

            if (element instanceof ContextType) {
                ContextType contextType = (ContextType) element;
                Object obj = helper.getParentContextItem(contextType);
                if (obj != null && obj instanceof ContextItem) {
                    ContextItem item = (ContextItem) obj;
                    if (item.getDefaultContext().equals(contextType.getName())) {

                        return contextType.getName() + DEFAULTFLAG;
                    }
                }
                return contextType.getName();
            }
            return null;
        }

        @Override
        public void addListener(ILabelProviderListener listener) {

        }

        @Override
        public void dispose() {

        }

        @Override
        public boolean isLabelProperty(Object element, String property) {

            return false;
        }

        @Override
        public void removeListener(ILabelProviderListener listener) {

        }

    }

    /**
     * 
     * ggu ContextViewerFilter class global comment. Detailled comment
     */
    class ContextViewerFilter extends ViewerFilter {

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (element instanceof ContextItem) {
                ContextItem contextItem = (ContextItem) element;
                if (selectedContextItems != null && !selectedContextItems.contains(contextItem)) {
                    return false;
                }
                //
                List<String> nameList = new ArrayList<String>();
                for (ContextType contextType : (List<ContextType>) contextItem.getContext()) {
                    nameList.add(contextType.getName());
                }
                if (isExistedContextGroupAll(nameList)) {
                    return false;
                }
                return true;
            }

            if (element instanceof ContextType) {
                ContextType contextType = (ContextType) element;
                if (isExistedContextGroupInJob(contextType.getName())) {
                    return false;
                }
            }
            return true;
        }

    }

    /**
     * Added by Marvin Wang on Jun. 6, 2012 for judging if the context group from repository exists in job context
     * groups without case sensitive.
     * 
     * @param repContextGrpName is not <code>null<code>.
     * @return
     */
    private boolean isExistedContextGroupInJob(String repContextGrpName) {
        List<IContext> contexts = manager.getListContext();
        if (contexts != null && contexts.size() > 0) {
            for (IContext context : manager.getListContext()) {
                String contextName = context.getName();
                if (contextName != null && repContextGrpName != null
                        && contextName.toLowerCase().equals(repContextGrpName.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isExistedContextGroup(String name) {
        List<String> nameList = new ArrayList<String>();
        nameList.add(name);
        return isExistedContextGroupAll(nameList);
    }

    private boolean isExistedContextGroupAll(List<String> nameList) {
        List<String> jobNameList = new ArrayList<String>();
        for (IContext context : manager.getListContext()) {
            jobNameList.add(context.getName());
        }
        if (jobNameList.containsAll(nameList)) {
            return true;
        }
        return false;
    }
}
