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
package org.talend.core.runtime.repository.selection;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.talend.core.model.general.Project;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.IRepositoryViewObject;
import org.talend.core.runtime.repository.IRepositorySelectionDialog;
import org.talend.repository.model.RepositoryNode;

/**
 * DOC ggu class global comment. Detailled comment
 */
public abstract class AbstractRepositorySelectionDialog extends Dialog implements IRepositorySelectionDialog {

    private Project project;

    private List<IRepositoryViewObject> repObjList;

    private ERepositoryObjectType repositoryType;

    private IRepositoryViewObject selectedObj;

    private TreeViewer treeViewer;

    protected RepositorySelectionViewerTextFilter textFilter = new RepositorySelectionViewerTextFilter();

    protected RepositoryTypeSelectionViewerFilter repoTypeViewerFilter;

    public AbstractRepositorySelectionDialog(Shell parent) {
        super(parent);
    }

    public AbstractRepositorySelectionDialog(Shell parent, List<IRepositoryViewObject> repObjList) {
        super(parent);
        this.repObjList = repObjList;
    }

    public AbstractRepositorySelectionDialog(Shell parent, ERepositoryObjectType repositoryType) {
        super(parent);
        this.repositoryType = repositoryType;
        repoTypeViewerFilter = new RepositoryTypeSelectionViewerFilter(repositoryType);
    }

    protected Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public IRepositoryViewObject getSelectedObj() {
        return this.selectedObj;
    }

    protected void setSelectedObj(IRepositoryViewObject selectedObj) {
        this.selectedObj = selectedObj;
    }

    protected List<IRepositoryViewObject> getRepObjList() {
        return repObjList;
    }

    protected ERepositoryObjectType getRepositoryType() {
        return repositoryType;
    }

    protected TreeViewer getTreeViewer() {
        return treeViewer;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.getShell().setText("Select a Job/Joblet");
        Composite createDialogArea = (Composite) super.createDialogArea(parent);
        GridLayout sashGrid = new GridLayout();
        createDialogArea.setLayout(sashGrid);
        Composite frameGroup = new Composite(createDialogArea, SWT.NONE);

        GridData gridData = new GridData(GridData.FILL_BOTH);
        frameGroup.setLayoutData(gridData);

        sashGrid = new GridLayout();
        sashGrid.horizontalSpacing = 0;
        sashGrid.verticalSpacing = 0;
        frameGroup.setLayout(sashGrid);
        frameGroup.setSize(400, 250);
        frameGroup.pack();
        Label label = new Label(frameGroup, SWT.NONE);
        label.setText("Job Name Filter:");
        gridData = new GridData(GridData.FILL_BOTH);
        label.setLayoutData(gridData);

        treeViewer = createTreeViewer(frameGroup);
        addFilters();
        Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

        gridData = new GridData(GridData.FILL_BOTH);
        gridData.widthHint = 300;
        gridData.heightHint = 200;
        tree.setLayoutData(gridData);
        return frameGroup;
    }

    protected abstract TreeViewer createTreeViewer(Composite parent);

    protected void addFilters() {
        if (repoTypeViewerFilter != null) {
            treeViewer.addFilter(repoTypeViewerFilter);
        }
    }

    @Override
    protected void okPressed() {
        TreeSelection selection = (TreeSelection) getTreeViewer().getSelection();
        if (selection.getFirstElement() instanceof RepositoryNode) {
            setSelectedObj(((RepositoryNode) selection.getFirstElement()).getObject());
        }
        super.okPressed();
    }
}
