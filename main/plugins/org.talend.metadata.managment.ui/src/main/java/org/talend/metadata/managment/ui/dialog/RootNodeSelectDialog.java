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
package org.talend.metadata.managment.ui.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.talend.datatools.xml.utils.ATreeNode;
import org.talend.metadata.managment.ui.wizard.metadata.xml.FoxNodeComboViewProvider;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class RootNodeSelectDialog extends Dialog {

    private ComboViewer rootComboViewer;

    private Combo rootCombo;

    private List<ATreeNode> nodes;

    private ATreeNode selectedNode;

    public RootNodeSelectDialog(Shell parentShell, List<ATreeNode> nodes) {
        super(parentShell);
        this.nodes = nodes;
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        composite.setLayout(new GridLayout(2, false));

        final Label rootLabel = new Label(composite, SWT.NONE);
        rootLabel.setText("Root");

        rootComboViewer = new ComboViewer(composite, SWT.READ_ONLY);
        FoxNodeComboViewProvider comboProvider = new FoxNodeComboViewProvider();
        rootComboViewer.setLabelProvider(comboProvider);
        rootComboViewer.setContentProvider(comboProvider);
        rootCombo = rootComboViewer.getCombo();
        GridData comboData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        rootCombo.setLayoutData(comboData);

        init();

        return composite;
    }

    private void init() {
        if (nodes != null && nodes.size() > 0) {
            rootComboViewer.setInput(nodes);
            rootCombo.select(0);
        }
    }

    protected void okPressed() {
        IStructuredSelection selection = ((IStructuredSelection) rootComboViewer.getSelection());
        Object obj = selection.getFirstElement();
        if (obj != null) {
            selectedNode = (ATreeNode) obj;
        }

        super.okPressed();
    }

    public ATreeNode getSelectedNode() {
        return this.selectedNode;
    }

}
