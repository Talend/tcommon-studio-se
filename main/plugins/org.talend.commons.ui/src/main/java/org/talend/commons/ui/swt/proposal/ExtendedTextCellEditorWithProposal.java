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
package org.talend.commons.ui.swt.proposal;

import org.eclipse.jface.fieldassist.IContentProposalProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.talend.commons.ui.runtime.swt.proposal.IShowInvisibleCellEditorMethods;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.CellEditorDialogBehavior;
import org.talend.commons.ui.runtime.swt.tableviewer.celleditor.ExtendedTextCellEditor;
import org.talend.commons.ui.swt.tableviewer.TableViewerCreatorColumn;

/**
 * This class extends the ExtendedTextCellEditor to add the Expression Builder to tMapper. Only tMapper can use this
 * class. <br/>
 * 
 * $Id: TextCellEditorWithProposal.java 3351 2007-05-04 12:14:00Z plegall $
 * 
 */
public class ExtendedTextCellEditorWithProposal extends ExtendedTextCellEditor implements
        IShowInvisibleCellEditorMethods {

    private CommonTextCellEditorWithProposal commonTextEditor;

    public ExtendedTextCellEditorWithProposal(Composite parent, int style,
            TableViewerCreatorColumn tableViewerCreatorColumn, CellEditorDialogBehavior behavior) {
        super(parent, behavior);
        setStyle(style);
        commonTextEditor.init(tableViewerCreatorColumn);
    }

    public ExtendedTextCellEditorWithProposal(Composite parent, TableViewerCreatorColumn tableViewerCreatorColumn,
            CellEditorDialogBehavior behavior) {
        this(parent, SWT.NONE, tableViewerCreatorColumn, behavior);
    }

    @Override
    public void activate() {
        super.activate();
        commonTextEditor.activate();
        // System.out.println("previousActivatedIndex =" + previousActivatedIndex);
    }

    /**
     * Can't be overrided since it is final
     */
    // @Override
    // public void create(Composite parent) {
    // }
    @Override
    protected void differedCreate(Composite parent) {
        super.differedCreate(parent);
        commonTextEditor = new CommonTextCellEditorWithProposal(this, text);
        commonTextEditor.create(parent);
    }

    @Override
    protected void keyReleaseOccured(KeyEvent keyEvent) {
        commonTextEditor.keyReleaseOccured(keyEvent);
    }

    public IContentProposalProvider getProposalProvider() {
        return commonTextEditor.getProposalProvider();
    }

    public void setContentProposalProvider(IContentProposalProvider proposalProvider) {
        commonTextEditor.setContentProposalProvider(proposalProvider);
    }

    /**
     * Getter for contentProposalAdapter.
     * 
     * @return the contentProposalAdapter
     */
    public ContentProposalAdapterExtended getContentProposalAdapter() {
        return commonTextEditor.getContentProposalAdapter();
    }

    @Override
    public void fireApplyEditorValue() {
        super.fireApplyEditorValue();
    }

    @Override
    public void fireCancelEditor() {
        super.fireCancelEditor();
    }

}
