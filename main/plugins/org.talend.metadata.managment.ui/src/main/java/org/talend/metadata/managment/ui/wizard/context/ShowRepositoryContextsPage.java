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
package org.talend.metadata.managment.ui.wizard.context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.talend.commons.ui.runtime.image.ECoreImage;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.context.JobContextManager;
import org.talend.core.model.properties.ContextItem;
import org.talend.core.model.properties.Project;
import org.talend.core.ui.context.ContextManagerHelper;
import org.talend.core.ui.context.model.table.ConectionAdaptContextVariableModel;
import org.talend.designer.core.model.utils.emf.talendfile.ContextParameterType;
import org.talend.designer.core.model.utils.emf.talendfile.ContextType;
import org.talend.metadata.managment.ui.i18n.Messages;
import org.talend.repository.ProjectManager;

public class ShowRepositoryContextsPage extends WizardPage {

    private Set<ContextItem> contextItemList = new HashSet<ContextItem>();

    public int step;

    private ContextManagerHelper helper;

    private TreeViewer treeViewer;

    private ContextItem selectItem;

    private List<ConectionAdaptContextVariableModel> adaptModels;

    public ShowRepositoryContextsPage(Set<ContextItem> contextItems, int step) {
        super(Messages.getString("ReuseRepositoryContext.name")); //$NON-NLS-1$
        setTitle(Messages.getString("ShowRepositoryContextPage.title")); //$NON-NLS-1$
        this.contextItemList = contextItems;
        this.step = step;
    }

    @Override
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setBackground(parent.getBackground());
        composite.setFont(parent.getFont());

        GridLayout layout = new GridLayout();
        layout.verticalSpacing = 10;
        composite.setLayout(layout);
        GridData gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 400;
        composite.setLayoutData(gridData);
        GridData gd = new GridData();

        SashForm sashForm = new SashForm(composite, SWT.HORIZONTAL);
        gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 400;
        sashForm.setLayoutData(gd);

        createDescriptionIn(sashForm);
        createTreeViewer(sashForm);
        sashForm.setWeights(new int[] { 3, 5 });
        setControl(composite);
        updatePageComplete();
        addTreeListener();
    }

    private void updatePageComplete() {
        evaluateSelectField();
    }

    private void evaluateSelectField() {
        if (treeViewer.getSelection() instanceof TreeSelection) {
            TreeSelection currentSel = (TreeSelection) treeViewer.getSelection();
            if (currentSel.getFirstElement() != null) {
                setPageComplete(true);
                setErrorMessage(null);
            } else {
                setPageComplete(false);
                setErrorMessage(Messages.getString("ShowRepositoryContextPage.warnMsg")); //$NON-NLS-1$
            }
        }
    }

    private void createDescriptionIn(Composite sashForm) {
        Text descriptionText = new Text(sashForm, SWT.BORDER | SWT.WRAP);
        descriptionText.setText(Messages.getString("ShowRepositoryContextPage.SelectReuseContext")); //$NON-NLS-1$
        descriptionText.setEditable(false);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.widthHint = 100;
        descriptionText.setLayoutData(gd);
    }

    private void createTreeViewer(Composite parent) {
        Group treeViewrGroup = new Group(parent, SWT.NONE);
        treeViewrGroup.setText(Messages.getString("ShowRepositoryContextPage.aviableContexts")); //$NON-NLS-1$
        GridLayout layoutGroup = new GridLayout();
        layoutGroup.numColumns = 1;
        treeViewrGroup.setLayout(layoutGroup);

        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        treeViewrGroup.setLayoutData(gridData);

        treeViewer = new TreeViewer(treeViewrGroup, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        Tree tree = treeViewer.getTree();
        tree.setLinesVisible(true);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 200;
        tree.setLayoutData(gridData);
        treeViewer.setContentProvider(new ContextTreeContentProvider());
        treeViewer.setLabelProvider(new ContextTreeLabelProvider());
        treeViewer.setFilters(new ViewerFilter[] { new ContextViewerFilter() });
        treeViewer.setSorter(new ContextViewerSorter());
        treeViewer.setInput(contextItemList);
    }

    private void addTreeListener() {
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                TreeSelection selection = (TreeSelection) event.getSelection();
                if (selection.getFirstElement() instanceof ContextItem) {

                    ContextItem selectedItem = (ContextItem) selection.getFirstElement();
                    ContextModeWizard currentWizard = null;
                    if (getWizard() instanceof ContextModeWizard) {
                        currentWizard = (ContextModeWizard) getWizard();
                        // currentWizard.clearModels();
                    }
                    if (selectedItem != null) {
                        // reassign manager here since we select an exist context item
                        currentWizard.setReuseContextManager(new JobContextManager(selectedItem.getContext(), selectedItem
                                .getDefaultContext()));
                    }
                    updatePageComplete();
                }
            }
        });
    }

    public ContextItem getSelectedContextItem() {
        TreeSelection obj = (TreeSelection) treeViewer.getSelection();
        if (obj.getFirstElement() instanceof ContextItem) {
            selectItem = (ContextItem) obj.getFirstElement();
        }
        return selectItem;
    }

    class ContextTreeContentProvider implements ITreeContentProvider {

        @Override
        public Object[] getChildren(Object parentElement) {
            return null;
        }

        @Override
        public Object getParent(Object element) {

            return helper.getParentContextItem(element);
        }

        @Override
        public boolean hasChildren(Object element) {
            return false;
        }

        @Override
        public Object[] getElements(Object inputElement) {
            return ((Set<ContextItem>) inputElement).toArray();
        }

        @Override
        public void dispose() {

        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

        }

    }

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
                return item.getProperty().getLabel();
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
            return true;
        }

    }

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
            return super.compare(viewer, e1, e2);
        }

    }

    public List<ConectionAdaptContextVariableModel> getAdaptModels() {
        return adaptModels;
    }

    public void setAdaptModels(List<ConectionAdaptContextVariableModel> adaptModels) {
        this.adaptModels = adaptModels;
    }

}
