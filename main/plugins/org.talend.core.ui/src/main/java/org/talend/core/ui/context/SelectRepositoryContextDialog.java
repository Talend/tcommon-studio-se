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
package org.talend.core.ui.context;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.SelectionDialog;
import org.talend.commons.ui.runtime.exception.MessageBoxExceptionHandler;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.commons.ui.swt.dialogs.ProgressDialog;
import org.talend.commons.ui.swt.formtools.Form;
import org.talend.core.model.process.IContext;
import org.talend.core.model.process.IContextManager;
import org.talend.core.model.process.IContextParameter;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Project;
import org.talend.core.ui.context.cmd.AddRepositoryContextGroupCommand;
import org.talend.core.ui.i18n.Messages;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.repository.ProjectManager;

/**
 * ggu class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40 +0000 (ææäº, 29 ä¹æ 2006) nrousseau $
 * 
 */
public class SelectRepositoryContextDialog extends SelectionDialog {

    private static final String DEFAULTMESAGE = Messages.getString("SelectRepositoryContextDialog.Label"); //$NON-NLS-1$

    private static final String TITILE = Messages.getString("SelectRepositoryContextDialog.Title"); //$NON-NLS-1$

    private static final String WARNING_TITLE = Messages.getString("SelectRepositoryContextDialog.DuplicationTitle"); //$NON-NLS-1$

    private IContextModelManager modelManager = null;

    private IContextManager manager;

    private List<ContextItem> contextItemList = new ArrayList<ContextItem>();

    private ContextManagerHelper helper;

    private CheckboxTreeViewer treeViewer;

    private Button bViewContent;

    private Button bSelectAll;

    private Button bDeselectAll;

    private String selectedContextName;

    protected SelectRepositoryContextDialog(IContextModelManager modelManager, Shell parentShell, ContextManagerHelper helper) {
        super(parentShell);
        setBlockOnOpen(true);
        // setDefaultImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
        setTitle(TITILE);
        setMessage(DEFAULTMESAGE);
        setHelpAvailable(false);
        setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
        this.modelManager = modelManager;
        this.manager = modelManager.getContextManager();
        this.helper = helper;
        helper.initHelper(manager);
        contextItemList.addAll(helper.getContextItems());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.dialogs.SelectionDialog#configureShell(org.eclipse.swt.widgets.Shell)
     */

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setImage(ImageProvider.getImage(ECoreImage.CONTEXT_ICON));
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setFont(parent.getFont());
        createMessageArea(composite);
        Group group = Form.createGroup(composite, 1, null, 200);
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
        treeViewer.setSorter(new ContextViewerSorter());
        treeViewer.setInput(contextItemList);
        treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
        addTreeListener();
        updateTreeCheckedState();

        TreeItem[] items = treeViewer.getTree().getItems();
        for (TreeItem item : items) {
            if (item.getChecked()) {
                item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                TreeItem[] subItems = item.getItems();
                for (TreeItem subitem : subItems) {
                    subitem.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
            }
        }
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

        bViewContent = new Button(buttons, SWT.PUSH);
        bViewContent.setText(Messages.getString("SelectRepositoryContextDialog.View")); //$NON-NLS-1$
        bViewContent.setFont(parent.getFont());
        setButtonLayoutData(bViewContent);
        bViewContent.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeSelection selection = (TreeSelection) treeViewer.getSelection();
                if (selection.isEmpty()) {
                    return;
                }
                if (selection.getFirstElement() instanceof ContextItem) {
                    ShowSelectedContextDialog showDialog = new ShowSelectedContextDialog((ContextItem) selection
                            .getFirstElement(), getParentShell());
                    showDialog.open();
                }
            }
        });

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
        treeViewer.setAllChecked(all);
        if (contextItemList != null) {
            for (ContextItem item : contextItemList) {
                updateSubCheckedState(item, all, false);
                updateParentCheckedState(item);
            }
        }
    }

    private void addTreeListener() {
        treeViewer.addCheckStateListener(new ICheckStateListener() {

            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                Object obj = event.getElement();
                if (obj instanceof ContextItem) {
                    updateSubCheckedState(obj, checkSubState(obj), false);
                } else {
                    updateSubCheckedState(obj, event.getChecked(), true);
                }
                // checked parent
                updateParentCheckedState(obj);
            }

        });
        treeViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                TreeSelection selection = (TreeSelection) event.getSelection();
                if (selection.getFirstElement() instanceof ContextItem) {
                    bViewContent.setEnabled(true);
                } else {
                    bViewContent.setEnabled(false);
                }
            }

        });

    }

    private boolean checkSubState(Object obj) {
        if (obj == null || helper == null) {
            return false;
        }
        if (obj instanceof ContextItem) {
            ContextItem item = (ContextItem) obj;
            ContextType type = helper.getDefaultContextType(item);

            for (Object paramObj : type.getContextParameter()) {
                if (!treeViewer.getChecked(paramObj) && hasSelectedParam((ContextParameterType) paramObj) == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateSubCheckedState(Object obj, boolean checked, boolean showWarning) {
        if (obj == null || helper == null) {
            return;
        }
        if (obj instanceof ContextItem) {
            ContextItem item = (ContextItem) obj;
            ContextType type = helper.getDefaultContextType(item);
            if (type != null) {
                for (Object paramObj : type.getContextParameter()) {
                    updateSubCheckedState(paramObj, checked, false);
                }
            }
        } else if (obj instanceof ContextParameterType) {
            ContextParameterType param = (ContextParameterType) obj;
            treeViewer.setGrayed(param, false);
            if (helper.existParameterForJob(param)) {
                treeViewer.setChecked(param, true); // checked always
            } else {
                ContextParameterType existedParam = hasSelectedParam(param);
                if (existedParam != null && selectedContextName != null) {
                    treeViewer.setChecked(param, false);
                    if (showWarning) {
                        String message = null;
                        if (helper.existParameterForJob(existedParam)) {
                            message = Messages
                                    .getString(
                                            "SelectRepositoryContextDialog.ExistenceMessage", existedParam.getName(), selectedContextName); //$NON-NLS-1$
                        } else {
                            message = Messages
                                    .getString(
                                            "SelectRepositoryContextDialog.DuplicationMessage", existedParam.getName(), selectedContextName); //$NON-NLS-1$
                        }
                        MessageDialog.openWarning(getParentShell(), WARNING_TITLE, message);
                    }
                    selectedContextName = null;
                } else {
                    treeViewer.setChecked(obj, checked);
                }
            }
        }

    }

    private void updateParentCheckedState(Object obj) {
        if (obj == null) {
            return;
        }
        Object parent = null;

        Set siblings = null;
        if (obj instanceof ContextItem) {
            parent = obj; // self
            siblings = new HashSet(helper.getContextParameterType((ContextItem) obj)); // child
        } else if (obj instanceof ContextParameterType) {
            parent = helper.getParentContextItem(obj, contextItemList);
            siblings = helper.getSiblingContextObject(obj, contextItemList);
        }
        if (parent == null || siblings == null) {
            return;
        }
        int num = 0;

        for (Object sibling : siblings) {
            if (sibling instanceof ContextParameterType) {
                IContextParameter existedContextParameter = helper.getExistedContextParameter(((ContextParameterType) sibling)
                        .getName());
                if (existedContextParameter != null && existedContextParameter.isBuiltIn()) {
                    num++;
                    continue;
                }
            }
            if (treeViewer.getChecked(sibling)) {
                num++;
            }

        }
        if (num == 0) {
            treeViewer.setGrayChecked(parent, false);
        } else if (num == siblings.size()) {
            treeViewer.setChecked(parent, true);
            treeViewer.setGrayed(parent, false);
        } else {
            treeViewer.setGrayChecked(parent, true);
        }

    }

    /**
     * 
     * ggu Comment method "updateTreeCheckedState".
     * 
     * init
     */
    private void updateTreeCheckedState() {
        for (ContextItem item : contextItemList) {
            ContextType type = getDefaultContextType(item);
            if (type != null) {
                List<?> paramList = type.getContextParameter();
                for (int i = 0; i < paramList.size(); i++) {
                    final ContextParameterType paramType = (ContextParameterType) paramList.get(i);
                    if (helper.existParameterForJob(paramType, contextItemList)) {
                        treeViewer.setChecked(paramType, true);
                    }
                    // only check parent state once at then end of the loop to avoid Cartesian product.
                    if (i == paramList.size() - 1) {
                        updateParentCheckedState(paramType);
                    }
                }
            }
        }
    }

    private ContextParameterType hasSelectedParam(ContextParameterType paramType) {
        if (helper == null) {
            return null;
        }
        ContextItem item = (ContextItem) helper.getParentContextItem(paramType);
        if (item == null) {
            return null;
        }
        for (ContextItem contextItem : helper.getContextItems()) {
            if (item != contextItem) {
                ContextType type = helper.getDefaultContextType(contextItem);
                for (ContextParameterType param : (List<ContextParameterType>) type.getContextParameter()) {
                    if (param.getName().equals(paramType.getName()) && treeViewer.getChecked(param)) {
                        selectedContextName = contextItem.getProperty().getLabel();
                        return param;
                    }
                }
            }
        }
        return null;
    }

    private List<ContextItem> getSelectedContextItem() {
        List<ContextItem> itemList = new ArrayList<ContextItem>();
        List objList = Arrays.asList(treeViewer.getCheckedElements());
        for (Object obj : objList) {
            if (obj instanceof ContextItem) {
                itemList.add((ContextItem) obj);
            }
        }
        return itemList;
    }

    private List<ContextParameterType> getSelectedContextParameterType() {
        List<ContextParameterType> itemList = new ArrayList<ContextParameterType>();
        List objList = Arrays.asList(treeViewer.getCheckedElements());
        for (Object obj : objList) {
            if (obj instanceof ContextParameterType) {
                itemList.add((ContextParameterType) obj);
            }
        }
        return itemList;
    }

    private void progressDialog(final List<ContextItem> selectedItems, final Set<String> contextGoupNameSet,
            final List<ContextParameterType> paramTypeList) {

        ProgressDialog progressDialog = new ProgressDialog(getParentShell()) {

            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

                List objList = Arrays.asList(treeViewer.getCheckedElements());
                monitor.beginTask("", contextGoupNameSet.size() + paramTypeList.size() + 2); //$NON-NLS-1$
                setResult(objList);
                AddRepositoryContextGroupCommand addGroupCmd = new AddRepositoryContextGroupCommand(monitor, modelManager,
                        selectedItems, contextGoupNameSet, helper, paramTypeList);
                // AddRepositoryContextVariablesCommand addVarCmd = new AddRepositoryContextVariablesCommand(monitor,
                // modelManager,
                // helper, paramTypeList);

                if (modelManager.getCommandStack() != null) {

                    modelManager.getCommandStack().execute(addGroupCmd);

                    // modelManager.getCommandStack().execute(addVarCmd);

                } else {
                    addGroupCmd.execute();
                    // addVarCmd.execute();
                }

                monitor.done();

            }
        };

        try {
            progressDialog.executeProcess();
        } catch (InvocationTargetException e) {
            MessageBoxExceptionHandler.process(e.getTargetException(), getParentShell());
        } catch (InterruptedException e) {
            // Nothing to do
        }
    }

    @Override
    protected void okPressed() {
        List<ContextItem> selectedItems = getSelectedContextItem();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            Set<String> contextGoupNameSet = new HashSet<String>();
            if (checkShowContextGroup(manager, selectedItems)) {
                SelectRepositoryContextGroupDialog groupDialog = new SelectRepositoryContextGroupDialog(getParentShell(),
                        manager, helper, selectedItems);
                if (Dialog.OK == groupDialog.open()) {
                    contextGoupNameSet = groupDialog.getSelectedContextGroupName();
                }
            }
            progressDialog(selectedItems, contextGoupNameSet, getSelectedContextParameterType());

            modelManager.refresh();
        }

        super.okPressed();

    }

    private ContextType getDefaultContextType(ContextItem item) {
        if (item == null) {
            return null;
        }
        for (Object obj : item.getContext()) {
            ContextType type = (ContextType) obj;
            if (type.getName().equals(item.getDefaultContext())) {
                return type;
            }
        }
        return null;
    }

    /**
     * 
     * ggu SelectRepositoryContextDialog class global comment. Detailled comment
     */
    class ContextTreeContentProvider implements ITreeContentProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof ContextItem) {
                ContextItem item = (ContextItem) parentElement;
                ContextType type = getDefaultContextType(item);
                if (type != null) {
                    return type.getContextParameter().toArray();
                }
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
     * ggu SelectRepositoryContextDialog class global comment. Detailled comment
     */
    class ContextTreeLabelProvider implements ILabelProvider {

        @Override
        public Image getImage(Object element) {
            if (element instanceof ContextItem) {
                ContextItem item = (ContextItem) element;

                ProjectManager projectManager = ProjectManager.getInstance();
                Project project = projectManager.getProject(item);

                if (projectManager.getCurrentProject().getEmfProject().equals(project)) {
                    // current project items
                    return ImageProvider.getImage(ECoreImage.CONTEXT_ICON);
                } else {
                    // context items in referenced project.
                    return ImageProvider.getImage(ECoreImage.REFERENCED_ICON);
                }
            }
            return null;
        }

        @Override
        public String getText(Object element) {
            if (element instanceof ContextItem) {
                ContextItem item = (ContextItem) element;

                ProjectManager projectManager = ProjectManager.getInstance();
                Project project = projectManager.getProject(item);
                String suffix = ""; //$NON-NLS-1$
                if (!projectManager.getCurrentProject().getEmfProject().equals(project)) {
                    suffix = " (" + project.getLabel() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                return "Context: " + item.getProperty().getLabel() + suffix; //$NON-NLS-1$
            }
            if (element instanceof ContextParameterType) {
                ContextParameterType paramType = (ContextParameterType) element;
                return "Var: " + paramType.getName(); //$NON-NLS-1$
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
     * ggu SelectRepositoryContextDialog class global comment. Detailled comment
     */
    class ContextViewerFilter extends ViewerFilter {

        @Override
        public boolean select(Viewer viewer, Object parentElement, Object element) {
            if (element instanceof ContextItem) {
                boolean show = false;
                ContextItem item = (ContextItem) element;
                final EList contexts = item.getContext();
                for (Iterator it = contexts.iterator(); it.hasNext();) {
                    final Object object = it.next();
                    if (object instanceof ContextType) {
                        final EList parameters = ((ContextType) object).getContextParameter();
                        for (Iterator para = parameters.iterator(); para.hasNext();) {
                            final Object obj = para.next();
                            if (obj instanceof ContextParameterType) {
                                show = select(viewer, element, obj);
                                // if no contextparameter show, contextItem shoud be hidden.
                                if (show) {
                                    break;
                                }
                            }
                        }
                    }
                }
                return show;
            }
            if (element instanceof ContextParameterType) {
                ContextParameterType paramType = (ContextParameterType) element;
                IContextParameter param = helper.getExistedContextParameter(paramType.getName());
                if (param != null && param.isBuiltIn()) {
                    return false;
                }
            }
            return true;
        }

    }

    /**
     * 
     * ggu ContextViewerSorter class global comment. Detailled comment
     */
    class ContextViewerSorter extends ViewerSorter {

        @SuppressWarnings("unchecked")
        @Override
        public int compare(Viewer viewer, Object e1, Object e2) {
            if (e1 instanceof ContextItem && e2 instanceof ContextItem) {
                ProjectManager projectManager = ProjectManager.getInstance();
                ContextItem item1 = (ContextItem) e1;
                ContextItem item2 = (ContextItem) e2;

                Project project1 = projectManager.getProject(item1);
                Project project2 = projectManager.getProject(item2);
                Project curProject = projectManager.getCurrentProject().getEmfProject();

                // in current project
                if (project1.equals(curProject) && project2.equals(curProject)) {
                    return getComparator().compare(item1.getProperty().getLabel(), item2.getProperty().getLabel());
                } else if (!project1.equals(curProject) && !project2.equals(curProject)) { // in referenced project.
                    int compare = getComparator().compare(project1.getLabel(), project2.getLabel());
                    if (compare == 0) {
                        compare = getComparator().compare(item1.getProperty().getLabel(), item2.getProperty().getLabel());
                    }
                    return compare;
                } else if (project1.equals(curProject)) { // item1 is in current project
                    return -1;
                } else if (project2.equals(curProject)) { // item2 is in current project
                    return 1;
                }
            }
            if (e1 instanceof ContextParameterType && e2 instanceof ContextParameterType) {
                return getComparator().compare(((ContextParameterType) e1).getName(), ((ContextParameterType) e2).getName());
            }
            return super.compare(viewer, e1, e2);
        }

    }

    private boolean checkShowContextGroup(IContextManager manager, List<ContextItem> selectedItems) {
        if (selectedItems == null || selectedItems.isEmpty()) {
            return false;
        }
        Set<String> groupSet = new HashSet<String>();
        for (ContextItem item : selectedItems) {
            for (ContextType type : (List<ContextType>) item.getContext()) {
                groupSet.add(type.getName());
            }
        }
        Set<String> curGroupSet = new HashSet<String>();
        for (IContext context : manager.getListContext()) {
            curGroupSet.add(context.getName());
        }
        if (curGroupSet.containsAll(groupSet)) {
            return false;
        }
        return true;
    }
}
