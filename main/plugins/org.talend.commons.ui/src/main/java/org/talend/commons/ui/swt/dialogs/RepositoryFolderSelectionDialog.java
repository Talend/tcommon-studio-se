// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2018 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package org.talend.commons.ui.swt.dialogs;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * yzhang class global comment. Detailled comment <br/>
 * 
 * $Id: talend.epf 1 2006-09-29 17:06:40Z nrousseau $
 * 
 */
public class RepositoryFolderSelectionDialog extends SelectionDialog {

    private TreeViewer treeViewer;

    private ITreeContentProvider contentProvider;

    private ILabelProvider labelProvider;

    private ITreeViewerListener treeListener;

    /**
     * Sets the treeListener.
     * 
     * @param treeListener the treeListener to set
     */
    public void setTreeListener(ITreeViewerListener treeListener) {
        this.treeListener = treeListener;
    }

    private Object input;

    private int widthInChars = 55;

    private int heightInChars = 15;

    /**
     * yzhang RepositoryFolderSelectionDialog constructor comment.
     * 
     * @param parentShell
     */
    public RepositoryFolderSelectionDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * Sets the input.
     * 
     * @param input the input to set
     */
    public void setInput(Object input) {
        this.input = input;
    }

    /**
     * Sets the contentProvider.
     * 
     * @param contentProvider the contentProvider to set
     */
    public void setContentProvider(ITreeContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Sets the labelProvider.
     * 
     * @param labelProvider the labelProvider to set
     */
    public void setLabelProvider(ILabelProvider labelProvider) {
        this.labelProvider = labelProvider;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createDialogArea(Composite container) {
        Composite parent = (Composite) super.createDialogArea(container);
        createMessageArea(parent);
        treeViewer = new TreeViewer(parent, SWT.BORDER);
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(labelProvider);
        treeViewer.setInput(input);
        treeViewer.setFilters(getFilter());
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {

            public void doubleClick(DoubleClickEvent event) {
                okPressed();
            }

        });

        treeViewer.addTreeListener(treeListener);

        GridData gd = new GridData(GridData.FILL_BOTH);
        treeViewer.getTree().setLayoutData(gd);
        gd.heightHint = convertHeightInCharsToPixels(heightInChars);
        gd.widthHint = convertWidthInCharsToPixels(widthInChars);
        return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer.getSelection();
        setResult(selection.toList());
        super.okPressed();
    }

    public boolean getExpandedState(Object element) {
        if (element == null) {
            return false;
        }
        return treeViewer.getExpandedState(element);
    }

    protected ViewerFilter[] getFilter() {
        return null;
    }
}
