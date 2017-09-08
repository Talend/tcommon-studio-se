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
package org.talend.core.repository.ui.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.talend.core.repository.i18n.Messages;
import org.talend.core.repository.model.ItemReferenceBean;

/**
 * DOC ycbai class global comment. Detailled comment
 */
public class ItemReferenceDialog extends Dialog {

    public static int OK_ID = IDialogConstants.OK_ID;

    public static int FORCE_DELETE_ID = 0xDE;

    public static int DO_NOT_DELETE_ID = 0xD0;

    private List<ItemReferenceBean> referenceList;

    private boolean showForceDeleteButton = false;

    public ItemReferenceDialog(Shell parentShell, List<ItemReferenceBean> referenceList) {
        super(parentShell);
        setShellStyle(getShellStyle() | SWT.MIN | SWT.APPLICATION_MODAL);
        this.referenceList = referenceList;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("ItemReferenceDialog.title")); //$NON-NLS-1$
        newShell.setSize(600, 400);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        if (isShowForceDeleteButton()) {
            createButton(parent, FORCE_DELETE_ID, Messages.getString("ItemReferenceDialog.forceDelete.button"), false); //$NON-NLS-1$
            createButton(parent, DO_NOT_DELETE_ID, Messages.getString("ItemReferenceDialog.doNotDelete.button"), true); //$NON-NLS-1$
        } else {
            createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.getString("ItemReferenceDialog.messages")); //$NON-NLS-1$

        TreeViewer viewer = new TreeViewer(composite, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);

        final Tree tree = viewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);
        tree.setLayoutData(new GridData(GridData.FILL_BOTH));

        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText(Messages.getString("ItemReferenceDialog.item")); //$NON-NLS-1$
        column1.setWidth(150);
        column1.setResizable(true);

        TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
        column2.setText(Messages.getString("ItemReferenceDialog.referenceItem")); //$NON-NLS-1$
        column2.setWidth(200);
        column2.setResizable(true);

        TreeColumn column3 = new TreeColumn(tree, SWT.LEFT);
        column3.setText(Messages.getString("ItemReferenceDialog.nodeTotals")); //$NON-NLS-1$
        column3.setWidth(50);
        column3.setResizable(true);
        column3.setToolTipText(Messages.getString("ItemReferenceDialog.nodeTotalsTip")); //$NON-NLS-1$

        TreeColumn column4 = new TreeColumn(tree, SWT.LEFT);
        column4.setText(Messages.getString("ItemReferenceDialog.project")); //$NON-NLS-1$
        column4.setWidth(150);
        column4.setResizable(true);

        ItemReferenceViewProvider provider = new ItemReferenceViewProvider();
        viewer.setContentProvider(provider);
        viewer.setLabelProvider(provider);
        viewer.setInput(referenceList);
        viewer.expandAll();

        return composite;
    }

    @Override
    protected void buttonPressed(int buttonId) {
        if (FORCE_DELETE_ID == buttonId) {
            MessageDialog confirmDialog = new MessageDialog(getShell(),
                    Messages.getString("ItemReferenceDialog.forceDelete.warn.title"), null, //$NON-NLS-1$
                    Messages.getString("ItemReferenceDialog.forceDelete.warn.message"), //$NON-NLS-1$
                    MessageDialog.WARNING, new String[] { IDialogConstants.YES_LABEL,
                            IDialogConstants.NO_LABEL },
                    1);
            if (confirmDialog.open() == 0) {
                forceDeletePressed();
            }
        } else if (DO_NOT_DELETE_ID == buttonId) {
            doNotDeletePressed();
        } else {
            super.buttonPressed(buttonId);
        }
    }

    @Override
    protected void initializeBounds() {
        super.initializeBounds();

        Point size = getShell().getSize();
        Point location = getInitialLocation(size);
        getShell().setBounds(getConstrainedShellBounds(new Rectangle(location.x, location.y, size.x, size.y)));
    }

    public void showForceDeleteButton(boolean forceDelete) {
        this.showForceDeleteButton = forceDelete;
    }

    public boolean isShowForceDeleteButton() {
        return this.showForceDeleteButton;
    }

    protected void forceDeletePressed() {
        setReturnCode(FORCE_DELETE_ID);
        close();
    }

    protected void doNotDeletePressed() {
        setReturnCode(DO_NOT_DELETE_ID);
        close();
    }
}
